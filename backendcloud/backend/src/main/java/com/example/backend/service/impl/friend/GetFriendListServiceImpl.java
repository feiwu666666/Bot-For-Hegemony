package com.example.backend.service.impl.friend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.mapper.FriendMapper;
import com.example.backend.pojo.Friend;
import com.example.backend.pojo.User;
import com.example.backend.service.impl.util.UserDetailsImpl;
import com.example.backend.service.user.friend.GetFriendListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetFriendListServiceImpl implements GetFriendListService {
    @Autowired
    private FriendMapper friendMapper;

    @Override
    public List<Friend> getFriendList() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        User user = userDetails.getUser();
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("my_id",user.getId());
        return friendMapper.selectList(queryWrapper);

    }
}
