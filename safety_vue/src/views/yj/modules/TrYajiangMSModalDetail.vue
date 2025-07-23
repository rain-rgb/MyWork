<template>
  <j-modal
    :title="title"
    :width="1400"
    :visible="visible"
    :maskClosable="false"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-tabs default-active-key="1" @change="callback1">
      <a-tab-pane v-if="yajiangrenwudanms" key="1" tab="智能压浆基本数据">
        <a-table rowKey="id" :pagination="ipagination" :columns="columns2" :data-source="data2" bordered> </a-table>
      </a-tab-pane>
      <a-tab-pane key="2" tab="智能压浆详情数据">
        <a-table rowKey="id" :pagination="ipagination" :columns="columns" :data-source="data" bordered>
          <span slot="isOverLevel" slot-scope="status, record">
            <a-tag color="red" v-if="record.isOverLevel == '1'">不合格</a-tag>
            <a-tag color="green" v-if="record.isOverLevel == '0'">合格</a-tag>
          </span>
        </a-table>
      </a-tab-pane>
    </a-tabs>
    <a-tabs default-active-key="1" @change="callback2">
      <a-tab-pane key="1" tab="智能压浆过程数据">
        <a-table rowKey="sid" :columns="columns1" :data-source="data1" :scroll="{ y: 400 }" :pagination="false">
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="2" tab="过程影像" >
<!--        <div class="imgButton" @click="showVideo()">过程影像</div>-->
      </a-tab-pane>
    </a-tabs>
    <a-tabs @change="callback">
      <a-tab-pane v-for="arry in data" :key="arry.holeid" :tab="arry.kongdao">

        <qxt-echarts title="压浆过程图1" :chart-style="chartStyle" :chartData="chartData1"></qxt-echarts>
        <qxt-echarts title="压浆过程图2" :chart-style="chartStyle" :chartData="chartData2"></qxt-echarts>
      </a-tab-pane>
    </a-tabs>
  </j-modal>
</template>

<script>
import { getAction } from '@api/manage'
import QxtEcharts from '@views/yj/modules/QxtEcharts'

