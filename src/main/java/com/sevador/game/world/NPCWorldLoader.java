package com.sevador.game.world;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.burtleburtle.cache.format.NPCDefinition;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.sevador.Main;
import com.sevador.game.node.Node;
import com.sevador.game.node.NodeType;
import com.sevador.game.node.NodeTypeRepositary;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.npc.NPC;
import com.sevador.utility.ReturnCodes;

/**
 * Handles the NPC world loading.
 * 
 * @author Emperor
 * 
 */
public class NPCWorldLoader implements WorldLoader<NPC> {

	/**
	 * The NPC classes.
	 */
	private static final Map<Integer, Class<?>> CUSTOM_NPCS = new HashMap<Integer, Class<?>>();

	@Override
	public LoginResult checkLogin(Node node) {
		return new LoginResult(ReturnCodes.SUCCESFUL, node);
	}

	@Override
	public NPC load(LoginResult result) {
		NPC npc = (NPC) result.getNode();
		NodeType n = NodeTypeRepositary.get(npc);
		if (n != null) {
			npc.setNodeType(n);
		}
		return npc;
	}

	@Override
	public NPC save(Node node) {
		return (NPC) node;
	}

	/**
	 * Loads custom NPC classes.
	 * 
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	public static boolean init() {
		NPCDefinition.init();
		Document doc;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File("./data/xml/custom_npcs.xml"));
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		Main.getLogger().info("Loading customized NPC classes...");
		for (short i = 1; i < nodeList.getLength(); i += 2) {
			org.w3c.dom.Node n = nodeList.item(i);
			if (n != null) {
				if (n.getNodeName().equalsIgnoreCase("npc")) {
					NodeList list = n.getChildNodes();
					Class<?> npcObject = null;
					for (int a = 1; a < list.getLength(); a += 2) {
						org.w3c.dom.Node node = list.item(a);
						if (node.getNodeName().equalsIgnoreCase("handler")) {
							try {
								npcObject = Class
										.forName(node.getTextContent());
							} catch (Throwable e) {
								e.printStackTrace();
								break;
							}
						} else if (node.getNodeName().equalsIgnoreCase("id")) {
							CUSTOM_NPCS.put(
									Integer.parseInt(node.getTextContent()),
									npcObject);
						}
					}
				}
			}
		}
		Main.getLogger().info("Loaded " + CUSTOM_NPCS.size() + " customized NPCs.");
		Main.getLogger().info("Loading NPC spawns...");
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File("./data/xml/npc_spawns.xml"));
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		nodeList = doc.getDocumentElement().getChildNodes();
		int count = 0;
		for (int i = 1; i < nodeList.getLength(); i += 2) {
			org.w3c.dom.Node n = nodeList.item(i);
			if (n != null) {
				if (n.getNodeName().equals("npc")) {
					try {
						NodeList list = n.getChildNodes();
						NPC npc = null;
						int x = 0;
						int y = 0;
						int z = 0;
						for (int a = 1; a < list.getLength(); a += 2) {
							org.w3c.dom.Node node = list.item(a);
							if (node.getNodeName().equals("id")) {
								npc = getNPC(Integer.parseInt(node
										.getTextContent()));
							} else if (node.getNodeName().equals("x")) {
								x = Integer.parseInt(node.getTextContent());
							} else if (node.getNodeName().equals("y")) {
								y = Integer.parseInt(node.getTextContent());
							} else if (node.getNodeName().equals("z")) {
								z = Integer.parseInt(node.getTextContent());
							} else if (node.getNodeName().equals("rotation")) {
								npc.setRotation(Integer.parseInt(node
										.getTextContent()));
							}
						}
						Location loc = Location.locate(x, y, z);
						npc.setLocation(loc);
						npc.setSpawnLocation(loc);
						Main.getNodeWorker().offer(npc);
						count++;
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
		}
		Main.getLogger().info("Loaded " + count + " NPC spawns.");
		return true;
	}

	/**
	 * Gets the NPC object.
	 * 
	 * @param npcId
	 *            The npc id.
	 * @return The NPC object.
	 */
	public static NPC getNPC(int npcId) {
		Class<?> npcHandler = CUSTOM_NPCS.get(npcId);
		if (npcHandler != null) {
			try {
				return (NPC) npcHandler.getConstructor(int.class).newInstance(npcId);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return new NPC(npcId);
	}

}