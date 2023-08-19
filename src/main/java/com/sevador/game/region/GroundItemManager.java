package com.sevador.game.region;

import java.util.ArrayList;
import java.util.List;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.ConstructGroundItem;
import com.sevador.network.out.RemoveGroundItem;

/**
 * The ground item manager, makes sure dropped items get added/removed/made
 * global when needed.
 *
 * @author Emperor
 */
public class GroundItemManager {

    /**
     * Represents all the current ground items.
     */
    private static List<GroundItem> groundItems = new ArrayList<GroundItem>();

    /**
     * Creates a new ground item.
     *
     * @param groundItem The ground item.
     */
    public static void createGroundItem(final GroundItem groundItem) {
        createGroundItem(groundItem, 100);
    }

    /**
     * Creates a new ground item.
     *
     * @param groundItem The ground item.
     */
    public static void createGroundItem(final GroundItem groundItem, int updateTicks) {
        groundItem.setUpdateTicks(updateTicks);
        groundItems.add(groundItem);
        if (groundItem.isPublic()) {
            if (groundItem.getPlayer() != null) {
            	groundItem.getPlayer().getIOSession().write(new RemoveGroundItem(groundItem.getPlayer(), groundItem));
            }
            List<Player> players = RegionManager.getLocalPlayers(groundItem.getLocation());
            for (Player player : players) {
            	player.getIOSession().write(new ConstructGroundItem(player, groundItem));
            }
        } else {
            if (groundItem.getPlayer() != null) {
            	groundItem.getPlayer().getIOSession().write(new ConstructGroundItem(groundItem.getPlayer(), groundItem));
            }
        }
    }

    /**
     * Sets a ground item in a public state.
     *
     * @param groundItem The ground item.
     */
    public static void setPublic(final GroundItem groundItem) {
        groundItem.setPublic(true);
        groundItem.setUpdateTicks(100);
        List<Player> players = RegionManager.getLocalPlayers(groundItem.getLocation());
        for (Player player : players) {
        	player.getIOSession().write(new RemoveGroundItem(player, groundItem));
        	player.getIOSession().write(new ConstructGroundItem(player, groundItem));
        }
    }

    /**
     * Removes a ground item.
     *
     * @param groundItem The ground item to remove.
     */
    public static void removeGroundItem(GroundItem groundItem) {
        groundItems.remove(groundItem);
        if (groundItem.isPublic()) {
            List<Player> players = RegionManager.getLocalPlayers(groundItem.getLocation());
            for (Player player : players) {
            	player.getIOSession().write(new RemoveGroundItem(player, groundItem));
            }
        } else {
            if (groundItem.getPlayer() != null) {
            	groundItem.getPlayer().getIOSession().write(new RemoveGroundItem(groundItem.getPlayer(), groundItem));
            }
        }
    }

    /**
     * Removes a ground item holding the public flag.
     *
     * @param item The ground item.
     */
    public static void removePublicGroundItem(GroundItem item) {
        groundItems.remove(item);
        List<Player> players = RegionManager.getLocalPlayers(item.getLocation());
        for (Player player : players) {
        	player.getIOSession().write(new RemoveGroundItem(player, item));
        }
    }

    /**
     * Removes a ground item holding the private flag.
     *
     * @param item The ground item.
     */
    public static void removePrivateGroundItem(GroundItem item) {
        groundItems.remove(item);
        if (item.getPlayer() != null) {
        	item.getPlayer().getIOSession().write(new RemoveGroundItem(item.getPlayer(), item));
        }
    }

    /**
     * Replaces an existing ground item with a new one.
     *
     * @param player    The player.
     * @param id        the id of the ground item to replace.
     * @param location  The location of the ground item to replace.
     * @param toReplace The ground item to replace with.
     */
    public static void replaceGroundItem(Player player, int id, Location location, GroundItem toReplace) {
        GroundItem item = getGroundItem(id, location);
        if (item == null) {
            return;
        }
        if (item.isPublic()) {
            List<Player> players = RegionManager.getLocalPlayers(item.getLocation());
            for (Player p : players) {
            	p.getIOSession().write(new ConstructGroundItem(p, item));
            }
        } else {
            if (item.getPlayer() != null) {
            	item.getPlayer().getIOSession().write(new ConstructGroundItem(item.getPlayer(), item));
            }
        }
        groundItems.remove(item);
        toReplace.setUpdateTicks(100);
        groundItems.add(toReplace);
        if (toReplace.isPublic()) {
            if (toReplace.getPlayer() != null) {
            	toReplace.getPlayer().getIOSession().write(new RemoveGroundItem(toReplace.getPlayer(), toReplace));
            }
            List<Player> players = RegionManager.getLocalPlayers(toReplace.getLocation());
            for (Player p : players) {
            	p.getIOSession().write(new ConstructGroundItem(p, toReplace));
            }
        } else {
            if (toReplace.getPlayer() != null) {
            	toReplace.getPlayer().getIOSession().write(new ConstructGroundItem(toReplace.getPlayer(), toReplace));
            }
        }
    }

