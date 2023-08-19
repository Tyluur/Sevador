package com.sevador.utility.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.MalformedURLException;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.utility.loaders.WebPage;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class WeightDumper implements Runnable {

	public static void main(String[] args) {
		WeightDumper price = new WeightDumper();
		price.run();
	}

	private static boolean dumpItem(int itemId) {
		String pageName = ItemDefinition.forId(itemId).getName();
		if (pageName == null || pageName.equals("null"))
			return false;
		pageName = pageName.replace("(p)", "");
		pageName = pageName.replace("(p+)", "");
		pageName = pageName.replace("(p++)", "");
		pageName = pageName.replaceAll(" ", "_");
		try {
			WebPage page = new WebPage("http://runescape.wikia.com/wiki/"+ pageName);
			try {
				page.load();
			} catch (Exception e) {
				System.out.println("Invalid page: " + itemId + ", " + pageName);
				return false;
			}
			boolean isNextLine = false;
			for (String line : page.getLines()) {
				if (!isNextLine) {
					if (line.equals("<th nowrap=\"nowrap\"><a href=\"/wiki/Weight\" title=\"Weight\">Weight</a>"))
						isNextLine = true;
				} else {
					String examine = line.replace("</th><td> ", "");
					examine = examine.replace("</th><td>", "");
					examine = examine.replace("<i> ", "");
					examine = examine.replace("</i> ", "");
					examine = examine.replace("&lt;colour&gt; ", "");
					examine = examine.replace("(bright/thick/warm)", "bright");
					examine = examine.replace("(Temple of Ikov) ", "");
					examine = examine.replace("(Fight Arena) ", "");
					examine = examine.replace("<span id=\"GEPrice\"><span class=\"GEItem\"><span>","");
					examine = examine.replace("</span></span> coins</span>", "");
					examine = examine.replace(",", "");
					examine = examine.replace("&#160;kg", "");
					examine = examine.replace("<span id=\"GEPrice\">", "");
					BufferedWriter writer = new BufferedWriter(new FileWriter("./data/nodes/items/weights.txt", true));
					writer.write(itemId + " - " + examine);
					writer.newLine();
					writer.flush();
					writer.close();
					return true;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			return dumpItem(itemId);
		}
		return false;
	}

	@Override
	public void run() {
		while (true) {
			Cache.init();
			for (int itemId = 0; itemId < Cache.getAmountOfItems(); itemId++) {
				if (!ItemDefinition.forId(itemId).isNoted())
					if (dumpItem(itemId))
						System.out.println("DUMPED ITEM : " + itemId);
					else
						System.out.println("FAILED ITEM: " + itemId + ", "
								+ ItemDefinition.forId(itemId).getName());
			}
		}
	}

}
