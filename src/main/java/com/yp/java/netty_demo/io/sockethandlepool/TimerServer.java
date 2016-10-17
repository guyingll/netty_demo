package com.yp.java.netty_demo.io.sockethandlepool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimerServer {
    public static void main(String[] args) {
        int port=8080;
        if(args!=null&&args.length>0){
            try{
                port=Integer.parseInt(args[0]);
            }catch(Exception e){
            }
        }
        
        ServerSocket server=null;
        try{
            server=new ServerSocket(port);
            System.out.println("The time server is start in port："+port);
            Socket socket=null;
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50,100);
            while(true){
                socket=server.accept();
                singleExecutor.excute(new TimeServerHandler(socket));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(server!=null){
                System.out.println("The time server close");
                try {
                    server.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }finally{
                    server=null;
                }
            }
        }
        
        
    }
}
