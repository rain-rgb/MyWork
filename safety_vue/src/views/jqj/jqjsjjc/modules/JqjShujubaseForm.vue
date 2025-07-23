<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['deviceCode']" placeholder="请输入设备编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="数据类标志" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['dataType']" placeholder="请输入数据类标志" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="大车横向行程" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['bigCrosswise']" placeholder="请输入大车横向行程(放大100倍)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="大车纵向行程" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['bigPortrait']" placeholder="请输入大车纵向行程（放大100倍）" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="支腿垂直度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['legPerpendicularity']" placeholder="请输入支腿垂直度(放大100倍)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="大车水平度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['bigLevel']" placeholder="请输入大车水平度(放大100倍)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="天车高度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['skyHeight1']" placeholder="请输入1#天车高度(放大100倍)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="天车吊重" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['skyWeight1']" placeholder="请输入1#天车吊重（放大100倍）" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="天车横向行程" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['skyCrosswise1']" placeholder="请输入1#天车横向行程（）" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="天车纵向行程" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['skyPortrait1']" placeholder="请输入1#天车纵向行程（）" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="天车吊钩载荷状态

" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['skyLoad']" placeholder="请输入1#天车吊钩载荷状态

" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="1#天车吊钩制动器1状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['skyBraking11']" placeholder="请输入1#天车吊钩制动器1状态
" style="width: 100%" />
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

  export default {
    name: 'JqjShujubaseForm',
    components: {
      JFormContainer,
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
          add: "/com.trtm.iot/jqjShujubase/add",
          edit: "/com.trtm.iot/jqjShujubase/edit",
          queryById: "/com.trtm.iot/jqjShujubase/queryById"
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
          this.form.setFieldsValue(pick(this.model,'deviceCode','dataType','bigCrosswise','bigPortrait','legPerpendicularity','bigLevel','skyHeight1','skyWeight1','skyCrosswise1','skyPortrait1','skyLoad','skyBraking11'))
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
        this.form.setFieldsValue(pick(row,'deviceCode','dataType','bigCrosswise','bigPortrait','legPerpendicularity','bigLevel','skyHeight1','skyWeight1','skyCrosswise1','skyPortrait1','skyLoad','skyBraking11'))
      },
    }
  }
</script>