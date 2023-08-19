package com.sevador.game.node.player;

import java.io.Serializable;
import java.util.HashMap;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.AppearanceUpdate;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.utility.Misc;

public class AuraManager implements Serializable {

	private static final long serialVersionUID = -8860530757819448608L;

	private transient Player player;
	private long activation;
	private HashMap<Integer, Long> cooldowns;

	public AuraManager() {
		cooldowns = new HashMap<Integer, Long>();
	}

	protected void setPlayer(Player player) {
		this.player = player;
	}

	public void process() {
		if (!isActivated() || Misc.currentTimeMillis() < activation)
			return;
		desactive();
		player.getUpdateMasks().register(new AppearanceUpdate(player));
	}

	public void removeAura() {
		if (isActivated()) {
			desactive();
		}
	}

	public void desactive() {
		activation = 0;
		player.getPacketSender().sendMessage("Your aura has depleted.");
	}

	public long getCoolDown(int aura) {
		Long coolDown = cooldowns.get(aura);
		if (coolDown == null)
			return 0;
		return coolDown;
	}

	public void activate() {
		int aura = player.getEquipment().getAuraId();
		if (aura == -1)
			return;
		player.getWalkingQueue().reset();
		if (activation != 0) {
			player.getPacketSender().sendMessage("You already have an activated aura.");
			return;
		}
		if (Misc.currentTimeMillis() <= getCoolDown(aura)) {
			return;
		}
		activation = Misc.currentTimeMillis() + getActivationTime(aura) * 1000;
		cooldowns.put(aura, activation + getCooldown(aura) * 1000);
		player.setAnimation(new Animation(2231));
		player.setGraphic(new Graphic(getActiveGraphic(getTier(aura))));
		player.getUpdateMasks().register(new AppearanceUpdate(player));
	}

	public void sendAuraRemainingTime() {
		if (!isActivated()) {
			long cooldown = getCoolDown(player.getEquipment().getAuraId());
			if (Misc.currentTimeMillis() <= cooldown) {
				player.getPacketSender().sendMessage(
						"Currently recharging. <col=ff0000>"
								+ getFormatedTime((cooldown - Misc
										.currentTimeMillis()) / 1000)
								+ " remaining.");
				return;
			}
			player.getPacketSender().sendMessage(
					"Currently desactivate. It is ready to use.");
			return;
		}
		player.getPacketSender().sendMessage(
				"Currently active. <col=00ff00>"
						+ getFormatedTime((activation - Misc
								.currentTimeMillis()) / 1000) + " remaining");
	}

	public String getFormatedTime(long seconds) {
		long minutes = seconds / 60;
		long hours = minutes / 60;
		minutes -= hours * 60;
		seconds -= (hours * 60 * 60) + (minutes * 60);
		String minutesString = (minutes < 10 ? "0" : "") + minutes;
		String secondsString = (seconds < 10 ? "0" : "") + seconds;
		return hours + ":" + minutesString + ":" + secondsString;
	}

	public void sendTimeRemaining(int aura) {
		long cooldown = getCoolDown(aura);
		if (cooldown < Misc.currentTimeMillis()) {
			player.getPacketSender().sendMessage(
					"The aura has finished recharging. It is ready to use.");
			return;
		}
		player.getPacketSender().sendMessage("Currently recharging. <col=ff0000>"+ getFormatedTime((cooldown - Misc.currentTimeMillis()) / 1000)+ " remaining.");
	}

	public boolean isActivated() {
		return activation != 0;
	}

	public int getAuraModelId() {
		Item weapon = player.getEquipment().getItem(Equipment.SLOT_WEAPON);
		if (weapon == null)
			return 8719;
		String name = weapon.getDefinition().getName().toLowerCase();
		if (name.contains("dagger"))
			return 8724;
		if (name.contains("whip"))
			return 8725;
		if (name.contains("2h sword") || name.contains("godsword"))
			return 8773;
		if (name.contains("sword") || name.contains("scimitar")
				|| name.contains("korasi"))
			return 8722;
		return 8719;
	}

	public int getActiveGraphic(int tier) {
		if (tier == 2)
			return 1764;
		if (tier == 3)
			return 1763;
		return 370; // default gold
	}

