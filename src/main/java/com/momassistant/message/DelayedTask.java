package com.momassistant.message;

import com.momassistant.service.LactationTodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zhufeng on 2018/8/15.
 */
public abstract class DelayedTask<T> {

    Executor mainExecutor = Executors.newFixedThreadPool(1);

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
    @Async
    public void init() {
        initDaemon();
        initQueue();
        mainExecutor.execute(new Runnable() {
            @Override
            public void run() {
                execute();
            }
        });
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

    /**
     * 结束订单
     * @param task
     */
    public boolean endTask(DelayedMessage task){
        return t.remove(task);
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
