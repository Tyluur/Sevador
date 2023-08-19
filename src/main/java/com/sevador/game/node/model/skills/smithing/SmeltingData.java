package com.sevador.game.node.model.skills.smithing;

import java.util.HashMap;
import java.util.Map;

import com.sevador.game.node.Item;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class SmeltingData {
	
	public enum SmeltingBar {

		BRONZE(1, 6.2, new Item[] { new Item(436), new Item(438) }, new Item(
				2349), 0),

		BLURITE(8, 8.0, new Item[] { new Item(668) }, new Item(9467), 1),

		IRON(15, 12.5, new Item[] { new Item(440) }, new Item(2351), 2),

		SILVER(20, 13.7, new Item[] { new Item(442) }, new Item(2355), 3),

		STEEL(30, 17.5, new Item[] { new Item(440), new Item(453, 2) },
				new Item(2353), 4),

		GOLD(40, 22.5, new Item[] { new Item(444) }, new Item(2357), 5),

		MITHRIL(50, 30, new Item[] { new Item(447), new Item(453, 4) },
				new Item(2359), 6),

		ADAMANT(70, 37.5, new Item[] { new Item(449), new Item(453, 6) },
				new Item(2361), 7),

		RUNE(85, 50, new Item[] { new Item(451), new Item(453, 8) }, new Item(
				2363), 8),

		DRAGONBANE(80, 50, new Item[] { new Item(21779) }, new Item(21783, 1),
				9),

		WALLASALKIBANE(80, 50, new Item[] { new Item(21780) }, new Item(21784,
				1), 10),

		BASILISKBANE(80, 50, new Item[] { new Item(21781) },
				new Item(21785, 1), 11), ABYSSSALBANE(80, 50,
				new Item[] { new Item(21782) }, new Item(21786, 1), 11);

		private static Map<Integer, SmeltingBar> bars = new HashMap<Integer, SmeltingBar>();

		public static SmeltingBar forId(int buttonId) {
			return bars.get(buttonId);
		}

		static {
			for (SmeltingBar bar : SmeltingBar.values()) {
				bars.put(bar.getButtonId(), bar);
			}
		}

		private int levelRequired;
		private double experience;
		private Item[] itemsRequired;
		private int buttonId;
		private Item producedBar;

		private SmeltingBar(int levelRequired, double experience,
				Item[] itemsRequired, Item producedBar, int buttonId) {
			this.levelRequired = levelRequired;
			this.experience = experience;
			this.itemsRequired = itemsRequired;
			this.producedBar = producedBar;
			this.buttonId = buttonId;
		}

		public Item[] getItemsRequired() {
			return itemsRequired;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public Item getProducedBar() {
			return producedBar;
		}

		public double getExperience() {
			return experience;
		}

		public int getButtonId() {
			return buttonId;
		}
	}

}
