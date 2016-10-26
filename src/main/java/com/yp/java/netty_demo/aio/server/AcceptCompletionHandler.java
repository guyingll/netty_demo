package com.yp.java.netty_demo.aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler
		implements
		CompletionHandler<AsynchronousSocketChannel,AsyncTimeServerHandler>{

	/* (non-Javadoc)
	 * @see java.nio.channels.CompletionHandler#completed(java.lang.Object, java.lang.Object)
	 */
	public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
	    //形成请求接受循环
		attachment.asynchronousServerSocketChannel.accept(attachment, this);
		
		// 一个客户端的请求接受成功completed后开辟空间
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		//请求处理
		result.read(buffer, buffer, new ReadCompletionHandler(result));
	}

	public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
		// TODO Auto-generated method stub
	    exc.printStackTrace();
		attachment.latch.countDown();
	}

}
