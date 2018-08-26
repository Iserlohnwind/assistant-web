package com.momassistant.iterceptor;

import com.momassistant.annotations.UserValidate;
import com.momassistant.exception.TokenValidateException;
import com.momassistant.service.UserInfoService;
import com.momassistant.utils.SpringContextAware;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Component
public class TokenValidateInterceptor implements HandlerInterceptor {

    protected static final Log logger = LogFactory.getLog(TokenValidateInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        UserValidate userValidate = ((HandlerMethod) handler).getMethodAnnotation(UserValidate.class);
        if (userValidate == null) {
            return true;
        }
        UserInfoService userInfoService = SpringContextAware.getBean(UserInfoService.class);
        String userToken = request.getHeader("userToken");
        String userIdStr = request.getHeader("userId");
        logger.info(String.format("userToken:%s,userId:%s", userToken, userIdStr));
        int userId = 0;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (Exception e) {

        }

        if (userId == 0 || !userInfoService.validateToken(userId, userToken)) {
            throw new TokenValidateException();
        }
        return true;
    }

}
