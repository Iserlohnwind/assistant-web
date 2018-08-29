package com.momassistant.entity.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhufeng on 2018/8/17.
 */
@Data
public class BabyBirth implements Serializable{
    private int birthNum;

    private String birthUnit;


}
