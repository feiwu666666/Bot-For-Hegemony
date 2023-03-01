<!--
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2023-03-01 15:45:58
 * @LastEditTime: 2023-03-01 16:53:43
 * @FilePath: \web\src\views\user\info\UserInfoIndexView.vue
-->
<template>
  <content-field>
    <h1>{{ $route.params.userId }}</h1>
  </content-field>

</template>


<script>
import ContentField from '@/base-ui/content-field'
export default {
  components: { ContentField },


}
</script>

<style>

</style>
