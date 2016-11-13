package com.yp.java.netty_demo.netty.private_protocol;


import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class LoginAuthReqHandler extends ChannelHandlerAdapter{

	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		NettyMessage nettyMsg = buildLoginReq();
		System.out.println("client send login auth request to server:"+nettyMsg);
		ctx.writeAndFlush(buildLoginReq());
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage nettyMsg = (NettyMessage)msg;
		if(nettyMsg!=null&&nettyMsg.getHeader()!=null){
			if(nettyMsg.getHeader().getType()==MessageType.LOGIN_RESP_SUCCESS
					||nettyMsg.getHeader().getType()==MessageType.LOGIN_RESP_FAILT){
				System.out.println("client received login auth response from server:"+nettyMsg);
			}
			ctx.fireChannelRead(msg);
		}
	}

	private NettyMessage buildLoginReq() {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOIGN_REQ);
		message.setHeader(header);
		message.setBody("It is request");
		return message;
	}
	
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
}
