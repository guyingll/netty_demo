package com.yp.java.netty_demo.netty.private_protocol;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage nettyMsg = (NettyMessage) msg;
		if(nettyMsg!=null&&nettyMsg.getHeader()!=null){
			if(nettyMsg.getHeader().getType()==MessageType.HEARTBEAT_REQ){
				System.out.println("server recevied client heart beat msg :"+nettyMsg);
				NettyMessage respMsg = buildHeartBeatRespMsg();
				ctx.writeAndFlush(respMsg);
			}else{
				ctx.fireChannelRead(msg);
			}
		}
	}
	
	private NettyMessage buildHeartBeatRespMsg(){
		NettyMessage msg = new NettyMessage();
		msg.setHeader(new Header());
		msg.getHeader().setType(MessageType.HEARBEAR_RESP);
		return msg;
	}
}
