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
      <a-button type="primary" icon="download" @click="handleExportXls('张拉预警配置表')">导出</a-button>
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

    <tr-zhangla-configure-modal ref="modalForm" @ok="modalFormOk"></tr-zhangla-configure-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import TrZhanglaConfigureModal from './modules/TrZhanglaConfigureModal'

  export default {
    name: 'TrZhanglaConfigureList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      TrZhanglaConfigureModal
    },
    data () {
      return {
        description: '张拉预警配置表管理页面',
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
            title:'设备编号',
            align:"center",
            dataIndex: 'shebeiNo'
          },
          {
            title:'是否预警',
            align:"center",
            dataIndex: 'alertOrNot'
          },
          {
            title:'张拉初级预警范围1',
            align:"center",
            dataIndex: 'tensionChu'
          },
          {
            title:'张拉初级预警范围2',
            align:"center",
            dataIndex: 'tensionChuend'
          },
          {
            title:'张拉中级预警',
            align:"center",
            dataIndex: 'tensionZhong'
          },
          {
            title:'张拉中级预警2',
            align:"center",
            dataIndex: 'tensionZhongend'
          },
          {
            title:'张拉高级预警',
            align:"center",
            dataIndex: 'tensionGao'
          },
          {
            title:'张拉高级预警2',
            align:"center",
            dataIndex: 'tensionGaoend'
          },
          {
            title:'伸长率初级',
            align:"center",
            dataIndex: 'elongationChu'
          },
          {
            title:'伸长率初级2',
            align:"center",
            dataIndex: 'elongationChuend'
          },
          {
            title:'伸长率中级',
            align:"center",
            dataIndex: 'elongationZhong'
          },
          {
            title:'伸长率中级2',
            align:"center",
            dataIndex: 'elongationZhongend'
          },
          {
            title:'伸长率高级',
            align:"center",
            dataIndex: 'elongationGao'
          },
          {
            title:'伸长率高级2',
            align:"center",
            dataIndex: 'elongationGaoend'
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
          list: "/trzhanglaconfigure/trZhanglaConfigure/list",
          delete: "/trzhanglaconfigure/trZhanglaConfigure/delete",
          deleteBatch: "/trzhanglaconfigure/trZhanglaConfigure/deleteBatch",
          exportXlsUrl: "/trzhanglaconfigure/trZhanglaConfigure/exportXls",
          importExcelUrl: "trzhanglaconfigure/trZhanglaConfigure/importExcel",
          
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
        fieldList.push({type:'string',value:'shebeiNo',text:'设备编号',dictCode:''})
        fieldList.push({type:'string',value:'alertOrNot',text:'是否预警',dictCode:''})
        fieldList.push({type:'double',value:'tensionChu',text:'张拉初级预警范围1',dictCode:''})
        fieldList.push({type:'double',value:'tensionChuend',text:'张拉初级预警范围2',dictCode:''})
        fieldList.push({type:'double',value:'tensionZhong',text:'张拉中级预警',dictCode:''})
        fieldList.push({type:'double',value:'tensionZhongend',text:'张拉中级预警2',dictCode:''})
        fieldList.push({type:'double',value:'tensionGao',text:'张拉高级预警',dictCode:''})
        fieldList.push({type:'double',value:'tensionGaoend',text:'张拉高级预警2',dictCode:''})
        fieldList.push({type:'double',value:'elongationChu',text:'伸长率初级',dictCode:''})
        fieldList.push({type:'double',value:'elongationChuend',text:'伸长率初级2',dictCode:''})
        fieldList.push({type:'double',value:'elongationZhong',text:'伸长率中级',dictCode:''})
        fieldList.push({type:'double',value:'elongationZhongend',text:'伸长率中级2',dictCode:''})
        fieldList.push({type:'double',value:'elongationGao',text:'伸长率高级',dictCode:''})
        fieldList.push({type:'double',value:'elongationGaoend',text:'伸长率高级2',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>