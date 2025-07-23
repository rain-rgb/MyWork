<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="人员姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['username']" placeholder="请输入人员姓名"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="卡号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['card']" placeholder="请输入卡号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="数据上传时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择数据上传时间" v-decorator="['datatime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="距离洞口距离" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['dv']" placeholder="请输入距离洞口距离" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="定位时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择定位时间" v-decorator="['dvtime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="定位基站名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['jz']" placeholder="请输入定位基站名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="距离基站距离" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['jzdv']" placeholder="请输入距离基站距离" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="基站距离洞口距离" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['jzkou']" placeholder="请输入基站距离洞口距离" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shebeino']" placeholder="请输入设备编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="基站位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['jzwz']" placeholder="请输入基站位置"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="班组编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['departmentid']" placeholder="请输入班组编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="班组名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['departname']" placeholder="请输入班组名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="X坐标" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['nx']" placeholder="请输入X坐标" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="Y坐标" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['ny']" placeholder="请输入Y坐标" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="电池电量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['voltages']" placeholder="请输入电池电量" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="报警状态0：无报警，1：按钮报警" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['alarms']" placeholder="请输入报警状态0：无报警，1：按钮报警" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="上报时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择上报时间" v-decorator="['uploadtime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="guid" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['guid']" placeholder="请输入guid"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="职务" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zhiwu']" placeholder="请输入职务"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="职务id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['zhiwuid']" placeholder="请输入职务id" style="width: 100%" />
            </a-form-item>
          </a-col>
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
    name: 'DeviceTunnelPositiondataRealForm',
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
          add: "/devicetunnelpositiondatareal/deviceTunnelPositiondataReal/add",
          edit: "/devicetunnelpositiondatareal/deviceTunnelPositiondataReal/edit",
          queryById: "/devicetunnelpositiondatareal/deviceTunnelPositiondataReal/queryById"
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
          this.form.setFieldsValue(pick(this.model,'username','card','datatime','dv','dvtime','jz','jzdv','jzkou','shebeino','jzwz','departmentid','departname','nx','ny','voltages','alarms','uploadtime','guid','zhiwu','zhiwuid'))
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
        this.form.setFieldsValue(pick(row,'username','card','datatime','dv','dvtime','jz','jzdv','jzkou','shebeino','jzwz','departmentid','departname','nx','ny','voltages','alarms','uploadtime','guid','zhiwu','zhiwuid'))
      },
    }
  }
</script>