package com.example.backend.controller.ranklist;


import com.alibaba.fastjson.JSONObject;
import com.example.backend.service.ranklist.GetRankListService;
import com.example.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetRankListController {
    @Autowired
    private GetRankListService getRankListService;

    @PostMapping("/api/ranklist/getlist/")
    public JSONObject getList(@RequestParam Map<String,String> data){
        Integer page = Integer.parseInt(data.get("page"));
        return getRankListService.getList(page);
    }
}
