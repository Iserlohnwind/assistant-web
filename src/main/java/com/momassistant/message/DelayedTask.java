package com.momassistant.message;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zhufeng on 2018/8/15.
 */
public abstract class DelayedTask<T> {


    Executor executor = Executors.newFixedThreadPool(20);

    /**
     * 创建一个最初为空的新 DelayQueue
     */
    private DelayQueue<DelayedMessage<T>> t = new DelayQueue<>();

    /**
     * 守护线程
     */
    private Thread daemonThread;



    @PostConstruct
    public void init() {
        initDaemon();
        initQueue();
        this.execute();
    }

    private void execute() {
        while (true) {
            try {
                DelayedMessage<T> t1 = t.take();
                if (t1 != null) {
                    //修改问题的状态
                    T message = t1.getMessage();
                    if (message == null) {
                        continue;
                    }
                    executor.execute(excuteRunable(message));
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * 添加任务，
     * time 延迟时间
     * task 任务
     * 用户为问题设置延迟时间
     */
    public void put(Date date, Todo task) {
        DelayedMessage k = new DelayedMessage(date, task);
        t.put(k);
    }

    public void initDaemon() {
        daemonThread = new Thread(() -> execute());
        daemonThread.setDaemon(true);
        daemonThread.setName("Task Queue Daemon Thread");
        daemonThread.start();
    }


    public abstract void initQueue();


    public abstract Runnable excuteRunable(T t);

}
