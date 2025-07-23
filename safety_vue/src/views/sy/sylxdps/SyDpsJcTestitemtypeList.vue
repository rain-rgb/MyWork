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
      <a-button type="primary" icon="download" @click="handleExportXls('sy_dps_jc_testitemtype')">导出</a-button>
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

    <sy-dps-jc-testitemtype-modal ref="modalForm" @ok="modalFormOk"></sy-dps-jc-testitemtype-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyDpsJcTestitemtypeModal from './modules/SyDpsJcTestitemtypeModal'

  export default {
    name: 'SyDpsJcTestitemtypeList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyDpsJcTestitemtypeModal
    },
    data () {
      return {
        description: 'sy_dps_jc_testitemtype管理页面',
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
          // {
          //   title:'主键UUID',
          //   align:"center",
          //   dataIndex: 'uuid'
          // },
          {
            title:'试验类型编码',
            align:"center",
            dataIndex: 'titcode'
          },
          {
            title:'试验类型名称',
            align:"center",
            dataIndex: 'titname'
          },
          {
            title:'样品标识',
            align:"center",
            dataIndex: 'titsamplemark'
          },
          {
            title:'试验类型父节点编码',
            align:"center",
            dataIndex: 'titparentcode'
          },
          {
            title:'当前节点的子节点个数',
            align:"center",
            dataIndex: 'titchildnodesnum'
          },
          {
            title:'试验类型备注',
            align:"center",
            dataIndex: 'titremark'
          },
          {
            title:'删除状态',
            align:"center",
            dataIndex: 'titisdel'
          },
          // {
          //   title:'tipandingyiju',
          //   align:"center",
          //   dataIndex: 'tipandingyiju'
          // },
          // {
          //   title:'tishiyanyiju',
          //   align:"center",
          //   dataIndex: 'tishiyanyiju'
          // },
          // {
          //   title:'tittype',
          //   align:"center",
          //   dataIndex: 'tittype'
          // },
          // {
          //   title:'记录表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容 格式（, |）：样品名称,sampleName|样品描述,sampleDescribe',
          //   align:"center",
          //   dataIndex: 'tiyangpinxinxijl'
          // },
          // {
          //   title:'报告表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容',
          //   align:"center",
          //   dataIndex: 'tiyangpinxinxibg'
          // },
          // {
          //   title:'tipushtablename',
          //   align:"center",
          //   dataIndex: 'tipushtablename'
          // },
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
          list: "/sylxdps/syDpsJcTestitemtype/list",
          delete: "/sylxdps/syDpsJcTestitemtype/delete",
          deleteBatch: "/sylxdps/syDpsJcTestitemtype/deleteBatch",
          exportXlsUrl: "/sylxdps/syDpsJcTestitemtype/exportXls",
          importExcelUrl: "sylxdps/syDpsJcTestitemtype/importExcel",
          
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
        fieldList.push({type:'string',value:'uuid',text:'主键UUID'})
        fieldList.push({type:'string',value:'titcode',text:'试验类型编码(两位编码01开始)'})
        fieldList.push({type:'string',value:'titname',text:'试验类型名称'})
        fieldList.push({type:'string',value:'titsamplemark',text:'样品标识'})
        fieldList.push({type:'string',value:'titparentcode',text:'试验类型父节点编码'})
        fieldList.push({type:'string',value:'titchildnodesnum',text:'当前节点的子节点个数'})
        fieldList.push({type:'string',value:'titremark',text:'试验类型备注'})
        fieldList.push({type:'int',value:'titisdel',text:'是否删除（0：正常1：已删除）'})
        fieldList.push({type:'string',value:'tipandingyiju',text:'tipandingyiju'})
        fieldList.push({type:'string',value:'tishiyanyiju',text:'tishiyanyiju'})
        fieldList.push({type:'int',value:'tittype',text:'tittype'})
        fieldList.push({type:'string',value:'tiyangpinxinxijl',text:'记录表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容 格式（, |）：样品名称,sampleName|样品描述,sampleDescribe'})
        fieldList.push({type:'string',value:'tiyangpinxinxibg',text:'报告表样品信息生成配置，用来生成dps_sy_tableHeader表的ypxx字段内容'})
        fieldList.push({type:'string',value:'tipushtablename',text:'tipushtablename'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>