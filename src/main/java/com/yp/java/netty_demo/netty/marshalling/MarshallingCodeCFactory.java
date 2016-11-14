package com.yp.java.netty_demo.netty.marshalling;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

public class MarshallingCodeCFactory {
    public static NettyMarshallingDecoder buildMarshallingDecoder() {  
        final MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");  
        final MarshallingConfiguration configuration = new MarshallingConfiguration();  
        configuration.setVersion(5);  
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(factory, configuration);  
        NettyMarshallingDecoder decoder = new NettyMarshallingDecoder(provider, 1024);  
        return decoder;  
    }  
  
    public static NettyMarshallingEncoder buildMarshallingEncoder() {  
        final MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");  
        final MarshallingConfiguration configuration = new MarshallingConfiguration();  
        configuration.setVersion(5);  
        MarshallerProvider provider = new DefaultMarshallerProvider(factory, configuration);  
        NettyMarshallingEncoder encoder = new NettyMarshallingEncoder(provider);  
        return encoder;  
    }  
}
