/*
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2022-12-08 14:18:52
 * @LastEditTime: 2022-12-08 15:42:05
 * @FilePath: \web\src\store\record.js
 */
export default{
  state:{
    is_record: false,
    a_step: "",
    b_step: "",
    record_loser:""
  },
  mutations:{
    updateIsRecord(state,is_record){
      state.is_record = is_record
    },
    updateSteps(state,data){
      state.a_step = data.a_step;
      state.b_step = data.b_step;
    },
    updateLoser(state,loser){
      state.record_loser = loser
    }
  },
  actions:{

  },
  getters:{

  }
}
