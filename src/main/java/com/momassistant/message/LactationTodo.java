package com.momassistant.message;

import com.momassistant.mapper.model.BabyInfo;
import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.TodoTypeDetail;
import com.momassistant.wechat.WeiXinTemplate;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by zhufeng on 2018/8/16.
 */
@Data
public class LactationTodo extends Todo  implements Serializable {
    private int babyId;

    public LactationTodo(int typeId, int userId, String openId, WeiXinTemplate data, BabyInfo babyInfo) {
        super(typeId, userId, openId, data);
        this.babyId = babyInfo.getBabyId();
    }


    public LactationTodo(int babyId) {
        super();
        this.babyId = babyId;
    }

    public LactationTodo(TodoLog todoLog) {
        super(todoLog);
        this.babyId = todoLog.getBabyId();
    }

    @Override
    public String toString() {
        return this.getUserId() + "-" + this.getBabyId();
    }
}
