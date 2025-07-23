<template>
  <div >
    <a-modal
      title="驳回"
      :width="width"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel2">

      <a-form-item label="驳回原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-input placeholder="请输入驳回原因" v-model="remark"></a-input>
      </a-form-item>
    </a-modal>
  </div>
</template>
<script>
import { getAction, putAction } from '../../../api/manage'
  import JImageUpload from '../../../components/jeecg/JImageUpload';

  // function getBase64(file) {
  //   return new Promise((resolve, reject) => {
  //     const reader = new FileReader();
  //     reader.readAsDataURL(file);
  //     reader.onload = () => resolve(reader.result);
  //     reader.onerror = error => reject(error);
  //   });
  // }
  export default {
    components: { JImageUpload },
    props: ['flag','hntbatch','bhz'],
    data() {
      return {
        remark:'',
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        width:800,
        visible: false,
        confirmLoading: false,
        previewVisible: false,
        previewImage: '',
        url: {
          add: "/czsh/bhzCementOverHandler/bohuiedit",
        }
      };
    },
    watch: {
      flag(n) {
        n===3 ? this.visible = true : this.visible = false}
    },
    mounted() {
      if(this.flag === 3) {
        this.visible = true
      }
    },
    methods: {
      showModal() {
        this.visible = true;
      },
      subcommit(){
        getAction(this.url.add,{
          remark:this.remark,
          baseid:this.hntbatch,
          bhz:this.bhz
        }).then(res =>{
          if(res.success){
            this.$message.success("驳回成功")
          }else{
            this.$message.error("驳回失败")
          }
          this.confirmLoading = false;
          this.visible = false;
          const demo = 0;
          this.$emit('change', demo, true);
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
        this.visible2 = false;
        const demo = 0
        this.$emit('change', demo, false)
      },
      // handleCancel() {
      //   this.previewVisible = false;
      // },
      // async handlePreview(file) {
      //   if (!file.url && !file.preview) {
      //     file.preview = await getBase64(file.originFileObj);
      //   }
      //   this.previewImage = file.url || file.preview;
      //   this.previewVisible = true;
      // },
      // handleChange({ fileList }) {
      //   this.fileList = fileList;
      // },
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