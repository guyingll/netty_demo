package com.yp.java.netty_demo.aio;

import org.apache.log4j.Logger;

import com.yp.java.netty_demo.aio.server.TimeServer;

public class TimeClient {
	private static Logger logger=Logger.getLogger(TimeServer.class);
	public static void main(String[] args) {
		int port=8080;
		if(args!=null&&args.length>0){
			try{
				Integer.parseInt(args[0]);
			}catch(Exception e){
				logger.info("use default port : "+port);
			}
		}
		
		AsyncTimeClientHandler timeClient=new AsyncTimeClientHandler("localhost",port);
		new Thread(timeClient,"AIO-Handler-001").start();
	}
}
