<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model :model="model" :rules="validatorRules" ref="form" slot="detail">
        <a-row>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="机械id" prop="machineid">-->
<!--              <a-input placeholder="请输入机械id" v-model="model.machineid"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="终端sn号" prop="terminalsncode">-->
<!--              <a-input placeholder="请输入终端sn号" v-model="model.terminalsncode"></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="机械宽度(m)" prop="machinesize">-->
<!--              <a-input-number placeholder="请输入机械宽度(m)" style="width: 100%" v-model="model.machinesize"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="机械名称" prop="machinename">
              <a-input placeholder="请输入机械名称" v-model="model.machinename"></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol"-->
<!--                               label="机械类型编码，0：振动碾，1：冲击碾，3：双钢轮，4：胶轮，5：沥青摊铺机，10：羊足碾，11：单钢轮，12：水稳摊铺机"-->
<!--                               prop="machinetypecode">-->
<!--              <a-input-number placeholder="请输入机械类型编码，0：振动碾，1：冲击碾，3：双钢轮，4：胶轮，5：沥青摊铺机，10：羊足碾，11：单钢轮，12：水稳摊铺机"-->
<!--                              style="width: 100%" v-model="model.machinetypecode"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="机械类型" prop="machinetypename">
              <a-input placeholder="请输入机械类型" v-model="model.machinetypename"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="是否报警" prop="iscall">
              <j-search-select-tag type="list" placeholder="请选择是否报警" v-model="model.iscall" :dictOptions="dictOptions1"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="报警号码" prop="phone">
              <j-dict-select-tag type="list" v-model="model.phone" :trigger-change="true"
                                 dictCode="bhz_phone,names,uid,phonesname='23'" dictTable="bhz_phone"
                                 placeholder="请选择报警号码组"/>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="安装开始使用时间" prop="tmstarttime">-->
<!--              <j-date placeholder="请选择安装开始使用时间" style="width: 100%" v-model="model.tmstarttime"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol"-->
<!--                               label="结束时间，3000-01-01 00:00:00表示未结束仍在使用，小于当前时间表示已结束" prop="tmendtime">-->
<!--              <j-date placeholder="请选择结束时间，3000-01-01 00:00:00表示未结束仍在使用，小于当前时间表示已结束" style="width: 100%"-->
<!--                      v-model="model.tmendtime"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="天线高度(m)" prop="tmwireheight">-->
<!--              <a-input-number placeholder="请输入天线高度(m)" style="width: 100%" v-model="model.tmwireheight"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="单双天线模式（0表示单天线，1表示双天线）"-->
<!--                               prop="tmantemode">-->
<!--              <a-input-number placeholder="请输入单双天线模式（0表示单天线，1表示双天线）" style="width: 100%" v-model="model.tmantemode"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="单天线AC" prop="tmwheelwidthsingle">-->
<!--              <a-input-number placeholder="请输入单天线AC" style="width: 100%" v-model="model.tmwheelwidthsingle"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="单天线BE长" prop="tmdistancefromwheel">-->
<!--              <a-input-number placeholder="请输入单天线BE长" style="width: 100%" v-model="model.tmdistancefromwheel"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="单天线DE长" prop="tmdistancefromcenter">-->
<!--              <a-input-number placeholder="请输入单天线DE长" style="width: 100%" v-model="model.tmdistancefromcenter"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="双天线CB长" prop="tmcentertomainx">-->
<!--              <a-input-number placeholder="请输入双天线CB长" style="width: 100%" v-model="model.tmcentertomainx"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="双天线GC长" prop="tmcentertomainy">-->
<!--              <a-input-number placeholder="请输入双天线GC长" style="width: 100%" v-model="model.tmcentertomainy"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="双天线FE长" prop="tmvicetomainx">-->
<!--              <a-input-number placeholder="请输入双天线FE长" style="width: 100%" v-model="model.tmvicetomainx"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="双天线GF长" prop="tmvicetomainy">-->
<!--              <a-input-number placeholder="请输入双天线GF长" style="width: 100%" v-model="model.tmvicetomainy"/>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="双天线AD长" prop="tmwheelwidthdouble">-->
<!--              <a-input-number placeholder="请输入双天线AD长" style="width: 100%" v-model="model.tmwheelwidthdouble"/>-->
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
  name: 'HcMachineForm',
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
        text: '报警',
        value: '0'
      }, {
        text: '不报警',
        value: '1'
      }],
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: '/hc_machine/hcMachine/add',
        edit: '/hc_machine/hcMachine/edit',
        queryById: '/hc_machine/hcMachine/queryById'
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
