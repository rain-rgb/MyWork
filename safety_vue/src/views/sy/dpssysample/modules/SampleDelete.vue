<template>
  <j-modal
    :title="title"
    :width="350"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <div>
      确定要删除所选样品数据吗？
    </div>
  </j-modal>
</template>

<script>
import { getAction, postAction } from '@/api/manage'

export default {
  name: 'SampleDelete',
  components: {},
  data() {
    return {
      title: '删除样品',
      visible: false,
    }
  },
  methods: {
    del() {
      let url = '/sydpssysample/syDpsSySample/del/' + this.sample.id
      getAction(url).then((res) => {
        if (res.success) {
          this.$message.success(res.message)
          this.routeReload()
        } else {
          that.$message.warning(res.message)
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
      this.del()
      this.visible = false
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>
