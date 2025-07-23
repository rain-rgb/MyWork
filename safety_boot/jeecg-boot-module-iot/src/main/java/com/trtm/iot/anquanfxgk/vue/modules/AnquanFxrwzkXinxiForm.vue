<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="主表guid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baseGuid">
              <a-input v-model="model.baseGuid" placeholder="请输入主表guid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="唯一键" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baseid">
              <a-input v-model="model.baseid" placeholder="请输入唯一键"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectname">
              <a-input v-model="model.projectname" placeholder="请输入项目名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="工点名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="workPointsName">
              <a-input v-model="model.workPointsName" placeholder="请输入工点名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主要内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="primaryCoverage">
              <a-input v-model="model.primaryCoverage" placeholder="请输入主要内容"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="风险等级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="riskLevel">
              <a-input v-model="model.riskLevel" placeholder="请输入风险等级"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="construction">
              <a-input v-model="model.construction" placeholder="请输入施工单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supervisionUnit">
              <a-input v-model="model.supervisionUnit" placeholder="请输入监理单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主要风险因素" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mainRiskFactor">
              <a-input v-model="model.mainRiskFactor" placeholder="请输入主要风险因素"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="对策措施" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="counterMeasures">
              <a-input v-model="model.counterMeasures" placeholder="请输入对策措施"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="管控-建设单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="managDevelopment">
              <a-input v-model="model.managDevelopment" placeholder="请输入管控-建设单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="管控-施工单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="managConstruction">
              <a-input v-model="model.managConstruction" placeholder="请输入管控-施工单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="管控-监理单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="managSupervision">
              <a-input v-model="model.managSupervision" placeholder="请输入管控-监理单位"  ></a-input>
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
    name: 'AnquanFxrwzkXinxiForm',
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
          add: "/anquanfxgk/anquanFxrwzkXinxi/add",
          edit: "/anquanfxgk/anquanFxrwzkXinxi/edit",
          queryById: "/anquanfxgk/anquanFxrwzkXinxi/queryById"
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