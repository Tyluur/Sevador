package com.sevador.database.mysql;

import com.sevador.database.DatabaseConfiguration;
import com.sevador.database.DatabaseConnection;

/**
 * A database configuration for MySQL Database servers
 * 
 * @author Nikki
 * 
 */
public class MySQLDatabaseConfiguration implements DatabaseConfiguration {

	/**
	 * The database server host
	 */
	private String host;

	/**
	 * The database server port
	 */
	private int port;

	/**
	 * The database on the server
	 */
	private String database;

	/**
	 * The username of the server
	 */
	private String username;

	/**
	 * The password of the server
	 */
	private String password;

	/**
	 * Create a new configuration
	 * 
	 * @param host
	 *            The host
	 * @param port
	 *            The port
	 * @param database
	 *            The database
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 */
	public MySQLDatabaseConfiguration(String host, int port, String database,
			String username, String password) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	/**
	 * Create an empty connection and use setters to set the information
	 */
	public MySQLDatabaseConfiguration() {

	}

	/**
	 * Get the database host
	 * 
	 * @return The database host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Set the database host
	 * 
	 * @param host
	 *            The database host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Get the database port
	 * 
	 * @return The port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Set the database port
	 * 
	 * @param port
	 *            The port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Get the database name
	 * 
	 * @return The database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * Set the database name
	 * 
	 * @param database
	 *            The database
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * Get the database username
	 * 
	 * @return The database username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set the database username
	 * 
	 * @param username
	 *            The database username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Get the database password
	 * 
	 * @return The database password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the database password
	 * 
	 * @param password
	 *            The password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public DatabaseConnection newConnection() {
		return new MySQLDatabaseConnection(this);
	}
}