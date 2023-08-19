package com.sevador.game.region;

import com.sevador.game.node.Item;
import com.sevador.game.node.Node;
import com.sevador.game.node.NodeType;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.player.Player;

/**
 * Represents a single ground item.
 *
 * @author Emperor
 */
public class GroundItem implements Node {

    /**
     * If the item is global.
     */
    private boolean isPublic;

    /**
     * If the item is removed.
     */
    private boolean removed;

    /**
     * The amount of update ticks.
     */
    private int updateTicks;

    /**
     * The item's location.
     */
    private final Location location;

    /**
     * The item's location.
     */
    private final Item item;

    /**
     * The player who dropped the item.
     */
    private final Player player;

    /**
     * The constructor.
     *
     * @param item     The item.
     * @param location The location.
     * @param isPublic The isPublic flag.
     */
    public GroundItem(Player player, Item item, Location location, boolean isPublic) {
        this.item = item;
        this.location = location;
        this.isPublic = isPublic;
        this.player = player;
    }

    public GroundItem(Player player, Item item, int x, int y, int z, boolean isPublic) {
        this.item = item;
        this.location = Location.locate(x, y, z);
        this.isPublic = isPublic;
        this.player = player;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Location getLocation() {
        return location;
    }

    public Item getItem() {
        return item;
    }

    public Player getPlayer() {
        return player;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setUpdateTicks(int updateTicks) {
        this.updateTicks = updateTicks;
    }

    public int getUpdateTicks() {
        return updateTicks;
    }

	@Override
	public NodeType getNodeType() {
		return NodeType.GROUND_ITEM;
	}

	@Override
	public void setNodeType(NodeType n) { }

	@Override
	public int size() {
		return 1;
	}
	
}