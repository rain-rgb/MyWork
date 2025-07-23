<template>
  <a-card :bordered="false">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }"
        ><span style="font-size: 22px; font-weight: bold"
          >安全帽预警：{{ aqmsata }}</span
        ></a-col
      >
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }"
        ><span style="font-size: 22px; font-weight: bold"
          >本月预警：{{ aqmysata }}</span
        ></a-col
      >
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }"
        ><span style="font-size: 22px; font-weight: bold"
          >反光衣预警：{{ fgydata }}</span
        ></a-col
      >
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }"
        ><span style="font-size: 22px; font-weight: bold"
          >本月预警：{{ fgyydata }}</span
        ></a-col
      >
    </a-row>
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24"> </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag
                placeholder="请选择设备名称"
                v-model="queryParam.sbbh"
                :dictOptions="dictOption"
              >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="报警时间">
              <j-date
                placeholder="开始时间"
                v-model="queryParam.warntime_begin"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束时间"
                v-model="queryParam.warntime_end"
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
    </div>
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
        <span slot="algtype" slot-scope="algtype, record">
          <a-tag v-if="record.algtype == '1'">安全帽识别</a-tag>
          <a-tag v-if="record.algtype == '2'">反光衣识别</a-tag>
        </span>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
          <img
            v-else
            :src="getImgView(text)"
            height="25px"
            alt=""
            style="max-width: 80px; font-size: 12px; font-style: italic"
          />
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handlePreview(record)">预览</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <ai-warn-msg-modal ref="modalForm" @ok="modalFormOk"></ai-warn-msg-modal>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import AiWarnMsgModal from "./modules/AiWarnMsgModal";
import { base64Encode } from "@api/kkfileView";
import { handertoken } from "@/mixins/getHanderToken";
import { usershebeiList } from "@api/api";
import Vue from "vue";
import { getAction } from "@api/manage";

export default {
  name: "AiWarnMsgList",
  mixins: [JeecgListMixin, mixinDevice, handertoken],
  components: {
    AiWarnMsgModal,
  },
  data() {
    return {
      aqmsata: "0",
      fgydata: "0",
      aqmysata: "0",
      fgyydata: "0",
      tjlist: [],
      selectValue: "",
      dictOption: [],
      description: "AI识别预警管理页面",
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
          dataIndex: "shebeiid_dictText",
        },
        // {
        //   title:'任务id',
        //   align:"center",
        //   dataIndex: 'cid'
        // },
        {
          title: "摄像头名称（报警工点）",
          align: "center",
          dataIndex: "cname",
        },
        {
          title: "报警时间",
          align: "center",
          dataIndex: "warntime",
        },
        {
          title: "报警内容",
          align: "center",
          dataIndex: "warnmsg",
        },
        {
          title: "报警类别",
          align: "center",
          dataIndex: "algtype",
          scopedSlots: { customRender: "algtype" },
        },
        // {
        //   title:'图片',
        //   align:"center",
        //   dataIndex: 'picurls',
        //   scopedSlots: { customRender: 'fileSlot' },
        // },
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
        list: "/aiwarnmsgs/aiWarnMsgs/list",
        listtj: "/aiwarnmsgs/aiWarnMsgs/listtj",
        listytj: "/aiwarnmsgs/aiWarnMsgs/listytj",
        delete: "/aiwarnmsg/aiWarnMsg/delete",
        deleteBatch: "/aiwarnmsg/aiWarnMsg/deleteBatch",
        exportXlsUrl: "/aiwarnmsg/aiWarnMsg/exportXls",
        importExcelUrl: "aiwarnmsg/aiWarnMsg/importExcel",
      },
      dictOptions: {},
      superFieldList: [],
    };
  },
  created() {
    this.getToken();
    this.getSuperFieldList();
    this.shebeiList();
    this.tongji();
    this.ytongji();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    tongji: function () {
      let params = {};
      this.tjlist = [];
      getAction(this.url.listtj, params).then((res) => {
        var data = res.result;
        data.forEach((item) => {
          if (item.algtype == 1) {
            this.aqmsata = item.enttype;
          } else if (item.algtype == 2) {
            this.fgydata = item.enttype;
          }
        });
      });
    },
    ytongji: function () {
      let params = {};
      getAction(this.url.listytj, params).then((res) => {
        var data = res.result;
        data.forEach((item) => {
          debugger;
          if (item.algtype == 1) {
            this.aqmysata = item.enttype;
          } else if (item.algtype == 2) {
            this.fgyydata = item.enttype;
          }
        });
      });
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: "57",
      }).then((res) => {
        this.dictOption = [];
        let result = res.result;
        result.forEach((re) => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno });
        });
        //console.log(res)
      });
    },
    handlePreview(record) {
      if (record && record.picurls) {
        let url =
          window._CONFIG["onlinePreviewDomainURL"] +
          "?url=" +
          encodeURIComponent(base64Encode(record.picurls));
        window.open(url, "_blank");
      }
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: "int", value: "cid", text: "设备编号", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "cname",
        text: "摄像头名称（报警工点）",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "warntime",
        text: "报警时间",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "warnmsg",
        text: "报警内容",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "algtype",
        text: "报警类别",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "picurls",
        text: "图片，多张用，分隔",
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
