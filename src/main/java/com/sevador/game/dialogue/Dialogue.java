package com.sevador.game.dialogue;

import com.sevador.game.node.player.Player;
import com.sevador.network.out.AnimateInterface;
import com.sevador.network.out.StringPacket;
import com.sevador.utility.Misc;

public abstract class Dialogue {

	public Dialogue() {
		stage = -1;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public abstract void start();

	public abstract void run(int interfaceId, int componentId);

	public abstract void finish();

	protected final void end() {
		player.getDialogueManager().finishDialogue();
	}

	private static int[] getIComponentsIds(short interId) {
		int childOptions[];
		switch (interId) {
		case 210:
			childOptions = new int[1];
			childOptions[0] = 1;
			break;

		case 211:
			childOptions = new int[2];
			childOptions[0] = 1;
			childOptions[1] = 2;
			break;

		case 212:
			childOptions = new int[3];
			childOptions[0] = 1;
			childOptions[1] = 2;
			childOptions[2] = 3;
			break;

		case 213:
			childOptions = new int[4];
			childOptions[0] = 1;
			childOptions[1] = 2;
			childOptions[2] = 3;
			childOptions[3] = 4;
			break;

		case 229:
			childOptions = new int[3];
			childOptions[0] = 1;
			childOptions[1] = 2;
			childOptions[2] = 3;
			break;

		case 231:
			childOptions = new int[4];
			childOptions[0] = 1;
			childOptions[1] = 2;
			childOptions[2] = 3;
			childOptions[3] = 4;
			break;

		case 236:
			childOptions = new int[3];
			childOptions[0] = 0;
			childOptions[1] = 1;
			childOptions[2] = 2;
			break;

		case 237:
			childOptions = new int[5];
			childOptions[0] = 0;
			childOptions[1] = 1;
			childOptions[2] = 2;
			childOptions[3] = 3;
			childOptions[4] = 4;
			break;

		case 238:
			childOptions = new int[6];
			childOptions[0] = 0;
			childOptions[1] = 1;
			childOptions[2] = 2;
			childOptions[3] = 3;
			childOptions[4] = 4;
			childOptions[5] = 5;
			break;

		case 241:
		case 245:
			childOptions = new int[2];
			childOptions[0] = 3;
			childOptions[1] = 4;
			break;

		case 242:
		case 246:
			childOptions = new int[3];
			childOptions[0] = 3;
			childOptions[1] = 4;
			childOptions[2] = 5;
			break;

		case 243:
		case 247:
			childOptions = new int[4];
			childOptions[0] = 3;
			childOptions[1] = 4;
			childOptions[2] = 5;
			childOptions[3] = 6;
			break;

		case 244:
		case 248:
			childOptions = new int[5];
			childOptions[0] = 3;
			childOptions[1] = 4;
			childOptions[2] = 5;
			childOptions[3] = 6;
			childOptions[4] = 7;
			break;

		case 214:
		case 215:
		case 216:
		case 217:
		case 218:
		case 219:
		case 220:
		case 221:
		case 222:
		case 223:
		case 224:
		case 225:
		case 226:
		case 227:
		case 228:
		case 230:
		case 232:
		case 233:
		case 234:
		case 235:
		case 239:
		case 240:
		default:
			return null;
		}
		return childOptions;
	}

	public boolean sendDialogue(short interId, String talkDefinitons[]) {
		int componentOptions[] = getIComponentsIds(interId);
		if (componentOptions == null)
			return false;
		player.getPacketSender().sendChatBoxInterface(interId);
		if (talkDefinitons.length != componentOptions.length)
			return false;
		for (int childOptionId = 0; childOptionId < componentOptions.length; childOptionId++)
			player.getIOSession().write(
					new StringPacket(player, talkDefinitons[childOptionId],
							interId, componentOptions[childOptionId]));
		return true;
	}

	public boolean sendEntityDialogue(short interId, String[] message,
			byte type, int entityId, int animationId) {
		if (entityId == 1) { entityId = -1; }
		player.getPacketSender().sendCloseInterface().sendCloseInventoryInterface().setDefaultInventory();
		int[] componentOptions = getIComponentsIds(interId);
		if (componentOptions == null)
			return false;
		if (message.length != componentOptions.length)
			return false;
		message[0] = Misc.formatPlayerNameForDisplay(message[0]);
		player.getPacketSender().sendChatBoxInterface(interId);
		for (int i = 0; i < componentOptions.length; i++)
			player.getIOSession().write(new StringPacket(player, message[i], interId, componentOptions[i]));
		player.getPacketSender().sendEntityOnInterface(entityId == -1, entityId, interId, 2);
		player.getIOSession().write(new AnimateInterface(player, interId, 2, animationId));
		return true;
	}

	/*
	 * if (dialogue.length == 0 || dialogue.length > 4) {
			return;
		}
		int interfaceId = (face == -1 ? 63 : 240) + dialogue.length;
		int index = 4;
		ActionSender.sendString(player, interfaceId, 3, face == -1 ? player.getUsername() : CacheNPCDefinition.forID(face).name);
		for (String s : dialogue) {
			ActionSender.sendString(player, interfaceId, index, s.replaceAll("@PLAYER_NAME@",player.getUsername()));
			index++;
		}
		ActionSender.sendChatboxInterface(player, interfaceId);
		ActionSender.sendEntityOnInterface(player, face == -1, face, interfaceId, 2);
		ActionSender.sendInterAnimation(player, anim, interfaceId, 2);
		player.setAttribute("nextDialougeStage", nextStage);
	 */

	protected Player player;
	protected byte stage;
	public Object parameters[];
	protected static final short SEND_1_TEXT_INFO = 210;
	protected static final short SEND_2_TEXT_INFO = 211;
	protected static final short SEND_3_TEXT_INFO = 212;
	protected static final short SEND_4_TEXT_INFO = 213;
	protected static final String SEND_DEFAULT_OPTIONS_TITLE = "Select an Option";
	protected static final short SEND_2_OPTIONS = 236;
	protected static final short SEND_3_OPTIONS = 235;
	protected static final short SEND_4_OPTIONS = 237;
	protected static final short SEND_5_OPTIONS = 238;
	protected static final short SEND_2_LARGE_OPTIONS = 229;
	protected static final short SEND_3_LARGE_OPTIONS = 231;
	protected static final short SEND_1_TEXT_CHAT = 241;
	protected static final short SEND_2_TEXT_CHAT = 242;
	protected static final short SEND_3_TEXT_CHAT = 243;
	protected static final short SEND_4_TEXT_CHAT = 244;
	protected static final short SEND_NO_CONTINUE_1_TEXT_CHAT = 245;
	protected static final short SEND_NO_CONTINUE_2_TEXT_CHAT = 246;
	protected static final short SEND_NO_CONTINUE_3_TEXT_CHAT = 247;
	protected static final short SEND_NO_CONTINUE_4_TEXT_CHAT = 248;
	protected static final short SEND_NO_EMOTE = -1;
	protected static final byte IS_NOTHING = -1;
	protected static final byte IS_PLAYER = 0;
	protected static final byte IS_NPC = 1;
	protected static final byte IS_ITEM = 2;
}
