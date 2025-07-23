<template>
  <j-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': true} }"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <a-tabs default-active-key="1">
      <a-tab-pane tab="拌合站子表材料信息" key="1" :forceRender="true">
        <a-table
          rowKey="id"
          size="middle"
          bordered
          class="j-table-force-nowrap"
          :pagination="false"
          :loading="bhzCementDetailTable.loading"
          :columns="bhzCementDetailTable.columns"
          :data-source="bhzCementDetailTable.dataSource"
          :rowClassName="setClass"
        >
          <template slot="tags" slot-scope="tags, record">
            <a-tag color="green" v-if="record.materialeOverLevel == '0'">合格</a-tag>
            <a-tag color="orange" v-if="record.materialeOverLevel == '1'">初级超标</a-tag>
            <a-tag color="yellow" v-if="record.materialeOverLevel == '2'">中级超标</a-tag>
            <a-tag color="red" v-if="record.materialeOverLevel == '3'">高级超标</a-tag>
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>
    <j-form-container disabled>
      <!-- 主表单区域 -->
      <a-form slot="detail">
        <a-card title="施工方处置信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}"
                style="margin-top:10px">
          <a-row>
            <a-col :span="12">
              <a-form-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-textarea
                  placeholder="无"
                  v-model="bhzCementOverHandler.problemReasons"
                  :auto-size="{ minRows: 5, maxRows: 20 }"
                ></a-textarea>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.handleWay" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.handleResult" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.handleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处置人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.handlePerson" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="处置附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <viewer :images="detailList">
                  <img
                    v-for="(img,index) in detailList"
                    :key="index"
                    style="height:100px;width: 100px;margin: 5px 10px 5px 10px;float: left"
                    :src="img"
                    alt="">
                </viewer>
              </a-form-item>
            </a-col>
          </a-row>

        </a-card>
        <a-card v-show="this.dealObj.overLevel > 1" title="监理单位审核" :bordered="false" :headStyle="{color:'#0785fd'}"
                :bodyStyle="{padding:'10'}" style="margin-top:10px">
          <a-row>
            <a-col :span="12">
              <a-form-item label="监理审核" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-if="bhzCementOverHandler.overproofStatus === 30 " v-model="bhzCementOverHandler.remark"
                  placeholder="无"></a-input>
                <a-input v-else v-model="bhzCementOverHandler.supervisorApproval" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="审核结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-if="bhzCementOverHandler.overproofStatus === 30 " placeholder="驳回"></a-input>
                <a-input v-else v-model="bhzCementOverHandler.supervisorResult" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.approvalPerson" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="监理处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.supervisorHandleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="24">
              <a-form-item label="审核附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <viewer :images="detailList1">
                  <img
                    v-for="(img,index) in detailList1"
                    :key="index"
                    style="height:100px;width: 100px;margin: 5px 10px 5px 10px;float: left"
                    :src="img"
                    alt="">
                </viewer>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card v-show="this.dealObj.overLevel === 3" title="指挥部核查" :bordered="false" :headStyle="{color:'#0785fd'}"
                :bodyStyle="{padding:'10'}" style="margin-top:10px">
          <a-row>
            <a-col :span="12">
              <a-form-item label="指挥部核查" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-if="bhzCementOverHandler.overproofStatus === 60 "
                  v-model="bhzCementOverHandler.headquartersRemark" placeholder="无"></a-input>
                <a-input
                  v-else-if="bhzCementOverHandler.overproofStatus !== 20 &&bhzCementOverHandler.headquartersRemark !== null"
                  v-model="bhzCementOverHandler.headquartersRemark" placeholder="无"></a-input>
                <a-input v-else v-model="bhzCementOverHandler.headquartersApproval" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="核查结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-if="bhzCementOverHandler.overproofStatus === 60 " placeholder="驳回"></a-input>
                <a-input
                  v-else-if="bhzCementOverHandler.overproofStatus !== 20 &&bhzCementOverHandler.headquartersRemark !== null "
                  placeholder="驳回"></a-input>
                <a-input v-else v-model="bhzCementOverHandler.headquartersResult" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="核查人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.headquartersPerson" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="核查时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="bhzCementOverHandler.headquartersHandleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>

        </a-card>
      </a-form>
    </j-form-container>
  </j-modal>
