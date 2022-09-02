<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
</template>

<script>
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex'
import PlayGround from '../../components/PlayGround.vue'
import MatchGround from '../../components/MatchGround.vue';


export default{
    components:{
    PlayGround,
    MatchGround
},
    setup(){
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;

        let socket = null;

        onMounted(() => {
            store.commit("updateOpponent",{
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            })
            socket = new WebSocket(socketUrl); // 前端创建一个WebSocket链接匹配后端的websocket接口

            socket.onopen = () => {
                store.state.pk.socket = socket;
                console.log("connected!");
            }

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                if(data.event === 'start-matching'){
                    store.commit("updateOpponent",{
                        username:data.opponent_username,
                        photo: data.opponent_photo,
                    });
                    setTimeout( () => {
                        store.commit("updateStatus","playing")
                    },2000); // 匹配成功之后改变status状态  显示pk界面  两秒之后显示pk界面
                    store.commit("updateGamemap",data.gamemap);

                }
                console.log(data);
            }
            socket.onclose = () => {
                console.log("断开");
                store.commit("updateStatus","matching")

            }

        }),
        onUnmounted(() => {
            socket.close();
        })
    }
}
</script>

<style scoped>

</style>