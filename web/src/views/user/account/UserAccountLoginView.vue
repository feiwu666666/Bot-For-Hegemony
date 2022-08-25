<template>
    <ContentField v-if="!$store.state.user.pulling_info">
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent = "login">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input type="text" v-model="username" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input type="password" v-model="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>
                    <div class="error_message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-success">登录</button>
                </form>
            </div>
        </div>


    </ContentField>
</template>
<script>
import ContentField from '@/components/ContentField.vue'
import { useStore } from 'vuex';
import { ref } from 'vue';
import router from '@/router';


export default{
    components:{
        ContentField
    },
    setup(){
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let error_message = ref('');

        const jwt_token = localStorage.getItem("jwt_token");
        if(jwt_token){
            store.commit("updateToken",jwt_token);
            store.dispatch("getinfo",{
                success(){
                    router.push({name: "home"});
                },
                error(){
                    console.log("token过期")
                    store.commit("updatePullingInfo",false);
                }
            })
        }else{
            store.commit("updatePullingInfo",false);
        }


        const login = () => {
            // 想调用store中的函数时，需要使用store.dispatch('函数名',参数);
            store.dispatch("login",{
                username: username.value,
                password: password.value,
                success(){
                    store.dispatch("getinfo",{
                        success(){
                            // 登陆成功之后，获取用户信息之后  自动跳转至'Home'页面
                            router.push({name: 'home'})
                            console.log(store.state.user)
                        }
                    })
                },
                error(){
                    error_message.value = "用户名或者密码错误";
                }
            })
        }

        return {
            username,password,error_message,login
        }
    }
}
</script>

<style scoped>
.btn{
    width:100%; 
}
.error_message{
    color:red;
}
</style>