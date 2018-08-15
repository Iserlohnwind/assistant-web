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
@Component
public class TodoTask {


    Executor executor = Executors.newFixedThreadPool(20);

    /**
     * 创建一个最初为空的新 DelayQueue
     */
    private DelayQueue<DelayedMessage> t = new DelayQueue<>();

    /**
     * 守护线程
     */
    private Thread daemonThread;



    /**
     * 初始化守护线程
     */
    @PostConstruct
    public void init() {
        daemonThread = new Thread(() -> execute());
        daemonThread.setDaemon(true);
        daemonThread.setName("Task Queue Daemon Thread");
        daemonThread.start();
        this.execute();
    }

    private void execute() {
        System.out.println("start:" + System.currentTimeMillis());
        while (true) {
            try {
                DelayedMessage t1 = t.take();
                if (t1 != null) {
                    //修改问题的状态
                    Todo task = t1.getTask();
                    if (task == null) {
                        continue;
                    }
                    executor.execute(task);
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
        //创建一个任务
        DelayedMessage k = new DelayedMessage(date, task);
        //将任务放在延迟的队列中
        t.put(k);
    }

    /**
     * 结束订单
     * @param task
     */
    public boolean endTask(DelayedMessage task){
        return t.remove(task);
    }


    public static void main(String[] args) {
    }
}