</template>

<script>
import { getAction } from '@/api/manage'

export default {
  name: 'BhzCementBaseCBFormTwo',
  components: {},
  data() {
    return {
      title: '',
      visible: false,
      dealObj: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      labelCol1: {
        xs: { span: 24 },
        sm: { span: 3 },
      },
      wrapperCol1: {
        xs: { span: 24 },
        sm: { span: 20 },
      },
      detailList: [],
      detailList1: [],
      bhzCementOverHandler: [],
      // 拌合站子表材料信息
      bhzCementDetailTable: {
        loading: false,
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
            title: '材料名',
            dataIndex: 'materialeName',
            align: 'center',
          },
          {
            title: '实际用量',
            dataIndex: 'realityNumber',
            align: 'center',
          },
          {
            title: '理论用量',
            dataIndex: 'theoryNumber',
            align: 'center',
          },
          {
            title: '误差值',
            dataIndex: 'errorValue',
            align: 'center',
          },
          {
            title: '超标值',
            dataIndex: 'overValue',
            align: 'center',
          },
          {
            title: '超标等级',
            dataIndex: 'materialeOverLevel',
            align: 'center',
            scopedSlots: { customRender: 'tags' },
          },
        ],
      },
      url: {
        list: '/czsh/bhzCementOverHandler/list',
        list2: "/hntbhz/bhzCementBase/list2",
        bhzCementDetail: '/hntbhz/bhzCementBase/queryBhzCementDetailByMainId',
      },
      batchNo: '',
    }
  },
  mounted(){
    let batchNo = this.$route.query.batchNo;
    this.getMsg(batchNo)
  },
  methods: {
    getTableList() {
      this.bhzCementDetailTable.loading = true
      this.bhzCementDetailTable.dataSource = []
      let params = { batchNo: this.batchNo }
      getAction(this.url.bhzCementDetail, params)
        .then((res) => {
          if (res.success) {
            this.bhzCementDetailTable.dataSource = res.result.records
          }
        })
        .finally(() => {
          this.bhzCementDetailTable.loading = false
        })
    },
    getDeal(){
      let params = { batchNo: this.batchNo }
      getAction(this.url.list2, params).then((res) => {
        if (res.success) {
          console.log(res.result.records[0]);
          if (res.result.records.length > 0) {
            this.dealObj = res.result.records[0]
          }
        }
      })
    },
    handleData() {
      this.detailList = []
      this.detailList1 = []
      this.bhzCementOverHandler = []
      let params = { baseid: this.batchNo }
      getAction(this.url.list, params).then((res) => {
        if (res.success) {
          if (res.result.records.length > 0) {
            this.bhzCementOverHandler = res.result.records[0]
            this.detailList = this.bhzCementOverHandler.filePath2 ? this.bhzCementOverHandler.filePath2.split(',') : []
            this.detailList1 = this.bhzCementOverHandler.filePath ? this.bhzCementOverHandler.filePath.split(',') : []
          } else {
            this.$message.warn('暂无超标处理信息！')
          }
        }
      })
    },
    getMsg(batchNo) {
      this.batchNo = batchNo
      this.visible = true
      this.getDeal()
      this.getTableList()
      this.handleData()
    },
    setClass(record) {
      return record.materialeOverLevel == '1' ? 'rowOrange' : record.materialeOverLevel == '2' ? 'rowYellow' : record.materialeOverLevel == '3' ? 'rowRed' : ''
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

<style scoped>
::v-deep .rowRed {
  color: red;
}

::v-deep .rowOrange {
  color: orange;
}

::v-deep .rowYellow {
  color: yellow;
}
</style>