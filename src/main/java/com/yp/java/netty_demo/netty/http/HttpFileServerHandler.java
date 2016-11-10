package com.yp.java.netty_demo.netty.http;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;


public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
    private final String url;
    
    public HttpFileServerHandler(String url) {
        this.url=url;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if(!request.decoderResult().isSuccess()){
            sendError(ctx,HttpResponseStatus.BAD_REQUEST);
            return;
        }
        
        if(request.method()!=HttpMethod.GET){
            sendError(ctx,HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        
        final String uri=request.uri();
        final String path=sanitizeUri(uri);
        if(path==null){
            sendError(ctx,HttpResponseStatus.FORBIDDEN);
            return;
        }
        
        File f=new File(path);
        
        if(f.isHidden()||!f.exists()){
            sendError(ctx,HttpResponseStatus.NOT_FOUND);
            return;
        }
        
        if(f.isDirectory()){
            if(uri.endsWith("/")){
                sendListing(ctx,f);
            }
            else{
                sendRedirect(ctx,uri+"/");
            }
            return ;
        }
        
        if(!f.isFile()){
            sendError(ctx,HttpResponseStatus.FORBIDDEN);
            return;
        }
        
        RandomAccessFile randomAccessFile=null;
        try{
            randomAccessFile=new RandomAccessFile(f,"r");
        }catch(Exception e){
            sendError(ctx,HttpResponseStatus.FORBIDDEN);
            return;
        }
        
        long length=randomAccessFile.length();
        
        //注意传输的response类型
        HttpResponse res=new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpUtil.setContentLength(res,length);
        setContentTypeHeader(res,f);
        if(HttpUtil.isKeepAlive(request)){
            res.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(res);
        
        
        ChannelFuture sendFileFuture;
        
        sendFileFuture=ctx.write(new ChunkedFile(randomAccessFile,0,length,8192), ctx.newProgressivePromise());
        
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            
            @Override
            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                System.out.println("Transfer complete");
            }
            
            @Override
            public void operationProgressed(ChannelProgressiveFuture cpf, long progress, long total) throws Exception {
                if(total<0){
                    System.err.println("Transfer progress :"+progress);
                }else{
                    System.err.println("Transfer progress :"+progress+"/"+total);
                }
                
            }
        });
        
        
        ChannelFuture lastContentFuture=ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        
        if(!HttpUtil.isKeepAlive(request)){
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if(ctx.channel().isActive()){
            sendError(ctx,HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private static final Pattern INSECURE_URI=Pattern.compile(".*[<>&\"].*");
    
    private String sanitizeUri(String uri){
        try {
            uri=URLDecoder.decode(uri,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri=URLDecoder.decode(uri,"ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }
        
        if(!uri.startsWith(url)){
            return null;
        }
        
        if(!uri.startsWith("/")){
            return null;
        }
        
        uri=uri.replace('/', File.separatorChar);
        
        if(uri.contains(File.separator+'.')
                ||uri.contains('.'+File.separator)
                ||uri.startsWith(".")
                ||uri.endsWith(".")
                ||INSECURE_URI.matcher(uri).matches()){
            return null;
        }
        return System.getProperty("user.dir")+File.separator+uri;
    }
    
    private static final Pattern ALLOWED_FILE_NAME=Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");
    
    private static void sendListing(ChannelHandlerContext ctx, File dir){
        FullHttpResponse res= new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        res.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html;charset=UTF-8");
        StringBuilder sb=new StringBuilder();
        String dirPath=dir.getPath();
        sb.append("<!DOCTYPE html>\r\n");
        sb.append("<html><head><title>");
        sb.append(dirPath);
        sb.append(" 目录：");
        sb.append("</title></head><body>\r\n");
        sb.append("<h3>");
        sb.append(dirPath).append(" 目录：");
        sb.append("</h3>\r\n");
        sb.append("<ul>");
        sb.append("<li>链接: <a href=\"../\">..</a></li>\r\n");
        for(File f:dir.listFiles()){
            if(f.isHidden()||!f.canRead()){
                continue;
            }
            
            String name =f.getName();
            if(!ALLOWED_FILE_NAME.matcher(name).matches()){
                continue;
            }
            
            sb.append("<li>链接：<a href=\"");
            sb.append(name);
            sb.append("\">");
            sb.append(name);
            sb.append("</a></li>\r\n");
        }
        sb.append("</ul></body></html>\r\n");
        
        ByteBuf buffer=Unpooled.copiedBuffer(sb,CharsetUtil.UTF_8);
        res.content().writeBytes(buffer);
        buffer.release();
        ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
    }
    
    private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
        FullHttpResponse res=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.FOUND);     
        res.headers().set(HttpHeaderNames.LOCATION,newUri);
        ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
    }
    
    private static void sendError(ChannelHandlerContext ctx,HttpResponseStatus status) {
        FullHttpResponse res=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,Unpooled.copiedBuffer("Failure: "+status.toString()+"\r\n",CharsetUtil.UTF_8));
        res.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=UTF-8");
        ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
    } 
    
    private void setContentTypeHeader(HttpResponse res, File f) {
        MimetypesFileTypeMap mimeTypesMap=new MimetypesFileTypeMap();
        res.headers().set(HttpHeaderNames.CONTENT_TYPE,mimeTypesMap.getContentType(f.getPath()));
    }
}