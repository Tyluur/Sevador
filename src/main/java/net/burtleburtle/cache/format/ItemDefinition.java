package net.burtleburtle.cache.format;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.Map;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.cache.CacheConstants;
import net.burtleburtle.cache.CacheManager;

import com.sevador.utility.BufferUtils;
import com.sevador.utility.Constants;

/**
 * @author 'Mystic Flow
 */
public class ItemDefinition implements Serializable {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = 8981839918681661937L;

	private static int[] bodyData;
	public int id;
	public boolean loaded;

	public int interfaceModelId;
	public String name;
	private static ItemDefinition[] definitions;

    private boolean extraDefinitions;
    private CacheItemDefinition cacheDefinition;
	// model size information
	public int modelZoom;
	public int modelRotation1;
	public int modelRotation2;
	public int modelOffset1;
	public int modelOffset2;

	// extra information
	public int stackable;
	public int value;
	public boolean membersOnly;

	// wearing model information
	public int maleWornModelId1 = -1;
	public int femaleWornModelId1;
	public int maleWornModelId2 = -1;
	public int femaleWornModelId2;

	// options
	public String[] groundOptions;
	public String[] inventoryOptions;

	// model information
	public short[] originalModelColors;
	public short[] modifiedModelColors;
	public short[] textureColour1;
	public short[] textureColour2;
	public byte[] unknownArray1;
	public int[] unknownArray2;

	// extra information, not used for newer items
	public boolean unnoted = false;

	public int colourEquip1;
	public int colourEquip2;
	public int noteId = -1;
	public int noteTemplateId = -1;
	public int[] stackIds;
	public int[] stackAmounts;
	public int teamId;
	public int lendId = -1;
	public int lendTemplateId = -1;
	public int recolourId = -1; // not sure
	public int recolourTemplateId = -1;

	@SuppressWarnings("unused")
	private int equipmentSlot = -1;
	private final int[] bonus = new int[15];
	private String examine = "null";
	private int lowAlch = 0;
	private int highAlch = 0;
	private int storePrice = 0;
	private boolean dropable = false;
	private boolean tradable = false;
	private boolean twoHanded = false;
	private boolean fullBody = false;
	private boolean fullHat = false;
	private boolean fullMask = false;
    private boolean noted;
	private final int[] absorb = new int[3];
	private int attackSpeed = 4;
	private double weight = 0.0;
	public int equipId;

	/**
	 * The item definition cache.
	 */
	private static final Map<Integer, ItemDefinition> ITEM_DEFINITIONS = new HashMap<Integer, ItemDefinition>();

	public HashMap<Integer, Object> clientScriptData;
	@SuppressWarnings("unused")
	private HashMap<Integer, Integer> itemRequiriments;
	public int grandExchangePrice;
	
	public double getWeight() {
		return weight;
	}
	
	public int getGEPrice() {
		return grandExchangePrice;
	}
	
	  /**
     * Loads the grand exchange prices.
     */
    private static void loadExchangePrices() {
    	try {
	    	DataInputStream dat = new DataInputStream(new FileInputStream("./data/nodes/items/grand_exchange_prices.dat"));
			while (true) {
				int itemId = dat.readShort();
				if (itemId == -1) {
					break;
				}
				ItemDefinition def = forId(itemId);
				def.grandExchangePrice = dat.readInt();
			}
			dat.close();
    	} catch (Throwable t) {
    		t.printStackTrace();
    		System.exit(1);
    	}
    }

	/*public int getGEPrice() {
		File inputFile = new File("data/nodes/items/geprices.txt");
		BufferedReader reader;
		String currentLine;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
			while((currentLine = reader.readLine()) != null) {
				String[] split = currentLine.split(" - ");
				if (Integer.parseInt(split[0]) == id && Constants.isInteger(split[1])) {
					reader.close();
					return Integer.parseInt(split[1]);
				}
			}
			reader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}*/
	
