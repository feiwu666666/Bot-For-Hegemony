package com.example.backend.service.impl.record;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.mapper.RecordMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.Record;
import com.example.backend.pojo.User;
import com.example.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class GetRecordListServiceImpl implements GetRecordListService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RecordMapper recordMapper;

    @Override
    public JSONObject getList(Integer page) {
        // Mybatis 中的一个用于分类的类  从page是访问的页数 每页10条数据
        IPage<Record> recordIPage = new Page<>(page,10);
        // 用于从数据库中查询数据
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        // 设置查询结果根据其id进行倒序排序
        queryWrapper.orderByDesc("id");
        // 查询对应结果  并且将其存储道records数组中
        List<Record> records = recordMapper.selectPage(recordIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for(Record record: records){
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            JSONObject item = new JSONObject();
            item.put("a_photo", userA.getPhoto());
            item.put("a_username", userA.getUsername());
            item.put("b_photo", userB.getPhoto());
            item.put("b_username", userB.getUsername());
            item.put("record", record);
            items.add(item);
        }
        resp.put("records",items);
        // 向前端发送对局记录总条数
        resp.put("records_count",recordMapper.selectCount(null));
        return resp;
    }
}
