/*
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2022-09-20 22:18:21
 * @LastEditTime: 2022-12-08 14:32:30
 * @FilePath: \web\src\router\index.js
 */
import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView.vue'
import NotFound from '../views/error/NotFound.vue'
import RanklistIndexView from '../views/ranklist/RanklistIndexView.vue'
import RecordIndexView from '../views/record/RecordIndexView.vue'
import UserBotIndexView from '../views/user/bot/UserBotIndexView.vue'
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView'
import UserAccountLoginView from '../views/user/account/UserAccountLoginView'
import RecordContentView from '../views/record/RecordContentView'
import store from '@/store'

const routes = [
  {
    path:'/',
    name:"home",
    redirect:"/pk/",
    meta: {
      requestAuth: true
    }
  },
  {
    path:"/pk/",
    name:"pk_index",
    component:PkIndexView,
    meta: {
      requestAuth: true
    }
  },
  {
    path:"/record/:recordId/",
    name:"record_content",
    component: RecordContentView,
    meta:{
      requestAuth: true
    }
  },
  {
    path:"/record/",
    name:"record_index",
    component:RecordIndexView,
    meta: {
      requestAuth: true
    }
  },
  {
    path:"/ranklist/",
    name:"ranklist_index",
    component:RanklistIndexView,
    meta: {
      requestAuth: true
    }
  },
  {
    path:"/user/bot/",
    name:"user_bot_index",
    component:UserBotIndexView,
    meta: {
      requestAuth: true
    }
  },
  {
    path:"/user/account/login/",
    name:"user_account_login",
    component: UserAccountLoginView,
    meta: {
      requestAuth: false
    }
  },
  {
    path:"/user/account/register/",
    name:"user_account_register",
    component:UserAccountRegisterView,
    meta: {
      requestAuth: false
    }
  },
  {
    path:"/404/",
    name:"404_index",
    component:NotFound,
    meta: {
      requestAuth: false
    }
  },
  {
    path:"/:catchAll(.*)",
    redirect:'/404/'
  },

]

const router = createRouter({
  history: createWebHistory(),
  routes
})
// 路由守卫
// router在起作用之前执行的函数
// 每次通过router进入某个页面之前，会调用beforEach函数 to: 跳转到某个画面 from：从哪个页面跳转过来 next: 将页面是否执行下一步操作
router.beforeEach((to, from,next) => {
  if(to.meta.requestAuth && !store.state.user.is_login){  // 如果去往的页面需要授权并且用户并未登录  则去往登录页面
    next({name: "user_account_login"});
  }else{
    next();
  }

})


export default router
