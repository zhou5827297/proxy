package com.proxy.model;

/**
 * 代理管理
 */
public interface ProxyManage {

    /**
     * 通知代理ip，可使用
     */
    public void noticeSuccess(ProxyBean proxyBean);

    /**
     * 通知代理ip，不可使用
     */

    public void noticeFail(ProxyBean proxyBean, String errorMessage);

    /**
     * 获取一条代理ip
     */
    public ProxyBean getProxyBean();

}
