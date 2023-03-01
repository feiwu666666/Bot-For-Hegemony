/*
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2022-09-20 22:18:21
 * @LastEditTime: 2023-01-04 11:35:23
 * @FilePath: \web\src\main.js
 */
import { createApp } from 'vue'
import App from './App.vue'
import store from './store'
import router from './router'


createApp(App).use(router).use(store).use(store).mount('#app')
