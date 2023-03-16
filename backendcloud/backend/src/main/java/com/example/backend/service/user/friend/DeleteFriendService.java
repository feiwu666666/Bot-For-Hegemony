package com.example.backend.service.user.friend;

import com.example.backend.pojo.Friend;

import java.util.List;

public interface DeleteFriendService {
    List<Friend> deletefriend(Integer userId);
}
