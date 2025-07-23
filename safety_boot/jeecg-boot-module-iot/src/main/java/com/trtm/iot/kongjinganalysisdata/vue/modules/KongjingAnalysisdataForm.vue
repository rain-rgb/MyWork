<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="基本信息 id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="basicinfoid">
              <a-input v-model="model.basicinfoid" placeholder="请输入基本信息 id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label=" 设计井深 单位 m" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="designdepth">
              <a-input-number v-model="model.designdepth" placeholder="请输入 设计井深 单位 m" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label=" 设计孔径 单位 mm" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="designdiameter">
              <a-input-number v-model="model.designdiameter" placeholder="请输入 设计孔径 单位 mm" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最大孔径 单位 mm" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="maxdiameter">
              <a-input-number v-model="model.maxdiameter" placeholder="请输入最大孔径 单位 mm" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最小孔径 单位mm" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mindiameter">
              <a-input-number v-model="model.mindiameter" placeholder="请输入最小孔径 单位mm" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均孔径 单位 mm" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="avgdiameter">
              <a-input-number v-model="model.avgdiameter" placeholder="请输入平均孔径 单位 mm" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="偏心距 单位mm" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="eccentricity">
              <a-input-number v-model="model.eccentricity" placeholder="请输入偏心距 单位mm" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label=" 沉渣厚度  单位m" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="thickness">
              <a-input-number v-model="model.thickness" placeholder="请输入 沉渣厚度  单位m" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="垂直度 %" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="verticality">
              <a-input-number v-model="model.verticality" placeholder="请输入垂直度 %" style="width: 100%" />
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
    name: 'KongjingAnalysisdataForm',
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
           basicinfoid: [
              { required: true, message: '请输入基本信息 id!'},
           ],
        },
        url: {
          add: "/kongjinganalysisdata/kongjingAnalysisdata/add",
          edit: "/kongjinganalysisdata/kongjingAnalysisdata/edit",
          queryById: "/kongjinganalysisdata/kongjingAnalysisdata/queryById"
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