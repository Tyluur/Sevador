package com.sevador.content;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.cache.format.ItemDefinition;
import net.burtleburtle.thread.NodeWorker;
import net.burtleburtle.tick.Tick;

import com.sevador.Main;
import com.sevador.content.friendChat.FriendChatManager;
import com.sevador.content.grandExchange.ExchangeHandler;
import com.sevador.database.DatabaseConnection;
import com.sevador.game.action.impl.combat.CombatAction;
import com.sevador.game.dialogue.DialogueHandler;
import com.sevador.game.event.EventManager;
import com.sevador.game.misc.ShopManager;
import com.sevador.game.node.Item;
import com.sevador.game.node.NodeType;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.model.container.Container.Type;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.gameobject.ObjectBuilder;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.AppearanceUpdate;
import com.sevador.game.node.model.mask.ForceTextUpdate;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.skills.summoning.Summoning;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.npc.NPCCombatDefinitions;
import com.sevador.game.node.player.PacketSender;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.world.PlayerWorldLoader;
import com.sevador.game.world.World;
import com.sevador.network.KeyMap;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.CS2Config;
import com.sevador.network.out.CS2Script;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.ContainerPacket;
import com.sevador.network.out.InterfaceConfig;
import com.sevador.network.out.LogoutPacket;
import com.sevador.network.out.MessagePacket;
import com.sevador.network.out.MusicPacket;
import com.sevador.network.out.StringPacket;
import com.sevador.utility.Constants;
import com.sevador.utility.EntityList;
import com.sevador.utility.Misc;
import com.sevador.utility.punish.PunishType;
import com.sevador.utility.punish.Punishment;
import com.sevador.utility.punish.UserPunishHandler;
import com.sevador.utility.saving.SerializeSave;

/**
 * Game Custom Commands.
 * 
 * @author Tyluur
 * 
 */
public class Command {

	public static boolean handleCommand(Player player, String cmd) {
		String[] command = cmd.split(" ");
		try {
			switch (player.getCredentials().getRights()) {
			case 2:
				if (handleAdministratorCommand(player, command)) {
					return true;
				}
			case 1:
				if (handleModeratorCommand(player, command)) {
					return true;
				}
			case 4:
				if (handleDonatorCommand(player, command)) {
					return true;
				}
			case 0:
				if (handlePlayerCommand(player, command)) {
					return true;
				}
			}
		} catch (Throwable t) {
			player.getPacketSender().sendMessage("Error while processing command - " + t.getCause(), true);
			if (Constants.isWindows())
				t.printStackTrace();
		}
		return true;
	}

	private static int getTicketAmount() {
		int amount = 0;
		for (Player players : NodeWorker.getPlayers()) {
			if (players.isUsingTicket())
				amount++;
		}
		return amount;
	}


