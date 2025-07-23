<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projName">
              <a-input v-model="model.projName" placeholder="请输入项目名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测点名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cedName">
              <a-input v-model="model.cedName" placeholder="请输入测点名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监测类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jcTypeName">
              <a-input v-model="model.jcTypeName" placeholder="请输入监测类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分组名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fenzName">
              <a-input v-model="model.fenzName" placeholder="请输入分组名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测点id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cedId">
              <a-input-number v-model="model.cedId" placeholder="请输入测点id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测点key" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cedKey">
              <a-input v-model="model.cedKey" placeholder="请输入测点key"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监测类型编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jcTypeCode">
              <a-input v-model="model.jcTypeCode" placeholder="请输入监测类型编码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="datatime">
              <j-date placeholder="请选择数据时间" v-model="model.datatime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="上传数据" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="datajson">
              <a-input v-model="model.datajson" placeholder="请输入上传数据"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测点状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cedStatus">
              <a-input v-model="model.cedStatus" placeholder="请输入测点状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据来源" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dataSource">
              <a-input v-model="model.dataSource" placeholder="请输入数据来源"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测点缩略图" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cedPic">
              <a-input v-model="model.cedPic" placeholder="请输入测点缩略图"  ></a-input>
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
    name: 'CofferdamHisdataForm',
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
          add: "/cofferdam/cofferdamHisdata/add",
          edit: "/cofferdam/cofferdamHisdata/edit",
          queryById: "/cofferdam/cofferdamHisdata/queryById"
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