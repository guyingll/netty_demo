package com.yp.java.netty_demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/** 
* 获取经纬度
* 
* @author jueyue 返回格式：Map<String,Object> map map.put("status", 
* reader.nextString());//状态 map.put("result", list);//查询结果 
* list<map<String,String>> 
* 密钥:f247cdb592eb43ebac6ccd27f796e2d2 
*/ 
public class GetLatAndLngByBaidu { 
	
	/** 
	* @param addr 
	* 查询的地址 
	* @return 
	* @throws IOException 
	*/ 
	public Object[] getCoordinate(String addr) throws IOException { 
		String lng = null;//经度
		String lat = null;//纬度
		String address = null; 
		try { 
			address = java.net.URLEncoder.encode(addr, "UTF-8"); 
		}catch (UnsupportedEncodingException e1) { 
			e1.printStackTrace(); 
		} 
		String key = "U2HbWBY2f5PlPboQAekvU7FrhowWB7tf"; 
		String url = String .format("http://api.map.baidu.com/geocoder/v2/?address=%s&output=json&ak=%s", address, key); 
		URL myURL = null; 
		URLConnection httpsConn = null; 
		try { 
			myURL = new URL(url); 
		} catch (MalformedURLException e) { 
			e.printStackTrace(); 
		} 
		InputStreamReader insr = null;
		BufferedReader br = null;
		try { 
			httpsConn = (URLConnection) myURL.openConnection();// 不使用代理 
			if (httpsConn != null) { 
				insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8"); 
				br = new BufferedReader(insr); 
				String data = null; 
				int count = 1;
				while((data= br.readLine())!=null){ 
				    System.out.println(data);
//					if(count==5){
//						lng = (String)data.subSequence(data.indexOf(":")+1, data.indexOf(","));//经度
//						count++;
//					}else if(count==6){
//						lat = data.substring(data.indexOf(":")+1);//纬度
//						count++;
//					}else{
//						count++;
//					}
				} 
			} 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} finally {
			if(insr!=null){
				insr.close();
			}
			if(br!=null){
				br.close();
			}
		}
		return new Object[]{lng,lat}; 
	} 

	   public Object[] getAddress(String addr) throws IOException { 
	        String lng = null;//经度
	        String lat = null;//纬度
	        String address = null; 
	        try { 
	            address = java.net.URLEncoder.encode(addr, "UTF-8"); 
	        }catch (UnsupportedEncodingException e1) { 
	            e1.printStackTrace(); 
	        } 
	        String key = "U2HbWBY2f5PlPboQAekvU7FrhowWB7tf"; 
	        String url = String .format("http://api.map.baidu.com/geocoder/v2/?location=%s&output=json&pois=1&ak=%s", address, key); 
	        URL myURL = null; 
	        URLConnection httpsConn = null; 
	        try { 
	            myURL = new URL(url); 
	        } catch (MalformedURLException e) { 
	            e.printStackTrace(); 
	        } 
	        InputStreamReader insr = null;
	        BufferedReader br = null;
	        try { 
	            httpsConn = (URLConnection) myURL.openConnection();// 不使用代理 
	            if (httpsConn != null) { 
	                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8"); 
	                br = new BufferedReader(insr); 
	                String data = null; 
	                String dataStr=null;
	                int count = 1;
	                while((data= br.readLine())!=null){ 
	                    dataStr+=data;
	                } 
	                System.out.println(dataStr);
	            } 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        } finally {
	            if(insr!=null){
	                insr.close();
	            }
	            if(br!=null){
	                br.close();
	            }
	        }
	        return new Object[]{lng,lat}; 
	    } 

	public static void main(String[] args) throws IOException {
		GetLatAndLngByBaidu getLatAndLngByBaidu = new GetLatAndLngByBaidu();
		Object[] o = getLatAndLngByBaidu.getCoordinate("奉贤区");
		getLatAndLngByBaidu.getAddress("32.01327292672134000000,120.85691208869823000000");
		
	}

}