package com.proxy.config;

import java.util.ResourceBundle;

/**
 * 静态字段
 */
public class ProxyConstant {

    private final static ResourceBundle BUNDLE = ResourceBundle.getBundle("proxy");

    /**
     * 代理检测器
     */
    public static final String PROXYCHECKER_CLASS = BUNDLE.getString("PROXYCHECKER_CLASS");

    /**
     * 代理初始化器
     */
    public static final String PROXYINIT_CLASS = BUNDLE.getString("PROXYINIT_CLASS");

    /**
     * ip最大失败次数
     */
    public static final int MAX_ERROR_COUNT = Integer.parseInt(BUNDLE.getString("MAX_ERROR_COUNT"));

    /**
     * 校验代理ip可用行
     */
    public static final String CHECK_URL = BUNDLE.getString("CHECK_URL");

    /**
     * 校验代理ip间隔时间(秒)
     */
    public static final int CHECK_INTERVAL = Integer.parseInt(BUNDLE.getString("CHECK_INTERVAL"));

    /**
     * 校验成功代理ip的重试次数
     */
    public static final int CHECK_SUCCESS_COUNT = Integer.parseInt(BUNDLE.getString("CHECK_SUCCESS_COUNT"));

    /**
     * 代理厂商购买
     */
    public static final String THIRD_URL = BUNDLE.getString("THIRD_URL");
    /**
     * 代理池中阀值，低于再次拉取一批ip
     */
    public static final int POOL_IP_MIN = Integer.parseInt(BUNDLE.getString("POOL_IP_MIN"));
    /**
     * 一次拉取数目
     */
    public static final int THIRD_PULL_NUM = Integer.parseInt(BUNDLE.getString("THIRD_PULL_NUM"));
    /**
     * 拉取质量
     */
    public static final String THIRD_PULL_QUALITY = BUNDLE.getString("THIRD_PULL_QUALITY");

}
