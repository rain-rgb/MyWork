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
      <a-button type="primary" icon="download" @click="handleExportXls('人员定位实时表')">导出</a-button>
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

    <device-tunnel-positiondata-real-modal ref="modalForm" @ok="modalFormOk"></device-tunnel-positiondata-real-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceTunnelPositiondataRealModal from './modules/DeviceTunnelPositiondataRealModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'

  export default {
    name: 'DeviceTunnelPositiondataRealList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceTunnelPositiondataRealModal,
      JSuperQuery,
    },
    data () {
      return {
        description: '人员定位实时表管理页面',
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
            title:'人员姓名',
            align:"center",
            dataIndex: 'username'
          },
          {
            title:'卡号',
            align:"center",
            dataIndex: 'card'
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
            title:'距离洞口距离',
            align:"center",
            dataIndex: 'dv'
          },
          {
            title:'定位时间',
            align:"center",
            dataIndex: 'dvtime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'定位基站名称',
            align:"center",
            dataIndex: 'jz'
          },
          {
            title:'距离基站距离',
            align:"center",
            dataIndex: 'jzdv'
          },
          {
            title:'基站距离洞口距离',
            align:"center",
            dataIndex: 'jzkou'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'shebeino'
          },
          {
            title:'基站位置',
            align:"center",
            dataIndex: 'jzwz'
          },
          {
            title:'班组编号',
            align:"center",
            dataIndex: 'departmentid'
          },
          {
            title:'班组名称',
            align:"center",
            dataIndex: 'departname'
          },
          {
            title:'X坐标',
            align:"center",
            dataIndex: 'nx'
          },
          {
            title:'Y坐标',
            align:"center",
            dataIndex: 'ny'
          },
          {
            title:'电池电量',
            align:"center",
            dataIndex: 'voltages'
          },
          {
            title:'报警状态0：无报警，1：按钮报警',
            align:"center",
            dataIndex: 'alarms'
          },
          {
            title:'上报时间',
            align:"center",
            dataIndex: 'uploadtime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'guid',
            align:"center",
            dataIndex: 'guid'
          },
          {
            title:'职务',
            align:"center",
            dataIndex: 'zhiwu'
          },
          {
            title:'职务id',
            align:"center",
            dataIndex: 'zhiwuid'
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
          list: "/devicetunnelpositiondatareal/deviceTunnelPositiondataReal/list",
          delete: "/devicetunnelpositiondatareal/deviceTunnelPositiondataReal/delete",
          deleteBatch: "/devicetunnelpositiondatareal/deviceTunnelPositiondataReal/deleteBatch",
          exportXlsUrl: "/devicetunnelpositiondatareal/deviceTunnelPositiondataReal/exportXls",
          importExcelUrl: "devicetunnelpositiondatareal/deviceTunnelPositiondataReal/importExcel",
          
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
        fieldList.push({type:'string',value:'username',text:'人员姓名',dictCode:''})
        fieldList.push({type:'string',value:'card',text:'卡号',dictCode:''})
        fieldList.push({type:'date',value:'datatime',text:'数据上传时间'})
        fieldList.push({type:'double',value:'dv',text:'距离洞口距离',dictCode:''})
        fieldList.push({type:'date',value:'dvtime',text:'定位时间'})
        fieldList.push({type:'string',value:'jz',text:'定位基站名称',dictCode:''})
        fieldList.push({type:'double',value:'jzdv',text:'距离基站距离',dictCode:''})
        fieldList.push({type:'double',value:'jzkou',text:'基站距离洞口距离',dictCode:''})
        fieldList.push({type:'string',value:'shebeino',text:'设备编号',dictCode:''})
        fieldList.push({type:'string',value:'jzwz',text:'基站位置',dictCode:''})
        fieldList.push({type:'string',value:'departmentid',text:'班组编号',dictCode:''})
        fieldList.push({type:'string',value:'departname',text:'班组名称',dictCode:''})
        fieldList.push({type:'double',value:'nx',text:'X坐标',dictCode:''})
        fieldList.push({type:'double',value:'ny',text:'Y坐标',dictCode:''})
        fieldList.push({type:'double',value:'voltages',text:'电池电量',dictCode:''})
        fieldList.push({type:'int',value:'alarms',text:'报警状态0：无报警，1：按钮报警',dictCode:''})
        fieldList.push({type:'date',value:'uploadtime',text:'上报时间'})
        fieldList.push({type:'string',value:'guid',text:'guid',dictCode:''})
        fieldList.push({type:'string',value:'zhiwu',text:'职务',dictCode:''})
        fieldList.push({type:'int',value:'zhiwuid',text:'职务id',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>