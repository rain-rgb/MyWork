<template>
  <!-- 碾压遍数 -->
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="组织机构">
              <j-search-select-tag style="width: 200px" placeholder="请选择组织机构">
              </j-search-select-tag>
              <!-- {{ selectValue }} -->
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="工程名称">
              <j-dict-select-tag style="width: 200px" type="list" placeholder="" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="选择工程">
              <j-dict-select-tag style="width: 200px" type="list" placeholder="" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="选择标段">
              <j-dict-select-tag style="width: 200px" type="list" placeholder="" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="选择结构层">
              <j-dict-select-tag
                style="width: 200px"
                type="list"
                placeholder="请选择设备"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="开始桩号">
              <j-dict-select-tag
                style="width: 200px"
                type="list"
                placeholder="请选择设备"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="结束桩号">
              <j-dict-select-tag
                style="width: 200px"
                type="list"
                placeholder="请选择设备"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button type="primary" @click="chaxun" icon="search">查询</a-button>
              <a-button
                type="primary"
                @click="chongzhi"
                icon="reload"
                style="margin-left: 8px"
                >重置</a-button
              >
            </span>
          </a-col>
        </a-row>
      </a-form>
      <a-row>
        <a-card
          title="碾压遍数统计"
          :bordered="false"
          :headStyle="{ color: '#0785fd' }"
          :bodyStyle="{ padding: '10' }"
        >
          <div>
            <!-- <LineChartMultid :data-source="dataSource2" /> -->
          </div>
          <div class="charts-box">
            <div id="PavingTcharts"></div>
            <div id="PavingTcharts1"></div>
            <div id="PavingTchartsR"></div>
          </div>
        </a-card>
      </a-row>
      <a-row>
        <a-card
          title="每日欠压率"
          :bordered="false"
          :headStyle="{ color: '#0785fd' }"
          :bodyStyle="{ padding: '10' }"
        >
          <div>
            <!-- <LineChartMultid :data-source="dataSource2" /> -->
          </div>
          <div>
            <div id="dailyCount"></div>
            <div id="dailyCount1"></div>
          </div>
        </a-card>
      </a-row>

      <!-- three -->
      <a-row>
        <a-card
          title="欠压分布"
          :bordered="false"
          :headStyle="{ color: '#0785fd' }"
          :bodyStyle="{ padding: '10' }"
        >
          <div>
            <!-- <LineChartMultid :data-source="dataSource2" /> -->
          </div>
          <div>
            <div id="chartsThree"></div>
            <div id="chartsThree1"></div>
          </div>
        </a-card>
      </a-row>
      <!-- three -->

      <!-- four -->
      <a-row>
        <a-card
          title="碾压遍数"
          :bordered="false"
          :headStyle="{ color: '#0785fd' }"
          :bodyStyle="{ padding: '10' }"
        >
          <div>
            <div id="chartsFour"></div>
            <div id="chartsFour1"></div>
          </div>
          <div></div>
        </a-card>
      </a-row>
      <!-- four -->
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" v-has="'hntbhz:add'" type="primary" icon="plus"
        >新增</a-button
      >
      <a-button
        type="primary"
        v-has="'hntbhz:dc'"
        icon="download"
        @click="handleExportXls('拌合站主表')"
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
      </a-upload>
      <!-- 禁用高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" v-has="'hntbhz:del'" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
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
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{ x: true }"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <span slot="tags" slot-scope="tags, record">
          <a-tag color="green" v-if="record.overLevel == '0'">合格</a-tag>
          <a-tag color="orange" v-if="record.overLevel == '1'">初级超标</a-tag>
          <a-tag color="yellow" v-if="record.overLevel == '2'">中级超标</a-tag>
          <a-tag color="red" v-if="record.overLevel == '3'">高级超标</a-tag>
        </span>
        <span slot="formulaNo" slot-scope="formulaNo, record">
          <a-tag color="green">{{ record.formulaNo }}</a-tag>
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

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'hntbhz:del'">
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
  </a-card>
