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
    <h1 class="titleSG">
      施工区域({{ changeDate(tableArr[1].value) }}-{{ tableArr[2].value }})信息汇总
    </h1>
    <div id="appTable">
      <a-col :span="howWidth" v-for="(item, index) in tableArr" :key="index" class="boxM">
        <div class="box">
          <div class="content1">{{ item.name }}</div>
          <div
            class="content2"
            v-if="item.name != `开始施工桩号` && item.name != `结束施工桩号`"
          >
            {{ item.value == "" ? "~" : item.value }}
          </div>
          <div class="content2" v-else>
            {{ item.value == "" ? "~" : getStartstation(item.value) }}
          </div>
        </div>
      </a-col>
    </div>
    <!-- 碾压遍数 Pie1-->
    <pie :data-source="dataSourcePie1" style="width: 100%" title="碾压遍数" />
    <a-table
      ref="table"
      size="middle"
      :columns="columnsPie1"
      :dataSource="dataSourcePie1"
      :pagination="false"
    >
    </a-table>
    <!-- 振动遍数 Pie2-->
    <pie :data-source="dataSourcePie2" style="width: 100%" title="振动遍数" />
    <a-table
      ref="table"
      size="middle"
      :columns="columnsPie2"
      :dataSource="dataSourcePie2"
      :pagination="false"
    >
    </a-table>
    <!-- 机械速度 Line1-->
    <h4 :style="{ marginBottom: '0px', marginTop: '40px' }">机械速度</h4>
    <div id="echartLine1"></div>
    <a-table
      :pagination="ipagination"
      rowKey="sid"
      :columns="columns1Line"
      :data-source="dataSourceLineTable"
      @change="tableChange"
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
    <!-- 压实层厚 Lin2-->
    <h4 :style="{ marginBottom: '0px', marginTop: '40px' }">压实层厚</h4>
    <div id="echartLine2"></div>
    <!-- <LineChartMultid :title="`压实层厚`" :data-source="dataSource1Line"></LineChartMultid> -->
    <a-table
      :pagination="ipagination1"
      rowKey="sid"
      :columns="columns2Line"
      :data-source="dataSourceLineTable"
      @change="tableChange1"
    >
    </a-table>
    <!-- 逐桩施工区域详情 -->
    <div class="zzTitle">逐桩施工区域详情</div>
    <a-table
      :pagination="ipagination2"
      rowKey="sid"
      :columns="columns3"
      :data-source="dataSourceLineTable"
      @change="tableChange2"
    >
    </a-table>
  </j-modal>
</template>

<script>
import { getAction } from "@api/manage";
import LineChartsYajiang from "@views/zlyj/modules/LineChartsYajiang";
import * as echarts from "echarts";
import Vue from "vue";
import Pie from "@comp/chart/BhzStaPie";
import LineChartMultid from "@comp/chart/YaLijiLzqxTF";
// import { JeecgListMixin } from "@/mixins/JeecgListMixin";

