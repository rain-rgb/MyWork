<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="18" :style="{ marginBottom: '24px' }">
        <div class="cont-main">
          <div id="HomeContainer" style="height: 43vh"></div>
        </div>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <div class="cont-main">
          <div id="homeTempSegr"></div>
        </div>
      </a-col>
    </a-row>
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <div class="cont-main">
          <div id="HomePavingSpeed"></div>
        </div>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <div class="cont-main">
          <div id="charts2"></div>
        </div>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <div class="cont-main">
          <div id="charts3"></div>
        </div>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <div class="cont-main">
          <div id="charts4"></div>
        </div>
      </a-col>
    </a-row>
    <a-row :gutter="12">
      <a-card
        :loading="loading"
        :class="{ 'anty-list-cust': true }"
        :bordered="false"
        :style="{ marginTop: '24px' }"
      >
        <a-tabs
          v-model="indexBottomTab"
          size="large"
          :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }"
        >
          <a-tab-pane loading="true" tab="梁场制梁工序进度" key="1">
            <a-table
              :dataSource="dataSource1"
              size="default"
              rowKey="id"
              :columns="columns"
              :pagination="ipagination1"
              @change="tableChange1"
            >
              <span slot="projname" slot-scope="projname, record">
                <a-tag color="orange">{{ record.projname }}</a-tag>
              </span>
              <span slot="code" slot-scope="code, record">
                <a-tag color="blue">{{ record.code }}</a-tag>
              </span>
              <span slot="recipe" slot-scope="recipe, record">
                <a-tag color="blue">{{ record.recipe }}</a-tag>
              </span>
              <template slot="flowRate" slot-scope="text, record, index">
                <a-progress
                  :strokeColor="getPercentColor(record.flag)"
                  :format="getPercentFormat"
                  :percent="getFlowRateNumber(record.flag)"
                  style="width: 80px"
                />
              </template>
            </a-table>
          </a-tab-pane>
        </a-tabs>
      </a-card>
    </a-row>
  </div>
</template>

<script>
import ACol from "ant-design-vue/es/grid/Col";
import ATooltip from "ant-design-vue/es/tooltip/Tooltip";
import ChartCard from "@/components/ChartCard";
import MiniBar from "@/components/chart/MiniBar";
import MiniArea from "@/components/chart/MiniArea";
import IndexBar from "@/components/chart/IndexBar";
import BarMultid from "@/components/chart/BarMultid";
import DashChartDemo from "@/components/chart/DashChartDemo";
import { Empty } from "ant-design-vue";
import { getAction } from "@api/manage";
import { SYS_DEPART_ORGCODE } from "@/store/mutation-types";
import Vue from "vue";
import * as echarts from "echarts";

const jhjgData = [
  {
    type: "房管",
    "1月": 900,
    "2月": 1120,
    "3月": 1380,
    "4月": 1480,
    "5月": 1450,
    "6月": 1100,
    "7月": 1300,
    "8月": 900,
    "9月": 1000,
    "10月": 1200,
    "11月": 600,
    "12月": 900,
  },
  {
    type: "税务",
    "1月": 1200,
    "2月": 1500,
    "3月": 1980,
    "4月": 2000,
    "5月": 1000,
    "6月": 600,
    "7月": 900,
    "8月": 1100,
    "9月": 1300,
    "10月": 2000,
    "11月": 900,
    "12月": 1100,
  },
  {
    type: "不动产",
    "1月": 2000,
    "2月": 1430,
    "3月": 1300,
    "4月": 1400,
    "5月": 900,
    "6月": 500,
    "7月": 600,
    "8月": 1000,
    "9月": 600,
    "10月": 1000,
    "11月": 1500,
    "12月": 1200,
  },
];

const jhjgFields = [
  "1月",
  "2月",
  "3月",
  "4月",
  "5月",
  "6月",
  "7月",
  "8月",
  "9月",
  "10月",
  "11月",
  "12月",
];

