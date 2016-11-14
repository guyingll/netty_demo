package com.yp.java.netty_demo.netty.overpackage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class OverPackageServerHandler extends ChannelInboundHandlerAdapter {
    private int counter;
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
		ByteBuf buf=(ByteBuf) msg;
		byte[] req=new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body=new String(req,"UTF-8").substring(0,req.length-System.getProperty("line.separator").length());
		System.out.println("The time server receive order :" + body+";the counter is "+ ++counter);
		String currentTime="QUERY TIME ORDER".equalsIgnoreCase(body)?new Date().toString():"BAD ORDER";
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.write(resp);
	}
	
	public void channelReadComplete(ChannelHandlerContext ctx){
		ctx.flush();
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
		ctx.close();
	}
}
