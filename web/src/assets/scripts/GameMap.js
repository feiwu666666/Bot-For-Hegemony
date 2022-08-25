import { Snake } from "./Snake";


import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject{
    constructor (ctx,parent){ // ctx画布 , parent 画布的父元素
        super();

        this.ctx = ctx;
        this.parent = parent; // 父元素
        this.L = 0; // 一个单位的长度
        //最好使行列相加为奇数，避免两条蛇下一步进入到同一个格子中
        //如果地图为13 * 13 双方起点为(11,1),(1,1)，  同为奇数，同为偶数，双蛇使可能进入到同一个格子中的
        //如果地图为13 * 14 双方起点为（11,1),(1,13)  一奇一偶 双蛇每一步不可能会进入到同一个格子
        // 所以地图尺寸的长宽需要一个奇数一个偶数
        this.cols = 13;
        this.rows = 14;

        this.inner_walls_count = 20;  // 假设地图中有20个障碍物
        this.walls = []; //墙体
        
        // 注册两个蛇
        this.snakes = [
            new Snake({id:0,color:"#4876EC", r : this.rows-2, c : 1},this),
            new Snake({id:1,color:"#F94848",r : 1,c : this.cols-2},this)
        ];

    }
    check_connectivity (g ,sx ,sy , zx , zy){
        // 使用Flood Fill 算法寻找连通块
        if(sx == zx && sy == zy) return true; // 如果起点和终点重合 即连通
        g[sx][sy] = true; // 标记为走过的路
        let fx = [-1,0,1,0]; // 四个方向的偏移量
        let fy = [0,1,0,-1];
        for(let i = 0 ;i < 4; i++){
            let x = sx + fx[i]; // 新的起点
            let y = sy + fy[i];
            if(!g[x][y] && this.check_connectivity(g,x,y,zx,zy)){ // 如果碰到墙体或者该格走过 就不能再走了
                return true;
            }

        }
        return false;

    }
    create_walls(){
          const g = [];
          // 将所有格子都初始化
          for(let r = 0; r < this.rows ; r++){
            g[r] = [];
            for(let c = 0;c < this.cols ;c++){
                g[r][c] = false;
            }
          }
          // 将四周一圈全部设为墙，封住地图
          for(let r = 0; r< this.rows; r++){
                g[r][0] = g[r][this.cols - 1] = true;
            }
            for(let c = 0; c < this.cols ; c ++){
                g[0][c]  =g[this.rows - 1][c] = true;
            }
          //  创建随机障碍物
          // 为了展现公平性 , 创建墙体时采用中心对称的方法，双方墙体中心对称，考验双方bot的机动性
          for(let i = 0 ;i < this.inner_walls_count / 2 ; i++){  // 每次放两个，所以i对半取
            for(let j = 0;j < 100000; j++){
                let r = parseInt(Math.random() * this.rows); // random函数返回[0,1)中的随机数  乘上行列数取整，返回[0,cols)的随机值
                let c = parseInt(Math.random() * this.cols);

                if(g[r][c] || g[this.rows - 1 -r][this.cols - 1 - c])  // 如果当前格子存在墙体，则进行下一次随机
                continue;
                else if(r == this.rows - 2 && c == 1) // 如果随即墙体刷在四周，也进行下一次随机
                continue;
                else if(r == 1 && c ==this.cols - 2)
                continue;
                
                g[r][c] = g[this.rows - 1 -r][this.cols - 1 - c] = true; // 设当前随机墙体为障碍物，并将其中心对称格子同样设为障碍物
                break;
                
            }
          }


          

          let copy_g = JSON.parse(JSON.stringify(g));
          // 检查是否可以连通
          if(!this.check_connectivity(copy_g,this.rows-2 , 1, 1,this.cols - 2)) return false;

          // 绘制墙体
          for(let r = 0;r < this.rows; r++){
            for(let c = 0; c < this.cols; c++){
                if(g[r][c]){
                    this.walls.push(new Wall(r,c,this));
                }
            }
          }

          return true;  // 如果创建墙体和障碍物成功  返回true

    }
    add_listen_events(){
        this.ctx.canvas.focus(); // 使canvas聚焦

        const [snake0,snake1] = this.snakes;
        this.ctx.canvas.addEventListener("keydown", e => {
            if (e.key === 'w') snake0.set_direction(0);
            else if (e.key === 'd') snake0.set_direction(1);
            else if (e.key === 's') snake0.set_direction(2);
            else if (e.key === 'a') snake0.set_direction(3);
            else if (e.key === 'ArrowUp') snake1.set_direction(0);
            else if (e.key === 'ArrowRight') snake1.set_direction(1);
            else if (e.key === 'ArrowDown') snake1.set_direction(2);
            else if (e.key === 'ArrowLeft') snake1.set_direction(3);
        });
// canvas绑定键盘监听事件e
    }
    start(){
        for(let i = 0;i < 1000;i ++){
            if(this.create_walls()) break;
        }
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
        console.log(this.snakes.length);
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