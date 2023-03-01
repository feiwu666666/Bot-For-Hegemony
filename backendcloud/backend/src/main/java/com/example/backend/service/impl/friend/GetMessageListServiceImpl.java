package com.example.backend.service.impl.friend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.mapper.MessageMapper;
import com.example.backend.pojo.Message;
import com.example.backend.pojo.User;
import com.example.backend.service.impl.util.UserDetailsImpl;
import com.example.backend.service.user.friend.GetMessageListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetMessageListServiceImpl implements GetMessageListService {
    @Autowired
    private MessageMapper messageMapper;
    @Override
    public List<Message> getMessageList() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginuser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginuser.getUser();

        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("senderid",user.getId());
        queryWrapper.or();
        queryWrapper.eq("receiverid",user.getId());
        return messageMapper.selectList(queryWrapper);
    }
}
