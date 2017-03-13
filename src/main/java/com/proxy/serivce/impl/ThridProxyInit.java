package com.proxy.serivce.impl;

import com.proxy.config.ProxyConstant;
import com.proxy.model.ProxyBean;
import com.proxy.remote.HttpPoolManage;
import com.proxy.serivce.ProxyInit;
import common.base.util.DateUtils;
import common.base.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 第三方代理获得
 */
public class ThridProxyInit implements ProxyInit {
    private static final Logger LOG = LogManager.getLogger(ThridProxyInit.class);

    /**
     * 初始化代理池
     */
    @Override
    public List<ProxyBean> initProxys(List<ProxyBean> proxys) {
        if (proxys.size() >= ProxyConstant.POOL_IP_MIN) {
            return proxys;
        }
        List<ProxyBean> tempProxys = Collections.synchronizedList(new LinkedList<ProxyBean>());
        tempProxys.addAll(proxys);
        try {
            String url = String.format(ProxyConstant.THIRD_URL, ProxyConstant.THIRD_PULL_NUM, "1");
            pullThirdIp(url, tempProxys);
            if ("0".equals(ProxyConstant.THIRD_PULL_QUALITY)) {
                url = String.format(ProxyConstant.THIRD_URL, ProxyConstant.THIRD_PULL_NUM, "0");
                pullThirdIp(url, tempProxys);
            }
            LOG.info("begin pull thrid ip [{}]...", url);
        } catch (Exception ex) {
            LOG.error("pull thrid ip fail", ex);
        }
        return tempProxys;
    }

    /**
     * 拉取ip，存放池中
     */
    private void pullThirdIp(String url, List<ProxyBean> proxys) throws Exception {
        String response = HttpPoolManage.sendGet(url, null);
        if (StringUtil.notEmpty(response)) {
            String[] proxies = response.split("\r\n");
            for (String line : proxies) {
                if (line.split(":") == null || line.split(":").length < 2) {
                    continue;
                }
                String ip = line.split(":")[0];
                String port = line.split(":")[1];
                ProxyBean proxyBean = new ProxyBean(ip, Integer.parseInt(port));
                if (!proxys.contains(proxyBean)) {
                    proxys.add(proxyBean);
                }
            }
            LOG.info("pull thrid ip success");
        } else {
            LOG.error("pull thrid ip fail");
        }
    }

}
