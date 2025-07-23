<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="配合比类型" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               :rules="[{ required: true, message: '样品类型不能为空' }]" prop="titCode">
              <sample-type v-model="model.titCode" placeholder="请选择配合比类型" :type="type"></sample-type>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品名称"  :rules="[{ required: true, message: '样品名称不能为空' }]"
                               v-show="model.titCode!=null&&model.titCode!='15'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleName">
              <a-input v-model="model.sampleName" placeholder="请输入样品名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品描述" :rules="[{ required: true, message: '请填写样品描述' }]"
                               v-show="model.titCode!=null&&model.titCode!='15'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleDescribe">
              <a-input v-model="model.sampleDescribe" placeholder="请输入样品描述"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样日期" :rules="[{ required: true, message: '请选择取样日期' }]"
                               v-show="model.titCode!=null&&model.titCode!='15'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleDate">
              <j-date v-model="model.sampleDate" placeholder="请选择取样日期" :show-time="true"
                      dateFormat="YYYY-MM-DD"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品数量"
                               v-show="model.titCode!=null&&model.titCode!='15'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleYangPinShuLiang">
              <a-input v-model="model.sampleYangPinShuLiang" placeholder="请输入样品数量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="生产批号"
                               v-show="model.titCode!=null&&model.titCode!='15'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleShengChanPiHao">
              <a-input v-model="model.sampleShengChanPiHao" placeholder="请输入生产批号"></a-input>
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

          <a-divider>分部分项</a-divider>
          <a-col :span="24">
            <a-form-model-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orgCode">
              <JselectDqDepart v-model="model.orgCode" ::multi="false"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分部分项" :labelCol="labelCol" :wrapperCol="wrapperCol" >
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
          v-show="model.titCode!=null&&model.titCode!='02'&&model.titCode!='07'&&model.titCode!='10'&&model.titCode!='14'&&model.titCode!='18'">
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
import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
import JselectDqProjName from '@/components/jeecgbiz/JselectDqProjName'
import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
import Vue from 'vue'
import { validateDuplicateValue } from '@/utils/util'

export default {
  name: 'SyDpsSySamplePHBForm',
  components: {
    SampleType,
    Report,
    JselectDqProjName,
    JSearchSelectTag,
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
      type: 2,
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
    this.model.orgCode=Vue.ls.get('SYS_DEPART_ORGCODE');
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
    getTiNames(val, ptiNo) {
      let params = {
        tino: val,
      }
      getAction('/sylxdps/syDpsJcTestitem/list', params).then((res) => {
        if (res.success) {
          let data = res.result.records
          let arr = data.map(item => {
            return item.tino
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