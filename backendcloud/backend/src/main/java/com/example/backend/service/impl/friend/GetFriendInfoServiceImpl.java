package com.example.backend.service.impl.friend;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.backend.mapper.FriendMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.Friend;
import com.example.backend.pojo.User;
import com.example.backend.service.impl.util.UserDetailsImpl;
import com.example.backend.service.user.friend.GetFriendInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class GetFriendInfoServiceImpl implements GetFriendInfoService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendMapper friendMapper;

    @Override
    public String getfriendinfo(Integer userId) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)
        SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginuser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User me = loginuser.getUser();

        JSONObject data = new JSONObject();
        User user = userMapper.selectById(userId);
        data.put("user_id",user.getId());
        data.put("username",user.getUsername());
        data.put("user_photo",user.getPhoto());
        data.put("user_rating",user.getRating());

        // 获取两个用户之间的关系
        QueryWrapper<Friend> connectionWrapper = new QueryWrapper<>();
        connectionWrapper.select("friend_status").eq("my_id",me.getId()).eq("friend_id",userId);
        Integer status = 1;
        if(friendMapper.selectOne(connectionWrapper) != null){
            status = friendMapper.selectOne(connectionWrapper).getFriendStatus();
        }

        data.put("connection",status);

        // 获取用户的好友数量
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("my_id",userId).eq("friend_status",0);
        data.put("user_friendnum",friendMapper.selectCount(queryWrapper));
        data.put("visitor",user.getVisitors());
        // 返回用户的rating积分  并且返回每个分数区间的个数
        data.put("rating",user.getRating());
        //0-1450 1450-1550 1550-1600 1600-1700 1700-1800 1800-∞
//        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
//        userQueryWrapper.between("rating",0,1450);

        Long arr[] = new Long[6];
        arr[0] = userMapper.selectCount(new QueryWrapper<User>().between("rating",0,1450));
        arr[1] = userMapper.selectCount(new QueryWrapper<User>().between("rating",1450,1550));
        arr[2] = userMapper.selectCount(new QueryWrapper<User>().between("rating",1550,1600));
        arr[3] = userMapper.selectCount(new QueryWrapper<User>().between("rating",1600,1700));
        arr[4] = userMapper.selectCount(new QueryWrapper<User>().between("rating",1700,1800));
        arr[5] = userMapper.selectCount(new QueryWrapper<User>().between("rating",1800,100000));
        data.put("rating_rank",arr);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",user.getId());
        // 每当有用户进入信息页面   浏览人数 + 1
        updateWrapper.set("visitors",user.getVisitors() + 1);
        userMapper.update(null,updateWrapper);
        return data.toJSONString();
    }
}
