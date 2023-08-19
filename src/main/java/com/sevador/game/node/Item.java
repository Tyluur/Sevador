package com.sevador.game.node;

import java.io.Serializable;

import net.burtleburtle.cache.format.ItemDefinition;

/**
 * Represents an item.
 * @author Emperor
 *
 */
public class Item implements Serializable {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = -8656546217917859268L;

	/**
	 * The item id.
	 */
	private short id;

	/**
	 * The item definition.
	 */
	@SuppressWarnings("unused")
	private transient ItemDefinition definition;

	/**
	 * The item amount.
	 */
	private int amount;

	/**
	 * The charges left on this particular item (if any).
	 */
	private int charge;

	/**
	 * Constructs a new {@code Item} {@code Object}.
	 * @param id The item id.
	 */
	public Item(int id) {
		this(id, 1);
		this.definition = ItemDefinition.forId(id);
	}

	/**
	 * Constructs a new {@code Item} {@code Object}.
	 * @param id The item id.
	 * @param amount The amount of this item.
	 */
	public Item(int id, int amount) {
		this.id = (short) id;
		this.definition = ItemDefinition.forId(id);
		this.amount = amount;
	}

	@Override
	public String toString() {
		return new StringBuilder("item [").append(id).append(", ").append(amount).append("]").toString();
	}

	@Override
	public boolean equals(Object o) {
		return ((Item) o).id == id;
	}

	/**
	 * Gets the item id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the item's definition.
	 * @return The item definition.
	 */
	public ItemDefinition getDefinition() {
		return ItemDefinition.forId(id);
	}

	/**
	 * @param def The Item Definition you are going to set it to.
	 */

	public void setDefinition(ItemDefinition def) {
		this.definition = def;
	}

	/**
	 * Sets the amount of this item.
	 * @param amount The amount.
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * Gets the amount of this item.
	 * @return The amount.
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Sets te charge of this item.
	 * @param charge The amount of charges left.
	 */
	public void setCharge(int charge) {
		this.charge = charge;
	}

	/**
	 * Gets the charges left of this item.
	 * @return The charges left.
	 */
	public int getCharge() {
		return charge;
	}

	public double getWeight() {
		return ItemDefinition.forId(id).getWeight();
		/*File inputFile = new File("data/nodes/items/weights.txt");
		BufferedReader reader;
		String currentLine;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
			while((currentLine = reader.readLine()) != null) {
				String[] split = currentLine.split(" - ");
				if (Integer.parseInt(split[0]) == id && Constants.isInteger(split[1])) {
					reader.close();
					return Double.parseDouble(split[1]);
				}
			}
			reader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;*/
	}

	public void setId(short newId) {
		this.id = newId;
	}

}
