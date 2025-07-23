<template>
  <div>
    <a-card :loading="loading" :bordered="false" :body-style="{ marginBottom: '24px' }">
      <div style="display: flex; justify-content: space-between">
        <div class="header-left">
          <div class="header-left-title">关注问题</div>
          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            class="j-table-force-nowrap"
            :scroll="{ y: 210 }"
            :loading="loadingTab"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="ipagination"
            @change="tableChange"
          >
            <span slot="tags" slot-scope="tags, record">
              <a-tag color="green" v-if="record.overLevel == '0'">合格</a-tag>
              <a-tag color="orange" v-if="record.overLevel == '1'">初级超标</a-tag>
              <a-tag color="yellow" v-if="record.overLevel == '2'">中级超标</a-tag>
              <a-tag color="red" v-if="record.overLevel == '3'">高级超标</a-tag>
            </span>
            <span slot="tags1" slot-scope="tags1, record">
              <a-tag color="red" v-if="record.bhzCementOverHandler.overproofStatus === 0">未处理</a-tag>
              <a-tag color="orange" v-if="record.bhzCementOverHandler.overproofStatus === 10">施工方已处置</a-tag>
              <a-tag color="green" v-if="record.bhzCementOverHandler.overproofStatus === 20">已闭合</a-tag>
              <a-tag color="red" v-if="record.bhzCementOverHandler.overproofStatus === 30">监理驳回</a-tag>
              <a-tag color="yellow" v-if="record.bhzCementOverHandler.overproofStatus === 40">监理已审核</a-tag>
              <a-tag color="yellow" v-if="record.bhzCementOverHandler.overproofStatus === 50">指挥部审核</a-tag>
              <a-tag color="red" v-if="record.bhzCementOverHandler.overproofStatus === 60">指挥部驳回</a-tag>
            </span>
            <span slot="action" slot-scope="text, record">
              <a-dropdown>
                <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
                <a-menu slot="overlay" v-show="record.bhzCementOverHandler.overproofStatus !== 20">
                  <a-menu-item
                    v-show="
                      record.bhzCementOverHandler.overproofStatus === 0 ||
                      record.bhzCementOverHandler.overproofStatus === 30
                    "
                  >
                    <a @click="showmadel1(record)">施工处置</a>
                  </a-menu-item>
                  <a-menu-item
                    v-show="
                      record.bhzCementOverHandler.overproofStatus === 10 ||
                      record.bhzCementOverHandler.overproofStatus === 60
                    "
                  >
                    <a @click="showmadel2(record)">监理审核</a>
                  </a-menu-item>
                  <a-menu-item v-show="record.bhzCementOverHandler.overproofStatus === 40">
                    <a @click="showmadel(record)">指挥部审批</a>
                  </a-menu-item>
                </a-menu>
              </a-dropdown>
            </span>
          </a-table>
        </div>
        <div class="header-right">
          <a-tabs default-active-key="1" tabPosition="left">
            <a-tab-pane tab="今日产能" key="1">
              <div class="header-right-content">
                <div>今日产能</div>
                <div>
                  <a-icon type="schedule" />
                  <span style="margin-left: 20px">{{ nowdate }}</span>
                </div>
              </div>
              <div class="header-right-sc">
                <div class="header-right-sc-item">
                  <img src="../../assets/img/pang-img.png" alt="" />
                  <span>生产方量</span>
                  <span style="font-size: 20px">{{ todaycn.fangliang || 0 }}m³</span>
                </div>
                <div class="header-right-sc-item">
                  <img src="../../assets/img/pang-img.png" alt="" />
                  <span>生产盘数</span>
                  <span style="font-size: 20px">{{ todaycn.shengchanpan || 0 }}盘</span>
                </div>
              </div>
              <div class="header-right-cb">
                <div class="header-right-cb-item">
                  <div class="header-right-cb-item-title">初级超标</div>
                  <div style="color: #387ffd; font-size: 20px">{{ todaycn.chu || 0 }}盘</div>
                </div>
                <div class="header-right-cb-item">
                  <div class="header-right-cb-item-title">中级超标</div>
                  <div style="color: #f08322; font-size: 20px">{{ todaycn.zhong || 0 }}盘</div>
                </div>
                <div class="header-right-cb-item">
                  <div class="header-right-cb-item-title">高级超标</div>
                  <div style="color: #cb5d60; font-size: 20px">{{ todaycn.gao || 0 }}盘</div>
                </div>
              </div>
            </a-tab-pane>
            <!-- 可添加更多 a-tab-pane -->
            <a-tab-pane tab="按强度统计" key="2">
              <div class="header-right-content">
                <div style="display: flex; justify-content: space-between; width: 100%;">
                  <a-range-picker v-model="Datetime_begin_end" :style="{ width: '326px' }" show-time @change="onChange1" @ok="onOk"/>
                  <span>总方量：{{zongfanglinag}}</span>
                </div>
              </div>
              <a-table
                ref="table"
                size="middle"
                bordered
                rowKey="id"
                class="j-table-force-nowrap"
                :scroll="{ y: 210 }"
                :loading="loadingTab"
                :columns="columns3"
                :dataSource="qdfangliangday"
                @change="tableChange"
                style="margin-top: 30px;margin-left: -20px;"
              >
              </a-table>
            </a-tab-pane>
          </a-tabs>
          <p style="width: 90%; border-top: 1px solid #ccc; margin: 40px auto"></p>
        </div>
        <!-- ... existing code ... -->
      </div>
    </a-card>

    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <div class="pour-card">
          <div class="pour-card-title">未审核</div>
          <div class="pour-card-number" style="color: #ff233d">{{ pourVaule.weishenhe || 0 }}</div>
        </div>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <div class="pour-card">
          <div class="pour-card-title" style="background-color: #00cc33">已审核未配料</div>
          <div class="pour-card-number" style="color: #ff8712">{{ pourVaule.shenhe || 0 }}</div>
        </div>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <div class="pour-card">
          <div class="pour-card-title">已完成</div>
          <div class="pour-card-number" style="color: #0fbf6e">{{ pourVaule.wancheng || 0 }}</div>
        </div>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <div class="pour-card">
          <div class="pour-card-title" style="background-color: #00cc33">已配料未生产</div>
          <div class="pour-card-number" style="color: #8333fa">{{ pourVaule.peiliao || 0 }}</div>
        </div>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <div class="pour-card">
          <div class="pour-card-title">生产中</div>
          <div class="pour-card-number" style="color: #387ffd">{{ pourVaule.shenchan || 0 }}</div>
        </div>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }">
        <div class="pour-card">
          <div class="pour-card-title" style="background-color: #00cc33">滞后任务</div>
          <div class="pour-card-number" style="color: #333333">{{ pourVaule.zhihou || 0 }}</div>
        </div>
      </a-col>
    </a-row>

    <a-card :loading="loading" :bordered="false" :body-style="{ marginBottom: '24px' }">
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
              <bar-and-multid-line1 title="砼拌合站设备产能统计" :height="height" :dataSource="dataSource6" />
            </a-col>
            <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
              <bhz-sta-pie title="合格率统计" :height="height" :dataSource="dataSource3" />
            </a-col>
          </a-row>
        </a-tab-pane>
      </a-tabs>
    </a-card>

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

    <a-row>
      <a-col :span="24">
        <a-card :loading="loading" :bordered="false" :headStyle="{ color: '#2a5caa' }" :body-style="{ padding: '5' }">
          <a-tabs default-active-key="1" size="large" :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }">
            <a-tab-pane loading="true" tab="分部分项工程进度" key="1">
              <a-table
                :dataSource="dataSource2"
                size="default"
                rowKey="id"
                :columns="columns2"
                :pagination="false"
                :scroll="{ y: 210 }"
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
            <a-tab-pane loading="true" tab="水泥混凝土拌合站生产情况分析" key="2">
              <a-table
                :dataSource="dataSource5"
                size="default"
                rowKey="id"
                :columns="columns1"
                :pagination="false"
                :scroll="{ y: 210 }"
              >
              </a-table>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </a-col>
    </a-row>

    <chu-zhi-two ref="modalChuZhi" :bhz="bhz" :batchNo="batchNo" :level="overLevel" @change="change"></chu-zhi-two>
    <shen-he-two ref="modalShenHe" :bhz="bhz" :batchNo="batchNo" :level="overLevel" @change="change"></shen-he-two>
    <shen-pi ref="modalShenPi" :bhz="bhz" :batchNo="batchNo" :level="overLevel" @change="change"></shen-pi>
  </div>
