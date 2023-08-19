package com.sevador.content.misc;

import com.sevador.game.node.player.Player;

/**
 * Handles a quick chat message.
 * @author Emperor
 */
public final class QuickChatUtils {

	/**
	 * Gets the quick message data.
	 * @param player The player.
	 * @param fileId The file id.
	 * @param data The data.
	 * @return The quick message data.
	 */
	public static byte[] getData(Player player, int fileId, byte data[]) {
		switch (fileId) {
		case 1:
			return new byte[] {(byte) player.getSkills().getStaticLevel(16)};
		case 8:
			return new byte[] {(byte) player.getSkills().getStaticLevel(0)};
		case 13:
			return new byte[] {(byte) player.getSkills().getStaticLevel(21)};
		case 16:
			return new byte[] {(byte) player.getSkills().getStaticLevel(7)};
		case 23:
			return new byte[] {(byte) player.getSkills().getStaticLevel(12)};
		case 30:
			return new byte[] {(byte) player.getSkills().getStaticLevel(1)};
		case 34:
			return new byte[] {(byte) player.getSkills().getStaticLevel(19)};
		case 41:
			return new byte[] {(byte) player.getSkills().getStaticLevel(11)};
		case 47:
			return new byte[] {(byte) player.getSkills().getStaticLevel(10)};
		case 55:
			return new byte[] {(byte) player.getSkills().getStaticLevel(9)};
		case 62:
			return new byte[] {(byte) player.getSkills().getStaticLevel(15)};
		case 70:
			return new byte[] {(byte) player.getSkills().getStaticLevel(3)};
		case 74:
			return new byte[] {(byte) player.getSkills().getStaticLevel(22)};
		case 135:
			return new byte[] {(byte) player.getSkills().getStaticLevel(6)};
		case 127:
			return new byte[] {(byte) player.getSkills().getStaticLevel(14)};
		case 120:
			return new byte[] {(byte) player.getSkills().getStaticLevel(5)};
		case 116:
			return new byte[] {(byte) player.getSkills().getStaticLevel(4)};
		case 111:
			return new byte[] {(byte) player.getSkills().getStaticLevel(20)};
		case 103:
			return new byte[] {(byte) player.getSkills().getStaticLevel(18)};
		case 96:
			return new byte[] {(byte) player.getSkills().getStaticLevel(13)};
		case 92:
			return new byte[] {(byte) player.getSkills().getStaticLevel(2)};
		case 85:
			return new byte[] {(byte) player.getSkills().getStaticLevel(23)};
		case 79:
			return new byte[] {(byte) player.getSkills().getStaticLevel(17)};
		case 142:
			return new byte[] {(byte) player.getSkills().getStaticLevel(8)};
		case 990:
			return new byte[] {(byte) player.getSkills().getStaticLevel(24)};
		case 965:
			int value = player.getSkills().getLifepoints();
			return new byte[] {(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value};
		case 547:
			value = /*player.getQuestListener().getQuestPoints();*/ 1337;
			return new byte[] {(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value};
		case 526: //Loyalty points
		case 611: //Stealing creation points
			value = 0;
			return new byte[] {(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value};
		default:
			System.out.println("Unhandled quick chat file id - " + fileId + ".");
			break;
		}
		return data;
	}
}