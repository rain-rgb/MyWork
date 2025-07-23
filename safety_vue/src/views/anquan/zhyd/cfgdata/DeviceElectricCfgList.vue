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
                v-model="queryParam.imei"
                :dictOptions="dictOption"
              >
              </j-search-select-tag>
              {{ selectValue }}
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
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button
        type="primary"
        v-has="'zhydcfg:dc'"
        icon="download"
        @click="handleExportXls('智慧用电配置主表')"
        >导出</a-button
      >
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" v-has="'zhydcfg:dr'" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
      <!--        </a-menu>-->
      <!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
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
        <span slot="tags" slot-scope="tags, record">
          <a-tag color="green" v-if="record.isCall == '0'">报警</a-tag>
          <a-tag color="red" v-if="record.isCall == '1'">不报警</a-tag>
        </span>
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

    <device-electric-cfg-modal
      ref="modalForm"
      @ok="modalFormOk"
    ></device-electric-cfg-modal>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import DeviceElectricCfgModal from "./modules/DeviceElectricCfgModal";
import JSuperQuery from "@/components/jeecg/JSuperQuery.vue";
import { SYS_DEPART_ORGCODE } from "@/store/mutation-types";
import { usershebeiList } from "@api/api";
import Vue from "vue";
import { handertoken } from "@/mixins/getHanderToken";

export default {
  name: "DeviceElectricCfgList",
  mixins: [JeecgListMixin, mixinDevice, handertoken],
  components: {
    DeviceElectricCfgModal,
    JSuperQuery,
  },
  data() {
    return {
      selectValue: "",
      asyncSelectValue: "",
      dictOption: [],
      description: "智慧用电配置主表管理页面",
      // 表头
      columns: [
        {
          title: "序号",
          dataIndex: "",
          key: "rowIndex",
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          },
        },
        {
          title: "所属组织机构",
          align: "center",
          dataIndex: "sysOrgCode_dictText",
        },
        {
          title: "智慧用电超标号码组",
          align: "center",
          dataIndex: "zhydPhones_dictText",
        },
        {
          title: "设备名称",
          align: "center",
          dataIndex: "imei_dictText",
        },
        {
          title: "电压A标准值(V)",
          align: "center",
          dataIndex: "voltagebza",
        },
        {
          title: "电压B标准值(V)",
          align: "center",
          dataIndex: "voltagebzb",
        },
        {
          title: "电压C标准值(V)",
          align: "center",
          dataIndex: "voltagebzc",
        },
        {
          title: "频率A标准值(hz)",
          align: "center",
          dataIndex: "frequencybza",
        },
        {
          title: "频率B标准值(hz)",
          align: "center",
          dataIndex: "frequencybzb",
        },
        {
          title: "频率C标准值(hz)",
          align: "center",
          dataIndex: "frequencybzc",
        },
        {
          title: "电流A标准值(A)",
          align: "center",
          dataIndex: "electricitybza",
        },
        {
          title: "电流B标准值(A)",
          align: "center",
          dataIndex: "electricitybzb",
        },
        {
          title: "电流C标准值(A)",
          align: "center",
          dataIndex: "electricitybzc",
        },
        {
          title: "漏电流标准值(mA)",
          align: "center",
          dataIndex: "seepelectricitybz",
        },
        {
          title: "温度A标准值(℃)",
          align: "center",
          dataIndex: "tempbza",
        },
        {
          title: "温度B标准值(℃)",
          align: "center",
          dataIndex: "tempbzb",
        },
        {
          title: "温度C标准值(℃)",
          align: "center",
          dataIndex: "tempbzc",
        },
        {
          title: "温度N标准值(℃)",
          align: "center",
          dataIndex: "tempbzn",
        },
        {
          title: "电能A标准值(KW.H)",
          align: "center",
          dataIndex: "energybza",
        },
        {
          title: "电能B标准值(KW.H)",
          align: "center",
          dataIndex: "energybzb",
        },
        {
          title: "电能C标准值(KW.H)",
          align: "center",
          dataIndex: "energybzc",
        },
        {
          title: "是否报警",
          align: "center",
          dataIndex: "isCall_dictText",
          key: "isCall_dictText",
          scopedSlots: { customRender: "tags" },
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
        list: "/zhydcfg/deviceElectricCfg/list",
        delete: "/zhydcfg/deviceElectricCfg/delete",
        deleteBatch: "/zhydcfg/deviceElectricCfg/deleteBatch",
        exportXlsUrl: "/zhydcfg/deviceElectricCfg/exportXls",
        importExcelUrl: "zhydcfg/deviceElectricCfg/importExcel",
      },
      dictOptions: {},
      superFieldList: [],
    };
  },
  created() {
    this.getToken();
    this.getSuperFieldList();
    this.shebeiList();
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
        sbtypes: "17",
      }).then((res) => {
        this.dictOption = [];
        let result = res.result;
        result.forEach((re) => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno });
        });
        //console.log(res)
      });
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: "string", value: "imei", text: "设备编号", dictCode: "" });
      fieldList.push({
        type: "double",
        value: "voltagebza",
        text: "电压A标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "voltagebzb",
        text: "电压B标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "voltagebzc",
        text: "电压C标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "frequencybza",
        text: "频率A标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "frequencybzb",
        text: "频率B标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "frequencybzc",
        text: "频率C标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "electricitybza",
        text: "电流A标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "electricitybzb",
        text: "电流B标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "electricitybzc",
        text: "电流C标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "seepelectricitybz",
        text: "漏电流标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "tempbza",
        text: "温度A标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "tempbzb",
        text: "温度B标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "tempbzc",
        text: "温度C标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "tempbzn",
        text: "温度N标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "energybza",
        text: "电能A标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "energybzb",
        text: "电能B标准值",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "energybzc",
        text: "电能C标准值",
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
