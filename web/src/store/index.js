/*
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2022-09-20 22:18:21
 * @LastEditTime: 2023-02-18 22:09:49
 * @FilePath: \web\src\store\index.js
 */
import { createStore } from 'vuex'
import ModuleUser from './user'
import ModulePk from './pk'
import ModuleRecord from './record'
import ModuleChat from './chat'

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: ModuleUser,
    pk:ModulePk,
    record: ModuleRecord,
    chat: ModuleChat
  }
})
