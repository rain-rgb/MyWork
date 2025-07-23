<template>
    <j-modal
        centered
        :title="title"
        :width="1400"
        :visible="visible"
        @ok="handleOk"
        :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
        @cancel="handleCancel"
        cancelText="关闭"
    >
        <a-table
            :rowKey="(record, index) => index"
            size="small"
            bordered
            :columns="columns"
            :dataSource="dataSource"
            :pagination="false"
            :loading="loading"
            :scroll="{ y: 300 }"
        ></a-table>
        <a-tabs default-active-key="1">
            <a-tab-pane key="1" tab="智能张拉过程图">
              <div class="imgButton" @click="showVideo()">过程影像</div>
                <div style="height: 500px; overflow: auto">
                    <qxt-echarts title="张拉力过程图" :chart-style="chartStyle" :chartData="chartData1"></qxt-echarts>
                    <qxt-echarts title="伸长量过程图" :chart-style="chartStyle" :chartData="chartData3"></qxt-echarts>
                    <qxt-echarts title="油压过程图" :chart-style="chartStyle" :chartData="chartData2"></qxt-echarts>
                </div>
            </a-tab-pane>
        </a-tabs>
    </j-modal>
</template>

<script>
import { getAction } from '@/api/manage'
import QxtEcharts from '@views/yj/modules/QxtEcharts'

export default {
    name: 'GChModal',
    components: {
        QxtEcharts,
    },
    data() {
        return {
            visible: false,
            loading: false,
            disableSubmit: true,
            chartStyle: { width: '100%', height: '460px' },
            chartData1: {
                xData: [],
                series: [],
            },
            chartData2: {
                xData: [],
                series: [],
            },
            chartData3: {
                xData: [],
                series: [],
            },
            zhanglaObj: {},
            title: '',
            dataSource: [],
          videotoken:'',
          begintime:'',
          endtime:'',
          remark:'无',
            columns: [
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
                    width:160,
                    align: 'center',
                    dataIndex: 'jlsj',
                },
                {
                    title: '张拉次数',
                    align: 'center',
                    dataIndex: 'zlcs',
                },
                // {
                //     title: '断面',
                //     align: 'center',
                //     dataIndex: 'dh',
                // },
                {
                    title: '张拉力1(KN)',
                    align: 'center',
                    dataIndex: 'zll1',
                },
                {
                    title: '油压1(Mpa)',
                    align: 'center',
                    dataIndex: 'yy1',
                },
                {
                    title: '顶行程1(mm)',
                    align: 'center',
                    dataIndex: 'dxc1',
                },
                {
                    title: '伸长量1(mm)',
                    align: 'center',
                    dataIndex: 'scl1',
                },
                {
                    title: '状态',
                    align: 'center',
                    dataIndex: 'zt1',
                },
                {
                    title: '状态2',
                    align: 'center',
                    dataIndex: 'zt2',
                },
                {
                    title: '张拉力2(KN)',
                    align: 'center',
                    dataIndex: 'zll2',
                },
                {
                    title: '油压2(Mpa)',
                    align: 'center',
                    dataIndex: 'yy2',
                },
                {
                    title: '顶行程2(mm)',
                    align: 'center',
                    dataIndex: 'dxc2',
                },
                {
                    title: '伸长量2(mm)',
                    align: 'center',
                    dataIndex: 'scl2',
                },
            ],
            url: {
                list1: '/zhanglass/trZhanglaSS/list1', //张拉每个孔道的过程数据
                tokenlist: '/sys/systems/sysMessages/tokenid' // 获取视频播放token
            },
        }
    },
    created() {

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


        getList(record) {
            this.title = record.gsbh + '孔道'
            this.zhanglaObj = Object.assign({}, record)
            this.zhanglass()
            this.visible = true
        },
        zhanglass() {
            //张拉每个孔道的过程数据
            let param = {
                holeid: this.zhanglaObj.holeid,
                pageSize: 800,
                pageNo: 1,
            }
            this.loading = true
            getAction(this.url.list1, param)
                .then((res) => {
                    if (res.success) {
                        let zll1List = []
                        let zll2List = []
                        let yy1List = []
                        let yy2List = []
                        let scl1List = []
                        let scl2List = []
                        let datatimeList = []
                        this.dataSource = res.result.records
                        let data = res.result.records
                        data.forEach((item, index) => {
                          if(index == 0){
                            this.remark = item.shebeibianhao
                            this.begintime=item.jlsj
                          }else{
                            this.endtime=item.jlsj
                          }
                            zll1List.push(item.zll1)
                            zll2List.push(item.zll2)
                            yy1List.push(item.yy1)
                            yy2List.push(item.yy2)
                            scl1List.push(item.scl1)
                            scl2List.push(item.scl2)
                            datatimeList.push(item.jlsj.substr(11))
                        })
                        this.chartData1.xData = datatimeList
                        this.chartData1.series = [
                            {
                                name: '张拉力1(KN)',
                                type: 'line',
                                smooth: true,
                                yAxisIndex: 0,
                                data: zll1List,
                            },
                            {
                                name: '张拉力2(KN)',
                                type: 'line',
                                smooth: true,
                                yAxisIndex: 1,
                                data: zll2List,
                            },
                        ]
                        this.chartData2.xData = datatimeList
                        this.chartData2.series = [
                            {
                                name: '油压1(Mpa)',
                                type: 'line',
                                smooth: true,
                                yAxisIndex: 0,
                                data: yy1List,
                            },
                            {
                                name: '油压2(Mpa)',
                                type: 'line',
                                smooth: true,
                                yAxisIndex: 1,
                                data: yy2List,
                            },
                        ]
                        this.chartData3.xData = datatimeList
                        this.chartData3.series = [
                            {
                                name: '伸长量1(mm)',
                                type: 'line',
                                smooth: true,
                                yAxisIndex: 0,
                                data: scl1List,
                            },
                            {
                                name: '伸长量2(mm)',
                                type: 'line',
                                smooth: true,
                                yAxisIndex: 1,
                                data: scl2List,
                            },
                        ]
                    } else {
                        this.$message.warning('暂无此孔号的张拉过程!')
                    }
                })
                .finally(() => {
                    this.loading = false
                })
        },
        handleCancel() {
            this.visible = false
        },
        handleOk() {
            this.visible = false
        },
    },
}
</script>
<style lang="less" scoped>
.imgButton {
  margin-top: 30px;
  position: absolute;
  left: 11%;
  top: -2.5%;
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