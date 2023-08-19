package net.burtleburtle.cache.format;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import net.burtleburtle.cache.CacheConstants;
import net.burtleburtle.cache.CacheManager;
import net.burtleburtle.cache.stream.RSByteArrayInputStream;
import net.burtleburtle.cache.stream.RSInputStream;

public class ObjectDefinition {

	private static HashMap<Integer, ObjectDefinition> objDefs = new HashMap<Integer, ObjectDefinition>();

	private short[] originalColors;
	int[] childrenIds;
	static int anInt3832;
	int[] anIntArray3833 = null;
	int anInt3835;
	static int anInt3836;
	int anInt3838 = -1;
	boolean aBoolean3839;
	static int anInt3842;
	static int anInt3843;
	int anInt3844;
	boolean aBoolean3845;
	static int anInt3846;
	int anInt3850;
	int anInt3851;
	public boolean secondBool;
	public boolean aBoolean3853;
	int anInt3855;
	public boolean clippingFlag;
	int anInt3857;
	private byte[] aByteArray3858;
	int[] anIntArray3859;
	int anInt3860;
	public String[] options;
	int configFileId;
	private short[] modifiedColors;
	int anInt3865;
	boolean aBoolean3866;
	boolean aBoolean3867;
	private boolean solid;
	private int[] anIntArray3869;
	boolean aBoolean3870;
	public int sizeX;
	boolean aBoolean3872;
	boolean aBoolean3873;
	public int thirdInt;
	int anInt3876;
	public int actionCount;
	private int anInt3881;
	public int sizeY;
	public boolean aBoolean3891;
	int anInt3892;
	public int secondInt;
	boolean aBoolean3894;
	boolean aBoolean3895;
	int anInt3896;
	int configId;
	private byte[] aByteArray3899;
	int anInt3900;
	public String name;
	int anInt3904;
	int anInt3905;
	boolean aBoolean3906;
	int[] anIntArray3908;
	int anInt3913;
	private int[][] anIntArrayArray3916;
	private short[] aShortArray3919;
	private short[] aShortArray3920;
	int anInt3921;
	boolean aBoolean3923;
	boolean aBoolean3924;
	private int walkBitFlag;
	public int id;
	private byte aByte3912;

