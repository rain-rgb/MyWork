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
      <a-button type="primary" icon="download" @click="handleExportXls('device_tunnel_environmentdata_real')">导出</a-button>
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

    <device-tunnel-environmentdata-real-modal ref="modalForm" @ok="modalFormOk"></device-tunnel-environmentdata-real-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceTunnelEnvironmentdataRealModal from './modules/DeviceTunnelEnvironmentdataRealModal'

  export default {
    name: 'DeviceTunnelEnvironmentdataRealList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceTunnelEnvironmentdataRealModal
    },
    data () {
      return {
        description: 'device_tunnel_environmentdata_real管理页面',
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
            title:'地点',
            align:"center",
            dataIndex: 'site'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'shebeino'
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
            title:'0无报警，1低报警，2高报警',
            align:"center",
            dataIndex: 'alarmtype'
          },
          {
            title:'氧气',
            align:"center",
            dataIndex: 'oxygen'
          },
          {
            title:'甲烷',
            align:"center",
            dataIndex: 'methane'
          },
          {
            title:'硫化氢',
            align:"center",
            dataIndex: 'hSulfide'
          },
          {
            title:'二氧化碳',
            align:"center",
            dataIndex: 'cDioxide'
          },
          {
            title:'一氧化碳',
            align:"center",
            dataIndex: 'cMonoxide'
          },
          {
            title:'风速',
            align:"center",
            dataIndex: 'windspeed'
          },
          {
            title:'数据时间',
            align:"center",
            dataIndex: 'uploadtime'
          },
          {
            title:'温度',
            align:"center",
            dataIndex: 'temperature'
          },
          {
            title:'guid',
            align:"center",
            dataIndex: 'guid'
          },
          {
            title:'粉尘',
            align:"center",
            dataIndex: 'dust'
          },
          {
            title:'噪声',
            align:"center",
            dataIndex: 'noise'
          },
          {
            title:'PM1.0',
            align:"center",
            dataIndex: 'pm10'
          },
          {
            title:'PM2.0',
            align:"center",
            dataIndex: 'pm20'
          },
          {
            title:'PM10',
            align:"center",
            dataIndex: 'pm10'
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
          list: "/deviceTunnelEnvironmentdataReal/deviceTunnelEnvironmentdataReal/list",
          delete: "/deviceTunnelEnvironmentdataReal/deviceTunnelEnvironmentdataReal/delete",
          deleteBatch: "/deviceTunnelEnvironmentdataReal/deviceTunnelEnvironmentdataReal/deleteBatch",
          exportXlsUrl: "/deviceTunnelEnvironmentdataReal/deviceTunnelEnvironmentdataReal/exportXls",
          importExcelUrl: "deviceTunnelEnvironmentdataReal/deviceTunnelEnvironmentdataReal/importExcel",
          
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
        fieldList.push({type:'string',value:'site',text:'地点'})
        fieldList.push({type:'string',value:'shebeino',text:'设备编号'})
        fieldList.push({type:'date',value:'datatime',text:'数据上传时间'})
        fieldList.push({type:'int',value:'alarmtype',text:'0无报警，1低报警，2高报警'})
        fieldList.push({type:'string',value:'oxygen',text:'氧气'})
        fieldList.push({type:'string',value:'methane',text:'甲烷'})
        fieldList.push({type:'string',value:'hSulfide',text:'硫化氢'})
        fieldList.push({type:'string',value:'cDioxide',text:'二氧化碳'})
        fieldList.push({type:'string',value:'cMonoxide',text:'一氧化碳'})
        fieldList.push({type:'string',value:'windspeed',text:'风速'})
        fieldList.push({type:'string',value:'uploadtime',text:'数据时间'})
        fieldList.push({type:'string',value:'temperature',text:'温度'})
        fieldList.push({type:'string',value:'guid',text:'guid'})
        fieldList.push({type:'string',value:'dust',text:'粉尘'})
        fieldList.push({type:'string',value:'noise',text:'噪声'})
        fieldList.push({type:'string',value:'pm10',text:'PM1.0'})
        fieldList.push({type:'string',value:'pm20',text:'PM2.0'})
        fieldList.push({type:'string',value:'pm10',text:'PM10'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>