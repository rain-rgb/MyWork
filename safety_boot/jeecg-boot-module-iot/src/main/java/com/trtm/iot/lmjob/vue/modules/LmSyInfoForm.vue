<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="试验类型：施工检测；监理检测；业主检测；竣工检测" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="syType">
              <a-input v-model="model.syType" placeholder="请输入试验类型：施工检测；监理检测；业主检测；竣工检测"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="结论描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="resultinfo">
              <a-input v-model="model.resultinfo" placeholder="请输入结论描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="0：合格；1不合格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reslut">
              <a-input-number v-model="model.reslut" placeholder="请输入0：合格；1不合格" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jctime">
              <j-date placeholder="请选择试验日期" v-model="model.jctime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="部位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="usewbs">
              <a-input v-model="model.usewbs" placeholder="请输入部位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="附件上传" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="file">
              <a-input v-model="model.file" placeholder="请输入附件上传"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="闭合状态：0：未闭合；20已闭合" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bihe">
              <a-input v-model="model.bihe" placeholder="请输入闭合状态：0：未闭合；20已闭合"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="syname">
              <a-input v-model="model.syname" placeholder="请输入试验名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="beizhu" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beizhu">
              <a-input v-model="model.beizhu" placeholder="请输入beizhu"  ></a-input>
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
    name: 'LmSyInfoForm',
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
          add: "/lmjob/lmSyInfo/add",
          edit: "/lmjob/lmSyInfo/edit",
          queryById: "/lmjob/lmSyInfo/queryById"
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