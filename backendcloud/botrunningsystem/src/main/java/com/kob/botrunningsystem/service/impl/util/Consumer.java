package com.kob.botrunningsystem.service.impl.util;

import com.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class Consumer extends Thread{
    private Bot bot;
    private static RestTemplate restTemplate;
    private final static String receiveBotMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move/";
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }
    public void startTimeout(long timeout, Bot bot){
        this.bot = bot;
        // 启动当前线程
        this.start();

        // 当前线程最长等待timeout毫秒 如果超时将自动切断线程
        try {
            this.join(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 切断线程
            this.interrupt();
        }
    }
    private String addUid(String code, String uid){
        int k = code.indexOf(" implements com.kob.botrunningsystem.utils.BotInterface");
        return code.substring(0,k) + uid +  code.substring(k);
    }


    @Override
    public void run() {
        // uuid是一个随机数  uid取一个随机数的前八位
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0,8);
        // Reflect.compile自动编译一段代码  由于该函数一个类只会执行一次，防止类名冲突 在类名后面加一个uuid
        BotInterface botInterface = Reflect.compile("com.kob.botrunningsystem.utils.Bot" + uid,
                addUid(bot.getBotCode(), uid)
        ).create().get();
        Integer move_direction = botInterface.nextMove(bot.getInput());
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        data.add("direction", move_direction.toString());

        restTemplate.postForObject(receiveBotMoveUrl,data,String.class);  // 返回结果给backend后端
    }
}
