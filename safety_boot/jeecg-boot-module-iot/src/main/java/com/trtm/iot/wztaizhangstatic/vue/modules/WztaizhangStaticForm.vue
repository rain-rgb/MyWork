<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="材料编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaono">
              <a-input v-model="model.cailiaono" placeholder="请输入材料编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规格类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao">
              <a-input v-model="model.guigexinghao" placeholder="请输入规格类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="供应商单位编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gongyingshangdanweibianma">
              <a-input v-model="model.gongyingshangdanweibianma" placeholder="请输入供应商单位编码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="jingzhongt" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jingzhongt">
              <a-input-number v-model="model.jingzhongt" placeholder="请输入jingzhongt" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="picicount" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="picicount">
              <a-input-number v-model="model.picicount" placeholder="请输入picicount" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="jianyanstate" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianyanstate">
              <a-input-number v-model="model.jianyanstate" placeholder="请输入jianyanstate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="choujianstate" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="choujianstate">
              <a-input-number v-model="model.choujianstate" placeholder="请输入choujianstate" style="width: 100%" />
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
    name: 'WztaizhangStaticForm',
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
           picicount: [
              { required: true, message: '请输入picicount!'},
           ],
           jianyanstate: [
              { required: true, message: '请输入jianyanstate!'},
           ],
           choujianstate: [
              { required: true, message: '请输入choujianstate!'},
           ],
        },
        url: {
          add: "/wztaizhangstatic/wztaizhangStatic/add",
          edit: "/wztaizhangstatic/wztaizhangStatic/edit",
          queryById: "/wztaizhangstatic/wztaizhangStatic/queryById"
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