package com.sevador.game.node.player;

import java.util.List;

import net.burtleburtle.thread.NodeWorker;
import net.burtleburtle.tick.Tick;
import net.burtleburtle.tick.impl.AreaUpdateTick;

import com.sevador.Main;
import com.sevador.content.friendChat.FriendChatManager;
import com.sevador.content.misc.IconManager.Icon;
import com.sevador.content.quest.impl.CooksAssistant;
import com.sevador.game.event.button.SpellBookButtonEvent;
import com.sevador.game.node.ChatMessage;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.LocationGraphic;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.model.container.Container.Type;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.gameobject.ObjectBuilder;
import com.sevador.game.region.RegionManager;
import com.sevador.game.world.World;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.BuildSceneGraph;
import com.sevador.network.out.CS2Config;
import com.sevador.network.out.CS2Script;
import com.sevador.network.out.CloseInterface;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.ConstructObject;
import com.sevador.network.out.ConstructProjectile;
import com.sevador.network.out.ContainerPacket;
import com.sevador.network.out.InterfaceConfig;
import com.sevador.network.out.InterfacePacket;
import com.sevador.network.out.LobbyResponse;
import com.sevador.network.out.LoginResponse;
import com.sevador.network.out.MessagePacket;
import com.sevador.network.out.PlayerOptionPacket;
import com.sevador.network.out.PositionedGraphic;
import com.sevador.network.out.StringPacket;
import com.sevador.network.out.WindowsPane;
import com.sevador.utility.BufferUtils;
import com.sevador.utility.Constants;
import com.sevador.utility.Misc;

/**
 * An assisting class in sending several packets.
 * 
 * @author Emperor
 * 
 */
public class PacketSender {

	/**
	 * The frame index.
	 */
	private int frameIndex;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Constructs a new {@code PacketSender} {@code Object}.
	 * 
	 * @param player
	 *            The player.
	 */
	public PacketSender(Player player) {
		this.player = player;
	}

