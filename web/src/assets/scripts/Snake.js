import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";

export class Snake extends AcGameObject{
    constructor(info,gamemap){
        super();
        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;
        this.step = 0;

        this.cells = [new Cell(info.r,info.c)]; // cells[0]存放蛇头
        this.next_cell = null;  // 下一步的目标位置

        this.speed = 5;  // 蛇每秒走五格

        this.direction = -1; // -1表示没有指令， 0、1、2、3、表示上右下左
        this.status = "idle" // 三个状态 idle静止 move移动中， die 死亡

        this.dr = [-1,0,1,0];
        this.dc = [0,1,0,-1]; //四个方向的行列偏移量

        this.eps = 1e-2;

        this.eye_dirction = 0;
        if(this.id === 1) this.eye_dirction = 2; // 0123 上右下左
        this.eye_dx = [  // 蛇眼在 x 上的偏移量
            [-1,1],
            [1,1],
            [-1,1],
            [-1,-1]
        ];
        this.eye_dy = [  // 蛇眼在 y 上的偏移量
            [-1,-1],
            [1,-1],
            [1,1],
            [1,-1]
        ];
    }
    start(){

    }
    set_direction(d){ // 设置蛇运动的方向
        this.direction = d;
    }
    next_step(){ // 将蛇的状态变为走下一步
        const d = this.direction; // 取行动方向
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]); // 计算下一步目标位置
        this.eye_dirction = d;
        this.direction = -1; // 重置行动方向
        this.status = 'move';
        this.step ++;

        

        const k = this.cells.length; // 计算蛇共有多少节身体
        // console.log(this.cells.length);
        for(let i = k ; i > 0; i --){
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i-1])); // 遍历cells 将每个cell向后推一位
        }
        // if(!this.gamemap.check_valid(this.next_cell)){
        //     this.status = 'die';
        // }
    }
    check_tail_increasing(){ // 检测当前回合，蛇的长度是否增加
        if(this.step <= 10) return true; // 前十步  每走一步蛇增加一个长度
        if(this.step % 3 === 1) return true;  // 每走两步增加一节 防止游戏无结果
        return false;
    }
    update_move(){
        const dx = this.next_cell.x - this.cells[0].x; // x轴偏移量
        const dy = this.next_cell.y - this.cells[0].y; // y轴偏移量
        const distance = Math.sqrt(dx * dx + dy * dy);

        if(distance < this.eps){ // 当distance小于允许的误差时，我们默认它到达了该目标位置
            this.cells[0] = this.next_cell; 
            this.next_cell = null;
            this.status = 'idle';
            if(!this.check_tail_increasing()){ // 如果蛇体不增长，那么需要把尾巴去掉
                this.cells.pop();
            }
        }else{ // 如果没到达目标位置，它下一帧会继续移动
            const move_distance = this.speed * this.timedelta / 1000; // 这一帧移动的距离

            this.cells[0].x += move_distance * dx / distance; // 计算这一帧蛇头移动到哪
            this.cells[0].y += move_distance * dy / distance;
            if(!this.check_tail_increasing()){  //  蛇尾向前一位移动
                const k = this.cells.length;
                const tail = this.cells[k - 1];
                const tail_target = this.cells[k - 2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
             
        }

    }
    update(){
        if(this.status === 'move'){
            this.update_move();
        }
        this.render();
    }
    render(){
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

           
        ctx.fillStyle = this.color;
        if(this.status === 'die'){
            ctx.fillStyle = 'white';
        }
        for(const cell of this.cells){
            ctx.beginPath();  // 开启一个通道
            ctx.arc(cell.x * L, cell.y * L, L * 0.8/2, 0, Math.PI*2);
            ctx.fill();
        }

        // 填充蛇身
        for(let i = 1; i < this.cells.length ; i++){
            const a = this.cells[i - 1], b = this.cells[i];
            if(Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps) continue;
            if(Math.abs(a.x - b.x) < this.eps){
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            }else{
                ctx.fillRect(Math.min(a.x,b.x) * L , (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }

        ctx.fillStyle = 'black';

        for(let i = 0; i < 2; i++){
            // 计算两个眼睛的位置
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_dirction][i] * 0.15) * L;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_dirction][i] * 0.15) * L;

            ctx.beginPath();
            ctx.arc(eye_x, eye_y, L*0.05, 0, Math.PI * 2);// 原点x,y坐标，半径，从0开始到2Π
            ctx.fill();
        }


    }

}