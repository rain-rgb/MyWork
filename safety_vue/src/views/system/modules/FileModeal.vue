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
    <FileForm ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></FileForm>
  </j-modal>
</template>

<script>

import FileForm  from '@views/system/modules/FileForm'
export default {
  name: 'FileModeal',
  components: {
    FileForm
  },
  data () {
    return {
      title:'',
      width:500,
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