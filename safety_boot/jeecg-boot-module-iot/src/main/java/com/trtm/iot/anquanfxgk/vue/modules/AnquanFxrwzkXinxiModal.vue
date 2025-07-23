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
    <anquan-fxrwzk-xinxi-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></anquan-fxrwzk-xinxi-form>
  </j-modal>
</template>

<script>

  import AnquanFxrwzkXinxiForm from './AnquanFxrwzkXinxiForm'
  export default {
    name: 'AnquanFxrwzkXinxiModal',
    components: {
      AnquanFxrwzkXinxiForm
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