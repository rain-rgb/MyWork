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
      <a-button type="primary" icon="download" @click="handleExportXls('w_hwgpy')">导出</a-button>
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

    <w-hwgpy-modal ref="modalForm" @ok="modalFormOk"></w-hwgpy-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WHwgpyModal from './modules/WHwgpyModal'

  export default {
    name: 'WHwgpyList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WHwgpyModal
    },
    data () {
      return {
        description: 'w_hwgpy管理页面',
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
            title:'设备归属',
            align:"center",
            dataIndex: 'customer'
          },
          {
            title:'项目id',
            align:"center",
            dataIndex: 'projectid'
          },
          {
            title:'项目名称',
            align:"center",
            dataIndex: 'projectname'
          },
          {
            title:'沥青种类',
            align:"center",
            dataIndex: 'product'
          },
          {
            title:'车号',
            align:"center",
            dataIndex: 'grade'
          },
          {
            title:'标段',
            align:"center",
            dataIndex: 'source'
          },
          {
            title:'0代表匹配，1代表不匹配',
            align:"center",
            dataIndex: 'result'
          },
          {
            title:'试验的时间yyyy-MM-dd HH:mm:ss',
            align:"center",
            dataIndex: 'datetime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'试验种类‘1’代表普通沥青;’2’改性沥青；',
            align:"center",
            dataIndex: 'type'
          },
          {
            title:'通过率',
            align:"center",
            dataIndex: 'access'
          },
          {
            title:'Sbs掺量',
            align:"center",
            dataIndex: 'sbsaccess'
          },
          {
            title:'光谱图文件 指定文件格式csv',
            align:"center",
            dataIndex: 'lighfileString'
          },
          {
            title:'isdj',
            align:"center",
            dataIndex: 'isdj'
          },
          {
            title:'预警原因',
            align:"center",
            dataIndex: 'overReason'
          },
          {
            title:'审核状态',
            align:"center",
            dataIndex: 'overproofStatus'
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
          list: "/whwgpy/wHwgpy/list",
          delete: "/whwgpy/wHwgpy/delete",
          deleteBatch: "/whwgpy/wHwgpy/deleteBatch",
          exportXlsUrl: "/whwgpy/wHwgpy/exportXls",
          importExcelUrl: "whwgpy/wHwgpy/importExcel",
          
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
        fieldList.push({type:'string',value:'customer',text:'设备归属'})
        fieldList.push({type:'int',value:'projectid',text:'项目id'})
        fieldList.push({type:'string',value:'projectname',text:'项目名称'})
        fieldList.push({type:'string',value:'product',text:'沥青种类'})
        fieldList.push({type:'string',value:'grade',text:'车号'})
        fieldList.push({type:'string',value:'source',text:'标段'})
        fieldList.push({type:'int',value:'result',text:'0代表匹配，1代表不匹配'})
        fieldList.push({type:'date',value:'datetime',text:'试验的时间yyyy-MM-dd HH:mm:ss'})
        fieldList.push({type:'int',value:'type',text:'试验种类‘1’代表普通沥青;’2’改性沥青；'})
        fieldList.push({type:'string',value:'access',text:'通过率'})
        fieldList.push({type:'string',value:'sbsaccess',text:'Sbs掺量'})
        fieldList.push({type:'byte',value:'lighfile',text:'光谱图文件 指定文件格式csv'})
        fieldList.push({type:'int',value:'isdj',text:'isdj'})
        fieldList.push({type:'string',value:'overReason',text:'预警原因'})
        fieldList.push({type:'int',value:'overproofStatus',text:'审核状态'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>