package com.yp.java.netty_demo.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

public class AsyncTimeClientHandler implements CompletionHandler<Void, AsyncTimeClientHandler>,Runnable{
	private Logger logger=Logger.getLogger(this.getClass());
	private String host;
	private int port;
	private AsynchronousSocketChannel client;
	private CountDownLatch latch;
	
	public AsyncTimeClientHandler(String host, int port) {
		// TODO Auto-generated constructor stub
		this.port=port;
		this.host=host;
		
		try {
		    //产生client
			client=AsynchronousSocketChannel.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		// TODO Auto-generated method stub
		latch=new CountDownLatch(1);
		client.connect(new InetSocketAddress(host,port), this, this);
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void completed(Void result, AsyncTimeClientHandler attachment) {
		// TODO Auto-generated method stub
		byte[] req="QUERY TIME ORDER".getBytes();
		ByteBuffer writeBuffer=ByteBuffer.allocate(req.length);
		writeBuffer.put(req);
		writeBuffer.flip();
		client.write(writeBuffer, writeBuffer, new CompletionHandler<Integer,ByteBuffer>() {

			public void completed(Integer result, ByteBuffer buffer) {
				// TODO Auto-generated method stub
				if(buffer.hasRemaining()){
					client.write(buffer, buffer, this);
				}else{
					ByteBuffer readBuffer=ByteBuffer.allocate(1024);
					client.read(readBuffer, readBuffer,new CompletionHandler<Integer,ByteBuffer>() {

						public void completed(Integer result, ByteBuffer buffer) {
							// TODO Auto-generated method stub
							buffer.flip();
							byte[] bytes=new byte[buffer.remaining()];
							
							buffer.get(bytes);
							try {
								String body=new String(bytes,"UTF-8");
								logger.info("Now is :"+body);
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							latch.countDown();
						}

						public void failed(Throwable exc, ByteBuffer attachment) {
							// TODO Auto-generated method stub
						    exc.printStackTrace();
							try {
								client.close();
								latch.countDown();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}
				
			}

			public void failed(Throwable exc, ByteBuffer attachment) {
			    exc.printStackTrace();
				// TODO Auto-generated method stub
				try {
					client.close();
					latch.countDown();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		
	}

	public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
		// TODO Auto-generated method stub
	    exc.printStackTrace();
		try {
			client.close();
			latch.countDown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
