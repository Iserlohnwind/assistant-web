package com.momassistant.service;

import com.momassistant.mapper.model.TodoType;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/20.
 */
public abstract class TodoService {
    protected abstract Date caculateTodoDate(Date birthDate, TodoType todoType);
}
