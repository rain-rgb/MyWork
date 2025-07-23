<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="样品类型" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               :rules="[{ required: true, message: '样品类型不能为空' }]" prop="titCode">
              <sample-type v-model="model.titCode" @change="changeOne" placeholder="请选择样品类型" :type="type"></sample-type>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品名称" v-show="model.titCode!=null&&model.titCode!='05'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleName">
              <a-input v-model="model.sampleName" placeholder="请输入样品名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品描述"
                               v-show="model.titCode!=null&&model.titCode!='05'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleDescribe">
              <a-input v-model="model.sampleDescribe" placeholder="请输入样品描述"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样日期"
                               v-show="model.titCode!=null&&model.titCode!='05'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleDate">
              <j-date v-model="model.sampleDate" placeholder="请选择取样日期" :show-time="true"
                      dateFormat="YYYY-MM-DD"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="产地" v-show="model.titCode=='0513'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleChanDi">
              <a-input v-model="model.sampleChanDi" placeholder="请输入产地"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品数量"
                               v-show="model.titCode!=null&&model.titCode!='05'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleYangPinShuLiang">
              <a-input v-model="model.sampleYangPinShuLiang" placeholder="请输入样品数量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验组数"
                               v-show="model.titCode=='0501'||model.titCode=='0503'||model.titCode=='0505'||model.titCode=='0506'||model.titCode=='0511'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleShiYanZuShu">
              <a-input v-model="model.sampleShiYanZuShu" placeholder="请输入试验组数"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样地点"
                               v-show="model.titCode!=null&&model.titCode!='05'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleQuYangDiDian">
              <a-input v-model="model.sampleQuYangDiDian" placeholder="请输入取样地点"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="强度等级"
                               v-show="model.titCode!=null&&model.titCode!='05'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleQiangDuDengJi">
              <j-dict-select-tag type="list" v-model="model.sampleQiangDuDengJi" :trigger-change="true"
                                 dictCode="sy_dps_jc_rules,rulename,ruleno,titCode = '0501'"
                                 placeholder="请选择强度等级"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="搅拌方式" :labelCol="labelCol"
                               v-show="model.titCode=='0501'||model.titCode=='0503'||model.titCode=='0505'||model.titCode=='0506'||model.titCode=='0508'||model.titCode=='0511'"
                               :wrapperCol="wrapperCol" prop="sampleJiaoBanFangShi">
              <j-search-select-tag type="list" v-model="model.sampleJiaoBanFangShi" :trigger-change="true"
                                   :dictOptions="dictOptions" placeholder="请选择搅拌方式"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="养护条件" :labelCol="labelCol" v-show="model.titCode=='0508'"
                               :wrapperCol="wrapperCol" prop="sampleYangHuTiaoJian">
              <j-search-select-tag type="list" v-model="model.sampleYangHuTiaoJian" :trigger-change="true"
                                   :dictOptions="dictOptions5" placeholder="请选择养护条件"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="混凝土种类" :labelCol="labelCol"
                               v-show="model.titCode=='0501'||model.titCode=='0503'||model.titCode=='0505'||model.titCode=='0511'"
                               :wrapperCol="wrapperCol" prop="sampleHunNingTuZhongLei">
              <j-search-select-tag type="list" v-model="model.sampleHunNingTuZhongLei" :trigger-change="true"
                                   :dictOptions="dictOptions1" placeholder="请选择混凝土种类"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="砂浆种类" :labelCol="labelCol" v-show="model.titCode=='0506'||model.titCode=='0508'"
                               :wrapperCol="wrapperCol" prop="sampleShaJiangZhongLei">
              <j-search-select-tag type="list" v-model="model.sampleShaJiangZhongLei" :trigger-change="true"
                                   :dictOptions="dictOptions4" placeholder="请选择砂浆种类"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="龄期"
                               v-show="model.titCode!=null&&model.titCode!='05'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleLingQi">
              <j-search-select-tag type="list" v-model="model.sampleLingQi" :trigger-change="true"
                                   :dictOptions="dictOptions2" placeholder="请选择龄期"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样人"
                               v-show="model.titCode!=null&&model.titCode!='05'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleQuYangRen">
              <a-input v-model="model.sampleQuYangRen" placeholder="请输入取样人"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否留样"
                               v-show="model.titCode!=null&&model.titCode!='05'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shifouliuyang">
              <j-search-select-tag type="list" v-model="model.shifouliuyang" :trigger-change="true"
                                   :dictOptions="dictOptions3" placeholder="请选择是否留样"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测依据" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="JianCeYiJu">
              <j-multi-select-tag v-model="model.JianCeYiJu"
                                  dictCode="sy_dps_jc_rules,rulename,ruleno,ruleType = '试验依据'"
                                  placeholder="请选择检测依据"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="判定依据" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ShiYanYiJu">
              <j-multi-select-tag v-model="model.ShiYanYiJu"
                                  dictCode="sy_dps_jc_rules,rulename,ruleno,ruleType = '判定依据'"
                                  placeholder="请选择判定依据"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注"
                               v-show="model.titCode!=null&&model.titCode!='05'"
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
          v-show="model.titCode!=null&&model.titCode!='05'">
          <report :id="model.titCode" @change="getTiNames"></report>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'
