<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="库别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="kubie">
              <j-search-select-tag placeholder="请选择库别" v-model="model.kubie" :dictOptions="dictOption1"
                                   @change="getLiebie">
              </j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="类别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="leibie">
              <j-search-select-tag placeholder="请选择类别" v-model="model.leibie" :dictOptions="dictOption2"
                                   @change="getCaiLiaoName">
              </j-search-select-tag>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="物资名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wzName">
              <j-search-select-tag placeholder="请选择物资名称" v-model="model.wzName" :dictOptions="dictOption3"
                                   @change="getXinghao">
              </j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="型号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xinghao">
              <j-search-select-tag placeholder="请选择型号" v-model="model.xinghao" :dictOptions="dictOption4"
                                   @change="getGuige"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guige">
              <j-search-select-tag placeholder="请选择规格" v-model="model.guige" :dictOptions="dictOption5"
              ></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="辅助规格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fuzhuguige">
              <j-search-select-tag v-model="model.fuzhuguige" placeholder="请选择辅助规格"
                                   :dictOptions="dictOption6"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="danwei">
              <a-input placeholder="请输入单位(不填默认为 吨 )" v-model="model.danwei"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="本期盘点数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qizhangPdnum">
              <a-input v-model="model.qizhangPdnum" placeholder="请输入本期盘点数量"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="存料地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cunliaodi">
              <a-input v-model="model.cunliaodi" placeholder="请输入存料地点"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="情况说明" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="situation">
              <a-input v-model="model.situation" placeholder="请输入情况说明"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="账面单价" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhangmianPrice">
              <a-input v-model="model.zhangmianPrice" placeholder="请输入账面单价"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="账面金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhangmianJine">
              <a-input v-model="model.zhangmianJine" placeholder="请输入账面金额"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beizhu">
              <a-input v-model="model.beizhu" placeholder="请输入备注"></a-input>
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
  name: 'WzinventoryTaizhangForm',
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
      dictOption1: [],
      dictOption2: [],
      dictOption3: [],
      dictOption4: [],
      dictOption5: [],
      dictOption6: [],
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
        add: '/inventory/wzinventoryTaizhang/add',
        edit: '/inventory/wzinventoryTaizhang/edit',
        queryById: '/inventory/wzinventoryTaizhang/queryById',
        getKubie: '/wzcailiaonamedictall/wzcailiaonamedictAll/getKubie',
        getType: '/wzcailiaonamedictall/wzcailiaonamedictAll/getTypeNode',
        getCaiLiaoNameList: '/wzcailiaonamedictall/wzcailiaonamedictAll/getCaiLiaoNameList',
        getGuigeList: '/wzcailiaonamedictall/wzcailiaonamedictAll/getGuigeList',
        getXinghaoList: '/wzcailiaonamedictall/wzcailiaonamedictAll/getXinghaoList',
        getFuzhuGuigeList: '/wzcailiaonamedictall/wzcailiaonamedictAll/getFuzhuGuigeList',
      }
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    },
  },
  created() {
    this.getKubie1()
    this.getLiebie()
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
    getKubie1: function () {//库别
      var params = {}
      getAction(this.url.getKubie, params).then(res => {
        this.dictOption1 = []
        let result = res.result
        result.forEach(re => {
          this.dictOption1.push({ text: re.parentNode, value: re.parentNode })
        })
      })
    },
    getLiebie: function () {//类别
      var params = {
        parentnode: this.model.kubie
      }
      getAction(this.url.getType, params).then(res => {
        this.dictOption2 = []
        let result = res.result
        result.forEach(re => {
          this.dictOption2.push({ text: re.chandi, value: re.chandi })
        })
      })
    },
    getCaiLiaoName: function () {//物资名称
      var params = {
        parentnode: this.model.kubie,
        chandi: this.model.leibie
      }
      getAction(this.url.getCaiLiaoNameList, params).then(res => {
        this.dictOption3 = []
        let result = res.result
        result.forEach(re => {
          this.dictOption3.push({ text: re.cailiaoName, value: re.cailiaoName })
        })
      })
    },
    getXinghao: function () {//型号
      var params = {
        parentnode: this.model.kubie,
        chandi: this.model.leibie,
        cailiaoname: this.model.wzName,
        guigexinghao: this.model.guige
      }
      getAction(this.url.getXinghaoList, params).then(res => {
        this.dictOption4 = []
        let result = res.result
        result.forEach(re => {
          this.dictOption4.push({ text: re.guigexinghao, value: re.guigexinghao })
        })
      })
    },
    getGuige: function () {
      var params = {
        parentnode: this.model.kubie,
        chandi: this.model.leibie,
        cailiaoname: this.model.wzName
      }
      getAction(this.url.getGuigeList, params).then(res => {//规格
        this.dictOption5 = []
        let result = res.result
        result.forEach(re => {
          this.dictOption5.push({ text: re.zhengpiancha, value: re.zhengpiancha })
        })
      }),
        getAction(this.url.getFuzhuGuigeList, params).then(res => {//辅助规格
          this.dictOption6 = []
          let result = res.result
          result.forEach(re => {
            this.dictOption6.push({ text: re.miaoshu, value: re.miaoshu })
          })
        })
    },
  }
}
</script>