<template>
  <j-modal
    centered
    :title="title"
    :width="1200"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <!-- <div class="cattj">
            <a-button type="primary" ghost>车次数：{{carQuantity}}</a-button>
            <a-button type="primary" ghost>浇筑时长：{{castingTime}}</a-button>
            <a-button type="primary" ghost>超标：{{overProof}}</a-button>
            <a-button type="primary" ghost>闭合：{{close}}</a-button>
            <a-button type="primary" ghost>运输超时：{{transportTimeout}}</a-button>
        </div> -->
    <a-table
      :rowKey="(record, index) => index"
      size="middle"
      bordered
      :columns="columns"
      :loading="loading"
      :data-source="carAndMixingStationList"
      :pagination="false"
    >
      <span slot="tags" slot-scope="tags, record">
        <a-tag color="orange" v-if="record.jianyanstate == '0'">未检测</a-tag>
        <a-tag color="green" v-if="record.jianyanstate == '1'">合格</a-tag>
        <a-tag color="red" v-if="record.jianyanstate == '2'">不合格</a-tag>
        <a-tag color="purple" v-if="record.jianyanstate == '3'">检验中</a-tag>
      </span>
      <span slot="action" slot-scope="text, record">
        <a @click="handleDetail(record)">详情</a>
      </span>
    </a-table>

    <!-- <wztaizhang-modal ref="modalForm" ></wztaizhang-modal> -->
    <wz-from ref="modalForm"></wz-from>
  </j-modal>
</template>

<script>
import WztaizhangModal from '@views/zlgl/wztaizhang/modules/WztaizhangModal'
import { getAction } from '@/api/manage'
import WzFrom from './wzFrom'

export default {
  name: 'wztaizhangDetail',
  components: {
    WztaizhangModal,
    WzFrom,
    
  },
  data() {
    return {
      visible: false,
      loading: false,
      disableSubmit: true,
      title: '检验批列表',

      carAndMixingStationList: [],
      columns: [
        {
          title: '进场时间',
          align: 'center',
          dataIndex: 'jinchangshijian',
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'shebeibianhao',
        },
        {
          title: '料仓名称',
          align: 'center',
          dataIndex: 'lcbianhao',
        },
        {
          title: '材料名称',
          align: 'center',
          dataIndex: 'cailiaono',
        },
        {
          title: '净重(吨)',
          align: 'center',
          dataIndex: 'jingzhongt',
        },
        {
          title: '检验批次',
          align: 'center',
          dataIndex: 'pici',
        },
        {
          title: '规格类型',
          align: 'center',
          dataIndex: 'guigexinghao',
        },
        {
          title: '是否合格',
          align: 'center',
          dataIndex: 'jianyanstate',
          scopedSlots: { customRender: 'tags' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
    }
  },
  created() {},
  methods: {
    handleCancel() {
      this.visible = false
    },
    handleOk() {
      this.visible = false
    },
    getWztaizhang(code) {
      this.loading = true
      let params = {
        rwdcode: code,
      }

      this.carAndMixingStationList = []
      getAction('/wztaizhang/wztaizhang/listByCode', params)
        .then((res) => {
          if (res.code == 200) {
            this.carAndMixingStationList = res.result
          } else {
            this.$message.warning(res.message)
          }
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleDetail(record) {
        this.$refs.modalForm.edit(record);
        // this.$refs.modalForm.title = "检验批详情";
        // this.$refs.modalForm.disableSubmit = true;
    },
  },
}
</script>
<style scoped>
.cattj {
  margin-bottom: 24px;
  display: flex;
  justify-content: space-around;
}
</style>