	public static ItemDefinition forId(int itemId) {
		if (ITEM_DEFINITIONS.containsKey(itemId)) {
			return ITEM_DEFINITIONS.get(itemId);
		}
		ItemDefinition def = new ItemDefinition(itemId);
		def.loadItemDefinition();
		ITEM_DEFINITIONS.put(itemId, def);
		return def;
	}

	public static void loadEquipIds() {
		int equipId = 0;
		for (int i = 0; i < Cache.getAmountOfItems(); i++) {
			ItemDefinition def = ItemDefinition.forId(i);
			if (def.maleWornModelId1 >= 0 || def.maleWornModelId2 >= 0) {
				def.equipId = equipId++;
			}
		}
	}

	private ItemDefinition(int id) {
		this.id = id;
	}
	public static void init() {
	        System.out.println("Loading item definitions...");
	        definitions = new ItemDefinition[Cache.getAmountOfItems()];
	        FileChannel channel;
			try {
				channel = new RandomAccessFile("data/nodes/items/itemDefinitions.bin", "r").getChannel();
		        ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
		        int length = buffer.getShort();
		        for (int i = 0; i < length; i++) {
		            int id = buffer.getShort();
		            if (id == -1) {
		                continue;
		            }
		            ItemDefinition def = ItemDefinition.forId(id);
		            def.id = id;
		            def.equipId = buffer.getShort();
		            def.noted = buffer.get() != 0; //get() == 0 //Unnoted
		            def.examine = BufferUtils.readRS2String(buffer);
		            if (!def.isNoted()) {
		            	def.weight = buffer.getDouble();
						def.equipmentSlot = buffer.get();
						def.setFullHat(buffer.get() == 1);
						def.setFullMask(buffer.get() == 1);
						def.fullBody = buffer.get() == 1;
						def.tradable = buffer.get() == 1;
						def.attackSpeed = buffer.get();
						boolean hasBonus = buffer.get() == 1;
						if (hasBonus) {
							for (int x = 0; x < 15; x++) {
								def.bonus[x] = buffer.getShort();
							}
						}
						hasBonus = buffer.get() == 1;
						if (hasBonus) {
							for (int x = 0; x < 3; x++) {
								def.absorb[x] = buffer.getShort();
							}
						}
		            }
		            def.highAlch = buffer.getInt();
					def.lowAlch = buffer.getInt();
					def.storePrice = buffer.getInt();
					def.dropable = buffer.get() == 1;
					def.twoHanded = buffer.get() == 1;
		        }
		        channel.close();
			} catch (Throwable t) {
				t.printStackTrace();
			}
	        loadEquipIds();
	        loadExchangePrices();
	        CacheItemDefinition.loadEquipIds();
	        System.out.println("Loaded " + definitions.length + " item definitions.");
	        channel = null;
	    }
	 
	 
	/**
	 * Initializes the item definitions.
	 */
	/*public static void init() {
		FileChannel channel;
		try {
			channel = new RandomAccessFile("data/nodes/itemDefinitions.bin", "r").getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			int length = buffer.getShort();
			for (int i = 0; i < length; i++) {
				int id = buffer.getShort();
				if (id == -1) {
					continue;
				}
				ItemDefinition def = ItemDefinition.forId(id);
				def.equipId = buffer.getShort();
				def.unnoted = buffer.get() == 0;
				def.examine = BufferUtils.readRS2String(buffer);
				if (!def.isNoted()) {
					def.weight = buffer.getDouble();
					def.equipmentSlot = buffer.get();
					def.fullHat = buffer.get() == 1;
					def.fullMask = buffer.get() == 1;
					def.fullBody = buffer.get() == 1;
					def.tradable = buffer.get() == 1;
					def.attackSpeed = buffer.get();
					boolean hasBonus = buffer.get() == 1;
					if (hasBonus) {
						for (int x = 0; x < 15; x++) {
							def.bonus[x] = buffer.getShort();
						}
					}
					hasBonus = buffer.get() == 1;
					if (hasBonus) {
						for (int x = 0; x < 3; x++) {
							def.absorb[x] = buffer.getShort();
						}
					}
				}
				def.highAlch = buffer.getInt();
				def.lowAlch = buffer.getInt();
				def.storePrice = buffer.getInt();
				def.dropable = buffer.get() == 1;
				def.twoHanded = buffer.get() == 1;
			}
	        loadExchangePrices();
			Main.getLogger().info("Loaded " + NumberFormat.getIntegerInstance().format(channel.size()) + " item definitions.");
			channel.close();
			loadEquipIds();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}*/

