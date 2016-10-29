package com.yp.java.netty_demo.netty.delimiterbasedframedecoder;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeServerHandler extends ChannelHandlerAdapter {
    private int counter;
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
	    String body=(String)msg;
		System.out.println("The time server receive order :" + body+" ;the counter is "+ ++counter);
		String currentTime="QUERY TIME ORDER".equalsIgnoreCase(body)?new Date().toString():"BAD ORDER";
		currentTime+="$_";
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.write(resp);
	}
	
	public void channelReadComplete(ChannelHandlerContext ctx){
		ctx.flush();
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
	    cause.printStackTrace();
		ctx.close();
	}
}