	private void readValues(RSInputStream stream, int opcode)
			throws IOException {
		if (opcode != 1 && opcode != 5) {
			if (opcode != 2) {
				if (opcode != 14) {
					if (opcode != 15) {
						if (opcode == 17) {
							solid = false;
							actionCount = 0;
						} else if (opcode != 18) {
							if (opcode == 19)
								secondInt = stream.readUnsignedByte();
							else if (opcode == 21)
								aByte3912 = (byte) 1;
							else if (opcode != 22) {
								if (opcode != 23) {
									if (opcode != 24) {
										if (opcode == 27)
											actionCount = 1;
										else if (opcode == 28)
											anInt3892 = (stream
													.readUnsignedByte() << 2);
										else if (opcode != 29) {
											if (opcode != 39) {
												if (opcode < 30 || opcode >= 35) {
													if (opcode == 40) {
														int i_53_ = (stream
																.readUnsignedByte());
														originalColors = new short[i_53_];
														modifiedColors = new short[i_53_];
														for (int i_54_ = 0; i_53_ > i_54_; i_54_++) {
															originalColors[i_54_] = (short) (stream
																	.readUnsignedShort());
															modifiedColors[i_54_] = (short) (stream
																	.readUnsignedShort());
														}
													} else if (opcode != 41) {
														if (opcode != 42) {
															if (opcode != 62) {
																if (opcode != 64) {
																	if (opcode == 65)
																		stream.readUnsignedShort();
																	else if (opcode != 66) {
																		if (opcode != 67) {
																			if (opcode == 69)
																				walkBitFlag = stream
																						.readUnsignedByte();
																			else if (opcode != 70) {
																				if (opcode == 71)
																					stream.readShort();
																				else if (opcode != 72) {
																					if (opcode == 73)
																						secondBool = true;
																					else if (opcode == 74)
																						clippingFlag = true;
																					else if (opcode != 75) {
																						if (opcode != 77
																								&& opcode != 92) {
																							if (opcode == 78) {
																								anInt3860 = stream
																										.readUnsignedShort();
																								anInt3904 = stream
																										.readUnsignedByte();
																							} else if (opcode != 79) {
																								if (opcode == 81) {
																									aByte3912 = (byte) 2;
																									stream.readUnsignedByte();
																								} else if (opcode != 82) {
																									if (opcode == 88)
																										aBoolean3853 = false;
																									else if (opcode != 89) {
																										if (opcode == 90)
																											aBoolean3870 = true;
																										else if (opcode != 91) {
																											if (opcode != 93) {
																												if (opcode == 94)
																													aByte3912 = (byte) 4;
																												else if (opcode != 95) {
																													if (opcode != 96) {
																														if (opcode == 97)
																															aBoolean3866 = true;
																														else if (opcode == 98)
																															aBoolean3923 = true;
																														else if (opcode == 99) {
																															anInt3857 = stream
																																	.readUnsignedByte();
																															anInt3835 = stream
																																	.readUnsignedShort();
																														} else if (opcode == 100) {
																															anInt3844 = stream
																																	.readUnsignedByte();
																															anInt3913 = stream
																																	.readUnsignedShort();
																														} else if (opcode != 101) {
																															if (opcode == 102)
																																anInt3838 = stream
																																		.readUnsignedShort();
																															else if (opcode == 103)
																																thirdInt = 0;
																															else if (opcode != 104) {
																																if (opcode == 105)
																																	aBoolean3906 = true;
																																else if (opcode == 106) {
																																	int i_55_ = stream
																																			.readUnsignedByte();
																																	anIntArray3869 = new int[i_55_];
																																	anIntArray3833 = new int[i_55_];
																																	for (int i_56_ = 0; i_56_ < i_55_; i_56_++) {
																																		anIntArray3833[i_56_] = stream
																																				.readUnsignedShort();
																																		int i_57_ = stream
																																				.readUnsignedByte();
																																		anIntArray3869[i_56_] = i_57_;
																																		anInt3881 += i_57_;
																																	}
																																} else if (opcode == 107)
																																	anInt3851 = stream
																																			.readUnsignedShort();
																																else if (opcode >= 150
																																		&& opcode < 155) {
																																	options[opcode - 150] = stream
																																			.readRS2String();
																																} else if (opcode != 160) {
																																	if (opcode == 162) {
																																		aByte3912 = (byte) 3;
																																		stream.readInt();
																																	} else if (opcode == 163) {
																																		stream.readByte();
																																		stream.readByte();
																																		stream.readByte();
																																		stream.readByte();
																																	} else if (opcode != 164) {
																																		if (opcode != 165) {
																																			if (opcode != 166) {
																																				if (opcode == 167)
																																					anInt3921 = stream
																																							.readUnsignedShort();
																																				else if (opcode != 168) {
																																					if (opcode == 169) {
																																						aBoolean3845 = true;
																																					} else if (opcode == 170) {
																																						stream.readUnsignedSmart();
																																					} else if (opcode == 171) {
																																						stream.readUnsignedSmart();
																																					} else if (opcode == 173) {
																																						stream.readUnsignedShort();
																																						stream.readUnsignedShort();
																																					} else if (opcode == 177) {
																																						// something
																																						// =
																																						// true
																																					} else if (opcode == 178) {
																																						stream.readUnsignedByte();
																																					} else if (opcode == 249) {
																																						int i_58_ = stream
																																								.readUnsignedByte();
																																						for (int i_60_ = 0; i_60_ < i_58_; i_60_++) {
																																							boolean bool = stream
																																									.readUnsignedByte() == 1;
																																							stream.read24BitInt();
																																							// int
																																							// length
																																							// =
																																							// stream.read24BitInt();
																																							if (!bool)
																																								stream.readInt();
																																							else
																																								stream.readRS2String();
																																						}
																																					}
																																				} else
																																					aBoolean3894 = true;
																																			} else
																																				stream.readShort();
																																		} else
																																			stream.readShort();
																																	} else
																																		stream.readShort();
																																} else {
																																	int i_62_ = stream
																																			.readUnsignedByte();
																																	anIntArray3908 = new int[i_62_];
																																	for (int i_63_ = 0; i_62_ > i_63_; i_63_++)
																																		anIntArray3908[i_63_] = stream
																																				.readUnsignedShort();
																																}
																															} else
																																anInt3865 = stream
																																		.readUnsignedByte();
																														} else
																															anInt3850 = stream
																																	.readUnsignedByte();
																													} else
																														aBoolean3924 = true;
																												} else {
																													aByte3912 = (byte) 5;
																													stream.readShort();
																												}
																											} else {
																												aByte3912 = (byte) 3;
																												stream.readUnsignedShort();
																											}
																										} else
																											aBoolean3873 = true;
																									} else
																										aBoolean3895 = false;
																								} else
																									aBoolean3891 = true;
																							} else {
																								anInt3900 = stream
																										.readUnsignedShort();
																								anInt3905 = stream
																										.readUnsignedShort();
																								anInt3904 = stream
																										.readUnsignedByte();
																								int i_64_ = stream
																										.readUnsignedByte();
																								anIntArray3859 = new int[i_64_];
																								for (int i_65_ = 0; i_65_ < i_64_; i_65_++)
																									anIntArray3859[i_65_] = stream
																											.readUnsignedShort();
																							}
																						} else {
																							configFileId = stream
																									.readUnsignedShort();
																							if (configFileId == 65535)
																								configFileId = -1;
																							configId = stream
																									.readUnsignedShort();
																							if (configId == 65535)
																								configId = -1;
																							int i_66_ = -1;
																							if (opcode == 92) {
																								i_66_ = stream
																										.readUnsignedShort();
																								if (i_66_ == 65535)
																									i_66_ = -1;
																							}
																							int i_67_ = stream
																									.readUnsignedByte();
																							childrenIds = new int[i_67_ + 2];
																							for (int i_68_ = 0; i_67_ >= i_68_; i_68_++) {
																								childrenIds[i_68_] = stream
																										.readUnsignedShort();
																								if (childrenIds[i_68_] == 65535)
																									childrenIds[i_68_] = -1;
																							}
																							childrenIds[i_67_ + 1] = i_66_;
																						}
																					} else
																						anInt3855 = stream
																								.readUnsignedByte();
																				} else
																					stream.readShort();
																			} else
																				stream.readShort();
																		} else
																			stream.readUnsignedShort();
																	} else
																		stream.readUnsignedShort();
																} else
																	aBoolean3872 = false;
															} else
																aBoolean3839 = true;
														} else {
															int i_69_ = stream
																	.readUnsignedByte();
															aByteArray3858 = new byte[i_69_];
															for (int i_70_ = 0; i_70_ < i_69_; i_70_++)
																aByteArray3858[i_70_] = stream
																		.readByte();
														}
													} else {
														int i_71_ = stream
																.readUnsignedByte();
														aShortArray3920 = new short[i_71_];
														aShortArray3919 = new short[i_71_];
														for (int i_72_ = 0; i_71_ > i_72_; i_72_++) {
															aShortArray3920[i_72_] = (short) stream
																	.readUnsignedShort();
															aShortArray3919[i_72_] = (short) stream
																	.readUnsignedShort();
														}
													}
												} else
													options[opcode - 30] = stream
															.readRS2String();
											} else
												stream.readByte();
										} else
											stream.readByte();
									} else {
										anInt3876 = stream.readUnsignedShort();
										if (anInt3876 == 65535)
											anInt3876 = -1;
									}
								} else
									thirdInt = 1;
							} else
								aBoolean3867 = true;
						} else
							solid = false;
					} else
						sizeX = stream.readUnsignedByte();
				} else
					sizeY = stream.readUnsignedByte();
			} else
				name = stream.readRS2String();
		} else {
			boolean aBoolean1162 = false;
			if (opcode == 5 && aBoolean1162)
				method3297(stream);
			int i_73_ = stream.readUnsignedByte();
			anIntArrayArray3916 = new int[i_73_][];
			aByteArray3899 = new byte[i_73_];
			for (int i_74_ = 0; i_74_ < i_73_; i_74_++) {
				aByteArray3899[i_74_] = stream.readByte();
				int i_75_ = stream.readUnsignedByte();
				anIntArrayArray3916[i_74_] = new int[i_75_];
				for (int i_76_ = 0; i_75_ > i_76_; i_76_++)
					anIntArrayArray3916[i_74_][i_76_] = stream
							.readUnsignedShort();
			}
			if (opcode == 5 && !aBoolean1162)
				method3297(stream);
		}
	}

