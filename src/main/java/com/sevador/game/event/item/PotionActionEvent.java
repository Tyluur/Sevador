package com.sevador.game.event.item;

import java.util.HashMap;
import java.util.Map;

import net.burtleburtle.thread.MajorUpdateWorker;
import net.burtleburtle.tick.TypeTick;

import com.sevador.game.action.impl.combat.OverloadAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.event.ItemActionEvent;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.utility.OptionType;
import com.sevador.utility.Priority;

/**
 * Represents a potion action event.
 * @author Emperor
 *
 */
public class PotionActionEvent implements ItemActionEvent {

	/**
	 * The mapping of food types.
	 */
	private static final Map<Integer, PotionType> POTION_TYPES = new HashMap<Integer, PotionType>();
	
	/**
	 * The eating animation.
	 */
	private static final Animation ANIMATION = new Animation(829, 0, false, Priority.HIGH);

	/**
	 * Represents the potion types.
	 * @author Emperor
	 *
	 */
	private static enum PotionType {
		
		SUPER_ATTACK_4(2436, new Item(145, 1), "Super attack potion", Skills.ATTACK, 5, .15, null),
		SUPER_ATTACK_3(145, new Item(147, 1), "Super attack potion", Skills.ATTACK, 5, .15, null),
		SUPER_ATTACK_2(147, new Item(149, 1), "Super attack potion", Skills.ATTACK, 5, .15, null),
		SUPER_ATTACK_1(149, new Item(229, 1), "Super attack potion", Skills.ATTACK, 5, .15, null),

		SUPER_STRENGTH_4(2440, new Item(157, 1), "Super strength potion", Skills.STRENGTH, 5, .15, null),
		SUPER_STRENGTH_3(157, new Item(159, 1), "Super strength potion", Skills.STRENGTH, 5, .15, null),
		SUPER_STRENGTH_2(159, new Item(161, 1), "Super strength potion", Skills.STRENGTH, 5, .15, null),
		SUPER_STRENGTH_1(161, new Item(229, 1), "Super strength potion", Skills.STRENGTH, 5, .15, null),

		SUPER_DEFENCE_4(2442, new Item(163, 1), "Super defence potion", Skills.DEFENCE, 5, .15, null),
		SUPER_DEFENCE_3(163, new Item(165, 1), "Super defence potion", Skills.DEFENCE, 5, .15, null),
		SUPER_DEFENCE_2(165, new Item(167, 1), "Super defence potion", Skills.DEFENCE, 5, .15, null),
		SUPER_DEFENCE_1(167, new Item(229, 1), "Super defence potion", Skills.DEFENCE, 5, .15, null),

		RANGING_POTION_4(2444, new Item(169, 1), "Ranging potion", Skills.RANGE, 4, .10, null),
		RANGING_POTION_3(169, new Item(171, 1), "Ranging potion", Skills.RANGE, 4, .10, null),
		RANGING_POTION_2(171, new Item(173, 1), "Ranging potion", Skills.RANGE, 4, .10, null),
		RANGING_POTION_1(173, new Item(229, 1), "Ranging potion", Skills.RANGE, 4, .10, null),

		MAGIC_POTION_4(3040, new Item(3042, 1), "Magic potion", Skills.MAGIC, 5, 0, null),
		MAGIC_POTION_3(3042, new Item(3044, 1), "Magic potion", Skills.MAGIC, 5, 0, null),
		MAGIC_POTION_2(3044, new Item(3046, 1), "Magic potion", Skills.MAGIC, 5, 0, null),
		MAGIC_POTION_1(3046, new Item(229, 1), "Magic potion", Skills.MAGIC, 5, 0, null),
		
		EXTREME_ATTACK_4(15308, new Item(15309, 1), "Extreme attack potion", Skills.ATTACK, 5, .23, null),
		EXTREME_ATTACK_3(15309, new Item(15310, 1), "Extreme attack potion", Skills.ATTACK, 5, .23, null),
		EXTREME_ATTACK_2(15310, new Item(15311, 1), "Extreme attack potion", Skills.ATTACK, 5, .23, null),
		EXTREME_ATTACK_1(15311, new Item(229, 1), "Extreme attack potion", Skills.ATTACK, 5, .23, null),

		EXTREME_STRENGTH_4(15312, new Item(15313, 1), "Extreme strength potion", Skills.STRENGTH, 5, .23, null),
		EXTREME_STRENGTH_3(15313, new Item(15314, 1), "Extreme strength potion", Skills.STRENGTH, 5, .23, null),
		EXTREME_STRENGTH_2(15314, new Item(15315, 1), "Extreme strength potion", Skills.STRENGTH, 5, .23, null),
		EXTREME_STRENGTH_1(15315, new Item(229, 1), "Extreme strength potion", Skills.STRENGTH, 5, .23, null),

