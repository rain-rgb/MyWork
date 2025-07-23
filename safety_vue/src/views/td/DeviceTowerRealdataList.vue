<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.deviceCode" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="上报时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.towerdate_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.towerdate_end" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <span slot="tags1" slot-scope="tags1, record">
          <a-tag color="green" v-if="record.mainHookstatus == 0">重量正常</a-tag>
          <a-tag color="orange" v-if="record.mainHookstatus == 1">重量预警</a-tag>
        </span>
        <span slot="tags2" slot-scope="tags2, record">
          <a-tag color="green" v-if="record.reservedVicehookstatus == 0">重量正常</a-tag>
          <a-tag color="orange" v-if="record.reservedVicehookstatus == 1">重量预警</a-tag>
        </span>
        <span slot="tags3" slot-scope="tags3, record">
          <a-tag color="green" v-if="record.mainHookstatus2 == 0">重量正常</a-tag>
          <a-tag color="orange" v-if="record.mainHookstatus2 == 1">吊重超重</a-tag>
        </span>
        <span slot="tags4" slot-scope="tags4, record">
          <a-tag color="green" v-if="record.reservedVicehookstatus2 == 0">重量正常</a-tag>
          <a-tag color="orange" v-if="record.reservedVicehookstatus2 == 1">吊重超重</a-tag>
        </span>

        <span slot="action" slot-scope="text, record">
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <device-tower-realdata-modal ref="modalForm" @ok="modalFormOk"></device-tower-realdata-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { usershebeiList } from '@api/api'
import Vue from 'vue'
import { handertoken } from '@/mixins/getHanderToken'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import DeviceTowerRealdataModal from './modules/DeviceTowerRealdataModal'

