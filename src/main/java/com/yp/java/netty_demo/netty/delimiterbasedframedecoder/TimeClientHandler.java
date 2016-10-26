package com.yp.java.netty_demo.netty.delimiterbasedframedecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeClientHandler extends ChannelHandlerAdapter{
    
    private int counter;
    
    private byte[] req;
    
    public TimeClientHandler() {
        req=("QUERY TIME ORDER"+"$_").getBytes();
    }
    
    public void channelActive(ChannelHandlerContext ctx){
        ByteBuf message=null;
        for(int i=0;i<100;i++){
            message=Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
        
    }
    
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body=(String)msg;
        System.out.println("Now is : "+body+",the counter is :"+ ++counter);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
