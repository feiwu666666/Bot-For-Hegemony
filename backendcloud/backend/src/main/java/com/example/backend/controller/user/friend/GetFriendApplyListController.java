package com.example.backend.controller.user.friend;

import com.example.backend.pojo.Friend;
import com.example.backend.service.user.friend.GetFriendApplyListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetFriendApplyListController {
    @Autowired
    private GetFriendApplyListService getFriendApplyListService;

    @GetMapping("/api/user/friend/friendapply/")
    public List<Friend> getFriendApplyList(){
        return getFriendApplyListService.getFriendApplyList();
    }
}
