<!--
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2022-09-20 22:18:21
 * @LastEditTime: 2023-02-20 19:38:47
 * @FilePath: \web\src\App.vue
-->
<template>
  <NavBar/>
  <router-view></router-view>
</template>


<script>
import NavBar from '@/base-ui/navbar/index';
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"

export default{
  components:{
    NavBar,
  },
}
</script>
<style>
body{
  background: url('@/assets/background.png');
  background-size: cover;
  height: 100%;
  width: 100%;

}
</style>
