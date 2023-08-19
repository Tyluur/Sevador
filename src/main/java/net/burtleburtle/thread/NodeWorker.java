package net.burtleburtle.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sevador.content.friendChat.FriendChatManager;
import com.sevador.game.node.Node;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.NPCWorldLoader;
import com.sevador.game.world.ObjectWorldLoader;
import com.sevador.game.world.PlayerWorldLoader;
import com.sevador.game.world.WorldLoader;
import com.sevador.utility.EntityList;

/**
 * The NodeWorker {@link Runnable} is used for adding and/or removing new nodes
 * to the game. <br>
 * This will also be used to handle several nodes (- when required).
 * 
 * Another name this can receive is: Virtual World
 * 
 * @author Emperor
 * 
 */
public final class NodeWorker implements Runnable {

	/**
	 * The players list.
	 */
	private static final EntityList<Player> PLAYERS = new EntityList<Player>(2000);

	/**
	 * The lobby players list.
	 */
	private static final EntityList<Player> LOBBY_PLAYERS = new EntityList<Player>(Short.MAX_VALUE);

	/**
	 * The NPCs list.
	 */
	private static final EntityList<NPC> NPCS = new EntityList<NPC>(32767);

	/**
	 * A queue holding all the nodes that await removal.
	 */
	private static final Queue<Node> AWAITING_REMOVAL = new LinkedBlockingQueue<Node>();

	/**
	 * A queue holding all the nodes that are awaiting to be added.
	 */
	private static final Queue<Node> AWAITING_ADDING = new LinkedBlockingQueue<Node>();

	/**
	 * The list of currently disconnected players that still have to be removed.
	 */
	private static final List<Player> DISCONNECTED_PLAYERS = new ArrayList<Player>();

	/**
	 * The loader instance.
	 */
	private static final WorldLoader<Player> PLAYER_LOADER = new PlayerWorldLoader();

	/**
	 * The NPC loader instance.
	 */
	private static final WorldLoader<NPC> NPC_LOADER = new NPCWorldLoader();

	/**
	 * The game object loader instance.
	 */
	private static final WorldLoader<GameObject> OBJECT_LOADER = new ObjectWorldLoader();

	/**
	 * The player names map.
	 */
	private static final Map<String, Player> PLAYER_NAME_MAP = new HashMap<String, Player>();

