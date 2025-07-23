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
    <clxx-realdata-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></clxx-realdata-form>
  </j-modal>
</template>

<script>

  import ClxxRealdataForm from './ClxxRealdataForm'
  import { clearAll } from '@/utils/storage'
  import {pushdepartidShebei} from '@/mixins/pushdepartidShebei'
  export default {
    name: 'ClxxRealdataModal',
    mixins:[pushdepartidShebei],
    components: {
      ClxxRealdataForm
    },
    data () {
      return {
        title:'',
        width:1400,
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
      detail (record) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.detail(record);
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
        //this.routeReload();
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
<style scoped>
>>>.ant-modal-body{
  padding: 5px;
}
</style>