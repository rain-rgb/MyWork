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
      <a-tab-pane key="1" tab="软化度试验详情数据">
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
        <a-card title="温度变化曲线图" :bordered="false" :headStyle="{ color: '#0785fd' }">
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
      
       labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      loading: false,
      ipagination: false,
      chartStyle: { width: '100%', height: '320px' }, //图表样式
      validatorRules: {},
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
    
    callchuzhishenhe(e) {
      this.syjid = e.syjid
      this.visible = true
       this.model = Object.assign({}, e)
       console.log(this.model,"model");
      // this.zhanglamessage()
      this.overHandler()
      this.$nextTick(() => {this.initEcharts()})
      
    },
    handleOk() {
      this.visible = false
    },
    handleCancel() {
      this.visible = false
    },
    initEcharts() {
      let xList = this.model.gcsj.split(',')
      let yList = this.model.gcz.split(',')
      let option = {
        // grid: {
        //   left: '3%',
        //   right: '4%',
        //   bottom: '3%',
        //   containLabel: true,
        // },
        // legend: {
        //   data: ['温度'],
        // },
        tooltip: {
          trigger: 'axis',
          formatter: function (params) {
            var relVal = params[0].name
            for (var i = 0, l = params.length; i < l; i++) {
              relVal += '<br/>' + params[i].marker + '温度' + params[i].value + '℃'
            }
            return relVal
          },
        },
        // title: {
        //   text: '曲线图',
        //   top: 10,
        //   left: 10,
        // },
        xAxis: {
          type: 'category',
          name: '时间(min)',
          boundaryGap: false,
          axisLabel: {
            interval: 0,
            fontSize: 16,
            formatter: '{value}',
          },
          data: xList,
        },
        yAxis: {
          type: 'value',
          name: '温度(℃)',
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
        series: [
          {
            name: '温度',
            type: 'line',
            smooth: true,
            data: yList,
            itemStyle: {
              color: '#1890ff', //改变折线点的颜色
              lineStyle: {
                color: '#1890ff', //改变折线颜色
              },
            },
          },
        ],
      }
      let ele = this.$refs.qxt
      let tCharts = echarts.init(ele)
      tCharts.setOption(option)
    },
  },
}
</script>

<style scoped>
</style>
