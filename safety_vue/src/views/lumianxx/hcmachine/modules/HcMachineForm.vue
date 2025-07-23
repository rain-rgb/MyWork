<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container>
      <div id="Tempcharts"></div>
    </j-form-container>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from "@/api/manage";
import { validateDuplicateValue } from "@/utils/util";
import * as echarts from "echarts";

export default {
  name: "HcMachineForm",
  components: {},
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: "/hc_machine/hcMachine/add",
        edit: "/hc_machine/hcMachine/edit",
        queryById: "/hc_machine/hcMachine/queryById",
      },
    };
  },
  computed: {
    formDisabled() {
      return this.disabled;
    },
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  methods: {
    add() {
      this.edit(this.modelDefault);
    },
    edit(record) {
      this.model = Object.assign({}, record);
      this.visible = true;
      console.log(record, "11111111111");
      this.clearCache(record);
    },
    submitForm() {
      const that = this;
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
          that.confirmLoading = true;
          let httpurl = "";
          let method = "";
          if (!this.model.id) {
            httpurl += this.url.add;
            method = "post";
          } else {
            httpurl += this.url.edit;
            method = "put";
          }
          httpAction(httpurl, this.model, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit("ok");
              } else {
                that.$message.warning(res.message);
              }
            })
            .finally(() => {
              that.confirmLoading = false;
            });
        }
      });
    },
    clearCache(record) {
      let params = {
        machineId: record.machineid,
        // gpsTime: `2023-08-10 07:17:57`,
        pageNo: 1,
        pageSize: 100,
      };
      getAction("openapigpsdatavo/openapigpsdatavo/listqx", params)
        .then((res) => {
          if (res.code == 200) {
            console.log(res, "res");
            let arr = res.result.records;
            this.getcharts(arr);
          } else {
            this.$message.warn(res.message);
          }
          // let arr = [
          //   {
          //     sjid: 2380539,
          //     machineId: "2584",
          //     gpsTime: "2023-08-10 07:09:12",
          //     qualityIndex: "4",
          //     north: "3405698.79413782",
          //     east: "495561.281787098",
          //     lon: "120.453633649",
          //     lat: "30.7720121895",
          //     temperature: "",
          //     roadStation: "923.754588445802",
          //     offset: "4.44468456263362", //左右幅，0为左幅，1为右幅
          //     velocity: "44.880094103296564",
          //     tempDiff: "0.0",
          //     geoh: "14.861",
          //     cmv: "0.52",
          //     frequency: "30.0", //振动频率（huizhi）
          //     rfid: "",
          //     layerIndex: "100",
          //     id: "1691596800469", //记录id
          //     isdj: 0,
          //   },
          //   {
          //     sjid: 2380540,
          //     machineId: "2584",
          //     gpsTime: "2023-08-10 07:09:13",
          //     qualityIndex: "4",
          //     north: "3405698.5585888",
          //     east: "495562.018321494",
          //     lon: "120.453641343833",
          //     lat: "30.7720100675",
          //     temperature: "",
          //     roadStation: "924.54032172441",
          //     offset: "4.48077145870538", //左右幅，0为左幅，1为右幅
          //     velocity: "44.90922774374496",
          //     tempDiff: "0.0",
          //     geoh: "14.8757",
          //     cmv: "0.39",
          //     frequency: "30.0", //振动频率（huizhi）
          //     rfid: "",
          //     layerIndex: "100",
          //     id: "1691596800470", //记录id
          //     isdj: 0,
          //   },
          //   {
          //     sjid: 2380541,
          //     machineId: "2584",
          //     gpsTime: "2023-08-10 07:09:14",
          //     qualityIndex: "4",
          //     north: "3405698.32412416",
          //     east: "495562.723153687",
          //     lon: "120.4536487075",
          //     lat: "30.7720079551667",
          //     temperature: "",
          //     roadStation: "925.295398925585",
          //     offset: "4.52183898489456", //左右幅，0为左幅，1为右幅
          //     velocity: "44.9426725140936",
          //     tempDiff: "0.0",
          //     geoh: "14.8895",
          //     cmv: "0.37",
          //     frequency: "30.0", //振动频率（huizhi）
          //     rfid: "",
          //     layerIndex: "100",
          //     id: "1691596800471", //记录id
          //     isdj: 0,
          //   },
          //   {
          //     sjid: 2380542,
          //     machineId: "2584",
          //     gpsTime: "2023-08-10 07:09:15",
          //     qualityIndex: "4",
          //     north: "3405698.06930791",
          //     east: "495563.447857786",
          //     lon: "120.453656278833",
          //     lat: "30.7720056593333",
          //     temperature: "",
          //     roadStation: "926.076311526042",
          //     offset: "4.57495648263673", //左右幅，0为左幅，1为右幅
          //     velocity: "44.98485342147756",
          //     tempDiff: "0.0",
          //     geoh: "14.9066",
          //     cmv: "0.32",
          //     frequency: "30.0", //振动频率（huizhi）
          //     rfid: "",
          //     layerIndex: "100",
          //     id: "1691596800472", //记录id
          //     isdj: 0,
          //   },
          // ];
        })
        .catch((e) => {
          this.$message.warn("数据获取失败");
        });
    },
    getcharts(arr) {
      if (arr.length == 0) {
        this.$message.warn("暂无数据");
        return;
      }
      //对数据进行处理
      let data = arr;
      let xAxis = data.map((e) => {
        return e.gpsTime;
      });
      let seriesData = data.map((e) => {
        return e.frequency;
      });
      // let { xAxis, seriesData } = this.processData(data);
      // console.log(xAxis, seriesData, 'xAxis1, seriesData1xAxis1, seriesData1xAxis1, seriesData1xAxis1, seriesData1');
      const option = {
        legend: {
          type: "scroll",
          // data: name,
          data: ["振动频率"],
          right: "5%",
          // pageIconColor: "white",
          pageIconInactiveColor: "#2f4554",
          textStyle: {
            // color: "#fff",
          },
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // type: "cross",
            label: {
              backgroundColor: "#6a7985",
            },
          },
        },
        dataZoom: [
          {
            show: true,
            height: 15,
            // xAxisIndex: [0],
            bottom: 10,
            start: 0,
            end: 100,
            handleIcon:
              "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
            handleSize: "100%",
            handleStyle: {
              // color: "#5B3AAE",
            },
            textStyle: {
              // color: "rgba(204,187,225,0.5)",
            },
            // fillerColor: "rgba(67,55,160,0.4)",
            // borderColor: "rgba(204,187,225,0.5)",
          },
          {
            type: "inside",
            show: true,
            height: 25,
            start: 1,
            end: 35,
          },
        ],
        color: ["#3ABADF", "#2DDCD7", "#F28E2A", "#005DFF"],
        yAxis: [
          {
            type: "value",
            // interval: 5,
            axisLabel: {
              formatter: "{value}",
              textStyle: {
                color: "#CFCFCF",
              },
            },
            axisLine: {
              lineStyle: {
                color: "#CFCFCF",
              },
            },
            splitLine: {
              // show: false,
            },
          },
        ],
        grid: {
          top: "12%",
          left: "3%",
          right: "7%",
          bottom: "15%",
          containLabel: true,
        },
        xAxis: [
          {
            // data: [1, 2, 3, 4, 5, 6, 7, 8, 9],
            data: xAxis,
            type: "category",
            boundaryGap: false,
            axisLabel: {
              // show: false, // 不显示坐标轴上的文字
            },
            axisTick: {
              // show: false,
              lineStyle: {
                color: "#CFCFCF",
              },
            },
            axisLine: {
              // show: false,
              lineStyle: {
                color: "#CFCFCF",
              },
            },
            splitLine: {
              // show: false,
            },
          },
        ],
        // series: seriesData,
        series: [
          {
            name: "振动频率",
            smooth: true,
            type: "line",
            // markLine: {
            //   silent: true,
            //   data: [
            //     {
            //       yAxis: 5,
            //     },
            //   ],
            // },
            // data: [5, 5, 6, 2, 6, 7, 2, 7, 2, 5],
            data: seriesData,
          },
        ],
      };
      if (this.charts) {
        this.charts.dispose();
      }
      this.charts = echarts.init(document.getElementById("Tempcharts"));
      this.charts.setOption(option);
    },
  },
};
</script>
<style lang="less" scoped>
#Tempcharts {
  width: 100%;
  height: 400px;
}
</style>
