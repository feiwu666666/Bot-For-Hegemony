package com.example.backend.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.backend.consumer.util.Game;
import com.example.backend.consumer.util.JwtAuthentication;
import com.example.backend.mapper.RecordMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    //每一个WebSocketServer实例都开启一个新的线程


    // ConcurrentHashMap用来映射线程安全的哈希map

    final public static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

    // 维护线程安全的线程池  当作匹配池


    private User user;

    private Game game = null;
    private Session session = null;

    // 定义传给匹配系统服务的链接
    private final static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";


    // 由于WebSocketServer不是spring中的类库（非单例模式）,所以不能直接注入
    /**
     * 非单例模式下 注入Mapper的方法
     */
    private static RestTemplate restTemplate;
    private static UserMapper userMapper;
    public static RecordMapper recordMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper = recordMapper;
    };
    // 用session维护前端传来的链接

    // 与另一个微服务通信
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate = restTemplate;
    }



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

    public static void startGame(Integer aId,Integer bId){
        User a = userMapper.selectById(aId);
        User b = userMapper.selectById(bId);

        Game game = new Game(13,14,20,a.getId(),b.getId());
        game.createGameMap();
        // 如果玩家在匹配中途退出了匹配 users。get的结果为空 需要特判


        if(users.get(a.getId()) != null){
            users.get(a.getId()).game = game;
        }
        if(users.get(b.getId()) != null){
            users.get(b.getId()).game = game;
        }

        game.start();
        JSONObject respGame = new JSONObject();
        respGame.put("a_id",game.getPlayerA().getId());
        respGame.put("a_sx",game.getPlayerA().getSx());
        respGame.put("a_sy",game.getPlayerA().getSy());
        respGame.put("b_id",game.getPlayerB().getId());
        respGame.put("b_sx",game.getPlayerB().getSx());
        respGame.put("b_sy",game.getPlayerB().getSy());
        respGame.put("gamemap",game.getG());

        // 匹配成功之后 将双方信息发送给前端

        JSONObject respA = new JSONObject();
        respA.put("event","start-matching");
        respA.put("opponent_username",b.getUsername());
        respA.put("opponent_photo",b.getPhoto());
        respA.put("game",respGame);
        if(users.get(a.getId()) != null){
            users.get(a.getId()).sendMessage(respA.toJSONString());
        }

        JSONObject respB = new JSONObject();
        respB.put("event","start-matching");
        respB.put("opponent_username",a.getUsername());
        respB.put("opponent_photo",a.getPhoto());
        respB.put("game",respGame);
        if(users.get(b.getId()) != null){
            users.get(b.getId()).sendMessage(respB.toJSONString());
        }
    }



    private void startMatching(){
        System.out.println("start");
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();

        // 将匹配系统需要用到的信息传给匹配系统云服务

        data.add("user_id",this.user.getId().toString());
        data.add("rating",this.user.getRating().toString());

        restTemplate.postForObject(addPlayerUrl,data,String.class);
    }
    private void stopMatching(){
        System.out.println("stop");
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl,data,String.class);
    }

    private void move(int direction){  // 设置方向
        if(game.getPlayerA().getId().equals(this.user.getId())){     // 判断当前蛇是哪一方
            game.setNextStepA(direction);
        }else if(game.getPlayerB().getId().equals(this.user.getId())){
            game.setNextStepB(direction);
        }

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
        }else if("move".equals(event)){
            move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message){
        // 给session加上互斥锁，让其他线程等待， 直到事件完成 锁被释放，其他线程才能操作锁中数据
        synchronized (this.session){
            try{
                // 将当前message发送到当前绑定的session中，即向前端发送信息
                if(session.isOpen()){
                    this.session.getBasicRemote().sendText(message);

                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}