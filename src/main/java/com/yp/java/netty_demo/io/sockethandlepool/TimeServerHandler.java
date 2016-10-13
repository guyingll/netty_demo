package com.yp.java.netty_demo.io.sockethandlepool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class TimeServerHandler implements Runnable {
    private Socket socket;
    
    public TimeServerHandler(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        try(BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            try(PrintWriter out=new PrintWriter(this.socket.getOutputStream(),true)){
                String time=null;
                String body=null;
                while(true){
                    body=in.readLine();
                    if(body==null){
                        break;
                    }
                    System.out.println("The time server receive the order:"+body);
                    time="query time".equals(body)?new Date().toString():"bad order";
                    out.println(time);
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            if(this.socket!=null){
                try {
                    this.socket.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }

    }

}
