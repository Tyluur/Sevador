package com.sevador.network;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import com.sevador.game.node.player.Player;

/**
 * Represents an active connection.
 * @author Dementhium development team
 *
 */
public class IOSession implements Message {

	/**
	 * The channel instance.
	 */
	private Channel channel;

	/**
	 * The connection stage.
	 */
	private byte connectionStage;

	/**
	 * The name hash.
	 */
	private byte nameHash;

	/**
	 * The session key of the connected player.
	 */
	private long sessionKey;

	/**
	 * The display mode of the connected player.
	 */
	private int screenSizeMode;
	
	/**
	 * The screen width.
	 */
	private int screenSizeX;
	
	/**
	 * The screen height.
	 */
	private int screenSizeY;
	
	/**
	 * The current display mode.
	 */
	private int displayMode;

	/**
	 * The connected player.
	 */
	private Player player;

	/**
	 * If the session is in lobby;
	 */
	private boolean inLobby;
	
	/**
	 * The constructor, sets the channel instance.
	 * 
	 * @param channel
	 *            The instance.
	 */
	public IOSession(Channel channel) {
		this.channel = channel;
	}
	
	/**
	 * Writes a new OutStream object to the Channel Future.
	 * 
	 * @param outputStream
	 *            The out-stream object.
	 * @return null.
	 */
	public synchronized ChannelFuture write(OutgoingPacket outgoingPacket) {
		try {
			if (channel != null && channel.isConnected()) {
				return channel.write(outgoingPacket.get());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean execute(ChannelHandler ch, ChannelHandlerContext ctx, MessageEvent e, IOSession session) {
		ch.setSession(this);
		return true;
	}

	/**
	 * Gets the channel instance.
	 * 
	 * @return The channel instance.
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * Sets the connection stage.
	 * 
	 * @param connectionStage
	 *            The amount to be set.
	 */
	public void setConnectionStage(byte connectionStage) {
		this.connectionStage = connectionStage;
	}

	/**
	 * Gets the connection stage.
	 * 
	 * @return The connection stage.
	 */
	public byte getConnectionStage() {
		return connectionStage;
	}

	/**
	 * Sets the name hash.
	 * @param nameHash
	 *            the nameHash to set
	 */
	public void setNameHash(byte nameHash) {
		this.nameHash = nameHash;
	}

	/**
	 * Gets the name hash.
	 * @return the nameHash
	 */
	public byte getNameHash() {
		return nameHash;
	}

	/**
	 * sets the current session key.
	 * @param sessionKey
	 *            the sessionKey to set
	 */
	public void setSessionKey(long sessionKey) {
		this.sessionKey = sessionKey;
	}

	/**
	 * Gets the session key.
	 * @return the sessionKey
	 */
	public long getSessionKey() {
		return sessionKey;
	}

	/**
	 * Sets the player's display mode.
	 * @param mode
	 *            the screenSizeMode to set
	 */
	public void setScreenSizeMode(int mode) {
		this.screenSizeMode = mode;
	}

	/**
	 * Gets the player's display mode.
	 * @return the screenSizeMode
	 */
	public int getScreenSizeMode() {
		return screenSizeMode;
	}

	/**
	 * Sets the player.
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Gets the player.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the is in lobby flag.
	 * @param inLobby
	 *            if the session is in the lobby.
	 */
	public void setInLobby(boolean inLobby) {
		this.inLobby = inLobby;
	}

	/**
	 * If the player is in the lobby.
	 * @return the lobby boolean.
	 */
	public boolean isInLobby() {
		return inLobby;
	}

	/**
	 * @return the screenSizeX
	 */
	public int getScreenSizeX() {
		return screenSizeX;
	}

	/**
	 * @param screenSizeX the screenSizeX to set
	 */
	public void setScreenSizeX(int screenSizeX) {
		this.screenSizeX = screenSizeX;
	}

	/**
	 * @return the screenSizeY
	 */
	public int getScreenSizeY() {
		return screenSizeY;
	}

	/**
	 * @param screenSizeY the screenSizeY to set
	 */
	public void setScreenSizeY(int screenSizeY) {
		this.screenSizeY = screenSizeY;
	}

	/**
	 * @return the displayMode
	 */
	public int getDisplayMode() {
		return displayMode;
	}

	/**
	 * @param displayMode the displayMode to set
	 */
	public void setDisplayMode(int displayMode) {
		this.displayMode = displayMode;
	}
}