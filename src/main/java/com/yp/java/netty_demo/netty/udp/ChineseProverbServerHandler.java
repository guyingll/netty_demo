package com.yp.java.netty_demo.netty.udp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ThreadLocalRandom;

public class ChineseProverbServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	private static final String[] DICTIONARY={"功夫深","飞燕","玉壶","光阴","福利"};
	
	private String nextQuote() {
		int quoteId=ThreadLocalRandom.current().nextInt(DICTIONARY.length);
		return DICTIONARY[quoteId];
	}
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet)
			throws Exception {
		String req=packet.content().toString(CharsetUtil.UTF_8);
		System.out.println(req);
		if("查询？".equals(req)){
			ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("结果："+nextQuote(),CharsetUtil.UTF_8), packet.sender()));
		}
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
		cause.printStackTrace();
	}
}