	public boolean isLoaded() {
		return loaded;
	}

	public void loadItemDefinition() {
		setDefaultsVariableValules();
		setDefaultOptions();
		byte[] is = null;
		try {
			is = CacheManager.getData(CacheConstants.ITEMDEF_IDX_ID, id >>> 8,
			id & 0xFF);
		} catch (Exception e) {
			//e.printStackTrace();
			//System.out.println("Item " + id + " doesn't exist in the cache!");
		}
		if (is != null) {
			try {
				readOpcodeValues(ByteBuffer.wrap(is));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error while reading " + id);
			}
		}
		if (noteTemplateId != -1) // done
			transferNoteDefinition(forId(noteId), forId(noteTemplateId));
		if (lendTemplateId != -1) // done
			transferLendDefinition(forId(lendId), forId(lendTemplateId));
		if (recolourTemplateId != -1) // need to finish
			transferRecolourDefinition(forId(recolourId),
					forId(recolourTemplateId));
		loaded = true;
	}

	public void transferNoteDefinition(ItemDefinition reference,
			ItemDefinition templateReference) {
		membersOnly = reference.membersOnly;
		interfaceModelId = templateReference.interfaceModelId;
		originalModelColors = templateReference.originalModelColors;
		name = reference.name;
		modelOffset2 = templateReference.modelOffset2;
		textureColour1 = templateReference.textureColour1;
		value = reference.value;
		modelRotation2 = templateReference.modelRotation2;
		stackable = 1;
		modifiedModelColors = templateReference.modifiedModelColors;
		modelRotation1 = templateReference.modelRotation1;
		modelZoom = templateReference.modelZoom;
		textureColour1 = templateReference.textureColour1;
	}

	public void transferLendDefinition(ItemDefinition reference,
			ItemDefinition templateReference) {
		femaleWornModelId1 = reference.femaleWornModelId1;
		maleWornModelId2 = reference.maleWornModelId2;
		membersOnly = reference.membersOnly;
		interfaceModelId = templateReference.interfaceModelId;
		textureColour2 = reference.textureColour2;
		groundOptions = reference.groundOptions;
		unknownArray1 = reference.unknownArray1;
		modelRotation1 = templateReference.modelRotation1;
		modelRotation2 = templateReference.modelRotation2;
		originalModelColors = reference.originalModelColors;
		name = reference.name;
		maleWornModelId1 = reference.maleWornModelId1;
		colourEquip1 = reference.colourEquip1;
		teamId = reference.teamId;
		modelOffset2 = templateReference.modelOffset2;
		clientScriptData = reference.clientScriptData;
		modifiedModelColors = reference.modifiedModelColors;
		colourEquip2 = reference.colourEquip2;
		modelOffset1 = templateReference.modelOffset1;
		textureColour1 = reference.textureColour1;
		value = 0;
		modelZoom = templateReference.modelZoom;
		inventoryOptions = new String[5];
		femaleWornModelId2 = reference.femaleWornModelId2;
		if (reference.inventoryOptions != null)
			inventoryOptions = reference.inventoryOptions.clone();
	}

