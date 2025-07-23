<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="现场类型" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               :rules="[{ required: true, message: '样品类型不能为空' }]" prop="titCode">
              <sample-type v-model="model.titCode" placeholder="请选择现场类型" :type="type"></sample-type>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品名称"
                               v-show="model.titCode=='1110'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleName">
              <a-input v-model="model.sampleName" placeholder="请输入样品名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样日期" :rules="[{ required: true, message: '请选择取样日期' }]"
                               v-show="model.titCode!=null&&model.titCode!='11'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleDate">
              <j-date v-model="model.sampleDate" placeholder="请选择取样日期" :show-time="true"
                      dateFormat="YYYY-MM-DD"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="代表数量"
                               v-show="model.titCode=='1110'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleDaiBiaoShuLiang">
              <a-input v-model="model.sampleDaiBiaoShuLiang" placeholder="请输入代表数量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="生产批号"
                               v-show="model.titCode!=null&&model.titCode!='11'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleShengChanPiHao">
              <a-input v-model="model.sampleShengChanPiHao" placeholder="请输入生产批号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样人"
                               v-show="model.titCode=='1110'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleQuYangRen">
              <a-input v-model="model.sampleQuYangRen" placeholder="请输入取样人"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否留样"
                               v-show="model.titCode=='1110'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shifouliuyang">
              <j-search-select-tag type="list" v-model="model.shifouliuyang" :trigger-change="true"
                                   :dictOptions="dictOptions2" placeholder="请选择是否留样"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备厂家" v-show="model.titCode=='1105'||model.titCode=='1106'" :labelCol="labelCol"
                               :wrapperCol="wrapperCol"
                               prop="RigidrReboundshebeiCJ">
              <j-search-select-tag type="list" v-model="model.RigidrReboundshebeiCJ" :trigger-change="true"
                                   :dictOptions="dictOptions" placeholder="请选择设备厂家"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="钢保回弹设备编号" v-show="model.titCode=='1105'||model.titCode=='1106'"
                               :labelCol="labelCol"
                               :wrapperCol="wrapperCol"
                               prop="RigidrReboundshebeiNo">
              <j-search-select-tag type="list" v-model="model.RigidrReboundshebeiNo" :trigger-change="true"
                                   :dictOptions="dictOptions1" placeholder="请选择钢保回弹设备"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测依据" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ShiYanYiJu">
              <j-multi-select-tag v-model="model.ShiYanYiJu"
                                  dictCode="sy_dps_jc_rules,rulename,ruleno,ruleType = '试验依据'"
                                  placeholder="请选择检测依据"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="判定依据" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="PanDingYiJu">
              <j-multi-select-tag v-model="model.PanDingYiJu"
                                  dictCode="sy_dps_jc_rules,rulename,ruleno,ruleType = '判定依据'"
                                  placeholder="请选择判定依据"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注"
                               v-show="model.titCode=='1110'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleRemark">
              <a-input v-model="model.sampleRemark" placeholder="请输入备注"></a-input>
            </a-form-model-item>
          </a-col>


          <a-divider>分部分项</a-divider>
          <a-col :span="24">
            <a-form-model-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orgCode">
              <JselectDqDepart v-model="model.orgCode" ::multi="false"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分部分项" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JselectDqProjName @change="changeOne" ::multi="false" placeholder="请选择"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="使用部位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleGcbw">
              <a-input v-model="model.sampleGcbw" placeholder="请输入使用部位"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="任务编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag type="list" :trigger-change="true" placeholder="选择编号"/>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row
          v-show="model.titCode!=null&&model.titCode!='11'">
          <report :id="model.titCode" @change="getTiNames"></report>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'
import Report from '@/views/sy/dpssysample/modules/Report'
import SampleType from '@/views/sy/dpssysample/modules/SampleType'
import JselectDqProjName from '@/components/jeecgbiz/JselectDqProjName'
import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
import Vue from 'vue'
import { validateDuplicateValue } from '@/utils/util'

