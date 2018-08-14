package com.momassistant.mapper;

import com.momassistant.mapper.model.UserSession;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Mapper
public interface UserSessionMapper {
    @Select("SELECT * FROM UserSession WHERE userId = #{userId} and token = #{userToken} and expireTime > now()")
    UserSession findByUserIdAndToken(@Param("userId") int userId, @Param("userToken") String userToken);

    @Insert("insert into UserSession(userId,token,expireTime) values(#{userId},#{userToken},#{expireTime})")
    void insertToken(@Param("userId") int userId, @Param("userToken") String userToken, @Param("expireTime") Date expireTime);

    @Update(
            "update UserSession set token=#{userToken}, expireTime=#{expireTime} where userId=#{userId}"
    )
    void updateToken(@Param("userId") int userId, @Param("userToken") String userToken, @Param("expiredTime") Date expiredTime);

}
