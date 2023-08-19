package com.sevador.utility.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.utility.Constants;

/**
 * Dumps several item definitions.
 * @author Emperor
 *
 */
public class ItemDefinitionsDumper implements Runnable {

	/**
	 * The website we're using to dump.
	 */
	private static final String WEBSITE = "http://runescape.wikia.com/wiki/";
	
	/**
	 * If the dumper is still dumping.
	 */
	private static boolean dumping = true;
	
	/**
	 * The current amount of item definitions dumped.
	 */
	private static int currentAmount = 0;
	
	/**
	 * The maximum amount of items to dump.
	 */
	private static int maximumAmount;
	
	/**
	 * The progress bar.
	 */
	private static ProgressBar progress;
	
	/**
	 * The current cache item definitions.
	 */
	private ItemDefinition def;
	
	/**
	 * The writer used to write the definitions.
	 */
	private BufferedWriter writer;
	
	/**
	 * The reader used to read the dumping site.
	 */
	private BufferedReader reader;
	
	/**
	 * The main method.
	 * @param args The arguments.
	 */
	public static void main(String...args) {
		ExecutorService parallelExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		final Runnable task = new ItemDefinitionsDumper();
		parallelExecutor.execute(task);
		parallelExecutor.execute(new Runnable() {
			@Override
			public void run() {
				while (dumping) {
					ItemDefinitionsDumper.progress.updateStatus(currentAmount);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}			
		});
	}

	/**
	 * Constructs a new {@code ItemDefinitionsDumper} {@code Object}.
	 */
	private ItemDefinitionsDumper() {
		Cache.init();
		try {
			this.writer = new BufferedWriter(new FileWriter("./ItemDump.txt"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		maximumAmount = Cache.getAmountOfItems();
		progress = new ProgressBar("Item definitions dumper", maximumAmount);
		progress.setVisible(true);
		progress.pack();
	}
	
	@Override
	public void run() {
		while (dumping) {
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter("./Undumpable.txt"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			int failed = 0;
			for (int id = 470; id < 2748; id++) { //0; id < maximumAmount; id++) {
				if (progress.paused) {
					try {
						this.writer.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				while (progress.paused) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				currentAmount++;
		        this.def = ItemDefinition.forId(id);
				if (def.name == null || def.name.equals("null")) {
					continue;
				}
				String name = def.name.replaceAll(" ", "_");
				try {
					this.reader = new BufferedReader(
							new InputStreamReader(
									new URL(new StringBuilder(WEBSITE).
											append(name).toString()).
													openStream()));
					dumpDefinition(id);
				} catch (Throwable t) {
					t.printStackTrace();
					failed++;
					if (writer != null) {
						try {
							writer.write(new StringBuilder("Failed to dump : ").append(id).append(", name: ").append(def.name).toString());
							writer.newLine();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			try {
				writer.flush();
				writer.close();
				this.writer.flush();
				this.writer.close();
				System.out.println("Finished dumping " + maximumAmount + " items; " + failed + " failed definitions.");
			} catch (Throwable e) {
				e.printStackTrace();
			}
			dumping = false;
			progress.updateStatus(currentAmount);
		}
	}
	
	/**
	 * Dumps the definitions for an item.
	 * @param id The item id.
	 */
	private void dumpDefinition(int id) {
		String s;
		try {
			boolean hasFailedBefore = false;
			boolean highAlch = false, lowAlch = false, 
					dropable = false, tradable = false, 
					store = false, examine = false;
			int bonusId = 0;
			int absorbId = 0;
			while ((s = reader.readLine()) != null) {
				if (s.contains("/wiki/Two-handed_slot")) {
					writer.write(new StringBuilder("EquipmentSlot").append(id).append(": ").append(Constants.SLOT_WEAPON).toString());
					writer.newLine();
					writer.write(new StringBuilder("TwoHanded").append(id).append(": ").append(true).toString());
					writer.newLine();
				} else if (s.contains("/wiki/Legwear_slot")) {
					writer.write(new StringBuilder("EquipmentSlot").append(id).append(": ").append(Constants.SLOT_LEGS).toString());
					writer.newLine();
				} else if (s.contains("/wiki/Ammunition_slot")) {
					writer.write(new StringBuilder("EquipmentSlot").append(id).append(": ").append(Constants.SLOT_ARROWS).toString());
					writer.newLine();
				} else if (s.contains("/wiki/Ring_slot")) {
					writer.write(new StringBuilder("EquipmentSlot").append(id).append(": ").append(Constants.SLOT_RING).toString());
					writer.newLine();
				} else if (s.contains("/wiki/Feet_slot")) {
					writer.write(new StringBuilder("EquipmentSlot").append(id).append(": ").append(Constants.SLOT_FEET).toString());
					writer.newLine();
				} else if (s.contains("/wiki/Hands_slot")) {
					writer.write(new StringBuilder("EquipmentSlot").append(id).append(": ").append(Constants.SLOT_HANDS).toString());
					writer.newLine();
				} else if (s.contains("/wiki/Shield_slot")) {
					writer.write(new StringBuilder("EquipmentSlot").append(id).append(": ").append(Constants.SLOT_SHIELD).toString());
					writer.newLine();
				} else if (s.contains("/wiki/Body_slot")) {
					writer.write(new StringBuilder("EquipmentSlot").append(id).append(": ").append(Constants.SLOT_CHEST).toString());
					writer.newLine();
				} else if (s.contains("/wiki/Weapon_slot")) {
					writer.write(new StringBuilder("EquipmentSlot").append(id).append(": ").append(Constants.SLOT_WEAPON).toString());
					writer.newLine();
				} else if (s.contains("/wiki/Neck_slot")) {
					writer.write(new StringBuilder("EquipmentSlot").append(id).append(": ").append(Constants.SLOT_AMULET).toString());
					writer.newLine();
				} else if (s.contains("/wiki/Cape_slot")) {
					writer.write(new StringBuilder("EquipmentSlot").append(id).append(": ").append(Constants.SLOT_CAPE).toString());
					writer.newLine();
				} else if (s.contains("/wiki/Head_slot")) {
					writer.write(new StringBuilder("EquipmentSlot").append(id).append(": ").append(Constants.SLOT_HAT).toString());
					writer.newLine(); 
				} else if (s.contains("/wiki/Aura_slot")) {
					writer.write(new StringBuilder("EquipmentSlot").append(id).append(": ").append(Constants.SLOT_AURA).toString());
					writer.newLine();
				} else if (s.contains("Speed5.gif")) {
					writer.write(new StringBuilder("AttackSpeed").append(id).append(": ").append(5).toString());
					writer.newLine();
				} else if (s.contains("Speed") && s.contains(".gif") && s.contains("alt=")) {
					String speedIdent = s.split("Speed")[1];
					try {
						int speed = Integer.parseInt(speedIdent.split(".gif")[0]);
						writer.write(new StringBuilder("AttackSpeed").append(id).append(": ").append(speed).toString());
						writer.newLine();
					} catch (Throwable t) {
						
					}
				} else if (s.contains("/wiki/Examine")) {
					examine = true;
				} else if (s.contains("/wiki/High_Level_Alchemy")) {
					highAlch = true;
				} else if (s.contains("/wiki/Low_Level_Alchemy")) {
					lowAlch = true;
				} else if (s.contains("/wiki/Destroy")) {
					dropable = true;
				} else if (s.contains("/wiki/Tradeable")) {
					tradable = true;
				} else if (s.contains("/wiki/Prices#Store_Price")) {
					store = true;
				} else if (s.contains("</th><td>")) {
					if (examine) {
						String ex = new StringBuilder("Examine").append(id).
								append(": ").append(s.replaceAll("\\<.*? >","").
										replaceAll("</th><td> ", "").
										replaceAll("&#160;", " ").
										replaceAll("<ul><li>", "").
										replaceAll("<b>Discontinued Item:</b> ", "").
										replaceAll("<b>", "").replaceAll("</b>", "").
										replaceAll("<br />", "").replaceAll("<i>", "").replaceAll("</i>", "")).toString();
						if (ex.equals("</th><td>") && !hasFailedBefore) {
							hasFailedBefore = true;
							continue;
						}
						if (ex.contains("(x)")) {
							int amount = formatString(def.name);
							if (amount > 0) {
								ex.replace("(x)", "" + amount);
							}
						}
						hasFailedBefore = false;
						writer.write(ex);
						writer.newLine();
						examine = false;
					}
					if (highAlch) {
						writer.write(new StringBuilder("High-alch").append(id).append(": ").append(formatString(s.replaceAll("</th><td>", ""))).toString());
						writer.newLine();
						highAlch = false;
					} else if (lowAlch) {
						writer.write(new StringBuilder("Low-alch").append(id).append(": ").append(formatString(s.replaceAll("</th><td>", ""))).toString());
						writer.newLine();
						lowAlch = false;
					} else if (dropable) {
						writer.write(new StringBuilder("Dropable").append(id).append(": ").append(getBoolean(s)).toString());
						writer.newLine();
						dropable = false;
					} else if (tradable) {
						writer.write(new StringBuilder("Tradeable").append(id).append(": ").append(getBoolean1(s)).toString());
						writer.newLine();
						tradable = false;
					} else if (store) {
						writer.write(new StringBuilder("StorePrice").append(id).append(": ").append(formatString(s.replaceAll("</th><td>", ""))).toString());
						writer.newLine();
						store = false;
					}
				} else if (s.equals("<ul><li>")) {
					if (examine && hasFailedBefore) {
						String ex = new StringBuilder("Examine").append(id).
								append(": ").append(s.replaceAll("\\<.*? >","").
										replaceAll("</th><td> ", "").
										replaceAll("&#160;", " ").
										replaceAll("<ul><li>", "").
										replaceAll("<b>Discontinued Item:</b> ", "").
										replaceAll("<b>", "").replaceAll("</b>", "").
										replaceAll("<br />", "").replaceAll("<i>", "").replaceAll("</i>", "")).toString();
						if (ex.contains("(x)")) {
							int amount = formatString(def.name);
							if (amount > 0) {
								ex.replace("(x)", "" + amount);
							}
						}
						hasFailedBefore = false;
						writer.write(ex);
						writer.newLine();
						examine = false;
					}
				} else if (s.contains("<td colspan=\"2\" width=\"30\" align=\"center\">")) {
					writer.write(new StringBuilder("Bonus").append(id).append(": ").append(bonusId++).append(" - ").append(formatString(s.replaceAll("<td colspan=\"2\" width=\"30\" align=\"center\">", "").replaceAll("</td>", ""))).toString());
					writer.newLine();
				} else if (s.contains("<td colspan=\"4\" width=\"60\" align=\"center\">")) {
					writer.write(new StringBuilder("Absorb").append(id).append(": ").append(absorbId++).append(" - ").append(formatString(s.replaceAll("<td colspan=\"4\" width=\"60\" align=\"center\">", "").replaceAll("</td>", "").replaceAll("%", ""))).toString());
					writer.newLine();
				} else if (s.contains("<td colspan=\"3\" width=\"45\" align=\"center\">")) {
					writer.write(new StringBuilder("Bonus").append(id).append(": ").append(bonusId++).append(" - ").append(formatString(s.replaceAll("<td colspan=\"3\" width=\"45\" align=\"center\">", "").replaceAll("</td>", "").replaceAll("%", ""))).toString());
					writer.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if the item is tradable.
	 * @param s The string.
	 * @return {@code True} if so.
	 */
	private boolean getBoolean1(String s) {
		return s.contains("Yes") || s.contains("yes");
	}

	/**
	 * Gets an integer value from a string.
	 * @param s The string.
	 * @return The value;
	 */
	private int formatString(String s) {
		s = s.replaceAll(", ", "").replaceAll(",", "");
		StringBuilder sb = new StringBuilder();
		char c;
		boolean foundStart = false;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isDigit(c) || c == '-') {
				sb.append(c);
				foundStart = true;
			} else if (foundStart) {
				break;
			}
		}
		try {
			int amount = Integer.parseInt(sb.toString());
			return amount;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * Checks if the input is true (regarding tradable & dropable).
	 * @param s The string to format.
	 * @return The boolean value.
	 */
	private boolean getBoolean(String s) {
		return s.contains("Drop") || s.contains("drop");
	}

}