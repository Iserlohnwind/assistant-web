package com.momassistant.entity.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Kris on 2018/8/14.
 */
@Data
public class BabyInfoReq {
    private String babyName;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date babyBirthday;
    private int babyGender;
}
