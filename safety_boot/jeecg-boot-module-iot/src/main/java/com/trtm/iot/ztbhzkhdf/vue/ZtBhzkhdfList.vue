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
      <a-button type="primary" icon="download" @click="handleExportXls('混凝土配送考核得分表')">导出</a-button>
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

    <zt-bhzkhdf-modal ref="modalForm" @ok="modalFormOk"></zt-bhzkhdf-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ZtBhzkhdfModal from './modules/ZtBhzkhdfModal'

  export default {
    name: 'ZtBhzkhdfList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ZtBhzkhdfModal
    },
    data () {
      return {
        description: '混凝土配送考核得分表管理页面',
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
            title:'分包单位',
            align:"center",
            dataIndex: 'customer'
          },
          {
            title:'合同编号',
            align:"center",
            dataIndex: 'taiz'
          },
          {
            title:'单位工程',
            align:"center",
            dataIndex: 'projname'
          },
          {
            title:'分部工程',
            align:"center",
            dataIndex: 'conspos'
          },
          {
            title:'分项工程',
            align:"center",
            dataIndex: 'projadr'
          },
          {
            title:'25以上(-45)',
            align:"center",
            dataIndex: 'aboveew'
          },
          {
            title:'25/20(10)',
            align:"center",
            dataIndex: 'abovees'
          },
          {
            title:'20/10(40)',
            align:"center",
            dataIndex: 'aboveys'
          },
          {
            title:'10/0(100)',
            align:"center",
            dataIndex: 'abovehg'
          },
          {
            title:'0/-10(95)',
            align:"center",
            dataIndex: 'abovejs'
          },
          {
            title:'-10/-20(40)',
            align:"center",
            dataIndex: 'abovejes'
          },
          {
            title:'-20/-40(-20)',
            align:"center",
            dataIndex: 'abovejss'
          },
          {
            title:'-40/-60(-40)',
            align:"center",
            dataIndex: 'abovejls'
          },
          {
            title:'>-60',
            align:"center",
            dataIndex: 'abovebhg'
          },
          {
            title:'得分',
            align:"center",
            dataIndex: 'scores'
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
          list: "/ztbhzkhdf/ztBhzkhdf/list",
          delete: "/ztbhzkhdf/ztBhzkhdf/delete",
          deleteBatch: "/ztbhzkhdf/ztBhzkhdf/deleteBatch",
          exportXlsUrl: "/ztbhzkhdf/ztBhzkhdf/exportXls",
          importExcelUrl: "ztbhzkhdf/ztBhzkhdf/importExcel",
          
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
        fieldList.push({type:'string',value:'customer',text:'分包单位',dictCode:''})
        fieldList.push({type:'string',value:'taiz',text:'合同编号',dictCode:''})
        fieldList.push({type:'string',value:'projname',text:'单位工程',dictCode:''})
        fieldList.push({type:'string',value:'conspos',text:'分部工程',dictCode:''})
        fieldList.push({type:'string',value:'projadr',text:'分项工程',dictCode:''})
        fieldList.push({type:'string',value:'aboveew',text:'25以上(-45)',dictCode:''})
        fieldList.push({type:'string',value:'abovees',text:'25/20(10)',dictCode:''})
        fieldList.push({type:'string',value:'aboveys',text:'20/10(40)',dictCode:''})
        fieldList.push({type:'string',value:'abovehg',text:'10/0(100)',dictCode:''})
        fieldList.push({type:'string',value:'abovejs',text:'0/-10(95)',dictCode:''})
        fieldList.push({type:'string',value:'abovejes',text:'-10/-20(40)',dictCode:''})
        fieldList.push({type:'string',value:'abovejss',text:'-20/-40(-20)',dictCode:''})
        fieldList.push({type:'string',value:'abovejls',text:'-40/-60(-40)',dictCode:''})
        fieldList.push({type:'string',value:'abovebhg',text:'>-60',dictCode:''})
        fieldList.push({type:'string',value:'scores',text:'得分',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>