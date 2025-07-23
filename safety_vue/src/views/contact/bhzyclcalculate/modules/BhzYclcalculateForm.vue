<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="库别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="kubie">
              <a-input v-model="model.kubie" placeholder="请输入库别"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="类别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="leibie">
              <a-input v-model="model.leibie" placeholder="请输入类别"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="物资名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wzName">
              <a-input v-model="model.wzName" placeholder="请输入物资名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="型号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xinghao">
              <a-input v-model="model.xinghao" placeholder="请输入型号"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guige">
              <a-input v-model="model.guige" placeholder="请输入规格"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="辅助规格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fuzhuGuige">
              <a-input v-model="model.fuzhuGuige" placeholder="请输入辅助规格"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="danwei">
              <a-input v-model="model.danwei" placeholder="请输入单位"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="上期末物资支出" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lastWzzc">
              <a-input v-model="model.lastWzzc" placeholder="请输入上期末物资支出"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="上期末系统消耗" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lastSysxh">
              <a-input v-model="model.lastSysxh" placeholder="请输入上期末系统消耗"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="上期末节超" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lastJc">
              <a-input v-model="model.lastJc" placeholder="请输入上期末节超(节+超-)"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="本期物资支出" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="thisWzzc">
              <a-input v-model="model.thisWzzc" placeholder="请输入本期物资支出"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="本期系统消耗" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="thisSysxh">
              <a-input v-model="model.thisSysxh" placeholder="请输入本期系统消耗"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="本期节超" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="thisJc">
              <a-input v-model="model.thisJc" placeholder="请输入本期节超(节+超-)"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="开累物资支出" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="klWzzc">
              <a-input v-model="model.klWzzc" placeholder="请输入开累物资支出"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="开累系统消耗" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="klSysxh">
              <a-input v-model="model.klSysxh" placeholder="请输入开累系统消耗"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="开累节超" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="klJc">
              <a-input v-model="model.klJc" placeholder="请输入开累节超(节+超-)"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beizhu">
              <a-input v-model="model.beizhu" placeholder="请输入备注"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'
import { validateDuplicateValue } from '@/utils/util'

export default {
  name: 'BhzYclcalculateForm',
  components: {},
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data() {
    return {
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
      validatorRules: {},
      url: {
        add: '/bhzyclcalculate/bhzYclcalculate/add',
        edit: '/bhzyclcalculate/bhzYclcalculate/edit',
        queryById: '/bhzyclcalculate/bhzYclcalculate/queryById'
      }
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    },
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
  },
  methods: {
    add() {
      this.edit(this.modelDefault)
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.visible = true
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
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
          httpAction(httpurl, this.model, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message)
              that.$emit('ok')
            } else {
              that.$message.warning(res.message)
            }
          }).finally(() => {
            that.confirmLoading = false
          })
        }

      })
    },
  }
}
</script>