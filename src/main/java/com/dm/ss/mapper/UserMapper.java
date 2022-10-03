package com.dm.ss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dm.ss.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-02 18:20:12
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
