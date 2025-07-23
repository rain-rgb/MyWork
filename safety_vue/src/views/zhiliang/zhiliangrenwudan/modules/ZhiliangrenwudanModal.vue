<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :maskClosable="false"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel">
    <zhiliangrenwudan-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></zhiliangrenwudan-form>
  </j-modal>
</template>

<script>

  import ZhiliangrenwudanForm from './ZhiliangrenwudanForm'
  export default {
    name: 'ZhiliangrenwudanModal',
    components: {
      ZhiliangrenwudanForm
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
        this.$refs.realForm.handleOk();
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