<template>
  <j-modal
    :title="title"
    width="100%"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <shigongphb-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></shigongphb-form>
  </j-modal>
</template>

<script>
import ShigongphbForm from './ShigongphbForm'
import { getAction } from '@/api/manage'

export default {
  name: 'ShigongphbModalTwo',
  components: {
    ShigongphbForm
  },
  data() {
    return {
      title: '',
      //width:100%,
      visible: false,
      disableSubmit: false
    }
  },
  created() {
    let code = this.$route.query.code
    this.codeDetail(code)
  },
  methods: {
    codeDetail(code) {
      let params = {
        Code: code
      }
      getAction('/system/shigongphb/sphblist', params).then((res) => {
        if (res.success) {
          let record = res.result.records[0] || {}
          this.edit(record)
          this.title = '详情'
          this.disableSubmit = true
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
    editAdd(record) {
      this.visible = true
      this.$nextTick(() => {
        this.$refs.realForm.editAdd(record)
      })
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
    }
  }
}
</script>