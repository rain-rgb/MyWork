<template>
  <j-modal
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    :title="title"
    :visible="visible"
    :width="width"
    @cancel="handleCancel"
    @ok="handleOk"
    cancelText="关闭"
    switchFullscreen>
    <skip-car-device-form :disabled="disableSubmit" @ok="submitCallback" ref="realForm"></skip-car-device-form>
  </j-modal>
</template>

<script>

  import SkipCarDeviceForm from './SkipCarDeviceForm'
  export default {
    name: 'SkipCarDeviceModal',
    components: {
      SkipCarDeviceForm
    },
    data () {
      return {
        title:'',
        width:800,
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
