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
      <a-button type="primary" icon="download" @click="handleExportXls('孔道灌浆主表')">导出</a-button>
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

    <kongdaoyaj-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import KongdaoyajModal from './modules/KongdaoyajModal'
  import '@/assets/less/TableExpand.less'

  export default {
    name: "KongdaoyajList",
    mixins:[JeecgListMixin],
    components: {
      KongdaoyajModal
    },
    data () {
      return {
        description: '孔道灌浆主表管理页面',
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
            title:'json文件唯一标识',
            align:"center",
            dataIndex: 'uuid'
          },
          {
            title:'json文件唯一标识',
            align:"center",
            dataIndex: 'uuid'
          },
          {
            title:'项目所在省份',
            align:"center",
            dataIndex: 'province'
          },
          {
            title:'项目所在省份',
            align:"center",
            dataIndex: 'province'
          },
          {
            title:'项目所属行业',
            align:"center",
            dataIndex: 'detectindustry'
          },
          {
            title:'项目所属行业',
            align:"center",
            dataIndex: 'detectindustry'
          },
          {
            title:'检测性质',
            align:"center",
            dataIndex: 'detectnaturenum'
          },
          {
            title:'检测性质',
            align:"center",
            dataIndex: 'detectnaturenum'
          },
          {
            title:'检测类型',
            align:"center",
            dataIndex: 'detecttype'
          },
          {
            title:'检测类型',
            align:"center",
            dataIndex: 'detecttype'
          },
          {
            title:'检测单位',
            align:"center",
            dataIndex: 'detectunit'
          },
          {
            title:'检测单位',
            align:"center",
            dataIndex: 'detectunit'
          },
          {
            title:'设备厂商',
            align:"center",
            dataIndex: 'devicecompany'
          },
          {
            title:'设备厂商',
            align:"center",
            dataIndex: 'devicecompany'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remarks'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remarks'
          },
          {
            title:'工程地点',
            align:"center",
            dataIndex: 'projplace'
          },
          {
            title:'工程地点',
            align:"center",
            dataIndex: 'projplace'
          },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'workname'
          },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'workname'
          },
          {
            title:'检测人员',
            align:"center",
            dataIndex: 'detectperson'
          },
          {
            title:'检测人员',
            align:"center",
            dataIndex: 'detectperson'
          },
          {
            title:'保留字段',
            align:"center",
            dataIndex: 'detectparamno'
          },
          {
            title:'保留字段',
            align:"center",
            dataIndex: 'detectparamno'
          },
          {
            title:'保留字段',
            align:"center",
            dataIndex: 'programname'
          },
          {
            title:'保留字段',
            align:"center",
            dataIndex: 'programname'
          },
          {
            title:'检测大类',
            align:"center",
            dataIndex: 'detectparam'
          },
          {
            title:'检测大类',
            align:"center",
            dataIndex: 'detectparam'
          },
          {
            title:'检测小类',
            align:"center",
            dataIndex: 'detectclass'
          },
          {
            title:'检测小类',
            align:"center",
            dataIndex: 'detectclass'
          },
          {
            title:'施工单位',
            align:"center",
            dataIndex: 'consunit'
          },
          {
            title:'施工单位',
            align:"center",
            dataIndex: 'consunit'
          },
          {
            title:'监理单位',
            align:"center",
            dataIndex: 'supervisionunit'
          },
          {
            title:'监理单位',
            align:"center",
            dataIndex: 'supervisionunit'
          },
          {
            title:'建设单位',
            align:"center",
            dataIndex: 'employer'
          },
          {
            title:'建设单位',
            align:"center",
            dataIndex: 'employer'
          },
          {
            title:'试验编号',
            align:"center",
            dataIndex: 'detectno'
          },
          {
            title:'试验编号',
            align:"center",
            dataIndex: 'detectno'
          },
          {
            title:'保留字段',
            align:"center",
            dataIndex: 'pushprojectname'
          },
          {
            title:'保留字段',
            align:"center",
            dataIndex: 'pushprojectname'
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
          list: "/kongdaoyaj/kongdaoyaj/list",
          delete: "/kongdaoyaj/kongdaoyaj/delete",
          deleteBatch: "/kongdaoyaj/kongdaoyaj/deleteBatch",
          exportXlsUrl: "/kongdaoyaj/kongdaoyaj/exportXls",
          importExcelUrl: "kongdaoyaj/kongdaoyaj/importExcel",
          
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
         fieldList.push({type:'string',value:'uuid',text:'json文件唯一标识'})
         fieldList.push({type:'string',value:'uuid',text:'json文件唯一标识',dictCode:''})
         fieldList.push({type:'string',value:'province',text:'项目所在省份',dictCode:''})
         fieldList.push({type:'string',value:'province',text:'项目所在省份'})
         fieldList.push({type:'string',value:'detectindustry',text:'项目所属行业',dictCode:''})
         fieldList.push({type:'string',value:'detectindustry',text:'项目所属行业'})
         fieldList.push({type:'string',value:'detectnaturenum',text:'检测性质',dictCode:''})
         fieldList.push({type:'string',value:'detectnaturenum',text:'检测性质'})
         fieldList.push({type:'string',value:'detecttype',text:'检测类型',dictCode:''})
         fieldList.push({type:'string',value:'detecttype',text:'检测类型'})
         fieldList.push({type:'string',value:'detectunit',text:'检测单位'})
         fieldList.push({type:'string',value:'detectunit',text:'检测单位',dictCode:''})
         fieldList.push({type:'string',value:'devicecompany',text:'设备厂商',dictCode:''})
         fieldList.push({type:'string',value:'devicecompany',text:'设备厂商'})
         fieldList.push({type:'Text',value:'remarks',text:'备注',dictCode:''})
         fieldList.push({type:'string',value:'remarks',text:'备注'})
         fieldList.push({type:'string',value:'projplace',text:'工程地点',dictCode:''})
         fieldList.push({type:'string',value:'projplace',text:'工程地点'})
         fieldList.push({type:'string',value:'workname',text:'工程名称'})
         fieldList.push({type:'string',value:'workname',text:'工程名称',dictCode:''})
         fieldList.push({type:'string',value:'detectperson',text:'检测人员'})
         fieldList.push({type:'string',value:'detectperson',text:'检测人员',dictCode:''})
         fieldList.push({type:'string',value:'detectparamno',text:'保留字段',dictCode:''})
         fieldList.push({type:'string',value:'detectparamno',text:'保留字段'})
         fieldList.push({type:'string',value:'programname',text:'保留字段'})
         fieldList.push({type:'string',value:'programname',text:'保留字段',dictCode:''})
         fieldList.push({type:'string',value:'detectparam',text:'检测大类'})
         fieldList.push({type:'string',value:'detectparam',text:'检测大类',dictCode:''})
         fieldList.push({type:'string',value:'detectclass',text:'检测小类',dictCode:''})
         fieldList.push({type:'string',value:'detectclass',text:'检测小类'})
         fieldList.push({type:'string',value:'consunit',text:'施工单位'})
         fieldList.push({type:'string',value:'consunit',text:'施工单位',dictCode:''})
         fieldList.push({type:'string',value:'supervisionunit',text:'监理单位',dictCode:''})
         fieldList.push({type:'string',value:'supervisionunit',text:'监理单位'})
         fieldList.push({type:'string',value:'employer',text:'建设单位',dictCode:''})
         fieldList.push({type:'string',value:'employer',text:'建设单位'})
         fieldList.push({type:'string',value:'detectno',text:'试验编号'})
         fieldList.push({type:'string',value:'detectno',text:'试验编号',dictCode:''})
         fieldList.push({type:'string',value:'pushprojectname',text:'保留字段'})
         fieldList.push({type:'string',value:'pushprojectname',text:'保留字段',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>