<template>
  <j-modal
    :title="title"
    :width="1400"
    :visible="visible"
    :maskClosable="false"
    switchFullscreen
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="延度试验详情数据">
        <!-- <a-table
          :rowKey="(record, index) => index"
          :pagination="ipagination"
          :columns="columns"
          :loading="loading"
          :data-source="data"
          bordered
          size="middle"
          class="j-table-force-nowrap"
        >
        </a-table> -->
        <a-card title="拉力-延度曲线图" :bordered="false" :headStyle="{ color: '#0785fd' }">
          <div ref="qxt" :style="chartStyle"></div>
        </a-card>
      </a-tab-pane>
    </a-tabs>
    <a-form>
      <j-form-container disabled>
        <a-card
          title="施工方处置信息"
          :bordered="false"
          :headStyle="{ color: '#0785fd' }"
          :bodyStyle="{ padding: '10' }"
          style="margin-top: 10px"
        >
          <a-row>
            <a-col :span="12">
              <a-form-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-textarea
                  placeholder="无"
                  v-model="overHandlerObj.problemReasons"
                  :auto-size="{ minRows: 5, maxRows: 20 }"
                ></a-textarea>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.handleWay" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.handleResult" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.handleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处置人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.handlePerson" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="处置附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <viewer :images="detailList">
                  <img
                    v-for="(img, index) in detailList"
                    :key="index"
                    style="height: 100px; width: 100px; margin: 5px 10px 5px 10px; float: left"
                    :src="img"
                    alt=""
                  />
                </viewer>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card
          title="监理单位审核"
          :bordered="false"
          :headStyle="{ color: '#0785fd' }"
          :bodyStyle="{ padding: '10' }"
          style="margin-top: 10px"
        >
          <a-row>
            <a-col :span="12">
              <a-form-item label="监理审核" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-if="overHandlerObj.overproofStatus === 30"
                  v-model="overHandlerObj.remark"
                  placeholder="无"
                ></a-input>
                <a-input v-else v-model="overHandlerObj.supervisorApproval" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="审核结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-if="overHandlerObj.overproofStatus === 30" placeholder="驳回" default-value="驳回"></a-input>
                <a-input v-else v-model="overHandlerObj.supervisorResult" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.approvalPerson" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="监理处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.supervisorHandleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="24">
              <a-form-item label="审核附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <viewer :images="detailList1">
                  <img
                    v-for="(img, index) in detailList1"
                    :key="index"
                    style="height: 100px; width: 100px; margin: 5px 10px 5px 10px; float: left"
                    :src="img"
                    alt=""
                  />
                </viewer>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card
          title="指挥部核查"
          :bordered="false"
          :headStyle="{ color: '#0785fd' }"
          :bodyStyle="{ padding: '10' }"
          style="margin-top: 10px"
        >
          <a-row>
            <a-col :span="12">
              <a-form-item label="指挥部核查" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-if="
                    overHandlerObj.overproofStatus === 60 ||
                    (overHandlerObj.overproofStatus !== 20 &&
                      overHandlerObj.headquartersRemark !== null &&
                      overHandlerObj.headquartersRemark !== undefined)
                  "
                  v-model="overHandlerObj.headquartersRemark"
                ></a-input>
                <a-input v-else v-model="overHandlerObj.headquartersApproval" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="核查结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-if="
                    overHandlerObj.overproofStatus === 60 ||
                    (overHandlerObj.overproofStatus !== 20 &&
                      overHandlerObj.headquartersRemark !== null &&
                      overHandlerObj.headquartersRemark !== undefined)
                  "
                  placeholder="驳回"
                  default-value="驳回"
                ></a-input>
                <a-input v-else v-model="overHandlerObj.headquartersResult" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="核查人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.headquartersPerson" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="核查时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.headquartersHandleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
      </j-form-container>
    </a-form>

    <g-ch-modal ref="gch"></g-ch-modal>
  </j-modal>
</template>

