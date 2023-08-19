package com.sevador.database;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A Thread-Safe connection pool, In the future, if needed a class can be a
 * DatabaseConnection sub-instance to add onto the pooled connection types
 * 
 * @author Nikki
 * 
 */
public class ConnectionPool<T extends DatabaseConnection> {

	/**
	 * The configuration to use for this pool
	 */
	private DatabaseConfiguration configuration;

	/**
	 * The max connections this pool can have.
	 */
	private int maxConnections;

	/**
	 * The amount of current connections..
	 */
	private int currentConnections = 0;

	/**
	 * A thread-safe linked queue which contains our connections
	 */
	private ConcurrentLinkedQueue<DatabaseConnection> pool = new ConcurrentLinkedQueue<DatabaseConnection>();

	/**
	 * Create a new database pool
	 * 
	 * @param configuration
	 *            The configuration
	 */
	public ConnectionPool(DatabaseConfiguration configuration) {
		this(configuration, 10);
	}

	/**
	 * Create a new database pool
	 * 
	 * @param configuration
	 *            The configuration
	 * @param maxConnections
	 *            The connection limit
	 */
	public ConnectionPool(DatabaseConfiguration configuration,
			int maxConnections) {
		this.configuration = configuration;
		this.maxConnections = maxConnections;
	}

	/**
	 * Get the next free database connection
	 * 
	 * @return The connection if found, or a new connection.
	 */
	@SuppressWarnings("unchecked")
	public T nextFree() {
		// First check if a connection is free
		DatabaseConnection connection = pool.poll();
		if (connection != null) {
			if (!connection.isFresh()) {
				// DISCARD, since the connection is bad
				currentConnections--;
				return nextFree();
			}
			return (T) connection;
		}
		if (currentConnections >= maxConnections) {
			return null;
		}
		// If we don't find a connection, create a new one!
		connection = configuration.newConnection();
		connection.setPool((ConnectionPool<DatabaseConnection>) this);
		if (!connection.connect()) {
			throw new RuntimeException("Connection was unable to connect!");
		} else {
			currentConnections++;
		}
		return (T) connection;
	}

	/**
	 * Return a connection to this pool
	 * 
	 * @param connection
	 *            The connection
	 */
	public void returnConnection(DatabaseConnection connection) {
		pool.offer(connection);
	}
}