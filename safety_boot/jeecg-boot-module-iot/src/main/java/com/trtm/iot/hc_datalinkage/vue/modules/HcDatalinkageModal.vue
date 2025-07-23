<template>
  <j-modal
    :maskClosable="false"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    :title="title"
    :visible="visible"
    :width="1200"
    @cancel="handleCancel"
    @ok="handleOk"
    switchFullscreen>
    <hc-datalinkage-form :disabled="disableSubmit" @ok="submitCallback" ref="realForm"/>
  </j-modal>
</template>

<script>

  import HcDatalinkageForm from './HcDatalinkageForm'

  export default {
    name: 'HcDatalinkageModal',
    components: {
      HcDatalinkageForm
    },
    data() {
      return {
        title:'',
        width:800,
        visible: false,
        disableSubmit: false
      }
    },
    methods:{
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

<style scoped>
</style>
