package com.sevador.game.node.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import net.burtleburtle.thread.MajorUpdateWorker;
import net.burtleburtle.tick.Tick;

import com.sevador.game.action.ActionManager;
import com.sevador.game.action.impl.combat.CombatAction;
import com.sevador.game.node.Node;
import com.sevador.game.node.NodeType;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.combat.form.RangeData;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.FaceEntityUpdate;
import com.sevador.game.node.model.mask.ForceTextUpdate;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.mask.UpdateFlag;
import com.sevador.game.node.model.mask.UpdateMasks;
import com.sevador.game.node.model.skills.prayer.PrayerSkeleton;
import com.sevador.game.node.model.skills.slayer.SlayerTask;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.npc.impl.KingBlackDragon;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Poison;
import com.sevador.game.node.player.Skills;
import com.sevador.game.world.World;
import com.sevador.utility.Misc;

/**
 * The {@code Entity} class is the super class for players and NPCs.
 * 
 * @author Emperor
 * 
 */
public abstract class Entity implements Node, Serializable {

	/**
	 * The default location.
	 */
	public static Location DEFAULT_LOCATION = Location.locate(3165 - Misc.random(3), 3486, 0);

	/**
	 * The long for finding the target.
	 */
	private transient long findTargetDelay;

	private transient long attackedByDelay;

	private int[] forceWalk;

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = -8266086910230893113L;

	/**
	 * The temporary attributes, used for skilling and other references.
	 */
	protected transient ConcurrentHashMap<Object, Object> temporaryAttributes;

	private transient Map<String, Tick> ticks;


	public void submitTick(String identifier, Tick tick, boolean replace) {
		if (ticks.containsKey(identifier) && !replace) {
			return;
		}
		ticks.put(identifier, tick);
	}

	public void submitTick(String identifier, Tick tick) {
		submitTick(identifier, tick, false);
	}

	public boolean inWilderness() {
		if (isPlayer()) {
			return getPlayer().getPlayerAreaTick().inWilderness();
		}
		return World.getWorld().getAreaManager().getAreaByName("Wilderness").contains(getLocation());
	}

	public void removeTick(String identifier) {
		if (hasTick("following_mob") && identifier.equals("following_mob")) {
			turnTo(null);
		}
		Tick tick = ticks.get(identifier);
		if (tick != null) {
			tick.stop();
			ticks.remove(identifier);
		}
	}

	public Tick retrieveTick(String string) {
		return ticks.get(string);
	}

	public boolean hasTick(String string) {
		return ticks.containsKey(string);
	}

