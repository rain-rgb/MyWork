<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <JSelectMyUserByDep v-decorator="['createBy']" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <j-date placeholder="请选择创建日期" v-decorator="['createTime']" :trigger-change="true" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="更新人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <JSelectMyUserByDep v-decorator="['updateBy']" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="更新日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <j-date placeholder="请选择更新日期" v-decorator="['updateTime']" :trigger-change="true" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="所属组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JselectDqDepart v-decorator="['sysOrgCode']" ::multi="false"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['sbtype']" :trigger-change="true" dictCode="sbtype" @change="changeOne"
                                 placeholder="请选择设备类型"/>
            </a-form-item>
          </a-col>
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['sbjno']" placeholder="请输入设备编号"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sbname']" placeholder="请输入设备名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备简称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sbjsimplename']" placeholder="请输入设备简称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sbjno']" placeholder="默认不写会自动生成编号" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备厂家" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['millname']" placeholder="设备厂家" ></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="24">
            <a-form-item label="计算方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['interfacetype']" :trigger-change="true"
                                 dictCode="interfacetype" placeholder="默认为内掺法"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="理论配比匹配模式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['dtubaud']" :trigger-change="true" dictCode="dtubaud"
                                 placeholder="默认为根据油石比范围判断"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="单位(kg/t)" :labelCol="labelCol" v-show="sbtypeshow" :wrapperCol="wrapperCol">
              <a-input v-decorator="['beiy1']" placeholder="请填写与该设备关联的单位(kg/t)"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备负责人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['responsperson']" placeholder="设备负责人" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="负责人联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['responsphone']" placeholder="负责人联系方式" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="projectid" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['projectid']" placeholder="填写项目id" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="sectionid" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sectionid']" placeholder="填写标段id" ></a-input>
            </a-form-item>
          </a-col>
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
import JSelectDepart from '@/components/jeecgbiz/JSelectDepart'//所有的用户的组织机构权限
import JselectDqDepart from '@/components/jeecgbiz/JselectDqDepart'//当前用户的组织机构权限（当前使用）
//import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
//import JSelectMyUserByDep from '@/components/jeecgbiz/JSelectMyUserByDep'
import JDictSelectTag from '@/components/dict/JDictSelectTag'

export default {
  name: 'ShebeiInfoForm',
  components: {
    JFormContainer,
    JDate,
    JSelectDepart,
    JDictSelectTag,
    JselectDqDepart,
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
      form: this.$form.createForm(this),
      model: {},
      sbtypeshow:false,
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
        add: '/sys/systems/api/shebeiadd',
        edit: '/sys/systems/api/shebeiedit',
        queryById: '/system/shebeiInfo/queryById'
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
  },
  methods: {
    changeOne(val){
      if (val==2){
        this.sbtypeshow=true
      }else {
        this.sbtypeshow=false
      }
    },
    add() {
      this.edit({})
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'sysOrgCode', 'sbtype', 'sbname', 'sbjsimplename', 'interfacetype', 'sbjno', 'dtubaud','beiy1','millname','responsperson','responsphone','projectid','sectionid'))
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
        }

      })
    },
    popupCallback(row) {
      this.form.setFieldsValue(pick(row, 'sysOrgCode', 'sbtype', 'sbname', 'sbjsimplename', 'interfacetype', 'sbjno', 'dtubaud','millname','responsperson','responsphone','projectid','sectionid'))
    },
  }
}
</script>