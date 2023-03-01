package com.example.backend.service.user.friend;

import com.example.backend.pojo.Message;
import org.springframework.stereotype.Service;

import java.util.List;


public interface GetMessageListService {
    List<Message> getMessageList();
}
