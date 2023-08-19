package com.sevador.game.action.impl.emote;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.burtleburtle.tick.TypeTick;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.AppearanceUpdate;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.player.Player;

/**
 * Represents an emote the player can use.
 * @author Emperor
 *
 */
public class Emote {

	/**
	 * The amount of ticks this emote takes to finish.
	 */
	private int ticks;
	
	/**
	 * The required item slot, used for skillcape emotes etc.
	 */
	private int requiredItemSlot = -1;
		
	/**
	 * The mapping of emote update ticks.
	 */
	private final Map<Integer, List<TypeTick<Player>>> emoteUpdates = new HashMap<Integer, List<TypeTick<Player>>>();
	
	/**
	 * The mapping of emotes.
	 */
	private static final Map<Integer, Emote> EMOTES = new HashMap<Integer, Emote>();
	
	/**
	 * Loads all the emotes and populates the mapping.
	 */
	public static void init() {
		EMOTES.clear();
		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File("./data/xml/emotes.xml"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		for (int i = 1; i < nodeList.getLength(); i += 2) {
			Node n = nodeList.item(i);
			if (n.getNodeName().equals("emote")) {
				NodeList list = n.getChildNodes();
				int buttonId = -1;
				Emote emote = new Emote();
				for (int a = 1; a < list.getLength(); a += 2) {
					Node node = list.item(a);
					if (node.getNodeName().equals("button_id")) {
						buttonId = Integer.parseInt(node.getTextContent());
					} else if (node.getNodeName().equals("max_ticks")) {
						emote.setTicks(Integer.parseInt(node.getTextContent()));
					} else if (node.getNodeName().equals("tick")) {
						int tick = Integer.parseInt(node.getAttributes().getNamedItem("value").getTextContent());
						List<TypeTick<Player>> updates = new ArrayList<TypeTick<Player>>();
						NodeList l = node.getChildNodes();
						for (int j = 1; j < l.getLength(); j += 2) {
							Node child = l.item(j);
							if (child.getNodeName().equals("anim")) {
								final int animId = Integer.parseInt(child.getTextContent());
								updates.add(new TypeTick<Player>(0) {
									@Override
									public boolean run(Player... arg) {
										arg[0].getUpdateMasks().register(new Animation(animId, 0, false));
										return true;
									}							
								});
							} else if (child.getNodeName().equals("gfx")) {
								final int gfxId = Integer.parseInt(child.getTextContent());
								updates.add(new TypeTick<Player>(0) {
									@Override
									public boolean run(Player... arg) {
										arg[0].getUpdateMasks().register(new Graphic(gfxId, 0, 120, false));
										return true;
									}							
								});
							} else if (child.getNodeName().equals("set_npc")) {
								final int npcId = Integer.parseInt(child.getTextContent());
								updates.add(new TypeTick<Player>(0) {
									@Override
									public boolean run(Player... arg) {
										arg[0].getCredentials().getAppearance().setNpcId(npcId);
										arg[0].getUpdateMasks().register(new AppearanceUpdate(arg[0]));
										return true;
									}							
								});
							}
						}
						emote.emoteUpdates.put(tick, updates);
					} else if (node.getNodeName().equals("req_item_slot")) {
						emote.setRequiredItemSlot(Integer.parseInt(node.getTextContent()));
					}
				}
				EMOTES.put(buttonId, emote);
			}
		}
	}

	/**
	 * @return the ticks
	 */
	public int getTicks() {
		return ticks;
	}

	/**
	 * @param ticks the ticks to set
	 */
	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	/**
	 * @return the requiredItemSlot
	 */
	public int getRequiredItemSlot() {
		return requiredItemSlot;
	}

	/**
	 * Gets the mapping of emote updates.
	 * @return The mapping.
	 */
	public Map<Integer, List<TypeTick<Player>>> getEmoteUpdates() {
		return emoteUpdates;
	}
	
	/**
	 * @param requiredItemSlot the requiredItemSlot to set
	 */
	public void setRequiredItemSlot(int requiredItemSlot) {
		this.requiredItemSlot = requiredItemSlot;
	}

	/**
	 * @return the emotes
	 */
	public static Map<Integer, Emote> getEmotes() {
		return EMOTES;
	}
}