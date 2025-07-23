<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="8" :style="{ marginBottom: '24px' }">
        <chart-card-two :loading="loading" title="混凝土拌合站(盘)" :total="hntsj.hntsum">
          <a-tooltip title="混凝土统计说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <TrendRed flag="down" style="margin-right: 16px">
              <span slot="term">超标率</span>
              {{ hntsj.hntcblv }}%
            </TrendRed>
            <trend flag="up">
              <span slot="term">合格率</span>
              {{ hntsj.hnthglv }}%
            </trend>
          </div>
          <template slot="btn">
            <a-button type="primary" size="small" @click="changeTime(1)">本年</a-button>
            <a-button type="primary" size="small" @click="changeTime(2)">本月</a-button>
            <a-button type="primary" size="small" @click="changeTime(3)">累计</a-button>
          </template>
          <template slot="footer"
            >本月超标率<span class="errors"> {{ hntsj.hntycblv }} %</span></template
          >
          <template slot="footer"
            >本月闭合率<span class="errors"> {{ Number(hntsj.hntbhlvY).toFixed(2) }} %</span></template
          >
        </chart-card-two>
      </a-col>
      <a-col :sm="24" :md="12" :xl="8" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="沥青拌合站(盘)" :total="lqsj.lqsum">
          <a-tooltip title="沥青统计说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-area />
          </div>
          <template slot="footer"
            >本月超标率<span class="errors"> {{ lqsj.lqycblv }} %</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="8" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="水稳拌合站(盘)" :total="swsj.swsum">
          <a-tooltip title="水稳统计说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-bar :height="40" />
          </div>
          <template slot="footer"
            >本月超标率 <span class="errors">{{ swsj.swycblv }} %</span></template
          >
        </chart-card>
      </a-col>
      <!--      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">-->
      <!--        <chart-card :loading="loading" title="试验室" :total="syssj.syssum">-->
      <!--          <a-tooltip title="试验室统计说明" slot="action">-->
      <!--            <a-icon type="info-circle-o" />-->
      <!--          </a-tooltip>-->
      <!--          <div>-->
      <!--            <TrendRed flag="down" style="margin-right: 16px;">-->
      <!--              <span slot="term">超标率</span>-->
      <!--              {{ syssj.syscblv }}%-->
      <!--            </TrendRed>-->
      <!--            <trend flag="up">-->
      <!--              <span slot="term">合格率</span>-->
      <!--              {{ syssj.syshglv }}%-->
      <!--            </trend>-->
      <!--            &lt;!&ndash;            <mini-progress color="rgb(19, 194, 194)" :target="80" :percentage="100" :height="8" />&ndash;&gt;-->
      <!--          </div>-->
      <!--          <template slot="footer">本月超标率<span class="errors"> {{ syssj.sysycblv }} %</span>-->
      <!--          </template>-->
      <!--          <template slot="footer">本月闭合率<span class="errors"> {{ syssj.sysbhlvY }} %</span></template>-->
      <!--        </chart-card>-->
      <!--      </a-col>-->
    </a-row>

    <a-card :loading="loading" :bordered="false" :body-style="{ padding: '0' }">
      <div class="salesCard">
        <a-tabs
          default-active-key="1"
          size="large"
          :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }"
          @change="callback"
        >
          <div class="extra-wrapper" slot="tabBarExtraContent">
            <!--            <div class="extra-item">-->
            <!--              <a>本年每月数据统计</a>-->
            <!--            </div>-->
            <a-range-picker :style="{ width: '256px' }" @change="onChange" />
          </div>
          <a-tab-pane loading="true" tab="砼拌合站" key="1">
            <!--            <span slot="tab" style="color: #f47920">-->
            <!--              砼拌合站-->
            <!--            </span>-->
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar title="砼拌合站统计" :dataSource="barData" />
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <indexhntPie title="饼图" :height="height" :dataSource="pieData" />
              </a-col>
            </a-row>
          </a-tab-pane>
          <a-tab-pane tab="沥青拌合站" key="2">
            <!--            <span slot="tab" style="color: #f47920">-->
            <!--              沥青拌合站-->
            <!--            </span>-->
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar title="沥青拌合站统计" :dataSource="barData1" />
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <pie title="饼图" :height="height" :dataSource="pieData1" />
              </a-col>
            </a-row>
          </a-tab-pane>
          <a-tab-pane tab="水稳拌合站" key="3">
            <!--             <span slot="tab" style="color: #f47920">-->
            <!--              水稳拌合站-->
            <!--            </span>-->
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar title="水稳拌合站统计" :dataSource="barData2" />
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <pie title="饼图" :height="height" :dataSource="pieData2" />
              </a-col>
            </a-row>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-card>

    <a-row>
      <a-col :span="24">
        <a-card
          :loading="loading"
          :bordered="false"
          :headStyle="{ color: '#2a5caa' }"
          :body-style="{ padding: '5' }"
          :style="{ marginTop: '24px' }"
        >
          <!--            <a-statistic-countdown title="实际正在开发中。。。。下面是演示事例" :value="deadline" format="D 天 H 时 m 分 s 秒" />-->
          <a-tabs
            v-model="indexBottomTab"
            size="large"
            :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }"
          >
            <a-tab-pane loading="true" tab="分部分项工程进度" key="1">
              <a-table
                :dataSource="dataSource1"
                size="default"
                rowKey="id"
                :columns="columns"
                :pagination="ipagination1"
                @change="tableChange1"
              >
                <template slot="flowRate" slot-scope="text, record">
                  <a-progress
                    :strokeColor="getPercentColor(record.bfb)"
                    :format="getPercentFormat"
                    :percent="getFlowRateNumber(record.bfb)"
                    style="width: 80px"
                  />
                </template>
                <span slot="projectname" slot-scope="projectname, record">
                  <a-tag color="orange">{{ record.projectname }}</a-tag>
                </span>
                <span slot="pour" slot-scope="pour, record">
                  <a-tag color="red">{{ record.pour }}</a-tag>
                </span>
                <span slot="betlev" slot-scope="betlev, record">
                  <a-tag color="green">{{ record.betlev }}</a-tag>
                </span>
                <span slot="lands" slot-scope="lands, record">
                  <a-tag color="green">{{ record.lands }}</a-tag>
                </span>
              </a-table>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import ChartCard from "@/components/ChartCard";
