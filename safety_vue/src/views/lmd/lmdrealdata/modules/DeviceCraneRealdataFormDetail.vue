<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['deviceCode_dictText']" placeholder="请输入设备名称"  ></a-input>
              </a-form-item>
            </a-col>
           
            <a-col :span="12">
              <a-form-item label="数据上传时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择数据上传时间" v-decorator="['cranedate']" :trigger-change="true" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
           <a-col :span="12">
             <a-form-item label="主钩载荷状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
               <a-input-number v-decorator="['mainHookstatus_dictText']" placeholder="请输入主钩载荷状态" style="width: 100%" />
             </a-form-item>
           </a-col>
           <a-col :span="12">
             <a-form-item label="预留副钩载荷状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
               <a-input-number v-decorator="['reservedVicehookstatus_dictText']" placeholder="请输入预留副钩载荷状态" style="width: 100%" />
             </a-form-item>
            </a-col>
           </a-row>

         <a-row>
            <a-col :span="12">
              <a-form-item label="风速报警" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['windSpeedalarm_dictText']" placeholder="请输入风速报警" style="width: 100%" />
             </a-form-item>
           </a-col>
           <a-col :span="12">
              <a-form-item label="风速" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['windSpeed']" placeholder="请输入风速" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="大车行程" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['bigCarroute']" placeholder="请输入大车行程" style="width: 100%" />
              </a-form-item>
            </a-col>
           <a-col :span="12">
             <a-form-item label="支腿偏差报警" :labelCol="labelCol" :wrapperCol="wrapperCol">
               <a-input-number v-decorator="['bigcarAlm_dictText']" placeholder="请输入支腿偏差报警" style="width: 100%" />
             </a-form-item>
           </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="主钩高度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['mainHookheight']" placeholder="请输入主钩高度" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="预留副钩高度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['reservedVicehookheight']" placeholder="请输入预留副钩高度" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="主钩吊重" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['mainHookload']" placeholder="请输入主钩吊重" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="预留副钩吊重" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['reservedVicehookload']" placeholder="请输入预留副钩吊重" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="小车行程" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['smallCarroute']" placeholder="请输入小车行程" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="预留小车2行程" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['reservedSmallcarroute']" placeholder="请输入预留小车2行程" style="width: 100%" />
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
    name: 'DeviceCraneRealdataForm',
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
          list:"/devicecranerealdata/deviceCraneRealdata/list",
          add: "/devicecranerealdata/deviceCraneRealdata/add",
          edit: "/devicecranerealdata/deviceCraneRealdata/edit",
          queryById: "/devicecranerealdata/deviceCraneRealdata/queryById"
        },
        disabled: {
          default: false,
          required: false
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
      showFlowSubmitButton(){
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      let e = this.$route.query.deviceCode
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
          this.form.setFieldsValue(pick(this.model,'deviceCode_dictText','deviceType','bigCarroute','cranedate','mainHookheight','reservedVicehookheight','mainHookload','reservedVicehookload','smallCarroute','reservedSmallcarroute','windSpeed','reservedAnalogquantityone','reservedAnalogquantitytwo','mainHookstatus_dictText','reservedVicehookstatus_dictText','windSpeedalarm_dictText','bigCarleftlimit','bigCarrightlimit','smallCarfrontlimit','smallCarbacklimit','reservedSmallcarfrontlimit','reservedSmallcarbacklimit','hookUplimit','reservedVicehookuplimit','doorLimit','windAntiskidstatus','wireDrumstatus','smallCar1brake1','smallCar1brake2','smallCar2brake1','smallCar2brake2','bigCarlegdeviation','allTime','allTimes','bigcarAlm_dictText','reservedOne','reservedTwo'))
        })
      },
      //渲染流程表单数据
      showFlowData(e){
        let deviceCode = e
        let params = {deviceCode};
          getAction(this.url.list,params).then((res)=>{
            if(res.success){
              this.edit (res.result.records[0]);
            }
          });
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
        this.form.setFieldsValue(pick(row,'deviceCode_dictText','deviceType','bigCarroute','cranedate','mainHookheight','reservedVicehookheight','mainHookload','reservedVicehookload','smallCarroute','reservedSmallcarroute','windSpeed','reservedAnalogquantityone','reservedAnalogquantitytwo','mainHookstatus_dictText','reservedVicehookstatus_dictText','windSpeedalarm_dictText','bigCarleftlimit','bigCarrightlimit','smallCarfrontlimit','smallCarbacklimit','reservedSmallcarfrontlimit','reservedSmallcarbacklimit','hookUplimit','reservedVicehookuplimit','doorLimit','windAntiskidstatus','wireDrumstatus','smallCar1brake1','smallCar1brake2','smallCar2brake1','smallCar2brake2','bigCarlegdeviation','allTime','allTimes','bigcarAlm_dictText','reservedOne','reservedTwo'))
      },
    }
  }
</script>