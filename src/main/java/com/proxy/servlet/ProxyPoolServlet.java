package com.proxy.servlet;

import com.proxy.model.ProxyBean;
import com.proxy.serivce.ProxyPool;
import common.base.util.JsonUtil;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 代理池的状态
 */
public class ProxyPoolServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static Logger LOG = LogManager.getLogger(ProxyPoolServlet.class);

    public ProxyPoolServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ProxyBean> proxyBeans = ProxyPool.getInstance().getProxys();
        String jsonStr = JsonUtil.toString(proxyBeans);
        response.getWriter().write(jsonStr);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
