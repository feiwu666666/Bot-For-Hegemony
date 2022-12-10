<!--
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2022-09-20 22:18:21
 * @LastEditTime: 2022-12-10 14:49:58
 * @FilePath: \web\src\views\record\RecordIndexView.vue
-->
<template>
    <ContentField>
      <div class="card-body">
        <table class="table table-striped table-hover"  style="text-align:center">
          <thead>
            <tr>
              <th>玩家1</th>
              <th>玩家2</th>
              <th>对战结果</th>
              <th>对战时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="record in records" :key="record.record.id">
              <td>
                <img :src="record.a_photo" alt="图片加载失败" class="record-user-photo"/>
                &nbsp;
                <span class="record-user-username">{{record.a_username}}</span>
              </td>
              <td>
                <img :src="record.b_photo" alt="图片加载失败" class="record-user-photo"/>
                &nbsp;
                <span class="record-user-username">{{record.b_username}}</span>
              </td>
              <td>
                <div v-if="record.record.loser === 'A'"> 玩家2胜</div>
                <div v-else-if="record.record.loser === 'B'">玩家1胜</div>
                <div v-else>平局</div>
              </td>
              <td>
                {{record.record.createtime}}
              </td>
              <td>
                <button type="button" class="btn btn-danger" @click="open_record_content(record.record.id)">查看录像</button>
              </td>
            </tr>
          </tbody>
        </table>
        <nav aria-label="..." style="float: right">
          <ul class="pagination">
            <li class="page-item">
              <a class="page-link" @click="click_page(-2)" href="#">上一页</a>
            </li>
            <li :class="'page-item '+ page.is_active" v-for="page in pages" :key="page.number">
              <a class='page-link' href="#" @click="click_page(page.number)">{{ page.number }}</a>
            </li>

            <li class="page-item">
              <a class="page-link" href="#" @click="click_page(-1)">下一页</a>
            </li>
          </ul>
        </nav>
      </div>
    </ContentField>

</template>
<script>
import ContentField from '@/base-ui/content-field'
import $ from 'jquery'
import { ref } from 'vue'
import { useStore } from 'vuex'
import router from '@/router'

export default{
    components:{
        ContentField
    },
    setup(){
      const store = useStore();
      let current_page = 1;
      const records = ref([])
      let total_records = 0;
      const pages = ref([])
      const click_page = (page) => {
        if(page === -2){
          page =current_page - 1
        }else if(page === -1){
          page = current_page + 1
        }
        const max_page = parseInt(Math.ceil(total_records / 10))
        if(page >=1 && page<=max_page){
          current_page = page
          pull_page(page)
        }
      }
      const update_page = () => {
        const new_pages = []
        // 求出最大页数
        const max_pages = Math.ceil(total_records / 10)
        // 每次只展示当前页面以及其前后两页
        for(let i = current_page - 2;i <= current_page + 2;i ++){
          if(i >= 1 && i <= max_pages){
            new_pages.push({
              number: i,
              is_active: current_page === i ? 'active' : ""
            })
          }
        }
        pages.value = new_pages
      }
      const pull_page = (page) => {
        $.ajax({
                url: 'https://app2803.acapp.acwing.com.cn/api/record/getlist/',
                type: 'post',
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data:{
                    page: page,
                },
                success(resp){
                      records.value = resp.records
                      total_records = resp.records_count
                      update_page()
                      console.log(resp)
                }
            })
      }
      pull_page(current_page)
      // 将后端传过来的map信息 转换成一个二维数组地图
      const stringTo2D = (map) => {
        const gamemap = []
        for(let i=0,k=0;i<13;i++){
          let line = [];
          for(let j=0;j<14;j++,k++){
            if (map[k] === '0') line.push(0)
            else line.push(1)
          }
          gamemap.push(line)
        }
        return gamemap
      }
      // 点击查看回放按钮 触发点击事件 跳转到回放页面
      const open_record_content = (recordId) => {
        for(const record of records.value){
          if(record.record.id === recordId){
            // 将当前对战标记为回放
            store.commit("updateIsRecord", true);
            // 更新地图
            store.commit("updateGame",{
              gamemap: stringTo2D(record.record.map),
              a_id: record.record.aid,
              a_sx: record.record.asx,
              a_sy: record.record.asy,
              b_id: record.record.bid,
              b_sx: record.record.bsx,
              b_sy: record.record.bsy,
            })
            store.commit("updateSteps",{
              a_step: record.record.asteps,
              b_step: record.record.bsteps
            })
            store.commit("updateLoser", record.record.loser)
            router.push({
              name: "record_content",
              params:{
                recordId
              }
            })
            break;
          }

        }
      }
      return {
        records,
        total_records,
        open_record_content,
        pages,
        click_page
      }
    }
}
</script>

<style scoped>
.record-user-photo{
  width: 5vh;
  border-radius: 50%;
}
</style>
