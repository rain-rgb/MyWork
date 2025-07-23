<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectName">
              <a-input v-model="model.projectName" placeholder="请输入项目名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
              <JselectDqDepart v-model="model.sysOrgCode" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="总投资" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totalInvestment">
              <a-input v-model="model.totalInvestment" placeholder="请输入总投资"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="总累计投资" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cumulativeInvestment">
              <a-input v-model="model.cumulativeInvestment" placeholder="请输入总累计投资"  @input="input1"></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="总进度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="schedule">-->
<!--              <a-input v-model="model.schedule" placeholder="请输入总进度"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="年总投资" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="annualInvestment">
              <a-input v-model="model.annualInvestment" placeholder="请输入年总投资"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="年累计投资" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="annualCuminvestment">
              <a-input v-model="model.annualCuminvestment" placeholder="请输入年累计投资"  @input="input2"></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="年进度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="annualSchedule">-->
<!--              <a-input v-model="model.annualSchedule" placeholder="请输入年进度"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'

  export default {
    name: 'InvestmentChecklistForm',
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
          add: "/investmentchecklist/investmentChecklist/add",
          edit: "/investmentchecklist/investmentChecklist/edit",
          queryById: "/investmentchecklist/investmentChecklist/queryById"
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
      input1(){
        if (this.model.totalInvestment&&this.model.cumulativeInvestment){
          if (Number(this.model.totalInvestment)!==0){
            this.model.schedule = (Number(this.model.cumulativeInvestment)/Number(this.model.totalInvestment)*100).toFixed(2)
          }else {
            this.model.schedule = 0;
          }
        }
      },
      input2(){
        if (this.model.annualInvestment&&this.model.annualCuminvestment){
          if (Number(this.model.totalInvestmen)!==0){
            this.model.annualSchedule = (Number(this.model.annualCuminvestment)/Number(this.model.annualInvestment)*100).toFixed(2)
          }else {
            this.model.annualSchedule = 0;
          }
        }
      },
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
        if (this.model.totalInvestment&&this.model.cumulativeInvestment){
          if (Number(this.model.totalInvestment)!==0){
            this.model.schedule = (Number(this.model.cumulativeInvestment)/Number(this.model.totalInvestment)*100).toFixed(2)
          }else {
            this.model.schedule = 0;
          }
        }
        if (this.model.annualInvestment&&this.model.annualCuminvestment){
          if (Number(this.model.annualInvestment)!==0){
            this.model.annualSchedule = (Number(this.model.annualCuminvestment)/Number(this.model.annualInvestment)*100).toFixed(2)
          }else {
            this.model.annualSchedule = 0;
          }
        }
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