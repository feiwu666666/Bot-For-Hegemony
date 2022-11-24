package com.example.backend.service.impl.bot;

import com.example.backend.mapper.BotMapper;
import com.example.backend.pojo.Bot;
import com.example.backend.pojo.User;
import com.example.backend.service.impl.util.UserDetailsImpl;
import com.example.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    BotMapper botMapper;
    @Override
    public Map<String, String> update(Map<String, String> data) {

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();


        String title = data.get("title");
        String content = data.get("content");
        String description = data.get("description");

        Map<String,String> map = new HashMap<>();

        if(title == null || title.length() == 0){
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

        Bot bot = botMapper.selectById(data.get(("bot_id")));
        if(bot == null){
            map.put("error_message","Bot不存在或者已被删除");
        }
        if(!bot.getUserId().equals(user.getId())){
            map.put("error_message","没有权限修改Bot");
            return map;
        }

        Bot new_bot = new Bot(
                bot.getId(),
                user.getId(),
                title,
                description,
                content,
                bot.getCreatetime(),
                new Date()
        );
        botMapper.updateById(new_bot);
        map.put("error_message","success");
        return map;

    }
}
