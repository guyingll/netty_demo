package com.yp.java.netty_demo.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.yp.java.netty_demo.netty.protobuf.SubscribeReqProto.SubscribeReq;
import com.yp.java.netty_demo.netty.protobuf.SubscribeRespProto.SubscribeResp;

public class SubReqServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // TODO Auto-generated method stub
        SubscribeReq req=(SubscribeReq)msg;
        if("zhangsan".equalsIgnoreCase(req.getUserName())){
            System.out.println("request is : "+req.toString());
            ctx.write(resp(req.getSubReqID()));
        }
        ctx.flush();
    }
    
    private SubscribeResp resp(int subReqId) {
        SubscribeResp.Builder res=SubscribeResp.newBuilder();
        res.setSubReqID(subReqId);
        res.setRespCode("0");
        res.setDesc("Netty book order success,3 days later,send to the address");
        return res.build();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // TODO Auto-generated method stub
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }
}
