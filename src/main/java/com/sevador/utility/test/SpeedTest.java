package com.sevador.utility.test;

import net.burtleburtle.thread.ParallelExecutor;

public class SpeedTest {

	private static long average = 0;
	private static int count = 0;
	private static boolean finished = false;
	private static long start;
	interface Lol {
		boolean isRoar();
	}
	static class Roar implements Lol {
		@Override
		public boolean isRoar() {
			return true;
		}		
	}
	static class Meow implements Lol {
		@Override
		public boolean isRoar() {
			return false;
		}		}
	@SuppressWarnings("unused")
	public static void main(String...args) {
		if (true) {
			start = System.currentTimeMillis();
			for (int i = 0; i < 500000; i++) {
				
			}
			long elapsed = System.currentTimeMillis() - start;
			count++;
			average += elapsed;
			System.out.println(new StringBuilder("Parallel executor took ").append(elapsed).append("ms to complete; average: ").append(average / count));
			main();
			return;
		}
		ParallelExecutor exec = new ParallelExecutor();
		Runnable r = new Runnable() {
			@Override
			public void run() {
				String s = "lol";
				for (int i = 0; i < 1024; i++) {
					s += "rofl";
					s.replace("rofl", "");
				}
				if (finished) {
					long elapsed = System.currentTimeMillis() - start;
					count++;
					average += elapsed;
					System.out.println(new StringBuilder("Parallel executor took ").append(elapsed).append("ms to complete; average: ").append(average / count));
					main();
					return;
				}
				finished = true;
			}			
		};
		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				String s = "lol";
				for (int i = 0; i < 1024; i++) {
					s += "rofl";
					s.replace("rofl", "");
				}
				if (finished) {
					long elapsed = System.currentTimeMillis() - start;
					count++;
					average += elapsed;
					System.out.println(new StringBuilder("Parallel executor took ").append(elapsed).append("ms to complete; average: ").append(average / count));
					main();
					return;
				}
				finished = true;
			}			
		};
		start = System.currentTimeMillis();
		exec.offer(r, r1);
		/*start = System.currentTimeMillis();
		String s = "lol";
		for (int i = 0; i < 2048; i++) {
			s += "rofl";
			s.replace("rofl", "");
		}
		long elapsed = System.currentTimeMillis() - start;
		count++;
		average += elapsed;
		System.out.println(new StringBuilder("Parallel executor took ").append(elapsed).append("ms to complete; average: ").append(average / count));
		main();*/
	}
	
}