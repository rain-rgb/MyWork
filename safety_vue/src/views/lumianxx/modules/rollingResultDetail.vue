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
          :pagination="ipagination1"
          rowKey="sid"
          :columns="columns2"
          :data-source="dataSource"
          @change="tableChange"
        >
          <span slot="tags" slot-scope="tags, record">
            <span v-if="record.offset == '0'">左幅</span>
            <span v-if="record.offset == '1'">右幅</span>
          </span>
          <span slot="avgtimes" slot-scope="avgtimes, record">
            <!-- <span>{{ record.rollerList[0].avgtimes }}</span> -->
            {{ record.rollerList | myFilterA }}
          </span>
          <span slot="avgtimes1" slot-scope="avgtimes1, record">
            <!-- <span>{{ record.rollerList[1].avgtimes }}</span> -->
            {{ record.rollerList | myFilterB }}
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
        title="碾压遍数统计"
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
        <a-tab-pane tab="碾压遍数" :key="0">
          <div id="Ecahrt_Tag0" style="height: 500px"></div>
          <div id="Ecahrt_Tag1" style="height: 500px"></div>
        </a-tab-pane>
        <a-tab-pane tab="欠压分布" :key="1">
          <div id="Ecahrt_Tag_Two0" style="height: 500px"></div>
          <div id="Ecahrt_Tag_Two1" style="height: 500px"></div>
        </a-tab-pane>
        <!-- <a-tab-pane tab="摊铺温度总览2" :key="2">
          <div id="Ecahrt_Line2" style="height:500px"></div>
        </a-tab-pane> -->
      </a-tabs>
    </a-row>
  </j-modal>
</template>

<script>
import { getAction } from "@api/manage";
import LineChartsYajiang from "@views/zlyj/modules/LineChartsYajiang";
import * as echarts from "echarts";
import Vue from "vue";

