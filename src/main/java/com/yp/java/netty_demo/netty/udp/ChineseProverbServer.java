package com.yp.java.netty_demo.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class ChineseProverbServer {
	private void run(int port) throws InterruptedException {
		EventLoopGroup group=new NioEventLoopGroup();
		try{
			Bootstrap b=new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
			.option(ChannelOption.SO_BROADCAST, true)
			.handler(new ChineseProverbServerHandler());
			
			b.bind(port).sync().channel().closeFuture().await();
		}finally{
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		int port=8080;
		new ChineseProverbServer().run(port);
	}
}
