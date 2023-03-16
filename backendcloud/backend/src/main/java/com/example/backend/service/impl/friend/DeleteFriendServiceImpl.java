package com.example.backend.service.impl.friend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.mapper.FriendMapper;
import com.example.backend.mapper.MessageMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.Friend;
import com.example.backend.pojo.Message;
import com.example.backend.pojo.User;
import com.example.backend.service.impl.util.UserDetailsImpl;
import com.example.backend.service.user.friend.DeleteFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteFriendServiceImpl implements DeleteFriendService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Override
    public List<Friend> deletefriend(Integer userId) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser =(UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        // 删除当前用户和好友之间的关系
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("my_id",user.getId()).eq("friend_id",userId);
        queryWrapper.or();
        queryWrapper.eq("friend_id",user.getId()).eq("my_id",userId);
        friendMapper.delete(queryWrapper);
        // 删除两个用户之间的聊天记录
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
        messageQueryWrapper.eq("senderid",user.getId()).eq("receiverid",userId);
        messageQueryWrapper.or();
        messageQueryWrapper.eq("senderid",userId).eq("receiverid",user.getId());

        messageMapper.delete(messageQueryWrapper);
        // 删除好友之后  返回新好友数组给前端
        QueryWrapper<Friend> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("my_id",user.getId()).eq("friend_status",0);
        return friendMapper.selectList(queryWrapper1);
    }
}
