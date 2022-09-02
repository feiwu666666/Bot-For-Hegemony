package com.example.backend.consumer.util;

import java.util.Random;

public class Game {
    final private Integer cols;
    final private Integer rows;
    final private int[][] g;
    final private Integer inner_walls_count;
    final private static int[] fx = {-1,0,1,0};
    final private static int[] fy = {0,-1,0,1};

    public Game(Integer rows, Integer cols, Integer inner_walls_count) {
        this.cols = cols;
        this.rows = rows;
        this.g = new int[rows][cols];
        this.inner_walls_count = inner_walls_count;
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
                if(!check_connectivity(x,y,zx,zy)){
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }
        g[sx][sy]  =0;
        return false;
    }
    public int[][] getG(){   // 返回地图
        return g;
    }
    private boolean draw(){
        for(int i = 0;i < this.rows ; i++){
            for(int j = 0;j < this.cols; j++){
                this.g[i][j] = 0;
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

}
