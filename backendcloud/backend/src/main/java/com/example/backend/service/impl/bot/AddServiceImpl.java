package com.example.backend.service.impl.bot;

import com.example.backend.mapper.BotMapper;
import com.example.backend.pojo.Bot;
import com.example.backend.pojo.User;
import com.example.backend.service.impl.util.UserDetailsImpl;
import com.example.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();  // 获取token

        UserDetailsImpl userDetails = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();

        // 获取token对应的用户信息
        User user = userDetails.getUser();




        String title = data.get("title");
        String content = data.get("content");
        String description = data.get("description");

        Map<String,String> map = new HashMap<>();

        if(title == null || title.length() <= 0){
            map.put("error_message","title不能为空");
            return map;
        }
        if(title.length() > 100){
            map.put("error_message","title过长");
            return map;
        }
        if(content == null || content.length() <= 0){
            map.put("error_message","代码内容不能为空");
            return map;
        }
        if(content.length() > 1000){
            map.put("error_message","代码内容过长");
            return map;
        }
        if(description == null || description.length() <= 0){
            description = "这个用户有点懒，什么都没有留下~";
        }
        if(description.length() > 10000){
            map.put("error_message","描述过长");
            return map;
        }

        Date date = new Date();
        Bot bot = new Bot(null,user.getId(),title,description,content,date,date);

        botMapper.insert(bot);

        map.put("error_message","success");
        return map;

    }
}
