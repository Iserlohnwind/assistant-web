package com.momassistant.mapper;

import com.momassistant.mapper.model.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Mapper
public interface UserInfoMapper {
    @Select("SELECT * FROM UserInfo WHERE id = #{userId} and token = #{userToken}")
    @Results({
            @Result(property = "userId",  column = "id"),
            @Result(property = "userToken", column = "token")
    })
    UserInfo findByUserIdAndToken(@Param("userId") int userId, @Param("token") String userToken);

    @Select("SELECT * FROM UserInfo WHERE openId = #{openId}")
    @Results({
            @Result(property = "userId",  column = "id"),
            @Result(property = "openId",  column = "openId"),
    })
    UserInfo getUser(@Param("openId") String openId);


    @Insert("insert into UserInfo(openId,createTime) values(#{openId}, now())")
    @Options(useGeneratedKeys=true,keyProperty="id")
    UserInfo createUser(UserInfo userInfo);

    @Update(
            "update UserInfo set token=#{userToken}, expiredTime=#{expiredTime} where id=#{userId}"
    )
    void addToken(@Param("userId")int userId, @Param("userToken")String userToken, @Param("expiredTime")Date expiredTime);


}
