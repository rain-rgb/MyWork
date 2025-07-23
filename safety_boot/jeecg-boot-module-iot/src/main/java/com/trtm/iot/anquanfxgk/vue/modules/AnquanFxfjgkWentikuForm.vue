<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="code">
              <a-input v-model="model.code" placeholder="请输入编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="问题类别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="questionType">
              <a-input v-model="model.questionType" placeholder="请输入问题类别"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="工程类别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="engineeringType">
              <a-input v-model="model.engineeringType" placeholder="请输入工程类别"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工工序" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="constructionProcess">
              <a-input v-model="model.constructionProcess" placeholder="请输入施工工序"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="安全风险项" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="securityRisk">
              <a-input v-model="model.securityRisk" placeholder="请输入安全风险项"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="安全风险项内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="securityRiskContent">
              <a-input v-model="model.securityRiskContent" placeholder="请输入安全风险项内容"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="风险事件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="riskEvent">
              <a-input v-model="model.riskEvent" placeholder="请输入风险事件"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="安全风险问题点描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="desc">
              <a-input v-model="model.desc" placeholder="请输入安全风险问题点描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="value">
              <a-input v-model="model.value" placeholder="请输入分值"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remarks">
              <a-input v-model="model.remarks" placeholder="请输入备注"  ></a-input>
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
    name: 'AnquanFxfjgkWentikuForm',
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
          add: "/anquanfxgk/anquanFxfjgkWentiku/add",
          edit: "/anquanfxgk/anquanFxfjgkWentiku/edit",
          queryById: "/anquanfxgk/anquanFxfjgkWentiku/queryById"
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