package com.yp.java.netty_demo.netty.java_serializable;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SubReqClientHandler extends ChannelHandlerAdapter {

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
        SubscribeReq req = new SubscribeReq();
        req.setAddress("南方公园");
        req.setPhoneName("13100004444");
        req.setSubReqId(i);
        req.setProductName("权威指南");
        req.setUserName("zhangsan");
        return req;
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
