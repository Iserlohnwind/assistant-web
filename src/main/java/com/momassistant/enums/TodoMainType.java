package com.momassistant.enums;

/**
 * Created by zhufeng on 2018/8/16.
 */
public enum TodoMainType {
    GESTATION(1),Lactation(2),;
    private int type;
    TodoMainType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
