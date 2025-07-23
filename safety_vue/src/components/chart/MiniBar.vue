<template>
  <div :style="{'width':width==null?'auto':width+'px'}">
    <v-chart :forceFit="width==null" :height="height" :data="data" :scale="scale" padding="0">
      <v-tooltip/>
      <v-bar position="x*y"/>
    </v-chart>
  </div>
</template>

<script>
  import moment from 'dayjs'
  import {getAction} from "@api/manage";

  const sourceData = []
  const beginDay = new Date().getTime()

  // for (let i = 0; i < 10; i++) {
  //   sourceData.push({
  //     x: moment(new Date(beginDay + 1000 * 60 * 60 * 24 * i)).format('YYYY-MM-DD'),
  //     y: Math.round(Math.random() * 10)
  //   })
  // }

  const tooltip = [
    'x*y',
    (x, y) => ({
      name: x,
      value: y
    })
  ]

  // const scale = [{
  //   dataKey: 'x',
  //   min: 2,
  //   //title: this.x, alias: this.x
  // }, {
  //   dataKey: 'y',
  //   min: 1,
  //   max: 30,
  //   title: '生产盘数', alias: '生产盘数'
  // }]

  export default {
    name: 'MiniBar',
    props: {
      dataSource: {
        type: Array,
        default: () => []
      },
      width: {
        type: Number,
        default: null
      },
      height: {
        type: Number,
        default: 200
      },
      // x 轴别名
      x: {
        type: String,
        default: 'x'
      },
      // y 轴别名
      y: {
        type: String,
        default: '生产盘数'
      }
    },
    created() {
      this.swtj();
      if (this.dataSource.length === 0) {
        this.data = sourceData
      } else {
        this.data = this.dataSource
      }
    },
    data() {
      return {
        url:{
          swtj:'/swbhz/bhzSwBases/list5'
        },
        tooltip,
        data: [],
        // scale
      }
    },
    computed:{
      scale() {
        return [
          { dataKey: 'x', title: this.x, alias: this.x },
          { dataKey: 'y', title: this.y, alias: this.y }
        ]
      }
    },
    methods:{
      swtj:function (){
        getAction(this.url.swtj,{
        }).then(res=>{
          if(res.success){
            for(let i=0;i<res.result.length;i++){
              sourceData.push({
                x:res.result[i].statisticsTime,y:res.result[i].allDish
              })
            }
          }
        })
      },
    },
  }
</script>

<style lang="less" scoped>
  @import "chart";
</style>