package com.yp.java.netty_demo.netty.private_protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;



public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage nettyMsg = (NettyMessage) msg;
		if(nettyMsg!=null&&nettyMsg.getHeader()!=null){
			if(nettyMsg.getHeader().getType()==MessageType.LOGIN_RESP_SUCCESS){
				ctx.executor().scheduleWithFixedDelay(new HeartBeatTask(ctx), 0, 5, TimeUnit.SECONDS);
			}else if(nettyMsg.getHeader().getType()==MessageType.HEARBEAR_RESP){
				System.out.println("client recived heart beat msg :"+nettyMsg);
			}else{
				ctx.fireChannelRead(msg);
			}
		}
	}
	
	private class HeartBeatTask implements Runnable{
		private final ChannelHandlerContext ctx;
		private NettyMessage hearBeatMsg;
		public HeartBeatTask(ChannelHandlerContext ctx){
			this.ctx = ctx;
			this.hearBeatMsg = buildHearBeatMsg();
		}
		@Override
		public void run() {
			System.out.println("client send heart beat msg    :"+hearBeatMsg);
			ctx.writeAndFlush(hearBeatMsg);
		}
		
		private NettyMessage buildHearBeatMsg(){
			NettyMessage hearBeatMsg = new NettyMessage();
			hearBeatMsg.setHeader(new Header());
			hearBeatMsg.getHeader().setType(MessageType.HEARTBEAT_REQ);
			return hearBeatMsg;
		}
	}
}
