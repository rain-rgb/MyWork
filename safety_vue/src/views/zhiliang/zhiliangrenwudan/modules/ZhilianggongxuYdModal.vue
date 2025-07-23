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
    <zhilianggongxu-yd-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></zhilianggongxu-yd-form>
  </j-modal>
</template>

<script>

  // import ZhilianggongxuForm from './ZhilianggongxuForm'
  import ZhilianggongxuYdFormForm from '@views/zhiliang/zhiliangrenwudan/modules/ZhilianggongxuForm'
  import ZhilianggongxuYdForm from '@views/zhiliang/zhiliangrenwudan/modules/ZhilianggongxuYdForm'
  export default {
    name: 'ZhilianggongxuYdModal',
    components: {
      ZhilianggongxuYdForm
    },
    data () {
      return {
        title:'',
        width:1500,
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