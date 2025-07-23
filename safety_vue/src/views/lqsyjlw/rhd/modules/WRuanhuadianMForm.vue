<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="false">
      <a-form :form="form" slot="detail">
        <a-card title="基础信息" :bordered="false" :headStyle="{ color: '#0785fd' }">
          <a-row>
            <a-col :span="12">
              <a-form-item label="样品编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['sampleno']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="样品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['samplename']"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="样品描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['samplems']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['biaozhunzhi1']"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="工程部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['gcbuwei']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['projectname']"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="试验时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['isTesttime']"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card title="检测信息" :bordered="false" :headStyle="{ color: '#0785fd' }">
          <a-row>
            <a-col :span="12">
              <a-form-item label="软化点1（℃）" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['ruanhuadian1']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="软化点2（℃）" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['ruanhuadian2']"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="软化点平均值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['rhdavg']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="是否合格" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['isqualified']"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card title="温度变化曲线图" :bordered="false" :headStyle="{ color: '#0785fd' }">
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
  name: 'WRuanhuadianMForm',
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
        add: '/ruanhuadu/wRuanhuadianM/add',
        edit: '/ruanhuadu/wRuanhuadianM/edit',
        queryById: '/ruanhuadu/wRuanhuadianM/queryById',
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
            'sampleno',
            'samplename',
            'samplems',
            'biaozhunzhi1',
            'gcbuwei',
            'projectname',
            'isTesttime',
            'ruanhuadian1',
            'ruanhuadian2',
            'rhdavg',
            'isqualified'
          )
        )
        this.initEcharts()
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
        pick(
          row,
          'sampleno',
          'samplename',
          'samplems',
          'biaozhunzhi1',
          'gcbuwei',
          'projectname',
          'isTesttime',
          'ruanhuadian1',
          'ruanhuadian2',
          'rhdavg',
          'isqualified'
        )
      )
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
              normal: {
                color: '#1890ff', //改变折线点的颜色
                lineStyle: {
                  color: '#1890ff', //改变折线颜色
                },
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