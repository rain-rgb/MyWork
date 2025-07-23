<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="合格(盘)" :total="titleDataSta.qualifiedCount">
          <a-tooltip title="砼拌合站合格盘数" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <trend flag="up">
            <span slot="term">合格率</span>
            {{ hegelv }}%
          </trend>
          <template slot="footer"
            >本月合格率<span class="errors"> {{ hegelv }}%</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="高级超标(盘)" :total="titleDataSta.advanceCount">
          <a-tooltip title="砼拌合站高级超标盘数" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <TrendRed flag="down">
            <span slot="term">高级超标率</span>
            {{ titleDataSta.advancedCent }}%
          </TrendRed>
          <template slot="footer"
            >本月高级超标率<span class="errors"> {{ titleDataSta.advancedCent }} %</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="中级超标(盘)" :total="titleDataSta.middleCount">
          <a-tooltip title="砼拌合站中级超标盘数" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <TrendRed flag="down">
            <span slot="term">中级超标率</span>
            {{ titleDataSta.middleCent }}%
          </TrendRed>
          <template slot="footer"
            >本月中级超标率<span class="errors"> {{ titleDataSta.middleCent }} %</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="初级超标(盘)" :total="titleDataSta.primaryCount">
          <a-tooltip title="砼拌合站初级超标盘数" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <TrendRed flag="down">
            <span slot="term">初级超标率</span>
            {{ titleDataSta.primaryCent }}%
          </TrendRed>
          <template slot="footer"
            >本月初级超标率<span class="errors"> {{ titleDataSta.primaryCent }} %</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <!--        <chart-card :loading="loading"   title="超标已处置(盘)" :total="titleDataSta.handleCent">-->
        <chart-card :loading="loading" title="超标已处置(盘)">
          <!--          <a-tooltip title="砼拌合站超标已处置盘数" slot="action">-->
          <!--            <a-icon type="info-circle-o" />-->
          <!--          </a-tooltip>-->
          <trend flag="up">
            <span slot="term">初级已处置</span>
            {{ chuzhixq.cyichuji }}
          </trend>
          <trend flag="up">
            <span slot="term">中级已处置</span>
            {{ chuzhixq.zyichuji }}
          </trend>
          <trend flag="up">
            <span slot="term">高级已处置</span>
            {{ chuzhixq.gyichuji }}
          </trend>
          <template slot="footer"
            >已处置盘数<span class="errors"> {{ chuzhixq.zongyichuji }}</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="超标未处置(盘)">
          <!--          <a-tooltip title="砼拌合站超标未处置盘数" slot="action">-->
          <!--            <a-icon type="info-circle-o" />-->
          <!--          </a-tooltip>-->
          <TrendRed flag="down">
            <span slot="term">初级未处置</span>
            {{ chuzhixq.nocyichuji }}
          </TrendRed>
          <TrendRed flag="down">
            <span slot="term">中级未处置</span>
            {{ chuzhixq.nozyichuji }}
          </TrendRed>
          <TrendRed flag="down">
            <span slot="term">高级未处置</span>
            {{ chuzhixq.nogyichuji }}
          </TrendRed>
          <template slot="footer"
            >未处置盘数<span class="errors"> {{ chuzhixq.nozongyichuji }}</span></template
          >
        </chart-card>
      </a-col>
    </a-row>

    <a-card :loading="loading" :bordered="false" :body-style="{ padding: '0' }">
      <div class="salesCard">
        <a-tabs default-active-key="1" size="large" :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }">
          <div class="extra-wrapper" slot="tabBarExtraContent">
            <a-range-picker :style="{ width: '256px' }" @change="onChange" />
          </div>
          <a-tab-pane loading="true" tab="产能分析" key="1">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar-and-multid-line title="砼拌合站产能统计" :height="height" :dataSource="dataSource1" />
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <bhz-sta-pie title="合格率统计" :height="height" :dataSource="dataSource3" />
              </a-col>
            </a-row>
          </a-tab-pane>
          <a-tab-pane loading="true" tab="标段产能" key="2">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar-and-multid-line title="砼拌合站标段产能统计" :height="height" :dataSource="dataSource4" />
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <bhz-sta-pie title="合格率统计" :height="height" :dataSource="dataSource3" />
              </a-col>
            </a-row>
          </a-tab-pane>
          <a-tab-pane loading="true" tab="设备产能" key="3">
            <a-row>
              <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
                <bar-and-multid-line title="砼拌合站设备产能统计" :height="height" :dataSource="dataSource6" />
              </a-col>
              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
                <bhz-sta-pie title="合格率统计" :height="height" :dataSource="dataSource3" />
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
          <a-tabs size="large" :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }">
            <a-tab-pane loading="true" tab="关注问题" key="1">
              <a-table
                :dataSource="dataSource"
                size="default"
                rowKey="id"
                :columns="columns"
                :pagination="ipagination"
                @change="tableChange"
              >
                <span slot="chaobiaodengji" slot-scope="chaobiaodengji, record">
                  <a-tag color="orange" v-if="record.chaobiaodengji === 1">初级超标</a-tag>
                  <a-tag color="yellow" v-if="record.chaobiaodengji === 2">中级超标</a-tag>
                  <a-tag color="red" v-if="record.chaobiaodengji === 3">高级超标</a-tag>
                </span>
                <span slot="overproofStatus" slot-scope="overproofStatus, record">
                  <a-tag color="blue" v-if="record.overproofStatus === 0">未处置</a-tag>
                  <a-tag color="orange" v-if="record.overproofStatus === 10">未审核</a-tag>
                </span>
                <span slot="action" slot-scope="text, record">
                  <a @click="showmadel1(record)">处置</a>
                  <a-divider type="vertical" />
                  <a @click="showmadel(record)">审核</a>
                </span>
              </a-table>
              <shen-he :flag="flag" :bhz="bhz" :hntbatch="guid" @change="change"></shen-he>
              <chu-zhi :flag="flag" :bhz="bhz" :hntbatch="guid" @change="change2"></chu-zhi>
            </a-tab-pane>
            <a-tab-pane loading="true" tab="生产信息统计" key="2">
              <a-table :dataSource="dataSource2" size="default" rowKey="key" :columns="columns1">
                <span slot="chaobiaodengji" slot-scope="chaobiaodengji, record">
                  <a-tag color="orange" v-if="record.chaobiaodengji === 1">初级超标</a-tag>
                  <a-tag color="yellow" v-if="record.chaobiaodengji === 2">中级超标</a-tag>
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
// import ChartCard from '@/components/ChartCard'
import ChartCard from '@/components/ChartCard1'
import ACol from 'ant-design-vue/es/grid/Col'
import ATooltip from 'ant-design-vue/es/tooltip/Tooltip'
import MiniProgress from '@/components/chart/MiniProgress'
import LineChartMultid from '@/components/chart/LineChartMultid'
import HeadInfo from '@/components/tools/HeadInfo.vue'
import { getAction } from '@api/manage'
import Trend from '@/components/Trend'
import TrendRed from '@comp/Trend/TrendRed'
import BarAndMultidLine from '@comp/chart/BarAndMultidLine'
import ChuZhi from '@views/bhz/czsh/ChuZhi'
import ShenHe from '@views/bhz/czsh/ShenHe'
import BhzStaPie from '@comp/chart/BhzStaPie'
import { handertoken } from '@/mixins/getHanderToken'
import Vue from 'vue'
export default {
  name: 'CMindex',
  mixins: [handertoken],
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
      guid: '',
      flag: 0,
      bhz: 0,
      titleDataSta: {},
      lqstas: {},
      hegelv: 0,
      nochuzhi: 0,
      nochuzhilv: 0,
      chuzhixq: {},
      ipagination: {
        current: 1,
        pageSize: 5,
        pageSizeOptions: ['5', '10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      dataSource: [],
      dataSource3: [],
      dataSource1: [],
      dataSource4: [],
      dataSource5: [],
      dataSource6: [],
      dataSource7: [],
      columns: [
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'shebeiNo_dictText',
        },
        {
          title: '代办内容',
          align: 'center',
          dataIndex: 'overLevel_dictText',
          scopedSlots: { customRender: 'overLevel_dictText' },
        },
        {
          title: '时间',
          align: 'center',
          dataIndex: 'productDatetime',
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'overproofStatus',
          scopedSlots: { customRender: 'overproofStatus' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
      dataSource2: [],
      columns1: [
        {
          title: '超标等级',
          align: 'center',
          dataIndex: 'chaobiaodengji',
          scopedSlots: { customRender: 'chaobiaodengji' },
        },
        {
          title: '数量(盘)',
          align: 'center',
          dataIndex: 'dish',
        },
        {
          title: '比率(%)',
          align: 'center',
          dataIndex: 'dishlv',
        },
        {
          title: '处置数(盘)',
          align: 'center',
          dataIndex: 'handle',
        },
        {
          title: '处置率(%)',
          align: 'center',
          dataIndex: 'handlelv',
        },
      ],
      url: {
        list: '/hntbhz/bhzCementBase/titleDataSta',
        list1: '/hntbhz/bhzCementBase/list11',
        list2: '/hntbhz/bhzCementBase/list12',
        list3: '/hntbhz/bhzCementBase/pieChart',
        list4: '/hntbhz/bhzCementBase/listbybd',
        list5: '/hntbhz/bhzCementBase/listbysb',
      },

      loading: true,
      center: null,
      height: 340,
      Starttime: null,
      Endtime: null,
      sys_depart_project: '',
      sys_depart_orgcode: ''
    }
  },
  created() {
    setTimeout(() => {
      this.loading = !this.loading
    }, 1000)
    this.sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
    this.getToken()
    this.getData() //盘数统计
    this.getDatas() //产能统计
    this.getHntData() //砼未处置信息
    this.getHntDatas() //本月超标率合格率等统计
    this.getHntstaData() //超标率饼图
    this.getLotDatas() //标段产能统计
    this.getEquipmentDatas() //设备产能统计
  },
  methods: {
    showmadel1: function (record) {
      this.guid = record.guid
      this.flag = 2
      this.bhz = 1
    },
    showmadel: function (record) {
      if (record.overproofStatus === 10) {
        this.guid = record.guid
        this.flag = 1
        this.bhz = 1
      } else {
        this.$message.warning('请先处置！')
      }
    },
    tableChange(pagination) {
      this.ipagination.current = pagination.current
      this.ipagination.pageSize = pagination.pageSize
      this.getHntData()
    },
    getData: function () {
      this.titleDataSta = {}
      this.dataSource2 = []
      getAction(this.url.list, {}).then((res) => {
        if (res.success) {
          this.titleDataSta = res.result
          // console.log(this.titleDataSta)
          this.nochuzhi =
            res.result.primaryCount +
            res.result.middleCount +
            res.result.advanceCount -
            res.result.primaryHandle -
            res.result.middleHandle -
            res.result.advanceHandle
          // this.nochuzhilv =
          // let cj = Math.abs(res.result.primaryCount -  res.result.primaryHandle).toFixed(0)
          // let zj = Math.abs(res.result.middleCount -  res.result.middleHandle).toFixed(0)
          // let gj = Math.abs(res.result.advanceCount -  res.result.advanceHandle).toFixed(0)
          this.chuzhixq.nocyichuji = res.result.primaryNotHandle
          this.chuzhixq.nozyichuji = res.result.middleNotHandle
          this.chuzhixq.nogyichuji = res.result.advanceNotHandle
          this.chuzhixq.nozongyichuji = res.result.untreatedCount

          this.chuzhixq.cyichuji = res.result.primaryHandle.toFixed(0)
          this.chuzhixq.zyichuji = res.result.middleHandle.toFixed(0)
          this.chuzhixq.gyichuji = res.result.advanceHandle.toFixed(0)
          this.chuzhixq.zongyichuji = (
            res.result.primaryHandle +
            res.result.middleHandle +
            res.result.advanceHandle
          ).toFixed(0)
          this.hegelv = (100 - res.result.primaryCent - res.result.middleCent - res.result.advancedCent).toFixed(2)

          this.dataSource2.push(
            {
              key: 1,
              chaobiaodengji: 1,
              dish: res.result.primaryCount,
              dishlv: res.result.primaryCent,
              handle: res.result.primaryHandle,
              handlelv: res.result.primaryHandleCent,
            },
            {
              key: 2,
              chaobiaodengji: 2,
              dish: res.result.middleCount,
              dishlv: res.result.middleCent,
              handle: res.result.middleHandle,
              handlelv: res.result.middleHandleCent,
            },
            {
              key: 3,
              chaobiaodengji: 3,
              dish: res.result.advanceCount,
              dishlv: res.result.advancedCent,
              handle: res.result.advanceHandle,
              handlelv: res.result.advanceHandleCent,
            }
          )
        }
      })
    },
    getHntData: function () {
      //砼未处置信息
      this.dataSource = []
      let param = {
        column: 'id',
        order: 'desc',
        pageSize: this.ipagination.pageSize,
        pageNo: this.ipagination.current,
      }
      getAction(this.url.list2, param).then((res) => {
        // debugger;
        if (res.success) {
          this.dataSource = res.result.records
          this.ipagination.total = res.result.total
        }
      })
    },
    getHntDatas: function () {
      //本月超标率合格率等统计
      this.lqstas = {}
      this.dataSource3 = []
      getAction(this.url.list3, {}).then((res) => {
        if (res.success) {
          this.lqstas = res.result
        }
      })
    },
    change(data) {
      this.flag = data
    },
    change2(data) {
      this.flag = data
    },
    getHntstaData: function (StartTime, EndTime) {
      this.dataSource3 = []
      let params = { statisticsTime_begin: StartTime, statisticsTime_end: EndTime }
      getAction(this.url.list3, params).then((res) => {
        if (res.success) {
          let data = res.result
          if (
            data.primaryCount !== 0 ||
            data.middleCount !== 0 ||
            data.advanceCount !== 0 ||
            data.qualifiedCount !== 0
          ) {
            this.dataSource3.push(
              { item: '初级超标', count: data.primaryCent },
              { item: '中级超标', count: data.middleCent },
              { item: '高级超标', count: data.advancedCent },
              { item: '合格', count: data.qualifiedCent }
            )
          }
        }
      })
    },
    onChange(date, dateString) {
      //根据时间重新去统计月数据
      this.Starttime = dateString[0]
      this.Endtime = dateString[1]
      this.getDatas(this.Starttime, this.Endtime)
      this.getHntstaData(this.Starttime, this.Endtime)
      this.getLotDatas(this.Starttime, this.Endtime)
      this.getEquipmentDatas(this.Starttime, this.Endtime)
    },
    getDatas: function (Starttime, Endtime) {
      //中间部分产能统计数据
      this.dataSource1 = []
      let params = { statisticsTime_begin: Starttime, statisticsTime_end: Endtime }
      getAction(this.url.list1, params).then((res) => {
        if (res.success) {
          let data = res.result
          // console.log(res.result,"2222222222222222")
          if (data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              this.dataSource1.push({
                type: data[i].statisticsTime + '月',
                jeecg: data[i].advancedlv,
                jeebt: data[i].middlelv,
                jeebts: data[i].primarylv,
                jeecgs: data[i].hegelv,
                总方量: data[i].estimateNumber,
              })
            }
          }
        }
      })
    },
    //标段产能统计
    getLotDatas(Starttime, Endtime) {
      this.dataSource4 = []
      let params = { sys_depart_orgcode: this.sys_depart_orgcode, statisticsTime_begin: Starttime, statisticsTime_end: Endtime }
      getAction(this.url.list4, params).then((res) => {
        if (res.success) {
          let data = res.result
          if (data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              this.dataSource4.push({
                type: data[i].name,
                jeecg: data[i].advancedlv,
                jeebt: data[i].middlelv,
                jeebts: data[i].primarylv,
                jeecgs: data[i].hegelv,
                总方量: data[i].estimateNumber,
              })
            }
          }
        }
      })
    },
    //设备产能统计
    getEquipmentDatas(Starttime, Endtime) {
      this.dataSource6 = []
      let params = { sys_depart_orgcode: this.sys_depart_orgcode, statisticsTime_begin: Starttime, statisticsTime_end: Endtime }
      getAction(this.url.list5, params).then((res) => {
        if (res.success) {
          let data = res.result
          // console.log(res.result,"2222222222222222")
          if (data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              this.dataSource6.push({
                type: data[i].name,
                jeecg: data[i].advancedlv,
                jeebt: data[i].middlelv,
                jeebts: data[i].primarylv,
                jeecgs: data[i].hegelv,
                总方量: data[i].estimateNumber,
              })
            }
          }
        }
      })
    },
  },
}
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
