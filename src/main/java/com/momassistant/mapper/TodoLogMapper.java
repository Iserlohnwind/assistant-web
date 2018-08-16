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
    @Select("SELECT * FROM TodoLog WHERE id > #{minId} and status=1 order by id asc limit 100")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "userId",  column = "userId"),
            @Result(property = "typeId",  column = "typeId"),
            @Result(property = "sendTime",  column = "sendTime"),
    })
    List<TodoLog> paginateLogs(@Param("minId") int minId);


    @Insert("INSERT INTO TodoLog(userId, typeId, node, sendTime) VALUES(#{userId},#{typeId}, #{node}, #{sendTime})")
    @Options(useGeneratedKeys=true,keyProperty="id")
    void insertLog(TodoLog todoLog);

    @Update("UPDATE TodoLog set typeId=#{typeId},sendTime=#{sendTime} WEHRE userId=#{userId}")
    void updateLog(TodoLog todoLog);

}
