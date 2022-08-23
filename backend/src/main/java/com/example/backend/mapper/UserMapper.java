package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author SJH0628
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
