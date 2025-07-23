<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="工程类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projType">
              <j-search-select-tag type="list" placeholder="请选择工程类型" v-model="model.projType"
                                   :dictOptions="dictOptions1"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="公司名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyName">
              <j-search-select-tag type="list" placeholder="请选择公司名称" v-model="model.companyName"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="准入证号" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               prop="accessCertificateNumber">
              <a-input-number v-model="model.accessCertificateNumber" placeholder="请输入准入证号" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="代理人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="agent">
              <a-input v-model="model.agent" placeholder="请输入代理人"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contactInformation">
              <a-input v-model="model.contactInformation" placeholder="请输入联系方式"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="合同摘要" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contractSummary">
              <a-input v-model="model.contractSummary" placeholder="请输入合同摘要"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="合同类别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contractCategory">
              <a-input v-model="model.contractCategory" placeholder="请输入合同类别"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="合同编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contractNumber">
              <a-input v-model="model.contractNumber" placeholder="请输入合同编号"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="签订日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="signingDate">
              <j-date placeholder="请选择签订日期" v-model="model.signingDate" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="招标类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="biddingType">
              <a-input v-model="model.biddingType" placeholder="请输入招标类型"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="合同金" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               prop="excludingTaxAmount">
              <a-input v-model="model.excludingTaxAmount" placeholder="请输入不含税合同价款金额(元)"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="增值税金" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="valueAddedTax">
              <a-input v-model="model.valueAddedTax" placeholder="请输入增值税金额(元)"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="履约保证金" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="paymentAmount">
              <a-input v-model="model.paymentAmount" placeholder="请输入履约保证缴纳金额(元)"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="履约进度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remarks">
              <j-search-select-tag type="list" placeholder="请选择履约进度" v-model="model.remarks"
                                   :dictOptions="dictOptions2"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
<!--          <a-col :span="12">-->
<!--            <a-form-model-item label="供应商编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supplier">-->
<!--              <a-input v-model="model.supplier" placeholder="请输入供应商编码"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-model-item label="guid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guid">-->
<!--              <a-input v-model="model.guid" placeholder="请输入guid"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="12">-->
<!--            <a-form-model-item label="firstParty" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="firstParty">-->
<!--              <a-input v-model="model.firstParty" placeholder="请输入firstParty"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="12">
            <a-form-model-item label="合同文件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fileurl">
              <j-image-upload v-model="fileurl" :isMultiple="isMultiple" ></j-image-upload>
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
  name: 'WzsubcontactMForm',
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
      dictOptions1: [{
        text: '临时工程',
        value: '1'
      }, {
        text: '主体工程',
        value: '2'
      }],
      dictOptions2:[{
        text:'正在履约',
        value:'1'
      },{
        text:'已退场，已封账',
        value:'2'
      },{
        text:'已退场，未封账',
        value:'3'
      }],
      isMultiple : true,
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: '/wzsubcontact/wzsubcontactM/add',
        edit: '/wzsubcontact/wzsubcontactM/edit',
        queryById: '/wzsubcontact/wzsubcontactM/queryById'
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