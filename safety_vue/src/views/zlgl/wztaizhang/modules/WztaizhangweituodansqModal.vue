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
    <wztaizhangweituodansq-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></wztaizhangweituodansq-form>
  </j-modal>
</template>

<script>

  import WztaizhangweituodansqForm from './WztaizhangweituodansqForm'
  export default {
    name: 'WztaizhangweituodansqModal',
    components: {
      WztaizhangweituodansqForm
    },
    data () {
      return {
        title:'',
        width:1200,
        visible: false,
        disableSubmit: false
      }
    },
    methods: {
      add (param) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add(param);
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