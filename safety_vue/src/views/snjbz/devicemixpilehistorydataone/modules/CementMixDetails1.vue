<template>
  <j-modal
    :title="title"
    :width="1200"
    :visible="visible"
    @ok="handleOk"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <a-tabs default-active-key="1">
      <a-tab-pane tab="图形分析" key="1" :forceRender="true">
        <div class="cementMix">
          <div class="cementMix-left">
            <div ref="qxtFour" :style="chartStyle4"></div>
          </div>
          <div class="cementMix-right">
            <div ref="qxtOne" :style="chartStyle1"></div>
            <div ref="qxtTwo" :style="chartStyle2"></div>
            <div ref="qxtThree" :style="chartStyle3"></div>
          </div>
        </div>
      </a-tab-pane>
      <a-tab-pane tab="评分表" key="2" :forceRender="true">
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="testData1"
          :precision="1"
          :pagination="false"
        >
          <template slot="lamination" slot-scope="text">
            <a>{{ text }}</a>
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </j-modal>
</template>

<script>
import { getAction, postAction } from "@/api/manage";
const renderContent = (value, row, index) => {
  const obj = {
    children: value,
    attrs: {},
  };
  if (index === 3) {
    // if(row === 3 || row === 4){
    //  obj.attrs.colSpan = 0;
    // }
  }
  return obj;
};

