<!--
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2022-09-20 22:18:21
 * @LastEditTime: 2022-12-10 14:12:11
 * @FilePath: \web\src\components\MatchGround.vue
-->
<template>
  <div class="matchground">
    <div class="row">
      <div class="col-4">
        <div class="user-photo">
          <img :src="$store.state.user.photo" alt="图片加载失败" />
        </div>
        <div class="uesr-username">{{ $store.state.user.username }}</div>
      </div>

      <div class="col-4">
        <div class="user-select-bot">
          <select class="user-select-bot" :disabled="match_btn_info==='开始匹配'? false:true" v-model="select_bot">
            <option selected value="-1">亲自出手</option>
            <option v-for="bot in bots" :key="bot.id"  :value="bot.id">{{ `${bot.title}出战` }}</option>
          </select>
        </div>
      </div>
      <div class="col-4">
        <div class="user-photo">
          <img :src="$store.state.pk.opponent_photo" alt="图片加载失败" />
        </div>
        <div class="uesr-username">{{ $store.state.pk.opponent_username }}</div>
      </div>

      <div class="col-12" style="text-align: center; margin-top: 15vh">
        <button
          @click="click_match_btn"
          class="btn btn-warning btn-lg"
          type="button"
        >
          {{ match_btn_info }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
import { useStore } from "vuex";
import $ from 'jquery'

export default {
  setup() {
    const store = useStore();
    let match_btn_info = ref("开始匹配");
    let bots = ref([]);
    let select_bot = ref('-1')
    const click_match_btn = () => {
      if (match_btn_info.value === "开始匹配") {
        match_btn_info.value = "取消匹配";
        store.state.pk.socket.send(
          JSON.stringify({
            event: "start-matching",
            bot_id: select_bot.value
          })
        );
      } else {
        match_btn_info.value = "开始匹配";
        store.state.pk.socket.send(
          JSON.stringify({
            event: "stop-matching",
          })
        );
      }
    };
    const refresh_bots  = () => {
      $.ajax({
          url:'https://app2803.acapp.acwing.com.cn/api/user/bot/getlist/',
          type:"get",
          headers :{
              Authorization: "Bearer " + store.state.user.token,
          },

          success(resp){
              bots.value = resp;
          }
      });
    }
    refresh_bots()
    return {
      match_btn_info,
      click_match_btn,
      bots,
      select_bot
    };
  },
};
</script>

<style scoped>
div.matchground {
  width: 60vw;
  height: 70vh;
  margin: 40px auto;
  background-color: rgba(50, 50, 50, 0.5);
}
div.user-photo {
  margin-top: 13vh;
  text-align: center;
}
div.user-photo > img {
  border-radius: 50%;
  width: 20vh;
}
div.uesr-username {
  margin-top: 1vh;
  text-align: center;
  font-size: 24px;
  font-weight: bold;
}
div.user-select-bot{
  margin-top:25vh;
}
.user-select-bot > select{
  width: 50%;
  font-size:x-large;
  margin:0 0 0 60px;
  text-align: center;
}
</style>
