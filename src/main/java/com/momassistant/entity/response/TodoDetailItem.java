package com.momassistant.entity.response;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhufeng on 2018/8/17.
 */
@Data
public class TodoDetailItem implements Serializable{
    private String title;
    private String content;
    private String keyword;
}
