<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper" style="margin-bottom: 30px">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="选择工程">
              <j-search-select-tag
                style="width: 200px"
                placeholder="请选择项目名称"
                v-model="queryParam.projectid"
                :dictOptions="dictOptionPro"
                @change="handleChange"
              >
              </j-search-select-tag>
              <!-- {{ selectValue }} -->
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="选择标段">
              <j-search-select-tag
                style="width: 200px"
                placeholder="请选择标段名称"
                v-model="queryParam.sectionid"
                :dictOptions="dictOptionBD"
                @change="handleChangeBD"
              >
              </j-search-select-tag>
              <!-- {{ selectValueBD }} -->
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="选择结构层">
              <j-dict-select-tag
                style="width: 200px"
                type="list"
                placeholder="请选择设备"
                v-model="queryParam.layerindex"
                dictCode="layerindex"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="桩号">
              K
              <a-input
                style="width: 90px"
                placeholder=""
                v-model="queryParam.pileNumStart1"
                @keyup.native="startStation()"
              ></a-input
              >+<a-input
                style="width: 90px"
                placeholder=""
                v-model="queryParam.pileNumStart2"
                :maxLength="3"
                @keyup.native="startStation()"
              ></a-input>
            </a-form-item>
          </a-col>
          <!-- <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="结束桩号">
              K
              <a-input
                style="width: 90px"
                placeholder=""
                v-model="queryParam.pileNumEnd1"
                @keyup.native="endStation()"
              ></a-input
              >+<a-input
                style="width: 90px"
                placeholder=""
                v-model="queryParam.pileNumEnd2"
                :maxLength="3"
                @keyup.native="endStation()"
              ></a-input>
            </a-form-item>
          </a-col> -->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间段">
              <j-date
                placeholder="开始"
                dateFormat="YYYY-MM-DD"
                v-model="queryParam.dataTime_begin"
                :showTime="true"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束"
                dateFormat="YYYY-MM-DD"
                v-model="queryParam.dataTime_end"
                :showTime="true"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <!-- <a-button type="primary" @click="searchQueryHandle" icon="search"
                >查询</a-button
              > -->
              <a-button
                type="primary"
                @click="searchReset"
                icon="reload"
                style="margin-left: 8px"
                >重置</a-button
              >
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
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
      <a-button
        type="primary"
        icon="download"
        @click="handleExportXls('hc_pavement_alarm')"
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
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <span slot="tags" slot-scope="tags, record">
          <a-tag color="green" v-if="record.alarmlevel == 0">无级别</a-tag>
          <a-tag color="orange" v-if="record.alarmlevel == 1">轻微</a-tag>
          <a-tag color="yellow" v-if="record.alarmlevel == 2">一般</a-tag>
          <a-tag color="red" v-if="record.alarmlevel == 3">严重</a-tag>
        </span>
        <span slot="tags1" slot-scope="tags1, record">
          <a-tag color="green" v-if="record.roadoffset >= 0">右幅</a-tag>
          <a-tag color="green" v-if="record.roadoffset < 0">左幅</a-tag>
        </span>
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

<!--        <span slot="action" slot-scope="text, record">-->
<!--          <a @click="handleDetail(record)">详情</a>-->
<!--           <a-divider type="vertical"/>-->
<!--          <a-dropdown>-->
<!--            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>-->
<!--            <a-menu slot="overlay">-->
<!--            </a-menu>-->
<!--          </a-dropdown>-->
<!--        </span>-->
      </a-table>
    </div>

    <hc-pavement-alarm-modal ref="modalForm" @ok="modalFormOk"></hc-pavement-alarm-modal>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import HcPavementAlarmModal from "./modules/HcPavementAlarmModal";
import { getAction } from "@api/manage";
import Vue from "vue";
import { SYS_DEPART_ORGCODE } from "@/store/mutation-types";

