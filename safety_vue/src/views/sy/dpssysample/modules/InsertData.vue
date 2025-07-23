<template>
  <j-modal
    centered
    :title="title"
    :width="1000"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <sample-insert-data ref="realForm" @ok="submitCallback"></sample-insert-data>

  </j-modal>
</template>
<script>
import { getAction } from '@/api/manage'
import SampleInsertData from '@views/sy/dpssysample/modules/SampleInsertData'

export default {
  name: 'InsertData',
  components: {
    SampleInsertData,
  },
  data() {
    return {
      visible: false,
      title: '插入',
    }
  },
  mounted() {
  },
  computed: {},
  methods: {

    // getComponent(name) {
    //   if (this.componentMap[name]) {
    //     return this.componentMap[name]
    //   }
    //   const syncImport = () => import(`@/views/syrj/reportform/form/${name}.vue`)
    //   this.componentMap[name] = syncImport
    //   return syncImport
    // },
    handleCancel() {
      this.visible = false
    },
    handleOk() {
      this.$refs.realForm.submitForm();
    },
    edit (record) {
      this.visible=true
      this.$nextTick(()=>{
        this.$refs.realForm.edit(record);
      })
    },
    submitCallback(){
      this.$emit('ok');
      this.visible = false;
    },
  },
}
</script>