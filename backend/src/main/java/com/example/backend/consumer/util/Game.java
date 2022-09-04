package com.example.backend.consumer.util;

import com.alibaba.fastjson.JSONObject;
import com.example.backend.consumer.WebSocketServer;
import com.example.backend.pojo.Record;

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

    public Game(Integer rows, Integer cols, Integer inner_walls_count,Integer idA,Integer idB) {
        this.cols = cols;
        this.rows = rows;
        this.g = new int[rows][cols];
        this.inner_walls_count = inner_walls_count;
        playerA = new Player(idA,rows-2,1,new ArrayList<>());
        playerB = new Player(idB,1,cols-2,new ArrayList<>());
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
            for(int j = 0;j < 10000 ;j ++){
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
    public boolean nextStep() {
        try {
            Thread.sleep(200);     // 因为前端每秒渲染五格  所以要休眠最短 200ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i=0;i < 50; i++){
            try {
                Thread.sleep(100);  // 如果5s之后还未输入就判失败，  可以增大循环次数减小sleep时间来优化用户体验

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
        WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }
    private void sendMove(){   // 向两个client发送移动信息
        // 由于要用到nextstepa、b元素  所以要加锁
        System.out.println("move");
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

    private void saveToDatabase(){
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
        System.out.println(n);
        Cell cell = cellsA.get(n-1);  // 取出头节点
        System.out.println(cell);
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
        System.out.println("judge");
        List<Cell> cellsA =playerA.getCells();
        List<Cell> cellsB =playerB.getCells();

        boolean validA = check_valid(cellsA,cellsB);
        boolean validB = check_valid(cellsB,cellsA);
        if(!validA || !validB){
            status = "finished";
            if(!validA && !validB){
                loser = "all";
            }else if(!validB){
                loser = "B";
            }else if(!validA){
                loser  = "A";
            }
        }
    }
    @Override
    public void run() {
        for(int i=0;i < 1000; i++){  // 13*14格子，最多600步，这里循环1000回合防止出现意外
            if (nextStep()) {
                judge();
                if(status.equals( "playing")) {
                    sendMove();
                }else{
                    sendResult();
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
