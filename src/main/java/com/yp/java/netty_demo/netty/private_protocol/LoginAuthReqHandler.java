package com.yp.java.netty_demo.netty.private_protocol;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter{

    @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		NettyMessage nettyMsg = buildLoginReq();
		System.out.println("client send login auth request to server:"+nettyMsg);
		ctx.writeAndFlush(buildLoginReq());
	}

    @Override
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
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
}
