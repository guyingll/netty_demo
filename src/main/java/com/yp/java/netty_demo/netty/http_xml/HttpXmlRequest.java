package com.yp.java.netty_demo.netty.http_xml;

import io.netty.handler.codec.http.FullHttpRequest;

public class HttpXmlRequest {
    private FullHttpRequest request;  
    private Object body;
    @Override
    public String toString() {
        return "HttpXmlRequest [request=" + request + ", body=" + body + "]";
    }
    public FullHttpRequest getRequest() {
        return request;
    }
    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }
    public Object getBody() {
        return body;
    }
    public void setBody(Object body) {
        this.body = body;
    }
    public HttpXmlRequest(FullHttpRequest request, Object body) {
        this.request = request;
        this.body = body;
    } 
}
