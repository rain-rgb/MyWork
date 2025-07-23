<template>
  <a-spin :spinning="confirmLoading">
    <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs" default-active-key="1">
      <a-tab-pane tab="压力机表信息" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="FYalijiTable.loading"
          :columns="FYalijiTable.columns"
          :dataSource="FYalijiTable.dataSource"
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
      <a-tab-pane tab="曲线图" key="2">
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
            <a-col :span="24">
              <div v-if="dataSource2.length > 0">
                <!-- <LineChartMultid
                  :title="dataSource2[0].name"
                  :data-source="dataSource2"
                ></LineChartMultid> -->
              </div>
            </a-col>
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
      </a-tab-pane>
    </a-tabs>
    <j-form-container :disabled="formDisabled">
      <!--     主表单区域 -->
      <a-form :form="form" slot="detail">
        <a-card
          title="处理信息"
          :bordered="false"
          :headStyle="{ color: '#0785fd' }"
          :bodyStyle="{ padding: '10' }"
          style="margin-top: 10px"
        >
          <a-row>
            <a-col :span="12">
              <a-form-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-model="syjOverHandler.problemReasons"
                  placeholder="无"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="syjOverHandler.handleWay" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="syjOverHandler.handleResult" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="syjOverHandler.handleTime" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处置人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="syjOverHandler.handlePerson" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="监理驳回原因"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
              >
                <a-input v-model="syjOverHandler.remark" placeholder="无"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="监理审批" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-model="syjOverHandler.supervisorApproval"
                  placeholder="无"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="监理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-model="syjOverHandler.supervisorResult"
                  placeholder="无"
                ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="监理处理时间"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
              >
                <a-input
                  v-model="syjOverHandler.supervisorHandleTime"
                  placeholder="无"
                ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input
                  v-model="syjOverHandler.approvalPerson"
                  placeholder="无"
                ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
      </a-form>
    </j-form-container>

    <div>
      <a-row>
        <a-col :span="24">
          <a-form-item label="处置附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
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
      <a-row>
        <a-col :span="24">
          <a-form-item label="审核附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
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
    </div>
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
// import LineChartMultid from '@comp/chart/YaLijiLzqx'
import LineChartMultid from "@comp/chart/YaLijiLzqxNew";

