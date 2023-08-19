package com.sevador.network;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.sevador.Main;
import com.sevador.game.node.player.Player;
import com.sevador.utility.Constants;

/**
 * This channel handler is used for handling new connections, disconnections and
 * received messages.
 * 
 * @author `Argo
 * @author Dementhium development team
 *
 */
public class ChannelHandler extends SimpleChannelHandler {

	/**
	 * The server bootstrap instance.
	 */
	private static final ServerBootstrap serverBootstrap = new ServerBootstrap();

	/**
	 * The IOSession instance.
	 */
	private IOSession session;

	/**
	 * Prepares the serverBootsTrap, and binds to port {@code 43594}.
	 */
	public static boolean init() {
		Executor e = Executors.newCachedThreadPool();
		serverBootstrap.setFactory(new NioServerSocketChannelFactory(e, e));
		serverBootstrap.setPipelineFactory(new PipelineFactory());
		try {
			serverBootstrap.bind(new InetSocketAddress(Constants.PORT));
		} catch (ChannelException c) {
			System.out.println(new StringBuilder("Failed to bind port ").append(Constants.PORT).append("."));
			return false;
		}
		System.out.println(new StringBuilder("Server port bound to - ").append(Constants.PORT).append("."));
		return true;
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		session = new IOSession(e.getChannel());
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		try {
			if (session != null) {
				Player player = session.getPlayer();
				if (player != null) {
					System.err.println("Channel disconnected; removing player " + player.getCredentials().getDisplayName() + " from node worker.");
					Main.getNodeWorker().remove(player);
				}
				session.setPlayer(null);
				session = null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent evt) {
		Channel channel = evt.getChannel();
		Main.getLogger().log(Level.WARNING, "Exception caught, closing in session for " + session.getPlayer().getCredentials() != null ? session.getPlayer().getCredentials().getDisplayName() : "non-player session", evt.getCause());
		if (channel.isConnected())
			channel.close();
	}

	@Override
	public final void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		Channel channel = e.getChannel();
		if (!channel.isConnected() || !(e.getMessage() instanceof Message)) {
			return;
		}
		((Message) e.getMessage()).execute(this, ctx, e, session);
	}

	/**
	 * Sets the current IOSession.
	 * 
	 * @param session
	 *            The IOSession.
	 */
	public void setSession(IOSession session) {
		this.session = session;
	}
}
