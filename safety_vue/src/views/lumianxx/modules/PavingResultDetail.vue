<template>
  <j-modal
    :title="title"
    :width="1600"
    :visible="visible"
    :maskClosable="false"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-tabs default-active-key="1" @change="callback2">
      <a-tab-pane key="1" tab="">
        <a-table
          :pagination="ipagination"
          rowKey="sid"
          :columns="columns"
          :data-source="dataSource"
          @change="tableChange"
        >
          <span slot="tags" slot-scope="tags, record">
            <span v-if="record.offset == '0'">左幅</span>
            <span v-if="record.offset == '1'">右幅</span>
          </span>
          <span slot="avgtempTags" slot-scope="avgtempTags, record">
            <span>{{ Number(record.paver[0].avgtemp).toFixed(3) }}</span>
          </span>
          <span slot="avgtempdiffTags" slot-scope="avgtempdiffTags, record">
            <span>{{ Number(record.paver[0].avgtempdiff).toFixed(3) }}</span>
          </span>
          <span slot="velocityratioTags" slot-scope="velocityratioTags, record">
            <span>{{ record.paver[0].velocityratio }}</span>
          </span>
        </a-table>
      </a-tab-pane>
    </a-tabs>
    <!-- <a-tabs default-active-key="1" @change="callback">
      <a-tab-pane v-for="arry in data" :key="arry.holeid" :tab="arry.kongdao">
        <LineChartsYajiang
          title="压浆过程图"
          :data-source="datadetail"
        ></LineChartsYajiang>
      </a-tab-pane>
    </a-tabs> -->
    <a-row>
      <a-card
        title=""
        :bordered="false"
        :headStyle="{ color: '#0785fd' }"
        :bodyStyle="{ padding: '10' }"
      >
        <div>
          <!-- <LineChartMultid :data-source="dataSource2" /> -->
        </div>
        <div class="charts-box">
          <div id="PavingRD_Ecahrt0"></div>
          <div id="PavingRD_Ecahrt1"></div>
          <div id="PavingRD_Ecahrt2"></div>
        </div>
      </a-card>
    </a-row>
    <a-row>
      <!-- <a-tabs default-active-key="1" @change="callback2">
        <a-tab-pane key="1" tab="">
          <a-table
            :pagination="ipagination1"
            rowKey="sid"
            :columns="columns2"
            :data-source="testData"
            @change="tableChange"
          >
          </a-table>
        </a-tab-pane>
      </a-tabs> -->
      <a-tabs :default-active-key="key" @change="callbackLine">
        <a-tab-pane tab="摊铺温度总览" :key="0">
          <div id="Ecahrt_Line0" style="height: 500px"></div>
        </a-tab-pane>
        <a-tab-pane tab="摊铺速度总览" :key="1">
          <div id="Ecahrt_Line1" style="height: 500px"></div>
        </a-tab-pane>
        <a-tab-pane tab="温度离析总览" :key="2">
          <div id="Ecahrt_Line2" style="height: 500px"></div>
        </a-tab-pane>
        <a-tab-pane tab="摊铺车辆" :key="3">
          <a-table
            :pagination="ipagination1"
            rowKey="sid"
            :columns="columns1"
            :data-source="carData"
            @change="tableChange1"
          >
            <span slot="mmit" slot-scope="mmit, record">
              <span>{{ record.hcTruck.mmit }}</span>
            </span>
            <span slot="rfid" slot-scope="rfid, record">
              <span>{{ record.hcTruck.rfid }}</span>
            </span>
            <span slot="licenseplate" slot-scope="licenseplate, record">
              <span>{{ record.hcTruck.licenseplate }}</span>
            </span>
            <span slot="drivername" slot-scope="drivername, record">
              <span>{{ record.hcTruck.drivername }}</span>
            </span>
            <span slot="driverphone" slot-scope="driverphone, record">
              <span>{{ record.hcTruck.driverphone }}</span>
            </span>
            <span slot="trucktype" slot-scope="trucktype, record">
              <span v-if="record.hcTruck.trucktype == 51">混合料运输车</span>
              <span v-if="record.hcTruck.trucktype == 52">沥青运输车</span>
            </span>
          </a-table>
        </a-tab-pane>
      </a-tabs>
    </a-row>
  </j-modal>
