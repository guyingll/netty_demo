package com.yp.java.netty_demo.netty.simple;


import com.yp.java.netty_demo.netty.overpackage.OverPackageServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
	public void bind(int port) throws InterruptedException {
		EventLoopGroup bossGroup=new NioEventLoopGroup();
		EventLoopGroup workerGroup=new NioEventLoopGroup();
		try{
			ServerBootstrap b=new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChildchannelHandler());
			ChannelFuture f = b.bind(port).sync();
			System.out.println("server start");
			
			f.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	private class ChildchannelHandler extends ChannelInitializer<SocketChannel>{
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
//			arg0.pipeline().addLast(new TimeServerHandler());
		    arg0.pipeline().addLast(new OverPackageServerHandler());
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		int port = 8082;
		if(args!=null && args.length>0){
			port=Integer.parseInt(args[0]);
		}
		new TimeServer().bind(port);
	}
}
