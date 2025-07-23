<template>
  <j-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @cancel="handleCancel"
  >
    <BhzLqBaseCBCXForm ref="realForm" @ok="submitCallback" :disabled="disableSubmit" />
  </j-modal>
</template>

<script>
import { getAction } from '@/api/manage'
import BhzLqBaseCBCXForm from './BhzLqBaseCBCXForm'

export default {
  name: 'BhzLqBaseCBCXModalLone',
  components: {
    BhzLqBaseCBCXForm,
  },
  data() {
    return {
      title: '详情',
      width: 800,
      visible: false,
      disableSubmit: true,
    }
  },
  created() {
    let guid = this.$route.query.guid
    guid && this.whiteDetail(guid)
  },
  methods: {
    whiteDetail(guid) {
      let params = {
        guid: guid,
      }
      getAction('/lqbhz/bhzLqBases/ChaoBiaolist', params).then((res) => {
        if (res.success) {
          let record = res.result.records[0] || {}
          this.edit(record)
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    add() {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.realForm.add()
      })
    },
    edit(record) {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.realForm.edit(record)
      })
    },
    close() {
      this.$emit('close')
      this.visible = false
    },
    handleOk() {
      this.$refs.realForm.handleOk()
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