<template>
    <ContentField>我的Bot</ContentField>
</template>
<script>
import ContentField from '@/components/ContentField.vue'
import $ from 'jquery'
import { useStore } from 'vuex';

export default{
    components:{
        ContentField
    },
    setup(){
        const store = useStore();
        // $.ajax({
        //     url:'http://127.0.0.1:3000/user/bot/add/',
        //     type:'post',
        //     headers:{
        //         //登陆验证
        //         Authorization:  "Bearer " + store.state.user.token,
        //     },
        //     data:{
        //         title:"标题",
        //         content:"代码内容",
        //         description:"代码描述",
        //     },
        //     success(resp){
        //         console.log(resp);
        //     },
        //     error(resp){
        //         console.log(resp);
        //     }
        // });
        $.ajax({
            url:'http://127.0.0.1:3000/user/bot/remove/',
            type:'post',
            headers:{
                //登陆验证
                Authorization:  "Bearer " + store.state.user.token,
            },
            data:{
                bot_id: 1
            },
            success(resp){
                console.log(resp);
            },
            error(resp){
                console.log(resp);
            }
        })
    }
}
</script>

<style scoped>
</style>