	private void method3297(RSInputStream stream) throws IOException {
		int length = stream.readUnsignedByte();
		for (int index = 0; index < length; index++) {
			stream.skip(1);
			stream.skip(stream.readUnsignedByte() * 2);
		}
	}

	private void readValueLoop(RSInputStream stream) throws IOException {
		for (;;) {
			int opcode = stream.readByte() & 0xff;
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	private ObjectDefinition() {
		anInt3835 = -1;
		anInt3860 = -1;
		configFileId = -1;
		aBoolean3866 = false;
		anInt3851 = -1;
		anInt3865 = 255;
		aBoolean3845 = false;
		aBoolean3867 = false;
		anInt3850 = 0;
		anInt3844 = -1;
		anInt3881 = 0;
		anInt3857 = -1;
		aBoolean3872 = true;
		options = new String[5];
		aBoolean3839 = false;
		anIntArray3869 = null;
		sizeX = 1;
		thirdInt = -1;
		solid = true;
		aBoolean3895 = true;
		aBoolean3870 = false;
		aBoolean3853 = true;
		secondBool = false;
		actionCount = 2;
		anInt3855 = -1;
		anInt3904 = 0;
		sizeY = 1;
		anInt3876 = -1;
		clippingFlag = false;
		aBoolean3891 = false;
		anInt3905 = 0;
		name = "null";
		anInt3913 = -1;
		aBoolean3906 = false;
		aBoolean3873 = false;
		anInt3900 = 0;
		secondInt = -1;
		aBoolean3894 = false;
		aByte3912 = (byte) 0;
		anInt3921 = 0;
		configId = -1;
		walkBitFlag = 0;
		anInt3892 = 64;
		aBoolean3923 = false;
		aBoolean3924 = false;
	}

	final void method3287() {
		if (secondInt == -1) {
			secondInt = 0;
			if (aByteArray3899 != null && aByteArray3899.length == 1
					&& aByteArray3899[0] == 10)
				secondInt = 1;
			for (int i_13_ = 0; i_13_ < 5; i_13_++) {
				if (options[i_13_] != null) {
					secondInt = 1;
					break;
				}
			}
		}
		if (anInt3855 == -1)
			anInt3855 = actionCount != 0 ? 1 : 0;
	}

	public static ObjectDefinition forId(int objectID) {
		ObjectDefinition objectDef = objDefs.get(objectID);
		if (objectDef != null)
			return objectDef;
		byte[] is = null;
		try {
			is = (CacheManager.getData(CacheConstants.OBJECTDEF_IDX_ID,
					objectID >>> 8, objectID & 0xff));
		} catch (Exception e) {
			// System.out.println("Could not grab object " + objectID);
		}
		objectDef = new ObjectDefinition();
		objectDef.id = objectID;
		if (is != null) {
			try {
				objectDef.readValueLoop(new RSInputStream(
						new RSByteArrayInputStream(is)));
			} catch (IOException e) {
				System.out.println("Could not load object " + objectID);
			}
		}
		objectDef.method3287();
		if (objectDef.clippingFlag) {
			objectDef.solid = false;
			objectDef.actionCount = 0;
		}
		if (objectDef.name.contains("booth")) {
			objectDef.clippingFlag = false;
			objectDef.solid = true;
			objectDef.actionCount = 2;
		}
		objDefs.put(objectID, objectDef);
		return objectDef;
	}

	public byte getAByte3912() {
		return aByte3912;
	}

	public void test() {
		Object[] ids = new Object[] { anInt3835, anInt3860, aBoolean3866,
				anInt3851, anInt3865, aBoolean3845, aBoolean3867, anInt3850,
				anInt3844, anInt3881, anInt3857, aBoolean3872, aBoolean3839,
				anIntArray3869, thirdInt, aBoolean3895, aBoolean3870,
				aBoolean3853, secondBool, anInt3855, anInt3904, anInt3876,
				clippingFlag, aBoolean3891, anInt3905, anInt3913, aBoolean3906,
				aBoolean3873, anInt3900, secondInt, aBoolean3894, aByte3912,
				anInt3921, walkBitFlag, anInt3892, aBoolean3923, aBoolean3924 };
		System.out.println(Arrays.toString(ids));
	}

	public boolean isSolid() {
		return solid;
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public int getActionCount() {
		return actionCount;
	}

	public boolean isClippingFlag() {
		return clippingFlag;
	}

	public String getName() {
		return name;
	}

	public int getWalkBit() {
		return walkBitFlag;
	}

}
