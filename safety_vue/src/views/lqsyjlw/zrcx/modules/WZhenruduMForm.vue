<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
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
              <a-form-item label="工程部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['gcbuwei']"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="最小标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['biaozhunzhi1']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="最大标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['biaozhunzhi2']"></a-input>
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
        <a-card title="测量信息" :bordered="false" :headStyle="{ color: '#0785fd' }">
          <a-row>
            <a-col :span="8">
              <a-form-item label="针入度1" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['zrd1']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="针入度2" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['zrd2']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="针入度3" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['zrd3']"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="8">
              <a-form-item label="平均针入度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['zrdavg']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="水浴温度(℃)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['gcz']"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="时间(秒)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['gcsj']"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="8">
              <a-form-item label="是否合格" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['isqualified']"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
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
import { validateDuplicateValue } from '@/utils/util'
import JFormContainer from '@/components/jeecg/JFormContainer'

export default {
  name: 'WZhenruduMForm',
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
      confirmLoading: false,
      validatorRules: {
        syjid: {
          rules: [{ required: true, message: '请输入syjid!' }],
        },
      },
      url: {
        add: '/zhenru/wZhenruduM/add',
        edit: '/zhenru/wZhenruduM/edit',
        queryById: '/zhenru/wZhenruduM/queryById',
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
            'gcbuwei',
            'biaozhunzhi1',
            'biaozhunzhi2',
            'isTesttime',
            'zrd1',
            'zrd2',
            'zrd3',
            'zrdavg',
            'gcz',
            'gcsj',
            'isqualified'
          )
        )
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
          'gcbuwei',
          'biaozhunzhi1',
          'biaozhunzhi2',
          'isTesttime',
          'zrd1',
          'zrd2',
          'zrd3',
          'zrdavg',
          'gcz',
          'gcsj',
          'isqualified'
        )
      )
    },
  },
}
</script>