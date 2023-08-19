package net.burtleburtle.cache.format;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.cache.CacheConstants;
import net.burtleburtle.cache.CacheManager;
import net.burtleburtle.cache.stream.RSInputStream;

import com.sevador.game.misc.ShopManager;
import com.sevador.game.node.npc.NPCCombatDefinitions;
import com.sevador.utility.BufferUtils;
import com.sevador.utility.loaders.NPCCombatDefinitionsL;

/**
 * Represents an NPC's definitions.
 * 
 * @author <b>Jagex</b> - <code>Most of the class in client</code>
 * @author <b>'Mystic Flow</b> - <code>Converting the client's class</code>
 * @author <b>Emperor</b> - <code>Custom NPC definitions</code>
 * @author <b>Tyluur</b> - <code>NPC Combat Definitions implementation</code>
 * 
 */
@SuppressWarnings("unused")
public final class NPCDefinition {

	/**
	 * The NPC definitions mapping.
	 */
	private static final HashMap<Integer, NPCDefinition> NPC_DEFINITIONS = new HashMap<Integer, NPCDefinition>();

	int id;
	int anInt3164 = -1;
	private short[] aShortArray3166;
	int anInt3167;
	boolean aBoolean3169;
	int configFileId;
	boolean aBoolean3172;
	int anInt3173;
	int anInt3178 = -1;
	int anInt3179;
	String[] options;
	int anInt3181;
	int anInt3182;
	private short[] aShortArray3183;
	int anInt3184;
	boolean aBoolean3187;
	boolean aBoolean3190;
	boolean isVisibleOnMap;
	private int[] anIntArray3192;
	byte aByte3193;
	Object aClass183_3195;
	boolean aBoolean3196;
	int anInt3200;
	private short[] aShortArray3201;
	int[] childrenIds;
	int anInt3203;
	private short[] aShortArray3204;
	private byte[] aByteArray3205;
	public int renderEmote;
	byte aByte3207;
	int configId;
	int anInt3209;
	boolean aBoolean3210;
	int anInt3212;
	short aShort3213;
	int anInt3214;
	byte aByte3215;
	int anInt3216;
	public String name;
	public int combatLevel;
	int[] anIntArray3219;
	boolean isClickable;
	int headIcons;
	int anInt3223;
	public byte direction;
	int anInt3226;
	int anInt3227;
	private int[] anIntArray3230;
	byte aByte3233;
	int anInt3235;
	public int size;
	short aShort3237;

	/**
	 * The NPCs bonuses.
	 */
	private int[] bonuses;

	/**
	 * The NPCs examine info.
	 */
	private String examine;

	/**
	 * The NPCs lifepoints.
	 */
	private int lifepoints;

	/**
	 * The NPCs respawn time.
	 */
	private int respawn;

	/**
	 * The NPCs attack animation.
	 */
	private int attackAnimation;

	/**
	 * The NPCs defence animation.
	 */
	private int defenceAnimation;

	/**
	 * The NPCs death animation.
	 */
	private int deathAnimation;

	/**
	 * The NPCs strength level.
	 */
	private int strengthLevel;

	/**
	 * The NPCs attack level.
	 */
	private int attackLevel;

	/**
	 * The NPCs defence level.
	 */
	private int defenceLevel;

	/**
	 * The NPCs range level.
	 */
	private int rangeLevel;

	/**
	 * The NPCs magic level.
	 */
	private int magicLevel;

	/**
	 * The NPCs attack speed.
	 */
	private int attackSpeed;

	/**
	 * The start graphics (Magic-Range)
	 */
	private int startGraphics;

	/**
	 * The projectile graphics id. (Magic-Range)
	 */
	private int projectileId;

	/**
	 * The end graphics (Magic-Range)
	 */
	private int endGraphics;

	/**
	 * If the NPC is using melee.
	 */
	private boolean usingMelee;

	/**
	 * If the NPC is using range.
	 */
	private boolean usingRange;

	/**
	 * If the NPC is using magic.
	 */
	private boolean usingMagic;

	/**
	 * If the NPC is immune to poison.
	 */
	private boolean poisonImmune;

	/**
	 * The NPC combat definitions instances
	 */

	public NPCCombatDefinitions getCombatDefinitions() {
		return NPCCombatDefinitionsL.getNPCCombatDefinitions(id);
	}

