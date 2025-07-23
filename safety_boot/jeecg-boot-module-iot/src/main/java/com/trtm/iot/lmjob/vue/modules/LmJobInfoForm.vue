<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="桩号里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhlc">
              <a-input v-model="model.zhlc" placeholder="请输入桩号里程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="路面结构类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wbstype">
              <a-input v-model="model.wbstype" placeholder="请输入路面结构类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分部分项code" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wbsOrgCode">
              <a-input v-model="model.wbsOrgCode" placeholder="请输入分部分项code"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="底基层混合料总方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="djcInfo1">
              <a-input v-model="model.djcInfo1" placeholder="请输入底基层混合料总方量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="基层混合料总方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jcInfo1">
              <a-input v-model="model.jcInfo1" placeholder="请输入基层混合料总方量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="底基层供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="djcInfo2">
              <a-input v-model="model.djcInfo2" placeholder="请输入底基层供应商"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="基层供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jcInfo2">
              <a-input v-model="model.jcInfo2" placeholder="请输入基层供应商"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="下面层混合料总方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xmcInfo1">
              <a-input v-model="model.xmcInfo1" placeholder="请输入下面层混合料总方量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="下面层供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xmcInfo2">
              <a-input v-model="model.xmcInfo2" placeholder="请输入下面层供应商"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="中面层混合料总方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zmcInfo1">
              <a-input v-model="model.zmcInfo1" placeholder="请输入中面层混合料总方量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="中面层供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zmcInfo2">
              <a-input v-model="model.zmcInfo2" placeholder="请输入中面层供应商"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="上面层混合料总方量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="smcInfo1">
              <a-input v-model="model.smcInfo1" placeholder="请输入上面层混合料总方量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="上面层供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="smcInfo2">
              <a-input v-model="model.smcInfo2" placeholder="请输入上面层供应商"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="文件数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="files">
              <a-input-number v-model="model.files" placeholder="请输入文件数量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="guid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guid">
              <a-input v-model="model.guid" placeholder="请输入guid"  ></a-input>
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
    name: 'LmJobInfoForm',
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
          add: "/lmjob/lmJobInfo/add",
          edit: "/lmjob/lmJobInfo/edit",
          queryById: "/lmjob/lmJobInfo/queryById"
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