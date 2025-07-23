<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="外键、样品表id，sy_dps_sy_sample" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleid">
              <a-input v-model="model.sampleid" placeholder="请输入外键、样品表id，sy_dps_sy_sample"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审批意见" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shenpiyijian">
              <a-textarea v-model="model.shenpiyijian" rows="4" placeholder="请输入审批意见" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审批人，当前登录用户名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shenpiren">
              <a-input v-model="model.shenpiren" placeholder="请输入审批人，当前登录用户名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审批时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shenpishijian">
              <a-input v-model="model.shenpishijian" placeholder="请输入审批时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态 1：提交 2：审核通过 3：审核退回" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shenpizhuangtai">
              <a-input-number v-model="model.shenpizhuangtai" placeholder="请输入状态 1：提交 2：审核通过 3：审核退回" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="evaluateid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="evaluateid">
              <a-input v-model="model.evaluateid" placeholder="请输入evaluateid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="img" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="img">
              <a-textarea v-model="model.img" rows="4" placeholder="请输入img" />
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
    name: 'DpsSyShenpirizhiForm',
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
          add: "/sydpsshenpirizhi/dpsSyShenpirizhi/add",
          edit: "/sydpsshenpirizhi/dpsSyShenpirizhi/edit",
          queryById: "/sydpsshenpirizhi/dpsSyShenpirizhi/queryById"
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