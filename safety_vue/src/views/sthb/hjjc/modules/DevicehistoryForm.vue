<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['devaddr_dictText']" placeholder="请输入设备地址" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="PM10(ug/m³)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['pm10']" placeholder="请输入PM10(ug/m³)（最高200）"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="PM2.5(ug/m³)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['pm25']" placeholder="请输入PM2.5(ug/m³)（最高500）"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="噪声(dB)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['noise']" placeholder="请输入噪声(dB)（最高不高于100）"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="温度(℃)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['temperature']" placeholder="请输入温度(℃)"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="湿度(%RH)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['humidity']" placeholder="请输入湿度(%RH)（最高100）"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="风力" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['wind']" placeholder="请输入风力（最高18）"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="风速(m/s)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['windspeed']" placeholder="请输入风速(m/s) （最高不大于100）"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="风向" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['winddirection']" placeholder="请输入风向  （风向度最高360）"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="数据保存时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择数据保存的时间点" v-decorator="['timevalue']" :trigger-change="true" style="width: 100%" />
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
    name: 'DevicehistoryForm',
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
          add: "/devicehistory/devicehistory/add",
          edit: "/devicehistory/devicehistory/edit",
          queryById: "/devicehistory/devicehistory/queryById"
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
          this.form.setFieldsValue(pick(this.model,'devaddr_dictText','pm10','pm25','noise','temperature','humidity','wind','windspeed','winddirection','timevalue'))
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
        this.form.setFieldsValue(pick(row,'devaddr_dictText','pm10','pm25','noise','temperature','humidity','wind','windspeed','winddirection','timevalue'))
      },
    }
  }
</script>