export default {
  name: "HcPavementAlarmList",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    HcPavementAlarmModal,
  },
  data() {
    return {
      description: "hc_pavement_alarm管理页面",
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
          title: "工程名",
          align: "center",
          dataIndex: "pjname",
        },
        {
          title: "标段名",
          align: "center",
          dataIndex: "sectionname",
        },

        {
          title: "机械类型id",
          align: "center",
          dataIndex: "machinetypeid",
        },
        {
          title: "设备sn号",
          align: "center",
          dataIndex: "sncode",
        },
        {
          title: "机械id",
          align: "center",
          dataIndex: "machineid",
        },
        {
          title: "机械名称",
          align: "center",
          dataIndex: "machinename",
        },
        {
          title: "告警时间",
          align: "center",
          dataIndex: "datatime"
          // customRender: function (text) {
          //   return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
          // },
        },
        {
          title: "速度标准值",
          align: "center",
          dataIndex: "standard",
        },
        {
          title: "速度真实值",
          align: "center",
          dataIndex: "actual",
        },
        {
          title: "经度",
          align: "center",
          dataIndex: "lon",
        },
        {
          title: "纬度",
          align: "center",
          dataIndex: "lat",
        },
        {
          title: "桩号",
          align: "center",
          dataIndex: "roadstation",
        },
        {
          title: "左右幅",
          align: "center",
          dataIndex: "roadoffset",
          scopedSlots: { customRender: 'tags1' },
        },
        {
          title: "告警类型",
          align: "center",
          dataIndex: "alarmtypeid_dictText",
        },
        {
          title: "告警内容",
          align: "center",
          dataIndex: "alarminfo",
        },
        {
          title: "告警级别",
          align: "center",
          dataIndex: "alarmlevel",
          scopedSlots: { customRender: 'tags' },
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
        list: "/hc_pavement_alarm/hcPavementAlarm/list",
        delete: "/hc_pavement_alarm/hcPavementAlarm/delete",
        deleteBatch: "/hc_pavement_alarm/hcPavementAlarm/deleteBatch",
        exportXlsUrl: "/hc_pavement_alarm/hcPavementAlarm/exportXls",
        importExcelUrl: "hc_pavement_alarm/hcPavementAlarm/importExcel",
        hcProject: "/hc_project/hcProject/list",
        hcSection: "/hc_section/hcSection/list",
      },
      dictOptions: {},
      superFieldList: [],
      queryParam: {
        projectid: "",
        pileNumStart1: "",
        pileNumStart2: "",
        pileNumEnd1: "",
        pileNumEnd2: "",
        dataTime_begin: "",
        dataTime_end: "",
      },
      dictOption: [],
      dictOptionPro: [],
      dictOptionBD: [],
      // sys_depart_orgcode: "",
    };
  },
  created() {
    // this.sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
    // this.sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
    // console.log("sys_depart_orgcode------------------------sys_depart_orgcode---------------------");
    this.getSuperFieldList();
    this.getProject();
  },
  // watch: {
  //   sys_depart_orgcode: {
  //     // immediate: true,
  //     deep:true,
  //     handler() {
  //       // this.sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
  //       console.log(
  //         this.sys_depart_orgcode,
  //         "getProject------------------------getProject---------------------"
  //       );
  //       this.getProject();
  //     },
  //   },
  // },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    searchQueryHandle() {
      console.log(111);
      this.getData();
    },
    tableChange(pagination) {
      this.ipagination.current = pagination.current;
      this.ipagination.pageSize = pagination.pageSize;
      this.loadData();
      // this.getData();
    },
    getData() {
      // this.loading = true;
      // let username = this.$route.query.username;
      let param = {
        projectid: this.queryParam.projectid,
        sectionid: "",
        dataTime_begin: "",
        dataTime_end: "",
        roadStation: "",
        endstation: "",
        pageSize: this.ipagination.pageSize,
        pageNo: this.ipagination.current,
      };
      getAction(this.url.list, param).then((res) => {
        // console.log(res);
        // this.dataSource = res.result.records;
        if (res.success) {
          //update-begin---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
          this.dataSource = res.result.records || res.result;
          if (res.result.total) {
            this.ipagination.total = res.result.total;
          } else {
            this.ipagination.total = 0;
          }
        } else {
          this.dataSource = null;
          this.$message.warning(res.message);
        }
        // this.loading = false;
      });
    },
    getProject() {
      // console.log(
      //   this.sys_depart_orgcode,
      //   "sys_depart_orgcode-----------------------------"
      // );
      // let username = this.$route.query.username;
      let param = {
        orgCode: this.sys_depart_orgcode,
      };
      getAction(this.url.hcProject, param).then((res) => {
        console.log(res, "getProject-------------------------");
        this.dictOptionPro = [];
        let result = res.result.records;
        this.projectList = res.result.records;
        this.orgcode = result[0].orgcode;
        this.projectId = result[0].pjid;
        // this.queryParam.pjname = result[0].pjname;
        result.forEach((res) => {
          this.dictOptionPro.push({ text: res.pjname, value: res.pjid });
        });
        // this.getDataBD(this.orgcode);
      });
    },
    getDataBD(orgcode) {
      let param = {
        orgcode,
      };
      getAction(this.url.hcSection, param).then((res) => {
        console.log(res, "hcSection------------");
        this.dictOptionBD = [];
        let result = res.result.records;
        this.sectionList = res.result.records;
        // this.sectionId = result[0].sectionid;
        // this.queryParam.biaoduan = result[0].sectionname;
        result.forEach((res) => {
          this.dictOptionBD.push({ text: res.sectionname, value: res.sectionid });
        });
        console.log(
          this.projectId,
          this.sectionId,
          result[0],
          "this.projectId------------getDataBD"
        );
      });
    },
    handleChange(selectedValue) {
      let arr = this.projectList.filter((e) => {
        return e.pjid == selectedValue;
      });
      this.projectId = arr[0].pjid;
      let orgcode = arr[0].orgcode;
      // console.log("selectedValue", arr, arr.pjid);
      this.getDataBD(orgcode);
      // console.log(this.projectId, this.sectionId, "this.projectId------------sectionId");
    },
    handleChangeBD(selectedValue) {
      let arr = this.sectionList.filter((e) => {
        return e.sectionid == selectedValue;
      });
      this.sectionId = arr[0].sectionid;
      // console.log("selectedValue", selectedValue);
      // this.sectionId = selectedValue;
      console.log(this.projectId, this.sectionId, "this.projectId------------sectionId");
    },
    //开始桩号
    startStation() {
      this.queryParam.roadStation = "";
      if (this.queryParam.pileNumStart1 || this.queryParam.pileNumStart2) {
        let numStart =
          Number(this.queryParam.pileNumStart1) || this.queryParam.pileNumStart1;
        let numEnd =
          Number(this.queryParam.pileNumStart2) || this.queryParam.pileNumStart2;
        let pileNumStart = Number(numStart + "" + numEnd);
        this.queryParam.roadStation = pileNumStart;
        console.log(
          this.queryParam.roadStation,
          numStart,
          numEnd,
          "roadStation-------------111"
        );
      } else {
        this.queryParam.endstation = "";
        console.log(this.queryParam.roadStation, "roadStation-------------222");
      }
    },
    //结束桩号
    endStation() {
      this.queryParam.endstation = "";
      if (this.queryParam.pileNumEnd1 || this.queryParam.pileNumEnd2) {
        let numStart = Number(this.queryParam.pileNumEnd1) || this.queryParam.pileNumEnd1;
        let numEnd = Number(this.queryParam.pileNumEnd2) || this.queryParam.pileNumEnd2;
        let pileNumStart = Number(numStart + "" + numEnd);
        this.queryParam.endstation = pileNumStart;
        console.log(this.queryParam.endstation, "endstation-------------111");
      } else {
        this.queryParam.endstation = "";
        console.log(this.queryParam.endstation, "endstation-------------222");
      }
      if (false || true) {
        console.log(true);
      } else {
        console.log(false);
      }
    },
    chaxun: function () {
      // this.cx = this.selectedValue;
      // this.dates = this.approval_remarks;
      // this.bhzsta();
      // this.fanglaing();
      // this.hegelv();
      let numStart = this.prefixZero(this.queryParam.pileNumStart2, 3);
      let numEnd = this.prefixZero(this.queryParam.pileNumEnd2, 3);
      console.log(numStart, "pileNumStart2.length-------------");
      let pileNumStart = this.queryParam.pileNumStart1 + numStart;
      let pileNumEnd = this.queryParam.pileNumEnd1 + numEnd;
      console.log(pileNumStart, pileNumEnd, "chaxun---------------------pileNumStart");
    },
    prefixZero(n, m) {
      var _a = (Array(m).join(0) + n).slice(-m);
      return _a;
    },
    chongzhi: function () {
      this.dictOption = [];
      this.selectedValue = null;
      this.approval_remarks = null;
      this.chaxun();
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({
        type: "int",
        value: "alarmtypeid",
        text:
          "告警类型，1碾压超速，2碾压温度过低，3摊铺超速，4摊铺温度过低，5碾压距摊铺机过远",
      });
      fieldList.push({ type: "string", value: "machinetypeid", text: "机械类型id" });
      fieldList.push({
        type: "int",
        value: "alarmlevel",
        text: "告警级别，0为无级别，1为轻微，2为一般，3为严重",
      });
      fieldList.push({ type: "string", value: "sncode", text: "设备sn号" });
      fieldList.push({ type: "string", value: "machineid", text: "机械id" });
      fieldList.push({ type: "string", value: "machinename", text: "机械名称" });
      fieldList.push({ type: "date", value: "datatime", text: "告警时间" });
      fieldList.push({ type: "string", value: "standard", text: "标准值" });
      fieldList.push({ type: "string", value: "actual", text: "真实值" });
      fieldList.push({ type: "string", value: "lon", text: "经度" });
      fieldList.push({ type: "string", value: "lat", text: "纬度" });
      fieldList.push({ type: "string", value: "roadstation", text: "桩号" });
      fieldList.push({ type: "string", value: "roadoffset", text: "左右幅，左负右正" });
      fieldList.push({ type: "string", value: "alarminfo", text: "告警内容" });
      fieldList.push({ type: "string", value: "projectid", text: "projectid" });
      fieldList.push({ type: "string", value: "sectionid", text: "sectionid" });
      this.superFieldList = fieldList;
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