</template>

<script>
import ChartCard from '@/components/ChartCard1'
import ACol from 'ant-design-vue/es/grid/Col'
import ATooltip from 'ant-design-vue/es/tooltip/Tooltip'
import MiniProgress from '@/components/chart/MiniProgress'
import LineChartMultid from '@/components/chart/LineChartMultid'
import HeadInfo from '@/components/tools/HeadInfo.vue'
import { getAction } from '@api/manage'
import moment from 'moment'
import ChuZhiTwo from '../bhz/czsh/ChuZhiTwo'
import ShenHeTwo from '../bhz/czsh/ShenHeTwo'
import ShenPi from '../bhz/czsh/ShenPi'
import Trend from '@/components/Trend'
import TrendRed from '@comp/Trend/TrendRed'
import BarAndMultidLine from '@comp/chart/BarAndMultidLine'
import BarAndMultidLine1 from '@comp/chart/BarAndMultidLine1'
import BhzStaPie from '@comp/chart/BhzStaPie'
import { handertoken } from '@/mixins/getHanderToken'
import Vue from 'vue'
import { getSpecificDate } from '@/utils/util'

export default {
  name: 'CMindexTwo',
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
    BarAndMultidLine1,
    ShenHeTwo,
    ChuZhiTwo,
    ShenPi
  },
  data() {
    return {
      zongfanglinag:0,
      titleDataSta: {},
      lqstas: {},
      hegelv: 0,
      nochuzhi: 0,
      nochuzhilv: 0,
      nowdate: moment().format('YYYY-MM-DD'),
      todaycn: {},
      batchNo: '',
      overLevel: '',
      bhz: 0,
      pourVaule: {
        peiliao: 0,
        wancheng: 0,
        shenchan: 0,
        zhihou: 0,
        weishenhe: 0,
        shenhe: 0
      },
      chuzhixq: {},
      ipagination: {
        current: 1,
        pageSize: 5,
        pageSizeOptions: ['5', '10', '15'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      dataSource: [],
      dataSource1: [],
      dataSource2: [],
      dataSource3: [],
      dataSource4: [],
      dataSource5: [],
      dataSource6: [],
      qdfangliangday: [],
      columns2: [
        {
          title: '项目名称',
          align: 'center',
          dataIndex: 'projectname',
          scopedSlots: { customRender: 'projectname' }
        },
        {
          title: '施工部位',
          align: 'center',
          dataIndex: 'conspos'
        },
        {
          title: '浇筑方式',
          align: 'center',
          dataIndex: 'pour',
          scopedSlots: { customRender: 'pour' }
        },
        {
          title: '开始生产时间',
          align: 'center',
          dataIndex: 'starttim'
        },
        {
          title: '结束生产时间',
          align: 'center',
          dataIndex: 'endtim'
        },
        {
          title: '强度等级',
          align: 'center',
          dataIndex: 'betlev',
          scopedSlots: { customRender: 'betlev' }
        },
        {
          title: '坍落度',
          align: 'center',
          dataIndex: 'lands',
          scopedSlots: { customRender: 'lands' }
        },
        {
          title: '任务方量(m³)',
          align: 'center',
          dataIndex: 'mete'
        },
        {
          title: '生产方量(m³)',
          align: 'center',
          dataIndex: 'metes'
        },
        {
          title: '进度(%)',
          align: 'center',
          dataIndex: 'bfb',
          scopedSlots: { customRender: 'flowRate' }
        }
      ],
      columns1: [
        {
          title: '项目名称',
          align: 'center',
          dataIndex: 'name'
        },
        {
          title: '安装数量(台)',
          align: 'center',
          dataIndex: 'shebeiNum'
        },
        {
          title: '总方量(m³)',
          align: 'center',
          dataIndex: 'estimateNumber'
        },
        {
          title: '总盘数',
          align: 'center',
          dataIndex: 'allDish'
        },
        {
          title: '原材料超限盘数',
          align: 'center',
          dataIndex: 'cbNum'
        },
        {
          title: '原材料超限率(%)',
          align: 'center',
          dataIndex: 'cblv'
        },
        {
          title: '水胶比超限盘数',
          align: 'center',
          dataIndex: 'sjbNum'
        },
        {
          title: '水胶比超限率(%)',
          align: 'center',
          dataIndex: 'sjbLv'
        }
      ],
      columns: [
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'shebeiNo',
          key: 'shebeiNo',
          ellipsis: true
        },
        {
          title: '最高超限率',
          align: 'center',
          dataIndex: 'additiveVariety',
          key: 'additiveVariety',
          ellipsis: true
        },
        {
          title: '超标等级',
          align: 'center',
          dataIndex: 'overLevel',
          key: 'overLevel',
          scopedSlots: { customRender: 'tags' },
          ellipsis: true
        },
        {
          title: '出料时间',
          align: 'center',
          dataIndex: 'productDatetime',
          key: 'productDatetime',
          ellipsis: true
        },
        {
          title: '处理状态',
          align: 'center',
          dataIndex: 'bhzCementOverHandler.overproofStatus',
          key: 'bhzCementOverHandler.overproofStatus',
          scopedSlots: { customRender: 'tags1' },
          ellipsis: true
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
          ellipsis: true
        }
      ],
      columns3: [
        {
          title: '强度等级',
          align: 'center',
          dataIndex: 'strengthRank',
          key: 'strengthRank',
          ellipsis: true
        },
        {
          title: '盘',
          align: 'center',
          dataIndex: 'panhao',
          key: 'panhao',
          ellipsis: true
        },
        {
          title: '方量',
          align: 'center',
          dataIndex: 'estimateNumber',
          key: 'estimateNumber',
          ellipsis: true
        },
        {
          title: '初',
          align: 'center',
          dataIndex: 'overLevel',
          key: 'overLevel',
          ellipsis: true
        },
        {
          title: '中',
          align: 'center',
          dataIndex: 'alertstate',
          key: 'alertstate',
          ellipsis: true
        },
        {
          title: '高',
          align: 'center',
          dataIndex: 'timeOverLevel',
          key: 'timeOverLevel',
          scopedSlots: { customRender: 'tags' },
          ellipsis: true
        }
      ],
      url: {
        renwujindu: '/zhiliangrenwudan/zhiliangrenwudan/list1',
        list: '/hntbhz/bhzCementBase/titleDataSta',
        list1: '/hntbhz/bhzCementBase/list11',
        list2: '/hntbhz/bhzCementBase/list12',
        list3: '/hntbhz/bhzCementBase/pieChart',
        list4: '/hntbhz/bhzCementBase/listbybd',
        list5: '/hntbhz/bhzCementBase/listbysb'
      },

      loading: true,
      loadingTab: false,
      center: null,
      height: 340,
      Starttime: null,
      Endtime: null,
      Datetime_begin: null,
      Datetime_end: null,
      Datetime_begin_end:null,
      sys_depart_project: '',
      sys_depart_orgcode: ''
    }
  },
  created() {
    setTimeout(() => {
      this.loading = !this.loading
    }, 1000)
    this.sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
    
    this.Datetime_begin = `${getSpecificDate(-1)} 17:00:00`;
    this.Datetime_end = `${getSpecificDate(0)} 17:00:00`;
    this.Datetime_begin_end = [moment(`${this.Datetime_begin}`, 'YYYY-MM-DD HH:mm:ss'), moment(`${this.Datetime_end}`, 'YYYY-MM-DD HH:mm:ss')]

    this.getToken()
    this.getData() //盘数统计
    this.getDatas() //产能统计
    this.getHntDatas() //本月超标率合格率等统计
    this.getHntstaData() //超标率饼图
    this.getLotDatas() //标段产能统计
    this.getEquipmentDatas() //设备产能统计

    this.getTodayCN() //获取今日产能
    this.getTodayQDCN() //获取今日产能,按强度
    this.overHandle() //超标处置
    this.getPourList() //获取浇筑任务
    this.renwudanjindu() //制梁任务单进度
    this.hntMixingPlant() //水泥混凝土拌合站生产情况分析
  },
  methods: {
    // 今日产能
    getTodayCN() {      
      let params = {
        statisticsTime: this.nowdate
      }
      getAction('/hntbhz/bhzCementBase/listtj', params).then((res) => {
        if (res.success) {
          // console.log(res)
          this.todaycn = res.result
        }
      })
    },
    // 今日产能,按强度
    getTodayQDCN() {
      let params = {
        productDatetime_begin: this.Datetime_begin,
        productDatetime_end: this.Datetime_end
      }
      getAction('/hntbhz/bhzCementBase/listtjgbqddj', params).then((res) => {
        if (res.success) {
          console.log(res)
          this.qdfangliangday = res.result.list
          this.zongfanglinag = res.result.estimateNumber
        }
      })
    },
    
    onChange1(date, dateString) {
      //根据时间重新去统计月数据
      this.Datetime_begin = dateString[0]
      this.Datetime_end = dateString[1]
      console.log(dateString,dateString[0],dateString[1],'onChange1------------------');
      // this.getDatas(this.Starttime, this.Endtime)
      // this.getHntstaData(this.Starttime, this.Endtime)
      // this.getLotDatas(this.Starttime, this.Endtime)
      // this.getEquipmentDatas(this.Starttime, this.Endtime)
    },
    onOk(){
      this.getTodayQDCN();
    },

    //获取浇筑任务
    getPourList() {
      let param = { sys_depart_orgcode: this.sys_depart_orgcode }
      getAction('/bhzrwdxx/bhzrwdxx/listtj',param).then((res) => {
        if (res.success) {
          // console.log(res)
          this.pourVaule = res.result
        }
      })
    },
    //制梁任务单进度
    renwudanjindu() {
      let param = { sys_depart_orgcode: this.sys_depart_orgcode }
      getAction(this.url.renwujindu, param).then((res) => {
        if (res.success) {
          // console.log(res)
          this.dataSource2 = res.result.records
        }
      })
    },
    //水泥混凝土拌合站生产情况分析
    hntMixingPlant() {
      let param = { sys_depart_orgcode: this.sys_depart_orgcode }
      getAction('/hntbhz/bhzCementBase/list15', param).then((res) => {
        if (res.success) {
          // console.log(res);
          this.dataSource5 = res.result
        }
      })
    },
    tableChange(pagination) {
      this.ipagination.current = pagination.current
      this.ipagination.pageSize = pagination.pageSize
      this.overHandle()
    },
    //超标处置
    overHandle() {
      this.loadingTab = true
      let params = {
        pageNo: this.ipagination.current,
        pageSize: this.ipagination.pageSize,
        overproofStatus: 0,
        column: 'productDatetime',
        order: 'desc'
      }
      getAction('/hntbhz/bhzCementBase/listczsh', params).then((res) => {
        if (res.success) {
          this.dataSource = res.result.records
          if (res.result.total) {
            this.ipagination.total = res.result.total
          } else {
            this.ipagination.total = 0
          }
        }
      }).finally(() => {
        this.loadingTab = false
      })
    },
    showmadel: function(record) {
      this.bhz = 0
      this.batchNo = record.batchNo
      this.overLevel = record.overLevel
      this.$refs.modalShenPi.showModal(record)
    },
    showmadel1: function(record) {
      this.bhz = 0
      this.batchNo = record.batchNo
      this.overLevel = record.overLevel
      this.$refs.modalChuZhi.showModal(record)
    },
    showmadel2: function(record) {
      this.bhz = 0
      this.batchNo = record.batchNo
      this.overLevel = record.overLevel
      this.$refs.modalShenHe.showModal(record)
    },
    change(type) {
      if (type) {
        this.overHandle()
      }
    },
    getPercentColor(value) {
      let p = Number(value)
      if (p >= 90 && p < 100) {
        return 'rgb(244, 240, 89)'
      } else if (p >= 100) {
        return 'red'
      } else {
        return 'rgb(16, 142, 233)'
      }
    },
    getFlowRateNumber(value) {
      return Number(value)
    },
    getData: function() {
      this.titleDataSta = {}
      getAction(this.url.list, {}).then((res) => {
        if (res.success) {
          this.titleDataSta = res.result
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
        }
      })
    },
    getHntDatas: function() {
      //本月超标率合格率等统计
      this.lqstas = {}
      this.dataSource3 = []
      getAction(this.url.list3, {}).then((res) => {
        if (res.success) {
          this.lqstas = res.result
        }
      })
    },
    getHntstaData: function(StartTime, EndTime) {
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
    getDatas: function(Starttime, Endtime) {
      //中间部分产能统计数据
      this.dataSource1 = []
      let params = { statisticsTime_begin: Starttime, statisticsTime_end: Endtime }
      getAction(this.url.list1, params).then((res) => {
        if (res.success) {
          let data = res.result
          if (data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              this.dataSource1.push({
                type: data[i].statisticsTime + '月',
                jeecg: data[i].advancedlv,
                jeebt: data[i].middlelv,
                jeebts: data[i].primarylv,
                jeecgs: data[i].hegelv,
                总方量: data[i].estimateNumber
              })
            }
          }
        }
      })
    },
    //标段产能统计
    getLotDatas(Starttime, Endtime) {
      this.dataSource4 = []
      let params = {
        sys_depart_orgcode: this.sys_depart_orgcode,
        statisticsTime_begin: Starttime,
        statisticsTime_end: Endtime
      }
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
                总方量: data[i].estimateNumber
              })
            }
          }
        }
      })
    },
    //设备产能统计
    getEquipmentDatas(Starttime, Endtime) {
      this.dataSource6 = []
      let params = {
        sys_depart_orgcode: this.sys_depart_orgcode,
        statisticsTime_begin: Starttime,
        statisticsTime_end: Endtime
      }
      getAction(this.url.list5, params).then((res) => {
        if (res.success) {
          let data = res.result
          if (data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              this.dataSource6.push({
                type: data[i].name,
                jeecg: data[i].advancedlv,
                jeebt: data[i].middlelv,
                jeebts: data[i].primarylv,
                jeecgs: data[i].hegelv,
                总方量: data[i].estimateNumber
              })
            }
          }
        }
      })
    }
  }
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

