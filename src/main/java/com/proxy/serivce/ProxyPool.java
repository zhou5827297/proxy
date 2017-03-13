package com.proxy.serivce;

import com.proxy.config.ProxyConstant;
import com.proxy.model.ProxyBean;
import com.proxy.model.ProxyManage;
import com.proxy.serivce.impl.SimpleProxyChecker;
import com.proxy.util.ThreadUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 代理池
 */
public class ProxyPool implements ProxyManage {
    protected static final Logger LOG = LogManager.getLogger(ProxyPool.class);
    /**
     * 代理对象池
     */
    protected List<ProxyBean> proxys = Collections.synchronizedList(new LinkedList<ProxyBean>());
    /**
     * 代理检测器
     */
    protected ProxyChecker proxychecker = new SimpleProxyChecker();
    /**
     * 代理初始化器
     */
    protected ProxyInit proxyInit = null;

    /**
     * 单例实现
     */
    protected static ProxyPool PROXYPOOL = null;

    /**
     * 是否初次启动
     */
    protected static boolean ISFIRSTINIT = true;


    /**
     * 初始化代理池对象
     */
    public static void initProxyPool() {
        if (PROXYPOOL == null) {
            PROXYPOOL = new ProxyPool();
            try {
                ProxyInit proxyInit = (ProxyInit) Class.forName(ProxyConstant.PROXYINIT_CLASS).newInstance();
                PROXYPOOL.setProxyInit(proxyInit);
            } catch (Exception ex) {
                LOG.error("proxyInit[{}]加载失败，请查看配置信息", ProxyConstant.PROXYINIT_CLASS);
                System.exit(0);
            }
            try {
                ProxyChecker proxychecker = (ProxyChecker) Class.forName(ProxyConstant.PROXYCHECKER_CLASS).newInstance();
                PROXYPOOL.setProxychecker(proxychecker);
            } catch (Exception ex) {
                LOG.error("proxychecker[{}]加载失败，请查看配置信息", ProxyConstant.PROXYCHECKER_CLASS);
                System.exit(0);
            }
            PROXYPOOL.getProxyInit().initProxys(PROXYPOOL.getProxys());
            PROXYPOOL.schedule();
        }
    }

    /**
     * 销毁代理池对象
     */
    public static void shutdownProxyPool() {
        ThreadUtils.shutdown();
        PROXYPOOL.getProxys().clear();
        PROXYPOOL = null;
        System.exit(0);
    }

    /**
     * 定时检测池中ip是否可用，并且充足
     */
    private void schedule() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ProxyPool proxyPool = getInstance();
                List<ProxyBean> tempProxys = proxyInit.initProxys(proxys);
                proxyPool.setProxys(proxychecker.check(tempProxys));
            }
        };
        ThreadUtils.executeQuertz(runnable, 0, ProxyConstant.CHECK_INTERVAL, TimeUnit.SECONDS);
    }


    private ProxyPool() {
    }


    @Override
    public void noticeSuccess(ProxyBean proxyBean) {
        ProxyBean pb = pullProxyBeanfromPool(proxyBean);
        if (pb != null) {
            pb.successIncrementAndGet();
        }
    }

    @Override
    public void noticeFail(ProxyBean proxyBean, String errorMessage) {
        ProxyBean pb = pullProxyBeanfromPool(proxyBean);
        if (pb != null) {
            pb.errorIncrementAndGet();
        }
    }

    /**
     * 从代理池中拉取指定的bean
     */
    private ProxyBean pullProxyBeanfromPool(ProxyBean proxyBean) {
        if (proxyBean != null) {
            for (ProxyBean pb : proxys) {
                if (pb.equals(proxyBean)) {
                    return pb;
                }
            }
        }
        return null;
    }

    @Override
    public ProxyBean getProxyBean() {
        ProxyPool proxyPool = getInstance();
        if (proxyPool.getProxys().isEmpty()) {
            return null;
        }
        ProxyBean goodProxy = null; // 优代理
        ProxyBean proxyBean = null;
        // 优先拉取未使用过的的代理ip
        for (int i = 0; i < ProxyConstant.CHECK_SUCCESS_COUNT; i++) {
            int size = proxyPool.getProxys().size();
            int index = ThreadLocalRandom.current().nextInt(size);
            proxyBean = proxyPool.getProxys().get(index);
            if (proxyBean.getPullCount() == 0) {
                break;
            }
            // 记录失败次数最少的代理ip
            if (goodProxy == null) {
                goodProxy = proxyBean;
            } else if (proxyBean.getSuccessCount() > goodProxy.getSuccessCount() && proxyBean.getErrorCount() < ProxyConstant.MAX_ERROR_COUNT) {
                goodProxy = proxyBean;
            }
            if (i == ProxyConstant.CHECK_SUCCESS_COUNT - 1) {
                proxyBean = goodProxy;
            }
        }
        proxyBean.pullIncrementAndGet();
        return proxyBean;
    }

    /**
     * 获取代理池实例
     */
    public static ProxyPool getInstance() {
        initProxyPool();
        if (ISFIRSTINIT) {
            //第一次停顿一下等待检测ip完毕
            ThreadUtils.sleepThreadSeconds(3);
            ISFIRSTINIT = false;
        }
        return PROXYPOOL;

    }

    public ProxyInit getProxyInit() {
        return proxyInit;
    }

    public void setProxyInit(ProxyInit proxyInit) {
        this.proxyInit = proxyInit;
    }

    public ProxyChecker getProxychecker() {
        return proxychecker;
    }

    public void setProxychecker(ProxyChecker proxychecker) {
        this.proxychecker = proxychecker;
    }

    public List<ProxyBean> getProxys() {
        return proxys;
    }

    public void setProxys(List<ProxyBean> proxys) {
        this.proxys = proxys;
    }

    public static void main(String[] args) {
        ProxyPool.getInstance().getProxyBean();
    }
}
