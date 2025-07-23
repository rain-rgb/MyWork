<template>
  <j-modal
    centered
    :title="title"
    :width="800"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    cancelText="关闭"
  >
    <a-form>
      <a-row>
        <a-col :span="24">
          <a-form-item label="处理意见" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag v-model="model.opinion" placeholder="请选择处理意见" dictCode="is_agree" />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="24">
          <a-form-item label="收件人" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <div style="display: flex">
              <a-input v-model="recipientName" disabled placeholder="请选择收件人"></a-input>
              <a-button type="primary" @click="searchUser(1)" icon="search">选择</a-button>
            </div>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row v-if="status == 1">
        <a-col :span="24">
          <a-form-item label="抄送给" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <div style="display: flex">
              <a-input v-model="ccToName" disabled placeholder="请选择抄送人"></a-input>
              <a-button type="primary" @click="searchUser(2)" icon="search">选择</a-button>
            </div>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
    <instruct-user ref="user" @ok="confirm"></instruct-user>
  </j-modal>
</template>
<script>
import { getAction } from '@/api/manage'
import InstructUser from '@/views/bhz/bhzSupervisionOrder/modules/InstructUser'

export default {
  name: 'InstructOpinion',
  components: {
    InstructUser,
  },
  data() {
    return {
      visible: false,
      title: '意见信息',
      disableSubmit: false,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      recipientName: '',
      ccToName: '',
      batchNo: '', //指令单编号
      status: 0, //签署状态
      model: {
        opinion: null, //签署意见
        recipient: null, //收件人id
        ccTo: null, //抄送人id
      },
      url: {
        add1: '/sys/sysAnnouncementSend/add1',
      },
    }
  },
  mounted() {},
  computed: {},
  methods: {
    handleCancel() {
      this.visible = false
    },
    handleOk() {
      let params
      if (this.status == 2) {
        params = {
          userId: this.model.recipient,
          signatureOpinion: this.model.opinion,
          status: this.status,
          batchNo: this.batchNo,
          copyUserId: this.model.ccTo,
        }
      } else {
        params = {
          userId: this.model.recipient,
          signatureOpinion: this.model.opinion,
          status: this.status,
          batchNo: this.batchNo,
        }
      }
      getAction(this.url.add1, params).then((res) => {
        if (res.success) {
          this.$message.success('签署成功！')
          this.$emit('change')
        } else {
          this.$message.warning('签署失败！')
        }
        this.visible = false
      })
    },
    searchUser(type) {
      this.$refs.user.type = type
      this.$refs.user.visible = true
    },
    confirm(data, type) {
      if (type == 1) {
        this.model.recipient = data.id
        this.recipientName = data.realname
      } else if (type == 2) {
        this.model.ccTo = data.id
        this.ccToName = data.realname
      }
    },
  },
}
</script>
<style lang="less" scoped>
</style>