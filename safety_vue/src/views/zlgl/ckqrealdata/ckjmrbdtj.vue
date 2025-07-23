<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间">
              <j-date placeholder="选择时间" v-model="time" :showTime="true" dateFormat="YYYY-MM-DD" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" v-has="'ckqreal:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'ckqreal:dc'" icon="download" @click="handleExportXls('采空区实时表信息')"
        >导出</a-button
      >
      <a-upload
        name="file"
        v-has="'ckqreal:dr'"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
    </div>
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
        :scroll="{ y: 540 }"
        bordered
        rowKey="idkey"
        :columns="columns"
        :dataSource="dataSource"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
        :pagination="false"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
          <img
            v-else
            :src="getImgView(text)"
            height="25px"
            alt=""
            style="max-width: 80px; font-size: 12px; font-style: italic"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
            下载
          </a-button>
        </template>
        <span slot="zjbh" slot-scope="text, record">
          <div v-for="(item,indexcode) in record.groutingPumpList" :key="indexcode">
            <div>{{item.groutingPumpCode}}</div>
            <a-divider style="margin:0;height:1px" />
          </div>
        </span>
        <span slot="zjl" slot-scope="text, record">
          <div v-for="(item,index) in record.groutingPumpList" :key="index">
            <div>{{item.groutingTotal}}</div>
            <a-divider style="margin:0;height:1px" />
          </div>
        </span>
      </a-table>
    </div>

    <device-traffic-realdata-modal ref="modalForm" @ok="modalFormOk"></device-traffic-realdata-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import DeviceTrafficRealdataModal from './modules/DeviceTrafficRealdataModal'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import Vue from 'vue'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { usershebeiList } from '@api/api'
import { getAction, postAction } from '../../../api/manage'
export default {
  name: 'StatisticsInitialization',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    DeviceTrafficRealdataModal,
    JSuperQuery,
  },
  data() {
    return {
      time:'',
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      description: '采空区每日标段数据统计',
      plsczl: '0',
      zjzl: '0',
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowKey',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '标段号',
          align: 'center',
          dataIndex: 'bidCode',
          key: 'bidCode',
        },
        {
          title: '搅拌站号',
          align: 'center',
          dataIndex: 'groutingPumpCode',
          key: 'groutingPumpCode',
        },
        {
          title: '注浆泵号',
          align: 'center',
          scopedSlots: { customRender: 'zjbh' },
        },
        {
          title: '注浆量(m³)',
          align: 'center',
          scopedSlots: { customRender: 'zjl' },
        },
        {
          title: '当日注浆量(m³)',
          align: 'center',
          dataIndex: 'todayGroutingTotal',
          key: 'todayGroutingTotal',
        },
        {
          title: '累计注浆量(m³)',
          align: 'center',
          dataIndex: 'allDayGroutingTotal',
          key: 'allDayGroutingTotal',
        },
        {
          title: '时间',
          align: 'center',
          dataIndex: 'time',
          key: 'time',
        },
      ],
      url: {
        list: '/grouting/pump/selectDeviceGroutingPumpDayVoList',
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
    this.shebeiList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    loadData() {
      this.loading = true
       let params = {time:this.time}
        getAction(this.url.list, params)
          .then((res) => {
            if (res) {
              let idkey = 1
              res.forEach(e => {
                e.idkey = idkey++
              })
              this.dataSource = res
              console.log(this.dataSource)
            }
          })
          .finally(() => {
            this.loading = false
          })
      },
    // },
    handleOk(records) {
      console.log(dataSource)
      console.log(records)
      let params = {
        id: records.id,
        batchingProductionUpdate: records.plsczl,
        groutingTotalUpdate: records.zjzl,
      }
      getAction(this.url.sjtjedit, params).then((res) => {
        console.log(res)
      })
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '36,37',
      }).then((res) => {
        this.dictOption = []
        let result = res.result
        result.forEach((re) => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'bidCode', text: '标段号' })
      fieldList.push({ type: 'string', value: 'mixinStationCode', text: '搅拌站号' })
      fieldList.push({ type: 'number', value: 'batchingProductionTotal', text: '配料生产总量' })
      fieldList.push({ type: 'integer', value: 'groutingWellsNumber', text: '已注浆井数量' })
      fieldList.push({ type: 'number', value: 'groutingTotal', text: '注浆总量' })
      this.superFieldList = fieldList
    },
  },
}
</script>
<style scoped>

@import '~@assets/less/common.less';
.j-table-force-nowrap>>>.ant-table-middle > .ant-table-content > .ant-table-scroll > .ant-table-body > table > .ant-table-tbody > tr > td{
    padding: 0;
  }
</style>