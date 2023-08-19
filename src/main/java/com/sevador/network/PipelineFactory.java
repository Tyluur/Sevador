package com.sevador.network;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;

import com.sevador.network.handshake.HandshakeDecoder;
import com.sevador.network.message.RS2GameEncoder;

/**
 * Our pipeline factory.
 * 
 * @author Jefferson
 *
 */
public class PipelineFactory implements ChannelPipelineFactory {
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = new DefaultChannelPipeline();
		pipeline.addLast("decoder", new HandshakeDecoder());
		pipeline.addLast("encoder", new RS2GameEncoder());
		pipeline.addLast("handler", new ChannelHandler());
		return pipeline;
	}

}