package com.momassistant.mapper;

import org.apache.ibatis.annotations.Insert;

import java.util.Date;

public interface BabyInfoMapper {

    @Insert(
            "insert into BabyInfo(userId,babyName,babyGender,babyBirthday) " +
                    "values (#{userId},#{babyName},#{babyGender},#{babyBirthday})"
    )
    void insertBabyInfo(int userId, String babyName, int babyGender, Date babyBirthday);
}
