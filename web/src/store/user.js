/*
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2022-11-24 17:10:28
 * @LastEditTime: 2022-12-10 14:49:49
 * @FilePath: \web\src\store\user.js
 */
import $ from 'jquery'

export default({
  state: {
    id : "",
    username: "",
    photo: "",
    token: "",
    is_login: false,
    pulling_info:true // 判断当前是否在拉取信息，即判断是否处在登陆状态
  },
  getters: {
  },

  // 用来修改数据
  mutations: {
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
  // 请求数据一般存在action中
  actions: {
    login(context,data){
        $.ajax({
            url:"https://app2803.acapp.acwing.com.cn/api/user/account/token/",
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
        $.ajax({
            url: "https://app2803.acapp.acwing.com.cn/api/user/account/info/",
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
            }
        });
    },
    logout(context){
        // 退出登陆时， 需要将本地存储的token清空
        localStorage.removeItem("jwt_token");
        context.commit("logout");
    },
  },
  modules: {
  }
})
