package com.example.backend.consumer.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player{
    private Integer id;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps;

    private boolean check_tail_increasing(int step){  // 检查蛇当前回合是否变长
        if(step <= 10) {
            return true;
        }
        return step % 3 == 1;
    }

    public List<Cell> getCells(){  // 获取蛇的每一格
        int step  =0;
        List<Cell> res = new ArrayList<Cell>();
        int[] dx = {-1,0,1,0},dy = {0,1,0,-1};
        int x = sx,y = sy;
        for(int d: steps){
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x,y));
            if(!check_tail_increasing( ++ step)){  // 如果当前回合不增长  移除蛇尾
                res.remove(0);
            }
        }
        return res;
    }
    public String getStepString(){
        StringBuilder res = new StringBuilder();
        for(int d: steps){
            res.append(d);
        }
        return res.toString();
    }

}
