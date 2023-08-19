package com.sevador.network.ondemand;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import com.sevador.network.ChannelHandler;
import com.sevador.network.IOSession;
import com.sevador.network.Message;

/**
 * Represents an on-demand request.
 * @author Dementhium development team
 * @author Emperor
 *
 */
public class OnDemandRequest implements Message {

	/**
	 * The priority of this request.
	 */
	private final boolean priority;
	
	/**
	 * The container id.
	 */
	private final int container;
	
	/**
	 * The file id.
	 */
	private final int file;

	/**
	 * Constructs a new {@code OnDemandRequest} {@code Object}.
	 * @param priority The priority.
	 * @param container The container id.
	 * @param file The file id.
	 */
	public OnDemandRequest(boolean priority, int container, int file) {
		this.priority = priority;
		this.container = container;
		this.file = file;
	}

	@Override
	public boolean execute(ChannelHandler ch, ChannelHandlerContext ctx, MessageEvent e, IOSession session) {
		ctx.sendDownstream(e);
		return true;
	}
	
	/**
	 * Gets the priority.
	 * @return {@code True} if the priority is high, {@code false} if normal priority.
	 */
	public boolean isHighPriority() {
		return priority;
	}

	/**
	 * Gets the container id.
	 * @return The container id.
	 */
	public int getContainer() {
		return container;
	}

	/**
	 * Gets the file id.
	 * @return The file id.
	 */
	public int getFile() {
		return file;
	}

}