	@Override
	public void run() {
		Thread.currentThread().setName("NodeWorker");
		Node n = null;
		while (true) {
			try {
				while (!AWAITING_ADDING.isEmpty()) {
					n = AWAITING_ADDING.poll();
					if(n instanceof Player) {
						try {
							Player player = (Player) PLAYER_LOADER.load(PLAYER_LOADER.checkLogin(n));
							if(player != null) {
								boolean isLobby = player.getIOSession().isInLobby();
								if(isLobby) {
									if(getLobbyPlayer(player.getCredentials().getUsername()) == null) {
										LOBBY_PLAYERS.add(player);
										PLAYER_NAME_MAP.put("+lobby>" + player.getCredentials().getUsername(), player);
									} else {
										System.err.println("You were already logged in; disconnected " + player.getCredentials().getDisplayName() + " from LOBBY PLAYERS.");
										player.getIOSession().getChannel().disconnect(); //Disconnects the channel handler.
										continue;
									}
								} else {
									if (getPlayer(player.getCredentials().getUsername()) == null) {
										PLAYERS.add(player);
										PLAYER_NAME_MAP.put(player.getCredentials().getUsername(), player); 
									} else {
										System.err.println("You were already logged in; disconnected " + player.getCredentials().getDisplayName() + " from PLAYERS.");
										player.getIOSession().getChannel().disconnect(); //Disconnects the channel handler.
										continue;
									}
								}
								player.init();
								player.setLoggedIn(true);
								System.out.println("Registered player [name=" + player.getCredentials().getUsername() + ", lobby=" + isLobby + ", index=" + player.getIndex() + ", online=" + (isLobby? LOBBY_PLAYERS.size() : PLAYERS.size()) + "]");
							}
						} catch(Throwable t) {
							t.printStackTrace();
						}
					} else if (n instanceof NPC) {
						NPC npc = NPC_LOADER.load(NPC_LOADER.checkLogin(n));
						if (npc != null) {
							npc.init();
							NPCS.add(npc);
						}
					} else if (n instanceof GameObject) {
						OBJECT_LOADER.load(OBJECT_LOADER.checkLogin(n));
					} else {
						throw new IllegalStateException("Unsupported node - "+ n.getClass().getSimpleName());
					}
				}
				while (!AWAITING_REMOVAL.isEmpty()) {
					n = AWAITING_REMOVAL.poll();
					if(n instanceof Player) {
						Player p = (Player) n;
						boolean isLobby = LOBBY_PLAYERS.get(p.getIndex()) == p;
						if(!isLobby && PLAYERS.get(p.getIndex()) != p) {
							PLAYER_NAME_MAP.remove("+lobby>" + p.getCredentials().getUsername());
							PLAYER_NAME_MAP.remove(p.getCredentials().getUsername());
							continue;
						}
						Player player = PLAYER_LOADER.save(p);
						if(player == null) {
							//We are unable to remove the player.
							//If the player is in combat the combat will continue.
							//They will be disconnected once their last ping timer runs out,
							//Or the minute runnable determines the channel is disconnected.
							//Either way, the channel disconnected will be true.
							continue;
						}
						if (isLobby) {
							LOBBY_PLAYERS.remove(player.clearLobby());
							PLAYER_NAME_MAP.remove("+lobby>" + player.getCredentials().getUsername());
						} else {
							PLAYERS.remove(player.clear());
							PLAYER_NAME_MAP.remove(player.getCredentials().getUsername());
						}
						System.out.println("Unregistered player [name=" + player.getCredentials().getUsername() + ", lobby=" + isLobby + ", online=" + (isLobby ? LOBBY_PLAYERS.size() : PLAYERS.size()) + "]");
					} else if (n instanceof NPC) {
						NPC npc = NPC_LOADER.save(n).clear();
						if (npc != null) {
							NPCS.remove(npc);
						}
					} else if (n instanceof GameObject) {
						OBJECT_LOADER.save(n);
					}
				}
				for (Iterator<Player> it = DISCONNECTED_PLAYERS.iterator(); it.hasNext();) {
					Player p = it.next();
					boolean isLobby = LOBBY_PLAYERS.get(p.getIndex()) == p;
					if (!isLobby && PLAYERS.get(p.getIndex()) != p) {
						PLAYER_NAME_MAP.remove("+lobby>"+ p.getCredentials().getUsername());
						PLAYER_NAME_MAP.remove(p.getCredentials().getUsername());
						it.remove(); // Prevent random resets.
						continue;
					}
					Player player = PLAYER_LOADER.save(p);
					if (player == null) continue; // If the player isn't ready to be removed yet.
					if (isLobby) {
						LOBBY_PLAYERS.remove(player.clearLobby());
						PLAYER_NAME_MAP.remove("+lobby>"+ player.getCredentials().getUsername());
					}
					if (PLAYERS.get(player.getIndex()) == player) {
						NodeWorker.getPlayerWorldLoader().save(player, true);
						player.getControlerManager().logout();
						FriendChatManager.getFriendChatManager().leaveChannel(player);
						PLAYERS.remove(player.clear());
						PLAYER_NAME_MAP.remove(player.getCredentials().getUsername());
						System.out.println("Player "+ player.getCredentials().getUsername()+ " disconnected: [" + player.getIndex() + ", " + PLAYERS.size() + "].");
					}
					it.remove();
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
			try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Adds this node to the awaiting adding queue.
	 * 
	 * @param node
	 *            The node to add.
	 * @return {@code True} if the queue didn't contain the node, {@code false}
	 *         if the queue already contained the node.
	 */
	public synchronized boolean offer(Node node) {
		if (AWAITING_ADDING.contains(node)) {
			return false;
		}
		return AWAITING_ADDING.add(node);
	}

	/**
	 * Adds this node to the awaiting removal queue.
	 * 
	 * @param node
	 *            The node to add.
	 * @return {@code True} if the queue didn't contain the node, {@code false}
	 *         if the queue already contained the node.
	 */
	public synchronized boolean remove(Node node) {
		if (AWAITING_REMOVAL.contains(node)) {
			return false;
		}
		return AWAITING_REMOVAL.add(node);
	}

	/**
	 * Gets the list of players.
	 * 
	 * @return The players.
	 */
	public synchronized static EntityList<Player> getPlayers() {
		synchronized (PLAYERS) {
			return PLAYERS;
		}
	}

	/**
	 * Gets the list of players.
	 * 
	 * @return The players.
	 */
	public synchronized static EntityList<NPC> getNPCs() {
		return NPCS;
	}

	/**
	 * Gets the player with the matching username.
	 * 
	 * @param username
	 *            The username.
	 * @return The player.
	 */
	public static Player getPlayer(String username) {
		return PLAYER_NAME_MAP.get(username);
	}

	/**
	 * Gets the player with the matching username.
	 * 
	 * @param username
	 *            The username.
	 * @return The player.
	 */
	public static Player getLobbyPlayer(String username) {
		return PLAYER_NAME_MAP.get("+lobby>" + username);
	}

	/**
	 * @return the lobbyPlayers
	 */
	public static EntityList<Player> getLobbyPlayers() {
		return LOBBY_PLAYERS;
	}

	/**
	 * Gets the player world loader instance.
	 * 
	 * @return The player world loader instance.
	 */
	public static PlayerWorldLoader getPlayerWorldLoader() {
		return (PlayerWorldLoader) PLAYER_LOADER;
	}
	
	public static boolean containsPlayer(String username) {
		for (Player p2 : NodeWorker.getPlayers()) {
			if (p2 == null)
				continue;
			if (p2.getCredentials().getUsername().equals(username))
				return true;
		}
		return false;
	}
}
