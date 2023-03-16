<!--
 * @Author: Cyan_Breeze
 * @Description：排行榜页面
 * @Date: 2022-09-20 22:18:21
 * @LastEditTime: 2023-03-05 22:45:35
 * @FilePath: \web\src\views\ranklist\RanklistIndexView.vue
-->
<template>
    <ContentField>
      <div class="card-body">
        <table class="table table-striped table-hover"  style="text-align:center">
          <thead>
            <tr>
              <th>玩家</th>
              <th>天梯积分</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.id">
              <td>
                <router-link :to="{name: 'user_info',params:{userId: user.id}}">
                  <img :src="user.photo" alt="图片加载失败" class="user-photo" loading="lazy"/>
                </router-link>
                &nbsp;
                <span class="user-username">{{user.username}}</span>
              </td>
              <td>
                {{ user.rating }}
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

export default{
    components:{
        ContentField
    },
    setup(){
      const store = useStore();
      let current_page = 1;
      const users = ref([])
      let total_users = 0;
      const pages = ref([])
      const click_page = (page) => {
        if(page === -2){
          page =current_page - 1
        }else if(page === -1){
          page = current_page + 1
        }
        const max_page = parseInt(Math.ceil(total_users / 8))
        if(page >=1 && page<=max_page){
          current_page = page
          pull_page(page)
        }
      }
      const update_page = () => {
        const new_pages = []
        // 求出最大页数
        const max_pages = Math.ceil(total_users / 8)
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
                url: 'http://127.0.0.1:3000/ranklist/getlist/',
                type: 'post',
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data:{
                    page: page,
                },
                success(resp){
                      users.value = resp.users
                      total_users = resp.users_count
                      update_page()
                      console.log(resp)
                }
            })
      }
      pull_page(current_page)
      return {
        users,
        total_users,
        pages,
        click_page
      }
    }
}
</script>

<style scoped>
.user-photo{
  width: 5vh;
  border-radius: 50%;
}
</style>
