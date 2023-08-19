package com.sevador.game.node.model.skills.combat;
/*package com.sevador.game.skills.combat;

import net.burtleburtle.cache.format.ItemDefinition;
import net.burtleburtle.cores.tasks.WorldTask;
import net.burtleburtle.cores.tasks.WorldTasksManager;
import net.burtleburtle.thread.NodeWorker;

import com.sevador.game.action.SkillAction;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.npc.impl.Familiar;
import com.sevador.game.node.player.ActionSender;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Settings;
import com.sevador.game.node.player.Skills;
import com.sevador.game.skills.combat.Hit.HitLook;
import com.sevador.game.world.World;
import com.sevador.util.Constants;
import com.sevador.util.Misc;

@SuppressWarnings("unused")
public class PlayerCombat extends SkillAction {

	private Entity target;
	private int max_hit; // temporary constant
	private double base_mage_xp; // temporary constant
	private int mage_hit_gfx; // temporary constant
	private int magic_sound; // temporary constant
	private int max_poison_hit; // temporary constant
	private int freeze_time; // temporary constant

	private boolean reduceAttack; // temporary constant
	private boolean blood_spell; // temporary constant
	private boolean block_tele;
	private boolean multi_attack;// temporary constant

	public PlayerCombat(Entity target) {
		this.target = target;
	}

	@Override
	public boolean start(Player player) {
		player.turnTo(target);
		if (checkAll(player))
			return true;
		player.turnTo(null);
		return false;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		int isRanging = isRanging(player);
		int spellId = player.getCombatDefinitions().getSpellId();
		int maxDistance = isRanging != 0 || spellId > 0 ? 7 : 0;
		int distanceX = player.getX() - target.getX();
		int distanceY = player.getY() - target.getY();
		int size = target.getSize();
		
		 * if (!player.clipedProjectile(target, target instanceof NexMinion ?
		 * false : maxDistance == 0)) return 0;
		 
		if (player.hasWalkSteps())
			maxDistance += 1;
		if (distanceX > size + maxDistance || distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance)
			return 0;
		if (!player.getControlerManager().keepCombating(target))
			return -1;
		addAttackedByDelay(player);
		if (spellId > 0) {
			boolean manualCast = spellId >= 256;
			return mageAttack(player, manualCast ? spellId - 256 : spellId,
					!manualCast);
		} else {
			if (isRanging == 0) {
				return meleeAttack(player);
			} else if (isRanging == 1) {
				player.getPacketSender().sendGameMessage(
						"This ammo is not very effective with your bow.", true);
				return -1;
			} else if (isRanging == 3) {
				player.getPacketSender().sendGameMessage(
						"You dont have any ammo in your backpack.", true);
				return -1;
			} else {
				return rangeAttack(player);
			}
		}
	}

	private void addAttackedByDelay(Entity player) {
		target.setAttackedBy(player);
		target.setAttackedByDelay(Misc.currentTimeMillis() + 8000); // 8seconds
	}

	private int getRangeCombatDelay(int weaponId, int attackStyle) {
		int delay = 6;
		if (weaponId != -1) {
			String weaponName = ItemDefinition.forId(weaponId).getName()
					.toLowerCase();
			if (weaponName.contains("shortbow")
					|| weaponName.contains("karil's crossbow"))
				delay = 3;
			else if (weaponName.contains("crossbow"))
				delay = 5;
			else if (weaponName.contains("dart")
					|| weaponName.contains("knife"))
				delay = 2;
			else {
				switch (weaponId) {
				case 15241:
					delay = 7;
					break;
				case 11235: // dark bows
				case 15701:
				case 15702:
				case 15703:
				case 15704:
					delay = 9;
					break;
				default:
					delay = 6;
					break;
				}
			}
		}
		if (attackStyle == 1)
			delay--;
		else if (attackStyle == 2)
			delay++;
		return delay;
	}

	public int mageAttack(Player player, int spellId, boolean autocast) {
		if (!autocast) {
			player.getCombatDefinitions().resetSpells(false);
			player.getSkillAction().forceStop();
		}
		if (!Magic.checkCombatSpell(player, spellId, -1, true)) {
			if (autocast)
				player.getCombatDefinitions().resetSpells(true);
			return -1; // stops
		}
		if (player.getCombatDefinitions().getSpellBook() == 192) {
			switch (spellId) {
			case 25: // air strike
				player.setAnimation(new Animation(14221));
				mage_hit_gfx = 2700;
				base_mage_xp = 5.5;
				delayMagicHit(2,
						getMagicHit(player, getRandomMagicMaxHit(player, 20)));
				ActionSender.sendProjectiles(Projectile.create(player, target, 2699, 18, 18, 50, 50, 0, 0));
				return 5;
				case 28: // water strike
					player.setGraphic(new Graphic(2701));
					player.setAnimation(new Animation(14221));
					mage_hit_gfx = 2708;
					base_mage_xp = 7.5;
					delayMagicHit(2,
							getMagicHit(player, getRandomMagicMaxHit(player, 40)));
					ActionSender.sendProjectiles(Projectile.create(player, target, 2703, 18, 18, 50, 50, 0, 0));
					return 5;
					case 36:// bind
						player.setGraphic(new Graphic(177));
						player.setAnimation(new Animation(710));
						mage_hit_gfx = 181;
						base_mage_xp = 60.5;
						Hit magicHit = getMagicHit(player,
								getRandomMagicMaxHit(player, 20));
						delayMagicHit(2, magicHit);
						ActionSender.sendProjectiles(Projectile.create(player, target, 178, 18, 18, 50, 50, 0, 0));
						long currentTime = Misc.currentTimeMillis();
						if (magicHit.getDamage() > 0
								&& target.getFrozenBlocked() < currentTime)
							target.addFreezeDelay(5000, true);
						return 5;
						case 55:// snare
							player.setGraphic(new Graphic(177));
							player.setAnimation(new Animation(710));
							mage_hit_gfx = 180;
							base_mage_xp = 91.1;
							Hit snareHit = getMagicHit(player,
									getRandomMagicMaxHit(player, 30));
							delayMagicHit(2, snareHit);
							if (snareHit.getDamage() > 0
									&& target.getFrozenBlocked() < Misc
									.currentTimeMillis())
								target.addFreezeDelay(10000, true);
							ActionSender.sendProjectiles(Projectile.create(player, target, 178, 18, 50, 50, 0, 0));
							return 5;
						case 81:// entangle
							player.setGraphic(new Graphic(177));
							player.setAnimation(new Animation(710));
							mage_hit_gfx = 179;
							base_mage_xp = 91.1;
							Hit entangleHit = getMagicHit(player,
									getRandomMagicMaxHit(player, 50));
							delayMagicHit(2, entangleHit);
							if (entangleHit.getDamage() > 0
									&& target.getFrozenBlocked() < Misc
									.currentTimeMillis())
								target.addFreezeDelay(20000, true);
							ActionSender.sendProjectiles(Projectile.create(player, target, 178, 18, 18, 50, 50, 0, 0));
							return 5;
							case 30: // earth strike
								player.setGraphic(new Graphic(2713));
								player.setAnimation(new Animation(14221));
								mage_hit_gfx = 2723;
								base_mage_xp = 9.5;
								delayMagicHit(2,
										getMagicHit(player, getRandomMagicMaxHit(player, 60)));
								ActionSender.sendProjectiles(Projectile.create(player, target, 2718, 18, 18, 50, 50, 0, 0));
								return 5;
								case 32: // fire strike
									player.setGraphic(new Graphic(2728));
									player.setAnimation(new Animation(14221));
									mage_hit_gfx = 2737;
									base_mage_xp = 11.5;
									int damage = getRandomMagicMaxHit(player, 80);
									if (target instanceof NPC) {
										NPC n = (NPC) target;
										if (n.getId() == 9463) // ice verm
											damage *= 2;
									}
									delayMagicHit(2, getMagicHit(player, damage));
									ActionSender.sendProjectiles(Projectile.create(player, target, 2729, 18, 18, 50, 50, 0, 0));
									return 5;
									case 34: // air bolt
										player.setAnimation(new Animation(14220));
										mage_hit_gfx = 2700;
										base_mage_xp = 13.5;
										delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 90)));
										ActionSender.sendProjectiles(Projectile.create(player, target, 2699, 18, 18, 50, 50, 0, 0));
										return 5;
										case 39: // water bolt
											player.setGraphic(new Graphic(2707, 0, 100, false));
											player.setAnimation(new Animation(14220));
											mage_hit_gfx = 2709;
											base_mage_xp = 16.5;
											delayMagicHit(2, getMagicHit(player, getRandomMagicMaxHit(player, 100)));
											ActionSender.sendProjectiles(Projectile.create(player, target, 2704, 18, 18, 50, 50, 0, 0));
											return 5;
											case 42: // earth bolt
												player.setGraphic(new Graphic(2714));
												player.setAnimation(new Animation(14222));
												mage_hit_gfx = 2724;
												base_mage_xp = 19.5;
												delayMagicHit(2,
														getMagicHit(player, getRandomMagicMaxHit(player, 110)));
												ActionSender.sendProjectiles(Projectile.create(player, target, 2719, 18, 18, 50, 50, 0, 0));
												return 5;
												case 45: // fire bolt
													player.setGraphic(new Graphic(2728));
													player.setAnimation(new Animation(14223));
													mage_hit_gfx = 2738;
													base_mage_xp = 22.5;
													damage = getRandomMagicMaxHit(player, 120);
													if (target instanceof NPC) {
														NPC n = (NPC) target;
														if (n.getId() == 9463) // ice verm
															damage *= 2;
													}
													delayMagicHit(2, getMagicHit(player, damage));
													ActionSender.sendProjectiles(Projectile.create(player, target, 2731, 18, 18, 50, 50, 0, 0));
													return 5;
													case 49: // air blast
														player.setAnimation(new Animation(14221));
														mage_hit_gfx = 2700;
														base_mage_xp = 25.5;
														delayMagicHit(2,
																getMagicHit(player, getRandomMagicMaxHit(player, 130)));
														ActionSender.sendProjectiles(Projectile.create(player, target, 2699, 18, 18, 50, 50, 0, 0));
														return 5;
														case 52: // water blast
															player.setGraphic(new Graphic(2701));
															player.setAnimation(new Animation(14220));
															mage_hit_gfx = 2710;
															base_mage_xp = 31.5;
															delayMagicHit(2,
																	getMagicHit(player, getRandomMagicMaxHit(player, 140)));
															ActionSender.sendProjectiles(Projectile.create(player, target, 2705, 18, 18, 50, 50, 0, 0));
															return 5;
															case 58: // earth blast
																player.setGraphic(new Graphic(2715));
																player.setAnimation(new Animation(14222));
																mage_hit_gfx = 2725;
																base_mage_xp = 31.5;
																delayMagicHit(2,
																		getMagicHit(player, getRandomMagicMaxHit(player, 150)));
																ActionSender.sendProjectiles(Projectile.create(player, target, 2720, 18, 18, 50, 50, 0, 0));
																return 5;
																case 63: // fire blast
																	player.setGraphic(new Graphic(2728));
																	player.setAnimation(new Animation(14223));
																	mage_hit_gfx = 2739;
																	base_mage_xp = 34.5;
																	damage = getRandomMagicMaxHit(player, 160);
																	if (target instanceof NPC) {
																		NPC n = (NPC) target;
																		if (n.getId() == 9463) // ice verm
																			damage *= 2;
																	}
																	delayMagicHit(2, getMagicHit(player, damage));
																	ActionSender.sendProjectiles(Projectile.create(player, target, 2733, 18, 18, 50, 50, 0, 0));
																	return 5;
																	case 66:// Saradomin Strike
																		player.setAnimation(new Animation(811));
																		mage_hit_gfx = 76;
																		base_mage_xp = 34.5;
																		delayMagicHit(2,
																				getMagicHit(player, getRandomMagicMaxHit(player, 300)));
																		return 5;
																	case 67: // Claws of Guthix
																		player.setAnimation(new Animation(811));
																		mage_hit_gfx = 77;
																		base_mage_xp = 34.5;
																		delayMagicHit(2,
																				getMagicHit(player, getRandomMagicMaxHit(player, 300)));
																		return 5;
																	case 68: // Flames of Zamorak
																		player.setAnimation(new Animation(811));
																		mage_hit_gfx = 78;
																		base_mage_xp = 34.5;
																		delayMagicHit(2,
																				getMagicHit(player, getRandomMagicMaxHit(player, 300)));
																		return 5;
																	case 70: // air wave
																		player.setAnimation(new Animation(14221));
																		mage_hit_gfx = 2700;
																		base_mage_xp = 36;
																		delayMagicHit(2,
																				getMagicHit(player, getRandomMagicMaxHit(player, 170)));
																		ActionSender.sendProjectiles(Projectile.create(player, target, 2699, 18, 18, 50, 50, 0, 0));
																		return 5;
																		case 73: // water wave
																			player.setGraphic(new Graphic(2702));
																			player.setAnimation(new Animation(14220));
																			mage_hit_gfx = 2710;
																			base_mage_xp = 37.5;
																			delayMagicHit(2,
																					getMagicHit(player, getRandomMagicMaxHit(player, 180)));
																			ActionSender.sendProjectiles(Projectile.create(player, target, 2706, 18, 18, 50, 50, 0, 0));
																			return 5;
																			case 77: // earth wave
																				player.setGraphic(new Graphic(2716));
																				player.setAnimation(new Animation(14222));
																				mage_hit_gfx = 2726;
																				base_mage_xp = 42.5;
																				delayMagicHit(2,
																						getMagicHit(player, getRandomMagicMaxHit(player, 190)));
																				ActionSender.sendProjectiles(Projectile.create(player, target, 2721, 18, 18, 50, 50, 0, 0));
																				return 5;
																				case 80: // fire wave
																					player.setGraphic(new Graphic(2728));
																					player.setAnimation(new Animation(14223));
																					mage_hit_gfx = 2740;
																					base_mage_xp = 42.5;
																					damage = getRandomMagicMaxHit(player, 200);
																					if (target instanceof NPC) {
																						NPC n = (NPC) target;
																						if (n.getId() == 9463) // ice verm
																							damage *= 2;
																					}
																					delayMagicHit(2, getMagicHit(player, damage));
																					ActionSender.sendProjectiles(Projectile.create(player, target, 2735, 18, 18, 50, 50, 0, 0));
																					return 5;
																					case 86: // teleblock
																						if (target instanceof Player
																								&& ((Player) target).getTeleBlockDelay() <= Misc
																								.currentTimeMillis()) {
																							player.setGraphic(new Graphic(1841));
																							player.setAnimation(new Animation(10503));
																							mage_hit_gfx = 1843;
																							base_mage_xp = 80;
																							Hit hit = getMagicHit(player,
																									getRandomMagicMaxHit(player, 30));
																							delayMagicHit(2, hit);
																							ActionSender.sendProjectiles(Projectile.create(player, target, 1842, 18, 18, 50, 50,
																									0, 0);
																							if (hit.getDamage() > 0)
																								block_tele = true;
																						} else {
																							player.getPacketSender().sendGameMessage(
																									"This player is already effected by this spell.",
																									true);
																						}
																						//TODO TELEBLOCK
																						return 5;
																					case 84:// air surge
																						player.setGraphic(new Graphic(457));
																						player.setAnimation(new Animation(10546));
																						mage_hit_gfx = 2700;
																						base_mage_xp = 80;
																						delayMagicHit(2,
																								getMagicHit(player, getRandomMagicMaxHit(player, 220)));
																						ActionSender.sendProjectiles(Projectile.create(player, target, 462, 18, 18, 50, 50, 0, 0));
																						return 5;
																						case 87:// water surge
																							player.setGraphic(new Graphic(2701));
																							player.setAnimation(new Animation(10542));
																							mage_hit_gfx = 2712;
																							base_mage_xp = 80;
																							delayMagicHit(2,
																									getMagicHit(player, getRandomMagicMaxHit(player, 240)));
																							ActionSender.sendProjectiles(Projectile.create(player, target, 2707, 18, 18, 50, 50, 3, 0));
																							return 5;
																							case 89:// earth surge
																								player.setGraphic(new Graphic(2717));
																								player.setAnimation(new Animation(14209));
																								mage_hit_gfx = 2727;
																								base_mage_xp = 80;
																								delayMagicHit(2,
																										getMagicHit(player, getRandomMagicMaxHit(player, 260)));
																								ActionSender.sendProjectiles(Projectile.create(player, target, 2722, 18, 18, 50, 50, 0, 0));
																								return 5;
																								case 91:// fire surge
																									player.setGraphic(new Graphic(2728));
																									player.setAnimation(new Animation(2791));
																									mage_hit_gfx = 2741;
																									base_mage_xp = 80;
																									damage = getRandomMagicMaxHit(player, 280);
																									if (target instanceof NPC) {
																										NPC n = (NPC) target;
																										if (n.getId() == 9463) // ice verm
																											damage *= 2;
																									}
																									delayMagicHit(2, getMagicHit(player, damage));
																									ActionSender.sendProjectiles(Projectile.create(player, target, 2735, 18, 18, 50, 50, 3, 0));
																									ActionSender.sendProjectiles(Projectile.create(player, target, 2736, 18, 18, 50, 50, 20,
																											0));
																									ActionSender.sendProjectiles(Projectile.create(player, target, 2736, 18, 18, 50, 50, 110,
																											0));
																									return 5;
																									case 99: // Storm of armadyl //Sonic and Tyler dumped
																										player.setGraphic(new Graphic(457));
																										player.setAnimation(new Animation(10546));
																										mage_hit_gfx = 1019;
																										base_mage_xp = 70;
																										int boost = (player.getSkills().getLevel(Skills.MAGIC) - 77) * 5;
																										int hit = getRandomMagicMaxHit(player, 160 + boost);
																										if (hit != 0 && hit < boost)
																											hit += boost;
																										delayMagicHit(2, getMagicHit(player, hit));
																										ActionSender.sendProjectiles(Projectile.create(player, target, 1019, 18, 18, 50, 30, 0, 0));
																										return player.getEquipment().getWeaponId() == 21777 ? 4 : 5;
			}
		} else if (player.getCombatDefinitions().getSpellBook() == 193) {
			switch (spellId) {
			case 32:// Shadow Rush
				player.setAnimation(new Animation(1978));
				mage_hit_gfx = 379;
				base_mage_xp = 31;
				reduceAttack = true;
				delayMagicHit(2,
						getMagicHit(player, getRandomMagicMaxHit(player, 140)));
				ActionSender.sendProjectiles(Projectile.create(player, target, 380, 18, 18, 50, 50, 0, 0));
				return 4;
				case 24:// Blood rush
					player.setAnimation(new Animation(1978));
					mage_hit_gfx = 373;
					base_mage_xp = 33;
					blood_spell = true;
					delayMagicHit(2,
							getMagicHit(player, getRandomMagicMaxHit(player, 150)));
					ActionSender.sendProjectiles(Projectile.create(player, target, 374, 18, 18, 50, 50, 0, 0));
					return 4;
					case 28:// Smoke Rush
						player.setAnimation(new Animation(1978));
						mage_hit_gfx = 385;
						base_mage_xp = 30;
						max_poison_hit = 20;
						delayMagicHit(2,
								getMagicHit(player, getRandomMagicMaxHit(player, 130)));
						ActionSender.sendProjectiles(Projectile.create(player, target, 386, 18, 18, 50, 50, 0, 0));
						return 4;
						case 29:// Smoke Blitz
							player.setAnimation(new Animation(1978));
							mage_hit_gfx = 387;
							base_mage_xp = 42;
							max_poison_hit = 40;
							delayMagicHit(2,
									getMagicHit(player, getRandomMagicMaxHit(player, 230)));
							ActionSender.sendProjectiles(Projectile.create(player, target, 386, 18, 18, 50, 50, 0, 0));
							return 4;
							case 33:// Shadow Blitz
								player.setAnimation(new Animation(1978));
								mage_hit_gfx = 381;
								base_mage_xp = 43;
								reduceAttack = true;
								delayMagicHit(2,
										getMagicHit(player, getRandomMagicMaxHit(player, 240)));
								ActionSender.sendProjectiles(Projectile.create(player, target, 380, 18, 18, 50, 50, 0, 0));
								return 4;
								case 25:// Blood Blitz
									player.setAnimation(new Animation(1978));
									mage_hit_gfx = 375;
									base_mage_xp = 45;
									blood_spell = true;
									delayMagicHit(2,
											getMagicHit(player, getRandomMagicMaxHit(player, 250)));
									ActionSender.sendProjectiles(Projectile.create(player, target, 374, 18, 18, 50, 50, 0, 0));
									return 4;
									case 20:// Ice rush
										player.setAnimation(new Animation(1978));
										mage_hit_gfx = 361;
										base_mage_xp = 34;
										freeze_time = 5000;
										delayMagicHit(2,
												getMagicHit(player, getRandomMagicMaxHit(player, 160)));
										ActionSender.sendProjectiles(Projectile.create(player, target, 362, 18, 18, 50, 50, 0, 0));
										return 4;
										case 21:// Ice Blitz
											player.setGraphic(new Graphic(366));
											player.setAnimation(new Animation(1978));
											mage_hit_gfx = 367;
											base_mage_xp = 46;
											freeze_time = 15000;
											magic_sound = 169;
											delayMagicHit(4,
													getMagicHit(player, getRandomMagicMaxHit(player, 260)));
											return 4;
										case 27:
											mage_hit_gfx = 377;
											base_mage_xp = 51D;
											blood_spell = true;
											player.setAnimation(new Animation(1979));
											playSound(170, player, target);
											delayMagicHit(
													2,
													new Hit[] { getMagicHit(player,
															getRandomMagicMaxHit(player, 250)) });
											return 4;
										case 23: // ice barrage
											player.setAnimation(new Animation(1979));
											playSound(171, player, target);
											magic_sound = 168;
											long currentTime = Misc.currentTimeMillis();
											if (target.getSize() >= 2
													|| target.getFreezeDelay() >= currentTime
													|| target.getFrozenBlocked() >= currentTime) {
												mage_hit_gfx = 1677;
											} else {
												mage_hit_gfx = 369;
												freeze_time = 20000;
											}
											base_mage_xp = 52;
											delayMagicHit(2,
													getMagicHit(player, getRandomMagicMaxHit(player, 300)));
											return 4;
			}

		}
		return -1; // stops atm

	}

	public int getRandomMagicMaxHit(Player player, int baseDamage) {
		int hit = Misc.random(getMagicMaxHit(player, baseDamage));
		if (hit != 0) {
			if (target instanceof NPC) {
				NPC n = (NPC) target;
				if (n.getId() == 9463 && hasFireCape(player)) // ice verm
					hit += 40;
			}
		}
		return hit;
	}

	
	 * original formula
	 
	public int getMagicMaxHit(Player player, int baseDamage) {
		double defBonus = 0;
		double effectiveMagicDefence = 0;
		double att = ((player.getSkills().getLevel(Skills.MAGIC) + 8)
																	 * * player
																	 * .
																	 * getPrayer
																	 * ().
																	 * getMageMultiplier
																	 * ()
																	 );
		// TODO mage multiplier
		int style = CombatDefinitions.getXpStyle(player.getEquipment()
				.getWeaponId(), player.getCombatDefinitions().getAttackStyle());
		att += style == Skills.ATTACK ? 3
				: style == CombatDefinitions.SHARED ? 1 : 0;
		// att *= player.getAuraManager().getMagicAccurayMultiplier();
		if (fullVoidEquipped(player, 11663, 11674))
			att *= 1.3;
		double def = 0;
		if (target instanceof Player) {
			Player p2 = (Player) target;
			int p2style = CombatDefinitions.getXpStyle(p2.getEquipment()
					.getWeaponId(), p2.getCombatDefinitions().getAttackStyle());
			def = ((p2.getSkills().getLevel(Skills.MAGIC) + 8)
					+ p2.getPrayer().getDefenceMultiplier() + p2style == Skills.DEFENCE ? 3
					: p2style == CombatDefinitions.SHARED ? 1 : 0);
			defBonus = p2.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DEF];
			effectiveMagicDefence = Math.floor((double) p2.getSkills()
					.getLevel(Skills.MAGIC));
		} else {
			NPC n = (NPC) target;
			def = n.getBonuses() == null ? 0
					: n.getBonuses()[CombatDefinitions.MAGIC_DEF];
			defBonus = def;
		}
		effectiveMagicDefence *= 0.7;
		def *= 0.3;

		def = Math.floor(def);
		effectiveMagicDefence = Math.floor(effectiveMagicDefence);

		def = def + effectiveMagicDefence;
		double attBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_ATTACK];
		double attackRoll = Math.floor(att * (1 + attBonus / 64));
		double defenceRoll = Math.floor(def * (1 + defBonus / 64));
		double accuracy = 0.005;
		if (attackRoll < defenceRoll)
			accuracy = (attackRoll - 1) / (2 * defenceRoll);
		else
			accuracy = 1 - (defenceRoll + 1) / (2 * attackRoll);
		if (Misc.getRandomGenerator().nextDouble() > accuracy)
			return 0;

		double extraDamage = player.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DAMAGE];
		max_hit = (int) (baseDamage * ((double) (1.0 + extraDamage / 100)));
		// max_hit *= player.getPrayer().getMageMultiplier();
		double boost = 1 + ((player.getSkills().getLevel(Skills.MAGIC) - player
				.getSkills().getLevel(Skills.MAGIC)) * 0.03);
		if (boost > 1)
			max_hit *= boost;
		return max_hit;
	}

	
	 * public int getMagicMaxHit(Player player, int baseDamage) { double att =
	 * player.getSkills().getLevel(6) +
	 * player.getCombatDefinitions().getBonuses()[3]; att *=
	 * player.getPrayer().getMageMultiplier(); if (fullVoidEquipped(player, new
	 * int[] { 11663, 11674 })) att *= 1.3D; att *=
	 * player.getAuraManager().getMagicAccurayMultiplier(); double def; if
	 * (target instanceof Player) { Player p2 = (Player) target; def =
	 * ((p2.getSkills().getLevel(1) * 0.30) + (p2.getSkills() .getLevel(6) *
	 * 0.70) + .2D) + (p2.getCombatDefinitions().getBonuses()[8]); def *=
	 * (p2.getPrayer().getDefenceMultiplier() * 0.30D) +
	 * (p2.getPrayer().getMageMultiplier() * 0.70D); } else { NPC n = (NPC)
	 * target; def = n.getBonuses() != null ? n.getBonuses()[8] : 0; } double
	 * prob = att / def; if (prob > 0.90000000000000002D) prob =
	 * 0.90000000000000002D; else if (prob < 0.050000000000000003D) prob =
	 * 0.050000000000000003D; //System.out.println(prob); if (prob <
	 * Math.random()) return 0; double extraDamage =
	 * player.getCombatDefinitions().getBonuses()[17]; max_hit = (int) ((double)
	 * baseDamage * (1.0D + extraDamage / 100D)); double boost = 1.0D + (double)
	 * (player.getSkills().getLevel(6) - player .getSkills().getLevelForXp(6)) *
	 * 0.029999999999999999D; if (boost > 1.0D) max_hit *= boost; return
	 * max_hit; }
	 // 184.1035

	private int rangeAttack(final Player player) {
		final int weaponId = player.getEquipment().getWeaponId();
		final int attackStyle = player.getCombatDefinitions().getAttackStyle();
		int combatDelay = getRangeCombatDelay(weaponId, attackStyle);
		int soundId = getSoundId(weaponId, attackStyle);
		if (player.getCombatDefinitions().isUsingSpecialAttack()) {
			int specAmt = getSpecialAmmount(weaponId);
			if (specAmt == 0) {
				player.getPacketSender().sendGameMessage("This weapon has no special Attack, if you still see special bar please relogin.");
				player.getCombatDefinitions().desecreaseSpecialAttack(0);
				return combatDelay;
			}
			if (player.getCombatDefinitions().hasRingOfVigour())
				specAmt *= 0.9;
			if (player.getCombatDefinitions().getSpecialAttackPercentage() < specAmt) {
				player.getPacketSender().sendGameMessage(
						"You don't have enough power left.", true);
				player.getCombatDefinitions().desecreaseSpecialAttack(0);
				return combatDelay;
			}
			player.getCombatDefinitions().desecreaseSpecialAttack(specAmt);
			switch (weaponId) {
			case 19149:// zamorak bow
			case 19151:
				player.setAnimation(new Animation(426));
				player.setGraphic(new Graphic(97));
				ActionSender.sendProjectiles(Projectile.create(player, target, 100, 41, 16, 25, 35, 16, 0));
				delayHit(
						1,
						weaponId,
						attackStyle,
						getRangeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										true, true, 1.0, true)));
				dropAmmo(player, 1);
				break;
				case 19146:
				case 19148:// guthix bow
					player.setAnimation(new Animation(426));
					player.setGraphic(new Graphic(95));
					ActionSender.sendProjectiles(Projectile.create(player, target, 98, 41, 16, 25, 35, 16, 0));
					delayHit(
							1,
							weaponId,
							attackStyle,
							getRangeHit(
									player,
									getRandomMaxHit(player, weaponId, attackStyle,
											true, true, 1.0, true)));
					dropAmmo(player, 1);
					break;
					case 19143:// saradomin bow
					case 19145:
						player.setAnimation(new Animation(426));
						player.setGraphic(new Graphic(96));
						ActionSender.sendProjectiles(Projectile.create(player, target, 99, 41, 16, 25, 35, 16, 0));
						delayHit(
								1,
								weaponId,
								attackStyle,
								getRangeHit(
										player,
										getRandomMaxHit(player, weaponId, attackStyle,
												true, true, 1.0, true)));
						dropAmmo(player, 1);
						break;
						case 859: // magic longbow
						case 861: // magic shortbow
						case 10284: // Magic composite bow
						case 18332: // Magic longbow (sighted)
							player.setAnimation(new Animation(1074));
							player.setGraphic(new Graphic(249, 0, 100, false));
							ActionSender.sendProjectiles(Projectile.create(player, target, 249, 41, 16, 50, 35, 16, 0));
							ActionSender.sendProjectiles(Projectile.create(player, target, 249, 41, 16, 50, 35, 21, 0));
							delayHit(
									2,
									weaponId,
									attackStyle,
									getRangeHit(
											player,
											getRandomMaxHit(player, weaponId, attackStyle,
													true, true, 1.0, true)));
							delayHit(
									3,
									weaponId,
									attackStyle,
									getRangeHit(
											player,
											getRandomMaxHit(player, weaponId, attackStyle,
													true, true, 1.0, true)));
							dropAmmo(player, 2);
							break;
							case 15241: // Hand cannon
								WorldTasksManager.schedule(new WorldTask() {
									int loop = 0;

									@Override
									public void run() {
										if ((target.isDead() || player.isDead() || loop > 1)
												&& !NodeWorker.getNPCs().contains(target)) {
											stop();
											return;	
										}
										if (loop == 0) {
											player.setAnimation(new Animation(12174));
											player.setGraphic(new Graphic(2138, 0, 0, true));
											ActionSender.sendProjectiles(Projectile.create(player, target, 2143, 18, 18,
													50, 50, 0, 0));
											delayHit(
													1,
													weaponId,
													attackStyle,
													getRangeHit(
															player,
															getRandomMaxHit(player, weaponId,
																	attackStyle, true, true,
																	1.0, true)));
										} else if (loop == 1) {
											player.setAnimation(new Animation(12174));
											player.setGraphic(new Graphic(2138));
											ActionSender.sendProjectiles(Projectile.create(player, target, 2143, 18, 18,
													50, 50, 0, 0));
											delayHit(
													1,
													weaponId,
													attackStyle,
													getRangeHit(
															player,
															getRandomMaxHit(player, weaponId,
																	attackStyle, true, true,
																	1.0, true)));
											stop();
										}
										loop++;
									}
								}, 0, (int) 0.25);
											combatDelay = 9;
											break;
										case 11235: // dark bows
										case 15701:
										case 15702:
										case 15703:
										case 15704:
											int ammoId = player.getEquipment().getAmmoId();
											player.setAnimation(new Animation(getWeaponAttackEmote(
													weaponId, attackStyle)));
											player.setGraphic(new Graphic(getArrowThrowGfxId(
													weaponId, ammoId), 0, 100, false));
											if (ammoId == 11212) {
												int damage = getRandomMaxHit(player, weaponId, attackStyle,
														true, true, 1.5, true);
												if (damage < 80)
													damage = 80;
												int damage2 = getRandomMaxHit(player, weaponId,
														attackStyle, true, true, 1.5, true);
												if (damage2 < 80)
													damage2 = 80;
												ActionSender.sendProjectiles(Projectile.create(player, target, 1099, 41, 16, 31, 35,
														16, 0));
												ActionSender.sendProjectiles(Projectile.create(player, target, 1099, 41, 16, 25, 35,
														21, 0));
												delayHit(2, weaponId, attackStyle,
														getRangeHit(player, damage));
												delayHit(3, weaponId, attackStyle,
														getRangeHit(player, damage2));
												WorldTasksManager.schedule(new WorldTask() {
													@Override
													public void run() {
														target.setGraphic(new Graphic(1100, 0, 100, false));
													}
												}, 2);
											} else {
												int damage = getRandomMaxHit(player, weaponId, attackStyle,
														true, true, 1.3, true);
												if (damage < 50)
													damage = 50;
												int damage2 = getRandomMaxHit(player, weaponId,
														attackStyle, true, true, 1.3, true);
												if (damage2 < 50)
													damage2 = 50;
												ActionSender.sendProjectiles(Projectile.create(player, target, 1101, 41, 16, 31, 35,
														16, 0));
												ActionSender.sendProjectiles(Projectile.create(player, target, 1101, 41, 16, 25, 35,
														21, 0));
												delayHit(2, weaponId, attackStyle,
														getRangeHit(player, damage));
												delayHit(3, weaponId, attackStyle,
														getRangeHit(player, damage2));
											}
											dropAmmo(player, 2);

											break;
										case 14684: // zanik cbow
											player.setAnimation(new Animation(getWeaponAttackEmote(
													weaponId, attackStyle)));
											player.setGraphic(new Graphic(1714));
											ActionSender.sendProjectiles(Projectile.create(player, target, 2001, 41, 41, 41, 35, 0, 0));
											delayHit(
													2,
													weaponId,
													attackStyle,
													getRangeHit(
															player,
															getRandomMaxHit(player, weaponId, attackStyle,
																	true, true, 1.0, true)
																	+ 30
																	+ Misc.random(120)));
											dropAmmo(player);
											break;
											case 13954:// morrigan javelin
											case 12955:
											case 13956:
											case 13879:
											case 13880:
											case 13881:
											case 13882:
												player.setGraphic(new Graphic(1836));
												player.setAnimation(new Animation(10501));
												ActionSender.sendProjectiles(Projectile.create(player, target, 1837, 41, 41, 41, 35, 0, 0));
												final int hit = getRandomMaxHit(player, weaponId, attackStyle,
														true, true, 1.0, true);
												delayHit(2, weaponId, attackStyle, getRangeHit(player, hit));
												if (hit > 0) {
													final Entity finalTarget = target;
													WorldTasksManager.schedule(new WorldTask() {
														int damage = hit;

														@Override
														public void run() {
															if (finalTarget.isDead()) {
																stop();
																return;
															}
															if (damage > 50) {
																damage -= 50;
																finalTarget.applyHit(new Hit(player, 50,
																		HitLook.REGULAR_DAMAGE));
															} else {
																finalTarget.applyHit(new Hit(player, damage,
																		HitLook.REGULAR_DAMAGE));
																stop();
															}
														}
													}, 4, 2);
												}
												dropAmmo(player, -1);
												break;
												case 13883:
												case 13957:// morigan thrown axe
													player.setGraphic(new Graphic(1838));
													player.setAnimation(new Animation(10504));
													ActionSender.sendProjectiles(Projectile.create(player, target, 1839, 41, 41, 41, 35, 0, 0));
													delayHit(
															2,
															weaponId,
															attackStyle,
															getRangeHit(
																	player,
																	getRandomMaxHit(player, weaponId, attackStyle,
																			true, true, 1.0, true)));
													dropAmmo(player, -1);
													break;
													default:
														player.getPacketSender()
														.sendGameMessage(
																"This weapon has no special Attack, if you still see special bar please relogin.");
														return combatDelay;
			}
		} else {
			if (weaponId != -1) {
				String weaponName = ItemDefinition
						.forId(weaponId).getName().toLowerCase();
				if (weaponName.contains("throwing axe")
						|| weaponName.contains("knife")
						|| weaponName.contains("dart")
						|| weaponName.contains("javelin")
						|| weaponName.contains("thrownaxe")) {
					if (!weaponName.contains("javelin")
							&& !weaponName.contains("thrownaxe"))
						player.setGraphic(new Graphic(
								getKnifeThrowGfxId(weaponId), 0, 100, false));
					ActionSender.sendProjectiles(Projectile.create(player, target,
							getKnifeThrowGfxId(weaponId), 41, 16, 60, 30, 16, 0));
					delayHit(
							1,
							weaponId,
							attackStyle,
							getRangeHit(
									player,
									getRandomMaxHit(player, weaponId,
											attackStyle, true)));
					dropAmmo(player, -1);
				} else if (weaponName.contains("crossbow")) {
					int damage = 0;
					int ammoId = player.getEquipment().getAmmoId();
					if (ammoId != -1 && Misc.random(10) == 5) {
						switch (ammoId) {
						case 9237:
							damage = getRandomMaxHit(player, weaponId,
									attackStyle, true);
							target.setGraphic(new Graphic(755));
							if (target instanceof Player) {
								Player p2 = (Player) target;
							} else {
								NPC n = (NPC) target;
								n.setTarget(null);
							}
							soundId = 2914;
							break;
						case 9242:
							max_hit = Short.MAX_VALUE;
							damage = (int) (target.getHitpoints() * 0.2);
							target.setGraphic(new Graphic(754));
							player.applyHit(new Hit(target, player
									.getHitpoints() > 20 ? (int) (player
											.getHitpoints() * 0.1) : 1,
											HitLook.REFLECTED_DAMAGE));
							soundId = 2912;
							break;
						case 9243:
							damage = getRandomMaxHit(player, weaponId,
									attackStyle, true, false, 1.15, true);
							target.setGraphic(new Graphic(751));
							soundId = 2913;
							break;
						case 9244:
							damage = getRandomMaxHit(
									player,
									weaponId,
									attackStyle,
									true,
									false,
									!Combat.hasAntiDragProtection(target) ? 1.45
											: 1.0, true);
							target.setGraphic(new Graphic(756));
							soundId = 2915;
							break;
						case 9245:
							damage = getRandomMaxHit(player, weaponId,
									attackStyle, true, false, 1.15, true);
							target.setGraphic(new Graphic(753));
							player.getSkills().heal((int) (player.getSkills().getLifepoints(); * 0.25));
							soundId = 2917;
							break;
						default:
							damage = getRandomMaxHit(player, weaponId,
									attackStyle, true);
						}
					} else
						damage = getRandomMaxHit(player, weaponId, attackStyle,
								true);
					delayHit(2, weaponId, attackStyle,
							getRangeHit(player, damage));
					if (weaponId != 4740)
						dropAmmo(player);
					else
						player.getEquipment().remove(ammoId, 1);
				} else if (weaponId == 15241) {// handcannon
					if (Misc.random(player.getSkills().getLevel(
							Skills.FIREMAKING)) == 0) {
						// explode
						player.setGraphic(new Graphic(2140));
						player.getEquipment().getItems().set(3, null);
						player.getEquipment().refresh(3);
						player.getAppearence().generateAppearenceData();
						player.applyHit(new Hit(player,
								Misc.random(150) + 10,
								HitLook.REGULAR_DAMAGE));
						player.setAnimation(new Animation(12175));
						return combatDelay;
					} else {
						player.setGraphic(new Graphic(2138));
						ActionSender.sendProjectiles(Projectile.create(player, target, 2143, 18, 18, 60,
								30, 0, 0));
						delayHit(
								1,
								weaponId,
								attackStyle,
								getRangeHit(
										player,
										getRandomMaxHit(player, weaponId,
												attackStyle, true)));
						dropAmmo(player, -2);
					}
				} else if (weaponName.contains("crystal bow")) {
					player.setAnimation(new Animation(getWeaponAttackEmote(
							weaponId, attackStyle)));
					player.setGraphic(new Graphic(250));
					ActionSender.sendProjectiles(Projectile.create(player, target, 249, 41, 41, 41, 35,
							0, 0));
					delayHit(
							2,
							weaponId,
							attackStyle,
							getRangeHit(
									player,
									getRandomMaxHit(player, weaponId,
											attackStyle, true)));
				} else { // bow/default
					final int ammoId = player.getEquipment().getAmmoId();
					player.setGraphic(new Graphic(getArrowThrowGfxId(
							weaponId, ammoId), 0, 100));
					ActionSender.sendProjectiles(Projectile.create(player, target,
							getArrowProjectileGfxId(weaponId, ammoId), 41, 16,
							20, 35, 16, 0));
					delayHit(
							2,
							weaponId,
							attackStyle,
							getRangeHit(
									player,
									getRandomMaxHit(player, weaponId,
											attackStyle, true)));
					if (weaponId == 11235 || weaponId == 15701
							|| weaponId == 15702 || weaponId == 15703
							|| weaponId == 15704) { // dbows
						ActionSender.sendProjectiles(Projectile.create(player, target,
								getArrowProjectileGfxId(weaponId, ammoId), 41,
								35, 26, 35, 21, 0));

						delayHit(
								3,
								weaponId,
								attackStyle,
								getRangeHit(
										player,
										getRandomMaxHit(player, weaponId,
												attackStyle, true)));
						dropAmmo(player, 2);
					} else {
						if (weaponId != -1) {
							if (!weaponName.endsWith("bow full")
									&& !weaponName.equals("zaryte bow"))
								dropAmmo(player);
						}
					}
				}
				player.setAnimation(new Animation(getWeaponAttackEmote(
						weaponId, attackStyle)));
			}
		}
		playSound(soundId, player, target);
		return combatDelay;
	}

	public void dropAmmo(Player player, int quantity) {
		if (quantity == -2) {
			final int ammoId = player.getEquipment().getAmmoId();
			player.getEquipment().remove(ammoId, 1);
		} else if (quantity == -1) {
			final int weaponId = player.getEquipment().getWeaponId();
			if (weaponId != -1) {
				if (Misc.random(3) > 0) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId != -1
							&& ItemDefinition.forId(capeId).getName()
									.contains("Ava's"))
						return; // nothing happens
				} else {
					player.getEquipment().remove(weaponId, quantity);
					return;
				}
				player.getEquipment().remove(weaponId, quantity);
				player.sendMessage("Ammo will be added");
			}
		} else {
			final int ammoId = player.getEquipment().getAmmoId();
			if (Misc.getRandom(3) > 0) {
				int capeId = player.getEquipment().getCapeId();
				if (capeId != -1
						&& ItemDefinition.forId(capeId).getName()
								.contains("Ava's"))
					return; // nothing happens
			} else {
				player.getEquipment().remove(ammoId, quantity);
				return;
			}
			if (ammoId != -1) {
				player.getEquipment().remove(ammoId, quantity);
				World.updateGroundItem(
						new Item(ammoId, quantity),
						new WorldTile(target.getCoordFaceX(target.getSize()),
								target.getCoordFaceY(target.getSize()), target
										.getZ()), player);
			}
		}
	}

	public void dropAmmo(Player player) {
		dropAmmo(player, 1);
	}

	public int getArrowThrowGfxId(int weaponId, int arrowId) {
		if (arrowId == 884) {
			return 18;
		} else if (arrowId == 886) {
			return 20;
		} else if (arrowId == 888) {
			return 21;
		} else if (arrowId == 890) {
			return 22;
		} else if (arrowId == 892)
			return 24;
		return 19; // bronze default
	}

	public int getArrowProjectileGfxId(int weaponId, int arrowId) {
		if (arrowId == 884) {
			return 11;
		} else if (arrowId == 886) {
			return 12;
		} else if (arrowId == 888) {
			return 13;
		} else if (arrowId == 890) {
			return 14;
		} else if (arrowId == 892)
			return 15;
		else if (arrowId == 11212)
			return 1120;
		else if (weaponId == 20171)
			return 1066;
		return 10;// bronze default
	}

	public int getKnifeThrowGfxId(int weaponId) {
		// knives TODO ALL
		if (weaponId == 868) {
			return 225;
		} else if (weaponId == 867) {
			return 224;
		} else if (weaponId == 866) {
			return 223;
		} else if (weaponId == 865) {
			return 221;
		} else if (weaponId == 864) {
			return 219;
		} else if (weaponId == 863) {
			return 220;
		}
		// darts
		if (weaponId == 806) {
			return 232;
		} else if (weaponId == 807) {
			return 233;
		} else if (weaponId == 808) {
			return 234;
		} else if (weaponId == 3093) {
			return 273;
		} else if (weaponId == 809) {
			return 235;
		} else if (weaponId == 810) {
			return 236;
		} else if (weaponId == 811) {
			return 237;
		} else if (weaponId == 11230) {
			return 1123;
		}
		// javelins
		if (weaponId >= 13954 && weaponId <= 13956 || weaponId >= 13879
				&& weaponId <= 13882)
			return 1837;
		// thrownaxe
		if (weaponId == 13883 || weaponId == 13957)
			return 1839;
		if (weaponId == 800)
			return 43;
		else if (weaponId == 13883 || weaponId == 13957)
			return 1839;
		else if (weaponId == 13954 || weaponId == 13955 || weaponId == 13956
				|| weaponId == 13879 || weaponId == 13880 || weaponId == 13881
				|| weaponId == 13882)
			return 1837;
		return 219;
	}

	private int getRangeHitDelay(Player player) {
		return Misc.getDistance(player.getX(), player.getY(), target.getX(),
				target.getY()) >= 5 ? 2 : 1;
	}

	private int meleeAttack(final Player player) {
		int weaponId = player.getEquipment().getWeaponId();
		int attackStyle = player.getCombatDefinitions().getAttackStyle();
		int combatDelay = getMeleeCombatDelay(player, weaponId);
		int soundId = getSoundId(weaponId, attackStyle);
		if (player.getCombatDefinitions().isUsingSpecialAttack()) {
			player.getCombatDefinitions().switchUsingSpecialAttack();
			int specAmt = getSpecialAmmount(weaponId);
			if (specAmt == 0) {
				player.getPacketSender()
						.sendGameMessage(
								"This weapon has no special Attack, if you still see special bar please relogin.");
				player.getCombatDefinitions().desecreaseSpecialAttack(0);
				return combatDelay;
			}
			if (player.getCombatDefinitions().hasRingOfVigour())
				specAmt *= 0.9;
			if (player.getCombatDefinitions().getSpecialAttackPercentage() < specAmt) {
				player.getPackets().sendGameMessage(
						"You don't have enough power left.");
				player.getCombatDefinitions().desecreaseSpecialAttack(0);
				return combatDelay;
			}
			player.getCombatDefinitions().desecreaseSpecialAttack(specAmt);
			switch (weaponId) {
			case 4153:// gmaul
				player.setAnimation(new Animation(1667));
				player.setGraphic(new Graphic(340, 0, 96 << 16));
				delayNormalHit(
						weaponId,
						attackStyle,
						getMeleeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										false, true, 1.1, true)));
				break;
			case 15442:// whip start
			case 15443:
			case 15444:
			case 15441:
			case 4151:
				player.setAnimation(new Animation(11971));
				target.setGraphic(new Graphic(2108, 0, 100));
				if (target instanceof Player) {
					Player p2 = (Player) target;
					p2.setRunEnergy(p2.getRunEnergy() > 25 ? p2.getRunEnergy() - 25
							: 0);
				}
				delayNormalHit(
						weaponId,
						attackStyle,
						getMeleeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										false, true, 1.2, true)));
				break;
			case 11730: // sara sword
				player.setAnimation(new Animation(11993));
				target.setGraphic(new Graphic(1194));
				delayNormalHit(
						weaponId,
						attackStyle,
						getMeleeHit(player, 50 + Misc.getRandom(100)),
						getMeleeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										false, true, 1.1, true)));
				soundId = 3853;
				break;
			case 1249:// d spear
			case 1263:
			case 3176:
			case 5716:
			case 5730:
			case 13770:
			case 13772:
			case 13774:
			case 13776:
				player.setAnimation(new Animation(12017));
				player.stopAll();
				target.setGraphic(new Graphic(80, 5, 60));

				if (!target.addWalkSteps(
						target.getX() - player.getX() + target.getX(),
						target.getY() - player.getY() + target.getY(), 1))
					player.setNextFaceEntity(target);
				target.setNextFaceEntity(player);
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						target.setNextFaceEntity(null);
						player.setNextFaceEntity(null);
					}
				});
				if (target instanceof Player) {
					final Player other = (Player) target;
					other.setInfiniteStopDelay();
					other.addFoodDelay(3000);
					other.setDisableEquip(true);
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							other.setDisableEquip(false);
							other.resetStopDelay();
						}
					}, 5);
				} else {
					NPC n = (NPC) target;
					n.setFreezeDelay(3000);
					n.resetCombat();
					n.setRandomWalk(false);
				}
				break;
			case 11698: // sgs
				player.setAnimation(new Animation(7071));
				player.setGraphic(new Graphic(2109));
				int sgsdamage = getRandomMaxHit(player, weaponId, attackStyle,
						false, true, 1.1, true);
				player.heal(sgsdamage / 2);
				player.getPrayer().restorePrayer((sgsdamage / 4) * 10);
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, sgsdamage));
				break;
			case 11696: // bgs
				player.setAnimation(new Animation(11991));
				player.setGraphic(new Graphic(2114));
				int damage = getRandomMaxHit(player, weaponId, attackStyle,
						false, true, 1.2, true);
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, damage));
				if (target instanceof Player) {
					Player targetPlayer = ((Player) target);
					int amountLeft;
					if ((amountLeft = targetPlayer.getSkills().drainLevel(
							Skills.DEFENCE, damage / 10)) > 0) {
						if ((amountLeft = targetPlayer.getSkills().drainLevel(
								Skills.STRENGTH, amountLeft)) > 0) {
							if ((amountLeft = targetPlayer.getSkills()
									.drainLevel(Skills.PRAYER, amountLeft)) > 0) {
								if ((amountLeft = targetPlayer.getSkills()
										.drainLevel(Skills.ATTACK, amountLeft)) > 0) {
									if ((amountLeft = targetPlayer.getSkills()
											.drainLevel(Skills.MAGIC,
													amountLeft)) > 0) {
										if (targetPlayer.getSkills()
												.drainLevel(Skills.RANGE,
														amountLeft) > 0) {
											break;
										}
									}
								}
							}
						}
					}
				}
				break;
			case 11694: // ags
				player.setAnimation(new Animation(11989));
				player.setGraphic(new Graphic(2113));
				delayNormalHit(
						weaponId,
						attackStyle,
						getMeleeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										false, true, 1.50, true)));
				break;
			case 13899: // vls
			case 13901:
				player.setAnimation(new Animation(10502));
				delayNormalHit(
						weaponId,
						attackStyle,
						getMeleeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										false, true, 1.20, true)));
				break;
			case 13902: // statius hammer
			case 13904:
				player.setAnimation(new Animation(10505));
				player.setGraphic(new Graphic(1840));
				delayNormalHit(
						weaponId,
						attackStyle,
						getMeleeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										false, true, 1.25, true)));
				break;
			case 13905: // vesta spear
			case 13907:
				player.setAnimation(new Animation(10499));
				player.setGraphic(new Graphic(1835));
				delayNormalHit(
						weaponId,
						attackStyle,
						getMeleeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										false, true, 1.1, true)));
				break;
			case 19784: // korasi sword
				player.setAnimation(new Animation(14788));
				player.setGraphic(new Graphic(1729));
				int korasiDamage = getMaxHit(player, weaponId, attackStyle,
						false, true, 1);
				double multiplier = 0.5;
				multiplier += Math.random();
				korasiDamage *= multiplier;
				delayNormalHit(weaponId, attackStyle,
						getMagicHit(player, korasiDamage));
				break;
			case 11700:
				int zgsdamage = getRandomMaxHit(player, weaponId, attackStyle,
						false, true, 1.0, true);
				player.setAnimation(new Animation(7070));
				player.setGraphic(new Graphic(1221));
				if (zgsdamage != 0 && target.getSize() <= 1) { // freezes small
					// npcs
					target.setGraphic(new Graphic(2104));
					target.addFreezeDelay(18000); // 18seconds
				}
				delayNormalHit(weaponId, attackStyle,
						getMeleeHit(player, zgsdamage));
				break;
			case 14484: // d claws
				player.setAnimation(new Animation(10961));
				player.setGraphic(new Graphic(1950));
				int hit1 = getRandomMaxHit(player, weaponId, attackStyle,
						false, true, 1.0, true);
				int hit2 = hit1 == 0 ? getRandomMaxHit(player, weaponId,
						attackStyle, false, true, 1.0, true) : hit1;
				if (hit1 == 0 && hit2 == 0) {
					int hit3 = getRandomMaxHit(player, weaponId, attackStyle,
							false, true, 1.0, true);
					if (hit3 == 0) {
						int hit4 = getRandomMaxHit(player, weaponId,
								attackStyle, false, true, 1.0, true);
						if (hit4 == 0) {
							delayNormalHit(weaponId, attackStyle,
									getMeleeHit(player, hit1),
									getMeleeHit(player, hit2));
							delayHit(1, weaponId, attackStyle,
									getMeleeHit(player, hit3),
									getMeleeHit(player, 1));
						} else {
							delayNormalHit(weaponId, attackStyle,
									getMeleeHit(player, hit1),
									getMeleeHit(player, hit2));
							delayHit(1, weaponId, attackStyle,
									getMeleeHit(player, hit3),
									getMeleeHit(player, (int) (hit4 * 1.5)));
						}
					} else {
						delayNormalHit(weaponId, attackStyle,
								getMeleeHit(player, hit1),
								getMeleeHit(player, hit2));
						delayHit(1, weaponId, attackStyle,
								getMeleeHit(player, hit3),
								getMeleeHit(player, hit3));
					}
				} else {
					delayNormalHit(weaponId, attackStyle,
							getMeleeHit(player, hit1),
							getMeleeHit(player, hit1 == 0 ? hit2 : hit2 / 2));
					delayHit(
							1,
							weaponId,
							attackStyle,
							getMeleeHit(player, hit1 == 0 ? hit2 / 2 : hit2 / 4),
							getMeleeHit(player, hit2 / 4));
				}
				break;
			case 10887: // anchor
				player.setAnimation(new Animation(5870));
				player.setGraphic(new Graphic(1027));
				delayNormalHit(
						weaponId,
						attackStyle,
						getMeleeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										false, false, 1.0, true)));
				break;
			case 1305: // dragon long
				player.setAnimation(new Animation(12033));
				player.setGraphic(new Graphic(2117));
				delayNormalHit(
						weaponId,
						attackStyle,
						getMeleeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										false, true, 1.25, true)));
				break;
			case 3204: // d hally
				player.setAnimation(new Animation(1665));
				player.setGraphic(new Graphic(282));
				if (target.getSize() < 3) {// giant npcs wont get stuned cuz of
					// a stupid hit
					target.setGraphic(new Graphic(254, 0, 100));
					target.setGraphic(new Graphic(80));
				}
				delayNormalHit(
						weaponId,
						attackStyle,
						getMeleeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										false, true, 1.1, true)));
				if (target.getSize() > 1)
					delayHit(
							1,
							weaponId,
							attackStyle,
							getMeleeHit(
									player,
									getRandomMaxHit(player, weaponId,
											attackStyle, false, true, 1.1, true)));
				break;
			case 4587: // dragon sci
				player.setAnimation(new Animation(12031));
				player.setGraphic(new Graphic(2118));
				if (target instanceof Player) {
					Player p2 = (Player) target;
					p2.setPrayerDelay(5000);// 5 seconds
				}
				delayNormalHit(
						weaponId,
						attackStyle,
						getMeleeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										false, true, 1.0, true)));
				soundId = 2540;
				break;
			case 1215: // dragon dagger
			case 5698: // dds
				player.setAnimation(new Animation(1062));
				player.setGraphic(new Graphic(252, 0, 100));
				delayNormalHit(
						weaponId,
						attackStyle,
						getMeleeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										false, true, 1.15, true)),
						getMeleeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										false, true, 1.15, true)));
				soundId = 2537;
				break;
			case 1434: // dragon mace
				player.setAnimation(new Animation(1060));
				player.setGraphic(new Graphic(251));
				delayNormalHit(
						weaponId,
						attackStyle,
						getMeleeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										false, true, 1.45, true)));
				soundId = 2541;
				break;
			default:
				player.getPackets()
						.sendGameMessage(
								"This weapon has no special Attack, if you still see special bar please relogin.");
				return combatDelay;
			}
		} else {
			delayNormalHit(
					weaponId,
					attackStyle,
					getMeleeHit(
							player,
							getRandomMaxHit(player, weaponId, attackStyle,
									false)));
			player.setAnimation(new Animation(getWeaponAttackEmote(weaponId,
					attackStyle)));
		}
		playSound(soundId, player, target);
		return combatDelay;
	}

	public void playSound(int soundId, Player player, Entity target) {
		if (soundId == -1)
			return;
		player.getPacketSender().sendSound(soundId, 0, 1);
		if (target instanceof Player) {
			Player p2 = (Player) target;
			p2.getPackets().sendSound(soundId, 0, 1);
		}
	}

	public static int getSpecialAmmount(int weaponId) {
		switch (weaponId) {
		case 4587: // dragon sci
		case 859: // magic longbow
		case 861: // magic shortbow
		case 10284: // Magic composite bow
		case 18332: // Magic longbow (sighted)
		case 19149:// zamorak bow
		case 19151:
		case 19143:// saradomin bow
		case 19145:
		case 19146:
		case 19148:// guthix bow
			return 55;
		case 11235: // dark bows
		case 15701:
		case 15702:
		case 15703:
		case 15704:
			return 65;
		case 13899: // vls
		case 13901:
		case 1305: // dragon long
		case 1215: // dragon dagger
		case 5698: // dds
		case 1434: // dragon mace
		case 1249:// d spear
		case 1263:
		case 3176:
		case 5716:
		case 5730:
		case 13770:
		case 13772:
		case 13774:
		case 13776:
			return 25;
		case 15442:// whip start
		case 15443:
		case 15444:
		case 15441:
		case 4151:
		case 11698: // sgs
		case 11694: // ags
		case 13902: // statius hammer
		case 13904:
		case 13905: // vesta spear
		case 13907:
		case 14484: // d claws
		case 10887: // anchor
		case 3204: // d hally
		case 4153: // granite maul
		case 14684: // zanik cbow
		case 15241: // hand cannon
		case 13908:
		case 13954:// morrigan javelin
		case 13955:
		case 13956:
		case 13879:
		case 13880:
		case 13881:
		case 13882:
		case 13883:// morigan thrown axe
		case 13957:
			return 50;
		case 11730: // ss
		case 11696: // bgs
		case 11700: // zgs
		case 35:// Excalibur
		case 8280:
		case 14632:
		case 1377:// dragon battle axe
		case 13472:
		case 15486:// staff of lights
		case 22207:
		case 22209:
		case 22211:
		case 22213:
			return 100;
		case 19784: // korasi sword
			return 60;
		default:
			return 0;
		}
	}

	public int getRandomMaxHit(Player player, int weaponId, int attackStyle,
			boolean ranging) {
		return getRandomMaxHit(player, weaponId, attackStyle, ranging, true,
				1.0D, false);
	}

	public int getRandomMaxHit(Player player, int weaponId, int attackStyle,
			boolean ranging, boolean defenceAffects, double specMultiplier,
			boolean usingSpec) {
		max_hit = getMaxHit(player, weaponId, attackStyle, ranging, usingSpec,
				specMultiplier);
		if (defenceAffects) {
			double att = player.getSkills().getLevel(ranging ? 4 : 0)
					+ player.getCombatDefinitions().getBonuses()[ranging ? 4
							: CombatDefinitions.getMeleeBonusStyle(weaponId,
									attackStyle)];
			att *= ranging ? player.getPrayer().getRangeMultiplier() : player
					.getPrayer().getAttackMultiplier();
			if (fullVoidEquipped(player, ranging ? (new int[] { 11664, 11675 })
					: (new int[] { 11665, 11676 })))
				att *= 1.1;
			if (ranging)
				att *= player.getAuraManager().getRangeAccurayMultiplier();
			double def = 0;
			if (target instanceof Player) {
				Player p2 = (Player) target;
				def = (double) p2.getSkills().getLevel(Skills.DEFENCE)
						+ (2 * p2.getCombatDefinitions().getBonuses()[ranging ? 9
								: CombatDefinitions
										.getMeleeDefenceBonus(CombatDefinitions
												.getMeleeBonusStyle(weaponId,
														attackStyle))]);

				def *= p2.getPrayer().getDefenceMultiplier();
				if (!ranging) {
					if (p2.getFamiliar() instanceof Steeltitan)
						def *= 1.15;
				}
			} else {
				NPC n = (NPC) target;
				def = n.getBonuses() != null ? n.getBonuses()[ranging ? 9
						: CombatDefinitions
								.getMeleeDefenceBonus(CombatDefinitions
										.getMeleeBonusStyle(weaponId,
												attackStyle))] : 0;
			}
			if (usingSpec) {
				double multiplier =  0.25 + specMultiplier;
				att *= multiplier;
			}
			double prob = att / def;
			if (prob > 0.90) // max, 90% prob hit so even lvl 138 can miss at
				// lvl 3
				prob = 0.90;
			else if (prob < 0.05) // minimun 5% so even lvl 3 can hit lvl 138
				prob = 0.05;
			if (prob < Math.random())
				return 0;
		}
		int hit = Misc.getRandom(max_hit);
		if (target instanceof NPC) {
			NPC n = (NPC) target;
			if (n.getId() == 9463 && hasFireCape(player))
				hit += 40;
		}
		if (player.getAuraManager().usingEquilibrium()) {
			int perc25MaxHit = (int) (max_hit * 0.25);
			hit -= perc25MaxHit;
			max_hit -= perc25MaxHit;
			if (hit < 0)
				hit = 0;
			if (hit < perc25MaxHit)
				hit += perc25MaxHit;

		}
		return hit;
	}

	
	 * public int getRandomMaxHit(Player player, int weaponId, int attackStyle,
	 * boolean ranging, boolean defenceAffects, double specMultiplier, boolean
	 * usingSpec) { max_hit = getMaxHit(player, weaponId, attackStyle, ranging,
	 * usingSpec, specMultiplier); if (defenceAffects) { double EA =
	 * player.getSkills().getLevel( ranging ? Skills.RANGE : Skills.ATTACK); EA
	 * *= ranging ? player.getPrayer().getRangeMultiplier() : player
	 * .getPrayer().getAttackMultiplier(); EA += 8; int style =
	 * CombatDefinitions.getXpStyle(weaponId, player
	 * .getCombatDefinitions().getAttackStyle()); EA += style == Skills.ATTACK ?
	 * 3 : style == CombatDefinitions.SHARED ? 1 : 0; if
	 * (fullVoidEquipped(player, ranging ? new int[] { 11664, 11675 } : new
	 * int[] { 11665, 11676 })) EA *= 1.1; if (ranging) EA *=
	 * player.getAuraManager().getRangeAccurayMultiplier(); Math.floor(EA);
	 * double ED; double defBonus = 0; if (target instanceof Player) { Player p2
	 * = (Player) target; ED = p2.getSkills().getLevel(Skills.DEFENCE); ED *=
	 * p2.getPrayer().getDefenceMultiplier(); ED += 8; int p2style =
	 * CombatDefinitions.getXpStyle(p2.getEquipment() .getWeaponId(),
	 * p2.getCombatDefinitions() .getAttackStyle()); ED += p2style ==
	 * Skills.DEFENCE ? 3 : p2style == CombatDefinitions.SHARED ? 1 : 0;
	 * defBonus = p2.getCombatDefinitions().getBonuses()[ranging ?
	 * CombatDefinitions.RANGE_DEF : CombatDefinitions
	 * .getMeleeDefenceBonus(CombatDefinitions .getMeleeBonusStyle(weaponId,
	 * attackStyle))]; if (!ranging) if (p2.getFamiliar() instanceof Steeltitan)
	 * ED *= 1.15; if (player.getPolDelay() >= Misc.currentTimeMillis()) ED *=
	 * 1.5D; Math.floor(ED); } else { NPC n = (NPC) target; ED = n.getBonuses()
	 * == null ? 0 : n.getBonuses()[ranging ? CombatDefinitions.RANGE_DEF :
	 * CombatDefinitions .getMeleeDefenceBonus(CombatDefinitions
	 * .getMeleeBonusStyle(weaponId, attackStyle))]; } double A = EA (1 +
	 * (player.getCombatDefinitions().getBonuses()[ranging ?
	 * CombatDefinitions.RANGE_ATTACK :
	 * CombatDefinitions.getMeleeBonusStyle(weaponId, attackStyle)] / 64.0D) *
	 * 10.0D); A *= getSpecialAccuracyModifier(player); double D = ED * (1 +
	 * (defBonus / 64.0D) * 10.0D); Math.floor(A); Math.floor(D); double
	 * accuracy = 0.5; if (A < D) accuracy = (A - 1) / (2 * D); else if (A > D)
	 * accuracy = 1 - (D + 1) / (2 * A); accuracy *= 100;
	 * System.out.println(accuracy); if (accuracy <= Math.random() * 100) return
	 * 0; } int hit = Misc.getRandom(max_hit); if (target instanceof NPC) { NPC
	 * n = (NPC) target; if (n.getId() == 9463 && hasFireCape(player)) max_hit
	 * += 40; } if (player.getAuraManager().usingEquilibrium()) { int
	 * perc25MaxHit = (int) (max_hit * 0.25); hit -= perc25MaxHit; max_hit -=
	 * perc25MaxHit; if (hit < 0) hit = 0; if (hit < perc25MaxHit) hit +=
	 * perc25MaxHit;
	 * 
	 * } return hit; }
	 
	
	 * private double getSpecialAccuracyModifier(Player player) { Item weapon =
	 * player.getEquipment().getItem(Constants.SLOT_WEAPON); if (weapon == null)
	 * return 1; String name = weapon.getDefinitions().getName(); if
	 * (name.contains("whip") || name.contains("dragon dagger") ||
	 * name.contains("dragon longsword") || name.contains("dragon scimitar") ||
	 * name.contains("magic shortbow")) return 1.1; if (name.contains("anchor")
	 * || name.contains("magic longbow")) return 2; return 1; }
	 

	public boolean hasFireCape(Player player) {
		int capeId = player.getEquipment().getCapeId();
		return capeId == 6570 || capeId == 20769 || capeId == 20771;
	}

	public static final int getMaxHit(Player player, int weaponId,
			int attackStyle, boolean ranging, boolean usingSpec,
			double specMultiplier) {
		if (!ranging) {
			int strengthLvl = player.getSkills().getLevel(Skills.STRENGTH);
			double xpStyle = CombatDefinitions
					.getXpStyle(weaponId, attackStyle);
			double styleBonus = xpStyle == Skills.STRENGTH ? 3
					: xpStyle == CombatDefinitions.SHARED ? 1 : 0;
			double otherBonus = 1;
			if (fullDharokEquipped(player)) {
				double hp = player.getHitpoints();
				double maxhp = player.getMaxHitpoints();
				double d = hp / maxhp;
				otherBonus = 2 - d;
			}
			double effectiveStrength = 8 + strengthLvl
					* player.getPrayer().getStrengthMultiplier() + styleBonus;
			if (fullVoidEquipped(player, 11665, 11676))
				effectiveStrength *= 1.1;
			double strengthBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions.STRENGTH_BONUS];
			double baseDamage = 5 + effectiveStrength
					* (1 + (strengthBonus / 64));
			return (int) (baseDamage * specMultiplier * otherBonus);
		} else {
			double rangedLvl = player.getSkills().getLevel(Skills.RANGE);
			double styleBonus = attackStyle == 0 ? 3 : attackStyle == 1 ? 0 : 1;
			double otherBonus = 1;
			double effectiveStrenght = (rangedLvl
					* player.getPrayer().getRangeMultiplier() * otherBonus)
					+ styleBonus;
			if (fullVoidEquipped(player, 11664, 11675))
				effectiveStrenght += (player.getSkills().getLevel(
						Skills.RANGE) / 5) + 1.6;
			double strengthBonus = player.getCombatDefinitions().getBonuses()[CombatDefinitions.RANGED_STR_BONUS];
			double baseDamage = 5 + (((effectiveStrenght + 8) * (strengthBonus + 64)) / 64);
			return (int) (baseDamage * specMultiplier);
		}
	}

	public static final boolean fullVanguardEquipped(Player player) {
		int helmId = player.getEquipment().getHatId();
		int chestId = player.getEquipment().getChestId();
		int legsId = player.getEquipment().getLegsId();
		int weaponId = player.getEquipment().getWeaponId();
		int bootsId = player.getEquipment().getBootsId();
		int glovesId = player.getEquipment().getGlovesId();
		if (helmId == -1 || chestId == -1 || legsId == -1 || weaponId == -1
				|| bootsId == -1 || glovesId == -1)
			return false;
		return ItemDefinition.forId(helmId).getName().contains("Vanguard")
				&& ItemDefinition.forId(chestId).getName().contains("Vanguard")
				&& ItemDefinition.forId(legsId).getName().contains("Vanguard")
				&& ItemDefinition.forId(weaponId).getName()
						.contains("Vanguard")
				&& ItemDefinition.forId(bootsId).getName().contains("Vanguard")
				&& ItemDefinition.forId(glovesId).getName()
						.contains("Vanguard");
	}

	public static final boolean usingGoliathGloves(Player player) {
		String name = player.getEquipment().getItem(Constants.SLOT_SHIELD) != null ? player
				.getEquipment().getItem(Constants.SLOT_SHIELD).getDefinitions()
				.getName().toLowerCase()
				: "";
		if (player.getEquipment().getItem((Constants.SLOT_HANDS)) != null) {
			if (player.getEquipment().getItem(Constants.SLOT_HANDS)
					.getDefinitions().getName().toLowerCase()
					.contains("goliath")
					&& player.getEquipment().getWeaponId() == -1) {
				if (name.contains("defender")
						&& name.contains("dragonfire shield"))
					return true;
				return true;
			}
		}
		return false;
	}

	public static final boolean fullDharokEquipped(Player player) {
		int helmId = player.getEquipment().getHatId();
		int chestId = player.getEquipment().getChestId();
		int legsId = player.getEquipment().getLegsId();
		int weaponId = player.getEquipment().getWeaponId();
		if (helmId == -1 || chestId == -1 || legsId == -1 || weaponId == -1)
			return false;
		return ItemDefinition.forId(helmId).getName().contains("Dharok's")
				&& ItemDefinition.forId(chestId).getName().contains("Dharok's")
				&& ItemDefinition.forId(legsId).getName().contains("Dharok's")
				&& ItemDefinition.forId(weaponId).getName()
						.contains("Dharok's");
	}

	public static final boolean fullVoidEquipped(Player player, int... helmid) {
		boolean hasDeflector = player.getEquipment().getShieldId() == 19712;
		if (player.getEquipment().getGlovesId() != 8842) {
			if (hasDeflector)
				hasDeflector = false;
			else
				return false;
		}
		int legsId = player.getEquipment().getLegsId();
		boolean hasLegs = legsId != -1
				&& (legsId == 8840 || legsId == 19786 || legsId == 19788 || legsId == 19790);
		if (!hasLegs) {
			if (hasDeflector)
				hasDeflector = false;
			else
				return false;
		}
		int torsoId = player.getEquipment().getChestId();
		boolean hasTorso = torsoId != -1
				&& (torsoId == 8839 || torsoId == 10611 || torsoId == 19785
						|| torsoId == 19787 || torsoId == 19789);
		if (!hasTorso) {
			if (hasDeflector)
				hasDeflector = false;
			else
				return false;
		}
		if (hasDeflector)
			return true;
		int helmId = player.getEquipment().getHatId();
		if (helmId == -1)
			return false;
		boolean hasHelm = false;
		for (int id : helmid) {
			if (helmId == id) {
				hasHelm = true;
				break;
			}
		}
		if (!hasHelm)
			return false;
		return true;
	}

	public void delayNormalHit(int weaponId, int attackStyle, Hit... hits) {
		delayHit(0, weaponId, attackStyle, hits);
	}

	public Hit getMeleeHit(Player player, int damage) {
		return new Hit(player, damage, HitLook.MELEE_DAMAGE);
	}

	public Hit getRangeHit(Player player, int damage) {
		return new Hit(player, damage, HitLook.RANGE_DAMAGE);
	}

	public Hit getMagicHit(Player player, int damage) {
		return new Hit(player, damage, HitLook.MAGIC_DAMAGE);
	}

	private void delayMagicHit(int delay, final Hit... hits) {
		delayHit(delay, -1, -1, hits);
	}

	private void delayHit(int delay, final int weaponId, final int attackStyle,
			final Hit... hits) {
		addAttackedByDelay(hits[0].getSource());
		final Entity target = this.target;
		for (Hit hit : hits) {
			Player player = (Player) hit.getSource();
			int damage = hit.getDamage() > target.getHitpoints() ? target
					.getHitpoints() : hit.getDamage();
			if (hit.getLook() == HitLook.RANGE_DAMAGE
					|| hit.getLook() == HitLook.MELEE_DAMAGE) {
				double combatXp = damage / 2.5;
				if (combatXp > 0) {
					player.getAuraManager().checkSuccefulHits(hit.getDamage());
					if (hit.getLook() == HitLook.RANGE_DAMAGE) {
						if (attackStyle == 2) {
							player.getSkills().addXp(Skills.RANGE,
									(combatXp / 2) * Settings.COMBAT_XP_RATE);
							player.getSkills().addXp(Skills.DEFENCE,
									(combatXp / 2) * Settings.COMBAT_XP_RATE);
						} else
							player.getSkills().addXp(Skills.RANGE,
									combatXp * Settings.COMBAT_XP_RATE);

					} else {
						int xpStyle = CombatDefinitions.getXpStyle(weaponId,
								attackStyle);
						if (xpStyle != CombatDefinitions.SHARED)
							player.getSkills().addXp(xpStyle,
									combatXp * Settings.COMBAT_XP_RATE);
						else {
							player.getSkills().addXp(Skills.ATTACK,
									(combatXp / 3) * Settings.COMBAT_XP_RATE);
							player.getSkills().addXp(Skills.STRENGTH,
									(combatXp / 3) * Settings.COMBAT_XP_RATE);
							player.getSkills().addXp(Skills.DEFENCE,
									(combatXp / 3) * Settings.COMBAT_XP_RATE);
						}
					}
					double hpXp = damage / 7.5;
					if (hpXp > 0)
						player.getSkills().addXp(Skills.HITPOINTS, hpXp);
				}
			} else if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
				if (mage_hit_gfx != 0 && damage > 0) {
					if (freeze_time > 0) {
						target.addFreezeDelay(freeze_time, freeze_time == 0);
						if (freeze_time > 0)
							target.addFrozenBlockedDelay(freeze_time
									+ (4 * 1000));// foure seconds :p
					}
				}
				double combatXp = base_mage_xp * 1 + (damage / 5);

				if (combatXp > 0) {
					player.getAuraManager().checkSuccefulHits(hit.getDamage());
					if (player.getCombatDefinitions().isDefensiveCasting()) {
						int defenceXp = (int) (damage / 7.5);
						if (defenceXp > 0) {
							combatXp -= defenceXp;
							player.getSkills()
									.addXp(Skills.DEFENCE,
											(defenceXp / 7.5)
													* Settings.COMBAT_XP_RATE);
						}
					}
					player.getSkills().addXp(Skills.MAGIC,
							combatXp * Settings.COMBAT_XP_RATE);
					double hpXp = damage / 7.5;
					if (hpXp > 0)
						player.getSkills().addXp(Skills.HITPOINTS,
								hpXp * Settings.COMBAT_XP_RATE);
				}
			}
		}

		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				for (Hit hit : hits) {
					Player player = (Player) hit.getSource();
					if (player.isDead() || player.hasFinished()
							|| target.isDead() || target.hasFinished())
						return;
					target.applyHit(hit); // also reduces damage if needed, pray
					// and special items affect here
					doDefenceEmote();
					int damage = hit.getDamage() > target.getHitpoints() ? target
							.getHitpoints() : hit.getDamage();
					if ((damage >= max_hit * 0.90)
							&& (hit.getLook() == HitLook.MAGIC_DAMAGE
									|| hit.getLook() == HitLook.RANGE_DAMAGE || hit
									.getLook() == HitLook.MELEE_DAMAGE))
						hit.setCriticalMark();
					if (hit.getLook() == HitLook.RANGE_DAMAGE
							|| hit.getLook() == HitLook.MELEE_DAMAGE) {
						double combatXp = damage / 2.5;
						if (combatXp > 0) {
							if (hit.getLook() == HitLook.RANGE_DAMAGE) {
								if (weaponId != -1) {
									String name = ItemDefinition
											.forId(weaponId)
											.getName();
									if (name.contains("(p++)")) {
										if (Misc.getRandom(8) == 0)
											target.getPoison().makePoisoned(48);
									} else if (name.contains("(p+)")) {
										if (Misc.getRandom(8) == 0)
											target.getPoison().makePoisoned(38);
									} else if (name.contains("(p)")) {
										if (Misc.getRandom(8) == 0)
											target.getPoison().makePoisoned(28);
									}
								}
							} else {
								if (weaponId != -1) {
									String name = ItemDefinition
											.forId(weaponId)
											.getName();
									if (name.contains("(p++)")) {
										if (Misc.getRandom(8) == 0)
											target.getPoison().makePoisoned(68);
									} else if (name.contains("(p+)")) {
										if (Misc.getRandom(8) == 0)
											target.getPoison().makePoisoned(58);
									} else if (name.contains("(p)")) {
										if (Misc.getRandom(8) == 0)
											target.getPoison().makePoisoned(48);
									}
									if (target instanceof Player) {
										if (((Player) target).getPolDelay() >= Misc
												.currentTimeMillis())
											target.setGraphic(new Graphic(2320));
									}
								}
							}
						}
					} else if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
						if (hit.getDamage() == 0) {
							target.setGraphic(new Graphic(85, 0, 100));
							playSound(227, player, target);
						} else {
							if (mage_hit_gfx != 0) {
								target.setGraphic(new Graphic(mage_hit_gfx, 0,
										mage_hit_gfx == 369
												|| mage_hit_gfx == 1843 ? 0
												: 100));
								if (blood_spell)
									player.heal(damage / 4);
								if (block_tele) {
									if (target instanceof Player) {
										Player targetPlayer = (Player) target;
										targetPlayer
												.setTeleBlockDelay((targetPlayer
														.getPrayer()
														.usingPrayer(0, 17)
														|| targetPlayer
																.getPrayer()
																.usingPrayer(1,
																		7) ? 100000
														: 300000));
										targetPlayer
												.getPackets()
												.sendGameMessage(
														"You have been teleblocked.",
														true);
									}
								}
							}
							if (magic_sound > 0)
								playSound(magic_sound, player, target);
						}
						mage_hit_gfx = 0;
						blood_spell = false;
						freeze_time = 0;
						magic_sound = 0;
						block_tele = false;
					}
					if (max_poison_hit > 0 && Misc.getRandom(10) == 0) {
						if (!target.getPoison().isPoisoned())
							target.getPoison().makePoisoned(max_poison_hit);
						max_poison_hit = 0;
					}
					if (target instanceof Player) {
						Player p2 = (Player) target;
						p2.closeInterfaces();
						if (p2.getCombatDefinitions().isAutoRelatie()
								&& !p2.getActionManager().hasSkillWorking()
								&& !p2.hasWalkSteps()) {
							p2.getActionManager().setSkill(
									new PlayerCombat(player));
						}
					} else {
						NPC n = (NPC) target;
						if (!n.isUnderCombat()
								|| n.canBeAttackedByAutoRelatie())
							n.setTarget(player);
					}

				}
			}
		}, delay);
	}

	private int getSoundId(int weaponId, int attackStyle) {
		if (weaponId != -1) {
			String weaponName = ItemDefinition.forId(weaponId).getName()
					.toLowerCase();
			if (weaponName.contains("dart") || weaponName.contains("knife"))
				return 2707;
		}
		return -1;
	}

	public static int getWeaponAttackEmote(int weaponId, int attackStyle) {
		if (weaponId != -1) {
			String weaponName = ItemDefinition.forId(weaponId).getName()
					.toLowerCase();
			if (weaponName != null && !weaponName.equals("null")) {
				if (weaponName.contains("crossbow"))
					return weaponName.contains("karil's crossbow") ? 2075
							: 4230;
				if (weaponName.contains("bow"))
					return 426;
				if (weaponName.contains("staff of light")) {
					switch (attackStyle) {
					case 0:
						return 15072;
					case 1:
						return 15071;
					case 2:
						return 414;
					}
				}
				if (weaponName.contains("staff") || weaponName.contains("wand"))
					return 419;
				if (weaponName.contains("dart"))
					return 6600;
				if (weaponName.contains("knife"))
					return 9055;
				if (weaponName.contains("scimitar")
						|| weaponName.contains("korasi's sword")) {
					switch (attackStyle) {
					case 2:
						return 15072;
					default:
						return 15071;
					}
				}
				if (weaponName.contains("granite mace"))
					return 400;
				if (weaponName.contains("mace")) {
					switch (attackStyle) {
					case 2:
						return 400;
					default:
						return 401;
					}
				}
				if (weaponName.contains("hatchet")) {
					switch (attackStyle) {
					case 2:
						return 401;
					default:
						return 395;
					}
				}
				if (weaponName.contains("warhammer")) {
					switch (attackStyle) {
					default:
						return 401;
					}
				}
				if (weaponName.contains("claws")) {
					switch (attackStyle) {
					case 2:
						return 1067;
					default:
						return 393;
					}
				}
				if (weaponName.contains("whip")) {
					switch (attackStyle) {
					case 1:
						return 11969;
					case 2:
						return 11970;
					default:
						return 11968;
					}
				}
				if (weaponName.contains("anchor")) {
					switch (attackStyle) {
					default:
						return 5865;
					}
				}
				if (weaponName.contains("tzhaar-ket-em")) {
					switch (attackStyle) {
					default:
						return 401;
					}
				}
				if (weaponName.contains("tzhaar-ket-om")) {
					switch (attackStyle) {
					default:
						return 13691;
					}
				}
				if (weaponName.contains("halberd")) {
					switch (attackStyle) {
					case 1:
						return 440;
					default:
						return 428;
					}
				}
				if (weaponName.contains("zamorakian spear")) {
					switch (attackStyle) {
					case 1:
						return 12005;
					case 2:
						return 12009;
					default:
						return 12006;
					}
				}
				if (weaponName.contains("spear")) {
					switch (attackStyle) {
					case 1:
						return 440;
					case 2:
						return 429;
					default:
						return 428;
					}
				}
				if (weaponName.contains("flail")) {
					return 2062;
				}
				if (weaponName.contains("javelin")) {
					return 10501;
				}
				if (weaponName.contains("morrigan's throwing axe"))
					return 10504;
				if (weaponName.contains("pickaxe")) {
					switch (attackStyle) {
					case 2:
						return 400;
					default:
						return 401;
					}
				}
				if (weaponName.contains("dagger")) {
					switch (attackStyle) {
					case 2:
						return 377;
					default:
						return 376;
					}
				}
				if (weaponName.contains("longsword")
						|| weaponName.contains("light")
						|| weaponName.contains("excalibur")) {
					switch (attackStyle) {
					case 2:
						return 12310;
					default:
						return 12311;
					}
				}
				if (weaponName.contains("rapier")
						|| weaponName.contains("brackish")) {
					switch (attackStyle) {
					case 2:
						return 13048;
					default:
						return 13049;
					}
				}

				if (weaponName.contains("godsword")) {
					switch (attackStyle) {
					case 2:
						return 11980;
					case 3:
						return 11981;
					default:
						return 11979;
					}
				}
				if (weaponName.contains("greataxe")) {
					switch (attackStyle) {
					case 2:
						return 12003;
					default:
						return 12002;
					}
				}
				if (weaponName.contains("granite maul")) {
					switch (attackStyle) {
					default:
						return 1665;
					}
				}
				if (weaponName.contains("2h sword")
						|| weaponName.equals("saradomin sword")) {
					switch (attackStyle) {
					case 2:
						return 7048;
					case 3:
						return 7049;
					default:
						return 7041;
					}
				}

			}
		}
		switch (weaponId) {
		case 16405:// novite maul
		case 16407:// Bathus maul
		case 16409:// Maramaros maul
		case 16411:// Kratonite maul
		case 16413:// Fractite maul
		case 18353:// chaotic maul
		case 16415:// Zephyrium maul
		case 16417:// Argonite maul
		case 16419:// Katagon maul
		case 16421:// Gorgonite maul
		case 16423:// Promethium maul
		case 16425:// primal maul
			return 2661; // maul
		case 13883: // morrigan thrown axe
			return 10504;
		case 15241:
			return 12174;
		default:
			switch (attackStyle) {
			case 1:
				return 423;
			default:
				return 422; // todo default emote
			}
		}
	}

	private void doDefenceEmote() {
		target.setAnimationNoPriority(new Animation(Combat
				.getDefenceEmote(target)));
	}

	private int getMeleeCombatDelay(Player player, int weaponId) {
		if (weaponId != -1) {
			String weaponName = ItemDefinition.forId(weaponId).getName()
					.toLowerCase();
			if (weaponName.contains("zamorakian spear"))
				return 3;
			if (weaponName.contains("spear")
					|| weaponName.contains("longsword")
					|| weaponName.contains("light")
					|| weaponName.contains("hatchet")
					|| weaponName.contains("pickaxe")
					|| weaponName.contains("mace")
					|| weaponName.contains("hasta")
					|| weaponName.contains("warspear")
					|| weaponName.contains("flail")
					|| weaponName.contains("hammers"))
				return 4;
			// Interval 3.6
			if (weaponName.contains("godsword")
					|| weaponName.contains("warhammer")
					|| weaponName.contains("battleaxe")
					|| weaponName.contains("maul"))
				return 5;
			// Interval 4.2
			if (weaponName.contains("greataxe")
					|| weaponName.contains("halberd")
					|| weaponName.contains("2h sword")
					|| weaponName.contains("two handed sword"))
				return 6;
		}
		switch (weaponId) {
		case 6527:// tzhaar-ket-em
			return 4;
		case 10887:// barrelchest anchor
			return 5;
		case 15403:// balmung
		case 6528:// tzhaar-ket-om
			return 6;
		default:
			return 3;
		}
	}

	@Override
	public void stop(Player player) {
		player.turnTo(null);
	}

	private boolean checkAll(Player player) {
		if (player.isDead() || target.isDead())
			return false;
		int distanceX = player.getX() - target.getX();
		int distanceY = player.getY() - target.getY();
		int size = target.getSize();
		int maxDistance = 16;
		if (player.getZ() != target.getZ() || distanceX > size + maxDistance
				|| distanceX < -1 - maxDistance
				|| distanceY > size + maxDistance
				|| distanceY < -1 - maxDistance)
			return false;
		if (player.getFreezeDelay() >= System.currentTimeMillis())
			return true;
		if (target instanceof Player) {
			Player p2 = (Player) target;
			if (!player.isCanPvp() || !p2.isCanPvp())
				return false;
		} else {
			NPC n = (NPC) target;
			if (n.isCantInteract())
				return false;
			if (n instanceof Familiar) {
				Familiar familiar = (Familiar) n;
				if (!familiar.canAttack(target))
					return false;
			} else {
				if (!n.canBeAttackFromOutOfArea()
						&& !MapAreas.isAtArea(n.getMapAreaNameHash(), player))
					return false;
				String slayerMessage = "You need a slayer level of X to attack "
						+ n.getDefinitions().name.toLowerCase() + ".";
				if (n.getId() == 14578) {
					if (player.getEquipment().getWeaponId() != 2402) {
						player.getPackets().sendGameMessage(
								"I'd better wield Silverlight first.");
						return false;
					}
				} else {
					if (player.getSkills().getLevel(Skills.SLAYER) < 93
							&& n.getId() == 9463) {
						player.sendMessage(slayerMessage.replaceAll("X", "93"));
						return false;
					}
					if (player.getSkills().getLevel(Skills.SLAYER) < 90
							& n.getId() == 2783) {
						player.sendMessage("You need a slayer level of 90 to attack "
								+ n.getDefinitions().name.toLowerCase() + "s.");
						return false;
					}
					if (player.getSkills().getLevel(Skills.SLAYER) < 85
							&& n.getId() == 1615) {
						player.sendMessage("You need a slayer level of 85 to attack "
								+ n.getDefinitions().name.toLowerCase() + "s.");
						return false;
					}
					if (player.getSkills().getLevel(Skills.SLAYER) < 75
							&& n.getId() == 1610) {
						player.sendMessage("You need a slayer level of 75 to attack "
								+ n.getDefinitions().name.toLowerCase() + "s.");
						return false;
					}
					if (player.getSkills().getLevel(Skills.SLAYER) < 80
							&& n.getId() == 1613) {
						player.sendMessage("You need a slayer level of 80 to attack "
								+ n.getDefinitions().name.toLowerCase() + "s.");
						return false;
					}
					if (player.getSkills().getLevel(Skills.SLAYER) < 50
							&& n.getId() == 1618) {
						player.sendMessage("You need a slayer level of 50 to attack "
								+ n.getDefinitions().name.toLowerCase() + "s.");
						return false;
					}
					if (player.getSkills().getLevel(Skills.SLAYER) < 45
							&& n.getId() == 1643) {
						player.sendMessage("You need a slayer level of 45 to attack "
								+ n.getDefinitions().name.toLowerCase() + "s.");
						return false;
					}
				}
			}
		}
		if ((!(target instanceof NPC) || !((NPC) target).isForceMultiAttacked())
				&& (!target.isAtMultiArea() || !player.isAtMultiArea())) {
			if (player.getAttackedBy() != target
					&& player.getAttackedByDelay() > System.currentTimeMillis())
				return false;
			if (target.getAttackedBy() != player
					&& target.getAttackedByDelay() > System.currentTimeMillis())
				return false;
		}
		int isRanging = isRanging(player);
		if (player.getX() == target.getX() && player.getY() == target.getY()
				&& target.getSize() == 1 && !target.hasWalkSteps()) {
			if (!player.addWalkSteps(target.getX() + 1, target.getY(), 1)
					&& !player
							.addWalkSteps(target.getX() - 1, target.getY(), 1)
					&& !player
							.addWalkSteps(target.getX(), target.getY() + 1, 1)
					&& !player
							.addWalkSteps(target.getX(), target.getY() - 1, 1))
				return false;
		} else if (isRanging == 0 && target.getSize() == 1
				&& player.getCombatDefinitions().getSpellId() <= 0
				&& Math.abs(player.getX() - target.getX()) == 1
				&& Math.abs(player.getY() - target.getY()) == 1
				&& !target.hasWalkSteps()) {
			if (!player.addWalkSteps(target.getX(), player.getY(), 1))
				player.addWalkSteps(player.getX(), target.getY(), 1);
			return true;
		}
		maxDistance = isRanging == 0
				&& player.getCombatDefinitions().getSpellId() <= 0 ? 0 : 7;
		if (!player.clipedProjectile(target,
				(target instanceof NexMinion) ? false : maxDistance == 0)
				|| (distanceX > size + maxDistance
						|| distanceX < -1 - maxDistance
						|| distanceY > size + maxDistance || distanceY < -1
						- maxDistance)) {
			if (!player.hasWalkSteps()) {
				player.resetWalkSteps();
				player.addWalkStepsInteract(target.getX(), target.getY(),
						player.getRun() ? 2 : 1, size, true);
			}
			return true;
		}
		player.resetWalkSteps();
		if (player.getPolDelay() >= System.currentTimeMillis()
				&& player.getEquipment().getWeaponId() != 15486
				&& player.getEquipment().getWeaponId() != 22207
				&& player.getEquipment().getWeaponId() != 22209
				&& player.getEquipment().getWeaponId() != 22211
				&& player.getEquipment().getWeaponId() != 22213)
			player.setPolDelay(0L);
		if ((player.getEquipment().getWeaponId() == 4153
				|| player.getEquipment().getWeaponId() == 15486
				|| player.getEquipment().getWeaponId() == 22207
				|| player.getEquipment().getWeaponId() == 22209
				|| player.getEquipment().getWeaponId() == 22211 || player
				.getEquipment().getWeaponId() == 22213)
				&& player.getCombatDefinitions().isUsingSpecialAttack()) {
			int weaponId = player.getEquipment().getWeaponId();
			int attackStyle = player.getCombatDefinitions().getAttackStyle();
			int specAmt = getSpecialAmmount(weaponId);
			if (player.getCombatDefinitions().hasRingOfVigour())
				specAmt = (int) ((double) specAmt * 0.90000000000000002D);
			if (player.getCombatDefinitions().getSpecialAttackPercentage() < specAmt) {
				player.getPackets().sendGameMessage(
						"You don't have enough power left.");
				player.getCombatDefinitions().desecreaseSpecialAttack(0);
				return false;
			}
			player.getCombatDefinitions().desecreaseSpecialAttack(specAmt);
			switch (weaponId) {
			case 4153:
				player.setAnimation(new Animation(1667));
				player.setGraphic(new Graphic(340));
				delayNormalHit(
						weaponId,
						attackStyle,
						new Hit[] { getMeleeHit(
								player,
								getRandomMaxHit(player, weaponId, attackStyle,
										false, true, 1.0D, true)) });
				break;

			case 15486:
			case 22207:
			case 22209:
			case 22211:
			case 22213:
				player.setAnimation(new Animation(12804));
				player.setGraphic(new Graphic(2320));
				player.setGraphic(new Graphic(2321));
				player.addPolDelay(60000L);
				break;
			}
			return true;
		} else {
			return true;
		}
	}

	
	 * 0 not ranging, 1 invalid ammo so stops att, 2 can range, 3 no ammo
	 
	public static final int isRanging(Player player) {
		int weaponId = player.getEquipment().get(Constants.SLOT_WEAPON) == null ? -1
				: player.getEquipment().get(Constants.SLOT_WEAPON).getId();
		if (weaponId == -1)
			return 0;
		String name = ItemDefinition.forId(weaponId).getName();
		if (name != null) { // those dont need arrows
			if (name.contains("knife") || name.contains("dart")
					|| name.contains("javelin") || name.contains("thrownaxe")
					|| name.contains("throwing axe")
					|| name.contains("Crystal bow")
					|| name.equalsIgnoreCase("Zaryte bow"))
				return 2;
		}
		int ammoId = player.getEquipment().get(Constants.SLOT_ARROWS) == null ? -1
				: player.getEquipment().get(Constants.SLOT_ARROWS).getId();
		switch (weaponId) {
		case 15241: // Hand cannon
			switch (ammoId) {
			case -1:
				return 3;
			case 15243: // bronze arrow
				return 2;
			default:
				return 1;
			}
		case 839: // longbow
		case 841: // shortbow
			switch (ammoId) {
			case -1:
				return 3;
			case 882: // bronze arrow
			case 884: // iron arrow
				return 2;
			default:
				return 1;
			}
		case 843: // oak longbow
		case 845: // oak shortbow
			switch (ammoId) {
			case -1:
				return 3;
			case 882: // bronze arrow
			case 884: // iron arrow
			case 886: // steel arrow
				return 2;
			default:
				return 1;
			}
		case 847: // willow longbow
		case 849: // willow shortbow
		case 13541: // Willow composite bow
			switch (ammoId) {
			case -1:
				return 3;
			case 882: // bronze arrow
			case 884: // iron arrow
			case 886: // steel arrow
			case 888: // mithril arrow
				return 2;
			default:
				return 1;
			}
		case 851: // maple longbow
		case 853: // maple shortbow
		case 18331: // Maple longbow (sighted)
			switch (ammoId) {
			case -1:
				return 3;
			case 882: // bronze arrow
			case 884: // iron arrow
			case 886: // steel arrow
			case 888: // mithril arrow
			case 890: // adamant arrow
				return 2;
			default:
				return 1;
			}
		case 2883:// ogre bow
			switch (ammoId) {
			case -1:
				return 3;
			case 2866: // ogre arrow
				return 2;
			default:
				return 1;
			}
		case 4827:// Comp ogre bow
			switch (ammoId) {
			case -1:
				return 3;
			case 2866: // ogre arrow
			case 4773: // bronze brutal
			case 4778: // iron brutal
			case 4783: // steel brutal
			case 4788: // black brutal
			case 4793: // mithril brutal
			case 4798: // adamant brutal
			case 4803: // rune brutal
				return 2;
			default:
				return 1;
			}
		case 855: // yew longbow
		case 857: // yew shortbow
		case 10281: // Yew composite bow
		case 14121: // Sacred clay bow
		case 859: // magic longbow
		case 861: // magic shortbow
		case 10284: // Magic composite bow
		case 18332: // Magic longbow (sighted)
		case 6724: // seercull
			switch (ammoId) {
			case -1:
				return 3;
			case 882: // bronze arrow
			case 884: // iron arrow
			case 886: // steel arrow
			case 888: // mithril arrow
			case 890: // adamant arrow
			case 892: // rune arrow
				return 2;
			default:
				return 1;
			}
		case 11235: // dark bows
		case 15701:
		case 15702:
		case 15703:
		case 15704:
			switch (ammoId) {
			case -1:
				return 3;
			case 882: // bronze arrow
			case 884: // iron arrow
			case 886: // steel arrow
			case 888: // mithril arrow
			case 890: // adamant arrow
			case 892: // rune arrow
			case 11212: // dragon arrow
				return 2;
			default:
				return 1;
			}
		case 19143: // saradomin bow
			switch (ammoId) {
			case -1:
				return 3;
			case 19152: // saradomin arrow
				return 2;
			default:
				return 1;
			}
		case 19146: // guthix bow
			switch (ammoId) {
			case -1:
				return 3;
			case 19157: // guthix arrow
				return 2;
			default:
				return 1;
			}
		case 19149: // zamorak bow
			switch (ammoId) {
			case -1:
				return 3;
			case 19162: // zamorak arrow
				return 2;
			default:
				return 1;
			}
		case 4734: // karil crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 4740: // bolt rack
				return 2;
			default:
				return 1;
			}
		case 10156: // hunters crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 10158: // Kebbit bolts
			case 10159: // Long kebbit bolts
				return 2;
			default:
				return 1;
			}
		case 8880: // Dorgeshuun c'bow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 8882: // bone bolts
				return 2;
			default:
				return 1;
			}
		case 14684: // zanik crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9142:// mithril bolts
			case 9143: // adam bolts
			case 9145: // silver bolts
			case 8882: // bone bolts
				return 2;
			default:
				return 1;
			}
		case 767: // phoenix crossbow
		case 837: // crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
				return 2;
			default:
				return 1;
			}
		case 9174: // bronze crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9236: // Opal bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9176: // blurite crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
			case 9139: // Blurite bolts
			case 9237: // Jade bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9177: // iron crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9179: // steel crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
				return 2;
			default:
				return 1;
			}
		case 13081: // black crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9181: // Mith crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9142:// mithril bolts
			case 9145: // silver bolts
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
			case 9240: // Sapphire bolts (e)
			case 9241: // Emerald bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9183: // adam c bow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9142:// mithril bolts
			case 9143: // adam bolts
			case 9145: // silver bolts wtf
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
			case 9240: // Sapphire bolts (e)
			case 9241: // Emerald bolts (e)
			case 9242: // Ruby bolts (e)
			case 9243: // Diamond bolts (e)
				return 2;
			default:
				return 1;
			}
		case 9185: // rune c bow
		case 18357: // chaotic crossbow
			switch (ammoId) {
			case -1:
				return 3;
			case 877: // bronze bolts
			case 9140: // iron bolts
			case 9141: // steel bolts
			case 13083: // black bolts
			case 9142:// mithril bolts
			case 9143: // adam bolts
			case 9144: // rune bolts
			case 9145: // silver bolts wtf
			case 9236: // Opal bolts (e)
			case 9238: // Pearl bolts (e)
			case 9239: // Topaz bolts (e)
			case 9240: // Sapphire bolts (e)
			case 9241: // Emerald bolts (e)
			case 9242: // Ruby bolts (e)
			case 9243: // Diamond bolts (e)
			case 9244: // Dragon bolts (e)
			case 9245: // Onyx bolts (e)
				return 2;
			default:
				return 1;
			}
		default:
			return 0;
		}
	}

}
*/