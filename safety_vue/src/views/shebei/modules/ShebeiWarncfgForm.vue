<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="主键uuid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="uid">-->
          <!--              <a-input v-model="model.uid" placeholder="请输入主键uuid"  ></a-input>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
              <JselectDqDepart v-model="model.sysOrgCode" @change="change"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbtype">
              <j-dict-select-tag type="list" v-model="model.sbtype" dictCode="sbtype" placeholder="请选择设备类型"
                                 @change="change1"/>
              <!--              <a-input v-model="model.phone" placeholder="请输入设备状态预警号码组"  ></a-input>-->
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeino">
              <j-search-select-tag placeholder="请选择设备名称" v-model="model.shebeino" :dictOptions="dictOption">
              </j-search-select-tag>
              <!--              <a-input v-model="model.shebeino" placeholder="请输入设备编号"  ></a-input>-->
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否报警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isCall">
              <j-dict-select-tag v-model="model.isCall" dictCode="is_call" placeholder="请选择是否报警"/>
              <!--              <a-input-number v-model="model.isCall" placeholder="请输入是否报警:0=报警,1=不报警" style="width: 100%" />-->
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备状态预警号码组" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="phone">
              <j-dict-select-tag type="list" v-model="model.phone" dictCode="bhz_phone,names,uid,phonesname='16'"
                                 dictTable="bhz_phone" placeholder="请选择设备状态预警号码组"/>
              <!--              <a-input v-model="model.phone" placeholder="请输入设备状态预警号码组"  ></a-input>-->
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
import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
import { usershebeiList } from '@api/api'
import Vue from 'vue'

export default {
  name: 'ShebeiWarncfgForm',
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
      sysOrgCode: null,
      sbtypes: 0,
      dictOption: [],
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
        add: '/shebeiWarncfg/shebeiWarncfg/add',
        edit: '/shebeiWarncfg/shebeiWarncfg/edit',
        queryById: '/shebeiWarncfg/shebeiWarncfg/queryById',
        list: '/sys/user/list5'
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
    this.shebeiList()
  },
  methods: {
    change(e) {
      this.sysOrgCode = e
      this.shebeiList()
    },
    change1(e) {
      this.sbtypes = e
      this.shebeiList()
    },
    shebeiList() {
      if (this.sysOrgCode === null) {
        this.sysOrgCode = Vue.ls.get('SYS_DEPART_ORGCODE')
      }
      usershebeiList({
        sys_depart_orgcode: this.sysOrgCode,
        sbtypes: this.sbtypes
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
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