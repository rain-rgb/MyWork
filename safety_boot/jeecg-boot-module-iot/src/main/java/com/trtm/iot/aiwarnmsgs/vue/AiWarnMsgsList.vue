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
      <a-button type="primary" icon="download" @click="handleExportXls('ai_warn_msgs')">导出</a-button>
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

    <ai-warn-msgs-modal ref="modalForm" @ok="modalFormOk"></ai-warn-msgs-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import AiWarnMsgsModal from './modules/AiWarnMsgsModal'

  export default {
    name: 'AiWarnMsgsList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      AiWarnMsgsModal
    },
    data () {
      return {
        description: 'ai_warn_msgs管理页面',
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
            title:'报警唯一码',
            align:"center",
            dataIndex: 'traceId'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'cid'
          },
          {
            title:'摄像头名称（报警工点）',
            align:"center",
            dataIndex: 'cname'
          },
          {
            title:'报警时间',
            align:"center",
            dataIndex: 'warntime'
          },
          {
            title:'报警内容',
            align:"center",
            dataIndex: 'warnmsg'
          },
          {
            title:'报警类别',
            align:"center",
            dataIndex: 'algtype'
          },
          {
            title:'图片，多张用，分隔',
            align:"center",
            dataIndex: 'picurls'
          },
          {
            title:'警报产生者',
            align:"center",
            dataIndex: 'warnent'
          },
          {
            title:'产生者类别',
            align:"center",
            dataIndex: 'enttype'
          },
          {
            title:'设备id 关联表shebei_info的id',
            align:"center",
            dataIndex: 'shebeiid'
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
          list: "/aiwarnmsgs/aiWarnMsgs/list",
          delete: "/aiwarnmsgs/aiWarnMsgs/delete",
          deleteBatch: "/aiwarnmsgs/aiWarnMsgs/deleteBatch",
          exportXlsUrl: "/aiwarnmsgs/aiWarnMsgs/exportXls",
          importExcelUrl: "aiwarnmsgs/aiWarnMsgs/importExcel",
          
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
        fieldList.push({type:'string',value:'traceId',text:'报警唯一码'})
        fieldList.push({type:'string',value:'cid',text:'设备编号'})
        fieldList.push({type:'string',value:'cname',text:'摄像头名称（报警工点）'})
        fieldList.push({type:'string',value:'warntime',text:'报警时间'})
        fieldList.push({type:'string',value:'warnmsg',text:'报警内容'})
        fieldList.push({type:'string',value:'algtype',text:'报警类别'})
        fieldList.push({type:'string',value:'picurls',text:'图片，多张用，分隔'})
        fieldList.push({type:'string',value:'warnent',text:'警报产生者'})
        fieldList.push({type:'string',value:'enttype',text:'产生者类别'})
        fieldList.push({type:'string',value:'shebeiid',text:'设备id 关联表shebei_info的id'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>