<!--
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2023-03-01 15:45:58
 * @LastEditTime: 2023-03-09 11:50:03
 * @FilePath: \web\src\views\user\info\UserInfoIndexView.vue
-->
<template>
    <div class="container">
      <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                        <img :src="data.user_photo" alt="图片不存在" style="width: 100%;">
                        <hr style="margin:1vh 0px"/>
                        <div class="username-display">{{ data.username }}
                        </div>
                        <hr style="margin:1vh 0px"/>
                        <div class="row">
                          <div class="col-6">
                            <div class="user_info_title">
                               好友人数
                            </div>
                            <div class="user_info_bottom" >
                              {{ data.user_friendnum }}
                            </div>
                          </div>
                          <div class="col-6">
                            <div class="user_info_title">
                              访问人数
                            </div>
                            <div class="user_info_bottom">
                              {{ data.visitor }}
                            </div>
                          </div>
                        </div>
                        <div style="display:flex;justify-content: center; align-items: center;">
                          <button v-if="data.user_id == $store.state.user.id" type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#ChangePhotoModal">更改头像</button>
                          <button v-else-if="data.connection == 0" type="button" class="btn btn-danger" @click="click_delete_friend">删除好友</button>
                          <button v-else-if="data.connection == 1" type="button" class="btn btn-success" @click="click_apply_add_friend">添加好友</button>
                          <button v-else-if="data.connection == 2" type="button" class="btn btn-info" disabled>申请中</button>
                          <button v-if="data.user_id == $store.state.user.id" type="button" class="btn btn-primary" style="margin-left:0.5vw"
                          data-bs-toggle="modal" data-bs-target="#FriendApply" @click="click_friend_apply">好友申请</button>
                        </div>

                    </div>

                    <!-- 好友申请模态框  -->
                    <div class="modal fade" id="FriendApply" tabindex="-1" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h1 class="modal-title fs-5">好友申请</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                          </div>
                          <div class="modal-body">
                            <div class="friend_apply_list">

                              <table class="table" style="padding: 0px;">
                                <thead>
                                  <tr>
                                    <td style="text-align: center" scope="col">用户</td>
                                    <td style="text-align: center" scope="col">信息</td>
                                    <td style="text-align: center" scope="col">操作</td>
                                  </tr>
                                </thead>
                                <tbody>
                                  <tr v-for="friend in friend_apply_list" :key="friend.myId" class="friend_apply_item">
                                    <td style="padding: 0px; padding-top: 0.5vh;"><img style="width: 3.5vw;border-radius: 50%;" :src="friend.myPhoto"/> &nbsp;{{ friend.myName }}</td>
                                    <td style="text-align: center;padding: 2vh 0px;color: grey;">请求加为好友</td>
                                    <td style="padding-right: 0px;">
                                      <button type="button" class="btn btn-danger">拒绝</button>&nbsp;
                                      <button type="button" class="btn btn-success" @click="click_accept_friend_apply(friend.myId)">同意</button>
                                    </td>
                                  </tr>
                                </tbody>
                              </table>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>

                    <!-- 更改头像模态框 -->
                    <div class="modal fade" id="ChangePhotoModal" tabindex="-1">
                        <div class="modal-dialog">
                            <div class="modal-content">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5" id="exampleModalLabel">更改头像</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form>
                                <div class="mb-3">
                                    <label for="head-sculpture" class="col-form-label">请输入图像的地址</label>
                                    <textarea class="form-control" id="head-sculpture" v-model="new_head"></textarea>
                                </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                <button type="button" class="btn btn-primary" @click="click_update_head">确认更改</button>
                            </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-9">
              <user-rating-echart :my_rating="data.rating" :rating_rank="data.rating_rank"></user-rating-echart>
            </div>
          </div>

    </div>

</template>


<script>
import $  from 'jquery'
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import { useStore } from 'vuex'
import { Modal } from 'bootstrap/dist/js/bootstrap.js'
import { update_head, apply_add_friend, delete_friend, accept_friend_apply  } from "@/hooks/update_user_info"
import UserRatingEchart from './UserRatingEchart.vue'

export default {
  components:{
    UserRatingEchart
  },
  setup(){
    const route = useRoute()
    const data = ref({})
    const new_head = ref('')
    const store = useStore()
    const friend_apply_list = ref([])

    // 获取用户信息
    $.ajax({
      url:"http://127.0.0.1:3000/user/friend/friendinfo/",
          type:'post',
          headers : {
              Authorization: "Bearer " + store.state.user.token,
          },
          data:{
            user_id: route.params.userId
          },
          success(resp){
            data.value = JSON.parse(resp)
            console.log(data)
          },
          error(resp){
            console.log(resp)
          }
    })

    const click_friend_apply = () => {
      $.ajax({
        url:"http://127.0.0.1:3000/user/friend/friendapply/",
        type:'get',
        headers : {
              Authorization: "Bearer " + store.state.user.token,
        },
        success(resp){
          friend_apply_list.value = resp
          console.log(resp)
        },
        error(resp){
          console.log(resp)
        }
      })

    }

    const click_apply_add_friend = () => {
      apply_add_friend(route.params.userId)
      data.value.connection = 2
    }

    const click_delete_friend = () => {
      delete_friend(route.params.userId)
      data.value.connection = 1
    }
    const click_update_head = () =>{
      update_head(new_head.value)
      new_head.value = ""
      Modal.getInstance("#ChangePhotoModal").hide();
    }
    const click_accept_friend_apply = (userId) => {
      accept_friend_apply(userId)
      Modal.getInstance('#FriendApply').hide()
    }
    return {
      data,
      new_head,
      click_update_head,
      click_apply_add_friend,
      click_delete_friend,
      click_friend_apply,
      friend_apply_list,
      click_accept_friend_apply
    }
  }



}
</script>

<style>
.username-display{
  display: flex;
  justify-content: center;
  font-weight: bold;
}
.col-6{
  font-size:13px;
}
.user_info_title{
  display: flex;
  justify-content: center;
  align-items: center;
}
.user_info_bottom{

  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
