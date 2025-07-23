<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="合格(盘)" :total="lqsta.lqhege">
          <a-tooltip title="沥青拌合站合格盘数" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <trend flag="up">
            <span slot="term">合格率</span>
            {{ lqsta.hegelv }}%
          </trend>
          <template slot="footer"
            >本月合格率<span class="errors"> {{ lqstas.hegelvs }} %</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="高级超标(盘)" :total="lqsta.lqadvance">
          <a-tooltip title="沥青拌合站高级超标盘数" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <TrendRed flag="down">
            <span slot="term">高级超标率</span>
            {{ lqsta.advancedlv }}%
          </TrendRed>
          <template slot="footer"
            >本月高级超标率<span class="errors">
              {{ lqstas.advancedlvs }} %</span
            ></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="中级超标(盘)" :total="lqsta.lqmiddle">
          <a-tooltip title="沥青拌合站中级超标盘数" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <TrendRed flag="down">
            <span slot="term">中级超标率</span>
            {{ lqsta.middlelv }}%
          </TrendRed>
          <template slot="footer"
            >本月中级超标率<span class="errors"> {{ lqstas.middlelvs }} %</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="初级超标(盘)" :total="lqsta.lqprimary">
          <a-tooltip title="沥青拌合站初级超标盘数" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <TrendRed flag="down">
            <span slot="term">初级超标率</span>
            {{ lqsta.primarylv }}%
          </TrendRed>
          <template slot="footer"
            >本月初级超标率<span class="errors">
              {{ lqstas.primarylvs }} %</span
            ></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="超标已处置(盘)" :total="lqsta.lqhandle">
          <a-tooltip title="沥青拌合站超标已处置盘数" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <trend flag="up">
            <span slot="term">超标已处置率</span>
            {{ lqsta.handlelv }}%
          </trend>
          <template slot="footer"
            >本月超标已处置率<span class="errors">
              {{ lqstas.handlelvs }} %</span
            ></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="超标未处置(盘)" :total="lqsta.lqnohandle">
          <a-tooltip title="沥青拌合站超标未处置盘数" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <TrendRed flag="down">
            <span slot="term">超标未处置率</span>
            {{ lqsta.nohandlelv }}%
          </TrendRed>
          <template slot="footer"
            >本月超标未处置率<span class="errors">
              {{ lqstas.nohandlelvs }} %</span
            ></template
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
          <a-tab-pane loading="true" tab="产能分析" key="1">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar-and-multid-line
                  title="沥青拌合站产能统计"
                  :height="height"
                  :dataSource="dataSource1"
                />
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <bhz-sta-pie
                  title="合格率统计"
                  :height="height"
                  :dataSource="dataSource3"
                />
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
          <a-tabs
            size="large"
            :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }"
          >
            <a-tab-pane loading="true" tab="关注问题" key="1">
              <a-table
                :dataSource="dataSource"
                size="default"
                rowKey="id"
                :columns="columns"
                :pagination="ipagination"
                @change="tableChange"
                :customRow="handleClick"
                :rowClassName="setRowClassName"
              >
                <span slot="chaobiaodengji" slot-scope="chaobiaodengji, record">
                  <a-tag color="orange" v-if="record.chaobiaodengji === 1"
                    >初级超标</a-tag
                  >
                  <a-tag color="yellow" v-if="record.chaobiaodengji === 2"
                    >中级超标</a-tag
                  >
                  <a-tag color="red" v-if="record.chaobiaodengji === 3">高级超标</a-tag>
                </span>
                <span slot="overproofStatus" slot-scope="overproofStatus, record">
                  <a-tag color="blue" v-if="record.overproofStatus === 0">未处置</a-tag>
                  <a-tag color="orange" v-if="record.overproofStatus === 10"
                    >未审核</a-tag
                  >
                </span>
                <span slot="action" slot-scope="text, record">
                  <a @click="showmadel1(record)">处置</a>
                  <a-divider type="vertical" />
                  <a @click="showmadel(record)">审核</a>
                </span>
              </a-table>
              <shen-he
                :flag="flag"
                :bhz="bhz"
                :hntbatch="guid"
                @change="change"
              ></shen-he>
              <chu-zhi
                :flag="flag"
                :bhz="bhz"
                :hntbatch="guid"
                @change="change2"
              ></chu-zhi>
            </a-tab-pane>
            <a-tab-pane loading="true" tab="生产信息统计" key="2">
              <a-table
                :dataSource="dataSource2"
                size="default"
                rowKey="key"
                :columns="columns1"
                :customRow="handleClick"
                :rowClassName="setRowClassName"
              >
                <span slot="chaobiaodengji" slot-scope="chaobiaodengji, record">
                  <a-tag color="orange" v-if="record.chaobiaodengji === 1"
                    >初级超标</a-tag
                  >
                  <a-tag color="yellow" v-if="record.chaobiaodengji === 2"
                    >中级超标</a-tag
                  >
                  <a-tag color="red" v-if="record.chaobiaodengji === 3">高级超标</a-tag>
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
import ACol from "ant-design-vue/es/grid/Col";
import ATooltip from "ant-design-vue/es/tooltip/Tooltip";
import MiniProgress from "@/components/chart/MiniProgress";
import LineChartMultid from "@/components/chart/LineChartMultid";
import HeadInfo from "@/components/tools/HeadInfo.vue";
import { getAction } from "@api/manage";
import Trend from "@/components/Trend";
import TrendRed from "@comp/Trend/TrendRed";
import BarAndMultidLine from "@comp/chart/BarAndMultidLine";
import ChuZhi from "@views/bhz/czsh/ChuZhi";
import ShenHe from "@views/bhz/czsh/ShenHe";
import BhzStaPie from "@comp/chart/BhzStaPie";
export default {
  name: "lqbhzIndexChart",
  components: {
    BhzStaPie,
    ATooltip,
    ACol,
    ChartCard,
    MiniProgress,
    Trend,
    LineChartMultid,
    HeadInfo,
    TrendRed,
    BarAndMultidLine,
    ChuZhi,
    ShenHe,
  },
  data() {
    return {
      guid: "",
      flag: 0,
      bhz: 0,
      lqsta: {
        lqhege: 0,
        hegelv: 0,
        lqadvance: 0,
        advancedlv: 0,
        lqmiddle: 0,
        middlelv: 0,
        lqprimary: 0,
        primarylv: 0,
        lqhandle: 0,
        handlelv: 0,
        lqnohandle: 0,
        nohandlelv: 0,
      },
      lqstas: {
        hegelvs: 0,
        advancedlvs: 0,
        middlelvs: 0,
        primarylvs: 0,
        handlelvs: 0,
        nohandlelvs: 0,
      },
      ipagination: {
        current: 1,
        pageSize: 5,
        pageSizeOptions: ["5", "10", "20", "30"],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      dataSource: [],
      dataSource3: [],
      dataSource1: [],
      columns: [
        {
          title: "设备名称",
          align: "center",
          dataIndex: "shebeibianhao_dictText",
        },
        {
          title: "工程名称",
          align: "center",
          dataIndex: "projectName",
        },
        {
          title: "混合料类型",
          align: "center",
          dataIndex: "hunheliaoid_dictText",
        },
        {
          title: "代办内容",
          align: "center",
          dataIndex: "chaobiaodengji",
          scopedSlots: { customRender: "chaobiaodengji" },
        },
        {
          title: "沥青温度",
          align: "center",
          dataIndex: "liqingwd",
        },
        {
          title: "骨料温度",
          align: "center",
          dataIndex: "guliaowd",
        },
        {
          title: "出料温度",
          align: "center",
          dataIndex: "chuliaowd",
          customRender: function (text) {
            return parseInt(text);
          },
        },
        {
          title: "总产量",
          align: "center",
          dataIndex: "zongchanliang",
          customRender: function (text) {
            return parseInt(text);
          },
        },
        {
          title: "时间",
          align: "center",
          dataIndex: "chuliaoshijian",
        },
        {
          title: "状态",
          align: "center",
          dataIndex: "overproofStatus",
          scopedSlots: { customRender: "overproofStatus" },
        },
        {
          title: "操作",
          dataIndex: "action",
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: { customRender: "action" },
        },
      ],
      dataSource2: [],
      columns1: [
        {
          title: "超标等级",
          align: "center",
          dataIndex: "chaobiaodengji",
          scopedSlots: { customRender: "chaobiaodengji" },
        },
        {
          title: "数量(盘)",
          align: "center",
          dataIndex: "dish",
        },
        {
          title: "比率(%)",
          align: "center",
          dataIndex: "dishlv",
        },
        {
          title: "处置数(盘)",
          align: "center",
          dataIndex: "handle",
        },
        {
          title: "处置率(%)",
          align: "center",
          dataIndex: "handlelv",
        },
      ],
      url: {
        list: "/lqbhz/bhzLqBases/list999",
        list1: "/lqbhz/bhzLqBases/list11",
        list2: "/lqbhz/bhzLqBases/list12",
        list3: "/lqbhz/bhzLqBases/list13",
      },

      loading: true,
      center: null,
      height: 340,
      Starttime: null,
      Endtime: null,
      sys_depart_project: "",
    };
  },
  created() {
    setTimeout(() => {
      this.loading = !this.loading;
    }, 1000);
    this.getData(); //盘数统计
    this.getDatas(); //产能统计
    this.getLqData(); //沥青未处置信息
    this.getLqDatas(); //本月超标率合格率等统计
    this.getlqstaData(); //超标率饼图
  },
  methods: {
    showmadel1: function (record) {
      this.guid = record.guid;
      this.flag = 2;
      this.bhz = 1;
    },
    showmadel: function (record) {
      if (record.overproofStatus === 10) {
        this.guid = record.guid;
        this.flag = 1;
        this.bhz = 1;
      } else {
        this.$message.warning("请先处置！");
      }
    },
    tableChange(pagination) {
      this.ipagination.current = pagination.current;
      this.ipagination.pageSize = pagination.pageSize;
      this.getLqData();
    },
    getData: function () {
      this.lqsta = {};
      this.dataSource2 = [];
      getAction(this.url.list, {}).then((res) => {
        if (res.success) {
          this.lqsta = res.result;
          this.dataSource2.push(
            {
              key: 1,
              chaobiaodengji: 1,
              dish: res.result.lqprimary,
              dishlv: res.result.primarylv,
              handle: res.result.primaryHandle,
              handlelv: res.result.primaryHandlelv,
            },
            {
              key: 2,
              chaobiaodengji: 2,
              dish: res.result.lqmiddle,
              dishlv: res.result.middlelv,
              handle: res.result.middleHandle,
              handlelv: res.result.middleHandlelv,
            },
            {
              key: 3,
              chaobiaodengji: 3,
              dish: res.result.lqadvance,
              dishlv: res.result.advancedlv,
              handle: res.result.advanceHandle,
              handlelv: res.result.advanceHandlelv,
            }
          );
          //console.log("this.dataSource2",this.dataSource2)
          //console.log("res.result.lqhandle",res.result.lqhandle)
        }
      });
    },
    getLqData: function () {
      //沥青未处置信息
      this.dataSource = [];
      let param = {
        column: "id",
        order: "desc",
        pageSize: this.ipagination.pageSize,
        pageNo: this.ipagination.current,
      };
      console.log(param);
      getAction(this.url.list2, param).then((res) => {
        if (res.success) {
          this.dataSource = res.result.records;
          this.ipagination.total = res.result.total;
        }
      });
    },
    getLqDatas: function () {
      //本月超标率合格率等统计
      this.lqstas = {};
      this.dataSource3 = [];
      getAction(this.url.list3, {}).then((res) => {
        if (res.success) {
          this.lqstas = res.result;
          console.log("this.lqstas", this.lqstas);
        }
      });
    },
    change(data) {
      this.flag = data;
    },
    change2(data) {
      this.flag = data;
    },
    getlqstaData: function (Starttime, Endtime) {
      this.dataSource3 = [];
      let params = { statisticsTime_begin: Starttime, statisticsTime_end: Endtime };
      getAction(this.url.list3, params).then((res) => {
        if (res.success) {
          let data = res.result;
          if (
            data.lqprimarys !== 0 ||
            data.lqmiddles !== 0 ||
            data.lqadvances !== 0 ||
            data.lqheges !== 0
          ) {
            this.dataSource3.push(
              { item: "初级超标", count: data.lqprimarys },
              { item: "中级超标", count: data.lqmiddles },
              { item: "高级超标", count: data.lqadvances },
              { item: "合格", count: data.lqheges }
            );
          }
        }
      });
    },
    onChange(date, dateString) {
      //根据时间重新去统计月数据
      this.Starttime = dateString[0];
      this.Endtime = dateString[1];
      this.getDatas(this.Starttime, this.Endtime);
      this.getlqstaData(this.Starttime, this.Endtime);
    },
    getDatas: function (Starttime, Endtime) {
      //中间部分产能统计数据
      this.dataSource1 = [];
      let params = { statisticsTime_begin: Starttime, statisticsTime_end: Endtime };
      getAction(this.url.list1, params).then((res) => {
        if (res.success) {
          let data = res.result;
          if (data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              this.dataSource1.push({
                type: data[i].statisticsTime + "月",
                jeecg: data[i].advancedlv,
                jeebt: data[i].middlelv,
                jeebts: data[i].primarylv,
                jeecgs: data[i].hegelv,
                总方量: data[i].estimateNumber,
              });
            }
          }
          //console.log("this.dataSource1",this.dataSource1)
        }
      });
    },
  },
};
</script>

<style lang="less" scoped>
.errors {
  color: red;
  font-size: 18px;
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
