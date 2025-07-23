<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="false">
      <a-form :form="form" slot="detail">
        <a-card title="检测值信息" :bordered="false" :headStyle="{ color: '#0785fd' }">
          <a-row>
            <a-col :span="8">
              <a-form-item label="延度1(cm)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['yandu11']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="延度2(cm)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['yandu12']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="延度3(cm)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['yandu13']"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="8">
              <a-form-item label="拉力值1(N)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['llz1']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="拉力值2(N)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['llz2']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="拉力值3(N)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['llz3']"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="最大拉力(N)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['llzmax']"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="平均延度(cm)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['ydavg']"></a-input>
              </a-form-item>
            </a-col>


          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="测试温度(℃)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['testtemp']"></a-input>
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item label="设定温度(℃)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['designtemp']"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card title="拉力-延度曲线图" :bordered="false" :headStyle="{ color: '#0785fd' }">
          <div ref="qxt" :style="chartStyle"></div>
        </a-card>
        <a-row>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from '@/api/manage'
import pick from 'lodash.pick'
import * as echarts from 'echarts'
import { validateDuplicateValue } from '@/utils/util'
import JFormContainer from '@/components/jeecg/JFormContainer'

export default {
  name: 'WYanduMForm',
  components: {
    JFormContainer,
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {},
      required: false,
    },
    //表单模式：true流程表单 false普通表单
    formBpm: {
      type: Boolean,
      default: false,
      required: false,
    },
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      form: this.$form.createForm(this),
      model: {},
      xList: [],
      yList1: [],
      yList2: [],
      yList3: [],
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      chartStyle: { width: '100%', height: '320px' }, //图表样式
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: '/yd/wYanduM/add',
        edit: '/yd/wYanduM/edit',
        queryById: '/yd/wYanduM/queryById',
        pull: '/ydcx/wYanduS/list',
      },
    }
  },
  computed: {
    formDisabled() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return false
        }
        return true
      }
      return this.disabled
    },
    showFlowSubmitButton() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true
        }
      }
      return false
    },
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData()
  },
  methods: {
    add() {
      this.edit({})
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(
          pick(
            this.model,
            'yandu11',
            'yandu12',
            'yandu13',
            'ydavg',
            'testtemp',
            'llz1',
            'llz2',
            'llz3',
            'llzmax',
            'designtemp'
          )
        )
        this.getPull()
      })
    },
    //渲染流程表单数据
    showFlowData() {
      if (this.formBpm === true) {
        let params = { id: this.formData.dataId }
        getAction(this.url.queryById, params).then((res) => {
          if (res.success) {
            this.edit(res.result)
          }
        })
      }
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          let formData = Object.assign(this.model, values)
          console.log('表单提交数据', formData)
          httpAction(httpurl, formData, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            })
            .finally(() => {
              that.confirmLoading = false
            })
        }
      })
    },
    popupCallback(row) {
      this.form.setFieldsValue(
        pick(row, 'yandu11', 'yandu12', 'yandu13', 'ydavg', 'testtemp', 'llz1', 'llz2', 'llz3', 'llzmax', 'designtemp')
      )
    },
    getPull() {
      this.xList = []
      this.yList1 = []
      this.yList2 = []
      this.yList3 = []
      let params = { syjid: this.model.syjid }
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
                normal: {
                  color: '#FD2B1E', //改变折线点的颜色
                  lineStyle: {
                    color: '#FD2B1E', //改变折线颜色
                  },
                },
              },
            },
            {
              name: '拉力2',
              type: 'line',
              smooth: true,
              data: this.yList2,
              itemStyle: {
                normal: {
                  color: '#1A9BFF', //改变折线点的颜色
                  lineStyle: {
                    color: '#1A9BFF', //改变折线颜色
                  },
                },
              },
            },
            {
              name: '拉力3',
              type: 'line',
              smooth: true,
              data: this.yList3,
              itemStyle: {
                normal: {
                  color: '#1AAD19', //改变折线点的颜色
                  lineStyle: {
                    color: '#1AAD19', //改变折线颜色
                  },
                },
              },
            },
          ]
          let legend = ['拉力1', '拉力2', '拉力3']
          series = series.slice(0, data.length)
          legend = legend.slice(0, data.length)
          this.initEcharts(legend, series)
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
  },
}
</script>