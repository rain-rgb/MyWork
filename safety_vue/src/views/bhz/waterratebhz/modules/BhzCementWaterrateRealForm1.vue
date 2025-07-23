<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container>
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <div v-if="model">
            <div class="title">{{ model.lcname }}</div>
            <div class="item">
              <div class="item-top">
                温度<span>{{ model.temperature }}℃</span>
              </div>
              <div class="item-top">
                <!-- 含水率<span>{{ model.water }}</span> -->
                平均含水率(%)<span>{{ model.averate }}</span>
              </div>
            </div>
            <div class="item">
              <div class="item-btm">
                湿度实时值<span>{{ model.humidity }}</span>
              </div>
              <div class="item-btm">
                湿度平均<span>{{ model.avehum }}</span>
              </div>
            </div>
            <div id="echartLine"></div>
          </div>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from "@/api/manage";
import { validateDuplicateValue } from "@/utils/util";
import * as echarts from "echarts";

export default {
  name: "BhzCementWaterrateRealForm",
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
        add: "/bhzcementwaterratereal/bhzCementWaterrateReal/add",
        edit: "/bhzcementwaterratereal/bhzCementWaterrateReal/edit",
        queryById: "/bhzcementwaterratereal/bhzCementWaterrateReal/queryById",
        echart: "/bhzcementwaterrate/bhzCementWaterrate/list",
      },
      charts: null,
      echartData: [],
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
      console.log(this.model, `备份model原始值___________________________ 222`);
      this.getData();
    },
    getData() {
      let params = {
        pageNo: 1,
        pageSize: 30,
      };
      //曲线图数据
      getAction(this.url.echart, params).then((res) => {
        if (res.success) {
          this.echartData = res.result.records;
          console.log(
            this.echartData,
            "this.echartData---------------------------------___________________"
          );
          this.getEcarts(this.echartData);
        }
      });
    },
    getEcarts(eData) {
      let dataList = eData.map((e) => {
        return e.averate;
        // return e.humidity;
      });
      let xData = eData.map((e) => {
        return e.uploadTime;
      });
      // var xData = (function () {
      //   var data = [];
      //   for (var i = 1; i < 13; i++) {
      //     data.push(i + "月份");
      //   }
      //   return data;
      // })();
      let title = `平均值采集完成`;
      let option = {
        title: {
          text: title,
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
          top: 110,
          bottom: 95,
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
        //   data: ["女", "男", "平均"],
        // },

        calculable: true,
        xAxis: [
          {
            type: "category",
            name: "时间",
            nameLocation: "middle",
            nameTextStyle: {
              padding: 15,
            },
            axisLine: {
              lineStyle: {
                color: "#90979c",
              },
            },
            splitLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            splitArea: {
              show: false,
            },
            axisLabel: {
              // interval: 0,
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            type: "value",
            name: "平均含水率(%)",
            // name: "湿度值",
            nameLocation: "middle",
            nameTextStyle: {
              padding: 45,
            },
            splitLine: {
              show: false,
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
              interval: 0,
            },
            splitArea: {
              show: false,
            },
          },
        ],
        dataZoom: [
          {
            show: true,
            height: 30,
            xAxisIndex: [0],
            bottom: 20,
            start: 10,
            end: 40,
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
            name: "平均含水率(%)",
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
      this.charts = echarts.init(document.getElementById("echartLine"));
      // this.charts = this.$refs.chart
      this.charts.setOption(option);
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
  },
};
</script>
<style scoped lang="less">
.title {
  font-size: 20px;
}
.item {
  display: flex;
  .item-top {
    font-size: 24px;
    font-weight: 600;
  }
  .item-btm {
    font-size: 20px;
    font-weight: 600;
  }
  .item-top,
  .item-btm {
    span {
      color: #fc0020;
      margin-left: 20px;
      margin-right: 70px;
    }
  }
}
#echartLine {
  width: 1000px;
  height: 650px;
}
</style>