export default {
  name: 'SyDpsSySampleXCForm',
  components: {
    SampleType,
    JselectDqProjName,
    JSearchSelectTag,
    Report,
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
      type: 1,
      model: {
        sampleGcbw: ''
      },
      selectValue: '',
      asyncSelectValue: '',
      dictOptions: [{
        text: '',
        value: ''
      }],
      dictOptions1: [{
        text: '',
        value: ''
      }],
      dictOptions2: [{
        text: '否',
        value: '0'
      }, {
        text: '是',
        value: '1'
      }],
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
        add: '/sydpssysample/syDpsSySample/saveOrUpdate',
        edit: '/sydpssysample/syDpsSySample/edit',
        queryById: '/sydpssysample/syDpsSySample/queryById'
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
    this.model.orgCode = Vue.ls.get('SYS_DEPART_ORGCODE')
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
  },
  methods: {
    add() {
      this.model = {
        PanDingYiJu: '',
        ShiYanYiJu: '',
        RigidrReboundshebeiCJ: '',
        RigidrReboundshebeiNo: '',
        tiTableNum: '',
        departid: '',
        gangjindengji: '',
        gangjinzhijing: '',
        liuyangchuli: '',
        liuyangqixian: '',
        liuyangriqi: '',
        project: '',
        projectId: '',
        projectName: '',
        ptiNo: '',
        sampleShiYanZuShu: '',
        sampleYangHuTiaoJian: '',
        sampleWaiJiaJiChanLiang: '',
        sampleQuYangRen: '',
        reportNoNew: '',
        sampleChanDi: '',
        sampleChanDi2: '',
        sampleChanDi3: '',
        sampleChangDu: '',
        sampleChuChangBianHao: '',
        sampleDaiBiaoShuLiang: '',
        sampleDaiBiaoShuLiang2: '',
        sampleDaiBiaoShuLiang3: '',
        sampleDaiBiaoShuLiang4: '',
        sampleDaiBiaoShuLiang5: '',
        sampleDate: '',
        sampleDate2: '',
        sampleDate3: '',
        sampleGangJinZhiJing: '',
        sampleGangJinZhongLei: '',
        sampleGcbw: '',
        sampleGongChengBiHou: '',
        sampleGuiGeXingHao: '',
        sampleGuiGeXingHao2: '',
        sampleGuiGeXingHao3: '',
        sampleGuiGeXingHao4: '',
        sampleGuiGeXingHao5: '',
        sampleHunNingTuZhongLei: '',
        sampleJiPeiLeiXing: '',
        sampleJiaoBanFangShi: '',
        sampleJieHeLiaoZhongLei: '',
        sampleLiQingBiaoHao: '',
        sampleLiQingHunHeLiaoLeiXing: '',
        sampleLiQingZhongLei: '',
        sampleLingQi: '',
        sampleLiuYangShuLiang: '',
        sampleNoNew: '',
        samplePinZhongDengJi: '',
        sampleQiangDuDengJi: '',
        sampleQuYangDiDian: '',
        sampleQuYangDiDian2: '',
        sampleQuYangDiDian3: '',
        sampleRemark: '',
        sampleShaJiangZhongLei: '',
        sampleSheJiJiLiang: '',
        sampleShengChanChangJia: '',
        sampleShengChanPiHao: '',
        sampleShengChanRIQi: '',
        sampleShiYanCengWei: '',
        tiNos: ''
      },
        this.visible = true
    },
    changeOne(val) {
      let params = {
        orgCode: val,
      }
      getAction('/sydpssysample/syDpsSySample/getProjNames', params).then((res) => {
        if (res.success) {
          this.model.sampleGcbw = res.result
        }
      })
    },
    getTiNames(val,ptiNo) {
      let params = {
        tiNo: val,
      }
      getAction('/sylxdps/syDpsJcTestitem/list', params).then((res) => {
        if (res.success) {
          let data = res.result.records
          let arr = data.map(item => {
            return item.tiNo
          })
          this.model.tiNos = arr.join()
          this.model.ptiNo = ptiNo
        }
      })
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