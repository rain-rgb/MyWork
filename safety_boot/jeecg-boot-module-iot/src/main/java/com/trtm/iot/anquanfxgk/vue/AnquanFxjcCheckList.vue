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
      <a-button type="primary" icon="download" @click="handleExportXls('anquan_fxjc_check')">导出</a-button>
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

    <anquan-fxjc-check-modal ref="modalForm" @ok="modalFormOk"></anquan-fxjc-check-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import AnquanFxjcCheckModal from './modules/AnquanFxjcCheckModal'

  export default {
    name: 'AnquanFxjcCheckList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      AnquanFxjcCheckModal
    },
    data () {
      return {
        description: 'anquan_fxjc_check管理页面',
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
            title:'年度如 2024年度研判',
            align:"center",
            dataIndex: 'niandu'
          },
          {
            title:'项目名称',
            align:"center",
            dataIndex: 'projectname'
          },
          {
            title:'所属业务部门',
            align:"center",
            dataIndex: 'departname'
          },
          {
            title:'guid',
            align:"center",
            dataIndex: 'guid'
          },
          {
            title:'说明',
            align:"center",
            dataIndex: 'note'
          },
          {
            title:'风险源点责任部',
            align:"center",
            dataIndex: 'riskDepart'
          },
          {
            title:'风险源点名称',
            align:"center",
            dataIndex: 'riskName'
          },
          {
            title:'问题描述',
            align:"center",
            dataIndex: 'problems'
          },
          {
            title:'风险等级',
            align:"center",
            dataIndex: 'riskLevel'
          },
          {
            title:'检查人',
            align:"center",
            dataIndex: 'checkperson'
          },
          {
            title:'编号',
            align:"center",
            dataIndex: 'serialNo'
          },
          {
            title:'信息反馈',
            align:"center",
            dataIndex: 'infoBack'
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
          list: "/anquanfxgk/anquanFxjcCheck/list",
          delete: "/anquanfxgk/anquanFxjcCheck/delete",
          deleteBatch: "/anquanfxgk/anquanFxjcCheck/deleteBatch",
          exportXlsUrl: "/anquanfxgk/anquanFxjcCheck/exportXls",
          importExcelUrl: "anquanfxgk/anquanFxjcCheck/importExcel",
          
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
        fieldList.push({type:'string',value:'niandu',text:'年度如 2024年度研判'})
        fieldList.push({type:'string',value:'projectname',text:'项目名称'})
        fieldList.push({type:'string',value:'departname',text:'所属业务部门'})
        fieldList.push({type:'string',value:'guid',text:'guid'})
        fieldList.push({type:'string',value:'note',text:'说明'})
        fieldList.push({type:'string',value:'riskDepart',text:'风险源点责任部'})
        fieldList.push({type:'string',value:'riskName',text:'风险源点名称'})
        fieldList.push({type:'string',value:'problems',text:'问题描述'})
        fieldList.push({type:'string',value:'riskLevel',text:'风险等级'})
        fieldList.push({type:'string',value:'checkperson',text:'检查人'})
        fieldList.push({type:'string',value:'serialNo',text:'编号'})
        fieldList.push({type:'string',value:'infoBack',text:'信息反馈'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>