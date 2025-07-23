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
    okText="确认"
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
  name: 'instructModel',
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
        edit: '/sys/systems/sysMessages/editOk',
      },
      instruct: {},
    }
  },
  mounted() {},
  computed: {
    disableSubmit() {
      return this.instruct.status == 3
    },
  },
  methods: {
    getList(id) {
      this.visible = true
      this.id = id
      this.instruct = {}
      this.getInstruct()
    },
    getInstruct() {
      let params = {
        id: this.id,
      }
      getAction(this.url.apiData, params).then((res) => {
        if (res.success) {
          this.instruct = res.data
        }
      })
    },
    getChange() {
      this.getInstruct()
      // setTimeout(()=>{
      this.$emit('change')
      // },1000)
    },
    handleCancel() {
      this.visible = false
    },
    handleOk() {
      let params = {
        id: this.instruct.id,
        penaltyAmount: this.instruct.penaltyAmount,
        shebeiNo:this.instruct.shebeiNos,
        zldurl:this.instruct.zldurl
      }
      putAction(this.url.edit, params).then((res) => {
        if (res.success) {
          this.$message.success('保存成功！')
        } else {
          this.$message.warning('保存失败！')
        }
        this.$refs.opinion.batchNo = this.instruct.batchNo
        this.$refs.opinion.status = this.instruct.status
        this.visible = false
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