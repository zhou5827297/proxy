package com.proxy.serivce.impl;

import com.proxy.config.HttpConstant;
import com.proxy.config.ProxyConstant;
import com.proxy.model.ProxyBean;
import com.proxy.remote.HttpPoolManage;
import com.proxy.serivce.ProxyChecker;
import com.proxy.util.ThreadUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * 通过url访问检测可用性
 */
public class SimpleProxyChecker implements ProxyChecker {
    private static final Logger LOG = LogManager.getLogger(SimpleProxyChecker.class);

    @Override
    public List<ProxyBean> check(Collection<ProxyBean> proxys) {
        final long t1 = System.currentTimeMillis();
        LOG.info("ProxyChecker.check() start, list.size=[{}]", proxys.size());

        List<ProxyBean> availableProxyList = Collections.synchronizedList(new LinkedList<ProxyBean>());
        List<Callable<String>> tasks = new ArrayList<Callable<String>>();
//        long nowTime = System.currentTimeMillis();
        for (final ProxyBean pb : proxys) {
            tasks.add(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    LOG.debug("check [{}] start...", pb);
                    try {
                        if (pb.getErrorCount() >= ProxyConstant.MAX_ERROR_COUNT && pb.getErrorCount() / (pb.getSuccessCount() + 1) > 2) {
                            return null;
                        }
                        if (pb.getErrorCount() / (pb.getSuccessCount() + 1) > 10) {
                            return null;
                        }
//                        if ((nowTime - pb.getPullTime()) / 1000 / 60 > 60) {
//                            return null;
//                        }
                        long start = System.currentTimeMillis();
                        boolean success = HttpPoolManage.checkProxy(ProxyConstant.CHECK_URL, pb);
                        if (success) {
                            availableProxyList.add(pb);
                        }
                        long end = System.currentTimeMillis();
                        LOG.debug("check [{}] success, time=[{}]", pb, end - start);
                    } catch (Exception e) {
                        //TODO 不做操作
                    }
                    return null;
                }
            });
        }
        try {
            ThreadUtils.EXECUTOR.invokeAll(tasks);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
        long t2 = System.currentTimeMillis();
        LOG.info("ProxyChecker.check() end, list.size=[{}], time=[{}]", availableProxyList.size(), t2 - t1);
        return availableProxyList;
    }
}
