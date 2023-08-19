package com.sevador.game.node.model.combat.form;

import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;

/**
 * Represents the range data for a ranged combat action cycle.
 * @author Emperor
 *
 */
public final class RangeData {

	/**
	 * If the mob is a player.
	 */
	private final boolean player;
	
	/**
	 * The range weapon used.
	 */
	private RangeWeapon weapon;
	
	/**
	 * The ammunition used.
	 */
	private Ammunition ammo;

	/**
	 * The projectile to send.
	 */
	private Projectile projectile;
	
	/**
	 * The animation id.
	 */
	private Animation animation;
	
	/**
	 * The graphics id.
	 */
	private Graphic graphics;
	
	/**
	 * Constructs a new {@code RangeData} {@code Object}.
	 * @param player If the mob is a player.
	 */
	public RangeData(boolean player) {
		this.player = player;
	}

	/**
	 * @return the player
	 */
	public boolean isPlayer() {
		return player;
	}

	/**
	 * @return the weapon
	 */
	public RangeWeapon getWeapon() {
		return weapon;
	}

	/**
	 * @param weapon the weapon to set
	 */
	public void setWeapon(RangeWeapon weapon) {
		this.weapon = weapon;
	}

	/**
	 * @return the ammo
	 */
	public Ammunition getAmmo() {
		return ammo;
	}

	/**
	 * @param ammo the ammo to set
	 */
	public void setAmmo(Ammunition ammo) {
		this.ammo = ammo;
	}

	/**
	 * @return the projectile
	 */
	public Projectile getProjectile() {
		return projectile;
	}

	/**
	 * @param projectile the projectile to set
	 */
	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}

	/**
	 * @return the animation
	 */
	public Animation getAnimation() {
		return animation;
	}

	/**
	 * @param animation the animation to set
	 */
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	/**
	 * @return the graphics
	 */
	public Graphic getGraphics() {
		return graphics;
	}

	/**
	 * @param graphics the graphics to set
	 */
	public void setGraphics(Graphic graphics) {
		this.graphics = graphics;
	}
}
