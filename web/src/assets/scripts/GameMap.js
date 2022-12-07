import { Snake } from "./Snake";


import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject{
    constructor (ctx,parent,store){ // ctx画布 , parent 画布的父元素
        super();
        this.store = store;
        this.ctx = ctx;
        this.parent = parent; // 父元素
        this.L = 0; // 一个单位的长度
        //最好使行列相加为奇数，避免两条蛇下一步进入到同一个格子中
        //如果地图为13 * 13 双方起点为(11,1),(1,1)，  同为奇数，同为偶数，双蛇使可能进入到同一个格子中的
        //如果地图为13 * 14 双方起点为（11,1),(1,13)  一奇一偶 双蛇每一步不可能会进入到同一个格子
        // 所以地图尺寸的长宽需要一个奇数一个偶数
        this.rows = 13;
        this.cols = 14;

        this.inner_walls_count = 20;  // 假设地图中有20个障碍物
        this.walls = []; //墙体

        // 注册两个蛇
        this.snakes = [
            new Snake({id:0,color:"#4876EC", r : this.rows-2, c : 1},this),
            new Snake({id:1,color:"#F94848",r : 1,c : this.cols-2},this)
        ];

    }
    create_walls() {
        const g = this.store.state.pk.gamemap;
        for (let r = 0; r < this.rows; r ++ ) {
            for (let c = 0; c < this.cols; c ++ ) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
    }

    add_listen_events(){
        this.ctx.canvas.focus(); // 使canvas聚焦

        // const [snake0] = this.snakes;
        this.ctx.canvas.addEventListener("keydown", e => {
            let d = -1;
            if (e.key === 'w') d = 0;
            else if (e.key === 'd') d = 1;
            else if (e.key === 's') d = 2;
            else if (e.key === 'a') d = 3;

            if(d >= 0){
                this.store.state.pk.socket.send(JSON.stringify({
                    event: "move",
                    direction: d
                }))
            }
        });
// canvas绑定键盘监听事件e
    }
    start(){
        this.create_walls();
        this.add_listen_events();
    }
    update_size(){
        // min中的值可能为浮点型，会导致每个墙体之间会出现缝隙，可以用parseInt取整，去除缝隙
        this.L = parseInt(Math.min(this.parent.clientWidth/this.cols,this.parent.clientHeight/this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

     // 判断两条蛇是否都准备好了进入下一回合
    check_ready(){
        for(let snake of this.snakes){
            if(snake.status !== 'idle') return false;  // 状态不为idle时  不再进行操作
            if(snake.direction === -1) return false;
        }
        return true;
    }
    next_step(){
        for(const snake of this.snakes){
            snake.next_step();
        }
    }

    check_valid(cell){ // 检测目标位置是否合法  :没有撞到蛇身或者障碍物
        for(const wall of this.walls){
            if(wall.r === cell.r && wall.c === cell.c){
                return false;
            }
        }
        for(const snake of this.snakes){
            let k = snake.cells.length;
            if(!snake.check_tail_increasing()){  // 如果蛇不增长， 长度要减一
                k--;
            }
            for(let i = 0 ;i < k ;i++){
                if(snake.cells[i].r === cell.r && snake.cells[i].c === cell.c){
                    return false;
                }
            }

        }
        return true;

    }
    update(){
        this.update_size();
        if(this.check_ready()){
            this.next_step();
        }
        this.render();
    }

    render(){
        const even_color = "#AAD751",old_color = "#A2D149";
        for(let r = 0; r < this.rows; r++){

            for(let c = 0; c<this.cols; c++){
                if((r+c) % 2 == 0){
                    this.ctx.fillStyle = even_color;
                }else{
                    this.ctx.fillStyle = old_color;
                }
                this.ctx.fillRect(this.L * c,this.L * r,this.L,this.L);
            }
        }
    }

}
