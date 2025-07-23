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
      <a-button type="primary" icon="download" @click="handleExportXls('车辆信息历史数据表')">导出</a-button>
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

    <front-device-historydata-modal ref="modalForm" @ok="modalFormOk"></front-device-historydata-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import FrontDeviceHistorydataModal from './modules/FrontDeviceHistorydataModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'

  export default {
    name: 'FrontDeviceHistorydataList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      FrontDeviceHistorydataModal,
      JSuperQuery,
    },
    data () {
      return {
        description: '车辆信息历史数据表管理页面',
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
            title:'设备类型A:压路机、B:摊铺机',
            align:"center",
            dataIndex: 'deviceType'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'shebeiNo'
          },
          {
            title:'项目id',
            align:"center",
            dataIndex: 'projectId'
          },
          {
            title:'速度A:km/h B:m/min',
            align:"center",
            dataIndex: 'speed'
          },
          {
            title:'温度',
            align:"center",
            dataIndex: 'temperature'
          },
          {
            title:'经度',
            align:"center",
            dataIndex: 'longitude'
          },
          {
            title:'纬度',
            align:"center",
            dataIndex: 'latitude'
          },
          {
            title:'数据上传时间',
            align:"center",
            dataIndex: 'datatime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'GPS卫星数',
            align:"center",
            dataIndex: 'gpsnum'
          },
          {
            title:'gps状态',
            align:"center",
            dataIndex: 'gpsstatus'
          },
          {
            title:'电源状态 ',
            align:"center",
            dataIndex: 'switchstatus'
          },
          {
            title:'航向',
            align:"center",
            dataIndex: 'direction'
          },
          {
            title:'高程',
            align:"center",
            dataIndex: 'highly'
          },
          {
            title:'海拔',
            align:"center",
            dataIndex: 'altitude'
          },
          {
            title:'终端时间',
            align:"center",
            dataIndex: 'uploadtime'
          },
          {
            title:'车辆IFRD',
            align:"center",
            dataIndex: 'ifid'
          },
          {
            title:'湿度',
            align:"center",
            dataIndex: 'humidity'
          },
          {
            title:'风速',
            align:"center",
            dataIndex: 'wind'
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
          list: "/clgl/frontDeviceHistorydata/list",
          delete: "/clgl/frontDeviceHistorydata/delete",
          deleteBatch: "/clgl/frontDeviceHistorydata/deleteBatch",
          exportXlsUrl: "/clgl/frontDeviceHistorydata/exportXls",
          importExcelUrl: "clgl/frontDeviceHistorydata/importExcel",
          
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
        fieldList.push({type:'string',value:'deviceType',text:'设备类型A:压路机、B:摊铺机',dictCode:''})
        fieldList.push({type:'string',value:'shebeiNo',text:'设备编号',dictCode:''})
        fieldList.push({type:'string',value:'projectId',text:'项目id',dictCode:''})
        fieldList.push({type:'BigDecimal',value:'speed',text:'速度A:km/h B:m/min',dictCode:''})
        fieldList.push({type:'BigDecimal',value:'temperature',text:'温度',dictCode:''})
        fieldList.push({type:'BigDecimal',value:'longitude',text:'经度',dictCode:''})
        fieldList.push({type:'BigDecimal',value:'latitude',text:'纬度',dictCode:''})
        fieldList.push({type:'date',value:'datatime',text:'数据上传时间'})
        fieldList.push({type:'int',value:'gpsnum',text:'GPS卫星数',dictCode:''})
        fieldList.push({type:'int',value:'gpsstatus',text:'gps状态',dictCode:''})
        fieldList.push({type:'int',value:'switchstatus',text:'电源状态 ',dictCode:''})
        fieldList.push({type:'int',value:'direction',text:'航向',dictCode:''})
        fieldList.push({type:'double',value:'highly',text:'高程',dictCode:''})
        fieldList.push({type:'double',value:'altitude',text:'海拔',dictCode:''})
        fieldList.push({type:'string',value:'uploadtime',text:'终端时间',dictCode:''})
        fieldList.push({type:'string',value:'ifid',text:'车辆IFRD',dictCode:''})
        fieldList.push({type:'BigDecimal',value:'humidity',text:'湿度',dictCode:''})
        fieldList.push({type:'BigDecimal',value:'wind',text:'风速',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>