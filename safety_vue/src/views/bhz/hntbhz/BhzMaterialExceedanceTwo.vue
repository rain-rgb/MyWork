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
        <span slot="level" slot-scope="level, record">
          <a-tag color="orange" v-if="record.level === 1">初级超标</a-tag>
          <a-tag color="yellow" v-if="record.level === 2">中级超标</a-tag>
          <a-tag color="red" v-if="record.level === 3">高级超标</a-tag>
        </span>
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
  name: 'BhzMaterialExceedanceTwo',
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
          dataIndex: 'shebeiName',
          customRender: (text, record, index) => this.columnsInit(text, index, 3),
        },
        {
          title: '总产量',
          align: 'center',
          dataIndex: 'zongchangliang',
          customRender: (text, record, index) => {
            let val = text ? text : 0
            return this.columnsInit(val, index, 3)
          },
        },
        {
          title: '总盘数',
          align: 'center',
          dataIndex: 'allDish',
          customRender: (text, record, index) => {
            let val = text ? text : 0
            return this.columnsInit(val, index, 3)
          },
        },
        {
          title: '超标盘数',
          align: 'center',
          dataIndex: 'allOverDish',
          customRender: (text, record, index) => {
            let val = text ? text : 0
            return this.columnsInit(val, index, 3)
          },
        },
        {
          title: '超标率',
          align: 'center',
          dataIndex: 'chaobiaolv',
          customRender: (text, record, index) => {
            let val = text ? text : 0
            return this.columnsInit(val, index, 3)
          },
        },
        {
          title: '超标等级',
          align: 'center',
          dataIndex: 'level',
          scopedSlots: { customRender: 'level' },
        },
        {
          title: '细骨料',
          align: 'center',
          dataIndex: 'xiguliao',
          customRender: function (t, r, index) {
            return t ? t : 0
          },
        },
        {
          title: '粗骨料(大石)',
          align: 'center',
          dataIndex: 'xiguliao_advanced',
          customRender: function (t, r, index) {
            return t ? t : 0
          },
        },
        {
          title: '粗骨料(中石)',
          align: 'center',
          dataIndex: 'xiguliao_middle',
          customRender: function (t, r, index) {
            return t ? t : 0
          },
        },
        {
          title: '粗骨料(小石)',
          align: 'center',
          dataIndex: 'xiguliao_primary',
          customRender: function (t, r, index) {
            return t ? t : 0
          },
        },
        {
          title: '水',
          align: 'center',
          dataIndex: 'water',
          customRender: function (t, r, index) {
            return t ? t : 0
          },
        },
        {
          title: '水泥',
          align: 'center',
          dataIndex: 'shuini',
          customRender: function (t, r, index) {
            return t ? t : 0
          },
        },
        {
          title: '矿粉',
          align: 'center',
          dataIndex: 'kuangfen',
          customRender: function (t, r, index) {
            return t ? t : 0
          },
        },
        {
          title: '粉煤灰',
          align: 'center',
          dataIndex: 'fenmeihui',
          customRender: function (t, r, index) {
            return t ? t : 0
          },
        },
        {
          title: '外加剂',
          align: 'center',
          dataIndex: 'waijiaji',
          customRender: function (t, r, index) {
            return t ? t : 0
          },
        },
        {
          title: '其他',
          align: 'center',
          dataIndex: 'qita',
          customRender: function (t, r, index) {
            return t ? t : 0
          },
        },
      ],
      ipagination: {
        current: 1,
        pageSize: 9,
        pageSizeOptions: ['9', '18', '27'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      dictOption: [],
      queryParam: {
        type: 1,
        statisticsTime_begin: moment().subtract(1,'months').format('YYYY-MM-DD'),
        statisticsTime_end: moment().format('YYYY-MM-DD'),
      },
      url: {
        list: '/hntbhz/bhzCementBase/cbdetailtongji',
        // exportXlsUrl: "/hntbhz/bhzCementBase/exportXlsCB",
      },
    }
  },
  created() {
    this.getToken()
    this.shebeiList()
  },
  computed: {},
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
    columnsInit(text, index, num) {
      const obj = {
        children: text,
        attrs: {},
      }
      if (index === 0 || index % num === 0) {
        obj.attrs.rowSpan = num
      } else {
        obj.attrs.rowSpan = 0
      }
      return obj
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>