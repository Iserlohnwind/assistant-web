package com.momassistant.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhufeng on 2018/8/14.
 */
public class HtmlUtil {
    public static int getUserId() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String userIdStr = request.getHeader("userId");
        int userId = 0;
        try {
            userId = Integer.valueOf(userIdStr);
        } catch (Exception e) {

        }
        return userId;
    }
}
