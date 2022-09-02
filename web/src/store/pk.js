
export default({
  state: {
    status: "matching", // matching表示匹配界面，playing表示对战界面
    socket: null,
    opponent_username: "", // 对手的用户名
    opponent_photo: "", // 对手的头像
    gamemap: null,
  },
  getters: {
  },
  
  // 用来修改数据
  mutations: {
        updateSocket(state,socket){
            state.socket = socket;
        },
        updateOpponent(state,opponent){
            state.opponent_username = opponent.username;
            state.opponent_photo = opponent.photo;
        },
        updateStatus(state,status){
            state.status = status;
        },
        updateGamemap(state,gamemap){
          state.gamemap = gamemap;
        }
  },
  // 修改state数据一般存在action中
  actions: {

  },
  modules: {
  }
})
