<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24"> </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('张拉信息主表')"
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
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query
        :fieldList="superFieldList"
        ref="superQueryModal"
        @handleSuperQuery="handleSuperQuery"
      ></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"
            ><a-icon type="delete" />删除</a-menu-item
          >
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

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
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
          <img
            v-else
            :src="getImgView(text)"
            height="25px"
            alt=""
            style="max-width: 80px; font-size: 12px; font-style: italic"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)"
          >
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
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
    </div>

    <tr-zhangla-m-modal ref="modalForm" @ok="modalFormOk"></tr-zhangla-m-modal>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import TrZhanglaMModal from "./moduls/TrZhanglaMModal";
import JSuperQuery from "@/components/jeecg/JSuperQuery.vue";

export default {
  name: "TrZhanglaMList",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    TrZhanglaMModal,
    JSuperQuery,
  },
  data() {
    return {
      description: "张拉信息主表管理页面",
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
        //   title:'唯一码',
        //   align:"center",
        //   dataIndex: 'fguid'
        // },
        // {
        //   title:'主键',
        //   align:"center",
        //   dataIndex: 'syjid'
        // },
        {
          title: "设备编号",
          align: "center",
          dataIndex: "shebeibianhao",
        },
        {
          title: "张拉时间",
          align: "center",
          dataIndex: "zlsj",
        },
        {
          title: "钢束编号",
          align: "center",
          dataIndex: "gsbh",
        },
        // {
        //   title:'holeid',
        //   align:"center",
        //   dataIndex: 'holeid'
        // },
        {
          title: "钢束股数",
          align: "center",
          dataIndex: "gsgs",
        },
        {
          title: "弹性模量",
          align: "center",
          dataIndex: "txml",
        },
        {
          title: "设计张拉力 KN",
          align: "center",
          dataIndex: "sjzll",
        },
        {
          title: "回弹量mm",
          align: "center",
          dataIndex: "htl",
        },
        {
          title: "总伸长量mm",
          align: "center",
          dataIndex: "zscl",
        },
        {
          title: "理论伸长量mm",
          align: "center",
          dataIndex: "llscl",
        },
        {
          title: "允许偏差值",
          align: "center",
          dataIndex: "yxpc",
        },
        {
          title: "延伸量误差%",
          align: "center",
          dataIndex: "sclper",
        },
        {
          title: "断丝及处理情况",
          align: "center",
          dataIndex: "wt",
        },
        {
          title: "处理情况",
          align: "center",
          dataIndex: "clqk",
        },
        {
          title: "备注",
          align: "center",
          dataIndex: "memo",
        },
        {
          title: "完成状态(0为未完成,状态为1代表",
          align: "center",
          dataIndex: "status",
        },
        {
          title: "是否合格",
          align: "center",
          dataIndex: "hege",
        },
        {
          title: "预张拉百分比",
          align: "center",
          dataIndex: "yzlb",
        },
        {
          title: "初张拉百分比",
          align: "center",
          dataIndex: "czlb",
        },
        {
          title: "终张拉百分比",
          align: "center",
          dataIndex: "zzlb",
        },
        {
          title: "记录时间",
          align: "center",
          dataIndex: "jlsj",
        },
        {
          title: "操作",
          dataIndex: "action",
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: { customRender: "action" },
        },
      ],
      url: {
        list: "/zhanglam/trZhanglaM/list",
        delete: "/zhanglam/trZhanglaM/delete",
        deleteBatch: "/zhanglam/trZhanglaM/deleteBatch",
        exportXlsUrl: "/zhanglam/trZhanglaM/exportXls",
        importExcelUrl: "zhanglam/trZhanglaM/importExcel",
      },
      dictOptions: {},
      superFieldList: [],
    };
  },
  created() {
    this.getSuperFieldList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: "string", value: "fguid", text: "唯一码", dictCode: "" });
      fieldList.push({ type: "string", value: "syjid", text: "主键", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "shebeibianhao",
        text: "设备编号",
        dictCode: "",
      });
      fieldList.push({ type: "string", value: "zlsj", text: "张拉时间", dictCode: "" });
      fieldList.push({ type: "string", value: "gsbh", text: "钢束编号", dictCode: "" });
      fieldList.push({ type: "string", value: "holeid", text: "holeid", dictCode: "" });
      fieldList.push({ type: "string", value: "gsgs", text: "钢束股数", dictCode: "" });
      fieldList.push({ type: "string", value: "txml", text: "弹性模量", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "sjzll",
        text: "设计张拉力 KN",
        dictCode: "",
      });
      fieldList.push({ type: "string", value: "htl", text: "回弹量mm", dictCode: "" });
      fieldList.push({ type: "string", value: "zscl", text: "总伸长量mm", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "llscl",
        text: "理论伸长量mm",
        dictCode: "",
      });
      fieldList.push({ type: "string", value: "yxpc", text: "允许偏差值", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "sclper",
        text: "延伸量误差%",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "wt",
        text: "断丝及处理情况",
        dictCode: "",
      });
      fieldList.push({ type: "string", value: "clqk", text: "处理情况", dictCode: "" });
      fieldList.push({ type: "string", value: "memo", text: "备注", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "status",
        text: "完成状态(0为未完成,状态为1代表",
        dictCode: "",
      });
      fieldList.push({ type: "string", value: "hege", text: "是否合格", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "yzlb",
        text: "预张拉百分比",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "czlb",
        text: "初张拉百分比",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "zzlb",
        text: "终张拉百分比",
        dictCode: "",
      });
      fieldList.push({ type: "string", value: "jlsj", text: "记录时间", dictCode: "" });
      this.superFieldList = fieldList;
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
