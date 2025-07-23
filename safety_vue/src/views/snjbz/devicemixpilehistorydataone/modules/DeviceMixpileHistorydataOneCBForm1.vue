<template>
  <a-spin :spinning="confirmLoading">
    <a-card
      title="基础信息"
      :bordered="false"
      :headStyle="{ color: '#0785fd' }"
      :bodyStyle="{ padding: '10' }"
      style="margin-top: 10px"
    >
      <div v-if="showTable">
        <div class="a-table-title">里程:{{ dataTableS[0].pileMileage }}</div>
        <a-table
          :columns="columnsTa"
          :data-source="dataTableS"
          bordered
          :loading="loading"
          size="middle"
          :pagination="false"
          :scroll="{ x: 'calc(945px + 50%)' }"
        >
          <span slot="titleScore" slot-scope="text, record">
            桩长：{{ dataTableS[0].pileDesigndep }}
          </span>
          <span slot="titleScore1" slot-scope="text, record">
            桩号：{{ dataTableS[0].pileNo }}
          </span>
          <span slot="jiluDate" slot-scope="text, record">
            记录日期:{{ dataTableS[0].datatime }}
          </span>
          <span slot="shb" slot-scope="text, record">
            水灰比：{{ Number(dataTableS[0].pileRatio).toFixed(3) }}
          </span>
          <span slot="sbName" slot-scope="text, record">
            设备名称：{{ dataTableS[0].shebeino }}
          </span>
          <span slot="gzcount" slot-scope="text, record">
            <span v-if="record.gzcount == 1">初搅</span>
            <span v-if="record.gzcount == 2">复搅</span>
          </span>
          <span slot="zjl" slot-scope="text, record">
            {{ (Number(record.pileDownbeton) + Number(record.pileUobeton)) * 1000 }}
          </span>
        </a-table>
      </div>
    </a-card>
    <!-- <j-form-container> -->
    <j-form-container>
      <a-form :form="form" slot="detail">
        <a-card
          title="处理信息"
          :bordered="false"
          :headStyle="{ color: '#0785fd' }"
          :bodyStyle="{ padding: '10' }"
          style="margin-top: 10px"
        >
          <div class="j-form">
            <div class="j-form-item">
              <div class="title">施工处置</div>
              <div class="j-form-item-box">
                <a-row>
                  <a-col :span="24">
                    <a-form-item
                      label="问题原因"
                      :labelCol="labelCol"
                      :wrapperCol="wrapperCol"
                    >
                      <a-input
                       :disabled="formDisabled"
                        v-model="handlemodel.problemReasons"
                        placeholder="无"
                      ></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-item
                      label="处理方式"
                      :labelCol="labelCol"
                      :wrapperCol="wrapperCol"
                    >
                      <a-input
                       :disabled="formDisabled" v-model="handlemodel.handleWay" placeholder="无"></a-input>
                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row>
                  <a-col :span="24">
                    <a-form-item
                      label="处理结果"
                      :labelCol="labelCol"
                      :wrapperCol="wrapperCol"
                    >
                      <a-input
                       :disabled="formDisabled"
                        v-model="handlemodel.handleResult"
                        placeholder="无"
                      ></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-item
                      label="处理时间"
                      :labelCol="labelCol"
                      :wrapperCol="wrapperCol"
                    >
                      <a-input
                       :disabled="formDisabled"
                        v-model="handlemodel.handleTime"
                        placeholder="无"
                      ></a-input>
                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row>
                  <a-col :span="24">
                    <a-form-item
                      label="处置人"
                      :labelCol="labelCol"
                      :wrapperCol="wrapperCol"
                    >
                      <a-input
                       :disabled="formDisabled"
                        v-model="handlemodel.handlePerson"
                        placeholder="无"
                      ></a-input>
                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row>
                  <a-col :span="24" style="marginLeft:55px">
                    <a-form-item
                      label="处置附件"
                      :labelCol="labelCol1"
                      :wrapperCol="wrapperCol1"
                    >
                      <viewer :images="detailList">
                        <img
                          v-for="(item, index) in detailList"
                          :key="index"
                          style="
                            height: 100px;
                            width: 100px;
                            margin: 5px 10px 5px 10px;
                            float: left;
                          "
                          :src="[item.icon]"
                          alt=""
                        />
                      </viewer>
                    </a-form-item>
                  </a-col>
                </a-row>
              </div>
            </div>
            <div class="j-form-item">
              <div class="title">监理审核</div>
              <div class="j-form-item-box">
                <a-row>
                  <a-col :span="24" v-if="handlemodel.overproofStatus == 30">
                    <a-form-item
                      label="监理驳回原因"
                      :labelCol="labelCol"
                      :wrapperCol="wrapperCol"
                    >
                      <a-input
                       :disabled="formDisabled" v-model="handlemodel.remark" placeholder="无"></a-input>
                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row>
                  <a-col :span="24">
                    <a-form-item
                      label="监理审批"
                      :labelCol="labelCol"
                      :wrapperCol="wrapperCol"
                    >
                      <a-input
                       :disabled="formDisabled"
                        v-model="handlemodel.supervisorApproval"
                        placeholder="无"
                      ></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-item
                      label="监理结果"
                      :labelCol="labelCol"
                      :wrapperCol="wrapperCol"
                    >
                      <a-input
                       :disabled="formDisabled"
                        v-model="handlemodel.supervisorResult"
                        placeholder="无"
                      ></a-input>
                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row>
                  <a-col :span="24">
                    <a-form-item
                      label="监理处理时间"
                      :labelCol="labelCol"
                      :wrapperCol="wrapperCol"
                    >
                      <a-input
                       :disabled="formDisabled"
                        v-model="handlemodel.supervisorHandleTime"
                        placeholder="无"
                      ></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-item
                      label="审核人"
                      :labelCol="labelCol"
                      :wrapperCol="wrapperCol"
                    >
                      <a-input
                       :disabled="formDisabled"
                        v-model="handlemodel.approvalPerson"
                        placeholder="无"
                      ></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :span="24" style="marginLeft:55px">
                    <a-form-item
                      label="审核附件"
                      :labelCol="labelCol1"
                      :wrapperCol="wrapperCol1"
                    >
                      <viewer :images="detailList1">
                        <img
                          v-for="(item, index) in detailList1"
                          :key="index"
                          style="
                            height: 100px;
                            width: 100px;
                            margin: 5px 10px 5px 10px;
                            float: left;
                          "
                          :src="[item.icon]"
                          alt=""
                        />
                      </viewer>
                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row>
                </a-row>
              </div>
            </div>
          </div>
        </a-card>
      </a-form>
    </j-form-container>
    <!-- <a-tabs v-model="activeKey">
      <a-tab-pane tab="分段数据" :key="refKeys[0]" :forceRender="true">
        <a-table
          rowKey="id"
          :columns="columns"
          :dataTableS="dataTableS"
          :loading="loading"
          :pagination="ipagination"
          class="j-table-force-nowrap"
          @change="tableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >
        </a-table>
      </a-tab-pane>
    </a-tabs> -->
  </a-spin>
