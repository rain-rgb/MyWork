<template>
  <a-modal
    centered
    :title="title"
    :width="1600"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <a-form-item label="施工部位">
              <a-input
                placeholder="请输入施工部位"
                v-model="queryParam.sgbwname"
              ></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <!-- <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button> -->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div>
      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{
          selectedRowKeys: selectedRowKeys1,
          onChange: onSelectChange1,
          type: 'radio',
        }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <span slot="uuid" slot-scope="uuid, record">
          <a-tag color="blue" @click="zhanglamessageList(uuid)">{{ record.uuid }}</a-tag>
        </span>
      </a-table>
    </div>
  </a-modal>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import moment from "moment";

export default {
  name: "RwdTable",
  mixins: [JeecgListMixin],
  components: {},
  data() {
    return {
      selectedRowKeys1: [],
      model1: {},
      visible: false,
      title: "任务单浇筑令",
      conspos: "",
      queryParam: {
        sgbwname: "",
        // dattim_begin: moment().subtract(7, 'days').format('YYYY-MM-DD'),
        // dattim_end: moment().format('YYYY-MM-DD'),
      },
      // 表头
      columns: [
        {
          title: "#",
          dataIndex: "",
          key: "rowIndex",
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          },
        },
        // {
        //   title: '状态',
        //   align: 'center',
        //   dataIndex: 'status',
        //   key: 'status',
        //   scopedSlots: { customRender: 'status' },
        //   filters: [
        //     {
        //       text: '已通过',
        //       value: '1',
        //     },
        //   ],
        //   onFilter: (value, record) => record.status == value,
        // },
        {
          title: "任务单号",
          align: "center",
          dataIndex: "uuid",
          scopedSlots: { customRender: "uuid" },
        },
        {
          title: "施工部位名称",
          align: "center",
          width: 200,
          dataIndex: "sgbwname",
        },
      ],
      url: {
        list: "/trzhanglarenwudan/trZhanglaRenwudan/list",
        // list: '/system/bhzrenwudan/list',
      },
    };
  },
  methods: {
    onSelectChange1(selectedRowKeys, selectionRows) {
      // console.log(selectedRowKeys, selectionRows)
      this.selectedRowKeys1 = selectedRowKeys;
      this.model1 = Object.assign({}, selectionRows[0]);
      console.log(this.model1.uuid,'uuid');
      console.log(this.model1.sgbwname,'sgbwname');
      // this.loadData()
    },
    handleCancel() {
      this.visible = false;
    },
    handleOk() {
      this.$emit("change", this.model1);
      this.visible = false;
    },
  },
  watch: {
    conspos(val) {
      if (val) {
        this.queryParam.conspos = `*${val}*`;
      } else {
        this.queryParam.conspos = undefined;
      }
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
