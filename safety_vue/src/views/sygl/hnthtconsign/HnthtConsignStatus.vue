<template>
  <div >
    <a-modal
      title="任务单完成"
      :width="width"
      :visible="visible2"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel2"
    >
      <a-form-item label="任务单完成情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-dict-select-tag style="width: 200px" type="list" v-model="status" dictCode="jcsystatus" placeholder="请选择"/>
      </a-form-item>

    </a-modal>
  </div>
</template>
<script>
  import {getAction,postAction,putAction} from '@api/manage';

  export default {
    components: { },
    props: ['flag','id'],
    data() {
      return {
        status:1,
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
        url: {
          edit: "/hnthtconsign/hnthtConsign/edit"
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
        putAction(this.url.edit,{
          id:this.id,
          status:this.status,
        }).then(res =>{
          if(res.success){
            this.$message.success("修改检测试验任务单状态成功")
          }else{
            this.$message.error("修改检测试验任务单状态失败")
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