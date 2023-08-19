package com.sevador.utility.loaders;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.cache.format.ItemDefinition;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class GrandExchangePrice implements Runnable {

	public static void main(String[] args) {
		ExecutorService parallelExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		final Runnable task = new GrandExchangePrice();
		parallelExecutor.execute(task);
		parallelExecutor.execute(new Runnable() {
			@Override
			public void run() {
				GrandExchangePrice price = new GrandExchangePrice();
				price.run();
			}
		});
	}

	private static boolean dumpItem(int itemId) {
		String pageName = ItemDefinition.forId(itemId).name;
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
					if (line.equals("<th><a href=\"/wiki/Prices#Grand_Exchange_Price\" title=\"Prices\">Exchange price</a>"))
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
					examine = examine.replace("<span id=\"GEPrice\">", "");
					BufferedWriter writer = new BufferedWriter(new FileWriter("./data/nodes/items/geprices.txt", true));
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
					if (dumpItem(11694))
						System.out.println("DUMPED ITEM : " + itemId);
					else
						System.out.println("FAILED ITEM: " + itemId + ", "
								+ ItemDefinition.forId(itemId).getName());
			}
		}
	}

}
