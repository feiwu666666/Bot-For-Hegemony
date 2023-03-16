package com.example.backend.controller.user.friend;

import com.example.backend.pojo.Friend;
import com.example.backend.service.user.friend.DeleteFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DeleteFriendController {
    @Autowired
    private DeleteFriendService deleteFriendService;

    @PostMapping("/api/user/friend/delete/")
    public List<Friend> deleteFriend(@RequestParam Map<String,String> data){
        Integer userId = Integer.valueOf(data.get("user_id"));
        return deleteFriendService.deletefriend(userId);
    }

}
