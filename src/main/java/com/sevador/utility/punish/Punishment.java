package com.sevador.utility.punish;

/**
 * Represents a punishment.
 * @author Emperor
 *
 */
public class Punishment {

	/**
	 * The duration of the punishment.
	 */
	private long duration;

	/**
	 * The punishment type.
	 */
	private PunishType type;
	
	/**
	 * Constructs a new {@code Punishment} {@code Object}.
	 * @param type The punishment type.
	 * @param duration The duration of the punishment, in milliseconds.
	 */
	public Punishment(PunishType type, long duration) {
		this.duration = duration;
		this.type = type;
	}
	
	/**
	 * Checks if the punishment is permanent.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isPermanent() {
		return duration == -1;
	}
	
	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * @return the type
	 */
	public PunishType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(PunishType type) {
		this.type = type;
	}
}