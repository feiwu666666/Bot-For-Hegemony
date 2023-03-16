package com.example.backend.controller.user.friend;

import com.alibaba.fastjson.JSONObject;
import com.example.backend.service.user.friend.ApplyAddFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ApplyAddFriendController {
    @Autowired
    private ApplyAddFriendService applyAddFriendService;

    @PostMapping("/api/user/friend/apply/")
    public Map<String,String> applyaddfriend(@RequestParam Map<String,String> data){
        Integer userId = Integer.valueOf(data.get("user_id"));
        return applyAddFriendService.applyAddFriend(userId);
    }

}
