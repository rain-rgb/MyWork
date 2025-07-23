<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="试验室" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testroom">
              <a-input v-model="model.testroom" placeholder="请输入试验室"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验室编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testroomid">
              <a-input v-model="model.testroomid" placeholder="请输入试验室编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="危险源名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weixianname">
              <a-input v-model="model.weixianname" placeholder="请输入危险源名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="危险编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wxid">
              <a-input v-model="model.wxid" placeholder="请输入危险编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="危险级别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wxlevel">
              <a-input v-model="model.wxlevel" placeholder="请输入危险级别"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="易发生事故伤害种类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="miaoshu">
              <a-input v-model="model.miaoshu" placeholder="请输入易发生事故伤害种类"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="控制要点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="kongzhipoint">
              <a-input v-model="model.kongzhipoint" placeholder="请输入控制要点"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="责任人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zerenr">
              <a-input v-model="model.zerenr" placeholder="请输入责任人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检查周期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianchazhouqi">
              <a-input v-model="model.jianchazhouqi" placeholder="请输入检查周期"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="公司名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="company">
              <a-input v-model="model.company" placeholder="请输入公司名称"  ></a-input>
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
    name: 'SyTestroomDetailForm',
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
          add: "/testroom/syTestroomDetail/add",
          edit: "/testroom/syTestroomDetail/edit",
          queryById: "/testroom/syTestroomDetail/queryById"
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