<script>
import { getAction } from '@api/manage'
import * as echarts from 'echarts'
import GChModal from '@/views/zlyj/modules/GChModal'
export default {
  name: 'TrZhanglaMModalChuZhiShenHeTwo',
  components: {
    GChModal,
  },
  data() {
    return {
      data: [],
      xList: [],
      yList1: [],
      yList2: [],
      yList3: [],
      loading: false,
      ipagination: false,
      chartStyle: { width: '100%', height: '320px' }, //图表样式
      columns: [
        {
          title: '延度设备名称',
          align: 'center',
          dataIndex: 'fsbbh_dictText',
        },
        {
          title: '试验时间',
          align: 'center',
          dataIndex: 'isTesttime',
        },
        {
          title: '工程名称',
          align: 'center',
          dataIndex: 'projectname',
        },
        {
          title: '样品编号',
          align: 'center',
          dataIndex: 'sampleno',
        },
        {
          title: '工程部位',
          align: 'center',
          dataIndex: 'gcbuwei',
        },
        {
          title: '样品名称',
          align: 'center',
          dataIndex: 'samplename',
        },
        {
          title: '样品描述',
          align: 'center',
          dataIndex: 'samplems',
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'isqualified',
          customRender: (text, record, index) => {
            let childrenVal
            if (text == 0 || text == null) {
              childrenVal = (
                <div>
                  <a-tag color="green">合格</a-tag>
                </div>
              )
            } else {
              childrenVal = (
                <div>
                  <a-tag color="red">不合格</a-tag>
                </div>
              )
            }
            const obj = {
              children: childrenVal,
              attrs: {},
            }
            if (index === 0 || index % 3 === 0) {
              obj.attrs.rowSpan = 3
            } else {
              obj.attrs.rowSpan = 0
            }
            return obj
          },
        },
        // {
        //   title: '预警原因',
        //   align: 'center',
        //   dataIndex: 'overReason',
        //   customRender: (text, record, index) => this.columnsInit(text, index, 6),
        // },
      ],
      title: '',
      width: 800,
      visible: false,
      disableSubmit: true,
      syjid: '',
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      labelCol1: {
        xs: { span: 24 },
        sm: { span: 3 },
      },
      wrapperCol1: {
        xs: { span: 24 },
        sm: { span: 20 },
      },
      overHandlerObj: {},
      detailList: [],
      detailList1: [],
      url: {
        list: '/syjoverhandler/syjOverHandler/list',
        pull: '/ydcx/wYanduS/list',
      },
    }
  },
  methods: {
    getPull() {
      this.xList = []
      this.yList1 = []
      this.yList2 = []
      this.yList3 = []
      let params = { syjid: this.syjid }
      getAction(this.url.pull, params).then((res) => {
        let data = res.result.records
        if (res.success && data.length > 0) {
          this.xList = data[0].yandu1.split(',')
          data.forEach((item) => {
            if (item.fxh == '1') {
              this.yList1 = item.zll.split(',')
            } else if (item.fxh == '2') {
              this.yList2 = item.zll.split(',')
            } else if (item.fxh == '3') {
              this.yList3 = item.zll.split(',')
            }
          })
          let series = [
            {
              name: '拉力1',
              type: 'line',
              smooth: true,
              data: this.yList1,
              itemStyle: {
                color: '#FD2B1E', //改变折线点的颜色
                lineStyle: {
                  color: '#FD2B1E', //改变折线颜色
                },
              },
            },
            {
              name: '拉力2',
              type: 'line',
              smooth: true,
              data: this.yList2,
              itemStyle: {
                color: '#1A9BFF', //改变折线点的颜色
                lineStyle: {
                  color: '#1A9BFF', //改变折线颜色
                },
              },
            },
            {
              name: '拉力3',
              type: 'line',
              smooth: true,
              data: this.yList3,
              itemStyle: {
                color: '#1AAD19', //改变折线点的颜色
                lineStyle: {
                  color: '#1AAD19', //改变折线颜色
                },
              },
            },
          ]
          let legend = ['拉力1', '拉力2', '拉力3']
          series = series.slice(0, data.length)
          legend = legend.slice(0, data.length)
          this.initEcharts(legend, series)
        } else {
          this.$message.warning('暂无折线图数据!')
        }
      })
    },
    // zhanglamessage() {
    //   this.loading = true
    //   let param = { syjid: this.syjid, overLevel_begin: 1 }
    //   getAction(this.url.list1s, param)
    //     .then((res) => {
    //       if (res.success) {
    //         this.data = res.result
    //       } else {
    //         this.data = []
    //       }
    //     })
    //     .finally(() => {
    //       this.loading = false
    //     })
    // },
    overHandler() {
      this.overHandlerObj = {}
      this.detailList = []
      this.detailList1 = []
      let param = { baseid: this.syjid }
      getAction(this.url.list, param).then((res) => {
        if (res.success) {
          if (res.result.records.length > 0) {
            this.overHandlerObj = res.result.records[0]
            this.detailList = this.overHandlerObj.filePath2 ? this.overHandlerObj.filePath2.split(',') : []
            this.detailList1 = this.overHandlerObj.filePath ? this.overHandlerObj.filePath.split(',') : []
          }
        }
      })
    },
    initEcharts(legend, series) {
      let option = {
        legend: {
          data: legend,
        },
        tooltip: {
          trigger: 'axis',
        },
        xAxis: {
          type: 'category',
          name: '延度(mm)',
          boundaryGap: false,
          axisLabel: {
            interval: 3,
            fontSize: 16,
            formatter: '{value}',
          },
          data: this.xList,
        },
        yAxis: {
          type: 'value',
          name: '拉力(N)',
          nameTextStyle: {
            align: 'center',
            fontSize: 16,
          },
          axisLabel: {
            fontSize: 16,
            formatter: '{value}',
          },
          splitLine: {
            show: true,
            lineStyle: {
              type: 'dashed',
            },
          },
        },
        series: series,
      }
      let ele = this.$refs.qxt
      let tCharts = echarts.init(ele)
      tCharts.setOption(option)
    },
    callchuzhishenhe(e) {
      this.syjid = e.syjid
      this.visible = true
      // this.zhanglamessage()
      this.overHandler()
      this.getPull()
    },
    handleOk() {
      this.visible = false
    },
    handleCancel() {
      this.visible = false
    },
    //表格合并
    columnsInit(text, index, num) {
      const obj = {
        children: text,
        attrs: {},
      }
      if (index === 0 || index % num === 0) {
        obj.attrs.rowSpan = num
      } else {
        obj.attrs.rowSpan = 0
      }
      return obj
    },
    gchDetail(record) {
      this.$refs.gch.getList(record)
    },
  },
}
</script>

<style scoped>
</style>
