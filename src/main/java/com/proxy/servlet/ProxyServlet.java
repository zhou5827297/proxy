package com.proxy.servlet;

import com.proxy.model.ProxyBean;
import com.proxy.serivce.ProxyPool;
import common.base.util.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 获取代理ip
 */
public class ProxyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static Logger LOG = LogManager.getLogger(ProxyServlet.class);

    public ProxyServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("Start get proxy bean.");
        ProxyBean proxyBean = ProxyPool.getInstance().getProxyBean();
        LOG.debug("proxy[ip:{}, port:{}]", proxyBean.getIp(), proxyBean.getPort());
        String jsonStr = JsonUtil.toString(proxyBean);
        response.getWriter().write(jsonStr);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