    /**
     * Replaces an existing ground item with a new one.
     *
     * @param player    The player.
     * @param id        the id of the ground item to replace.
     * @param location  The location of the ground item to replace.
     * @param toReplace The ground item to replace with.
     */
    public static void replacePrivateGroundItem(Player player, int id,
                                                Location location, GroundItem toReplace) {
        GroundItem item = getPrivateGroundItem(id, location);
        if (item == null) {
            return;
        }
    	player.getIOSession().write(new RemoveGroundItem(player, item));
        groundItems.remove(item);
        toReplace.setUpdateTicks(100);
        groundItems.add(toReplace);
    	player.getIOSession().write(new ConstructGroundItem(player, toReplace));
    }

    /**
     * Increases the amount of a ground item by {@code 1}, <br>
     * or creates a new ground item at the given location when there was no
     * ground item found.
     *
     * @param player   The player.
     * @param id       The id of the ground item to increase.
     * @param location The location of the ground item to increase.
     */
    public static void increaseAmount(Player player, int id, Location location) {
        increaseAmount(player, id, location, 1);
    }
    
    /**
     * Increases the amount of a ground item by {@code 1}, <br>
     * or creates a new ground item at the given location when there was no
     * ground item found.
     *
     * @param player   The player.
     * @param id       The id of the ground item to increase.
     * @param location The location of the ground item to increase.
     * @param amount The amount to increase.
     */
    public static void increaseAmount(Player player, int id, Location location, int amount) {
        GroundItem item = getPrivateGroundItem(id, location);
        if (item == null) {
            createGroundItem(new GroundItem(player, new Item(id, amount),
                    location, false));
            return;
        }
    	player.getIOSession().write(new RemoveGroundItem(player, item));
        groundItems.remove(item);
        item.getItem().setAmount(item.getItem().getAmount() + amount);
        groundItems.add(item);
    	player.getIOSession().write(new ConstructGroundItem(player, item));
    }

    /**
     * Gets a ground item with the given id and location, holding the private
     * flag.
     *
     * @param id  The id.
     * @param loc The location.
     * @return The ground item.
     */
    public static GroundItem getPrivateGroundItem(int id, Location loc) {
        GroundItem lastGroundItem = null;
        for (GroundItem item : groundItems) {
            if (item == null) {
                continue;
            }
            if (item.getItem().getId() == id && item.getLocation() == loc && !item.isPublic()) {
                if (lastGroundItem == null) {
                    lastGroundItem = item;
                    continue;
                }
                if (item.getUpdateTicks() > lastGroundItem.getUpdateTicks()) {
                    lastGroundItem = item;
                }
            }
        }
        return lastGroundItem;
    }

    /**
     * Gets a ground item with the given id and location, holding the public
     * flag.
     *
     * @param id  The id.
     * @param loc The location.
     * @return The ground item.
     */
    public static GroundItem getPublicGroundItem(int id, Location loc) {
        GroundItem lastGroundItem = null;
        for (GroundItem item : groundItems) {
            if (item == null) {
                continue;
            }
            if (item.getItem().getId() == id && item.getLocation() == loc && item.isPublic()) {
                if (lastGroundItem == null) {
                    lastGroundItem = item;
                    continue;
                }
                if (item.getUpdateTicks() > lastGroundItem.getUpdateTicks()) {
                    lastGroundItem = item;
                }
            }
        }
        return lastGroundItem;
    }

    /**
     * Gets a ground item with the given id and location.
     *
     * @param id  The id.
     * @param loc The location.
     * @return The ground item.
     */
    public static GroundItem getGroundItem(int id, Location loc) {
        for (GroundItem item : groundItems) {
            if (item == null) {
                continue;
            }
            if (item.getItem().getId() == id && item.getLocation() == loc) {
                return item;
            }
        }
        return null;
    }

    /**
     * Gets the list of ground items.
     *
     * @return The list.
     */
    public static List<GroundItem> getGroundItems() {
        return groundItems;
    }

    /**
     * Refreshes the ground items when changing regions.
     *
     * @param player
     */
    public static void refresh(Player player) {
        for (GroundItem item : groundItems) {
            if (item != null) {
            	player.getIOSession().write(new RemoveGroundItem(player, item));
                if (item.getPlayer() == player || item.isPublic()) {
                	player.getIOSession().write(new ConstructGroundItem(player, item));
                }
            }
        }
    }
}