export default {
  name: "SyyLjBasesForm",
  mixins: [JEditableTableMixin],
  components: {
    JFormContainer,
    JDate,
    JDictSelectTag,
    LineChartMultid,
  },
  data() {
    return {
      detailList: [],
      detailList1: [],
      syjOverHandler: {},
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
      labelCol1: {
        xs: { span: 24 },
        sm: { span: 3 },
      },
      wrapperCol1: {
        xs: { span: 24 },
        sm: { span: 20 },
      },
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      validatorRules: {},
      refKeys: ["SyyljBases"],
      tableKeys: ["SyyljBases"],
      activeKey: "SyyljBases",
      // 压力机数据信息
      FYalijiTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: "试件序号",
            key: "sjxh",
            type: FormTypes.normal,
            width: "200px",
            placeholder: "请输入${title}",
            defaultValue: "",
          },
          // {
          //   title: '实际配比',
          //   key: 'shijipb',
          //   type: FormTypes.normal,
          //   width:"200px",
          //   placeholder: '请输入${title}',
          //   defaultValue:'',
          // },
          // {
          //   title: '理论配比',
          //   key: 'lilunpb',
          //   type: FormTypes.normal,
          //   width:"200px",
          //   placeholder: '请输入${title}',
          //   defaultValue:'',
          // },
          // {
          //   title: '制件编号',
          //   key: 'zzjj',
          //   type: FormTypes.normal,
          //   width:"200px",
          //   placeholder: '请输入${title}',
          //   defaultValue:'',
          // },
          {
            title: "抗压力值",
            key: "kylz",
            type: FormTypes.normal,
            width: "200px",
            placeholder: "请输入${title}",
            defaultValue: "",
          },
          {
            title: "抗压强度",
            key: "kyqd",
            type: FormTypes.normal,
            width: "200px",
            placeholder: "请输入${title}",
            defaultValue: "",
          },
          {
            title: "试验时间",
            key: "sysj",
            type: FormTypes.normal,
            width: "200px",
            placeholder: "请输入${title}",
            defaultValue: "",
          },

          {
            title: "完成时间",
            type: FormTypes.normal,
            key: "wcsj",
            width: "200px",
            placeholder: "请输入${title}",
            defaultValue: "",
          },
        ],
      },
      url: {
        add: "/syj/tSyjzb/add",
        edit: "/syj/tSyjzb/edit",
        queryById: "/syj/tSyjzb/queryById",
        SyyljBases: {
          list: "/syj/tSyjzb/selectById",
        },
        list: "/syj/tSyjzb/selectById",
        list1: "/syjoverhandler/syjOverHandler/list",
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
    addBefore() {
      this.form.resetFields();
      this.FYalijiTable.dataSource = [];
    },
    getAllTable() {
      let values = this.tableKeys.map((key) => getRefPromise(this, key));
      return Promise.all(values);
    },
    /** 调用完edit()方法之后会自动调用此方法 */
    editAfter() {
      let fieldval = pick(this.model); //,'syjid','sbbh','sylx','sjbh','zzrq','syrq','lq','sjcc','sjsl','sjqd','qddbz','pdjg','czry','cjmc'
      this.$nextTick(() => {
        this.form.setFieldsValue(fieldval);
      });
      // 加载子表数据
      if (this.model.syjid) {
        let params = { syjid: this.model.syjid };
        this.requestSubTableData(this.url.SyyljBases.list, params, this.FYalijiTable);
        this.lzqx(); //力值过程值曲线图
        this.getdealdate(); //处理信息
      }
    },
    /** 整理成formData */
    classifyIntoFormData(allValues) {
      let main = Object.assign(this.model, allValues.formValue);
      return {
        ...main, // 展开
        FYalijiTableList: allValues.tablesValue[0].values,
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
      this.form.setFieldsValue(pick(row)); //,'syjid','sbbh','sylx','sjbh','zzrq','syrq','lq','sjcc','sjsl','sjqd','qddbz','pdjg','czry','cjmc'
    },
    getdealdate: function () {
      this.detailList = [];
      this.detailList1 = [];
      this.syjOverHandler = {};
      let params = { baseid: this.model.syjid };
      getAction(this.url.list1, params).then((res) => {
        if (res.success) {
          let data = res.result.records;
          console.log("data", data);
          if (data.length > 0) {
            this.syjOverHandler = data[0];
            //console.log("this.syjOverHandler",this.syjOverHandler)
            if (this.syjOverHandler.filePath2 !== null) {
              var filePath1 = this.syjOverHandler.filePath2.split(",");
              filePath1.forEach((re) => {
                this.detailList.push({ icon: re });
              });
              //console.log('filePath1', filePath1)
            }
            if (this.syjOverHandler.filePath !== null) {
              var filePath3 = this.syjOverHandler.filePath.split(",");
              filePath3.forEach((re) => {
                this.detailList1.push({ icon: re });
              });
            }
          }
        }
      });
    },
    lzqx: function () {
      //力值过程值曲线图
      let params = { syjid: this.model.syjid };
      getAction(this.url.list, params).then((res) => {
        this.dataSource1 = [];
        this.dataSource2 = [];
        this.dataSource3 = [];
        this.dataSource4 = [];
        this.dataSource5 = [];
        this.dataSource6 = [];
        if (res.success) {
          let data = res.result;
          if (data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              if (i === 0) {
                var yskylz1 = data[i].yskylz.split(",");
                var sjgc1 = data[i].sjgc.split(",");
                for (let j = 0; j < yskylz1.length; j++) {
                  this.dataSource1.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(yskylz1[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                }
              }
              if (i === 1) {
                var yskylz2 = data[i].yskylz.split(",");
                var sjgc2 = data[i].sjgc.split(",");
                for (let j = 0; j < yskylz2.length; j++) {
                  this.dataSource2.push({
                    type: parseFloat(sjgc2[j]),
                    抗压力值: parseFloat(yskylz2[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                }
              }
              if (i === 2) {
                var yskylz3 = data[i].yskylz.split(",");
                var sjgc3 = data[i].sjgc.split(",");
                for (let j = 0; j < yskylz3.length; j++) {
                  this.dataSource3.push({
                    type: parseFloat(sjgc3[j]),
                    抗压力值: parseFloat(yskylz3[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                }
              }
              if (i === 3) {
                var yskylz4 = data[i].yskylz.split(",");
                var sjgc4 = data[i].sjgc.split(",");
                for (let j = 0; j < yskylz4.length; j++) {
                  this.dataSource4.push({
                    type: parseFloat(sjgc4[j]),
                    抗压力值: parseFloat(yskylz4[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                }
              }
              if (i === 4) {
                var yskylz5 = data[i].yskylz.split(",");
                var sjgc5 = data[i].sjgc.split(",");
                for (let j = 0; j < yskylz5.length; j++) {
                  this.dataSource5.push({
                    type: parseFloat(sjgc5[j]),
                    抗压力值: parseFloat(yskylz5[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                }
              }
              if (i === 5) {
                var yskylz6 = data[i].yskylz.split(",");
                var sjgc6 = data[i].sjgc.split(",");
                for (let j = 0; j < yskylz6.length; j++) {
                  this.dataSource6.push({
                    type: parseFloat(sjgc6[j]),
                    抗压力值: parseFloat(yskylz6[j]),
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
            //console.log(this.dataSource);
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
