package com.momassistant.annotations;

import java.lang.annotation.*;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Documented
@Target(
        { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenValidate
{
}