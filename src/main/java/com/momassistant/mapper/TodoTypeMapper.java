package com.momassistant.mapper;

import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.TodoType;
import org.apache.ibatis.annotations.*;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Mapper
public interface TodoTypeMapper {
    @Select("SELECT * FROM TodoLog WHERE id = #{typeId}")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "nextId",  column = "nextId"),
            @Result(property = "preId",  column = "preId"),
            @Result(property = "titleTemplate",  column = "titleTemplate"),
            @Result(property = "contentTemplate",  column = "contentTemplate"),
            @Result(property = "urlTemplate",  column = "urlTemplate"),
    })
    TodoType findById(@Param("typeId") int typeId);

    @Select("SELECT * FROM TodoLog WHERE preId = #{preId}")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "nextId",  column = "nextId"),
            @Result(property = "preId",  column = "preId"),
            @Result(property = "titleTemplate",  column = "titleTemplate"),
            @Result(property = "contentTemplate",  column = "contentTemplate"),
            @Result(property = "urlTemplate",  column = "urlTemplate"),
            @Result(property = "todoDay",  column = "todoDay"),
    })
    TodoType findByPreId(@Param("preId") int preId);

    @Select("SELECT * FROM TodoLog WHERE mainTypeId = #{mainTypeId} and minTodoDay >= #{minTodoDay}")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "nextId",  column = "nextId"),
            @Result(property = "preId",  column = "preId"),
            @Result(property = "titleTemplate",  column = "titleTemplate"),
            @Result(property = "contentTemplate",  column = "contentTemplate"),
            @Result(property = "urlTemplate",  column = "urlTemplate"),
            @Result(property = "todoDay",  column = "todoDay"),
    })
    TodoType findByMainTypeIdAndMinTodoDay(@Param("mainTypeId") int mainTypeId, @Param("minTodoDay") int minTodoDay);
}
