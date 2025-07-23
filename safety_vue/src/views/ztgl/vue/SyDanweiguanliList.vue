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
      <a-button type="primary" icon="download" @click="handleExportXls('试验单位管理')">导出</a-button>
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
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >

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

    <sy-danweiguanli-modal ref="modalForm" @ok="modalFormOk"></sy-danweiguanli-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyDanweiguanliModal from './modules/SyDanweiguanliModal'

  export default {
    name: 'SyDanweiguanliList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyDanweiguanliModal
    },
    data () {
      return {
        description: '试验单位管理管理页面',
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
            title:'单位编码',
            align:"center",
            dataIndex: 'dwbm'
          },
          {
            title:'单位名称',
            align:"center",
            dataIndex: 'dwname'
          },
          {
            title:'单位类型',
            align:"center",
            dataIndex: 'dwtype_dictText'
          },
          {
            title:'资质证书编码',
            align:"center",
            dataIndex: 'zzzs'
          },
          {
            title:'附件',
            align:"center",
            dataIndex: 'file'
          },
          // {
          //   title:'所属机构',
          //   align:"center",
          //   dataIndex: 'sysorgcode'
          // },
          // {
          //   title:'上级部门',
          //   align:"center",
          //   dataIndex: 'syssjcode'
          // },
          // {
          //   title:'登记人',
          //   align:"center",
          //   dataIndex: 'dnegjiren'
          // },
          // {
          //   title:'登记时间',
          //   align:"center",
          //   dataIndex: 'dengjitime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
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
          list: "/syztgl/syDanweiguanli/list",
          delete: "/syztgl/syDanweiguanli/delete",
          deleteBatch: "/syztgl/syDanweiguanli/deleteBatch",
          exportXlsUrl: "/syztgl/syDanweiguanli/exportXls",
          importExcelUrl: "syztgl/syDanweiguanli/importExcel",
          
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
        fieldList.push({type:'string',value:'dwbm',text:'单位编码'})
        fieldList.push({type:'string',value:'dwname',text:'单位名称'})
        fieldList.push({type:'int',value:'dwtype',text:'单位类型（施工单位；监理单位；指挥部；实验室；外委单位；辅助员）'})
        fieldList.push({type:'string',value:'zzzs',text:'自治证书编码'})
        fieldList.push({type:'string',value:'file',text:'附件'})
        fieldList.push({type:'string',value:'sysorgcode',text:'所属机构'})
        fieldList.push({type:'string',value:'syssjcode',text:'上级部门'})
        fieldList.push({type:'string',value:'dnegjiren',text:'登记人'})
        fieldList.push({type:'date',value:'dengjitime',text:'登记时间'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>