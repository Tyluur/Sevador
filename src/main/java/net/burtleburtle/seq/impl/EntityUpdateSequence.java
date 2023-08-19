package net.burtleburtle.seq.impl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.burtleburtle.seq.Sequence;
import net.burtleburtle.thread.NodeWorker;

import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.BuildSceneGraph;
import com.sevador.network.out.NPCRendering;
import com.sevador.network.out.PlayerRendering;

/**
 * The Entity updating sequence.
 * 
 * @author Emperor
 * 
 */
public final class EntityUpdateSequence implements Sequence<Object> {

	/**
	 * The executor used.
	 */
	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	/**
	 * Constructs a new {@code EntityUpdateSequence} {@code Object}.
	 */
	public EntityUpdateSequence() {
		/*
		 * empty.
		 */
	}

	@Override
	public boolean preparation(Object t) {
		try {
			for (Player player : NodeWorker.getPlayers()) {
				if (player == null)
					continue;
				if (player.isLoggedIn()) {
					player.tick();
					player.getPoison().processPoison();
					player.getWalkingQueue().updateMovement();
					player.getActionManager().update();
					player.getUpdateMasks().prepare(player);
					player.getSkillAction().process();
				}
			}
			for (final NPC npc : NodeWorker.getNPCs()) {
				if (npc != null) {
					npc.tick();
					npc.getWalkingQueue().updateMovement();
					npc.getActionManager().update();
					npc.getUpdateMasks().prepare(npc);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean execution(Object t) {
		final CountDownLatch latch = new CountDownLatch(NodeWorker.getPlayers().size());
		for (final Player player : NodeWorker.getPlayers()) {
			if (player == null) {
				continue;
			}
			EXECUTOR.execute(new Runnable() {
				@Override
				public void run() {
					try {
						synchronized (player) {
							if (player.isLoggedIn()) {
								int musicId = player.getLocation().getMusicId();
								if (musicId != -1)
									player.getMusicsManager().checkMusic(player.getLocation().getMusicId());
								if (player.getPlayerFlags().isMapRegionChanged()) {
									player.getLocation().loadMusicIds();
									player.getIOSession().write(new BuildSceneGraph(player, false));
								}
								player.getIOSession().write(new PlayerRendering(player));
								player.getIOSession().write(new NPCRendering(player));
							}
						}
					} catch (Throwable t) {
						t.printStackTrace();
					}
					latch.countDown();
				}
			});
		}
		try {
			latch.await(600, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean finalization(Object t) {
		try {
			for (Player player : NodeWorker.getPlayers()) {
				if (player == null || player.getRenderInformation() == null)
					continue;
				if (player.isLoggedIn()) {
					player.getUpdateMasks().finish();
					player.getRenderInformation().updateInformation();
					// player.getPlayerFlags().update(player); WAS CHAT MESSAGE
					// BEFORE.
					player.getDamageMap().getDamageList().clear();
				} else {
					player.setLoggedIn(true);
				}
			}
			for (NPC npc : NodeWorker.getNPCs()) {
				if (npc != null) {
					npc.getUpdateMasks().finish();
					npc.getDamageMap().getDamageList().clear();
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return true;
	}

}