		EXTREME_DEFENCE_4(15316, new Item(15317, 1), "Extreme defence", Skills.DEFENCE, 5, .23, null),
		EXTREME_DEFENCE_3(15317, new Item(15318, 1), "Extreme defence", Skills.DEFENCE, 5, .23, null),
		EXTREME_DEFENCE_2(15318, new Item(15319, 1), "Extreme defence", Skills.DEFENCE, 5, .23, null),
		EXTREME_DEFENCE_1(15319, new Item(229, 1), "Extreme defence", Skills.DEFENCE, 5, .23, null),	
		
		EXTREME_RANGING_4(15324, new Item(15325, 1), "Extreme ranging potion", Skills.RANGE, 4, .1923076923076923, null),
		EXTREME_RANGING_3(15325, new Item(15326, 1), "Extreme ranging potion", Skills.RANGE, 4, .1923076923076923, null),
		EXTREME_RANGING_2(15326, new Item(15327, 1), "Extreme ranging potion", Skills.RANGE, 4, .1923076923076923, null),
		EXTREME_RANGING_1(15327, new Item(229, 1), "Extreme ranging potion", Skills.RANGE, 4, .1923076923076923, null),

		EXTREME_MAGIC_4(15320, new Item(15321, 1), "Extreme magic potion", Skills.MAGIC, 7, 0, null),
		EXTREME_MAGIC_3(15321, new Item(15322, 1), "Extreme magic potion", Skills.MAGIC, 7, 0, null),
		EXTREME_MAGIC_2(15322, new Item(15323, 1), "Extreme magic potion", Skills.MAGIC, 7, 0, null),
		EXTREME_MAGIC_1(15323, new Item(229, 1), "Extreme magic potion", Skills.MAGIC, 7, 0, null),
		
