package com.sevador.game.event;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.sevador.Main;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.combat.form.MagicSpell;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * Handles events such as action buttons, object options, item options, ...
 *
 * @author Emperor
 */
public final class EventManager {

	/**
	 * The button events.
	 */
	private static final Map<Integer, ButtonEvent> BUTTON_EVENTS = new HashMap<Integer, ButtonEvent>();

	/**
	 * The NPC action events.
	 */
	private static final Map<Integer, NPCActionEvent> NPC_ACTION_EVENTS = new HashMap<Integer, NPCActionEvent>();

	/**
	 * The caller for the NPC action events.
	 */

	public static final Map<Integer, NPCActionEvent> getNPCActions() {
		return NPC_ACTION_EVENTS;
	}

	/**
	 * The object action events.
	 */
	private static final Map<Integer, ObjectEvent> OBJECT_EVENTS = new HashMap<Integer, ObjectEvent>();

	/**
	 * The item action events.
	 */
	private static final Map<Integer, ItemActionEvent> ITEM_ACTION_EVENTS = new HashMap<Integer, ItemActionEvent>();

	/**
	 * The magic spell events.
	 */
	private static final Map<Integer, MagicSpell> MAGIC_SPELLS = new HashMap<Integer, MagicSpell>();

	/**
	 * The special attack events.
	 */
	private static final Map<Integer, TypeHandler> SPECIAL_ATTACKS = new HashMap<Integer, TypeHandler>();

	/**
	 * Initializes the event manager.
	 *
	 * @return {@code True} if successful.
	 * @throws Throwable When an exception occurs.
	 */
	public static boolean init() throws Throwable {
		ClassLoader classLoader = Main.class.getClassLoader(); // Assuming Main is your main class
		String[][] packagesAndClasses = {
				{"com.sevador.game.event.button", "ButtonEvent"},
				{"com.sevador.game.event.spell", "MagicSpell"},
				{"com.sevador.game.event.special", "TypeHandler"},
				{"com.sevador.game.event.npc", "NPCActionEvent"},
				{"com.sevador.game.event.object", "ObjectEvent"},
				{"com.sevador.game.event.item", "ItemActionEvent"}
		};

		for (String[] packageAndClass : packagesAndClasses) {
			String packageName = packageAndClass[0];
			String classNameSuffix = packageAndClass[1];
			String path = packageName.replace('.', '/');
			URL resource = classLoader.getResource(path);
			if (resource == null) {
				continue;
			}
			File directory = new File(resource.getFile());
			for (File file : directory.listFiles()) {
				if (file.isDirectory() || !file.getName().endsWith(".class")) {
					continue; // Skip directories and non-class files
				}
				String className = packageName + "." + file.getName().replace(".class", "");
				Class<?> clazz = Class.forName(className);
				if (Modifier.isAbstract(clazz.getModifiers()) || clazz.isAnonymousClass() || clazz.isLocalClass() || clazz.isMemberClass()) {
					continue; // Skip abstract, anonymous, local, and inner classes
				}
				Constructor<?> constructor = clazz.getDeclaredConstructor();
				if (!Modifier.isPublic(constructor.getModifiers())) {
					continue; // Skip classes without a public default constructor
				}
				Object instance = constructor.newInstance();
				if (classNameSuffix.equals("ButtonEvent") && !((ButtonEvent) instance).init()) return false;
				if (classNameSuffix.equals("MagicSpell") && !((MagicSpell) instance).init()) return false;
				if (classNameSuffix.equals("TypeHandler") && !((TypeHandler) instance).init()) return false;
				if (classNameSuffix.equals("NPCActionEvent") && !((NPCActionEvent) instance).init()) return false;
				if (classNameSuffix.equals("ObjectEvent") && !((ObjectEvent) instance).init()) return false;
				if (classNameSuffix.equals("ItemActionEvent") && !((ItemActionEvent) instance).init()) return false;
			}
		}

		Main.getLogger().info("Loaded " + BUTTON_EVENTS.size() + " button events.");
		Main.getLogger().info("Loaded " + MAGIC_SPELLS.size() + " magic spell events.");
		Main.getLogger().info("Loaded " + SPECIAL_ATTACKS.size() + " special attack events.");
		Main.getLogger().info("Loaded " + NPC_ACTION_EVENTS.size() + " npc action events.");
		Main.getLogger().info("Loaded " + OBJECT_EVENTS.size() + " object action events.");
		Main.getLogger().info("Loaded " + ITEM_ACTION_EVENTS.size() + " item action events.");
		return true;
	}

