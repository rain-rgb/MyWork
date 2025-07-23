<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="实验ID,唯一键" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="trialid">
              <a-input v-model="model.trialid" placeholder="请输入实验ID,唯一键"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指挥部实验室ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="labid">
              <a-input v-model="model.labid" placeholder="请输入指挥部实验室ID"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="第三方名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入第三方名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="实验类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="trialtype">
              <a-input v-model="model.trialtype" placeholder="请输入实验类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="engineeringname">
              <a-input v-model="model.engineeringname" placeholder="请输入工程名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试件编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reagentno">
              <a-input v-model="model.reagentno" placeholder="请输入试件编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="龄期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="age">
              <a-input-number v-model="model.age" placeholder="请输入龄期" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试剂尺寸" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="trialsize">
              <a-input v-model="model.trialsize" placeholder="请输入试剂尺寸"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试件数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="trialnum">
              <a-input-number v-model="model.trialnum" placeholder="请输入试件数量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设计强度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="designstrength">
              <a-input v-model="model.designstrength" placeholder="请输入设计强度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设计特征值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="designvalue">
              <a-input v-model="model.designvalue" placeholder="请输入设计特征值"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="实验时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="trialtime">
              <a-input v-model="model.trialtime" placeholder="请输入实验时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="实验结果0合格1不合格2未出报告" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="trialresult">
              <a-input-number v-model="model.trialresult" placeholder="请输入实验结果0合格1不合格2未出报告" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createid">
              <a-input v-model="model.createid" placeholder="请输入创建人"  ></a-input>
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
    name: 'FTrialForm',
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
          add: "/outsource/fTrial/add",
          edit: "/outsource/fTrial/edit",
          queryById: "/outsource/fTrial/queryById"
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
