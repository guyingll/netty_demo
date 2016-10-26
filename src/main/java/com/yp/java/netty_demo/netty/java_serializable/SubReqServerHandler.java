package com.yp.java.netty_demo.netty.java_serializable;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SubReqServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // TODO Auto-generated method stub
        SubscribeReq req=(SubscribeReq)msg;
        if("zhangsan".equalsIgnoreCase(req.getUserName())){
            System.out.println("request is : "+req.toString());
            ctx.write(resp(req.getSubReqId()));
        }
        ctx.flush();
    }
    
    private SubscribeResp resp(int subReqId) {
        SubscribeResp res=new SubscribeResp();
        res.setSubReqId(subReqId);
        res.setRespCode(0);
        res.setDesc("Netty book order success,3 days later,send to the address");
        return res;
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
