package com.yp.java.netty_demo.netty.http_xml;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

public class HttpXmlClient {
    private void connect(int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();  
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("http-decoder", new HttpResponseDecoder())
                    .addLast("http-aggregator", new HttpObjectAggregator(65536))
                    .addLast("xml-decoder", new HttpXmlResponseDecoder(Order.class, true))
                    .addLast("http-encoder", new HttpRequestEncoder())
                    .addLast("xml-encoder", new HttpXmlRequestEncoder())
                    .addLast("xmlClientHandler", new HttpXmlClientHandle());
                }
            });
            ChannelFuture f=b.connect(new InetSocketAddress(port)).sync();
            
            //等待客戶端链路退出
            f.channel().closeFuture().sync();
        }finally{
            group.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        new HttpXmlClient().connect(port);
    }
}