.header-left {
  width: 46%;
  height: auto;

  .header-left-title {
    height: 40px;
    line-height: 40px;
    width: 150px;
    padding-left: 20px;
    font-size: 16px;
    color: #fff;
    float: left;
    background: #f63;
    font-weight: bold;
    margin-bottom: 10px;
  }
}

.header-right {
  width: 46%;
  height: 360px;
  background: url(../../assets/img/daycn.png) no-repeat;
  background-size: 100% 100%;
  // border: 1px solid #ccc;
  border-radius: 4px;

  &-content {
    display: flex;
    justify-content: space-between;
    font-size: 20px;
    color: #ffffff;
    padding: 4px 20px 0;
  }

  &-sc {
    display: flex;
    justify-content: space-evenly;
    margin-top: 71px;

    &-item {
      height: 45px;
      width: 227px;
      border-radius: 14px;
      border: 2px solid #d0d2da;
      text-align: center;
      line-height: 39px;
      // padding: 10px 20px;
      img {
        width: 17px;
        height: 17px;
        margin-bottom: 4px;
        // margin: 0 12px;
      }

      span {
        margin-left: 12px;
        color: #4c5971;
      }
    }
  }

  &-cb {
    display: flex;
    justify-content: space-evenly;
    margin-top: 40px;
    text-align: center;

    &-item {
      border-radius: 14px;
      // border: 2px solid #d0d2da;
      background-color: rgba(208, 210, 218, 0.5);
      padding: 15px 40px;
    }
  }
}

