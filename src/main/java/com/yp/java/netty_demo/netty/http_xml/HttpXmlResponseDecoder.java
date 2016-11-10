package com.yp.java.netty_demo.netty.http_xml;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.util.List;


public class HttpXmlResponseDecoder extends AbstractHttpXmlDecoder<FullHttpResponse> {

    protected HttpXmlResponseDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz, isPrint);
    }
    
    protected HttpXmlResponseDecoder(Class<?> clazz) {
        this(clazz, false);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpResponse msg, List<Object> out) throws Exception {
        HttpXmlResponse request = new HttpXmlResponse(msg, decode0(ctx, msg.content()));  
        // 将请求交给下一个解码器处理  
        out.add(request);  
    }  
  
    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {  
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,  
                Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));  
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");  
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);  
    }
    
}
