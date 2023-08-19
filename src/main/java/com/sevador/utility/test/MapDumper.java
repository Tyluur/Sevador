package com.sevador.utility.test;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.cache.format.LandscapeParser;
import net.burtleburtle.thread.ParallelExecutor;

import com.sevador.utility.MapData;

/**
 * Dumps all the maps.
 * @author Emperor
 *
 */
public class MapDumper implements Runnable {

	static int count = 0;
	
	static boolean dumping = true;
	
	/**
	 * The progress bar.
	 */
	private static ProgressBar progress;
	
	public static void main(String...strings) {
		Cache.init();
		MapData.init();
		progress = new ProgressBar("Map dumper", 1712);
		progress.setVisible(true);
		progress.pack();
		ParallelExecutor exec = new ParallelExecutor();
		final Runnable task = new MapDumper(0, MapData.getMapData().size() >> 1);
		final Runnable task1 = new MapDumper(MapData.getMapData().size() >> 1, MapData.getMapData().size());
		exec.offer(task, task1);
	}
	
	final int start, end;
	private MapDumper(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public void run() {
		Object[] keyset = MapData.getMapData().keySet().toArray();
		for (int i = start; i < end; i++) {
			if (keyset[i] == null) {
				continue;
			}
			int key = (Integer) keyset[i];
			if (LandscapeParser.parseLandscape(key, MapData.getMapData(key))) {
				count++;
				progress.updateStatus(count);
			}
		}
		dumping = false;
	}
}
