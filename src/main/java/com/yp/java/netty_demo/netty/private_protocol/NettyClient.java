package com.yp.java.netty_demo.netty.private_protocol;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class NettyClient {
	private EventLoopGroup group = new NioEventLoopGroup();
	private Executor executor = Executors.newScheduledThreadPool(1);
	public void connect(String host, int port) throws Exception{
		try{
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group);
			bootstrap.option(ChannelOption.TCP_NODELAY, true);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.handler(new ChannelInitializer<SocketChannel>(){
				@Override
				protected void initChannel(SocketChannel ch)
						throws Exception {
					ch.pipeline()
						.addLast(new NettyMessageDecoder(1024*1024, 4, 4, -8, 0))
						.addLast(new NettyMessageEncoder())
						.addLast(new ReadTimeoutHandler(50))
						.addLast(new LoginAuthReqHandler())
						.addLast(new HeartBeatReqHandler());
				}
			});
			System.out.println("client start");
			ChannelFuture future = bootstrap.connect(NettyConfig.HOST, NettyConfig.PORT).sync();
			future.channel().closeFuture().sync();
		}finally{
			executor.execute(new Runnable(){
				@Override
				public void run() {
					try {
						TimeUnit.SECONDS.sleep(5);
						try {
							connect(NettyConfig.HOST, NettyConfig.PORT);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			});
		}
	}
	public static void main(String[] args) {
		try {
			new NettyClient().connect(NettyConfig.HOST, NettyConfig.PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
