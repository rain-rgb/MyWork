<template>
  <j-modal
    :title="title"
    :width="350"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <j-date v-model="time" placeholder="请选择取样日期" :show-time="true"
            dateFormat="YYYY-MM-DD"/>
  </j-modal>
</template>

<script>
import { getAction } from '@/api/manage'

export default {
  name: 'SampleCopyDate',
  components: {},
  data() {
    return {
      title: '取样日期',
      visible: false,
      id: '',
      time: '',
      type: '',
    }
  },
  methods: {
    CYList() {

      // let params = {
      //   id: this.sample.id,
      //   sampleDate: this.time,
      //   type: this.sample.titType
      // }
      let url = '/sydpssysample/syDpsSySample/copy' + '/' + this.sample.id + '/' + this.time + '/' + this.sample.titType
      getAction(url).then((res) => {
        if (res.success) {
          this.routeReload()
        } else {
          this.$message.error('复制失败！')
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
      this.CYList()
      this.visible = false
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>
