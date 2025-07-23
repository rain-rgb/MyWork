<template>
  <j-modal
    :title="title"
    :width="1400"
    :visible="visible"
    :maskClosable="false"
    switchFullscreen
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="红外光谱仪详情数据">
        <j-form-container disabled>
          <a-form>
            <a-row>
              <a-col :span="12">
                <a-form-item label="设备归属" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.customer" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="匹配情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-if="data.result == '0'" placeholder="匹配"></a-input>
                  <a-input v-else-if="data.result == '1'" placeholder="不匹配"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.projectname" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="沥青种类" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.product" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="车号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.grade" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="标段" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.source" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <!-- <a-row>
              <a-col :span="12">
                <a-form-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.roadstation" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="告警内容" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="data.alarminfo" placeholder="无"></a-input>
                </a-form-item>
              </a-col>
            </a-row> -->
          </a-form>
        </j-form-container>
      </a-tab-pane>
    </a-tabs>
    <a-form>
      <j-form-container disabled>
        <a-card
          title="施工方处置信息"
          :bordered="false"
          :headStyle="{ color: '#0785fd' }"
          :bodyStyle="{ padding: '10' }"
          style="margin-top: 10px"
        >
          <a-row>
            <a-col :span="12">
              <a-form-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-textarea
                  placeholder="无"
                  v-model="overHandlerObj.problemReasons"
                  :auto-size="{ minRows: 5, maxRows: 20 }"
                ></a-textarea>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.handleWay" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.handleResult" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.handleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处置人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.handlePerson" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="处置附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <viewer :images="detailList">
                  <img
                    v-for="(img, index) in detailList"
                    :key="index"
                    style="height: 100px; width: 100px; margin: 5px 10px 5px 10px; float: left"
                    :src="img"
                    alt=""
                  />
                </viewer>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card
          title="监理单位审核"
          :bordered="false"
          :headStyle="{ color: '#0785fd' }"
          :bodyStyle="{ padding: '10' }"
          style="margin-top: 10px"
        >
          <a-row>
            <a-col :span="12">
              <a-form-item label="监理审核" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-if="overHandlerObj.overproofStatus === 30"
                  v-model="overHandlerObj.remark"
                  placeholder="无"
                ></a-input>
                <a-input v-else v-model="overHandlerObj.supervisorApproval" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="审核结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-if="overHandlerObj.overproofStatus === 30" placeholder="驳回" default-value="驳回"></a-input>
                <a-input v-else v-model="overHandlerObj.supervisorResult" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.approvalPerson" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="监理处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.supervisorHandleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="24">
              <a-form-item label="审核附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <viewer :images="detailList1">
                  <img
                    v-for="(img, index) in detailList1"
                    :key="index"
                    style="height: 100px; width: 100px; margin: 5px 10px 5px 10px; float: left"
                    :src="img"
                    alt=""
                  />
                </viewer>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card
          title="指挥部核查"
          :bordered="false"
          :headStyle="{ color: '#0785fd' }"
          :bodyStyle="{ padding: '10' }"
          style="margin-top: 10px"
        >
          <a-row>
            <a-col :span="12">
              <a-form-item label="指挥部核查" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-if="
                    overHandlerObj.overproofStatus === 60 ||
                    (overHandlerObj.overproofStatus !== 20 &&
                      overHandlerObj.headquartersRemark !== null &&
                      overHandlerObj.headquartersRemark !== undefined)
                  "
                  v-model="overHandlerObj.headquartersRemark"
                ></a-input>
                <a-input v-else v-model="overHandlerObj.headquartersApproval" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="核查结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-if="
                    overHandlerObj.overproofStatus === 60 ||
                    (overHandlerObj.overproofStatus !== 20 &&
                      overHandlerObj.headquartersRemark !== null &&
                      overHandlerObj.headquartersRemark !== undefined)
                  "
                  placeholder="驳回"
                  default-value="驳回"
                ></a-input>
                <a-input v-else v-model="overHandlerObj.headquartersResult" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="核查人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.headquartersPerson" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="核查时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="overHandlerObj.headquartersHandleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
      </j-form-container>
    </a-form>

    <g-ch-modal ref="gch"></g-ch-modal>
  </j-modal>
</template>

<script>
import { getAction } from '@api/manage'
import GChModal from '@/views/zlyj/modules/GChModal'
export default {
  name: 'TrZhanglaMModalChuZhiShenHeTwo',
  components: {
    GChModal,
  },
  data() {
    return {
      data: {},
      xList: [],
      yList1: [],
      yList2: [],
      yList3: [],
      loading: false,
      ipagination: false,
      title: '',
      width: 800,
      visible: false,
      disableSubmit: true,
      id: '',
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
      overHandlerObj: {},
      detailList: [],
      detailList1: [],
      url: {
        list: '/syjoverhandler/syjOverHandler/list',
      },
    }
  },
  methods: {
    overHandler() {
      this.overHandlerObj = {}
      this.detailList = []
      this.detailList1 = []
      let param = { baseid: this.id }
      getAction(this.url.list, param).then((res) => {
        if (res.success) {
          if (res.result.records.length > 0) {
            this.overHandlerObj = res.result.records[0]
            this.detailList = this.overHandlerObj.filePath2 ? this.overHandlerObj.filePath2.split(',') : []
            this.detailList1 = this.overHandlerObj.filePath ? this.overHandlerObj.filePath.split(',') : []
          }
        }
      })
    },
    callchuzhishenhe(e) {
      this.id = e.id
      this.visible = true
      // this.zhanglamessage()
      this.overHandler()
      this.data = e
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
</style>
