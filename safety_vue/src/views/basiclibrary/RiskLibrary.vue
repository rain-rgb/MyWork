<template xmlns:background-color="http://www.w3.org/1999/xhtml">
  <a-row :gutter="10">
    <a-col :md="5" :sm="24">
      <a-card :bordered="false" v-model="currentOrgCode">
        <!-- 按钮操作区域 -->
        <a-row style="margin-left: 14px">
          <a-button
            v-has="'plan:add'"
            @click="handleAdd(1)"
            type="primary"
            icon="plus"
            style="margin-bottom: 10px"
            >新增</a-button
          >
          <span style="position: absolute; right: 0px">
            <a-button type="primary" icon="download" @click="handleExportXls('部门信息')"
              >导出</a-button
            >
            <a-upload
              name="file"
              :showUploadList="false"
              :multiple="false"
              :headers="tokenHeader"
              :action="importExcelUrl"
              @change="handleImportExcel"
            >
              <a-button v-has="'plan:add'" type="primary" icon="import">导入</a-button>
            </a-upload>
          </span>
          <!--<a-button @click="refresh" type="default" icon="reload" :loading="loading">刷新</a-button>-->
        </a-row>
        <div style="background: #fff; padding-left: 16px; height: 100%; margin-top: 5px">
          <a-alert type="info" :showIcon="true">
            <div slot="message">
              当前选择：<span v-if="this.currSelected.title">{{
                getCurrSelectedTitle()
              }}</span>
              <a
                v-if="this.currSelected.title"
                style="margin-left: 10px"
                @click="onClearSelected"
                >取消选择</a
              >
            </div>
          </a-alert>
          <a-input-search
            @search="onSearch"
            style="width: 100%; margin-top: 10px"
            placeholder="请输入部门名称"
          />
          <!-- 树-->
          <a-col :md="10" :sm="24">
            <template>
              <a-dropdown :trigger="[this.dropTrigger]" @visibleChange="dropStatus">
                <span style="user-select: none">
                  <a-tree
                    @select="handleTreeSelect"
                    @rightClick="rightHandle"
                    :selectedKeys="selectedKeys"
                    :checkedKeys="checkedKeys"
                    :treeData="departTree"
                    :checkStrictly="checkStrictly"
                    :expandedKeys="iExpandedKeys"
                    :autoExpandParent="autoExpandParent"
                    @expand="onExpand"
                    showLine
                  />
                </span>
                <!--新增右键点击事件,和增加添加和删除功能-->
                <a-menu slot="overlay">
                  <a-menu-item @click="handleAdd(3)" key="1">添加</a-menu-item>
                  <a-menu-item @click="handleDelete" key="2">删除</a-menu-item>
                  <a-menu-item @click="closeDrop" key="3">取消</a-menu-item>
                </a-menu>
              </a-dropdown>
            </template>
          </a-col>
        </div>
      </a-card>
      <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
      <div class="drawer-bootom-button">
        <a-dropdown :trigger="['click']" placement="topCenter">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="switchCheckStrictly(1)">父子关联</a-menu-item>
            <a-menu-item key="2" @click="switchCheckStrictly(2)">取消关联</a-menu-item>
            <a-menu-item key="3" @click="checkALL">全部勾选</a-menu-item>
            <a-menu-item key="4" @click="cancelCheckALL">取消全选</a-menu-item>
            <a-menu-item key="5" @click="expandAll">展开所有</a-menu-item>
            <a-menu-item key="6" @click="closeAll">合并所有</a-menu-item>
          </a-menu>
          <a-button> 树操作 <a-icon type="up" /> </a-button>
        </a-dropdown>
      </div>
      <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
    </a-col>

    <a-col :md="19" :sm="24">
      <a-card
        class="j-address-list-right-card-box"
        :loading="cardLoading"
        :bordered="false"
        v-model="currentOrgCode"
      >
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="userId"
          :pagination="ipagination"
          :columns="columns"
          :dataSource="dataSource"
          :loading="loading"
          @change="handleTableChange"
          :customRow="handleClick"
          :rowClassName="setRowClassName"
        >
          <span slot="action" slot-scope="text, record">
            <a v-has="'plan:edit'" @click="handleEdit(record)">编辑</a>
            <a-divider type="vertical" />
            <a-dropdown v-has="'plan:del'">
              <a class="ant-dropdown-link"> 更多 <a-icon type="down" /> </a>
              <a-menu slot="overlay">
                <!-- <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item> -->
                <a-menu-item v-has="'plan:del'">
                  <a-popconfirm
                    title="确定删除吗?"
                    @confirm="() => handleDelete(record.id)"
                  >
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </span>
        </a-table>
        <management-modal ref="modalForm" @ok="modalFormOk"></management-modal>
      </a-card>
    </a-col>
    <depart-modal ref="departModal" @ok="loadTree"></depart-modal>
  </a-row>
