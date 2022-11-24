package com.kob.matchingsystem.service.impl.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author: SJH
 * description:匹配池  一些匹配的相关操作
 */

@Component
public class MatchingPool extends Thread{
    private static List<Player> players = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();

    private static RestTemplate restTemplate;
    private final static String startGameUrl = "http://127.0.0.1:3000/pk/start/game/";




    @Autowired
    public void  setRestTemplate(RestTemplate restTemplate){
        MatchingPool.restTemplate = restTemplate;
    }


        public void addPlayer(Integer userId,Integer rating){
        // 由于添加玩家涉及到了读写user信息，  run（）函数中涉及到读取user信息，所以会发生读写冲突，需要加锁
        lock.lock();
        try{
            players.add(new Player(userId,rating,0));
        }finally{
            lock.unlock();
        }
    }
    public void remove(Integer userId){
        lock.lock();
        try{
            List<Player> newPlayers = new ArrayList<Player>();
            for(Player p : players){
                if(!p.getUserId().equals(userId)){
                    newPlayers.add(p);
                }
            }
            players = newPlayers;
        }finally{
            lock.unlock();
        }
    }
    private boolean checkMatched(Player a,Player b){ // 判断两名玩家是否匹配
        int ratingDelta  =Math.abs(a.getRating()-b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(),b.getWaitingTime());
        return ratingDelta <= waitingTime*10;
        // 如果分差小于两个玩家最小等待时间*10，即匹配成功
    }
    private void sendResult(Player a,Player b){ // 返回匹配结果
        System.out.println("sendResult: " + a + " " + b);
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("a_id",a.getUserId().toString());
        data.add("b_id",b.getUserId().toString());
        System.out.println(a.getUserId() + b.getUserId());
        restTemplate.postForObject(startGameUrl,data,String.class);
    }
    private void increaseWaitingTime(){ // 如果没有匹配成功，等待时长加一
        for(Player p : players){
            p.setWaitingTime(p.getWaitingTime() + 1);

        }
    }
    private void matchPlayers(){   // 尝试匹配池中所有人
        System.out.println("matchPlayers" + players.toString());
        boolean[] used = new boolean[players.size()];
        for(int i = 0; i < players.size(); i++){
            if(used[i]) {
                continue;
            }
            for(int j = i+1; j < players.size(); j++){
                if(used[j]){
                    continue;
                }
                Player a = players.get(i),b = players.get(j);
                if(checkMatched(a,b)){
                    used[i] = used[j] = true;
                    sendResult(a,b);
                    break;
                }
            }
        }
        // 将匹配成功的玩家从匹配池中删除
        List<Player> newPlayers = new ArrayList<>();
        for(int i = 0; i < players.size(); i++){
            if(!used[i]){
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;
    }
    
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);
                lock.lock();
                try{
                    increaseWaitingTime();
                    matchPlayers();
                }finally{
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