	public void transferRecolourDefinition(ItemDefinition reference,
			ItemDefinition templateReference) {
		femaleWornModelId2 = reference.femaleWornModelId2;
		inventoryOptions = new String[5];
		modelRotation2 = templateReference.modelRotation2;
		name = reference.name;
		maleWornModelId1 = reference.maleWornModelId1;
		modelOffset2 = templateReference.modelOffset2;
		femaleWornModelId1 = reference.femaleWornModelId1;
		maleWornModelId2 = reference.maleWornModelId2;
		modelOffset1 = templateReference.modelOffset1;
		unknownArray1 = reference.unknownArray1;
		stackable = reference.stackable;
		modelRotation1 = templateReference.modelRotation1;
		textureColour1 = reference.textureColour1;
		colourEquip1 = reference.colourEquip1;
		textureColour2 = reference.textureColour2;
		modifiedModelColors = reference.modifiedModelColors;
		modelZoom = templateReference.modelZoom;
		colourEquip2 = reference.colourEquip2;
		teamId = reference.teamId;
		value = 0;
		groundOptions = reference.groundOptions;
		originalModelColors = reference.originalModelColors;
		membersOnly = reference.membersOnly;
		clientScriptData = reference.clientScriptData;
		interfaceModelId = templateReference.interfaceModelId;
		if (reference.inventoryOptions != null)
			inventoryOptions = reference.inventoryOptions.clone();
	}

	public boolean hasSpecialBar() {
		if (clientScriptData == null)
			return false;
		Object specialBar = clientScriptData.get(687);
		return specialBar != null && specialBar instanceof Integer
				&& (Integer) specialBar == 1;
	}

	public int getGroupId() {
		if (clientScriptData == null)
			return 0;
		Object specialBar = clientScriptData.get(686);
		if (specialBar != null && specialBar instanceof Integer)
			return (Integer) specialBar;
		return 0;
	}

	public int getRenderAnimId() {
		if (clientScriptData == null)
			return 1426;
		Object animId = clientScriptData.get(644);
		if (animId != null && animId instanceof Integer)
			return (Integer) animId;
		return 1426;
	}

	public int getQuestId() {
		if (clientScriptData == null)
			return -1;
		Object questId = clientScriptData.get(861);
		if (questId != null && questId instanceof Integer)
			return (Integer) questId;
		return -1;
	}

	public HashMap<Integer, Integer> getWearingSkillRequiriments() {
		return getCacheDefinition().getWearingRequirements();
		/*if (clientScriptData == null)
			return null;
		if (itemRequiriments == null) {
			HashMap<Integer, Integer> skills = new HashMap<Integer, Integer>();
			for (int i = 0; i < 10; i++) {
				Integer skill = (Integer) clientScriptData.get(749 + (i * 2));
				if (skill != null) {
					Integer level = (Integer) clientScriptData
							.get(750 + (i * 2));
					if (level != null)
						skills.put(skill, level);
				}
			}
			Integer maxedSkill = (Integer) clientScriptData.get(277);
			if (maxedSkill != null)
				skills.put(maxedSkill, id == 19709 ? 120 : 99);
			itemRequiriments = skills;
			if (id == 7462)
				itemRequiriments.put((int) Skills.DEFENCE, 40);
			else if (name.equals("Dragon defender")) {
				itemRequiriments.put((int) Skills.ATTACK, 60);
				itemRequiriments.put((int) Skills.DEFENCE, 60);
			}
		}

		return itemRequiriments;*/
	}

	public void setDefaultsVariableValules() {

	}

	public void setDefaultOptions() {
		groundOptions = new String[] { null, null, "take", null, null };
		inventoryOptions = new String[] { null, null, null, null, "drop" };
	}

