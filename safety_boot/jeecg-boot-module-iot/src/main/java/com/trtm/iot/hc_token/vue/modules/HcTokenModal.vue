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
    <hc-token-form :disabled="disableSubmit" @ok="submitCallback" ref="realForm"></hc-token-form>
  </j-modal>
</template>

<script>

  import HcTokenForm from './HcTokenForm'
  export default {
    name: 'HcTokenModal',
    components: {
      HcTokenForm
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
