<template>
  <j-modal
    :title="title"
    width="95%"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <ShigongphbForm1 ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></ShigongphbForm1>
  </j-modal>
</template>

<script>

  import ShigongphbForm1 from './ShigongphbForm1'
  export default {
    name: 'ShigongphbModal1',
    components: {
      ShigongphbForm1
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