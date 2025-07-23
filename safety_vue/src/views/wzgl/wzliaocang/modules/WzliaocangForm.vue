<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JselectDqDepart v-decorator="['sysOrgCode']" ::multi="false"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="料仓名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['name']" placeholder="请输入料仓名称"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="料仓状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['liaocangStatus']" :trigger-change="true"
                                 dictCode="liaocang_status" placeholder="请选择料仓状态"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="红外栅栏设备" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag placeholder="请选择关联红外栅栏设备"
                                   v-decorator="['infraredFence']"
                                   :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="材料类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['cailiaono']" :trigger-change="true" dictCode="nodeType"
                                 placeholder="请选择材料类型"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="规格类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['guigexinghao']" placeholder="请输入规格类型"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="批次" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['pici']" placeholder="请输入批次"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="料斗号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['cailiaoname',validatorRules.cailiaoname]" placeholder="请输入料斗号"></a-input>
            </a-form-item>
          </a-col>
          <!--          <a-col :span="12">-->
          <!--            <a-form-item label="唯一标识" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['guid']" placeholder="请输入唯一标识"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="12">-->
          <!--            <a-form-item label="isdel" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input-number v-decorator="['isdel']" placeholder="请输入isdel" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="12">-->
          <!--            <a-form-item label="时间戳" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input-number v-decorator="['ts']" placeholder="请输入时间戳" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="12">-->
          <!--            <a-form-item label="orgcode" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['orgcode']" placeholder="请输入orgcode"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="12">-->
          <!--            <a-form-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['cailiaono']" placeholder="请输入cailiaono"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->

        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="重量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['picizhong']" placeholder="请输入重量"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="计量单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['danwei']" :trigger-change="true" dictCode="danwei"
                                 placeholder="请选择计量单位"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="生产厂家" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['changjia']" placeholder="请输入生产厂家"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="试验编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['bgbianhao']" placeholder="请输入试验编号"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="进场日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择进场日期" v-decorator="['jinchangTime']" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="检验日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择检验日期" v-decorator="['jianyanTime']" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="当前库存" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['kucun']" placeholder="当前库存" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="料位设备编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['liaoweino']" placeholder="料位设备编码"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="料位传感器重量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['weight']" placeholder="料位传感器重量" :disabled="true"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="设计容量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['designCapacity']" placeholder="设计容量" ></a-input>
            </a-form-item>
          </a-col>
          <!--          <a-col :span="12">-->
          <!--            <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input v-decorator="['createBy']" placeholder="请输入创建人"  ></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="12">-->
          <!--            <a-form-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <j-date placeholder="请选择创建日期" v-decorator="['createTime']" :trigger-change="true" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="12">-->
          <!--            <a-form-item label="更新人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <j-date placeholder="请选择更新人" v-decorator="['updateBy']" :trigger-change="true" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--          <a-col :span="12">-->
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
import { usershebeiList } from '@api/api'

export default {
  name: 'WzliaocangForm',
  components: {
    JFormContainer,
    JDate,
    JselectDqDepart
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
      selectValue:'',
      dictOption: [],
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
          rules: [{
            required: true, message: '请输入料斗号'
          }]
        }
      },
      url: {
        add: '/wzliaocang/wzliaocang/add',
        edit: '/wzliaocang/wzliaocang/edit',
        queryById: '/wzliaocang/wzliaocang/queryById'
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
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '65'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        ////console.log(res)
      })
    },
    add() {
      this.edit({})
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model, 'kucun', 'name', 'sysOrgCode','infraredFence', 'guid', 'isdel', 'ts', 'orgcode', 'cailiaono', 'danwei', 'createBy', 'createTime', 'updateBy', 'updateTime', 'jianyanTime', 'liaocangStatus', 'guigexinghao', 'pici', 'picizhong', 'cailiaoname', 'changjia', 'jinchangTime', 'bgbianhao', 'liaoweino', 'weight'))
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
          formData['departid'] = Vue.ls.get(DEPART_ID)
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
      this.form.setFieldsValue(pick(row, 'kucun', 'name', 'sysOrgCode','infraredFence', 'guid', 'isdel', 'ts', 'orgcode', 'cailiaono', 'danwei', 'createBy', 'createTime', 'updateBy', 'updateTime', 'jianyanTime', 'liaocangStatus', 'guigexinghao', 'pici', 'picizhong', 'cailiaoname', 'changjia', 'jinchangTime', 'bgbianhao', 'liaoweino', 'weight'))
    },
  }
}
</script>