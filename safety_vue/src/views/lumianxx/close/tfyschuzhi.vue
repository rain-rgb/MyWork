<template>
  <div>
    <a-modal
      title="处置"
      :width="width"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel2"
    >
      <div class="reason">{{ reason }}</div>
      <a-form-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol" centered>
        <a-input placeholder="请输入问题原因" v-model="wtyy"></a-input>
      </a-form-item>
      <a-form-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-input placeholder="请输入处理方式" v-model="clfs"></a-input>
      </a-form-item>
      <a-form-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-input placeholder="请输入处理结果" v-model="cljg"></a-input>
      </a-form-item>
      <a-form-item label="上传文件/图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-upload name="file" :action="action" :headers="tokenHeader" @change="handleChange">
          <a-button> <a-icon type="upload" />上传文件</a-button>
        </a-upload>
        <!-- <j-image-upload v-model="fileList" :isMultiple="isMultiple"></j-image-upload> -->
      </a-form-item>
    </a-modal>
  </div>
</template>
<script>
import { getAction, putAction } from '../../../api/manage'
import { ACCESS_TOKEN } from "@/store/mutation-types"
import JImageUpload from '../../../components/jeecg/JImageUpload'
import Vue from 'vue'

function getBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = (error) => reject(error)
  })
}
export default {
  name: 'tfyschuzhi',
  components: { JImageUpload },
  props: ['flag', 'id', 'reason'],
  data() {
    return {
      action: window._CONFIG['domianURL'] + '/sys/common/upload',
      tokenHeader: {},
      fileList: [],
      cljg: '',
      wtyy: '',
      clfs: '',
      // isMultiple: true,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      width: 800,
      visible: false,
      confirmLoading: false,
      previewVisible: false,
      previewImage: '',
      url: {
        edit: '/hctfysworkareaoverhandler/hcTfysworkareaOverHandler/deallist',
      },
    }
  },
  watch: {
    flag(n) {
      n === 2 ? (this.visible = true) : (this.visible = false)
    },
  },
  created() {
    const token = Vue.ls.get(ACCESS_TOKEN)
    this.tokenHeader = { 'X-Access-Token': token }
  },
  mounted() {
    if (this.flag === 2) {
      this.visible = true
    }
  },
  methods: {
    showModal() {
      this.visible = true
    },
    subcommit() {
      let param = {
        baseid: this.id,
        flag: this.flag,
        handleResult: this.cljg,
        problemReasons: this.wtyy,
        handleWay: this.clfs,
        filePath2: this.fileList.join(),
      }
      //console.log(this.id)
      getAction(this.url.edit, param).then((res) => {
        debugger
        if (res.success) {
          this.$message.success('处置成功')
        } else {
          this.$message.error('处置失败')
        }
        this.confirmLoading = false
        this.visible = false
        const demo = 0
        this.$emit('change', demo)
      })
      this.routeReload()
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
      console.log('刷新页面')
    },
    handleOk(e) {
      if (this.fileList.length === 0) {
        this.$message.warning('请上传处理文件！')
        return
      }
      this.confirmLoading = true
      setTimeout(() => {
        this.subcommit()
      }, 1000)
    },

    handleCancel2(e) {
      this.visible2 = false
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
    handleChange({ file, fileList }) {
      if (file.status !== 'uploading') {
        this.fileList = []
        fileList.forEach((item) => {
          this.fileList.push(item.response.message)
        })
      }
    },
  },
}
</script>
<style scoped>
.ant-upload-picture-card-wrapper {
  display: flex !important;
  flex-wrap: wrap;
  padding: 0 150px;
}
.ant-upload-picture-card-wrapper::before {
  display: block;
}

.ant-upload.ant-upload-select-picture-card {
  display: block;
}
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
.ant-upload-list {
  display: flex;
  flex-wrap: wrap;
}

.ant-upload.ant-upload-select-picture-card > .ant-upload {
  display: block !important;
  margin-top: 15px;
}

.reason {
  height: 30px;
  text-align: center;
  color: red;
  font-weight: bold;
  font-size: 20px;
  margin-bottom: 20px;
}
</style>