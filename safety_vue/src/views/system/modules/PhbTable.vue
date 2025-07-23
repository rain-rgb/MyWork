<template>
  <a-modal
    centered
    :title="title"
    :width="1000"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{ x: true }"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{
          selectedRowKeys: selectedRowKeys1,
          onChange: onSelectChange1,
          type: 'radio',
        }"
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >
        <span slot="code" slot-scope="code, record">
          <a-tag color="green">{{ record.code }}</a-tag>
        </span>
      </a-table>
    </div>
  </a-modal>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import "@/assets/less/TableExpand.less";

export default {
  name: "PhbTable",
  mixins: [JeecgListMixin],
  components: {},
  data() {
    return {
      selectedRowKeys1: [],
      model1: {},
      visible: false,
      title: "砼拌合站理论配合比",
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
        //   title:'唯一ID',
        //   align:"center",
        //   dataIndex: 'uuid'
        // },
        {
          title: "配合比编号",
          align: "center",
          dataIndex: "code",
          key: "code",
          scopedSlots: { customRender: "code" },
        },
        // {
        //   title:'砼标记',
        //   align:"center",
        //   dataIndex: 'tag'
        // },
        // {
        //   title:'混凝土类别',
        //   align:"center",
        //   dataIndex: 'variety'
        // },
        {
          title: "强度等级",
          align: "center",
          dataIndex: "betlevel",
        },
        {
          title: "抗渗等级",
          align: "center",
          dataIndex: "filters",
        },
        {
          title: "抗冻等级",
          align: "center",
          dataIndex: "freeze",
        },
        {
          title: "抗折等级",
          align: "center",
          dataIndex: "bend",
        },
        // {
        //   title:'施工季节',
        //   align:"center",
        //   dataIndex: 'season'
        // },
        {
          title: "坍落度",
          align: "center",
          dataIndex: "lands",
        },
        {
          title: "搅拌时长",
          align: "center",
          dataIndex: "mixlast",
        },
        // {
        //   title:'设计比例',
        //   align:"center",
        //   dataIndex: 'scale'
        // },
        // {
        //   title:'骨料最大粒径',
        //   align:"center",
        //   dataIndex: 'bonesz'
        // },
        // {
        //   title:'水泥品种',
        //   align:"center",
        //   dataIndex: 'cementtype'
        // },
        // {
        //   title:'status',
        //   align:"center",
        //   dataIndex: 'status'
        // },
        {
          title: "建立时间",
          align: "center",
          dataIndex: "createdate",
        },
        {
          title: "拌合机名称",
          align: "center",
          dataIndex: "bhjno_dictText",
        },
        // {
        //   title:'砂率',
        //   align:"center",
        //   dataIndex: 'sandratio'
        // },
        {
          title: "水胶比",
          align: "center",
          dataIndex: "waterbindratio",
        },
        // {
        //   title:'每方重量kg',
        //   align:"center",
        //   dataIndex: 'onevolume'
        // },
      ],
      url: {
        list: "/bhzrecipe/bhzRecipe/list",
      },
      queryParam: {
        bhjno: ''
      },
    };
  },
  created() {},
  methods: {
    onSelectChange1(selectedRowKeys, selectionRows) {
      // console.log(selectedRowKeys, selectionRows)
      this.selectedRowKeys1 = selectedRowKeys;
      this.model1 = Object.assign({}, selectionRows[0]);
      // console.log(this.model1)
      // this.loadData()
    },
    handleCancel() {
      this.visible = false;
    },
    handleOk() {
      this.$emit("change", this.model1.code);
      this.visible = false;
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
