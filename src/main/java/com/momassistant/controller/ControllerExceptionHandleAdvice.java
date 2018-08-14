package com.momassistant.controller;

import com.momassistant.ReturnCode;
import com.momassistant.entity.Response;
import com.momassistant.exception.TokenValidateException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhufeng on 2018/8/14.
 */
@RestControllerAdvice
public class ControllerExceptionHandleAdvice {

    @ExceptionHandler
    public Response handler(HttpServletRequest req, HttpServletResponse res, Exception e) {
        if (e instanceof TokenValidateException) {
            return Response.error(ReturnCode.TOKEN_EXPIRED);
        }
        return Response.error(ReturnCode.UKNOWN);
    }
}
