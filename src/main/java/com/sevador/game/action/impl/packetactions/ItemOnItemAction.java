package com.sevador.game.action.impl.packetactions;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.skills.firemaking.Firemaking;
import com.sevador.game.node.model.skills.fletch.Fletching;
import com.sevador.game.node.model.skills.fletch.FletchingData.Fletch;
import com.sevador.game.node.model.skills.herblore.Herblore;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class ItemOnItemAction extends Action {

	private Item firstItem, secondItem;
	/**
	 * The action's type-flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();

	public ItemOnItemAction(Entity entity, Item firstItem, Item secondItem) {
		super(entity);
		this.firstItem = firstItem;
		this.secondItem = secondItem;
	}

	@Override
	public boolean execute() {
		int itemUsed = firstItem.getId();
		int itemUsedWith = secondItem.getId();
		if (Firemaking.isFiremaking(entity.getPlayer(), new Item(itemUsed),
				new Item(itemUsedWith)))
			return true;
		Fletch fletch = Fletching.isFletching(new Item(itemUsedWith), new Item(
				itemUsed));
		if (fletch != null) {
			entity.getPlayer().getDialogueManager().startDialogue("FletchingD", fletch);
			return true;
		}
		int herblore = Herblore.isHerbloreSkill(new Item(itemUsed), new Item(itemUsedWith));
		int herblore2 = Herblore.isHerbloreSkill(new Item(itemUsedWith), new Item(itemUsed));
		if (herblore > -1 || herblore2 > -1) {
			entity.getPlayer().getDialogueManager().startDialogue("HerbloreD", herblore, new Item(itemUsed), new Item(itemUsedWith));
			return true;
		}
		if (itemUsed == 13736) {
			switch (itemUsedWith) {
			case 13752: // spectral
				entity.getPlayer().getInventory()
						.remove(new Item(itemUsedWith, 1));
				entity.getPlayer().getInventory().remove(new Item(itemUsed, 1));
				entity.getPlayer().getInventory().add(new Item(13744, 1));
				return true;
			case 13750: // elysian
				entity.getPlayer().getInventory()
						.remove(new Item(itemUsedWith, 1));
				entity.getPlayer().getInventory().remove(new Item(itemUsed, 1));
				entity.getPlayer().getInventory().add(new Item(13742, 1));
				return true;
			case 13748: // divine
				entity.getPlayer().getInventory()
						.remove(new Item(itemUsedWith, 1));
				entity.getPlayer().getInventory().remove(new Item(itemUsed, 1));
				entity.getPlayer().getInventory().add(new Item(13740, 1));
				return true;
			case 13746: // arcane
				entity.getPlayer().getInventory()
						.remove(new Item(itemUsedWith, 1));
				entity.getPlayer().getInventory().remove(new Item(itemUsed, 1));
				entity.getPlayer().getInventory().add(new Item(13738, 1));
				return true;
			}
		}
		if (itemUsedWith == 13736) {
			switch (itemUsed) {
			case 13746:// arcane
				entity.getPlayer().getInventory()
						.remove(new Item(itemUsedWith, 1));
				entity.getPlayer().getInventory().remove(new Item(itemUsed, 1));
				entity.getPlayer().getInventory().add(new Item(13738, 1));
				return true;
			case 13748: // divine
				entity.getPlayer().getInventory()
						.remove(new Item(itemUsedWith, 1));
				entity.getPlayer().getInventory().remove(new Item(itemUsed, 1));
				entity.getPlayer().getInventory().add(new Item(13740, 1));
				return true;
			case 13750: // elysian
				entity.getPlayer().getInventory()
						.remove(new Item(itemUsedWith, 1));
				entity.getPlayer().getInventory().remove(new Item(itemUsed, 1));
				entity.getPlayer().getInventory().add(new Item(13742, 1));
				return true;
			case 13752: // spectral
				entity.getPlayer().getInventory()
						.remove(new Item(itemUsedWith, 1));
				entity.getPlayer().getInventory().remove(new Item(itemUsed, 1));
				entity.getPlayer().getInventory().add(new Item(13744, 1));
				return true;
			}
		}
		if (itemUsed == 11690) {
			switch (itemUsedWith) {
			case 11704:
				entity.getPlayer().getInventory()
						.remove(new Item(itemUsedWith, 1));
				entity.getPlayer().getInventory().remove(new Item(itemUsed, 1));
				entity.getPlayer().getInventory().add(new Item(11696, 1));
				entity.getPlayer().getPacketSender().sendMessage(
						"You have made yourself a bandos godsword!");
				return true;
			case 11702:
				entity.getPlayer().getInventory()
						.remove(new Item(itemUsedWith, 1));
				entity.getPlayer().getInventory().remove(new Item(itemUsed, 1));
				entity.getPlayer().getInventory().add(new Item(11694, 1));
				entity.getPlayer().getPacketSender().sendMessage(
						"You have made yourself an armadyl godsword!");
				return true;
			case 11706:
				entity.getPlayer().getInventory()
						.remove(new Item(itemUsedWith, 1));
				entity.getPlayer().getInventory().remove(new Item(itemUsed, 1));
				entity.getPlayer().getInventory().add(new Item(11698, 1));
				entity.getPlayer().getPacketSender().sendMessage(
						"You have made yourself a saradomin godsword!");
				return true;
			case 11708:
				entity.getPlayer().getInventory()
						.remove(new Item(itemUsedWith, 1));
				entity.getPlayer().getInventory().remove(new Item(itemUsed, 1));
				entity.getPlayer().getInventory().add(new Item(11700, 1));
				entity.getPlayer().getPacketSender().sendMessage(
						"You have made yourself a zamork godsword!");
				return true;
			}
		}
		switch (itemUsedWith) {
		case 11710:
		case 11712:
		case 11714:
			switch (itemUsed) {
			case 11710:
			case 11712:
			case 11714:
				if (entity.getPlayer().getInventory().contains(11710, 1)
						&& entity.getPlayer().getInventory().contains(11712, 1)
						&& entity.getPlayer().getInventory().contains(11714, 1)) {
					entity.getPlayer().getInventory()
							.remove(new Item(11710, 1));
					entity.getPlayer().getInventory()
							.remove(new Item(11712, 1));
					entity.getPlayer().getInventory()
							.remove(new Item(11714, 1));
					entity.getPlayer().getInventory().add(new Item(11690, 1));
				} else {
					entity.getPlayer()
							.getPacketSender().sendMessage(
									"You need all 3 godsword shards to make a godsword blade.");
				}
				return true;
			}
			return true;
		}
		return true;
	}

	@Override
	public int getActionType() {
		return FLAG;
	}

}
