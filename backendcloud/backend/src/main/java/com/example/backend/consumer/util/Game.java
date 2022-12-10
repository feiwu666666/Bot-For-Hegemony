package com.example.backend.consumer.util;

import com.alibaba.fastjson.JSONObject;
import com.example.backend.consumer.WebSocketServer;
import com.example.backend.pojo.Bot;
import com.example.backend.pojo.Record;
import com.example.backend.pojo.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    private final Integer cols;
    private final Integer rows;
    private final int[][] g;
    private final Integer inner_walls_count;
    private final static int[] fx = {-1,0,1,0};
    private final static int[] fy = {0,-1,0,1};
    private final Player playerA,playerB;
    private Integer nextStepA = null;
    private String status = "playing"; // "playing"表示正在游戏中，finished表示游戏结束
    private String loser = ""; // A：A败 B:B败 all：平局

    // 如果判断之后  next step为空  说明无路可走 ，判死亡
    private Integer nextStepB = null;
    // 定义一个锁 防止读写冲突
    private ReentrantLock lock  = new ReentrantLock();
    private final static String addBotUrl = "http://127.0.0.1:3002/bot/add/";

    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Bot botA, Integer idB, Bot botB) {
        this.cols = cols;
        this.rows = rows;
        this.g = new int[rows][cols];
        this.inner_walls_count = inner_walls_count;
        Integer aBotId = -1, bBotId = -1;
        String aBotCode = "", bBotCode = "";
        if(botA != null){
            aBotId = botA.getId();
            aBotCode = botA.getContent();
        }

        if(botB != null ){
            bBotId = botB.getId();
            bBotCode  = botB.getContent();
        }

        playerA = new Player(idA,aBotId,aBotCode, rows-2,1,new ArrayList<>());
        playerB = new Player(idB,bBotId,bBotCode, 1,cols-2,new ArrayList<>());
    }
    public void setNextStepA(Integer nextStepA){
        lock.lock();
        try{
            this.nextStepA= nextStepA;
        }finally{
            lock.unlock();
        }
    }
    public void setNextStepB(Integer nextStepB){
        lock.lock();
        try{
            this.nextStepB = nextStepB;
        }finally{
            lock.unlock();
        }

    }
    public Player getPlayerA(){
        return playerA;
    }

    public Player getPlayerB(){
        return playerB;
    }
    // Flood fill算法 计算连通块
    private boolean check_connectivity(int sx,int sy,int zx,int zy){
        if(sx == zx && sy == zy) {
            return true;
        }
        g[sx][sy] = 1;

        for(int i=0 ;i < 4;i ++){
            int x = sx + fx[i];
            int y = sy + fy[i];
            if(x >= 0 && x < this.rows && y >= 0 && y<this.cols && g[x][y]==0){
                if(check_connectivity(x,y,zx,zy)){
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }
        g[sx][sy] = 0;
        return false;
    }
    public int[][] getG(){   // 返回地图
        return g;
    }
    private boolean draw(){  // 生成障碍物
        for(int i = 0;i < this.rows ; i++){
            for(int j = 0;j < this.cols; j++){
                g[i][j] = 0;
            }
        }
        for(int i = 0;i < this.rows;i ++){
            g[i][0] = g[i][this.cols-1] = 1;
        }
        for(int j=0 ;j<this.cols;j++){
            g[0][j] = g[this.rows-1][j]  =1;
        }
        Random random = new Random();
        for(int i = 0;i <this.inner_walls_count/2 ;i ++){
            for(int j = 0;j < 1000 ;j ++){
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if(g[r][c] == 1 || g[this.rows - r - 1][this.cols - c - 1] == 1){
                    continue;   // 如果当前位置存在墙体  则进行下一次随机生成
                }
                if(r == this.rows - 2 && c == 1){
                    continue;
                }
                if(r== 1 && c == this.cols -2){
                    continue;
                }
                g[r][c] = g[this.rows - 1 -r][this.cols - 1 - c] = 1;
                break;
            }
        }
        return check_connectivity(this.rows-2,1,1,this.cols-2);
    }

    public void createGameMap(){
        for(int i = 0; i <1000; i++){
            if(draw()) {
                break;
            }
        }
    }
    // 获取整个地图的所有信息
    private String getInput(Player player){ // 将当前局面 格式化成字符串
        // 字符串格式: 地图信息#我的x#我的y#(我的操作序列)#对手的x#对手的y#（对手的操作序列)
        Player me,you;
        if(player.getId().equals(playerA.getId())){
            me = playerA;
            you = playerB;
        }else {
            me = playerB;
            you = playerA;
        }
        return getMapString() + "#"
                + me.getSx() + "#"
                +me.getSy()+ "#"
                + "(" + me.getStepString() + ")#"
                + you.getSx() + "#"
                + you.getSy() + "#"
                + "(" + you.getStepString() + ")";
    }
    // 发送bot信息给botrunning服务
    public void sendBotCode(Player player) {

        // 如果botId为-1 则为人工手动操作 直接退出函数
        if(player.getBotId().equals(-1)){
            return;
        }
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getId().toString());
        data.add("bot_code", player.getBotCode());
        data.add("input",getInput(player));

        WebSocketServer.restTemplate.postForObject(addBotUrl,data,String.class);
    }
    // 获取下一步输入
    public boolean nextStep() {
        try {
            Thread.sleep(200);
            // 因为前端每秒渲染五格  所以要休眠最短 200ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendBotCode(playerA);
        sendBotCode(playerB);
        for(int i=0;i < 50 ; i++){
            try {
                Thread.sleep(100);
                // 如果5s之后还未输入就判失败，  可以增大循环次数减小sleep时间来优化用户体验

                lock.lock();
                try{
                    if(nextStepA != null && nextStepB != null){    // 如果双方都准备好了下一步，就将方向传入steps中
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                }finally{
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
    private void sendAllMessage(String message){
        if (WebSocketServer.users.get(playerA.getId()) != null) {
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        }
        if (WebSocketServer.users.get(playerB.getId()) != null) {
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
        }

    }
    private void sendMove(){   // 向两个client发送移动信息
        // 由于要用到nextstepa、b元素  所以要加锁
        lock.lock();

        try{
            JSONObject resp = new JSONObject();
            resp.put("event","move");
            resp.put("a_direction",nextStepA);
            resp.put("b_direction",nextStepB);

            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        }finally{
            lock.unlock();
        }
    }
    private String getMapString(){
        StringBuilder res = new StringBuilder();
        for(int i =0 ;i < rows; i++){
            for(int j = 0; j < cols; j++){
                res.append(g[i][j]);
            }
        }

        return res.toString();
    }
    private void updateRating(Player player, Integer rating){
        User user = WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);
    }
    private void saveToDatabase(){
        // 玩家A的天梯分
        Integer ratingA =WebSocketServer.userMapper.selectById(playerA.getId()).getRating();
        // 玩家B的天梯分
        Integer ratingB = WebSocketServer.userMapper.selectById(playerB.getId()).getRating();
        // 随机生成一个随机数 每局随机加减天梯分  随机值尽量小一点  因为匹配时是按照天梯分进行匹配  两者天梯分差别过大 匹配时间增长
        Integer randomRating = (int)(Math.random() * 10);
        // 如果一方失败  对应进行加减分  如果是平局的话  则不进行操作
        if("A".equals(loser)){
            ratingA -= randomRating;
            ratingB += randomRating;
        }else if("B".equals(loser)){
            ratingA += randomRating;
            ratingB -= randomRating;
        }
        // 将天梯分进行更新
        updateRating(playerA, ratingA);
        updateRating(playerB, ratingB);
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepString(),
                playerB.getStepString(),
                getMapString(),
                loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }
    private void sendResult(){  // 向两位玩家广播游戏结果
        JSONObject resp = new JSONObject();
        resp.put("event","result");
        resp.put("loser",loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }
    private boolean check_valid(List<Cell> cellsA,List<Cell> cellsB){ //
        int n = cellsA.size();
        Cell cell = cellsA.get(n-1);  // 取出头节点
        if(g[cell.x][cell.y] == 1){  // 判断头节点是否合法
            return false;
        }
        for( int i=0; i<n-1; i++){ // 判断A头节点是否撞击自己的身体
            if(cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y){
                return false;
            }
        }
        for(int i=0 ;i < n-1 ;i ++){
            if(cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y){
                return false;
            }
        }
        return true;
    }
    private void judge(){   // 判断行为是否合法
        List<Cell> cellsA =playerA.getCells();
        List<Cell> cellsB =playerB.getCells();

        boolean validA = check_valid(cellsA,cellsB);
        boolean validB = check_valid(cellsB,cellsA);
        if(!validA || !validB){
            status = "finished";
            if(!validA && !validB){
                loser = "all";
            }else if(!validA){
                loser = "A";
            }else{
                loser  = "B";
            }
        }
    }
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(int i=0;i < 1000; i++){  // 13*14格子，最多600步，这里循环1000回合防止出现意外
            if (nextStep()) {
                judge();
                if(status.equals( "playing")) {
                    sendMove();
                }else{
                    sendResult();
                    break;
                }


            }else{
                status = "finished";   // 判定游戏结束
                lock.lock();
                try{
                    if(nextStepA == null && nextStepB == null){
                        loser = "all";
                    }else if(nextStepA == null){
                        loser = "A";
                    }else {
                        loser = "B";
                    }
                }finally{
                    lock.unlock();
                }
                sendResult(); //  游戏结束后  将游戏结果发送给玩家
                break;

            }
        }
    }
}
