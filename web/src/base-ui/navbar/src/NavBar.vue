<!--
 * @Author: Cyan_Breeze
 * @Description:导航栏
 * @Date: 2022-09-20 22:18:21
 * @LastEditTime: 2023-02-20 22:13:40
 * @FilePath: \web\src\base-ui\navbar\src\NavBar.vue
-->
<template>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container">
          <router-link class="navbar-brand" :to="{name:'home'}">最强AI</router-link>
          <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
           <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarText">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                <router-link :class="route_name=='pk_index' ? 'nav-link active':'nav-link'" :to="{name:'pk_index'}">对战</router-link>
                </li>
                <li class="nav-item">
                <router-link :class="route_name=='record_index'?'nav-link active':'nav-link'" :to="{name:'record_index'}">对局列表</router-link>
                </li>
                <li class="nav-item">
                <router-link :class="route_name=='ranklist_index'?'nav-link active':'nav-link'" :to="{name:'ranklist_index'}">排行榜</router-link>
                </li>
            </ul>
            <ul class="navbar-nav" v-if="$store.state.user.is_login" >
                <li class="nav-item dropdown" style="display:flex;justify-content:right;align-items: center;">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
                    role="button" data-bs-toggle="dropdown" aria-expanded="false" style="width:10%;padding: 0;justify-content: center;align-items: center;display: flex;">
                        <!-- {{ $store.state.user.username }} -->
                      <img :src="$store.state.user.photo" alt="图片无法加载" style="width: 70%;border-radius: 50%;" />
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown" style="margin-left:78%">
                      <li>
                        <router-link :class="route_name=='user_info_index'?'dropdown-item active':'dropdown-item'" :to="{name:'user_bot_index'}" >个人信息</router-link>
                      </li>
                      <li>
                        <router-link :class="route_name=='user_bot_index'?'dropdown-item active':'dropdown-item'" :to="{name:'user_bot_index'}" >我的Bot</router-link>
                      </li>
                      <li><hr class="dropdown-divider"></li>
                      <li><a class="dropdown-item" href="" @click="logout" >退出登录</a>

                      </li>
                    </ul>
                      <!--聊天功能入口-->
                    <a data-bs-toggle="offcanvas" href="#offcanvasChat" role="button" aria-controls="offcanvasChat"
                    style="justify-content:center;display: flex;width:8%">
                      <img src="@/assets/message.png" style="width: 90%;">
                    </a>
                    <!-- <button class="btn btn-primary" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasRight" aria-controls="offcanvasRight">右侧侧边栏</button> -->
                </li>
            </ul>
            <ul class="navbar-nav" v-else-if="!$store.state.user.pulling_info">
                <li class="nav-item">
                    <router-link class="nav-link " :to="{name:'user_account_login'}"  role="button"  aria-expanded="false">
                        登录
                    </router-link>
                </li>
                <li class="nav-item ">
                    <router-link class="nav-link" :to="{name:'user_account_register'}"  role="button"  aria-expanded="false">
                        注册
                    </router-link>
                </li>
            </ul>
          </div>
      </div>

    </nav>
    <!-- 聊天室侧边导航栏页面设置-->
    <div class="offcanvas offcanvas-end text-bg-dark" tabindex="-1" id="offcanvasChat" aria-labelledby="offcanvasChatLabel" v-if="$store.state.user.is_login">
      <div class="offcanvas-header" style="background-color:black;">
        <h5 id="offcanvasRightLabel" style="margin-bottom: 0px;">
          <img src="@/assets/message.png" style="width:10%;border-radius: 50%;" />
          YourChat
        </h5>
        <button type="button" class="btn-close text-reset btn-close-white" data-bs-dismiss="offcanvas" aria-label="Close"></button>
      </div>
      <div class="offcanvas-body" style="padding:0px;color:black">
        <IChat />
      </div>
    </div>
</template>


<script>
import { useRoute } from 'vue-router';
import { computed } from 'vue';
import store from '@/store';

import IChat from '@/components/chat';
export default{
  components: {
    IChat
  },
  setup(){
        const route = useRoute();  // 取出当前route
        let route_name = computed(() => route.name) // 实时获取当前route的name值
        const logout = () =>{
            store.dispatch("logout");
         }
        return {
            route_name,logout
        }
    },

}
</script>

<style scoped>
</style>
