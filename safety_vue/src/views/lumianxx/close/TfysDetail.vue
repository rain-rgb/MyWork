<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    @ok="handleOk"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <a-card title="处理信息" :bordered="false" :headStyle="{ color: '#0785fd' }">
      <a-form-model>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input placeholder="无" v-model="dealObj.problemReasons" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input placeholder="无" v-model="dealObj.handleWay" disabled></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input placeholder="无" v-model="dealObj.handleResult" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="处理人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input placeholder="无" v-model="dealObj.handlePerson" disabled></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input placeholder="无" v-model="dealObj.handleTime" disabled></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="处理附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <div>
                <a-tag v-for="(item, i) in filePath2" :key="i" @click="handleClick(item)">附件{{ i + 1 }}</a-tag>
              </div>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
    <a-card title="审核信息" :bordered="false" :headStyle="{ color: '#0785fd' }">
      <a-form-model>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="审批意见" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input placeholder="无" v-model="dealObj.supervisorApproval" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="审批结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input placeholder="无" v-model="dealObj.supervisorResult" disabled></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="审批人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input placeholder="无" v-model="dealObj.approvalPerson" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="审批时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input placeholder="无" v-model="dealObj.supervisorHandleTime" disabled></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="审批附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <div>
                <a-tag v-for="(item, i) in filePath" :key="i" @click="handleClick(item)">附件{{ i + 1 }}</a-tag>
              </div>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </a-card>
  </j-modal>
</template>
<script>
import { getAction } from '@/api/manage'

export default {
  name: 'TfysDetail',
  components: {},
  data() {
    return {
      title: '详情',
      width: 800,
      visible: false,
      disableSubmit: false,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      blockid: '',
      dealObj: {},
      filePath: [],
      filePath2: [],
    }
  },
  methods: {
    handleClick(url) {
      if (url) {
        window.open(url, '_blank')
      }
    },
    show(blockid) {
      this.blockid = blockid
      this.getDealMsg()
      this.visible = true
    },
    getDealMsg() {
      this.dealObj = {}
      this.filePath = []
      this.filePath2 = []
      let params = { baseid: this.blockid }
      getAction('/hctfysworkareaoverhandler/hcTfysworkareaOverHandler/list', params).then((res) => {
        if (res.success) {
          if (res.result.records.length > 0) {
            this.dealObj = res.result.records[0]
            this.filePath = this.dealObj.filePath ? this.dealObj.filePath.split(',') : []
            this.filePath2 = this.dealObj.filePath2 ? this.dealObj.filePath2.split(',') : []
          }
        } else {
          this.$message.error(res.message)
        }
      })
    },
    handleOk() {
      this.visible = false
    },
    handleCancel() {
      this.visible = false
    },
  },
}
</script>