<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="全球唯一码，主键" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sid">
              <a-input v-model="model.sid" placeholder="请输入全球唯一码，主键"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="孔号，和主表kongdao相同" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="holeid">
              <a-input v-model="model.holeid" placeholder="请输入孔号，和主表kongdao相同"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="孔号名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="holename">
              <a-input v-model="model.holename" placeholder="请输入孔号名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="梁名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sname">
              <a-input v-model="model.sname" placeholder="请输入梁名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeibianh">
              <a-input v-model="model.shebeibianh" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="记录时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jlsj">
              <a-input v-model="model.jlsj" placeholder="请输入记录时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="持压时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cysj">
              <a-input v-model="model.cysj" placeholder="请输入持压时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zt">
              <a-input v-model="model.zt" placeholder="请输入状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进浆压力MPa" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jjyl">
              <a-input v-model="model.jjyl" placeholder="请输入进浆压力MPa"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="返浆压力MPa" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fjyl">
              <a-input v-model="model.fjyl" placeholder="请输入返浆压力MPa"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进浆量L" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jjl">
              <a-input v-model="model.jjl" placeholder="请输入进浆量L"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="返浆量L" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fjl">
              <a-input v-model="model.fjl" placeholder="请输入返浆量L"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="环境温度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hjwd">
              <a-input v-model="model.hjwd" placeholder="请输入环境温度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="水温度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="swd">
              <a-input v-model="model.swd" placeholder="请输入水温度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="粉料温度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="flwd">
              <a-input v-model="model.flwd" placeholder="请输入粉料温度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="浆液温度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jywd">
              <a-input v-model="model.jywd" placeholder="请输入浆液温度"  ></a-input>
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
    name: 'TrYajiangSSRealdataForm',
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
           sid: [
              { required: true, message: '请输入全球唯一码，主键!'},
           ],
        },
        url: {
          add: "/yajiangssrealdata/trYajiangSSRealdata/add",
          edit: "/yajiangssrealdata/trYajiangSSRealdata/edit",
          queryById: "/yajiangssrealdata/trYajiangSSRealdata/queryById"
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