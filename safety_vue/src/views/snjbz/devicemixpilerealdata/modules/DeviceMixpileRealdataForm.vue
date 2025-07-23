<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['shebeino_dictText']" placeholder="请输入设备名称"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="设计深度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['designdep']" placeholder="请输入设计深度"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="当前深度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['curdep']" placeholder="请输入当前深度"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="x轴角度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['x']" placeholder="请输入x轴角度"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="y轴角度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['y']" placeholder="请输入y轴角度"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="当前电流" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['elec']" placeholder="请输入当前电流"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="已灌浆量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['grouting']" placeholder="请输入已灌浆量"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['pileno']" placeholder="请输入桩号"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="设计水灰比" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['designratio']" placeholder="请输入设计水灰比"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="设计灌浆量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['designgrout']" placeholder="请输入设计灌浆量"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="水泥量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['cement']" placeholder="请输入水泥量"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="用水量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['water']" placeholder="请输入用水量"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="水灰比" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['ratio']" placeholder="请输入水灰比"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="盘次" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['serial']" placeholder="请输入盘次"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="操作员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['username']" placeholder="请输入操作员"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择数据时间" v-decorator="['datatime']" :trigger-change="true" style="width: 100%" />
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
    name: 'DeviceMixpileRealdataForm',
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
          add: "/devicemixpilerealdata/deviceMixpileRealdata/add",
          edit: "/devicemixpilerealdata/deviceMixpileRealdata/edit",
          queryById: "/devicemixpilerealdata/deviceMixpileRealdata/queryById"
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
          this.form.setFieldsValue(pick(this.model,'shebeino_dictText','designdep','curdep','x','y','elec','grouting','pileno','designratio','designgrout','cement','water','ratio','serial','username','datatime'))
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
        this.form.setFieldsValue(pick(row,'shebeino_dictText','designdep','curdep','x','y','elec','grouting','pileno','designratio','designgrout','cement','water','ratio','serial','username','datatime'))
      },
    }
  }
</script>