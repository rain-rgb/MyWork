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
      <a-button type="primary" icon="download" @click="handleExportXls('环境监测数据统计表')">导出</a-button>
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

    <devicehistory-static-modal ref="modalForm" @ok="modalFormOk"></devicehistory-static-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DevicehistoryStaticModal from './modules/DevicehistoryStaticModal'

  export default {
    name: 'DevicehistoryStaticList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DevicehistoryStaticModal
    },
    data () {
      return {
        description: '环境监测数据统计表管理页面',
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
            title:'设备地址',
            align:"center",
            dataIndex: 'devaddr'
          },
          {
            title:'总数',
            align:"center",
            dataIndex: 'total'
          },
          {
            title:'超标总数',
            align:"center",
            dataIndex: 'chaototal'
          },
          {
            title:'初级超标数',
            align:"center",
            dataIndex: 'primaryTotal'
          },
          {
            title:'pm25超标总数',
            align:"center",
            dataIndex: 'pm25chao'
          },
          {
            title:'pm10超标总数',
            align:"center",
            dataIndex: 'pm10chao'
          },
          {
            title:'噪音超标总数',
            align:"center",
            dataIndex: 'noisechao'
          },
          {
            title:'温度超标数',
            align:"center",
            dataIndex: 'temchao'
          },
          {
            title:'湿度超标数',
            align:"center",
            dataIndex: 'humchao'
          },
          {
            title:'风力超标数',
            align:"center",
            dataIndex: 'winchao'
          },
          {
            title:'风速超标数',
            align:"center",
            dataIndex: 'winsdchao'
          },
          {
            title:'统计时间，以天为单位',
            align:"center",
            dataIndex: 'statisticsTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'中级超标数',
            align:"center",
            dataIndex: 'middleTotal'
          },
          {
            title:'高级超标数',
            align:"center",
            dataIndex: 'advanceTotal'
          },
          {
            title:'pm25初级超标数',
            align:"center",
            dataIndex: 'pm25primary'
          },
          {
            title:'pm25中级超标数',
            align:"center",
            dataIndex: 'pm25middle'
          },
          {
            title:'pm25高级超标数',
            align:"center",
            dataIndex: 'pm25advance'
          },
          {
            title:'pm10初级超标数',
            align:"center",
            dataIndex: 'pm10primary'
          },
          {
            title:'pm10中级超标数',
            align:"center",
            dataIndex: 'pm10middle'
          },
          {
            title:'pm10高级超标数',
            align:"center",
            dataIndex: 'pm10advance'
          },
          {
            title:'噪声初级超标数',
            align:"center",
            dataIndex: 'noiseprimary'
          },
          {
            title:'噪声中级超标数',
            align:"center",
            dataIndex: 'noisemiddle'
          },
          {
            title:'噪声高级超标数',
            align:"center",
            dataIndex: 'noiseadvance'
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
          list: "/devicehistorystatic/devicehistoryStatic/list",
          delete: "/devicehistorystatic/devicehistoryStatic/delete",
          deleteBatch: "/devicehistorystatic/devicehistoryStatic/deleteBatch",
          exportXlsUrl: "/devicehistorystatic/devicehistoryStatic/exportXls",
          importExcelUrl: "devicehistorystatic/devicehistoryStatic/importExcel",
          
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
        fieldList.push({type:'string',value:'devaddr',text:'设备地址',dictCode:''})
        fieldList.push({type:'int',value:'total',text:'总数',dictCode:''})
        fieldList.push({type:'int',value:'chaototal',text:'超标总数',dictCode:''})
        fieldList.push({type:'int',value:'primaryTotal',text:'初级超标数',dictCode:''})
        fieldList.push({type:'int',value:'pm25chao',text:'pm25超标总数',dictCode:''})
        fieldList.push({type:'int',value:'pm10chao',text:'pm10超标总数',dictCode:''})
        fieldList.push({type:'int',value:'noisechao',text:'噪音超标总数',dictCode:''})
        fieldList.push({type:'int',value:'temchao',text:'温度超标数',dictCode:''})
        fieldList.push({type:'int',value:'humchao',text:'湿度超标数',dictCode:''})
        fieldList.push({type:'int',value:'winchao',text:'风力超标数',dictCode:''})
        fieldList.push({type:'int',value:'winsdchao',text:'风速超标数',dictCode:''})
        fieldList.push({type:'date',value:'statisticsTime',text:'统计时间，以天为单位'})
        fieldList.push({type:'int',value:'middleTotal',text:'中级超标数',dictCode:''})
        fieldList.push({type:'int',value:'advanceTotal',text:'高级超标数',dictCode:''})
        fieldList.push({type:'int',value:'pm25primary',text:'pm25初级超标数',dictCode:''})
        fieldList.push({type:'int',value:'pm25middle',text:'pm25中级超标数',dictCode:''})
        fieldList.push({type:'int',value:'pm25advance',text:'pm25高级超标数',dictCode:''})
        fieldList.push({type:'int',value:'pm10primary',text:'pm10初级超标数',dictCode:''})
        fieldList.push({type:'int',value:'pm10middle',text:'pm10中级超标数',dictCode:''})
        fieldList.push({type:'int',value:'pm10advance',text:'pm10高级超标数',dictCode:''})
        fieldList.push({type:'int',value:'noiseprimary',text:'噪声初级超标数',dictCode:''})
        fieldList.push({type:'int',value:'noisemiddle',text:'噪声中级超标数',dictCode:''})
        fieldList.push({type:'int',value:'noiseadvance',text:'噪声高级超标数',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>