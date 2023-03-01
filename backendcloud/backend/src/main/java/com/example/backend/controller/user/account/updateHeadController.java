package com.example.backend.controller.user.account;

import com.example.backend.service.user.account.UpdateHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;


@RestController
public class updateHeadController {
    @Autowired
    UpdateHeadService updateHeadService;

    @PostMapping("/user/account/updateHead")
    public Map<String,String> updateHead(@RequestParam Map<String,String> data){
        Map<String,String> map = new LinkedHashMap<>();
        // 获取前端传来的图片地址
        String headInfo = data.get("new_head");
        System.out.println(headInfo);
        map.put("head_sculpture",headInfo);
        return updateHeadService.updateHead(map);
    }

}
