package com.sevador.game.dialogue;

import java.util.HashMap;

import com.sevador.Main;

public final class DialogueHandler {

	@SuppressWarnings({ "unchecked" })
	public static final void init() {
		try {
			handledDialogues.put("Mandrith", Class.forName("com.sevador.game.dialogue.impl.Mandrith"));
			handledDialogues.put("SimpleMessage", Class.forName("com.sevador.game.dialogue.impl.SimpleMessage"));
			handledDialogues.put("BankerEvent", Class.forName("com.sevador.game.dialogue.impl.Banker"));
			handledDialogues.put("Roddeck", Class.forName("com.sevador.game.dialogue.impl.Roddeck"));
			handledDialogues.put("CookingD", Class.forName("com.sevador.game.dialogue.impl.CookingDialogue"));
			handledDialogues.put("FletchingD", Class.forName("com.sevador.game.dialogue.impl.FletchingD"));
			handledDialogues.put("SlayerDialogue", Class.forName("com.sevador.game.dialogue.impl.SlayerDialogue"));
			handledDialogues.put("SlayerGem", Class.forName("com.sevador.game.dialogue.impl.SlayerGem"));
			handledDialogues.put("ZarosAltar", Class.forName("com.sevador.game.dialogue.impl.ZarosAltar"));
			handledDialogues.put("AncientAltar", Class.forName("com.sevador.game.dialogue.impl.AncientAltar"));
			handledDialogues.put("LunarAltar", Class.forName("com.sevador.game.dialogue.impl.LunarAltar"));
			handledDialogues.put("TeleportCrystal", Class.forName("com.sevador.game.dialogue.impl.TeleportCrystal"));
			handledDialogues.put("StartDialogue", Class.forName("com.sevador.game.dialogue.impl.StartDialogue"));
			handledDialogues.put("MrEx", Class.forName("com.sevador.game.dialogue.impl.MrEx"));
			handledDialogues.put("BarrowsD", Class.forName("com.sevador.game.dialogue.impl.BarrowsD"));
			handledDialogues.put("BarrowsRewards", Class.forName("com.sevador.game.dialogue.impl.BarrowsRewards"));
			handledDialogues.put("GEOffer", Class.forName("com.sevador.game.dialogue.impl.GEOffer"));
			handledDialogues.put("HerbloreD", Class.forName("com.sevador.game.dialogue.impl.HerbloreD"));
			handledDialogues.put("MakeOverMage", Class.forName("com.sevador.game.dialogue.impl.MakeOverMage"));
			handledDialogues.put("FinancialAdvisor", Class.forName("com.sevador.game.dialogue.impl.FinancialAdvsior"));
			handledDialogues.put("CooksAssistantChat", Class.forName("com.sevador.game.dialogue.impl.CooksAssistantChat"));
			handledDialogues.put("MaxSkillcape", Class.forName("com.sevador.game.dialogue.impl.MaxSkillcape"));
			Main.getLogger().info("Loaded " + handledDialogues.size()+ " dialogues successfully.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static final void reload() {
		handledDialogues.clear();
		init();
	}

	@SuppressWarnings("rawtypes")
	public static final Dialogue getDialogue(Object key) {
		Class classD = (Class) handledDialogues.get(key);
		if (classD == null)
			return null;
		try {
			return (Dialogue) classD.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private DialogueHandler() {
	}

	@SuppressWarnings("rawtypes")
	private static final HashMap handledDialogues = new HashMap();

}
