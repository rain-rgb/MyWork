<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JselectDqDepart v-decorator="['sysOrgCode']" ::multi="false"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['cailiaoname',validatorRules.cailiaoname]" placeholder="请输入材料名称"></a-input>
            </a-form-item>
          </a-col>
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="材料编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['cailiaono']" placeholder="请输入材料编号"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="parentnode" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['parentnode']" placeholder="请输入parentnode"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="材料类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['nodetype',validatorRules.nodetypeCL]"
                                 :trigger-change="true" dictCode="nodetypeCL" placeholder="请输入材料类型"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="规格类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['guigexinghao']" placeholder="请输入规格类型"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="材料计量单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['wzcailiaojiliangdanwei']"
                                 :trigger-change="true" dictCode="WZCaiLiaoJiLiangDanWei" placeholder="请输入材料计量单位"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="材料分类" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['lmcailiaolx']"
                                 :trigger-change="true" dictCode="lmcailiaolx" placeholder="请选择材料分类"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="是否使用电子锁" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['iselocks']"
                                 :trigger-change="true" dictCode="yn" placeholder="请选择是否使用电子锁"/>
            </a-form-item>
          </a-col>
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="称重" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['ischengzhong']" placeholder="请输入称重"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['miaoshu']" placeholder="请输入描述"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="偏差" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['isyunxupiancha']" placeholder="请输入偏差"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="正偏差" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['zhengpiancha']" placeholder="请输入正偏差"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="负偏差" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['fupiancha']" placeholder="请输入负偏差"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="pinpai" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['pinpai']" placeholder="请输入pinpai"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="chandi" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['chandi']" placeholder="请输入chandi"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="danjia" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['danjia']" placeholder="请输入danjia"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['createBy']" placeholder="请输入创建人"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <j-date placeholder="请输入创建日期" v-decorator="['createTime']" :trigger-change="true" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="材料字典的唯一id" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['guid']" placeholder="请输入材料字典的唯一id"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="时间戳" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input-number v-decorator="['ts']" placeholder="请输入时间戳" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="isdel" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input-number v-decorator="['isdel']" placeholder="请输入isdel" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="status" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input-number v-decorator="['status']" placeholder="请输入status" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="原材料上限" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input-number v-decorator="['shangxian']" placeholder="请输入原材料上限" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="更新人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['updateBy']" placeholder="请输入更新人"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="更新日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <j-date placeholder="请选择更新日期" v-decorator="['updateTime']" :trigger-change="true" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
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
import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
import Vue from 'vue'
import { DEPART_ID } from '@/store/mutation-types'

export default {
  name: 'WzcailiaonamedictForm',
  components: {
    JFormContainer,
    JDate,
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
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {
        cailiaoname: {
          rules: [
            { required: true, message: '请选择材料名称' },
          ]
        },
        nodetypeCL: {
          rules: [
            { required: true, message: '请选择材料类型' }
          ]
        },
      },
      url: {
        add: '/wzcailiaonamedict/wzcailiaonamedict/add',
        edit: '/wzcailiaonamedict/wzcailiaonamedict/edit',
        queryById: '/wzcailiaonamedict/wzcailiaonamedict/queryById'
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
    add() {
      this.edit({})
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'sysOrgCode', 'cailiaoname', 'cailiaono', 'parentnode', 'nodetype', 'guigexinghao', 'wzcailiaojiliangdanwei', 'ischengzhong', 'miaoshu', 'isyunxupiancha', 'zhengpiancha', 'fupiancha', 'wzcailiaodanweihuansuan', 'pinpai', 'chandi', 'danjia', 'createBy', 'createTime', 'guid', 'ts', 'isdel', 'status', 'lmcailiaolx', 'shangxian', 'updateBy', 'updateTime', 'iselocks'))
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
          console.log('表单提交数据1', values)
          formData['wzcailiaodanweihuansuan'] = Vue.ls.get(DEPART_ID)
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
      this.form.setFieldsValue(pick(row, 'sysOrgCode', 'cailiaoname', 'cailiaono', 'parentnode', 'nodetype', 'guigexinghao', 'wzcailiaojiliangdanwei', 'ischengzhong', 'miaoshu', 'isyunxupiancha', 'zhengpiancha', 'fupiancha', 'wzcailiaodanweihuansuan', 'pinpai', 'chandi', 'danjia', 'createBy', 'createTime', 'guid', 'ts', 'isdel', 'status', 'lmcailiaolx', 'shangxian', 'updateBy', 'updateTime', 'iselocks'))
    },
  }
}
</script>