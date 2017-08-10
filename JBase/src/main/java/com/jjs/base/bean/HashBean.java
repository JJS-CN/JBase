package com.jjs.base.bean;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 说明：如果url有值，将直接获取map中数据执行http请求方法
 * Created by aa on 2017/8/10.
 */

public class HashBean implements Serializable {
    private HashMap<String, Object> hashMap;
    private int requestCode;
    private String url;//根据url有无，P层执行不同方法
    private boolean hasHeader;//控制是否以header请求
    private boolean isGET;//是否为get请求
    private boolean showLoading;//是否显示loading窗

    public HashBean(int requestCode) {
        this.requestCode = requestCode;
        this.hashMap = new HashMap();
    }

    public int getRequestCode() {
        return requestCode;
    }

    public HashMap getHashMap() {
        return hashMap;
    }

    /**
     * 如果url有值，将直接获取map中数据执行http请求方法
     * 否则会根据code查询对应注解方法调用
     */
    public HashBean setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public boolean isHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public boolean isGET() {
        return isGET;
    }

    public void setGET(boolean GET) {
        isGET = GET;
    }

    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
    }

    public HashBean put(String key, Object value) {
        hashMap.put(key, value);
        return this;
    }

    public Object getValue(String key) {
        return hashMap.get(key);
    }
}
