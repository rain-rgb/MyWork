<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24"> </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
           <a-form-item label="告警类型">
              <j-dict-select-tag placeholder="请选择告警类型" v-model="queryParam.alarmtypeid"
                                 dictCode="alarmtypeid"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="预警等级">
              <j-dict-select-tag
                placeholder="请选择预警等级"
                v-model="queryParam.alarmlevel"
                dictCode="over_level"
              ></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date
                placeholder="开始时间范围"
                v-model="queryParam.datatime_begin"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束时间范围"
                v-model="queryParam.datatime_end"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
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
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <!-- <a-button @click="handleAdd" v-has="'swbhz:add'" type="primary" icon="plus">新增</a-button> -->
      <a-button type="primary" icon="download" v-has="'swbhz:dc'" @click="handleExportXls('水稳主表')">导出</a-button>
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" v-has="'swbhz:dr'" icon="import">导入</a-button>
      </a-upload>
    </div>

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
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <!-- <span slot="tags" slot-scope="tags, record">
          <a-tag v-if="record.alarmtypeid == '1'">碾压超速</a-tag>
          <a-tag v-if="record.alarmtypeid == '2'">碾压温度过低</a-tag>
          <a-tag v-if="record.alarmtypeid == '3'">摊铺超速</a-tag>
          <a-tag v-if="record.alarmtypeid == '4'">摊铺温度过低</a-tag>
          <a-tag v-if="record.alarmtypeid == '5'">碾压距摊铺机过远</a-tag>
        </span> -->
        <span slot="tags2" slot-scope="tags1, record">
        <a-tag color="red" v-if="record.overproofStatus === 0">未处理</a-tag>
        <a-tag color="orange" v-if="record.overproofStatus === 10">监理核查中</a-tag>
        <a-tag color="green" v-if="record.overproofStatus === 20">已闭合</a-tag>
        <a-tag color="red" v-if="record.overproofStatus === 30">监理驳回</a-tag>
        <a-tag color="yellow" v-if="record.overproofStatus === 40">指挥部核查中</a-tag>
        <a-tag color="red" v-if="record.overproofStatus === 60">指挥部驳回</a-tag>
      </span>
        <span slot="tags1" slot-scope="tags1, record">
          <a-tag color="green" v-if="record.alarmlevel == '0'">合格</a-tag>
          <a-tag color="orange" v-if="record.alarmlevel == '1'">初级超标</a-tag>
          <a-tag color="yellow" v-if="record.alarmlevel == '2'">中级超标</a-tag>
          <a-tag color="red" v-if="record.alarmlevel == '3'">高级超标</a-tag>
        </span>
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
        <span slot="tags5" slot-scope="tags5, record">
          <a-tag color="green" v-if="record.roadoffset >= 0">右幅</a-tag>
          <a-tag color="green" v-if="record.roadoffset < 0">左幅</a-tag>
        </span>
        <span slot="action" slot-scope="text, record">
          <a v-has="'hntsyj:edit'" @click="handleEdit(record)">编辑</a>
          <a @click="detail(record)">详情</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay" v-show="record.overproofStatus !== 20">
              <a-menu-item
                v-show="record.overproofStatus === 0 || record.overproofStatus === 30 || record.overproofStatus === 60"
              >
                <a @click="showmadelchuzhi(record)">施工处置</a>
              </a-menu-item>
              <a-menu-item v-show="record.overproofStatus === 10">
                <a @click="showmadelshenhe(record)">监理审核</a>
              </a-menu-item>
              <a-menu-item v-show="record.overproofStatus === 40">
                <a @click="showmadelshenpi(record)">指挥部核查</a>
              </a-menu-item>
              <!-- <a-menu-item v-has="'hntsyj:del'">
              <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                <a>删除</a>
              </a-popconfirm>
            </a-menu-item> -->
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <chu-zhi-two ref="chuzhi" @change="change"></chu-zhi-two>
    <shen-he-two ref="shenhe" @change="change"></shen-he-two>
    <shen-pi ref="zhbshenpi" @change="change"></shen-pi>
    <detail ref="detail"></detail>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import HcPavementAlarmModal from './modules/HcPavementAlarmModal'
