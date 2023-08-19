package com.sevador.game.node.model.combat;

import net.burtleburtle.tick.TypeTick;

import com.sevador.game.node.Node;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.Damage.DamageType;
import com.sevador.game.node.model.combat.impl.MagicHandler;
import com.sevador.game.node.model.combat.impl.MeleeHandler;
import com.sevador.game.node.model.combat.impl.RangeHandler;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.region.path.ProjectilePathFinder;

/**
 * Represents the combat types.
 * @author Emperor
 *
 */
public enum CombatType {

	/**
	 * The melee combat type.
	 */
	MELEE(new MeleeHandler(), DamageType.MELEE, PrayerType.PROTECT_FROM_MELEE, PrayerType.DEFLECT_MELEE, new TypeTick<Node>(0) {
		@Override
		public boolean run(Node... arg) {
			return arg[0].getLocation().getDistance(arg[1].getLocation()) < 1;
		}		
	}),
	
	/**
	 * The range combat type.
	 */
	RANGE(new RangeHandler(), DamageType.RANGE, PrayerType.PROTECT_FROM_MISSILES, PrayerType.DEFLECT_MISSILES, new TypeTick<Node>(0) {
		@Override
		public boolean run(Node... arg) {
			return arg[0].getLocation().getDistance(arg[1].getLocation()) < 9 && arg[0].getLocation() != arg[1].getLocation() && ProjectilePathFinder.findPath((Entity) arg[0], (Entity) arg[1]);
		}		
	}),
	
	/**
	 * The magic combat type.
	 */
	MAGIC(new MagicHandler(), DamageType.MAGIC, PrayerType.PROTECT_FROM_MAGIC, PrayerType.DEFLECT_MAGIC, new TypeTick<Node>(0) {
		@Override
		public boolean run(Node... arg) {
			return arg[0].getLocation().getDistance(arg[1].getLocation()) < 12 && arg[0].getLocation() != arg[1].getLocation() && ProjectilePathFinder.findPath((Entity) arg[0], (Entity) arg[1]);
		}		
	});
	
	/**
	 * The handler used to handle this combat type action.
	 */
	private final TypeHandler handler;
	
	/**
	 * The damage type for this combat type.
	 */
	private final DamageType type;
	
	/**
	 * The protect prayer.
	 */
	private final PrayerType protectPrayer;
	
	/**
	 * The deflect curse.
	 */
	private final PrayerType deflectCurse;
	
	/**
	 * A type tick used to check if one entity can attack the other one depending on the combat type.
	 */
	private final TypeTick<Node> interactDeterminator;
	
	/**
	 * Constructs a new {@code CombatType} {@code Object}.
	 * @param handler The type handler.
	 * @param type The damage type.
	 * @param protectPrayer The protection prayer.
	 * @param deflectCurse The deflect curse.
	 * @param interactDeterminator Used for checking if one entity can attack another one.
	 */
	private CombatType(TypeHandler handler, DamageType type, PrayerType protectPrayer, PrayerType deflectCurse, TypeTick<Node> interactDeterminator) {
		this.handler = handler;
		this.type = type;
		this.protectPrayer = protectPrayer;
		this.deflectCurse = deflectCurse;
		this.interactDeterminator = interactDeterminator;
	}

	/**
	 * @return the handler
	 */
	public TypeHandler getHandler() {
		return handler;
	}

	/**
	 * @return the type
	 */
	public DamageType getType() {
		return type;
	}

	/**
	 * @return the protectPrayer
	 */
	public PrayerType getProtectPrayer() {
		return protectPrayer;
	}

	/**
	 * @return the deflectCurse
	 */
	public PrayerType getDeflectCurse() {
		return deflectCurse;
	}

	/**
	 * Checks if the entity can attack the victim.
	 * @param entity The entity.
	 * @param victim The victim.
	 * @return {@code true} if so.
	 */
	public boolean canInteract(Entity entity, Node victim) {
		return interactDeterminator.run(entity, victim);
	}
	
}