	private void readValues(ByteBuffer buffer, int opcode) {
		if (opcode == 1)
			interfaceModelId = buffer.getShort() & 0xFFFF;
		else if (opcode == 2)
			name = BufferUtils.readRS2String(buffer);
		else if (opcode == 4)
			modelZoom = buffer.getShort() & 0xFFFF;
		else if (opcode == 5)
			modelRotation1 = buffer.getShort() & 0xFFFF;
		else if (opcode == 6)
			modelRotation2 = buffer.getShort() & 0xFFFF;
		else if (opcode == 7) {
			modelOffset1 = buffer.getShort() & 0xFFFF;
			if (modelOffset1 > 32767)
				modelOffset1 -= 65536;
		} else if (opcode == 8) {
			modelOffset2 = buffer.getShort() & 0xFFFF;
			if (modelOffset2 > 32767)
				modelOffset2 -= 65536;
		} else if (opcode == 11)
			stackable = 1;
		else if (opcode == 12)
			value = buffer.getInt();
		else if (opcode == 16)
			membersOnly = true;
		else if (opcode == 18)
			buffer.getShort();
		else if (opcode == 23)
			maleWornModelId1 = buffer.getShort() & 0xFFFF;
		else if (opcode == 24)
			femaleWornModelId1 = buffer.getShort() & 0xFFFF;
		else if (opcode == 25)
			maleWornModelId2 = buffer.getShort() & 0xFFFF;
		else if (opcode == 26)
			femaleWornModelId2 = buffer.getShort() & 0xFFFF;
		else if (opcode >= 30 && opcode < 35)
			groundOptions[opcode - 30] = BufferUtils.readRS2String(buffer);
		else if (opcode >= 35 && opcode < 40)
			inventoryOptions[opcode - 35] = BufferUtils.readRS2String(buffer);
		else if (opcode == 40) {
			int length = buffer.get() & 0xFF;
			originalModelColors = new short[length];
			modifiedModelColors = new short[length];
			for (int index = 0; index < length; index++) {
				originalModelColors[index] = buffer.getShort();
				modifiedModelColors[index] = buffer.getShort();
			}
		} else if (opcode == 41) {
			int length = buffer.get() & 0xFF;
			textureColour1 = new short[length];
			textureColour2 = new short[length];
			for (int index = 0; index < length; index++) {
				textureColour1[index] = buffer.getShort();
				textureColour2[index] = buffer.getShort();
			}
		} else if (opcode == 42) {
			int length = buffer.get() & 0xFF;
			unknownArray1 = new byte[length];
			for (int index = 0; index < length; index++)
				unknownArray1[index] = buffer.get();
		} else if (opcode == 65)
			unnoted = true;
		else if (opcode == 78)
			colourEquip1 = buffer.getShort() & 0xFFFF;
		else if (opcode == 79)
			colourEquip2 = buffer.getShort() & 0xFFFF;
		else if (opcode == 90)
			buffer.getShort();
		else if (opcode == 91)
			buffer.getShort();
		else if (opcode == 92)
			buffer.getShort();
		else if (opcode == 93)
			buffer.getShort();
		else if (opcode == 95)
			buffer.getShort();
		else if (opcode == 96)
			buffer.get();
		else if (opcode == 97)
			noteId = buffer.getShort() & 0xFFFF;
		else if (opcode == 98)
			noteTemplateId = buffer.getShort() & 0xFFFF;
		else if (opcode >= 100 && opcode < 110) {
			if (stackIds == null) {
				stackIds = new int[10];
				stackAmounts = new int[10];
			}
			stackIds[opcode - 100] = buffer.getShort() & 0xFFFF;
			stackAmounts[opcode - 100] = buffer.getShort() & 0xFFFF;
		} else if (opcode == 110)
			buffer.getShort();
		else if (opcode == 111)
			buffer.getShort();
		else if (opcode == 112)
			buffer.getShort();
		else if (opcode == 113)
			buffer.get();
		else if (opcode == 114)
			buffer.get();
		else if (opcode == 115)
			teamId = buffer.get();
		else if (opcode == 121)
			lendId = buffer.getShort() & 0xFFFF;
		else if (opcode == 122)
			lendTemplateId = buffer.getShort() & 0xFFFF;
		else if (opcode == 125) {
			buffer.get();
			buffer.get();
			buffer.get();
		} else if (opcode == 126) {
			buffer.get();
			buffer.get();
			buffer.get();
		} else if (opcode == 127) {
			buffer.get();
			buffer.getShort();
		} else if (opcode == 128) {
			buffer.get();
			buffer.getShort();
		} else if (opcode == 129) {
			buffer.get();
			buffer.getShort();
		} else if (opcode == 130) {
			buffer.get();
			buffer.getShort();
		} else if (opcode == 132) {
			int length = buffer.get() & 0xFF;
			unknownArray2 = new int[length];
			for (int index = 0; index < length; index++)
				unknownArray2[index] = buffer.getShort() & 0xFFFF;
		} else if (opcode == 134)
			buffer.get();
		else if (opcode == 139)
			recolourId = buffer.getShort() & 0xFFFF;
		else if (opcode == 140)
			recolourTemplateId = buffer.getShort() & 0xFFFF;
		else if (opcode == 249) {
			int length = buffer.get() & 0xFF;
			if (clientScriptData == null) {
				clientScriptData = new HashMap<Integer, Object>();
			}
			for (int index = 0; index < length; index++) {
				boolean string = (buffer.get() & 0xFF) == 1;
				int key = BufferUtils.getMediumInt(buffer);
				Object value = string ? BufferUtils.readRS2String(buffer)
						: buffer.getInt();
				clientScriptData.put(key, value);
			}
		} else {
			System.out.println("Unhandled opcode! opcode:" + opcode);
		}
	}

