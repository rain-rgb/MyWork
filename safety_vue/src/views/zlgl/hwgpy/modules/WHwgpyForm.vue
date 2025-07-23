<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备归属" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="customer">
              <a-input v-model="model.customer" placeholder="请输入设备归属"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="项目id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectid">
              <a-input-number v-model="model.projectid" placeholder="请输入项目id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectname">
              <a-input v-model="model.projectname" placeholder="请输入项目名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="沥青种类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="product">
              <a-input v-model="model.product" placeholder="请输入沥青种类"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="车号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="grade">
              <a-input v-model="model.grade" placeholder="请输入车号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="标段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="source">
              <a-input v-model="model.source" placeholder="请输入标段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="0代表匹配，1代表不匹配" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="result">
              <a-input-number v-model="model.result" placeholder="请输入0代表匹配，1代表不匹配" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验的时间yyyy-MM-dd HH:mm:ss" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="datetime">
              <j-date placeholder="请选择试验的时间yyyy-MM-dd HH:mm:ss" v-model="model.datetime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验种类‘1’代表普通沥青;’2’改性沥青；" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="type">
              <a-input-number v-model="model.type" placeholder="请输入试验种类‘1’代表普通沥青;’2’改性沥青；" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通过率" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="access">
              <a-input v-model="model.access" placeholder="请输入通过率"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="Sbs掺量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbsaccess">
              <a-input v-model="model.sbsaccess" placeholder="请输入Sbs掺量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="光谱图文件 指定文件格式csv" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lighfileString">
              <a-input v-model="model.lighfileString" placeholder="请输入光谱图文件 指定文件格式csv" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="isdj" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isdj">
              <a-input-number v-model="model.isdj" placeholder="请输入isdj" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="预警原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="overReason">
              <a-input v-model="model.overReason" placeholder="请输入预警原因"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审核状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="overproofStatus">
              <a-input-number v-model="model.overproofStatus" placeholder="请输入审核状态" style="width: 100%" />
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
    name: 'WHwgpyForm',
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
          add: "/whwgpy/wHwgpy/add",
          edit: "/whwgpy/wHwgpy/edit",
          queryById: "/whwgpy/wHwgpy/queryById"
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