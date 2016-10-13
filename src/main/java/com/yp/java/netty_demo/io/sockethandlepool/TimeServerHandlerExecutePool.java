package com.yp.java.netty_demo.io.sockethandlepool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimeServerHandlerExecutePool {
    private ExecutorService executorService;

    public TimeServerHandlerExecutePool(int maxPoolSize, int queueSize) {
        executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize, 120l,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void excute(Runnable task) {
        executorService.execute(task);
    }

}