	/**
	 * If the NPC is aggressive.
	 */
	private boolean aggressive;

	/**
	 * Initializes the NPC definitions.
	 */
	public static void init() {
		try {
			FileChannel channel = new RandomAccessFile(
					"data/nodes/npcs/NPCDefinitions.bin", "r").getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
					channel.size());
			NPCDefinition def = null;
			for (int i = 0; i < Cache.getAmountOfNpcs(); i++) {
				int id = buffer.getShort();
				def = forId(i);
				if (id == -1) {
					continue;
				}
				def.combatLevel = buffer.getShort();
				def.examine = BufferUtils.readRS2String(buffer);
				for (int x = 0; x < 14; x++) {
					def.bonuses[x] = buffer.getShort();
				}
				def.lifepoints = buffer.getShort();
				def.respawn = buffer.get();
				def.attackAnimation = buffer.getShort();
				def.defenceAnimation = buffer.getShort();
				def.deathAnimation = buffer.getShort();
				def.strengthLevel = buffer.getShort();
				def.attackLevel = buffer.getShort();
				def.defenceLevel = buffer.getShort();
				def.rangeLevel = buffer.getShort();
				def.magicLevel = buffer.getShort();
				def.attackSpeed = buffer.get();
				def.startGraphics = buffer.getShort();
				def.projectileId = buffer.getShort();
				def.endGraphics = buffer.getShort();
				def.usingMelee = buffer.get() == 1;
				def.usingRange = buffer.get() == 1;
				def.usingMagic = buffer.get() == 1;
				def.aggressive = buffer.get() == 1;
				def.poisonImmune = buffer.get() == 1;
			}
			channel.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		for (int i : ShopManager.getShopOwners().keySet()) {
			forId(i & 0xFFFF).combatLevel = 0;
		}
	}

