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
            <a-form-model-item label="支付日期(年-月)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="paymentDate">
              <a-input placeholder="请输入支付日期" v-model="model.paymentDate" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="应付工资" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="payableWages">
              <a-input v-model="model.payableWages" placeholder="请输入应付工资"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="已付工资" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="salaryPaid">
              <a-input v-model="model.salaryPaid" placeholder="请输入已付工资"  @input="input"></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="支付比例" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="payoutRatio">-->
<!--              <a-input v-model="model.payoutRatio" placeholder="请输入支付比例"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
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
    name: 'SalaryListForm',
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
          add: "/salarylist/salaryList/add",
          edit: "/salarylist/salaryList/edit",
          queryById: "/salarylist/salaryList/queryById"
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
      input(){
        if (this.model.payableWages&&this.model.salaryPaid){
          if (Number(this.model.payableWages)!==0){
            this.model.payoutRatio = (Number(this.model.salaryPaid)/Number(this.model.payableWages)*100).toFixed(2)
          }else {
            this.model.payoutRatio = 0;
          }
        }
      },
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
        if (this.model.payableWages&&this.model.salaryPaid){
          if (Number(this.model.payableWages)!==0){
            this.model.payoutRatio = (Number(this.model.salaryPaid)/Number(this.model.payableWages)*100).toFixed(2)
          }else {
            this.model.payoutRatio = 0;
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