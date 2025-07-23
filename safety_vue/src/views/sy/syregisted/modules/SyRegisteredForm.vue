<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="检测项目" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="checkproject">
              <a-input-number v-model="model.checkproject" placeholder="请输入检测项目（0：水泥胶砂强度）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="责任人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="owner">
              <a-input v-model="model.owner" placeholder="请输入责任人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="不合格类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="buhegetype">
              <a-input-number v-model="model.buhegetype" placeholder="请输入不合格类型（0：主动；1：被动）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="time">
              <j-date placeholder="请选择时间" v-model="model.time"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="原因描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="原因描述">
              <a-input v-model="model.reason" placeholder="请输入原因描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="罚款奖励金额数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="amount">
              <a-input v-model="model.amount" placeholder="请输入罚款奖励金额数"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="订单所属组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysorgcode">
              <a-input v-model="model.sysorgcode" placeholder="请输入订单所属组织机构"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="闭合状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysorgcode">
              <a-input v-model="model.closestatus" placeholder="请输入闭合状态（0:未闭合；1:已闭合）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
          
               <a-form-model-item label="闭合时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="time">
              <j-date placeholder="请选择时间" v-model="model.closetime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="闭合人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysorgcode">
              <a-input v-model="model.closeren" placeholder="请输入闭合人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="闭合措施" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysorgcode">
              <a-input v-model="model.closesteps" placeholder="请输入闭合措施"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="登记人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysorgcode">
              <a-input v-model="model.registerren" placeholder="请输入登记人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
           
             <a-form-model-item label="登记时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="time">
              <j-date placeholder="请选择时间" v-model="model.registertime"  style="width: 100%" />
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
  name: 'SyRegisteredForm',
  components: {},
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
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
        add: '/syRegistered/syRegistered/add',
        edit: '/syRegistered/syRegistered/edit',
        queryById: '/syRegistered/syRegistered/queryById',
      },
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
      this.$refs.form.validate((valid) => {
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
          httpAction(httpurl, this.model, method)
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
  },
}
</script>