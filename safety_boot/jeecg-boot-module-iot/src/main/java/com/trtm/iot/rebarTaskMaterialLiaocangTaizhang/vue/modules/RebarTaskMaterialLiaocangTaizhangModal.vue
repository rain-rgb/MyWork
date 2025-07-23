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
    <rebar-task-material-liaocang-taizhang-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></rebar-task-material-liaocang-taizhang-form>
  </j-modal>
</template>

<script>

  import RebarTaskMaterialLiaocangTaizhangForm from './RebarTaskMaterialLiaocangTaizhangForm'
  export default {
    name: 'RebarTaskMaterialLiaocangTaizhangModal',
    components: {
      RebarTaskMaterialLiaocangTaizhangForm
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