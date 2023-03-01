package com.example.backend.controller.user.friend;

import com.example.backend.pojo.Friend;
import com.example.backend.service.user.friend.GetFriendListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetFriendListController {
    @Autowired
    private GetFriendListService getFriendListService;
    @GetMapping("/user/friend/friendlist/")
    public List<Friend> getfriendlist(){
        return getFriendListService.getFriendList();
    }
}
