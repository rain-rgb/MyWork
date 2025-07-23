<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="departid">-->
          <!--              <a-input v-model="model.departid" placeholder="请输入组织机构"  ></a-input>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbjno" required>
                <j-search-select-tag placeholder="请选择设备名称" v-model="model.sbjno" :dictOptions="dictOption"
                                     @change="handleAsyncChange">
                </j-search-select-tag>
                <!--              <a-input v-model="model.sbjno" placeholder="请输入设备编号"  ></a-input>-->
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
<!--              <a-form-model-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="poureLocation"-->
<!--                                 required>-->
<!--                <j-search-select-tag placeholder="请选择设备名称" v-model="model.poureLocation" :dictOptions="dictOptions">-->
<!--                </j-search-select-tag>-->
<!--                &lt;!&ndash;              <a-input v-model="model.poureLocation" placeholder="请输入施工部位"  ></a-input>&ndash;&gt;-->
<!--              </a-form-model-item>-->
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="材料类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaomingcheng"
                                 required>
                <!--              <a-input v-model="model.cailiaomingcheng" placeholder="请输入材料名称"  ></a-input>-->
                <j-dict-select-tag placeholder="请选择材料类型" v-model="model.cailiaomingcheng" type="list"
                                   :trigger-change="true" dictCode="material_type">
                </j-dict-select-tag>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="级配标准" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jipeibiaozhun"
                                 required>
                <j-search-select-tag placeholder="请选择级配标准" v-model="model.jipeibiaozhun" :dictOptions="dictOption2">
                </j-search-select-tag>
                <!--              <a-input v-model="model.jipeibiaozhun" placeholder="请输入级配标准"  ></a-input>-->
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="筛孔53" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shaikong53">
                <a-input v-model="model.shaikong53" placeholder="请输入筛孔53"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="筛孔37.5" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shaikong375">
                <a-input v-model="model.shaikong375" placeholder="请输入筛孔37.5"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="筛孔31.5" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shaikong315">
                <a-input v-model="model.shaikong315" placeholder="请输入筛孔31.5"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="筛孔26.5" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shaikong265">
                <a-input v-model="model.shaikong265" placeholder="请输入筛孔26.5"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="筛孔19" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shaikong19">
                <a-input v-model="model.shaikong19" placeholder="请输入筛孔19"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="筛孔16" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shaikong16">
                <a-input v-model="model.shaikong16" placeholder="请输入筛孔16"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="筛孔13.2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shaikong132">
                <a-input v-model="model.shaikong132" placeholder="请输入筛孔13.2"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="筛孔9.5" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shaikong95">
                <a-input v-model="model.shaikong95" placeholder="请输入筛孔9.5"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="筛孔4.75" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shaikong475">
                <a-input v-model="model.shaikong475" placeholder="请输入筛孔4.75"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="筛孔2.36" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shaikong236">
                <a-input v-model="model.shaikong236" placeholder="请输入筛孔2.36"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="筛孔1.18" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shaikong118">
                <a-input v-model="model.shaikong118" placeholder="请输入筛孔1.18"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="筛孔0.6" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shaikong06">
                <a-input v-model="model.shaikong06" placeholder="请输入筛孔0.6"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="筛孔0.3" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shaikong03">
                <a-input v-model="model.shaikong03" placeholder="请输入筛孔0.3"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="筛孔0.15" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shaikong015">
                <a-input v-model="model.shaikong015" placeholder="请输入筛孔0.15"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="筛孔0.075" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shaikong0075">
                <a-input v-model="model.shaikong0075" placeholder="请输入筛孔0.075"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="是否使用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isuse" required>
                <j-dict-select-tag placeholder="是否使用" v-model="model.isuse" type="list" :trigger-change="true"
                                   dictCode="isuse">
                </j-dict-select-tag>
                <!--              <a-input-number v-model="model.isuse" placeholder="请输入是否使用" style="width: 100%"/>-->
              </a-form-model-item>
            </a-col>
          </a-row>

        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'
import Vue from 'vue'
import { validateDuplicateValue } from '@/utils/util'
import { duplicateCheck6, usershebeiList } from '@api/api'

export default {
  name: 'BhzLqShaifenShiyanForm',
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
      selectedAsyncValue: '',
      selectValues: '',
      dictOptions: [],
      selectValue: '',
      dictOption: [],
      dictOption1: [],
      selectValue1: '',
      dictOption2: [],
      selectValue2: '',
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
        sbjno: [{
          required: true, message: '请选择设备!'
        }],
        cailiaomingcheng: [{
          required: true, message: '请选择材料类型!'
        }],
        jipeibiaozhun: [{
          required: true, message: '请选择级配标准!'
        }],
        isuse: [{
          validator: this.validateisUse
        }]
      },
      url: {
        add: '/bhzlqshaifenshiyan/bhzLqShaifenShiyan/add',
        edit: '/bhzlqshaifenshiyan/bhzLqShaifenShiyan/edit',
        queryById: '/bhzlqshaifenshiyan/bhzLqShaifenShiyan/queryById',
        list: '/lqbhz/bhzLqBases/sgbwlist',
        list2: '/bhzlqjipeifanwei/bhzLqJipeiFanwei/list1'
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
    this.getjipei()
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
  },
  methods: {
    handleAsyncChange(e) {
      this.selectValue = e
      // console.log('e', e)
      this.getsgbw()
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '1'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    getsgbw: function () {
      this.dictOptions = []
      // console.log("this.selectValue",this.selectValue)
      let params = {
        shebeibianhao: this.selectValue
      }
      getAction(this.url.list, params).then(res => {
        // console.log("施工部位res",res)
        let result = res.result
        result.forEach(re => {
          if (re !== null) {
            this.dictOptions.push({ text: re.poureLocation, value: re.poureLocation })
          }
        })
      })
    },
    getjipei: function () {
      this.dictOption2 = []
      getAction(this.url.list2, {}).then(res => {
        let result = res.result
        result.forEach(re => {
          if (re !== null) {
            this.dictOption2.push({ text: re.poureLocation, value: re.uuid })
          }
        })
      })
    },
    add() {
      this.edit(this.modelDefault)
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.selectValue = this.model.sbjno
      this.getsgbw()
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
    validateisUse(rule, value, callback) {
      let params = {
        tableName: 'bhz_lq_shaifen_shiyan',
        fieldName: 'isUse',
        fieldVal: value,
        dataId:this.model.id,
        dataId1: this.model.isuse,
        dataId2:this.model.sbjno,
        dataId3:this.model.cailiaomingcheng,
        dataId4:this.model.jipeibiaozhun
      }
      duplicateCheck6(params).then((res) => {
        if (res.success) {
          callback()
        } else {
          callback("该筛分实验已配置！")
        }
      })
    }
  }
}
</script>