package com.dm.ss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dm.ss.domain.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    //根据用户id查询权限
    List<String> selectPermsByUserId(@Param("id") Long id);

}