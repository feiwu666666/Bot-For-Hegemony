package com.example.kob.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

// 前后端分离  向服务端传数据时用ResController 注解

@RestController
@RequestMapping("/pk/")
public class BotInfoController {

    @RequestMapping("getbotinfo/")
    public Map<String,String > getBotInfo(){
        List<Map<String,String>> list = new LinkedList<>();
        Map<String,String> bot1 = new HashMap<>();
        bot1.put("name","Jack");
        bot1.put("rating","1003");
        return bot1;
    }

}
