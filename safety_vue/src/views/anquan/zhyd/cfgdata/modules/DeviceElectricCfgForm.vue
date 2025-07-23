<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JselectDqDepart v-decorator="['sysOrgCode']"  />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-search-select-tag placeholder="请选择设备名称" v-decorator="['imei']" :dictOptions="dictOption" >
                </j-search-select-tag>
                {{ selectValue }}
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12" >
              <a-form-item label="智慧用电超标号码组" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['zhydPhones']" :trigger-change="true" dictCode="bhz_phone,names,uid,phonesname='6'" dictTable="bhz_phone" placeholder="请选择智慧用电超标号码"/>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="是否报警" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['isCall']" :trigger-change="true" dictCode="is_call" placeholder="请选择是否报警" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="电压A标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['voltagebza']" placeholder="请输入电压A标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="电压B标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['voltagebzb']" placeholder="请输入电压B标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="电压C标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['voltagebzc']" placeholder="请输入电压C标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="频率A标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['frequencybza']" placeholder="请输入频率A标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="频率B标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['frequencybzb']" placeholder="请输入频率B标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="频率C标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['frequencybzc']" placeholder="请输入频率C标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="电流A标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['electricitybza']" placeholder="请输入电流A标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="电流B标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['electricitybzb']" placeholder="请输入电流B标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="电流C标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['electricitybzc']" placeholder="请输入电流C标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="漏电流标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['seepelectricitybz']" placeholder="请输入漏电流标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="温度A标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['tempbza']" placeholder="请输入温度A标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="温度B标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['tempbzb']" placeholder="请输入温度B标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="温度C标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['tempbzc']" placeholder="请输入温度C标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="温度N标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['tempbzn']" placeholder="请输入温度N标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="电能A标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['energybza']" placeholder="请输入电能A标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="电能B标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['energybzb']" placeholder="请输入电能B标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="电能C标准值" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['energybzc']" placeholder="请输入电能C标准值" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

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
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'

  export default {
    name: 'DeviceElectricCfgForm',
    components: {
      JFormContainer,
      JselectDqDepart
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
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
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        form: this.$form.createForm(this),
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
        },
        url: {
          add: "/zhydcfg/deviceElectricCfg/add",
          edit: "/zhydcfg/deviceElectricCfg/edit",
          queryById: "/zhydcfg/deviceElectricCfg/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return true
          }
        }
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData();
      this.shebeiList();
    },
    methods: {
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'17'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'sysOrgCode','zhydPhones','imei','isCall','voltagebza','voltagebzb','voltagebzc','frequencybza','frequencybzb','frequencybzc','electricitybza','electricitybzb','electricitybzc','seepelectricitybz','tempbza','tempbzb','tempbzc','tempbzn','energybza','energybzb','energybzc'))
        })
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
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
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
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
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'sysOrgCode','zhydPhones','imei','isCall','voltagebza','voltagebzb','voltagebzc','frequencybza','frequencybzb','frequencybzc','electricitybza','electricitybzb','electricitybzc','seepelectricitybz','tempbza','tempbzb','tempbzc','tempbzn','energybza','energybzb','energybzc'))
      },
    }
  }
</script>