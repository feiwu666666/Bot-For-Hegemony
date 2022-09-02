package com.example.backend.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.backend.consumer.util.Game;
import com.example.backend.consumer.util.JwtAuthentication;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    //每一个WebSocketServer实例都开启一个新的线程


    // ConcurrentHashMap用来映射线程安全的哈希map

    final private static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

    // 维护线程安全的线程池  当作匹配池

    final private static CopyOnWriteArraySet matchpool = new CopyOnWriteArraySet();

    private User user;


    // 由于WebSocketServer不是spring中的类库（非单例模式）,所以不能直接注入
    /**
     * 非单例模式下 注入Mapper的方法
     */
    private static UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }

    // 用session维护前端传来的链接

    private Session session = null;


    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 从前端获取链接信息
        this.session = session;
        System.out.println("connected!");
        // 建立连接
        Integer userId = JwtAuthentication.getId(token);
        this.user = userMapper.selectById(userId);
        // 找到用户后 将用户id映射到map中
        if(this.user != null){
            users.put(userId,this);
        }else{
            this.session.close();  // 如果没有找到token对应的用户  就断开该连接
        }
        System.out.println(users);
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("close");
        if(this.user != null){
            users.remove(this.user);
        }

    }

    private void startMatching(){
        System.out.println("start");
        matchpool.add(this.user);
        while(matchpool.size() >= 2){
            Iterator<User> it = matchpool.iterator();   // 创建一个迭代器，迭代存储线程池中的用户
            User a = it.next(),b = it.next();
            matchpool.remove(a);
            matchpool.remove(b);

            // 创建地图
            Game game = new Game(13,14,20);
            game.createGameMap();


            // 匹配成功之后 将双方信息发送给前端

            JSONObject respA = new JSONObject();
            respA.put("event","start-matching");
            respA.put("opponent_username",b.getUsername());
            respA.put("opponent_photo",b.getPhoto());
            respA.put("gamemap",game.getG());
            users.get(a.getId()).sendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event","start-matching");
            respB.put("opponent_username",a.getUsername());
            respB.put("opponent_photo",a.getPhoto());
            respB.put("gamemap",game.getG());
            users.get(b.getId()).sendMessage(respB.toJSONString());

        }
    }
    private void stopMatching(){
        System.out.println("stop");
        matchpool.remove(this.user);
    }

    @OnMessage
    public void onMessage(String message, Session session) {   //当作路由 用来判断前后端通信内容
        // 从Client接收消息
        System.out.println("receive message");

        JSONObject data = JSON.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)){
            startMatching();
        }else if("stop-matching".equals(event)){
            stopMatching();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message){
        // 给session加上互斥锁，让其他线程等待， 直到事件完成 锁被释放，其他前程才能操作锁中数据
        synchronized (this.session){
            try{
                // 将当前message发送到当前绑定的session中，即向前端发送信息
                this.session.getBasicRemote().sendText(message);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}