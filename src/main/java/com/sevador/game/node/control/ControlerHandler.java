package com.sevador.game.node.control;

import java.util.HashMap;

import com.sevador.game.node.control.impl.Barrows;
import com.sevador.game.node.control.impl.DuelArenaControler;
import com.sevador.game.node.control.impl.StartControler;

public class ControlerHandler {

	private static final HashMap<Object, Class<Controler>> handledControlers = new HashMap<Object, Class<Controler>>();

	@SuppressWarnings("unchecked")
	public static final void init() {
		try {
			handledControlers.put("DuelArena", (Class<Controler>) Class.forName(DuelArenaControler.class.getCanonicalName()));
			handledControlers.put("StartControler", (Class<Controler>) Class.forName(StartControler.class.getCanonicalName()));
			handledControlers.put("Barrows", (Class<Controler>) Class.forName(Barrows.class.getCanonicalName()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static final void reload() {
		handledControlers.clear();
		init();
	}

	public static final Controler getControler(Object key) {
		if (key instanceof Controler)
			return (Controler) key;
		Class<Controler> classC = handledControlers.get(key);
		if (classC == null)
			return null;
		try {
			return classC.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
