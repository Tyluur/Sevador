package com.sevador.game.node.player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.cache.format.ObjectDefinition;
import net.burtleburtle.thread.MajorUpdateWorker;
import net.burtleburtle.tick.Tick;
import net.burtleburtle.tick.impl.PlayerAreaTick;

import com.sevador.content.PriceCheck;
import com.sevador.content.chardesign.DesignState;
import com.sevador.content.grandExchange.ItemOffer;
import com.sevador.content.grandExchange.ItemOffer.OfferType;
import com.sevador.content.quest.QuestListener;
import com.sevador.database.DatabaseConnection;
import com.sevador.game.action.ActionManager;
import com.sevador.game.action.impl.TeleportAction;
import com.sevador.game.action.impl.combat.CombatAction;
import com.sevador.game.action.impl.combat.DeathAction;
import com.sevador.game.action.impl.combat.OverloadAction;
import com.sevador.game.dialogue.DialogueManager;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.Item;
import com.sevador.game.node.NodeType;
import com.sevador.game.node.control.ControlerManager;
import com.sevador.game.node.model.DamageMap;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.Properties;
import com.sevador.game.node.model.WalkingQueue;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.combat.form.Ammunition;
import com.sevador.game.node.model.combat.form.RangeData;
import com.sevador.game.node.model.combat.form.RangeWeapon;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.model.container.Container.Type;
import com.sevador.game.node.model.container.impl.BankContainerListener;
import com.sevador.game.node.model.container.impl.EquipmentContainerListener;
import com.sevador.game.node.model.container.impl.InventoryContainerListener;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.mask.AppearanceUpdate;
import com.sevador.game.node.model.mask.FaceLocationUpdate;
import com.sevador.game.node.model.mask.UpdateMasks;
import com.sevador.game.node.model.skills.combat.CombatDefinitions;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.npc.impl.Familiar;
import com.sevador.game.node.npc.impl.sum.NullFamiliar;
import com.sevador.game.node.player.action.SkillActionManager;
import com.sevador.game.region.GroundItem;
import com.sevador.game.region.GroundItemManager;
import com.sevador.game.world.World;
import com.sevador.network.IOSession;
import com.sevador.network.out.CS2Config;
import com.sevador.network.out.MessagePacket;
import com.sevador.update.PlayerFlags;
import com.sevador.update.RenderInformation;
import com.sevador.utility.Constants;
import com.sevador.utility.Misc;

/**
 * Represents a Player-node.
 * 
 * @author Emperor
 * 
 */
public final class Player extends Entity {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = 1848988747792466826L;

	/**
	 * The player's credentials.
	 */
	private final Credentials credentials;

	/**
	 * The player's cape recolouring instance
	 */

	private CapeRecolouring capeRecolouring;

	/**
	 * The controler for events that need it.
	 */

	private ControlerManager controlerManager;

	/**
	 * The player's contacts.
	 */
	private final Contacts contacts;

	/**
	 * The player's bank handler.
	 */
	private final BankHandler bank;

	/**
	 * The equipment container.
	 */
	private final Container equipment;

	/**
	 * The player's inventory container.
	 */
	private final Container inventory;

	/**
	 * The amount of bones you should make
	 */

	public int bonesToMake;

	/**
	 * The player's settings.
	 */
	private final Settings settings;

	/**
	 * The instance for the charges.
	 */

	private ChargeManager chargeManager;


	/**
	 * The player's IOSession.
	 */
	private transient IOSession ioSession;

	/**
	 * The left click option for summoning.
	 */

	private int summoningLeftClickOption;

	/**
	 * The player's dialogue manager
	 */
	private transient DialogueManager dialogueManager;

	/**
	 * The packet sender instance.
	 */
	private transient PacketSender packetSender;

	/**
	 * The instance of the player's price checker
	 */

	private PriceCheck priceCheck;

	/**
	 * The array of the godwars kills.
	 */

	private int[] killCount;

	private boolean usingTicket;

	/**
	 * The potion delay.
	 */

	private transient long potDelay;

	/**
	 * How long you're immune to poison.
	 */

