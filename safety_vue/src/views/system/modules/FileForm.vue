<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="所属组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <JselectDqDepart v-decorator="['sysOrgCode']" ::multi="false"  />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="文件名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="name" placeholder="请输入文件名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-upload style="margin-left: 20%" :action="uploadAction" :headers="headers" :default-file-list="defaultFileList" @change="handleChange">
              <a-button  ><a-icon type="upload"/>
                文件上传
              </a-button>
            </a-upload>
          </a-col>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>
import JUpload from '@/components/jeecg/JUpload'
import { httpAction, getAction ,postAction} from '@/api/manage'
import pick from 'lodash.pick'
import JFormContainer from '@/components/jeecg/JFormContainer'
import JDate from '@/components/jeecg/JDate'
import JSelectDepart from '@/components/jeecgbiz/JSelectDepart'//所有的用户的组织机构权限
import JselectDqDepart from '@/components/jeecgbiz/JselectDqDepart'//当前用户的组织机构权限（当前使用）
import JDictSelectTag from '@/components/dict/JDictSelectTag'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import Vue from 'vue'

export default {
  name: 'FileForm',
  components: {
    JFormContainer,
    JDate,
    JSelectDepart,
    JDictSelectTag,
    JselectDqDepart,
    JUpload
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {
      },
      required: false
    },
    //表单模式：true流程表单 false普通表单
    formBpm: {
      type: Boolean,
      default: false,
      required: false
    },
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data() {
    return {
      uploadAction:window._CONFIG['domianURL']+"/sys/common/upload",
      name:'',
      fileList:'',
      defaultFileList:[],
      headers:{},
      form: this.$form.createForm(this),
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {},
      url: {
        upload: '/sys/oss/file/add',
      }
    }
  },

  computed: {
    formDisabled(){
      if(this.formBpm===true){
        if(this.formData.disabled===false){
          return false
        }
        return true
      }
      return this.disabled
    },
    showFlowSubmitButton(){
      if(this.formBpm===true){
        if(this.formData.disabled===false){
          return true
        }
      }
      return false
    }
  },
  created() {
      const token = Vue.ls.get(ACCESS_TOKEN);
      this.headers = {"X-Access-Token":token};
  },
  methods: {
    handleChange({ file, fileList }) {
      if (file.status !== 'uploading') {
        this.fileList=file.response.message;
      }
      console.log(this.fileList)
    },
    submitForm:function () {
      const that = this;
      let params={name:this.name,url:this.fileList}
      getAction(this.url.upload,params).then(res=>{
        if(res.success){
          that.$message.success(res.message);
          that.$emit('ok');
        }else{
          that.$message.warning(res.message);
        }

      })
    },
    add () {
      this.edit({});
    },
    edit (record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.visible = true;
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.model,'sysOrgCode','sbtype','sbname','sbjsimplename','interfacetype'))
      })
    },
  }
}
</script>