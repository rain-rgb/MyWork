<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="完成桩基数(根)" :total="totalData.finishiData">
          <a-tooltip title="完成桩基数" slot="action">
            <a-icon type="info-circle-o"/>
          </a-tooltip>
          <trend flag="up">
            <span slot="term">今日完成桩基数(根)</span>
            {{ totalDataD.finishiDataD }}
          </trend>
          <!--          <template slot="footer">今日完成桩基数(根)<span class="errors"> {{0}}</span></template>-->
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="累计注浆量" :total="totalData.leijiaData">
          <a-tooltip title="累计注浆量" slot="action">
            <a-icon type="info-circle-o"/>
          </a-tooltip>
          <trend flag="up">
            <span slot="term">今日累计注浆量</span>
            {{ totalDataD.leijiaDataD }}
          </trend>
          <!--          <template slot="footer">本月合格率<span class="errors"> {{hegelv}}%</span></template>-->
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="总钻深度" :total="totalData.totaldep">
          <a-tooltip title="总钻深度" slot="action">
            <a-icon type="info-circle-o"/>
          </a-tooltip>
          <trend flag="up">
            <span slot="term">今日钻深度</span>
            {{ totalDataD.totaldepD }}
          </trend>
          <!--          <template slot="footer">本月合格率<span class="errors"> {{hegelv}}%</span></template>-->
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="质量预警" :total="totalData.warnData">
          <a-tooltip title="质量预警" slot="action">
            <a-icon type="info-circle-o"/>
          </a-tooltip>
          <trend flag="up">
            <span slot="term">今日质量预警</span>
            {{ totalDataD.warnDataD }}
          </trend>
          <!--          <template slot="footer">本月合格率<span class="errors"> {{hegelv}}%</span></template>-->
        </chart-card>
      </a-col>
    </a-row>

    <a-card :loading="loading" :bordered="false" :body-style="{padding: '0'}">
      <div class="salesCard">
        <a-tabs default-active-key="1" size="large" :tab-bar-style="{marginBottom: '24px', paddingLeft: '16px'}">
          <div class="extra-wrapper" slot="tabBarExtraContent">
            <a-range-picker :style="{width: '256px'}" @change="onChange"/>
          </div>
          <a-tab-pane loading="true" tab="产能分析" key="1">
            <!--            <div class="poursta">-->
            <!--              <div class="poursta-title">产能统计</div>-->
            <!--              <div class="poursta-title-item">-->
            <!--                年-->
            <!--              </div>-->
            <!--              <div class="poursta-title-item">-->
            <!--                月-->
            <!--              </div>-->
            <!--              <div class="poursta-title-item">-->
            <!--                天-->
            <!--              </div>-->
            <!--            </div>-->
            <a-row>
              <a-col :xl="24" :lg="12" :md="12" :sm="24" :xs="24">
                <mixpile-bar title="产能统计" :height="height" :dataSource="dataSource1"/>
              </a-col>
              <!--              <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">-->
              <!--                <bhz-sta-pie title="合格率统计" :height="height" :dataSource="dataSource3" />-->
              <!--              </a-col>-->
            </a-row>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-card>

  </div>
</template>

<script>
import ChartCard from '@comp/ChartCard1'
import ATooltip from 'ant-design-vue/lib/tooltip/Tooltip'
import ACol from 'ant-design-vue/lib/grid/Col'
import Trend from '@comp/Trend'
import { getAction } from '@api/manage'
import MixpileBar from '@views/home/mixpileBar'
import dateFormat from '@comp/jeecg/JEasyCron/format-date'

export default {
  name: 'bhmixpile',
  components: {
    MixpileBar,
    ChartCard,
    ATooltip,
    ACol, Trend
  },
  data() {
    return {
      Starttime: null,
      Endtime: null,
      StarttimeD: dateFormat(new Date()) + ' 00:00:00',
      EndtimeD: dateFormat(new Date()) + ' 23:59:59',
      height: 340,
      date: 1,
      dataSource1: [],
      totalData: {
        finishiData: 0,
        leijiaData: 0,
        totaldep: 0,
        warnData: 0
      },
      totalDataD: {
        finishiDataD: 0,
        leijiaDataD: 0,
        totaldepD: 0,
        warnDataD: 0
      },
      url: {
        list: '/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/list1',
        list1: '/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/stalists'
      },
      loading: true,
    }
  },
  created() {
    this.getstaData()
    this.getchanneng()
    this.getstaDataD()
    setTimeout(() => {
      this.loading = !this.loading
    }, 1000)
  },
  methods: {
    callback(key) {
      console.log(key)
    },
    onChange(date, dateString) {//根据时间重新去统计月数据
      this.Starttime = dateString[0]
      this.Endtime = dateString[1]
      this.getchanneng()
    },
    getstaData: function () {
      this.totalData = {
        finishiData: 0,
        leijiaData: 0,
        totaldep: 0,
        warnData: 0
      }
      getAction(this.url.list, {}).then(res => {
        if (res.success) {
          this.totalData.finishiData = res.result.size
          this.totalData.leijiaData = res.result.sumzjl
          this.totalData.totaldep = res.result.zonglong
          this.totalData.warnData = res.result.chaobiaocount
        }
      })
    },
    getstaDataD: function () {
      this.totalDataD = {
        finishiDataD: 0,
        leijiaDataD: 0,
        totaldepD: 0,
        warnDataD: 0
      }
      console.log('this.StarttimeD', this.StarttimeD)
      console.log('this.EndtimeD', this.EndtimeD)
      getAction(this.url.list, {
        pileTime_begin: this.StarttimeD,
        pileTime_end: this.EndtimeD
      }).then(res => {
        if (res.success) {
          this.totalDataD.finishiDataD = res.result.size
          this.totalDataD.leijiaDataD = res.result.sumzjl
          this.totalDataD.totaldepD = res.result.zonglong
          this.totalDataD.warnDataD = res.result.chaobiaocount
        }
      })
    },
    getchanneng: function () {
      this.dataSource1 = []
      getAction(this.url.list1, {
        pileTime_begin: this.Starttime,
        pileTime_end: this.Endtime,
        date: this.date
      }).then(res => {
        if (res.success) {
          console.log('res', res.result)
          let data = res.result
          data.forEach(re => {
            this.dataSource1.push({
              type: re.pileTime, 'jeecg': re.pileLength, '桩基数(根)': re.pileNum
            })
          })
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
    color: rgba(0, 0, 0, .45);
    display: inline-block;
    font-size: .95rem;
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


.poursta {
  height: 50px;
  margin-top: 30px;
  border-radius: 16px;
  background-color: #FFFFFF;

  &-title {
    padding-top: 10px;
    margin-left: 30px;
    font-size: 18px;
    font-weight: 60;
    color: #333333;

    &-item {
      margin-right: 40%;
      text-align: center;
      float: right;
      font-weight: normal;
      font-size: 3px;
    }

    .item1 {
      background-color: rgba(56, 127, 253, .5);
      border-radius: 8px;
    }

    .item2 {
      background-color: rgba(51, 51, 51, .5);
      border-radius: 8px;
    }

  }
}
</style>