	private long poisonImmune;

	/**
	 * If the player is logged in.
	 */
	private transient boolean loggedIn;

	/**
	 * The last location in the last region the player was on.
	 */
	private transient Location region;

	/**
	 * The rendering information.
	 */
	private transient RenderInformation renderInformation;

	/**
	 * The player's flags.
	 */
	private transient PlayerFlags playerFlags;

	/**
	 * The player's current familiar.
	 */
	private transient Familiar familiar;

	private QuestListener questListener;

	public int clanColor;

	/**
	 * The array of the killed barrows brothers.
	 */

	private boolean[] killedBarrowBrothers;

	/**
	 * The amount of barrows brothers you have killed.
	 */
	private int barrowsKillCount;

	/**
	 * The hidden brother??
	 */

	private int hiddenBrother;

	/**
	 * Constructs a new {@code Player} {@code Object}.
	 * 
	 * @param username
	 *            The username.
	 * @param password
	 *            The password
	 */
	public Player(String username, String password) {
		super(NodeType.PLAYER);
		setLocation(DEFAULT_LOCATION);
		setSkills(new Skills(this));
		setPrayer(new PlayerPrayerHandler());
		credentials = new Credentials(username, password);
		bank = new BankHandler(this);
		settings = new Settings(this);
		contacts = new Contacts(this);
		equipment = new Container(Type.STANDARD, 15,
				new EquipmentContainerListener(this));
		inventory = new Container(Type.STANDARD, 28,
				new InventoryContainerListener(this));
		if (controlerManager == null) {
			controlerManager = new ControlerManager();
		}
		controlerManager.setPlayer(this);
	}

