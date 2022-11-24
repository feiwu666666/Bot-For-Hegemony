package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.pojo.Bot;
import com.example.backend.pojo.Record;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface RecordMapper extends BaseMapper<Record> {

}