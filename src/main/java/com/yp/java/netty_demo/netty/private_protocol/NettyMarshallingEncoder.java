package com.yp.java.netty_demo.netty.private_protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

import org.jboss.marshalling.Marshaller;


public class NettyMarshallingEncoder extends MarshallingEncoder{
	Marshaller marshaller;

	public NettyMarshallingEncoder(MarshallerProvider provider) {
    	super(provider);
    }

	@Override
    public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
       super.encode(ctx, msg, out);
    }
	
}
