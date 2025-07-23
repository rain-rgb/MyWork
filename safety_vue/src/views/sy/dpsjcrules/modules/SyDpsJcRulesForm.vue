<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="titcode" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titcode">-->
          <!--              <a-input v-model="model.titcode" placeholder="请输入titcode"  ></a-input>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="规程编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ruleno">
              <a-input v-model="model.ruleno" placeholder="请输入规程编号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规程名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rulename">
              <a-input v-model="model.rulename" placeholder="请输入规程名称"></a-input>
            </a-form-model-item>
          </a-col>
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="规程依据类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ruletype">-->
          <!--              <a-input v-model="model.ruletype" placeholder="请输入规程依据类型"  ></a-input>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="规程类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['titcode']" :trigger-change="true"
                                 placeholder="请选择规程类型"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规程依据类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-model="model.ruletype" :trigger-change="true" dictCode="ruleType"
                                 placeholder="请选择规程依据类型"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规程单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ruleunit">
              <a-input v-model="model.ruleunit" placeholder="请输入规程单位"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规程实施时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rulebegindate">
              <a-input v-model="model.rulebegindate" placeholder="请输入规程实施时间"></a-input>
            </a-form-model-item>
          </a-col>
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="显示状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ruleisshow">-->
          <!--              <a-input-number v-model="model.ruleisshow" placeholder="请输入显示状态" style="width: 100%" />-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ruleremark">
              <a-input v-model="model.ruleremark" placeholder="请输入备注"></a-input>
            </a-form-model-item>
          </a-col>
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="删除状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ruleisdel">-->
          <!--              <a-input-number v-model="model.ruleisdel" placeholder="请输入删除状态" style="width: 100%" />-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'
import { validateDuplicateValue } from '@/utils/util'

export default {
  name: 'SyDpsJcRulesForm',
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
        add: '/sydpsjcrules/syDpsJcRules/add',
        edit: '/sydpsjcrules/syDpsJcRules/edit',
        queryById: '/sydpsjcrules/syDpsJcRules/queryById'
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