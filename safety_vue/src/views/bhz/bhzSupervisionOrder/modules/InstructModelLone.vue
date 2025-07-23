<template>
  <j-modal
    centered
    :title="title"
    :width="1000"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    cancelText="关闭"
    okText="保存"
  >
    <div id="printContent">
      <a-button class="not-print print" v-print="'#printContent'" type="primary">打印</a-button>
      <instruct :instructData.sync="instruct"></instruct>
    </div>

    <instruct-opinion ref="opinion" @change="getChange"></instruct-opinion>
  </j-modal>
</template>
<script>
import { getAction, putAction } from '@/api/manage'
import Instruct from '@/views/bhz/bhzSupervisionOrder/modules/Instruct'
import InstructOpinion from '@/views/bhz/bhzSupervisionOrder/modules/InstructOpinion'

export default {
  name: 'InstructModelLone',
  components: {
    Instruct,
    InstructOpinion,
  },
  data() {
    return {
      visible: false,
      title: '指令单',
      id: null,
      url: {
        apiData: '/bhzSupervisionOrder/bhzSupervisionOrder/apiData2',
        edit: '/bhzSupervisionOrder/bhzSupervisionOrder/edit',
      },
      instruct: {},
    }
  },
  created() {
    let id = this.$route.query.id
    id && this.view(id)
  },
  computed: {
    disableSubmit() {
      return this.instruct.status == 3
    },
  },
  methods: {
    view(data) {
      this.visible = true
      this.instruct = {}
      let params
      if (typeof data === 'object') {
        params = data
        this.id = data.id
      } else {
        params = {
          id: data,
        }
        this.id = data
      }
      getAction(this.url.apiData, params).then((res) => {
        if (res.success) {
          this.instruct = res.data
        }
      })
    },
    getChange() {
      let params = {
        id: this.id,
      }
      getAction(this.url.apiData, params).then((res) => {
        if (res.success) {
          this.instruct = res.data[0]
        }
      })
    },
    handleCancel() {
      this.visible = false
    },
    handleOk() {
      let params = {
        id: this.instruct.id,
        penaltyAmount: this.instruct.penaltyAmount,
      }
      putAction(this.url.edit, params).then((res) => {
        if (res.success) {
          this.$message.success('保存成功！')
        } else {
          this.$message.warning('保存失败！')
        }
        this.$refs.opinion.batchNo = this.instruct.batchNo
        this.$refs.opinion.status = this.instruct.status
        this.$refs.opinion.visible = true
      })
    },
  },
}
</script>
<style lang="less" scoped>
.print {
  position: absolute;
  top: 6%;
  right: 4%;
}
@media print {
  /* 隐藏不需要打印的部分 */
  .not-print {
    display: none !important;
  }
}
</style>