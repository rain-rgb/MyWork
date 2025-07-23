<template>
  <div>
    <a-modal
      title="审核"
      :width="width"
      :visible="visible1"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel2"
    >
      <a-form-item label="审核结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <j-dict-select-tag style="width: 200px" type="list" v-model="modelObj.checkStatus" dictCode="checkStatus"
                           placeholder="请选择审核结果"/>
      </a-form-item>
      <a-form-item label="审核意见" :labelCol="labelCol" :wrapperCol="wrapperCol" centered>
        <a-input placeholder="请输入审核意见" v-model="modelObj.shyijian"></a-input>
      </a-form-item>

    </a-modal>
  </div>
</template>
<script>
import { getAction, putAction } from '@api/manage'

function getBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}

export default {
  name: 'ShigongphbShenHe',
  props: ['id'],
  data() {
    return {
      modelObj: {},
      isMultiple: true,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      width: 800,
      visible1: false,
      confirmLoading: false,
      previewVisible: false,
      previewImage: '',
      url: {
        edit: '/system/shigongphb/edit',
      }
    }
  },
  created() {
  },
  mounted() {},
  methods: {
    showModal() {
      this.visible = true
    },
    subcommit() {
      putAction(this.url.edit, this.modelObj).then(res => {
        if (res.success) {
          this.$message.success('审核成功')
        } else {
          this.$message.error('审核失败')
        }
        this.confirmLoading = false
        this.visible1 = false
        const demo = 0
        this.$emit('change', demo)
      })
      // this.routeReload()
    },
    routeReload() {//刷新页面
      this.reloadFlag = false
      let ToggleMultipage = 'ToggleMultipage'
      this.$store.dispatch(ToggleMultipage, false)
      this.$nextTick(() => {
        this.$store.dispatch(ToggleMultipage, true)
        this.reloadFlag = true
      })
      console.log('刷新页面')
    },
    handleOk(e) {
      this.confirmLoading = true
      setTimeout(() => {
        this.subcommit()
      }, 500)
    },

    handleCancel2(e) {
      this.visible1 = false
      const demo = 0
      this.$emit('change', demo)
    },
    handleCancel() {
      this.previewVisible = false
    },
    async handlePreview(file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    handleChange({ fileList }) {
      this.fileList = fileList
    },
  },
}
</script>