	private void readOpcodeValues(ByteBuffer buffer) {
		while (true) {
			int opcode = buffer.get() & 0xFF;
			if (opcode == 0)
				break;
			readValues(buffer, opcode);
		}
	}

	/**
	 * @return the bodyData
	 */
	public static int[] getBodyData() {
		return bodyData;
	}

	/**
	 * @param bodyData
	 *            the bodyData to set
	 */
	public static void setBodyData(int[] bodyData) {
		ItemDefinition.bodyData = bodyData;
	}

	private static String[] LEGS = { "leggings", "void knight robe",
		"druidic robe", "cuisse", "pants", "platelegs", "plateskirt",
		"skirt", "bottoms", "chaps", "platelegs (t)", "platelegs (g)",
		"bottom", "skirt", "skirt (g)", "skirt (t)", "chaps (g)",
		"chaps (t)", "tassets", "legs", "trousers", "robe bottom" };

	private static String[] WEAPONS = { "bolas", "blade", "Butterfly net",
		"scythe", "rapier", "hatchet", "bow", "Hand cannon",
		"Inferno adze", "Silverlight", "Darklight", "wand",
		"Statius's warhammer", "anchor", "spear.", "Vesta's longsword.",
		"scimitar", "longsword", "sword", "longbow", "shortbow", "dagger",
		"mace", "halberd", "spear", "Abyssal whip", "Abyssal vine whip",
		"Ornate katana", "axe", "flail", "crossbow", "Torags hammers",
		"dagger(p)", "dagger (p++)", "dagger(+)", "dagger(s)", "spear(p)",
		"spear(+)", "spear(s)", "spear(kp)", "maul", "dart", "dart(p)",
		"javelin", "javelin(p)", "knife", "knife(p)", "Longbow",
		"Shortbow", "Crossbow", "Toktz-xil", "Toktz-mej", "Tzhaar-ket",
		"staff", "Staff", "godsword", "c'bow", "Crystal bow", "Dark bow",
		"claws", "warhammer", "hammers", "adze", "hand", "Broomstick",
		"Flowers", "flowers", "trident", "excalibur" };

	private static String[] CAPES = { "cloak", "cape", "ava's", "TokHaar" };

	private static String[] HATS = { "visor", "ears", "goggles", "bearhead",
		"tiara", "cowl", "druidic wreath", "halo", "Royal", "crown",
		"sallet", "helm", "hood", "coif", "Coif", "partyhat", "hat", "cap",
		" bandana", "full helm (t)", "full helm (g)", "cav", "boater",
		"helmet", "afro", "beard", "gnome goggles", "mask",
		"Helm of neitiznot", "mitre" };

	private static String[] BOOTS = { "boots", "Boots", "shoes", "Shoes",
	"flippers" };

	private static String[] GLOVES = { "gloves", "gauntlets", "Gloves",
		"vambraces", "vamb", "bracers", "brace" };

	private static String[] AMULETS = { "stole", "amulet", "necklace",
		"Amulet of", "scarf", "Super dominion medallion" };

