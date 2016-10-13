package com.yp.netty_demos.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Set;


public class MultiplexerTimeServer implements Runnable {

    //多路复用器
    private Selector selector;
    
    private ServerSocketChannel  servChannel;
    
    private volatile boolean stop;
    
    /**
     * 初始化多路复用器、绑定监听端口
     * @param port
     */
    public MultiplexerTimeServer(int port) {
        try {
            selector=Selector.open();
            servChannel=ServerSocketChannel.open();
            servChannel.configureBlocking(false);
            servChannel.socket().bind(new InetSocketAddress(port),1024);
            //将ServerSocketChannel 注册到 Selector
            servChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port："+port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public void stop(){
        this.stop=true;
    }

    @Override
    public void run() {
        while(!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectedKeys=selector.selectedKeys();
                for(SelectionKey key:selectedKeys){
                    try{
                        handleInput(key);
                    }catch(Exception e){
                        if(key!=null){
                            key.cancel();
                            if(key.channel()!=null)
                                key.channel().close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        if(selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if(key.isValid()){
            //处理新接入的请求消息
            if(key.isAcceptable()){
                ServerSocketChannel ssc= (ServerSocketChannel) key.channel();
                SocketChannel sc=ssc.accept();
                sc.configureBlocking(true);
                sc.register(selector, SelectionKey.OP_READ);
            }
            
            if(key.isReadable()){
                SocketChannel sc= (SocketChannel) key.channel();
                ByteBuffer readBuffer=ByteBuffer.allocate(1024);
                int readBytes=sc.read(readBuffer);
                if(readBytes>0){
                    readBuffer.flip();
                    byte[] bytes=new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body=new String(bytes,"UTF-8");
                    System.out.println("The time server receive the order:"+body);
                    String time="query time".equals(body)?new Date().toString():"bad order";
                    doWrite(sc, time);
                }else if(readBytes<0){
                    //对端链路关闭
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doWrite(SocketChannel sc,String response) throws IOException {
        if(response!=null&&response.trim().length()>0){
            byte[] bytes=response.getBytes();
            ByteBuffer writeBuffer=ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            sc.write(writeBuffer);
        }
        
    }
}
