
export default({
  state: {
    status: "matching", // matching表示匹配界面，playing表示对战界面
    socket: null,
    opponent_username: "", // 对手的用户名
    opponent_photo: "", // 对手的头像
    gamemap: null,
    a_id: 0,
    b_id: 0,
    a_sx:0,
    a_sy:0,
    b_sx:0,
    b_sy:0,
    gameobject: null,
    loser: "none" // none A B all  代表谁输了
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

        updateGame(state,game){
            state.gamemap = game.gamemap;
            state.a_id = game.a_id;
            state.a_sx  = game.a_sx;
            state.a_sy  = game.a_sy;
            state.b_sx  = game.b_sx;
            state.b_id  = game.b_id;
            state.b_sy  = game.a_sy;
        },
        updateGameObject(state,gameobject){
          state.gameobject = gameobject;
        },
        updateLoser(state,loser){
          state.loser = loser;
        }
  },
  // 修改state数据一般存在action中
  actions: {

  },
  modules: {
  }
})
