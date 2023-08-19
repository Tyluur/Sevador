package com.sevador.game.node.model.combat.impl;

import java.util.ArrayList;
import java.util.List;

import net.burtleburtle.thread.MajorUpdateWorker;

import com.sevador.game.event.EventManager;
import com.sevador.game.misc.MagicStaff;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.combat.form.MagicSpell;
import com.sevador.game.node.model.combat.form.NPCMagicFormula;
import com.sevador.game.node.model.container.impl.EquipmentContainerListener;
import com.sevador.game.node.model.skills.Experience.Spells;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Constants;
import com.sevador.utility.WeaponInterface;

/**
 * Handles the magic combat style.
 * @author Emperor
 *
 */
public class MagicHandler implements TypeHandler {

	@Override
	public boolean init() {
		return true;
	}

	@Override
	public boolean handle(Interaction i) {
		MagicSpell spell;
		if (i.source.isPlayer()) {
			if (i.source.getAttribute("spellId", -1) > -1) {
				spell = EventManager.getSpellEvent(i.source.getAttribute("spellId", -1));
				i.source.setAttribute("spellId", -1);
				if (i.source.getAttribute("autocastId", -1) == -1) {
					i.source.getCombatAction().setType(EquipmentContainerListener.getCombatType(i.source.getPlayer()));
					i.source.getCombatAction().setVictim(null);
				}
			} else {
				spell = EventManager.getSpellEvent(i.source.getAttribute("autocastId", -1));
			}
		} else {
			spell = EventManager.getSpellEvent(-5);
		}
		i.ticks = 1 + (int) Math.floor(i.source.getLocation().distance(i.victim.getLocation()) * 0.5);
		if (spell != null && i.source.isPlayer()) {
			if (!checkRunes(i.source.getPlayer(), spell.getRunes(), true)) {
				i.source.getPlayer().getIOSession().write(new MessagePacket(i.source.getPlayer(), "You do not have enough runes to cast this spell."));
				return false;
			}
			for (Spells spells : Spells.values()) 
				if (spell.getAutocastConfig() == spells.getConfig())
					if (i.source.getPlayer().getSkills().getLevel(Skills.MAGIC) < spells.getRequiredLevel())  {
						i.source.getPlayer().getPacketSender().sendMessage("You need a magic level of " + spells.getRequiredLevel() + " to cast " + spells.name().replaceAll("_", " ").toLowerCase()+ ".");
						return false;
					}
		} else if (spell == null) {
			return false;
		}
		i.source.getCombatAction().increaseTicks((MajorUpdateWorker.getTicks() + 5) - i.source.getCombatAction().getTicks(), true);
		return spell.handle(i);
	}

	@Override
	public double getAccuracy(Entity e, Object...args) {
		if (e.isNPC()) {
			return NPCMagicFormula.SINGLETON.getAccuracy(e);
		}
		int magicLevel = e.getSkills().getLevel(Skills.MAGIC) + 1;
		int magicBonus = e.getProperties().getStats()[3];
		double prayer = 1.0 + e.getPrayer().getMod(Skills.MAGIC) + e.getProperties().getCursedModifiers()[Skills.MAGIC];
		double accuracy = ((magicLevel + (magicBonus * 4)) * 1.4) * prayer;
		if (args.length > 0) {
			MagicSpell spell = (MagicSpell) args[0];
			accuracy += (spell.getNormalDamage() + spell.getBaseDamage()) >> 1;
		}
		return accuracy < 1 ? 1 : accuracy;
	}

