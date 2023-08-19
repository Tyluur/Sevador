package com.sevador.game.node.model.combat.form;

import java.util.Random;

import com.sevador.game.event.spell.NPCSpell;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.TypeHandler;

/**
 * Handles gaussian distributed randomizing of values.
 * @author Emperor
 *
 */
public class GaussianGen {
	
	/**
	 * The random instance.
	 */
	private static final Random RANDOM = new Random();
	
    /**
     * Gets the current damage to be dealt to the victim.
     * @param handler The type handler used for calculating current hit.
     * @param source The attacking Entity.
     * @param victim The Entity being attacked.
     * @return The current damage.
     */
    public static final int getDamage(TypeHandler handler, Entity source, Entity victim, Object[] arg) {
        return getDamage(handler, source, victim, handler.getAccuracy(source, arg), 
        		handler.getMaximum(source, arg), handler.getDefence(victim, source.getProperties().getAttackType(), arg));
    }
    
    /**
     * Gets the current damage to be dealt to the victim.
     * @param handler The type handler used for calculating current hit.
     * @param source The attacking Entity.
     * @param victim The Entity being attacked.
     * @param maximum The maximum hit.
     * @return The current damage.
     */
    public static final int getDamage(TypeHandler handler, Entity source, Entity victim, double maximum, Object...arg) {
        return getDamage(handler, source, victim, handler.getAccuracy(source, arg), maximum, handler.getDefence(victim, source.getProperties().getAttackType(), arg));
    }

    /**
     * Gets the current melee damage.
     *
     * @param source             The attacking Entity.
     * @param victim             The Entity being attacked.
     * @param accuracyMultiplier The amount to increase the accuracy with.
     * @param hitMultiplier      The amount to increase the hit with.
     * @param defenceMultiplier  The amount to increase the defence with.
     * @return The amount to hit.
     */
    public static int getDamage(TypeHandler handler, Entity source, Entity victim, double accuracy, double maximum, double defence) {
    	if (source.getAttribute("set-Verac", false) && source.getRandom().nextInt(50) < 15) {
    		return (int) maximum;
    	}
        double accuracyMod = getGaussian(0.5, accuracy);
        double defenceMod = getGaussian(0.5, defence);
        double mod = accuracyMod / (accuracyMod + defenceMod);
        if (accuracyMod > defenceMod) {
            return (int) getGaussian(mod, maximum);
        }
        return handler == CombatType.MAGIC.getHandler() || handler == NPCSpell.FORMULA ? -1 : 0;
    }
    
	/**
	 * Gets a gaussian distributed randomized value between 0 and the {@code maximum} value.
	 * <br>The mean (average) is maximum / 2.
	 * @param meanModifier The modifier used to determine the mean.
	 * @param maximum The maximum value.
	 * @return The randomized value.
	 */
	public static double getGaussian(double meanModifier, double maximum) {
		double mean = maximum * meanModifier;
		double deviation = mean * 1.79;
		double value = 0;
		do {
			value = Math.floor(mean + RANDOM.nextGaussian() * deviation);
		} while (value < 0 || value > maximum);
		return value;
	}
}