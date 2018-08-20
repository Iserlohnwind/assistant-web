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
    @Results(id = "todoType", value = {
            @Result(property = "id",  column = "id"),
            @Result(property = "nextId",  column = "nextId"),
            @Result(property = "preId",  column = "preId"),
            @Result(property = "title",  column = "title"),
            @Result(property = "todoWeek",  column = "todoWeek"),
            @Result(property = "todoMonth",  column = "todoMonth"),
            @Result(property = "mainType",  column = "mainType"),
    })
    TodoType findById(@Param("typeId") int typeId);

    @Select("SELECT * FROM TodoType WHERE preId = #{preId}")
    @ResultMap("todoType")
    TodoType findByPreId(@Param("preId") int preId);


    @Select("SELECT * FROM TodoType WHERE mainType = #{mainType} and todoWeek >= #{todoWeek} ORDER BY todoWeek asc LIMIT 1")
    @ResultMap("todoType")
    TodoType findByMainTypeIdAndMinTodoWeek(@Param("mainType") int mainType, @Param("todoWeek") int todoWeek);

    @Select("SELECT * FROM TodoType WHERE mainType = #{mainType} and todoMonth >= #{todoMonth} ORDER BY todoMonth asc LIMIT 1")
    @ResultMap("todoType")
    TodoType findByMainTypeIdAndMinTodoMonth(@Param("mainType") int mainType, @Param("todoMonth") int todoMonth);


}
