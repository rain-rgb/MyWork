<template>
  <div id="paperA4">
    <div class="tableHeader">
      <JJHeadReport :formData.sync="model" :dataTar="dataTar[0]"></JJHeadReport>
    </div>
    <div class="tableContent">
      <table class="la-table">
        <tr>
          <td style="width: 61px">单位工程</td>
          <td style="width: 199px">{{ model.danwei }}</td>
          <td style="width: 76px">分项工程</td>
          <td colspan="3" style="width: 149px">
            {{ model.biaoduan }}
          </td>
          <td>施工日期</td>
          <td style="width: 73px">
            {{ model.starttimes }}
          </td>
        </tr>
        <tr>
          <td colspan="1">分部工程</td>
          <td>{{ model.xiangmu }}</td>
          <td>桩号、部位</td>
          <td colspan="5">{{ model.mileage }}</td>
        </tr>
      </table>
      <table class="la-table next">
        <tr>
          <td style="width: 61px">桩号范围</td>
          <td style="width: 198px">{{ getStartstation(model.startstation) + "~" + getStartstation(model.endstation) }}</td>
          <td style="width: 75px">碾压层数</td>
          <td style="width: 46px">{{ model.layername }}</td>
          <td style="width: 65px">施工高程</td>
          <td style="width: 65px">{{ model.avgheights }}</td>
          <td style="width: 65px">设计宽度</td>
          <td style="width: 65px">{{ model.thickavgs }}</td>
        </tr>
      </table>
      <table class="la-table next">
        <tr>
          <td style="width: 50px">桩号</td>
          <td style="width: 70px">平均高程</td>
          <td style="width: 70px">平均厚度</td>
          <td style="width: 70px">碾压速度</td>
          <td style="width: 76px">平均遍数</td>
          <td style="width: 46px">桩号</td>
          <td style="width: 65px">平均高程</td>
          <td style="width: 65px">平均厚度</td>
          <td>碾压速度</td>
          <td>平均遍数</td>
        </tr>
        <tr v-for="item in 5" :key="item">
          <td>
            <span v-if="dataTar[item - 1]">{{ dataTar[item - 1].station }}</span>
          </td>
          <td>
            <span v-if="dataTar[item - 1]">{{ dataTar[item - 1].avgheight }}</span>
          </td>
          <td>
            <span v-if="dataTar[item - 1]">{{ dataTar[item - 1].avgthick }}</span>
          </td>
          <td>
            <span v-if="dataTar[item - 1]">{{ dataTar[item - 1].avgspeed }}</span>
          </td>
          <td>
            <span v-if="dataTar[item - 1]">{{ dataTar[item - 1].avgtimes }}</span>
          </td>
          <td>
            <span v-if="dataTar[item + 4]">{{ dataTar[item + 4].station }}</span>
          </td>
          <td>
            <span v-if="dataTar[item + 4]">{{ dataTar[item + 4].avgheight }}</span>
          </td>
          <td>
            <span v-if="dataTar[item + 4]">{{ dataTar[item + 4].avgthick }}</span>
          </td>
          <td>
            <span v-if="dataTar[item + 4]">{{ dataTar[item + 4].avgspeed }}</span>
          </td>
          <td>
            <span v-if="dataTar[item + 4]">{{ dataTar[item + 4].avgtimes }}</span>
          </td>
        </tr>
      </table>
    </div>
    <div class="tableContent">
      <table class="la-table">
        <tr>
          <td colspan="3">
            <div :id="`echartLine${index}`" style="width: 100%;height: 350px;color: #90979c;"></div>
            <!-- <div id="echartLine1" style="width: 100%;height: 350px;color: #90979c;"></div> -->
          </td>
        </tr>
      </table>
    </div>
    <div class="tableRemark">
      <JJTailReport :formData.sync="reportData1"></JJTailReport>
    </div>
  </div>
</template>
<script>
import * as echarts from "echarts";
import JJHeadReport from "./head/JJHeadReport";
import JJTailReport from "./tail/JJTailReport";
import { httpAction, getAction } from "@/api/manage";
import LineChartsZhangla from "@views/zl/modules/LineChartsZhangla";

