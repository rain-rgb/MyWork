<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="公司名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyName">
              <j-search-select-tag type="list" placeholder="请选择公司名称" v-model="model.companyName"
                                   :dictOptions="dictOption2"></j-search-select-tag>
              {{ selectValue }}
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="准入证号" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               prop="accessCertificateNumber">
              <a-input-number v-model="model.accessCertificateNumber" placeholder="请输入准入证号" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="代理人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="agent">
              <a-input v-model="model.agent" placeholder="请输入代理人"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contactInformation">
              <a-input v-model="model.contactInformation" placeholder="请输入联系方式"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="合同摘要" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contractSummary">
              <a-input v-model="model.contractSummary" placeholder="请输入合同摘要"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="合同类别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contractCategory">
              <a-input v-model="model.contractCategory" placeholder="请输入合同类别"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <!--                    <a-col :span="12">-->
          <!--                      <a-form-model-item label="合同编号(唯一码)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contractNumber">-->
          <!--                        <a-input-number v-model="model.contractNumber" placeholder="请输入合同编号" style="width: 100%"/>-->
          <!--                      </a-form-model-item>-->
          <!--                    </a-col>-->
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
            <a-form-model-item label="履约保证金" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="paymentAmount">
              <a-input v-model="model.paymentAmount" placeholder="请输入履约保证缴纳金额(元)"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="增值税金" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="valueAddedTax">
              <a-input v-model="model.valueAddedTax" placeholder="请输入增值税金额(元)"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="履约进度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remarks">
              <j-search-select-tag type="list" placeholder="请选择履约进度" v-model="model.remarks"
                                   :dictOptions="dictOptions1"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="合同文件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fileurl">
              <!--                <a-upload style="margin-left: 20%" :action="uploadAction" :headers="headers" :default-file-list="defaultFileList" @change="handleChange1">-->
              <!--                  <a-button><a-icon type="upload"/>-->
              <!--                    请选择上传的合同文件-->
              <!--                  </a-button>-->
              <!--                </a-upload>-->
              <j-image-upload v-model="fileurl" :isMultiple="isMultiple"></j-image-upload>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
    <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="供应商合同台账子表" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="wzContactSTable.loading"
          :columns="wzContactSTable.columns"
          :dataSource="wzContactSTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true"
          @valueChange=""
          @confirm="">
        </j-editable-table>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'
import { validateDuplicateValue } from '@/utils/util'
import { FormTypes } from '@/utils/JEditableTableUtil'
import JFormContainer from '@/components/jeecg/JFormContainer'
import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
import { gongyingshangList } from '@api/api'

export default {
  name: 'WzcontactMForm',
  mixins: [JEditableTableMixin],
  components: {
    JFormContainer,
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
      selectValue: '',
      dictOption2: [],
      uploadAction: window._CONFIG['domianURL'] + '/sys/common/upload',
      headers: {},
      defaultFileList: [],
      isMultiple: true,
      fileList: [],
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
        text: '正在履约',
        value: '1'
      }, {
        text: '已退场，已封账',
        value: '2'
      }, {
        text: '已退场，未封账',
        value: '3'
      }],
      activeKey: 'wzcontacts',
      refKeys: ['wzcontacts',],
      // tableKeys: ['wzcontacts',],
      confirmLoading: false,
      validatorRules: {},
      mtype: '',
      wzContactSTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '库别',
            key: 'parentnode',
            width: '300px',
            type: FormTypes.select,
            options: [],
            placeholder: '请选择${title}',
            validateRules: [{ required: true, message: '请选择${title}' }]
          },
          {
            title: '类别',
            key: 'chandi',
            width: '300px',
            type: FormTypes.select,
            options: [],
            placeholder: '请选择${title}',
            validateRules: [{ required: true, message: '请选择${title}' }]
          },
          {
            title: '规格',
            key: '',
            width: '300px',
            type: FormTypes.select,
            options: [],
            placeholder: '请选择${title}',
            validateRules: [{ required: true, message: '请选择${title}' }]
          },
          {
            title: '数量',
            key: '',
            width: '300px',
            type: FormTypes.input,
            options: [],
            placeholder: '请选择${title}',
            validateRules: [{ required: true, message: '请选择${title}' }]
          },
        ]
      },
      url: {
        add: '/wzcontact/wzcontactM/add',
        edit: '/wzcontact/wzcontactM/edit',
        queryById: '/wzcontact/wzcontactM/queryById',
        wzcontacts: {
          list: ''
        },
        getKubie: '/wzcailiaonamedictall/wzcailiaonamedictAll/getKubie',
        getType: "/wzcailiaonamedictall/wzcailiaonamedictAll/getTypeNode",
        list1: '/sys/dictItem/cailiaonamelist'
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
    this.gongyingshangData()
    this.getLiebie()
    this.getKubie1()
  },
  methods: {
    cailiaoname: function () {//材料名称下拉选择
      let params = { itemValue: this.mtype }
      this.wzContactSTable.columns[1].options = []
      getAction(this.url.list1, params)
        .then(res => {
          if (res.success) {
            let data = res.result
            console.log(this.wzContactSTable.columns[1].options)
            for (let i = 0; i < data.length; i++) {
              this.wzContactSTable.columns[1].options.push({
                text: data[i].itemValue,
                title: data[i].itemValue,
                value: data[i].itemValue
              })
            }
          }
        })
    },
    add() {
      this.edit(this.modelDefault)
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.visible = true
    },
    handleChange1({ file, fileList }) {
      if (file.status !== 'uploading') {
        this.fileList = file.response.message
        this.model.url = this.fileList
        //console.log(this.model.filePath,"this.model.filePath")
      }
      console.log(this.fileList)
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
    gongyingshangData: function () {//公司名称选择
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      gongyingshangList({
        sys_depart_orgcode: sys_depart_orgcode,
        pageNo: 1,
        pageSize: 100
      }).then(res => {
        this.dictOption2 = []
        let result = res.result.records
        result.forEach(re => {
          this.dictOption2.push({ text: re.gongyingshangname, value: re.guid })
        })
      })
    },
    getKubie1: function () {//库别选择
      var params = {}
      getAction(this.url.getKubie, params).then(res => {
        this.dictOption1 = []
        let result = res.result
        result.forEach(re => {
          this.dictOption1.push({ text: re.parentNode, value: re.parentNode })
        })
        //console.log(res)
      })
    },
    getLiebie: function () {//类别选择
      var params = {
        parentnode: this.queryParam.parentnode
      }
      getAction(this.url.getType, params).then(res => {
        this.dictOption2 = []
        let result = res.result
        result.forEach(re => {
          this.dictOption2.push({ text: re.chandi, value: re.chandi })
        })
        //console.log(res)
      })
    },
  }
}
</script>