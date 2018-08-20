package com.momassistant.mapper.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhufeng on 2018/8/15.
 */
@Data
public class TodoTypeDetail implements Serializable{
    private int id;
    private int typeId;
    private String title;
    private String content;
    private String keyword;
}
