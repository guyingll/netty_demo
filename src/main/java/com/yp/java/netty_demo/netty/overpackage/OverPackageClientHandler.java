package com.yp.java.netty_demo.netty.overpackage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class OverPackageClientHandler extends ChannelHandlerAdapter{
    
    private int counter;
    
    private byte[] req;
    
    public OverPackageClientHandler() {
        req=("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
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
        ByteBuf buf=(ByteBuf) msg;
        byte[] req=new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body=new String(req,"UTF-8");
        System.out.println("Now is : "+body+",the counter is :"+ ++counter);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
