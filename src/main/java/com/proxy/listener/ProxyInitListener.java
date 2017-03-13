package com.proxy.listener;

import com.proxy.serivce.ProxyPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 初始化监听器
 */
public class ProxyInitListener implements ServletContextListener {

    private static Logger LOG = LogManager.getLogger(ProxyInitListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("Proxy init servlet.");
        ProxyPool.initProxyPool();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.info("Proxy init servlet destroy.");
        ProxyPool.shutdownProxyPool();
    }
}