export default {
  name: "JJ1412",
  // props: ["dataTar","model"],
  props: {
    dataTar: {  
      type: Array, // 指定 prop 的类型  
    },
    model: {  
      type: Object, // 指定 prop 的类型  
    },
    index: {  
      type: Number, // 指定 prop 的类型  
      default: 1,  // 设置默认值  
    },
  },
  // watch:{
  //   dataTar:{
  //     handler(data){
  //       console.log(data,"data--------");
  //       this.getEcarts(this.dataTar, `echartLine${this.index}`, `高程(m)`, `avgheight`);
  //     },
  //     deep: true, // 开启深度监听
  //   }
  // },
  components: {
    JJHeadReport,
    JJTailReport,
    LineChartsZhangla,
  },
  data() {
    return {
      reportData1: {
        tiTableName: "施工过程记录表",
      },
      // model: {},
    };
  },
  mounted() {
  },
  // computed: {
  //   reportData: {
  //     get: function () {
  //       return this.formData ? this.formData : {};
  //     },
  //     set: function (newVal) {
  //       this.$emit("update:formData", newVal);
  //       return newVal;
  //     },
  //   },
  // },
  methods: {
    getEcarts(eData, DomId, xName, target) {
        // console.log(eData,"eData--------",DomId);
      let that = this;
      let dataList = eData.map((e) => {
        return e[target];
      });
      let xData = eData.map((e) => {
        return e.station;
      });
      // console.log(dataList, xData, `dataList,xData`);
      // var xData = (function () {
      //   var data = [];
      //   for (var i = 1; i < 13; i++) {
      //     data.push(i + "月份");
      //   }
      //   return data;
      // })();
      // let title = `平均值采集完成`;
      let option = {
        title: {
          // text: title,
          x: "4%",
          y: "8%",
          textStyle: {
            color: "#3c40d0",
            fontSize: "22",
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
          top: 40,
          bottom: 70,
          left: "5%",
          right: "7%",
          textStyle: {
            color: "#fff",
          },
        },
        // legend: {
        //   x: "4%",
        //   top: "8%",
        //   textStyle: {
        //     color: "#90979c",
        //   },
        //   // data: ["女", "男", "平均"],
        // },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow", // 默认为直线，可选为：'line' | 'shadow'
          },
          formatter: function (params) {
            // console.log(params, "params-------------");
            let axisValue = that.getStartstation(params[0].axisValue);
            return (
              axisValue +
              "<br/>" +
              `<div  style='width:10px;height:10px;background:#fce630;display:inline-block;border-radius:10px;'></div> ` +
              xName +
              ": " +
              params[0].value
            );
          },
        },
        calculable: true,
        xAxis: [
          {
            type: "category",
            name: "桩号",
            axisLine: {
              lineStyle: {
                color: "#90979c",
              },
            },
            splitLine: {
              // show: false,
            },
            axisTick: {
              show: false,
            },
            splitArea: {
              show: false,
            },
            axisLabel: {
              // interval: 100,
              formatter: function (val) {
                let station = that.getStartstation(`${val}`);
                return station;
              },
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            type: "value",
            name: xName,
            splitLine: {
              // show: false,
            },
            axisLine: {
              lineStyle: {
                color: "#90979c",
              },
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              // interval: 0,
            },
            splitArea: {
              show: false,
            },
          },
        ],
        series: [
          {
            name: xName,
            type: "line",
            symbolSize: 10,
            symbol: "circle",
            itemStyle: {
              normal: {
                color: "rgba(252,230,48,1)",
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
            data: dataList,
          },
        ],
      };
      let charts = echarts.init(document.getElementById(DomId));
      charts.setOption(option);
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
<style lang="less" scoped>
#paperA4 {
  height: 1000px;
  width: 716px;
  background: #fff9ec;
  margin: 0 auto;
  padding: 18px 36px;
  font-family: SimSun;
  font-size: 12px;
  color: #000;
  .tableContent {
    margin-top: -1px;
    .la-table {
      width: 100%;
      margin: 0;
      // border-right: 2px solid #000;
      // border-left: 2px solid #000;
      color: #000;
      // background-color: #fff9ec;
      td {
        padding: 1px;
        height: 26px;
        font-size: 12px;
        line-height: 18px;
        text-align: center;
        border: 1px solid #000;
        position: relative;
      }
    }
    .next {
      margin-top: -1px;
    }
    input {
      width: 100%;
      height: 100%;
      border: none;
      outline: none;
      text-align: center;
    }
    select {
      width: 100%;
      height: 100%;
      text-align-last: center;
      outline: none;
    }
    textarea {
      resize: none;
      border: none;
      width: 100%;
      font-size: 12px;
      color: #000;
      line-height: 16px;
      min-height: 18px;
      padding: 1px;
      text-align: center;
      // outline: none;
      // word-break: break-all;
      // overflow: hidden;
    }
  }
  .tableRemark {
    height: 100px;
  }
}
#echartLine1 {
  width: 100%;
  height: 350px;
  color: #90979c;
}
</style>
