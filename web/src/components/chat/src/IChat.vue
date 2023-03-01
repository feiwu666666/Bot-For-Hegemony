<!--
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2023-01-02 12:10:32
 * @LastEditTime: 2023-02-23 17:03:18
 * @FilePath: \web\src\components\chat\src\IChat.vue
-->
<template>
  <div class="chat">
    <div class="chat-nav">
      <chat-nav @handleSelect="handleSelectFriend"></chat-nav>
    </div>
    <div class="chat-content">
      <chat-content :receiverId="select_friend"></chat-content>
    </div>
  </div>
</template>

<script>
import chatContent from './chat-element/chat-content.vue'
import chatNav from './chat-element/chat-nav.vue'
import { ref } from 'vue'


export default {
  components:{
    chatContent,
    chatNav
  },
  setup(){

    const select_friend = ref()
    const handleSelectFriend = (id) => {
      select_friend.value = id
    }
    return {
      handleSelectFriend,
      select_friend
    }
  }

}
</script>

<style scope>
.chat{
  height: 100%;
}
.chat > .chat-nav{
  height:100%;
  float:left;
  width: 40%;
  background-color: #E8E8E8;
}
.chat > .chat-content{
  height: 100%;
  float:right;
  width: 60%;
  background-color: white;
}
</style>
