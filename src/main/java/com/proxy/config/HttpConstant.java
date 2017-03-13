package com.proxy.config;

import java.util.ResourceBundle;

/**
 * 静态字段
 */
public class HttpConstant {
    private final static ResourceBundle BUNDLE = ResourceBundle.getBundle("http");

    /**
     * 读取超时时间
     */
    public static int TIMEOUT_READ = Integer.parseInt(BUNDLE.getString("TIMEOUT_READ"));
    /**
     * 连接超时时间
     */
    public static int TIMEOUT_CONNECT = Integer.parseInt(BUNDLE.getString("TIMEOUT_CONNECT"));

}