</template>
<script>
import DepartModal from "./modules/DepartModal";
import pick from "lodash.pick";
import { queryDepartTreeList, searchByKeywords, deleteByDepartId } from "@/api/api";
import { httpAction, deleteAction, getAction } from "@/api/manage";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import DepartAuthModal from "./modules/DepartAuthModal";

import JDate from "@/components/jeecg/JDate";
import ManagementModal from "./modules/ManagementModal";

// 表头
const columns = [
  {
    title: "编号",
    key: "rowIndex",
    dataIndex: "",
    align: "center",
    customRender: (t, r, i) => parseInt(i) + 1,
  },
  {
    title: "风险描述",
    align: "center",
    dataIndex: "realname",
    width: "17%",
  },
  {
    title: "预测事故类型",
    dataIndex: "workNo",
    key: "building",
    width: 70,
    align: "center",
  },
  {
    title: "LEC法风险估测",
    children: [
      {
        title: "L",
        dataIndex: "sgcd",
        key: "building",
        align: "center",
      },
      {
        title: "E",
        dataIndex: "sgcd",
        key: "building",
        align: "center",
      },
      {
        title: "C",
        dataIndex: "sgcd",
        key: "building",
        align: "center",
      },
      {
        title: "D",
        dataIndex: "sgcd",
        key: "building",
        align: "center",
      },
      {
        title: "风险等级",
        dataIndex: "telephone",
        key: "number",
        width: 70,
        align: "center",
      },
    ],
  },
  {
    title: "风险控制措施",
    align: "center",
    dataIndex: "departName",
    width: 120,
  },
  {
    title: "残余风险判断",
    align: "center",
    width: 120,
    dataIndex: "post",
  },
  {
    title: "操作",
    dataIndex: "action",
    scopedSlots: { customRender: "action" },
    align: "center",
    width: 120,
  },
];
export default {
  name: "DepartList",
  mixins: [JeecgListMixin],
  components: {
    DepartAuthModal,
    DepartModal,
  },
  data() {
    return {
      url: {
        list: "/sys/user/queryByOrgCodeForAddressList",
        listByPosition: "/sys/position/list",
      },
      currentOrgCode: "",

      iExpandedKeys: [],
      loading: false,
      autoExpandParent: true,
      currFlowId: "",
      currFlowName: "",
      disable: true,
      treeData: [],
      visible: false,
      departTree: [],
      rightClickSelectedKey: "",
      rightClickSelectedOrgCode: "",
      hiding: true,
      model: {},
      dropTrigger: "",
      depart: {},
      columns: columns,
      disableSubmit: false,
      checkedKeys: [],
      selectedKeys: [],
      autoIncr: 1,
      currSelected: {},

      allTreeKeys: [],
      checkStrictly: true,

      form: this.$form.createForm(this),
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      graphDatasource: {
        nodes: [],
        edges: [],
      },
      validatorRules: {
        departName: { rules: [{ required: true, message: "请输入机构/部门名称!" }] },
        orgCode: { rules: [{ required: true, message: "请输入机构编码!" }] },
        orgCategory: { rules: [{ required: true, message: "请输入机构类型!" }] },
        mobile: { rules: [{ validator: this.validateMobile }] },
      },
      url: {
        delete: "/sys/sysDepart/delete",
        edit: "/sys/sysDepart/edit",
        deleteBatch: "/sys/sysDepart/deleteBatch",
        exportXlsUrl: "sys/sysDepart/exportXls",
        importExcelUrl: "sys/sysDepart/importExcel",

        list: "/sys/user/queryByOrgCodeForAddressList",
        listByPosition: "/sys/position/list",
      },
      orgCategoryDisabled: false,
    };
  },
  watch: {
    value: {
      immediate: true,
      handler(orgCode) {
        this.dataSource = [];
        this.loadData(1, orgCode);
      },
    },
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    emitInput(orgCode) {
      this.$emit("input", orgCode);
    },

    handleTreeSelect(selectedKeys, event) {
      if (selectedKeys.length > 0 && this.selectedKeys[0] !== selectedKeys[0]) {
        this.selectedKeys = [selectedKeys[0]];
        let orgCode = event.node.dataRef.orgCode;
        this.emitInput(orgCode);
        this.onSelect();
      }
    },

    loadData(pageNum, orgCode) {
      this.loading = true;
      if (pageNum === 1) {
        this.ipagination.current = 1;
      }
      // update-begin- --- author:wangshuai ------ date:20200102 ---- for:传过来的部门编码为空全查
      if (!orgCode) {
        getAction(this.url.list, {
          ...this.getQueryParams(),
        })
          .then((res) => {
            if (res.success) {
              this.dataSource = res.result.records;
              this.ipagination.total = res.result.total;
            }
          })
          .finally(() => {
            this.loading = false;
            this.cardLoading = false;
          });
        // update-end- --- author:wangshuai ------ date:20200102 ---- for:传过来的部门编码为空全查
      } else {
        //加载数据 若传入参数1则加载第一页的内容
        getAction(this.url.list, {
          orgCode,
          ...this.getQueryParams(),
        })
          .then((res) => {
            if (res.success) {
              this.dataSource = res.result.records;
              this.ipagination.total = res.result.total;
            }
          })
          .finally(() => {
            this.loading = false;
            this.cardLoading = false;
          });
      }
    },
    searchQuery() {
      this.loadData(1, this.value);
    },
    searchReset() {
      this.queryParam = {};
      this.loadData(1, this.value);
    },

    handleTableChange(pagination, filters, sorter) {
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field;
        this.isorter.order = "ascend" === sorter.order ? "asc" : "desc";
      }
      this.ipagination = pagination;
      this.loadData(null, this.value);
    },

    // 查询职务信息
    queryPositionInfo() {
      getAction(this.url.listByPosition, { pageSize: 99999 }).then((res) => {
        if (res.success) {
          let positionInfo = {};
          res.result.records.forEach((record) => {
            positionInfo[record["code"]] = record["name"];
          });
          this.positionInfo = positionInfo;
        }
      });
    },

    loadTree() {
      var that = this;
      that.treeData = [];
      that.departTree = [];
      queryDepartTreeList().then((res) => {
        if (res.success) {
          //部门全选后，再添加部门，选中数量增多
          this.allTreeKeys = [];
          for (let i = 0; i < res.result.length; i++) {
            let temp = res.result[i];
            that.treeData.push(temp);
            that.departTree.push(temp);
            that.setThisExpandedKeys(temp);
            that.getAllKeys(temp);
            // console.log(temp.id)
          }
          this.loading = false;
        }
      });
    },
    setThisExpandedKeys(node) {
      if (node.children && node.children.length > 0) {
        this.iExpandedKeys.push(node.key);
        for (let a = 0; a < node.children.length; a++) {
          this.setThisExpandedKeys(node.children[a]);
        }
      }
    },
    refresh() {
      this.loading = true;
      this.loadTree();
    },
    // 右键操作方法
    rightHandle(node) {
      this.dropTrigger = "contextmenu";
      console.log(node.node.eventKey);
      this.rightClickSelectedKey = node.node.eventKey;
      this.rightClickSelectedOrgCode = node.node.dataRef.orgCode;
    },
    onExpand(expandedKeys) {
      console.log("onExpand", expandedKeys);
      // if not set autoExpandParent to false, if children expanded, parent can not collapse.
      // or, you can remove all expanded children keys.
      this.iExpandedKeys = expandedKeys;
      this.autoExpandParent = false;
    },
    backFlowList() {
      this.$router.back(-1);
    },
    // 右键点击下拉框改变事件
    dropStatus(visible) {
      if (visible == false) {
        this.dropTrigger = "";
      }
    },
    // 右键店家下拉关闭下拉框
    closeDrop() {
      this.dropTrigger = "";
    },
    addRootNode() {
      this.$refs.nodeModal.add(this.currFlowId, "");
    },
    batchDel: function () {
      console.log(this.checkedKeys);
      if (this.checkedKeys.length <= 0) {
        this.$message.warning("请选择一条记录！");
      } else {
        var ids = "";
        for (var a = 0; a < this.checkedKeys.length; a++) {
          ids += this.checkedKeys[a] + ",";
        }
        var that = this;
        this.$confirm({
          title: "确认删除",
          content:
            "确定要删除所选中的 " +
            this.checkedKeys.length +
            " 条数据，以及子节点数据吗?",
          onOk: function () {
            deleteAction(that.url.deleteBatch, { ids: ids }).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.loadTree();
                that.onClearSelected();
              } else {
                that.$message.warning(res.message);
              }
            });
          },
        });
      }
    },
    onSelect(selectedKeys, e) {
      this.hiding = false;
      let record = e.node.dataRef;
      this.currSelected = Object.assign({}, record);
      this.model = this.currSelected;
      this.selectedKeys = [record.key];
      this.model.parentId = record.parentId;
      this.setValuesToForm(record);
      this.$refs.departAuth.show(record.id);
    },
    onSearch(value) {
      let that = this;
      if (value) {
        searchByKeywords({ keyWord: value }).then((res) => {
          if (res.success) {
            that.departTree = [];
            for (let i = 0; i < res.result.length; i++) {
              let temp = res.result[i];
              that.departTree.push(temp);
            }
          } else {
            that.$message.warning(res.message);
          }
        });
      } else {
        that.loadTree();
      }
    },
    nodeModalOk() {
      this.loadTree();
    },
    nodeModalClose() {},
    hide() {
      console.log(111);
      this.visible = false;
    },

    setValuesToForm(record) {
      if (record.orgCategory == "1") {
        this.orgCategoryDisabled = true;
      } else {
        this.orgCategoryDisabled = false;
      }
      this.$nextTick(() => {
        this.form.getFieldDecorator("fax", { initialValue: "" });
        this.form.setFieldsValue(
          pick(
            record,
            "departName",
            "orgCategory",
            "orgCode",
            "departOrder",
            "mobile",
            "fax",
            "address",
            "memo"
          )
        );
      });
    },
    getCurrSelectedTitle() {
      return !this.currSelected.title ? "" : this.currSelected.title;
    },
    onClearSelected() {
      this.hiding = true;
      this.checkedKeys = [];
      this.currSelected = {};
      this.form.resetFields();
      this.selectedKeys = [];
      this.$refs.departAuth.departId = "";
    },
    handleNodeTypeChange(val) {
      this.currSelected.nodeType = val;
    },
    notifyTriggerTypeChange(value) {
      this.currSelected.notifyTriggerType = value;
    },
    receiptTriggerTypeChange(value) {
      this.currSelected.receiptTriggerType = value;
    },
    submitCurrForm() {
      this.form.validateFields((err, values) => {
        if (!err) {
          if (!this.currSelected.id) {
            this.$message.warning("请点击选择要修改部门!");
            return;
          }

          let formData = Object.assign(this.currSelected, values);
          console.log("Received values of form: ", formData);
          httpAction(this.url.edit, formData, "put").then((res) => {
            if (res.success) {
              this.$message.success("保存成功!");
              this.loadTree();
            } else {
              this.$message.error(res.message);
            }
          });
        }
      });
    },
    emptyCurrForm() {
      this.form.resetFields();
    },
    nodeSettingFormSubmit() {
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log("Received values of form: ", values);
        }
      });
    },
    openSelect() {
      this.$refs.sysDirectiveModal.show();
    },
    handleAdd(num) {
      if (num == 1) {
        this.$refs.departModal.add();
        this.$refs.departModal.title = "新增";
      } else if (num == 2) {
        let key = this.currSelected.key;
        if (!key) {
          this.$message.warning("请先点击选中上级部门！");
          return false;
        }
        this.$refs.departModal.add(this.selectedKeys);
        this.$refs.departModal.title = "新增";
      } else {
        this.$refs.departModal.add(this.rightClickSelectedKey);
        this.$refs.departModal.title = "新增";
      }
    },
    handleDelete() {
      var that = this;
      this.$confirm({
        title: "确认删除",
        content: "确定要删除此部门以及子节点数据吗?",
        onOk: function () {
          deleteByDepartId({ id: that.rightClickSelectedKey }).then((resp) => {
            if (resp.success) {
              //删除成功后，去除已选中中的数据
              that.checkedKeys.splice(
                that.checkedKeys.findIndex((key) => key === that.rightClickSelectedKey),
                1
              );
              that.$message.success("删除成功!");
              that.loadTree();
              //删除后同步清空右侧基本信息内容
              let orgCode = that.form.getFieldValue("orgCode");
              if (orgCode && orgCode === that.rightClickSelectedOrgCode) {
                that.onClearSelected();
              }
            } else {
              that.$message.warning("删除失败!");
            }
          });
        },
      });
    },
    selectDirectiveOk(record) {
      console.log("选中指令数据", record);
      this.nodeSettingForm.setFieldsValue({ directiveCode: record.directiveCode });
      this.currSelected.sysCode = record.sysCode;
    },
    getFlowGraphData(node) {
      this.graphDatasource.nodes.push({
        id: node.id,
        text: node.flowNodeName,
      });
      if (node.children.length > 0) {
        for (let a = 0; a < node.children.length; a++) {
          let temp = node.children[a];
          this.graphDatasource.edges.push({
            source: node.id,
            target: temp.id,
          });
          this.getFlowGraphData(temp);
        }
      }
    },
    // <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
    expandAll() {
      this.iExpandedKeys = this.allTreeKeys;
    },
    closeAll() {
      this.iExpandedKeys = [];
    },
    checkALL() {
      this.checkStriccheckStrictlytly = false;
      this.checkedKeys = this.allTreeKeys;
    },
    cancelCheckALL() {
      //this.checkedKeys = this.defaultCheckedKeys
      this.checkedKeys = [];
    },
    switchCheckStrictly(v) {
      if (v == 1) {
        this.checkStrictly = false;
      } else if (v == 2) {
        this.checkStrictly = true;
      }
    },
    getAllKeys(node) {
      // console.log('node',node);
      this.allTreeKeys.push(node.key);
      if (node.children && node.children.length > 0) {
        for (let a = 0; a < node.children.length; a++) {
          this.getAllKeys(node.children[a]);
        }
      }
    },
    // <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
  },

  created() {
    this.currFlowId = this.$route.params.id;
    this.currFlowName = this.$route.params.name;

    this.refresh();
    this.queryPositionInfo();
    // this.loadTree()
  },
};
</script>
<style scoped>
.ant-card-body .table-operator {
  margin: 15px;
}

.anty-form-btn {
  width: 100%;
  text-align: center;
}

.anty-form-btn button {
  margin: 0 5px;
}

.anty-node-layout .ant-layout-header {
  padding-right: 0;
}

.header {
  padding: 0 8px;
}

.header button {
  margin: 0 3px;
}

.ant-modal-cust-warp {
  height: 100%;
}

.ant-modal-cust-warp .ant-modal-body {
  height: calc(100% - 110px) !important;
  overflow-y: auto;
}

.ant-modal-cust-warp .ant-modal-content {
  height: 90% !important;
  overflow-y: hidden;
}

#app .desktop {
  height: auto !important;
}

/** Button按钮间距 */
.ant-btn {
  margin-left: 3px;
}

.drawer-bootom-button {
  /*position: absolute;*/
  bottom: 0;
  width: 100%;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: left;
  left: 0;
  background: #fff;
  border-radius: 0 0 2px 2px;
}

.j-address-list-right-card-box .ant-table-placeholder {
  min-height: 46px;
}
.j-address-list-right-card-box {
  height: 100%;
  min-height: 300px;
}
</style>
