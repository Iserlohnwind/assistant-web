package com.momassistant.mapper;

import com.momassistant.mapper.model.TodoLog;
import com.momassistant.mapper.model.TodoType;
import org.apache.ibatis.annotations.*;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Mapper
public interface TodoTypeMapper {
    @Select("SELECT * FROM TodoType WHERE id = #{typeId}")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "nextId",  column = "nextId"),
            @Result(property = "preId",  column = "preId"),
            @Result(property = "title",  column = "title"),
            @Result(property = "mainType",  column = "mainType"),
    })
    TodoType findById(@Param("typeId") int typeId);

    @Select("SELECT * FROM TodoType WHERE preId = #{preId}")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "nextId",  column = "nextId"),
            @Result(property = "preId",  column = "preId"),
            @Result(property = "title",  column = "title"),
            @Result(property = "todoDay",  column = "todoDay"),
            @Result(property = "mainType",  column = "mainType"),
    })
    TodoType findByPreId(@Param("preId") int preId);

    @Select("SELECT * FROM TodoType WHERE mainType = #{mainType} and todoDay > #{minTodoDay} ORDER BY todoDay asc LIMIT 1")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "nextId",  column = "nextId"),
            @Result(property = "preId",  column = "preId"),
            @Result(property = "title",  column = "title"),
            @Result(property = "todoDay",  column = "todoDay"),
            @Result(property = "mainType",  column = "mainType"),
    })
    TodoType findByMainTypeIdAndMinTodoDay(@Param("mainType") int mainType, @Param("minTodoDay") int minTodoDay);
}
