package com.momassistant.entity;

import com.momassistant.ReturnCode;
import lombok.Data;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Data
public class Response<T> {
    private int code;
    private String msg;
    private T data;

    public static<T> Response<T> success(T data) {
        Response response = new Response();
        response.setData(data);
        response.setCode(ReturnCode.OK.getCode());
        return response;
    }
    public static<T> Response<T> error(ReturnCode returnCode) {
        Response response = new Response();
        response.setCode(returnCode.getCode());
        response.setMsg(returnCode.getMsg());
        return response;
    }
}
