<template>
  <div >
    <a-modal
      title="审核"
      :width="width"
      :visible="visible1"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel2"
    >

      <a-form-item label="审批意见" :labelCol="labelCol" :wrapperCol="wrapperCol" centered>
        <a-input placeholder="请输入审批意见" v-model="spyj"></a-input>
      </a-form-item>
      <a-form-item label="审批结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-input placeholder="无" v-model="spjg"></a-input>
      </a-form-item>
      <a-form-item label="上传文件/图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-image-upload v-model="fileList" :isMultiple="isMultiple" ></j-image-upload>
      </a-form-item>
<!--      <div class="clearfix">-->
<!--        <a-upload-->
<!--          action="https://www.mocky.io/v2/5cc8019d300000980a055e76"-->
<!--          list-type="picture-card"-->
<!--          @preview="handlePreview"-->
<!--          @change="handleChange"-->
<!--        >-->
<!--          <div v-if="fileList.length < 4">-->
<!--            <a-icon type="plus" />-->
<!--              <div class="ant-upload-text">-->
<!--              上传图片-->
<!--            </div>-->
<!--          </div>-->
<!--        </a-upload>-->
<!--        <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">-->
<!--          <img alt="example" style="width: 100%" :src="previewImage" />-->
<!--        </a-modal>-->
<!--      </div>-->
    </a-modal>
  </div>
</template>
<script>
  import {getAction} from "../../../api/manage";
  import JImageUpload from '../../../components/jeecg/JImageUpload';

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
    props: ['flag','hntbatch','bhz'],
    data() {
      return {
        fileList:[],
        spyj:'',
        spjg:'',
        tuPian:'',
        isMultiple : true,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        width:800,
        visible1: false,
        confirmLoading: false,
        previewVisible: false,
        previewImage: '',
        url: {
          add: "/czsh/bhzCementOverHandler/SHAddOrUpdate",
        }
      };
    },
    watch: {
      flag(n) {
        n===1 ? this.visible1 = true : this.visible1 = false}
    },
    mounted() {
      if(this.flag === 1) {
        this.visible1 = true
      }
    },
    methods: {
      showModal() {
        this.visible = true;
      },
      subcommit(){
        getAction(this.url.add,{
          spyj:this.spyj,
          spjg:this.spjg,
          hntbatch:this.hntbatch,
          bhz:this.bhz,
          fileList:this.fileList,
        }).then(res =>{
          if(res.success){
            this.$message.success("审核成功")

          }else{
            this.$message.error("审核失败")
          }
          this.confirmLoading = false;
          this.visible1 = false;
          const demo = 0;
          this.$emit('change', demo, true);
          this.spyj = '';
          this.spjg = '';
          this.fileList = [];
        })
        // this.routeReload();
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
        this.visible1 = false;
        const demo = 0
        this.$emit('change', demo, false)
      },
      handleCancel() {
        this.previewVisible = false;
      },
      async handlePreview(file) {
        if (!file.url && !file.preview) {
          file.preview = await getBase64(file.originFileObj);
        }
        this.previewImage = file.url || file.preview;
        this.previewVisible = true;
      },
      handleChange({ fileList }) {
        this.fileList = fileList;
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