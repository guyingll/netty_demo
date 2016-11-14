package com.yp.java.netty_demo.netty.private_protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {
    @Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage message = (NettyMessage) msg;
		System.out.println(msg);
		if(message!=null&&message.getHeader()!=null){
			if(message.getHeader().getType()==MessageType.LOIGN_REQ){
				System.out.println("server recevied login auth msg from client :" + message);
				ctx.writeAndFlush(buildLoginResponse());
			}else{
				ctx.fireChannelRead(msg);
			}
		}
	}

	private NettyMessage buildLoginResponse() {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_RESP_SUCCESS);
		message.setHeader(header);
		return message;
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
		cause.printStackTrace();
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
	    System.out.println("1111111111111111111111-----------------------");
	}
}
