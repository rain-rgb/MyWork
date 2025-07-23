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
    <a-tabs default-active-key="1" @change="callback1">
      <a-tab-pane key="1" tab="智能压浆处置审核信息">
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
      <a-tab-pane key="2" tab="智能压浆详情数据">
        <a-table
          rowKey="id"
          :pagination="ipagination"
          :columns="columns"
          :data-source="data"
          bordered
          :customRow="handleClick"
          :rowClassName="setRowClassName"
        >
          <span slot="hege" slot-scope="status, record">
            <a-tag color="green" v-if="record.hege == '1'">合格</a-tag>
            <a-tag color="red" v-if="record.hege == '0'">不合格</a-tag>
          </span>
        </a-table>
      </a-tab-pane>
    </a-tabs>
    <a-tabs default-active-key="1" @change="callback2">
      <a-tab-pane key="1" tab="智能压浆过程数据">
        <a-table
          :pagination="ipagination1"
          rowKey="sid"
          :columns="columns1"
          :data-source="data1"
          @change="tableChange"
          :customRow="handleClick"
          :rowClassName="setRowClassName"
        >
        </a-table>
      </a-tab-pane>
    </a-tabs>
    <a-tabs default-active-key="1" @change="callback">
      <a-tab-pane v-for="arry in data" :key="arry.holeid" :tab="arry.kongdao">
        <LineChartsYajiang
          title="压浆过程图"
          :data-source="datadetail"
        ></LineChartsYajiang>
      </a-tab-pane>
    </a-tabs>
  </j-modal>
</template>

<script>
import { getAction } from "@api/manage";
import LineChartsYajiang from "@views/zlyj/modules/LineChartsYajiang";

export default {
  name: "TrYajiangMSModalChuZhiShenHe",
  components: {
    LineChartsYajiang,
  },
  data() {
    return {
      height: 420,
      data: [],
      ipagination: false,
      yajiangrenwudanms: false,
      columns: [
        {
          title: "压浆时间",
          align: "center",
          dataIndex: "yajiangsj",
        },
        {
          title: "孔道",
          align: "center",
          dataIndex: "kongdao",
        },
        {
          title: "压浆模式",
          align: "center",
          dataIndex: "yajiangmosh",
        },
        {
          title: "配合比",
          align: "center",
          dataIndex: "peihebi",
        },
        {
          title: "水胶比",
          align: "center",
          dataIndex: "shuijiaobi",
        },
        {
          title: "搅拌时间",
          align: "center",
          dataIndex: "jiaobansj",
        },
        {
          title: "开始时间",
          align: "center",
          dataIndex: "starttime",
        },
        {
          title: "结束时间",
          align: "center",
          dataIndex: "endtime",
        },
        {
          title: "进浆压力MPa",
          align: "center",
          dataIndex: "jinjiangyal",
        },
        {
          title: "返浆压力MPa",
          align: "center",
          dataIndex: "fanjiangyal",
        },
        {
          title: "持续时间",
          align: "center",
          dataIndex: "chixushijia",
        },
        {
          title: "进浆量L",
          align: "center",
          dataIndex: "jinjiangshu",
        },
        {
          title: "返浆量L",
          align: "center",
          dataIndex: "fanjianglia",
        },
        {
          title: "真空泵压力MPa",
          align: "center",
          dataIndex: "zkyl",
        },
        {
          title: "压浆次数",
          align: "center",
          dataIndex: "yjcs",
        },
        {
          title: "是否合格",
          align: "center",
          dataIndex: "hege",
          scopedSlots: { customRender: "hege" },
        },
      ],
      columns1: [
        {
          title: "记录时间",
          align: "center",
          dataIndex: "jlsj",
        },
        {
          title: "状态",
          align: "center",
          dataIndex: "zt",
        },
        {
          title: "进浆压力(MPa)",
          align: "center",
          dataIndex: "jjyl",
        },
        {
          title: "返浆压力(MPa)",
          align: "center",
          dataIndex: "fjyl",
        },
        {
          title: "进浆量(L)",
          align: "center",
          dataIndex: "jjl",
        },
        {
          title: "返浆量(L)",
          align: "center",
          dataIndex: "fjl",
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
      data2: [],
      data1: [],
      title: "",
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
      width: 800,
      visible: false,
      disableSubmit: false,
      syjid: "",
      uuid: "",
      datadetail: [],
      url: {
        list: "/yajiangs/trYajiangS/list1",
        listdetail: "/yajiangs/trYajiangSS/list",
        listbutton: "/yajiangm/trYajiangM/list2", //压浆任务单下压浆主表信息查询
        chuzhishenhe: "/zhanglam/zhanglaYajiangOverHandler/list", //处置审核信息
      },
      holeid: "",
    };
  },
  methods: {
    tableChange(pagination) {
      this.ipagination1.current = pagination.current;
      this.ipagination1.pageSize = pagination.pageSize;
      this.yajiangmessagedetail(this.holeid);
    },
    callback(key) {
      this.ipagination1.current = 1;
      this.ipagination1.pageSize = 10;
      this.yajiangmessagedetail(key);
      console.log(key);
    },
    callback1(key) {
      console.log(key);
    },
    callback2(key) {
      console.log(key);
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
    yajiangmessagedetail: function (holeid) {
      var datadetail = [];
      let param = {
        holeid: holeid,
        pageSize: this.ipagination1.pageSize,
        pageNo: this.ipagination1.current,
      };
      getAction(this.url.listdetail, param).then((res) => {
        console.log(res);
        if (res.result.records.length > 0) {
          let data = res.result.records;
          this.ipagination1.total = res.result.total;
          this.data1 = res.result.records;
          data.forEach(function (item, index) {
            //,
            datadetail.push({
              type: item.jlsj,
              "进浆压力(MPa)": item.jjyl,
              "返浆压力(MPa)": item.fjyl,
              "进浆量(L)": item.jjl,
              "返浆量(L)": item.fjl,
            });
          });
          this.datadetail = datadetail;
          console.log(this.datadetail, "1");
        } else {
          this.$message.warning("暂无此孔号的压浆过程!");
          this.datadetail = [];
        }
      });
    },
    yajiangmessage: function () {
      //请求压浆的每个孔道的数据
      let param = { syjid: this.syjid };
      getAction(this.url.list, param).then((res) => {
        // console.log(res)
        if (res.result.length > 0) {
          this.data = res.result;
          this.holeid = res.result[0].holeid;
          setTimeout(this.yajiangmessagedetail(this.holeid), 1000);
          //console.log(this.data, '1')
        } else {
          this.$message.warning("此任务单下暂无压浆试验监测数据!");
        }
      });
    },
    yajiangMList: function () {
      let param = { uuid: this.uuid };
      getAction(this.url.listbutton, param).then((res) => {
        if (res.result.length > 0) {
          this.data2 = res.result;
          this.syjid = res.result[0].syjid;
          setTimeout(this.yajiangmessage(), 1000);
        } else {
          this.$message.warning("此任务单下暂无压浆试验监测数据!");
        }
      });
    },
    call(e) {
      this.syjid = e;
      this.visible = true;
      this.yajiangmessage();
      this.chuzhishenhemessage();
    },
    callmeaages(e) {
      this.uuid = e;
      this.visible = true;
      this.yajiangrenwudanms = true;
      this.yajiangMList();
    },
    close() {
      this.visible = false;
    },
    handleOk() {
      this.visible = false;
    },
    handleCancel() {
      this.visible = false;
    },
  },
};
</script>
