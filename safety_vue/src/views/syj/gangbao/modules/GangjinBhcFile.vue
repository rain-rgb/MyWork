<template>
  <div>
    <a-modal
      title="附件上传"
      :width="width"
      :visible="visible1"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel2"
    >
      <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="file">
        <a-upload style="margin-left: 20%" :action="uploadAction" :headers="headers" v-model="file"
                  :default-file-list="defaultFileList" @change="handleChange">
          <a-button>
            <a-icon type="upload"/>
            附件上传
          </a-button>
        </a-upload>
      </a-form-item>
    </a-modal>
  </div>
</template>
<script>
import { getAction, putAction } from '@api/manage'
import JUpload from '@/components/jeecg/JUpload'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import Vue from 'vue'

function getBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}

export default {
  name: 'GangjinBhcFile',
  props: ['flag', 'id'],
  components: {
    JUpload
  },
  data() {
    return {
      uploadAction: window._CONFIG['domianURL'] + '/sys/common/upload',
      headers: {},
      defaultFileList: [],
      file: '',
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
        edit: '/trgangjinbhcm/trGangjinbhcM/edit',
      }
    }
  },
  watch: {
    flag(n) {
      n === true ? this.visible1 = true : this.visible1 = false
    }
  },
  created() {
    const token = Vue.ls.get(ACCESS_TOKEN)
    this.headers = { 'X-Access-Token': token }
  },
  mounted() {
    if (this.flag === true) {
      this.visible1 = true
    }
  },
  methods: {
    showModal() {
      this.visible = true
    },
    handleChange({ file, fileList }) {
      if (file.status !== 'uploading') {
        this.file = file.response.message
      }
    },
    subcommit() {
      let param = { id: this.id, file: this.file }
      putAction(this.url.edit, param).then(res => {
        if (res.success) {
          this.$message.success('提交成功')
        } else {
          this.$message.error('提交失败')
        }
        this.confirmLoading = false
        this.visible1 = false
        const demo = 0
        this.$emit('change', demo)
      })
      this.routeReload()
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
      // this.visible1=false;
      this.confirmLoading = true
      // formData.img = that.fileList;
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
  },
}
</script>
