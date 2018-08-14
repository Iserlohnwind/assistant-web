package com.momassistant.entity.request;

import lombok.Data;

import java.util.Date;

/**
 * Created by Kris on 2018/8/14.
 */
@Data
public class BabyInfoReq {
    private String babyName;
    private Date babyBirthday;
    private int babyGender;
}
