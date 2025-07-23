<template>
  <j-modal
    :title="title"
    width="100%"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <shigongphb-forms ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></shigongphb-forms>
  </j-modal>
</template>

<script>

  import ShigongphbForms from './ShigongphbForms'
  export default {
    name: 'ShigongphbModal',
    components: {
      ShigongphbForms
    },
    data () {
      return {
        title:'',
        //width:100%,
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
      editAdd (record) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.editAdd(record);
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