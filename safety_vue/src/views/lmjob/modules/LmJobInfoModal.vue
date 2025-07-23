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
    <lm-job-info-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></lm-job-info-form>
  </j-modal>
</template>

<script>

  import LmJobInfoForm from './LmJobInfoForm'
  import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
  export default {
    name: 'LmJobInfoModal',
    components: {
      LmJobInfoForm,
      JSelectDqProjName
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