package com.kob.botrunningsystem.utils;

import java.util.ArrayList;
import java.util.List;

public class bot implements com.kob.botrunningsystem.utils.BotInterface{
    static class Cell{
        public int x,y;
        public Cell(int x,int y){
            this.x = x;
            this.y = y;
        }
    }
    private boolean check_tail_increasing(int step){  // 检查蛇当前回合是否变长
        if(step <= 10) {
            return true;
        }
        return step % 3 == 1;
    }
    // 根据蛇的起点和所有步数  找出蛇的身体
    public List<Cell> getCells(int sx, int sy, String steps){  // 获取蛇的每一格
        int step  =0;
        List<Cell> res = new ArrayList<Cell>();
        int[] dx = {-1,0,1,0},dy = {0,1,0,-1};
        int x = sx,y = sy;
        res.add(new Cell(x,y));
        for(int i = 1;i < steps.length()-1;i ++){
            int d = steps.charAt(i) - '0';
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x,y));
            if(!check_tail_increasing( ++ step)){  // 如果当前回合不增长  移除蛇尾
                res.remove(0);
            }
        }
        return res;
    }
    @Override
    public Integer nextMove(String input) {
        // input格式   ~~~~~~~~~~
        String[] strs = input.split("#");
        int[][] g = new int[13][14];
        for( int i=0,k=0;i<13;i++){ // 依据传来的input局面  将地图g还原
            for(int j=0;j<14;j++,k++){
                if(strs[0].charAt(k)=='1'){
                    g[i][j] = 1;
                }
            }
        }
        int aSx = Integer.parseInt(strs[1]),aSy = Integer.parseInt(strs[2]);
        int bSx = Integer.parseInt(strs[4]),bSy = Integer.parseInt(strs[5]);
        // 查找两条蛇的身体

        List<Cell> aCells = getCells(aSx,aSy,strs[3]);
        List<Cell> bCells = getCells(bSx,bSy,strs[6]);
        // 将两条蛇的身体进行标记
        for(Cell c: aCells) g[c.x][c.y] = 1;
        for(Cell c: bCells) g[c.x][c.y] = 1;
        int[] dx = {-1,0,1,0},dy = {0,1,0,-1};
        // 判断上下左右四个方向  返回一个可以行走的方向
        for(int i=0;i<4;i++){
            int x = aCells.get(aCells.size() - 1).x + dx[i];
            int y = aCells.get(aCells.size()  -1).y + dy[i];
            if(x >0 && x< 13 && y>0 && y<14 && g[x][y] == 0){
                return i;
            }
        }
        return 0;
    }
}
