<template>
  <j-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel">
    <tr-maoxiayuyingli-m-form1 ref="realForm" @ok="submitCallback" :disabled="disableSubmit"/>
  </j-modal>
</template>

<script>

import TrMaoxiayuyingliMForm1 from './TrMaoxiayuyingliMForm1'

export default {
  name: 'TrMaoxiayuyingliMModal1',
  components: {
    TrMaoxiayuyingliMForm1
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