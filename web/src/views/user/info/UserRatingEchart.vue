<!--
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2023-03-08 11:38:14
 * @LastEditTime: 2023-03-09 16:22:26
 * @FilePath: \web\src\views\user\info\UserRatingEchart.vue
-->
<template>
  <div class="card" style="margin-top: 20px;">
    <div>
      <h1 style="text-align: center;">
        天梯积分：
        {{ my_rating }}
      </h1>
    </div>
    <div id="line-chart" style="width: 100% ;height: 30vh;">
    </div>
    <div id="pie-chart" style="width:100%;height:30vh"></div>
  </div>

</template>

<script>
import * as echart from 'echarts'
import { onMounted } from 'vue'
import  setLineOptions, { setPieOptions }  from './config/UserRating.config'
export default {
  props:{
    my_rating:{
      type:Number,
      default:() => 0,
      required:true
    },
    rating_rank: {
      type: [Number],
      required:true,
    }
  },


  setup(props){

    console.log(props.rating_rank)
    let echart_line = null
    let echart_pie = null
    console.log(props.rating_rank)
    onMounted(() => {

      setTimeout(() => {
        // const option = {
        // title:{
        //   text:'Rating Description bar chart',
        //   left:'center'
        // },

        // tooltip: {
        //   trigger: 'axis',
        //   axisPointer: {
        //     type: 'shadow'
        //   }
        // },
        // xAxis: [
        //   {
        //     type: 'category',
        //     data: ['0-1450', '1450-1550', '1550-1600', '1600-1700', '1700-1800', '1800＋'],
        //     axisTick: {
        //       alignWithLabel: true
        //     }
        //   }
        // ],
        // yAxis: [
        //   {
        //     type: 'value'
        //   }
        // ],
        // series: [
        //   {
        //     name: '人数',
        //     type: 'bar',
        //     barWidth: '60%',
        //     data:props.rating_rank,
        //     // 设置自己积分这一档为红色props.rating_rank
        //     itemStyle:{
        //       normal:{
        //         color: function(params){
        //           let index = 0
        //           const colorList = ['#1e80ff','#1e80ff','#1e80ff','#1e80ff','#1e80ff','#1e80ff',]
        //           if(props.my_rating >= 0 && props.my_rating <1450){
        //             index = 0
        //           }else if(props.my_rating >= 1450 && props.my_rating < 1550){
        //             index = 1
        //           }else if(props.my_rating >= 1550 && props.my_rating < 1600){
        //             index = 2
        //           }else if(props.my_rating >= 1600 && props.my_rating < 1700){
        //             index = 3
        //           }else if(props.my_rating >= 1700 && props.my_rating < 1800){
        //             index = 4
        //           }else if(props.my_rating >= 1800){
        //             index = 5
        //           }
        //           colorList[index] = '#f53f3f'
        //           return colorList[params.dataIndex]
        //         }
        //       }
        //     }
        //   }
        // ]
        // }




        const pie_options = setPieOptions(props.rating_rank)
        const line_options = setLineOptions(props.my_rating,props.rating_rank)
        echart_line = echart.init(document.getElementById("line-chart"))
        echart_pie = echart.init(document.getElementById("pie-chart"))
        echart_line.setOption(line_options)
        echart_pie.setOption(pie_options)
      },500)

    })

    window.addEventListener('resize',function(){
      echart_line.resize();
      echart_pie.resize();
    });
    return {

    }
  }

}
</script>

<style>

</style>