export default {
  name: "PavingResultDetail",
  components: {
    LineChartsYajiang,
    Pie,
    LineChartMultid,
  },
  // mixins: [JeecgListMixin],
  data() {
    return {
      height: 420,
      data: [],
      yajiangrenwudanms: false,

      tableArr: [
        {
          key: "projectid",
          name: "工程名称",
          value: "~",
        },
        {
          key: "starttime",
          name: "开始施工时间",
          value: `~`,
        },
        {
          key: "startstation",
          name: "开始施工桩号",
          value: "~!",
        },
        {
          key: "workmile",
          name: "施工里程(m)",
          value: "",
        },
        {
          key: "speedavg",
          name: "平均速度(km/h)",
          value: "",
        },
        {
          key: "timesvibrateavg",
          name: "平均振动遍数",
          value: "",
        },
        {
          key: "inspectstat",
          name: "结束桩号高程(m)",
          value: "",
        },
        {
          key: "inspectstat",
          name: "标段名称",
          value: "",
        },
        {
          key: "endtime",
          name: "最后施工时间",
          value: "",
        },
        {
          key: "endstation",
          name: "结束施工桩号",
          value: "",
        },
        {
          key: "thickavg",
          name: "平均厚度(cm)",
          value: "",
        },
        {
          key: "timesavg",
          name: "平均碾压遍数",
          value: "",
        },
        {
          key: "qualitystat",
          name: "开始桩号高程(m)",
          value: "",
        },
      ],
      howWidth: 12,
      dataSourcePie1: [],
      columnsPie1: [
        {
          title: "遍数",
          align: "center",
          dataIndex: "item",
        },
        {
          title: "碾压遍数占比(%)",
          align: "center",
          dataIndex: "count",
        },
      ],
      columnsPie2: [
        {
          title: "遍数",
          align: "center",
          dataIndex: "item",
        },
        {
          title: "振动遍数占比(%)",
          align: "center",
          dataIndex: "count",
        },
      ],
      dataSource1Line: [],
      dataSourceLineTable: [],
      columns1Line: [
        {
          title: "桩号",
          align: "center",
          dataIndex: "station",
        },
        {
          title: "平均高程(m)",
          align: "center",
          dataIndex: "avgheight",
        },
        // {
        //   title: "施工完成高程(m)",
        //   align: "center",
        //   dataIndex: "count1",
        // },
        {
          title: "平均碾压速度(km/h)",
          align: "center",
          dataIndex: "avgspeed",
        },
      ],
      columns2Line: [
        {
          title: "桩号",
          align: "center",
          dataIndex: "station",
        },
        {
          title: "平均高程(m)",
          align: "center",
          dataIndex: "avgheight",
        },
        // {
        //   title: "施工完成高程(m)",
        //   align: "center",
        //   dataIndex: "count1",
        // },
        {
          title: "施工压实厚度(m)",
          align: "center",
          dataIndex: "avgthick",
        },
      ],
      columns3: [
        {
          title: "桩号",
          align: "center",
          dataIndex: "station",
        },
        {
          title: "平均高程(m)",
          align: "center",
          dataIndex: "avgheight",
        },
        {
          title: "平均厚度(m)",
          align: "center",
          dataIndex: "avgthick",
        },
        {
          title: "平均碾压遍数",
          align: "center",
          dataIndex: "avgtimes",
        },
        {
          title: "平均振动遍数",
          align: "center",
          dataIndex: "avgvibratetimes",
        },
        {
          title: "碾压速度(km/h)",
          align: "center",
          dataIndex: "avgspeed",
        },
      ],
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
      ipagination2: {
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
      url: {
        listbg: "hc_tfstationdetail/hcTfstationdetail/listbg",
      },
    };
  },
  mounted() {
    this.getParams();
  },
  methods: {
    getParams() {
      let arr = {};
      let id = this.$route.query.id;
      let blockid = this.$route.query.blockid;
      if (id) {
        let arr = { id, blockid };
        this.getData(arr);
        this.visible = true;
      }
    },
    call(arr) {
      this.visible = true;
      // console.log(123189283901);
      // this.testDataFnc();
      this.getData(arr);
    },
    testDataFnc() {
      for (let j = 0; j < 111; j++) {
        // if (i === 0) {
        this.dataSource1Line.push({ type: j, 抗压力值: parseFloat(j) });
        // if (this.imgURl1.length < 2) {
        //   this.imgURl1.push(data[i].spic, data[i].pspic);
        // }
        // }
      }
    },
    getData(arr) {
      // let resTest = {
      //   success: true,
      //   message: "操作成功！",
      //   code: 200,
      //   result: {
      //     id: 2488,
      //     blockid: "1695260017783",
      //     blockname: null,
      //     layername: null,
      //     startstation: "25665",
      //     endstation: "25745",
      //     starttime: "2023-09-21 09:27:55",
      //     endtime: "2023-09-21 10:59:49",
      //     worktime: "1.5",
      //     workmile: "80",
      //     workarea: "1819",
      //     border:
      //       "[[3211525.618,477569.314],[3211525.024,477569.574],[3211523.318,477569.325],[3211519.613,477571.196],[3211519.888,477572.165],[3211517.575,477573.799],[3211516.597,477573.917],[3211515.784,477574.904],[3211515.247,477574.988],[3211509.170,477578.910],[3211509.051,477579.430],[3211509.190,477579.683],[3211507.547,477602.203],[3211507.093,477610.101],[3211505.526,477609.833],[3211504.857,477612.110],[3211503.101,477615.909],[3211504.130,477617.325],[3211498.180,477630.198],[3211497.979,477633.344],[3211493.547,477640.769],[3211496.718,477642.900],[3211498.892,477643.652],[3211501.735,477641.574],[3211504.872,477635.309],[3211507.051,477636.045],[3211507.792,477635.368],[3211510.065,477635.722],[3211511.116,477637.360],[3211513.295,477638.096],[3211517.790,477636.895],[3211521.075,477632.608],[3211527.963,477618.472],[3211529.814,477618.258],[3211532.675,477613.608],[3211534.022,477609.916],[3211535.473,477605.555],[3211534.710,477597.981],[3211536.770,477595.882],[3211538.940,477594.592],[3211538.097,477593.216],[3211536.878,477592.501],[3211536.402,477591.068],[3211536.270,477590.191],[3211539.894,477584.259],[3211541.921,477584.248],[3211542.398,477582.525],[3211540.175,477581.933],[3211539.204,477580.529],[3211542.940,477575.114],[3211536.265,477570.753],[3211532.801,477569.446],[3211530.602,477568.773],[3211525.618,477569.314],[3211525.618,477569.314]]",
      //     vibrateratio: "93",
      //     timesavg: "5",
      //     timesvibrateavg: "5",
      //     speedavg: "2.8",
      //     thickavg: "21.0",
      //     alarmnum: "0",
      //     qualitystat: "68.48",
      //     workstat: "1",
      //     inspectstat: "69.12",
      //     projectid: "甬金衢上高速公路金华城区段",
      //     sectionid: "第SG01标段",
      //     standard: "0",
      //     reason: null,
      //     overproofStatus: 0,
      //     i: 50.617283950617285,
      //     j: 9.876543209876543,
      //     n: 39.50617283950617,
      //     x: 53.086419753086425,
      //     y: 8.641975308641975,
      //     z: 38.2716049382716,
      //     hcTfstationdetail: [
      //       {
      //         id: 673304,
      //         station: "25665",
      //         avgheight: "68.48",
      //         avgthick: "0.0",
      //         avgtimes: "1",
      //         avgspeed: "3.76",
      //         avgvibratetimes: "1",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673305,
      //         station: "25666",
      //         avgheight: "68.48",
      //         avgthick: "0.0",
      //         avgtimes: "1",
      //         avgspeed: "3.76",
      //         avgvibratetimes: "1",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673306,
      //         station: "25667",
      //         avgheight: "68.49",
      //         avgthick: "0.0",
      //         avgtimes: "1",
      //         avgspeed: "3.76",
      //         avgvibratetimes: "1",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673307,
      //         station: "25668",
      //         avgheight: "68.48",
      //         avgthick: "0.0",
      //         avgtimes: "2",
      //         avgspeed: "3.4",
      //         avgvibratetimes: "2",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673308,
      //         station: "25669",
      //         avgheight: "68.48",
      //         avgthick: "0.0",
      //         avgtimes: "2",
      //         avgspeed: "3.25",
      //         avgvibratetimes: "2",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673309,
      //         station: "25670",
      //         avgheight: "68.48",
      //         avgthick: "0.0",
      //         avgtimes: "2",
      //         avgspeed: "3.22",
      //         avgvibratetimes: "2",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673310,
      //         station: "25671",
      //         avgheight: "68.47",
      //         avgthick: "0.0",
      //         avgtimes: "2",
      //         avgspeed: "3.23",
      //         avgvibratetimes: "2",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673311,
      //         station: "25672",
      //         avgheight: "68.46",
      //         avgthick: "0.0",
      //         avgtimes: "2",
      //         avgspeed: "3.05",
      //         avgvibratetimes: "2",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673312,
      //         station: "25673",
      //         avgheight: "68.47",
      //         avgthick: "0.0",
      //         avgtimes: "2",
      //         avgspeed: "3.21",
      //         avgvibratetimes: "2",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673313,
      //         station: "25674",
      //         avgheight: "68.46",
      //         avgthick: "0.0",
      //         avgtimes: "2",
      //         avgspeed: "3.06",
      //         avgvibratetimes: "2",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673314,
      //         station: "25675",
      //         avgheight: "68.42",
      //         avgthick: "0.0",
      //         avgtimes: "2",
      //         avgspeed: "2.89",
      //         avgvibratetimes: "2",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673315,
      //         station: "25676",
      //         avgheight: "68.38",
      //         avgthick: "0.0",
      //         avgtimes: "2",
      //         avgspeed: "2.86",
      //         avgvibratetimes: "2",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673316,
      //         station: "25677",
      //         avgheight: "68.37",
      //         avgthick: "0.0",
      //         avgtimes: "2",
      //         avgspeed: "2.77",
      //         avgvibratetimes: "2",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673317,
      //         station: "25678",
      //         avgheight: "68.37",
      //         avgthick: "0.0",
      //         avgtimes: "3",
      //         avgspeed: "2.72",
      //         avgvibratetimes: "3",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673318,
      //         station: "25679",
      //         avgheight: "68.36",
      //         avgthick: "0.0",
      //         avgtimes: "3",
      //         avgspeed: "2.74",
      //         avgvibratetimes: "3",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673319,
      //         station: "25680",
      //         avgheight: "68.38",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "2.76",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673320,
      //         station: "25681",
      //         avgheight: "68.37",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "2.78",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673321,
      //         station: "25682",
      //         avgheight: "68.37",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "2.71",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673322,
      //         station: "25683",
      //         avgheight: "68.38",
      //         avgthick: "0.01",
      //         avgtimes: "4",
      //         avgspeed: "2.69",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673323,
      //         station: "25684",
      //         avgheight: "68.38",
      //         avgthick: "0.02",
      //         avgtimes: "4",
      //         avgspeed: "2.69",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673324,
      //         station: "25685",
      //         avgheight: "68.39",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "2.71",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673325,
      //         station: "25686",
      //         avgheight: "68.39",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "2.7",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673326,
      //         station: "25687",
      //         avgheight: "68.39",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "2.76",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673327,
      //         station: "25688",
      //         avgheight: "68.39",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "2.7",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673328,
      //         station: "25689",
      //         avgheight: "68.4",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "2.61",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673329,
      //         station: "25690",
      //         avgheight: "68.41",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "2.64",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673330,
      //         station: "25691",
      //         avgheight: "68.41",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "2.68",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673331,
      //         station: "25692",
      //         avgheight: "68.42",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "2.75",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673332,
      //         station: "25693",
      //         avgheight: "68.44",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "2.79",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673333,
      //         station: "25694",
      //         avgheight: "68.45",
      //         avgthick: "0.0",
      //         avgtimes: "5",
      //         avgspeed: "2.74",
      //         avgvibratetimes: "5",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673334,
      //         station: "25695",
      //         avgheight: "68.46",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "2.77",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673335,
      //         station: "25696",
      //         avgheight: "68.47",
      //         avgthick: "0.0",
      //         avgtimes: "5",
      //         avgspeed: "2.7",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673336,
      //         station: "25697",
      //         avgheight: "68.48",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "2.7",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673337,
      //         station: "25698",
      //         avgheight: "68.49",
      //         avgthick: "0.0",
      //         avgtimes: "5",
      //         avgspeed: "2.69",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673338,
      //         station: "25699",
      //         avgheight: "68.5",
      //         avgthick: "0.0",
      //         avgtimes: "5",
      //         avgspeed: "2.76",
      //         avgvibratetimes: "5",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673339,
      //         station: "25700",
      //         avgheight: "68.51",
      //         avgthick: "0.0",
      //         avgtimes: "5",
      //         avgspeed: "2.73",
      //         avgvibratetimes: "5",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673340,
      //         station: "25701",
      //         avgheight: "68.51",
      //         avgthick: "0.0",
      //         avgtimes: "5",
      //         avgspeed: "2.78",
      //         avgvibratetimes: "5",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673341,
      //         station: "25702",
      //         avgheight: "68.51",
      //         avgthick: "0.0",
      //         avgtimes: "6",
      //         avgspeed: "2.83",
      //         avgvibratetimes: "5",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673342,
      //         station: "25703",
      //         avgheight: "68.51",
      //         avgthick: "0.0",
      //         avgtimes: "6",
      //         avgspeed: "2.84",
      //         avgvibratetimes: "6",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673343,
      //         station: "25704",
      //         avgheight: "68.52",
      //         avgthick: "0.0",
      //         avgtimes: "6",
      //         avgspeed: "2.8",
      //         avgvibratetimes: "6",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673344,
      //         station: "25705",
      //         avgheight: "68.53",
      //         avgthick: "0.0",
      //         avgtimes: "6",
      //         avgspeed: "2.83",
      //         avgvibratetimes: "6",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673345,
      //         station: "25706",
      //         avgheight: "68.54",
      //         avgthick: "0.0",
      //         avgtimes: "6",
      //         avgspeed: "2.83",
      //         avgvibratetimes: "6",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673346,
      //         station: "25707",
      //         avgheight: "68.54",
      //         avgthick: "0.0",
      //         avgtimes: "7",
      //         avgspeed: "2.8",
      //         avgvibratetimes: "7",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673347,
      //         station: "25708",
      //         avgheight: "68.55",
      //         avgthick: "0.0",
      //         avgtimes: "7",
      //         avgspeed: "2.79",
      //         avgvibratetimes: "7",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673348,
      //         station: "25709",
      //         avgheight: "68.56",
      //         avgthick: "0.0",
      //         avgtimes: "7",
      //         avgspeed: "2.75",
      //         avgvibratetimes: "7",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673349,
      //         station: "25710",
      //         avgheight: "68.57",
      //         avgthick: "0.0",
      //         avgtimes: "7",
      //         avgspeed: "2.74",
      //         avgvibratetimes: "7",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673350,
      //         station: "25711",
      //         avgheight: "68.59",
      //         avgthick: "0.0",
      //         avgtimes: "8",
      //         avgspeed: "2.75",
      //         avgvibratetimes: "8",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673351,
      //         station: "25712",
      //         avgheight: "68.61",
      //         avgthick: "0.0",
      //         avgtimes: "8",
      //         avgspeed: "2.75",
      //         avgvibratetimes: "8",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673352,
      //         station: "25713",
      //         avgheight: "68.62",
      //         avgthick: "0.0",
      //         avgtimes: "7",
      //         avgspeed: "2.74",
      //         avgvibratetimes: "7",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673353,
      //         station: "25714",
      //         avgheight: "68.65",
      //         avgthick: "0.0",
      //         avgtimes: "7",
      //         avgspeed: "2.74",
      //         avgvibratetimes: "7",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673354,
      //         station: "25715",
      //         avgheight: "68.66",
      //         avgthick: "0.0",
      //         avgtimes: "8",
      //         avgspeed: "2.81",
      //         avgvibratetimes: "8",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673355,
      //         station: "25716",
      //         avgheight: "68.67",
      //         avgthick: "0.0",
      //         avgtimes: "7",
      //         avgspeed: "2.81",
      //         avgvibratetimes: "7",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673356,
      //         station: "25717",
      //         avgheight: "68.69",
      //         avgthick: "0.0",
      //         avgtimes: "8",
      //         avgspeed: "2.83",
      //         avgvibratetimes: "8",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673357,
      //         station: "25718",
      //         avgheight: "68.71",
      //         avgthick: "0.0",
      //         avgtimes: "8",
      //         avgspeed: "2.8",
      //         avgvibratetimes: "8",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673358,
      //         station: "25719",
      //         avgheight: "68.73",
      //         avgthick: "0.0",
      //         avgtimes: "8",
      //         avgspeed: "2.79",
      //         avgvibratetimes: "8",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673359,
      //         station: "25720",
      //         avgheight: "68.75",
      //         avgthick: "0.0",
      //         avgtimes: "8",
      //         avgspeed: "2.81",
      //         avgvibratetimes: "8",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673360,
      //         station: "25721",
      //         avgheight: "68.77",
      //         avgthick: "0.0",
      //         avgtimes: "8",
      //         avgspeed: "2.79",
      //         avgvibratetimes: "8",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673361,
      //         station: "25722",
      //         avgheight: "68.78",
      //         avgthick: "0.0",
      //         avgtimes: "8",
      //         avgspeed: "2.8",
      //         avgvibratetimes: "8",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673362,
      //         station: "25723",
      //         avgheight: "68.8",
      //         avgthick: "0.0",
      //         avgtimes: "8",
      //         avgspeed: "2.8",
      //         avgvibratetimes: "8",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673363,
      //         station: "25724",
      //         avgheight: "68.83",
      //         avgthick: "0.0",
      //         avgtimes: "8",
      //         avgspeed: "2.84",
      //         avgvibratetimes: "8",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673364,
      //         station: "25725",
      //         avgheight: "68.85",
      //         avgthick: "0.0",
      //         avgtimes: "8",
      //         avgspeed: "2.86",
      //         avgvibratetimes: "8",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673365,
      //         station: "25726",
      //         avgheight: "68.86",
      //         avgthick: "0.0",
      //         avgtimes: "8",
      //         avgspeed: "2.87",
      //         avgvibratetimes: "8",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673366,
      //         station: "25727",
      //         avgheight: "68.89",
      //         avgthick: "0.0",
      //         avgtimes: "8",
      //         avgspeed: "2.87",
      //         avgvibratetimes: "8",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673367,
      //         station: "25728",
      //         avgheight: "68.9",
      //         avgthick: "0.0",
      //         avgtimes: "7",
      //         avgspeed: "2.87",
      //         avgvibratetimes: "7",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673368,
      //         station: "25729",
      //         avgheight: "68.91",
      //         avgthick: "0.0",
      //         avgtimes: "7",
      //         avgspeed: "2.85",
      //         avgvibratetimes: "7",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673369,
      //         station: "25730",
      //         avgheight: "68.93",
      //         avgthick: "0.0",
      //         avgtimes: "7",
      //         avgspeed: "2.84",
      //         avgvibratetimes: "7",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673370,
      //         station: "25731",
      //         avgheight: "68.94",
      //         avgthick: "0.0",
      //         avgtimes: "7",
      //         avgspeed: "2.84",
      //         avgvibratetimes: "7",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673371,
      //         station: "25732",
      //         avgheight: "68.96",
      //         avgthick: "0.0",
      //         avgtimes: "6",
      //         avgspeed: "2.87",
      //         avgvibratetimes: "6",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673372,
      //         station: "25733",
      //         avgheight: "68.97",
      //         avgthick: "0.0",
      //         avgtimes: "6",
      //         avgspeed: "2.87",
      //         avgvibratetimes: "6",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673373,
      //         station: "25734",
      //         avgheight: "68.98",
      //         avgthick: "0.0",
      //         avgtimes: "5",
      //         avgspeed: "2.85",
      //         avgvibratetimes: "5",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673374,
      //         station: "25735",
      //         avgheight: "69.0",
      //         avgthick: "0.0",
      //         avgtimes: "5",
      //         avgspeed: "2.96",
      //         avgvibratetimes: "5",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673375,
      //         station: "25736",
      //         avgheight: "69.0",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "2.99",
      //         avgvibratetimes: "4",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673376,
      //         station: "25737",
      //         avgheight: "69.02",
      //         avgthick: "0.0",
      //         avgtimes: "4",
      //         avgspeed: "3.03",
      //         avgvibratetimes: "3",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673377,
      //         station: "25738",
      //         avgheight: "69.02",
      //         avgthick: "0.0",
      //         avgtimes: "3",
      //         avgspeed: "3.03",
      //         avgvibratetimes: "3",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673378,
      //         station: "25739",
      //         avgheight: "69.04",
      //         avgthick: "0.0",
      //         avgtimes: "3",
      //         avgspeed: "3.04",
      //         avgvibratetimes: "3",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673379,
      //         station: "25740",
      //         avgheight: "69.04",
      //         avgthick: "0.0",
      //         avgtimes: "3",
      //         avgspeed: "3.01",
      //         avgvibratetimes: "3",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673380,
      //         station: "25741",
      //         avgheight: "69.05",
      //         avgthick: "0.0",
      //         avgtimes: "3",
      //         avgspeed: "2.97",
      //         avgvibratetimes: "3",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673381,
      //         station: "25742",
      //         avgheight: "69.07",
      //         avgthick: "0.0",
      //         avgtimes: "3",
      //         avgspeed: "2.9",
      //         avgvibratetimes: "3",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673382,
      //         station: "25743",
      //         avgheight: "69.1",
      //         avgthick: "0.0",
      //         avgtimes: "3",
      //         avgspeed: "2.74",
      //         avgvibratetimes: "3",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673383,
      //         station: "25744",
      //         avgheight: "69.1",
      //         avgthick: "0.0",
      //         avgtimes: "2",
      //         avgspeed: "2.63",
      //         avgvibratetimes: "2",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //       {
      //         id: 673384,
      //         station: "25745",
      //         avgheight: "69.12",
      //         avgthick: "0.0",
      //         avgtimes: "2",
      //         avgspeed: "2.63",
      //         avgvibratetimes: "2",
      //         blockid: "1695260017783",
      //         sectionid: "jishu01@20230909192710",
      //       },
      //     ],
      //   },
      //   data: null,
      //   timestamp: 1704871864428,
      // };
      // let dataTest = resTest.result;
      // Object.keys(dataTest).forEach((key) => {
      //   this.tableArr.forEach((e) => {
      //     if (key == e[`key`]) e[`value`] = dataTest[key];
      //     // console.log(key, e[`key`], "key  this.tableArr[key]-------");
      //   });
      // });
      // this.getPieData1(dataTest);
      // console.log(this.tableArr, "this.tableArr-------");

      let { id, blockid } = arr;
      let param = {
        id,
        blockId: blockid,
        // pageSize: this.ipagination.pageSize,
        // pageNo: this.ipagination.current,
      };
      getAction(this.url.listbg, param).then((res) => {
        // this.dataSource = res.result.records;

        if (res.success) {
          let data = res.result;
          Object.keys(data).forEach((key) => {
            this.tableArr.forEach((e) => {
              if (key == e[`key`]) e[`value`] = data[key];
              // console.log(key, e[`key`], "key  this.tableArr[key]-------");
            });
          });
          this.getPieData1(data);
          // console.log(this.dataSource, "this.dataSource reportDetail");
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
    getPieData1(data) {
      this.dataSourcePie1 = [];
      this.dataSourcePie1.push(
        { item: "<5(遍)", count: data.i },
        { item: "=5(遍)", count: data.j },
        { item: ">5(遍)", count: data.n }
      );
      this.dataSourcePie2 = [];
      this.dataSourcePie2.push(
        { item: "<5(遍)", count: data.x },
        { item: "=5(遍)", count: data.y },
        { item: ">5(遍)", count: data.z }
      );

      for (let j = 0; j < data.hcTfstationdetail.length; j++) {
        this.dataSource1Line.push({
          type: data.hcTfstationdetail[j].station,
          平均碾压速度: parseFloat(data.hcTfstationdetail[j].avgspeed),
        });
      }
      // 找出value的最小值
      let minValue = Math.min(
        ...data.hcTfstationdetail.map((item) => Number(item.station))
      );
      // 找出value的最大值
      let maxValue = Math.max(
        ...data.hcTfstationdetail.map((item) => Number(item.station))
      );
      console.log(minValue, maxValue, "minValue minValue");

      this.dataSourceLineTable = [];
      this.dataSourceLineTable = data.hcTfstationdetail;
      this.$nextTick(() => {
        this.getEcarts(
          data.hcTfstationdetail,
          `echartLine1`,
          `平均碾压速度(km/h)`,
          `avgspeed`
        );
        this.getEcarts(
          data.hcTfstationdetail,
          `echartLine2`,
          `施工压实厚度(m)`,
          `avgthick`
        );
      });
    },

    getEcarts(eData, DomId, xName, target) {
      let that = this;
      let dataList = eData.map((e) => {
        return e[target];
      });
      let xData = eData.map((e) => {
        return e.station;
      });
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
          right: "5%",
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
            console.log(params, "params-------------");
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
              show: false,
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
              // interval: 0,
            },
            splitArea: {
              show: false,
            },
          },
        ],
        dataZoom: [
          {
            show: true,
            height: 20,
            xAxisIndex: [0],
            bottom: 20,
            start: 0,
            end: 50,
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
            show: false,
            height: 15,
            start: 1,
            end: 35,
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
    tableChange2(pagination) {
      this.ipagination2.current = pagination.current;
      this.ipagination2.pageSize = pagination.pageSize;
      // this.yajiangmessagedetail(this.holeid);
    },
    callback(key) {
      this.ipagination1.current = 1;
      this.ipagination1.pageSize = 10;
      // this.yajiangmessagedetail(key);
      // console.log(key);
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
    changeDate(target) {
      const dateString = target;
      const date = new Date(dateString);
      const formattedDate =
        date.getFullYear().toString().padStart(4, "0") +
        (date.getMonth() + 1).toString().padStart(2, "0") +
        date.getDate().toString().padStart(2, "0");
      // console.log(formattedDate); // 输出：20230921
      return formattedDate;
    },
  },
};
</script>
<style lang="less" scoped>
.titleSG {
  text-align: center;
}
.zzTitle {
  margin: 0 0 50px 0;
}
#appTable {
  width: 100%;
  // height: 300px;
  // min-height: 100vh;
  box-sizing: border-box;
  padding: 0 50px 150px 50px;
  // background-color: #884545;
  margin-bottom: 150px;
  .boxM {
    height: 40px;
  }
  .box {
    width: 100%;
    height: 40px;
    display: flex;
    border-left: 1px solid #e9e9e9;
    border-top: 1px solid #e9e9e9;
    .content1 {
      width: 40%;
      height: 40px;
      line-height: 40px;
      text-align: center;
      background-color: #fafafa;
      border-right: 1px solid #e9e9e9;
      border-bottom: 1px solid #e9e9e9;
      color: #000;
      font-size: 14px;
    }
    .content2 {
      width: 60%;
      height: 40px;
      line-height: 40px;
      text-align: center;
      background-color: #fff;
      border-right: 1px solid #e9e9e9;
      border-bottom: 1px solid #e9e9e9;
      color: #444444;
      font-size: 14px;
    }
  }
}
#echartLine1 {
  width: 100%;
  height: 350px;
}

#echartLine2 {
  width: 100%;
  height: 350px;
}
/deep/ .ant-table-tbody .ant-table-row td {
  padding-top: 8px !important;
  padding-bottom: 8px !important;
}
</style>
