package com.yp.java.netty_demo.netty.linebaseframeworkdecoder;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeClient {
        public void connect(int port,String host) throws InterruptedException {
             EventLoopGroup group = new NioEventLoopGroup();
             try{
                 Bootstrap b=new Bootstrap();
                 b.group(group).channel(NioSocketChannel.class)
                 .option(ChannelOption.TCP_NODELAY, true)
                 .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sch) throws Exception {
                        sch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        sch.pipeline().addLast(new StringDecoder());
                        sch.pipeline().addLast(new TimeClientHandler());
                    }});
                 ChannelFuture f=b.connect(host,port).sync();
                 f.channel().closeFuture().sync();
             }finally{
                 group.shutdownGracefully();
             }
        }
        
        public static void main(String[] args) throws InterruptedException {
            int port=8082;
            if(args!=null&&args.length>0){
                port=Integer.parseInt(args[0]);
            }
            new TimeClient().connect(port, "127.0.0.1");
        }
}
