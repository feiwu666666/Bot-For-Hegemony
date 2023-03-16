/*
 * @Author: Cyan_Breeze
 * @Description:
 * @Date: 2023-03-09 13:53:11
 * @LastEditTime: 2023-03-09 21:26:41
 * @FilePath: \web\src\views\user\info\config\UserRating.config.js
 */
const setLineOptions = (my_rating,all_rating) => {
  const option = {
    title:{
      text:'Rating Description Bar chart',
      left:'center'
    },

    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: [
      {
        type: 'category',
        data: ['0-1450', '1450-1550', '1550-1600', '1600-1700', '1700-1800', '1800＋'],
        axisTick: {
          alignWithLabel: true
        }
      }
    ],
    yAxis: [
      {
        type: 'value'
      }
    ],
    series: [
      {
        name: '人数',
        type: 'bar',
        barWidth: '60%',
        data:all_rating,
        // 设置自己积分这一档为红色props.rating_rank
        itemStyle:{
          normal:{
            color: function(params){
              let index = 0
              const colorList = ['#1e80ff','#1e80ff','#1e80ff','#1e80ff','#1e80ff','#1e80ff',]
              if(my_rating >= 0 && my_rating <1450){
                index = 0
              }else if(my_rating >= 1450 && my_rating < 1550){
                index = 1
              }else if(my_rating >= 1550 && my_rating < 1600){
                index = 2
              }else if(my_rating >= 1600 && my_rating < 1700){
                index = 3
              }else if(my_rating >= 1700 && my_rating < 1800){
                index = 4
              }else if(my_rating >= 1800){
                index = 5
              }
              colorList[index] = '#f53f3f'
              return colorList[params.dataIndex]
            }
          }
        }
      }
    ]
  };
  return option
}

const setPieOptions = (rating_rank) =>{
  const option = {
    title: {
      text: 'Rating Description pie chart',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },

    series: [
      {
        name: '人数',
        type: 'pie',
        radius: '50%',
        center:['50%','50%'],
        data: [
          { value: rating_rank[0], name: '0-1450',itemStyle:{
            normal:{
              label:{
                show: false
              },
              labelline: {
                show:false
              }
            }
          } },
          { value: rating_rank[1], name: '1450-1550' },
          { value: rating_rank[2], name: '1550-1600' },
          { value: rating_rank[3], name: '1600-1700' },
          { value: rating_rank[4], name: '1700-1800' },
          { value: rating_rank[4], name: '1800＋' },
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };
  console.log(option)
  const options = {
    title:{
      text:'Rating Description Pie Chart',
      left:'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top:'10%',
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '分段人数',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 10,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value:rating_rank[0] , name: '0-1450' },
          { value: rating_rank[1], name: '1450-1550' },
          { value: rating_rank[2], name: '1550-1600' },
          { value: rating_rank[3], name: '1600-1700' },
          { value: rating_rank[4], name: '1700-1800' },
          { value: rating_rank[5], name: '1800＋' }
        ]
      }
    ]
  };
  return options
}



export default setLineOptions
export {
  setPieOptions
}
