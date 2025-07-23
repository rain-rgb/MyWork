<template>
  <j-modal
    :title="title"
    :width="width"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
    switchFullscreen>
    <skip-car-form :disabled="disableSubmit" @ok="submitCallback" ref="realForm"></skip-car-form>
  </j-modal>
</template>

<script>

  import SkipCarForm from './SkipCarForm'
  export default {
    name: 'SkipCarModal',
    components: {
      SkipCarForm
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
