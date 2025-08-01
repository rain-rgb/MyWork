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
      <a-button type="primary" icon="download" @click="handleExportXls('bhz_not_order')">导出</a-button>
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

    <bhz-not-order-modal ref="modalForm" @ok="modalFormOk"></bhz-not-order-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BhzNotOrderModal from './modules/BhzNotOrderModal'

  export default {
    name: 'BhzNotOrderList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BhzNotOrderModal
    },
    data () {
      return {
        description: 'bhz_not_order管理页面',
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
            title:'出料日期',
            align:"center",
            dataIndex: 'chuliaodate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'标段名称',
            align:"center",
            dataIndex: 'sectionname'
          },
          {
            title:'盘数（未使用浇筑令）',
            align:"center",
            dataIndex: 'panshu'
          },
          {
            title:'生产量（未使用浇筑令））',
            align:"center",
            dataIndex: 'produce'
          },
          {
            title:'初级超标数',
            align:"center",
            dataIndex: 'chuji'
          },
          {
            title:'中级超标数',
            align:"center",
            dataIndex: 'zhongji'
          },
          {
            title:'高级超标数',
            align:"center",
            dataIndex: 'gaoji'
          },
          {
            title:'问题原因',
            align:"center",
            dataIndex: 'problemReasons'
          },
          {
            title:'填报时间',
            align:"center",
            dataIndex: 'handleTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'填报人',
            align:"center",
            dataIndex: 'handlePerson'
          },
          {
            title:'审批说明',
            align:"center",
            dataIndex: 'approvalReasons',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'审批时间',
            align:"center",
            dataIndex: 'approvalTime',
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
            title:'状态：0为未处理，10为施工方已处理，20为闭合',
            align:"center",
            dataIndex: 'overproofStatus'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'note'
          },
          {
            title:'附件',
            align:"center",
            dataIndex: 'fileurl'
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
          list: "/bhzcfg/bhzNotOrder/list",
          delete: "/bhzcfg/bhzNotOrder/delete",
          deleteBatch: "/bhzcfg/bhzNotOrder/deleteBatch",
          exportXlsUrl: "/bhzcfg/bhzNotOrder/exportXls",
          importExcelUrl: "bhzcfg/bhzNotOrder/importExcel",
          
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
        fieldList.push({type:'date',value:'chuliaodate',text:'出料日期'})
        fieldList.push({type:'string',value:'sectionname',text:'标段名称'})
        fieldList.push({type:'string',value:'panshu',text:'盘数（未使用浇筑令）'})
        fieldList.push({type:'string',value:'produce',text:'生产量（未使用浇筑令））'})
        fieldList.push({type:'string',value:'chuji',text:'初级超标数'})
        fieldList.push({type:'string',value:'zhongji',text:'中级超标数'})
        fieldList.push({type:'string',value:'gaoji',text:'高级超标数'})
        fieldList.push({type:'string',value:'problemReasons',text:'问题原因'})
        fieldList.push({type:'date',value:'handleTime',text:'填报时间'})
        fieldList.push({type:'string',value:'handlePerson',text:'填报人'})
        fieldList.push({type:'date',value:'approvalReasons',text:'审批说明'})
        fieldList.push({type:'date',value:'approvalTime',text:'审批时间'})
        fieldList.push({type:'string',value:'approvalPerson',text:'审批人'})
        fieldList.push({type:'int',value:'overproofStatus',text:'状态：0为未处理，10为施工方已处理，20为闭合'})
        fieldList.push({type:'string',value:'note',text:'备注'})
        fieldList.push({type:'string',value:'fileurl',text:'附件'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>