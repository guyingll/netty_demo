package com.yp.java.netty_demo;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static boolean checkTelValid(String tel) {
        String regex = "^[1][3,4,5,7,8][0-9]{9}$";
        return tel.matches(regex);
    }
    
    
    public static void main(String[] args) throws IOException {
//        OkHttpClient client = new OkHttpClient();
//
//        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=---011000010111000001101001");
//        RequestBody body = RequestBody.create(mediaType, "-----011000010111000001101001\r\nContent-Disposition: form-data; name=\"fileType\"\r\n\r\njpg\r\n-----011000010111000001101001\r\nContent-Disposition: form-data; name=\"workOrderNo\"\r\n\r\n1111\r\n-----011000010111000001101001\r\nContent-Disposition: form-data; name=\"workOrderTaskId\"\r\n\r\n1111\r\n-----011000010111000001101001\r\nContent-Disposition: form-data; name=\"picture\"; filename=\"[object Object]\"\r\nContent-Type: false\r\n\r\n\r\n-----011000010111000001101001\r\nContent-Disposition: form-data; name=\"picture\"; filename=\"[object Object]\"\r\nContent-Type: false\r\n\r\n\r\n-----011000010111000001101001--");
//        Request request = new Request.Builder()
//          .url("http://192.168.0.109:8082/app_cs/service/upload")
//          .post(body)
//          .addHeader("content-type", "multipart/form-data; boundary=---011000010111000001101001")
//          .addHeader("cache-control", "no-cache")
//          .addHeader("postman-token", "92e74274-70cd-9241-047f-f73181fe7464")
//          .build();
//
//        Response response = client.newCall(request).execute();

    }
}
