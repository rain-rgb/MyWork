<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirm-loading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <cunliang-procedure-config-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></cunliang-procedure-config-form>
  </j-modal>
</template>

<script>

  import CunliangProcedureConfigForm from './CunliangProcedureConfigForm'
  export default {
    name: 'CunliangProcedureConfigModal',
    components: {
      CunliangProcedureConfigForm
    },
    data () {
      return {
        confirmLoading: false,
        title:'',
        width:1200,
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