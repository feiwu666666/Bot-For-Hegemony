package com.example.backend.service.impl.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.User;
import com.example.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        /**
         *  判断输入的用户名和密码是否符合规定
         */
        Map<String,String> map = new HashMap<>();
        if(username.length() > 100){
            map.put("error_message","用户名过长");
            return map;
        }
        if(username.length() == 0){
            map.put("error_message","用户名不能为空");
            return map;
        }
        if(username == null){
            map.put("error_message", "请输入用户名");
            return map;
        }
        // 判断username有无空白符
        if(username.contains(" ")){
            map.put("error_message","用户名不能含有空白符");
            return map;
        }
        if(password.length() == 0 || confirmedPassword.length() == 0){
            map.put("error_message","密码不能为空");
            return map;
        }
        if(password.length() > 100 || confirmedPassword.length() > 100){
            map.put("error_message","密码过长");
            return map;
        }
        if(!password.equals(confirmedPassword)){
            map.put("error_message","两次密码不一致");
            return map;
        }
        /**
         * 查询该username是否以及存在
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        List<User> users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty()){
            map.put("error_message","用户名已存在");
            return map;
        }
        // 密码进行加密
        String encode = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/63997_lg_8aaf892643.jpg";
        // 创建一个user
        User user = new User(null,username,encode,photo);
        // 将user插入到数据库中
        System.out.println("插入成功!!!!!");
        userMapper.insert(user);

        map.put("error_message","注册成功");
        return map;

    }
}
