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
      <a-button type="primary" icon="download" @click="handleExportXls('环境监测实时数据')">导出</a-button>
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

    <device-modal ref="modalForm" @ok="modalFormOk"></device-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceModal from './modules/DeviceModal'

  export default {
    name: 'DeviceList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceModal
    },
    data () {
      return {
        description: '环境监测实时数据管理页面',
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
            title:'设备唯一ID',
            align:"center",
            dataIndex: 'devkey'
          },
          {
            title:'设备名',
            align:"center",
            dataIndex: 'devname'
          },
          {
            title:'0为模拟量，1为开关量',
            align:"center",
            dataIndex: 'devtype'
          },
          {
            title:'设备地址(也是设备注册时的编号)',
            align:"center",
            dataIndex: 'devaddr'
          },
          {
            title:'模拟量一名称',
            align:"center",
            dataIndex: 'devtempname'
          },
          {
            title:'模拟量一的值',
            align:"center",
            dataIndex: 'devtempvalue'
          },
          {
            title:'模拟量二名称',
            align:"center",
            dataIndex: 'devhuminame'
          },
          {
            title:'模拟量二值',
            align:"center",
            dataIndex: 'devhumivalue'
          },
          {
            title:'设备状态  flase表示离线，true 表示在线',
            align:"center",
            dataIndex: 'devstatus'
          },
          {
            title:'设备所处纬度',
            align:"center",
            dataIndex: 'devlng'
          },
          {
            title:'设备所处经度',
            align:"center",
            dataIndex: 'devlat'
          },
          {
            title:'模拟量一报警状态（0 表示不报警，1 表示超上限，2 表示超下限）',
            align:"center",
            dataIndex: 'tempstatus'
          },
          {
            title:'模拟量二报警状态（0 表示不报警，1 表示超上限，2 表示超下限）',
            align:"center",
            dataIndex: 'humistatus'
          },
          {
            title:'模拟量一相关参数设置标志（0 表示不具备设置权限，1 表示有设置权限）',
            align:"center",
            dataIndex: 'devdatatype1'
          },
          {
            title:'模拟量二相关参数设置标志（0 表示不具备设置权限，1 表示有设置权限）',
            align:"center",
            dataIndex: 'devdatatype2'
          },
          {
            title:'设备节点号',
            align:"center",
            dataIndex: 'devpos'
          },
          {
            title:'数据保存的时间点',
            align:"center",
            dataIndex: 'timevalue',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
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
          list: "/device/device/list",
          delete: "/device/device/delete",
          deleteBatch: "/device/device/deleteBatch",
          exportXlsUrl: "/device/device/exportXls",
          importExcelUrl: "device/device/importExcel",
          
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
        fieldList.push({type:'int',value:'devkey',text:'设备唯一ID',dictCode:''})
        fieldList.push({type:'string',value:'devname',text:'设备名',dictCode:''})
        fieldList.push({type:'int',value:'devtype',text:'0为模拟量，1为开关量',dictCode:''})
        fieldList.push({type:'string',value:'devaddr',text:'设备地址(也是设备注册时的编号)',dictCode:''})
        fieldList.push({type:'string',value:'devtempname',text:'模拟量一名称',dictCode:''})
        fieldList.push({type:'string',value:'devtempvalue',text:'模拟量一的值',dictCode:''})
        fieldList.push({type:'string',value:'devhuminame',text:'模拟量二名称',dictCode:''})
        fieldList.push({type:'string',value:'devhumivalue',text:'模拟量二值',dictCode:''})
        fieldList.push({type:'string',value:'devstatus',text:'设备状态  flase表示离线，true 表示在线',dictCode:''})
        fieldList.push({type:'BigDecimal',value:'devlng',text:'设备所处纬度',dictCode:''})
        fieldList.push({type:'BigDecimal',value:'devlat',text:'设备所处经度',dictCode:''})
        fieldList.push({type:'int',value:'tempstatus',text:'模拟量一报警状态（0 表示不报警，1 表示超上限，2 表示超下限）',dictCode:''})
        fieldList.push({type:'int',value:'humistatus',text:'模拟量二报警状态（0 表示不报警，1 表示超上限，2 表示超下限）',dictCode:''})
        fieldList.push({type:'string',value:'devdatatype1',text:'模拟量一相关参数设置标志（0 表示不具备设置权限，1 表示有设置权限）',dictCode:''})
        fieldList.push({type:'string',value:'devdatatype2',text:'模拟量二相关参数设置标志（0 表示不具备设置权限，1 表示有设置权限）',dictCode:''})
        fieldList.push({type:'int',value:'devpos',text:'设备节点号',dictCode:''})
        fieldList.push({type:'date',value:'timevalue',text:'数据保存的时间点'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>