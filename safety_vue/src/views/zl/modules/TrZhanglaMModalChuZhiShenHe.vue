<template>
  <j-modal
    :title="title"
    :width="1400"
    :visible="visible"
    :maskClosable="false"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-tabs default-active-key="1" @change="callback">
      <a-tab-pane key="1" tab="智能张拉处置审核信息">
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
      <a-tab-pane key="2" tab="智能张拉详情数据">
        <a-table
          rowKey="id"
          :pagination="ipagination"
          :columns="columns"
          :data-source="data"
          bordered
          :customRow="handleClick"
          :rowClassName="setRowClassName"
        >
          <span class="separate" slot="action" slot-scope="text, record">
            <div v-for="(item, index) in record.trZhanglaSList" :key="index">
              <div>{{ item.dh }}</div>
              <a-divider style="margin: 0; height: 1px" />
            </div>
          </span>
          <span class="separate" slot="jld" slot-scope="text, record">
            <div v-for="(item, index) in record.trZhanglaSList">
              <div>{{ item.jdzll }}</div>
              <a-divider style="margin: 0; height: 1px" />
            </div>
          </span>
          <span class="separate" slot="jd" slot-scope="text, record">
            <div v-for="(item, index) in record.trZhanglaSList">
              <div>{{ item.jdbfb }}</div>
              <a-divider style="margin: 0; height: 1px" />
            </div>
          </span>
          <span class="separate" slot="chsj" slot-scope="text, record">
            <div v-for="(item, index) in record.trZhanglaSList">
              <div>{{ item.chsj }}</div>
              <a-divider style="margin: 0; height: 1px" />
            </div>
          </span>
          <span class="separate" slot="jdscl" slot-scope="text, record">
            <div v-for="(item, index) in record.trZhanglaSList">
              <div>{{ item.jdscl }}</div>
              <a-divider style="margin: 0; height: 1px" />
            </div>
          </span>
          <span slot="hege" slot-scope="status, record">
            <a-tag color="green" v-if="record.trZhanglaMList.hege == '1'">合格</a-tag>
            <a-tag color="red" v-if="record.trZhanglaMList.hege == '0'">不合格</a-tag>
          </span>
        </a-table>
      </a-tab-pane>
    </a-tabs>
    <a-tabs default-active-key="1" @change="callback1">
      <a-tab-pane
        v-for="arry in data"
        :key="arry.trZhanglaMList.holeid"
        :tab="arry.gsbh + remark"
      >
        <a-table
          rowKey="id"
          :pagination="ipagination1"
          :columns="columns1"
          :data-source="data1"
          @change="tableChange"
          :customRow="handleClick"
          :rowClassName="setRowClassName"
        />
      </a-tab-pane>
    </a-tabs>
    <a-tabs default-active-key="1" @change="callback2">
      <a-tab-pane key="1" tab="智能张拉过程图">
        <LineChartsZhangla :height="height" :data-source="datadetail"></LineChartsZhangla>
      </a-tab-pane>
    </a-tabs>
  </j-modal>
</template>

