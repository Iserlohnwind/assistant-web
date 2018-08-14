package com.momassistant.iterceptor;

import com.momassistant.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Component
public class TokenValidateInterceptor implements HandlerInterceptor {


    @Autowired
    private UserInfoService userInfoService;
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj)
            throws Exception {
        String userToken = request.getParameter("userToken");
        String userIdStr = request.getParameter("userId");
        int userId = 0;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (Exception e) {

        }

        if (userId == 0 || !userInfoService.validateToken(userId, userToken)) {
            return false;
        }
        return true;
    }

}
