package com.sevador.game.node.model.skills.slayer;

/**
 *
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public enum SlayerRequirements {

	DUST_DEVIL("Dust devil", 65),
	ABERRANT_SPECTRE("Aberrant spectre", 60),
	GARGOYLE("Gargoyle", 75),
	NECHRAEL("Nechryael", 80),
	ABYSSAL_DEMON("Abyssal demon", 85),
	ICE_STRYKEWYRM("Ice strykewyrm", 93);

	private String name;
	private int req;

	private SlayerRequirements(String name, int req) {
		this.name = name;
		this.req = req;
	}

	public int getReq() {
		return req;
	}

	public String getName() {
		return name;
	}

	public static SlayerRequirements getReqs(String name) {
		for (SlayerRequirements req : SlayerRequirements.values()) {
			if (req.name.equalsIgnoreCase(name))
				return req;
		}
		return null;
	}

}
