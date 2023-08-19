package com.sevador.game.minigames.pestcontrol;

/**
 *
 * @author Mystic Flow <steven@rune-server.org>
 */
public enum PestControlDifficulty {
	EASY(40),
	MEDIUM(70),
	HARD(100);

	private int minimumLevel;
	
	private PestControlDifficulty(int minimumLevel) {
		this.minimumLevel = minimumLevel;
	}

	public int getMinimumLevel() {
		return minimumLevel;
	}

}
