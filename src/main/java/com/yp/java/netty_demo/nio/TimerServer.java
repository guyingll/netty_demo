package com.yp.java.netty_demo.nio;


public class TimerServer {
    public static void main(String[] args) {
        int port=8080;
        if(args!=null&&args.length>0){
            try{
                port=Integer.parseInt(args[0]);
            }catch(Exception e){
            }
        }
        //多路复用类(轮询多路复用器Selector)
        MultiplexerTimeServer timeServer =new MultiplexerTimeServer(port);
        
        //
        new Thread(timeServer,"NIO-MultiplexerTimeServer-001").start();
    }
}
