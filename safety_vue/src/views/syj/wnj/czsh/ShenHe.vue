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
    </a-modal>
  </div>
</template>
<script>
  import {getAction,postAction} from "@api/manage";
  import JImageUpload from '../../../../components/jeecg/JImageUpload';
  export default {
    components: { JImageUpload },
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
        syjid:'',
        flag:1,
        type:0,
        url: {
          add: "/syjoverhandler/syjOverHandler/deallist",
        }
      };
    },

    methods: {
      showModal(e,i) {
        this.type=i;
        this.syjid=e;
        this.visible1 = true;
      },
      subcommit(){
          if(this.fileList.length>0){
            getAction(this.url.add,{
              supervisorApproval:this.spyj,
              supervisorResult:this.spjg,
              baseid:this.syjid,
              overproofStatus:20,
              filePath:this.fileList,
              flag:this.flag
            }).then(res =>{
              if(res.success){
                this.$message.success("审核成功")
              }else{
                this.$message.error("审核失败")
              }
              this.confirmLoading = false;
              this.visible1 = false;
              const demo = 0;
              this.$emit('change', demo);
              this.spyj = '';
              this.spjg = '';
              this.fileList = [];
            })
          }else{
            getAction(this.url.add,{
              supervisorApproval:this.spyj,
              supervisorResult:this.spjg,
              baseid:this.syjid,
              overproofStatus:20,
              flag:this.flag
            }).then(res =>{
              if(res.success){
                this.$message.success("审核成功")
              }else{
                this.$message.error("审核失败")
              }
              this.confirmLoading = false;
              this.visible1 = false;
              const demo = 0;
              this.$emit('change', demo);
              this.spyj = '';
              this.spjg = '';
              this.fileList = [];
            })
          }
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
        this.visible1 = false;
        const demo = 0
        this.$emit('change', demo)
      },
      handleCancel() {
        this.previewVisible = false;
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