package com.sevador.game.minigames.pestcontrol;

import com.sevador.game.node.activity.Activity;
import com.sevador.game.node.activity.ActivityConstraint;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class PestControlConstraint implements ActivityConstraint {

	public static final PestControlConstraint INSTANCE = new PestControlConstraint();


	@Override
	public boolean constrains(Activity activity) {
		PestControlActivity pest = (PestControlActivity) activity;
		return !pest.active;
	}

}
