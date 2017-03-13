package com.proxy.serivce;

import com.proxy.model.ProxyBean;

import java.util.List;

/**
 * 检测代理ip可用性能
 */
public interface ProxyInit {

    /**
     * 初始化代理池
     */
    public List<ProxyBean> initProxys(List<ProxyBean> proxys);
}
