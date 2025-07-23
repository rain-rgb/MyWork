<template>
  <div >
    <a-modal
      title="驳回"
      :width="width"
      :visible="visible1"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel2"
    >
      <a-form-item label="驳回原因" :labelCol="labelCol" :wrapperCol="wrapperCol" centered>
        <a-input placeholder="请输入驳回原因" v-model="spyj"></a-input>
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
        type:0,
        flag:3,
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
        getAction(this.url.add,{
            remark:this.spyj,
            baseid:this.syjid,
            overproofStatus:30,
            flag:this.flag
          }).then(res =>{
            if(res.success){
              this.$message.success("驳回成功")
            }else{
              this.$message.error("驳回失败")
            }
            this.confirmLoading = false;
            this.visible1 = false;
            const demo = 0;
            this.$emit('change', demo);
            this.spyj = '';
            this.spjg = '';
            this.fileList = [];
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