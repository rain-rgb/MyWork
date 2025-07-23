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
      <a-button type="primary" icon="download" @click="handleExportXls('kanbaninfo')">导出</a-button>
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

    <kanbaninfo-modal ref="modalForm" @ok="modalFormOk"></kanbaninfo-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import KanbaninfoModal from './modules/KanbaninfoModal'

  export default {
    name: 'KanbaninfoList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      KanbaninfoModal
    },
    data () {
      return {
        description: 'kanbaninfo管理页面',
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
            title:'kanbanname',
            align:"center",
            dataIndex: 'kanbanname'
          },
          {
            title:'1:梁场；2.隧道；3：梁场主要岗位负责人 4：人员工种分布图',
            align:"center",
            dataIndex: 'type'
          },
          {
            title:'负责人姓名',
            align:"center",
            dataIndex: 'people'
          },
          {
            title:'总数量（制梁）',
            align:"center",
            dataIndex: 'allcount'
          },
          {
            title:'负责人职位',
            align:"center",
            dataIndex: 'job'
          },
          {
            title:'负责人状态；隧道图片',
            align:"center",
            dataIndex: 'pic'
          },
          {
            title:'负责人联系方式',
            align:"center",
            dataIndex: 'phone'
          },
          {
            title:'人员工种分布图',
            align:"center",
            dataIndex: 'peoplesf'
          },
          {
            title:'2 隧道介绍',
            align:"center",
            dataIndex: 'suidaoinfo'
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
          list: "/kanbaninfo/kanbaninfo/list",
          delete: "/kanbaninfo/kanbaninfo/delete",
          deleteBatch: "/kanbaninfo/kanbaninfo/deleteBatch",
          exportXlsUrl: "/kanbaninfo/kanbaninfo/exportXls",
          importExcelUrl: "kanbaninfo/kanbaninfo/importExcel",
          
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
        fieldList.push({type:'string',value:'kanbanname',text:'kanbanname'})
        fieldList.push({type:'int',value:'type',text:'1:梁场；2.隧道；3：梁场主要岗位负责人 4：人员工种分布图'})
        fieldList.push({type:'string',value:'people',text:'负责人姓名'})
        fieldList.push({type:'string',value:'allcount',text:'总数量（制梁）'})
        fieldList.push({type:'string',value:'job',text:'负责人职位'})
        fieldList.push({type:'string',value:'pic',text:'负责人状态；隧道图片'})
        fieldList.push({type:'string',value:'phone',text:'负责人联系方式'})
        fieldList.push({type:'string',value:'peoplesf',text:'人员工种分布图'})
        fieldList.push({type:'string',value:'suidaoinfo',text:'2 隧道介绍'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>