package com.proxy.servlet;

import com.proxy.model.ProxyBean;
import com.proxy.serivce.ProxyPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 通知成功
 */
public class NotifySuccessServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger LOG = LogManager.getLogger(NotifySuccessServlet.class);

    public NotifySuccessServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ip = request.getParameter("ip");
        int port = Integer.parseInt(request.getParameter("port"));
        LOG.debug("NotifySuccess [ip:{}, port:{}]", ip, port);
        ProxyPool.getInstance().noticeSuccess(new ProxyBean(ip, port));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
