<template>
  <div class="statistical">
    <div class="statistical-header">
      <div class="time">
        <a-button :type="timeType === '1' ? 'primary' : 'default'" @click="handleTime('1')">本年</a-button>
        <a-button :type="timeType === '2' ? 'primary' : 'default'" @click="handleTime('2')">本季</a-button>
        <a-button :type="timeType === '3' ? 'primary' : 'default'" @click="handleTime('3')">本月</a-button>
        <a-button :type="timeType === '4' ? 'primary' : 'default'" @click="handleTime('4')">本周</a-button>
        <a-button :type="timeType === '5' ? 'primary' : 'default'" @click="handleTime('5')">今天</a-button>
      </div>
    </div>
    <div class="statistical-con">
      <div class="con-left">
        <div class="title">信息列表</div>
        <p style="border-top: 2px solid rgba(0, 78, 160, 1)"></p>
        <div>
          <a-table
            ref="table"
            size="middle"
            bordered
            :rowKey="(record, index) => index"
            class="j-table-force-nowrap"
            :scroll="{ y: 205 }"
            :loading="loading"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="false"
          >
          </a-table>
        </div>
      </div>
      <div class="con-right">
        <div class="title">环形统计图</div>
        <p style="border-top: 2px solid rgba(0, 78, 160, 1)"></p>
        <div ref="hxt" style="width: 100%; height: 250px"></div>
      </div>
    </div>
  </div>
</template>
<script>
import { getAction } from '@/api/manage'
import Vue from 'vue'

export default {
  name: 'BhzStatisticalVolume',
  components: {},
  data() {
    return {
      sys_depart_orgcode: Vue.ls.get('SYS_DEPART_ORGCODE'),
      timeType: '1',
      loading: false,
      dataSource: [],
      circularData: {},
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      columns: [
        {
          title: '标段名',
          align: 'center',
          dataIndex: 'projectName',
        },
        {
          title: '强度等级',
          align: 'center',
          dataIndex: 'strengthRank',
        },
        {
          title: '方量',
          align: 'center',
          dataIndex: 'estimateNumber',
        },
      ],
      url: {
        bhzTongJiLists: '/bhzStatistics/bhzCementStatistics/bhzTongJiLists',
        bhzInfoList: '/bhzStatistics/bhzCementStatistics/bhzInfoList',
      },
    }
  },
  mounted() {
    this.getTongJiList()
    this.getInfoList()
  },
  methods: {
    getTongJiList() {
      this.loading = true
      let params = {
        time: this.timeType,
        sys_depart_orgcode: this.sys_depart_orgcode,
      }
      getAction(this.url.bhzTongJiLists, params).then((res) => {
        if (res.success) {
          this.dataSource = res.result
        } else {
          this.$message.error(res.message)
        }
      }).finally(() => {
        this.loading = false;
      })
    },
    getInfoList() {
      let params = {
        time: this.timeType,
        shebeiNo: '',
      }
      getAction(this.url.bhzInfoList, params).then((res) => {
        if (res.success) {
          this.circularData = res.result
          this.initEchartsHxt(this.circularData)
        } else {
          this.$message.error(res.message)
        }
      })
    },
    //环形图
    initEchartsHxt(objData) {
      let dataList = []
      for (let o in objData) {
        dataList.push({
          value: objData[o],
          name: o,
        })
      }
      let option = {
        // color: ['#FFCC00', '#FF315F', '#8E3CF0', '#0095FF', '#00C64A'],
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}方<br />比例: {d}%',
        },
        legend: {
          orient: 'vertical',
          top: '5%',
          left: 'left',
        },
        series: [
          {
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            emphasis: {
              label: {
                show: true,
                fontSize: 20,
                fontWeight: 'bold',
              },
            },
            data: dataList,
          },
        ],
      }
      let myChart = this.$echarts.getInstanceByDom(this.$refs.hxt)
      if (myChart == null) {
        myChart = this.$echarts.init(this.$refs.hxt)
      }
      myChart.setOption(option)
    },
    handleTime(type) {
      this.timeType = type
      this.getTongJiList()
      this.getInfoList()
    },
  },
}
</script>
<style lang="less" scoped>
.statistical {
  padding: 10px 15px 0;
  &-header {
    margin-bottom: 20px;
    padding: 10px 20px;
    border-radius: 8px;
    border: 1px solid #004ea0;
    background-color: #fff;
    .time {
      display: flex;
      justify-content: flex-end;
      .ant-btn:nth-child(n + 2) {
        margin-left: 8px;
      }
    }
  }
  &-con {
    height: 350px;
    display: flex;
    .con-left {
      width: 50%;
      height: 100%;
      margin-right: 30px;
      padding: 10px 20px 20px;
      border-radius: 8px;
      border: 1px solid rgba(0, 78, 160, 1);
      background-color: #fff;
      .title {
        width: 150px;
        height: 40px;
        line-height: 40px;
        font-size: 16px;
        font-weight: bold;
        padding-left: 20px;
        margin-bottom: 10px;
      }
    }
    .con-right {
      flex: 1;
      height: 100%;
      padding: 10px 20px 20px;
      border-radius: 8px;
      border: 1px solid rgba(0, 78, 160, 1);
      background-color: #fff;
      .title {
        width: 150px;
        height: 40px;
        line-height: 40px;
        font-size: 16px;
        font-weight: bold;
        padding-left: 20px;
        margin-bottom: 10px;
      }
    }
  }
}
</style>