<script>
import { getAction } from "@api/manage";
import LineChartsZhangla from "@views/zl/modules/LineChartsZhangla";
export default {
  name: "TrZhanglaMModal",
  components: {
    LineChartsZhangla,
  },
  data() {
    return {
      remark: "孔道",
      data: [],
      data1: [],
      data2: [],
      datas: [],
      datadetail: [],
      height: 420,
      zhanglarenwudandows: false,
      ipagination: false,
      ipagination1: {
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
      columns1: [
        {
          title: "记录时间",
          align: "center",
          dataIndex: "jlsj",
        },
        {
          title: "张拉次数",
          align: "center",
          dataIndex: "zlcs",
        },
        {
          title: "状态",
          align: "center",
          dataIndex: "zt1",
        },
        {
          title: "张拉力1(KN)",
          align: "center",
          dataIndex: "zll1",
        },
        {
          title: "油压1(Mpa)",
          align: "center",
          dataIndex: "yy1",
        },
        {
          title: "顶行程1(mm)",
          align: "center",
          dataIndex: "dxc1",
        },
        {
          title: "伸长量1(mm)",
          align: "center",
          dataIndex: "scl1",
        },
        {
          title: "状态2",
          align: "center",
          dataIndex: "zt2",
        },
        {
          title: "张拉力2(KN)",
          align: "center",
          dataIndex: "zll2",
        },
        {
          title: "油压2(Mpa)",
          align: "center",
          dataIndex: "yy2",
        },
        {
          title: "顶行程2(mm)",
          align: "center",
          dataIndex: "dxc2",
        },
        {
          title: "伸长量2(mm)",
          align: "center",
          dataIndex: "scl2",
        },
      ],

      columns: [
        {
          title: "孔号",
          align: "center",
          dataIndex: "gsbh",
        },
        {
          title: "张拉时间",
          align: "center",
          dataIndex: "zlsj",
        },
        {
          title: "张拉断面",
          align: "center",
          scopedSlots: { customRender: "action" },
        },
        {
          title: "阶段行程(%)",
          align: "center",
          scopedSlots: { customRender: "jd" },
        },
        {
          title: "伸长量(mm)",
          align: "center",
          scopedSlots: { customRender: "jdscl" },
        },
        {
          title: "张拉力(KN)",
          align: "center",
          scopedSlots: { customRender: "jld" },
        },
        {
          title: "设计张力(KN)",
          align: "center",
          dataIndex: "sjzll",
        },
        {
          title: "总伸长量(mm)",
          align: "center",
          dataIndex: "zscl",
        },
        {
          title: "设计伸长量(mm)",
          align: "center",
          dataIndex: "llscl",
        },
        {
          title: "延长量误差(%)",
          align: "center",
          dataIndex: "sclper",
        },
        {
          title: "超张百分比(%)",
          align: "center",
          dataIndex: "zzlb",
        },
        {
          title: "持荷时间(s)",
          align: "center",
          scopedSlots: { customRender: "chsj" },
        },
        {
          title: "记录时间",
          align: "center",
          dataIndex: "jlsj",
        },
        {
          title: "状态",
          align: "center",
          dataIndex: "hege",
          scopedSlots: { customRender: "hege" },
        },
      ],
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
      title: "",
      width: 800,
      visible: false,
      disableSubmits: false,
      syjid: "",
      uuid: "",
      kongList: [],
      holeid: "",
      shebeibianhao: "",
      url: {
        list: "/zhanglam/trZhanglaM/list1",
        list1: "/zhanglass/trZhanglaSS/list1", //张拉每个孔道的过程数据
        listxxb: "/zhanglaxxb/trZhanglaXxb/list2", //张拉主表信息
        chuzhishenhe: "/zhanglam/zhanglaYajiangOverHandler/list", //处置审核信息
      },
    };
  },
  methods: {
    tableChange(pagination) {
      this.ipagination1.current = pagination.current;
      this.ipagination1.pageSize = pagination.pageSize;
      this.zhanglass();
    },
    callback2(key) {
      //console.log(key)
    },
    callback(key) {
      //console.log(key)
    },
    callback1(key) {
      this.holeid = key;
      this.zhanglass();
      //console.log(key)
    },
    zhanglamessage: function () {
      //请求张拉每个孔的断面数据
      let param = { syjid: this.syjid };
      getAction(this.url.list, param).then((res) => {
        if (res.success) {
          this.data = res.result;
          this.holeid = res.result[0].trZhanglaMList.holeid;
          this.shebeibianhao = res.result[0].trZhanglaMList.shebeibianhao;
          setTimeout(this.zhanglass(), 1000);
        }
      });
    },
    zhanglass: function () {
      //张拉每个孔道的过程数据
      var datadetail = [];
      let param = {
        holeid: this.holeid,
        shebeibianhao: this.shebeibianhao,
        pageSize: this.ipagination1.pageSize,
        pageNo: this.ipagination1.current,
      };
      getAction(this.url.list1, param).then((res) => {
        if (res.success) {
          this.data1 = res.result.records;
          this.ipagination1.total = res.result.total;
          let data = res.result.records;
          data.forEach(function (item, index) {
            //,
            datadetail.push({
              type: item.jlsj,
              "张拉力1(KN)": item.zll1,
              "油压1(Mpa)": item.yy1,
              "顶行程1(mm)": item.dxc1,
              "伸长量1(mm)": item.scl1,
              "张拉力2(KN)": item.zll2,
              "油压2(Mpa)": item.yy2,
              "顶行程2(mm)": item.dxc2,
              "伸长量2(mm)": item.scl2,
            });
          });
          this.datadetail = datadetail;
          //console.log(res)
        } else {
          this.$message.warning("暂无此孔号的张拉过程!");
          this.datadetail = [];
        }
      });
    },
    handleOk() {
      this.visible = false;
    },
    call(e) {
      this.syjid = e;
      this.visible = true;
      this.zhanglamessage();
    },
    callchuzhishenhe(e) {
      this.syjid = e;
      this.visible = true;
      this.chuzhishenhemessage();
      this.zhanglamessage();
    },
    chuzhishenhemessage() {
      let param = { baseid: this.syjid };
      getAction(this.url.chuzhishenhe, param).then((res) => {
        if (res.result.records.length > 0) {
          this.data2 = res.result.records;
        } else {
          this.$message.warning("暂无处置审核信息");
        }
      });
    },
    zhanglaxxb: function () {
      let param = { uuid: this.uuid };
      getAction(this.url.listxxb, param).then((res) => {
        if (res.result.length > 0) {
          this.data2 = res.result;
          this.syjid = res.result[0].syjid;
          setTimeout(this.zhanglamessages(), 1000);
        } else {
          this.$message.warning("此任务单下暂无张拉试验监测数据!");
        }
      });
    },
    zhanglamessages: function () {
      //请求张拉每个孔的断面数据
      let param = { syjid: this.syjid };
      getAction(this.url.list, param).then((res) => {
        if (res.result.length > 0) {
          this.data = res.result;
          this.holeid = res.result[0].trZhanglaMList.holeid;
          this.shebeibianhao = res.result[0].trZhanglaMList.shebeibianhao;
          setTimeout(this.zhanglasss(), 1000);
        } else {
          this.$message.warning("此任务单下暂无张拉试验监测数据!");
        }
      });
    },
    zhanglasss: function () {
      //张拉每个孔道的过程数据
      var datadetail = [];
      let param = {
        holeid: this.holeid,
        shebeibianhao: this.shebeibianhao,
        pageSize: this.ipagination1.pageSize,
        pageNo: this.ipagination1.current,
      };
      getAction(this.url.list1, param).then((res) => {
        if (res.result.length > 0) {
          this.data1 = res.result.records;
          this.ipagination1.total = res.result.total;
          let data = res.result.records;
          data.forEach(function (item, index) {
            //,
            datadetail.push({
              type: item.jlsj,
              "张拉力1(KN)": item.zll1,
              "油压1(Mpa)": item.yy1,
              "顶行程1(mm)": item.dxc1,
              "伸长量1(mm)": item.scl1,
              "张拉力2(KN)": item.zll2,
              "油压2(Mpa)": item.yy2,
              "顶行程2(mm)": item.dxc2,
              "伸长量2(mm)": item.scl2,
            });
          });
          this.datadetail = datadetail;
          //console.log(res)
        } else {
          this.$message.warning("暂无此孔号的张拉过程!");
          this.datadetail = [];
        }
      });
    },
    callzhanglarenwudown(e) {
      this.uuid = e;
      this.visible = true;
      this.zhanglarenwudandows = true;
      this.zhanglaxxb();
    },
    handleCancel() {
      this.visible = false;
    },
  },
};
</script>

<style scoped>
.separate div {
  /* flex: 1; */
  height: 25px;
  line-height: 25px;

  /* border-bottom: 1px solid #999999; */
}
</style>