const toFixedNum2 = (num) => {
  return Number(num).toFixed(2)
}
export default {
  name: "CementMixDetails",
  components: {},
  data() {
    return {
      title: "水泥搅拌桩详情",
      visible: false,
      disableSubmit: true,
      model: {},
      chartStyle1: { width: "100%", height: "250px" },
      chartStyle2: { width: "100%", height: "250px" },
      chartStyle3: { width: "100%", height: "250px" },
      chartStyle4: { width: "100%", height: "800px" },
      /* 分页参数 */
      ipagination: {
        current: 1,
        pageSize: 5,
        // pageSizeOptions: ['2'],
        // showTotal: (total, range) => {
        //   return range[0] + '-' + range[1] + ' 共' + total + '条'
        // },
        showQuickJumper: false,
        showSizeChanger: false,
        total: 0,
      },
      dictOptions: {},
      newArr: [],
      newDataSource: [],
      initDateSource: [],
      initPageSize: 2,
      testData1: null,
      echart4Data: null,
      // 表头
      columns: [
        // {
        //   title: '分层(m)',
        //   dataIndex: 'lamination',
        //   customRender: (text, row, index) => {
        //     if (index < 4) {
        //       return <a href="javascript:;">{text}</a>;
        //     }
        //     return {
        //       children: <a href="javascript:;">{text}</a>,
        //       attrs: {
        //         colSpan: 4,
        //       },
        //     };
        //   },
        // },
        // {
        //   title: '序号',
        //   dataIndex: '',
        //   key:'rowIndex',
        //   // width:60,
        //   align:"center",
        //   fixed:"left",
        //   customRender:function (t,r,index) {
        //     return parseInt(index)+1;
        //   }
        // },
        {
          title: "分层(m)",
          align: "center",
          dataIndex: "lamination",
          customRender: renderContent,
        },
        // {
        //   title:'设备名称',
        //   align:"center",
        //   dataIndex: 'c',
        //   fixed:"left",
        //   scopedSlots: { customRender: 'lamination' }
        // },
        {
          title: "段灰量(kg/m)",
          align: "center",
          dataIndex: "mixpileWb",
          customRender: renderContent,
          render: (item) => (
          <div title={toFixedNum2(item)}>
            {toFixedNum2(item)}
          </div>)
        },
        {
          title: "段灰量得分",
          align: "center",
          dataIndex: "mixpileWbscore",
          customRender: renderContent,
        },
        {
          title: "搅拌次数",
          align: "center",
          dataIndex: "mixpileM",
          customRender: (value, row, index) => {
            const obj = {
              children: value,
              attrs: {},
            };
            // if (index === 2) {
            //   obj.attrs.rowSpan = 2;
            // }
            // // These two are merged into above cell
            // if (index === 3) {
            //   obj.attrs.rowSpan = 0;
            // }
            if (index === this.arrLength) {
              obj.attrs.colSpan = 2;
              // obj.attrs.colSpan = 0;
            }
            return obj;
          },
        },
        {
          title: "搅拌次数得分",
          align: "center",
          dataIndex: "mixpileMscore",
          customRender: (value, row, index) => {
            const obj = {
              children: value,
              attrs: {},
            };
            // if (index === 2) {
            //   obj.attrs.rowSpan = 2;
            // }
            // // These two are merged into above cell
            // if (index === 3) {
            //   obj.attrs.rowSpan = 0;
            // }
            // if (index === 4) {
            //   obj.attrs.colSpan = 0;
            // }
            if (index === this.arrLength) {
              obj.attrs.colSpan = 0;
              // obj.attrs.colSpan = 0;
            }
            return obj;
          },
        },
        {
          title: "分段得分",
          align: "center",
          dataIndex: "segmentedScoring",
          customRender: renderContent,
        },
      ],
      arrLength: 0,
    };
  },
  mounted() {
    console.log("test00000000000000000");
  },
  methods: {
    show(record) {
      console.log(record.pileRealdep, "pileRealdep");
      this.model = Object.assign({}, record);
      this.visible = true;
      this.$nextTick(() => {
        this.getData();
        this.getScore();
      });
    },
    dataHandling1() {
      //动态新增每页条数
      this.ipagination.pageSize = this.newDataSource.length + 1;
      this.newArr = [];
      let arrs = this.newDataSource;
      if (arrs.length > 0) {
        let item = {};
        item.lamination = "桩长";
        item.mixpileM = "施工等级判定";
        let mixpileWb = 0;
        this.arrLength = arrs.length;
        for (let i = 0; i < arrs.length; i++) {
          if (arrs[i].mixpileWb) {
            mixpileWb += Number(arrs[i].mixpileWb);
          }
          if (arrs.length - 1 == i) {
            console.log(arrs[i - 1], "arrs[i-1]");
            item.mixpileWbscore = arrs[i].hege; //段灰量得分
            item.segmentedScoring = arrs[i].mination; //分段得分
          }
        }
        if (this.model.deviceMixpileHistorydataOneList) {
          let arrDev = this.model.deviceMixpileHistorydataOneList;
          let minNum = arrDev.reduce((a, b) => (a.pileRealdep < b.pileRealdep ? a : b))
            .pileRealdep;
          item.mixpileWb = minNum.substring(0, minNum.indexOf(".") + 3) + "m";
        } else {
          item.mixpileWb = Number(this.model.pileRealdep).toFixed(2) + "m";
        }
        this.newDataSource.push(item);
      }
      this.testData1 = Object.values(this.newDataSource);
      this.testData1[this.testData1.length - 2].lamination = "得分";
      this.testData1[this.testData1.length - 2].mixpileWb = "";//段灰量(kg/m)
      this.testData1[this.testData1.length - 2].mixpileM = "";//搅拌次数
    },
    getScore() {
      let params = {
        pileno: this.model.pileNo,
        shebeino: this.model.shebeino,
        pileMileage: this.model.pileMileage,
        partEndtime: this.model.pileTime.match(/\d{4}-\d{2}-\d{2}/)[0],
        // pageSize:300,
      };
      getAction("/mixpileanalysis/mixpileAnalysis/lists", params).then((res) => {
        if (res.success) {
          if (res.result.length > 0) {
            let dataArr = res.result;
            for (let i = 0; i < dataArr.length; i++) {  
              dataArr[i].mixpileWb = Number(dataArr[i].mixpileWb).toFixed(1);  
              dataArr[i].mixpileWbscore = Number(dataArr[i].mixpileWbscore).toFixed(1);
              dataArr[i].mixpileM = Number(dataArr[i].mixpileM).toFixed(1);
              dataArr[i].mixpileMscore = Number(dataArr[i].mixpileMscore).toFixed(1);
              dataArr[i].segmentedScoring = Number(dataArr[i].segmentedScoring).toFixed(1);
            }  
            this.echart4Data = JSON.parse(JSON.stringify(dataArr));
            this.testData1 = JSON.parse(JSON.stringify(dataArr));
            this.newDataSource = JSON.parse(JSON.stringify(dataArr));
            this.echart4Data.length = this.echart4Data.length - 1;
            let data = this.echart4Data;
            let partXList = []; //段灰量(kg/m)
            let partYList = []; //分层(m)
            let mixpileWbscore = [];
            data.forEach((item) => {
              partXList.push(item.mixpileWb);
              partYList.push(item.lamination);
              mixpileWbscore.push(item.mixpileWbscore);
            });
            this.qxtEchart4(partXList, partYList, mixpileWbscore);
            this.dataHandling1();
          } else {
            this.echart4Data = [];
            this.testData1 = [];
            this.newDataSource = [];
            this.$message.warning("暂无评分数据");
          }
          console.log(res.result, "res.result----------");
        }
      });
    },
    getData() {
      let params = {
        partEndtime: this.model.pileTime,
        shebeino: this.model.shebeino,
        pileNo: this.model.pileNo,
        pageSize: 300,
      };
      getAction(
        "/devicemixpilehistorydatapart/deviceMixpileHistorydataPart/list",
        params
      ).then((res) => {
        if (res.success) {
          let data = res.result.records;
          let partXList = []; //段X
          let partYList = []; //段Y
          let partBetonList = []; //段浆量
          let partDepList = []; //段深度
          let partEndtimeList = []; //时间
          let pileDensityList = []; // 密度
          let partElectricityList = []; //电流
          let partSpeedList = []; //速度
          data.forEach((item) => {
            partXList.push(item.partX);
            partYList.push(item.partY);
            partBetonList.push(item.partBeton); //流量
            partSpeedList.push(item.partSpeed); //速度
            partDepList.push(item.partDep);
            partEndtimeList.push(item.partEndtime);
            pileDensityList.push(item.pileDensity); // 密度
            partElectricityList.push(item.partElectricity); //电流
          });
          this.qxtEchart1(partEndtimeList, partDepList, pileDensityList);
          this.qxtEchart2(partEndtimeList, partDepList, partElectricityList);
          this.qxtEchart3(partEndtimeList, partBetonList, partSpeedList);
          // this.qxtEchart2(partEndtimeList, partBetonList)
          // this.qxtEchart3(partEndtimeList, partXList, partYList)
        }
      });
    },
    //曲线图
    qxtEchart1(xList, yList, yList1) {
      let option = {
        legend: {
          data: ["深度", "密度"],
        },
        tooltip: {
          trigger: "axis",
        },
        title: [
          {
            text: "深度、密度曲线",
            // text: '深度/时间图',
            top: 0,
            left: 10,
            textStyle: {
              fontSize: 16,
            },
          },
        ],
        xAxis: {
          type: "category",
          boundaryGap: true,
          axisLabel: {
            // interval: 3,
            fontSize: 14,
          },
          axisTick: {
            show: false, // 隐藏坐标轴的刻度线
          },
          data: xList,
        },
        yAxis: [
          {
            name: "深度(m)",
            nameTextStyle: {
              align: "center",
              fontSize: 12,
            },
            axisLine: { show: true },
            type: "value",
            axisLabel: {
              fontSize: 14,
            },
          },
          {
            name: "密度(g/cm³)",
            position: "right",
            nameTextStyle: {
              align: "center",
              fontSize: 12,
            },
            axisLine: { show: true },
            type: "value",
            axisLabel: {
              fontSize: 14,
            },
          },
        ],
        series: [
          {
            name: "深度",
            type: "line",
            smooth: true,
            data: yList,
            yAxisIndex: 0,
          },
          {
            name: "密度",
            type: "line",
            smooth: true,
            data: yList1,
            yAxisIndex: 1,
          },
        ],
      };
      let myChart = this.$echarts.getInstanceByDom(this.$refs.qxtOne);
      if (myChart == null) {
        myChart = this.$echarts.init(this.$refs.qxtOne);
      }
      myChart.setOption(option);
    },
    qxtEchart2(xList, yList, yList1) {
      let option = {
        legend: {
          data: ["深度", "电流"],
          // data: ['浆量'],
        },
        tooltip: {
          trigger: "axis",
        },
        title: [
          {
            text: "深度、电流曲线",
            top: 0,
            left: 10,
            textStyle: {
              fontSize: 16,
            },
          },
        ],
        xAxis: {
          type: "category",
          boundaryGap: true,
          axisLabel: {
            // interval: 3,
            fontSize: 14,
          },
          axisTick: {
            show: false, // 隐藏坐标轴的刻度线
          },
          data: xList,
        },
        yAxis: [
          {
            name: "深度(m)",
            nameTextStyle: {
              align: "center",
              fontSize: 12,
            },
            axisLine: { show: true },
            type: "value",
            axisLabel: {
              fontSize: 14,
            },
          },
          {
            name: "电流(A)",
            position: "right",
            nameTextStyle: {
              align: "center",
              fontSize: 12,
            },
            axisLine: { show: true },
            type: "value",
            axisLabel: {
              fontSize: 14,
            },
          },
        ],
        series: [
          {
            name: "深度",
            type: "line",
            smooth: true,
            data: yList,
            yAxisIndex: 0,
          },
          {
            name: "电流",
            type: "line",
            smooth: true,
            data: yList1,
            yAxisIndex: 1,
          },
        ],
      };
      let myChart = this.$echarts.getInstanceByDom(this.$refs.qxtTwo);
      if (myChart == null) {
        myChart = this.$echarts.init(this.$refs.qxtTwo);
      }
      myChart.setOption(option);
    },
    qxtEchart3(xList, yList, yList1) {
      let option = {
        legend: {
          data: ["流量", "速度"],
        },
        tooltip: {
          trigger: "axis",
        },
        title: [
          {
            text: "流量、速度曲线",
            // text: '速度/电流/压力图',
            top: 0,
            left: 10,
            textStyle: {
              fontSize: 16,
            },
          },
        ],
        xAxis: {
          // name: '时间',
          type: "category",
          boundaryGap: true,
          axisLabel: {
            // interval: 3,
            fontSize: 14,
          },
          axisTick: {
            show: false, // 隐藏坐标轴的刻度线
          },
          data: xList,
        },
        yAxis: [
          {
            name: "流量(L/min)",
            // name: '倾角',
            nameTextStyle: {
              align: "center",
              fontSize: 12,
            },
            axisLine: { show: true },
            type: "value",
            axisLabel: {
              fontSize: 14,
            },
          },
          {
            name: "速度(m/min)",
            // name: "速度(cm/min)",
            // name: '倾角',
            position: "right",
            nameTextStyle: {
              align: "center",
              fontSize: 12,
            },
            axisLine: { show: true },
            type: "value",
            axisLabel: {
              fontSize: 14,
            },
          },
          // {
          //   name: '深度(m)',
          //   nameTextStyle: {
          //     align: 'center',
          //     fontSize: 12,
          //   },
          //   axisLine: { show: true },
          //   type: 'value',
          //   axisLabel: {
          //     fontSize: 14,
          //   },
          // },
        ],
        series: [
          {
            name: "流量",
            type: "line",
            smooth: true,
            yAxisIndex: 0,
            data: yList,
          },
          {
            name: "速度",
            type: "line",
            smooth: true,
            yAxisIndex: 1,
            data: yList1,
          },
        ],
      };
      let myChart = this.$echarts.getInstanceByDom(this.$refs.qxtThree);
      if (myChart == null) {
        myChart = this.$echarts.init(this.$refs.qxtThree);
      }
      myChart.setOption(option);
    },
    qxtEchart4(xList, yList, wbs) {
      xList = xList.map((e) => {
        return Number(e).toFixed(2);
      });
      let color = ["#006600", "#669900", "#99CC00", "#666666"];
      let dataList = this.qxtEchart4HandleData(xList, yList, wbs, color);
      let option = {
        title: {
          text: "成桩灰量分布-设计灰量：",
          left: "left",
          top: "top",
        },
        tooltip: {
          trigger: "item",
          formatter: "{b} : {c}",
        },
        legend: {
          orient: "vertical",
          left: "right",
          data: ["优", "良", "合格", "不合格"],
        },
        calculable: true,
        series: [
          {
            type: "funnel",
            width: "50%",
            left: "30%",
            // top: "10%",
            bottom: "6%",
            sort: "none",
            //min的值设置为value中最小的值
            minSize: "20%",
            label: {
              position: "left",
              fontSize: 14,
              formatter: function (params) {
                //自动提示工具
                return "分层:" + params.data.value1 + "m";
              },
            },
            itemStyle: {
              normal: {},
            },
            data: dataList,
          },
          {
            name: "漏斗图",
            type: "funnel",
            width: "50%",
            left: "30%",
            // top: "10%",
            bottom: "6%",
            sort: "none",
            //min的值设置为value中最小的值
            minSize: "20%",
            label: {
              position: "inside",
              fontSize: 14,
              formatter: function (params) {
                //自动提示工具
                return (
                  params.data.value + "kg"
                  // "\n" +
                );
              },
            },
            data: dataList,
          },
        ],
      };
      let myChart = this.$echarts.getInstanceByDom(this.$refs.qxtFour);
      if (myChart == null) {
        myChart = this.$echarts.init(this.$refs.qxtFour);
      }
      myChart.setOption(option);
    },
    qxtEchart4HandleData(xList, yList, wbs, color) {
      let dataList = [];
      xList.forEach((e, i) => {
        let score = "";
        let colorIndex = 0;
        if (wbs[i] >= 80) {
          score = "优";
        } else if (wbs[i] <= 79 && wbs[i] >= 70) {
          score = "良";
          colorIndex = 1;
        } else if (wbs[i] <= 69 && wbs[i] >= 60) {
          score = "合格";
          colorIndex = 2;
        } else if (wbs[i] <= 59) {
          score = "不合格";
          colorIndex = 3;
        }
        let obj = {};
        // if(i == xList.length-1){
        //   obj = {
        //     value: e,
        //     value1: yList[i],
        //     name: score,
        //     itemStyle: { height: 0 },
        //     label: { show: false },
        //     labelLine: { show: false },
        //   }
        // }else{
        obj = {
          value: e,
          value1: yList[i],
          name: score,
          itemStyle: {
            normal: {
              color: color[colorIndex],
            },
          },
        };
        // }
        dataList.push(obj);
      });
      return dataList;
    },
    handleOk() {
      this.visible = false;
    },
    handleCancel() {
      this.visible = false;
    },
  },
};
</script>
<style lang="less" scoped>
.cementMix {
  display: flex;
  &-left {
    width: 500px;
    margin-right: 50px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
  &-right {
    width: calc(100% - 550px);
    height: 800px;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
  }
}
/deep/ .ant-table table {
  border-collapse: collapse;
  td {
    // border: 2px solid #f0f0f0;
  }
}
.border-con {
  border-bottom: 2px solid black;
}
</style>
