package com.yp.java.netty_demo.netty.http_xml;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.util.List;

public class HttpXmlRequestDecoder extends AbstractHttpXmlDecoder<FullHttpRequest> {

    protected HttpXmlRequestDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz, isPrint);
    }
    

    public HttpXmlRequestDecoder(Class<?> clazz) {  
        this(clazz, false);  
    }  
  

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
     // 返回客户端错误信息  
        if (!msg.decoderResult().isSuccess()) {  
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);  
            return;  
        }  
        HttpXmlRequest request = new HttpXmlRequest(msg, decode0(ctx, msg.content()));  
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
