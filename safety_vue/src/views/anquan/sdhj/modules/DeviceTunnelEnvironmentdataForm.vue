<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="地点" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['site']" placeholder="请输入地点"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['shebeino_dictText']" placeholder="请输入设备编号"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="数据上传时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择数据上传时间" v-decorator="['datatime']" :trigger-change="true" style="width: 100%" dateFormat="YYYY-MM-DD HH:mm:ss"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="报警类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['alarmtype_dictText']" placeholder="请输入0无报警，1低报警，2高报警" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="氧气" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['oxygen']" placeholder="请输入氧气"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="甲烷" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['methane']" placeholder="请输入甲烷"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="硫化氢" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['hsulfide']" placeholder="请输入硫化氢"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="二氧化碳" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['cdioxide']" placeholder="请输入二氧化碳"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="一氧化碳" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['cmonoxide']" placeholder="请输入一氧化碳"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="温度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['temperature']" placeholder="请输入温度"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="风速" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['windspeed']" placeholder="请输入风速"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择数据时间" v-decorator="['uploadtime']" :trigger-change="true" style="width: 100%" dateFormat="YYYY-MM-DD HH:mm:ss"/>
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
    name: 'DeviceTunnelEnvironmentdataForm',
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
          add: "/devicetunnelenvironmentdata/deviceTunnelEnvironmentdata/add",
          edit: "/devicetunnelenvironmentdata/deviceTunnelEnvironmentdata/edit",
          queryById: "/devicetunnelenvironmentdata/deviceTunnelEnvironmentdata/queryById"
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
          this.form.setFieldsValue(pick(this.model,'site','shebeino_dictText','datatime','alarmtype_dictText','oxygen','methane','hsulfide','cdioxide','cmonoxide','uploadtime','temperature','windspeed'))
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
        this.form.setFieldsValue(pick(row,'site','shebeino_dictText','datatime','alarmtype_dictText','oxygen','methane','hsulfide','cdioxide','cmonoxide','uploadtime','temperature','windspeed'))
      },
    }
  }
</script>