	/**
	 * Sends the login packets.
	 * 
	 * @return This ActionSender instance.
	 */
	public PacketSender sendLogin() {
		if (player.getIOSession().isInLobby()) {
			player.getIOSession().write(new LobbyResponse(player));
			player.getContacts().init();
			return this;
		}
		for (String[] a : Constants.STAFF) {
			if (player.getCredentials().getUsername().equalsIgnoreCase(a[0])) {
				player.getCredentials().setRights((byte) Integer.parseInt(a[1]));
			}
		}
		player.getIOSession().write(new LoginResponse(player));
		player.getIOSession().write(new BuildSceneGraph(player, true));
		sendLoginInterfaces();
		player.getSettings().refresh();
		((Skills) player.getSkills()).refresh();
		player.getEquipment().refresh();
		player.getInventory().refresh();
		if (player.getIOSession().getScreenSizeMode() > 1) {
			sendFullScreenAMasks();
		} else {
			sendFixedAMasks();
		}
		organizeSpells();
		player.getContacts().init();
		player.getPrayer().reset(player);
		player.getProperties().updateCursedModifiers(player, 0, 0, 0.25);
		player.getIOSession().write(new ConfigPacket(player, 1160, -1)); // summoning
																			// orb
		player.getIOSession().write(new ConfigPacket(player, 43, player.getSettings().getAttackBox()));
		player.getIOSession().write(new ConfigPacket(player, 1584, player.getPrayer().isCurses() ? 1 : 0));
		player.getIOSession().write(new CS2Script(player, 1297, ""));
		player.getIOSession().write(new CS2Script(player, 4717, "IIIg", 3874, 38666249, 38666247, 38666248));
		player.getIOSession().write(new CS2Script(player, 4704, ""));
		player.getIOSession().write(new CS2Script(player, 3336, ""));
		player.getIOSession().write(new PlayerOptionPacket(player, "Follow", false, 2));
		player.getIOSession().write(new PlayerOptionPacket(player, "Trade with", false, 3));
		player.getIOSession().write(new PlayerOptionPacket(player, "Req Assist", false, 4));
		player.getIOSession().write(new ConfigPacket(player, 300, player.getSettings().getSpecialEnergy() * 10));
		player.getIOSession().write(new ConfigPacket(player, 313, -1));// Emotes
		player.getIOSession().write(new ConfigPacket(player, 2230, -1));// Loyalty-program-Emotes
		player.getIOSession().write(new ConfigPacket(player, 465, -1));// Goblin-
		// Emotes
		player.getIOSession().write(new ConfigPacket(player, 802, -1));// Stronghold-Emotes
		player.getIOSession().write(new ConfigPacket(player, 1085, 12));// Zombie-Hand-
		// Emote
		player.getIOSession().write(new ConfigPacket(player, 2032, 7341));// Seal-of-approval
		player.getIOSession().write(new ConfigPacket(player, 2033, 1043648799));// Seal-of-
		// approval-req?
		player.getIOSession().write(new ConfigPacket(player, 1921, -893736236));// Puppet-master-emote
		sendBConfig(player, 768, 3);
		sendBConfig(player, 234, 0);
		sendBConfig(player, 181, 0);
		sendBConfig(player, 168, 4);
		sendBConfig(player, 695, 0);
		player.getIOSession().write(new InterfaceConfig(player, 34, 13, false));
		player.getIOSession().write(new InterfaceConfig(player, 34, 3, false));
		player.getIOSession().write(new ConfigPacket(player, 281, 1000));// Tutorial-completed-config
		player.getIOSession().write(new CS2Config(player, 168, 4));
		player.getIOSession().write(new CS2Config(player, 1273, 1));
		player.getIOSession().write(new CS2Config(player, 1000, 1));
		player.getIOSession().write(new CS2Config(player, 232, 0));
		player.getIOSession().write(new CS2Config(player, 233, 0));
		player.getIOSession().write(new CS2Config(player, 234, 0));
		player.getIOSession().write(new CS2Config(player, 1423, 44));// tasklist-total
		player.getIOSession().write(new CS2Config(player, 1424, 8));// tasklist-
		player.getIOSession().write(new CS2Config(player, 822, 0));
		player.getIOSession().write(new CS2Config(player, 181, 0));
		player.getIOSession().write(new CS2Config(player, 823, 0));
		player.getIOSession().write(new CS2Config(player, 1027, 1));
		player.getIOSession().write(new CS2Config(player, 1034, 2));
		player.getIOSession().write(new CS2Config(player, 245, 0));
		player.getIOSession().write(new CS2Config(player, 1000, 66));
		player.getIOSession().write(new CS2Config(player, 1428, 0));
		player.getIOSession().write(new CS2Config(player, 629, -1));
		player.getIOSession().write(new CS2Config(player, 630, -1));
		player.getIOSession().write(new CS2Config(player, 627, -1));
		player.getIOSession().write(new CS2Config(player, 628, -1));
		player.getIOSession().write(new CS2Config(player, 1416, 0));
		player.getIOSession().write(new CS2Config(player, 1469, 51));
		player.getIOSession().write(new CS2Config(player, 1470, 50));
		player.getIOSession().write(new CS2Config(player, 1471, 50));
		player.getIOSession().write(new CS2Config(player, 1472, 20));
		player.getIOSession().write(new CS2Config(player, 1473, 56));
		player.getIOSession().write(new CS2Config(player, 1474, 60));
		player.getIOSession().write(new CS2Config(player, 1475, 49));
		player.getIOSession().write(new CS2Config(player, 1476, 24));
		player.getIOSession().write(new CS2Config(player, 1477, 22));
		player.getIOSession().write(new CS2Config(player, 1478, 32));
		player.getIOSession().write(new CS2Config(player, 1479, 50));
		player.getIOSession().write(new CS2Config(player, 1480, 14));
		player.getIOSession().write(new CS2Config(player, 1481, 37));
		player.getIOSession().write(new CS2Config(player, 1482, 30));
		player.getIOSession().write(new CS2Config(player, 1483, 44));
		player.getIOSession().write(new CS2Config(player, 1484, 31));
		player.getIOSession().write(new CS2Config(player, 1485, 4));
		player.getIOSession().write(new CS2Config(player, 1486, 64));
		player.getIOSession().write(new CS2Config(player, 1487, 18));
		player.getIOSession().write(new CS2Config(player, 1488, 8));
		player.getIOSession().write(new CS2Config(player, 1489, 10));
		player.getIOSession().write(new CS2Config(player, 1490, 3));
		player.getIOSession().write(new CS2Config(player, 1491, 15));
		player.getIOSession().write(new CS2Config(player, 1492, 18));
		player.getIOSession().write(new CS2Config(player, 1493, 6));
		player.getIOSession().write(new CS2Config(player, 1000, 66));
		player.getIOSession().write(new CS2Config(player, 1413, 1));
		player.getIOSession().write(new CS2Config(player, 616, 1));
		player.getIOSession().write(new CS2Config(player, 1416, 0));
		player.getIOSession().write(new CS2Config(player, 695, 0));
		player.getIOSession().write(new CS2Config(player, 695, 0));
		sendConfig(29, (player.getQuestListener().start(CooksAssistant.ID).completed() ? 2 : (player.getQuestListener().start(CooksAssistant.ID).getProgress() == -1 ? 0 : -1)));// Cook's
																																													// Assistant
		sendConfig(1054, 0); // clan
		sendConfig(1055, 0); // assist
		sendConfig(1056, 0); // game
		sendPrivateChatSettings(player, 0);
		if (player.isMultiArea()) {
			player.getIOSession().write(new InterfaceConfig(player, 745, 3, true));
			player.getIOSession().write(new InterfaceConfig(player, 745, 6, true));
			player.getIOSession().write(new InterfaceConfig(player, 745, 1, true));
		}
		player.getIOSession().write(new InterfaceConfig(player, 34, 13, true));
		player.getIOSession().write(new InterfaceConfig(player, 34, 13, true));
		player.getIOSession().write(new InterfaceConfig(player, 34, 3, true));
		sendMessage("Welcome to " + Constants.SERVER_NAME + ".", false);
		ObjectBuilder.remove(new GameObject(47150, new Location(3163, 3490, 0)));
		player.getQuestListener().init();
		player.getPacketSender().sendMessage("Your experience is currently " + (player.getCredentials().isExperienceLocked() ? "locked" : "unlocked") + ", talk to Roddeck to switch!");
		long milliseconds = System.currentTimeMillis() - Main.UPTIME;
		int seconds = (int) (milliseconds / 1000) % 60;
		int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
		int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
		long days = milliseconds / (24 * 60 * 60 * 1000);
		player.getPacketSender().sendMessage("The server has been online for " + days + " days, " + hours + " hours, " + minutes + " minutes, and " + seconds + " seconds.", false);
		if (player.getCredentials().isDonator() || player.getCredentials().donatorTill != 0) {
			if (!player.getCredentials().isDonator() && player.getCredentials().donatorTill < Misc.currentTimeMillis())
				player.getPlayer();
			else
				sendMessage("Your donator rank expires " + player.getCredentials().getDonatorTill(), false);
		}
		World.getWorld().submit(new Tick(6) {
			@Override
			public boolean run() {
				player.getIOSession().write(new InterfaceConfig(player, 745, 1, true));
				Main.getWorkingSet().submitLogic(new AreaUpdateTick(player));
				return true;
			}
		});
		if (player.getFamiliar() != null)
			player.getFamiliar().init();
		player.getIOSession().write(new ConfigPacket(player, 1437, player.clanColor));
		player.getIOSession().write(new ConfigPacket(player, 1438, player.clanColor));
		player.getMusicsManager().playMusic(player.getLocation().getMusicId());
		FriendChatManager.getFriendChatManager().joinFriendChat(player, "tyluur");
		Item[] starterItems = { new Item(995, 500000), new Item(6099), new Item(1153), new Item(1115), new Item(1067), new Item(864, 500), new Item(1323), new Item(1712), new Item(3105), new Item(3842), };
		player.getControlerManager().login();
		if (!player.getSettings().hasReceivedStarter()) {
			for (Item item : starterItems) {
				if (item == null)
					continue;
				player.getInventory().add(item);
				((Skills) player.getSkills()).setStaticLevel(Skills.HERBLORE, 3);
				((Skills) player.getSkills()).refresh();
			}
			player.getSettings().setReceivedStarter(true);
		}
		if (player.getSettings().getExpMode() == 1)
			player.getDialogueManager().startDialogue("StartDialogue");
		return this;
	}

