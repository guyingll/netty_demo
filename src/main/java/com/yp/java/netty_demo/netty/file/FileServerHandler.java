package com.yp.java.netty_demo.netty.file;

import java.io.File;
import java.io.RandomAccessFile;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

public class FileServerHandler extends SimpleChannelInboundHandler<String> {

    private static final String CR=System.getProperty("line.separator");
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // TODO Auto-generated method stub
        File file=new File(msg);
        if(file.exists()){
            if(!file.isFile()){
                ctx.writeAndFlush("Not a file :"+file+CR);
                return;
            }
            ctx.write(file+" "+file.length()+CR);
            RandomAccessFile  rf=new RandomAccessFile(msg, "r");
            FileRegion region=new DefaultFileRegion(rf.getChannel(), 0, rf.length());
            ctx.write(region);
            ctx.writeAndFlush(CR);
            rf.close();
        }else{
            ctx.writeAndFlush("File not found: "+file+CR);
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

}
