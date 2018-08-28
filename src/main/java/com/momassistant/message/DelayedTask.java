package com.momassistant.message;

import com.momassistant.service.CommonTodoService;
import com.momassistant.utils.SpringContextAware;
import com.momassistant.wechat.WeiXinMessageService;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by zhufeng on 2018/8/15.
 */
public abstract class DelayedTask<T extends Todo> {

    Executor mainExecutor = Executors.newFixedThreadPool(1);

    Executor executor = Executors.newFixedThreadPool(20);

    /**
     * 创建一个最初为空的新 DelayQueue
     */
    private DelayQueue<DelayedMessage> t = new DelayQueue<>();

    /**
     * 守护线程
     */
    private Thread daemonThread;
    protected DelayedMessageSerializer delayedMessageSerializer;


    @PostConstruct
    @Async
    public void init() {
        initDelayedMessageSerializer();
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
                DelayedMessage<T> delayedMessage = t.take();
                if (delayedMessage != null) {
                    //修改问题的状态
                    T message = delayedMessage.getMessage();
                    if (message == null) {
                        continue;
                    }
                    executor.execute(excuteRunable(message));
                    delayedMessageSerializer.delete(delayedMessage);
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
        delayedMessageSerializer.serialize(k);
    }


    /**
     * 结束订单
     * @param task
     */
    public void remove(Todo task){
        DelayedMessage delayedMessage = new DelayedMessage(new Date(), task);
        t.remove(delayedMessage);
        delayedMessageSerializer.delete(delayedMessage);
    }


    public void initDaemon() {
        daemonThread = new Thread(() -> execute());
        daemonThread.setDaemon(true);
        daemonThread.setName("Task Queue Daemon Thread");
        daemonThread.start();
    }


    private void initQueue() {
        t.addAll(delayedMessageSerializer.deSerialize());
    }

    public Runnable excuteRunable(T todo) {
        return new Runnable() {
            @Override
            public void run() {

                CommonTodoService commonTodoService = SpringContextAware.getBean(CommonTodoService.class);
                WeiXinMessageService weiXinMessageService = SpringContextAware.getBean(WeiXinMessageService.class);
                if (commonTodoService.checkTodoNotifySwitchOn(todo.getUserId())){
                    //发送过程，暂未实现
                    weiXinMessageService.sendTemplateMessage(todo.getWeiXinTemplate());
                }
                //新建下一个提醒,存入队列
                createNextTodo(todo);
            }
        };
    }


    protected abstract void initDelayedMessageSerializer();

    protected abstract void createNextTodo(Todo oldTodo);

    }