	private static String[] SHIELDS = { "tome of frost", "kiteshield",
		"sq shield", "Toktz-ket", "books", "book", "kiteshield (t)",
		"kiteshield (g)", "kiteshield(h)", "defender", "shield",
	"deflector" };

	private static String[] ARROWS = { "arrow", "arrows", "arrow(p)",
		"arrow(+)", "arrow(s)", "bolt", "Bolt rack", "Opal bolts",
		"Dragon bolts", "bolts (e)", "bolts", "Hand cannon shot" };

	private static String[] RINGS = { "ring" };

	private static String[] BODY = { "poncho", "apron", "robe top", "armour",
		"hauberk", "platebody", "chainbody", "robetop", "leathertop",
		"platemail", "top", "brassard", "body", "platebody (t)",
		"platebody (g)", "body(g)", "body_(g)", "chestplate", "torso",
		"shirt", "Rock-shell plate" };

	private static String[] AURAS = { "poison purge", "runic accuracy",
		"sharpshooter", "lumberjack", "quarrymaster", "call of the sea",
		"reverence", "five finger discount", "resourceful", "equilibrium",
		"inspiration", "vampyrism", "penance", "wisdom", "jack of trades" };

	private static int[] BODY_LIST = { 21463, 21549, 544, 6107 };

	private static int[] LEGS_LIST = { 542, 6108, 10340, 7398 };

	public static int getItemSlot(int itemId) {
		for (int i = 0; i < BODY_LIST.length; i++)
			if (itemId == BODY_LIST[i])
				return 4;
		for (int i = 0; i < LEGS_LIST.length; i++)
			if (itemId == LEGS_LIST[i])
				return 7;
		String item = ItemDefinition.forId(itemId).getName().toLowerCase();
		if (item == null)
			return -1;
		for (int i = 0; i < CAPES.length; i++)
			if (item.contains(CAPES[i].toLowerCase()))
				return 1;
		for (int i = 0; i < BOOTS.length; i++)
			if (item.contains(BOOTS[i].toLowerCase()))
				return 10;
		for (int i = 0; i < GLOVES.length; i++)
			if (item.contains(GLOVES[i].toLowerCase()))
				return 9;
		for (int i = 0; i < SHIELDS.length; i++)
			if (item.contains(SHIELDS[i].toLowerCase()))
				return 5;
		for (int i = 0; i < AMULETS.length; i++)
			if (item.contains(AMULETS[i].toLowerCase()))
				return 2;
		for (int i = 0; i < ARROWS.length; i++)
			if (item.contains(ARROWS[i].toLowerCase()))
				return 13;
		for (int i = 0; i < RINGS.length; i++)
			if (item.contains(RINGS[i].toLowerCase()))
				return 12;
		for (int i = 0; i < WEAPONS.length; i++)
			if (item.contains(WEAPONS[i].toLowerCase()))
				return 3;
		for (int i = 0; i < HATS.length; i++)
			if (item.contains(HATS[i].toLowerCase()))
				return 0;
		for (int i = 0; i < BODY.length; i++)
			if (item.contains(BODY[i].toLowerCase()))
				return 4;
		for (int i = 0; i < LEGS.length; i++)
			if (item.contains(LEGS[i].toLowerCase()))
				return 7;
		for (int i = 0; i < AURAS.length; i++)
			if (item.contains(AURAS[i].toLowerCase()))
				return Constants.SLOT_AURA;
		return -1;
	}

	/**
	 * @return the equipmentSlot
	 */
	public int getEquipmentSlot() {
		return getItemSlot(id);
	}

	public void setEquipmentSlot(int slot) {
		this.equipmentSlot = slot;
	}

	public int getEquipId() {
		return equipId;
	}

	/**
	 * @param equipmentSlot
	 *            the equipmentSlot to set
	 */
	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	/**
	 * @return the bonus
	 */
	public int[] getBonus() {
		return bonus;
	}

	/**
	 * @return the examine
	 */
	public String getExamine() {
		return examine;
	}

