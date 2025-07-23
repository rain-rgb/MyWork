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
    <hc-machine-onlinerecord-form :disabled="disableSubmit" @ok="submitCallback" ref="realForm"></hc-machine-onlinerecord-form>
  </j-modal>
</template>

<script>

  import HcMachineOnlinerecordForm from './HcMachineOnlinerecordForm'
  export default {
    name: 'HcMachineOnlinerecordModal',
    components: {
      HcMachineOnlinerecordForm
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
