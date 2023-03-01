<!--
 * @Author: Cyan_Breeze
 * @Description:  聊天导航栏
 * @Date: 2023-01-02 18:28:42
 * @LastEditTime: 2023-02-28 23:41:43
 * @FilePath: \web\src\components\chat\src\chat-element\chat-nav.vue
-->
<template>
  <div class="chat-me-info">
    <!-- 我的头像和用户名 -->
    <div class="chat-me-info-userphoto">
      <img :src="$store.state.user.photo"/>
      <!-- <i class="bi bi-check-circle-fill online"></i> -->
    </div>
    <div class="chat-me-info-username">
      {{ $store.state.user.username }}
    </div>
  </div>
  <div class="chat-search">
    <!-- 搜索好友 -->
    <div class="chat-search-input" style="display:flex">
      <input type="text" class="form-control" style="margin-left:1%" placeholder="输入用户名"/>
      <span class="input-group-add" style="width:30%;background-color:#D8D9D8;margin:0 2%;cursor: pointer;">
        <i class="bi bi-search" style="display:flex;justify-content:center;align-items:center"></i>
      </span>
    </div>
    <!-- 添加用户 -->
    <div class="chat-search-friend"></div>
  </div>
  <div class="chat-friend-list">
    <div v-for="user in friendList" :key="user.friendId" class="friends" :id="user.friendId" @click="select_friend(user.friendId)">
      <img :src="user.friendPhoto"/>
      <div class="friend-name">
        {{user.friendName}}
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useStore } from 'vuex'


export default {

  emits:['handleSelect'],
  setup(props,{ emit }){
    const store = useStore()
    console.log(store)
    const friendList = ref([])
    console.log(store.state.user.friends)
    friendList.value = store.state.user.friends
    const select_friend = (id) => {
      emit('handleSelect',id)
      const beforeSelect = document.getElementsByClassName('friends')
      for(let i = 0;i < beforeSelect.length;i ++){
        if(beforeSelect[i].id == id){
          beforeSelect[i].classList.add('active')
        }else{
          beforeSelect[i].classList.remove('active')
        }
      }
    }
    return {
      friendList,
      select_friend,
    }
  }

}
</script>

<style>
.chat-friend-list > .active{
  background-color: #c2c8d1;
}
div.friends:hover{
  background-color: #D1D1D1;
}

span :hover {
  background-color: #D1D1D1;
}
.bi-search{
  height: 100%;
  display:flex;
  justify-content: center;
  align-items: center;
}
.chat-search-input{
  margin-bottom: 2%;
  width: 100%;
}
.chat-me-info-username{
  width: 65%;
  display:inline-block
}
.friend-name{
  display: inline-flex;
}
.chat-me-info-userphoto{
  display: inline-block;
}
.chat-me-info-userphoto > img{
  width: 6vh;
  height: 6vh;
  border-radius: 50%;
  margin: 0.5vh;
  z-index: 0;
}
.chat-friend-list > div > img{
  margin: 0.3rem;
  width: 6vh;
  height: 6vh;
  border-radius: 50%;
}
</style>
