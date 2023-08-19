package com.sevador.game.node.player;

import java.io.Serializable;

import com.sevador.content.friendChat.FriendChat;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Location;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.RunEnergy;

/**
 * Holds the player's settings.
 * 
 * @author Emperor
 * 
 */
public class Settings implements Serializable {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = -8677551701145256270L;

	/**
	 * The player.
	 */
	private final Player player;
	
	/**
	 * The exp mode.
	 */
	private int expMode = 1;

	/**
	 * The chances used for drops.
	 */
	private int chances;
	
	public Item[] starterItems = new Item[30];
	
	/**
	 * An array of the player's warriorsGuild tokens.
	 */
	
	private int[] tokens = new int[5];
	
	/**
	 * The array of the barrows brothers you've killed
	 */

    private boolean[] killedBrothers = new boolean[6];
	
	/**
	 * Whether or not you've received your starter.
	 */
	
	private boolean receivedStarter;
	
	/**
	 * The location used for teleporting with crystals.
	 */
	
	private Location savedLocation;

	/**
	 * If the player has run toggled.
	 */
	private transient boolean runToggled;

	/**
	 * The amount of special energy left.
	 */
	private int specialEnergy = 100;

	/**
	 * If the special attack bar is enabled.
	 */
	private boolean specialEnabled;

	/**
	 * The player's run energy.
	 */
	private double runEnergy = 100.0;

	/**
	 * The player's spell book.
	 */
	private int spellBook = 192;

	/**
	 * The current attack box used.
	 */
	private int attackBox = 0;

	/**
	 * The current attack style used.
	 */
	private int attackStyle = 0;

	/**
	 * The amount of times you've killed people in the wilderness.
	 */

	private int kills;

	/**
	 * The amount of times you've died in the wilderness.
	 */

	private int deaths;

	/**
	 * The friends chat variable
	 */

	private transient FriendChat currentFriendChat;

	/**
	 * The current attack type used.
	 */
	private int attackType = 0;

    private int[] killCount;

	/**
	 * If the player is retaliating.
	 */
	private boolean retaliating = true;

	/**
	 * Constructs a new {@code Settings} {@code Object}.
	 * 
	 * @param player
	 *            The player.
	 */
	public Settings(Player player) {
		this.player = player;
		this.setKilledBrothers(new boolean[6]);
	}

	/**
	 * Refreshes the player's settings.
	 */
	public void refresh() {
		player.getIOSession().write(new RunEnergy(player, this.runEnergy));
		setRunToggled(runToggled);
		setRetaliating(retaliating);
	}

	/**
	 * Sets the run toggled flag.
	 * 
	 * @param runToggled
	 *            If the player has run toggled.
	 */
	public void setRunToggled(boolean runToggled) {
		this.runToggled = runToggled;
		player.getIOSession()
				.write(new ConfigPacket(player, 173, runToggled ? 1 : 0));
	}

	/**
	 * Checks if the player has run toggled.
	 * 
	 * @return {@code True} if so.
	 */
	public boolean isRunToggled() {
		return runToggled;
	}

	/**
	 * Increases the current amount of run energy, and updates it.
	 * 
	 * @param runEnergy
	 *            The amount.
	 */
	public void increaseRunEnergy(double runEnergy) {
		if (this.runEnergy > 99) {
			return;
		}
		this.runEnergy += runEnergy;
		if (this.runEnergy > 99) {
			this.runEnergy = 100;
		}
		player.getIOSession().write(new RunEnergy(player, this.runEnergy));
	}

	/**
	 * Decreases the current amount of run energy, and updates it.
	 * 
	 * @param runEnergy
	 *            The amount.
	 */
	public void decreaseRunEnergy(double runEnergy) {
		this.runEnergy -= runEnergy;
		if (this.runEnergy < 1) {
			this.runEnergy = 0;
			setRunToggled(false);
		}
		player.getIOSession().write(new RunEnergy(player, this.runEnergy));
	}

	/**
	 * Gets the run energy left.
	 * 
	 * @return The run energy left.
	 */
	public double getRunEnergy() {
		return runEnergy;
	}

	/**
	 * @return the specialEnergy
	 */
	public int getSpecialEnergy() {
		return specialEnergy;
	}

	/**
	 * @param specialEnergy
	 *            the specialEnergy to set
	 */
	public void updateSpecialEnergy(int specialEnergy) {
		this.specialEnergy -= specialEnergy;
		if (this.specialEnergy < 0) {
			this.specialEnergy = 0;
		} else if (this.specialEnergy > 100) {
			this.specialEnergy = 100;
		}
		player.getIOSession().write(
				new ConfigPacket(player, 300, this.specialEnergy * 10));
	}

