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
      <a-button type="primary" icon="download" @click="handleExportXls('混凝土见证取样二维码表信息')">导出</a-button>
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

    <hnt-consign-code-modal ref="modalForm" @ok="modalFormOk"></hnt-consign-code-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import HntConsignCodeModal from './modules/HntConsignCodeModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'

  export default {
    name: 'HntConsignCodeList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      HntConsignCodeModal,
      JSuperQuery,
    },
    data () {
      return {
        description: '混凝土见证取样二维码表信息管理页面',
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
            title:'二维码',
            align:"center",
            dataIndex: 'codeno'
          },
          {
            title:'委托ID--guid',
            align:"center",
            dataIndex: 'wtid'
          },
          {
            title:'委托编号',
            align:"center",
            dataIndex: 'wtbh'
          },
          {
            title:'状态（0：取样，1：开始养护，2：出养护室）',
            align:"center",
            dataIndex: 'cstatus'
          },
          {
            title:'养护时间',
            align:"center",
            dataIndex: 'jyhdate'
          },
          {
            title:'进养护人',
            align:"center",
            dataIndex: 'jyhr'
          },
          {
            title:'出养护室时间',
            align:"center",
            dataIndex: 'cyhdate'
          },
          {
            title:'出养护人',
            align:"center",
            dataIndex: 'cyhr'
          },
          {
            title:'取样人',
            align:"center",
            dataIndex: 'qyr'
          },
          {
            title:'取样时间',
            align:"center",
            dataIndex: 'qydate'
          },
          {
            title:'外键，与t_consigin_sample_pos关联',
            align:"center",
            dataIndex: 'huojianname'
          },
          {
            title:'货物所在层数',
            align:"center",
            dataIndex: 'huojiacenshu'
          },
          {
            title:'二维码处理字段',
            align:"center",
            dataIndex: 'code'
          },
          {
            title:'序号',
            align:"center",
            dataIndex: 'no'
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
          list: "/hntconsigncode/hntConsignCode/list",
          delete: "/hntconsigncode/hntConsignCode/delete",
          deleteBatch: "/hntconsigncode/hntConsignCode/deleteBatch",
          exportXlsUrl: "/hntconsigncode/hntConsignCode/exportXls",
          importExcelUrl: "hntconsigncode/hntConsignCode/importExcel",
          
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
        fieldList.push({type:'string',value:'codeno',text:'二维码',dictCode:''})
        fieldList.push({type:'string',value:'wtid',text:'委托ID--guid',dictCode:''})
        fieldList.push({type:'string',value:'wtbh',text:'委托编号',dictCode:''})
        fieldList.push({type:'int',value:'cstatus',text:'状态（0：取样，1：开始养护，2：出养护室）',dictCode:''})
        fieldList.push({type:'string',value:'jyhdate',text:'养护时间',dictCode:''})
        fieldList.push({type:'string',value:'jyhr',text:'进养护人',dictCode:''})
        fieldList.push({type:'string',value:'cyhdate',text:'出养护室时间',dictCode:''})
        fieldList.push({type:'string',value:'cyhr',text:'出养护人',dictCode:''})
        fieldList.push({type:'string',value:'qyr',text:'取样人',dictCode:''})
        fieldList.push({type:'string',value:'qydate',text:'取样时间',dictCode:''})
        fieldList.push({type:'string',value:'huojianname',text:'外键，与t_consigin_sample_pos关联',dictCode:''})
        fieldList.push({type:'string',value:'huojiacenshu',text:'货物所在层数',dictCode:''})
        fieldList.push({type:'string',value:'code',text:'二维码处理字段',dictCode:''})
        fieldList.push({type:'string',value:'no',text:'序号',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>