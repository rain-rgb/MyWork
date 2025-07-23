<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="Erp端ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="erpid">
              <a-input-number v-model="model.erpid" placeholder="请输入Erp端ID" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="生产线(1或2)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="station">
              <a-input-number v-model="model.station" placeholder="请输入生产线(1或2)" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="任务单编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="code">
              <a-input v-model="model.code" placeholder="请输入任务单编号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="施工配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recipe">
              <a-input v-model="model.recipe" placeholder="请输入施工配合比编号"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="砂浆配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="morrec">
              <a-input v-model="model.morrec" placeholder="请输入砂浆配合比编号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dattim">
              <j-date placeholder="请选择创建日期" v-model="model.dattim" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="车辆编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vehicle">
              <a-input v-model="model.vehicle" placeholder="请输入车辆编号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="驾驶员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="driver">
              <a-input v-model="model.driver" placeholder="请输入驾驶员"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="生产方量(方)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="prodmete">
              <a-input-number v-model="model.prodmete" placeholder="请输入生产方量(方)" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="砂浆方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mormete">
              <a-input-number v-model="model.mormete" placeholder="请输入砂浆方量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="累计车次" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totvehs">
              <a-input-number v-model="model.totvehs" placeholder="请输入累计车次" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="累计方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totmete">
              <a-input-number v-model="model.totmete" placeholder="请输入累计方量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="质检员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qualitor">
              <a-input v-model="model.qualitor" placeholder="请输入质检员"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="标识" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="flag">
              <a-input v-model="model.flag" placeholder="请输入标识"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="note">
              <a-input v-model="model.note" placeholder="请输入备注"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'
import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
import { validateDuplicateValue } from '@/utils/util'

export default {
  name: 'YSSchedulingCarForm',
  mixins: [JEditableTableMixin],
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
      validatorRules: {
        erpid: [
          { required: true, message: '请输入Erp端ID!' },
        ],
      },
      url: {
        add: '/car/schedulingCar/add',
        edit: '/car/schedulingCar/edit',
        queryById: '/car/schedulingCar/queryById',
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