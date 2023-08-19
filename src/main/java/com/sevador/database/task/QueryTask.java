package com.sevador.database.task;

import com.sevador.database.DatabaseTaskEngine.QueryPriority;

public class QueryTask {

	private QueryPriority priority = QueryPriority.NORMAL;

	public QueryTask(QueryPriority priority) {
		this.priority = priority;
	}

	public void execute() {
	}

	public QueryPriority getPriority() {
		return priority;
	}
}