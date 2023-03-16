package com.example.backend.controller.user.friend;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.User;
import com.example.backend.service.user.friend.GetFriendInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class GetFriendInfoController {

    @Autowired
    private GetFriendInfoService getFriendInfoService;

    @PostMapping("/api/user/friend/friendinfo/")
    public String getFriendInfo(@RequestParam Map<String,String> message){

        Integer user_id = Integer.valueOf(message.get("user_id"));
        return getFriendInfoService.getfriendinfo(user_id);

    }

}