import SampleType from '@/views/sy/dpssysample/modules/SampleType'
import Report from '@/views/sy/dpssysample/modules/Report'
import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
import JselectDqProjName from '@/components/jeecgbiz/JselectDqProjName'
import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
import Vue from 'vue'
import { validateDuplicateValue } from '@/utils/util'

export default {
  name: 'SyDpsSySampleTSYForm',
  components: {
    JSearchSelectTag,
    SampleType,
    Report,
    JselectDqProjName,
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
      type: 3,
      selectValue: '',
      asyncSelectValue: '',
      dictOptions: [{
        text: '机械搅拌',
        value: '机械搅拌'
      }, {
        text: '人工+机械搅拌',
        value: '人工+机械搅拌'
      }, {
        text: '人工搅拌',
        value: '人工搅拌'
      }],
      dictOptions1: [{
        text: '泵送混凝土',
        value: '泵送混凝土'
      }, {
        text: '碾压混凝土',
        value: '碾压混凝土'
      }, {
        text: '喷射混凝土',
        value: '喷射混凝土'
      }, {
        text: '普通混凝土',
        value: '普通混凝土'
      }, {
        text: '水下混凝土',
        value: '水下混凝土'
      }, {
        text: '抗渗混凝土',
        value: '抗渗混凝土'
      }],
      dictOptions2: [{
        text: '1',
        value: '1'
      }, {
        text: '3',
        value: '3'
      }, {
        text: '7',
        value: '7'
      }, {
        text: '10',
        value: '10'
      }, {
        text: '28',
        value: '28'
      }],
      dictOptions3: [{
        text: '不留',
        value: '0'
      }, {
        text: '留样',
        value: '1'
      }],
      dictOptions4: [{
        text: '水泥混合砂浆',
        value: '水泥混合砂浆'
      }, {
        text: '水泥砂浆',
        value: '水泥砂浆'
      }, {
        text: '预拌砌墙砂浆',
        value: '预拌砌墙砂浆'
      }],
      dictOptions5: [{
        text: '自然养护',
        value: '自然养护'
      }, {
        text: '标准养护',
        value: '标准养护'
      }],
      model: {
        sampleGcbw: ''
      },
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
    add() {
      this.model = {
        PanDingYiJu: '',
        RigidrReboundshebeiCJ: '',
        RigidrReboundshebeiNo: '',
        tiTableNum: '',
        ShiYanYiJu: '',
        departid: '',
        gangjindengji: '',
        gangjinzhijing: '',
        jianceyiju: '',
        liuyangchuli: '',
        liuyangqixian: '',
        liuyangriqi: '',
        pandingyiju: '',
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