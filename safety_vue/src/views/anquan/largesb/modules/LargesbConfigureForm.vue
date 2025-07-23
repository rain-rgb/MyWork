<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="设备类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sblx">-->
<!--              <j-dict-select-tag type="list" v-model="model.sblx" :dictOptions="dictOptions1"-->
<!--                                 placeholder="请选择设备类型"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
              <JselectDqDepart v-model="model.sysOrgCode" multi/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeiNo">
              <j-search-select-tag placeholder="请选择设备" v-model="model.shebeiNo" :dictOptions="dictOption" />
              {{ selectValue }}
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="预警联系人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="names">
              <j-dict-select-tag type="list" v-model="model.names" :trigger-change="true"
                                 dictCode="bhz_phone,names,uid,phonesname='24'" dictTable="bhz_phone"
                                 placeholder="请选择预警联系人"/>
            </a-form-model-item>
          </a-col>
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="预警号码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="phones">-->
          <!--              <a-input v-model="model.phones" placeholder="请选择预警号码"  ></a-input>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="是否报警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yesornot">
              <j-search-select-tag type="list" placeholder="请选择是否报警" v-model="model.yesornot" :dictOptions="dictOptions2" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@api/manage'
import { validateDuplicateValue } from '@/utils/util'
import JselectDqDepart from '@/components/jeecgbiz/JselectDqDepart'
import { usershebeiList } from '@api/api'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import Vue from 'vue'

export default {
  name: 'LargesbConfigureForm',
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
      dictOptions2:[{
        text: '报警',
        value: '0'
      }, {
        text: '不报警',
        value: '1'
      }],
      // dictOptions1: [{
      //   text: '架桥机',
      //   value: '23'
      // }, {
      //   text: '龙门吊',
      //   value: '21'
      // },{
      //   text: '塔吊',
      //   value: '51'
      // }, {
      //   text: '挂篮',
      //   value: '22'
      // }],
      dictOption:[],
      selectValue:'',
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: '/largesbconfigure/largesbConfigure/add',
        edit: '/largesbconfigure/largesbConfigure/edit',
        queryById: '/largesbconfigure/largesbConfigure/queryById'
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
        sbtypes: '21,22,23,51'
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