// .pour-card-title::after {
//   content: '';
//   width: 0;
//   height: 0;
//   border-left: 10px solid transparent;
//   border-right: 10px solid transparent;
//   border-top: 10px solid rgb(227, 37, 37);
//   position: absolute;
//   top: 40px;
//   left: 20px;
// }
.pour-card {
  width: 100%;
  height: 187px;
  color: rgba(0, 0, 0, 0.65);
  box-shadow: 3px 5px 5px #eee;
  font-size: 14px;
  font-variant: tabular-nums;
  text-align: center;
  background: #ffffff;
  border-radius: 4px;
  position: relative;

  &-title {
    height: 40px;
    background-color: #333333;
    color: #ffffff;
    font-weight: bold;
    font-size: 16px;
    line-height: 40px;
    border-radius: 4px 4px 0 0;
  }

  &-number {
    height: 157px;
    font-weight: bold;
    font-size: 40px;
    line-height: 140px;
  }
}

//.header-right-content {
//  display: flex;
//  flex-direction: column;
//  height: 100%; /* 确保容器有高度 */
//}
//
//.table-move-and-connect {
//  margin-top: 20px;
//  margin-bottom: 0;
//  margin-left: auto;
//  margin-right: auto;
//  flex-grow: 1; /* 让表格填充剩余空间 */
//  display: flex;
//  flex-direction: column;
//}
//
//.table-move-and-connect .ant-table {
//  margin-top: auto; /* 让表格内容靠底部排列 */
//}
</style>
