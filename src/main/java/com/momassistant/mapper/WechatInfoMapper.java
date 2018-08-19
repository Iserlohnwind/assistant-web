package com.momassistant.mapper;

import com.momassistant.mapper.model.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Mapper
public interface WechatInfoMapper {
    @Select("SELECT wechatId FROM WechatInfo WHERE userId = #{userId} and type = #{type}")
    String getWechatId(@Param("userId") int userId, @Param("type") int type);
}
