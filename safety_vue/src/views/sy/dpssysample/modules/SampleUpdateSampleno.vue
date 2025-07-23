<template>
  <j-modal
    :title="title"
    :width="350"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <a-input placeholder="请输入新的样品编号" v-model="newSampleNo"></a-input>
  </j-modal>
</template>

<script>
import { getAction, postAction } from '@/api/manage'

export default {
  name: 'SampleUpdateSampleno',
  components: {},
  data() {
    return {
      title: '修改样品编号',
      visible: false,
      newSampleNo: '',
    }
  },
  methods: {
    Edit() {
      let params = {
        id: this.sample.id,
        sampleNo:this.newSampleNo
      }
      postAction('/sydpssysample/syDpsSySample/updateNo', params).then((res) => {
        if (res.success) {
          this.$message.success(res.message)
          this.routeReload()
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    routeReload() {
      //刷新页面
      this.reloadFlag = false
      let ToggleMultipage = 'ToggleMultipage'
      this.$store.dispatch(ToggleMultipage, false)
      this.$nextTick(() => {
        this.$store.dispatch(ToggleMultipage, true)
        this.reloadFlag = true
      })
    },
    handleCancel() {
      this.visible = false
    },
    handleOk() {
      this.Edit()
      this.visible = false
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>
