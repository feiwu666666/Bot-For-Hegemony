<!--
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2022-09-20 22:18:21
 * @LastEditTime: 2022-12-10 14:55:47
 * @FilePath: \web\src\views\user\account\UserAccountRegisterView.vue
-->
<template>
    <ContentField>
         <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent = "register">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input type="text" v-model="username" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input type="password" v-model="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>
                    <div class="mb-3">
                        <label for="confirmedPassword" class="form-label">确认密码</label>
                        <input type="password" v-model="confirmedPassword" class="form-control" id="confirmedPassword" placeholder="请确认密码">
                    </div>
                    <div class="error_message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-success">注册</button>
                </form>
            </div>
        </div>
    </ContentField>
</template>
<script>
import ContentField from '@/base-ui/content-field'
import { ref } from 'vue';
import router from '@/router';
import $ from 'jquery'


export default{
    components:{
        ContentField
    },
    setup(){
         let username = ref('');
         let password = ref('');
         let confirmedPassword = ref('');
         let error_message = ref('');

         const register = ()=> {
            $.ajax({
                url: 'http://127.0.0.1:3000/user/account/register/',
                type: 'post',
                data:{
                    username: username.value,
                    password: password.value,
                    confirmedPassword: confirmedPassword.value
                },
                success(resp){
                    if(resp.error_message === "success"){
                        router.push({name: "user_account_login"}); // 如果注册成功  跳转至登录页面
                    }else{
                        error_message.value = resp.error_message;
                    }
                },
                error(resp){
                     error_message.value = resp.error_message;
                }
            })
         }

         return {
            username,password,confirmedPassword,error_message,register
         }
    }
}
</script>

<style scoped>
.error_message{
    color: red;
}
.btn{
    width:100%;
}
</style>
