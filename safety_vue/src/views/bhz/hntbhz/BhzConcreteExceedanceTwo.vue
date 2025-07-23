<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeiNo" :dictOptions="dictOption">
              </j-search-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="分组类型">
              <j-dict-select-tag
                placeholder="请选择"
                v-model="queryParam.type"
                dictCode="group_type"
              ></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="出料时间">
              <j-date
                placeholder="开始日期"
                v-model="queryParam.statisticsTime_begin"
                dateFormat="YYYY-MM-DD"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束日期"
                v-model="queryParam.statisticsTime_end"
                dateFormat="YYYY-MM-DD"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <!-- <div class="table-operator">
      <a-button type="primary" icon="download" @click="handleExportXls('拌合站主表')">导出</a-button>
    </div> -->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        :rowKey="(record,index)=>index" 
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
      </a-table>
    </div>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { handertoken } from '@/mixins/getHanderToken'
import Vue from 'vue'
import moment from 'moment';
import { usershebeiList } from '@api/api'

export default {
  name: 'BhzConcreteExceedanceTwo',
  mixins: [JeecgListMixin, handertoken],
  components: {},
  data() {
    return {
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '拌和机名称',
          align: 'center',
          dataIndex: 'name',
        },
        {
          title: '总产量',
          align: 'center',
          dataIndex: 'zongchangliang',
        },
        {
          title: '总盘数',
          align: 'center',
          dataIndex: 'allCount',
        },
        {
          title: '初级超标盘数',
          align: 'center',
          dataIndex: 'primaryCount',
        },
        {
          title: '初级超标率',
          align: 'center',
          dataIndex: 'primaryWarnLv',
        },
        {
          title: '中级超标盘数',
          align: 'center',
          dataIndex: 'middleCount',
        },
        {
          title: '中级超标率',
          align: 'center',
          dataIndex: 'middleWarnLv',
        },
        {
          title: '高级超标盘数',
          align: 'center',
          dataIndex: 'advancedCount',
        },
        {
          title: '高级超标率',
          align: 'center',
          dataIndex: 'advancedWarnLv',
        },
        {
          title: '初级处置盘数',
          align: 'center',
          dataIndex: 'primaryHandleCount',
        },
        {
          title: '初级处置率',
          align: 'center',
          dataIndex: 'primaryHandleLv',
        },
        {
          title: '中级处置盘数',
          align: 'center',
          dataIndex: 'middleHandleCount',
        },
        {
          title: '中级处置率',
          align: 'center',
          dataIndex: 'middleHandleLv',
        },
        {
          title: '高级处置盘数',
          align: 'center',
          dataIndex: 'advancedHandleCount',
        },
        {
          title: '高级处置率',
          align: 'center',
          dataIndex: 'advancedHandleLv',
        },
      ],
      dictOption: [],
      queryParam: {
        type: 1,
        statisticsTime_begin: moment().subtract(1,'months').format('YYYY-MM-DD'),
        statisticsTime_end: moment().format('YYYY-MM-DD'),
      },
      url: {
        list: '/hntbhz/bhzCementBase/cbtongji',
        // exportXlsUrl: "/hntbhz/bhzCementBase/exportXlsCB",
      },
    }
  },
  created() {
    this.getToken()
    this.shebeiList()
  },
  methods: {
    shebeiList() {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '0',
      }).then((res) => {
        this.dictOption = []
        let result = res.result
        result.forEach((re) => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
      })
    },
    searchReset() {
      this.queryParam = {
        type: 1,
        statisticsTime_begin: moment().subtract(1, 'months').format('YYYY-MM-DD'),
        statisticsTime_end: moment().format('YYYY-MM-DD'),
      }
      this.loadData(1)
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>