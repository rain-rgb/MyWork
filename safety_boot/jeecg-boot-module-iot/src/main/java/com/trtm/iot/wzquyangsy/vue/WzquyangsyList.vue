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
      <a-button type="primary" icon="download" @click="handleExportXls('原材料物资取样表信息')">导出</a-button>
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

    <wzquyangsy-modal ref="modalForm" @ok="modalFormOk"></wzquyangsy-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WzquyangsyModal from './modules/WzquyangsyModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'

  export default {
    name: 'WzquyangsyList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WzquyangsyModal,
      JSuperQuery,
    },
    data () {
      return {
        description: '原材料物资取样表信息管理页面',
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
            title:'台账id',
            align:"center",
            dataIndex: 'taizhangid'
          },
          {
            title:'取样时间',
            align:"center",
            dataIndex: 'quyangshijian'
          },
          {
            title:'qypic',
            align:"center",
            dataIndex: 'qypic'
          },
          {
            title:'gzpic',
            align:"center",
            dataIndex: 'gzpic'
          },
          {
            title:'jlpic',
            align:"center",
            dataIndex: 'jlpic'
          },
          {
            title:'pzpic',
            align:"center",
            dataIndex: 'pzpic'
          },
          {
            title:'进场时间',
            align:"center",
            dataIndex: 'jinchangshijian'
          },
          {
            title:'sy1pic',
            align:"center",
            dataIndex: 'sy1pic'
          },
          {
            title:'sy2pic',
            align:"center",
            dataIndex: 'sy2pic'
          },
          {
            title:'systate',
            align:"center",
            dataIndex: 'systate'
          },
          {
            title:'shouyang1pic',
            align:"center",
            dataIndex: 'shouyang1pic'
          },
          {
            title:'shouyang2pic',
            align:"center",
            dataIndex: 'shouyang2pic'
          },
          {
            title:'提交判断 1为已提交',
            align:"center",
            dataIndex: 'tjstate'
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
          list: "/wzquyangsy/wzquyangsy/list",
          delete: "/wzquyangsy/wzquyangsy/delete",
          deleteBatch: "/wzquyangsy/wzquyangsy/deleteBatch",
          exportXlsUrl: "/wzquyangsy/wzquyangsy/exportXls",
          importExcelUrl: "wzquyangsy/wzquyangsy/importExcel",
          
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
        fieldList.push({type:'int',value:'taizhangid',text:'台账id',dictCode:''})
        fieldList.push({type:'string',value:'quyangshijian',text:'取样时间',dictCode:''})
        fieldList.push({type:'string',value:'qypic',text:'qypic',dictCode:''})
        fieldList.push({type:'string',value:'gzpic',text:'gzpic',dictCode:''})
        fieldList.push({type:'string',value:'jlpic',text:'jlpic',dictCode:''})
        fieldList.push({type:'string',value:'pzpic',text:'pzpic',dictCode:''})
        fieldList.push({type:'string',value:'jinchangshijian',text:'进场时间',dictCode:''})
        fieldList.push({type:'string',value:'sy1pic',text:'sy1pic',dictCode:''})
        fieldList.push({type:'string',value:'sy2pic',text:'sy2pic',dictCode:''})
        fieldList.push({type:'string',value:'systate',text:'systate',dictCode:''})
        fieldList.push({type:'string',value:'shouyang1pic',text:'shouyang1pic',dictCode:''})
        fieldList.push({type:'string',value:'shouyang2pic',text:'shouyang2pic',dictCode:''})
        fieldList.push({type:'string',value:'tjstate',text:'提交判断 1为已提交',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>