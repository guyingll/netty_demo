package com.yp.java.netty_demo.aio.server;

import org.apache.log4j.Logger;

public class TimeServer {
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
		
		AsyncTimeServerHandler timeServer=new AsyncTimeServerHandler(port);
		new Thread(timeServer,"AIO-Handler-001").start();
	}
}
