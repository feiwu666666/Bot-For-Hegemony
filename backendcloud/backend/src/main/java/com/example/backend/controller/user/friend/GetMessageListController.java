package com.example.backend.controller.user.friend;


import com.example.backend.pojo.Message;
import com.example.backend.service.user.friend.GetMessageListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetMessageListController {
    @Autowired
    private GetMessageListService getMessageListService;
    @GetMapping("/user/friend/getchatlog/")
    public List<Message> getChatlog(){
        return getMessageListService.getMessageList();
    }
}