export default {
  name: 'DeviceTowerRealdataList',
  mixins: [JeecgListMixin, mixinDevice, handertoken],
  components: {
    DeviceTowerRealdataModal
  },
  data() {
    return {
      description: 'device_tower_realdata管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'deviceCode_dictText'
        },
        {
          title: '上传时间',
          align: 'center',
          dataIndex: 'towerdate',
          customRender: function (text) {
            return !text ? '' : (text.length > 10 ? text : text)
          }
        },
        {
          title: '主钩小车位移',
          align: 'center',
          dataIndex: 'mainHookroute'
        },
        {
          title: '副钩小车位移',
          align: 'center',
          dataIndex: 'reservedViceroute'
        },
        {
          title: '主钩吊钩高度',
          align: 'center',
          dataIndex: 'mainHookheight'
        },
        {
          title: '副钩吊钩高度',
          align: 'center',
          dataIndex: 'reservedVicehookheight'
        },
        {
          title: '主钩吊钩重量',
          align: 'center',
          dataIndex: 'mainHookload'
        },
        {
          title: '副钩吊钩重量',
          align: 'center',
          dataIndex: 'reservedVicehookload'
        },
        {
          title: '主钩力矩值',
          align: 'center',
          dataIndex: 'mainHookmoment'
        },
        {
          title: '副钩力矩值',
          align: 'center',
          dataIndex: 'reservedVicemoment'
        },
        {
          title: '实时风速',
          align: 'center',
          dataIndex: 'windSpeed'
        },
        {
          title: '回转角度',
          align: 'center',
          dataIndex: 'angleRotation'
        },
        {
          title: '主钩吊钩载荷',
          align: 'center',
          dataIndex: 'mainHookstatus',
          scopedSlots: { customRender: 'tags1' }
        },
        {
          title: '副钩吊钩载荷',
          align: 'center',
          dataIndex: 'reservedVicehookstatus',
          scopedSlots: { customRender: 'tags2' }
        },
        {
          title: '主钩吊钩载荷',
          align: 'center',
          dataIndex: 'mainHookstatus2',
          scopedSlots: { customRender: 'tags3' }
        },
        {
          title: '副钩吊钩载荷',
          align: 'center',
          dataIndex: 'reservedVicehookstatus2',
          scopedSlots: { customRender: 'tags4' }
        },
        {
          title: '幅度',
          align: 'center',
          dataIndex: 'amplitude'
        },
        {
          title: '碰撞危险',
          align: 'center',
          dataIndex: 'collisionHazard'
        },
        {
          title: '悬臂倾角',
          align: 'center',
          dataIndex: 'cantileverAngle'
        },
        {
          title: '倾斜',
          align: 'center',
          dataIndex: 'tilt'
        },
        {
          title: '主钩吊钩升降速度',
          align: 'center',
          dataIndex: 'mainHookspeed'
        },
        {
          title: '副钩吊钩升降速度',
          align: 'center',
          dataIndex: 'reservedVicehookSpeed'
        },
        {
          title: '主钩小车移动速度',
          align: 'center',
          dataIndex: 'mainCarspeed'
        },
        {
          title: '副钩小车移动速度',
          align: 'center',
          dataIndex: 'reservedVicecarSpeed'
        },
        {
          title: '累计时间(h)',
          align: 'center',
          dataIndex: 'allTime'
        },
        {
          title: '循环使用次数',
          align: 'center',
          dataIndex: 'allTimes'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/tadiao/deviceTowerRealdata/list',
        delete: '/tadiao/deviceTowerRealdata/delete',
        deleteBatch: '/tadiao/deviceTowerRealdata/deleteBatch',
        exportXlsUrl: '/tadiao/deviceTowerRealdata/exportXls',
        importExcelUrl: 'tadiao/deviceTowerRealdata/importExcel',

      },
      selectValue: '',
      dictOption: [],
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
    this.shebeiList()
    this.getToken()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '51'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'deviceCode', text: '设备编号' })
      fieldList.push({ type: 'int', value: 'deviceType', text: '设备类型2桥门机3架桥机' })
      fieldList.push({ type: 'number', value: 'mainHookroute', text: '主钩小车位移' })
      fieldList.push({ type: 'date', value: 'towerdate', text: '数据上传时间' })
      fieldList.push({ type: 'number', value: 'mainHookheight', text: '主钩吊钩高度' })
      fieldList.push({ type: 'number', value: 'reservedVicehookheight', text: '副钩吊钩高度' })
      fieldList.push({ type: 'number', value: 'mainHookload', text: '主钩吊钩重量' })
      fieldList.push({ type: 'number', value: 'reservedVicehookload', text: '副钩吊钩重量' })
      fieldList.push({ type: 'number', value: 'mainHookmoment', text: '主钩力矩值' })
      fieldList.push({ type: 'number', value: 'reservedViceroute', text: '副钩小车位移' })
      fieldList.push({ type: 'number', value: 'windSpeed', text: '实时风速' })
      fieldList.push({ type: 'number', value: 'reservedVicemoment', text: '副钩力矩值' })
      fieldList.push({ type: 'number', value: 'angleRotation', text: '回转角度' })
      fieldList.push({ type: 'int', value: 'mainHookstatus', text: '主钩吊钩载荷状态0重量正常1重量预警' })
      fieldList.push({ type: 'int', value: 'reservedVicehookstatus', text: '副钩吊钩载荷状态0重量正常1重量预警' })
      fieldList.push({ type: 'int', value: 'mainHookstatus2', text: '主钩吊钩载荷状态0重量正常1吊重超重' })
      fieldList.push({ type: 'int', value: 'reservedVicehookstatus2', text: '副钩吊钩载荷状态0重量正常1吊重超重' })
      fieldList.push({ type: 'number', value: 'amplitude', text: '幅度' })
      fieldList.push({ type: 'int', value: 'allTime', text: '累计时间(单位小时)' })
      fieldList.push({ type: 'int', value: 'allTimes', text: '循环使用次数' })
      fieldList.push({ type: 'int', value: 'collisionHazard', text: '碰撞危险' })
      fieldList.push({ type: 'int', value: 'reservedOne', text: '预留' })
      fieldList.push({ type: 'int', value: 'reservedTwo', text: '预留' })
      fieldList.push({ type: 'number', value: 'cantileverAngle', text: '悬臂倾角' })
      fieldList.push({ type: 'number', value: 'tilt', text: '倾斜' })
      fieldList.push({ type: 'number', value: 'mainHookspeed', text: '主钩吊钩升降速度' })
      fieldList.push({ type: 'number', value: 'mainCarspeed', text: '主钩小车移动速度' })
      fieldList.push({ type: 'number', value: 'reservedVicehookSpeed', text: '副钩吊钩升降速度' })
      fieldList.push({ type: 'number', value: 'reservedVicecarSpeed', text: '副钩小车移动速度' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>