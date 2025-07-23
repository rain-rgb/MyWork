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
    <device-mixpile-historydata-one-h-b-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></device-mixpile-historydata-one-h-b-form>
  </j-modal>
</template>

<script>

  import DeviceMixpileHistorydataOneHBForm from './DeviceMixpileHistorydataOneHBForm'
  export default {
    name: 'DeviceMixpileHistorydataOneHBModal',
    components: {
      DeviceMixpileHistorydataOneHBForm
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