	/**
	 * Gets the NPC definitions for the given NPC id.
	 * 
	 * @param npcId
	 *            The npc id.
	 * @return The definitions.
	 */
	public static NPCDefinition forId(int npcId) {
		NPCDefinition def = NPC_DEFINITIONS.get(npcId);
		if (def != null)
			return def;
		byte[] is = null;
		try {
			is = CacheManager.getData(CacheConstants.NPCDEF_IDX_ID,
					npcId >>> 7, npcId & 0x7f);
		} catch (Exception e) {
			// System.out.println("Could not grab Npc " + npcID);
		}
		def = new NPCDefinition();
		def.id = npcId;
		if (is != null) {
			try {
				def.loadSettings(new RSInputStream(new ByteArrayInputStream(is)));
			} catch (IOException e) {
				System.out.println("Could not load Npc " + npcId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		NPC_DEFINITIONS.put(npcId, def);
		return def;
	}

	/**
	 * Loads this NPC definition's settings.
	 * 
	 * @param stream
	 *            The stream to read from.
	 * @throws Exception
	 *             When an exception occurs.
	 */
	private void loadSettings(RSInputStream stream) throws Exception {
		for (;;) {
			int opcode = stream.readByte() & 0xFF;
			if (opcode == 0) {
				break;
			}
			readOpcode(opcode, stream);
		}
	}

	/**
	 * Reads the data for a certain opcode.
	 * 
	 * @param opcode
	 *            The opcode.
	 * @param stream
	 *            The stream to read from.
	 * @throws Exception
	 *             When an exception occurs.
	 */
	private void readOpcode(int opcode, RSInputStream stream) throws Exception {
		switch (opcode) {
		case 1:
			int i_21_ = stream.readUnsignedByte();
			anIntArray3230 = new int[i_21_];
			for (int i_22_ = 0; i_21_ > i_22_; i_22_++) {
				anIntArray3230[i_22_] = stream.readUnsignedShort();
				if (anIntArray3230[i_22_] == 65535) {
					anIntArray3230[i_22_] = -1;
				}
			}
			return;
		case 2:
			name = stream.readRS2String();
			return;
		case 12:
			size = stream.readUnsignedByte();
			return;
		case 30:
		case 31:
		case 32:
		case 33:
		case 34:
			options[opcode - 30] = stream.readRS2String();
			return;
		case 40:
			int i_1_ = stream.readUnsignedByte();
			aShortArray3166 = new short[i_1_];
			aShortArray3201 = new short[i_1_];
			for (int i_2_ = 0; i_2_ < i_1_; i_2_++) {
				aShortArray3201[i_2_] = (short) stream.readUnsignedShort();
				aShortArray3166[i_2_] = (short) stream.readUnsignedShort();
			}
			return;
		case 41:
			int i_19_ = stream.readUnsignedByte();
			aShortArray3183 = new short[i_19_];
			aShortArray3204 = new short[i_19_];
			for (int i_20_ = 0; i_20_ < i_19_; i_20_++) {
				aShortArray3183[i_20_] = (short) stream.readUnsignedShort();
				aShortArray3204[i_20_] = (short) stream.readUnsignedShort();
			}
			return;
		case 42:
			int i_17_ = stream.readUnsignedByte();
			aByteArray3205 = new byte[i_17_];
			for (int i_18_ = 0; i_18_ < i_17_; i_18_++) {
				aByteArray3205[i_18_] = stream.readByte();
			}
			return;
		case 60:
			int i_15_ = stream.readUnsignedByte();
			anIntArray3192 = new int[i_15_];
			for (int i_16_ = 0; i_16_ < i_15_; i_16_++) {
				anIntArray3192[i_16_] = stream.readUnsignedShort();
			}
			return;
		case 93:
			isVisibleOnMap = false;
			return;
		case 95:
			combatLevel = stream.readUnsignedShort();
			return;
		case 97:
		case 98:
			stream.readUnsignedShort();
			return;
		case 99:
			aBoolean3210 = true;
			return;
		case 102:
			headIcons = stream.readUnsignedShort();
			return;
		case 103:
			anInt3235 = stream.readUnsignedShort();
			return;
		case 106:
		case 118:
			configFileId = stream.readUnsignedShort();
			if (configFileId == 65535)
				configFileId = -1;
			configId = stream.readUnsignedShort();
			if (configId == 65535)
				configId = -1;
			int i_12_ = -1;
			if (opcode == 118) {
				i_12_ = stream.readUnsignedShort();
				if (i_12_ == 65535)
					i_12_ = -1;
			}
			int i_13_ = stream.readUnsignedByte();
			childrenIds = new int[2 + i_13_];
			for (int i_14_ = 0; i_14_ <= i_13_; i_14_++) {
				childrenIds[i_14_] = stream.readUnsignedShort();
				if ((childrenIds[i_14_]) == 65535)
					childrenIds[i_14_] = -1;
			}
			childrenIds[1 + i_13_] = i_12_;
			return;
		case 107:
			isClickable = false;
			return;
		case 109:
			aBoolean3169 = false;
			return;
		case 111:
			aBoolean3172 = false;
			return;
		case 113:
			aShort3213 = (short) stream.readUnsignedShort();
			aShort3237 = (short) stream.readUnsignedShort();
			return;
		case 114:
			aByte3215 = stream.readByte();
			aByte3193 = stream.readByte();
			return;
		case 119:
			aByte3207 = stream.readByte();
			return;
		case 121:
			int i_9_ = stream.readUnsignedByte();
			for (int i_10_ = 0; (i_9_ > i_10_); i_10_++) {
				stream.readUnsignedByte();
				int[] is = new int[3];
				is[0] = stream.readByte();
				is[1] = stream.readByte();
				is[2] = stream.readByte();
			}
			return;
		case 122:
			anInt3182 = stream.readUnsignedShort();
			return;
		case 123:
			anInt3203 = stream.readUnsignedShort();
			return;
		case 125:
			direction = stream.readByte();
			return;
		case 127:
			renderEmote = stream.readUnsignedShort();
			return;
		case 134:
			anInt3173 = stream.readUnsignedShort();
			if (anInt3173 == 65535)
				anInt3173 = -1;
			anInt3212 = stream.readUnsignedShort();
			if (anInt3212 == 65535)
				anInt3212 = -1;
			anInt3226 = stream.readUnsignedShort();
			if (anInt3226 == 65535)
				anInt3226 = -1;
			anInt3179 = stream.readUnsignedShort();
			if (anInt3179 == 65535)
				anInt3179 = -1;
			anInt3184 = stream.readUnsignedByte();
			return;
		case 135:
			anInt3214 = stream.readUnsignedByte();
			anInt3178 = stream.readUnsignedShort();
			return;
		case 136:
			anInt3181 = stream.readUnsignedByte();
			anInt3227 = stream.readUnsignedShort();
			return;
		case 137:
			anInt3223 = stream.readUnsignedShort();
			return;
		case 138:
			anInt3167 = stream.readUnsignedShort();
			return;
		case 139:
			anInt3164 = stream.readUnsignedShort();
			return;
		case 140:
			anInt3216 = stream.readUnsignedByte();
			return;
		case 141:
			aBoolean3187 = true;
			return;
		case 142:
			anInt3200 = stream.readUnsignedShort();
			return;
		case 143:
			aBoolean3196 = true;
			return;
		case 150:
		case 151:
		case 152:
		case 153:
		case 154:
			options[opcode - 150] = stream.readRS2String();
			return;
		case 155:
			stream.readByte();
			stream.readByte();
			stream.readByte();
			stream.readByte();
			return;
		case 158:
			aByte3233 = (byte) 1;
			return;
		case 159:
			aByte3233 = (byte) 0;
			return;
		case 160:
			int i_7_ = stream.readUnsignedByte();
			anIntArray3219 = new int[i_7_];
			for (int i_8_ = 0; i_7_ > i_8_; i_8_++)
				anIntArray3219[i_8_] = stream.readUnsignedShort();
			return;
		case 161:
			aBoolean3190 = true;
			return;
		case 249:
			int i_3_ = stream.readUnsignedByte();
			for (int i_5_ = 0; i_3_ > i_5_; i_5_++) {
				boolean bool = stream.readUnsignedByte() == 1;
				stream.read24BitInt();
				if (!bool)
					stream.readInt();
				else
					stream.readRS2String();
			}
			return;
		case 100:
		case 101:
		case 128:
		case 163:
		case 165:
		case 168:
			stream.readByte();
			return;
		case 164:
			stream.readShort();
			stream.readShort();
			return;
		}
	}

	/**
	 * Constructs a new {@code NPCDefinition} {@code Object}.
	 */
	private NPCDefinition() {
		aBoolean3172 = true;
		options = new String[5];
		aBoolean3169 = true;
		aBoolean3196 = false;
		anInt3200 = -1;
		anInt3173 = -1;
		anInt3167 = -1;
		anInt3179 = -1;
		renderEmote = -1;
		aByte3193 = (byte) -16;
		anInt3184 = 0;
		anInt3214 = -1;
		aBoolean3210 = false;
		aByte3207 = (byte) 0;
		aByte3215 = (byte) -96;
		anInt3181 = -1;
		configId = -1;
		anInt3216 = 255;
		isVisibleOnMap = true;
		anInt3212 = -1;
		anInt3226 = -1;
		anInt3203 = -1;
		aBoolean3187 = false;
		anInt3227 = -1;
		anInt3223 = -1;
		direction = (byte) 4;
		configFileId = -1;
		combatLevel = -1;
		isClickable = true;
		headIcons = -1;
		anInt3235 = 32;
		aShort3213 = (short) 0;
		aByte3233 = (byte) -1;
		name = "null";
		anInt3182 = -1;
		size = 1;
		aShort3237 = (short) 0;
		examine = "It's a monster.";
		bonuses = new int[14];
		lifepoints = 100;
		attackSpeed = 5;
		respawn = 16;
		attackAnimation = -1;
		defenceAnimation = -1;
		deathAnimation = 836;
		attackLevel = 1;
		strengthLevel = 1;
		defenceLevel = 1;
		rangeLevel = 1;
		magicLevel = 1;
	}

	/**
	 * Gets the NPC's headicon.
	 * 
	 * @return The head icon id.
	 */
	public int getHeadIcon() {
		return headIcons;
	}

	/**
	 * Prints unidentified definitions.
	 */
	public void printUnidentified() {
		System.out.println(""+new StringBuilder("int3200: ").append(anInt3200)
				.append(", anInt3173: ").append(anInt3173)
				.append(", anInt3167: ").append(anInt3167)
				.append(", anInt3179: ").append(anInt3179)
				.append(", anInt3214: ").append(anInt3214)
				.append(", anInt3181: ").append(anInt3181)
				.append(", anInt3212: ").append(anInt3212)
				.append(", anInt3226: ").append(anInt3226)
				.append(", anInt3203: ").append(anInt3203)
				.append(", anInt3227: ").append(anInt3227)
				.append(", anInt3223: ").append(anInt3223)
				.append(", anInt3182: ").append(anInt3182));
	}

	/**
	 * Gets the name of the NPC.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the bonuses.
	 * 
	 * @return The bonuses.
	 */
	public int[] getBonuses() {
		return bonuses;
	}

	/**
	 * Sets the bonuses.
	 * 
	 * @param bonuses
	 *            The bonuses to set.
	 */
	public void setBonuses(int[] bonuses) {
		this.bonuses = bonuses;
	}

	/**
	 * Gets the examine.
	 * 
	 * @return The examine.
	 */
	public String getExamine() {
		return examine;
	}

	/**
	 * Sets the examine.
	 * 
	 * @param examine
	 *            The examine to set.
	 */
	public void setExamine(String examine) {
		this.examine = examine;
	}

	/**
	 * Gets the lifepoints.
	 * 
	 * @return The lifepoints.
	 */
	public int getLifepoints() {
		return lifepoints;
	}

	/**
	 * Sets the lifepoints.
	 * 
	 * @param lifepoints
	 *            The lifepoints to set.
	 */
	public void setLifepoints(int lifepoints) {
		this.lifepoints = lifepoints;
	}

	/**
	 * Gets the respawn.
	 * 
	 * @return The respawn.
	 */
	public int getRespawn() {
		return respawn;
	}

	/**
	 * Sets the respawn.
	 * 
	 * @param respawn
	 *            The respawn to set.
	 */
	public void setRespawn(int respawn) {
		this.respawn = respawn;
	}

	/**
	 * Gets the attackAnimation.
	 * 
	 * @return The attackAnimation.
	 */
	public int getAttackAnimation() {
		return getCombatDefinitions().getAttackEmote();
	}

	/**
	 * Sets the attackAnimation.
	 * 
	 * @param attackAnimation
	 *            The attackAnimation to set.
	 */
	public void setAttackAnimation(int attackAnimation) {
		this.attackAnimation = attackAnimation;
	}

	/**
	 * Gets the defenceAnimation.
	 * 
	 * @return The defenceAnimation.
	 */
	public int getDefenceAnimation() {
		return this.getCombatDefinitions().getDefenceEmote();
	}

	/**
	 * Sets the defenceAnimation.
	 * 
	 * @param defenceAnimation
	 *            The defenceAnimation to set.
	 */
	public void setDefenceAnimation(int defenceAnimation) {
		this.defenceAnimation = defenceAnimation;
	}

	/**
	 * Gets the deathAnimation.
	 * 
	 * @return The deathAnimation.
	 */
	public int getDeathAnimation() {
		return this.getCombatDefinitions().getDeathEmote();
	}

	/**
	 * Sets the deathAnimation.
	 * 
	 * @param deathAnimation
	 *            The deathAnimation to set.
	 */
	public void setDeathAnimation(int deathAnimation) {
		this.deathAnimation = deathAnimation;
	}

	/**
	 * Gets the strengthLevel.
	 * 
	 * @return The strengthLevel.
	 */
	public int getStrengthLevel() {
		return strengthLevel;
	}

	/**
	 * Sets the strengthLevel.
	 * 
	 * @param strengthLevel
	 *            The strengthLevel to set.
	 */
	public void setStrengthLevel(int strengthLevel) {
		this.strengthLevel = strengthLevel;
	}

	/**
	 * Gets the attackLevel.
	 * 
	 * @return The attackLevel.
	 */
	public int getAttackLevel() {
		return attackLevel;
	}

	/**
	 * Sets the attackLevel.
	 * 
	 * @param attackLevel
	 *            The attackLevel to set.
	 */
	public void setAttackLevel(int attackLevel) {
		this.attackLevel = attackLevel;
	}

	/**
	 * Gets the defenceLevel.
	 * 
	 * @return The defenceLevel.
	 */
	public int getDefenceLevel() {
		return defenceLevel;
	}

	/**
	 * Sets the defenceLevel.
	 * 
	 * @param defenceLevel
	 *            The defenceLevel to set.
	 */
	public void setDefenceLevel(int defenceLevel) {
		this.defenceLevel = defenceLevel;
	}

	/**
	 * Gets the rangeLevel.
	 * 
	 * @return The rangeLevel.
	 */
	public int getRangeLevel() {
		return rangeLevel;
	}

	/**
	 * Sets the rangeLevel.
	 * 
	 * @param rangeLevel
	 *            The rangeLevel to set.
	 */
	public void setRangeLevel(int rangeLevel) {
		this.rangeLevel = rangeLevel;
	}

	/**
	 * Gets the magicLevel.
	 * 
	 * @return The magicLevel.
	 */
	public int getMagicLevel() {
		return magicLevel;
	}

	/**
	 * Sets the magicLevel.
	 * 
	 * @param magicLevel
	 *            The magicLevel to set.
	 */
	public void setMagicLevel(int magicLevel) {
		this.magicLevel = magicLevel;
	}

	/**
	 * Gets the attackSpeed.
	 * 
	 * @return The attackSpeed.
	 */
	public int getAttackSpeed() {
		return attackSpeed;
	}

	/**
	 * Sets the attackSpeed.
	 * 
	 * @param attackSpeed
	 *            The attackSpeed to set.
	 */
	public void setAttackSpeed(int attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	/**
	 * Gets the startGraphics.
	 * 
	 * @return The startGraphics.
	 */
	public int getStartGraphics() {
		return this.getCombatDefinitions().getAttackGfx();
	}

	/**
	 * Sets the startGraphics.
	 * 
	 * @param startGraphics
	 *            The startGraphics to set.
	 */
	public void setStartGraphics(int startGraphics) {
		this.startGraphics = startGraphics;
	}

	/**
	 * Gets the projectileId.
	 * 
	 * @return The projectileId.
	 */
	public int getProjectileId() {
		return this.getCombatDefinitions().getAttackProjectile();
	}

	/**
	 * Sets the projectileId.
	 * 
	 * @param projectileId
	 *            The projectileId to set.
	 */
	public void setProjectileId(int projectileId) {
		this.projectileId = projectileId;
	}

	/**
	 * Gets the endGraphics.
	 * 
	 * @return The endGraphics.
	 */
	public int getEndGraphics() {
		return endGraphics;
	}

	/**
	 * Sets the endGraphics.
	 * 
	 * @param endGraphics
	 *            The endGraphics to set.
	 */
	public void setEndGraphics(int endGraphics) {
		this.endGraphics = endGraphics;
	}

	/**
	 * Gets the usingMelee.
	 * 
	 * @return The usingMelee.
	 */
	public boolean isUsingMelee() {
		return usingMelee;
	}

	/**
	 * Sets the usingMelee.
	 * 
	 * @param usingMelee
	 *            The usingMelee to set.
	 */
	public void setUsingMelee(boolean usingMelee) {
		this.usingMelee = usingMelee;
	}

	/**
	 * Gets the usingRange.
	 * 
	 * @return The usingRange.
	 */
	public boolean isUsingRange() {
		return usingRange;
	}

	/**
	 * Sets the usingRange.
	 * 
	 * @param usingRange
	 *            The usingRange to set.
	 */
	public void setUsingRange(boolean usingRange) {
		this.usingRange = usingRange;
	}

	/**
	 * Gets the usingMagic.
	 * 
	 * @return The usingMagic.
	 */
	public boolean isUsingMagic() {
		return usingMagic;
	}

	/**
	 * Sets the usingMagic.
	 * 
	 * @param usingMagic
	 *            The usingMagic to set.
	 */
	public void setUsingMagic(boolean usingMagic) {
		this.usingMagic = usingMagic;
	}

	/**
	 * Gets the poisonImmune.
	 * 
	 * @return The poisonImmune.
	 */
	public boolean isPoisonImmune() {
		return poisonImmune;
	}

	/**
	 * Sets the poisonImmune.
	 * 
	 * @param poisonImmune
	 *            The poisonImmune to set.
	 */
	public void setPoisonImmune(boolean poisonImmune) {
		this.poisonImmune = poisonImmune;
	}

	/**
	 * Gets the aggressive.
	 * 
	 * @return The aggressive.
	 */
	public boolean isAggressive() {
		return aggressive;
	}

	/**
	 * Sets the aggressive.
	 * 
	 * @param aggressive
	 *            The aggressive to set.
	 */
	public void setAggressive(boolean aggressive) {
		this.aggressive = aggressive;
	}

	public int getId() {
		return id;
	}
}