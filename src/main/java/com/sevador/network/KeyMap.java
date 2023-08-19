package com.sevador.network;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sevador.Main;
import com.sevador.network.in.DefaultPacketHandler;

/**
 * The mapping holding all packet handling classes, sorted by key (opcode).
 * 
 * @author Emperor
 *
 */
public class KeyMap {
	
	/**
	 * The mapping of incoming packets.
	 */
	private static final HashMap<Integer, PacketSkeleton> HANDLING_MAP = new HashMap<Integer, PacketSkeleton>();

	/**
	 * Initializes the key map.
	 */
	public static void initialize() {
		Document doc;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File("./data/xml/packet_handlers.xml"));
		} catch (Throwable e) {
			e.printStackTrace();
			return;
		}
		HANDLING_MAP.clear();
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		int count = 0;
		PacketSkeleton lastSkeleton = new DefaultPacketHandler();
		Main.getLogger().info("Binding packet handlers...");
		for (short i = 1; i < nodeList.getLength(); i += 2) {
			Node n = nodeList.item(i);
			if (n != null) {
				if (n.getNodeName().equalsIgnoreCase("packet")) {
					NodeList list = n.getChildNodes();
					int id = 0;
					for (int a = 1; a < list.getLength(); a += 2) {
						Node node = list.item(a);
						if (node.getNodeName().equals("opcode")) {
							id = Integer.parseInt(node.getTextContent());
						} else if (node.getNodeName().equals("handler")) {
							try {
								Class<?> skeleton = Class.forName(node.getTextContent());
								if (skeleton != lastSkeleton.getClass()) {
									lastSkeleton = (PacketSkeleton) skeleton.newInstance();
								}
							} catch (Throwable e) {
								System.out.println(new StringBuilder("Unable to bind packet handler - [").append(id).append(", ").append(e.getMessage()).append("]."));
								count--;
								continue;
							}
							HANDLING_MAP.put(id, lastSkeleton);
						}
					}
					count++;
				}
			}
		}
		Main.getLogger().info("Bound " + count + " packet handlers");
	}
	
	/**
	 * Gets the packet handler used for the opcode.
	 * @param opcode The opcode.
	 * @return The packet handler.
	 */
	public static PacketSkeleton get(int opcode) {
		return HANDLING_MAP.get(opcode);
	}
}