	/**
	 * @param examine
	 *            the examine to set
	 */
	public void setExamine(String examine) {
		this.examine = examine;
	}

	/**
	 * @return the lowAlch
	 */
	public int getLowAlch() {
		return lowAlch;
	}

	/**
	 * @param lowAlch
	 *            the lowAlch to set
	 */
	public void setLowAlch(int lowAlch) {
		this.lowAlch = lowAlch;
	}

	/**
	 * @return the highAlch
	 */
	public int getHighAlch() {
		return highAlch;
	}

	/**
	 * @param highAlch
	 *            the highAlch to set
	 */
	public void setHighAlch(int highAlch) {
		this.highAlch = highAlch;
	}

	/**
	 * @return the storePrice
	 */
	public int getStorePrice() {
		return storePrice;
	}

	/**
	 * @param storePrice
	 *            the storePrice to set
	 */
	public void setStorePrice(int storePrice) {
		this.storePrice = storePrice;
	}

	/**
	 * @return the dropable
	 */
	public boolean isDropable() {
		return dropable;
	}

	/**
	 * @param dropable
	 *            the dropable to set
	 */
	public void setDropable(boolean dropable) {
		this.dropable = dropable;
	}

	/**
	 * @return the tradable
	 */
	public boolean isTradable() {
		return tradable;
	}
	
	/**
	 * @param tradable
	 *            the tradable to set
	 */
	public void setTradable(boolean tradable) {
		this.tradable = tradable;
	}

	/**
	 * @return the absorb
	 */
	public int[] getAbsorb() {
		return absorb;
	}

	/**
	 * @param twoHanded
	 *            the twoHanded to set
	 */
	public void setTwoHanded(boolean twoHanded) {
		this.twoHanded = twoHanded;
	}

	public boolean isTwoHanded() {
		return twoHanded;
	}

	/**
	 * @return the fullBody
	 */
	public boolean isFullBody() {
		return fullBody;
	}

	/**
	 * @param fullBody
	 *            the fullBody to set
	 */
	public void setFullBody(boolean fullBody) {
		this.fullBody = fullBody;
	}

	/**
	 * @return the fullHat
	 */
	public boolean isFullHat() {
		return fullHat;
	}
	

	/**
	 * @param fullHat
	 *            the fullHat to set
	 */
	public void setFullHat(boolean fullHelm) {
		this.fullHat = fullHelm;
	}

	/**
	 * @return the fullMask
	 */
	public boolean isFullMask() {
		return fullMask;
	}

	/**
	 * @param fullMask
	 *            the fullMask to set
	 */
	public void setFullMask(boolean fullMask) {
		this.fullMask = fullMask;
	}

	public boolean isNoted() {
		return noted;
	}

	/**
	 * @return the attackSpeed
	 */
	public int getAttackSpeed() {
		return attackSpeed;
	}

	/**
	 * @param attackSpeed
	 *            the attackSpeed to set
	 */
	public void setAttackSpeed(int attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	public boolean isStackable() {
		return stackable == 1;
	}

	public void setNoted(boolean b) {
		this.unnoted = !b;
	}

	public String getName() {
		return name;
	}

	/**
	 * @param replace
	 * @return
	 */
	public static ItemDefinition forName(String name) {
		for (ItemDefinition definition : definitions) {
			if (definition.name.equalsIgnoreCase(name)) {
				return definition;
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public int[] getAbsorptionBonus() {
		return getAbsorb();
	}
	
    public CacheItemDefinition getCacheDefinition() {
        if (cacheDefinition == null) {
            cacheDefinition = CacheItemDefinition.getItemDefinition(id);
        }
        return cacheDefinition;
    }
    
    public static ItemDefinition[] getDefinitions() {
        return definitions;
    }
    
    public void setBonusAtIndex(int index, int value) {
        this.bonus[index] = value;
    }
    
    public void setExtraDefinitions(boolean extraDefinition) {
        this.extraDefinitions = extraDefinition;
    }

    public boolean isExtraDefinitions() {
        return extraDefinitions;
    }


}