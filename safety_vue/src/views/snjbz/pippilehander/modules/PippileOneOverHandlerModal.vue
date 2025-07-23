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
    <pippile-one-over-handler-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit" :process="process"></pippile-one-over-handler-form>
  </j-modal>
</template>

<script>

  import PippileOneOverHandlerForm from './PippileOneOverHandlerForm'
  export default {
    name: 'PippileOneOverHandlerModal',
    components: {
      PippileOneOverHandlerForm
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
      add (record) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add(record);
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