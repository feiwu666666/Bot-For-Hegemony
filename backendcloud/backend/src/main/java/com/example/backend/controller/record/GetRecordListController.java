package com.example.backend.controller.record;


import com.alibaba.fastjson.JSONObject;
import com.example.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class GetRecordListController {
    @Autowired
    private GetRecordListService getRecordListService;
    @PostMapping("/api/api/record/getlist/")
    JSONObject getList(@RequestParam Map<String,String> data){

        Integer page =Integer.parseInt(data.get("page"));

        return getRecordListService.getList(page);
    }
}