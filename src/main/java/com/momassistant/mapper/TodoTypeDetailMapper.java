package com.momassistant.mapper;

import com.momassistant.mapper.model.TodoType;
import com.momassistant.mapper.model.TodoTypeDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by zhufeng on 2018/8/14.
 */
@Mapper
public interface TodoTypeDetailMapper {
    @Select("SELECT * FROM TodoTypeDetail WHERE typeId = #{typeId}")
    @Results({
            @Result(property = "id",  column = "id"),
            @Result(property = "title",  column = "title"),
            @Result(property = "content",  column = "content"),
            @Result(property = "keyword",  column = "keyword")
    })
    List<TodoTypeDetail> findByTypeId(@Param("typeId") int typeId);
}
