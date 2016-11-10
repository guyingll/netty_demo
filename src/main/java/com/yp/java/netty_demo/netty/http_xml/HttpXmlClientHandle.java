package com.yp.java.netty_demo.netty.http_xml;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HttpXmlClientHandle extends SimpleChannelInboundHandler<HttpXmlResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpXmlResponse msg) throws Exception {
        System.out.println("The client receive response of http header is : " + msg.getHttpResponse().headers().names());  
        System.out.println("The client receive response of http body is : " + msg.getResult()); 
    }

    @Override  
    public void channelActive(ChannelHandlerContext ctx) {  
        //向服务端端发送请求消息，HttpXmlRequest包含FullHttpRequest和Order这个了类  
        HttpXmlRequest request = new HttpXmlRequest(null, OrderFactory.create(123));  
        ctx.writeAndFlush(request);  
    }  
  
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {  
        cause.printStackTrace();  
        ctx.close();  
    }  
}
