package com.proxy.remote;

import com.proxy.config.HttpConstant;
import com.proxy.model.ProxyBean;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * http连接池
 */
public class HttpPoolManage {

    private static final Logger LOG = LogManager.getLogger(HttpPoolManage.class);

    private static final int TIME_OUT = HttpConstant.TIMEOUT_READ;

    private static final int CONNECT_TIME_OUT = HttpConstant.TIMEOUT_CONNECT;

    private static MultiThreadedHttpConnectionManager CONNECTIONMANAGER;

    private static HttpClientParams CLIENTPARAMS;

    private static final String USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";

    static {
        init();
    }

    private static void init() {
        try {
            CONNECTIONMANAGER = new MultiThreadedHttpConnectionManager();
            HttpConnectionManagerParams params = CONNECTIONMANAGER.getParams();
            params.setDefaultMaxConnectionsPerHost(8000);
            params.setConnectionTimeout(CONNECT_TIME_OUT);
            params.setSoTimeout(TIME_OUT);
            params.setMaxTotalConnections(8000);
            CLIENTPARAMS = new HttpClientParams();
            CLIENTPARAMS.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
            CLIENTPARAMS.setSoTimeout(TIME_OUT);
            CLIENTPARAMS.setConnectionManagerTimeout(CONNECT_TIME_OUT);
            CLIENTPARAMS.setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        } catch (Exception e) {
            LOG.error("http连接池加载失败", e);
        }
    }

    /**
     * 释放连接池中连接,其实释放不了
     */
    public static void dispose(HttpClient httpClient) {
        httpClient.getHttpConnectionManager().closeIdleConnections(0);
    }

    /**
     * 获取一条http连接
     *
     * @param proxyBean 代理对象
     */
    public static HttpClient getHttpClient(ProxyBean proxyBean) {
        HttpClient httpClient = new HttpClient(CLIENTPARAMS, CONNECTIONMANAGER);
        if (proxyBean != null) {
            httpClient.getParams().setAuthenticationPreemptive(true);
            httpClient.getHostConfiguration().setProxy(proxyBean.getIp(), proxyBean.getPort());
        }
        return httpClient;
    }

    /**
     * 释放当前连接到连接池中
     *
     * @param httpMethod 请求对象
     */
    public static void disposeHttpClient(HttpMethod httpMethod) {
        httpMethod.releaseConnection();
    }


    /**
     * 简单发送get请求
     */
    public static String sendGet(String url, ProxyBean proxyBean) {
        GetMethod method = null;
        String response = "";
        try {
            HttpClient httpClient = getHttpClient(proxyBean);
            method = new CustomGetMethod(url);
            method.addRequestHeader("Accept-Encoding", "gzip, deflate, sdch");
            method.addRequestHeader("User-Agent", USERAGENT);
            method.addRequestHeader("Connection", "close");
            int statusCode = httpClient.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                response = method.getResponseBodyAsString();
            }
        } catch (Exception ex) {
            LOG.error("deal http-get error", ex);
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
        return response;
    }

    /**
     * 检测代理可用性
     */
    public static boolean checkProxy(String url, ProxyBean proxyBean) {
        HeadMethod method = null;
        boolean success = false;
        try {
            HttpClient httpClient = getHttpClient(proxyBean);
            method = new HeadMethod(url);
            method.addRequestHeader("User-Agent", USERAGENT);
            httpClient.executeMethod(method);
            success = true;
        } catch (Exception ex) {
            //LOG.error("deal http-get error", ex);
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
        return success;
    }

}
