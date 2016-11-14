package com.yp.java.netty_demo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {
    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\workspace1\\netty-demos\\resource\\test.txt", "rw");
        FileChannel  f=randomAccessFile.getChannel();
        testRead(f);
        testWrite(f);
        f.close();
    }

    private static void testWrite(FileChannel  f) throws IOException {
        ByteBuffer b=ByteBuffer.allocate(64);
        String content="1111111111111111111111  好的";
        b.put(content.getBytes());
        b.flip();;
        f.write(b);
    }

    private static void testRead(FileChannel  f) throws FileNotFoundException, IOException {
        
        ByteBuffer b=ByteBuffer.allocate(64);
        f.read(b);
        
        b.flip();
        
//        Charset charset = Charset.forName("UTF-8");
//        CharsetDecoder decoder = charset.newDecoder();
//        CharBuffer charBuffer = decoder.decode(b);
//        System.out.println(charBuffer.asReadOnlyBuffer().toString());
        
        byte[] bytes=new byte[b.remaining()];
        b.get(bytes);
        System.out.println(new String(bytes));
    }
}   
