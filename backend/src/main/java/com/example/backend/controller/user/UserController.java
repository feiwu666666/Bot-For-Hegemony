package com.example.backend.controller.user;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    // 如果需要用到数据库的mapper接口  就要带上auto wired注解

    @Autowired
    UserMapper userMapper;

    /**
     * getMapping  映射get请求
     * 获取所有用户的信息
     * @return
     */
    @GetMapping("/user/all/")
    public List<User> getAll(){
        return userMapper.selectList(null);
    }
    // 获取某个id用户的信息

    /**
     * 获取某个固定id的用户信息
     * @param userId
     * @return
     */
    @GetMapping("user/{userId}/")
    public User getuser(@PathVariable int userId) {

        //  return userMapper.selectById(userId);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        return userMapper.selectOne(queryWrapper);
    }


    /**
     *
     * @param userId
     * @param username
     * @param password
     * @向数据库中添加一条数据
     * @return
     */
    @GetMapping("user/add/{userId}/{username}/{password}/")
    public String addUser(
            @PathVariable int userId,
            @PathVariable String username,
            @PathVariable String password
    ){
        // 密码采用加密方式存入数据库

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodepassword = passwordEncoder.encode(password);
        User user = new User(userId,username,encodepassword);
        userMapper.insert(user);
        return "Add User Successfully";
    }
    /**
     * 删除用户
     */
    @GetMapping("/user/delete/{userId}/")
    public String deleteUser(@PathVariable int userId){
        userMapper.deleteById(userId);
        return "Delete User Successfully";
    }


}
