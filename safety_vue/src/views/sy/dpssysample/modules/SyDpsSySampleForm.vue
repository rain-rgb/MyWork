<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="样品类型" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               :rules="[{ required: true, message: '样品类型不能为空' }]" prop="titCode">
              <!--              <j-dict-select-tag type="list" v-model="model.titCode" :trigger-change="true"-->
              <!--                                 placeholder="请选择样品类型"/>-->
              <sample-type v-model="model.titCode" placeholder="请选择样品类型" :type="type"></sample-type>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品名称" :rules="[{ required: true, message: '样品名称不能为空' }]"
                               v-show="model.titCode!=null&&model.titCode!='02'&&model.titCode!='07'&&model.titCode!='10'&&model.titCode!='14'&&model.titCode!='18'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleName">
              <a-input v-model="model.sampleName" placeholder="请输入样品名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品描述" :rules="[{ required: true, message: '请填写样品描述' }]"
                               v-show="model.titCode!=null&&model.titCode!='02'&&model.titCode!='07'&&model.titCode!='10'&&model.titCode!='14'&&model.titCode!='18'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleDescribe">
              <a-input v-model="model.sampleDescribe" placeholder="请输入样品描述"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样日期" :rules="[{ required: true, message: '请选择取样日期' }]"
                               v-show="model.titCode!=null&&model.titCode!='02'&&model.titCode!='07'&&model.titCode!='10'&&model.titCode!='14'&&model.titCode!='18'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleDate">
              <j-date v-model="model.sampleDate" placeholder="请选择取样日期" :show-time="true"
                      dateFormat="YYYY-MM-DD"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品数量"
                               v-show="model.titCode!=null&&model.titCode!='02'&&model.titCode!='07'&&model.titCode!='10'&&model.titCode!='14'&&model.titCode!='18'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleYangPinShuLiang">
              <a-input v-model="model.sampleYangPinShuLiang" placeholder="请输入样品数量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="代表数量"
                               v-show="model.titCode!=null&&model.titCode!='02'&&model.titCode!='07'&&model.titCode!='10'&&model.titCode!='14'&&model.titCode!='18'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleDaiBiaoShuLiang">
              <a-input v-model="model.sampleDaiBiaoShuLiang" placeholder="请输入代表数量"></a-input>
            </a-form-model-item>
          </a-col>
          <!-- ***********选择显示部分----start************** -->
          <a-col :span="24">
            <a-form-model-item label="规格型号"
                               v-show="model.titCode=='03'||model.titCode=='0201'||model.titCode=='0202'||model.titCode=='0203'||model.titCode=='0206'||model.titCode=='0207'||model.titCode=='0208'||model.titCode=='0209'||model.titCode=='0701'||model.titCode=='1801'||model.titCode=='1802'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleGuiGeXingHao">
              <a-input v-model="model.sampleGuiGeXingHao" placeholder="请输入规格型号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品数量" v-show="model.titCode=='0201'||model.titCode=='0207'||model.titCode=='0208'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleYangPinShuLiang2">
              <a-input v-model="model.sampleYangPinShuLiang2" placeholder="请输入样品数量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="代表数量" v-show="model.titCode=='0201'||model.titCode=='0207'||model.titCode=='0208'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleDaiBiaoShuLiang2">
              <a-input v-model="model.sampleDaiBiaoShuLiang2" placeholder="请输入代表数量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规格型号" v-show="model.titCode=='0201'||model.titCode=='0207'||model.titCode=='0208'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleGuiGeXingHao2">
              <a-input v-model="model.sampleGuiGeXingHao2" placeholder="请输入规格型号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品数量" v-show="model.titCode=='0201'||model.titCode=='0207'" :labelCol="labelCol"
                               :wrapperCol="wrapperCol" prop="sampleYangPinShuLiang3">
              <a-input v-model="model.sampleYangPinShuLiang3" placeholder="请输入样品数量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="代表数量" v-show="model.titCode=='0201'||model.titCode=='0207'" :labelCol="labelCol"
                               :wrapperCol="wrapperCol" prop="sampleDaiBiaoShuLiang3">
              <a-input v-model="model.sampleDaiBiaoShuLiang3" placeholder="请输入代表数量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规格型号" v-show="model.titCode=='0201'||model.titCode=='0207'" :labelCol="labelCol"
                               :wrapperCol="wrapperCol" prop="sampleGuiGeXingHao3">
              <a-input v-model="model.sampleGuiGeXingHao3" placeholder="请输入规格型号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样地点"
                               v-show="model.titCode=='01'||model.titCode=='04'||model.titCode=='0201'||model.titCode=='0202'||model.titCode=='0203'||model.titCode=='0206'||model.titCode=='0207'||model.titCode=='0208'||model.titCode=='0209'||model.titCode=='0701'||model.titCode=='1001'||model.titCode=='1002'||model.titCode=='1003'||model.titCode=='1004'||model.titCode=='1005'||model.titCode=='1402'||model.titCode=='1801'||model.titCode=='1802'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleQuYangDiDian">
              <a-input v-model="model.sampleQuYangDiDian" placeholder="请输入取样地点"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="产地"
                               v-show="model.titCode=='03'||model.titCode=='0201'||model.titCode=='0202'||model.titCode=='0203'||model.titCode=='0206'||model.titCode=='0207'||model.titCode=='0208'||model.titCode=='0209'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleChanDi">
              <a-input v-model="model.sampleChanDi" placeholder="请输入产地"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="质检日期" v-show="model.titCode=='04'" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               prop="sampleZhiJianRiQi">
              <j-date v-model="model.sampleZhiJianRiQi" placeholder="请选择质检日期" :show-time="true"
                      dateFormat="YYYY-MM-DD"></j-date>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验组数"
                               v-show="model.titCode=='04'||model.titCode=='1001'||model.titCode=='1002'||model.titCode=='1003'||model.titCode=='1004'||model.titCode=='1005'||model.titCode=='1402'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleShiYanZuShu">
              <a-input v-model="model.sampleShiYanZuShu" placeholder="请输入试验组数"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="生产厂家"
                               v-show="model.titCode=='04'||model.titCode=='0701'||model.titCode=='1001'||model.titCode=='1002'||model.titCode=='1003'||model.titCode=='1004'||model.titCode=='1005'||model.titCode=='1402'||model.titCode=='1801'||model.titCode=='1802'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleShengChanChangJia">
              <a-input v-model="model.sampleShengChanChangJia" placeholder="请输入生产厂家"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="出厂日期" v-show="model.titCode=='04'" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               prop="sampleShengChanRiQi">
              <j-date v-model="model.sampleShengChanRiQi" placeholder="请选择出厂日期" :show-time="true"
                      dateFormat="YYYY-MM-DD"></j-date>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="出厂编号" v-show="model.titCode=='04'" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               prop="sampleChuChangBianHao">
              <a-input v-model="model.sampleChuChangBianHao" placeholder="请输入出厂编号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="强度等级" v-show="model.titCode=='04'" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               prop="sampleQiangDuDengJi">
              <j-dict-select-tag type="list" v-model="model.sampleQiangDuDengJi" :trigger-change="true"
                                 dictCode="qiangdudengji" placeholder="请选择强度等级"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="龄期" v-show="model.titCode=='04'" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               prop="sampleLingQi">
              <j-search-select-tag type="list" v-model="model.sampleLingQi" :trigger-change="true"
                                   :dictOptions="dictOptions" placeholder="请选择龄期"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="生产日期" v-show="model.titCode=='0701'||model.titCode=='1801'||model.titCode=='1802'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleShengChanRiQi">
              <j-date v-model="model.sampleShengChanRiQi" placeholder="请选择生产日期" :show-time="true"
                      dateFormat="YYYY-MM-DD"></j-date>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="结合料种类" v-show="model.titCode=='0702'" :labelCol="labelCol"
                               :wrapperCol="wrapperCol" prop="sampleJieHeLiaoZhongLei">
              <a-input v-model="model.sampleJieHeLiaoZhongLei" placeholder="请输入结合料种类"></a-input>
            </a-form-model-item>
          </a-col>
          <!--          <a-col :span="24">-->
          <!--            <a-form-model-item label="钢筋直径"-->
          <!--                               v-show="model.titCode=='1001'||model.titCode=='1002'||model.titCode=='1003'||model.titCode=='1004'||model.titCode=='1005'"-->
          <!--                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleGangJinZhiJing">-->
          <!--              <a-input-number type="list" v-model="model.sampleGangJinZhiJing" style="width: 200px" :min="0" :max="100"-->
          <!--                              :step="1" string-mode-->
          <!--                              placeholder="请输入钢筋直径"/>-->
          <!--            </a-form-model-item>-->
          <!--          </a-col>-->
          <a-col :span="15"
                 v-show="model.titCode=='1402'||model.titCode=='1001'||model.titCode=='1002'||model.titCode=='1003'||model.titCode=='1004'||model.titCode=='1005'">
            <a-form-model-item label="钢筋直径" :labelCol="{span:8}" :wrapperCol="{span:12}" prop="zhonglei">
              <j-search-select-tag type="list" v-model="model.zhonglei" :trigger-change="true"
                                   :dictOptions="dictOptions2" placeholder="请选择钢筋类型"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="9"
                 v-show="model.titCode=='1402'||model.titCode=='1001'||model.titCode=='1002'||model.titCode=='1003'||model.titCode=='1004'||model.titCode=='1005'">
            <a-form-model-item :wrapperCol="wrapperCol" prop="zhijing">
              <a-input-number type="list" v-model="model.zhijing" style="width: 200px" :min="0" :max="100" :step="1"
                              string-mode
                              placeholder="请输入钢筋直径"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="公称壁厚" v-show="model.titCode=='1402'" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               prop="sampleGongChengBiHou">
              <a-input v-model="model.sampleGongChengBiHou" placeholder="请输入公称壁厚"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="长度" v-show="model.titCode=='1402'" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               prop="sampleChangDu">
              <a-input v-model="model.sampleChangDu" placeholder="请输入长度"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="钢筋牌号"
                               v-show="model.titCode=='1001'||model.titCode=='1002'||model.titCode=='1003'||model.titCode=='1004'||model.titCode=='1005'||model.titCode=='1402'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleGangJinZhongLei">
              <j-dict-select-tag type="list" v-model="model.sampleGangJinZhongLei" :trigger-change="true"
                                 dictCode="gangjinzhonglei" placeholder="请选择钢筋牌号"/>
            </a-form-model-item>
          </a-col>
          <!-- ***********选择显示部分---end************** -->
          <a-col :span="24">
            <a-form-model-item label="生产批号"
                               v-show="model.titCode!=null&&model.titCode!='02'&&model.titCode!='07'&&model.titCode!='10'&&model.titCode!='14'&&model.titCode!='18'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleShengChanPiHao">
              <a-input v-model="model.sampleShengChanPiHao" placeholder="请输入生产批号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样人" :rules="[{ required: true, message: '取样人不能为空' }]"
                               v-show="model.titCode!=null&&model.titCode!='02'&&model.titCode!='07'&&model.titCode!='10'&&model.titCode!='14'&&model.titCode!='18'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleQuYangRen">
              <a-input v-model="model.sampleQuYangRen" placeholder="请输入取样人"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否留样"
                               v-show="model.titCode!=null&&model.titCode!='02'&&model.titCode!='07'&&model.titCode!='10'&&model.titCode!='14'&&model.titCode!='18'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shifouliuyang">
              <j-search-select-tag type="list" v-model="model.shifouliuyang" :trigger-change="true"
                                   :dictOptions="dictOptions1" placeholder="请选择是否留样"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测依据" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ShiYanYiJu">
              <j-multi-select-tag v-model="model.ShiYanYiJu"
                                  dictCode="sy_dps_jc_rules,rulename,ruleno,ruleType = '试验依据'"
                                  placeholder="请选择检测依据"/>
              <!--                                 dictCode="(SELECT CONCAT(ruleName,'(',ruleNo,')') as ruleNameNo , ruleNo,ruleName,uuid,ruletype FROM sy_dps_jc_rules )|ruleNameNo|ruleNo| ruletype = '试验依据'"-->
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
            <a-form-model-item label="生产日期" v-show="model.titCode=='0701'||model.titCode=='1801'||model.titCode=='1802'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleShengChanRIQi">
              <j-date v-model="model.sampleShengChanRIQi" placeholder="请选择生产日期" :show-time="true"
                      dateFormat="YYYY-MM-DD"></j-date>
            </a-form-model-item>
          </a-col>

          <a-col :span="24">
            <a-form-model-item label="留样日期"
                               v-show="model.shifouliuyang=='1'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="liuyangriqi">
              <j-date v-model="model.liuyangriqi" placeholder="请选择留样日期" :show-time="true"
                      dateFormat="YYYY-MM-DD"></j-date>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="留样数量" v-show="model.shifouliuyang=='1'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleLiuYangShuLiang">
              <a-input v-model="model.sampleLiuYangShuLiang" placeholder="请输入留样数量"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="留样期限(天)" v-show="model.shifouliuyang=='1'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="liuyangqixian">
              <a-input v-model="model.liuyangqixian" placeholder="请输入留样期限"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="留样处理" v-show="model.shifouliuyang=='1'"
                               :labelCol="labelCol" :wrapperCol="wrapperCol" prop="liuyangchuli">
              <a-input v-model="model.liuyangchuli" placeholder="请输入留样处理"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注"
                               v-show="model.titCode!=null&&model.titCode!='02'&&model.titCode!='07'&&model.titCode!='10'&&model.titCode!='14'&&model.titCode!='18'"
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
  name: 'SyDpsSySampleForm',
  components: {
    JselectDqProjName,
    JSearchSelectTag,
    SampleType,
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
      selectValue: '',
      asyncSelectValue: '',
      dictOptions: [{
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
      dictOptions1: [{
        text: '否',
        value: '0'
      }, {
        text: '是',
        value: '1'
      }],
      dictOptions2: [{
        text: 'Ⅰ类钢筋',
        value: 'A'
      }, {
        text: 'Ⅱ类钢筋',
        value: 'B'
      }, {
        text: 'Ⅲ类钢筋',
        value: 'C'
      }, {
        text: 'Ⅳ类钢筋',
        value: 'D'
      }],
      dictOptions3: [{
        text: 'TJ04合同段',
        value: 'TJ04合同段'
      }],
      type: 0,
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
    getTiNames(val, ptiNo) {
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
          if (this.model.sampleGangJinZhiJing != null) {
            this.model.sampleGangJinZhiJing = '' + this.model.zhonglei + this.model.zhijing
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