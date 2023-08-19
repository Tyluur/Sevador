package com.sevador.database;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import com.sevador.database.task.QueryTask;
import com.sevador.database.task.TaskComparator;

public class DatabaseTaskEngine {

	private boolean running = false;

	private BlockingQueue<QueryTask> tasks = new PriorityBlockingQueue<QueryTask>(
			10, new TaskComparator());

	private ExecutorService importantService = Executors
			.newSingleThreadExecutor();

	private ExecutorService normalService = Executors.newFixedThreadPool(2);

	public void run() {
		while (running) {
			try {
				final QueryTask task = tasks.take();
				switch (task.getPriority()) {
				case NORMAL:
					normalService.execute(new Runnable() {
						@Override
						public void run() {
							task.execute();
						}
					});
				case IMPORTANT:
					importantService.execute(new Runnable() {
						@Override
						public void run() {
							task.execute();
						}
					});
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public enum QueryPriority {
		NORMAL, IMPORTANT
	}
}