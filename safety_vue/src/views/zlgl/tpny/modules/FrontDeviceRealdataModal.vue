<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <front-device-realdata-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></front-device-realdata-form>
  </j-modal>
</template>

<script>

  import FrontDeviceRealdataForm from './FrontDeviceRealdataForm'
  export default {
    name: 'FrontDeviceRealdataModal',
    components: {
      FrontDeviceRealdataForm
    },
    data () {
      return {
        title:'',
        width:1400,
        visible: false,
        disableSubmit: false
      }
    },
    methods: {
      add () {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add();
        })
      },
      edit (record) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.edit(record);
        })
      },
      detail (record) {
        console.log("进入")
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.detail(record);
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        this.$refs.realForm.submitForm();
      },
      submitCallback(){
        this.$emit('ok');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      }
    }
  }
</script>