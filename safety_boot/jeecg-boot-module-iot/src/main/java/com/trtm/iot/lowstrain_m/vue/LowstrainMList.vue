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
      <a-button type="primary" icon="download" @click="handleExportXls('低应变主表')">导出</a-button>
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
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
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

    <lowstrain-m-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import LowstrainMModal from './modules/LowstrainMModal'
  import '@/assets/less/TableExpand.less'

  export default {
    name: "LowstrainMList",
    mixins:[JeecgListMixin],
    components: {
      LowstrainMModal
    },
    data () {
      return {
        description: '低应变主表管理页面',
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
            title:'报检单ID',
            align:"center",
            dataIndex: 'inspectionformid'
          },
          {
            title:'设备商标识',
            align:"center",
            dataIndex: 'vendorid'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'machineno'
          },
          {
            title:'施工部位',
            align:"center",
            dataIndex: 'sgbw'
          },
          {
            title:'流水号(任务单编号)',
            align:"center",
            dataIndex: 'serialno'
          },
          {
            title:'桩id',
            align:"center",
            dataIndex: 'pileid'
          },
          {
            title:'桩编号',
            align:"center",
            dataIndex: 'pileno'
          },
          {
            title:'开始检测时间(yyyy-MM-dd HH:mm:ss)',
            align:"center",
            dataIndex: 'starttime'
          },
          {
            title:'实际桩径（mm）',
            align:"center",
            dataIndex: 'pilediameter'
          },
          {
            title:'实际桩长（m）',
            align:"center",
            dataIndex: 'pilelength'
          },
          {
            title:'预设波速(m/s)',
            align:"center",
            dataIndex: 'velocity'
          },
          {
            title:'检测数量',
            align:"center",
            dataIndex: 'samplecount'
          },
          {
            title:'桩完整性',
            align:"center",
            dataIndex: 'intergrityevaluation'
          },
          {
            title:'桩等级',
            align:"center",
            dataIndex: 'integrityclassification'
          },
          {
            title:'有效数据总条数',
            align:"center",
            dataIndex: 'validdatacount'
          },
          {
            title:'经度',
            align:"center",
            dataIndex: 'gpslongitude'
          },
          {
            title:'纬度',
            align:"center",
            dataIndex: 'gpslatitude'
          },
          {
            title:'上传时间',
            align:"center",
            dataIndex: 'uploaded'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/lowstrain_m/lowstrainM/list",
          delete: "/lowstrain_m/lowstrainM/delete",
          deleteBatch: "/lowstrain_m/lowstrainM/deleteBatch",
          exportXlsUrl: "/lowstrain_m/lowstrainM/exportXls",
          importExcelUrl: "lowstrain_m/lowstrainM/importExcel",
          
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
      }
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
         fieldList.push({type:'int',value:'inspectionformid',text:'报检单ID'})
         fieldList.push({type:'string',value:'vendorid',text:'设备商标识'})
         fieldList.push({type:'string',value:'machineno',text:'设备编号'})
         fieldList.push({type:'string',value:'sgbw',text:'施工部位'})
         fieldList.push({type:'string',value:'serialno',text:'流水号(任务单编号)'})
         fieldList.push({type:'int',value:'pileid',text:'桩id'})
         fieldList.push({type:'string',value:'pileno',text:'桩编号'})
         fieldList.push({type:'string',value:'starttime',text:'开始检测时间(yyyy-MM-dd HH:mm:ss)'})
         fieldList.push({type:'int',value:'pilediameter',text:'实际桩径（mm）'})
         fieldList.push({type:'number',value:'pilelength',text:'实际桩长（m）'})
         fieldList.push({type:'int',value:'velocity',text:'预设波速(m/s)'})
         fieldList.push({type:'int',value:'samplecount',text:'检测数量'})
         fieldList.push({type:'string',value:'intergrityevaluation',text:'桩完整性'})
         fieldList.push({type:'int',value:'integrityclassification',text:'桩等级'})
         fieldList.push({type:'int',value:'validdatacount',text:'有效数据总条数'})
         fieldList.push({type:'string',value:'gpslongitude',text:'经度'})
         fieldList.push({type:'string',value:'gpslatitude',text:'纬度'})
         fieldList.push({type:'string',value:'uploaded',text:'上传时间'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>