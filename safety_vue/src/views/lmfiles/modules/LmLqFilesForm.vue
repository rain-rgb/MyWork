<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="文件上传" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fileurl">
              <a-upload style="margin-left: 20%" :action="uploadAction" :headers="headers"
                        :default-file-list="defaultFileList" @change="handleChange1">
                <a-button>
                  <a-icon type="upload"/>
                  图片/文件上传
                </a-button>
              </a-upload>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@api/manage'
import { validateDuplicateValue } from '@/utils/util'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'

export default {
  name: 'LmLqFilesForm',
  components: {},
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data() {
    return {
      uploadAction: window._CONFIG['domianURL'] + '/sys/common/upload',
      headers: {},
      defaultFileList: [],
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: '/lmjob/lmLqFiles/add',
        edit: '/lmjob/lmLqFiles/edit',
        queryById: '/lmjob/lmLqFiles/queryById'
      }
    }
  },
  computed: {
    formDisabled() {
      return this.disabled
    },
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
    const token = Vue.ls.get(ACCESS_TOKEN)
    this.headers = { 'X-Access-Token': token }
    console.log(this.model)
  },
  methods: {
    handleChange1({ file, fileList }) {
      if (file.status !== 'uploading') {
        this.model.record.fileurl = file.response.message
      }
    },
    add(record) {
      this.edit(this.modelDefault)
      this.model = Object.assign({}, record)
      this.visible = true
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.visible = true
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          httpurl += this.url.edit
          method = 'put'
          httpAction(httpurl, this.model.record, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message)
              that.$emit('ok')
            } else {
              that.$message.warning(res.message)
            }
            this.routeReload()
          }).finally(() => {
            that.confirmLoading = false
          })
        }
      })
    },
    routeReload() {//刷新页面
      this.reloadFlag = false
      let ToggleMultipage = 'ToggleMultipage'
      this.$store.dispatch(ToggleMultipage, false)
      this.$nextTick(() => {
        this.$store.dispatch(ToggleMultipage, true)
        this.reloadFlag = true
      })
    },
  }
}
</script>