		OVERLOAD_4(15332, new Item(15333, 1), "Overload", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				if (arg[0].getCredentials().getOverloadTicks() > 488) {
					arg[0].getPacketSender().sendMessage("You can't do that right now.");
					return false;
				} else if (arg[0].getSkills().getLifepoints() < 501) {
					arg[0].getPacketSender().sendMessage("Drinking this potion would kill you right now.");
					return false;
				}
				arg[0].getActionManager().register(new OverloadAction(arg[0], 499));
				return true;
			}			
		}),
		OVERLOAD_3(15333, new Item(15334, 1), "Overload", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				if (arg[0].getCredentials().getOverloadTicks() > 488) {
					arg[0].getPacketSender().sendMessage("You can't do that right now.");
					return false;
				} else if (arg[0].getSkills().getLifepoints() < 501) {
					arg[0].getPacketSender().sendMessage("Drinking this potion would kill you right now.");
					return false;
				}
				arg[0].getActionManager().register(new OverloadAction(arg[0], 499));
				return true;
			}			
		}),
		OVERLOAD_2(15334, new Item(15335, 1), "Overload", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				if (arg[0].getCredentials().getOverloadTicks() > 488) {
					arg[0].getPacketSender().sendMessage("You can't do that right now.");
					return false;
				} else if (arg[0].getSkills().getLifepoints() < 501) {
					arg[0].getPacketSender().sendMessage("Drinking this potion would kill you right now.");
					return false;
				}
				arg[0].getActionManager().register(new OverloadAction(arg[0], 499));
				return true;
			}			
		}),
		OVERLOAD_1(15335, new Item(229, 1), "Overload", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				if (arg[0].getCredentials().getOverloadTicks() > 488) {
					arg[0].getPacketSender().sendMessage("You can't do that right now.");
					return false;
				} else if (arg[0].getSkills().getLifepoints() < 501) {
					arg[0].getPacketSender().sendMessage("Drinking this potion would kill you right now.");
					return false;
				}
				arg[0].getActionManager().register(new OverloadAction(arg[0], 499));
				return true;
			}			
		}),
		
		SARADOMIN_BREW_4(6685, new Item(6687, 1), "foul liquid", Skills.DEFENCE, 0, 0.25, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				int heal = (int) (p.getSkills().getMaximumLifepoints() * .15);
				int left = p.getSkills().heal((int) (20 + heal));
				if (left > 0) {
					if (left > heal) {
						left = heal;
					}
					p.getSkills().setLifepoints(p.getSkills().getLifepoints() + left);
				}
				p.getSkills().updateLevel(Skills.ATTACK, -(p.getSkills().getLevel(Skills.ATTACK) / 10), 0);
				p.getSkills().updateLevel(Skills.STRENGTH, -(p.getSkills().getLevel(Skills.STRENGTH) / 10), 0);
				p.getSkills().updateLevel(Skills.MAGIC, -(p.getSkills().getLevel(Skills.MAGIC) / 10), 0);
				p.getSkills().updateLevel(Skills.RANGE, -(p.getSkills().getLevel(Skills.RANGE) / 10), 0);
				return true;
			}			
		}),
		SARADOMIN_BREW_3(6687, new Item(6689, 1), "foul liquid", Skills.DEFENCE, 0, 0.25, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				int heal = (int) (p.getSkills().getMaximumLifepoints() * .15);
				int left = p.getSkills().heal((int) (20 + heal));
				if (left > 0) {
					if (left > heal) {
						left = heal;
					}
					p.getSkills().setLifepoints(p.getSkills().getLifepoints() + left);
				}
				p.getSkills().updateLevel(Skills.ATTACK, -(p.getSkills().getLevel(Skills.ATTACK) / 10), 0);
				p.getSkills().updateLevel(Skills.STRENGTH, -(p.getSkills().getLevel(Skills.STRENGTH) / 10), 0);
				p.getSkills().updateLevel(Skills.MAGIC, -(p.getSkills().getLevel(Skills.MAGIC) / 10), 0);
				p.getSkills().updateLevel(Skills.RANGE, -(p.getSkills().getLevel(Skills.RANGE) / 10), 0);
				return true;
			}			
		}),
		SARADOMIN_BREW_2(6689, new Item(6691, 1), "foul liquid", Skills.DEFENCE, 0, 0.25, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				int heal = (int) (p.getSkills().getMaximumLifepoints() * .15);
				int left = p.getSkills().heal((int) (20 + heal));
				if (left > 0) {
					if (left > heal) {
						left = heal;
					}
					p.getSkills().setLifepoints(p.getSkills().getLifepoints() + left);
				}
				p.getSkills().updateLevel(Skills.ATTACK, -(p.getSkills().getLevel(Skills.ATTACK) / 10), 0);
				p.getSkills().updateLevel(Skills.STRENGTH, -(p.getSkills().getLevel(Skills.STRENGTH) / 10), 0);
				p.getSkills().updateLevel(Skills.MAGIC, -(p.getSkills().getLevel(Skills.MAGIC) / 10), 0);
				p.getSkills().updateLevel(Skills.RANGE, -(p.getSkills().getLevel(Skills.RANGE) / 10), 0);
				return true;
			}			
		}),
		SARADOMIN_BREW_1(6691, new Item(229, 1), "foul liquid", Skills.DEFENCE, 0, 0.25, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				int heal = (int) (p.getSkills().getMaximumLifepoints() * .15);
				int left = p.getSkills().heal((int) (20 + heal));
				if (left > 0) {
					if (left > heal) {
						left = heal;
					}
					p.getSkills().setLifepoints(p.getSkills().getLifepoints() + left);
				}
				p.getSkills().updateLevel(Skills.ATTACK, -(p.getSkills().getLevel(Skills.ATTACK) / 10), 0);
				p.getSkills().updateLevel(Skills.STRENGTH, -(p.getSkills().getLevel(Skills.STRENGTH) / 10), 0);
				p.getSkills().updateLevel(Skills.MAGIC, -(p.getSkills().getLevel(Skills.MAGIC) / 10), 0);
				p.getSkills().updateLevel(Skills.RANGE, -(p.getSkills().getLevel(Skills.RANGE) / 10), 0);
				return true;
			}			
		}),
		
		SUPER_RESTORE_4(3024, new Item(3026, 1), "super restore potion", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				for (int i = 0; i < 25; i++) {
					if (i != 5) {
						p.getSkills().updateLevel(i, (int) (p.getSkills().getStaticLevel(i) * 0.33), p.getSkills().getStaticLevel(i));
					}
				}
				int difference = (int) Math.ceil((p.getSkills().getStaticLevel(Skills.PRAYER) * 10) - p.getSkills().getPrayerPoints());
				if (difference > 0) {
					double restore = p.getSkills().getStaticLevel(Skills.PRAYER) * 3.3;
					if (restore > difference) {
						restore = difference;
					}
					p.getSkills().updatePrayerPoints(-restore);
				}
				return true;
			}			
		}),
		SUPER_RESTORE_3(3026, new Item(3028, 1), "super restore potion", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				for (int i = 0; i < 25; i++) {
					if (i != 5) {
						p.getSkills().updateLevel(i, (int) (p.getSkills().getStaticLevel(i) * 0.33), p.getSkills().getStaticLevel(i));
					}
				}
				int difference = (int) Math.ceil((p.getSkills().getStaticLevel(Skills.PRAYER) * 10) - p.getSkills().getPrayerPoints());
				if (difference > 0) {
					double restore = p.getSkills().getStaticLevel(Skills.PRAYER) * 3.3;
					if (restore > difference) {
						restore = difference;
					}
					p.getSkills().updatePrayerPoints(-restore);
				}
				return true;
			}			
		}),
		SUPER_RESTORE_2(3028, new Item(3030, 1), "super restore potion", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				for (int i = 0; i < 25; i++) {
					if (i != 5) {
						p.getSkills().updateLevel(i, (int) (p.getSkills().getStaticLevel(i) * 0.33), p.getSkills().getStaticLevel(i));
					}
				}
				int difference = (int) Math.ceil((p.getSkills().getStaticLevel(Skills.PRAYER) * 10) - p.getSkills().getPrayerPoints());
				if (difference > 0) {
					double restore = p.getSkills().getStaticLevel(Skills.PRAYER) * 3.3;
					if (restore > difference) {
						restore = difference;
					}
					p.getSkills().updatePrayerPoints(-restore);
				}
				return true;
			}			
		}),
		SUPER_RESTORE_1(3030, new Item(229, 1), "super restore potion", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				for (int i = 0; i < 25; i++) {
					if (i != 5) {
						p.getSkills().updateLevel(i, (int) (p.getSkills().getStaticLevel(i) * 0.33), p.getSkills().getStaticLevel(i));
					}
				}
				int difference = (int) Math.ceil((p.getSkills().getStaticLevel(Skills.PRAYER) * 10) - p.getSkills().getPrayerPoints());
				if (difference > 0) {
					double restore = p.getSkills().getStaticLevel(Skills.PRAYER) * 3.3;
					if (restore > difference) {
						restore = difference;
					}
					p.getSkills().updatePrayerPoints(-restore);
				}
				return true;
			}			
		}),
		
		PRAYER_POTION_4(2434, new Item(139, 1), "prayer potion", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				int difference = (int) Math.ceil((p.getSkills().getStaticLevel(Skills.PRAYER) * 10) - p.getSkills().getPrayerPoints());
				if (difference > 0) {
					double restore = 70 + p.getSkills().getStaticLevel(Skills.PRAYER) * 2.5;
					if (restore > difference) {
						restore = difference;
					}
					p.getSkills().updatePrayerPoints(-restore);
				}
				return true;
			}			
		}),
		PRAYER_POTION_3(139, new Item(141, 1), "prayer potion", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				int difference = (int) Math.ceil((p.getSkills().getStaticLevel(Skills.PRAYER) * 10) - p.getSkills().getPrayerPoints());
				if (difference > 0) {
					double restore = 70 + p.getSkills().getStaticLevel(Skills.PRAYER) * 2.5;
					if (restore > difference) {
						restore = difference;
					}
					p.getSkills().updatePrayerPoints(-restore);
				}
				return true;
			}			
		}),
		PRAYER_POTION_2(141, new Item(143, 1), "prayer potion", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				int difference = (int) Math.ceil((p.getSkills().getStaticLevel(Skills.PRAYER) * 10) - p.getSkills().getPrayerPoints());
				if (difference > 0) {
					double restore = 70 + p.getSkills().getStaticLevel(Skills.PRAYER) * 2.5;
					if (restore > difference) {
						restore = difference;
					}
					p.getSkills().updatePrayerPoints(-restore);
				}
				return true;
			}			
		}),
		PRAYER_POTION_1(143, new Item(229, 1), "prayer potion", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				int difference = (int) Math.ceil((p.getSkills().getStaticLevel(Skills.PRAYER) * 10) - p.getSkills().getPrayerPoints());
				if (difference > 0) {
					double restore = 70 + p.getSkills().getStaticLevel(Skills.PRAYER) * 2.5;
					if (restore > difference) {
						restore = difference;
					}
					p.getSkills().updatePrayerPoints(-restore);
				}
				return true;
			}			
		}),
		
		SUPER_PRAYER_POTION_4(15328, new Item(15329, 1), "super prayer potion", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				int difference = (int) Math.ceil((p.getSkills().getStaticLevel(Skills.PRAYER) * 10) - p.getSkills().getPrayerPoints());
				if (difference > 0) {
					double restore = 70 + p.getSkills().getStaticLevel(Skills.PRAYER) * 3.43;
					if (restore > difference) {
						restore = difference;
					}
					p.getSkills().updatePrayerPoints(-restore);
				}
				return true;
			}			
		}),
		SUPER_PRAYER_POTION_3(15329, new Item(15330, 1), "super prayer potion", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				int difference = (int) Math.ceil((p.getSkills().getStaticLevel(Skills.PRAYER) * 10) - p.getSkills().getPrayerPoints());
				if (difference > 0) {
					double restore = 70 + p.getSkills().getStaticLevel(Skills.PRAYER) * 3.43;
					if (restore > difference) {
						restore = difference;
					}
					p.getSkills().updatePrayerPoints(-restore);
				}
				return true;
			}			
		}),
		SUPER_PRAYER_POTION_2(15330, new Item(15331, 1), "super prayer potion", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				int difference = (int) Math.ceil((p.getSkills().getStaticLevel(Skills.PRAYER) * 10) - p.getSkills().getPrayerPoints());
				if (difference > 0) {
					double restore = 70 + p.getSkills().getStaticLevel(Skills.PRAYER) * 3.43;
					if (restore > difference) {
						restore = difference;
					}
					p.getSkills().updatePrayerPoints(-restore);
				}
				return true;
			}			
		}),
		SUPER_PRAYER_POTION_1(15331, new Item(229, 1), "super prayer potion", 0, 0, 0, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				Player p = arg[0];
				int difference = (int) Math.ceil((p.getSkills().getStaticLevel(Skills.PRAYER) * 10) - p.getSkills().getPrayerPoints());
				if (difference > 0) {
					double restore = 70 + p.getSkills().getStaticLevel(Skills.PRAYER) * 3.43;
					if (restore > difference) {
						restore = difference;
					}
					p.getSkills().updatePrayerPoints(-restore);
				}
				return true;
			}			
		});
	
		/**
		 * Populate the mapping.
		 */
		static {
			for (PotionType type : PotionType.values()) {
				POTION_TYPES.put(type.id, type);
			}
		}
		
		/**
		 * The item id.
		 */
		private final int id;
		
		/**
		 * The item to replace with.
		 */
		private final Item replace;
		
		/**
		 * The skill id.
		 */
		private final int skillId;
		
		/**
		 * The base increase amount.
		 */
		private final int baseIncrease;
		
		/**
		 * The multiplier of the skill level to increase with.
		 */
		private final double multiplier;
		
		/**
		 * The name.
		 */
		private final String name;
		
		/**
		 * The potion effect.
		 */
		private final TypeTick<Player> effect;
		
		/**
		 * Constructs a new {@code PotionType} {@code Object}.
		 * @param id The item id.
		 * @param replace The item to replace with.
		 * @param skillId The skill id.
		 * @param baseIncrease The base increase amount.
		 * @param multi The skill level multiplier.
		 * @param effect The extra effect.
		 */
		private PotionType(int id, Item replace, String name, int skillId, int baseIncrease, double multi, TypeTick<Player> effect) {
			this.id = id;
			this.replace = replace;
			this.name = name;
			this.skillId = skillId;
			this.baseIncrease = baseIncrease;
			this.multiplier = multi;
			this.effect = effect;
		}
		
	}
	
	@Override
	public boolean init() {
		for (PotionType type : PotionType.values()) {
			if (!EventManager.register(type.id, this)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean handle(Player player, Item item, int interfaceId, int slot, OptionType type) {
		if (player.getAttribute("potionDelay", -1) > MajorUpdateWorker.getTicks()) {
			return true;
		}
		PotionType potion = POTION_TYPES.get((int) item.getId());
		if (potion != null) {
			player.setAttribute("potionDelay", MajorUpdateWorker.getTicks() + 3);
			if (potion.effect == null || potion.effect.run(player)) {
				player.getUpdateMasks().register(ANIMATION);
				player.getInventory().replace(potion.replace, slot);
				int staticLevel = player.getSkills().getStaticLevel(potion.skillId);
				int amount = (int) (potion.baseIncrease + (staticLevel * potion.multiplier));
				player.getSkills().updateLevel(potion.skillId, amount, staticLevel + amount);
				player.getPacketSender().sendMessage("You drink some of your " + potion.name + ".");
			}
			return true;
		}
		return false;
	}

}