	/**
	 * @return the specialEnabled
	 */
	public boolean isSpecialEnabled() {
		return specialEnabled;
	}

	/**
	 * @param specialEnabled
	 *            the specialEnabled to set
	 */
	public void setSpecialEnabled(boolean specialEnabled) {
		this.specialEnabled = specialEnabled;
		player.getIOSession().write(
				new ConfigPacket(player, 301, specialEnabled ? 1 : 0));
	}

	/**
	 * @return the spellBook
	 */
	public int getSpellBook() {
		return spellBook;
	}

	/**
	 * @param spellBook
	 *            the spellBook to set
	 */
	public void setSpellBook(int spellBook) {
		this.spellBook = spellBook;
	}

	/**
	 * @return the attackBox
	 */
	public int getAttackBox() {
		return attackBox;
	}

	/**
	 * @param attackBox
	 *            the attackBox to set
	 */
	public void setAttackBox(int attackBox) {
		this.attackBox = attackBox;
	}

	/**
	 * @return the attackStyle
	 */
	public int getAttackStyle() {
		return attackStyle;
	}

	/**
	 * @param attackStyle
	 *            the attackStyle to set
	 */
	public void setAttackStyle(int attackStyle) {
		this.attackStyle = attackStyle;
	}

	/**
	 * @return the attackType
	 */
	public int getAttackType() {
		return attackType;
	}

	/**
	 * @param attackType
	 *            the attackType to set
	 */
	public void setAttackType(int attackType) {
		this.attackType = attackType;
	}

	/**
	 * @return the retaliating
	 */
	public boolean isRetaliating() {
		return retaliating;
	}

	/**
	 * @param retaliating
	 *            the retaliating to set
	 */
	public void setRetaliating(boolean retaliating) {
		this.retaliating = retaliating;
		player.getIOSession().write(
				new ConfigPacket(player, 172, retaliating ? 0 : 1));
	}

	/**
	 * @return the currentFriendChat
	 */
	public FriendChat getCurrentFriendChat() {
		return currentFriendChat;
	}

	/**
	 * @param currentFriendChat
	 *            the currentFriendChat to set
	 */
	public void setCurrentFriendChat(FriendChat currentFriendChat) {
		this.currentFriendChat = currentFriendChat;
	}

	/**
	 * @return the kills
	 */
	public int getKills() {
		return kills;
	}

	/**
	 * @param kills
	 *            the kills to set
	 */
	public void setKills(int kills) {
		this.kills = kills;
	}

	/**
	 * @return the deaths
	 */
	public int getDeaths() {
		return deaths;
	}

	/**
	 * @param deaths
	 *            the deaths to set
	 */
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public void increaseKills() {
		this.kills++;
	}

	public void increaseDeaths() {
		this.deaths++;
	}

	/**
	 * @return the chances
	 */
	public int getChances() {
		return chances;
	}

	/**
	 * @param chances
	 *            the chances to set
	 */
	public void setChances(int chances) {
		this.chances = chances;
	}

	public void incChances() {
		chances++;
	}

	public void decChances() {
		chances--;
	}

	/**
	 * @return the savedLocation
	 */
	public Location getSavedLocation() {
		return savedLocation;
	}

	/**
	 * @param savedLocation the savedLocation to set
	 */
	public void setSavedLocation(Location savedLocation) {
		this.savedLocation = savedLocation;
	}

	/**
	 * @return the receivedStarter
	 */
	public boolean hasReceivedStarter() {
		return receivedStarter;
	}

	/**
	 * @param receivedStarter the receivedStarter to set
	 */
	public void setReceivedStarter(boolean receivedStarter) {
		this.receivedStarter = receivedStarter;
	}

	/**
	 * @return the expMode
	 */
	public int getExpMode() {
		return expMode;
	}

	/**
	 * @param expMode the expMode to set
	 */
	public void setExpMode(int expMode) {
		this.expMode = expMode;
	}

	/**
	 * @return the tokens
	 */
	public int[] getTokens() {
		return tokens;
	}

	/**
	 * @param tokens the tokens to set
	 */
	public void setTokens(int[] tokens) {
		this.tokens = tokens;
	}

	public boolean[] getKilledBrothers() {
		return killedBrothers;
	}

	public void setKilledBrothers(boolean[] killedBrothers) {
		this.killedBrothers = killedBrothers;
	}

	public int[] getKillCount() {
		return killCount;
	}

	public void setKillCount(int[] killCount) {
		this.killCount = killCount;
	}

}