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
    <sy-dps-yy-yuancaijinchangdengji-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></sy-dps-yy-yuancaijinchangdengji-form>
  </j-modal>
</template>

<script>

  import SyDpsYyYuancaijinchangdengjiForm from './SyDpsYyYuancaijinchangdengjiForm'
  export default {
    name: 'SyDpsYyYuancaijinchangdengjiModal',
    components: {
      SyDpsYyYuancaijinchangdengjiForm
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