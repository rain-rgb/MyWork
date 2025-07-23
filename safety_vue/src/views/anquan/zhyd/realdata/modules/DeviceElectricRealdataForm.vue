<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['imei_dictText']" placeholder="请输入设备编号"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="电压A" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['voltagea']" placeholder="请输入电压A" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="电压B" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['voltageb']" placeholder="请输入电压B" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="电压C" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['voltagec']" placeholder="请输入电压C" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="频率A" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['frequencya']" placeholder="请输入频率A" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="频率B" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['frequencyb']" placeholder="请输入频率B" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="频率C" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['frequencyc']" placeholder="请输入频率C" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="电流A" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['electricitya']" placeholder="请输入电流A" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="电流B" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['electricityb']" placeholder="请输入电流B" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="电流C" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['electricityc']" placeholder="请输入电流C" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="漏电流" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['seepelectricity']" placeholder="请输入漏电流" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="温度A" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['tempa']" placeholder="请输入温度A" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="温度B" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['tempb']" placeholder="请输入温度B" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="温度C" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['tempc']" placeholder="请输入温度C" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="温度N" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['tempn']" placeholder="请输入温度N" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="电能A" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['energya']" placeholder="请输入电能A" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="电能B" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['energyb']" placeholder="请输入电能B" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="电能C" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['energyc']" placeholder="请输入电能C" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="数据上传时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择数据上传时间" v-decorator="['elecdate', validatorRules.elecdate]" :trigger-change="true" style="width: 100%" />
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
  import JDate from '@/components/jeecg/JDate'

  export default {
    name: 'DeviceElectricRealdataForm',
    components: {
      JFormContainer,
      JDate,
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
          add: "/zhydreal/deviceElectricRealdata/add",
          edit: "/zhydreal/deviceElectricRealdata/edit",
          queryById: "/zhydreal/deviceElectricRealdata/queryById"
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
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'imei_dictText','voltagea','voltageb','voltagec','frequencya','frequencyb','frequencyc','electricitya','electricityb','electricityc','seepelectricity','tempa','tempb','tempc','tempn','energya','energyb','energyc','elecdate'))
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
        this.form.setFieldsValue(pick(row,'imei_dictText','voltagea','voltageb','voltagec','frequencya','frequencyb','frequencyc','electricitya','electricityb','electricityc','seepelectricity','tempa','tempb','tempc','tempn','energya','energyb','energyc','elecdate'))
      },
    }
  }
</script>