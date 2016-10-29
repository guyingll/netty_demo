package com.yp.java.netty_demo.aio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

import org.apache.log4j.Logger;

public class ReadCompletionHandler implements
		CompletionHandler<Integer,  ByteBuffer> {
	private Logger logger=Logger.getLogger(this.getClass());
	
	private AsynchronousSocketChannel channel;
	
	public ReadCompletionHandler(AsynchronousSocketChannel result){
		if(channel==null)
			this.channel=result;
	}

	
	
	
	/* (non-Javadoc)
	 * @see java.nio.channels.CompletionHandler#completed(java.lang.Object, java.lang.Object)
	 */
	public void completed(Integer result, ByteBuffer attachment) {
		// TODO Auto-generated method stub
	    //处理业务
		attachment.flip();
		byte[] body=new byte[attachment.remaining()];
		attachment.get(body);
		try{
			String req=new String(body,"UTF-8");
			logger.info("The time server receive order : "+req);
			String currentTime="QUERY TIME ORDER".equalsIgnoreCase(req)?new Date().toString():"BAD ORDER";
			
			//异步写入
			doWrite(currentTime);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private void doWrite(String currentTime) {
		// TODO Auto-generated method stub
		if(currentTime!=null && currentTime.trim().length()>0){
			byte[] bytes=currentTime.getBytes();
			ByteBuffer writeBuffer=ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

				public void completed(Integer result, ByteBuffer buffer) {
					// TODO Auto-generated method stub
				    //迭代写入
					if(buffer.hasRemaining())
						channel.write(buffer, buffer, this);
				}

				public void failed(Throwable exc, ByteBuffer attachment) {
					// TODO Auto-generated method stub
					try {
						channel.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			});
		}
	}




	public void failed(Throwable exc, ByteBuffer attachment) {
		// TODO Auto-generated method stub
		try {
			this.channel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
