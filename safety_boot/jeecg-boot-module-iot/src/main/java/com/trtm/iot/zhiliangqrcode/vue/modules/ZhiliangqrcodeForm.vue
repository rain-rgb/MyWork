<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="uuid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="uuid">
              <a-textarea v-model="model.uuid" rows="4" placeholder="请输入uuid" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="stamppath" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="stamppath">
              <a-input v-model="model.stamppath" placeholder="请输入stamppath"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="createtime" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createtime">
              <a-input v-model="model.createtime" placeholder="请输入createtime"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="imgname" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="imgname">
              <a-input v-model="model.imgname" placeholder="请输入imgname"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="isdown" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isdown">
              <a-input v-model="model.isdown" placeholder="请输入isdown"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="number" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="number">
              <a-input-number v-model="model.number" placeholder="请输入number" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="ts" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ts">
              <a-input-number v-model="model.ts" placeholder="请输入ts" style="width: 100%" />
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
    name: 'ZhiliangqrcodeForm',
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
          add: "/zhiliangqrcode/zhiliangqrcode/add",
          edit: "/zhiliangqrcode/zhiliangqrcode/edit",
          queryById: "/zhiliangqrcode/zhiliangqrcode/queryById"
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