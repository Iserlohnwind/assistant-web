package com.momassistant.enums;

/**
 * Created by zhufeng on 2018/8/17.
 */
public enum TodoNotifySwitch {
    ON(1),OFF(0),
    ;
    int val;
    TodoNotifySwitch(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
