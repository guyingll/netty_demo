package com.yp.java.netty_demo.netty.marshalling;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class SubReqServer {
    public void bind(int port) {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try {
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
            .handler(new LoggingHandler(LogLevel.INFO))
            .childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                    
                    .addLast(MarshallingCodeCFactory.buildMarshallingEncoder())
                    .addLast(MarshallingCodeCFactory.buildMarshallingDecoder())
                    .addLast(new ServerChannelHandler());
                }
                
            });
            ChannelFuture future = b.bind(port).sync();
            System.out.println("server open");
            future.channel().closeFuture().sync();
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        
    }
    
    public static void main(String[] args) {
        int port=8080;
        new SubReqServer().bind(port);
    }
}