	public void teleportToLocation(Location outsideLocation) {
		this.getActionManager().register(new TeleportAction(this, outsideLocation,TeleportAction.MODERN_ANIM,TeleportAction.MODERN_GRAPHIC,TeleportAction.MODERN_END_ANIM, TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
	}


	/**
	 * The entity's action manager, based off Matrix.
	 */

	private transient SkillActionManager mActionManager;

	/**
	 * The entity's aura manager.
	 */

	private AuraManager auraManager;

	/**
	 * The player's character design
	 */
	private DesignState designState;

	/**
	 * The entity's combat definitions.
	 */

	private CombatDefinitions combatDefinitions;

	/**
	 * Grand Exchange Offers Map.
	 */
	private ItemOffer[] geOffers;

	/**
	 * The player's last used grand exchange slot.
	 */
	private int GESlot;

	public int[] geOrdinal;

	/**
	 * The player's last offertype used.
	 */
	public OfferType offerType;

	/**
	 * @return the mActionManager
	 */
	public SkillActionManager getSkillAction() {
		return mActionManager;
	}

	/**
	 * @param mActionManager
	 *            the mActionManager to set
	 */
	public void setmActionManager(SkillActionManager mActionManager) {
		this.mActionManager = mActionManager;
	}

	private transient PlayerAreaTick playerAreaTick;

	/**
	 * Initializes the player.
	 * 
	 * @return This player instance.
	 */
	public Player init() {
		musicManager = new MusicManager();
		musicManager.setPlayer(this);
		auraManager = new AuraManager();
		auraManager.setPlayer(this);
		questListener = new QuestListener(this);
		if (getGeOffers() == null)
			setGeOffers(new ItemOffer[6]);
		capeRecolouring = new CapeRecolouring(this);
		setPoison(new Poison());
		setTicks(new HashMap<String, Tick>());
		priceCheck = new PriceCheck(this);
		getPoison().setEntity(this);
		setCombatDefinitions(new CombatDefinitions());
		getCombatDefinitions().setPlayer(this);
		setAttributes(new HashMap<String, Object>());
		temporaryAttributes = new ConcurrentHashMap<Object, Object>();
		mActionManager = new SkillActionManager(this);
		equipment.setListener(new EquipmentContainerListener(this));
		chargeManager = new ChargeManager();
		chargeManager.setPlayer(this);
		killedBarrowBrothers = new boolean[6];
		if (settings.getKilledBrothers() == null)
			settings.setKilledBrothers(new boolean[6]);
		if (settings.getKillCount() == null)
			settings.setKillCount(new int[10]);
		killCount = new int[4];
		geOrdinal = new int[Cache.getAmountOfItems()];
		submitTick("player_area_tick", setPlayerAreaTick(new PlayerAreaTick(this)));
		if (!ioSession.isInLobby()) {
			designState = new DesignState();
			setRandom(new Random());
			setUpdateMasks(new UpdateMasks());
			setDamageMap(new DamageMap(this));
			setProperties(new Properties());
			setLocation(getLocation().transform(0, 0, 0));
			setWalkingQueue(new WalkingQueue(this));
			setActionManager(new ActionManager(this));
			setCombatAction(new CombatAction(this));
			setDialogueManager(new DialogueManager(this));
			if (credentials.getAppearance().getBodyPart(0) == -1)
				credentials.getAppearance().setDefaultAppearance();
			familiar = new NullFamiliar();
			familiar.setOwner(this);
			playerFlags = new PlayerFlags();
			renderInformation = new RenderInformation(this);
			bank.getContainer().setListener(new BankContainerListener(this));
			inventory.setListener(new InventoryContainerListener(this));
			if (credentials.getOverloadTicks() > 0) {
				World.getWorld().submit(new Tick(3) {
					@Override
					public boolean run() {
						getActionManager().register(
								new OverloadAction(Player.this, credentials
										.getOverloadTicks() > 488 ? 488
												: credentials.getOverloadTicks()));
						return true;
					}
				});
			}
		}
		packetSender = new PacketSender(this);
		packetSender.sendLogin();
		return this;
	}

	/**
	 * Clears the player (only used on complete logout).
	 * 
	 * @return The player instance.
	 */
	public Player clear() {
		if (getLocation().getRegion() != null) {
			getLocation().getRegion().removeEntity(this);
		}
		familiar.instantDeath();
		getPrayer().reset(this);
		contacts.finalization(true);
		setLoggedIn(false);
		return this;
	}

	/**
	 * Clears the player (only used on lobby logout).
	 * 
	 * @return The player instance.
	 */
	public Player clearLobby() {
		return this;
	}

	@Override
	public boolean isPlayer() {
		return true;
	}

	@Override
	public Player getPlayer() {
		return this;
	}

	@Override
	public TypeHandler getHandler(CombatType type) {
		if (getAttribute("spellId", -1) > -1
				|| getAttribute("autocastId", -1) > -1) {
			return CombatType.MAGIC.getHandler();
		} else if (settings.isSpecialEnabled()) {
			TypeHandler handler = EventManager.getSpecialAttackEvent(equipment
					.getItem(3).getId());
			if (handler != null) {
				return handler;
			}
			settings.setSpecialEnabled(false);
		}
		return type.getHandler();
	}

	/**
	 * Handles the tick update.
	 */
	public void tick() {
		processTicks();
		if (getPrayer().getDrainRate() > 0) {
			getPoison().processPoison();
			double interval = getPrayer().getDrainRate()
					/ (1 + (getProperties().getStats()[13] / 30));
			getSkills().updatePrayerPoints(interval);
			if (getSkills().getPrayerPoints() < 1) {
				getPrayer().reset(this);
				ioSession.write(new CS2Config(this, 182, 0));
				ioSession.write(new MessagePacket(this,
						"You have run out of prayer points."));
				getUpdateMasks().register(new AppearanceUpdate(this));
			}
		}
		auraManager.process();
		chargeManager.process();
		controlerManager.process();
	}

	@Override
	public Damage updateHit(Entity source, int hit, CombatType type) {
		if (source.isPlayer() && type == CombatType.MELEE
				&& getAttribute("spearWall", -1) > MajorUpdateWorker.getTicks()) {
			ioSession.write(new MessagePacket(this,
					"Your spear wall deflects the damage."));
			return new Damage(0);
		}
		if (type == CombatType.MELEE
				&& getAttribute("staffOfLightEffect", -1) > MajorUpdateWorker
				.getTicks()) {
			ioSession.write(new MessagePacket(this,
					"Your staff of light deflects some damage."));
			hit *= 0.5;
		}
		if (getSkills().getPrayerPoints() > 0
				&& getEquipment().getNew(Constants.SLOT_SHIELD).getId() == 13740) {
			double decrease = hit * .3;
			double prayerDecrease = Math.ceil(decrease / 20);
			if (getSkills().getPrayerPoints() >= prayerDecrease) {
				getSkills().updatePrayerPoints(prayerDecrease);
				hit -= decrease;
			} else {
				hit -= getSkills().getPrayerPoints() * 20;
				getSkills().updatePrayerPoints(9);
			}
		} else if (getRandom().nextInt(10) < 7
				&& getEquipment().getNew(Constants.SLOT_SHIELD).getId() == 13742) {
			hit *= .75;
		}
		return super.updateHit(source, hit, type);
	}

	public Container getEquipment() {
		return equipment;
	}

	@Override
	public RangeData getRangeData(Interaction i) {
		RangeData data = new RangeData(true);
		data.setWeapon(RangeWeapon.get(equipment.getItem(3).getId()));
		if (data.getWeapon() == null) {
			return null;
		}
		if (data.getWeapon().getAmmunitionSlot() > -1) {
			data.setAmmo(Ammunition.get(equipment.getNew(data.getWeapon().getAmmunitionSlot()).getId()));
		}
		if (data.getAmmo() == null
				|| !data.getWeapon().getAmmunition()
				.contains(data.getAmmo().getItemId())) {
			ioSession.write(new MessagePacket(this,
					"You do not have enough ammo left."));
			return null;
		}
		data.setAnimation(data.getWeapon().getAnimation());
		if (data.getWeapon().getType() == 2) {
			data.setGraphics(data.getAmmo().getDarkBowGraphics());
		} else {
			data.setGraphics(data.getAmmo().getStartGraphics());
		}
		data.setProjectile(data.getAmmo().getProjectile());
		return data;
	}

	private static final String[] DEATH_MESSAGES = {
		"You were clearly a better fighter than X.",
		"X was no match for you.", "With a powerful blow, you defeat X.",
		"With the power of the gods, you defeat X.",
		"You have sent X to an early grave.",
		"With an almighty strike, you murder X.",
	"A humiliating defeat for X." };

	@Override
	public void onDeath() {
		fullRestore();
		credentials.setOverloadTicks(0);
		Entity killer = getDamageMap().getMostDamageEntity();
		Player dead = this;
		dead.getPacketSender().sendMessage("Oh dear, you have died.");
		if (killer.isPlayer()) {
			if (dead.getAttribute("area:safe", false)) {
				if (killer != this) {
					killer.getPlayer().getPacketSender().sendMessage("You have killed " + Misc.formatPlayerNameForDisplay(dead.credentials.getDisplayName()) + ".");
				}
			} else {
				Container[] containers = DeathAction.getContainers(dead);
				dead.equipment.clear();
				dead.inventory.clear();
				dead.inventory.addAll(containers[0]);
				for (Item item : containers[1].toArray()) {
					if (item != null) 
						GroundItemManager.createGroundItem(new GroundItem(killer.getPlayer(), item, getLocation(), false));
				}
				if (!killer.getPlayer().getCredentials().getUsername().equalsIgnoreCase(dead.getCredentials().getUsername())) {
					killer.getPlayer().getPacketSender().sendMessage(DEATH_MESSAGES[Misc.random(DEATH_MESSAGES.length - 1)].replaceAll("X", Misc.formatPlayerNameForDisplay(getCredentials().getDisplayName())));
					killer.getPlayer().getSettings().increaseKills();
					dead.getSettings().increaseDeaths();
				}
			}
		}
		dead.getDamageMap().clear();
		final Location deathLoc = new Location(3222, 3217, 0);
		dead.getProperties().setTeleportLocation(deathLoc);
	}

	private int[] completionistCapeCustomized;

	private MusicManager musicManager;

	@Override
	public boolean isAttackable(Entity e) {
		if (e.isPlayer()) {
			int combatDifference = getCombatLevel() - e.getCombatLevel();
			if (combatDifference < 0) {
				combatDifference = -combatDifference;
			} 
			int lvl = TeleportAction.getWildernessLevel(getLocation());
			if (getAttribute("area:wilderness", true)) {
				lvl = 20;
			}
			if (combatDifference > lvl) {
				e.getPlayer().getPacketSender().sendMessage("You need to be "+ combatDifference+ " levels deeper in the wilderness to initiate combat.");
				return false;
			}
			if (!e.getAttribute("area:wilderness", false)) {
				e.getPlayer().getPacketSender().sendMessage("You have to be in a pvp-zone to attack a player.");
				return false;
			} else if (!getAttribute("area:wilderness", false)) {
				e.getPlayer().getPacketSender().sendMessage("This player is not in a pvp-zone.");
				return false;
			}
		}
		if (!isMultiArea()) {
			if (e.getCombatAction().getLastAttacker() != null
					&& e.getCombatAction().getLastAttacker() != this) {
				if (e.isPlayer())
					e.getPlayer().getPacketSender().sendMessage("You're already under attack.");
				return false;
			} else if (getCombatAction().getLastAttacker() != null && getCombatAction().getLastAttacker() != e) {
				if (e.isPlayer())
					e.getPlayer().getPacketSender().sendMessage("This player is already under attack.");
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the player's credentials.
	 * 
	 * @return The credentials.
	 */
	public Credentials getCredentials() {
		return credentials;
	}

	/**
	 * @return the bank
	 */
	public BankHandler getBank() {
		return bank;
	}

	/**
	 * Gets the player's inventory.
	 * 
	 * @return The inventory container.
	 */
	public Container getInventory() {
		return inventory;
	}

	/**
	 * Gets the player's settings.
	 * 
	 * @return The settings.
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Sets the player's io session.
	 * 
	 * @param ioSession
	 *            The ioSession to set.
	 */
	public void setIOSession(IOSession ioSession) {
		this.ioSession = ioSession;
	}

	/**
	 * Gets the player's current IO Session.
	 * 
	 * @return The IOSession.
	 */
	public IOSession getIOSession() {
		return ioSession;
	}

	/**
	 * Gets the packet sender instance.
	 * 
	 * @return The packet sender.
	 */
	public PacketSender getPacketSender() {
		return packetSender;
	}

	/**
	 * Sets the player's logged in flag.
	 * 
	 * @param loggedIn
	 *            If the player is logged in.
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	/**
	 * Checks if the player is logged in.
	 * 
	 * @return {@code True} if so.
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * @return the region
	 */
	public Location getRegion() {
		return region;
	}

	/**
	 * @param region
	 *            the region to set
	 */
	public void setRegion(Location region) {
		this.region = region;
	}

	/**
	 * @return the renderInformation
	 */
	public RenderInformation getRenderInformation() {
		return renderInformation;
	}

	/**
	 * @return the contacts
	 */
	public Contacts getContacts() {
		return contacts;
	}

	/**
	 * @return the playerFlags
	 */
	public PlayerFlags getPlayerFlags() {
		return playerFlags;
	}

	@Override
	public boolean isRunning() {
		return settings.isRunToggled();
	}

	/**
	 * Gets the familiar.
	 * 
	 * @return The familiar.
	 */
	public Familiar getFamiliar() {
		return familiar;
	}

	/**
	 * Sets the familiar.
	 * 
	 * @param familiar
	 *            The familiar to set.
	 */
	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	/**
	 * @return the dialogueManager
	 */
	public DialogueManager getDialogueManager() {
		return dialogueManager;
	}

	/**
	 * @param dialogueManager
	 *            the dialogueManager to set
	 */
	public void setDialogueManager(DialogueManager dialogueManager) {
		this.dialogueManager = dialogueManager;
	}

	/**
	 * @param outsideLocation
	 */
	public void teleport(Location outsideLocation) {
		this.getActionManager().register(
				new TeleportAction(this, outsideLocation,
						TeleportAction.MODERN_ANIM,
						TeleportAction.MODERN_GRAPHIC,
						TeleportAction.MODERN_END_ANIM,
						TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
	}

	public void faceObject(GameObject object) {
		ObjectDefinition def = object.getDefinition();
		getUpdateMasks().register(
				new FaceLocationUpdate(this, object.getLocation().transform(
						def.sizeX >> 1, def.sizeY >> 1, 0)));
	}

	/**
	 * @return the controlerManager
	 */
	public ControlerManager getControlerManager() {
		return controlerManager;
	}

	/**
	 * @param controlerManager
	 *            the controlerManager to set
	 */
	public void setControlerManager(ControlerManager controlerManager) {
		this.controlerManager = controlerManager;
	}

	/**
	 * @return the completionistCapeCustomized
	 */
	public int[] getCompletionistCapeCustomized() {
		return completionistCapeCustomized;
	}

	/**
	 * @param completionistCapeCustomized
	 *            the completionistCapeCustomized to set
	 */
	public void setCompletionistCapeCustomized(int[] completionistCapeCustomized) {
		this.completionistCapeCustomized = completionistCapeCustomized;
	}

	public MusicManager getMusicsManager() {
		return musicManager;
	}

	/**
	 * @return the summoningLeftClickOption
	 */
	public int getSummoningLeftClickOption() {
		return summoningLeftClickOption;
	}

	/**
	 * @param summoningLeftClickOption
	 *            the summoningLeftClickOption to set
	 */
	public void setSummoningLeftClickOption(int summoningLeftClickOption) {
		this.summoningLeftClickOption = summoningLeftClickOption;
	}

	/**
	 * @return the potDelay
	 */
	public long getPotDelay() {
		return potDelay;
	}

	/**
	 * @param potDelay
	 *            the potDelay to set
	 */
	public void setPotDelay(long potDelay) {
		this.potDelay = potDelay;
	}

	public void addPotDelay(long time) {
		potDelay = time + Misc.currentTimeMillis();
	}

	public void addPoisonImmune(long time) {
		setPoisonImmune(time + Misc.currentTimeMillis());
		getPoison().reset();
	}

	/**
	 * @return the poisonImmune
	 */
	public long getPoisonImmune() {
		return poisonImmune;
	}

	/**
	 * @param poisonImmune
	 *            the poisonImmune to set
	 */
	public void setPoisonImmune(long poisonImmune) {
		this.poisonImmune = poisonImmune;
	}

	/**
	 * @return the combatDefinitions
	 */
	public CombatDefinitions getCombatDefinitions() {
		return combatDefinitions;
	}

	/**
	 * @param combatDefinitions the combatDefinitions to set
	 */
	public void setCombatDefinitions(CombatDefinitions combatDefinitions) {
		this.combatDefinitions = combatDefinitions;
	}

	/**
	 * @return the geOffers
	 */
	public ItemOffer[] getGeOffers() {
		return geOffers;
	}

	/**
	 * @param geOffers the geOffers to set
	 */
	public void setGeOffers(ItemOffer[] geOffers) {
		this.geOffers = geOffers;
	}

	/**
	 * @return the player's last used grand exchange slot.
	 */

	public int getGESlot() {
		return GESlot;
	}

	/**
	 * @param GESlot the GESlot to set
	 */

	public void setGESlot(int GESlot) {
		this.GESlot = GESlot;
	}

	/**
	 * @return the killedBarrowBrothers
	 */
	public boolean[] getKilledBarrowBrothers() {
		return killedBarrowBrothers;
	}

	/**
	 * @param killedBarrowBrothers the killedBarrowBrothers to set
	 */
	public void setKilledBarrowBrothers(boolean[] killedBarrowBrothers) {
		this.killedBarrowBrothers = killedBarrowBrothers;
	}

	/**
	 * @return the barrowsKillCount
	 */
	public int getBarrowsKillCount() {
		return barrowsKillCount;
	}

	/**
	 * @param barrowsKillCount the barrowsKillCount to set
	 */
	public void setBarrowsKillCount(int barrowsKillCount) {
		this.barrowsKillCount = barrowsKillCount;
	}

	/**
	 * @return the hiddenBrother
	 */
	public int getHiddenBrother() {
		return hiddenBrother;
	}

	/**
	 * @param hiddenBrother the hiddenBrother to set
	 */
	public void setHiddenBrother(int hiddenBrother) {
		this.hiddenBrother = hiddenBrother;
	}

	/**
	 * @return the auraManager
	 */
	public AuraManager getAuraManager() {
		return auraManager;
	}

	/**
	 * @param auraManager the auraManager to set
	 */
	public void setAuraManager(AuraManager auraManager) {
		this.auraManager = auraManager;
	}

	public boolean protectedFromFire() {
		return this.getPrayer().get(PrayerType.DEFLECT_MAGIC)
				|| this.getPrayer().get(PrayerType.PROTECT_FROM_MAGIC)
				|| this.getEquipment().getNew(Constants.SLOT_SHIELD).getDefinition().name.toLowerCase().contains("fire");
	}

	/**
	 * @return the chargeManager
	 */
	public ChargeManager getChargeManager() {
		return chargeManager;
	}

	/**
	 * @param chargeManager the chargeManager to set
	 */
	public void setChargeManager(ChargeManager chargeManager) {
		this.chargeManager = chargeManager;
	}

	/**
	 * @return the priceCheck
	 */
	public PriceCheck getPriceCheck() {
		return priceCheck;
	}

	/**
	 * @param priceCheck the priceCheck to set
	 */
	public void setPriceCheck(PriceCheck priceCheck) {
		this.priceCheck = priceCheck;
	}

	/**
	 * The last player display name on the forum.
	 */
	public String getForumDisplayName() {
		return credentials.getUsername();
	}

	/**
	 * @return the player's post count on the forum.
	 */
	public Object getForumTable(String table) {
		DatabaseConnection connection = World.getWorld().getConnectionPool().nextFree();
		if (connection == null) return null;
		try {
			Statement stmt = connection.createStatement();
			String username = credentials.getUsername().replaceAll("_", "-").replaceAll(" ", "-");
			ResultSet rs = stmt.executeQuery("SELECT * FROM `ipb_members` WHERE "+ "members_seo_name='"	+ username + "' LIMIT 1");
			if (rs.next()) {
				return rs.getObject(table);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.returnConnection();
		}
		return -1;
	}

	/**
	 * @return the designState
	 */
	public DesignState getDesignState() {
		return designState;
	}

	/**
	 * @param designState the designState to set
	 */
	public void setDesignState(DesignState designState) {
		this.designState = designState;
	}

	/**
	 * @return the capeRecolouring
	 */
	public CapeRecolouring getCapeRecolouring() {
		return capeRecolouring;
	}

	/**
	 * @param capeRecolouring the capeRecolouring to set
	 */
	public void setCapeRecolouring(CapeRecolouring capeRecolouring) {
		this.capeRecolouring = capeRecolouring;
	}

	/**
	 * @return the playerAreaTick
	 */
	public PlayerAreaTick getPlayerAreaTick() {
		return playerAreaTick;
	}

	/**
	 * @param playerAreaTick the playerAreaTick to set
	 */
	public PlayerAreaTick setPlayerAreaTick(PlayerAreaTick playerAreaTick) {
		this.playerAreaTick = playerAreaTick;
		return playerAreaTick;
	}

	/**
	 * @return the killCount
	 */
	public int[] getKillCount() {
		return killCount;
	}

	/**
	 * @param killCount the killCount to set
	 */
	public void setKillCount(int[] killCount) {
		this.killCount = killCount;
	}

	/**
	 * @return the usingTicket
	 */
	public boolean isUsingTicket() {
		return usingTicket;
	}

	/**
	 * @param usingTicket the usingTicket to set
	 */
	public void setUsingTicket(boolean usingTicket) {
		this.usingTicket = usingTicket;
	}

	/**
	 * @return the questListener
	 */
	public QuestListener getQuestListener() {
		return questListener;
	}

	/**
	 * @param questListener the questListener to set
	 */
	public void setQuestListener(QuestListener questListener) {
		this.questListener = questListener;
	}

}
