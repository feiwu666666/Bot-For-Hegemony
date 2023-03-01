/*
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2022-11-24 17:10:28
 * @LastEditTime: 2023-02-25 19:51:53
 * @FilePath: \web\src\store\user.js
 */
import $ from 'jquery'

export default({
  state: {
    id : "",
    socket: null,
    username: "",
    photo: "",
    token: "",
    friends:[],
    chat_log:[],
    is_login: false,
    pulling_info:true // 判断当前是否在拉取信息，即判断是否处在登陆状态
  },
  getters: {
  },

  // 用来修改数据
  mutations: {
    updateFriends(state,friends){
      state.friends = friends
    },
    updateChatLog(state,chat_log){
      state.chat_log = chat_log
    },
    addChatLog(state,message){
      state.chat_log.push(message)
    },
    updateSocket(state,socket){
      state.socket = socket;
      console.log("ok")
    },
    updateUser(state,user){
        state.id = user.id;
        state.username = user.username;
        state.photo = user.photo;
        state.is_login = user.is_login;
    },
    updateToken(state,token){
        state.token = token;
    },
    logout(state){
        state.id = "",
        state.username = "",
        state.photo = "",
        state.token = "",
        state.is_login = false
    },
    updatePullingInfo(state,pulling_info){
        state.pulling_info = pulling_info;
    }

  },
  // 请求数据一般存在action中  调用方法dispatch
  actions: {
    login(context,data){
        $.ajax({
            url:"http://127.0.0.1:3000/user/account/token/",
            type: 'post',
            data: {
              username: data.username,
              password: data.password
            },
            success(resp){
                if(resp.error_message ==="success"){
                    // 登陸成功后將jwt_token存入loaclStorage中  形成持久化登录效果
                    localStorage.setItem("jwt_token",resp.token)
                    // action中调用motation中的函数 需要使用context.commit("函数名",参数)
                    context.commit("updateToken",resp.token)
                    data.success(resp);
                }else{
                    data.error(resp);
                }
            },
            error(resp){
              data.error(resp);
            }
          });
    },
    getinfo(context,data){
        // 登陆成功后 直接连接websocket
        let socket = null
        const websocketUrl = `ws://127.0.0.1:3000/websocket/${this.state.user.token}/`
        socket = new WebSocket(websocketUrl)
        console.log("socket连接成功")
        // this.state.socket = socket
        context.commit("updateSocket",socket)
        // 登录成功后  连接websocket 方便即时通信  同时向后端请求friendlist  先获取friendlist  然后获取自己相关的聊天记录
        $.ajax({
          url:"http://127.0.0.1:3000/user/friend/friendlist/",
          type:'get',
          headers:{
           Authorization: "Bearer " + context.state.token
          },
          success(resp){
            context.commit("updateFriends",resp)
            $.ajax({
              url: "http://127.0.0.1:3000/user/account/info/",
              type: 'get',
              headers:{
                  //登陆验证
                  Authorization:  "Bearer " + context.state.token,
              },
              success(resp){
                  if(resp.error_message === "success"){
                      context.commit("updateUser",{
                          ...resp,
                          is_login: true
                      })

                      data.success(resp);
                  }else{
                      data.error(resp);
                  }
              },
              error(resp){
                  data.error(resp);
              },
          });
        },
        error(resp){
          console.log("error" + resp)
        }
      })
        $.ajax({
          url:"http://127.0.0.1:3000/user/friend/getchatlog/",
          type:'get',
          headers:{
            Authorization: "Bearer " + context.state.token,
          },
          success(resp){
            context.commit("updateChatLog",resp)
            console.log(resp)
          },
          error(resp){
            console.log(resp)
          }
        })
        // $.ajax({
        //     url: "http://127.0.0.1:3000/user/account/info/",
        //     type: 'get',
        //     headers:{
        //         //登陆验证
        //         Authorization:  "Bearer " + context.state.token,
        //     },
        //     success(resp){
        //         if(resp.error_message === "success"){
        //             context.commit("updateUser",{
        //                 ...resp,
        //                 is_login: true
        //             })

        //             data.success(resp);
        //         }else{
        //             data.error(resp);
        //         }
        //     },
        //     error(resp){
        //         data.error(resp);
        //     },
        // });
        console.log("login success")

    },
    logout(context){
        // 退出登陆时， 需要将本地存储的token清空
        localStorage.removeItem("jwt_token");
        context.commit("logout");
        context.state.socket.close()
        console.log("退出")

    },
    addchatlog(context,data){
      context.commit("addChatLog",data)
    }
  },
  modules: {
  }
})
