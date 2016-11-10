package com.yp.java.netty_demo.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;

public class WebSocketServer {
    private void run(int port) throws InterruptedException {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try{
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sch) throws Exception {
                    sch.pipeline().addLast("http-codec",new HttpServerCodec())
                    .addLast("aggregator",new HttpObjectAggregator(65536))
                    .addLast("http-chunked", new ChunkedWriteHandler())
//                    .addLast("wssch",new WebSocketServerCompressionHandler())
                    .addLast("handler",new WebSocketServerHandler());
                }
            });
            Channel channel = b.bind(new InetSocketAddress(port)).sync().channel();
            System.out.println("ws server is open in port :"+port);
            channel.closeFuture().sync();
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        int port=8080;
        new WebSocketServer().run(port);
    }
}
