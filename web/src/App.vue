<template>
  <NavBar/>
  <router-view></router-view>
</template>


<script>
import NavBar from './components/NavBar.vue';
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
// import $ from 'jquery'
export default{
  components:{
    NavBar,
},
// setup(){
//     $.ajax({
//       url:"http://127.0.0.1:3000/user/account/token/",
//       type: 'post',
//       data: {
//         username:'b',
//         password:'pb'
//       },
//       success(resp){
//         console.log(resp);
//       },
//       error(resp){
//         console.log(resp);
//       }
//     }); 
//     $.ajax({
//       url: "http://127.0.0.1:3000/user/account/info/",
//       type: 'get',
//       headers:{
//         //登陆验证
//         Authorization:  "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3NjhkMDdlMmY2ZDA0ZjE2YTE3NTY3YTgwODc5NDU3NiIsInN1YiI6IjIiLCJpc3MiOiJzZyIsImlhdCI6MTY2MTM0MTcyNSwiZXhwIjoxNjYyNTUxMzI1fQ.KQA1Hq1l5N3MKSOqbFGqBHV6LYFV_Y8ApGOi_QIXYBg",
//       },
//       success(resp){
//         console.log(resp);
//       },
//       error(resp){
//         console.log(resp);
//       }
//     });
//     $.ajax({
//       url: "http://127.0.0.1:3000/user/account/register/",
//       type:"post",
//       data:{
//         username:"e",
//         password:'pe',
//         confirmedPassword:'pe',
//       },
//       success(resp){
//         console.log(resp);
//       },
//       error(resp){
//         console.log(resp);
//       }
//     });
//   }
}
</script>


<style>
body{
  background-image: url('@/assets/background.png');
  background-size: cover;
}
</style>
