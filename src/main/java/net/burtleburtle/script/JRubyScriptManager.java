package net.burtleburtle.script;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.jruby.exceptions.RaiseException;

import com.sevador.game.node.model.combat.TypeHandler;

/**
 * The JRuby script manager.
 * 
 * @author Emperor
 * @author Graham Edgecombe
 */
public class JRubyScriptManager {

	/**
	 * The Script Engine manager used.
	 */
	private static final ScriptEngineManager m = new ScriptEngineManager();

	/**
	 * The item action scripts.
	 */
	public static final Map<Integer, ItemActionScript> ITEM_ACTIONS = new HashMap<Integer, ItemActionScript>();

	/**
	 * The action button scripts.
	 */
	public static final Map<Integer, ActionButtonScript> ACTION_BUTTONS = new HashMap<Integer, ActionButtonScript>();

	/**
	 * The special attack type handler scripts.
	 */
	public static final Map<Integer, TypeHandler> SPECIAL_ATTACKS = new HashMap<Integer, TypeHandler>();

	/**
	 * The ScriptEngine used.
	 */
	public static final ScriptEngine ENGINE = m.getEngineByName("jruby");

	/**
	 * Initializes the Script Manager.
	 */
	public static void init() {
		 System.out.println("Loading JRuby scripts...");
		try {
			ITEM_ACTIONS.clear();
			SPECIAL_ATTACKS.clear();
			ACTION_BUTTONS.clear(); // So we can reload while running.
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					"./data/scripts/initializer.rb"));
			ENGINE.eval(isr);
			ENGINE.eval("Initializer.new");
			isr.close();
		} catch (RaiseException t) {
			t.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		int itemActions = ITEM_ACTIONS.size();
		int SPECS = SPECIAL_ATTACKS.size();
		int actionButtons = ACTION_BUTTONS.size();
		System.out.println("Loaded " + (itemActions + SPECS + actionButtons)
				+ " JRuby scripts.");
	}

	/**
	 * Attempts to invoke a javascript.
	 * 
	 * @param identifier
	 *            The method name.
	 * @param args
	 *            The parameters.
	 * @return The {@code return type} if succeeded, {@code Boolean.FALSE} if
	 *         not.
	 */
	public static Object invokeScript(String identifier, Object... args) {
		Invocable invEngine = (Invocable) ENGINE;
		try {
			return invEngine.invokeFunction(identifier, args);
		} catch (Throwable ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