</template>
<script>
import BarAndMultidLine from "@comp/chart/BarAndMultidLine";
import LineChartMultid from "@comp/chart/BhzStafangliang";
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
import Vue from "vue";
export default {
  name: "MaxTemp",
  //mixins: [JeecgListMixin],
  mixins: [JeecgListMixin, handertoken],
  components: {
    BarAndMultidLine,
    // Pie,
    LineChartMultid,
    // JSuperQuery
    // BhzCementBaseModal,
    JSuperQuery,
  },
  data() {
    return {
      approval_remarks: null,
      selectValue: "",
      description: "拌合站主表管理页面",
      asyncSelectValue: "",
      dictOption: [],
      datalist: [],
      dataSource: [],
      fields: [],
      aliases: [],
      dataSource1: [],
      dataSource2: [],
      url: {
        // list: "/hntbhz/bhzCementBase/list6",
        // list2: "/hntbhz/bhzCementBase/list7",
        // delete: "/hntbhz/bhzCementBase/delete",
        // deleteBatch: "/hntbhz/bhzCementBase/deleteBatch",
        // exportXlsUrl: "/hntbhz/bhzCementBase/exportXls",
        // importExcelUrl: "hntbhz/bhzCementBase/importExcel",

        list: "/hntbhz/bhzCementBase/list",
        // list: '/hntbhz/bhzCementBase/listrc',
        delete: "/hntbhz/bhzCementBase/delete",
        deleteBatch: "/hntbhz/bhzCementBase/deleteBatch",
        exportXlsUrl: "/hntbhz/bhzCementBase/exportXls",
        importExcelUrl: "hntbhz/bhzCementBase/importExcel",
      },
      dictOptions: {},
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
        // {
        //   title:'所属组织机构',
        //   align:"center",
        //   dataIndex: 'sysOrgCode_dictText'
        // },
        {
          title: "设备名称",
          align: "center",
          dataIndex: "shebeiNo_dictText",
        },

        {
          title: "工程名称",
          align: "center",
          dataIndex: "projectName",
        },
        {
          title: "配方号/配比单号",
          align: "center",
          dataIndex: "formulaNo",
          scopedSlots: { customRender: "formulaNo" },
        },
        {
          title: "施工地点",
          align: "center",
          dataIndex: "jobLocation",
        },
        {
          title: "浇筑部位",
          align: "center",
          dataIndex: "poureLocation",
        },
        {
          title: "出料时间",
          align: "center",
          dataIndex: "productDatetime",
        },
        {
          title: "强度等级",
          align: "center",
          dataIndex: "strengthRank",
        },
        {
          title: "操作者",
          align: "center",
          dataIndex: "handlers",
        },
        {
          title: "方量",
          align: "center",
          dataIndex: "estimateNumber",
        },

        {
          title: "坍落度",
          align: "center",
          dataIndex: "slump",
        },
        {
          title: "超标等级",
          align: "center",
          dataIndex: "overLevel_dictText", //overLevel_dictText
          key: "overLevel_dictText",
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
      superFieldList: [],
      charts: null,
      charts1: null,
      dailyCharts: null,
      dailyCharts1: null,
      chartsFour: null,
      chartsFour1: null,
      pagination: {
        total: 0,
        pageSize: 10, //每页中显示10条数据
        showSizeChanger: true,
        pageSizeOptions: ["10", "20", "50", "100"], //每页中显示的数据
        showTotal: (total) => `共有 ${total} 条数据`, //分页中显示总的数据
        current: 1,
      },
      loading: true,
      // 查询参数
      queryParam: {
        page: 1, //第几页
        size: 10, //每页中显示数据的条数
        hosName: "",
        hosCode: "",
        province: "",
        city: "",
      },
      form: {
        startTime: "2021-04-01",
        endTime: "2021-12-10",
      },
    };
  },
  created() {
    // this.bhzsta();
    // this.fanglaing();
    // this.hegelv();
    this.getToken();
    this.getSuperFieldList();
    this.shebeiList();

    this.username();
  },
  mounted() {

    // this.getdailyCharts();
    // this.getdailyCharts1();

    this.getcharts();
    this.getcharts1();

    this.getchartsFour();
    this.getchartsFour1();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    username() {
      let param = { appId: "KFPT1", pw: 123456789 };
      this.$http1.post(`/api/token/get.shtml`, param).then((res) => {
        if (res.message == "请求成功") {
          this.$store.state.key = res.data.token;
          // console.log(res.data.token, "res.data.data.token---------------------");
          // this.getData();
          this.getChartOne();
          this.getChartTwo();
        }
      });
    },
    //碾压遍数统计
    getChartOne() {
      let param = {
        projectId: "jishu01@20201209210207",
        sectionId: "jishu01@20201209210431",
        startTime: this.form.startTime,
        endTime: this.form.endTime,
      };
      this.$http1.post(`/api/pavement/rollingTimesPass.shtml`, param).then((res) => {
        if (res.code == 0) {
          let arr = res.data;
          this.getecharts(arr);
          this.getecharts1(arr);
          this.getechartsR(arr);
          console.log(res, "getChartOne 碾压遍数统计------------------------");
        }
        this.loading = false;
      });
    },
    //每日欠压率
    getChartTwo() {
      let param = {
        projectId: "jishu01@20200813165458",
        sectionId: "jishu01@20201023010249",
        startTime: this.form.startTime,
        endTime: this.form.endTime,
      };
      this.$http1.post(`/api/pavement/dailyUnderVoltageRate.shtml`, param).then((res) => {
        if (res.code == 0) {
          // this.dataSource = res.data.records;
          // const pagination = { ...this.pagination };
          // pagination.total = res.data.rowCount;
          // this.pagination = pagination;
          let arr = res.data;
          this.getdailyCharts(arr);
          this.getdailyCharts1(arr);
          // console.log(res, "getChartTwo 每日欠压率------------------------");
        }
        this.loading = false;
      });
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
      this.cx = this.selectedValue;
      this.dates = this.approval_remarks;
      this.bhzsta();
      this.fanglaing();
      this.hegelv();
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
        value: "shebeiNo",
        text: "设备编号",
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
    getecharts(data) {
      console.log(data, "arr-------------------");
      data.steelNormal;
      let that = this;
      var colorList = [
        "green",
        "red",
        // "#FDD56A",
        // "#FDB36A",
        // "#FD866A",
        // "#9E87FF",
        // "#58D5FF",
      ];
      var arr = [
        { value: 0, name: "遍数正常" },
        { value: 0, name: "遍数过低" },
      ];
      arr[0].value = data.steelNormal;
      arr[1].value = 1 - data.steelNormal;
      let option = {
        color: colorList,
        title: {
          text: "钢轮碾压",
          // subtext:'50%',
          x: "258",
          y: "125",
          textStyle: {
            // color: "#fff",
            fontSize: 15,
          },
        },
        grid: {
          // borderWidth: 0,
          top: 220,
          bottom: 195,
          // textStyle: {
          //   color: "#fff",
          // },
        },
        legend: {
          orient: "vertical",
          left: "left",
          right: 60,
          top: 0,
          data: ["遍数正常", "遍数过低"],
          // data: ["TJ01", "TJ02", "TJ03", "TJ04"],
          textStyle: {
            // color: "#fff",
            // fontSize: 10,
          },
        },
        tooltip: {
          trigger: "item",
        },
        series: [
          {
            type: "pie",
            center: ["55%", "55%"],
            radius: ["40%", "70%"],
            clockwise: true,
            avoidLabelOverlap: true,
            hoverOffset: 15,
            itemStyle: {
              normal: {
                color: function (params) {
                  return colorList[params.dataIndex];
                },
              },
            },
            label: {
              show: true,
              position: "outside",
              formatter: "{a|{b}：{d}%}\n{hr|}",
              rich: {
                hr: {
                  backgroundColor: "t",
                  borderRadius: 3,
                  width: 3,
                  height: 3,
                  padding: [3, 3, 0, -12],
                },
                a: {
                  padding: [-20, -5, 0, 5],
                },
              },
            },
            labelLine: {
              normal: {
                length: 5,
                length2: 30,
                lineStyle: {
                  color: "#0b5263",
                },
              },
            },
            data: arr,
          },
        ],
      };
      let chart = echarts.init(document.getElementById("PavingTcharts"));
      chart.setOption(option);
      //点击传递当前标段
      chart.on("click", function (params) {
        that.$refs.LeftTwoTab.dialogVisible = true;
        // that.departList = that.bdList.filter(
        //   (item) => item.departname == params.seriesName
        // );
        that.departList = [];
        let id = that.data[that.tag].id;
        let departname = that.data[that.tag].departname;
        that.data[that.tag].data.forEach((e) => {
          // params.dataIndex
          if (params.dataIndex + 1 == e.month) {
            // console.log(e.month, "e--------------------");
            that.departList.push({
              id,
              departname,
              month2: e.month2,
              qiangdudengji: that.tag1,
            });
          }
        });
        console.log(that.departList, "departList------------------------");
        // console.log(params, that.tag1, "departList------------------------");
      });
      //建议加上以下这一行代码，不加的效果图如下（当浏览器窗口缩小的时候）。超过了div的界限（红色边框）
      window.addEventListener("resize", function () {
        chart.resize();
      });
    },
    getecharts1(data) {
      console.log(data, "arr-------------------");
      data.steelNormal;
      let that = this;
      var colorList = [
        "green",
        "red",
      ];
      var arr = [
        { value: 0, name: "遍数正常" },
        { value: 0, name: "遍数过低" },
      ];
      arr[0].value = data.rubberNormal;
      arr[1].value = 1 - data.rubberNormal;
      let option = {
        color: colorList,
        title: {
          text: "胶轮碾压",
          // subtext:'50%',
          x: "258",
          y: "125",
          textStyle: {
            // color: "#fff",
            fontSize: 15,
          },
        },
        grid: {
          // borderWidth: 0,
          top: 220,
          bottom: 195,
          // textStyle: {
          //   color: "#fff",
          // },
        },
        legend: {
          orient: "vertical",
          left: "left",
          right: 60,
          top: 0,
          data: ["遍数正常", "遍数过低"],
          // data: ["TJ01", "TJ02", "TJ03", "TJ04"],
          textStyle: {
            // color: "#fff",
            // fontSize: 10,
          },
        },
        tooltip: {
          trigger: "item",
        },
        series: [
          {
            type: "pie",
            center: ["55%", "55%"],
            radius: ["40%", "70%"],
            clockwise: true,
            avoidLabelOverlap: true,
            hoverOffset: 15,
            itemStyle: {
              normal: {
                color: function (params) {
                  return colorList[params.dataIndex];
                },
              },
            },
            label: {
              show: true,
              position: "outside",
              formatter: "{a|{b}：{d}%}\n{hr|}",
              rich: {
                hr: {
                  backgroundColor: "t",
                  borderRadius: 3,
                  width: 3,
                  height: 3,
                  padding: [3, 3, 0, -12],
                },
                a: {
                  padding: [-20, -5, 0, 5],
                },
              },
            },
            labelLine: {
              normal: {
                length: 5,
                length2: 30,
                lineStyle: {
                  color: "#0b5263",
                },
              },
            },
            data: arr,
          },
        ],
      };
      let chart = echarts.init(document.getElementById("PavingTcharts1"));
      chart.setOption(option);
      //建议加上以下这一行代码，不加的效果图如下（当浏览器窗口缩小的时候）。超过了div的界限（红色边框）
      window.addEventListener("resize", function () {
        chart.resize();
      });
    },
    getechartsR(data) {
      console.log(data, "arr-------------------");
      data.steelNormal;
      let that = this;
      var colorList = [
        "green",
        "red",
      ];
      var arr = [
        { value: 0, name: "遍数正常" },
        { value: 0, name: "遍数过低" },
      ];
      arr[0].value = data.mixNormal;
      arr[1].value = 1 - data.mixNormal;
      let option = {
        color: colorList,
        title: {
          text: "总遍数",
          // subtext:'50%',
          x: "264",
          y: "125",
          textStyle: {
            // color: "#fff",
            fontSize: 15,
          },
        },
        grid: {
          // borderWidth: 0,
          top: 220,
          bottom: 195,
          // textStyle: {
          //   color: "#fff",
          // },
        },
        legend: {
          orient: "vertical",
          left: "left",
          right: 60,
          top: 0,
          data: ["遍数正常", "遍数过低"],
          // data: ["TJ01", "TJ02", "TJ03", "TJ04"],
          textStyle: {
            // color: "#fff",
            // fontSize: 10,
          },
        },
        tooltip: {
          trigger: "item",
        },
        series: [
          {
            type: "pie",
            center: ["55%", "55%"],
            radius: ["40%", "70%"],
            clockwise: true,
            avoidLabelOverlap: true,
            hoverOffset: 15,
            itemStyle: {
              normal: {
                color: function (params) {
                  return colorList[params.dataIndex];
                },
              },
            },
            label: {
              show: true,
              position: "outside",
              formatter: "{a|{b}：{d}%}\n{hr|}",
              rich: {
                hr: {
                  backgroundColor: "t",
                  borderRadius: 3,
                  width: 3,
                  height: 3,
                  padding: [3, 3, 0, -12],
                },
                a: {
                  padding: [-20, -5, 0, 5],
                },
              },
            },
            labelLine: {
              normal: {
                length: 5,
                length2: 30,
                lineStyle: {
                  color: "#0b5263",
                },
              },
            },
            data: arr,
          },
        ],
      };
      let chart = echarts.init(document.getElementById("PavingTchartsR"));
      chart.setOption(option);
      //建议加上以下这一行代码，不加的效果图如下（当浏览器窗口缩小的时候）。超过了div的界限（红色边框）
      window.addEventListener("resize", function () {
        chart.resize();
      });
    },
    //对数据进行处理
    processData(arr, value) {
      let dataList = {
        month: [],
        arr: [],
      };
      dataList.month = arr.map((e) => {
        return e.date;
      });
      dataList.arr = arr.map((e) => {
        return e[value];
      });
      return dataList;
    },
    getdailyCharts(arr) {
      let variable = "steel";
      let data = this.processData(arr, variable);
      // console.log(arr,'getdailyCharts -----------------------------');
      const option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
            textStyle: {
              color: "#fff",
            },
          },
        },
        grid: {
          borderWidth: 0,
          top: 110,
          bottom: 95,
          textStyle: {
            color: "#fff",
          },
        },
        legend: {
          // x: '4%',
          // top: '8%',
          textStyle: {
            color: "#90979c",
          },
          data: ["钢轮"],
        },

        calculable: true,
        xAxis: [
          {
            type: "category",
            axisLine: {
              lineStyle: {
                color: "#90979c",
              },
            },
            // "splitLine": {
            //     "show": false
            // },
            // "axisTick": {
            //     "show": false
            // },
            // "splitArea": {
            //     "show": false
            // },
            axisLabel: {
              interval: 0,
            },
            data: data.month,
          },
        ],
        yAxis: [
          {
            type: "value",
            // "splitLine": {
            //     "show": false
            // },
            axisLine: {
              // lineStyle: {
              //     color: '#90979c'
              // }
            },
            // "axisTick": {
            //     "show": false
            // },
            // "axisLabel": {
            //     "interval": 0,

            // },
            // "splitArea": {
            //     "show": false
            // },
          },
        ],
        dataZoom: [
          {
            show: true,
            height: 30,
            xAxisIndex: [0],
            bottom: 30,
            start: 10,
            end: 80,
            handleIcon:
              "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
            handleSize: "110%",
            handleStyle: {
              color: "#d3dee5",
            },
            textStyle: {
              color: "#fff",
            },
            borderColor: "#90979c",
          },
          {
            type: "inside",
            show: true,
            height: 15,
            start: 1,
            end: 35,
          },
        ],
        series: [
          {
            name: "钢轮",
            type: "bar",
            barMaxWidth: 35,
            itemStyle: {
              normal: {
                color: "rgba(255,144,128,1)",
                label: {
                  show: true,
                  textStyle: {
                    color: "#fff",
                  },
                  position: "inside",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: data.arr,
          },
        ],
      };
      if (this.dailyCharts) {
        this.dailyCharts.dispose();
      }
      this.dailyCharts = echarts.init(document.getElementById("dailyCount"));
      this.dailyCharts.setOption(option);
    },
    getdailyCharts1(arr) {
      let variable = "rubber";
      let data = this.processData(arr, variable);
      const option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
            textStyle: {
              color: "#fff",
            },
          },
        },
        grid: {
          borderWidth: 0,
          top: 110,
          bottom: 95,
          textStyle: {
            color: "#fff",
          },
        },
        legend: {
          // x: '4%',
          // top: '8%',
          textStyle: {
            color: "#90979c",
          },
          data: ["胶轮"],
        },

        calculable: true,
        xAxis: [
          {
            type: "category",
            axisLine: {
              lineStyle: {
                color: "#90979c",
              },
            },
            axisLabel: {
              interval: 0,
            },
            data: data.month,
          },
        ],
        yAxis: [
          {
            type: "value",
            axisLine: {},
          },
        ],
        dataZoom: [
          {
            show: true,
            height: 30,
            xAxisIndex: [0],
            bottom: 30,
            start: 10,
            end: 80,
            handleIcon:
              "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
            handleSize: "110%",
            handleStyle: {
              color: "#d3dee5",
            },
            textStyle: {
              color: "#fff",
            },
            borderColor: "#90979c",
          },
          {
            type: "inside",
            show: true,
            height: 15,
            start: 1,
            end: 35,
          },
        ],
        series: [
          {
            name: "胶轮",
            type: "bar",
            barMaxWidth: 35,
            itemStyle: {
              normal: {
                color: "rgba(0,191,183,1)",
                barBorderRadius: 0,
                label: {
                  show: true,
                  position: "inside",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: data.arr,
          },
        ],
      };
      if (this.dailyCharts1) {
        this.dailyCharts1.dispose();
      }
      this.dailyCharts1 = echarts.init(document.getElementById("dailyCount1"));
      this.dailyCharts1.setOption(option);
    },
    // three
    getcharts() {
      var xData = (function () {
        var data = [];
        for (var i = 1; i < 13; i++) {
          data.push(i + "月份");
        }
        return data;
      })();

      const option = {
        // backgroundColor: "#344b58",
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
            textStyle: {
              color: "#fff",
            },
          },
        },
        grid: {
          top: 10,
          bottom: 5,
          // textStyle: {
          //   color: "#fff",
          // },
        },
        legend: {
          // x: '4%',
          // top: '8%',
          textStyle: {
            color: "#90979c",
          },
          data: ["胶轮"],
        },

        // calculable: true,
        xAxis: [
          {
            type: "category",
            boundaryGap: false,
            axisLine: {
              lineStyle: {
                color: "#90979c",
              },
            },
            // "splitLine": {
            //     "show": false
            // },
            // "axisTick": {
            //     "show": false
            // },
            // "splitArea": {
            //     "show": false
            // },
            // axisLabel: {
            //   interval: 0,
            // },
            data: xData,
          },
        ],
        yAxis: [
          {
            type: "value",
            // "splitLine": {
            //     "show": false
            // },
            axisLine: {
              // lineStyle: {
              //     color: '#90979c'
              // }
            },
            // "axisTick": {
            //     "show": false
            // },
            // "axisLabel": {
            //     "interval": 0,

            // },
            // "splitArea": {
            //     "show": false
            // },
          },
        ],
        dataZoom: [
          {
            show: true,
            height: 30,
            xAxisIndex: [0],
            bottom: 30,
            start: 10,
            end: 80,
            handleIcon:
              "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
            handleSize: "110%",
            handleStyle: {
              color: "#d3dee5",
            },
            textStyle: {
              color: "#fff",
            },
            borderColor: "#90979c",
          },
          {
            type: "inside",
            show: true,
            height: 15,
            start: 1,
            end: 35,
          },
        ],
        series: [
          {
            name: "胶轮",
            type: "line",
            barMaxWidth: 35,
            itemStyle: {
              normal: {
                color: "rgba(0,191,183,1)",
                barBorderRadius: 0,
                label: {
                  show: true,
                  position: "top",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: [327, 1776, 507, 1200, 800, 482, 204, 1390, 1001, 951, 381, 220],
          },
        ],
      };
      if (this.charts) {
        this.charts.dispose();
      }
      this.charts = echarts.init(document.getElementById("chartsThree"));
      this.charts.setOption(option);
    },
    getcharts1() {
      var xData = (function () {
        var data = [];
        for (var i = 1; i < 13; i++) {
          data.push(i + "月份");
        }
        return data;
      })();

      const option = {
        // backgroundColor: "#344b58",
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
            textStyle: {
              color: "#fff",
            },
          },
        },
        grid: {
          borderWidth: 0,
          top: 110,
          bottom: 95,
          textStyle: {
            color: "#fff",
          },
        },
        legend: {
          // x: '4%',
          // top: '8%',
          textStyle: {
            color: "#90979c",
          },
          data: ["胶轮"],
        },

        // calculable: true,
        xAxis: [
          {
            type: "category",
            boundaryGap: false,
            axisLine: {
              lineStyle: {
                color: "#90979c",
              },
            },
            // "splitLine": {
            //     "show": false
            // },
            // "axisTick": {
            //     "show": false
            // },
            // "splitArea": {
            //     "show": false
            // },
            // axisLabel: {
            //   interval: 0,
            // },
            data: xData,
          },
        ],
        yAxis: [
          {
            type: "value",
            // "splitLine": {
            //     "show": false
            // },
            axisLine: {
              // lineStyle: {
              //     color: '#90979c'
              // }
            },
            // "axisTick": {
            //     "show": false
            // },
            // "axisLabel": {
            //     "interval": 0,

            // },
            // "splitArea": {
            //     "show": false
            // },
          },
        ],
        dataZoom: [
          {
            show: true,
            height: 30,
            xAxisIndex: [0],
            bottom: 30,
            start: 10,
            end: 80,
            handleIcon:
              "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
            handleSize: "110%",
            handleStyle: {
              color: "#d3dee5",
            },
            textStyle: {
              color: "#fff",
            },
            borderColor: "#90979c",
          },
          {
            type: "inside",
            show: true,
            height: 15,
            start: 1,
            end: 35,
          },
        ],
        series: [
          {
            name: "胶轮",
            type: "line",
            barMaxWidth: 35,
            itemStyle: {
              normal: {
                color: "rgba(0,191,183,1)",
                barBorderRadius: 0,
                label: {
                  show: true,
                  position: "top",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: [327, 1776, 507, 1200, 800, 482, 204, 1390, 1001, 951, 381, 220],
          },
        ],
      };
      if (this.charts1) {
        this.charts1.dispose();
      }
      this.charts1 = echarts.init(document.getElementById("chartsThree1"));
      this.charts1.setOption(option);
    },
    // three

    // four
    getchartsFour() {
      var xData = (function () {
        var data = [];
        for (var i = 1; i < 13; i++) {
          data.push(i + "月份");
        }
        return data;
      })();

      const option = {
        // backgroundColor: "#344b58",
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
            textStyle: {
              color: "#fff",
            },
          },
        },
        grid: {
          borderWidth: 0,
          top: 110,
          bottom: 95,
          textStyle: {
            color: "#fff",
          },
        },
        legend: {
          // x: '4%',
          // top: '8%',
          textStyle: {
            color: "#90979c",
          },
          data: ["胶轮"],
        },

        // calculable: true,
        xAxis: [
          {
            type: "category",
            boundaryGap: false,
            axisLine: {
              lineStyle: {
                color: "#90979c",
              },
            },
            // "splitLine": {
            //     "show": false
            // },
            // "axisTick": {
            //     "show": false
            // },
            // "splitArea": {
            //     "show": false
            // },
            // axisLabel: {
            //   interval: 0,
            // },
            data: xData,
          },
        ],
        yAxis: [
          {
            type: "value",
            // "splitLine": {
            //     "show": false
            // },
            axisLine: {
              // lineStyle: {
              //     color: '#90979c'
              // }
            },
            // "axisTick": {
            //     "show": false
            // },
            // "axisLabel": {
            //     "interval": 0,

            // },
            // "splitArea": {
            //     "show": false
            // },
          },
        ],
        dataZoom: [
          {
            show: true,
            height: 30,
            xAxisIndex: [0],
            bottom: 30,
            start: 10,
            end: 80,
            handleIcon:
              "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
            handleSize: "110%",
            handleStyle: {
              color: "#d3dee5",
            },
            textStyle: {
              color: "#fff",
            },
            borderColor: "#90979c",
          },
          {
            type: "inside",
            show: true,
            height: 15,
            start: 1,
            end: 35,
          },
        ],
        series: [
          {
            name: "胶轮",
            type: "line",
            barMaxWidth: 35,
            itemStyle: {
              normal: {
                color: "rgba(0,191,183,1)",
                barBorderRadius: 0,
                label: {
                  show: true,
                  position: "top",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: [327, 1776, 507, 1200, 800, 482, 204, 1390, 1001, 951, 381, 220],
          },
        ],
      };
      if (this.chartsFour) {
        this.chartsFour.dispose();
      }
      this.chartsFour = echarts.init(document.getElementById("chartsFour"));
      this.chartsFour.setOption(option);
    },
    getchartsFour1() {
      var xData = (function () {
        var data = [];
        for (var i = 1; i < 13; i++) {
          data.push(i + "月份");
        }
        return data;
      })();

      const option = {
        // backgroundColor: "#344b58",
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
            textStyle: {
              color: "#fff",
            },
          },
        },
        grid: {
          borderWidth: 0,
          top: 110,
          bottom: 95,
          textStyle: {
            color: "#fff",
          },
        },
        legend: {
          // x: '4%',
          // top: '8%',
          textStyle: {
            color: "#90979c",
          },
          data: ["胶轮"],
        },

        // calculable: true,
        xAxis: [
          {
            type: "category",
            boundaryGap: false,
            axisLine: {
              lineStyle: {
                color: "#90979c",
              },
            },
            // "splitLine": {
            //     "show": false
            // },
            // "axisTick": {
            //     "show": false
            // },
            // "splitArea": {
            //     "show": false
            // },
            // axisLabel: {
            //   interval: 0,
            // },
            data: xData,
          },
        ],
        yAxis: [
          {
            type: "value",
            // "splitLine": {
            //     "show": false
            // },
            axisLine: {
              // lineStyle: {
              //     color: '#90979c'
              // }
            },
            // "axisTick": {
            //     "show": false
            // },
            // "axisLabel": {
            //     "interval": 0,

            // },
            // "splitArea": {
            //     "show": false
            // },
          },
        ],
        dataZoom: [
          {
            show: true,
            height: 30,
            xAxisIndex: [0],
            bottom: 30,
            start: 10,
            end: 80,
            handleIcon:
              "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
            handleSize: "110%",
            handleStyle: {
              color: "#d3dee5",
            },
            textStyle: {
              color: "#fff",
            },
            borderColor: "#90979c",
          },
          {
            type: "inside",
            show: true,
            height: 15,
            start: 1,
            end: 35,
          },
        ],
        series: [
          {
            name: "胶轮",
            type: "line",
            barMaxWidth: 35,
            itemStyle: {
              normal: {
                color: "rgba(0,191,183,1)",
                barBorderRadius: 0,
                label: {
                  show: true,
                  position: "top",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: [327, 1776, 507, 1200, 800, 482, 204, 1390, 1001, 951, 381, 220],
          },
        ],
      };
      if (this.chartsFour1) {
        this.chartsFour1.dispose();
      }
      this.chartsFour1 = echarts.init(document.getElementById("chartsFour1"));
      this.chartsFour1.setOption(option);
    },
    // four
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
  height: 250px;
  /* background-color: #d66f6f; */
}
#PavingTcharts1 {
  width: 100%;
  height: 250px;
}
#PavingTchartsR {
  width: 100%;
  height: 250px;
}
#dailyCount {
  width: 100%;
  height: 400px;
}
#dailyCount1 {
  width: 100%;
  height: 400px;
}
#chartsThree {
  width: 100%;
  height: 400px;
}
#chartsThree1 {
  width: 100%;
  height: 400px;
}
#chartsFour {
  width: 100%;
  height: 400px;
}
#chartsFour1 {
  width: 100%;
  height: 400px;
}
</style>