	public boolean hasPoisonPurge() {
		if (!isActivated())
			return false;
		int aura = player.getEquipment().getAuraId();
		return aura == 20958 || aura == 22268;
	}

	public double getMagicAccurayMultiplier() {
		if (!isActivated() || player.isAtWilderness())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 20962)
			return 1.03;
		if (aura == 22270)
			return 1.05;
		return 1;
	}

	public double getRangeAccurayMultiplier() {
		if (!isActivated() || player.isAtWilderness())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 20967)
			return 1.03;
		if (aura == 22272)
			return 1.05;
		return 1;
	}

	public double getWoodcuttingAccurayMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 22280)
			return 1.03;
		if (aura == 22282)
			return 1.05;
		return 1;
	}

	public double getMininingAccurayMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 22284)
			return 1.03;
		if (aura == 22286)
			return 1.05;
		return 1;
	}

	public double getFishingAccurayMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 20966)
			return 1.03;
		if (aura == 22274)
			return 1.05;
		return 1;
	}

	public double getPrayerPotsRestoreMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 20965)
			return 1.03;
		if (aura == 22276)
			return 1.05;
		return 1;
	}

	public double getThievingAccurayMultiplier() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 22288)
			return 1.03;
		if (aura == 22290)
			return 1.05;
		return 1;
	}

	public double getChanceNotDepleteMN_WC() {
		if (!isActivated())
			return 1;
		int aura = player.getEquipment().getAuraId();
		if (aura == 22292)
			return 1.1;
		return 1;
	}

	public boolean usingEquilibrium() {
		if (!isActivated() || player.isAtWilderness())
			return false;
		int aura = player.getEquipment().getAuraId();
		return aura == 22294;
	}

	public boolean usingPenance() {
		if (!isActivated())
			return false;
		int aura = player.getEquipment().getAuraId();
		return aura == 22300;
	}

	public void checkSuccefulHits(int damage) {
		if (!isActivated() || player.isAtWilderness())
			return;
		int aura = player.getEquipment().getAuraId();
		if (aura == 22296)
			useInspiration();
		else if (aura == 22298)
			useVampyrism(damage);
	}

	public void useVampyrism(int damage) {
		int heal = (int) (damage * 0.05);
		if (heal > 0)
			player.getSkills().heal(heal);
	}

	public void useInspiration() {
		Integer atts = (Integer) player.getTemporaryAttributtes().get(
				"InspirationAura");
		if (atts == null)
			atts = 0;
		atts++;
		if (atts == 5) {
			atts = 0;
			player.getSettings().updateSpecialEnergy(-1);
		}
		player.getTemporaryAttributtes().put("InspirationAura", atts);
	}

	public boolean usingWisdom() {
		if (!isActivated())
			return false;
		int aura = player.getEquipment().getAuraId();
		return aura == 22302;
	}

	/*
	 * return seconds
	 */
	public static int getActivationTime(int aura) {
		switch (aura) {
		case 20958:
			return 600; // 10minutes
		case 22268:
			return 1200; // 20minutes
		case 22302:
			return 1800; // 30minutes
		case 22294:
			return 7200; // 2hours
		case 20959:
			return 10800; // 3hours
		default:
			return 3600; // default 1hour
		}
	}

	public static int getCooldown(int aura) {
		switch (aura) {
		case 20962:
		case 22270:
		case 20967:
		case 22272:
		case 22280:
		case 22282:
		case 22284:
		case 22286:
		case 20966:
		case 22274:
		case 20965:
		case 22276:
		case 22288:
		case 22290:
		case 22292:
		case 22296:
		case 22298:
		case 22300:
			return 10800; // 3hours
		case 22294:
			return 14400; // 4hours
		case 20959:
		case 22302:
			return 86400; // 24hours
		default:
			return 3600; // default 1hour
		}
	}

	public static int getTier(int aura) {
		switch (aura) {
		case 22302:
			return 3;
		case 20959:
		case 22270:
		case 22272:
		case 22282:
		case 22286:
		case 22274:
		case 22276:
		case 22290:
		case 22292:
		case 22294:
		case 22296:
		case 22298:
		case 22300:
			return 2;
		default:
			return 1; // default 1
		}
	}
}
