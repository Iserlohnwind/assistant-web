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
    Integer getUserId(@Param("openId") String openId);

    @Select("SELECT userType FROM UserInfo WHERE id = #{userId}")
    Integer getUserType(@Param("userId") int userId);



    @Select("SELECT * FROM UserInfo WHERE id = #{userId}")
    @Results({
            @Result(property = "userId",  column = "id"),
            @Result(property = "wechatName",  column = "wechatName"),
            @Result(property = "userName",  column = "userName"),
            @Result(property = "userHeadPic",  column = "userHeadPic"),
            @Result(property = "userRegion",  column = "userRegion"),
            @Result(property = "mobile",  column = "mobile"),
            @Result(property = "userType",  column = "userType"),
            @Result(property = "edc",  column = "edc"),
            @Result(property = "todoNotifySwitch",  column = "todoNotifySwitch"),
            @Result(property = "paOpenId",  column = "paOpenId"),


    })
    UserInfo getUserDetail(@Param("userId") int userId);



    @Insert("insert into UserInfo(openId) values(#{openId})")
    @Options(useGeneratedKeys=true,keyProperty="userId")
    void createUser(UserInfo userInfo);


    @Update(
            "update UserInfo set wechatName=#{wechatName}, userName=#{userName},userHeadPic=#{userHeadPic}," +
                    "userRegion=#{userRegion},mobile=#{mobile},userType=#{userType},edc=#{edc}" +
                    " where id=#{userId}"
    )
    void updateUserDetail(UserInfo userInfo);

    @Update(
            "update UserInfo set  todoNotifySwitch=#{todoNotifySwitch} where id=#{userId}"
    )
    void updateTodoNotifySwitch(@Param("userId") int userId, @Param("todoNotifySwitch") int todoNotifySwitch);

    @Select("SELECT todoNotifySwitch FROM UserInfo WHERE id = #{userId}")
    Integer getTodoNotifySwitch(@Param("userId") int userId);


    @Update(
            "update UserInfo set token=#{userToken}, expiredTime=#{expiredTime} where id=#{userId}"
    )
    void addToken(@Param("userId")int userId, @Param("userToken")String userToken, @Param("expiredTime")Date expiredTime);


    @Update("update UserInfo set wechatName=#{wechatName}, userHeadPic=#{userHeadPic} where id=#{userId} ")
    void updateUserWechat(@Param("userId") int userId,
                          @Param("wechatName") String wechatName,
                          @Param("userHeadPic") String userHeadPic);

    @Update("<script>update UserInfo set userName=#{userName}, <when test='mobile!=null'>mobile=#{mobile},</when> userRegion=#{userRegion}, userType=1," +
            "edc=#{edcDate} where id=#{userId}</script>")
    void updatePregancyInfo(@Param("userId") int userId,
                            @Param("userName") String userName,
                            @Param("mobile") String mobile,
                            @Param("userRegion") String userRegion,
                            @Param("edcDate") Date edcDate);


    @Update("<script>update UserInfo set userName=#{userName}, <when test='mobile!=null'>mobile=#{mobile},</when> userRegion=#{userRegion}, userType=2 where id=#{userId}</script> ")
    void updateLactationInfo(@Param("userId") int userId,
                            @Param("userName") String userName,
                            @Param("mobile") String mobile,
                            @Param("userRegion") String userRegion);


    @Update("update UserInfo set paOpenId=#{paOpenId} WHERE id=#{userId} ")
    void updatePublicAccountOpenIdByUserId(@Param("userId") int userId,
                             @Param("paOpenId") String paOpenId);


}
