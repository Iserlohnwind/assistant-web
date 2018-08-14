package com.momassistant.mapper;

import com.momassistant.mapper.model.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Mapper
public interface UserInfoMapper {
    @Select("SELECT id FROM UserInfo WHERE openId = #{openId}")
    int getUserId(@Param("openId") String openId);


    @Select("SELECT * FROM UserInfo WHERE id = #{userId}")
    @Results({
            @Result(property = "userId",  column = "id"),
            @Result(property = "wechatName",  column = "wechatName"),
            @Result(property = "userName",  column = "userName"),
            @Result(property = "userHeadPic",  column = "userHeadPic"),
            @Result(property = "userRegion",  column = "userRegion"),
            @Result(property = "gender",  column = "gender"),
            @Result(property = "mobile",  column = "mobile"),
            @Result(property = "userType",  column = "userType"),
            @Result(property = "edc",  column = "edc"),

    })
    UserInfo getUserDetail(@Param("userId") int userId);



    @Insert("insert into UserInfo(openId,createTime) values(#{openId}, now())")
    @Options(useGeneratedKeys=true,keyProperty="id")
    UserInfo createUser(UserInfo userInfo);


    @Update(
            "update UserInfo set wechatName=#{wechatName}, userName=#{userName},userHeadPic=#{userHeadPic}," +
                    "userRegion=#{userRegion},gender=#{gender},mobile=#{mobile},userType=#{userType},edc=#{edc}" +
                    " where id=#{userId}"
    )
    void updateUserDetail(UserInfo userInfo);

    @Update(
            "update UserInfo set token=#{userToken}, expiredTime=#{expiredTime} where id=#{userId}"
    )
    void addToken(@Param("userId")int userId, @Param("userToken")String userToken, @Param("expiredTime")Date expiredTime);


}
