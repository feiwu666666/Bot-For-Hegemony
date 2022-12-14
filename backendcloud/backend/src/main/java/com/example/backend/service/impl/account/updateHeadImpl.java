package com.example.backend.service.impl.account;

import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.User;
import com.example.backend.service.impl.util.UserDetailsImpl;
import com.example.backend.service.user.account.UpdateHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class updateHeadImpl implements UpdateHeadService {
    @Autowired
    UserMapper userMapper;

    @Override
    public Map<String, String> updateHead(Map<String, String> data) {
        Map<String,String> map = new LinkedHashMap<>();
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        // 获取到当前的用户信息
        User user = loginUser.getUser();
        // 取出前端传过来的图片地址  并且进行更改

        user.setPhoto(data.get("head_sculpture"));
        userMapper.updateById(user);

        map.put("error_message","success");
        return map;
    }
}
