<!--
 * @Author: Cyan_Breeze
 * @Description:聊天框实现
 * @Date: 2023-01-02 18:29:09
 * @LastEditTime: 2023-02-28 19:46:56
 * @FilePath: \web\src\components\chat\src\chat-element\chat-content.vue
-->
<template>
<div class="chat-frame" v-if="receiverId !== null">
    <div class="chat-frame-friendname">{{receiverName}}</div>
    <!-- 聊天记录 -->
    <div class="chat-frame-content" ref="chatContent">
      <div v-for="message in chatRecord" :key="message.sendtime" style="margin: 0.8vh" class="chat-message-body">
        <!-- 我的信息 -->

        <div class='send-photo' v-if="JSON.parse((message.senderid == $store.state.user.id ) && (message.receiverid == receiverId))">
          <img class="img-circle" :src="$store.state.user.photo" />
        </div>
        <div class='send-content' v-if="JSON.parse((message.senderid == $store.state.user.id) && (message.receiverid == receiverId))">{{ message.sendcontent }}</div>
        <!-- 对方的信息 -->

        <div class='receive-photo' v-if="JSON.parse((message.receiverid == $store.state.user.id) && (message.senderid == receiverId))">
        <img class="img-circle" :src="other_photo" />
        </div>
        <div class='receive-content' v-if="JSON.parse((message.receiverid == $store.state.user.id) && (message.senderid == receiverId))">{{ message.sendcontent }}</div>
      </div>
    </div>
    <!-- 聊天输入框 -->
    <div class="chat-frame-input" style="margin-top:0.2rem;height: 21%">
      <textarea v-model="messageContent" class="input-field" cols="50"
        style="border:none;resize: none;outline: none;background-color: rgb(245,245,245);"
        @keydown.enter="sendMessage"
        @keypress="(e) => handleInputEnter(e)"
      ></textarea>
    </div>
    <div class="send-message-btn" >
      <button style="float: right;margin-right: 1vh; border: black solid 1px;" @click="sendMessage()">发送</button>
    </div>
  </div>
</template>

<script>
import { ref, watchEffect} from 'vue'
import { useStore } from 'vuex'
export default {
  // 当页面加载完成和数据发生改变时，滚轮自动滑到底部
  updated(){
    this.scrollToBottm();
  },

  methods:{
    scrollToBottm(){
      if(this.receiverName!==null){
        this.$nextTick(() => {
          this.$refs.chatContent.scrollTop = this.$refs.chatContent.scrollHeight;
        })
      }
    }

  },
  props:{
    receiverId:{
      type: Number,
      default:null
    }
  },
  setup(props){

    const messageContent = ref('')
    const store = useStore()
    const receiverName = ref('')
    const select_friend_id = ref(props.receiverId)
    const other_photo = ref('')
    const chatRecord = ref(store.state.user.chat_log)
    const socket = store.state.user.socket

    // 监听每次信息接收者的改变
    watchEffect(() => {
      // 遍历所有好友列表  找到当前选择的好友
      store.state.user.friends.forEach((friend) => {
        if(friend.friendId === props.receiverId){
          receiverName.value = friend.friendName
          other_photo.value = friend.friendPhoto

        }
      })
    })
    // 取消输入框回车键换行功能
    const handleInputEnter = (e) => {
        if(e.keyCode === 13){
          e.preventDefault()
        }
    }
    // 发送信息功能
    const sendMessage = () => {
      if(messageContent.value === ''){
        alert('不能发送空白消息')
      }
      if(messageContent.value !== ''){
        // chatRecord.value.push({
        //   sendtime: (new Date()).valueOf(),
        //   senderid: store.state.user.id,
        //   receiverid:props.receiverId,
        //   sendcontent: messageContent.value,
        //   messageStatus: 0,
        //   messageType: 0,
        // })
        store.dispatch("addchatlog",{
          sendtime: (new Date()).valueOf(),
          senderid:store.state.user.id,
          receiverid:props.receiverId,
          sendcontent:messageContent.value,
          messageStatus: 0,
          messageType: 0
        })
        socket.send(JSON.stringify({
          event: 'send_message',
          sendtime: (new Date()).valueOf(),
          senderid:store.state.user.id,
          receiverid:props.receiverId,
          sendcontent:messageContent.value
        }))
      }
      // 发送信息完之后  输入框自动聚焦
      document.querySelector('.input-field').select()
      messageContent.value = ''
    }

    return {
      other_photo,
      select_friend_id,
      receiverName,
      chatRecord,
      messageContent,
      sendMessage,
      handleInputEnter
    }
  }
}
</script>
<style>

.chat-message-body{
  overflow: hidden;
}
.img-circle{
  height: 4vh;
  width: 4vh;
  border-radius: 50%;
}
.send-photo{
  float:right;
  /* display: inline-block; */
}
.receive-photo{
  /* display: inline-block; */
  float:left;
}
.send-content{
  margin-left:2vh;
  padding:0.8vh;
  float:right;
  display: inline-block;
  border-radius: 3px;
  background-color:#98E165;
  margin-right: 1vh;
  word-break: break-all;
  max-width: calc(100% - 4vh - 1vh - 2vh);
  font-size: 1.5vh;
}
.receive-content{
  margin-right:2vh;
  padding:0.8vh;
  float:left;
  display: inline-block;
  border-radius: 3px;
  margin-left: 1vh;
  background-color: white;
  word-break: break-all;
  max-width: calc(100% - 4vh - 1vh - 2vh);
  font-size: 1.5vh;
}
.input-field{
  height: 90%;
  width:99%;
  font-size: 1.5vh;
}

.chat-frame{
  height: 100%;
  background-color: rgb(245,245,245);
}
.chat-frame-friendname{
  height: 8%;
  display: flex;
  justify-content: center;
  align-items: center;
  border-bottom: lightgrey solid 1px;
  font-weight: 600;
}
.chat-frame-content{
  overflow-y:scroll;
  height:65%;
  border-bottom: lightgrey solid 1px;
}
</style>
