package com.sevador.game.node.control.impl;

import com.sevador.game.minigames.Barrows.BarrowsBrother;
import com.sevador.game.node.control.Controler;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;
import com.sevador.utility.Misc;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class Barrows extends Controler {

	private BarrowsBrother brother;
	private Brothers brotherInform;
	private int barrowsFaces;
	private int resetFace = -1;
	
	@Override
	public void start() {
		this.sendInterfaces();
		this.giveHiddenBrother();
		setBarrowsFaces(10 + Misc.random(10));
	}

	private void giveHiddenBrother() {
		if (player.getHiddenBrother() <= 0)
			player.setHiddenBrother(Misc.random(5));
	}
	
	public void sendInterfaces() {
		player.getPacketSender().sendOverlay(24);
		player.getPacketSender().sendConfig(453, slayedBrothers());
	}


	/**
	 * Handling of barrows brothers
	 */
	public static enum Brothers {
		AHRIM(2025, 0, 6821, 6702, new Location(3565, 3289, 0),
				new Location(3561, 3286, 0), new Location(3567, 3292, 0),
				new Location(3557, 9703, 0)),

				VERAC(2030, 1, 6823, 6707, new Location(3556, 3297, 0),
						new Location(3554, 3294, 0), new Location(3559, 3300, 0),
						new Location(3578, 9706, 0)),

						DHAROK(2026, 2, 6771, 6703, new Location(3574, 3297, 0),
								new Location(3570, 3293, 0), new Location(3576, 3299, 0),
								new Location(3556, 9718, 0)),

								GUTHAN(2027, 3, 6773, 6704, new Location(3576, 3281, 0),
										new Location(3574, 3279, 0), new Location(3579, 3284, 0),
										new Location(3534, 9704, 0)),

										TORAG(2029, 4, 6772, 6706, new Location(3553, 3283, 0),
												new Location(3550, 3280, 0), new Location(3555, 3285, 0),
												new Location(3568, 9683, 0)),

												KARIL(2028, 5, 6822, 6705, new Location(3565, 3276, 0),
														new Location(3563, 3273, 0), new Location(3568, 3279, 0),
														new Location(3546, 9684, 0));

		private int npcId, coffinId, stairsId, index;
		private Location exitLocation, moundArea, moundArea2, playerEntry;

		Brothers(int npcId, int index, int coffinId, int stairsId,
				Location exitLocation, Location moundArea,
				Location moundArea2, Location playerEntry) {
			this.npcId = npcId;
			this.index = index;
			this.coffinId = coffinId;
			this.stairsId = stairsId;
			this.exitLocation = exitLocation;
			this.moundArea = moundArea;
			this.moundArea2 = moundArea2;
			this.playerEntry = playerEntry;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getCoffinId() {
			return coffinId;
		}

		public int getStairsId() {
			return stairsId;
		}

		public Location getExitLocation() {
			return exitLocation;
		}

		public Location getMoundArea() {
			return moundArea;
		}

		public Location getMoundArea2() {
			return moundArea2;
		}

		public Location getplayerEntry() {
			return playerEntry;
		}

		public int getIndex() {
			return index;
		}
	}

	public static boolean digToBrother(final Player player) {
		int coordX = player.getX();
		int coordY = player.getY();
		for (Brothers bro : Brothers.values()) {
			if (coordX >= bro.getMoundArea().getX() && coordX <= bro.getMoundArea2().getX() && coordY >= bro.getMoundArea().getY() 	&& coordY <= bro.getMoundArea2().getY()) {
				player.getProperties().setTeleportLocation(Location.locate(bro.getplayerEntry().getX(), bro.getplayerEntry().getY(), 3));
				player.getPacketSender().sendMessage("You've broken into a crypt.");
				player.getControlerManager().setPlayer(player);
				player.getControlerManager().startControler("Barrows");
				return true;
			}
		}
		return false;
	}

	public void killedBrother() {
		if (brother != null) {
			if (brotherInform != null) {
				player.getKilledBarrowBrothers()[brotherInform.getIndex()] = true;
				player.setBarrowsKillCount(player.getBarrowsKillCount() + 1);
				player.getPacketSender().sendConfig(453, slayedBrothers());
			}
			brother = null;
		}
	}

	@Override
	public boolean processObjectClick1(GameObject obj) {
		switch(obj.getId()) {
		case 6703: // Barrows
		case 6704:
		case 6702:
		case 6705:
		case 6707:
		case 6706:
			for (Brothers bro : Brothers.values()) {
				if (obj.getId() == bro.getStairsId()) {
					player.getProperties().setTeleportLocation(new Location(bro.getExitLocation()
							.getX(), bro.getExitLocation().getY(), 0));
				}
			}
			return false;
		/*case 6823:
		case 6771:
		case 6821:
		case 6774:
		case 6822:
		case 6772:
		case 6773:
			if (brother != null) {
				player.getPacketSender().sendMessage("You found nothing.");
				return false;
			} else {
				for (Brothers bro : Brothers.values()) {
					if (obj.getId() == bro.getCoffinId()) {
//						if (player.getHiddenBrother() == bro.getIndex()) {
//							player.getDialogueManager().startDialogue(
//									"BarrowsD");
//							return false;
//						}
						if (player.getKilledBarrowBrothers()[bro.getIndex()]) {
							player.getPacketSender().sendMessage(
									"You found nothing.");
							return false;
						}
						brotherInform = bro;
						brother = new BarrowsBrother(bro.getNpcId(),
								new Location(player.getX(), player.getY(),
										player.getZ()), this);
						NodeWorker.getNPCs().add(brother);
						Main.getNodeWorker().offer(brother);
						brother.init();
						brother.getCombatAction().setVictim(player);
						brother.getCombatAction().execute();
						brother.getUpdateMasks().register(new ForceTextUpdate(
								"You dare disturb my rest!", brother.isNPC()));
						brother.getActionManager().register(brother.getCombatAction());
						player.setAttribute("barrowsbrother", brother);
						return false;
					}
				}
			}
			return false;*/
		default:
			return true;
		}
	}
	
	public int slayedBrothers() {
		int config = 0;
		for (int i = 0; i < player.getKilledBarrowBrothers().length; i++) {
			if (!player.getKilledBarrowBrothers()[i]) {
				continue;
			}
			config |= 1 << i;
		}
		int killCount = player.getBarrowsKillCount();
		return (killCount << 1) << 16 | config;
	}

	/**
	 * @return the barrowsFaces
	 */
	public int getBarrowsFaces() {
		return barrowsFaces;
	}

	/**
	 * @param barrowsFaces the barrowsFaces to set
	 */
	public void setBarrowsFaces(int barrowsFaces) {
		this.barrowsFaces = barrowsFaces;
	}

	/**
	 * @return the resetFace
	 */
	public int getResetFace() {
		return resetFace;
	}

	/**
	 * @param resetFace the resetFace to set
	 */
	public void setResetFace(int resetFace) {
		this.resetFace = resetFace;
	}
}
