package com.example.backend.service.impl.friend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.backend.mapper.FriendMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.Friend;
import com.example.backend.pojo.User;
import com.example.backend.service.impl.util.UserDetailsImpl;
import com.example.backend.service.user.friend.AcceptFriendApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcceptFriendApplyServiceImpl implements AcceptFriendApplyService {
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Friend> acceptFriendApply(Integer userId) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        User friend= userMapper.selectOne(queryWrapper);

        Friend connection = new Friend(user.getId(),userId,0,friend.getUsername(),
                friend.getPhoto(),user.getPhoto(),user.getUsername());
        UpdateWrapper<Friend> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("my_id",userId).eq("friend_id",user.getId());
        updateWrapper.set("friend_status",0);
        friendMapper.update(null,updateWrapper);
        friendMapper.insert(connection);
        QueryWrapper<Friend> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("my_id",user.getId()).eq("friend_status",0);
        return friendMapper.selectList(queryWrapper1);
    }
}
