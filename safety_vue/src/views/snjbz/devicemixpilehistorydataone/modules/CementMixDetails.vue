<template>
  <j-modal
    :title="title"
    :width="1200"
    :visible="visible"
    @ok="handleOk"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <div class="cementMix">
      <div class="cementMix-left">
        <a-card title="图形分析" :bordered="false">
          <div class="title">成桩形状图</div>
          <div class="pile">
            <div class="pile-left">
              <span class="textTop">0米</span>
              <span class="textBottom">{{ model.pileRealdep }}米</span>
              <div class="imgTop"></div>
              <div class="imgBottom"></div>
            </div>
            <div class="pile-right"></div>
          </div>
        </a-card>
      </div>
      <div class="cementMix-right">
        <div ref="qxtOne" :style="chartStyle1"></div>
        <div ref="qxtTwo" :style="chartStyle2"></div>
        <div ref="qxtThree" :style="chartStyle3"></div>
      </div>
    </div>
  </j-modal>
</template>

<script>
import { getAction, postAction } from '@/api/manage'
export default {
  name: 'CementMixDetails',
  components: {},
  data() {
    return {
      title: '水泥搅拌桩详情',
      visible: false,
      disableSubmit: true,
      model: {},
      chartStyle1: { width: '100%', height: '250px' },
      chartStyle2: { width: '100%', height: '250px' },
      chartStyle3: { width: '100%', height: '250px' },
    }
  },
  mounted() {},
  methods: {
    show(record) {
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        this.getData()
      })
    },
    getData() {
      let params = {
        partEndtime: this.model.pileTime,
        shebeino: this.model.shebeino,
        pileNo: this.model.pileNo,
        pageNo: 1,
        pageSize: 100,
      }
      getAction('/devicemixpilehistorydatapart/deviceMixpileHistorydataPart/list', params).then((res) => {
        if (res.success) {
          let data = res.result.records
          let partXList = [] //段X
          let partYList = [] //段Y
          let partBetonList = [] //段浆量
          let partDepList = [] //段深度
          let partEndtimeList = [] //时间
          data.forEach((item) => {
            partXList.push(item.partX)
            partYList.push(item.partY)
            partBetonList.push(item.partBeton)
            partDepList.push(item.partDep)
            partEndtimeList.push(item.partEndtime)
          })
          this.qxtEchart1(partEndtimeList, partDepList)
          this.qxtEchart2(partEndtimeList, partBetonList)
          this.qxtEchart3(partEndtimeList, partXList, partYList)
        }
      })
    },
    //曲线图
    qxtEchart1(xList, yList) {
      let option = {
        legend: {
          data: ['深度'],
        },
        tooltip: {
          trigger: 'axis',
        },
        title: [
          {
            text: '深度/时间图',
            top: 0,
            left: 10,
            textStyle: {
              fontSize: 16,
            },
          },
        ],
        xAxis: {
          type: 'category',
          boundaryGap: true,
          axisLabel: {
            interval: 15,
            fontSize: 14,
          },
          axisTick: {
            show: false, // 隐藏坐标轴的刻度线
          },
          data: xList,
        },
        yAxis: [
          {
            name: '深度(m)',
            nameTextStyle: {
              align: 'center',
              fontSize: 12,
            },
            axisLine: { show: true },
            type: 'value',
            axisLabel: {
              fontSize: 14,
            },
          },
        ],
        series: [
          {
            name: '深度',
            type: 'line',
            smooth: true,
            data: yList,
          },
        ],
      }
      let myChart = this.$echarts.getInstanceByDom(this.$refs.qxtOne)
      if (myChart == null) {
        myChart = this.$echarts.init(this.$refs.qxtOne)
      }
      myChart.setOption(option)
    },
    qxtEchart2(xList, yList) {
      let option = {
        legend: {
          data: ['浆量'],
        },
        tooltip: {
          trigger: 'axis',
        },
        title: [
          {
            text: '浆量变化图',
            top: 0,
            left: 10,
            textStyle: {
              fontSize: 16,
            },
          },
        ],
        xAxis: {
          type: 'category',
          boundaryGap: true,
          axisLabel: {
            interval: 15,
            fontSize: 14,
          },
          axisTick: {
            show: false, // 隐藏坐标轴的刻度线
          },
          data: xList,
        },
        yAxis: [
          {
            name: '浆量(单位)',
            nameTextStyle: {
              align: 'center',
              fontSize: 12,
            },
            axisLine: { show: true },
            type: 'value',
            axisLabel: {
              fontSize: 14,
            },
          },
        ],
        series: [
          {
            name: '浆量',
            type: 'line',
            smooth: true,
            data: yList,
          },
        ],
      }
      let myChart = this.$echarts.getInstanceByDom(this.$refs.qxtTwo)
      if (myChart == null) {
        myChart = this.$echarts.init(this.$refs.qxtTwo)
      }
      myChart.setOption(option)
    },
    qxtEchart3(xList, ySpeed, yDepth) {
      let option = {
        legend: {
          data: ['X', 'Y'],
        },
        tooltip: {
          trigger: 'axis',
        },
        // title: [
        //   {
        //     text: '速度/电流/压力图',
        //     top: 0,
        //     left: 10,
        //     textStyle: {
        //       fontSize: 16,
        //     },
        //   },
        // ],
        xAxis: {
          name: '时间',
          type: 'category',
          boundaryGap: true,
          axisLabel: {
            interval: 15,
            fontSize: 14,
          },
          axisTick: {
            show: false, // 隐藏坐标轴的刻度线
          },
          data: xList,
        },
        yAxis: [
          {
            name: '倾角',
            nameTextStyle: {
              align: 'center',
              fontSize: 12,
            },
            axisLine: { show: true },
            type: 'value',
            axisLabel: {
              fontSize: 14,
            },
          },
          // {
          //   name: '深度(m)',
          //   nameTextStyle: {
          //     align: 'center',
          //     fontSize: 12,
          //   },
          //   axisLine: { show: true },
          //   type: 'value',
          //   axisLabel: {
          //     fontSize: 14,
          //   },
          // },
        ],
        series: [
          {
            name: 'X',
            type: 'line',
            smooth: true,
            // yAxisIndex: 0,
            data: ySpeed,
          },
          {
            name: 'Y',
            type: 'line',
            smooth: true,
            // yAxisIndex: 1,
            data: yDepth,
          },
        ],
      }
      let myChart = this.$echarts.getInstanceByDom(this.$refs.qxtThree)
      if (myChart == null) {
        myChart = this.$echarts.init(this.$refs.qxtThree)
      }
      myChart.setOption(option)
    },
    handleOk() {
      this.visible = false
    },
    handleCancel() {
      this.visible = false
    },
  },
}
</script>
<style lang="less" scoped>
.cementMix {
  display: flex;
  &-left {
    width: 500px;
    margin-right: 50px;
    .title {
      font-weight: bold;
    }
    .pile {
      display: flex;
      justify-content: space-around;
      margin-top: 20px;
      &-left {
        position: relative;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        .textTop {
          width: 50px;
          position: absolute;
          top: -8px;
          left: 53px;
        }
        .textBottom {
          width: 50px;
          position: absolute;
          bottom: -8px;
          left: 53px;
        }
        .imgTop {
          width: 52px;
          height: 63px;
          background: url(../../../../assets/img/up-arrow.png) no-repeat;
        }
        .imgBottom {
          width: 52px;
          height: 63px;
          background: url(../../../../assets/img/down-arrow.png) no-repeat;
        }
      }
      &-right {
        width: 200px;
        height: 600px;
        background: url(../../../../assets/img/pipe-pile.png) center/contain no-repeat;
      }
    }
  }
  &-right {
    width: calc(100% - 550px);
    height: 800px;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
  }
}
</style>