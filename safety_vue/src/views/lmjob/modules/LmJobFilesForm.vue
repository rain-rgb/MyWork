<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">

            <a-form-model-item label="阶段类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="type1" required>
              <j-dict-select-tag placeholder="请阶段类型" v-model="model.type1"
                                 dictCode="lmjd"></j-dict-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="文件类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="filetype" required>
              <j-dict-select-tag placeholder="请选择文件类型" v-model="model.filetype"
                                 :dictCode= "model.type1" ></j-dict-select-tag>
            </a-form-model-item>
          </a-col>
              <a-col :span="24">
                 <a-form-model-item label="文件名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="filename" >
                <a-input v-model="model.filename" placeholder="文件名称"  ></a-input>
             </a-form-model-item>
           </a-col>
          <a-col :span="24">
            <a-form-model-item label="文件上传" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fileurl"  >
<!--              <j-image-upload v-model="model.fileurl" :isMultiple="isMultiple" ></j-image-upload>-->
              <a-upload style="margin-left: 20%" :action="uploadAction" :headers="headers" :default-file-list="defaultFileList" @change="handleChange1">
                <a-button  ><a-icon type="upload"/>
                  图片/文件上传
                </a-button>
              </a-upload>
            </a-form-model-item>


          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="wbsOrgCode" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wbsOrgCode">-->
<!--              <a-input v-model="model.wbsOrgCode" placeholder="请输入wbsOrgCode"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import Vue from "vue";
  import {ACCESS_TOKEN} from "@/store/mutation-types";

  export default {
    name: 'LmJobFilesForm',
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
        uploadAction: window._CONFIG['domianURL']+"/sys/common/upload",
        headers: {},
        defaultFileList:[],
        model:{
          type1:'lmjd1'
         },
        isMultiple:false,
        type1:'',
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
          add: "/lmjob/lmJobFiles/add",
          edit: "/lmjob/lmJobFiles/edit",
          queryById: "/lmjob/lmJobFiles/queryById"
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
      const token = Vue.ls.get(ACCESS_TOKEN);
      this.headers = {"X-Access-Token":token};
      console.log(this.model)

    },
    methods: {
      handleChange1({ file, fileList }) {
        if (file.status !== 'uploading') {
          this.model.fileurl=file.response.message;

        }
        // console.log(this.fileList)
      },

      add (record) {
        this.edit(record);
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