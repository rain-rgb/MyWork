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
    <mixpile-one-over-handler-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit" :process="process"></mixpile-one-over-handler-form>
  </j-modal>
</template>

<script>

  import MixpileOneOverHandlerForm from './MixpileOneOverHandlerForm'
  export default {
    name: 'MixpileOneOverHandlerModal',
    components: {
      MixpileOneOverHandlerForm
    },
    data () {
      return {
        title:'',
        width:800,
        visible: false,
        process:0,
        disableSubmit: false
      }
    },
    methods: {
      add (recode) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add(recode);
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