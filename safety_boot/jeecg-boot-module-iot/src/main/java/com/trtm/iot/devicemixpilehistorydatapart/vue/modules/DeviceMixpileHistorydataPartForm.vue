<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shebeino']" placeholder="请输入设备编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['pileNo']" placeholder="请输入桩号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="段号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['partPilec']" placeholder="请输入段号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="段深度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['partDep']" placeholder="请输入段深度"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="段浆量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['partBeton']" placeholder="请输入段浆量"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="段速度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['partSpeed']" placeholder="请输入段速度"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="段时长" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['partTime']" placeholder="请输入段时长"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="段x" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['partX']" placeholder="请输入段x"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="段y" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['partY']" placeholder="请输入段y"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="段状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['partState']" placeholder="请输入段状态"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="段结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['partEndtime']" placeholder="请输入段结束时间"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择数据时间" v-decorator="['datatime']" :trigger-change="true" style="width: 100%" />
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
    name: 'DeviceMixpileHistorydataPartForm',
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
          add: "/devicemixpilehistorydatapart/deviceMixpileHistorydataPart/add",
          edit: "/devicemixpilehistorydatapart/deviceMixpileHistorydataPart/edit",
          queryById: "/devicemixpilehistorydatapart/deviceMixpileHistorydataPart/queryById"
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
          this.form.setFieldsValue(pick(this.model,'shebeino','pileNo','partPilec','partDep','partBeton','partSpeed','partTime','partX','partY','partState','partEndtime','datatime'))
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
        this.form.setFieldsValue(pick(row,'shebeino','pileNo','partPilec','partDep','partBeton','partSpeed','partTime','partX','partY','partState','partEndtime','datatime'))
      },
    }
  }
</script>