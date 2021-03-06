package com.yp.java.netty_demo.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServer {
    public static final String DEFAULT_URL="/src/main/java";
    public void run(final int port,final String url) throws InterruptedException{
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try{
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup,workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("http-decoder",new HttpRequestDecoder())
                    .addLast("http-aggregator",new HttpObjectAggregator(65536))
                    .addLast("http-encoder",new HttpResponseEncoder())
                    .addLast("http-chunked",new ChunkedWriteHandler())
                    .addLast("fileServerHandler",new HttpFileServerHandler(url));
                }
                
            });
            ChannelFuture f=b.bind("127.0.0.1",port).sync();
            System.out.println("http 文件目錄服務器啓動，网址是http://127.0.0.1:"+port+url);
            f.channel().closeFuture().sync();
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        int port=8080;
        new HttpFileServer().run(port,DEFAULT_URL);
    }
}
