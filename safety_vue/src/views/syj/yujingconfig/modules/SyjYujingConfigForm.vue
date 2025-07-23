<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
              <JselectDqDepart v-model="model.sysOrgCode" multi/>
            </a-form-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="设备类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbType">-->
<!--              <a-input v-model="model.sbType" placeholder="请选择设备类型"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeiNo">
              <j-search-select-tag v-model="model.shebeiNo" placeholder="请选择设备" :dictOptions="dictOption"/>
              {{ selectValue }}
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="预警联系人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="names">
              <j-dict-select-tag type="list" v-model="model.names" :trigger-change="true"
                                 dictCode="bhz_phone,names,uid,phonesname='28'" dictTable="bhz_phone"
                                 placeholder="请输入预警人"></j-dict-select-tag>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="预警号码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="phones">-->
<!--              <a-input v-model="model.phones" placeholder="请输入预警号码"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="是否报警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isCall">
              <j-search-select-tag type="list" v-model="model.isCall" placeholder="请选择是否报警" style="width: 100%"
                                   :dictOptions="dictOptions1"/>
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
import { usershebeiList } from '@api/api'
import JselectDqDepart from '@/components/jeecgbiz/JselectDqDepart'
import Vue from 'vue'

export default {
  name: 'SyjYujingConfigForm',
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
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      selectValue: '',
      dictOption: [],
      dictOptions1: [{
        text: '报警',
        value: '0'
      }, {
        text: '不报警',
        value: '1'
      }],
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: '/syjyujingconfig/syjYujingConfig/add',
        edit: '/syjyujingconfig/syjYujingConfig/edit',
        queryById: '/syjyujingconfig/syjYujingConfig/queryById'
      }
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    },
  },
  created() {
    this.shebeiList()
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
  },
  methods: {
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '3,4,12'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
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