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
      <a-button type="primary" icon="download" @click="handleExportXls('device_pipepile_historydata')">导出</a-button>
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

    <device-pipepile-historydata-modal ref="modalForm" @ok="modalFormOk"></device-pipepile-historydata-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DevicePipepileHistorydataModal from './modules/DevicePipepileHistorydataModal'

  export default {
    name: 'DevicePipepileHistorydataList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DevicePipepileHistorydataModal
    },
    data () {
      return {
        description: 'device_pipepile_historydata管理页面',
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
            dataIndex: 'shebeino'
          },
          {
            title:'桩号',
            align:"center",
            dataIndex: 'pileno'
          },
          {
            title:'设计桩长(m)',
            align:"center",
            dataIndex: 'designdep'
          },
          {
            title:'当前桩长度(m)',
            align:"center",
            dataIndex: 'curdep'
          },
          {
            title:'离地桩长(m)',
            align:"center",
            dataIndex: 'grounddep'
          },
          {
            title:'最大垂直度(%)',
            align:"center",
            dataIndex: 'pileY'
          },
          {
            title:'最大压桩力(KN)',
            align:"center",
            dataIndex: 'pileUpress'
          },
          {
            title:'最大夹持力(KN)',
            align:"center",
            dataIndex: 'pileDpress'
          },
          {
            title:'平均桩压力(KN)',
            align:"center",
            dataIndex: 'pileAveupress'
          },
          {
            title:'平均夹持力(KN)',
            align:"center",
            dataIndex: 'pileAvedpress'
          },
          {
            title:'开始时间',
            align:"center",
            dataIndex: 'pileStarttime'
          },
          {
            title:'数据时间',
            align:"center",
            dataIndex: 'datatime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'速度',
            align:"center",
            dataIndex: 'speed'
          },
          {
            title:'接桩时间',
            align:"center",
            dataIndex: 'piletime'
          },
          {
            title:'有效桩长(m)',
            align:"center",
            dataIndex: 'pileEffdep'
          },
          {
            title:'时长',
            align:"center",
            dataIndex: 'pileWorktime'
          },
          {
            title:'打桩次数',
            align:"center",
            dataIndex: 'times'
          },
          {
            title:'工作状态',
            align:"center",
            dataIndex: 'worksta'
          },
          {
            title:'时间戳',
            align:"center",
            dataIndex: 'ts'
          },
          {
            title:'经度',
            align:"center",
            dataIndex: 'ltdfloat'
          },
          {
            title:'纬度',
            align:"center",
            dataIndex: 'lgdfloat'
          },
          {
            title:'里程',
            align:"center",
            dataIndex: 'mileage'
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
          list: "/devicepipepilehistorydata/devicePipepileHistorydata/list",
          delete: "/devicepipepilehistorydata/devicePipepileHistorydata/delete",
          deleteBatch: "/devicepipepilehistorydata/devicePipepileHistorydata/deleteBatch",
          exportXlsUrl: "/devicepipepilehistorydata/devicePipepileHistorydata/exportXls",
          importExcelUrl: "devicepipepilehistorydata/devicePipepileHistorydata/importExcel",
          
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
        fieldList.push({type:'string',value:'shebeino',text:'设备编号'})
        fieldList.push({type:'string',value:'pileno',text:'桩号'})
        fieldList.push({type:'string',value:'designdep',text:'设计桩长(m)'})
        fieldList.push({type:'string',value:'curdep',text:'当前桩长度(m)'})
        fieldList.push({type:'string',value:'grounddep',text:'离地桩长(m)'})
        fieldList.push({type:'string',value:'pileY',text:'最大垂直度(%)'})
        fieldList.push({type:'string',value:'pileUpress',text:'最大压桩力(KN)'})
        fieldList.push({type:'string',value:'pileDpress',text:'最大夹持力(KN)'})
        fieldList.push({type:'string',value:'pileAveupress',text:'平均桩压力(KN)'})
        fieldList.push({type:'string',value:'pileAvedpress',text:'平均夹持力(KN)'})
        fieldList.push({type:'string',value:'pileStarttime',text:'开始时间'})
        fieldList.push({type:'date',value:'datatime',text:'数据时间'})
        fieldList.push({type:'string',value:'speed',text:'速度'})
        fieldList.push({type:'string',value:'piletime',text:'接桩时间'})
        fieldList.push({type:'string',value:'pileEffdep',text:'有效桩长(m)'})
        fieldList.push({type:'string',value:'pileWorktime',text:'时长'})
        fieldList.push({type:'int',value:'times',text:'打桩次数'})
        fieldList.push({type:'string',value:'worksta',text:'工作状态'})
        fieldList.push({type:'string',value:'ts',text:'时间戳'})
        fieldList.push({type:'string',value:'ltdfloat',text:'经度'})
        fieldList.push({type:'string',value:'lgdfloat',text:'纬度'})
        fieldList.push({type:'string',value:'mileage',text:'里程'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>