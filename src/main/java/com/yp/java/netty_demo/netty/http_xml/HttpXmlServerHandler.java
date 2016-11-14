package com.yp.java.netty_demo.netty.http_xml;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.ArrayList;
import java.util.List;

public class HttpXmlServerHandler extends SimpleChannelInboundHandler<HttpXmlRequest>{

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, HttpXmlRequest xmlRequest) throws Exception {
        HttpRequest request = xmlRequest.getRequest();  
        Order order = (Order) (xmlRequest.getBody());  
        // 输出解码获得的Order对象  
        System.out.println("Http server receive request : " + order);  
        dobusiness(order);  
        System.out.println(order);  
        ChannelFuture future = ctx.writeAndFlush(new HttpXmlResponse(null, order));  
        if (request.headers().get(HttpHeaderNames.CONNECTION).equalsIgnoreCase(HttpHeaderValues.KEEP_ALIVE.toString())) {  
            future.addListener(new GenericFutureListener<Future<? super Void>>() {  
                public void operationComplete(@SuppressWarnings("rawtypes") Future future) throws Exception {  
                    ctx.close();  
                }  
            });  
        }  
    }
    
    private void dobusiness(Order order) {  
        order.getCustomer().setFirstName("狄");  
        order.getCustomer().setLastName("仁杰");  
        List<String> midNames = new ArrayList<String>();  
        midNames.add("李元芳");  
        order.getCustomer().setMiddleNames(midNames);  
        Address address = order.getBillTo();  
        address.setCity("洛阳");  
        address.setCountry("大唐");  
        address.setState("河南道");  
        address.setPostCode("123456");  
        order.setBillTo(address);  
        order.setShipTo(address);  
    }  
    
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
        cause.printStackTrace();  
        // 在链路没有关闭并且出现异常的时候发送给客户端错误信息  
        if (ctx.channel().isActive()) {  
            sendError(ctx, HttpResponseStatus
                    .INTERNAL_SERVER_ERROR);  
        }  
    }  
  
    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {  
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,  
                Unpooled.copiedBuffer("失败: " + status.toString() + "\r\n", CharsetUtil.UTF_8));  
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");  
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);  
    }  


}
