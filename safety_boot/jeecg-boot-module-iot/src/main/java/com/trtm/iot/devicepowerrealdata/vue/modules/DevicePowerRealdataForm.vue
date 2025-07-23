<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="imei">
              <a-input v-model="model.imei" placeholder="请输入设备id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据上传时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="powerdate">
              <j-date placeholder="请选择数据上传时间" v-model="model.powerdate"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devicestatus">
              <a-input-number v-model="model.devicestatus" placeholder="请输入设备状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="信号强度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="signal">
              <a-input-number v-model="model.signal" placeholder="请输入信号强度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道1漏电mA" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t1">
              <a-input-number v-model="model.t1" placeholder="请输入通道1漏电mA" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道2A相温度℃" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t2">
              <a-input-number v-model="model.t2" placeholder="请输入通道2A相温度℃" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道B相温度℃" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t3">
              <a-input-number v-model="model.t3" placeholder="请输入通道B相温度℃" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道4C相温度℃" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t4">
              <a-input-number v-model="model.t4" placeholder="请输入通道4C相温度℃" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道5N相温度℃" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t5">
              <a-input-number v-model="model.t5" placeholder="请输入通道5N相温度℃" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道6A相电流A" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t6">
              <a-input-number v-model="model.t6" placeholder="请输入通道6A相电流A" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道7B相电流A" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t7">
              <a-input-number v-model="model.t7" placeholder="请输入通道7B相电流A" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道8C相电流A" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t8">
              <a-input-number v-model="model.t8" placeholder="请输入通道8C相电流A" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道9A相电压V" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t9">
              <a-input-number v-model="model.t9" placeholder="请输入通道9A相电压V" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道10B相电压V" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t10">
              <a-input-number v-model="model.t10" placeholder="请输入通道10B相电压V" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道11C相电压V" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t11">
              <a-input-number v-model="model.t11" placeholder="请输入通道11C相电压V" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道12A相频率Hz" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t12">
              <a-input-number v-model="model.t12" placeholder="请输入通道12A相频率Hz" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道13B相频率Hz" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t13">
              <a-input-number v-model="model.t13" placeholder="请输入通道13B相频率Hz" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道14C相频率Hz" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t14">
              <a-input-number v-model="model.t14" placeholder="请输入通道14C相频率Hz" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道15A相电压相位角°" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t15">
              <a-input-number v-model="model.t15" placeholder="请输入通道15A相电压相位角°" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道16B相电压相位角°" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t16">
              <a-input-number v-model="model.t16" placeholder="请输入通道16B相电压相位角°" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道17C相电压相位角°" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t17">
              <a-input-number v-model="model.t17" placeholder="请输入通道17C相电压相位角°" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道18A相电流相位角°" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t18">
              <a-input-number v-model="model.t18" placeholder="请输入通道18A相电流相位角°" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道19B相电流相位角°" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t19">
              <a-input-number v-model="model.t19" placeholder="请输入通道19B相电流相位角°" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道20C相电流相位角°" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t20">
              <a-input-number v-model="model.t20" placeholder="请输入通道20C相电流相位角°" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道21A相功率因素%" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t21">
              <a-input-number v-model="model.t21" placeholder="请输入通道21A相功率因素%" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道22B相功率因素%" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t22">
              <a-input-number v-model="model.t22" placeholder="请输入通道22B相功率因素%" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道23C相功率因素%" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t23">
              <a-input-number v-model="model.t23" placeholder="请输入通道23C相功率因素%" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道24合相功率因素%" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t24">
              <a-input-number v-model="model.t24" placeholder="请输入通道24合相功率因素%" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道25A相有功功率KW" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t25">
              <a-input-number v-model="model.t25" placeholder="请输入通道25A相有功功率KW" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道26B相有功功率KW" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t26">
              <a-input-number v-model="model.t26" placeholder="请输入通道26B相有功功率KW" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道27C相有功功率KW" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t27">
              <a-input-number v-model="model.t27" placeholder="请输入通道27C相有功功率KW" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道28合相有功功率KW" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t28">
              <a-input-number v-model="model.t28" placeholder="请输入通道28合相有功功率KW" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道29A相无功功率KVar" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t29">
              <a-input-number v-model="model.t29" placeholder="请输入通道29A相无功功率KVar" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道30B相无功功率KVar" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t30">
              <a-input-number v-model="model.t30" placeholder="请输入通道30B相无功功率KVar" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道31C相无功功率KVar" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t31">
              <a-input-number v-model="model.t31" placeholder="请输入通道31C相无功功率KVar" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道32合相无功功率KVar" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t32">
              <a-input-number v-model="model.t32" placeholder="请输入通道32合相无功功率KVar" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道33A相视在功率KVA" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t33">
              <a-input-number v-model="model.t33" placeholder="请输入通道33A相视在功率KVA" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道34B相视在功率KVA" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t34">
              <a-input-number v-model="model.t34" placeholder="请输入通道34B相视在功率KVA" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道35C相视在功率KVA" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t35">
              <a-input-number v-model="model.t35" placeholder="请输入通道35C相视在功率KVA" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道36合相视在功率KVA" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t36">
              <a-input-number v-model="model.t36" placeholder="请输入通道36合相视在功率KVA" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道37A相有功电能高位W KWH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t37">
              <a-input-number v-model="model.t37" placeholder="请输入通道37A相有功电能高位W KWH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道37A相有功电能低位KWH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t38">
              <a-input-number v-model="model.t38" placeholder="请输入通道37A相有功电能低位KWH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道39B相有功电能高位W KWH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t39">
              <a-input-number v-model="model.t39" placeholder="请输入通道39B相有功电能高位W KWH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道40B相有功电能低位KWH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t40">
              <a-input-number v-model="model.t40" placeholder="请输入通道40B相有功电能低位KWH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道41C相有功电能高位W KWH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t41">
              <a-input-number v-model="model.t41" placeholder="请输入通道41C相有功电能高位W KWH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道42C相有功电能低位KWH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t42">
              <a-input-number v-model="model.t42" placeholder="请输入通道42C相有功电能低位KWH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道43合相有功电能高位W KWH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t43">
              <a-input-number v-model="model.t43" placeholder="请输入通道43合相有功电能高位W KWH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道44合相有功电能低位KWH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t44">
              <a-input-number v-model="model.t44" placeholder="请输入通道44合相有功电能低位KWH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道45A相无功电能高位W KVarH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t45">
              <a-input-number v-model="model.t45" placeholder="请输入通道45A相无功电能高位W KVarH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道46A相无功电能低位KVarH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t46">
              <a-input-number v-model="model.t46" placeholder="请输入通道46A相无功电能低位KVarH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道47B相无功电能高位W KVarH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t47">
              <a-input-number v-model="model.t47" placeholder="请输入通道47B相无功电能高位W KVarH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道48B相无功电能低位KVarH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t48">
              <a-input-number v-model="model.t48" placeholder="请输入通道48B相无功电能低位KVarH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道49C相无功电能高位W KVarH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t49">
              <a-input-number v-model="model.t49" placeholder="请输入通道49C相无功电能高位W KVarH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道50C相无功电能低位KVarH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t50">
              <a-input-number v-model="model.t50" placeholder="请输入通道50C相无功电能低位KVarH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道51合相无功电能高位W KVarH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t51">
              <a-input-number v-model="model.t51" placeholder="请输入通道51合相无功电能高位W KVarH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道52合相无功电能低位KVarH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t52">
              <a-input-number v-model="model.t52" placeholder="请输入通道52合相无功电能低位KVarH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道53A相视在电能高位W KVAH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t53">
              <a-input-number v-model="model.t53" placeholder="请输入通道53A相视在电能高位W KVAH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道54A相视在电能低位KVAH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t54">
              <a-input-number v-model="model.t54" placeholder="请输入通道54A相视在电能低位KVAH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道55B相视在电能高位W KVAH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t55">
              <a-input-number v-model="model.t55" placeholder="请输入通道55B相视在电能高位W KVAH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道56B相视在电能低位KVAH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t56">
              <a-input-number v-model="model.t56" placeholder="请输入通道56B相视在电能低位KVAH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道57C相视在电能高位W KVAH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t57">
              <a-input-number v-model="model.t57" placeholder="请输入通道57C相视在电能高位W KVAH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道58C相视在电能低位KVAH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t58">
              <a-input-number v-model="model.t58" placeholder="请输入通道58C相视在电能低位KVAH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道59合相视在电能高位W KVAH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t59">
              <a-input-number v-model="model.t59" placeholder="请输入通道59合相视在电能高位W KVAH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道60合相视在电能低位KVAH" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t60">
              <a-input-number v-model="model.t60" placeholder="请输入通道60合相视在电能低位KVAH" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道61三相电流平衡度%" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t61">
              <a-input-number v-model="model.t61" placeholder="请输入通道61三相电流平衡度%" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label=" 通道62三相电压平衡度%" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="t62">
              <a-input-number v-model="model.t62" placeholder="请输入 通道62三相电压平衡度%" style="width: 100%" />
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
    name: 'DevicePowerRealdataForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
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
        validatorRules: {
        },
        url: {
          add: "/devicepowerrealdata/devicePowerRealdata/add",
          edit: "/devicepowerrealdata/devicePowerRealdata/edit",
          queryById: "/devicepowerrealdata/devicePowerRealdata/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
    }
  }
</script>