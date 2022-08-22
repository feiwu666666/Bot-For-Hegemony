export class Cell{
    constructor(r,c){
        this.r = r;
        this.c = c;
        this.x = c + 0.5; // 切换坐标系  x轴为行 y轴为列
        this.y = r + 0.5; // x,y * L为圆心的 绝对坐标

    }
}