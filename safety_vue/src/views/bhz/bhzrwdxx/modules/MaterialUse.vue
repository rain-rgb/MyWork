<template>
    <j-modal
        :title="title"
        :width="1200"
        :visible="visible"
        @ok="handleOk"
        :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
        @cancel="handleCancel"
        cancelText="关闭"
    >
        <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            class="j-table-force-nowrap"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="false"
            :loading="loading"
        >
            <span slot="tags" slot-scope="tags, record">
                <a-tag color="green" v-if="record.materialeOverLevel == '0'">合格</a-tag>
                <a-tag color="orange" v-if="record.materialeOverLevel == '1'">初级超标</a-tag>
                <a-tag color="yellow" v-if="record.materialeOverLevel == '2'">中级超标</a-tag>
                <a-tag color="red" v-if="record.materialeOverLevel == '3'">高级超标</a-tag>
            </span>
            <div slot="batchSlot" slot-scope="text, record">
              <!-- <a @click="handleDetail(record)">{{text}}</a> -->
              <a @click="inspectionDetail(record)">{{ text }}</a>
              <!-- <p @click="">{{text}}</p> -->
            </div>
        </a-table>
      <wzFrom ref="modalForm"></wzFrom>
      <wztaizhangModal ref="wztaizhangForm"></wztaizhangModal>
    </j-modal>
</template>

<script>
import { getAction } from '@/api/manage'
import WzFrom from './wzFrom'
import wztaizhangModal from '@/views/zlgl/wztaizhang/wztaizhangnew/modules/WztaizhangModal'

export default {
    name: 'MaterialUse',
    components: {
      WzFrom,
      wztaizhangModal,
    },
    data() {
        return {
            title: '材料使用',
            visible: false,
            loading: false,
            disableSubmit: true,
            code: '',
            dataSource: [],
            columns: [
                {
                    title: '序号',
                    dataIndex: '',
                    key: 'rowIndex',
                    width: 60,
                    align: 'center',
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1
                    },
                },
                {
                    title: '材料名称',
                    align: 'center',
                    dataIndex: 'materialeName',
                },
                // {
                //     title: '规格型号',
                //     align: 'center',
                //     dataIndex: 'ggxh',
                // },
                {
                    title: '来源料仓',
                    align: 'center',
                    dataIndex: 'binName',
                },
                {
                    title: '批次号',
                    align: 'center',
                    dataIndex: 'batch',
                    scopedSlots: { customRender: 'batchSlot' },
                },
                // {
                //     title: '试验编号',
                //     align: 'center',
                //     dataIndex: 'sybh',
                // },
                {
                    title: '实际用量',
                    align: 'center',
                    dataIndex: 'realityNumber',
                },
                {
                    title: '理论用量',
                    align: 'center',
                    dataIndex: 'theoryNumber',
                },
                {
                    title: '误差值',
                    align: 'center',
                    dataIndex: 'errorValue',
                },
                {
                    title: '超标值',
                    align: 'center',
                    dataIndex: 'overValue',
                },
                {
                    title: '超标等级',
                    align: 'center',
                    dataIndex: 'materialeOverLevel',
                    key: 'materialeOverLevel',
                    scopedSlots: { customRender: 'tags' },
                }
            ],
        }
    },
    methods: {
        getUseList(batchNo) {
            this.loading = true
            let params = {
                batchNo: batchNo
            }
            // /hntbhz/bhzCementBase/queryBhzCementDetailByMainId
            getAction('/hntbhz/bhzCementBase/cementDetailAndStockBin', params).then((res) => {
                if (res.success) {
                    this.dataSource = res.result
                } else {
                    this.$message.warning(res.message)
                }
            })
            .finally(() => {
                this.loading = false
            })
        },
        handleCancel() {
            this.visible = false
        },
        handleOk() {
            this.visible = false
        },
        handleDetail(record) {
            this.$refs.modalForm.edit(record);
            // this.$refs.modalForm.title = "检验批详情";
            // this.$refs.modalForm.disableSubmit = true;
        },
        inspectionDetail({ batch }) {
          console.log(batch,'inspectionDetail--------');
          let params = {
            pici: batch
          }
          getAction('/wztaizhang/wztaizhang/lists', params).then((res) => {
            if (res.success) {
              let record = res.result.records[0] || {}
              this.$refs.wztaizhangForm.edit(record)
              this.$refs.wztaizhangForm.title = '详情'
              this.$refs.wztaizhangForm.disableSubmit = true
            } else {
              this.$message.warning(res.message)
            }
          })
        },
    },
}
</script>