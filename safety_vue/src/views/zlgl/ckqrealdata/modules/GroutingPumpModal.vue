<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <line-chart-multid :fields="fields" :dataSource="dataSource"></line-chart-multid>
  </j-modal>
</template>

<script>
import LineChartMultid from '@/components/chart/LineChartMultid'
const dataSources = [
  { type: 'Feb', make: 16.9, lose: 24.2 },
  { type: 'Mar', make: 11.5, lose: 35.7 },
  { type: 'Apr', make: 14.5, lose: 18.5 },
  { type: 'May', make: 18.4, lose: 11.9 },
  { type: 'Jun', make: 21.5, lose: 15.2 },
  { type: 'Jul', make: 25.2, lose: 30.0 },
  { type: 'Aug', make: 26.5, lose: 16.6 },
  { type: 'Sep', make: 23.3, lose: 14.2 },
  { type: 'Oct', make: 18.3, lose: 10.3 },
  { type: 'Nov', make: 13.9, lose: 16.6 },
  { type: 'Dec', make: 19.6, lose: 24.8 },
]
export default {
  name: 'GroutingPumpModal',
  components: { LineChartMultid },
  data() {
    return {
      dataSource: [],
      fields:['make','lose'],
      title: '',
      width: 800,
      visible: false,
      disableSubmit: false,
    }
  },
  created() {
    this.dataSource = dataSources
  },
  methods: {
    add() {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.realForm.add()
      })
    },
    edit(record) {
      console.log(record)
      this.visible = true
      console.log(this.$refs.realForm)
      // this.$nextTick(() => {
      //   this.$refs.realForm.edit(record)
      // })
    },
    close() {
      this.$emit('close')
      this.visible = false
    },
    handleOk() {
      this.$refs.realForm.submitForm()
    },
    submitCallback() {
      this.$emit('ok')
      this.visible = false
    },
    handleCancel() {
      this.close()
    },
  },
}
</script>
<style scoped>
</style>