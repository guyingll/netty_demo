package com.yp.java.netty_demo.netty.java_serializable;

import java.io.Serializable;

public class SubscribeReq implements Serializable{

    /**
     */
    private static final long serialVersionUID = 1L;
    
    private int subReqId;
    
    private String userName;
    
    private String productName;
    
    private String phoneName;
    private String address;
    public int getSubReqId() {
        return subReqId;
    }
    public void setSubReqId(int subReqId) {
        this.subReqId = subReqId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getPhoneName() {
        return phoneName;
    }
    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    @Override
    public String toString() {
        return "SubscribeReq [subReqId=" + subReqId + ", userName=" + userName + ", productName=" + productName
                + ", phoneName=" + phoneName + ", address=" + address + "]";
    }
    
}
