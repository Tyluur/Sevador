package com.sevador.game.event.item;

import java.util.HashMap;
import java.util.Map;

import net.burtleburtle.thread.MajorUpdateWorker;
import net.burtleburtle.tick.Tick;
import net.burtleburtle.tick.TypeTick;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ItemActionEvent;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.world.World;
import com.sevador.utility.Constants;
import com.sevador.utility.OptionType;
import com.sevador.utility.Priority;

/**
 * Represents a food-item action event.
 * @author Emperor
 *
 */
public final class FoodActionEvent implements ItemActionEvent {

	/**
	 * The mapping of food types.
	 */
	private static final Map<Integer, FoodType> FOOD_TYPES = new HashMap<Integer, FoodType>();
	
	/**
	 * The eating animation.
	 */
	private static final Animation ANIMATION = new Animation(829, 0, false, Priority.HIGH);
	
	/**
	 * Represents a food type.
	 * @author Emperor
	 *
	 */
	private static enum FoodType {
		
		/**
		 * The crayfish food type.
		 */
		CRAYFISH(13433, 20, null, null),
		
		/**
		 * The anchovies food type.
		 */
		ANCHOVIES(319, 10, null, null),
		
		/**
		 * The garden pie food type.
		 */
		GARDEN_PIE(7178, 60, new Item(7180, 1), new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				arg[0].getSkills().updateLevel(Skills.FARMING, 3, arg[0].getSkills().getStaticLevel(Skills.FARMING) + 3);
				return true;
			}			
		}),
		
		/**
		 * The garden pie food type.
		 */
		HALF_GARDEN_PIE(7180, 60, new Item(2313, 1), new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				arg[0].getSkills().updateLevel(Skills.FARMING, 3, arg[0].getSkills().getStaticLevel(Skills.FARMING) + 3);
				return true;
			}			
		}),
		
		/**
		 * Represents the shrimp food type.
		 */
		SHRIMP(315, 30, null, null),
		
		/**
		 * Represents the cooked meat food type.
		 */
		COOKED_MEAT(2142, 30, null, null),
		
		/**
		 * Represents the sardine food type.
		 */
		SARDINE(325, 40, null, null),
		
		/**
		 * Represents the bread food type.
		 */
		BREAD(2309, 50, null, null),
		
		/**
		 * Represents the herring food type.
		 */
		HERRING(347, 50, null, null),
		
		/**
		 * Represents the mackerel food type.
		 */
		MACKEREL(355, 60, null, null),
		
		/**
		 * Represents the trout food type.
		 */
		TROUT(333, 70, null, null),
		
		/**
		 * Represents the cod food type.
		 */
		COD(339, 70, null, null),
		
		/**
		 * Represents the pike food type.
		 */
		PIKE(351, 80, null, null),
		
		/**
		 * Represents the salmon food type.
		 */
		SALMON(329, 90, null, null),
		
		/**
		 * Represents the tuna food type.
		 */
		TUNA(361, 100, null, null),
		
		/**
		 * Represents the cake food type.
		 */
		CAKE(1891, 40, new Item(1893, 1), null),
		
		/**
		 * Represents the cake food type.
		 */
		PIECE_OF_CAKE(1893, 40, new Item(1895, 1), null),
		
		/**
		 * Represents the cake food type.
		 */
		SLICE_OF_CAKE(1895, 40, null, null),
		
		/**
		 * Represents the lobster food type.
		 */
		LOBSTER(379, 120, null, null),
		
		/**
		 * Represents the bass food type.
		 */
		BASS(365, 130, null, null),
		
		/**
		 * Represents the swordfish food type.
		 */
		SWORDFISH(373, 140, null, null),
		
		/**
		 * Represents the potato with cheese food type.
		 */
		POTATO_WITH_CHEESE(6705, 160, null, null),
		
		/**
		 * Represents the monkfish food type.
		 */
		MONKFISH(7946, 160, null, null),
		
		/**
		 * Represents the anchovy food type.
		 */
		ANCHOVY_PIZZA(2297, 90, new Item(2299, 1), null),
		
		/**
		 * Represents the anchovy food type.
		 */
		LAST_PIECE_OF_ANCHOVY_PIZZA(2299, 90, null, null),
		
		/**
		 * Represents the shark food type.
		 */
		SHARK(385, 200, null, null),
		
		/**
		 * Represents the cavefish food type.
		 */
		CAVEFISH(15266, 200, null, null),
		
		/**
		 * Represents the sea turtle food type.
		 */
		SEA_TURTLE(397, 210, null, null),
		
		/**
		 * Represents the pineapple pizza food type.
		 */
		PINEAPPLE_PIZZA(2301, 110, new Item(2303, 1), null),
		
		/**
		 * Represents the pineapple pizza food type.
		 */
		LAST_PIECE_OF_PINEAPPLE_PIZZA(2303, 110, null, null),
		
		/**
		 * Represents the manta ray food type.
		 */
		MANTA_RAY(391, 220, null, null),
		
		/**
		 * Represents the tuna potato food type.
		 */
		TUNA_POTATO(7060, 220, null, null),
		
		/**
		 * The wild pie food type.
		 */
		WILD_PIE(7208, 110, new Item(7210, 1), new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				arg[0].getSkills().updateLevel(Skills.RANGE, 4, arg[0].getSkills().getStaticLevel(Skills.RANGE) + 4);
				arg[0].getSkills().updateLevel(Skills.SLAYER, 5, arg[0].getSkills().getStaticLevel(Skills.SLAYER) + 5);
				return true;
			}			
		}),
		
		/**
		 * The wild pie food type.
		 */
		HALF_A_WILD_PIE(7210, 110, new Item(2313, 1), new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				arg[0].getSkills().updateLevel(Skills.RANGE, 4, arg[0].getSkills().getStaticLevel(Skills.RANGE) + 4);
				arg[0].getSkills().updateLevel(Skills.SLAYER, 5, arg[0].getSkills().getStaticLevel(Skills.SLAYER) + 5);
				return true;
			}			
		}),
		
		/**
		 * The summer pie food type.
		 */
		SUMMER_PIE(7218, 110, new Item(7220, 1), new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				arg[0].getSettings().increaseRunEnergy(20.0);
				arg[0].getSkills().updateLevel(Skills.AGILITY, 5, arg[0].getSkills().getStaticLevel(Skills.AGILITY) + 5);
				return true;
			}			
		}),
		
		/**
		 * The summer pie food type.
		 */
		HALF_A_SUMMER_PIE(7220, 110, new Item(2313, 1), new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				arg[0].getSettings().increaseRunEnergy(20.0);
				arg[0].getSkills().updateLevel(Skills.AGILITY, 5, arg[0].getSkills().getStaticLevel(Skills.AGILITY) + 5);
				return true;
			}			
		}),
		
		/**
		 * Represents the rocktail food type.
		 */
		ROCKTAIL(15272, 230, null, null),
		
		/**
		 * Represents the baron shark food type.
		 */
		BARON_SHARK(19948, 300, null, new TypeTick<Player>(0) {
			@Override
			public boolean run(Player... arg) {
				if (arg[0].getEquipment().getItem(Constants.SLOT_AMULET).getId() == 21526) {
					arg[0].getSkills().heal(50);
				}
				return true;
			}			
		}),
		
		/**
		 * Represents the Juju gumbo food type.
		 */
		JUJU_GUMBO(19949, 220, new Item(1923, 1), new TypeTick<Player>(0) {
			@Override
			public boolean run(final Player... arg) {
				World.getWorld().submit(new Tick(2) {
					int ticked = 0;
					@Override
					public boolean run() {
						arg[0].getSkills().heal(10);
						return ++ticked > 9;
					}					
				});
				return true;
			}			
		});

		/**
		 * Populate the mapping.
		 */
		static {
			for (FoodType type : FoodType.values()) {
				FOOD_TYPES.put(type.id, type);
			}
		}
		
		/**
		 * The item id.
		 */
		private final int id;
		
		/**
		 * The amount to heal with.
		 */
		private final int health;
		
		/**
		 * The extra effect.
		 */
		private final TypeTick<Player> effect;
		
		/**
		 * The item to replace the old item with.
		 */
		private final Item replace;
		
		/**
		 * Constructs a new {@code FoodType} {@code Object}.
		 * @param id The item id.
		 * @param health The amount to heal with.
		 * @param replace The item to replace with.
		 * @param effect The effect.
		 */
		private FoodType(int id, int health, Item replace, TypeTick<Player> effect) {
			this.id = id;
			this.health = health;
			this.replace = replace;
			this.effect = effect;
		}
	}
	
	@Override
	public boolean init() {
		for (FoodType type : FoodType.values()) {
			if (!EventManager.register(type.id, this)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean handle(Player player, Item item, int interfaceId, int slot, OptionType type) {
		if (player.getAttribute("foodDelay", -1) > MajorUpdateWorker.getTicks()) {
			return true;
		}
		FoodType food = FOOD_TYPES.get((int) item.getId());
		if (food != null) {
			player.getCombatAction().increaseTicks(3, true);
			player.setAttribute("foodDelay", MajorUpdateWorker.getTicks() + 3);
			player.getUpdateMasks().register(ANIMATION);
			player.getPacketSender().sendMessage("You eat the " + food.name().toLowerCase().replaceAll("_", " ") + ".");
			if (food.replace != null) {
				if (food.replace.getId() == 2313) {
					player.getPacketSender().sendMessage("You have finished your pie.");
				} else if (food.replace.getId() == 1923) { 
					player.getPacketSender().sendMessage("You have finished your juju gumbo.");
				} else if (food.replace.getDefinition().name.toLowerCase().contains("half") || food.replace.getDefinition().name.contains("1/")) {
					player.getPacketSender().sendMessage("You have one piece left.");
				} else if (food.replace.getDefinition().name.contains("2/")) {
					player.getPacketSender().sendMessage("You have two pieces left.");
				}
			}
			player.getInventory().replace(food.replace, slot);
			int left = player.getSkills().heal(food.health);
			if (food == FoodType.ROCKTAIL && left > 0) {
				if (left > 100) {
					left = 100;
				}
				player.getSkills().setLifepoints(player.getSkills().getLifepoints() + left);
			}
			if (food.effect != null) {
				food.effect.run(player);
			}
			return true;
		}
		return false;
	}

}