/*
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2023-03-05 14:53:24
 * @LastEditTime: 2023-03-07 21:57:51
 * @FilePath: \web\src\hooks\update_user_info.js
 */
import $ from 'jquery'
import store from '@/store/index'

export const update_head = (new_head) => {
  $.ajax({
    url: 'http://127.0.0.1:3000/user/account/updateHead/',
    type: 'post',
    headers: {
      Authorization: 'Bearer ' + store.state.user.token
    },
    data: {
      new_head: new_head
    },
    success(resp) {
      if (resp.error_message === 'success') {
        store.state.user.photo = new_head
        console.log(store.state.user.photo)
        // new_head.value = ""
        // Modal.getInstance("#ChangePhotoModal").hide();
      }
    },
    error(resp) {
      console.log(resp)
    }
  })
}

// 申请添加好友
export const apply_add_friend = (userId) => {
  $.ajax({
    url: 'http://127.0.0.1:3000/user/friend/apply/',
    type: 'post',
    headers: {
      Authorization: 'Bearer ' + store.state.user.token
    },
    data: {
      user_id: userId
    },
    success(resp) {
      return resp
    }
  })
}
// 删除好友
export const delete_friend = (userId) => {
  $.ajax({
    url: 'http://127.0.0.1:3000/user/friend/delete/',
    type: 'post',
    headers: {
      Authorization: 'Bearer ' + store.state.user.token
    },
    data: {
      user_id: userId
    },
    success(resp) {
      store.commit("updateFriends",resp)
    }
  })
}

// 同意添加好友
export const accept_friend_apply = (userId) =>{
  console.log("同意")
  $.ajax({
    url:'http://127.0.0.1:3000/user/friend/acceptapply',
    type:'post',
    headers: {
      Authorization: 'Bearer ' + store.state.user.token
    },
    data:{
      user_id:userId
    },
    success(resp){
      store.commit("updateFriends",resp)
      console.log(resp)
    },
    error(resp){
      console.log(resp)
    }
  })

}
