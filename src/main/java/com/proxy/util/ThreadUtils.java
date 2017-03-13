package com.proxy.util;


import com.proxy.config.ProxyConstant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 线程工具类
 */
public class ThreadUtils {
    public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(ProxyConstant.THIRD_PULL_NUM / 10);
    public static final ScheduledExecutorService SCHEDULEDEXECUTOR = Executors.newSingleThreadScheduledExecutor();

    /**
     * 获取cpu数量
     */
    public static int getCpuProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 获取最优执行线程数量 (6 CPU * 4 Core+1 * 2 HT =48 Threading.)
     */
    public static int getThreadCount() {
        try {
            //return 500;
            return getCpuProcessors() * (4 + 1) * 5;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 500;
    }

    /**
     * 定时执行一条任务
     *
     * @param command      执行线程
     * @param initialDelay 延时时间
     * @param period       执行间隔
     * @param timeUnit     间隔类型
     */
    public static void executeQuertz(Runnable command, long initialDelay, long period, TimeUnit timeUnit) {
        SCHEDULEDEXECUTOR.scheduleAtFixedRate(command, initialDelay, period, timeUnit);
    }

    /**
     * 休眠一条线程，指定时间
     *
     * @param timeUnit timeunit
     * @param timeout  时间
     */
    public static void sleepThreadByTimeUnit(TimeUnit timeUnit, long timeout) {
        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 休眠一条线程timeout秒钟
     */
    public static void sleepThreadSeconds(long timeout) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭线程池
     */
    public static void shutdown() {
        EXECUTOR.shutdown();
        SCHEDULEDEXECUTOR.shutdown();
    }

    public static void main(String[] args) {
        System.out.println(getThreadCount());
    }
}
