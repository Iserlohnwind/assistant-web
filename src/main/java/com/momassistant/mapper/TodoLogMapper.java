package com.momassistant.mapper;

import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Mapper
public interface TodoLogMapper {
    @Select("SELECT * FROM TodoLog WHERE id > #{minId} and mainTypeId=#{mainTypeId} and status=1 order by id asc limit 100")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "userId",  column = "userId"),
            @Result(property = "openId",  column = "openId"),
            @Result(property = "typeId",  column = "typeId"),
            @Result(property = "babyId",  column = "babyId"),
            @Result(property = "babyName",  column = "babyName"),
            @Result(property = "sendTime",  column = "sendTime"),
            @Result(property = "dataJson",  column = "dataJson"),
            @Result(property = "url",  column = "url"),

    })
    List<TodoLog> paginateLogs(@Param("minId") int minId, @Param("mainTypeId") int mainTypeId);


    @Insert("INSERT INTO TodoLog(userId, openId, babyId, babyName, typeId, mainTypeId, dataJson, url, sendTime) VALUES(#{userId},#{openId},#{babyId},#{babyName},#{typeId},#{mainTypeId}, #{dataJson}, #{url}, #{sendTime})")
    @Options(useGeneratedKeys=true,keyProperty="id")
    void insertLog(TodoLog todoLog);

    @Update("UPDATE TodoLog set typeId=#{typeId},sendTime=#{sendTime},dataJson=#{dataJson},url=#{url} WHERE userId=#{userId}")
    void updateLog(TodoLog todoLog);


    @Delete("DELETE FROM TodoLog  WHERE userId=#{userId}")
    void deleteLogByUserId(@Param("userId")int userId);

    @Delete("DELETE FROM TodoLog  WHERE babyId=#{babyId}")
    void deleteLogByBabyId(@Param("babyId")int babyId);

}