</template>

<script>
import { httpAction, getAction } from "@/api/manage";
import pick from "lodash.pick";
import { validateDuplicateValue } from "@/utils/util";
import JFormContainer from "@/components/jeecg/JFormContainer";
import JDate from "@/components/jeecg/JDate";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { mixinDevice } from "@/utils/mixin";
import { FormTypes } from "@/utils/JEditableTableUtil";

export default {
  name: "DeviceMixpileHistorydataOneCBForm",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    JFormContainer,
    JDate,
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {},
      required: false,
    },
    //表单模式：true流程表单 false普通表单
    formBpm: {
      type: Boolean,
      default: false,
      required: false,
    },
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      titleTest: "测试表头",
      loading: true,
      showTable: false,
      columnsTa: [
        {
          scopedSlots: { customRender: "serial", title: "titleScore" },
          fixed: "left",
          width: 200,
          children: [
            {
              title: "超标原因",
              dataIndex: "problem",
              align: "center",
            },
            {
              title: "实际桩长(m)",
              dataIndex: "pileRealdep",
              align: "center",
            },
          ],
        },
        {
          scopedSlots: { customRender: "serial", title: "titleScore1" },
          fixed: "left",
          width: 100,
          children: [
            {
              title: "搅拌",
              dataIndex: "gzcount",
              align: "center",
              scopedSlots: { customRender: "gzcount" },
            },
          ],
        },
        {
          // title: "记录日期：",
          // width: 300,
          scopedSlots: { title: "jiluDate" },
          align: "center",
          children: [
            {
              title: "成桩时间",
              children: [
                {
                  title: "开始时间",
                  dataIndex: "pileStarttime",
                  align: "center",
                },
                {
                  title: "结束时间",
                  dataIndex: "pileTime",
                  align: "center",
                },
              ],
            },
            {
              title: "下钻",
              children: [
                {
                  title: "平均速度 (m/min)",
                  dataIndex: "pileDspeed",
                  align: "center",
                },
                {
                  title: "平均电流(A)",
                  dataIndex: "pileDelectr",
                  align: "center",
                },
              ],
            },
          ],
        },
        {
          // title: "水灰比：",
          // width: 300,
          scopedSlots: { title: "shb" },
          align: "center",
          children: [
            {
              title: "提钻",
              children: [
                {
                  title: "平均速度(m/min)",
                  dataIndex: "pileUspeed",
                  align: "center",
                },
                {
                  title: "平均电流(A)",
                  dataIndex: "pileUelectr",
                  align: "center",
                },
              ],
            },
            {
              title: "垂直度(°)",
              dataIndex: "pileY",
              align: "center",
            },
            {
              title: "浆液比重(g/cm³)",
              dataIndex: "pileDensity",
              align: "center",
            },
          ],
        },
        {
          // title: "设备名称:",
          // width: 300,
          scopedSlots: { title: "sbName" },
          align: "center",
          children: [
            {
              title: "喷浆深度（m）",
              dataIndex: "pjdep",
              align: "center",
            },
            {
              title: "总浆量(L)",
              dataIndex: "",
              scopedSlots: { customRender: "zjl" },
              align: "center",
            },
            {
              title: "每米水泥用量(Kg/m)",
              dataIndex: "pileMinelec",
              align: "center",
            },
            {
              title: "水泥用量(Kg)",
              dataIndex: "pileCement",
              align: "center",
            },
            {
              title: "峰值电流(A)",
              dataIndex: "pileMaxelec",
              align: "center",
            },
          ],
        },
      ],
      dataTAble: [],
      detailList: [],
      detailList1: [],
      labelCol1: {
        xs: { span: 24 },
        sm: { span: 3 },
      },
      wrapperCol1: {
        xs: { span: 24 },
        sm: { span: 20 },
      },
      handlemodel: {},
      form: this.$form.createForm(this),
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
      refKeys: ["partdata"],
      tableKeys: ["partdata"],
      activeKey: "partdata",
      dataTableS: [],
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ["10", "20", "30"],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      columns: [
        //供应商名称	进场时间	出场时间	磅单号	车牌号	司磅员	毛重（吨）	皮重（吨）	净重（吨）
        {
          title: "段号",
          dataIndex: "partPilec",
        },
        {
          title: "段深度",
          dataIndex: "partDep",
        },
        {
          title: "段浆量",
          dataIndex: "partBeton",
          type: FormTypes.normal,
        },
        {
          title: "段速度",
          dataIndex: "partSpeed",
          type: FormTypes.normal,
        },
        {
          title: "段时长",
          dataIndex: "partTime",
          type: FormTypes.normal,
        },
        {
          title: "段x",
          dataIndex: "partX",
          type: FormTypes.normal,
        },
        {
          title: "段y",
          dataIndex: "partY",
          type: FormTypes.normal,
        },
        {
          title: "段状态",
          dataIndex: "partState_dictText",
          type: FormTypes.normal,
        },
        {
          title: "段结束时间",
          dataIndex: "partEndtime",
          type: FormTypes.normal,
        },
      ],

      url: {
        add: "/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/add",
        edit: "/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/edit",
        queryById: "/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/queryById",
        partdata: "/devicemixpilehistorydatapart/deviceMixpileHistorydataPart/list",
        list: "/mixpileoneoverhandler/mixpileOneOverHandler/list",
        listTable:
          "/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/queryInfoList",
      },
    };
  },
  computed: {
    formDisabled() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return false;
        }
        return true;
      }
      return this.disabled;
    },
    showFlowSubmitButton() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true;
        }
      }
      return false;
    },
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData();
  },
  methods: {
    tableChange(pagination) {
      this.ipagination.current = pagination.current;
      this.ipagination.pageSize = pagination.pageSize;
      this.partdatas();
    },
    add() {
      this.edit({});
    },
    edit(record) {
      this.showTable = false;
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.getData();

      this.gethandledata();
      this.$nextTick(() => {
        this.form.setFieldsValue(
          pick(
            this.model,
            "shebeino_dictText",
            "pileDesigndep",
            "pileRealdep",
            "pileX",
            "pileY",
            "pileMaxelectr",
            "grouting",
            "pileNo",
            "designratio",
            "designgrout",
            "cement",
            "water",
            "pileRatio",
            "serial",
            "username",
            "datatime",
            "pileLgd",
            "pileLtd",
            "flowlst",
            "pileSpeed",
            "starttime",
            "endtime",
            "pileFlowtotal",
            "pileWorktime",
            "pileDownbeton",
            "pileUobeton",
            "pileDowntime",
            "pileUptime",
            "pileTime",
            "pileUspeed",
            "pileUelectr",
            "pileDspeed",
            "pileDelectr",
            "pileCement",
            "pileDensity",
            "pileMileage",
            "pileUpress",
            "pileDpress",
            "piletype",
            "problem",
            "pjdep"
          )
        );
        this.partdatas();
      });
    },
    getData() {
      let ids = "";
      let idsArr = [];
      this.dataTableS = null;
      this.model.deviceMixpileOneHandlerPages.forEach((e) => {
        idsArr.push(e.id);
      });
      ids = idsArr.toString();
      let params = {
        ids,
      };
      getAction(this.url.listTable, params).then((res) => {
        if (res.success) {
          if (res.result.length > 0) {
            this.dataTableS = res.result;
            setTimeout(() => {
              this.showTable = true;
            }, 500);
            // this.ipagination.total = res.result.total;
          } else {
            this.$message.warn("暂无超标处理信息！");
          }
        }
      });
    },
    //渲染流程表单数据
    showFlowData() {
      if (this.formBpm === true) {
        let params = { id: this.formData.dataId };
        getAction(this.url.queryById, params).then((res) => {
          if (res.success) {
            this.edit(res.result);
          }
        });
      }
    },
    submitForm() {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true;
          let httpurl = "";
          let method = "";
          if (!this.model.id) {
            httpurl += this.url.add;
            method = "post";
          } else {
            httpurl += this.url.edit;
            method = "put";
          }
          let formData = Object.assign(this.model, values);
          // console.log("表单提交数据", formData);
          httpAction(httpurl, formData, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit("ok");
              } else {
                that.$message.warning(res.message);
              }
            })
            .finally(() => {
              that.confirmLoading = false;
            });
        }
      });
    },
    partdatas: function () {
      //分段数据
      let params = {
        shebeino: this.model.shebeino,
        pileNo: this.model.pileNo,
        pageSize: this.ipagination.pageSize,
        pageNo: this.ipagination.current,
      };
      getAction(this.url.partdata, params).then((res) => {
        if (res.success) {
          // this.dataTableS = res.result.recordsTabl;
          // this.ipagination.total = res.result.total;
          // console.log(this.dataTableS, "分段数据")Tabl;
        } else {
          this.$message.error("暂无分段数据！");
        }
      });
    },
    gethandledata: function () {
      //处置信息
      let params = {
        baseid: this.model.id,
      };
      getAction(this.url.list, params).then((res) => {
        if (res.success) {
          if (res.result.records.length > 0) {
            this.handlemodel = res.result.records[0];
            if (this.handlemodel.filePath2 !== null) {
              var filePath1 = this.handlemodel.filePath2.split(",");
              filePath1.forEach((re) => {
                this.detailList.push({ icon: re });
              });
              console.log("filePath1", filePath1);
            }
            if (this.handlemodel.filePath !== null) {
              var filePath3 = this.handlemodel.filePath.split(",");
              filePath3.forEach((re) => {
                this.detailList1.push({ icon: re });
              });
            }
          } else {
            this.$message.warn("暂无超标处理信息！");
          }
        }
      });
    },
    popupCallback(row) {
      this.form.setFieldsValue(
        pick(
          row,
          "shebeino_dictText",
          "pileDesigndep",
          "pileRealdep",
          "pileX",
          "pileY",
          "pileMaxelectr",
          "grouting",
          "pileNo",
          "designratio",
          "designgrout",
          "cement",
          "water",
          "pileRatio",
          "serial",
          "username",
          "datatime",
          "pileLgd",
          "pileLtd",
          "flowlst",
          "pileSpeed",
          "starttime",
          "endtime",
          "pileFlowtotal",
          "pileWorktime",
          "pileDownbeton",
          "pileUobeton",
          "pileDowntime",
          "pileUptime",
          "pileTime",
          "pileUpress",
          "pileDpress",
          "piletype",
          "problem",
          "pjdep"
        )
      );
    },
  },
};
</script>
<style lang="less" scoped>
.a-table-title {
  height: 80px;
  font-size: 16px;
  text-align: center;
  font-weight: 500;
  color: rgba(0, 0, 0, 0.85);
  font-variant: tabular-nums;
  line-height: 80px;
  list-style: none;
  font-feature-settings: "tnum";
  background-color: #fafafa;
  border: 1px solid #e8e8e8;
  border-bottom: none;
}
.j-form {
  display: flex;
  .j-form-item {
    width: 50%;
    .title {
      color: #1d7eec;
      font-size: 16px;
      position: relative;
      top: 10px;
      left: 30px;
      width: 75px;
      background-color: #fff;
    }
    .j-form-item-box {
      min-height: 500px;
      // margin-top: 20px;
      margin-left: 20px;
      padding-top: 20px;
      border: 1px solid #1d7eec;
    }
  }
}
</style>
