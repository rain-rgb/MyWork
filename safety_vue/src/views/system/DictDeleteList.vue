<template>
  <a-modal
    :width="modalWidth"
    :style="modalStyle"
    :visible="visible"
    :maskClosable="false"
    @cancel="handleCancel"
  >
    <template slot="footer">
      <a-button @click="handleCancel">关闭</a-button>
    </template>
    <a-table
      ref="table"
      rowKey="id"
      size="middle"
      :columns="columns"
      :loading="loading"
      :dataSource="dataSource"
      :pagination="false"
      :customRow="handleClick"
      :rowClassName="setRowClassName"
    >
      <span slot="action" slot-scope="text, record">
        <a @click="handleBack(record.id)"><a-icon type="redo" />字典取回</a>
        <a-divider type="vertical" />
        <a @click="handleDelete(record.id)"><a-icon type="scissor" />彻底删除</a>
      </span>
    </a-table>
  </a-modal>
</template>

<script>
import { getAction, deleteAction, putAction } from "@/api/manage";
export default {
  name: "DictDeleteList",
  data() {
    return {
      modalWidth: "90%",
      modalStyle: { top: "20px" },
      title: "操作",
      visible: false,
      loading: false,
      dataSource: [],
      columns: [
        {
          title: "#",
          dataIndex: "",
          key: "rowIndex",
          width: 120,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          },
        },
        {
          title: "字典名称",
          align: "left",
          dataIndex: "dictName",
        },
        {
          title: "字典编号",
          align: "left",
          dataIndex: "dictCode",
        },
        {
          title: "描述",
          align: "left",
          dataIndex: "description",
        },
        {
          title: "操作",
          dataIndex: "action",
          align: "center",
          scopedSlots: { customRender: "action" },
        },
      ],
    };
  },

  methods: {
    handleCancel() {
      this.visible = false;
      //回收站字典列表刷新
      this.$emit("refresh");
    },
    show() {
      this.visible = true;
      this.loadData();
    },
    loadData() {
      this.loading = true;
      getAction("/sys/dict/deleteList").then((res) => {
        this.loading = false;
        if (res.success) {
          this.dataSource = res.result;
        } else {
          this.$message.warning(res.message);
        }
      });
    },
    handleBack(id) {
      putAction("/sys/dict/back/" + id).then((res) => {
        if (res.success) {
          this.$message.success(res.message);
          this.loadData();
        } else {
          this.$message.warning(res.message);
        }
      });
    },
    handleDelete(id) {
      this.$confirm({
        title: "彻底删除字典",
        content: (
          <div>
            <p>您确定要彻底删除这个字典项吗？</p>
            <p style="color:red;">注意：彻底删除后将无法恢复，请谨慎操作！</p>
          </div>
        ),
        centered: false,
        onOk: () => {
          var that = this;
          deleteAction("/sys/dict/deletePhysic/" + id).then((res) => {
            if (res.success) {
              this.$message.success(res.message);
              this.loadData();
            } else {
              that.$message.warning(res.message);
            }
          });
        },
      });
    },
  },
};
</script>

<style scoped></style>
