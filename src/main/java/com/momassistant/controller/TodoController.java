package com.momassistant.controller;

import com.momassistant.annotations.UserValidate;
import com.momassistant.entity.Response;
import com.momassistant.entity.request.UserGestationTodoDetailReq;
import com.momassistant.entity.request.UserGestationTodoReq;
import com.momassistant.entity.response.UserGestationTodoDetailResp;
import com.momassistant.entity.response.UserGestationTodoResp;
import com.momassistant.service.GestationTodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhufeng on 2018/8/17.
 */
@RestController
public class TodoController {

    @Autowired
    private GestationTodoService gestationTodoService;

    @RequestMapping("/todo/getGestationTodo")
    @UserValidate
    public Response<UserGestationTodoResp> getGestationTodo(UserGestationTodoReq req) {
        int userId = 0;
        return Response.success(gestationTodoService.getGestationTodoList(userId));
    }

    @RequestMapping("/todo/getGestationTodoDetail")
    @UserValidate
    public Response<UserGestationTodoDetailResp> getGestationTodoDetail(UserGestationTodoDetailReq req) {
        return Response.success(gestationTodoService.getGestationTodoDetail(req.getTypeId()));
    }
}
