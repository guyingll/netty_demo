package com.yp.java.netty_demo.aio.server;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

public class AsyncTimeServerHandler implements Runnable{
	
	private Logger logger=Logger.getLogger(this.getClass());
	
	@SuppressWarnings("unused")
	private int port;
	
	CountDownLatch latch;
	
	AsynchronousServerSocketChannel asynchronousServerSocketChannel;
	
	public AsyncTimeServerHandler(int port) {
		this.port=port;
		try{
			asynchronousServerSocketChannel=AsynchronousServerSocketChannel.open();
			asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
			
			logger.info("The time server is start in port: "+port);
		}catch(Exception e){
			logger.error("server start error");
		}
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		latch=new CountDownLatch(1);
		doAccept();
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	private void doAccept() {
		asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
	}
	
}
