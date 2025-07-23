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
      <a-button type="primary" icon="download" @click="handleExportXls('中交象山疏港高速(原海德 wbs数据)')">导出</a-button>
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

    <pms-com-wbs-modal ref="modalForm" @ok="modalFormOk"></pms-com-wbs-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PmsComWbsModal from './modules/PmsComWbsModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'

  export default {
    name: 'PmsComWbsList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      PmsComWbsModal,
      JSuperQuery,
    },
    data () {
      return {
        description: '中交象山疏港高速(原海德 wbs数据)管理页面',
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
            title:'sectid',
            align:"center",
            dataIndex: 'sectid'
          },
          {
            title:'wbsIsbn',
            align:"center",
            dataIndex: 'wbsIsbn'
          },
          {
            title:'wbsParent',
            align:"center",
            dataIndex: 'wbsParent'
          },
          {
            title:'wbsName',
            align:"center",
            dataIndex: 'wbsName'
          },
          {
            title:'wbsMoney',
            align:"center",
            dataIndex: 'wbsMoney'
          },
          {
            title:'wbsLevel',
            align:"center",
            dataIndex: 'wbsLevel'
          },
          {
            title:'hasChild',
            align:"center",
            dataIndex: 'hasChild'
          },
          {
            title:'deleFlag',
            align:"center",
            dataIndex: 'deleFlag'
          },
          {
            title:'wbsUnit',
            align:"center",
            dataIndex: 'wbsUnit'
          },
          {
            title:'wbsAmount',
            align:"center",
            dataIndex: 'wbsAmount'
          },
          {
            title:'wbsAmount2',
            align:"center",
            dataIndex: 'wbsAmount2'
          },
          {
            title:'wbsZhBegi',
            align:"center",
            dataIndex: 'wbsZhBegi'
          },
          {
            title:'wbsZhEnd',
            align:"center",
            dataIndex: 'wbsZhEnd'
          },
          {
            title:'wbsMapno',
            align:"center",
            dataIndex: 'wbsMapno'
          },
          {
            title:'wbsType1',
            align:"center",
            dataIndex: 'wbsType1'
          },
          {
            title:'wbsType2',
            align:"center",
            dataIndex: 'wbsType2'
          },
          {
            title:'wbsType3',
            align:"center",
            dataIndex: 'wbsType3'
          },
          {
            title:'sortorder',
            align:"center",
            dataIndex: 'sortorder'
          },
          {
            title:'1 代表已经更新 0 代表未更新',
            align:"center",
            dataIndex: 'status'
          },
          {
            title:'ids',
            align:"center",
            dataIndex: 'ids'
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
          list: "/pmscomwbs/pmsComWbs/list",
          delete: "/pmscomwbs/pmsComWbs/delete",
          deleteBatch: "/pmscomwbs/pmsComWbs/deleteBatch",
          exportXlsUrl: "/pmscomwbs/pmsComWbs/exportXls",
          importExcelUrl: "pmscomwbs/pmsComWbs/importExcel",
          
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
        fieldList.push({type:'string',value:'sectid',text:'sectid',dictCode:''})
        fieldList.push({type:'string',value:'wbsIsbn',text:'wbsIsbn',dictCode:''})
        fieldList.push({type:'string',value:'wbsParent',text:'wbsParent',dictCode:''})
        fieldList.push({type:'string',value:'wbsName',text:'wbsName',dictCode:''})
        fieldList.push({type:'int',value:'wbsMoney',text:'wbsMoney',dictCode:''})
        fieldList.push({type:'int',value:'wbsLevel',text:'wbsLevel',dictCode:''})
        fieldList.push({type:'int',value:'hasChild',text:'hasChild',dictCode:''})
        fieldList.push({type:'int',value:'deleFlag',text:'deleFlag',dictCode:''})
        fieldList.push({type:'string',value:'wbsUnit',text:'wbsUnit',dictCode:''})
        fieldList.push({type:'string',value:'wbsAmount',text:'wbsAmount',dictCode:''})
        fieldList.push({type:'string',value:'wbsAmount2',text:'wbsAmount2',dictCode:''})
        fieldList.push({type:'string',value:'wbsZhBegi',text:'wbsZhBegi',dictCode:''})
        fieldList.push({type:'string',value:'wbsZhEnd',text:'wbsZhEnd',dictCode:''})
        fieldList.push({type:'string',value:'wbsMapno',text:'wbsMapno',dictCode:''})
        fieldList.push({type:'string',value:'wbsType1',text:'wbsType1',dictCode:''})
        fieldList.push({type:'string',value:'wbsType2',text:'wbsType2',dictCode:''})
        fieldList.push({type:'string',value:'wbsType3',text:'wbsType3',dictCode:''})
        fieldList.push({type:'int',value:'sortorder',text:'sortorder',dictCode:''})
        fieldList.push({type:'int',value:'status',text:'1 代表已经更新 0 代表未更新',dictCode:''})
        fieldList.push({type:'int',value:'ids',text:'ids',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>