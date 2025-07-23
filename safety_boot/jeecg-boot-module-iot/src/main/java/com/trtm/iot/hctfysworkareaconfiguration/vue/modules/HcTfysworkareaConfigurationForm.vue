<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectid">
              <a-input v-model="model.projectid" placeholder="请输入项目名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="标段名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sectionid">
              <a-input v-model="model.sectionid" placeholder="请输入标段名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开振占比（%）最小值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vibrateratioMin">
              <a-input v-model="model.vibrateratioMin" placeholder="请输入开振占比（%）最小值"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开振占比（%）最大值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vibrateratioMax">
              <a-input v-model="model.vibrateratioMax" placeholder="请输入开振占比（%）最大值"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均碾压遍数最小值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="timesavgMin">
              <a-input v-model="model.timesavgMin" placeholder="请输入平均碾压遍数最小值"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均碾压遍数最大值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="timesavgMax">
              <a-input v-model="model.timesavgMax" placeholder="请输入平均碾压遍数最大值"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均厚度（m）最小值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="thickavgMin">
              <a-input v-model="model.thickavgMin" placeholder="请输入平均厚度（m）最小值"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均厚度（m）最大值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="thickavgMax">
              <a-input v-model="model.thickavgMax" placeholder="请输入平均厚度（m）最大值"  ></a-input>
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
    name: 'HcTfysworkareaConfigurationForm',
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
          add: "/hctfysworkareaconfiguration/hcTfysworkareaConfiguration/add",
          edit: "/hctfysworkareaconfiguration/hcTfysworkareaConfiguration/edit",
          queryById: "/hctfysworkareaconfiguration/hcTfysworkareaConfiguration/queryById"
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