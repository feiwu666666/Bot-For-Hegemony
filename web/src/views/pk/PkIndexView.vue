<!--
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2022-11-24 17:10:28
 * @LastEditTime: 2023-03-02 13:58:47
 * @FilePath: \web\src\views\pk\PkIndexView.vue
-->
<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
    <ResultBoard v-if="$store.state.pk.loser !== 'none'" />

</template>

<script>
import { onMounted } from 'vue';
import { useStore } from 'vuex'
import PlayGround from '../../components/PlayGround.vue'
import MatchGround from '../../components/MatchGround.vue';
import ResultBoard from '../../components/ResultBoard.vue'

export default{
    components:{
    PlayGround,
    MatchGround,
    ResultBoard,
    },
    setup(){
        const store = useStore();
        // 访问后端3000 端口  websocket服务
        // const socketUrl = `wss://app2803.acapp.acwing.com.cn/websocket/${store.state.user.token}/`;
        let socket = null;
        store.commit("updateLoser","none");

        store.commit("updateIsRecord", false)
        onMounted(() => {
            store.commit("updateOpponent",{
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            })
            // if(socket != null){
            //   socket.close()
            // }
            // socket = new WebSocket(socketUrl); // 前端创建一个WebSocket链接匹配后端的websocket接口

            // socket.onopen = () => {
            //     store.state.user.socket = socket;
            // }
            socket = store.state.user.socket

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                if(data.event === 'start-matching'){ // 匹配成功
                    store.commit("updateOpponent",{
                        username:data.opponent_username,
                        photo: data.opponent_photo,
                    });
                    setTimeout( () => {
                        store.commit("updateStatus","playing")
                        store.commit("updateGame",data.game);
                    },2000); // 匹配成功之后改变status状态  显示pk界面  两秒之后显示pk界面

                }else if(data.event === "move"){
                    const game = store.state.pk.gameobject;
                    const [snake0,snake1] = game.snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);

                }else if(data.event === "result"){
                    const game = store.state.pk.gameobject;
                    const [snake0,snake1] = game.snakes;

                    if(data.loser === "all" || data.loser === "A"){
                        snake0.status ="die";
                       }
                    if(data.loser ==="all" || data.loser === "B"){
                        snake1.status = "die";
                    }
                    store.commit("updateLoser",data.loser);
                    //  角色死亡默认游戏结束 状态进入匹配状态
                    store.commit("updateStates","matching")
                    // 如果是好友发送回来的信息 则将其保存进store的聊天记录中
                }
                if(data.super_event === "send_message"){
                  console.log(data)
                  store.dispatch("addchatlog",{
                    sendtime: data.sendtime,
                    senderid: data.senderid,
                    receiverid:data.receiverid,
                    sendcontent:data.sendcontent,
                    messageStatus: 0,
                    messageType: 0
                  })
                  console.log("接收成功")
                }

            }
            // socket.onclose = () => {
            //     store.commit("updateStatus","matching")
            // }
        })
        // onUnmounted(() => {
        //     socket.close();
        // })
        // window.close()
    }
}
</script>

<style scoped>
</style>
