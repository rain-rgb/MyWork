<template>
  <div >
    <a-modal
      title="取样照片上传"
      :width="width"
      :visible="visible2"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel2"
    >

      <a-form-item label="上传图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-image-upload :isMultiple="isMultiple" v-model="fileList" ></j-image-upload>
      </a-form-item>

    </a-modal>
  </div>
</template>
<script>
  import {getAction,postAction,putAction} from "../../../api/manage";
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
    props: ['flag','jinchangshijian','id'],
    data() {
      return {
        fileList:[],
        isquyang:1,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        isMultiple:true,
        width:1200,
        visible2: false,
        confirmLoading: false,
        url: {
          add: "/wzquyangsy/wzquyangsy/add1",
          edit: "/wztaizhang/wztaizhang/edit"
        }
      };
    },
    watch: {
      flag(n) {
        n===1 ? this.visible2 = true : this.visible2 = false}
    },
    mounted() {
      if(this.flag === 1) {
        this.visible2 = true
      }
    },
    methods: {
      showModal() {
        this.visible = true;
      },
      subcommit(){
        this.changeStatus();
        postAction(this.url.add,{
          taizhangid:this.id,
          jinchangshijian:this.jinchangshijian,
          qypic:this.fileList.split(",")[0],
          gzpic:this.fileList.split(",")[1],
          jlpic:this.fileList.split(",")[2],
          pzpic:this.fileList.split(",")[3],
          sy1pic:this.fileList.split(",")[4],
          sy2pic:this.fileList.split(",")[5],
          shouyang1pic:this.fileList.split(",")[6],
          shouyang2pic:this.fileList.split(",")[7],
        }).then(res =>{
          if(res.success){
            this.$message.success("添加取样图片成功")
          }else{
            this.$message.error("添加取样图片失败")
          }
          this.confirmLoading = false;
          this.visible2 = false;
          const demo = 0;
          this.$emit('change', demo);
        })
        this.routeReload();
      },
      changeStatus(){
        let param = {id: this.id, isquyang: 1}
        putAction(this.url.edit, param).then(res =>{
          if(res.success){
            this.$message.success("修改取样状态成功")
          }
        })
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