	public void sendReportPlayersOnline(final Player player) {
		sendOverlay(10);
		for (int i = 0; i < 3; i++)
			player.getIOSession().write(new StringPacket(player, "", 10, i));
		player.getIOSession().write(new StringPacket(player, "  Players  <col=FF000>    " + NodeWorker.getPlayers().size(), 10, 1));
	}

	public void sendSystemUpdate(Player p, double seconds) {
		long l = Math.round(seconds);
		int value = Math.round(l);
		OutgoingPacket bldr = new OutgoingPacket(p, 137);
		bldr.putShort(value);
		p.getIOSession().write(bldr);
	}

	public void sendItemKeptOnDeath(Player player) {
		player.getIOSession().write(new AccessMask(player, 211, 0, 2, 102, 18, 4));
		player.getIOSession().write(new AccessMask(player, 212, 0, 2, 102, 21, 42));
		Object[] params = new Object[] { 11510, 12749, "", 0, 0, -1, 4151, 15441, 15443, 3, 0 };
		player.getIOSession().write(new CS2Script(player, 118, "Iviiiiiiiiiiiissssssssssss", params));
		sendBConfig(player, 199, 442);
	}

	/**
	 * Sends an overlay interface.
	 * 
	 * @param childId
	 *            The child id.
	 * @return This action sender instance, for chaining.
	 */
	public PacketSender sendOverlay(int childId) {
		switch (player.getIOSession().getScreenSizeMode()) {
		case 0:
		case 1:
			player.getIOSession().write(new InterfacePacket(player, 548, 7, childId, true));
			return this;
		case 2:
		case 3:
			player.getIOSession().write(new InterfacePacket(player, 746, 7, childId, true));
			return this;
		}
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public PacketSender closeOverlay() {
		switch (player.getIOSession().getScreenSizeMode()) {
		case 0:
		case 1:
			player.getIOSession().write(new CloseInterface(player, 548, 7));
			player.getPacketSender().sendReportPlayersOnline(player);
			return this;
		case 2:
		case 3:
			player.getIOSession().write(new CloseInterface(player, 746, 7));
			player.getPacketSender().sendReportPlayersOnline(player);
			return this;
		}
		return this;
	}

	/**
	 * Sends the player's interface on login.
	 * 
	 * @return This action sender instance, for chaining.
	 */
	public PacketSender sendLoginInterfaces() {
		switch (player.getIOSession().getScreenSizeMode()) {
		case 0:
		case 1:
			player.getIOSession().write(new WindowsPane(player, 548, 0));
			player.getIOSession().write(new InterfacePacket(player, 548, 67, 751, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 192, 752, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 16, 754, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 182, 748, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 184, 749, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 185, 750, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 187, 747, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 14, 745, true));
			player.getIOSession().write(new InterfacePacket(player, 752, 9, 137, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 203, 884, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 205, 320, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 206, 190, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 204, 1056, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 207, 679, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 208, 387, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 209, 271, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 210, player.getSettings().getSpellBook(), true));
			player.getIOSession().write(new InterfacePacket(player, 548, 204, 1056, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 212, 550, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 213, 1109, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 214, 1110, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 215, 261, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 216, 590, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 217, 187, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 218, 34, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 221, 182, true));
			player.getIOSession().write(new InterfacePacket(player, 548, 203, 884, true));
			break;
		case 2:
		case 3:
			player.getIOSession().write(new WindowsPane(player, 746, 0));
			player.getIOSession().write(new InterfacePacket(player, 746, 18, 751, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 71, 752, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 72, 754, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 176, 748, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 177, 749, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 178, 750, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 179, 747, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 14, 745, true));
			player.getIOSession().write(new InterfacePacket(player, 752, 9, 137, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 89, 884, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 91, 320, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 92, 190, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 90, 1056, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 93, 679, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 94, 387, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 95, 271, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 96, player.getSettings().getSpellBook(), true));
			player.getIOSession().write(new InterfacePacket(player, 746, 90, 1056, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 98, 550, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 99, 1109, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 100, 1110, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 101, 261, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 102, 590, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 103, 187, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 104, 34, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 107, 182, true));
			player.getIOSession().write(new InterfacePacket(player, 746, 89, 884, true));
			break;
		}
		return this;
	}

	/**
	 * Sends all the fixed mode click options.
	 * 
	 * @return This action sender instance, for chaining.
	 */
	public PacketSender sendFixedAMasks() {
		player.getIOSession().write(new AccessMask(player, 0, 99, 137, 58, 0, 2046));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 129, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 884, 11, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 884, 12, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 884, 13, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 131, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 132, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 300, 190, 18, 0, 14));
		player.getIOSession().write(new AccessMask(player, 0, 11, 190, 15, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 130, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 133, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 27, 679, 0, 69, 0x457d8e));
		player.getIOSession().write(new AccessMask(player, 28, 55, 679, 0, 32, 0));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 134, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 135, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 30, 271, 8, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 136, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 99, 0, 0));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 130, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 100, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 101, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 600, 1109, 5, 0, 1024));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 102, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 200, 1110, 11, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 600, 1110, 16, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 600, 1110, 14, 0, 1024));
		player.getIOSession().write(new AccessMask(player, 0, 600, 1110, 5, 0, 1024));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 103, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 104, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 93, 590, 8, 0, 6));
		player.getIOSession().write(new AccessMask(player, 0, 11, 590, 13, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 105, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 2033, 187, 1, 0, 26));
		player.getIOSession().write(new AccessMask(player, 0, 11, 187, 9, 36, 6));
		player.getIOSession().write(new AccessMask(player, 12, 23, 187, 9, 0, 4));
		player.getIOSession().write(new AccessMask(player, 24, 24, 187, 9, 32, 0));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 106, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 29, 34, 9, 40, 30));
		player.getIOSession().write(new AccessMask(player, 0, 0, 747, 17, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 0, 662, 74, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 548, 129, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 884, 11, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 884, 12, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 884, 13, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 884, 14, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 0, 747, 17, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 0, 662, 74, 0, 2));
		return this;
	}

	/**
	 * Sends all the full-screen click options.
	 * 
	 * @return This action sender instance, for chaining.
	 */
	public PacketSender sendFullScreenAMasks() {
		player.getIOSession().write(new AccessMask(player, 0, 99, 137, 58, 0, 2046));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 39, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 884, 11, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 884, 12, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 884, 13, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 41, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 42, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 300, 190, 18, 0, 14));
		player.getIOSession().write(new AccessMask(player, 0, 11, 190, 15, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 40, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 43, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 27, 679, 0, 69, 0x457d8e));
		player.getIOSession().write(new AccessMask(player, 28, 55, 679, 0, 32, 0));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 44, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 45, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 30, 271, 8, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 46, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 47, 0, 0));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 40, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 48, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 49, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 600, 1109, 5, 0, 1024));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 50, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 200, 1110, 11, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 600, 1110, 16, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 600, 1110, 14, 0, 1024));
		player.getIOSession().write(new AccessMask(player, 0, 600, 1110, 5, 0, 1024));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 51, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 52, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 93, 590, 8, 0, 6));
		player.getIOSession().write(new AccessMask(player, 0, 11, 590, 13, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 53, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 2033, 187, 1, 0, 26));
		player.getIOSession().write(new AccessMask(player, 0, 11, 187, 9, 36, 6));
		player.getIOSession().write(new AccessMask(player, 12, 23, 187, 9, 0, 4));
		player.getIOSession().write(new AccessMask(player, 24, 24, 187, 9, 32, 0));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 54, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 29, 34, 9, 40, 30));
		player.getIOSession().write(new AccessMask(player, 0, 0, 747, 17, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 0, 662, 74, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 746, 39, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 884, 11, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 884, 12, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 884, 13, 0, 2));
		player.getIOSession().write(new AccessMask(player, -1, -1, 884, 14, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 0, 747, 17, 0, 2));
		player.getIOSession().write(new AccessMask(player, 0, 0, 662, 74, 0, 2));
		return this;
	}

	/**
	 * Sends a new chatbox interface.
	 * 
	 * @param id
	 *            The interface id.
	 * @return This action sender instance, for chaining.
	 */
	public PacketSender sendChatBoxInterface(int id) {
		if (id == 137) {
			player.getIOSession().write(new CloseInterface(player, 752, 13));
			return this;
		}
		player.getIOSession().write(new InterfacePacket(player, 752, 13, id, false));
		return this;
	}

	/**
	 * Sends a normal interface.
	 * 
	 * @param id
	 *            The interface id.
	 * @return This action sender instance, for chaining.
	 */
	public PacketSender sendInterface(int id) {
		switch (player.getIOSession().getScreenSizeMode()) {
		case 0:
		case 1:
			player.getIOSession().write(new InterfacePacket(player, 548, 18, id, false));
			player.setAttribute("interfaceId", id);
			return this;
		case 2:
		case 3:
			player.getIOSession().write(new InterfacePacket(player, 746, 11 /* 9 */, id, false));
			player.setAttribute("interfaceId", id);
			return this;
		}
		return this;
	}

	public PacketSender sendDuelOptions(Player p) {
		sendAMask(p, 1278, 631, 94, 0, 27); // brb food
		sendAMask(p, 1278, 628, 0, 0, 27);
		Object[] tparams1 = new Object[] { "", "", "", "", "Remove X", "Remove All", "Remove 10", "Remove 5", "Remove", 1, 0, 2, 2, 134, 631 << 16 | 94 };
		p.getIOSession().write(new CS2Script(p, 150, "IviiiIsssssssss", tparams1));
		Object[] tparams2 = new Object[] { "", "", "", "", "Stake X", "Stake All", "Stake 10", "Stake 5", "Stake", -1, 0, 7, 4, 93, 628 << 16 };
		p.getIOSession().write(new CS2Script(p, 150, "IviiiIsssssssss", tparams2));
		return this;
	}

	/**
	 * Organizes the player's spell book.
	 * 
	 * @return This SkillAction sender instance, for chaining.
	 */
	public PacketSender organizeSpells() {
		switch (player.getSettings().getSpellBook()) {
		case 193:
			player.getIOSession().write(new ConfigPacket(player, 439, 1));
			SpellBookButtonEvent.refreshAncient(player);
			return this;
		case 430:
			player.getIOSession().write(new ConfigPacket(player, 439, 2));
			SpellBookButtonEvent.refreshLunar(player);
			return this;
		default:
			player.getIOSession().write(new ConfigPacket(player, 439, 0));
			SpellBookButtonEvent.refreshModern(player);
			return this;
		}
	}

	/**
	 * Sends an inventory interface.
	 * 
	 * @param id
	 *            The interface id.
	 */
	public PacketSender sendInventoryInterface(int id) {
		switch (player.getIOSession().getScreenSizeMode()) {
		case 0:
		case 1:
			player.getIOSession().write(new InterfacePacket(player, 548, 198, id, false));
			return this;
		case 2:
		case 3:
			player.getIOSession().write(new InterfacePacket(player, 746, 86, id, false));
			return this;
		}
		return this;
	}

	/**
	 * Closes the current interface opened.
	 * 
	 * @return This action sender instance, for chaining.
	 */
	public PacketSender sendCloseInterface() {
		if (player.getPriceCheck().isOpen())
			player.getPriceCheck().close();
		switch (player.getIOSession().getScreenSizeMode()) {
		case 0:
		case 1:
			player.getIOSession().write(new CloseInterface(player, 548, 18));
			return this;
		case 2:
		case 3:
			player.getIOSession().write(new CloseInterface(player, 746, 11));
			return this;
		}
		return this;
	}

	/**
	 * Closes the current interface opened.
	 * 
	 * @return This action sender instance, for chaining.
	 */
	public PacketSender sendCloseInventoryInterface() {
		boolean fullscreen = player.getIOSession().getDisplayMode() == 2;
		player.getIOSession().write(new CloseInterface(player, fullscreen ? 746 : 548, fullscreen ? 84 : 198));
		return this;
	}

	/**
	 * Sets the inventory back to the default inventory tab.
	 * 
	 * @return This action sender instance, for chaining.
	 */
	public PacketSender setDefaultInventory() {
		boolean fullscreen = player.getIOSession().getDisplayMode() == 2;
		player.getIOSession().write(new CloseInterface(player, fullscreen ? 746 : 548, fullscreen ? 84 : 198));
		return this;
	}

	/**
	 * Gets the frame index and increments it.
	 * 
	 * @return The frameIndex.
	 */
	public int getFrameIndex() {
		return frameIndex++;
	}

	public static void rotateCamera(Player p, int x, int y, int z, int speed, int... arg) {
		OutgoingPacket bldr = new OutgoingPacket(p, 26);
		bldr.writeByte(y); // Y
		bldr.writeByte(x); // X
		bldr.putShort(z); // height
		bldr.writeByte(speed); // speed :S
		bldr.putByteS(arg.length > 0 ? arg[0] : 0);
		p.getIOSession().write(bldr);
	}

	public static void moveCamera(Player p, int x, int y, int height, int speed, int... arg) {
		OutgoingPacket bldr = new OutgoingPacket(p, 120);
		bldr.putShort(height >> 2); // Height
		bldr.putByteS(arg.length > 0 ? arg[0] : speed); // Speed
		bldr.writeByte(speed); // Speed
		bldr.putByteA(x); // X
		bldr.putByteC(y); // Y
		p.getIOSession().write(bldr);
	}

	public static void setCameraPosition(Player p, int... args) {

	}

	public static void resetCamera(Player player) {
		player.getIOSession().write(new OutgoingPacket(player, 125));
	}

	public static void shakeCamera(Player p, int speed) {
		OutgoingPacket bldr = new OutgoingPacket(p, 80);
		bldr.putShort(0); // sizeX (dont go over like 10 or so)
		bldr.putByteA(0); // speed to move
		bldr.putByteC(0);
		bldr.writeByte(0);
		bldr.putByteC(speed); // nothing?
		p.getIOSession().write(bldr);
	}

	public void sendIcon(Player p, Icon icon) {
		OutgoingPacket bldr = new OutgoingPacket(p, 116);
		bldr.writeByte(icon.getSlot() << 5 | icon.getTargetType());
		if (icon.getTargetType() > 0) {
			bldr.writeByte(icon.getArrowId());
			if (icon.getTargetType() == 1 || icon.getTargetType() == 10) {
				bldr.putShort(icon.getIndex());
				bldr.skip(6);
			}
			bldr.putShort(icon.getModelId());
		} else {
			bldr.skip(11);
		}
		p.getIOSession().write(bldr);
	}

	public void sendPublicChatMessage(Player player, int playerIndex, int rights, ChatMessage chat) {
		if (chat == null) {
			return;
		}
		OutgoingPacket bldr = new OutgoingPacket(player, 36, PacketType.VAR_BYTE);
		bldr.putShort(playerIndex);
		bldr.putShort(chat.getEffects());
		bldr.put(chat.isQuickChat() ? 0 : rights);
		if (chat.isQuickChat()) {
			bldr.putShort(chat.getFileId());
			if (chat.getData() != null) {
				bldr.putBytes(chat.getData());
			}
		} else {
			byte[] chatStr = new byte[256];
			chatStr[0] = (byte) chat.getChatText().length();
			int offset = 1 + BufferUtils.huffmanCompress(chat.getChatText(), chatStr, 1);
			bldr.putBytes(chatStr, 0, offset);
		}
		player.getIOSession().write(bldr);
	}

	/**
	 * Sends a list of projectiles.
	 * 
	 * @param projectiles
	 *            The projectiles.
	 */
	public static void sendProjectiles(Projectile... projectiles) {
		if (projectiles.length == 0 || projectiles[0] == null) {
			return;
		}
		List<Player> players = RegionManager.getLocalPlayers(projectiles[0].getSourceLocation());
		if (projectiles[0].getVictim() != null) {
			players.addAll(RegionManager.getLocalPlayers(projectiles[0].getVictim().getLocation()));
		}
		for (Player p : players) {
			for (Projectile proj : projectiles) {
				if (proj != null) {
					p.getIOSession().write(new ConstructProjectile(p, proj));
				}
			}
		}
	}

	/**
	 * Sends a list of positioned graphics.
	 * 
	 * @param graphic
	 *            The location graphic to send.
	 */
	public static void sendGraphics(LocationGraphic graphic) {
		for (Player p : RegionManager.getLocalPlayers(graphic.getLocation())) {
			p.getIOSession().write(new PositionedGraphic(p, graphic));
		}
	}

	/**
	 * Sends a new {@code MessagePacket} to the player.
	 * 
	 * @param string
	 *            The message.
	 */
	public void sendMessage(String string) {
		sendMessage(string, true);
	}

	/**
	 * Sends a new {@code MessagePacket} to the player.
	 * 
	 * @param string
	 *            The message.
	 * @param filter
	 *            The interface type, can be the console or the chat box, etc.
	 */
	public void sendMessage(String string, boolean filter) {
		player.getIOSession().write(new MessagePacket(player, filter ? 109 : 0, string));
	}

	/**
	 * Shows the Item Kept on Death Interface.
	 * 
	 * @param player
	 */
	public void sendIKOD(Player player) {
		final int[] ikod = new int[] { -1, -1, -1, -1 };
		Container container = new Container(Type.SHOP, 99);
		player.getPacketSender().sendInterface(102);
		player.getIOSession().write(new AccessMask(player, 211, 0, 2, 6684690, 4, 0));
		player.getIOSession().write(new AccessMask(player, 211, 0, 2, 6684693, 42, 0));
		player.getIOSession().write(new ContainerPacket(player, 93, container, false));
		player.getIOSession().write(new ContainerPacket(player, 93, player.getInventory(), false));
		final Object[] params = { 11510, 12749, "", 1, 3, ikod[3], ikod[2], ikod[1], ikod[0], /*
																							 * 0
																							 * -
																							 * 4
																							 */0, 0 };
		player.getIOSession().write(new CS2Script(player, 118, "Iiooooiisii", params));
	}

	public void sendIComponentText(int i, int j, String string) {
		player.getIOSession().write(new StringPacket(player, string, i, j));
	}

	public PacketSender sendConfig(int i, int j) {
		player.getIOSession().write(new ConfigPacket(player, i, j));
		return this;
	}

	public void sendDuelReq(Player player, String user, String message) {
		OutgoingPacket bldr = new OutgoingPacket(player, 98, PacketType.VAR_BYTE);
		bldr.writeByte(101);
		bldr.putInt(0);
		bldr.writeByte(0x1);
		bldr.putRS2String(user);
		bldr.putRS2String(message);
		player.getIOSession().write(bldr);
	}

	public void sendAMask(Player player, int set, int interfaceId, int childId, int off, int len) {
		player.getIOSession().write(new OutgoingPacket(player, 42).putLEShort(off).putInt1(interfaceId << 16 | childId).putLEShort(len));
	}

	public void sendInterfaceScript(Player player, Object... args) {
		OutgoingPacket message = new OutgoingPacket(player, 38);
		StringBuilder types = new StringBuilder();
		for (int i = 1; i < args.length; i++) {
			Object arg = args[i];
			if (arg instanceof Number) {
				types.append('i');
			} else {
				types.append('s');
			}
		}
		message.putRS2String(types.toString());
		for (int i = args.length - 1; i >= 0; i--) {
			Object arg = args[i];
			if (arg instanceof Number) {
				message.putInt(((Number) arg).intValue());
			} else {
				message.putRS2String(arg.toString());
			}
		}
		player.getIOSession().write(message);
	}

	public void sendWindowPane(Player player, int pane, int subWindowsId) {
		OutgoingPacket bldr = new OutgoingPacket(player, 100);
		bldr.putByteA(subWindowsId);
		bldr.putLEShortA(pane);
		player.getIOSession().write(bldr);
	}

	public void sendBConfig(Player player, int id, int value) {
		OutgoingPacket bldr;
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			bldr = new OutgoingPacket(player, 37).putInt2(value).putLEShort(id);
		} else {
			bldr = new OutgoingPacket(player, 49).putShortA(id).putByteC(value);
		}
		player.getIOSession().write(bldr);
	}

	public void sendModelOnInterface(Player player, int interfaceId, int childId, int modelId) {
		player.getIOSession().write(new OutgoingPacket(player, 22).put(interfaceId << 16 | childId).putLEShort(modelId));
	}

	public void sendObject(Player player, GameObject object) {
		sendObject(player, object.getId(), object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getZ(), object.getType(), object.getRotation());
	}

	public static void sendObject(Player player, int objectId, int x, int y, int z, int type, int rotation) {
		player.getIOSession().write(new ConstructObject(player, new GameObject(objectId, Location.locate(x, y, z))));
	}

	public void sendEntityOnInterface(boolean isPlayer, int entityId, int interId, int childId) {
		if (isPlayer)
			player.getPacketSender().sendPlayerOnInterface(interId, childId); // ...
																				// oh
																				// sorry
																				// :p
		else
			player.getPacketSender().sendNpcOnInterface(interId, childId, entityId);
	}

	public void sendPlayerOnInterface(int interId, int childId) {
		OutgoingPacket bldr = new OutgoingPacket(player, 48);
		bldr.putInt1(interId << 16 | childId);
		player.getIOSession().write(bldr);
	}

	public void sendNpcOnInterface(int interId, int childId, int npcId) {
		OutgoingPacket bldr = new OutgoingPacket(player, 136);
		bldr.putInt(interId << 16 | childId);
		bldr.putLEShortA(npcId);
		player.getIOSession().write(bldr);
	}

	public void sendPrivateChatSettings(Player player, int setting) {
		player.getIOSession().write(new ConfigPacket(player, 2159, setting));
		player.getIOSession().write(new OutgoingPacket(player, 104).put(setting));
	}

}
