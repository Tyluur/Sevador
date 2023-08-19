package com.sevador.game.node.npc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.burtleburtle.cache.format.ItemDefinition;
import net.burtleburtle.cache.format.NPCDefinition;
import net.burtleburtle.tick.Tick;
import net.burtleburtle.tick.impl.AreaUpdateTick;

import com.sevador.Main;
import com.sevador.content.friendChat.FriendChat;
import com.sevador.game.action.ActionManager;
import com.sevador.game.action.impl.combat.CombatAction;
import com.sevador.game.action.impl.packetactions.MovementAction;
import com.sevador.game.minigames.Barrows;
import com.sevador.game.minigames.Barrows.BarrowsBrother;
import com.sevador.game.node.Item;
import com.sevador.game.node.NodeType;
import com.sevador.game.node.model.DamageMap;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.Properties;
import com.sevador.game.node.model.WalkingQueue;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.form.RangeData;
import com.sevador.game.node.model.combat.form.RangeWeapon;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.mask.UpdateMasks;
import com.sevador.game.node.npc.DropLoader.Drop;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Poison;
import com.sevador.game.node.player.Skills;
import com.sevador.game.region.GroundItem;
import com.sevador.game.region.GroundItemManager;
import com.sevador.game.region.RegionManager;
import com.sevador.game.region.path.PrimitivePathFinder;
import com.sevador.game.world.World;
import com.sevador.utility.Constants;
import com.sevador.utility.Misc;
import com.sevador.utility.NPCBonuses;
import com.sevador.utility.Priority;

/**
 * Represents a non-player character in the game.
 * 
 * @author Emperor
 * 
 */
@SuppressWarnings("serial")
public class NPC extends Entity {

	/**
	 * The npc's id.
	 */
	private final int id;

	/**
	 * If the npc respawns upon death.
	 */
	private boolean respawns = true;

	/**
	 * The NPC's rotation.
	 */
	private int rotation;

	/**
	 * The NPC's definitions.
	 */
	private NPCDefinition definition;

	/**
	 * The range data to use if the NPC is using ranged as combat style.
	 */
	private RangeData rangeData;

	/**
	 * The NPC's spawn location.
	 */
	private Location spawnLocation;

	/**
	 * If the NPC is visible.
	 */
	private boolean visible;

	public transient Player instancedPlayer;

	/**
	 * Constructs a new {@code NPC} {@code Object}.
	 * 
	 * @param npcId
	 *            The npc id.
	 */
	public NPC(int npcId) {
		this(npcId, NodeType.HUMAN, true);
	}

	/**
	 * Constructs a new {@code NPC} {@code Object}.
	 * 
	 * @param id
	 *            The NPC id.
	 * @param nodeType
	 *            The node type.
	 * @param respawns
	 *            If the NPC respawns
	 */
	public NPC(int id, NodeType nodeType, boolean respawns) {
		super(nodeType);
		this.id = id;
		this.respawns = respawns;
		this.definition = NPCDefinition.forId(id);
		this.visible = true;
		this.spawnLocation = DEFAULT_LOCATION;
		setCombatLevel(definition.combatLevel);
	}

	/**
	 * Initializes the NPC.
	 * 
	 * @return This NPC instance.
	 */
	public NPC init() {
		setTicks(new HashMap<String, Tick>());
		setRandom(new Random());
		setPoison(new Poison());
		getPoison().setEntity(this);
		setAttributes(new HashMap<String, Object>());
		setWalkingQueue(new WalkingQueue(this));
		setActionManager(new ActionManager(this));
		setUpdateMasks(new UpdateMasks());
		setDamageMap(new DamageMap(this));
		setCombatAction(new CombatAction(this));
		setSkills(new NPCSkills(this));
		setPrayer(new NPCPrayer(this));
		bonuses = NPCBonuses.getBonuses(id);
		setProperties(new Properties());
		getProperties().setAttackSpeed(definition.getAttackSpeed());
		getProperties().setStats(definition.getBonuses());
		getSkills().setLifepoints(getNPCCombatDefinitions().getHitpoints());
		((NPCSkills) getSkills()).setMaximumLifepoints(getNPCCombatDefinitions().getHitpoints());
		getProperties().setAttackAnimation(new Animation(this.getNPCCombatDefinitions().getAttackEmote(), 0, true, Priority.HIGHEST));
		getProperties().setDefenceAnimation(new Animation(this.getNPCCombatDefinitions().getDefenceEmote(), 0, true, Priority.HIGH));
		if (definition.isUsingRange()) {
			getCombatAction().setType(CombatType.RANGE);
			setRangeData(this);
		} else if (definition.isUsingMagic()) {
			getCombatAction().setType(CombatType.MAGIC);
		} else {
			getCombatAction().setType(CombatType.MELEE);
		}
		setLocation(getLocation().transform(0, 0, 0));
		getLocation().getRegion().addEntity(this);
		Main.getWorkingSet().submitLogic(new AreaUpdateTick(this));
		return this;
	}

