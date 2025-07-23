<template>
  <a-spin :spinning="confirmLoading">
    <a-tabs default-active-key="1" @change="callback">
      <a-tab-pane key="1" tab="超标处置审核信息">
        <a-table
          rowKey="id"
          :pagination="ipagination"
          :columns="columns2"
          :data-source="data2"
          bordered
          :customRow="handleClick"
          :rowClassName="setRowClassName"
        >
          <span slot="status" slot-scope="status, record">
            <a-tag color="green" v-if="record.overproofStatus == '0'">未处理</a-tag>
            <a-tag color="orange" v-if="record.overproofStatus == '10'"
              >施工方已处置</a-tag
            >
            <a-tag color="blue" v-if="record.overproofStatus == '20'">监理方已审核</a-tag>
            <a-tag color="red" v-if="record.overproofStatus == '30'">监理方驳回</a-tag>
          </span>
          <span slot="filePath" slot-scope="filePath, record">
            <img width="100px" height="100px" :src="record.filePath" />
          </span>
          <span slot="filePath2" slot-scope="filePath2, record">
            <img width="100px" height="100px" :src="record.filePath2" />
          </span>
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="2" tab="试件详细数据" force-render>
        <j-editable-table
          :ref="refKeys[0]"
          :loading="bhzSwCailiaoTable.loading"
          :columns="bhzSwCailiaoTable.columns"
          :dataSource="bhzSwCailiaoTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="false"
        >
          <template v-slot:action="props">
            <a-tag color="green" v-if="props.value == '0'">合格</a-tag>
            <a-tag color="orange" v-if="props.value == '1'">初级超标</a-tag>
            <a-tag color="yellow" v-if="props.value == '2'">中级超标</a-tag>
            <a-tag color="red" v-if="props.value == '3'">高级超标</a-tag>
          </template>
        </j-editable-table>
      </a-tab-pane>
    </a-tabs>

    <div>
      <a-row>
        <a-col :span="24">
          <div v-if="dataSource1.length > 0">
            <!-- <LineChartMultid
              :title="dataSource1[0].name"
              :data-source="dataSource1"
            ></LineChartMultid> -->
            <LineChartMultid
              :title="dataSource1[0].name"
              :data-source="dataList"
            ></LineChartMultid>
          </div>
        </a-col>
        <!-- <a-col :span="24">
          <div v-if="dataSource2.length > 0">
            <LineChartMultid
              :title="dataSource2[0].name"
              :data-source="dataSource2"
            ></LineChartMultid>
          </div>
        </a-col> -->
        <!-- <a-col :span="24">
          <div v-if="dataSource3.length > 0">
            <LineChartMultid
              :title="dataSource3[0].name"
              :data-source="dataSource3"
            ></LineChartMultid>
          </div>
        </a-col>
        <a-col :span="24">
          <div v-if="dataSource4.length > 0">
            <LineChartMultid
              :title="dataSource4[0].name"
              :data-source="dataSource4"
            ></LineChartMultid>
          </div>
        </a-col>
        <a-col :span="24">
          <div v-if="dataSource5.length > 0">
            <LineChartMultid
              :title="dataSource5[0].name"
              :data-source="dataSource5"
            ></LineChartMultid>
          </div>
        </a-col>
        <a-col :span="24">
          <div v-if="dataSource6.length > 0">
            <LineChartMultid
              :title="dataSource6[0].name"
              :data-source="dataSource6"
            ></LineChartMultid>
          </div>
        </a-col> -->
      </a-row>
    </div>
    <!--      </a-tab-pane>-->
    <!--    </a-tabs>-->
  </a-spin>
</template>

<script>
import pick from "lodash.pick";
import { getAction } from "@/api/manage";
import { FormTypes, getRefPromise } from "@/utils/JEditableTableUtil";
import { JEditableTableMixin } from "@/mixins/JEditableTableMixin";
import { validateDuplicateValue } from "@/utils/util";
import JFormContainer from "@/components/jeecg/JFormContainer";
import JDate from "@/components/jeecg/JDate";
import JDictSelectTag from "@/components/dict/JDictSelectTag";
// import LineChartMultid from "@comp/chart/YaLijiLzqx";
import LineChartMultid from "@comp/chart/YaLijiLzqxNew";

