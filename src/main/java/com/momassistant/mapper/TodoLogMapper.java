package com.momassistant.mapper;

import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Mapper
public interface TodoLogMapper {
    @Select("SELECT * FROM TodoLog WHERE id > #{minId} and status=1 order by id asc limit 100")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "body",  column = "body"),
    })
    TodoLog paginateLogs(@Param("minId") int minId);


    @Insert("INSERT INTO TodoLog(body) VALUES(#{body})")
    @Options(useGeneratedKeys=true,keyProperty="id")
    void insertLog(TodoLog todoLog);


    @Update("UPDATE TodoLog SET status = 0 WEHRE id=#{id}")
    void deleteLog(@Param("id") int id);

}
