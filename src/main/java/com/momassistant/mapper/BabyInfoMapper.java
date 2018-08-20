package com.momassistant.mapper;

import com.momassistant.mapper.model.BabyInfo;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface BabyInfoMapper {

    @Insert(
            "insert into BabyInfo(userId,babyName,babyGender,babyBirthday) " +
                    "values (#{userId},#{babyName},#{babyGender},#{babyBirthday})"
    )
    void insertBabyInfo(@Param("userId")int userId, @Param("babyName")String babyName, @Param("babyGender") int babyGender, @Param("babyBirthday") Date babyBirthday);


    @Select("SELECT * FROM BabyInfo WHERE userId=#{userId}")
    @Results({
            @Result(property = "userId",  column = "userId"),
            @Result(property = "babyId",  column = "id"),
            @Result(property = "babyName",  column = "babyName"),
            @Result(property = "babyBirthday",  column = "babyBirthday"),
            @Result(property = "babyGender",  column = "babyGender"),
    })
    List<BabyInfo> findByUserId(@Param("userId") int userId);

    @Select("SELECT * FROM BabyInfo WHERE userId=#{userId} and id=#{babyId}")
    @Results({
            @Result(property = "userId",  column = "userId"),
            @Result(property = "babyId",  column = "id"),
            @Result(property = "babyName",  column = "babyName"),
            @Result(property = "babyBirthday",  column = "babyBirthday"),
            @Result(property = "babyGender",  column = "babyGender"),
    })
    BabyInfo findByUserIdAndBabyId(@Param("userId") int userId, @Param("babyId") int babyId);
}
