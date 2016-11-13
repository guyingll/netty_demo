package com.yp.java.netty_demo.netty.private_protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;


public class NettyMarshallingDecoder extends MarshallingDecoder{

	public NettyMarshallingDecoder(UnmarshallerProvider provider, int objectMaxSize) {
    	super(provider, objectMaxSize);
    }
    
    public NettyMarshallingDecoder(UnmarshallerProvider provider) {
    	super(provider);
    }
    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        return super.decode(ctx, in);
    }

    @Override
    public ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
        return super.extractFrame(ctx, buffer, index, length);
    }
	
}
