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
      <a-button type="primary" icon="download" @click="handleExportXls('zhangla_yajiang_over_handler')">导出</a-button>
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

    <zhangla-yajiang-over-handler-modal ref="modalForm" @ok="modalFormOk"></zhangla-yajiang-over-handler-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ZhanglaYajiangOverHandlerModal from './modules/ZhanglaYajiangOverHandlerModal'

  export default {
    name: 'ZhanglaYajiangOverHandlerList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ZhanglaYajiangOverHandlerModal
    },
    data () {
      return {
        description: 'zhangla_yajiang_over_handler管理页面',
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
            title:'对应张拉压浆主表的syjid',
            align:"center",
            dataIndex: 'baseid'
          },
          {
            title:'问题原因',
            align:"center",
            dataIndex: 'problemReasons'
          },
          {
            title:'处理方式',
            align:"center",
            dataIndex: 'handleWay'
          },
          {
            title:'处理结果',
            align:"center",
            dataIndex: 'handleResult'
          },
          {
            title:'处理时间',
            align:"center",
            dataIndex: 'handleTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'处理人',
            align:"center",
            dataIndex: 'handlePerson'
          },
          {
            title:'监理审批',
            align:"center",
            dataIndex: 'supervisorApproval'
          },
          {
            title:'监理结果',
            align:"center",
            dataIndex: 'supervisorResult'
          },
          {
            title:'监理处理时间',
            align:"center",
            dataIndex: 'supervisorHandleTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'审批人',
            align:"center",
            dataIndex: 'approvalPerson'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title:'审核附件',
            align:"center",
            dataIndex: 'filePath'
          },
          {
            title:'处置附件',
            align:"center",
            dataIndex: 'filePath2'
          },
          {
            title:'超标状态：0为未处理，10为施工方已处理，20为监理方已处理',
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
          list: "/zhanglam/zhanglaYajiangOverHandler/list",
          delete: "/zhanglam/zhanglaYajiangOverHandler/delete",
          deleteBatch: "/zhanglam/zhanglaYajiangOverHandler/deleteBatch",
          exportXlsUrl: "/zhanglam/zhanglaYajiangOverHandler/exportXls",
          importExcelUrl: "zhanglam/zhanglaYajiangOverHandler/importExcel",
          
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
        fieldList.push({type:'string',value:'baseid',text:'对应张拉压浆主表的syjid'})
        fieldList.push({type:'string',value:'problemReasons',text:'问题原因'})
        fieldList.push({type:'string',value:'handleWay',text:'处理方式'})
        fieldList.push({type:'string',value:'handleResult',text:'处理结果'})
        fieldList.push({type:'date',value:'handleTime',text:'处理时间'})
        fieldList.push({type:'string',value:'handlePerson',text:'处理人'})
        fieldList.push({type:'string',value:'supervisorApproval',text:'监理审批'})
        fieldList.push({type:'string',value:'supervisorResult',text:'监理结果'})
        fieldList.push({type:'date',value:'supervisorHandleTime',text:'监理处理时间'})
        fieldList.push({type:'string',value:'approvalPerson',text:'审批人'})
        fieldList.push({type:'string',value:'remark',text:'备注'})
        fieldList.push({type:'string',value:'filePath',text:'审核附件'})
        fieldList.push({type:'string',value:'filePath2',text:'处置附件'})
        fieldList.push({type:'int',value:'overproofStatus',text:'超标状态：0为未处理，10为施工方已处理，20为监理方已处理'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>