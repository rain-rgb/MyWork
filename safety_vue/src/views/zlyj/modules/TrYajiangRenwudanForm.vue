<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-search-select-tag placeholder="请选择设备名称" v-decorator="['shebeibianhao', validatorRules.shebeibianhao]"
                                     :dictOptions="dictOption">
                </j-search-select-tag>
                {{ selectValue }}
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="请选择施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JSelectDqProjName v-decorator="['sgbwuuid']" ::multi="false"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="压浆日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择压浆日期" v-decorator="['yjdate']" :trigger-change="true" style="width: 100%"
                        dateFormat="YYYY-MM-DD"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="设计压力" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <!-- 添加验证规则 -->
                <a-input
                  v-decorator="['sjyl', {
                    rules: [
                      {
                        pattern: /^\d+(\.\d+)?$/,
                        message: '请输入有效的数字或小数'
                      }
                    ]
                  }]"
                  placeholder="请输入设计压力"
                ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="理论浆量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['lljl']" placeholder="请输入理论浆量"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="设计密度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['sjmd']" placeholder="请输入设计密度"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="梁型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag v-decorator="['lx']" placeholder="请选择梁型" dictCode="lx"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="孔道类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag v-decorator="['kdlx']" placeholder="请选择孔道类型"  dictCode="kdlx"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="孔道数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['kds']" placeholder="请输入孔道数"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
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
import JDate from '@/components/jeecg/JDate'
import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
import { usershebeiList } from '@api/api'
import Vue from 'vue'

export default {
  name: 'TrYajiangRenwudanForm',
  components: {
    JFormContainer,
    JDate,
    JSelectDqProjName
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {
      },
      required: false
    },
    //表单模式：true流程表单 false普通表单
    formBpm: {
      type: Boolean,
      default: false,
      required: false
    },
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
      dictOption: [],
      form: this.$form.createForm(this),
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      labelCol1: {
        xs: { span: 24 },
        sm: { span: 3 },
      },
      wrapperCol1: {
        xs: { span: 24 },
        sm: { span: 20 },
      },
      confirmLoading: false,
      validatorRules: {
        shebeibianhao: {
          rules: [{
            required: true, message: '请选择设备!'
          }]
        },
        // sgbwuuid:{
        //   rules: [{
        //     required: true, message: '请选择施工部位!'
        //   }]
        // }
      },
      url: {
        add: '/sys/sysDepartproject47/yjrenwudanadd',
        edit: '/sys/sysDepartproject47/yjrenwudanedit',
        queryById: '/yajiangrenwudan/trYajiangRenwudan/queryById',
        aaa: '/sys/sysDepartproject/queryByOrgcode'
      }
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
    }
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData()
    this.shebeiList()
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
        this.form.setFieldsValue(pick(this.model, 'uuid', 'projectname', 'girderplace', 'component', 'sgbwuuid', 'sgbwname', 'pedestal', 'status', 'yjdate', 'departid', 'orgcode', 'departname', 'shebeibianhao', 'sysOrgCode', 'createTime', 'updateTime', 'createBy','lx','kdlx','kds'))
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
          let params = { id: values.sgbwuuid }
          getAction(this.url.aaa, params).then(res1 => {
            if (res1.result == 1) {
              httpAction(httpurl, formData, method).then((res) => {
                if (res.success) {
                  that.$message.success(res.message)
                  that.$emit('ok')
                } else {
                  that.$message.warning(res.message)
                }
              }).finally(() => {
                that.confirmLoading = false
              })
            } else {
              that.$message.warning('请选择最底层部位！')
              that.confirmLoading = false
            }
          })
        }

      })
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '10'
      }).then(res => {
        //console.log(res)
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })

      })
    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row, 'uuid', 'projectname', 'girderplace', 'component', 'sgbwuuid', 'sgbwname', 'pedestal', 'status', 'yjdate', 'departid', 'orgcode', 'departname', 'shebeibianhao', 'sysOrgCode', 'createTime', 'updateTime', 'createBy','lx','kdlx','kds'))
    },
  }
}
</script>