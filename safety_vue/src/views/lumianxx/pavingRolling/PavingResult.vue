<template>
  <!-- 施工成果-->
  <a-card :bordered="false">
    <div class="table-page-search-wrapper" style="margin-bottom: 30px">
      <a-form layout="inline">
        <a-row :gutter="24">
          <!-- <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="组织机构">
              <j-search-select-tag placeholder="请选择组织机构" v-model="queryParam.projectid"> </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col> -->
          <!-- <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="工程名称">
              <j-dict-select-tag style="width: 200px" type="list" placeholder="" />
            </a-form-item>
          </a-col> -->
          <!-- <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="选择工程">
              <j-dict-select-tag style="width: 200px" type="list" placeholder="" />
            </a-form-item>
          </a-col> -->
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
            <a-form-item label="开始桩号">
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
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
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
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间段">
              <j-date
                placeholder="开始"
                dateFormat="YYYY-MM-DD"
                v-model="queryParam.date_begin"
                :showTime="true"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束"
                dateFormat="YYYY-MM-DD"
                v-model="queryParam.date_end"
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

    <!-- 操作按钮区域 -->
    <!-- <div class="table-operator"> -->
    <!-- <a-button @click="handleAdd" v-has="'hntbhz:add'" type="primary" icon="plus"
        >新增</a-button
      >
      <a-button
        type="primary"
        v-has="'hntbhz:dc'"
        icon="download"
        @click="handleExportXls('施工成果')"
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
        <a-button type="primary" v-has="'hntbhz:dr'" icon="import">导入</a-button>
      </a-upload> -->
    <!-- 禁用高级查询区域 -->
    <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
    <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
    <!--        <a-menu slot="overlay">-->
    <!--          <a-menu-item key="1" v-has="'hntbhz:del'" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
    <!--        </a-menu>-->
    <!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
    <!--      </a-dropdown>-->
    <!-- </div> -->
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
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{ x: true }"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="tableChange"
      >
        <!-- :loading="loading" -->
        <span slot="tags" slot-scope="tags, record">
          <span v-if="record.offset == '0'">左幅</span>
          <span v-if="record.offset == '1'">右幅</span>
        </span>
        <span slot="yajiangji" slot-scope="yajiangji, record">

          <a class="ant-dropdown-link" @click="showResult(record.id, record)"
            >施工成果图</a
          >
        </span>
        <span slot="formulaNo" slot-scope="formulaNo, record">
          <!-- <a-tag color="green">{{ record.formulaNo }}</a-tag> -->
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
          <a v-has="'hntbhz:edit'" @click="handleEdit(record)">编辑</a>

          <!-- <a-divider type="vertical" /> -->
          <a-dropdown>
            <!-- <a class="ant-dropdown-link" @click="showmadel1(record.id)">摊铺详情</a>
            <a class="ant-dropdown-link" @click="showmadel2(record.id)">碾压详情</a> -->
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="showmadel1(record.id, record)">摊铺详情</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="showmadel2(record.id, record)">碾压详情</a>
              </a-menu-item>
              <!-- <a-menu-item v-has="'hntbhz:del'">
                <a-popconfirm
                  title="确定删除吗?"
                  @confirm="() => handleDelete(record.id)"
                >
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item> -->
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <PavingResultDetail ref="PavingResultDetail"></PavingResultDetail>
    <rollingResultDetail ref="rollingResultDetail"></rollingResultDetail>
    <PavingResultResult ref="PavingResultResult"></PavingResultResult>
  </a-card>
