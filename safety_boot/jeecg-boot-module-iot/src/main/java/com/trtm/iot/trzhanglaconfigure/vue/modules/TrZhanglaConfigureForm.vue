<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeiNo">
              <a-input v-model="model.shebeiNo" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否预警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="alertOrNot">
              <a-input v-model="model.alertOrNot" placeholder="请输入是否预警"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="张拉初级预警范围1" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tensionChu">
              <a-input-number v-model="model.tensionChu" placeholder="请输入张拉初级预警范围1" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="张拉初级预警范围2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tensionChuend">
              <a-input-number v-model="model.tensionChuend" placeholder="请输入张拉初级预警范围2" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="张拉中级预警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tensionZhong">
              <a-input-number v-model="model.tensionZhong" placeholder="请输入张拉中级预警" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="张拉中级预警2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tensionZhongend">
              <a-input-number v-model="model.tensionZhongend" placeholder="请输入张拉中级预警2" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="张拉高级预警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tensionGao">
              <a-input-number v-model="model.tensionGao" placeholder="请输入张拉高级预警" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="张拉高级预警2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tensionGaoend">
              <a-input-number v-model="model.tensionGaoend" placeholder="请输入张拉高级预警2" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="伸长率初级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="elongationChu">
              <a-input-number v-model="model.elongationChu" placeholder="请输入伸长率初级" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="伸长率初级2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="elongationChuend">
              <a-input-number v-model="model.elongationChuend" placeholder="请输入伸长率初级2" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="伸长率中级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="elongationZhong">
              <a-input-number v-model="model.elongationZhong" placeholder="请输入伸长率中级" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="伸长率中级2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="elongationZhongend">
              <a-input-number v-model="model.elongationZhongend" placeholder="请输入伸长率中级2" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="伸长率高级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="elongationGao">
              <a-input-number v-model="model.elongationGao" placeholder="请输入伸长率高级" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="伸长率高级2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="elongationGaoend">
              <a-input-number v-model="model.elongationGaoend" placeholder="请输入伸长率高级2" style="width: 100%" />
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
    name: 'TrZhanglaConfigureForm',
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
        },
        url: {
          add: "/trzhanglaconfigure/trZhanglaConfigure/add",
          edit: "/trzhanglaconfigure/trZhanglaConfigure/edit",
          queryById: "/trzhanglaconfigure/trZhanglaConfigure/queryById"
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