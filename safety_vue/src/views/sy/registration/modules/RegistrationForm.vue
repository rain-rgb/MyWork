<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="实验室名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysName">
              <a-input v-model="model.sysName" placeholder="请输入实验室名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="评分表名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="evaluationName">
              <a-input v-model="model.evaluationName" placeholder="请输入评分表名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="所属组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JselectDqDepart v-model="model.sysOrgCode" ::multi="false"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="年份" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="year">
              <j-search-select-tag type="list" v-model="model.year" :trigger-change="true"
                                   placeholder="请选择年份" :dictOptions="dictOption1"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="季度" :rules="[{ required: true, message: '季度不能为空' }]"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="season">
              <j-search-select-tag type="list" v-model="model.season" :trigger-change="true"
                                   placeholder="请选择季度" :dictOptions="dictOption2"/>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="人员管理扣分原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="personnelReason">-->
<!--              <a-input v-model="model.personnelReason" placeholder="请输入人员管理扣分原因"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="人员管理分数" :rules="[{ required: true, message: '人员管理分数不能为空' }]"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="personnelNum">
              <a-input-number v-model="model.personnelNum" placeholder="请输入人员管理分数" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="仪器设备扣分原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeiReason">-->
<!--              <a-input v-model="model.shebeiReason" placeholder="请输入仪器设备扣分原因"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="仪器设备分数" :rules="[{ required: true, message: '仪器设备分数不能为空' }]"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeiNum">
              <a-input-number v-model="model.shebeiNum" placeholder="请输入仪器设备分数" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="实验环境扣分原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="environmentReason">-->
<!--              <a-input v-model="model.environmentReason" placeholder="请输入实验环境扣分原因"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="实验环境分数" :rules="[{ required: true, message: '实验环境分数不能为空' }]"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="environmentNum">
              <a-input-number v-model="model.environmentNum" placeholder="请输入实验环境分数" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="检测行为扣分原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="behaviorReason">-->
<!--              <a-input v-model="model.behaviorReason" placeholder="请输入检测行为扣分原因"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="检测行为分数" :rules="[{ required: true, message: '检测行为分数不能为空' }]"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="behaviorNum">
              <a-input-number v-model="model.behaviorNum" placeholder="请输入检测行为分数" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="内业资料扣分原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="informationReason">-->
<!--              <a-input v-model="model.informationReason" placeholder="请输入内业资料扣分原因"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="内业资料分数" :rules="[{ required: true, message: '内业资料分数不能为空' }]"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="informationNum">
              <a-input-number v-model="model.informationNum" placeholder="请输入内业资料分数" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="体系管理扣分原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="systemReason">-->
<!--              <a-input v-model="model.systemReason" placeholder="请输入体系管理扣分原因"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="体系管理分数" :rules="[{ required: true, message: '体系管理分数不能为空' }]"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="systemNum">
              <a-input-number v-model="model.systemNum" placeholder="请输入体系管理分数" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@api/manage'
import JselectDqDepart from '@/components/jeecgbiz/JselectDqDepart'//当前用户的组织机构权限（当前使用）
import { validateDuplicateValue } from '@/utils/util'

export default {
  name: 'RegistrationForm',
  components: {
    JselectDqDepart
  },
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
      model: {
        year:'2023',
      },
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
      dictOption1: [],
      dictOption2: [
        {
          text: '第一季度',
          value: '1'
        }, {
          text: '第二季度',
          value: '2'
        }, {
          text: '第三季度',
          value: '3'
        }, {
          text: '第四季度',
          value: '4'
        }
      ],
      url: {
        add: '/pfdj/registration/add',
        edit: '/pfdj/registration/edit',
        queryById: '/pfdj/registration/queryById'
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
    this.initYear()
  },
  methods: {
    initYear() {
      let yearn = (new Date()).getFullYear()
      let years = []
      for (let i = 0; i < 3; i++) {
        years.push({ text: yearn - i, value: yearn - i })
      }
      this.dictOption1 = years
    },
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