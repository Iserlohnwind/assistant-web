package com.momassistant.message;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zhufeng on 2018/8/15.
 */
public class DelayedMessage<T> implements Delayed{
    /**
     * 到期时间
     */
    private final Date todoDate;

    /**
     * 问题对象
     */
    private final T message;
    private static final AtomicLong atomic = new AtomicLong(0);

    private final long n;

    public DelayedMessage(Date todoDate, T t) {
        this.todoDate = todoDate;
        this.message = t;
        this.n = atomic.getAndIncrement();
    }

    /**
     * 返回与此对象相关的剩余延迟时间，以给定的时间单位表示
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(todoDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed other) {
        // TODO Auto-generated method stub
        if (other == this) // compare zero ONLY if same object
            return 0;
        if (other instanceof DelayedMessage) {
            DelayedMessage x = (DelayedMessage) other;
            long diff = todoDate.getTime() - x.todoDate.getTime();
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
            else if (n < x.n)
                return -1;
            else
                return 1;
        }
        long d = (getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    public T getMessage() {
        return this.message;
    }

    @Override
    public int hashCode() {
        return message.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DelayedMessage) {
            return object.hashCode() == hashCode() ? true : false;
        }
        return false;
    }

}