</template>

<script>
import { getAction } from "@api/manage";
import LineChartsYajiang from "@views/zlyj/modules/LineChartsYajiang";
import * as echarts from "echarts";
import Vue from "vue";
// import { JeecgListMixin } from "@/mixins/JeecgListMixin";

export default {
  name: "PavingResultDetail",
  components: {
    LineChartsYajiang,
  },
  // mixins: [JeecgListMixin],
  data() {
    return {
      height: 420,
      data: [],
      ipagination: false,
      yajiangrenwudanms: false,
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
          title: "摊铺温度(℃)",
          align: "center",
          dataIndex: "paver",
          scopedSlots: { customRender: "avgtempTags" },
        },
        {
          title: "温度离析(℃)",
          align: "center",
          dataIndex: "paver",
          scopedSlots: { customRender: "avgtempdiffTags" },
        },
        {
          title: "速度正常占比",
          align: "center",
          dataIndex: "paver",
          scopedSlots: { customRender: "velocityratioTags" },
        },
        // {
        //   title: "速度过快占比",
        //   align: "center",
        //   dataIndex: "paver",
        //   scopedSlots: { customRender: "speed1" },
        // },
        // {
        //   title: '完成状态',
        //   align: 'center',
        //   dataIndex: 'status',
        //   scopedSlots: { customRender: 'status' },
        // }
      ],
      columns1: [
        {
          title: "摊铺id",
          align: "center",
          dataIndex: "id",
        },
        {
          title: "拌合站",
          align: "center",
          dataIndex: "stationname",
        },
        {
          title: "出场时间",
          align: "center",
          dataIndex: "outstationtime",
        },
        {
          title: "运输时长",
          align: "center",
          dataIndex: "transporttime",
        },
        {
          title: "MMIT编号",
          align: "center",
          dataIndex: "hcTruck",
          scopedSlots: { customRender: "mmit" },
        },
        {
          title: "RFID号",
          align: "center",
          dataIndex: "hcTruck",
          scopedSlots: { customRender: "rfid" },
        },
        {
          title: "车牌号",
          align: "center",
          dataIndex: "hcTruck",
          scopedSlots: { customRender: "licenseplate" },
        },
        {
          title: "司机名称",
          align: "center",
          dataIndex: "hcTruck",
          scopedSlots: { customRender: "drivername" },
        },
        {
          title: "司机电话",
          align: "center",
          dataIndex: "hcTruck",
          scopedSlots: { customRender: "driverphone" },
        },
        {
          title: "运输车类型",
          align: "center",
          dataIndex: "hcTruck",
          scopedSlots: { customRender: "trucktype" },
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
          yajiangji: 42,
          kongdaoshu: "100%",
          yajiangfang: "0%",
        },
      ],
      data2: [],
      data1: [],
      title: "",
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ["10", "20", "30"],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      ipagination1: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ["10", "20", "30"],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      width: 800,
      visible: false,
      disableSubmit: false,
      syjid: "",
      uuid: "",
      datadetail: [],
      url: {
        list: "/hc_constructionresults/hcConstructionresults/listtp",
        lineData: "/hc_constructionresults/hcConstructionresults/listisite", //折线图
        circularData: "/hc_constructionresults/hcConstructionresults/listisitewdhgl", //环形图
        carData: "/hc_transportrecords/hcTransportrecords/listysc", //摊铺车辆
        listdetail: "/yajiangs/trYajiangSS/list",
        listbutton: "/yajiangm/trYajiangM/list2", //压浆任务单下压浆主表信息查询
      },
      holeid: "",
      arrEchart: [
        [
          { value: 0, name: "正常" },
          { value: 0, name: "过低" },
        ],
        [
          { value: 0, name: "正常" },
          { value: 0, name: "过快" },
        ],
        // [
        //   { value: 13, name: "正常" },
        //   { value: 102, name: "过快" },
        // ],
      ],
      myChart1: null,
      myChart2: null,
      myChart3: null,
      key: 0,
      projectid: "",
      sectionid: "",
      date_begin: "",
      date_end: "",
      dataSource: [],
      lineData: [],
      circularData: [],
      carData: [],
    };
  },
  mounted() {},
  methods: {
    call(e, arr) {
      this.syjid = e;
      this.visible = true;
      this.getData(this.syjid, arr);
      this.getDataEchart(arr);
      this.getCircularData(arr);
      this.getCircularData1(arr);
      this.getCarData(arr);
      // this.yajiangmessage();

      // this.$nextTick(() => {
      //   this.getecharts();
      // });
    },
    getData(id, arr) {
      // this.loading = true;
      // let sectionid = arr.sectionid;
      // let layername = arr.layername;
      let { date, projectid, sectionid, layername, offset } = arr;
      let param = {
        date,
        projectid,
        sectionid,
        layername,
        offset,
        pageSize: this.ipagination.pageSize,
        pageNo: this.ipagination.current,
      };
      getAction(this.url.list, param).then((res) => {
        // this.dataSource = res.result.records;
        if (res.success) {
          this.dataSource = res.result;
          // this.dataSource[0].avgtemp = Number(res.result.paver[0].avgtemp).toFixed(2);
          // this.dataSource[0].avgtempdiff = Number(
          //   res.result.paver[0].avgtempdiff
          // ).toFixed(2);
          // this.dataSource[0].velocityratio = res.result.paver[0].velocityratio;
          // console.log(
          //   this.dataSource,
          //   "详情---------------------------"
          //   // res.result.paver[0].avgtemp
          // );
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
    getCarData(arr) {
      let { date, projectid, sectionid, offset } = arr;
      let param = {
        date: date,
        projectid,
        sectionid,
        // offset,
      };
      getAction(this.url.carData, param).then((res) => {
        if (res.success) {
          this.carData = res.result;
          if (res.result.total) {
            this.ipagination1.total = res.result.total;
          } else {
            this.ipagination1.total = 0;
          }
        } else {
          this.$message.warning(res.message);
        }
      });
    },
    getDataEchart(arr) {
      // this.loading = true;
      // let username = this.$route.query.username;
      let { date, projectid, sectionid, layername, offset } = arr;
      let param = {
        projectid,
        sectionid,
        offset,
        begintime: date,
        endtime: date,
      };
      getAction(this.url.lineData, param).then((res) => {
        if (res.success) {
          this.lineData = res.result;
          this.$nextTick(() => {
            this.testEchart(this.lineData);
          });
        } else {
          this.$message.warning(res.message);
        }
      });
    },
    getCircularData(arr) {
      let { date, projectid, sectionid, layername } = arr;
      let param = {
        projectid,
        sectionid,
        begintime: date,
        endtime: date,
        type: 1, //1摊铺温度合格率/2摊铺速度合格率
      };
      getAction(this.url.circularData, param).then((res) => {
        if (res.success) {
          this.arrEchart[0][0].value = res.result.normal;
          this.arrEchart[0][1].value = 1 - res.result.normal;
          this.getecharts(this.arrEchart[0], 0);
        } else {
          this.$message.warning(res.message);
        }
      });
    },
    getCircularData1(arr) {
      let { date, projectid, sectionid, layername } = arr;
      let param = {
        projectid,
        sectionid,
        begintime: date,
        endtime: date,
        type: 2, //1摊铺温度合格率/2摊铺速度合格率
      };
      getAction(this.url.circularData, param).then((res) => {
        if (res.success) {
          this.arrEchart[1][0].value = res.result.normal;
          this.arrEchart[1][1].value = 1 - res.result.normal;
          this.getecharts(this.arrEchart[1], 1);
        } else {
          this.$message.warning(res.message);
        }
      });
    },
    tableChange(pagination) {
      this.ipagination.current = pagination.current;
      this.ipagination.pageSize = pagination.pageSize;
      // this.yajiangmessagedetail(this.holeid);
    },
    tableChange1(pagination) {
      this.ipagination1.current = pagination.current;
      this.ipagination1.pageSize = pagination.pageSize;
      // this.yajiangmessagedetail(this.holeid);
    },
    callback(key) {
      this.ipagination1.current = 1;
      this.ipagination1.pageSize = 10;
      // this.yajiangmessagedetail(key);
      console.log(key);
    },
    callbackLine(key) {
      this.key = key;
      // if (key === "0") {
      //   this.myChart1.resize();
      // } else if (key === "1") {
      //   this.myChart2.resize();
      // } else if (key === "2") {
      //   this.myChart3.resize();
      // }
      this.$nextTick(() => {
        this.testEchart(this.lineData);
      });
    },
    callback2(key) {
      console.log(key);
    },
    close() {
      this.visible = false;
    },
    handleOk() {
      this.visible = false;
    },
    handleCancel() {
      this.visible = false;
    },
    getecharts(e, index) {
      console.log(e, "test-------------------------------------------");
      let that = this;
      var colorList = [
        // "green",
        // "red",
        "#73DDFF",
        "#FD866A",
        "#73ACFF",
        "#FDD56A",
        "#FDB36A",
        "#9E87FF",
        "#58D5FF",
      ];
      let arr = e;
      let arrLegend = arr.map((e) => {
        return e.name;
      });
      let option = {
        color: colorList,
        grid: {
          left: "43%",
          right: "4%",
          bottom: "5%",
          top: "23%",
          // containLabel: true,
        },
        legend: {
          orient: "vertical",
          left: "left",
          right: 60,
          data: arrLegend,
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
            center: ["50%", "50%"],
            radius: ["40%", "65%"],
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
                  // padding: [3, 3, 0, -12],
                },
                a: {
                  padding: [0, 15, -20, 15],
                },
              },
            },
            labelLine: {
              normal: {
                // length: 20,
                // length2: 30,
                lineStyle: {
                  width: 1,
                },
              },
            },
            data: arr,
          },
        ],
      };
      let chart = echarts.init(document.getElementById(`PavingRD_Ecahrt${index}`));
      chart.setOption(option);
      //建议加上以下这一行代码，不加的效果图如下（当浏览器窗口缩小的时候）。超过了div的界限（红色边框）
      window.addEventListener("resize", function () {
        chart.resize();
      });
    },
    testEchart(array) {
      // 基于准备好的dom，初始化echarts实例
      let offset = "";
      let TemperatureLeft = [];
      let pavingSpeedLeft = [];
      let tempSeLeft = [];
      let TemperatureRight = [];
      let pavingSpeedRight = [];
      let tempSeRight = [];
      if (array[0].offset == "0") {
         offset = "左幅";
         TemperatureLeft = array.map((e) => {
          return e.pavingTemperature;
        });
         pavingSpeedLeft = array.map((e) => {
          return e.pavingSpeed;
        });
         tempSeLeft = array.map((e) => {
          return e.temperatureSegregation;
        });
      } else {
         offset = "右幅";
         TemperatureRight = array.map((e) => {
          return e.pavingTemperature;
        });
         pavingSpeedRight = array.map((e) => {
          return e.pavingSpeed;
        });
         tempSeRight = array.map((e) => {
          return e.temperatureSegregation;
        });
      }
      // 指定图表的配置项和数据
      let xData = array.map((e) => {
        let station = this.getStartstation(e.station);
        return station;
      });
      const option1 = {
        // backgroundColor: "#344b58",
        title: {
          text: "温度℃",
          // "subtext": "BY Wang Dingding",
          x: "8%",
          top: "10%",
          textStyle: {
            // color: '#fff',
            fontSize: "16",
          },
          subtextStyle: {
            color: "#90979c",
            fontSize: "16",
          },
        },
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
          data: [offset],
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
            start: 0,
            end: 100,
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
            name: "左幅",
            type: "line",
            barMaxWidth: 35,
            itemStyle: {
              normal: {
                color: "rgba(255,144,128,1)",
                label: {
                  show: true,
                  textStyle: {
                    color: "rgba(255,144,128,1)",
                  },
                  // position: "inside",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: TemperatureLeft,
          },
          {
            name: "右幅",
            type: "line",
            barMaxWidth: 35,
            itemStyle: {
              normal: {
                color: "rgba(0,191,183,1)",
                barBorderRadius: 0,
                label: {
                  show: true,
                  textStyle: {
                    color: "rgba(0,191,183,1)",
                  },
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            // data: [327, 1776, 507, 1200, 800, 482, 204, 1390, 1001, 951, 381, 220],
            data: TemperatureRight,
          },
          {
            name: "平行于y轴的趋势线",
            type: "line",
            markLine: {
              name: "aa",
              data: [
                {
                  name: "合格线:120℃",
                  color: "red",
                  yAxis: 120,
                  lineStyle: {
                    //设置折线色颜色
                    color: "red",
                  },
                },
              ],
              symbol: ["arrow", "arrow"], //将箭头向左 默认值是向右的
              label: {
                color: "red",
                show: true,
                position: "middle", //markline描述位于中间 right，left，middle
                formatter: "{b}", //显示name中的描述
              },
            },
          },
        ],
      };
      const option2 = {
        // backgroundColor: "#344b58",
        title: {
          text: "速度(m/min)",
          // "subtext": "BY Wang Dingding",
          x: "8%",
          top: "10%",
          textStyle: {
            // color: '#fff',
            fontSize: "16",
          },
          subtextStyle: {
            color: "#90979c",
            fontSize: "16",
          },
        },
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
          data: [offset],
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
            start: 0,
            end: 100,
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
            name: "左幅",
            type: "line",
            barMaxWidth: 35,
            itemStyle: {
              normal: {
                color: "rgba(255,144,128,1)",
                label: {
                  show: true,
                  textStyle: {
                    color: "rgba(255,144,128,1)",
                  },
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: pavingSpeedLeft,
          },
          {
            name: "右幅",
            type: "line",
            barMaxWidth: 35,
            itemStyle: {
              normal: {
                color: "rgba(0,191,183,1)",
                barBorderRadius: 0,
                label: {
                  show: true,
                  textStyle: {
                    color: "rgba(0,191,183,1)",
                  },
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: pavingSpeedRight,
          },
          {
            name: "平行于y轴的趋势线",
            type: "line",
            markLine: {
              name: "aa",
              data: [
                {
                  name: "合格线:120℃",
                  color: "red",
                  yAxis: 120,
                  lineStyle: {
                    //设置折线色颜色
                    color: "red",
                  },
                },
              ],
              symbol: ["arrow", "arrow"], //将箭头向左 默认值是向右的
              label: {
                color: "red",
                show: true,
                position: "middle", //markline描述位于中间 right，left，middle
                formatter: "{b}", //显示name中的描述
              },
            },
          },
        ],
      };
      const option3 = {
        // backgroundColor: "#344b58",
        title: {
          text: "温度离析(°C)",
          // "subtext": "BY Wang Dingding",
          x: "8%",
          top: "10%",
          textStyle: {
            // color: '#fff',
            fontSize: "16",
          },
          subtextStyle: {
            color: "#90979c",
            fontSize: "16",
          },
        },
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
          data: [offset],
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
            start: 0,
            end: 100,
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
            name: "左幅",
            type: "line",
            barMaxWidth: 35,
            itemStyle: {
              normal: {
                color: "rgba(255,144,128,1)",
                label: {
                  show: true,
                  textStyle: {
                    color: "rgba(255,144,128,1)",
                  },
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: tempSeLeft,
          },
          {
            name: "右幅",
            type: "line",
            barMaxWidth: 35,
            itemStyle: {
              normal: {
                color: "rgba(0,191,183,1)",
                barBorderRadius: 0,
                label: {
                  show: true,
                  textStyle: {
                    color: "rgba(0,191,183,1)",
                  },
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: tempSeRight,
          },

          {
            name: "平行于y轴的趋势线",
            type: "line",
            markLine: {
              name: "aa",
              data: [
                {
                  name: "合格线:10℃",
                  color: "red",
                  yAxis: 10,
                  lineStyle: {
                    //设置折线色颜色
                    color: "red",
                  },
                },
              ],
              symbol: ["arrow", "arrow"], //将箭头向左 默认值是向右的
              label: {
                color: "red",
                show: true,
                position: "middle", //markline描述位于中间 right，left，middle
                formatter: "{b}", //显示name中的描述
              },
            },
          },
        ],
      };

      // 使用刚指定的配置项和数据显示图表。
      console.log(this.key, "this.key--------------");
      if (this.key === 0) {
        this.myChart1 = echarts.init(document.getElementById("Ecahrt_Line0"));
        this.myChart1.setOption(option1);
        this.myChart1.resize();
      } else if (this.key === 1) {
        this.myChart2 = echarts.init(document.getElementById("Ecahrt_Line1"));
        this.myChart2.setOption(option2);
        console.log(this.key, "this.key--------------");
        this.myChart2.resize();
      } else if (this.key === 2) {
        this.myChart3 = echarts.init(document.getElementById("Ecahrt_Line2"));
        this.myChart3.setOption(option3);
        this.myChart3.resize();
        console.log(this.key, "this.key--------------");
      }
      // 监听标签页的切换事件，根据当前激活的标签页显示相应的图表
      // this.$refs.tabs.$on("change", (activeKey) => {
      //   if (activeKey === "1") {
      //     this.myChart1.resize();
      //   } else if (activeKey === "2") {
      //     this.myChart2.resize();
      //   } else if (activeKey === "3") {
      //     this.myChart3.resize();
      //   }
      // });
    },
    getStartstation(startstation) {
      let startsta = null;
      if (startstation != null) {
        if (startstation.length == 2) {
          startsta = "K0+0" + startstation;
        } else if (startstation.length == 3) {
          startsta = "K0+" + startstation;
        } else if (startstation.length > 3) {
          let substring = startstation.substring(0, startstation.length - 3);
          let substring1 = startstation.substring(startstation.length - 3);
          startsta = "K" + substring + "+" + substring1;
        }
      }
      return startsta;
    },
    // getLineCharts(e, index) {
    //   //对数据进行处理
    //   // let data = this.qdbzc;
    //   // let { xAxis, seriesData } = this.processData(data);
    //   // console.log(xAxis, seriesData, 'xAxis1, seriesData1xAxis1, seriesData1xAxis1, seriesData1xAxis1, seriesData1');
    //   var xData = (function () {
    //     var data = [];
    //     for (var i = 1; i < 13; i++) {
    //       data.push(i + "月份");
    //     }
    //     return data;
    //   })();

    //   const option = {
    //     // backgroundColor: "#344b58",
    //     title: {
    //       text: "合格率",
    //       // "subtext": "BY Wang Dingding",
    //       x: "8%",
    //       top: "10%",
    //       textStyle: {
    //         // color: '#fff',
    //         fontSize: "16",
    //       },
    //       subtextStyle: {
    //         color: "#90979c",
    //         fontSize: "16",
    //       },
    //     },
    //     tooltip: {
    //       trigger: "axis",
    //       axisPointer: {
    //         type: "shadow",
    //         textStyle: {
    //           color: "#fff",
    //         },
    //       },
    //     },
    //     grid: {
    //       borderWidth: 0,
    //       top: 110,
    //       bottom: 95,
    //       textStyle: {
    //         color: "#fff",
    //       },
    //     },
    //     legend: {
    //       // x: '4%',
    //       // top: '8%',
    //       textStyle: {
    //         color: "#90979c",
    //       },
    //       data: ["左幅", "右幅"],
    //     },

    //     calculable: true,
    //     xAxis: [
    //       {
    //         type: "category",
    //         axisLine: {
    //           lineStyle: {
    //             color: "#90979c",
    //           },
    //         },
    //         // "splitLine": {
    //         //     "show": false
    //         // },
    //         // "axisTick": {
    //         //     "show": false
    //         // },
    //         // "splitArea": {
    //         //     "show": false
    //         // },
    //         axisLabel: {
    //           interval: 0,
    //         },
    //         // data: xData,
    //       },
    //     ],
    //     yAxis: [
    //       {
    //         type: "value",
    //         // "splitLine": {
    //         //     "show": false
    //         // },
    //         axisLine: {
    //           // lineStyle: {
    //           //     color: '#90979c'
    //           // }
    //         },
    //         // "axisTick": {
    //         //     "show": false
    //         // },
    //         // "axisLabel": {
    //         //     "interval": 0,

    //         // },
    //         // "splitArea": {
    //         //     "show": false
    //         // },
    //       },
    //     ],
    //     dataZoom: [
    //       {
    //         show: true,
    //         height: 30,
    //         xAxisIndex: [0],
    //         bottom: 30,
    //         start: 0,
    //         end: 100,
    //         handleIcon:
    //           "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
    //         handleSize: "110%",
    //         handleStyle: {
    //           color: "#d3dee5",
    //         },
    //         textStyle: {
    //           color: "#fff",
    //         },
    //         borderColor: "#90979c",
    //       },
    //       {
    //         type: "inside",
    //         show: true,
    //         height: 15,
    //         start: 1,
    //         end: 35,
    //       },
    //     ],
    //     series: [
    //       {
    //         name: "左幅",
    //         type: "line",
    //         barMaxWidth: 35,
    //         itemStyle: {
    //           normal: {
    //             color: "rgba(255,144,128,1)",
    //             label: {
    //               show: true,
    //               // textStyle: {
    //               //   color: "#fff",
    //               // },
    //               position: "inside",
    //               formatter: function (p) {
    //                 return p.value > 0 ? p.value : "";
    //               },
    //             },
    //           },
    //         },
    //         data: [709, 1917, 2455, 2610, 1719, 1433, 1544, 3285, 5208, 3372, 2484, 4078],
    //       },

    //       {
    //         name: "右幅",
    //         type: "line",
    //         barMaxWidth: 35,
    //         itemStyle: {
    //           normal: {
    //             color: "rgba(0,191,183,1)",
    //             barBorderRadius: 0,
    //             label: {
    //               show: true,
    //               position: "inside",
    //               formatter: function (p) {
    //                 return p.value > 0 ? p.value : "";
    //               },
    //             },
    //           },
    //         },
    //         data: [327, 1776, 507, 1200, 800, 482, 204, 1390, 1001, 951, 381, 220],
    //       },
    //     ],
    //   };
    //   if (this.dailyCharts) {
    //     this.dailyCharts.dispose();
    //   }
    //   this.chartLine = echarts.init(document.getElementById(`Ecahrt_Line${index}`));
    //   chart.setOption(option);
    // },
  },
};
</script>
<style scoped>
.charts-box {
  display: flex;
  justify-content: space-around;
  /* background-color: #aa6c6c; */
}
#PavingRD_Ecahrt0 {
  width: 50%;
  height: 250px;
  /* background-color: #b88f8f; */
}
#PavingRD_Ecahrt1 {
  width: 50%;
  height: 250px;
  /* background-color: #a2b88f; */
}
/* #PavingRD_Ecahrt2 {
  width: 50%;
  height: 250px;
} */
#Ecahrt_Line0 {
  /* width: 50%; */
  height: 450px;
  /* background-color: #b88f8f; */
}
#Ecahrt_Line1 {
  /* width: 50%; */
  height: 450px;
  /* background-color: #a2b88f; */
}
#Ecahrt_Line2 {
  /* width: 50%; */
  height: 450px;
  /* background-color: #908fb8; */
}
</style>
