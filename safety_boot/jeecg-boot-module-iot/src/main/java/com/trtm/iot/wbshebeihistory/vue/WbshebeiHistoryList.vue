<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('电子锁历史数据表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <wbshebei-history-modal ref="modalForm" @ok="modalFormOk"></wbshebei-history-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WbshebeiHistoryModal from './modules/WbshebeiHistoryModal'

  export default {
    name: 'WbshebeiHistoryList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WbshebeiHistoryModal
    },
    data () {
      return {
        description: '电子锁历史数据表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'teid'
          },
          {
            title:'时间',
            align:"center",
            dataIndex: 'time',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'纬度',
            align:"center",
            dataIndex: 'lat'
          },
          {
            title:'经度',
            align:"center",
            dataIndex: 'lng'
          },
          {
            title:'方向',
            align:"center",
            dataIndex: 'direction'
          },
          {
            title:'高度',
            align:"center",
            dataIndex: 'height'
          },
          {
            title:'车辆状态',
            align:"center",
            dataIndex: 'carState'
          },
          {
            title:'teState',
            align:"center",
            dataIndex: 'teState'
          },
          {
            title:'预警状态',
            align:"center",
            dataIndex: 'alarmState'
          },
          {
            title:'电量',
            align:"center",
            dataIndex: 'batteryVol'
          },
          {
            title:'基站',
            align:"center",
            dataIndex: 'baseStation'
          },
          {
            title:'数据包类型',
            align:"center",
            dataIndex: 'packetType'
          },
          {
            title:'sublock',
            align:"center",
            dataIndex: 'sublock'
          },
          {
            title:'业务编号',
            align:"center",
            dataIndex: 'businessId'
          },
          {
            title:'射频识别',
            align:"center",
            dataIndex: 'rfid'
          },
          {
            title:'温度',
            align:"center",
            dataIndex: 'temp'
          },
          {
            title:'湿度',
            align:"center",
            dataIndex: 'humidity'
          },
          {
            title:'里程',
            align:"center",
            dataIndex: 'mileage'
          },
          {
            title:'油量',
            align:"center",
            dataIndex: 'fuel'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/wbshebeihistory/wbshebeiHistory/list",
          delete: "/wbshebeihistory/wbshebeiHistory/delete",
          deleteBatch: "/wbshebeihistory/wbshebeiHistory/deleteBatch",
          exportXlsUrl: "/wbshebeihistory/wbshebeiHistory/exportXls",
          importExcelUrl: "wbshebeihistory/wbshebeiHistory/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'teid',text:'设备编号',dictCode:''})
        fieldList.push({type:'date',value:'time',text:'时间'})
        fieldList.push({type:'string',value:'lat',text:'纬度',dictCode:''})
        fieldList.push({type:'string',value:'lng',text:'经度',dictCode:''})
        fieldList.push({type:'int',value:'direction',text:'方向',dictCode:''})
        fieldList.push({type:'double',value:'height',text:'高度',dictCode:''})
        fieldList.push({type:'double',value:'carState',text:'车辆状态',dictCode:''})
        fieldList.push({type:'double',value:'teState',text:'teState',dictCode:''})
        fieldList.push({type:'double',value:'alarmState',text:'预警状态',dictCode:''})
        fieldList.push({type:'double',value:'batteryVol',text:'电量',dictCode:''})
        fieldList.push({type:'string',value:'baseStation',text:'基站',dictCode:''})
        fieldList.push({type:'string',value:'packetType',text:'数据包类型',dictCode:''})
        fieldList.push({type:'string',value:'sublock',text:'sublock',dictCode:''})
        fieldList.push({type:'string',value:'businessId',text:'业务编号',dictCode:''})
        fieldList.push({type:'string',value:'rfid',text:'射频识别',dictCode:''})
        fieldList.push({type:'int',value:'temp',text:'温度',dictCode:''})
        fieldList.push({type:'double',value:'humidity',text:'湿度',dictCode:''})
        fieldList.push({type:'double',value:'mileage',text:'里程',dictCode:''})
        fieldList.push({type:'double',value:'fuel',text:'油量',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>