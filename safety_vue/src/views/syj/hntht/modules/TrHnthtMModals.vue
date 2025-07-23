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
      <a-tab-pane key="1" tab="混凝土回弹检测数据分析">
        <a-table
          rowKey="id"
          :pagination="ipagination1"
          :columns="columns1"
          :data-source="data1"
          bordered
          :customRow="handleClick"
          :rowClassName="setRowClassName"
        >
          <span slot="thickness" slot-scope="thickness, record">
            <a-tag color="red">{{ record.thickness - designthickness }}</a-tag>
          </span>
          <span slot="pouringsurface" slot-scope="pouringsurface, record">
            <a-tag color="green" v-if="record.pouringsurface == 0">浇筑侧面</a-tag>
            <a-tag color="green" v-if="record.pouringsurface == 1">浇筑底面</a-tag>
          </span>
          <span slot="surface" slot-scope="surface, record">
            <a-tag color="green" v-if="record.surface == 1">表面</a-tag>
            <a-tag color="green" v-if="record.surface == 2">底面</a-tag>
            <a-tag color="green" v-if="record.surface == 3">侧面</a-tag>
          </span>
          <span slot="number" slot-scope="number, record">
            <div v-for="(item, index) in record.cequlist">
              <a-tag color="green">{{ item }}</a-tag>
            </div>
          </span>
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </j-modal>
</template>

<script>
import { getAction } from "@api/manage";

export default {
  name: "TrHnthtMModals",
  components: {},
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
          title: "回弹平均值",
          align: "center",
          dataIndex: "average",
        },
        {
          title: "测区",
          align: "center",
          dataIndex: "number",
          scopedSlots: { customRender: "number" },
        },
        {
          title: "强度值",
          align: "center",
          dataIndex: "carbonize",
        },
        {
          title: "强度值",
          align: "center",
          dataIndex: "strcar",
        },
        {
          title: "面修正值",
          align: "center",
          dataIndex: "calsurface",
        },
        {
          title: "角度修正值",
          align: "center",
          dataIndex: "calangle",
        },
        {
          title: "浇筑面",
          align: "center",
          dataIndex: "pouringsurface",
          scopedSlots: { customRender: "pouringsurface" },
        },
        {
          title: "浇筑面",
          align: "center",
          dataIndex: "surface",
          scopedSlots: { customRender: "surface" },
        },
        {
          title: "碳化值1",
          align: "center",
          dataIndex: "carbonization",
        },
        {
          title: "碳化值2",
          align: "center",
          dataIndex: "carbonizetwo",
        },
        {
          title: "碳化值3",
          align: "center",
          dataIndex: "carbonizethree",
        },
        {
          title: "平均碳化值",
          align: "center",
          dataIndex: "flagcarbonization",
        },
      ],
      testid: "",
      designthickness: 0,
      url: {
        list: "/trhnthts/trHnthtS/list1",
      },
    };
  },
  methods: {
    callback1(e) {},
    tableChange(pagination) {
      this.ipagination1.current = pagination.current;
      this.ipagination1.pageSize = pagination.pageSize;
      this.hnthtdetail(this.testid);
    },
    hnthtdetail(testid) {
      var datadetail = [];
      let param = { testid: testid };
      getAction(this.url.list, param).then((res) => {
        if (res.success) {
          if (res.result.records.length > 0) {
            let data = res.result.records;
            this.ipagination1.total = res.result.total;
            this.data1 = res.result.records;
            this.datadetail = datadetail;
            console.log(this.datadetail, "1");
          } else {
            this.$message.warning("暂无此试验的数据!");
          }
        }
      });
    },
    call(e) {
      console.log(e);
      this.testid = e.testid;
      //this.designthickness=e.designthickness;
      this.hnthtdetail(this.testid);
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
