package com.sevador.game.node;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.sevador.Main;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;

/**
 * Holds all the nodes' node types.
 * 
 * @author Emperor
 * 
 */
public class NodeTypeRepositary {

	/**
	 * The node types of each NPC.
	 */
	private static final Map<Integer, NodeType> NPC_NODE_TYPES = new HashMap<Integer, NodeType>();

	/**
	 * The node types of each game object.
	 */
	private static final Map<Integer, NodeType> OBJECT_NODE_TYPES = new HashMap<Integer, NodeType>();

	/**
	 * Initializes the node types.
	 * 
	 * @return {@code True} if succesful.
	 */
	public static boolean init() {
		Document doc;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File("./data/xml/node_types.xml"));
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		Main.getLogger().info("Loading node types...");
		try {
			for (int i = 1; i < nodeList.getLength(); i += 2) {
				org.w3c.dom.Node n = nodeList.item(i);
				if (n != null && n.getNodeName().equals("node")) {
					String type = n.getAttributes().getNamedItem("type")
							.getTextContent();
					NodeList list = n.getChildNodes();
					int id = -1;
					NodeType nodeType = NodeType.DEFAULT;
					for (int a = 1; a < list.getLength(); a += 2) {
						org.w3c.dom.Node node = list.item(a);
						if (node.getNodeName().equals("type")) {
							nodeType = Enum.valueOf(NodeType.class,
									node.getTextContent());
						} else if (node.getNodeName().equals("id")) {
							id = Integer.parseInt(node.getTextContent());
						}
					}
					if (type.equals("game_object")) {
						if (OBJECT_NODE_TYPES.containsKey(id)) {
							throw new IllegalStateException(
									"Object node type for id " + id
											+ " already exists!");
						}
						OBJECT_NODE_TYPES.put(id, nodeType);
					} else if (type.equals("npc")) {
						if (NPC_NODE_TYPES.containsKey(id)) {
							throw new IllegalStateException(
									"NPC node type for id " + id
											+ " already exists!");
						}
						NPC_NODE_TYPES.put(id, nodeType);
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		Main.getLogger().info("Loaded " + OBJECT_NODE_TYPES.size()+ " object node types & " + NPC_NODE_TYPES.size()+ " NPC node types.");
		return true;
	}

	/**
	 * Gets an NPC's node type.
	 * 
	 * @param npc
	 *            The NPC.
	 * @return The node type.
	 */
	public static NodeType get(NPC npc) {
		return NPC_NODE_TYPES.get(npc.getId());
	}

	/**
	 * Gets a game object's node type.
	 * 
	 * @param object
	 *            The game object.
	 * @return The node type.
	 */
	public static NodeType get(GameObject object) {
		return OBJECT_NODE_TYPES.get(object.getId());
	}

	/**
	 * Gets a node type.
	 * 
	 * @param node
	 *            The node.
	 * @return The node type.
	 */
	public static NodeType get(Node node) {
		if (node instanceof NPC) {
			return NPC_NODE_TYPES.get(((NPC) node).getId());
		} else if (node instanceof Player) {
			return NodeType.PLAYER;
		} else if (node instanceof GameObject) {
			return OBJECT_NODE_TYPES.get(((GameObject) node).getId());
		}
		return NodeType.DEFAULT;
	}

}