<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <a-tabs default-active-key="1" @change="callback1">
      <a-tab-pane key="1" tab="钢保检测数据分析">
        <a-table
          rowKey="id"
          :pagination="ipagination1"
          :columns="columns1"
          :data-source="data1"
          @change="tableChange"
          bordered
          :customRow="handleClick"
          :rowClassName="setRowClassName"
        >
          <span slot="thickness1" slot-scope="thickness1, record">
            <a-tag color="red">{{ record.thickness - designthickness }}</a-tag>
          </span>
          <span slot="flagpassrate" slot-scope="flagpassrate, record">
            <a-tag color="red" v-if="record.flagpassrate == 0">不合格</a-tag>
            <a-tag color="green" v-if="record.flagpassrate == 1">合格</a-tag>
          </span>
        </a-table>
      </a-tab-pane>
    </a-tabs>
    <a-tabs default-active-key="1" @change="callback">
      <a-tab-pane key="2" tab="钢保检测数据分析厚度">
        <Trgangjindetail
          title="数据分析厚度过程图"
          :data-source="datadetail"
        ></Trgangjindetail>
      </a-tab-pane>
    </a-tabs>
  </j-modal>
</template>

<script>
import { getAction } from "@api/manage";
import Trgangjindetail from "@views/syj/gangbao/modules/Trgangjindetail";
export default {
  name: "TrGangjinbhcMModaldetail",
  components: {
    Trgangjindetail,
  },
  data() {
    return {
      title: "",
      width: 1200,
      visible: false,
      disableSubmit: false,
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
      data1: [],
      datadetail: [],
      columns1: [
        {
          title: "序号",
          dataIndex: "",
          key: "rowIndex",
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          },
        },
        {
          title: "厚度",
          align: "center",
          dataIndex: "thickness",
        },
        {
          title: "偏差",
          align: "center",
          dataIndex: "thickness1",
          scopedSlots: { customRender: "thickness1" },
        },
        {
          title: "复测点数值",
          align: "center",
          dataIndex: "strthickness",
        },
        {
          title: "距离",
          align: "center",
          dataIndex: "distance",
        },
        {
          title: "是否合格",
          align: "center",
          dataIndex: "flagpassrate",
          scopedSlots: { customRender: "flagpassrate" },
        },
      ],
      testid: "",
      designthickness: 0,
      url: {
        list: "/trgangjinbhcs/trGangjinbhcS/list",
      },
    };
  },
  methods: {
    callback1(e) {},
    callback(e) {},
    tableChange(pagination) {
      this.ipagination1.current = pagination.current;
      this.ipagination1.pageSize = pagination.pageSize;
      //console.log(this.ipagination1.current);
      //console.log(this.ipagination1.pageSize);
      this.gangjinbaohucengdetail(this.testid);
    },
    gangjinbaohucengdetail(testid) {
      var datadetail = [];
      let param = {
        testid: testid,
        pageNo: this.ipagination1.current,
        pageSize: this.ipagination1.pageSize,
      };
      getAction(this.url.list, param).then((res) => {
        if (res.success) {
          if (res.result.records.length > 0) {
            let data = res.result.records;
            this.ipagination1.total = res.result.total;
            this.data1 = res.result.records;
            data.forEach(function (item, index) {
              //,
              datadetail.push({ type: index, "厚度(mm)": item.thickness });
            });
            this.datadetail = datadetail;
            //console.log( this.datadetail, '1')
          } else {
            this.$message.warning("暂无此试验的数据!");
          }
        }
      });
    },
    call(e) {
      //console.log(e)
      this.testid = e.testid;
      // console.log(this.testid,"1111111111")
      this.designthickness = e.designthickness;
      this.gangjinbaohucengdetail(this.testid);
      this.visible = true;
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

<style scoped></style>
