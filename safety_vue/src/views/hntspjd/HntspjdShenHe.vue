<template>
  <div>
    <a-modal
      title="审核"
      :width="width"
      :visible="visible1"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel2">
      <a-form-item label="审批结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-dict-select-tag style="width: 200px" type="list" v-model="status" dictCode="hntspjdstatus" placeholder="请选择"/>
      </a-form-item>
      <a-form-item label="审批意见" :labelCol="labelCol" :wrapperCol="wrapperCol" centered>
        <a-input placeholder="请输入审批意见" v-model="beizhu"></a-input>
      </a-form-item>
    </a-modal>
  </div>
</template>
<script>
  import {getAction,putAction} from "@api/manage";
  function getBase64(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = error => reject(error);
    });
  }
  export default {
    name:'HntspjdShenHe',
    props: ['flag','id'],
    data() {
      return {
        beizhu:'',
        status:1,
        fileList:[],
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
          edit:"/hntspjd/shigongphbTappraisal/edit",
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
        let param={id:this.id,status:this.status,beizhu:this.beizhu}
        putAction(this.url.edit,param).then(res =>{
          if(res.success){
            this.$message.success("审核成功")
          }else{
            this.$message.error("审核失败")
          }
          this.confirmLoading = false;
          this.visible1 = false;
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
        }, 500);
      },

      handleCancel2(e) {
        this.visible1 = false;
        const demo = 0
        this.$emit('change', demo)
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