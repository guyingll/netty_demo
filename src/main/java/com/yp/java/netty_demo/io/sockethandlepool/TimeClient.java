package com.yp.java.netty_demo.io.sockethandlepool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeClient {
    public static void main(String[] args) {
        int port=8080;
        try(Socket socket=new Socket("127.0.0.1",port)){
            try(BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()))){
                try(PrintWriter out=new PrintWriter(socket.getOutputStream(),true)){
                    out.println("query time");
                    System.out.println("Send order 2 server successed");
                    String resp=in.readLine();
                    System.out.println("Now is : "+resp);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
