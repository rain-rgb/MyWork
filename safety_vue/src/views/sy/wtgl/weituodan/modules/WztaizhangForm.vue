<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="施工单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shigongdanwei">
              <a-textarea v-model="model.shigongdanwei" rows="1" placeholder="" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianglidanwei">
              <a-textarea v-model="model.jianglidanwei" rows="1" placeholder="" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianglidanwei">
              <a-textarea v-model="model.weituodanwei" rows="1" placeholder=""/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituoren">
              <a-input v-model="model.weituoren" placeholder="" rows="1" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituoriqi">
              <!--              <a-input v-model="model.weituoriqi" placeholder=""  ></a-input>-->
              <j-date
                placeholder="委托日期"
                v-model="model.weituoriqi"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleno">
              <a-textarea v-model="model.cailiaono_dictText" rows="1" placeholder="" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料厂家" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleno">
              <a-textarea v-model="model.gongyingshangdanweibianma_dictText" rows="1" placeholder="" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规格类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao">
              <a-input v-model="model.guigexinghao" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="批次" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pici">
              <a-input v-model="model.pici" disabled></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">
            <a-form-model-item label="出厂时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chuchangshijian">
              <j-date v-model="model.chuchangshijian" style="width: 100%"/>
            </a-form-model-item>
          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="进场时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jinchangshijian">
              <!--              <a-input v-model="model.jinchangshijian"   ></a-input>-->
              <j-date disabled v-model="model.jinchangshijian" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="使用部位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="usePart">
              <a-input v-model="model.usePart" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="净重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jingzhongt">
              <a-input v-model="model.jingzhongt" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangdidian">
              <a-textarea v-model="model.storagePlace" rows="1" placeholder="" disabled/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报告份数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baogaofenshu">
              <a-input-number v-model="model.baogaofenshu" placeholder="" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yangpinchulifangshi">
              <a-textarea v-model="model.yangpinchulifangshi" rows="1" placeholder=""/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测项目" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jiancexiangmu">
              <a-textarea v-model="model.jiancexiangmu" rows="1" placeholder=""/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beizhu">
              <!--              <a-input v-model="model.beizhu" placeholder="" rows="4"></a-input>-->
              <a-textarea v-model="model.beizhu" rows="2" placeholder=""/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fujian">
              <a-input v-model="model.fujian" placeholder=""></a-input>
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
import Vue from 'vue'

export default {
  name: 'WztaizhangForm',
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
        add: '/wztaizhang/wztaizhang/add',
        edit: '/qywtd/syDpsYyYuancaiquyangweituo/insertDelegate',
        queryById: '/wztaizhang/wztaizhang/queryById'
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
            httpurl += this.url.edit + `/${this.model.id}`
            method = 'post'
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