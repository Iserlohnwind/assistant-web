package com.momassistant.controller;

import com.momassistant.annotations.UserValidate;
import com.momassistant.entity.Response;
import com.momassistant.entity.request.UpdateTodoNotifySwitchReq;
import com.momassistant.entity.request.TodoDetailReq;
import com.momassistant.entity.request.UserGestationTodoReq;
import com.momassistant.entity.request.UserLactationTodoReq;
import com.momassistant.entity.response.TodoDetailResp;
import com.momassistant.entity.response.UserGestationTodoResp;
import com.momassistant.entity.response.UserLactationTodoResp;
import com.momassistant.enums.TodoNotifySwitch;
import com.momassistant.service.CommonTodoService;
import com.momassistant.service.GestationTodoService;
import com.momassistant.service.LactationTodoService;
import com.momassistant.utils.HtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhufeng on 2018/8/17.
 */
@Api("todo相关api")
@RestController
public class TodoController {

    @Autowired
    private GestationTodoService gestationTodoService;
    @Autowired
    private LactationTodoService lactationTodoService;
    @Autowired
    private CommonTodoService commonTodoService;

    @ApiOperation(value = "获得产检todo列表", notes = "获得产检todo列表", httpMethod = "GET")
    @ApiImplicitParam(name = "userInfoReq", value = "用户详细实体user", required = true, dataType = "SetupWechatInfoReq")
    @RequestMapping(value = "/todo/getGestationTodoList", method = RequestMethod.GET)
    @UserValidate
    public Response<UserGestationTodoResp> getGestationTodoList() {
        return Response.success(gestationTodoService.getGestationTodoList(HtmlUtil.getUserId()));
    }

    @ApiOperation(value = "获得某条todo详情", notes = "获得某条todo详情", httpMethod = "GET")
    @ApiImplicitParam(name = "typeId", value = "todo类型id", required = true, dataType = "int")
    @RequestMapping(value = "/todo/getTodoDetail", method = RequestMethod.GET)
    @UserValidate
    public Response<TodoDetailResp> getTodoDetail(int typeId) {
        return Response.success(commonTodoService.getTodoDetail(typeId));
    }


    @ApiOperation(value = "获得疫苗接种todo列表", notes = "获得疫苗接种todo列表", httpMethod = "GET")
    @RequestMapping(value = "/todo/getLactationTodoList", method = RequestMethod.GET)
    @UserValidate
    public Response<UserLactationTodoResp> getLactationTodoList() {
        return Response.success(lactationTodoService.getTodoList(HtmlUtil.getUserId()));
    }

    @ApiOperation(value = "todo提醒开关", notes = "todo提醒开关", httpMethod = "POST")
    @ApiImplicitParam(name = "updateTodoNotifySwitchReq", value = "提醒开关更新请求实体", required = true, dataType = "UpdateTodoNotifySwitchReq")
    @RequestMapping(value = "/todo/updateTodoNotifySwitch", method = RequestMethod.POST)
    @UserValidate
    public Response<Boolean> updateTodoNotifySwitch(@RequestBody UpdateTodoNotifySwitchReq updateTodoNotifySwitchReq) {
        if (updateTodoNotifySwitchReq.getTodoNotifySwitch() == TodoNotifySwitch.ON.getVal()) {
            commonTodoService.notifyOn(HtmlUtil.getUserId());
        } else {
            commonTodoService.notifyOff(HtmlUtil.getUserId());
        }
        return Response.success(Boolean.TRUE);
    }
}