const xljgData = [
  { type: "一月", 房管: 1.12, 税务: 1.55, 不动产: 1.2 },
  { type: "二月", 房管: 1.65, 税务: 1.32, 不动产: 1.42 },
  { type: "三月", 房管: 1.85, 税务: 1.1, 不动产: 1.5 },

  { type: "四月", 房管: 1.33, 税务: 1.63, 不动产: 1.4 },
  { type: "五月", 房管: 1.63, 税务: 1.8, 不动产: 1.7 },
  { type: "六月", 房管: 1.85, 税务: 1.98, 不动产: 1.8 },

  { type: "七月", 房管: 1.98, 税务: 1.5, 不动产: 1.76 },
  { type: "八月", 房管: 1.48, 税务: 1.2, 不动产: 1.3 },
  { type: "九月", 房管: 1.41, 税务: 1.9, 不动产: 1.6 },

  { type: "十月", 房管: 1.1, 税务: 1.1, 不动产: 1.4 },
  { type: "十一月", 房管: 1.85, 税务: 1.6, 不动产: 1.5 },
  { type: "十二月", 房管: 1.5, 税务: 1.4, 不动产: 1.3 },
];
const xljgFields = ["房管", "税务", "不动产"];

const dataCol1 = [
  {
    title: "项目名称",
    align: "center",
    dataIndex: "projname",
    scopedSlots: { customRender: "projname" },
  },
  {
    title: "任务单编号",
    align: "center",
    dataIndex: "code",
    key: "code",
    scopedSlots: { customRender: "code" },
  },
  {
    title: "配合比编号",
    align: "center",
    dataIndex: "recipe",
    key: "recipe",
    scopedSlots: { customRender: "recipe" },
  },
  {
    title: "创建时间",
    align: "center",
    dataIndex: "dattim",
  },
  {
    title: "施工部位",
    align: "center",
    dataIndex: "conspos",
  },
  {
    title: "进度",
    align: "center",
    dataIndex: "flag",
    scopedSlots: { customRender: "flowRate" },
  },
];