export default {
  name: 'TrYajiangMSModal',
  components: {
    QxtEcharts,
  },
  data() {
    return {
      videotoken:'',
      begintime:'',
      endtime:'',
      remark:'无',
      data: [],
      ipagination: false,
      yajiangrenwudanms: false,
      columns: [
        {
          title: '压浆时间',
          align: 'center',
          dataIndex: 'yajiangsj',
        },
        {
          title: '孔道',
          align: 'center',
          dataIndex: 'kongdao',
        },
        {
          title: '压浆模式',
          align: 'center',
          dataIndex: 'yajiangmosh',
        },
        {
          title: '配合比',
          align: 'center',
          dataIndex: 'peihebi',
        },
        {
          title: '水胶比',
          align: 'center',
          dataIndex: 'shuijiaobi',
        },
        {
          title: '搅拌时间',
          align: 'center',
          dataIndex: 'jiaobansj',
        },
        {
          title: '开始时间',
          align: 'center',
          dataIndex: 'starttime',
        },
        {
          title: '结束时间',
          align: 'center',
          dataIndex: 'endtime',
        },
        {
          title: '进浆压力MPa',
          align: 'center',
          dataIndex: 'jinjiangyal',
        },
        {
          title: '返浆压力MPa',
          align: 'center',
          dataIndex: 'fanjiangyal',
        },
        {
          title: '持续时间',
          align: 'center',
          dataIndex: 'chixushijia',
        },
        {
          title: '进浆量L',
          align: 'center',
          dataIndex: 'jinjiangshu',
        },
        {
          title: '返浆量L',
          align: 'center',
          dataIndex: 'fanjianglia',
        },
        {
          title: '真空泵压力MPa',
          align: 'center',
          dataIndex: 'zkyl',
        },
        {
          title: '压浆次数',
          align: 'center',
          dataIndex: 'yjcs',
        },
        {
          title: '是否合格',
          align: 'center',
          dataIndex: 'isOverLevel',
          scopedSlots: { customRender: 'isOverLevel' },
        },
        {
          title: '预警原因',
          align: 'center',
          dataIndex: 'overReason',
        },
      ],
      columns1: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '记录时间',
          align: 'center',
          dataIndex: 'jlsj',
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'zt',
        },
        {
          title: '进浆压力(MPa)',
          align: 'center',
          dataIndex: 'jjyl',
        },
        {
          title: '返浆压力(MPa)',
          align: 'center',
          dataIndex: 'fjyl',
        },
        {
          title: '进浆量(L)',
          align: 'center',
          dataIndex: 'jjl',
        },
        {
          title: '返浆量(L)',
          align: 'center',
          dataIndex: 'fjl',
        },
      ],
      columns2: [
        {
          title: '工程名称',
          align: 'center',
          dataIndex: 'gcmc',
        },
        {
          title: '梁板类型',
          align: 'center',
          dataIndex: 'lblx',
        },
        {
          title: '压浆时间',
          align: 'center',
          dataIndex: 'yjsj',
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'yjsbbh_dictText',
        },
        {
          title: '梁号',
          align: 'center',
          dataIndex: 'lianghao',
        },
        {
          title: '施工部位',
          align: 'center',
          dataIndex: 'sgbw',
        },
        {
          title: '掺减水剂量',
          align: 'center',
          dataIndex: 'cjsjl',
        },
        {
          title: '张拉时间',
          align: 'center',
          dataIndex: 'zlsj',
        },
        {
          title: '压浆剂',
          align: 'center',
          dataIndex: 'yajiangji',
        },
        {
          title: '孔道数',
          align: 'center',
          dataIndex: 'kongdaoshu',
        },
        {
          title: '压浆方向',
          align: 'center',
          dataIndex: 'yajiangfang',
        },
        {
          title: '压浆步骤',
          align: 'center',
          dataIndex: 'yajiangbuzh',
        },
        {
          title: '初始流动速度',
          align: 'center',
          dataIndex: 'chushisudu',
        },
        {
          title: '流动度',
          align: 'center',
          dataIndex: 'liudongdu',
        },
        {
          title: '值班人员',
          align: 'center',
          dataIndex: 'memo',
        },
        {
          title: '完成状态',
          align: 'center',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
        },
      ],
      data2: [],
      data1: [],
      title: '详情',
      width: 800,
      visible: false,
      disableSubmit: false,
      syjid: '',
      uuid: '',
      chartStyle: { width: '100%', height: '450px' },
      chartData1: {
        xData: [],
        series: [],
      },
      chartData2: {
        xData: [],
        series: [],
      },
      url: {
        list: '/yajiangs/trYajiangS/list1',
        listdetail: '/yajiangs/trYajiangSS/list',
        listbutton: '/yajiangm/trYajiangM/list2', //压浆任务单下压浆主表信息查询
        tokenlist: '/sys/systems/sysMessages/tokenid' // 获取视频播放token
      },
      holeid: '',
    }
  },
  mounted(){
    let e = this.$route.query.syjid;
    e && this.callFun(e)
  },
  methods: {

    showVideo() {
      this.videotoken = ''
      getAction(this.url.tokenlist, {remark:this.remark}).then(res => {
          if (res.success) {
            console.log("token",res)
            this.videotoken = res.result.token
            var devid =  res.result.devid
            if(devid === "无"){
              this.$message.warning("未匹配到摄像设备")
            }else{
              window.open('http://47.97.173.113:9271/VideoMonitor?id=' + devid +'&begintime='+this.begintime+'&endtime='+this.endtime+ '&token=' + this.videotoken)
            }

          }
        }
      )

    },

    callback(key) {
      this.yajiangmessagedetail(key)
      console.log(key)
    },
    callback1(key) {
      console.log(key)
    },
    callback2(key) {
      if(key ==2){
        this.showVideo()
      }
      console.log(key)
    },
    yajiangmessagedetail: function (holeid) {
      let param = {
        holeid: holeid,
        pageSize: 800,
        pageNo: 1,
      }
      getAction(this.url.listdetail, param).then((res) => {
        console.log(res)
        if (res.result.records.length > 0) {
          let jjylList = []
          let fjylList = []
          let jjlList = []
          let fjlList = []
          let datatimeList = []
          let data = res.result.records
          this.data1 = res.result.records
          data.forEach((item,index) => {
            if(index === 0){
              this.remark = item.shebeibianh
              this.begintime=item.jlsj
            }else{
              this.endtime = item.jlsj;
            }
            jjylList.push(item.jjyl)
            fjylList.push(item.fjyl)
            jjlList.push(item.jjl)
            fjlList.push(item.fjl)
            datatimeList.push(item.jlsj.substr(11))
          })
          this.chartData1.xData = datatimeList
          this.chartData1.series = [
            {
              name: '进浆压力(MPa)',
              type: 'line',
              smooth: true,
              yAxisIndex: 0,
              data: jjylList,
            },
            {
              name: '返浆压力(MPa)',
              type: 'line',
              smooth: true,
              yAxisIndex: 1,
              data: fjylList,
            },
          ]
          this.chartData2.xData = datatimeList
          this.chartData2.series = [
            {
              name: '进浆量(L)',
              type: 'line',
              smooth: true,
              yAxisIndex: 0,
              data: jjlList,
            },
            {
              name: '返浆量(L)',
              type: 'line',
              smooth: true,
              yAxisIndex: 1,
              data: fjlList,
            },
          ]
        } else {
          this.$message.warning('暂无此孔号的压浆过程!')
        }
      })
    },
    yajiangmessage: function () {
      //请求压浆的每个孔道的数据
      let param = { syjid: this.syjid }
      getAction(this.url.list, param).then((res) => {
        // console.log(res)
        if (res.result.length > 0) {
          this.data = res.result
          this.holeid = res.result[0].holeid
          setTimeout(this.yajiangmessagedetail(this.holeid), 1000)
          //console.log(this.data, '1')
        } else {
          this.$message.warning('此任务单下暂无压浆试验监测数据!')
        }
      })
    },
    yajiangMList: function () {
      let param = { uuid: this.uuid }
      getAction(this.url.listbutton, param).then((res) => {
        if (res.result.length > 0) {
          this.data2 = res.result
          this.syjid = res.result[0].syjid
          setTimeout(this.yajiangmessage(), 1000)
        } else {
          this.$message.warning('此任务单下暂无压浆试验监测数据!')
        }
      })
    },
    callFun(e) {
      this.syjid = e
      this.visible = true
      this.yajiangmessage()
    },
    callmeaages(e) {
      this.uuid = e
      this.visible = true
      this.yajiangrenwudanms = true
      this.yajiangMList()
    },
    close() {
      this.visible = false
    },
    handleOk() {
      this.visible = false
    },
    handleCancel() {
      this.visible = false;
    },
  },
}
</script>

<style lang="less" scoped>
.imgButton {
  margin-top: 30px;
  color: #1d3759;
  font-weight: 500;

  .viewerBox {
    display: flex;

    img {
      margin-right: 20px;
    }
  }

}
</style>