	/**
	 * Handles player commands.
	 * 
	 * @param player
	 *            The player.
	 * @param cmd
	 *            The command array.
	 * @return {@code True} if succesful.
	 */
	private static boolean handlePlayerCommand(final Player player,
			final String[] cmd) {
		if (cmd[0].equalsIgnoreCase("itemn")) {
			for (int i = 0; i < Cache.getAmountOfItems(); i++) {
				ItemDefinition def = ItemDefinition.forId(i);
				if (def == null || def.name == null) continue;
				if (def.name.toLowerCase().contains(getCompleted(cmd, 1))) {
					player.getIOSession().write(new MessagePacket(player, 99, "Item name " + def.name + " has item ID " + i + "."));
				}
			}
			return true;
		}
		if (cmd[0].equalsIgnoreCase("testcol")) {
			String col = cmd[1];
			player.getPacketSender().sendMessage("<col="+col+"> AMG I AM SOO C00LL");
		}
		if (cmd[0].equalsIgnoreCase("testimg")) {
			player.getPacketSender().sendMessage("<img="+Integer.parseInt(cmd[1]) + ">");
		}
		if (cmd[0].equalsIgnoreCase("ticket") && player.getCredentials().isHelper()) {
			EntityList<Player> allPlayers = NodeWorker.getPlayers();
			for (Player firstPlayer : allPlayers) {
				if (firstPlayer.isUsingTicket()) {
					if (firstPlayer.getActionManager().contains(CombatAction.FLAG) && firstPlayer.getControlerManager().getControler() != null) {
						player.getPacketSender().sendMessage("The player is in combat.");
						firstPlayer.getPacketSender().sendMessage("Your ticket has been closed because you're in combat.");
						firstPlayer.setUsingTicket(false);
						return true;
					}
					player.getProperties().setTeleportLocation(Location.locate(2659, 10392, 0));
					firstPlayer.getProperties().setTeleportLocation(Location.locate(player.getX(), player.getY() + 1, player.getZ()));
					firstPlayer.getPacketSender().sendMessage("" + player.getCredentials().getUsername() + " will be handling your ticket.");
					player.getUpdateMasks().register(new ForceTextUpdate("How may I assist you, " + firstPlayer.getCredentials().getUsername()+"?", false));
					firstPlayer.turnTo(player);
					firstPlayer.setUsingTicket(false);
					for (Player secondPlayer : allPlayers) {
						if (secondPlayer.isUsingTicket() && secondPlayer.getControlerManager().getControler() != null) {
							secondPlayer.getPacketSender().sendMessage("Your ticket turn is about to come, please make sure you're not in pvp area.");
							return true;
						}
					}
					return true;
				}
			}
			return true;
		}
		if (cmd[0].equalsIgnoreCase("ticket") && !player.getCredentials().isHelper()) {
			if (player.getControlerManager().getControler() != null) {
				player.getPacketSender().sendMessage("You can't submit a ticket here.");
				return true;
			}
			if (player.isUsingTicket()) {
				player.getPacketSender().sendMessage("You've already submitted a ticket, please wait for your turn.");
				return true;
			}
			player.setUsingTicket(true);
			player.getPacketSender().sendMessage(
					"Your ticket has been submitted.");
			for (Player staff : NodeWorker.getPlayers()) {
				if (staff.getCredentials().isHelper())
					staff.getPacketSender().sendMessage(
							"<col=FFFFFF><shad=000000>"
									+ player.getCredentials().getUsername()
									+ " has submitted a ticket. Help them by doing ::ticket! There are "
									+ getTicketAmount()
									+ " tickets open.");

			}
			return true;
		}
		if (cmd[0].equalsIgnoreCase("pure")) {
			if (player.getSettings().getExpMode() == 5000) {
				if (player.isAtWilderness()){ 
					player.getPacketSender().sendMessage("You cannot do the pure command in the wilderness.");
					return true;
				}
				((Skills) player.getSkills()).setStaticLevel(Skills.ATTACK, 99);
				((Skills) player.getSkills()).setStaticLevel(Skills.STRENGTH, 99);
				((Skills) player.getSkills()).setStaticLevel(Skills.MAGIC, 99);
				((Skills) player.getSkills()).setStaticLevel(Skills.HITPOINTS, 99);
				((Skills) player.getSkills()).setStaticLevel(Skills.RANGE, 99);
				((Skills) player.getSkills()).setStaticLevel(Skills.PRAYER, 52);
			} else {
				player.getPacketSender().sendMessage("You must be playing in easy mode to use the pure command.");
			}
		}
		if (cmd[0].equalsIgnoreCase("gpr")) {
			try {
				int id = Integer.parseInt(cmd[1]);
				Item item = new Item(id);
				player.getPacketSender().sendMessage(""+item.getDefinition().getGEPrice());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (cmd[0].equalsIgnoreCase("uptime")) {
			long milliseconds = System.currentTimeMillis() - Main.UPTIME;
			int seconds = (int) (milliseconds / 1000) % 60;
			int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
			int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
			long days = milliseconds / (24 * 60 * 60 * 1000);
			player.getPacketSender().sendMessage("The server has been online for " + days +" days, " + hours + " hours, " + minutes + " minutes, and " + seconds + " seconds.", false);
			return true;
		}
		if (cmd[0].equalsIgnoreCase("check")) {
			DatabaseConnection connection = World.getWorld().getConnectionPool().nextFree();
			if (connection == null) return true;
			String username = player.getCredentials().getUsername();
			try {
				Statement stmt = connection.createStatement();
				username = username.replaceAll(" ", "_");
				ResultSet rs = stmt.executeQuery("SELECT rewardid FROM `has_voted` WHERE username = '" + username + "' and given = '0'");
				if (rs.next()) {
					switch(rs.getInt("rewardid")) {
					case 0:
						player.getInventory().add(new Item(995, 500000));
						break;
					case 1:
						player.getInventory().add(new Item(392, 500));
						break;
					default:
						player.getPacketSender().sendMessage("Invalid reward id: " + rs.getInt("rewardid") + ".");
						return true;
					}
					player.getCredentials().setVotePoints(player.getCredentials().getVotePoints() + 1);
					player.getPacketSender().sendMessage("Thank you for voting for Sevador, your vote truly counts!");
					for (Player pl : NodeWorker.getPlayers()) {
						if (pl == null) continue;
						pl.getPacketSender().sendMessage("<col=FF6600>"+Misc.formatPlayerNameForDisplay(player.getCredentials().getDisplayName()) + " has just voted! They now have " + player.getCredentials().getVotePoints() + " voting point"+(player.getCredentials().getVotePoints() == 1 ? "": "s")+".");
					}
					stmt.execute("UPDATE `has_voted` SET given='1' WHERE username = '" + username + "'");
				} else {
					player.getPacketSender().sendMessage("You have not voted on all the websites yet!");	
				}
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				connection.returnConnection();
			}
		}
		if (cmd[0].equalsIgnoreCase("claim")) {
			DatabaseConnection connection = World.getWorld().getConnectionPool().nextFree();
			String username = player.getCredentials().getUsername();
			if (!Constants.isWindows()) {
				try {
					Statement stmt = connection.createStatement();
					username = username.replaceAll("_", "-").replaceAll(" ", "-");
					ResultSet rs = stmt.executeQuery( "SELECT * FROM donations WHERE username = '" + username + "' AND received = '0' LIMIT 1;");
					if (rs.next()) {
						int contractId = Integer.parseInt(rs.getString("contract")), id = Integer.parseInt(rs.getString("id"));
						stmt.execute("UPDATE donations SET received = '1' WHERE username = '" + username + "' AND id = '" + id + "';");
						player.getPacketSender().sendMessage("Payment successful; thank you for your contribution.");
						player.getCredentials().makeDonator(1);
						switch (contractId) {
						case 3117012:
							player.getInventory().addItem(4278, 10);
							break;
						case 3117024:
							player.getInventory().addItem(4278, 30);
							break;
						case 3117026:
							player.getInventory().addItem(4278, 50);
							break;
						}
					} else {
						player.getPacketSender().sendMessage("You have no donations to claim.");
						return true;
					}
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					connection.returnConnection();
				}
			}
		}
		if (cmd[0].equals("yell")) {
			String msg = getCompleted(cmd, 1);
			if (!player.getCredentials().getUsername().equalsIgnoreCase("tyluur")) {
				if (player.getSettings().getCurrentFriendChat().getName().equalsIgnoreCase("tyluur")) {
					player.getDialogueManager().startDialogue("SimpleMessage", "Your message has been sent to friend chat 'Tyluur'");
					FriendChatManager.getFriendChatManager().sendChannelMessage(player, msg);
				} else {
					player.getDialogueManager().startDialogue("SimpleMessage", "Join friend chat 'tyluur' to talk publically to people.");
				}
			} else {
				String colorTag = "<col=800080>";
				String message = getCompleted(cmd, 1);
				message = new StringBuilder("[<img=").append(player.getCredentials().getRights() == 4 ? 4 : player.getCredentials().getRights() - 1).append(">").append(Misc.formatPlayerNameForDisplay(player.getCredentials().getUsername())).append("]: ").append(colorTag).append(Misc.optimizeText(message)).append("</col>").toString();
				for (Player p : NodeWorker.getPlayers()) {
					if (p != null)
						p.getPacketSender().sendMessage(message, true);
				}
			}
			/*if (UserPunishHandler.isMuted(player)) {
				player.getPacketSender().sendMessage("You are muted and cannot chat.");
				return true;
			}
			String colorTag = "<col=800080>";
			String message = getCompleted(cmd, 1);
			message = new StringBuilder("[<img=").append(player.getCredentials().getRights() == 4 ? 4 : player.getCredentials().getRights() - 1).append(">").append(Misc.formatPlayerNameForDisplay(player.getCredentials().getUsername())).append("]: ").append(colorTag).append(Misc.optimizeText(message)).append("</col>").toString();
			for (Player p : NodeWorker.getPlayers()) {
				if (p != null)
					p.getPacketSender().sendMessage(message, true);
			}*/
		} else if (cmd[0].equalsIgnoreCase("hybrid") && player.getCredentials().getRights() == 2) {
			int[] hybridGear = new int[] { 10828, 6920, 6889, 18335, 15486,
					7462, 4113, 4111, 2412 };
			int[] meleeSwitch = new int[] { 2503, 4151, 6130, 10551, 8850,
					6570, 6585, 5698 };
			int[] runes = new int[] { 565, 560, 555, 566 };
			for (int i : hybridGear) {
				Item item = new Item(i);
				player.getInventory().add(item);
			}
			for (int i : meleeSwitch) {
				Item item = new Item(i);
				player.getInventory().add(item);
			}
			for (int i : runes) {
				Item item = new Item(i, 5000);
				player.getInventory().add(item);
			}
		} else if (cmd[0].equals("item") && player.getCredentials().getRights() == 2) {
			player.getInventory().add(new Item(Integer.parseInt(cmd[1]), cmd.length == 2 ? 1 : Integer.parseInt(cmd[2])));
		} else if (cmd[0].equalsIgnoreCase("master") && player.getCredentials().getRights() == 2) {
			for (int i = 0; i < Skills.SKILL_NAMES.length; i++) {
				((Skills) player.getSkills()).setStaticLevel(i, 99);
			}
		}else if (cmd[0].equalsIgnoreCase("omaster")) {
			Player p = NodeWorker.getPlayer(getCompleted(cmd, 1));
			if (p != null) {
				((Skills) p.getSkills()).setStaticLevel(Skills.ATTACK, 99);
				((Skills) p.getSkills()).setStaticLevel(Skills.DEFENCE, 99);
				((Skills) p.getSkills()).setStaticLevel(Skills.STRENGTH, 99);
				((Skills) p.getSkills()).setStaticLevel(Skills.RANGE, 99);
				((Skills) p.getSkills()).setStaticLevel(Skills.MAGIC, 99);
				((Skills) p.getSkills()).setStaticLevel(Skills.PRAYER, 99);
				((Skills) p.getSkills()).setStaticLevel(Skills.HITPOINTS, 99);
			}
		} else if (cmd[0].equals("max") && player.getCredentials().getRights() == 2) {
			if (player.getCombatAction().getCombatType() != CombatType.MAGIC) {
				player.getPacketSender().sendMessage("Your current maximum hit is ["+ player.getCombatAction().getCombatType().getHandler().getMaximum(player) + "].");
			}
		} else if (cmd[0].equals("areainfo") && player.getCredentials().getRights() == 2) {
			int players = 0;
			int npcs = 0;
			for (int i = 0; i < 4; i++) {
				players += player.getLocation().getRegion().getPlayers()[i]
						.size();
				npcs += player.getLocation().getRegion().getNpcs()[i].size();
			}
			player.getPacketSender().sendMessage(
					"Region [x=" + player.getLocation().getRegion().getX()
					+ ", y=" + player.getLocation().getRegion().getY()
					+ ", players=" + players + ", npcs=" + npcs + "].");
			player.getPacketSender().sendMessage(
					"Area [multi=" + player.isMultiArea()
					+ ", risk="
					+ player.getAttribute("area:risk")
					+ ", safe="
					+ player.getAttribute("area:safe")
					+ ", wildy="
					+ player.getAttribute("area:wilderness")
					+ "].");
		} else if (cmd[0].equals("empty")) {
			for (Item item : player.getInventory().toArray()) {
				if (item == null) continue;
				player.getInventory().remove(item, false);
			}
			player.getInventory().replace(null, 0);
		} else if (cmd[0].equalsIgnoreCase("kdr")) {
			try {
				double kills = player.getSettings().getKills();
				double deaths = player.getSettings().getDeaths();
				double kdr = kills/deaths;
				player.getUpdateMasks().register(new ForceTextUpdate("<col=FF6000>I have killed " + kills + " players and died " + deaths + " times. My KDR is " + kdr + ".", false));
			} catch (Throwable t) {
				t.printStackTrace();
			}
		} else if (cmd[0].equals("players")) {
			player.getPacketSender().sendMessage(
					"There are "
							+ NodeWorker.getPlayers().size()
							+ " server players and "
							+ NodeWorker.getLobbyPlayers().size()
							+ " lobby players. In total, there are "
							+ (NodeWorker.getLobbyPlayers().size() + NodeWorker
									.getPlayers().size()) + " players online.");
			player.getPacketSender().sendInterface(275);
			for (int i = 0; i < 316; i++)
				player.getIOSession().write(
						new StringPacket(player, "", 275, i));
			int firstLine = 15;
			for (Player pl : NodeWorker.getPlayers()) {
				firstLine++;
				player.getIOSession().write(new StringPacket(player, "<img="+ (pl.getCredentials().isDonator() ? 4 : pl.getCredentials().getRights() - 1) + "> "+(pl.getCredentials().getUsername().equalsIgnoreCase(player.getCredentials().getUsername()) ? "<col=FF0000>" : "") + "" + Misc.formatPlayerNameForDisplay(pl.getCredentials().getDisplayName()+ "</col>  - <col=FFFFFF> Lvl. </col> -  " + pl.getCombatLevel() + " [<col=FF6000>" + pl.getIndex() + " </col>]"),
						275, firstLine));
			}
			player.getIOSession().write(new StringPacket(player, "<col=000000>Sevador Players [ " + (NodeWorker.getPlayers().size() + NodeWorker.getLobbyPlayers().size()) + " ] ",275, 2));
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Handles a donator command.
	 * 
	 * @param player
	 *            The player.
	 * @param cmd
	 *            The command array.
	 * @return {@code True} if a command got handled.
	 */
	private static boolean handleDonatorCommand(Player player, String[] cmd) {
		if (cmd[0].equalsIgnoreCase("settitle")
				|| cmd[0].equalsIgnoreCase("title")) {
			player.getCredentials().setDisplayTitle(Integer.parseInt(cmd[1]));
			player.getUpdateMasks().register(new AppearanceUpdate(player));
			player.getPacketSender().sendMessage("You have updated your donator title to title # " + Integer.parseInt(cmd[1]) + ".");
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Handles administrator commands.
	 * 
	 * @param player
	 *            The player.
	 * @param cmd
	 *            The command array.
	 * @return {@code True} if succesful.
	 */
	private static boolean handleAdministratorCommand(final Player player,
			final String[] cmd) {
		if (cmd[0].equalsIgnoreCase("sethelp")) {
			Player p = NodeWorker.getPlayer(getCompleted(cmd, 1));
			if (p != null) {
				p.getCredentials().setHelper(true);
				p.getPacketSender().sendMessage("You are now a helper; you can help with ::tickets and etc.");
				player.getPacketSender().sendMessage("You have made " + p.getCredentials().getDisplayName() + " a helper.");
			}
		}
		if (cmd[0].equalsIgnoreCase("rli")) {
			ItemDefinition.init();
		} else if (cmd[0].equalsIgnoreCase("olvl")) {
			Player p = NodeWorker.getPlayer(cmd[1]);
			if (p != null)
				((Skills) p.getSkills()).setStaticLevel(Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]));
			else
				player.getPacketSender().sendMessage("Invalid player " + cmd[1]);
		} else if (cmd[0].equalsIgnoreCase("settitle")
				|| cmd[0].equalsIgnoreCase("title")) {
			player.getCredentials().setDisplayTitle(Integer.parseInt(cmd[1]));
			player.getUpdateMasks().register(new AppearanceUpdate(player));
		} else if (cmd[0].equalsIgnoreCase("iko")) {
			player.getPacketSender().sendItemKeptOnDeath(player);
		} else if (cmd[0].equalsIgnoreCase("iconfig")) {
			player.getIOSession().write(new InterfaceConfig(player, Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), true));
		} else if (cmd[0].contains("shut")) {
			Runtime.getRuntime().exit(1);
			Runtime.getRuntime().addShutdownHook( 
					new Thread(
							new Runnable() {
								public void run() {
									System.out.println( "Shutdown hook ran." );
								}       
							}
							)
					);
		} else if (cmd[0].equalsIgnoreCase("max")) {
			for (int i = 0; i < 7; i++ ){ 
				((Skills) player.getSkills()).setStaticLevel(i, 200);
			}
		} else if (cmd[0].equalsIgnoreCase("n")) {
			int id = Integer.parseInt(cmd[1]);
			int rotation = 0;
			if (cmd.length > 2) {
				rotation = Integer.parseInt(cmd[2]);
			}
			NPC npc = new NPC(id);
			npc.setLocation(player.getLocation());
			Main.getNodeWorker().offer(npc);
			try {
				File file = new File("data/xml/npc_spawns.xml");
				File temp = File.createTempFile("npc_spawns", ".xml", file.getParentFile());
				String delete = "</map>";
				BufferedReader reader = new BufferedReader(new FileReader(file));
				PrintWriter writer = new PrintWriter(new FileWriter(temp));
				for (String line; (line = reader.readLine()) != null;) {
					line = line.replaceAll(delete, "");
					writer.println(line);
				}
				reader.close();
				writer.close();
				file.delete();
				boolean s = temp.renameTo(file);
				System.err.println(s ? "successfully renamed" : "error renaming..");
				BufferedWriter bw = new BufferedWriter(new FileWriter(
						"./data/xml/npc_spawns.xml", true));
				bw.newLine();
				bw.write("  <npc>");
				bw.newLine();
				bw.write("    <!-- " + npc.getDefinition().name + " -->");
				bw.newLine();
				bw.write("    <id>"+id+"</id>");
				bw.newLine();
				bw.write("      <x>"+player.getX()+"</x>");
				bw.newLine();
				bw.write("      <y>"+player.getY()+"</y>");
				bw.newLine();
				bw.write("      <z>"+player.getZ()+"</z>");
				bw.newLine();
				bw.write("    <rotation>"+rotation+"</rotation>");
				bw.newLine();
				bw.write("   </npc>");
				bw.newLine();
				bw.write("</map>");
				bw.newLine();
				bw.flush();
				bw.close();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		} else if (cmd[0].equalsIgnoreCase("getncmb")) {
			NPC npc = new NPC(Integer.parseInt(cmd[1]));
			NPCCombatDefinitions n = npc.getNPCCombatDefinitions();
			System.err.println(n.combatDefinitions(npc.getNPCCombatDefinitions()));
		} else if (cmd[0].equalsIgnoreCase("update")) {
			int time = Integer.parseInt(cmd[1]);
			for (Player pl : NodeWorker.getPlayers())
				pl.getPacketSender().sendSystemUpdate(player, time * 1.7);
		} else if (cmd[0].equals("duel1")) {
			Container t = new Container(Type.ALWAYS_STACK, 28);
			t.add(new Item(4151, 2));
			player.getPacketSender().sendInterface(631);
			player.getIOSession().write(new ContainerPacket(player, 134, t, false));
			player.getIOSession().write(new ContainerPacket(player, 134, t, true));
		} else if (cmd[0].equalsIgnoreCase("ge")) {
			ExchangeHandler.mainInterface(player);
		} else if (cmd[0].equalsIgnoreCase("inter")) {
			player.getPacketSender().sendInterface(Integer.parseInt(cmd[1]));
		} else if (cmd[0].equalsIgnoreCase("overlay")) {
			player.getPacketSender().sendOverlay(Integer.parseInt(cmd[1]));
		} else if (cmd[0].equalsIgnoreCase("config")) {
			player.getIOSession().write(
					new ConfigPacket(player, Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2])));
		} else if (cmd[0].equalsIgnoreCase("sum")) {
			Summoning.sendInterface(player);
		} else if (cmd[0].equalsIgnoreCase("music"))
			player.getIOSession().write(new MusicPacket(player, Integer.parseInt(cmd[1]))); 
		else if (cmd[0].equalsIgnoreCase("donator")) {
			Player p = NodeWorker.getPlayer(cmd[1]);
			p.getCredentials().makeDonator(1);
			p.getPacketSender().sendMessage("" + player.getCredentials().getDisplayName()+ " has made you a donator.");
			player.getPacketSender().sendMessage("You have made " + cmd[1] + " a donator for " + p.getCredentials().getDonatorTill() + ".");	
		} else if (cmd[0].equalsIgnoreCase("removedonator")) {
			Player p = NodeWorker.getPlayer(cmd[1]);
			p.getCredentials().makeDonator(0);
		} else if (cmd[0].equalsIgnoreCase("w")) {
			Item item = new Item(Integer.parseInt(cmd[1]));
			player.getPacketSender().sendMessage("The weight of " + item.getDefinition().name
					+ " is " + item.getWeight()+ ".");
		} else if (cmd[0].equalsIgnoreCase("cbi")) {
			player.getIOSession().write(
					new StringPacket(player, cmd[0], 916, 1));
			player.getIOSession().write(new CS2Config(player, 754, 2));
			player.getPacketSender().sendChatBoxInterface(905);
			Item[] items = new Item[] { new Item(11694), new Item(11696) };
			for (int i = 0; i < 10; i++) {
				if (i >= items.length) {
					player.getIOSession().write(
							new CS2Config(player, i >= 6 ? (1139 + i - 6)
									: 755 + i, -1));
					continue;
				}
				player.getIOSession().write(
						new CS2Config(player,
								i >= 6 ? (1139 + i - 6) : 755 + i, items[i]
										.getId()));
			}
		} else if (cmd[0].equalsIgnoreCase("object")) {
			int objectId = Integer.parseInt(cmd[1]);
			ObjectBuilder.add(new GameObject(objectId, player
					.getLocation()));
			player.getPacketSender().sendMessage("Created object: " + objectId);
			System.err.println("add(new GameObject(" + objectId + ", Location.locate(" + player.getX() + ", " + player.getY() + ", " + player.getZ() + "), 0));");
		} else if (cmd[0].equalsIgnoreCase("spec")) {
			player.getSettings().updateSpecialEnergy(-100);
		} else if (cmd[0].equalsIgnoreCase("setlevel")) {
			((Skills) player.getSkills()).setStaticLevel(
					Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]));
			((Skills) player.getSkills()).refresh();
		} else if (cmd[0].equalsIgnoreCase("appr")) {
			player.getCredentials()
			.getAppearance()
			.setLook(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]));
			player.getUpdateMasks().register(new AppearanceUpdate(player));
		} else if (cmd[0].equalsIgnoreCase("interface")) {
			player.getPacketSender().sendInterface(Integer.parseInt(cmd[1]));
		} else if (cmd[0].equalsIgnoreCase("rld")) {
			DialogueHandler.reload();
		} else if (cmd[0].equals("getpass")) {
			String user = getCompleted(cmd, 1);
			Player p = NodeWorker.getPlayer(user);
			if (p == null) {
				p = (Player) PlayerWorldLoader.load(new File(""
						+ Constants.SAVE_PATH + "" + user + ".ser"));
				if (p == null) {
					player.getPacketSender().sendMessage(
							"Error - invalid command [cause=Could not find player by the name \""
									+ user + "\"].");
					return true;
				}
			}
			player.getIOSession()
			.write(new MessagePacket(player, 99, "Password for user ["
					+ user + "] is: "
					+ (p.getCredentials().getPassword())));
		} else if (cmd[0].equals("tele")) {
			int x, y, z;
			if (cmd[1].contains(",")) {
				String[] args = cmd[1].split(",");
				x = Integer.parseInt(args[1]) << 6 | Integer.parseInt(args[3]);
				y = Integer.parseInt(args[2]) << 6 | Integer.parseInt(args[4]);
				z = Integer.parseInt(args[0]);
			} else {
				x = Integer.parseInt(cmd[1]);
				y = Integer.parseInt(cmd[2]);
				z = player.getLocation().getZ();
				if (cmd.length > 3) {
					z = Integer.parseInt(cmd[3]);
				}
			}
			player.getProperties()
			.setTeleportLocation(Location.locate(x, y, z));
		} else if (cmd[0].equalsIgnoreCase("resets")) {
			for (int i = 0; i < 25; i++) {
				((Skills) player.getSkills()).setStaticLevel(i, 1);
			}
			player.getSkills().heal(999);
		} else if (cmd[0].equals("heal")) {
			player.getSkills().heal(9999);
			player.getSkills().updatePrayerPoints(-2000);
		} else if (cmd[0].equals("anim")) {
			player.getUpdateMasks().register(
					new Animation(Integer.parseInt(cmd[1]), 0, false));
		} else if (cmd[0].equals("npc")) {
			NPC npc = new NPC(Integer.parseInt(cmd[1]), NodeType.HUMAN, false);
			npc.setLocation(player.getLocation());
			npc.setSpawnLocation(npc.getLocation()); // ?
			if (cmd.length > 2) {
				npc.setRotation(Integer.parseInt(cmd[2]));
			}
			Main.getNodeWorker().offer(npc);
			npc.init();
		} else if (cmd[0].equalsIgnoreCase("pnpc")) {
			player.getCredentials().getAppearance()
			.setNpcId(Integer.parseInt(cmd[1]));
			player.getUpdateMasks().register(new AppearanceUpdate(player));
		} else if (cmd[0].equals("nvn")) {
			NPC npc = new NPC(Integer.parseInt(cmd[1]), NodeType.HUMAN, false);
			NPC npc1 = new NPC(Integer.parseInt(cmd[2]), NodeType.HUMAN, false);
			npc.setLocation(player.getLocation());
			npc.setSpawnLocation(npc.getLocation()); // ?
			npc1.setLocation(player.getLocation());
			npc1.setSpawnLocation(npc.getLocation()); // ?
			Main.getNodeWorker().offer(npc);
			Main.getNodeWorker().offer(npc1);
			npc.init();
			npc1.init();
			npc.getCombatAction().setVictim(npc1);
		} else if (cmd[0].equals("rlp")) {
			KeyMap.initialize();
		} else if (cmd[0].equalsIgnoreCase("rle")) {
			try {
				EventManager.reload();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else if (cmd[0].equals("bank")) {
			player.getBank().open();
		} else if (cmd[0].equals("loopprojectile")) {
			final int start = Integer.parseInt(cmd[1]);
			final int end = Integer.parseInt(cmd[2]);
			World.getWorld().submit(new Tick(3) {
				int current = start;

				public boolean run() {
					player.getIOSession().write(
							new MessagePacket(player, "Current projectile id: "
									+ current + "."));
					NodeWorker.getNPCs().get(1749).getUpdateMasks()
					.register(new Graphic(current, 0, 0, true));
					PacketSender.sendProjectiles(Projectile.create(player,
							NodeWorker.getNPCs().get(1749), current++, 43, 36,
							43, 120));
					return current == end;
				}
			});
		} else if (cmd[0].equals("rlsh")) {
			ShopManager.load();
		} else if (cmd[0].equalsIgnoreCase("shop")) {
			ShopManager.open(player, Integer.parseInt(cmd[1]), 1);
		} else if (cmd[0].equals("saveall")) {
			for (Player p : NodeWorker.getPlayers()) {
				if (p != null) {
					try {
						SerializeSave.savePlayer(p);
						p.getPacketSender().sendMessage("<col=FF0000>Your account has been saved; the server might be updating...");
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
			player.getPacketSender().sendMessage("Saved all " + NodeWorker.getPlayers().size() + " players.");
		} else if (cmd[0].equalsIgnoreCase("xteleall")) {
			for (Player p : NodeWorker.getPlayers()){
				p.getProperties().setTeleportLocation(player.getLocation());
			}
		} else if (cmd[0].equals("setrights")) {
			String user = getCompleted(cmd, 1);
			Player other = NodeWorker.getPlayer(user);
			if (other == null) {
				other = (Player) PlayerWorldLoader.load(new File(""
						+ Constants.SAVE_PATH + "" + user + "."
						+ PlayerWorldLoader.suffix + ""));
				if (other == null) {
					player.getPacketSender().sendMessage(
							"Error - invalid command [cause=Could not find player by the name \""
									+ user + "\"].");
					return true;
				}
			}
			other.getCredentials().setRights(
					Byte.parseByte(cmd[cmd.length - 1].replace("+", "")));
			if (!other.isLoggedIn()) {
				final Player p = other;
				Main.getWorkingSet().submitLogic(new Runnable() {
					@Override
					public void run() {
						PlayerWorldLoader.store(p, new File("" + Constants.SAVE_PATH
								+ "" + p.getCredentials().getUsername() + "."
								+ PlayerWorldLoader.suffix + ""));
					}
				});
			} else {
				other.getPacketSender()
				.sendMessage("Your rights have changed.");
			}
			player.getPacketSender().sendMessage(
					"Set rights of user \"" + user + "\" to "
							+ other.getCredentials().getRights() + ".");
		} else if (cmd[0].equals("safepunishments")) {
			try {
				UserPunishHandler.onShutDown();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (cmd[0].equals("unban") || cmd[0].equals("unipban")
				|| cmd[0].equals("unmute") || cmd[0].equals("unipmute")) {
			String user = getCompleted(cmd, 1);
			boolean ipBased = cmd[0].contains("unip");
			String key = user;
			Player other = NodeWorker.getPlayer(key);
			if (ipBased) {
				if (other != null) {
					key = other.getCredentials().getLastAddress();
				} else {
					other = (Player) PlayerWorldLoader.load(new File(""
							+ Constants.SAVE_PATH + "" + key + "."
							+ PlayerWorldLoader.suffix + ""));
					if (other == null) {
						player.getPacketSender().sendMessage(
								"Error - invalid command [cause=Could not find player by the name \""
										+ user + "\"].");
						return true;
					}
					key = other.getCredentials().getLastAddress();
				}
			}
			UserPunishHandler.unflag(key,
					cmd[0].contains("ban") ? PunishType.BAN : PunishType.MUTE,
							ipBased);
			player.getPacketSender().sendMessage(
					"Succesfully removed punishment from user \"" + user
					+ "\".");
		} else if (cmd[0].equals("trade")) {
			World.getWorld().submit(new Tick(3) {
				int child = 0;

				@Override
				public boolean run() {
					player.getIOSession().write(
							new CS2Script(player, 150, "IviiiIsssssssss",
									new Object[] { "", "", "Lend",
									"Value<col=FF9040>", "Offer-X",
									"Offer-All", "Offer-10", "Offer-5",
									"Offer", -1, 0, 7, 4, 93,
									336 << 16 | child }));
					player.getIOSession().write(
							new AccessMask(player, 0, 27, 336, child, 0, 1278));
					player.getPacketSender().sendInventoryInterface(336);
					// player.getInventory().refresh();
					player.getIOSession().write(
							new MessagePacket(player, 99, "Currently tested child: "
									+ child));
					return true;
				}
			});
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Handles moderator commands.
	 * 
	 * @param player
	 *            The player.
	 * @param cmd
	 *            The command array.
	 * @return {@code True} if succesful.
	 */
	private static boolean handleModeratorCommand(Player player, String[] cmd) {
		if (cmd[0].equalsIgnoreCase("oinv")) {
			Player other = NodeWorker.getPlayer(cmd[1]);
			if (other != null) {
				player.getIOSession().write(new ContainerPacket(player, 93, other.getInventory(), false));
			}
		}
		if (cmd[0].equalsIgnoreCase("oinv")) {
			Player other = NodeWorker.getPlayer(cmd[1]);
			if (other != null) {
				player.getIOSession().write(new ContainerPacket(player, 93, other.getInventory(), false));
			}
		}
		if (cmd[0].equals("visual")) {
			player.visual(new Animation(Integer.parseInt(cmd[1]), 0, false),
					new Graphic(Integer.parseInt(cmd[2]), 0, 0, false));
		} else if (cmd[0].equals("pos")) {
			player.getIOSession().write(
					new MessagePacket(player, 99, player.getLocation() + ", ["
							+ player.getLocation().getRegionX() + ", "
							+ player.getLocation().getRegionY() + "], "
							+ player.getLocation().getRegion().hashCode()
							+ ", " + player.getLocation().getRegionID()));
		} else if (cmd[0].equals("rls")) {
			if (EventManager.reload()) {
				player.getIOSession().write(
						new MessagePacket(player,
								"Succesfully reloaded event manager!"));
			} else {
				player.getIOSession().write(
						new MessagePacket(player,
								"Exception caught while reloading events!"));
			}
		} else if (cmd[0].equals("kick")) {
			String user = getCompleted(cmd, 1);
			Player p = NodeWorker.getPlayer(user);
			if (p != null) {
				player.getPacketSender().sendMessage(
						"Succesfully kicked player \"" + user + "\".");
				p.getIOSession().write(new LogoutPacket(p, 0));
				return true;
			}
			player.getPacketSender().sendMessage(
					"Error - invalid command [cause=Could not find player by the name \""
							+ user + "\"].");
		} else if (cmd[0].equals("teleto")) {
			String user = getCompleted(cmd, 1);
			Player p = NodeWorker.getPlayer(user);
			if (p != null) {
				player.getProperties().setTeleportLocation(p.getLocation());
				return true;
			}
			player.getPacketSender().sendMessage(
					"Error - invalid command [cause=Could not find player by the name \""
							+ user + "\"].");
		} else if (cmd[0].equals("teletome")) {
			String user = getCompleted(cmd, 1);
			Player p = NodeWorker.getPlayer(user);
			if (p != null) {
				p.getProperties().setTeleportLocation(player.getLocation());
				return true;
			}
			player.getPacketSender().sendMessage(
					"Error - invalid command [cause=Could not find player by the name \""
							+ user + "\"].");
		} else if (cmd[0].equals("ban") || cmd[0].equals("ipban")
				|| cmd[0].equals("mute") || cmd[0].equals("ipmute")) {
			String user = getCompleted(cmd, 1);
			boolean ipBased = cmd[0].contains("ip");
			long end = System.currentTimeMillis();
			if (cmd[cmd.length - 1].startsWith("+")) {
				int hours = Integer.parseInt(cmd[cmd.length - 1].replace("+",
						""));
				if (hours < 0) {
					end = -1;
				} else {
					end += hours * 3600000;
				}
			} else {
				end += 48 * 3600000; // 2-days ban as default.
			}
			String key = user;
			Player other = NodeWorker.getPlayer(key);
			if (ipBased) {
				if (other != null) {
					key = other.getCredentials().getLastAddress();
				} else {
					other = (Player) PlayerWorldLoader.load(new File(""
							+ Constants.SAVE_PATH + "/" + key + "."
							+ PlayerWorldLoader.suffix + ""));
					if (other == null) {
						player.getPacketSender().sendMessage(
								"Error - invalid command [cause=Could not find player by the name \""
										+ user + "\"].");
						return true;
					}
					key = other.getCredentials().getLastAddress();
				}
			}
			int hours = (int) ((end - System.currentTimeMillis() + 50000) / 3600000);
			Punishment p = new Punishment(
					cmd[0].contains("ban") ? PunishType.BAN : PunishType.MUTE,
							end);
			UserPunishHandler.flag(key, p, ipBased);
			player.getPacketSender()
			.sendMessage(
					"Succesfully flagged user \""
							+ user
							+ "\" - punish: [type="
							+ p.getType()
							+ ", dur="
							+ (p.getDuration()
									- System.currentTimeMillis() + 50000)
									/ 3600000 + "h, ip=" + ipBased + "].");
			if (p.getType() == PunishType.BAN) {
				if (other != null) {
					if (ipBased) {
						int count = 0;
						for (Player pl : NodeWorker.getPlayers()) {
							if (pl != null
									&& pl.getCredentials().getLastAddress()
									.equals(key)) {
								count++;
								try {
									pl.getIOSession().write(
											new LogoutPacket(pl, 0));
								} catch (Throwable t) {
									// Empty
								}
							}
						}
						player.getPacketSender().sendMessage(
								"Disconnected " + count
								+ " users due to ip-ban punishment.");
					} else {
						other.getIOSession().write(new LogoutPacket(other, 0));
						player.getPacketSender().sendMessage(
								"Disconnected user \"" + user
								+ "\" due to ban punishment.");
					}
				}
				return true;
			}
			if (other != null) {
				if (ipBased) {
					for (Player pl : NodeWorker.getPlayers()) {
						if (pl != null
								&& pl.getCredentials().getLastAddress()
								.equals(key)) {
							pl.getPacketSender().sendMessage(
									"You have been muted by "
											+ player.getCredentials()
											.getUsername() + " for "
											+ hours + " hours.");
						}
					}
				} else {
					other.getPacketSender().sendMessage(
							"You have been muted by "
									+ player.getCredentials().getUsername()
									+ " for " + hours + " hours.");
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Gets the completed string.
	 * 
	 * @param cmd
	 *            The command.
	 * @param index
	 *            The start index.
	 * @return The string gained.
	 */
	private static String getCompleted(String[] cmd, int index) {
		StringBuilder sb = new StringBuilder();
		for (int i = index; i < cmd.length; i++) {
			if (i == cmd.length - 1 || cmd[i + 1].startsWith("+")) {
				return sb.append(cmd[i]).toString();
			}
			sb.append(cmd[i]).append(" ");
		}
		return "null";
	}
}
