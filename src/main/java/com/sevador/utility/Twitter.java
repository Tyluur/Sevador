package com.sevador.utility;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Manages our Twitter Page content.
 * 
 * @author Tyluur
 *
 */
public class Twitter {

	/**
	 * Gets the last Tweet.
	 * 
	 * @return
	 */
	public static String getLastTweet() {
		Document doc = null;
		try {
			String url = "http://api.twitter.com/1/statuses/user_timeline/sevador666.xml";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new URL(url).openStream()));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		for (int i = 1; i < nodeList.getLength(); i += 2) {
			Node n = nodeList.item(i);
			if (n.getNodeName().equals("status")) {
				NodeList list = n.getChildNodes();
				for (int a = 1; a < list.getLength(); a += 2) {
					Node node = list.item(a);
					if (node.getNodeName().equals("text")) {
						return node.getTextContent();
					}
				}
			}
		}
		return null;
	}

}