	/**
	 * Instantly kills the NPC.
	 */
	public void instantDeath() {
		this.visible = false;
		Main.getNodeWorker().remove(this);
	}

	/**
	 * Called every 600ms.
	 */
	public void tick() {
		if (isDead()) return;
		processTicks();
		getPoison().processPoison();
		if (getCombatAction().getLastAttacker() == null && getCombatAction().getVictim() == null)
			addRandomWalk();
		if (getCombatAction().getLastAttacker() != null) {
			getCombatAction().setVictim(getCombatAction().getLastAttacker());
			getActionManager().register(getCombatAction());
		}
		if (!checkAggressivity()) {
		}
	}

	public boolean checkAggressivity() {
		final NPC npc = this;
		ArrayList<Entity> possibleTarget = getPossibleTargets();
		if (!possibleTarget.isEmpty()) {
			if (npc.getDefinition().combatLevel > 1) {
				Entity target = possibleTarget.get(Misc.random(possibleTarget.size() - 1));
				if (target.getCombatAction().getLastAttacker() != this || target.getCombatAction().getVictim() != this) {
					npc.getCombatAction().setVictim(null);
					npc.getCombatAction().execute();
					return false;
				}
				npc.getCombatAction().setVictim(target);
				npc.getCombatAction().execute();
				target.setFindTargetDelay(Misc.currentTimeMillis() + 10000);
				target.setAttackedBy(npc);
			}
		}
		return true;
	}


	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (Player player : RegionManager.getLocalPlayers(getLocation(), 3)) {
			if (player == null
					|| player.isDead()
					|| !player.isRunning()
					|| (!isMultiArea() || !player
							.isMultiArea())
							&& player.getAttackedBy() != this && player
							.getFindTargetDelay() > System
							.currentTimeMillis())
				continue;
			possibleTarget.add(player);
		}
		return possibleTarget;
	}

	public void addRandomWalk() {
		if (walkCount > 0) {
			walkCount--;
			return;
		}
		final NPC npc = this;
		if (npc.getCombatLevel() > 1)
			getActionManager().register(new MovementAction(npc, getSpawnLocation().getX() - Misc.random(3), getSpawnLocation().getY() - Misc.random(3), false, new PrimitivePathFinder()));
		walkCount = Misc.random(10) + 5;
	}

	private transient int walkCount = 10;

	/**
	 * Clears the NPC.
	 * 
	 * @return This NPC instance.
	 */
	public NPC clear() {
		getLocation().getRegion().removeEntity(this);
		return this;
	}

	@Override
	public NPC getNPC() {
		return this;
	}

	@Override
	public boolean isNPC() {
		return true;
	}

	@Override
	public int size() {
		return definition.size;
	}

	/**
	 * Gets the npc's id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Checks if the npc respawns.
	 * 
	 * @return {@code True} if the npc will respawn upon death, {@code false} if
	 *         not.
	 */
	public boolean isRespawns() {
		return respawns;
	}
	
	/**
	 * @param respawns
	 *            - the boolean as to whether or not the npc will respawn upon
	 *            death.
	 */
	public void setRespawns(boolean respawns) {
		this.respawns = respawns;
	}

	/**
	 * @return the rotation
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	/**
	 * @return the definition
	 */
	public NPCDefinition getDefinition() {
		return definition;
	}

	/**
	 * @param definition
	 *            the definition to set
	 */
	public void setDefinition(NPCDefinition definition) {
		this.definition = definition;
	}

	@Override
	public RangeData getRangeData(Interaction i) {
		return rangeData;
	}

	@Override
	public void onDeath() {
		getUpdateMasks().register(new Animation(this.getNPCCombatDefinitions().getDeathEmote(), 0, true));
		Entity killer = getDamageMap().getMostDamageEntity();
		if (killer != null) {
			if (id >= 2025 && id <= 2030) { // barrow brothers
				NPC npc = killer.getAttribute("currentlyFightingBrother");
				if (id == npc.getId()) {
					killer.getPlayer().getSettings().getKilledBrothers()[Barrows.getIndexForBrother(this)] = true;
					killer.getPlayer().getPlayerAreaTick().updateBarrowsInterface();
					killer.removeAttribute("currentlyFightingBrother");
				}
				BarrowsBrother brother = killer.getAttribute("barrows_tunnel");
				if (brother != null && brother.getNpcId() == id) {
					killer.getPlayer().getSettings().getKillCount()[0] = 0;
					killer.removeAttribute("barrows_tunnel");
					killer.setAttribute("canLoot", Boolean.TRUE);
				}
			}
			for (int i : Barrows.TUNNEL_NPCS) {
				if (id == i) {
					killer.getPlayer().getSettings().getKillCount()[0]++;
					if (killer.getPlayer().getSettings().getKillCount()[0] > 9994) {
						killer.getPlayer().getSettings().getKillCount()[0] = 9994;
					}
					killer.getPlayer().getPlayerAreaTick().updateBarrowsInterface();
				}
			}
			if (killer.getTask() != null && killer.getTask().getName().equalsIgnoreCase(getDefinition().name)) {
				killer.getTask().decreaseAmount();
				((Skills) killer.getSkills()).addExperience(Skills.SLAYER, killer.getTask().getXPAmount());
				if (killer.getTask().getTaskAmount() == 0) {
					killer.getPlayer().getDialogueManager().startDialogue("SimpleMessage", "You have completed your slayer task; go to your slayer master for another one.");
					killer.setTask(null);
				}
			}
		}
		setAttribute("state:dead", true);
		visible = false;
		((NPCSkills) getSkills()).reset();
		if (respawns) {
			World.getWorld().submit(new Tick(definition.getRespawn()) {
				@Override
				public boolean run() {
					removeAttribute("state:dead");
					return visible = true;
				}
			});
		} else {
			Main.getNodeWorker().remove(this);
		}
		getDamageMap().clear();

		List<Drop> drops = World.getWorld().getDroploader().getDropMap().get(getId());
		if (drops != null)
			Collections.shuffle(drops, new Random());
		if (drops != null) {
			List<Drop> hitDrops = new ArrayList<Drop>();
			boolean rareDrop = false;
			for (Drop d : drops) {
				if (d == null) continue;
				int chance = getRandom().nextInt(100);
				if (chance <= d.getRate()) {
					if (d.getRate() < 100) {
						if (d.getItemId() >= 20135 && d.getItemId() <= 20174) {
							if (rareDrop) {
								continue;
							}
							rareDrop = true;
						}
						hitDrops.add(d);
					} else {
						Player receiver = killer.getPlayer();
						for (int i = 0; i < Constants.BANDOS_NPCS.length; i++) {
							if (getId() == Constants.BANDOS_NPCS[i]) {
								receiver.getKillCount()[Constants.BANDOS_KILLS]++;
							}
						}
						for (int i = 0; i < Constants.SARADOMIN_NPCS.length; i++) {
							if (getId() == Constants.SARADOMIN_NPCS[i]) {
								receiver.getKillCount()[Constants.SARADOMIN_KILLS]++;
							}
						}
						for (int i = 0; i < Constants.ARMADYL_NPCS.length; i++) {
							if (getId() == Constants.ARMADYL_NPCS[i]) {
								receiver.getKillCount()[Constants.ARMADYL_KILLS]++;
							}
						}
						for (int i = 0; i < Constants.ZAMORAK_NPCS.length; i++) {
							if (getId() == Constants.ZAMORAK_NPCS[i]) {
								receiver.getKillCount()[Constants.ZAMORAK_KILLS]++;
							}
						}
						int itemAmount = Misc.random(d.getMinAmount(), d.getMaxAmount());
						if (receiver != null && receiver.getSettings() != null && receiver.getSettings().getCurrentFriendChat() != null && receiver.getSettings().getCurrentFriendChat().isLootsharing() && receiver.getSettings().getCurrentFriendChat().isLootsharing()) {
							if (ItemDefinition.forId(d.getItemId()).getStorePrice() > 500000) {
								splitDrop(d, receiver.getSettings().getCurrentFriendChat());
							} else {
								receiver = getLooter(receiver, d.getItemId(), itemAmount, receiver.getSettings().getCurrentFriendChat(), getLocation());
								GroundItemManager.createGroundItem(new GroundItem(receiver, new Item(d.getItemId(), Misc.random(d.getMinAmount(), d.getMaxAmount())), getLocation(), false));
							}
						} else if (receiver != null && receiver.getSettings() != null && receiver.getSettings().getCurrentFriendChat() != null && receiver.getSettings().getCurrentFriendChat().isLootsharing()) {
							splitDrop(d, receiver.getSettings().getCurrentFriendChat());
						} else if (receiver != null && receiver.getSettings() != null &&receiver.getSettings().getCurrentFriendChat() != null && receiver.getSettings().getCurrentFriendChat().isLootsharing()) {
							GroundItemManager.createGroundItem(new GroundItem(getLooter(receiver, d.getItemId(), itemAmount, receiver.getSettings().getCurrentFriendChat(), getLocation()), new Item(d.getItemId(), Misc.random(d.getMinAmount(), d.getMaxAmount())), getLocation(), false));
						} else {
							GroundItemManager.createGroundItem(new GroundItem(receiver, new Item(d.getItemId(), Misc.random(d.getMinAmount(), d.getMaxAmount())), getLocation(), false));
						}
					}
				}
			}
		}
		getPoison().makePoisoned(0);
		setLocation(spawnLocation);
	}

	private void splitDrop(Drop d, FriendChat clan) {
		ArrayList<Player> receivingPlayers = new ArrayList<Player>();
		int amount = Misc.random(d.getMinAmount(), d.getMaxAmount());
		int price = ItemDefinition.forId(d.getItemId()).getStorePrice() * amount;
		for (Player pl : RegionManager.getLocalPlayers(getLocation(), 16)) {
			if (clan.getMembers().contains(pl)) {
				if (!getDamageMap().getDamageList().contains(pl)) {
					continue;
				}
				receivingPlayers.add(pl);
			}
		}
		int priceSplit = price / receivingPlayers.size();
		for (Player pl : receivingPlayers) {
			GroundItemManager.createGroundItem(new GroundItem(pl, new Item(995, priceSplit), getLocation(), false));
			pl.getPacketSender().sendMessage("<col=009900>You received " + priceSplit + " coins as your split of the drop: " + priceSplit + " " + ItemDefinition.forId(d.getItemId()).getName());
		}

	}

	public Player getLooter(Player player, int id, int amount, FriendChat clan, Location location) {
		Player done = player;
		if (clan != null) {
			if (clan.isLootsharing()) {
				List<Player> playersGetLoot = new LinkedList<Player>();
				int best = 0;
				for (Player pl : RegionManager.getLocalPlayers(getLocation(), 16)) {
					if (pl.getLocation().getDistance(location) > 20 || !clan.isLootsharing()) {
						continue;
					}
					if (clan.getMembers().contains(pl)) {
						if (!getDamageMap().getDamageList().contains(pl)) {
							continue;
						}
						int damage = getDamageMap().getDamageList().get(pl.getIndex()).getHit() + pl.getSettings().getChances();
						if (damage > best) {
							playersGetLoot.add(pl);
							best = damage;
						} else {
							if (Misc.random(2) == 1) {
								playersGetLoot.add(pl);
							}
						}
						pl.getSettings().incChances();
						if (pl.getSettings().getChances() < 0) {
							pl.getSettings().setChances(0);
						}
					}
				}
				for (Player pl : playersGetLoot) {
					if (Misc.random(2) == 1) {
						done = pl;
						for (int i = 0; i < 5; i++) {
							pl.getSettings().decChances();
						}
						break;
					}
				}
			}
		}
		if (clan != null && clan.isLootsharing()) {
			done.getPacketSender().sendMessage("<col=009900>You received: " + amount + " " + ItemDefinition.forId(id).getName());
			for (final Player pl : RegionManager.getLocalPlayers(getLocation(), 16)) {
				if (pl.getLocation().getDistance(location) > 20 || !clan.isLootsharing()) {
					continue;
				}
				if (pl.getIndex() == done.getIndex()) {
					continue;
				}
				if (clan.getMembers().contains(pl)) {
					pl.getPacketSender().sendMessage(Misc.formatPlayerNameForDisplay(done.getCredentials().getDisplayName()) + " received: " + amount + " " + ItemDefinition.forId(id).getName());
					World.getWorld().submit(new Tick(6) {
						@Override
						public boolean run() {
							pl.getPacketSender().sendMessage("Your chance of receiving loot has improved.");
							return true;
						}
					});
				}
			}
		}
		return done;
	}

	@Override
	public boolean isAttackable(Entity e) {
		if (getCombatLevel() < 1 || this.getDefinition().name.toLowerCase().equals("max")) {
			if (e.isPlayer()) {
				e.getPlayer().getPacketSender().sendMessage("You can't attack this npc.");
			}
			return false;
		}
		if (!this.isMultiArea()) {
			if (e.getCombatAction().getLastAttacker() != null && e.getCombatAction().getLastAttacker() != this) {
				if (e.isPlayer()) {
					e.getPlayer().getPacketSender().sendMessage("You're already under attack.");
				}
				return false;
			} else if (getCombatAction().getLastAttacker() != null
					&& getCombatAction().getLastAttacker() != e) {
				if (e.isPlayer()) {
					e.getPlayer().getPacketSender().sendMessage("This monster is already under attack.");
				}
				return false;
			}
		}
		return true;
	}

	/**
	 * Sets the range data for an NPC.
	 * 
	 * @param npc
	 *            The npc.
	 */
	private static void setRangeData(NPC npc) {
		RangeData data = new RangeData(false);
		data.setWeapon(new RangeWeapon(0, null, 0, 0, 0, false, null));
		data.setAnimation(new Animation(npc.getNPCCombatDefinitions().getAttackEmote()));
		data.setGraphics(new Graphic(npc.definition.getStartGraphics(), 96, 0,
				true));
		data.setProjectile(Projectile.create(npc, null,
				npc.definition.getProjectileId(), 40, 36, 41, 46, 5,
				npc.size() << 6));
		npc.rangeData = data;
	}

	/**
	 * Gets the spawnLocation.
	 * 
	 * @return The spawnLocation.
	 */
	public Location getSpawnLocation() {
		return spawnLocation;
	}

	/**
	 * Sets the spawnLocation.
	 * 
	 * @param spawnLocation
	 *            The spawnLocation to set.
	 */
	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

	/**
	 * Gets the visible.
	 * 
	 * @return The visible.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets the visible.
	 * 
	 * @param visible
	 *            The visible to set.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public NPCCombatDefinitions getNPCCombatDefinitions() {
		return getDefinition().getCombatDefinitions();
	}

	/**
	 * @return the bonuses
	 */
	public int[] getBonuses() {
		return bonuses;
	}

	/**
	 * @param bonuses
	 *            the bonuses to set
	 */
	public void setBonuses(int[] bonuses) {
		this.bonuses = bonuses;
	}

	/**
	 * @return the walkCount
	 */
	public int getWalkCount() {
		return walkCount;
	}

	/**
	 * @param walkCount the walkCount to set
	 */
	public void setWalkCount(int walkCount) {
		this.walkCount = walkCount;
	}

	private int[] bonuses;
}
