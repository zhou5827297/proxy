package com.proxy.serivce;

import com.proxy.model.ProxyBean;

import java.util.Collection;
import java.util.List;

/**
 * 检测代理ip可用性
 */
public interface ProxyChecker {

    /**
     * 返回可用的ip代理池
     */
    public List<ProxyBean> check(Collection<ProxyBean> proxys);

}
