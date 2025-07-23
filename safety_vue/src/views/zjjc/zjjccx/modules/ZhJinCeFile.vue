<template>
  <div>
    <a-modal
      title="评级附件提交"
      :width="width"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <a-form-item label="评级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="level">
        <j-search-select-tag style="width: 200px" type="list"  placeholder="请选择评级" v-model="level"
                             :dictOptions="dictOptions1"/>
      </a-form-item>
      <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="file">
        <a-upload style="margin-left: 20%" :action="uploadAction" :headers="headers" v-model="file"
                  :default-file-list="defaultFileList" @change="handleChange">
          <a-button>
            <a-icon type="upload"/>
            附件上传
          </a-button>
        </a-upload>
      </a-form-item>
<!--      <a-form-item label="附件说明" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fileInfo" centered>-->
<!--        <a-input placeholder="请输入附件说明" v-model="fileInfo"></a-input>-->
<!--      </a-form-item>-->
    </a-modal>
  </div>
</template>
<script>
import { getAction, putAction } from '@api/manage'
import JUpload from '@/components/jeecg/JUpload'
import {ACCESS_TOKEN} from "@/store/mutation-types";
import Vue from "vue";

function getBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}

export default {
  name: 'ZhJinCeFile',
  props: ['id'],
  components: {
    JUpload
  },
  data() {
    return {
      uploadAction: window._CONFIG['domianURL']+"/sys/common/upload",
      headers: {},
      defaultFileList: [],
      file: '',
      level: undefined,
      // fileInfo: '',
      isMultiple: true,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      dictOptions1: [{
        text: 'Ⅰ类',
        value: '1'
      }, {
        text: 'Ⅱ类',
        value: '2'
      }, {
        text: 'Ⅲ类',
        value: '3'
      }],
      width: 800,
      visible: false,
      confirmLoading: false,
      previewImage: '',
      url: {
        edit: '/chaoshengbo/chaoshengboSyjbsj/editById',
      }
    }
  },
  created() {
    const token = Vue.ls.get(ACCESS_TOKEN);
    this.headers = {"X-Access-Token":token};
  },
  methods: {
    showModal() {
      this.level = undefined
      this.visible = true
    },
    handleChange({ file, fileList }) {
      if (file.status !== 'uploading') {
        this.file = file.response.message
      }
    },
    subcommit() {
      // let param = { id: this.id, level: this.level, file: this.file, fileInfo: this.fileInfo }
      let param = { id: this.id, level: this.level, file: this.file }
      putAction(this.url.edit, param).then(res => {
        if (res.success) {
          this.$message.success('提交成功')
        } else {
          this.$message.error('提交失败')
        }
        this.confirmLoading = false
        this.visible = false
        this.$emit('change')
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
    handleOk() {
      this.confirmLoading = true
      setTimeout(() => {
        this.subcommit()
      }, 500)
    },

    handleCancel() {
      this.visible = false
      this.$emit('change')
    },
    async handlePreview(file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
    },
  },
}
</script>