	/**
	 * Reloads the event manager.
	 *
	 * @return specified by {@link #init()}.
	 */
	public static boolean reload() {
		try {
			BUTTON_EVENTS.clear();
			MAGIC_SPELLS.clear();
			SPECIAL_ATTACKS.clear();
			NPC_ACTION_EVENTS.clear();
			OBJECT_EVENTS.clear();
			ITEM_ACTION_EVENTS.clear();
			return init();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Handles a button event.
	 *
	 * @param player      The player.
	 * @param opcode      The opcode.
	 * @param interfaceId The interface id.
	 * @param buttonId    The button id.
	 * @param itemId      The item id.
	 * @param slot        The item slot.
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	public static boolean handleButtonEvent(Player player, int opcode, int interfaceId, int buttonId, int itemId, int slot) {
		ButtonEvent event = BUTTON_EVENTS.get(interfaceId);
		if (event == null) {
			return false;
		}
		return event.handle(player, opcode, interfaceId, buttonId, itemId, slot);
	}

	/**
	 * Handles an NPC action event.
	 *
	 * @param player The player.
	 * @param npc    The NPC.
	 * @param option The option type.
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	public static boolean handleNPCActionEvent(Player player, NPC npc, OptionType option) {
		NPCActionEvent event = NPC_ACTION_EVENTS.get(npc.getId());
		if (event == null) {
			return false;
		}
		return event.handle(player, npc, option);
	}

	/**
	 * Handles an object action event.
	 *
	 * @param player The player.
	 * @param obj    The game object.
	 * @param option The option type.
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	public static boolean handleObjectEvent(Player player, GameObject obj, OptionType option) {
		ObjectEvent event = OBJECT_EVENTS.get(obj.getId());
		if (event == null) {
			return false;
		}
		return event.handle(player, obj, option);
	}

	/**
	 * Handles an item action event.
	 *
	 * @param player      The player.
	 * @param item        The item.
	 * @param interfaceId The interface id.
	 * @param slot        The item slot.
	 * @param option      The option type.
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	public static boolean handleItemEvent(Player player, Item item, int interfaceId, int slot, OptionType option) {
		ItemActionEvent event = ITEM_ACTION_EVENTS.get((int) item.getId());
		if (event == null) {
			return false;
		}
		return event.handle(player, item, interfaceId, slot, option);
	}

	/**
	 * Gets a spell event.
	 *
	 * @param spellId The spell id.
	 * @return The magic spell for this spell id.
	 */
	public static MagicSpell getSpellEvent(int spellId) {
		return MAGIC_SPELLS.get(spellId);
	}

	/**
	 * Gets a special attack event.
	 *
	 * @param itemId The item id.
	 * @return The special attack event for this item id.
	 */
	public static TypeHandler getSpecialAttackEvent(int itemId) {
		return SPECIAL_ATTACKS.get(itemId);
	}

	/**
	 * Gets an object action event.
	 *
	 * @param id The object id.
	 * @return The object event.
	 */
	public static ObjectEvent getObjectActionEvent(int id) {
		return OBJECT_EVENTS.get(id);
	}

	/**
	 * Registers a new {@code ButtonEvent} for an interface id.
	 *
	 * @param interfaceId The interface id.
	 * @param event       The event to register.
	 * @return {@code True} if succesful.
	 */
	public static boolean register(int interfaceId, ButtonEvent event) {
		if (BUTTON_EVENTS.containsKey(interfaceId)) {
			System.err.println("Could not register button event [Already registered key: " + interfaceId + ", name=" + event.getClass().getSimpleName() + ", old=" + BUTTON_EVENTS.get(interfaceId).getClass().getSimpleName() + "].");
			return false;
		}
		BUTTON_EVENTS.put(interfaceId, event);
		return true;
	}

	/**
	 * Registers a new {@code MagicSpell} for a spell id.
	 *
	 * @param spellId The spell id.
	 * @param event   The spell event to register.
	 * @return {@code True} if succesful.
	 */
	public static boolean register(int spellId, MagicSpell event) {
		if (MAGIC_SPELLS.containsKey(spellId)) {
			System.err.println("Could not register spell event [Already registered key: " + spellId + ", name=" + event.getClass().getSimpleName() + ", old=" + MAGIC_SPELLS.get(spellId).getClass().getSimpleName() + "].");
			return false;
		}
		MAGIC_SPELLS.put(spellId, event);
		return true;
	}

	/**
	 * Registers a new {@code TypeHandler} for an item id (used for special
	 * attack events).
	 *
	 * @param itemId The item id.
	 * @param event  The type handler to register.
	 * @return {@code True} if succesful.
	 */
	public static boolean register(int itemId, TypeHandler event) {
		if (SPECIAL_ATTACKS.containsKey(itemId)) {
			System.err.println("Could not register special attack event [Already registered key: " + itemId + ", name=" + event.getClass().getSimpleName() + ", old=" + SPECIAL_ATTACKS.get(itemId).getClass().getSimpleName() + "].");
			return false;
		}
		SPECIAL_ATTACKS.put(itemId, event);
		return true;
	}

	/**
	 * Registers a new {@code NPCActionEvent} for an NPC id.
	 *
	 * @param npcId The NPC id.
	 * @param event The NPC action event to register.
	 * @return {@code True} if succesful.
	 */
	public static boolean register(int npcId, NPCActionEvent event) {
		if (NPC_ACTION_EVENTS.containsKey(npcId)) {
			System.err.println("Could not register NPC action event [Already registered key: " + npcId + ", name=" + event.getClass().getSimpleName() + ", old=" + NPC_ACTION_EVENTS.get(npcId).getClass().getSimpleName() + "].");
			return false;
		}
		NPC_ACTION_EVENTS.put(npcId, event);
		return true;
	}

	/**
	 * Registers a new {@code ObjectEvent} for an object id.
	 *
	 * @param objId The object id.
	 * @param event The object action event to register.
	 * @return {@code True} if succesful.
	 */
	public static boolean register(int objId, ObjectEvent event) {
		if (OBJECT_EVENTS.containsKey(objId)) {
			System.err.println("Could not register object action event [Already registered key: " + objId + ", name=" + event.getClass().getSimpleName() + ", old=" + OBJECT_EVENTS.get(objId).getClass().getSimpleName() + "].");
			return false;
		}
		OBJECT_EVENTS.put(objId, event);
		return true;
	}

	/**
	 * Registers a new {@code ItemActionEvent} for an object id.
	 *
	 * @param objId The item id.
	 * @param event The item action event to register.
	 * @return {@code True} if succesful.
	 */
	public static boolean register(int itemId, ItemActionEvent event) {
		if (ITEM_ACTION_EVENTS.containsKey(itemId)) {
			System.err.println("Could not register item action event [Already registered key: " + itemId + ", name=" + event.getClass().getSimpleName() + ", old=" + ITEM_ACTION_EVENTS.get(itemId).getClass().getSimpleName() + "].");
			return false;
		}
		ITEM_ACTION_EVENTS.put(itemId, event);
		return true;
	}

}