	@Override
	public double getMaximum(Entity e, Object...args) {
		MagicSpell spell = (MagicSpell) args[0];
		Entity victim = (Entity) args[1];
		int damage = spell.getStartDamage(e, victim);
		double multiplier = 1;
		multiplier *= (e.getProperties().getStats()[14] * 0.01) + 1;
		if (e.isPlayer())
			multiplier *= e.getPlayer().getAuraManager().getMagicAccurayMultiplier();
		if (e.getSkills().getLevel(Skills.MAGIC) > e.getSkills().getStaticLevel(Skills.MAGIC)) {
			multiplier *= 1 + ((e.getSkills().getLevel(Skills.MAGIC) - e.getSkills().getStaticLevel(Skills.MAGIC)) * 0.03);
		}
		//TODO: Slayer helm/HexCreft helm multiplier on slayer tasks.
		if (e.isPlayer()) {
			if (victim.isNPC() && victim.getNPC().getId() == 9463) {
				boolean isFireSpell = spell.getClass().getSimpleName().contains("Fire");
				if (e.getPlayer().getEquipment().getItem(Constants.SLOT_CAPE).getId() == 6570 && isFireSpell) {
					damage += 40;
					multiplier *= 2.0;
				} else if (isFireSpell) {
					multiplier *= 1.5;
				} else if (e.getPlayer().getEquipment().getItem(Constants.SLOT_CAPE).getId() == 6570) {
					damage += 40;
				}
			}
			if (e.getAttribute("set-Void:magic", false)) {
				multiplier += .1;
			}
		}
		return damage * multiplier;
	}

	@Override
	public double getDefence(Entity e, int attackBonus, Object...args) {
		int style = 0;
		if (e.getProperties().getAttackStyle() == WeaponInterface.STYLE_DEFENSIVE) {
			style = 3;
		} else if (e.getProperties().getAttackStyle() == WeaponInterface.STYLE_CONTROLLED) {
			style = 1;
		}
		double defLvl = e.getSkills().getLevel(Skills.DEFENCE) * 0.3;
		defLvl += e.getSkills().getLevel(Skills.MAGIC) * 0.7;
		int defBonus = e.getProperties().getStats()[8];
		double defMult = 1.0 + e.getPrayer().getMod(Skills.DEFENCE) + e.getProperties().getCursedModifiers()[Skills.DEFENCE];
		/*TODO: if (victim instanceof MetalDragon && spell.getClass().getSimpleName().contains("Fire")) {
        	defMult -= 0.5;
        }*/
		double defence = ((defLvl + (defBonus * 4)) + style) * defMult;
		return defence < 1 ? 1 : defence;
	}

	/**
	 * Checks if we have the runes required for this spell.
	 * @param player The player casting the spell.
	 * @param runes The runes needed.
	 * @param remove If we should remove the items.
	 * @return {@code True} if the player has the runes, {@code false} if not.
	 */
	public static boolean checkRunes(Player player, Item[] runes, boolean remove) {
		if (runes == null) {
			return true;
		}
		List<Item> toRemove = new ArrayList<Item>();
		for (Item item : runes) {
			if (!hasStaff(player, item.getId())) {
				if (!player.getInventory().contains(item.getId(), item.getAmount())) {
					return false;
				}
				toRemove.add(item);
			}
		}
		if (remove) {
			Item weapon = player.getEquipment().getItem(3);
			if (weapon != null && weapon.getDefinition().name.startsWith("Staff of light")) {
				if (player.getRandom().nextInt(8) == 0) {
					player.getIOSession().write(new MessagePacket(player, "Your spell draws its power completely from your staff."));
					return true;
				}
			}
			player.getInventory().remove(toRemove.toArray(new Item[0]));
		}
		return true;
	}

	/**
	 * Checks if the player is wearing the correct staff.
	 * @param player The player.
	 * @param rune The rune item id.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public static boolean hasStaff(Player player, int rune) {
		Item weapon = player.getEquipment().get(3);
		if (weapon == null) {
			return false;
		}
		MagicStaff staff = MagicStaff.forId(rune);
		if (staff == null) {
			return false;
		}
		int[] staves = staff.getStaves();
		for (int id : staves) {
			if (weapon.getId() == id) {
				return true;
			}
		}
		return false;
	}
}