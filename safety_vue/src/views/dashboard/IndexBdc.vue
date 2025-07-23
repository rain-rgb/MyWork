<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card
          :loading="loading"
          title="制梁任务单(总)"
          :total="renwudanhander.renwudanzong"
        >
          <a-tooltip title="制梁任务单总量说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <template slot="footer"
            >本月任务单量：<span class="red">{{
              renwudanhander.renwudanbenyue
            }}</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card
          :loading="loading"
          title="已审核量(总)"
          :total="renwudanhander.renwudanysh"
        >
          <a-tooltip title="已审核量说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <template slot="footer"
            >本月已审核量：<span class="red">{{
              renwudanhander.renwudanyshbenyue
            }}</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card
          :loading="loading"
          title="未审核量(总)"
          :total="renwudanhander.renwudanweishenhezong"
        >
          <a-tooltip title="未审核量说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <template slot="footer"
            >本月未审核量：<span class="red">{{
              renwudanhander.renwudanweishenhebenyue
            }}</span>
          </template>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card
          :loading="loading"
          title="完成量(总)"
          :total="renwudanhander.renwudanwc"
        >
          <a-tooltip title="完成量说明" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <template slot="footer"
            >本月完成量：<span class="red">{{
              renwudanhander.renwudanwcbenyue
            }}</span></template
          >
        </chart-card>
      </a-col>
    </a-row>

    <a-card :loading="loading" :bordered="false" :body-style="{ padding: '0' }">
      <div class="salesCard">
        <a-tabs
          default-active-key="1"
          size="large"
          :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }"
        >
          <div class="extra-wrapper" slot="tabBarExtraContent">
            <a-range-picker :style="{ width: '256px' }" @change="onChange" />
          </div>

          <a-tab-pane loading="true" tab="生产监管" key="1">
            <a-row>
              <a-col :xl="20" :lg="12" :md="12" :sm="24" :xs="24">
                <index-bar title="生产量统计" :dataSource="barData" />
              </a-col>
              <a-col :xl="4" :lg="12" :md="12" :sm="24" :xs="24">
                <a-card
                  title="快速开始 / 便捷导航"
                  :bordered="false"
                  :body-style="{ padding: 0 }"
                >
                  <div class="item-group">
                    <a-row>
                      <a-col :class="'more-btn'" :span="24">
                        <a-button
                          @click="goPage"
                          style="margin-bottom: 10px"
                          size="small"
                          type="primary"
                          ghost
                        >
                          任务单查询
                        </a-button>
                        <a-button
                          v-has="'zlrenwudan:sh'"
                          @click="goPage1"
                          style="margin-bottom: 10px; margin-left: 10px"
                          size="small"
                          type="primary"
                          ghost
                        >
                          任务单审核
                        </a-button>
                      </a-col>
                    </a-row>
                  </div>
                </a-card>
              </a-col>
            </a-row>
          </a-tab-pane>

          <a-tab-pane tab="存梁区" key="2">
            <a-carousel autoplay>
              <a-row
                type="flex"
                justify="center"
                :xl="24"
                :lg="12"
                :md="12"
                :sm="24"
                :xs="24"
                v-for="(items, index) in zhonglist"
                :key="index"
              >
                <a-col
                  class="height-200"
                  :span="4"
                  v-for="(item, index) in items"
                  :key="index"
                >
                  <div class="liangzong">
                    <div
                      class="liangzong1"
                      v-if="item.zhiliangGongxuList.length == 1"
                      v-for="(items, indexs) in item.zhiliangGongxuList"
                      :key="indexs"
                    >
                      <div class="liangone">
                        <div v-show="items.liangceng == 2">
                          <div class="liangchang">
                            {{ items.taizuoname }}
                          </div>
                          <div class="lianghao">
                            {{ items.remark
                            }}<span class="liangceng">{{ items.liangceng }} 层</span>
                          </div>
                        </div>
                      </div>
                      <div class="liangone">
                        <div v-show="items.liangceng == 1">
                          <div class="liangchang">
                            {{ items.taizuoname }}
                          </div>
                          <div class="lianghao">
                            {{ items.remark
                            }}<span class="liangceng">{{ items.liangceng }} 层</span>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="liangzong1" v-if="item.zhiliangGongxuList.length == 2">
                      <div
                        style="height: 100%"
                        v-if="item.zhiliangGongxuList[0].liangceng == 2"
                      >
                        <div class="liangone">
                          <div class="liangchang">
                            {{ item.zhiliangGongxuList[0].taizuoname }}
                          </div>
                          <div class="lianghao">
                            {{ item.zhiliangGongxuList[0].remark
                            }}<span class="liangceng"
                              >{{ item.zhiliangGongxuList[0].liangceng }} 层</span
                            >
                          </div>
                        </div>
                        <div class="liangone">
                          <div class="liangchang">
                            {{ item.zhiliangGongxuList[1].taizuoname }}
                          </div>
                          <div class="lianghao">
                            {{ item.zhiliangGongxuList[1].remark
                            }}<span class="liangceng"
                              >{{ item.zhiliangGongxuList[1].liangceng }} 层</span
                            >
                          </div>
                        </div>
                      </div>
                      <div style="height: 100%" v-else>
                        <div class="liangone">
                          <div class="liangchang">
                            {{ item.zhiliangGongxuList[1].taizuoname }}
                          </div>
                          <div class="lianghao">
                            {{ item.zhiliangGongxuList[1].remark
                            }}<span class="liangceng"
                              >{{ item.zhiliangGongxuList[1].liangceng }} 层</span
                            >
                          </div>
                        </div>
                        <div class="liangone">
                          <div class="liangchang">
                            {{ item.zhiliangGongxuList[0].taizuoname }}
                          </div>
                          <div class="lianghao">
                            {{ item.zhiliangGongxuList[0].remark
                            }}<span class="liangceng"
                              >{{ item.zhiliangGongxuList[0].liangceng }} 层</span
                            >
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <p class="liangzuo">{{ item.liangzuoname }} 存梁台座</p>
                </a-col>
              </a-row>
            </a-carousel>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-card>

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
              :customRow="handleClick"
              :rowClassName="setRowClassName"
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
  },
  methods: {
    liangxinxitaizuo: function () {
      let param = { sta: 1, sta1: 1 };
      this.zhonglist = [];
      getAction(this.url.liangxinxi, param).then((res) => {
        console.log(res, "存梁区信息");
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
          console.log(res);
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
  },
};
</script>
<style lang="less" scoped>
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
</style>
