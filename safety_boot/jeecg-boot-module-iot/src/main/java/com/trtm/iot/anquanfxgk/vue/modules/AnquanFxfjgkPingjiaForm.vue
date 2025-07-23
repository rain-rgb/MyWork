<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="项目" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectName">
              <a-input v-model="model.projectName" placeholder="请输入项目"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="位置" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="position">
              <a-input v-model="model.position" placeholder="请输入位置"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="风险" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="risk">
              <a-input v-model="model.risk" placeholder="请输入风险"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="相关单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="conherentUnit">
              <a-input v-model="model.conherentUnit" placeholder="请输入相关单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="管控负责人管控情况" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="managementHead">
              <a-input v-model="model.managementHead" placeholder="请输入管控负责人管控情况"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="管控负责人管控情况评价" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="managementHeadPj">
              <a-input v-model="model.managementHeadPj" placeholder="请输入管控负责人管控情况评价"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="安全管理人员管控情况" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="securityManagement">
              <a-input v-model="model.securityManagement" placeholder="请输入安全管理人员管控情况"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="安全管理人员管控情况评价" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="securityManagementPj">
              <a-input v-model="model.securityManagementPj" placeholder="请输入安全管理人员管控情况评价"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="安全负责人管控情况" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="securityHead">
              <a-input v-model="model.securityHead" placeholder="请输入安全负责人管控情况"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="安全负责人管控情况评价" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="securityHeadPj">
              <a-input v-model="model.securityHeadPj" placeholder="请输入安全负责人管控情况评价"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主要负责人管控情况" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="head">
              <a-input v-model="model.head" placeholder="请输入主要负责人管控情况"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主要负责人管控情况评价" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="headPj">
              <a-input v-model="model.headPj" placeholder="请输入主要负责人管控情况评价"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="单位管控情况" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unitName">
              <a-input v-model="model.unitName" placeholder="请输入单位管控情况"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="单位管控情况评价" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="unitPj">
              <a-input v-model="model.unitPj" placeholder="请输入单位管控情况评价"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="总体评价" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totalityPj">
              <a-input v-model="model.totalityPj" placeholder="请输入总体评价"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="评价人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pjName">
              <a-input v-model="model.pjName" placeholder="请输入评价人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="评价时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pjTime">
              <j-date placeholder="请选择评价时间" v-model="model.pjTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主表guid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baseGuid">
              <a-input v-model="model.baseGuid" placeholder="请输入主表guid"  ></a-input>
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
    name: 'AnquanFxfjgkPingjiaForm',
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
          add: "/anquanfxgk/anquanFxfjgkPingjia/add",
          edit: "/anquanfxgk/anquanFxfjgkPingjia/edit",
          queryById: "/anquanfxgk/anquanFxfjgkPingjia/queryById"
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