	public void processTicks() {
		if (ticks.isEmpty()) {
			return;
		}
		Map<String, Tick> ticks = new HashMap<String, Tick>(this.ticks);
		Iterator<Map.Entry<String, Tick>> it = ticks.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Tick> entry = it.next();
			if (!entry.getValue().run()) {
				this.ticks.remove(entry.getKey());
			}
		}
	}

	private Poison poison;

	/**
	 * @return the X location
	 */

	public int getX() {
		return location.getX();
	}

	/**
	 * @return the Y location
	 */

	public int getY() {
		return location.getY();
	}

	public boolean hasAttribute(String string) {
		return attributes.get(string) != null;
	}

	public void forceMovement(Animation animation, int x, int y, int speed1, int speed2, int dir, int cycles, boolean removeAttribute) {
		forceMovement(animation, x, y, speed1, speed2, dir, cycles, removeAttribute, true);
	}

	public void forceMovement(Animation animation, int x, int y, int speed1, int speed2, int dir, int cycles, boolean removeAttribute, boolean teleport) {
		if (dir == -1) {
			dir = Misc.forceDir(location, Location.locate(x, y, 0));
		}
		if (animation != null) {
			setAnimation(animation);
		}
		setForceWalk(x, y, speed1, speed2, dir, cycles, removeAttribute, teleport);
		walkingQueue.reset();
		setAttribute("cantMove", Boolean.TRUE);
	}


	public void requestWalk(int x, int y) {
		int firstX = x - (getLocation().getRegionX() - 6) * 8;
		int firstY = y - (getLocation().getRegionY() - 6) * 8;
		walkingQueue.reset();
		walkingQueue.addPath(firstX, firstY);
	}

	public void setForceWalk(final int x, final int y, final int speed1, final int speed2, final int dir, final int cycles, final boolean removeAttribute, final boolean teleport) {
		this.forceWalk = new int[]{x, y, speed1, speed2, dir, cycles};
		World.getWorld().submit(new Tick(forceWalk[5]) {

			@Override
			public boolean run() {
				if (teleport) {
					getProperties().setTeleportLocation(new Location(forceWalk[0], forceWalk[1], location.getZ()));
				}
				if (removeAttribute) {
					removeAttribute("busy");
					removeAttribute("cantMove");
				}
				return true;
			}
		});
	}

	/**
	 * @return the Z location
	 */

	public int getZ() {
		return location.getZ();
	}

	/**
	 * @return the size of the entity.
	 */

	public int getSize() {
		return size();
	}

	public void setAnimation(Animation animation) {
		getUpdateMasks().register(animation);
	}

	public void setNextForceTalk(String text) {
		getUpdateMasks().register(new ForceTextUpdate(text, isNPC()));
	}

	public void setGraphic(Graphic animation) {
		getUpdateMasks().register(animation);
	}

	/**
	 * The random instance used.
	 */
	private transient Random random = new Random();

	/**
	 * The attacked by instance
	 */
	private transient Entity attackedBy;

	/**
	 * The node type.
	 */
	private NodeType nodeType;

	/**
	 * The location.
	 */
	private Location location = DEFAULT_LOCATION;

	/**
	 * The entity's combat level.
	 */
	private int combatLevel;

	/**
	 * The entity's skills.
	 */
	private SkillSkeleton skills;

	/**
	 * The slayer task
	 */

	private SlayerTask task;

	/**
	 * The entity's prayer handler.
	 */
	private PrayerSkeleton prayer;

	/**
	 * The entity's index.
	 */
	private transient int index;

	/**
	 * The entity's update masks.
	 */
	private transient UpdateMasks updateMasks;

	/**
	 * The combat action.
	 */
	private transient CombatAction combatAction;

	/**
	 * The list of temporary attributes.
	 */
	private transient Map<String, Object> attributes;

	/**
	 * The entity's walking queue.
	 */
	private transient WalkingQueue walkingQueue;

	/**
	 * The entity's properties.
	 */
	private transient Properties properties;

	/**
	 * The entity's damage map.
	 */
	private transient DamageMap damageMap;

	/**
	 * The entity's action manager.
	 */
	private transient ActionManager actionManager;

	/**
	 * Constructs a new {@code Entity} {@code Object}.
	 * 
	 * @param nodeType
	 *            The node type of this entity.
	 */
	public Entity(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	/**
	 * Gets called when this entity is dying.
	 */
	public abstract void onDeath();

	/**
	 * If this entity is attackable by the attacking entity.
	 * 
	 * @param e
	 *            The attacking entity.
	 * @return {@code True} if the attacking entity can attack this entity.
	 */
	public abstract boolean isAttackable(Entity e);

	/**
	 * Fully restores this entity.
	 */
	public void fullRestore() {
		for (int i = 0; i < 25; i++) {
			skills.setLevel(i, skills.getStaticLevel(i));
		}
		if (isPlayer()) {
			((Skills) skills).refresh();
			getPlayer().getSettings().updateSpecialEnergy(
					getPlayer().getSettings().getSpecialEnergy() - 100);
			getPlayer().getSettings().increaseRunEnergy(
					100 - getPlayer().getSettings().getRunEnergy());
		}
		setAttribute("freezeImmunity", MajorUpdateWorker.getTicks() + 5);
		setAttribute("poisonImmunity", MajorUpdateWorker.getTicks() + 5);
		skills.heal(50000);
		skills.updatePrayerPoints(skills.getPrayerPoints()
				- (skills.getStaticLevel(5) * 10));
		prayer.reset(getPlayer());
	}

	/**
	 * Does a visual update (Animation & Graphic at the same time). <br>
	 * The graphic will only be displayed if the animation is displayed.
	 * 
	 * @param animation
	 *            The animation.
	 * @param graphic
	 *            The graphic.
	 * @return {@code True} If the animation could be done, {@code false} if
	 *         not.
	 */
	public boolean visual(Animation animation, UpdateFlag graphic) {
		if (animation != null && animation.canRegister(updateMasks)) {
			updateMasks.register(animation);
			updateMasks.register(graphic);
			return true;
		}
		return false;
	}

	/**
	 * Turns this entity to the locked on entity.
	 * 
	 * @param lockon
	 *            The locked on entity.
	 * @return {@code True}.
	 */
	public boolean turnTo(Entity lockon) {
		int index = lockon == null ? -1 : lockon.getClientIndex();
		updateMasks.register(new FaceEntityUpdate(index, isNPC()));
		return true;
	}

	/**
	 * Creates a damage object depending on several factors that can in-and
	 * decrease the damage dealt.
	 * 
	 * @param source
	 *            The attacking entity.
	 * @param hit
	 *            The amount to hit.
	 * @param type
	 *            The combat type.
	 * @return The constructed Damage object.
	 */
	public Damage updateHit(Entity source, int hit, CombatType type) {
		Damage d = new Damage(hit);
		if (prayer.get(type.getDeflectCurse())) {
			d.setDeflected(hit / 10);
			d.setHit(source.isPlayer() ? (int) (hit * 0.6) : 0);
		} else if (prayer.get(type.getProtectPrayer())) {
			d.setHit(source.isPlayer() ? (int) (hit * 0.6) : 0);
		}
		return d;
	}

	/**
	 * Sets the attributes.
	 * 
	 * @param attributes
	 *            The attributes.
	 */
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Sets an attribute value.
	 * 
	 * @param key
	 *            The attribute name.
	 * @param value
	 *            The attribute value.
	 */
	public synchronized void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	/**
	 * Gets an attribute.
	 * 
	 * @param key
	 *            The attribute name.
	 * @return The attribute value.
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T> T getAttribute(String key) {
		if (!attributes.containsKey(key)) {
			return null;
		}
		return (T) attributes.get(key);
	}

	/**
	 * Gets an attribute.
	 * 
	 * @param string
	 *            The attribute name.
	 * @param fail
	 *            The value to return if the attribute is null.
	 * @return The attribute value, or the fail argument when null.
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T> T getAttribute(String string, T fail) {
		Object object = null;
		if (attributes != null)
			object = attributes.get(string);
		if (object != null) {
			return (T) object;
		}
		return fail;
	}

	/**
	 * Removes an attribute.
	 * 
	 * @param string
	 *            The attribute name.
	 */
	public synchronized void removeAttribute(String string) {
		if (attributes != null)
			attributes.remove(string);
	}

	@Override
	public NodeType getNodeType() {
		return nodeType;
	}

	@Override
	public void setNodeType(NodeType n) {
		this.nodeType = n;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 * 
	 * @param location
	 *            The location.
	 */
	public void setLocation(Location location) {
		if (this.location == null || this.location.getRegion() == null) {
			this.location = location;
			location.getRegion().addEntity(this);
			return;
		}
		if (location.getRegion() != this.location.getRegion()) {
			this.location.getRegion().removeEntity(this);
			if (location.getRegion() != null && location != null)
				location.getRegion().addEntity(this);
		}
		this.location = location;
	}

	/**
	 * Sets the player's combat level.
	 * 
	 * @param combatLevel
	 *            The combat level.
	 */
	public void setCombatLevel(int combatLevel) {
		this.combatLevel = combatLevel;
	}

	/**
	 * Gets the player's combat level.
	 * 
	 * @return The combat level.
	 */
	public int getCombatLevel() {
		return combatLevel;
	}

	/**
	 * Sets the entity's skills.
	 * 
	 * @param skills
	 *            The skills.
	 */
	public void setSkills(SkillSkeleton skills) {
		this.skills = skills;
	}

	/**
	 * Gets the entity's skills.
	 * 
	 * @return The skills.
	 */
	public SkillSkeleton getSkills() {
		return skills;
	}

	/**
	 * @param prayer
	 *            the prayer to set
	 */
	public void setPrayer(PrayerSkeleton prayer) {
		this.prayer = prayer;
	}

	/**
	 * @return the prayer
	 */
	public PrayerSkeleton getPrayer() {
		return prayer;
	}

	/**
	 * sets the entity's index.
	 * 
	 * @param index
	 *            The index to set.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Gets the entity's index.
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the client index of the entity.
	 * 
	 * @return The client index.
	 */
	public int getClientIndex() {
		if (this instanceof Player) {
			return index + 0x8000;
		}
		return index;
	}

	/**
	 * Checks if the entity is running.
	 * 
	 * @return {@code True} if so.
	 */
	public boolean isRunning() {
		return false;
	}

	/**
	 * Checks if this entity is a player.
	 * 
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isPlayer() {
		return false;
	}

	/**
	 * Checks if this entity is an NPC.
	 * 
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isNPC() {
		return false;
	}

	/**
	 * Returns the player instance of this entity.
	 * 
	 * @return The player instance, or {@code null} if this entity isn't a
	 *         player.
	 */
	public Player getPlayer() {
		return null;
	}

	/**
	 * Returns the NPC instance of this entity.
	 * 
	 * @return The NPC instance, or {@code null} if this entity isn't a NPC.
	 */
	public NPC getNPC() {
		return null;
	}

	/**
	 * Gets the combat handler for the specified combat type.
	 * 
	 * @param type
	 *            The combat type.
	 * @return The type handler.
	 */
	public TypeHandler getHandler(CombatType type) {
		return type.getHandler();
	}

	/**
	 * Gets the range data for this entity.
	 * 
	 * @param i
	 *            The interaction.
	 * @return The range data.
	 */
	public abstract RangeData getRangeData(Interaction i);

	/**
	 * @return the updateMasks
	 */
	public UpdateMasks getUpdateMasks() {
		return updateMasks;
	}

	/**
	 * Sets the update masks.
	 * 
	 * @param updateMasks
	 *            The update masks.
	 */
	public void setUpdateMasks(UpdateMasks updateMasks) {
		this.updateMasks = updateMasks;
	}

	/**
	 * @return the combatAction
	 */
	public CombatAction getCombatAction() {
		return combatAction;
	}

	/**
	 * @param combatAction
	 *            the combatAction to set
	 */
	public void setCombatAction(CombatAction combatAction) {
		this.combatAction = combatAction;
	}

	public boolean isAtWilderness() {
		Location tile = getLocation();
		return (tile.getX() >= 2940 && tile.getX() <= 3395
				&& tile.getY() >= 3525 && tile.getY() <= 4000)
				|| (tile.getX() >= 3264 && tile.getX() <= 3279
				&& tile.getY() >= 3279 && tile.getY() <= 3672)
				|| (tile.getX() >= 2756 && tile.getX() <= 2875
				&& tile.getY() >= 5512 && tile.getY() <= 5627)
				|| (tile.getX() >= 3158 && tile.getX() <= 3181
				&& tile.getY() >= 3679 && tile.getY() <= 3697)
				|| (tile.getX() >= 3280 && tile.getX() <= 3183
				&& tile.getY() >= 3883 && tile.getY() <= 3888);
	}

	public boolean isAtWildSafe() {
		Entity player = this;
		return (player.getX() >= 2940 && player.getX() <= 3395
				&& player.getY() <= 3524 && player.getY() >= 3523);
	}

	public boolean isMultiArea() {
		Location tile = getLocation();
		int destX = tile.getX();
		int destY = tile.getY();
		return (destX >= 3200 && destX <= 3390 && destY >= 3840 && destY <= 3967) // wild
				|| (destX >= 2992 && destX <= 3007 && destY >= 3912 && destY <= 3967)
				|| (destX >= 2946 && destX <= 2959 && destY >= 3816 && destY <= 3831)
				|| (destX >= 3008 && destX <= 3199 && destY >= 3856 && destY <= 3903)
				|| (destX >= 3008 && destX <= 3071 && destY >= 3600 && destY <= 3711)
				|| (destX >= 3270 && destX <= 3346 && destY >= 3532 && destY <= 3625)
				|| (destX >= 2965 && destX <= 3050 && destY >= 3904 && destY <= 3959) // wild
				|| (destX >= 2815 && destX <= 2966 && destY >= 5240 && destY <= 5375)
				|| (destX >= 2840 && destX <= 2950 && destY >= 5190 && destY <= 5230) // godwars
				|| (destX >= 3547 && destX <= 3555 && destY >= 9690 && destY <= 9699) // zaros-godwars
				|| (destX >= 2970 && destX <= 3000 && destY >= 4365 && destY <= 4400)// corp
				|| KingBlackDragon.atKBD(getLocation())
				|| (destX >= 3136 && destX <= 3327 && destY >= 3520 && destY <= 3970 || (destX >= 2376 && 5127 >= destY && destX <= 2422 && 5168 <= destY))
				|| (destX >= 2374 && destY >= 5129 && destX <= 2424 && destY <= 5168) // pits
				|| (destX >= 2622 && destY >= 5696 && destX <= 2573 && destY <= 5752) // torms
				|| (destX >= 2368 && destY >= 3072 && destX <= 2431 && destY <= 3135) // castlewars
				|| (destX >= 2365 && destY >= 9470 && destX <= 2436 && destY <= 9532); // castlewars
	}

	/**
	 * @return the walkingQueue
	 */
	public WalkingQueue getWalkingQueue() {
		return walkingQueue;
	}

	/**
	 * @param walkingQueue
	 *            the walkingQueue to set
	 */
	public void setWalkingQueue(WalkingQueue walkingQueue) {
		this.walkingQueue = walkingQueue;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return the damageMap
	 */
	public DamageMap getDamageMap() {
		return damageMap;
	}

	/**
	 * @param damageMap
	 *            the damageMap to set
	 */
	public void setDamageMap(DamageMap damageMap) {
		this.damageMap = damageMap;
	}

	/**
	 * @return the actionManager
	 */
	public ActionManager getActionManager() {
		return actionManager;
	}

	/**
	 * @param actionManager
	 *            the actionManager to set
	 */
	public void setActionManager(ActionManager actionManager) {
		this.actionManager = actionManager;
	}

	/**
	 * Gets the entity's size.
	 * 
	 * @return The size.
	 */
	public int size() {
		return 1;
	}

	/**
	 * @return the random
	 */
	public Random getRandom() {
		return random;
	}

	/**
	 * @param random
	 *            the random to set
	 */
	public void setRandom(Random random) {
		this.random = random;
	}

	public void setAttackedBy(NPC npc) {

	}

	/**
	 * @return the attackedBy
	 */
	public Entity getAttackedBy() {
		return attackedBy;
	}

	/**
	 * @param attackedBy
	 *            the attackedBy to set
	 */
	public void setAttackedBy(Entity attackedBy) {
		this.attackedBy = attackedBy;
	}

	public ConcurrentHashMap<Object, Object> getTemporaryAttributtes() {
		return temporaryAttributes;
	}

	/**
	 * @return the task
	 */
	public SlayerTask getTask() {
		return task;
	}

	/**
	 * @param task
	 *            the task to set
	 */
	public void setTask(SlayerTask task) {
		this.task = task;
	}

	public boolean isDead() {
		return (getAttribute("state:dead") != null && getAttribute("state:dead").equals(true));
	}

	/**
	 * @return the poison
	 */
	public Poison getPoison() {
		return poison;
	}

	/**
	 * @param poison
	 *            the poison to set
	 */
	public void setPoison(Poison poison) {
		this.poison = poison;
	}

	/**
	 * @return the findTargetDelay
	 */
	public long getFindTargetDelay() {
		return findTargetDelay;
	}

	/**
	 * @param findTargetDelay the findTargetDelay to set
	 */
	public void setFindTargetDelay(long findTargetDelay) {
		this.findTargetDelay = findTargetDelay;
	}

	/**
	 * @return the attackedByDelay
	 */
	public long getAttackedByDelay() {
		return attackedByDelay;
	}

	/**
	 * @param attackedByDelay the attackedByDelay to set
	 */
	public void setAttackedByDelay(long attackedByDelay) {
		this.attackedByDelay = attackedByDelay;
	}

	/**
	 * @param ticks the ticks to set
	 */
	public void setTicks(Map<String, Tick> ticks) {
		this.ticks = ticks;
	}

}