package com.sevador.game.node.model.combat;

import java.util.ArrayList;
import java.util.List;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.UpdateFlag;

/**
 * Represents a target.
 * @author Emperor
 *
 */
public class Target {

	/**
	 * The entity.
	 */
	public Entity entity;
	
	/**
	 * The damage.
	 */
	public Damage damage;

	/**
	 * The projectile.
	 */
	public final List<Projectile> projectile = new ArrayList<Projectile>();
	
	/**
	 * The graphics to cast on hit.
	 */
	public UpdateFlag graphic;
	
	/**
	 * The second graphics to cast on hit.
	 */
	public UpdateFlag graphic2;
	
	/**
	 * The animation to cast.
	 */
	public Animation animation;
	
	/**
	 * If the victim is deflecting.
	 */
	public boolean deflected;
	
	/**
	 * If the player is getting frozen (used for multi-ice attacks).
	 */
	public boolean frozen;
	
	/**
	 * Constructs a new {@code Target} {@code Object}.
	 * @param entity The victim.
	 */
	public Target(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Target)) {
			return false;
		}
		return ((Target) o).entity == entity;
	}
}