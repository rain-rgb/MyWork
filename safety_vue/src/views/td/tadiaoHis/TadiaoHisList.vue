<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag
                placeholder="请选择设备名称"
                v-model="queryParam.devicesn"
                :dictOptions="dictOption"
              >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date
                placeholder="开始时间"
                v-model="queryParam.devicetime_begin"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束时间"
                v-model="queryParam.devicetime_end"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button
                type="primary"
                @click="searchReset"
                icon="reload"
                style="margin-left: 8px"
                >重置</a-button
              >
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? "收起" : "展开" }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
      <!-- 查询区域-END -->
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus" v-has="'td:add'"
        >新增</a-button
      >
      <a-button
        type="primary"
        icon="download"
        @click="handleExportXls('塔吊')"
        v-has="'td:dc'"
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
        <a-button type="primary" icon="import" v-has="'td:dr'">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal"-->
      <!--                     @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" @click="batchDel">-->
      <!--            <a-icon type="delete"/>-->
      <!--            删除-->
      <!--          </a-menu-item>-->
      <!--        </a-menu>-->
      <!--        <a-button style="margin-left: 8px"> 批量操作-->
      <!--          <a-icon type="down"/>-->
      <!--        </a-button>-->
      <!--      </a-dropdown>-->
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

    <tadiao-his-modal ref="modalForm" @ok="modalFormOk"></tadiao-his-modal>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import TadiaoHisModal from "./modules/TadiaoHisModal";
import { usershebeiList } from "@api/api";
import { handertoken } from "@/mixins/getHanderToken";
import Vue from "vue";

export default {
  name: "TadiaoHisList",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    TadiaoHisModal,
  },
  data() {
    return {
      description: "塔吊历史数据管理页面",
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
        {
          title: "设备名称",
          align: "center",
          dataIndex: "devicesn_dictText",
        },
        {
          title: "回转",
          align: "center",
          dataIndex: "angle",
        },
        {
          title: "吊绳倍率",
          align: "center",
          dataIndex: "fall",
        },
        {
          title: "吊钩高度",
          align: "center",
          dataIndex: "height",
        },
        {
          title: "吊重(T)",
          align: "center",
          dataIndex: "heavy",
        },
        {
          title: "力矩百分比",
          align: "center",
          dataIndex: "percent",
        },
        {
          title: "倾角角度",
          align: "center",
          dataIndex: "obliqueangle",
        },
        {
          title: "倾角方向",
          align: "center",
          dataIndex: "obliquedirection",
        },
        {
          title: "风速",
          align: "center",
          dataIndex: "windspeed",
        },
        {
          title: "幅度",
          align: "center",
          dataIndex: "radius",
        },
        {
          title: "设备数据时间",
          align: "center",
          dataIndex: "devicetime",
        },
        {
          title: "司机身份证编号",
          align: "center",
          dataIndex: "driverid",
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
        list: "/tadiao/tadiaoHis/list",
        delete: "/tadiao/tadiaoHis/delete",
        deleteBatch: "/tadiao/tadiaoHis/deleteBatch",
        exportXlsUrl: "/tadiao/tadiaoHis/exportXls",
        importExcelUrl: "tadiao/tadiaoHis/importExcel",
      },
      selectValue: "",
      dictOption: [],
      dictOptions: {},
      superFieldList: [],
    };
  },
  created() {
    this.getSuperFieldList();
    this.shebeiList();
    this.getToken();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: "51",
      }).then((res) => {
        this.dictOption = [];
        let result = res.result;
        result.forEach((re) => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno });
        });
      });
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({
        type: "string",
        value: "devicesn",
        text: "设备编码",
        dictCode: "",
      });
      fieldList.push({ type: "string", value: "angle", text: "回转", dictCode: "" });
      fieldList.push({ type: "string", value: "fall", text: "吊绳倍率", dictCode: "" });
      fieldList.push({ type: "string", value: "height", text: "吊钩高度", dictCode: "" });
      fieldList.push({ type: "string", value: "heavy", text: "吊重", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "percent",
        text: "力矩百分比",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "obliqueangle",
        text: "倾角角度",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "obliquedirection",
        text: "倾角方向",
        dictCode: "",
      });
      fieldList.push({ type: "string", value: "windspeed", text: "风速", dictCode: "" });
      fieldList.push({ type: "string", value: "radius", text: "幅度", dictCode: "" });
      fieldList.push({ type: "date", value: "devicetime", text: "设备数据时间" });
      fieldList.push({
        type: "string",
        value: "driverid",
        text: "司机身份证编号",
        dictCode: "",
      });
      this.superFieldList = fieldList;
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