import ChuZhiTwo from './cbcz/ChuZhiTwo'
import ShenHeTwo from './cbcz/ShenHeTwo'
import ShenPi from './cbcz/ShenPi'
import detail from './cbcz/detail'
export default {
  name: 'HcPavementAlarmList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    ChuZhiTwo,
    ShenHeTwo,
    ShenPi,
    detail,
    HcPavementAlarmModal,
  },
  data() {
    return {
      description: 'hc_pavement_alarm管理页面',
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
          title: '告警类型',
          align: 'center',
          dataIndex: 'alarmtypeid_dictText',
          // scopedSlots: { customRender: 'tags' },
        },
        {
          title: '机械类型id',
          align: 'center',
          dataIndex: 'machinetypeid',
        },
        {
          title: '告警级别',
          align: 'center',
          dataIndex: 'alarmlevel',
          scopedSlots: { customRender: 'tags1' },
        },
        {
          title: '设备sn号',
          align: 'center',
          dataIndex: 'sncode',
        },
        {
          title: '机械id',
          align: 'center',
          dataIndex: 'machineid',
        },
        {
          title: '机械名称',
          align: 'center',
          dataIndex: 'machinename',
        },
        {
          title: '告警时间',
          align: 'center',
          dataIndex: 'datatime',
          customRender: function (text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          },
        },
        {
          title: '标准值',
          align: 'center',
          dataIndex: 'standard',
        },
        {
          title: '真实值',
          align: 'center',
          dataIndex: 'actual',
        },
        {
          title: '经度',
          align: 'center',
          dataIndex: 'lon',
        },
        {
          title: '纬度',
          align: 'center',
          dataIndex: 'lat',
        },
        {
          title: '桩号',
          align: 'center',
          dataIndex: 'roadstation',
        },
        {
          title: '左右幅',
          align: 'center',
          dataIndex: 'roadoffset',
          scopedSlots: { customRender: 'tags5' },
        },
        {
          title: '告警内容',
          align: 'center',
          dataIndex: 'alarminfo',
        },
         {
          title: '处理状态',
          align: 'center',
          dataIndex: 'overproofStatus',
          scopedSlots: { customRender: 'tags2' },
        },
        // {
        //   title: 'projectid',
        //   align: 'center',
        //   dataIndex: 'projectid',
        // },
        // {
        //   title: 'sectionid',
        //   align: 'center',
        //   dataIndex: 'sectionid',
        // },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/hc_pavement_alarm/hcPavementAlarm/cblist',
        delete: '/hc_pavement_alarm/hcPavementAlarm/delete',
        deleteBatch: '/hc_pavement_alarm/hcPavementAlarm/deleteBatch',
        exportXlsUrl: '/hc_pavement_alarm/hcPavementAlarm/exportXls',
        importExcelUrl: '/hc_pavement_alarm/hcPavementAlarm/importExcel',
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    detail: function (record) {
      this.$refs.detail.title = '详情'
      this.$refs.detail.callchuzhishenhe(record)
    },
    change(type) {
      if (type) {
        this.loadData()
      }
    },

    showmadelchuzhi(record) {
      console.log(record, 'record')
      this.$refs.chuzhi.showModal(record)
    },
    showmadelshenhe(record) {
      this.$refs.shenhe.showModal(record)
    },
    showmadelshenpi(record) {
      this.$refs.zhbshenpi.showModal(record)
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({
        type: 'int',
        value: 'alarmtypeid',
        text: '告警类型，1碾压超速，2碾压温度过低，3摊铺超速，4摊铺温度过低，5碾压距摊铺机过远',
      })
      fieldList.push({ type: 'string', value: 'machinetypeid', text: '机械类型id' })
      fieldList.push({ type: 'int', value: 'alarmlevel', text: '告警级别，0为无级别，1为轻微，2为一般，3为严重' })
      fieldList.push({ type: 'string', value: 'sncode', text: '设备sn号' })
      fieldList.push({ type: 'string', value: 'machineid', text: '机械id' })
      fieldList.push({ type: 'string', value: 'machinename', text: '机械名称' })
      fieldList.push({ type: 'date', value: 'datatime', text: '告警时间' })
      fieldList.push({ type: 'string', value: 'standard', text: '标准值' })
      fieldList.push({ type: 'string', value: 'actual', text: '真实值' })
      fieldList.push({ type: 'string', value: 'lon', text: '经度' })
      fieldList.push({ type: 'string', value: 'lat', text: '纬度' })
      fieldList.push({ type: 'string', value: 'roadstation', text: '桩号' })
      fieldList.push({ type: 'string', value: 'roadoffset', text: '左右幅，左负右正' })
      fieldList.push({ type: 'string', value: 'alarminfo', text: '告警内容' })
      fieldList.push({ type: 'string', value: 'projectid', text: 'projectid' })
      fieldList.push({ type: 'string', value: 'sectionid', text: 'sectionid' })
      this.superFieldList = fieldList
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>