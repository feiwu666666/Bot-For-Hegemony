<template>
    <div ref="parent" class="gamemap" >
        <canvas ref="canvas" tabindex="0"></canvas> 
    </div>
    <!-- tabindex 属性 可以使canvas对象拥有键盘操作的功能 -->
</template>

<script>
import { GameMap } from "@/assets/scripts/GameMap";
import { ref , onMounted} from "vue";
import { useStore } from "vuex";
export default{
    setup(){
        const store = useStore();
        let parent = ref(null);
        let canvas = ref(null);
        // 当前页面挂载完毕之后 创建一个游戏地图
        onMounted(() => {
            store.commit("updateGameObject", new GameMap(canvas.value.getContext('2d'),parent.value,store));
            
        });
        return {
            parent,canvas
        }
    }
}
</script> 


<style scoped>
div.gamemap{
    width:100%;
    height:100%;
    display: flex;
    justify-content: center;
}
</style>