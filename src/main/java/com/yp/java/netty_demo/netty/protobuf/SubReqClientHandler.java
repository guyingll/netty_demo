package com.yp.java.netty_demo.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.yp.java.netty_demo.netty.protobuf.SubscribeReqProto.SubscribeReq;

public class SubReqClientHandler extends ChannelInboundHandlerAdapter  {

    @Override
    public void channelRead(ChannelHandlerContext paramChannelHandlerContext, Object paramObject) throws Exception {
        System.out.println("response:"+paramObject);
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i=0;i<10;i++){
            ctx.write(subReq(i));
        }
        ctx.flush();
    }

    private SubscribeReq subReq(int i) {
        SubscribeReq.Builder req = SubscribeReq.newBuilder();
        req.addAddress("南方公园");
        req.setSubReqID(i);
        req.setProductName("权威指南");
        req.setUserName("zhangsan");
        return req.build();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext cxt) throws Exception {
        cxt.flush();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
