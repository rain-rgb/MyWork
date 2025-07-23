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
      <a-button type="primary" icon="download" @click="handleExportXls('自建拌和站配送考核标准时间表')">导出</a-button>
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

    <zt-bhz-peisongtime-modal ref="modalForm" @ok="modalFormOk"></zt-bhz-peisongtime-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ZtBhzPeisongtimeModal from './modules/ZtBhzPeisongtimeModal'

  export default {
    name: 'ZtBhzPeisongtimeList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ZtBhzPeisongtimeModal
    },
    data () {
      return {
        description: '自建拌和站配送考核标准时间表管理页面',
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
            title:'工程类型',
            align:"center",
            dataIndex: 'engineeringtype'
          },
          {
            title:'单位工程',
            align:"center",
            dataIndex: 'engineering'
          },
          {
            title:'基准运距 （Km)',
            align:"center",
            dataIndex: 'distance'
          },
          {
            title:'基准考核时间（分钟）',
            align:"center",
            dataIndex: 'assessmenttime'
          },
          {
            title:'隧道洞口里程',
            align:"center",
            dataIndex: 'mileage'
          },
          {
            title:'月末里程',
            align:"center",
            dataIndex: 'endmileage'
          },
          {
            title:'增加运距 （Km)',
            align:"center",
            dataIndex: 'gddistance'
          },
          {
            title:'增加考核时间（分钟）',
            align:"center",
            dataIndex: 'gdassessmenttime'
          },
          {
            title:'综合运距 （Km)',
            align:"center",
            dataIndex: 'zhdistance'
          },
          {
            title:'本期考核时间 （分钟）',
            align:"center",
            dataIndex: 'bqassessmenttime'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'note'
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
          list: "/ztbhzpeisongtime/ztBhzPeisongtime/list",
          delete: "/ztbhzpeisongtime/ztBhzPeisongtime/delete",
          deleteBatch: "/ztbhzpeisongtime/ztBhzPeisongtime/deleteBatch",
          exportXlsUrl: "/ztbhzpeisongtime/ztBhzPeisongtime/exportXls",
          importExcelUrl: "ztbhzpeisongtime/ztBhzPeisongtime/importExcel",
          
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
        fieldList.push({type:'int',value:'engineeringtype',text:'工程类型',dictCode:''})
        fieldList.push({type:'string',value:'engineering',text:'单位工程',dictCode:''})
        fieldList.push({type:'string',value:'distance',text:'基准运距 （Km)',dictCode:''})
        fieldList.push({type:'string',value:'assessmenttime',text:'基准考核时间（分钟）',dictCode:''})
        fieldList.push({type:'string',value:'mileage',text:'隧道洞口里程',dictCode:''})
        fieldList.push({type:'string',value:'endmileage',text:'月末里程',dictCode:''})
        fieldList.push({type:'string',value:'gddistance',text:'增加运距 （Km)',dictCode:''})
        fieldList.push({type:'string',value:'gdassessmenttime',text:'增加考核时间（分钟）',dictCode:''})
        fieldList.push({type:'string',value:'zhdistance',text:'综合运距 （Km)',dictCode:''})
        fieldList.push({type:'string',value:'bqassessmenttime',text:'本期考核时间 （分钟）',dictCode:''})
        fieldList.push({type:'string',value:'note',text:'备注',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>