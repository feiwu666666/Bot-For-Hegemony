package com.example.backend.service.impl.friend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.mapper.FriendMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.Friend;
import com.example.backend.pojo.User;
import com.example.backend.service.impl.util.UserDetailsImpl;
import com.example.backend.service.user.friend.ApplyAddFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ApplyAddFriendServiceImpl implements ApplyAddFriendService {
    /**
     *
     * @param userId 其他用户的id
     */
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public Map<String,String> applyAddFriend(Integer userId) {
        // 获取当前用户自己的id
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser =(UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        // 获取另一个用户的所有信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        User friend = userMapper.selectOne(queryWrapper);
        // 创建两者之间的关系 0好友 1非好友且未申请 2非好友但申请中
        Friend connection = new Friend(user.getId(),userId,2,
                friend.getUsername(),friend.getPhoto(),user.getPhoto(),user.getUsername());
        // 将两者关系插入到数据库中 实现持久化储存

        friendMapper.insert(connection);
        Map<String,String> map = new LinkedHashMap<>();
        map.put("message","success");
        return map;
    }
}
