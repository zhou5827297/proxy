package com.proxy.model;

import java.io.Serializable;

/*
 *代理bean
 */
public class ProxyBean implements Serializable {

    private static final long serialVersionUID = 9214313860895272893L;

    /**
     * ip地址
     */
    private String ip;
    /**
     * 端口
     */
    private int port;
    /**
     * 拉取次数
     */
    private int pullCount;

    /**
     * 成功次数
     */
    private int successCount;

    /**
     * 失败次数
     */
    private int errorCount;

    /**
     * 拉取时间
     */
    private long pullTime;


    public ProxyBean(String ip, int port) {
        this.ip = ip;
        this.port = port;
        pullTime = System.currentTimeMillis();
    }


    @Override
    public int hashCode() {
        return ip.hashCode() + new Integer(port).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        ProxyBean other = (ProxyBean) obj;
        return other != null && ip.equals(other.ip) && port == other.port;
    }

    /**
     * 失败次数加1
     */
    public synchronized int errorIncrementAndGet() {
        return errorCount++;
    }

    /**
     * 拉取次数加1
     */
    public synchronized int pullIncrementAndGet() {
        return pullCount++;
    }

    /**
     * 成功次数加1
     */
    public synchronized int successIncrementAndGet() {
        return successCount++;
    }


    @Override
    public String toString() {
        return ip + ":" + port;
    }

    public int getPullCount() {
        return pullCount;
    }

    public void setPullCount(int pullCount) {
        this.pullCount = pullCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public long getPullTime() {
        return pullTime;
    }

    public void setPullTime(long pullTime) {
        this.pullTime = pullTime;
    }
}