export default {
  name: "WnjCBFrom",
  mixins: [JEditableTableMixin],
  components: {
    JFormContainer,
    JDate,
    JDictSelectTag,
    LineChartMultid,
  },
  data() {
    return {
      ipagination: false,
      columns2: [
        {
          title: "问题原因",
          align: "center",
          dataIndex: "problemReasons",
        },
        {
          title: "处理方式",
          align: "center",
          dataIndex: "handleWay",
        },
        {
          title: "处理结果",
          align: "center",
          dataIndex: "handleResult",
        },
        {
          title: "处理时间",
          align: "center",
          dataIndex: "handleTime",
        },
        {
          title: "处理人",
          align: "center",
          dataIndex: "handlePerson",
        },
        {
          title: "监理审批",
          align: "center",
          dataIndex: "supervisorApproval",
        },
        {
          title: "监理结果",
          align: "center",
          dataIndex: "supervisorResult",
        },
        {
          title: "监理处理时间",
          align: "center",
          dataIndex: "supervisorHandleTime",
        },
        {
          title: "审批人",
          align: "center",
          dataIndex: "approvalPerson",
        },
        {
          title: "驳回原因",
          align: "center",
          dataIndex: "remark",
        },
        {
          title: "审核照片",
          align: "center",
          dataIndex: "filePath",
          scopedSlots: { customRender: "filePath" },
        },
        {
          title: "处置照片",
          align: "center",
          dataIndex: "filePath2",
          scopedSlots: { customRender: "filePath2" },
        },
        {
          title: "状态",
          align: "center",
          dataIndex: "overproofStatus",
          scopedSlots: { customRender: "status" },
        },
      ],
      data2: [],
      dataSource1: [],
      dataSource2: [],
      dataSource3: [],
      dataSource4: [],
      dataSource5: [],
      dataSource6: [],
      dataList: {
        dataSource1: [],
        dataSource2: [],
        dataSource3: [],
        dataSource4: [],
        dataSource5: [],
        dataSource6: [],
      },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      labelCol2: {
        xs: { span: 24 },
        sm: { span: 3 },
      },
      wrapperCol2: {
        xs: { span: 24 },
        sm: { span: 20 },
      },
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      validatorRules: {},
      refKeys: ["SyyljBases"],
      tableKeys: ["SyyljBases"],
      activeKey: "SyyljBases",
      // 万能机表信息
      bhzSwCailiaoTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: "原始标距",
            key: "wsbz",
            type: FormTypes.normal,
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "断后标距",
            key: "dhbz",
            type: FormTypes.normal,
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "最大力值",
            key: "lz",
            type: FormTypes.normal,
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "抗拉强度",
            key: "lzqd",
            type: FormTypes.normal,
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "屈服力值",
            key: "qflz",
            type: FormTypes.normal,
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "屈服强度",
            type: FormTypes.normal,
            key: "qfqd",
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "伸长率",
            type: FormTypes.normal,
            key: "scl",
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "试验时间",
            type: FormTypes.normal,
            key: "sysj",
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "最大力值伸长率",
            type: FormTypes.normal,
            key: "zdlzscl",
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "拉断处描述",
            type: FormTypes.normal,
            key: "ldcms",
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "断口位置",
            type: FormTypes.normal,
            key: "dkwz",
            width: "200px",
            placeholder: "请输入${title}",
          },
        ],
      },
      url: {
        add: "/syj/tSyjzb/add",
        edit: "/syj/tSyjzb/edit",
        queryById: "/syj/tSyjzb/queryById",
        SyyljBases: {
          list: "/syj/tSyjzb/selectwnjById",
        },
        list: "/syj/tSyjzb/selectwnjById",
        chuzhishenhes: "/syjoverhandler/syjOverHandler/list",
      },
    };
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {},
      required: false,
    },
    //表单模式：false流程表单 true普通表单
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
    chuzhishenhemessage(syjid) {
      let param = { baseid: syjid };
      getAction(this.url.chuzhishenhes, param).then((res) => {
        if (res.result.records.length > 0) {
          this.data2 = res.result.records;
        } else {
          this.$message.warning("暂无处置审核信息");
        }
      });
    },
    callback(key) {},
    addBefore() {
      this.form.resetFields();
      this.bhzSwCailiaoTable.dataSource = [];
    },
    getAllTable() {
      let values = this.tableKeys.map((key) => getRefPromise(this, key));
      return Promise.all(values);
    },
    /** 调用完edit()方法之后会自动调用此方法 */
    editAfter() {
      let fieldval = pick(this.model); //,'sbbh_dictText','sylx','wtbh','zzrq','syrq','lq','sjcc','sjmj','sjsl','sjqd','zsxs','qddbz','pdjg','czry','cjmc','pzbm','gczj','area','fbl','beizhu','sjscsj'
      this.$nextTick(() => {
        this.form.setFieldsValue(fieldval);
      });
      // 加载子表数据
      if (this.model.syjid) {
        let params = { syjid: this.model.syjid };
        this.requestSubTableData(
          this.url.SyyljBases.list,
          params,
          this.bhzSwCailiaoTable
        );
        this.chuzhishenhemessage(this.model.syjid);
        this.lzqx(); //力值过程值曲线图
      }
    },
    /** 整理成formData */
    classifyIntoFormData(allValues) {
      let main = Object.assign(this.model, allValues.formValue);
      return {
        ...main, // 展开
        bhzSwCailiaoList: allValues.tablesValue[0].values,
      };
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
    validateError(msg) {
      this.$message.error(msg);
    },
    popupCallback(row) {
      // this.form.setFieldsValue(pick(row,'shebeibianhao','shiyanleixing','weituobianhao','zhijianriqi','shiyanriqi','lingqi','shijianchicun','shijianmianji','shiianshuliang','shejiqiangdu','zhesuanxishu','qiangdudaibiaozhi','pandingjieguo','caozuorenyuan','gongchengmingcheng','pinzhongbianma','gongchengzhijing','chengyamianji','shengchanchangjia','beizhu','shujushangchuanshijian'))
      this.form.setFieldsValue(pick(row)); //,'sbbh_dictText','sylx','wtbh','zzrq','syrq','lq','sjcc','sjmj','sjsl','sjqd','zsxs','qddbz','pdjg','czry','cjmc','pzbm','gczj','area','fbl','beizhu','sjscsj'
    },
    lzqx: function () {
      //力值过程值曲线图
      let params = { syjid: this.model.syjid };
      this.dataSource1 = [];
      this.dataSource2 = [];
      this.dataSource3 = [];
      this.dataSource4 = [];
      this.dataSource5 = [];
      this.dataSource6 = [];
      getAction(this.url.list, params).then((res) => {
        if (res.success) {
          let data = res.result;
          if (data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              var lzgc1 = data[i].lzgc.split(",");
              var sjgc1 = data[i].sjgc.split(",");
              for (let j = 0; j < lzgc1.length; j++) {
                if (i === 0) {
                  this.dataSource1.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(lzgc1[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                }
                if (i === 1) {
                  this.dataSource2.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(lzgc1[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                }
                if (i === 2) {
                  this.dataSource3.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(lzgc1[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                }
                if (i === 3) {
                  this.dataSource4.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(lzgc1[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                }
                if (i === 4) {
                  this.dataSource5.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(lzgc1[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                }
                if (i === 5) {
                  this.dataSource6.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(lzgc1[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                }
              }
            }
            this.dataList.dataSource1 = this.dataSource1;
            this.dataList.dataSource2 = this.dataSource2;
            this.dataList.dataSource3 = this.dataSource3;
            this.dataList.dataSource4 = this.dataSource4;
            this.dataList.dataSource5 = this.dataSource5;
            this.dataList.dataSource6 = this.dataSource6;
          } else {
            this.$message.error("该设备的抗压力值没有数据");
          }
        }
      });
    },
  },
};
</script>

<style scoped></style>