export default {
  beforeCreate() {
    this.simpleImage = Empty.PRESENTED_IMAGE_SIMPLE;
  },
  name: "IndexBdc",
  components: {
    ATooltip,
    ACol,
    ChartCard,
    MiniArea,
    MiniBar,
    DashChartDemo,
    BarMultid,
    IndexBar,
  },
  data() {
    return {
      imgUrl: require("@/assets/img/liang.png"),
      imgUrl1: require("@/assets/img/liang1.png"),
      loading: true,
      cardCount: {
        sll: 100,
        bjl: 87,
        isll: 15,
        ibjl: 9,
      },
      barData: [],
      renwudanhander: {
        renwudanzong: 0,
        renwudanbenyue: 0,
        renwudanysh: 0,
        renwudanyshbenyue: 0,
        renwudanweishenhezong: 0,
        renwudanweishenhebenyue: 0,
        renwudanwc: 0,
        renwudanwcbenyue: 0,
      },
      zhonglist: [],
      todaySll: 60,
      todayBjl: 54,
      todayISll: 13,
      todayIBjl: 7,
      chartData: {
        sll: [],
        bjl: [],
        isll: [],
        ibjl: [],
      },
      jhjgFields,
      jhjgData,
      xljgData,
      xljgFields,
      diskInfo: [
        { name: "C盘", restPPT: 7 },
        { name: "D盘", restPPT: 5 },
      ],
      dataSource1: [],
      dataSource2: [],
      columns: dataCol1,
      // columns2:dataCol2,
      ipagination1: {
        current: 1,
        pageSize: 5,
        pageSizeOptions: ["10", "20", "30"],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      ipagination2: {
        current: 1,
        pageSize: 5,
        pageSizeOptions: ["10", "20", "30"],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      Starttime: null,
      Endtime: null,
      indexRegisterType: "转移登记",
      indexBottomTab: "1",
      url: {
        renwujindu: "/zhiliangrenwudan/zhiliangrenwudan/list1",
        renwudanzong: "/zhiliangrenwudan/zhiliangrenwudan/list2",
        renwudanzongs: "/zhiliangrenwudan/zhiliangrenwudan/list13",
        renwudanzongbenyue: "/zhiliangrenwudan/zhiliangrenwudan/list3",
        renwudansh: "/zhiliangrenwudan/zhiliangrenwudan/list4",
        renwudanshbenyue: "/zhiliangrenwudan/zhiliangrenwudan/list5",
        zhongjianrenwudan: "/zhiliangrenwudan/zhiliangrenwudan/list6",
        liangxinxi: "/cunliang/beammanagementProcedure/list3",
      },
      charts: null,
      testData:{
        "message": "请求成功",
        "code": 0,
        "data": [
          {
            "id": "1637856109419",
            "machineId": "1284",
            "gpsTime": "2021-11-26 16:34:07",
            "temperature": "24;0;0;0;0",
            "velocity": "2.73513560565364",
            "tempDiff": "0.0"
          },
          {
            "id": "1637856111497",
            "machineId": "1285",
            "gpsTime": "2021-11-26 16:43:35",
            "temperature": "29;0;0;0;0",
            "velocity": "2.79456075500249",
            "tempDiff": "0.0"
          },
          {
            "id": "1637856116495",
            "machineId": "1286",
            "gpsTime": "2021-11-26 17:10:47",
            "temperature": "81;0;0;0;0",
            "velocity": "3.05145905496616",
            "tempDiff": "0.0"
          },
          {
            "id": "1637856137112",
            "machineId": "1318",
            "gpsTime": "2021-11-26 16:40:56",
            "temperature": "",
            "velocity": "3.48148072763096",
            "tempDiff": "0.0"
          }
        ]
      }
    };
  },
  created() {
    this.renwudanjindu(); //制梁任务单进度
    this.renwudanzong(); //制梁任务单(总)
    this.renwudanzongbenyue(); //制梁任务单(总本月)
    this.renwudansh(); //已审核总
    this.renwudanwsh(); //未审核总
    this.renwudanshbenyue(); //已审核总本月
    this.renwudanzhongjian1(); //中间部分每月统计
    this.liangxinxitaizuo(); //梁入台座信息
    setTimeout(() => {
      this.loading = !this.loading;
      this.renwudanwancheng(); //任务单完成总量
      this.renwudanwanchengbenyue(); //任务单完成本月量
      this.renwudanwshbenyue(); //未审核本月
    }, 1000);
    this.username();
  },
  mounted() {
    this.getcharts1();
    this.getcharts2();
    this.getcharts3();
    this.getcharts4();
    this.initMap();
  },
  methods: {
    username() {
      // let username = this.$route.query.username;
      // console.log(username, "username----------------------");
      let param = { appId: "KFPT1", pw: 123456789 };
      // let param = { appId: "openapi", pw: 123456789 };
      this.$http1.post(`/api/token/get.shtml`, param).then((res) => {
        // console.log(res.status == 200,message, "token/get.shtml------------------------");
        if (res.message == "请求成功") {
          this.$store.state.key = res.data.token;
          console.log(res.data.token, "res.data.data.token---------------------");
          // this.getData();
          this.gethomeTempSegr();
        }
      });
    },
    //获取温度离析数据
    gethomeTempSegr(){
      // let username = this.$route.query.username;
      let param = {
        projectId: "jishu01@20210712235430",//"jishu01@20190425211048"
        sectionId: "jishu01@20210712235746",//jishu01@20190425211403
        machineIds: [1284,1285],
      };
      this.$http1.post(`/api/pavement/curData.shtml`, param).then((res) => {//数据联动
        console.log(res, "获取温度离析数据------------------------");
        if (res.code === 0 ) {
          let arr = res.data
          if(arr.length ==0){
            arr = this.testData.data;
            console.log(arr,'arr -------------------------------------------------');
          }
          this.homeTempSegrChart(arr);
        }
      });
    },
    //获取摊铺速度数据
    gethomeTempSegr1(){
      // let username = this.$route.query.username;
      let param = {
        projectId: "jishu01@20210712235430",//"jishu01@20190425211048"
        sectionId: "jishu01@20210712235746",//jishu01@20190425211403
        machineIds: [1284,1285],
      };
      this.$http1.post(`/api/pavement/curData.shtml`, param).then((res) => {//数据联动
        console.log(res, "获取温度离析数据------------------------");
        if (res.code === 0 ) {
          // let arr = res.data
          this.homeTempSegrChart();
        }
      });
    },
    // getData() {
    //   // let username = this.$route.query.username;
    //   console.log(11111111, "11111111----------------------");
    //   let param = {
    //     projectId: "jishu03@20200511123137",//"jishu01@20190425211048"
    //     sectionId: "jishu01@20210428143945",//jishu01@20190425211403
    //     materialType: "2",
    //   };
    //   this.$http1.post(`/api/pavement/dataLinkage.shtml`, param).then((res) => {//数据联动
    //     console.log(res, "getData------------------------");
    //     if (res.success) {
    //     }
    //   });
    // },
    homeTempSegrChart(data) {
      let gpsTime = data[0].gpsTime;
      
      let titleTime = "3月3日"
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
        title: {
          text: `${titleTime}温度离析`,
          textStyle: { color: "#000000", fontSize: 20, fontWeight: "normal" },
          x: "center",
          top: "2%",
        },
        grid: {
          borderWidth: 0,
          top: 80,
          bottom: 55,
          textStyle: {
            color: "#fff",
          },
        },
        // legend: {
        //   // x: '4%',
        //   // top: '8%',
        //   textStyle: {
        //     color: "#90979c",
        //   },
        //   data: ["胶轮"],
        // },
        xAxis: [
          {
            type: "category",
            boundaryGap: false,
            axisLine: {
              lineStyle: {
                color: "#90979c",
              },
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            name: "温度（℃）",
            type: "value",
            // "splitLine": {
            //     "show": false
            // },
            axisLine: {},
          },
        ],
        // dataZoom: [
        //   {
        //     show: true,
        //     height: 30,
        //     xAxisIndex: [0],
        //     bottom: 30,
        //     start: 10,
        //     end: 80,
        //     handleIcon:
        //       "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
        //     handleSize: "110%",
        //     handleStyle: {
        //       color: "#d3dee5",
        //     },
        //     textStyle: {
        //       color: "#fff",
        //     },
        //     borderColor: "#90979c",
        //   },
        //   {
        //     type: "inside",
        //     show: true,
        //     height: 15,
        //     start: 1,
        //     end: 35,
        //   },
        // ],
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
      let charts = null;
      if (charts) {
        charts.dispose();
      }
      charts = echarts.init(document.getElementById("homeTempSegr"));
      charts.setOption(option);
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
        title: {
          text: "02月23日摊铺温度",
          textStyle: { color: "#000000", fontSize: 20, fontWeight: "normal" },
          x: "center",
          top: "2%",
        },
        grid: {
          borderWidth: 0,
          top: 80,
          bottom: 55,
          textStyle: {
            color: "#fff",
          },
        },
        // legend: {
        //   // x: '4%',
        //   // top: '8%',
        //   textStyle: {
        //     color: "#90979c",
        //   },
        //   data: ["胶轮"],
        // },
        xAxis: [
          {
            type: "category",
            boundaryGap: false,
            axisLine: {
              lineStyle: {
                color: "#90979c",
              },
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            name: "温度（℃）",
            type: "value",
            // "splitLine": {
            //     "show": false
            // },
            axisLine: {},
          },
        ],
        // dataZoom: [
        //   {
        //     show: true,
        //     height: 30,
        //     xAxisIndex: [0],
        //     bottom: 30,
        //     start: 10,
        //     end: 80,
        //     handleIcon:
        //       "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
        //     handleSize: "110%",
        //     handleStyle: {
        //       color: "#d3dee5",
        //     },
        //     textStyle: {
        //       color: "#fff",
        //     },
        //     borderColor: "#90979c",
        //   },
        //   {
        //     type: "inside",
        //     show: true,
        //     height: 15,
        //     start: 1,
        //     end: 35,
        //   },
        // ],
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
      let charts = null;
      if (charts) {
        charts.dispose();
      }
      charts = echarts.init(document.getElementById("HomePavingSpeed"));
      charts.setOption(option);
    },
    initMap() {
      var that = this;
      var shebeiNo = null;
      var lineArr = [
        [116.478935, 39.997761],
        [108.983569, 34.285675],
        [103.85094, 35.987496],
        [106.205794, 38.458831],
        [111.761777, 40.875595],
      ];
      setTimeout(function () {
        that.map = new AMap.Map("HomeContainer", {
          resizeEnable: true, //窗口大小调整
          center: [116.478935, 39.997761], //中心 firstArr: ,
          // center: that.center, //中心 firstArr: [116.478935, 39.997761],
          zoom: 17,
          mapStyle: "amap://styles/normal",
        });
        // 绘制还未经过的路线
        that.polyline = new AMap.Polyline({
          map: that.map,
          path: lineArr,
          showDir: true,
          strokeColor: "#77DDFF", // 线颜色--浅蓝色
          // strokeOpacity: 1,     //线透明度
          strokeWeight: 6, // 线宽
          // strokeStyle: "solid"  //线样式
          lineJoin: "round", // 折线拐点的绘制样式
        });
        that.map.setFitView(); //合适的视口
      }, 500);
    },
    liangxinxitaizuo: function () {
      let param = { sta: 1, sta1: 1 };
      this.zhonglist = [];
      getAction(this.url.liangxinxi, param).then((res) => {
        // console.log(res, "存梁区信息");
        if (res.success) {
          // this.zhonglist = res.result
          for (let i = 0; i < res.result.length; i = i + 12) {
            this.zhonglist.push(res.result.slice(i, i + 12));
          }
        }
      });
    },
    onChange(date, dateString) {
      //根据时间重新去统计月数据
      //console.log(date, dateString);
      this.Starttime = dateString[0];
      this.Endtime = dateString[1];
      this.renwudanzhongjian1();
    },
    renwudanzhongjian1() {
      this.sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      let params = {
        sys_depart_orgcode: this.sys_depart_orgcode,
        dattim_begin: this.Starttime,
        dattim_end: this.Endtime,
        status: 1,
        isdel: 0,
      };
      getAction(this.url.zhongjianrenwudan, params).then((res) => {
        //console.log(res)
        if (res.success) {
          let data = res.result;
          this.barData = [];
          this.pieData = [];
          for (let i = 0; i < data.length; i++) {
            this.barData.push({ x: data[i].Dattim + "月", y: data[i].Flag });
          }
        }
      });
    },
    goPage() {
      //制梁任务单查询
      this.$router.push("/zhiliang/zhiliangrenwudan/ZhiliangrenwudanList");
    },
    goPage1() {
      //制梁任务单审核
      this.$router.push("/zhiliang/zhiliangrenwudan/ZhiliangrenwudanSHList");
    },
    renwudanjindu() {
      //制梁任务单进度
      this.sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      let param = { sys_depart_orgcode: this.sys_depart_orgcode };
      getAction(this.url.renwujindu, param).then((res) => {
        if (res.success) {
          // console.log(res);
          this.dataSource1 = res.result.records;
        }
      });
    },
    renwudanzong() {
      this.sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      let param = { sys_depart_orgcode: this.sys_depart_orgcode };
      getAction(this.url.renwudanzongs, param).then((res) => {
        if (res.success) {
          this.renwudanhander.renwudanzong = Number(res.result[0].flag);
        }
      });
    },
    renwudanzongbenyue() {
      this.sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      let param = { sys_depart_orgcode: this.sys_depart_orgcode };
      getAction(this.url.renwudanzongs, param).then((res) => {
        if (res.success) {
          this.renwudanhander.renwudanbenyue = Number(res.result[0].flag);
        }
      });
    },
    renwudansh() {
      this.sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      let param = { sys_depart_orgcode: this.sys_depart_orgcode, status: 1 };
      getAction(this.url.renwudansh, param).then((res) => {
        if (res.success) {
          this.renwudanhander.renwudanysh = Number(res.result[0].flag);
        }
      });
    },
    renwudanwsh() {
      this.sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      let param = { sys_depart_orgcode: this.sys_depart_orgcode, status: 0 };
      getAction(this.url.renwudansh, param).then((res) => {
        if (res.success) {
          this.renwudanhander.renwudanweishenhezong = Number(res.result[0].flag);
        }
      });
    },
    renwudanshbenyue() {
      this.sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      let param = { sys_depart_orgcode: this.sys_depart_orgcode, status: 1 };
      getAction(this.url.renwudanshbenyue, param).then((res) => {
        if (res.success) {
          this.renwudanhander.renwudanyshbenyue = Number(res.result[0].flag);
        }
      });
    },
    renwudanwshbenyue() {
      this.sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      let param = { sys_depart_orgcode: this.sys_depart_orgcode, status: 0 };
      getAction(this.url.renwudanshbenyue, param).then((res) => {
        if (res.success) {
          this.renwudanhander.renwudanweishenhebenyue = Number(res.result[0].flag);
        }
      });
    },
    renwudanwancheng() {
      this.sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      let param = { sys_depart_orgcode: this.sys_depart_orgcode, status: 1 };
      getAction(this.url.renwudanzong, param).then((res) => {
        if (res.success) {
          this.renwudanhander.renwudanwc = Number(res.result);
        }
      });
    },
    renwudanwanchengbenyue() {
      this.sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      let param = { sys_depart_orgcode: this.sys_depart_orgcode, status: 1 };
      getAction(this.url.renwudanzongbenyue, param).then((res) => {
        if (res.success) {
          this.renwudanhander.renwudanwcbenyue = Number(res.result);
        }
      });
    },
    changeRegisterType(e) {
      this.indexRegisterType = e.target.value;
      if (this.indexBottomTab == "1") {
        this.loadDataSource1();
      } else {
        this.loadDataSource2();
      }
    },
    tableChange1(pagination) {
      this.ipagination1.current = pagination.current;
      this.ipagination1.pageSize = pagination.pageSize;
      this.queryTimeoutInfo();
    },
    tableChange2(pagination) {
      this.ipagination2.current = pagination.current;
      this.ipagination2.pageSize = pagination.pageSize;
      this.queryNodeTimeoutInfo();
    },
    getFlowRateNumber(value) {
      return Number(value);
    },
    getPercentFormat(value) {
      if (value == 100) {
        return "完成";
      } else {
        return value + "%";
      }
    },
    getPercentColor(value) {
      let p = Number(value);
      if (p >= 90 && p < 100) {
        return "rgb(244, 240, 89)";
      } else if (p >= 100) {
        return "red";
      } else {
        return "rgb(16, 142, 233)";
      }
    },

    getcharts2() {
      var xData = (function () {
        var data = [];
        for (var i = 1; i < 13; i++) {
          data.push(i + "月份");
        }
        return data;
      })();
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
        title: {
          text: "02月23日摊铺温度",
          textStyle: { color: "#000000", fontSize: 20, fontWeight: "normal" },
          x: "center",
          top: "2%",
        },
        grid: {
          borderWidth: 0,
          top: 80,
          bottom: 55,
          textStyle: {
            color: "#fff",
          },
        },
        xAxis: [
          {
            type: "category",
            boundaryGap: false,
            axisLine: {
              lineStyle: {
                color: "#90979c",
              },
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            name: "温度（℃）",
            type: "value",
            // "splitLine": {
            //     "show": false
            // },
            axisLine: {},
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
      let charts = null;
      if (charts) {
        charts.dispose();
      }
      charts = echarts.init(document.getElementById("charts2"));
      charts.setOption(option);
    },
    getcharts3() {
      var xData = (function () {
        var data = [];
        for (var i = 1; i < 13; i++) {
          data.push(i + "月份");
        }
        return data;
      })();
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
        title: {
          text: "02月23日摊铺温度",
          textStyle: { color: "#000000", fontSize: 20, fontWeight: "normal" },
          x: "center",
          top: "2%",
        },
        grid: {
          borderWidth: 0,
          top: 80,
          bottom: 55,
          textStyle: {
            color: "#fff",
          },
        },
        xAxis: [
          {
            type: "category",
            boundaryGap: false,
            axisLine: {
              lineStyle: {
                color: "#90979c",
              },
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            name: "温度（℃）",
            type: "value",
            // "splitLine": {
            //     "show": false
            // },
            axisLine: {},
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
      let charts = null;
      if (charts) {
        charts.dispose();
      }
      charts = echarts.init(document.getElementById("charts3"));
      charts.setOption(option);
    },
    getcharts4() {
      var xData = (function () {
        var data = [];
        for (var i = 1; i < 13; i++) {
          data.push(i + "月份");
        }
        return data;
      })();
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
        title: {
          text: "02月23日摊铺温度",
          textStyle: { color: "#000000", fontSize: 20, fontWeight: "normal" },
          x: "center",
          top: "2%",
        },
        grid: {
          borderWidth: 0,
          top: 80,
          bottom: 55,
          textStyle: {
            color: "#fff",
          },
        },
        xAxis: [
          {
            type: "category",
            boundaryGap: false,
            axisLine: {
              lineStyle: {
                color: "#90979c",
              },
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            name: "温度（℃）",
            type: "value",
            // "splitLine": {
            //     "show": false
            // },
            axisLine: {},
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
      let charts = null;
      if (charts) {
        charts.dispose();
      }
      charts = echarts.init(document.getElementById("charts4"));
      charts.setOption(option);
    },
  },
};
</script>
<style lang="less" scoped>
#homeTempSegr{
  width: 100%;
  height: 400px;
}
#HomePavingSpeed {
  width: 100%;
  height: 400px;
}
#charts2 {
  width: 100%;
  height: 400px;
}
#charts3 {
  width: 100%;
  height: 400px;
}
#charts4 {
  width: 100%;
  height: 400px;
}

.liangzong {
  border: 1px solid #f0f0f0;
  text-align: center;
  height: 180px;
  margin-left: 2px;
  border-radius: 4px;
}

.liangzuo {
  text-align: center;
  color: #1890ff;
}

.liangtwo {
  height: 100%;
  line-height: 40px;
  border-radius: 4px;
  background-color: #f0f0f0;
}

.liangzong1 {
  //border-bottom: 1px dotted grey;
  height: 50%;
  line-height: 40px;
}
.liangone {
  border-bottom: 1px dotted #ffffff;
  height: 100%;
  line-height: 40px;
  background-color: #f0f0f0;
}

.liangceng {
  margin-left: 20%;
  color: #1890ff;
  font-size: 7px;
}

.lianghao {
  height: 60%;
  font-size: 10px;
  line-height: 14px;
  color: #e74c3c;
}

.liangchang {
  height: 40%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #000c17;
}

.extra-wrapper {
  line-height: 55px;
  padding-right: 24px;

  .extra-item {
    display: inline-block;
    margin-right: 24px;

    a {
      margin-left: 24px;
    }
  }
}

.item-group {
  padding: 20px 0 8px 24px;
  font-size: 0;

  a {
    color: rgba(0, 0, 0, 0.65);
    display: inline-block;
    font-size: 14px;
    margin-bottom: 13px;
    width: 25%;
  }
}

.item-group {
  .more-btn {
    margin-bottom: 13px;
    text-align: center;
  }
}

.list-content-item {
  color: rgba(0, 0, 0, 0.45);
  display: inline-block;
  vertical-align: middle;
  font-size: 14px;
  margin-left: 40px;
}

@media only screen and (min-width: 1600px) {
  .list-content-item {
    margin-left: 60px;
  }
}

@media only screen and (max-width: 1300px) {
  .list-content-item {
    margin-left: 20px;
  }

  .width-hidden4 {
    display: none;
  }
}

.list-content-item {
  span {
    line-height: 20px;
  }
}

.list-content-item {
  p {
    margin-top: 4px;
    margin-bottom: 0;
    line-height: 22px;
  }
}

.anty-list-cust {
  .ant-list-item-meta {
    flex: 0.3 !important;
  }
}

.anty-list-cust {
  .ant-list-item-content {
    flex: 1 !important;
    justify-content: flex-start !important;
    margin-left: 20px;
  }
}

.red {
  color: red;
}

.cont-main {
  background-color: #fff;
  // .cont-top {
  //   display: flex;
  //   justify-content: space-between;
  //   // background-color: #bb1616;
  //   .cont-radio {
  //     margin: 10px;
  //     .cont-img {
  //       width: 20px;
  //     }
  //   }
  // }
  // .title {
  //   height: 24px;
  //   opacity: 1;
  //   font-size: 22px;
  //   font-family: Adobe Heiti Std, Adobe Heiti Std-R;
  //   text-align: left;
  //   color: #000000;
  //   padding-left: 20px;
  //   padding-top: 10px;
  //   // position: absolute;
  //   // top: 10px;
  // }
  // .cont {
  //   width: 100%;
  //   display: flex;
  //   flex-wrap: wrap;
  //   justify-content: space-around;
  //   .cont-box {
  //     // background-color: #c9bf64;
  //     width: 40%;
  //     height: 200px;
  //     text-align: center;
  //     .cont-num {
  //       font-size: 60px;
  //       color: #1caacf;
  //     }
  //     .cont-text {
  //       text-align: center;
  //       opacity: 1;
  //       font-size: 16px;
  //       font-family: Adobe Heiti Std, Adobe Heiti Std-R;
  //       color: #000000;
  //       display: inline-block;
  //       width: 100%;
  //       height: 50px;
  //     }
  //     .cont-text:before {
  //       content: "";
  //       width: 6px;
  //       height: 16px;
  //       opacity: 1;
  //       background: #eb833e;
  //       color: #eb833e;
  //       border-radius: 3px;
  //       display: inline-block;
  //       bottom: -2px;
  //       position: relative;
  //     }
  //     .cont-span {
  //       margin-left: 3px;
  //     }
  //   }
  // }
}
</style>
