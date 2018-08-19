package com.momassistant.entity.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhufeng on 2018/8/19.
 */
@Data
public class SetupPregancyInfoReq implements Serializable {
    private String userName;
    private String userRegion;
    private String mobile;

    private int userType;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date edc;
}
