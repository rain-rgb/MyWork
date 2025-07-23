<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="单位工程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="danweiProj">
              <a-input v-model="model.danweiProj" placeholder="请输入单位工程"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="分部工程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fenbuProj">
              <a-input v-model="model.fenbuProj" placeholder="请输入分部工程"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="分项工程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fenxiangProj">
              <a-input v-model="model.fenxiangProj" placeholder="请输入分项工程"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="danwei">
              <a-input v-model="model.danwei" placeholder="请输入单位"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="总控数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zongkongNum">
              <a-input v-model="model.zongkongNum" placeholder="请输入总控数量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="日完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dayFinish">
              <a-input v-model="model.dayFinish" placeholder="请输入日完成量"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="月完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="monthFinish">
              <a-input v-model="model.monthFinish" placeholder="请输入月完成量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="年完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yearFinish">
              <a-input v-model="model.yearFinish" placeholder="请输入年完成量"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="开累" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="allFinish">
              <a-input v-model="model.allFinish" placeholder="请输入开累"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="剩余量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="residue">
              <a-input v-model="model.residue" placeholder="请输入剩余量"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="1号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day1">
              <a-input-number v-model="model.day1" placeholder="请输入1号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="2号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day2">
              <a-input-number v-model="model.day2" placeholder="请输入2号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="3号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day3">
              <a-input-number v-model="model.day3" placeholder="请输入3号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="4号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day4">
              <a-input-number v-model="model.day4" placeholder="请输入4号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="5号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day5">
              <a-input-number v-model="model.day5" placeholder="请输入5号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="6号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day6">
              <a-input-number v-model="model.day6" placeholder="请输入6号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="7号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day7">
              <a-input-number v-model="model.day7" placeholder="请输入7号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="8号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day8">
              <a-input-number v-model="model.day8" placeholder="请输入8号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="9号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day9">
              <a-input-number v-model="model.day9" placeholder="请输入9号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="10号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day10">
              <a-input-number v-model="model.day10" placeholder="请输入10号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="11号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day11">
              <a-input-number v-model="model.day11" placeholder="请输入11号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="12号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day12">
              <a-input-number v-model="model.day12" placeholder="请输入12号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="13号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day13">
              <a-input-number v-model="model.day13" placeholder="请输入13号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="14号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day14">
              <a-input-number v-model="model.day14" placeholder="请输入14号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="15号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day15">
              <a-input-number v-model="model.day15" placeholder="请输入15号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="16号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day16">
              <a-input-number v-model="model.day16" placeholder="请输入16号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="17号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day17">
              <a-input-number v-model="model.day17" placeholder="请输入17号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="18号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day18">
              <a-input-number v-model="model.day18" placeholder="请输入18号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="19号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day19">
              <a-input-number v-model="model.day19" placeholder="请输入19号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="20号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day20">
              <a-input-number v-model="model.day20" placeholder="请输入20号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="21号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day21">
              <a-input-number v-model="model.day21" placeholder="请输入21号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="22号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day22">
              <a-input-number v-model="model.day22" placeholder="请输入22号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="23号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day23">
              <a-input-number v-model="model.day23" placeholder="请输入23号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="24号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day24">
              <a-input-number v-model="model.day24" placeholder="请输入24号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="25号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day25">
              <a-input-number v-model="model.day25" placeholder="请输入25号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="26号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day26">
              <a-input-number v-model="model.day26" placeholder="请输入26号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="27号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day27">
              <a-input-number v-model="model.day27" placeholder="请输入27号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="28号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day28">
              <a-input-number v-model="model.day28" placeholder="请输入28号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="29号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day29">
              <a-input-number v-model="model.day29" placeholder="请输入29号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="30号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day30">
              <a-input-number v-model="model.day30" placeholder="请输入30号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="31号完成量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="day31">
              <a-input-number v-model="model.day31" placeholder="请输入31号完成量" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="上月年累" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lastmonthYearfinish">
              <a-input v-model="model.lastmonthYearfinish" placeholder="请输入上月年累"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="上月开累" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lastmonthKaifinish">
              <a-input v-model="model.lastmonthKaifinish" placeholder="请输入上月开累"></a-input>
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

export default {
  name: 'BhzhntRibaoForm',
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
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: '/bhzhntribao/bhzhntRibao/add',
        edit: '/bhzhntribao/bhzhntRibao/edit',
        queryById: '/bhzhntribao/bhzhntRibao/queryById'
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