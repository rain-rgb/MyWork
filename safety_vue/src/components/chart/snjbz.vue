<template>
  <div :style="{ padding: '40 0 32px 32px' }">
    <!-- <h4 :style="{ marginBottom: '20px', marginTop: '40px' }">{{ title }}</h4> -->
    <div id="myChart" ref="myChart"></div>
  </div>
</template>

<script>
import { DataSet } from "@antv/data-set";
import { ChartEventMixins } from "./mixins/ChartMixins";
import * as echarts from "echarts";
export default {
  name: "YaLijiLzqx",
  mixins: [ChartEventMixins],
  props: {
    title: {
      type: String,
      default: "",
    },
    dataSource: {
      // type: Array,
      // default: () => [
      //   { type: "Jan", jeecg: 7.0, jeebt: 3.9 },
      //   { type: "Feb", jeecg: 6.9, jeebt: 4.2 },
      //   { type: "Mar", jeecg: 9.5, jeebt: 5.7 },
      //   { type: "Apr", jeecg: 14.5, jeebt: 8.5 },
      //   { type: "May", jeecg: 18.4, jeebt: 11.9 },
      //   { type: "Jun", jeecg: 21.5, jeebt: 15.2 },
      //   { type: "Jul", jeecg: 25.2, jeebt: 17.0 },
      //   { type: "Aug", jeecg: 26.5, jeebt: 16.6 },
      //   { type: "Sep", jeecg: 23.3, jeebt: 14.2 },
      //   { type: "Oct", jeecg: 18.3, jeebt: 10.3 },
      //   { type: "Nov", jeecg: 13.9, jeebt: 6.6 },
      //   { type: "Dec", jeecg: 9.6, jeebt: 4.8 },
      // ],
    },
    fields: {
      type: Array,
      default: () => ["密度"],
    },
    // 别名，需要的格式：[{field:'name',alias:'姓名'}, {field:'sex',alias:'性别'}]
    aliases: {
      type: Array,
      default: () => [{ field: "密度", alias: "密度" }],
    },
    height: {
      type: Number,
      default: 254,
    },
  },
  data() {
    return {
      scale: [
        {
          type: "cat",
          dataKey: "x",
          min: 0,
          max: 1,
        },
      ],
      style: { stroke: "#fff", lineWidth: 1 },
    };
  },
  computed: {
    data() {
      const dv = new DataSet.View().source(this.dataSource);
      dv.transform({
        type: "fold",
        fields: this.fields,
        key: "x",
        value: "y",
      });
      let rows = dv.rows;
      // 替换别名
      rows.forEach((row) => {
        for (let item of this.aliases) {
          if (item.field === row.x) {
            row.x = item.alias;
            break;
          }
        }
      });
      return rows;
    },
  },

  mounted() {
    // 调用绘制图表的方法
    // console.log(this.dataSource, "dataSource ((((((((((((((((((((((");
    this.getcharts();
  },
  methods: {
    getcharts() {
      const that = this;
      let arr1 = [],legendData = [],arrsData = [];
      let {
        dataSource1,
      } = that.dataSource;
      if (dataSource1.length > 0) {
        dataSource1.forEach((e) => {
          if (e.密度) {
            arr1.push(e.密度);
            arrsData.push(e.type)
          }
        });
        legendData.push("密度");
      }
      console.log(arrsData,'arrsData ;;;;;;;;');
      console.log(arr1, "dataSource ((((((((((((((((((((((");
      const option = {
        tooltip: {
          trigger: "axis",
        },
        color: ["red", "skyblue", "orange"],
        legend: {
          // data: ["试件序号1", "试件序号2","试件序号3","试件序号4","试件序号5","试件序号6"],
          data: legendData,
          textStyle: {
            color: "#7BBCEE",
            fontSize: 14,
            fontWeight: "bold",
          },
        },
        grid: {
          left: "7%",
          right: "7%",
          bottom: "3%",
          containLabel: true,
        },
        toolbox: {},
        xAxis: {
          type: "category",
          boundaryGap: false,
          axisLabel: {
            textStyle: {
              color: "#7BBCEE",
            },
          },
          axisTick: {
            lineStyle: {
              color: "#657CA8",
            },
          },
          axisLine: {
            lineStyle: {
              color: "#657CA8",
            },
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: ["#657CA8"],
              width: 1,
              type: "dashed",
            },
          },
          // data: this.total,
          data: arrsData,
        },
        yAxis: {
          type: "value",
          // min: "140",//最小
          axisLabel: {
            // formatter: `{value} C°`,
            // formatter: `{value} 吨`,
            textStyle: {
              color: "#7BBCEE",
            },
          },
          axisTick: {
            lineStyle: {
              color: "#657CA8",
            },
          },
          axisLine: {
            lineStyle: {
              color: "#657CA8",
            },
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: ["#657CA8"],
              width: 1,
              type: "dashed",
            },
          },
        },
        // series: this.eSeries,
        series: [
          {
            name: "密度",
            type: "line",
            // stack: "总量",
            // smooth: true,
            data: arr1,
          },
        ],
      };
      this.charts = echarts.init(document.getElementById("myChart"));
      this.charts.setOption(option);
    },
  },
};
</script>

<style scoped>
#myChart {
  padding: 10px;
  padding-top: 20px;
  width: 100%;
  height: 260px;
}
</style>
