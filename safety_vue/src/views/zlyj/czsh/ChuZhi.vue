<template>
  <div >
    <a-modal
      title="审核"
      :width="width"
      :visible="visible2"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel2"
    >
      <a-form-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol" centered>
        <a-input placeholder="请输入问题原因" v-model="wtyy"></a-input>
      </a-form-item>
      <a-form-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-input placeholder="请输入处理方式" v-model="clfs"></a-input>
      </a-form-item>
      <a-form-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-input placeholder="请输入处理结果" v-model="cljg"></a-input>
      </a-form-item>
      <a-form-item label="上传文件/图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-image-upload v-model="fileList" :isMultiple="isMultiple" ></j-image-upload>
      </a-form-item>
    </a-modal>
  </div>
</template>
<script>
  import {getAction,postAction} from "@api/manage";
  import JImageUpload from '@comp/jeecg/JImageUpload';
  export default {
    components: { JImageUpload },
    data() {
      return {
        fileList:[],
        cljg:'',
        wtyy:'',
        clfs:'',
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
        visible2: false,
        confirmLoading: false,
        previewVisible: false,
        previewImage: '',
        syjid:'',
        type:0,
        url: {
          add: "/zhanglam/zhanglaYajiangOverHandler/addOverHander",
          addyaj:'/zhanglam/zhanglaYajiangOverHandler/addOverHanderYaJ',
        }
      };
    },
    mounted() {
    },
    methods: {
      showModal(syjid,i) {
        this.type=i;
        this.syjid=syjid;
        this.visible2 = true;
      },
      subcommit(){
        if(this.type===1){
          if(this.fileList.length>0){
            postAction(this.url.add,{
              handleResult:this.cljg,
              problemReasons:this.wtyy,
              handleWay:this.clfs,
              overproofStatus:10,
              baseid:this.syjid,
              filePath2:this.fileList,
            }).then(res =>{
              if(res.success){
                this.$message.success("处置成功")
              }else{
                this.$message.error("处置失败")
              }
              this.confirmLoading = false;
              this.visible2 = false;
              const demo = 0;
              this.$emit('change', demo);
              this.cljg = '';
              this.wtyy = '';
              this.clfs = '';
              this.fileList = [];
            })
          }else{
            postAction(this.url.add,{
              handleResult:this.cljg,
              problemReasons:this.wtyy,
              handleWay:this.clfs,
              overproofStatus:10,
              baseid:this.syjid,
            }).then(res =>{
              if(res.success){
                this.$message.success("处置成功")
              }else{
                this.$message.error("处置失败")
              }
              this.confirmLoading = false;
              this.visible2 = false;
              const demo = 0;
              this.$emit('change', demo);
              this.cljg = '';
              this.wtyy = '';
              this.clfs = '';
              this.fileList = [];
            })
          }

        }else if(this.type===2){
          if(this.fileList.length>0){
            postAction(this.url.addyaj,{
              handleResult:this.cljg,
              problemReasons:this.wtyy,
              handleWay:this.clfs,
              overproofStatus:10,
              baseid:this.syjid,
              filePath2:this.fileList,
            }).then(res =>{
              if(res.success){
                this.$message.success("处置成功")
              }else{
                this.$message.error("处置失败")
              }
              this.confirmLoading = false;
              this.visible2 = false;
              const demo = 0;
              this.$emit('change', demo);
              this.cljg = '';
              this.wtyy = '';
              this.clfs = '';
              this.fileList = [];
            })
          }else{
            postAction(this.url.addyaj,{
              handleResult:this.cljg,
              problemReasons:this.wtyy,
              handleWay:this.clfs,
              overproofStatus:10,
              baseid:this.syjid,
            }).then(res =>{
              if(res.success){
                this.$message.success("处置成功")
              }else{
                this.$message.error("处置失败")
              }
              this.confirmLoading = false;
              this.visible2 = false;
              const demo = 0;
              this.$emit('change', demo);
              this.cljg = '';
              this.wtyy = '';
              this.clfs = '';
              this.fileList = [];
            })
          }

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
        this.visible2 = false;
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