<!--
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2022-09-20 22:18:21
 * @LastEditTime: 2022-12-09 23:00:10
 * @FilePath: \web\src\components\PlayGround.vue
-->
<template>
    <div class="playground">
        <div class="legend">
            <div class="me">
                我方
                <div class="me-color" :style="$store.state.user.id == $store.state.pk.a_id ? 'background-color:#4876EC':'background-color:red'"></div>
            </div>
            <div class="you">
                敌方
                <div class="you-color" :style="$store.state.user.id == $store.state.pk.a_id ? 'background-color:red':'background-color:#4876EC'"></div>
            </div>
        </div>
        <GameMap />
    </div>
</template>


<script>
import GameMap from './GameMap.vue'

export default{
    components:{
        GameMap,
    },
    setup(){

    }
}
</script>


<style scoped>
div.playground{
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
}
div.legend{
    font-size: xx-large;

    position: absolute;
    top:230px;
    left: 200px;
}
.legend > .me{
    padding-bottom: 20px;
}
.legend > .you{
    padding-top: 20px;
}
.me-color{
    margin-left: 10px;
    position: relative;
    transform: translateY(35%);
    float: right;
    width: 50px;
    height: 30px;
}
.you-color{
    width: 50px;
    height: 30px;
    float: right;
    position: relative;
    transform: translateY(35%);
}
</style>