import ChartCardTwo from "@/components/ChartCardTwo";
import ACol from "ant-design-vue/es/grid/Col";
import ATooltip from "ant-design-vue/es/tooltip/Tooltip";
import MiniArea from "@/components/chart/MiniArea";
import MiniBar from "@/components/chart/MiniBar";
import MiniProgress from "@/components/chart/MiniProgress";
import RankList from "@/components/chart/RankList";
import Bar from "@/components/chart/Bar";
import Pie from "@/components/chart/Pie";
import indexhntPie from "@comp/chart/indexhntPie";
import LineChartMultid from "@/components/chart/LineChartMultid";
import HeadInfo from "@/components/tools/HeadInfo.vue";
import { getAction } from "@api/manage";
import Trend from "@/components/Trend";
import TrendRed from "@comp/Trend/TrendRed";
import { getLoginfo, getVisitInfo } from "@/api/api";
import { SAFETY_PARTIAL_BASIC } from "@/store/mutation-types";
import Vue from "vue";
const rankList = [];
for (let i = 0; i < 7; i++) {
  rankList.push({
    name: "白鹭岛 " + (i + 1) + " 号店",
    total: 1234.56 - i * 100,
  });
}
// const barData = []
// const barData1 = []
// const barData2 = []
// const  pieData=[]
// for (let i = 0; i < 12; i += 1) {
//   barData.push({
//     x: `${i + 1}月`,
//     y: Math.floor(Math.random() * 1000) + 200
//   })
// }
const dataCol1 = [
  {
    title: "项目名称",
    align: "center",
    dataIndex: "projectname",
    scopedSlots: { customRender: "projectname" },
  },
  {
    title: "施工部位",
    align: "center",
    dataIndex: "conspos",
  },
  {
    title: "浇筑方式",
    align: "center",
    dataIndex: "pour",
    scopedSlots: { customRender: "pour" },
  },
  {
    title: "开始生产时间",
    align: "center",
    dataIndex: "starttim",
  },
  {
    title: "结束生产时间",
    align: "center",
    dataIndex: "endtim",
  },
  {
    title: "强度等级",
    align: "center",
    dataIndex: "betlev",
    scopedSlots: { customRender: "betlev" },
  },
  {
    title: "坍落度",
    align: "center",
    dataIndex: "lands",
    scopedSlots: { customRender: "lands" },
  },
  {
    title: "任务方量(m³)",
    align: "center",
    dataIndex: "mete",
  },
  {
    title: "生产方量(m³)",
    align: "center",
    dataIndex: "metes",
  },
  {
    title: "进度(%)",
    align: "center",
    dataIndex: "bfb",
    scopedSlots: { customRender: "flowRate" },
  },
];
export default {
  name: "IndexChart",
  components: {
    ATooltip,
    ACol,
    ChartCard,
    ChartCardTwo,
    MiniArea,
    MiniBar,
    MiniProgress,
    RankList,
    Bar,
    Trend,
    LineChartMultid,
    HeadInfo,
    Pie,
    indexhntPie,
    TrendRed,
  },
  data() {
    return {
      deadline: Date.now() + 8000 * 60 * 60 * 24 * 2 + 1000 * 30,
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
      indexRegisterType: "转移登记",
      indexBottomTab: "1",
      dataSource1: [],
      dataSource2: [],
      columns: dataCol1,
      //columns2:dataCol2,
      url: {
        renwujindu: "/renwudan/renwudanSchedule/listHome",
        hnttj: "/hntbhz/bhzCementBase/TongjiData2",
        hnttj1: "/hntbhz/bhzCementBase/listMid",
        lqtj: "/lqbhz/bhzLqBases/list4",
        lqtj1: "/lqbhz/bhzLqBases/list6",
        swtj: "/swbhz/bhzSwBases/list4",
        swtj1: "/swbhz/bhzSwBases/list6",
        systj: "/syj/tSyjzb/list5",
      },
      type: 1,
      hntsj: {
        hntsum: 0,
        // hntcb: 0,
        hntcblv: 0,
        hnthglv: 0,
        hntycblv: 0,
        hntbhlvY: 0,
      },
      lqsj: {
        lqsum: 0,
        lqcb: 0,
        lqcblv: 0,
        lqhglv: 0,
        lqycblv: 0,
      },
      swsj: {
        swsum: 0,
        swcb: 0,
        swcblv: 0,
        swhglv: 0,
        swycblv: 0,
      },
      syssj: {
        syssum: 0,
        syscb: 0,
        syscblv: 0,
        syshglv: 0,
        sysycblv: 0,
        sysbhlvY: 0,
      },
      loading: true,
      center: null,
      rankList,
      height: 340,
      Starttime: null,
      Endtime: null,
      barData: [],
      barData1: [],
      barData2: [],
      tabskey: 0,
      pieData: [],
      pieData1: [],
      pieData2: [],
      loginfo: {},
      sys_depart_project: "",
      sys_depart_orgcode: "",
      visitFields: ["ip", "visit"],
      visitInfo: [],
      // indicator: <a-icon type="loading" style="font-size: 24px" spin />
    };
  },
  created() {
    //this.loadDataSource1()
    //this.loadDataSource2()
    // setTimeout(() => {
    //   this.loading = !this.loading;
    // }, 1000);
    // this.initLogInfo();
    this.sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
    this.hnttj();
    this.lqtj();
    this.swtj();
    this.systj();
    this.hnttj1();
    this.lqtj1();
    this.swtj1();
    this.projectjindu();
  },
  methods: {
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
    loadDataSource1() {
      this.dataSource1 = dataSource1.filter((item) => {
        if (!this.indexRegisterType) {
          return true;
        }
        return item.type == this.indexRegisterType;
      });
    },
    loadDataSource2() {
      this.dataSource2 = dataSource2.filter((item) => {
        if (!this.indexRegisterType) {
          return true;
        }
        return item.type == this.indexRegisterType;
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
    callback(key) {
      //获取tabs的标签key
      this.tabskey = key;
    },
    hnttj() {
      this.loading = true
      //混凝土统计数据
      getAction(this.url.hnttj, { type: this.type }).then((res) => {
        if (res.success) {
          this.hntsj.hntycblv = res.result.hntycblv
          this.hntsj.hntbhlvY = res.result.hntbhlvY
          this.hntsj.hntsum = res.result.hntsum
          this.hntsj.hntcblv = Number(res.result.hntcblv).toFixed(2);
          this.hntsj.hnthglv = Number(100 - res.result.hntcblv).toFixed(2);
        }
      })
      .finally(() => {
        this.loading = false;
      });
    },
    projectjindu: function () {
      //任务单进度统计
      this.sys_depart_project = Vue.ls.get("SAFETY_PARTIAL_BASIC");
      let param = {
        sys_depart_project: this.sys_depart_project,
        column: "id",
        order: "desc",
        type: this.type
      };
      getAction(this.url.renwujindu, param).then((res) => {
        this.dataSource1 = [];
        if (res.success) {
          this.dataSource1 = res.result.records;
        }
      });
    },
    hnttj1(Starttime, Endtime) {
      //混凝土统计数据中间部分统计数据
      let params = { statisticsTime_begin: Starttime, statisticsTime_end: Endtime, type: this.type };
      getAction(this.url.hnttj1, params).then((res) => {
        if (res.success) {
          let data = res.result;
          this.barData = [];
          this.pieData = [];
          for (let i = 0; i < data.length; i++) {
            this.barData.push({ x: data[i].statisticsTime + "月", y: data[i].allDish });
            this.pieData.push({
              item: data[i].statisticsTime + "月",
              count: data[i].allDish,
            });
          }
        }
      });
    },
    onChange(date, dateString) {
      //根据时间重新去统计月数据
      this.Starttime = dateString[0];
      this.Endtime = dateString[1];
      this.hnttj1(this.Starttime, this.Endtime); //混凝土
      this.swtj1(this.Starttime, this.Endtime); //沥青
      this.lqtj1(this.Starttime, this.Endtime); //水稳
    },
    lqtj: function () {
      //沥青统计数据
      getAction(this.url.lqtj, {}).then((res) => {
        if (res.success) {
          this.lqsj.lqcb = res.result.lqcb;
          // this.lqsj.lqsum = res.result.lqsum;
          this.lqsj.lqcblv = res.result.lqcblv;
          this.lqsj.lqycblv = Number(res.result.lqycblv).toFixed(1);
        }
      });
      getAction("/hntbhz/bhzCementBase/bhzWarnbycode", {
        bhzType: "lqbhz",
        orgCode: this.sys_depart_orgcode,
      }).then((res) => {
        if (res.success) {
          this.lqsj.lqsum = res.result[0].allDish;
        }
      });
    },
    lqtj1: function (Starttime, Endtime) {
      //沥青统计数据中间部分统计数据
      let params = { statisticsTime_begin: Starttime, statisticsTime_end: Endtime };
      getAction(this.url.lqtj1, params).then((res) => {
        if (res.success) {
          let data = res.result;
          this.barData1 = [];
          this.pieData1 = [];
          for (let i = 0; i < data.length; i++) {
            this.barData1.push({ x: data[i].statisticsTime + "月", y: data[i].allDish });
            this.pieData1.push({
              item: data[i].statisticsTime + "月",
              count: data[i].allDish,
            });
          }
        }
      });
    },
    swtj: function () {
      getAction(this.url.swtj, {}).then((res) => {
        if (res.success) {
          this.swsj.swcb = res.result.swcb;
          // this.swsj.swsum = res.result.swsum;
          this.swsj.swcblv = res.result.swcblv;
          this.swsj.swycblv = Number(res.result.swycblv).toFixed(1);
        }
      });
      getAction("/hntbhz/bhzCementBase/bhzWarnbycode", {
        bhzType: "swbhz",
        orgCode: this.sys_depart_orgcode,
      }).then((res) => {
        if (res.success) {
          this.swsj.swsum = res.result[0].allDish;
        }
      });
    },
    swtj1: function (Starttime, Endtime) {
      //水稳统计数据中间部分统计数据
      let params = { statisticsTime_begin: Starttime, statisticsTime_end: Endtime };
      getAction(this.url.swtj1, params).then((res) => {
        if (res.success) {
          let data = res.result;
          this.barData2 = [];
          this.pieData2 = [];
          for (let i = 0; i < data.length; i++) {
            this.barData2.push({ x: data[i].statisticsTime + "月", y: data[i].allDish });
            this.pieData2.push({
              item: data[i].statisticsTime + "月",
              count: data[i].allDish,
            });
          }
        }
      });
    },
    systj: function () {
      getAction(this.url.systj, {}).then((res) => {
        if (res.success) {
          this.syssj.syscb = res.result.syjcb;
          this.syssj.syssum = res.result.syjsum;
          this.syssj.syscblv = Number(res.result.syjcblv).toFixed(1);
          this.syssj.syshglv = Number(100 - res.result.syjcblv).toFixed(1);
          this.syssj.sysycblv = Number(res.result.syjycblv).toFixed(1);
          this.syssj.sysbhlvY = Number(res.result.sysbhlvY).toFixed(1);
        }
      });
    },
    initLogInfo() {
      getLoginfo(null).then((res) => {
        if (res.success) {
          Object.keys(res.result).forEach((key) => {
            res.result[key] = res.result[key] + "";
          });
          this.loginfo = res.result;
        }
      });
      getVisitInfo().then((res) => {
        if (res.success) {
          this.visitInfo = res.result;
        }
      });
    },
    changeTime(type){
      this.type = type
      this.hnttj()//混凝土统计数据
      this.hnttj1()//混凝土统计数据中间部分统计数据
      this.projectjindu();//分部分项工程进度
    }
  },
};
</script>

<style lang="less" scoped>
.errors {
  color: red;
  font-size: 18px;
  padding-right: 20px;
}

.successs {
  color: #52c41a;
}

.circle-cust {
  position: relative;
  top: 28px;
  left: -100%;
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

/* 首页访问量统计 */
.head-info {
  position: relative;
  text-align: left;
  padding: 0 32px 0 0;
  min-width: 250px;

  &.center {
    text-align: center;
    padding: 0 32px;
  }

  span {
    color: rgba(0, 0, 0, 0.45);
    display: inline-block;
    font-size: 0.95rem;
    line-height: 42px;
    margin-bottom: 4px;
  }

  p {
    line-height: 42px;
    margin: 0;

    a {
      font-weight: 600;
      font-size: 1rem;
    }
  }
}
</style>
