<template>
  <div >
    <a-modal
      title="添加报告"
      :width="width"
      :visible="visible2"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel2"
    >
      <a-form-item label="报告结论" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-dict-select-tag style="width: 200px" type="list" v-model="status" dictCode="baogaostatus" placeholder="请选择"/>
      </a-form-item>
      <a-form-item label="鉴定附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-upload style="width: 200px" :action="uploadAction" :headers="headers" :default-file-list="defaultFileList" @change="handleChange1">
          <a-button><a-icon type="upload"/>
            添加附件
          </a-button>
        </a-upload>
      </a-form-item>

    </a-modal>
  </div>
</template>
<script>
  import {getAction,postAction,putAction} from "../../../api/manage";
  import JImageUpload from '../../../components/jeecg/JImageUpload';
  import { ACCESS_TOKEN } from '@/store/mutation-types'
  import Vue from 'vue'

  const token = Vue.ls.get(ACCESS_TOKEN);
  function getBase64(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = error => reject(error);
    });
  }
  export default {
    components: { JImageUpload },
    props: ['flag','id'],
    data() {
      return {
        uploadAction:window._CONFIG['domianURL']+"/sys/common/upload",
        headers:{"X-Access-Token":token},
        defaultFileList:[],
        status:1,
        fileList:[],
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        isMultiple:true,
        width:800,
        visible2: false,
        confirmLoading: false,
        url: {
          edit: "/wztaizhang/wztaizhang/edit"
        }
      };
    },
    watch: {
      flag(n) {
        n===2 ? this.visible2 = true : this.visible2 = false}
    },
    mounted() {
      if(this.flag === 2) {
        this.visible2 = true
      }
    },
    methods: {
      showModal() {
        this.visible = true;
      },
      handleChange1({ file, fileList }) {
        if (file.status !== 'uploading') {
          this.fileList=file.response.message;
          //console.log(this.model.filePath,"this.model.filePath")
        }
        console.log(this.fileList)
      },
      subcommit(){
        putAction(this.url.edit,{
          id:this.id,
          jianyanstate:this.status,
          baogaofile:this.fileList
        }).then(res =>{
          if(res.success){
            this.$message.success("添加报告成功")
          }else{
            this.$message.error("添加报告失败")
          }
          this.confirmLoading = false;
          this.visible2 = false;
          const demo = 0;
          this.$emit('change', demo);
        })
        this.routeReload();
      },
      routeReload() {//刷新页面
        this.reloadFlag = false
        let ToggleMultipage = 'ToggleMultipage'
        this.$store.dispatch(ToggleMultipage, false)
        this.$nextTick(() => {
          this.$store.dispatch(ToggleMultipage, true)
          this.reloadFlag = true
        })
        console.log("刷新页面")
      },
      handleOk(e) {
        // this.visible1=false;
        this.confirmLoading = true;
        // formData.img = that.fileList;
        setTimeout(() => {
          this.subcommit();
        }, 2000);
      },

      handleCancel2(e) {
        this.visible2 = false;
        const demo = 0
        this.$emit('change', demo)
      },
    },
  };
</script>
<style>
  .ant-upload-picture-card-wrapper{
    display:flex !important;
    flex-wrap: wrap;
    padding: 0 150px;
  }
  .ant-upload-picture-card-wrapper::before{
    display: block;
  }

  .ant-upload.ant-upload-select-picture-card{
    display: block;
  }
  .ant-upload-select-picture-card i {
    font-size: 32px;
    color: #999;
  }

  .ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
  }
  .ant-upload-list{
    display: flex;
    flex-wrap: wrap;
  }


  .ant-upload.ant-upload-select-picture-card > .ant-upload{
    display: block !important;
    margin-top: 15px;
  }
</style>