</template>
<script>
import { getAction } from "@api/manage";
import { usershebeiList } from "@api/api";
import * as echarts from "echarts";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
// import BhzCementBaseModal from './modules/BhzCementBaseModal'
import { filterMultiDictText } from "@/components/dict/JDictSelectUtil";
import "@/assets/less/TableExpand.less";
import JSuperQuery from "@/components/jeecg/JSuperQuery.vue";
import { handertoken } from "@/mixins/getHanderToken";
import { SYS_DEPART_ORGCODE } from "@/store/mutation-types";
import PavingResultDetail from "@views/lumianxx/modules/PavingResultDetail.vue";
import rollingResultDetail from "@views/lumianxx/modules/rollingResultDetail.vue";
import PavingResultResult from "@views/lumianxx/modules/PavingResultResult.vue";
import Vue from "vue";
export default {
  name: "PavingResult",
  //mixins: [JeecgListMixin],
  mixins: [JeecgListMixin, handertoken],
  components: {
    // Pie,
    // JSuperQuery
    // BhzCementBaseModal,
    JSuperQuery,
    PavingResultDetail,
    PavingResultResult,
    rollingResultDetail,
  },
  data() {
    return {
      approval_remarks: null,
      selectValue: "",
      selectValueBD: "",
      description: "拌合站主表管理页面",
      asyncSelectValue: "",
      dictOption: [],
      dictOptionPro: [],
      dictOptionBD: [],
      datalist: [],
      dataSource: [],
      fields: [],
      aliases: [],
      url: {
        list: "/hc_constructionresults/hcConstructionresults/list",
        hcProject: "/hc_project/hcProject/list",
        hcSection: "/hc_section/hcSection/list",
        hcProject1: "/hc_section/hcSection/listls",
        // list: '/hntbhz/bhzCementBase/listrc',
        delete: "/hntbhz/bhzCementBase/delete",
        deleteBatch: "/hntbhz/bhzCementBase/deleteBatch",
        exportXlsUrl: "/hntbhz/bhzCementBase/exportXls",
        importExcelUrl: "hntbhz/bhzCementBase/importExcel",
      },
      dictOptions: {},
      columns: [
        {
          title: "日期",
          align: "center",
          dataIndex: "date",
        },
        {
          title: "工程",
          align: "center",
          dataIndex: "pjname",
        },
        {
          title: "标段",
          align: "center",
          dataIndex: "sectionname",
        },
        {
          title: "结构层",
          align: "center",
          dataIndex: "layername",
        },
        {
          title: "开始桩号",
          align: "center",
          dataIndex: "startstation",
        },
        {
          title: "结束桩号",
          align: "center",
          dataIndex: "endstation",
        },
        {
          title: "左右幅",
          align: "center",
          dataIndex: "offset",
          scopedSlots: { customRender: "tags" },
        },
        {
          title: "铺摊里程(m)",
          align: "center",
          dataIndex: "workmileage",
        },
        {
          title: "成果图",
          align: "center",
          dataIndex: "yajiangji",
          key: "yajiangji",
          scopedSlots: { customRender: "yajiangji" },
        },
        // {
        //   title: '完成状态',
        //   align: 'center',
        //   dataIndex: 'status',
        //   scopedSlots: { customRender: 'status' },
        // }
        {
          title: "操作",
          dataIndex: "action",
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: { customRender: "action" },
        },
      ],
      testData: [
        {
          gcmc: "2023-04-22",
          lblx: "宁波急山湾疏港高速坤亭至,",
          yjsj: "宁波急山湾疏港高速坤亭至",
          yjsbbh_dictText: "下面层",
          lianghao: "K2+550",
          sgbw: "K2+560",
          cjsjl: "左福",
          zlsj: 172,
          yajiangji: "施工成果图",
          kongdaoshu: "100%",
          yajiangfang: "0%",
        },
      ],
      superFieldList: [],
      charts: null,
      charts1: null,
      dailyCharts: null,
      queryParam: {
        projectid: "",
        pileNumStart1: "",
        pileNumStart2: "",
        pileNumEnd1: "",
        pileNumEnd2: "",
        date_begin: "",
        date_end: "",
      },
      loading: false,
    };
  },
  created() {
    // this.bhzsta();
    // this.fanglaing();
    // this.hegelv();
    this.getToken();
    // this.getSuperFieldList();
    this.shebeiList();
    // this.getData();
    this.getProject();
  },
  mounted() {},
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
        date_begin: "",
        date_end: "",
        startstation: "",
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
      // let username = this.$route.query.username;
      let param = {};
      getAction(this.url.hcProject, param).then((res) => {
        console.log(res, "getProject-------------------------");
        this.dictOptionPro = [];
        let result = res.result.records;
        if (result == null){
          this.getProject1()
        } else {
          this.projectList = res.result.records;
          this.orgcode = result[0].orgcode;
          this.projectId = result[0].pjid;
          // this.queryParam.pjname = result[0].pjname;
          result.forEach((res) => {
            this.dictOptionPro.push({ text: res.pjname, value: res.pjid });
          });
          // this.getDataBD(this.orgcode);
        }
      });
    },
    getProject1() {
      // let username = this.$route.query.username;
      let param = {};
      getAction(this.url.hcProject1, param).then((res) => {
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
      this.queryParam.startstation = "";
      if (this.queryParam.pileNumStart1 || this.queryParam.pileNumStart2) {
        let numStart =
          Number(this.queryParam.pileNumStart1) || this.queryParam.pileNumStart1;
        let numEnd =
          Number(this.queryParam.pileNumStart2) || this.queryParam.pileNumStart2;
        let pileNumStart = Number(numStart + "" + numEnd);
        this.queryParam.startstation = pileNumStart;
        console.log(
          this.queryParam.startstation,
          numStart,
          numEnd,
          "startstation-------------111"
        );
      } else {
        this.queryParam.endstation = "";
        console.log(this.queryParam.startstation, "startstation-------------222");
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
    showmadel1(syjid, array) {
      console.log(array, "array-----------");
      // console.log(array, this.dataSource, "array-----------");
      this.$refs.PavingResultDetail.title = "详情";
      this.$refs.PavingResultDetail.projectid = array.projectid;
      this.$refs.PavingResultDetail.sectionid = array.sectionid;
      this.$refs.PavingResultDetail.date_begin = array.date_begin;
      this.$refs.PavingResultDetail.date_end = array.date_end;
      this.$refs.PavingResultDetail.call(syjid, array);
    },
    showmadel2(syjid, array) {
      this.$refs.rollingResultDetail.title = "详情";
      this.$refs.rollingResultDetail.projectid = array.projectid;
      this.$refs.rollingResultDetail.sectionid = array.sectionid;
      this.$refs.rollingResultDetail.date_begin = array.date_begin;
      this.$refs.rollingResultDetail.date_end = array.date_end;
      this.$refs.rollingResultDetail.call(syjid, array);
    },
    showResult(syjid, array) {
      this.$refs.PavingResultResult.title = "成果图";
      this.$refs.PavingResultResult.projectid = array.projectid;
      this.$refs.PavingResultResult.sectionid = array.sectionid;
      this.$refs.PavingResultResult.startTime = array.date + " 00:00:00";
      this.$refs.PavingResultResult.endTime = array.date + " 23:00:00";
      // this.$refs.PavingResultResult.startTime = "2022-10-30 20:00:00";
      // this.$refs.PavingResultResult.endTime = "2022-10-30 23:00:00";
      console.log(array,'array.begintime-----------------------');
      // console.log(this.$refs.PavingResultResult.projectid,'array.begintime-----------------------');
      this.$refs.PavingResultResult.call(syjid);
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: "0",
      }).then((res) => {
        this.dictOption = [];
        let result = res.result;
        result.forEach((re) => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno });
          this.datalist.push({ text: re.sbname, value: re.sbjno });
        });
        // this.datalist = JSON.parse(JSON.stringify(this.dictOption))
        //console.log(res)
      });
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
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: "string", value: "batchNo", text: "唯一ID", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "date",
        text: "日期",
        dictCode: "",
      });
      fieldList.push({ type: "string", value: "workNo", text: "工单号", dictCode: "" });
      fieldList.push({ type: "string", value: "handlers", text: "操作者", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "projectName",
        text: "工程名称",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "jobLocation",
        text: "施工地点",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "poureLocation",
        text: "浇筑部位",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "cementVariety",
        text: "水泥品种",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "additiveVariety",
        text: "外加剂品种",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "formulaNo",
        text: "配方号",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "strengthRank",
        text: "强度等级",
        dictCode: "",
      });
      fieldList.push({
        type: "int",
        value: "stirDatetime",
        text: "搅拌时间",
        dictCode: "",
      });
      fieldList.push({ type: "date", value: "saveDatetime", text: "保存时间" });
      fieldList.push({
        type: "string",
        value: "clientNo",
        text: "客户端编号",
        dictCode: "",
      });
      fieldList.push({ type: "int", value: "status", text: "状态", dictCode: "" });
      fieldList.push({ type: "date", value: "collectDatetime", text: "采集时间" });
      fieldList.push({
        type: "double",
        value: "estimateNumber",
        text: "方量",
        dictCode: "",
      });
      fieldList.push({ type: "date", value: "productDatetime", text: "出料时间" });
      fieldList.push({ type: "string", value: "slump", text: "坍落度", dictCode: "" });
      fieldList.push({
        type: "int",
        value: "overLevel",
        text: "超标等级",
        dictCode: "over_level",
      });
      fieldList.push({
        type: "int",
        value: "alertstate",
        text: "是否超标",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "formulaId",
        text: "配方uuid(车结束符)",
        dictCode: "",
      });
      fieldList.push({
        type: "int",
        value: "timeOverLevel",
        text: "搅拌时间超标等级",
        dictCode: "",
      });
      this.superFieldList = fieldList;
    },
  },
};
</script>
<style scope>
@import "~@assets/less/common.less";
.charts-box {
  display: flex;
}
#PavingTcharts {
  width: 100%;
  height: 200px;
}
#PavingTcharts1 {
  width: 100%;
  height: 200px;
}
#dailyCount {
  width: 100%;
  height: 400px;
}
#Tempcharts {
  width: 100%;
  height: 200px;
}
#Tempcharts1 {
  width: 100%;
  height: 200px;
}
</style>