export default {
  name: "rollingResultDetail",
  components: {
    LineChartsYajiang,
  },
  data() {
    return {
      height: 420,
      data: [],
      ipagination: false,
      yajiangrenwudanms: false,
      columns2: [
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
          title: "日期",
          align: "center",
          dataIndex: "date",
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
          title: "钢轮平均碾压遍数",
          align: "center",
          dataIndex: "rollerList",
          scopedSlots: { customRender: "avgtimes" },
        },
        {
          title: "胶轮平均碾压遍数",
          align: "center",
          dataIndex: "rollerList",
          scopedSlots: { customRender: "avgtimes1" },
        },
        // {
        //   title: "速意正常占比",
        //   align: "center",
        //   dataIndex: "kongdaoshu",
        // },
        // {
        //   title: "速意过快占比",
        //   align: "center",
        //   dataIndex: "yajiangfang",
        // },
        // {
        //   title: '完成状态',
        //   align: 'center',
        //   dataIndex: 'status',
        //   scopedSlots: { customRender: 'status' },
        // }
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
          zlsj: 9,
          yajiangji: 7,
          kongdaoshu: "100%",
          yajiangfang: "0%",
        },
      ],
      data2: [],
      data1: [],
      title: "",
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
        list: "/hc_constructionresults/hcConstructionresults/listny",
        lineData: "/hc_constructionresults/hcConstructionresults/listisite", //折线图
        circularData: "/hc_constructionresults/hcConstructionresults/listisitelybs", //环形图
        listdetail: "/yajiangs/trYajiangSS/list",
        listbutton: "/yajiangm/trYajiangM/list2", //压浆任务单下压浆主表信息查询
      },
      holeid: "",
      arrEchart: [
        [
          { value: 123, name: "钢轮遍数正常占比" },
          { value: 102, name: "钢轮遍数过低占比" },
        ],
        [
          { value: 102, name: "胶轮遍数正常占比" },
          { value: 102, name: "胶轮遍数过低占比" },
        ],
        [
          { value: 13, name: "总体遍数正常占比" },
          { value: 102, name: "总体遍数过低占比" },
        ],
      ],
      myChartTag0: null,
      myChartTag1: null,
      myChartTagTwo0: null,
      myChartTagTwo1: null,
      myChart1: null,
      myChart2: null,
      myChart3: null,
      key: 0,
      dataSource: [],
      projectid: "",
      sectionid: "",
      date_begin: "",
      date_end: "",
      lineData: [],
    };
  },
  filters: {
    myFilterA(arr) {
      let arr1 = arr.filter((item) => item.vehicletype === "tyre");
      console.log(arr1, "arr1------------myFilterA");
      return arr1[0].avgtimes;
    },
    myFilterB(arr) {
      let arr1 = arr.filter((item) => item.vehicletype === "steel");
      console.log(arr1, "arr1---------myFilterB");
      return arr1[0].avgtimes;
    },
  },
  mounted() {},
  methods: {
    call(e, arr) {
      this.syjid = e;
      this.visible = true;
      this.getData(this.syjid, arr);
      this.getDataEchart(arr);
      this.getCircularData(arr);
      // this.arrEchart.forEach((e, index) => {
      //   this.$nextTick(() => {
      //     this.getecharts(e, index);
      //     // this.getLineCharts(e, index);
      //   });
      // });
      // this.$nextTick(() => {
      //   this.getecharts();
      // });
    },
    getData(id, arr) {
      let { date, projectid, sectionid, layername, offset } = arr;
      let param = {
        date,
        projectid,
        sectionid,
        layername,
        offset,
        pageSize: this.ipagination1.pageSize,
        pageNo: this.ipagination1.current,
      };
      getAction(this.url.list, param).then((res) => {
        // this.dataSource = res.result.records;
        if (res.success) {
          this.dataSource = res.result;
          console.log(this.dataSource, "详情---------------------------");
          if (res.result.total) {
            this.ipagination1.total = res.result.total;
          } else {
            this.ipagination1.total = 0;
          }
        } else {
          this.dataSource = null;
          this.$message.warning(res.message);
        }
        // this.loading = false;
      });
    },
    getDataEchart(arr) {
      let { date, projectid, sectionid, layername,offset } = arr;
      let param = {
        projectid,
        sectionid,
        offset,
        begintime: date,
        endtime: date,
        // pageSize: this.ipagination1.pageSize,
        // pageNo: this.ipagination1.current,
      };
      getAction(this.url.lineData, param).then((res) => {
        // console.log(res, "lineData-------------------------");
        // this.dataSource = res.result.records;
        if (res.success) {
          this.lineData = res.result;
          this.$nextTick(() => {
            this.testEchart(this.lineData);
          });
          // this.dataSource = res.result.records || res.result;
          // if (res.result.total) {
          //   this.ipagination1.total = res.result.total;
          // } else {
          //   this.ipagination1.total = 0;
          // }
        } else {
          // this.dataSource = null;
          this.$message.warning(res.message);
        }
        // this.loading = false;
      });
    },
    getCircularData(arr) {
      let { date, projectid, sectionid, layername } = arr;
      let param = {
        projectid,
        sectionid,
        begintime: date,
        endtime: date,
        type: 1,
        // pageSize: this.ipagination1.pageSize,
        // pageNo: this.ipagination1.current,
      };
      getAction(this.url.circularData, param).then((res) => {
        // this.dataSource = res.result.records;
        if (res.success) {
          this.arrEchart[0][0].value = res.result.steelNormal;
          this.arrEchart[0][1].value = 1 - res.result.steelNormal;
          this.arrEchart[1][0].value = res.result.rubberNormal;
          this.arrEchart[1][1].value = 1 - res.result.rubberNormal;
          this.arrEchart[2][0].value = res.result.mixNormal;
          this.arrEchart[2][1].value = 1 - res.result.mixNormal;
          this.arrEchart.forEach((e, index) => {
            this.$nextTick(() => {
              this.getecharts(e, index);
            });
          });
        } else {
          // this.dataSource = null;
          this.$message.warning(res.message);
        }
        // this.loading = false;
      });
    },
    tableChange(pagination) {
      this.ipagination1.current = pagination.current;
      this.ipagination1.pageSize = pagination.pageSize;
    },
    callback(key) {
      this.ipagination1.current = 1;
      this.ipagination1.pageSize = 10;
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
    callback1(key) {
      console.log(key);
    },
    callback2(key) {
      console.log(key);
    },
    callmeaages(e) {
      this.uuid = e;
      this.visible = true;
      this.yajiangrenwudanms = true;
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
      console.log("test-------------------------------------------");
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
      // var arr = [
      //   { value: 123, name: "正常" },
      //   { value: 102, name: "过低" },
      // ];
      // setInterval(function () {
      //   for (let index in arr) {
      //     arr[index].value = (Math.random() * 50 + 100).toFixed(0);
      //   }
      //   myChart.setOption({
      //     series: [
      //       {
      //         data: arr,
      //       },
      //     ],
      //   });
      // }, 1000);
      let option = {
        color: colorList,
        // title: {
        //   text: "钢轮速度",
        //   // subtext:'50%',
        //   x: "center",
        //   y: "center",
        //   textStyle: {
        //     // color: "#fff",
        //     fontSize: 15,
        //   },
        // },
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
          // data: ["丙烯腈", "环氧乙烷"],arrLegend
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
            radius: ["20%", "35%"],
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
              formatter: "{a|{b}}",
              rich: {
                hr: {
                  backgroundColor: "t",
                  borderRadius: 3,
                  width: 3,
                  height: 3,
                  // padding: [3, 3, 0, -12],
                },
                a: {
                  padding: [0, -5, 0, 0],
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
      let offset = "";
      let timesFirstLeft = [];
      let timesRepeatLeft = [];
      let timesFirstRight = [];
      let timesRepeatRight = [];

      let steelRateLeft = [];
      let steelRateRight = [];
      let rubberRateLeft = [];
      let rubberRateRight = [];

      if (array[0].offset == "0") {
         offset = "左幅";
         timesFirstLeft = array.map((e) => {
            return e.timesFirst;
        });
         timesRepeatLeft = array.map((e) => {
            return e.timesRepeat;
        });

        steelRateLeft = array.map((e) => {
            return e.steelRate;
        });
        rubberRateLeft = array.map((e) => {
            return e.rubberRate;
        });
      } else {
         offset = "右幅";
         timesFirstRight = array.map((e) => {
            return e.timesFirst;
        });
         timesRepeatRight = array.map((e) => {
            return e.timesRepeat;
        });

        steelRateRight = array.map((e) => {
            return e.steelRate;
        });
        rubberRateRight = array.map((e) => {
            return e.rubberRate;
        });
      }
      let xData = array.map((e) => {
          let station = this.getStartstation(e.station);
          return station;
      });
      const optionTag0 = {
        title: {
          text: "钢轮碾压遍数",
          left: "3%",
          top: "10%",
          textStyle: {
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
          top: "8%",
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
            axisLabel: {
              interval: 0,
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            type: "value",
            axisLine: {
            },
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
            data: timesFirstLeft,
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
                  // position: "inside",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: timesFirstRight,
          },
          {
            name: "平行于y轴的趋势线",
            type: "line",
            markLine: {
              name: "aa",
              data: [
                {
                  name: "合格线:4遍",
                  color: "red",
                  yAxis: 4,
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
      const optionTag1 = {
        title: {
          text: "胶轮碾压遍数",
          left: "3%",
          top: "10%",
          textStyle: {
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
          top: "8%",
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
            axisLabel: {
              interval: 0,
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            type: "value",
            axisLine: {
            },
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
            data: timesRepeatLeft,
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
                  // position: "inside",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: timesRepeatRight,
          },
          {
            name: "平行于y轴的趋势线",
            type: "line",
            markLine: {
              name: "aa",
              data: [
                {
                  name: "合格线:2遍",
                  color: "red",
                  yAxis: 2,
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
      const optionTagTwo0 = {
        title: {
          text: "钢轮欠压率",
          left: "3%",
          top: "10%",
          textStyle: {
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
          top: "8%",
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
            axisLabel: {
              interval: 0,
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            type: "value",
            axisLine: {
            },
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
            data: steelRateLeft,
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
                  // position: "inside",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: steelRateRight,
          },
          {
            name: "平行于y轴的趋势线",
            type: "line",
            markLine: {
              name: "aa",
              data: [
                {
                  name: "合格线:4遍",
                  color: "red",
                  yAxis: 4,
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
      const optionTagTwo1 = {
        title: {
          text: "胶轮欠压率",
          left: "3%",
          top: "10%",
          textStyle: {
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
          top: "8%",
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
            axisLabel: {
              interval: 0,
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            type: "value",
            axisLine: {
            },
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
            data: rubberRateLeft,
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
                  // position: "inside",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: rubberRateRight,
          },
          {
            name: "平行于y轴的趋势线",
            type: "line",
            markLine: {
              name: "aa",
              data: [
                {
                  name: "合格线:2遍",
                  color: "red",
                  yAxis: 2,
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
        this.myChartTag0 = echarts.init(document.getElementById("Ecahrt_Tag0"));
        this.myChartTag1 = echarts.init(document.getElementById("Ecahrt_Tag1"));
        this.myChartTag0.setOption(optionTag0);
        this.myChartTag1.setOption(optionTag1);
        this.myChartTag0.resize();
        this.myChartTag1.resize();
      } else if (this.key === 1) {
        this.myChartTagTwo0 = echarts.init(document.getElementById("Ecahrt_Tag_Two0"));
        this.myChartTagTwo1 = echarts.init(document.getElementById("Ecahrt_Tag_Two1"));
        this.myChartTagTwo0.setOption(optionTagTwo0);
        this.myChartTagTwo1.setOption(optionTagTwo1);
        this.myChartTagTwo0.resize();
        this.myChartTagTwo1.resize();
        console.log(this.key, "this.key--------------");
      }
      //  else if (this.key === 2) {
      //   this.myChart3 = echarts.init(document.getElementById("Ecahrt_Line2"));
      //   this.myChart3.setOption(option3);
      //   this.myChart3.resize();
      //   console.log(this.key,'this.key--------------');
      // }
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
  height: 450px;
  /* background-color: #b88f8f; */
}
#PavingRD_Ecahrt1 {
  width: 50%;
  height: 450px;
  /* background-color: #a2b88f; */
}
#PavingRD_Ecahrt2 {
  width: 50%;
  height: 450px;
  /* background-color: #908fb8; */
}
#Ecahrt_Tag0 {
  /* width: 50%; */
  height: 450px;
  /* background-color: #b88f8f; */
}
#Ecahrt_Tag1 {
  /* width: 50%; */
  height: 450px;
  /* background-color: #b88f8f; */
}
#Ecahrt_Tag_Two0 {
  /* width: 50%; */
  height: 450px;
  /* background-color: #a2b88f; */
}
#Ecahrt_Tag_Two1 {
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
