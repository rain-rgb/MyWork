<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
              <JselectDqDepart v-model="model.sysOrgCode" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="已学习数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="learnedNum">
              <a-input-number v-model="model.learnedNum" placeholder="请输入已学习数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="未学习数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unlearnedNum">
              <a-input-number v-model="model.unlearnedNum" placeholder="请输入未学习数" style="width: 100%" />
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
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'

  export default {
    name: 'OnboardingRecordForm',
    components: {
      JselectDqDepart
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
          add: "/onboardingrecord/onboardingRecord/add",
          edit: "/onboardingrecord/onboardingRecord/edit",
          queryById: "/onboardingrecord/onboardingRecord/queryById"
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