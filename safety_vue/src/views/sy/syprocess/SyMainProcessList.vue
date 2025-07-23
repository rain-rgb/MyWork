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
      <a-button type="primary" icon="download" @click="handleExportXls('sy_main_process')">导出</a-button>
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

    <sy-main-process-modal ref="modalForm" @ok="modalFormOk"></sy-main-process-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyMainProcessModal from './modules/SyMainProcessModal'

  export default {
    name: 'SyMainProcessList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyMainProcessModal
    },
    data () {
      return {
        description: 'sy_main_process管理页面',
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
            title:'部门',
            align:"center",
            dataIndex: 'sysorgcode_dictText'
          },
          // {
          //   title:'唯一id',
          //   align:"center",
          //   dataIndex: 'uuid'
          // },
          {
            title:'试验编号',
            align:"center",
            dataIndex: 'testId'
          },
          {
            title:'工序编号',
            align:"center",
            dataIndex: 'processId'
          },
          {
            title:'工序名称',
            align:"center",
            dataIndex: 'processName'
          },
          {
            title:'试验员',
            align:"center",
            dataIndex: 'testBy'
          },
          {
            title:'试验状态（0：未开始，1：已完成，默认为0）',
            align:"center",
            dataIndex: 'status'
          },
          {
            title:'试验时间',
            align:"center",
            dataIndex: 'testTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'deviceId'
          },
          // {
          //   title:'试验记录(PDF)',
          //   align:"center",
          //   dataIndex: 'testRecord'
          // },
          // {
          //   title:'试验图片',
          //   align:"center",
          //   dataIndex: 'testPhoto'
          // },
          // {
          //   title:'备注',
          //   align:"center",
          //   dataIndex: 'remark'
          // },
          // {
          //   title:'创建人',
          //   align:"center",
          //   dataIndex: 'createBy_dictText'
          // },
          // {
          //   title:'创建时间',
          //   align:"center",
          //   dataIndex: 'createTime'
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
          list: "/syMainProcess/syMainProcess/list",
          delete: "/syMainProcess/syMainProcess/delete",
          deleteBatch: "/syMainProcess/syMainProcess/deleteBatch",
          exportXlsUrl: "/syMainProcess/syMainProcess/exportXls",
          importExcelUrl: "syMainProcess/syMainProcess/importExcel",

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
        fieldList.push({type:'string',value:'sysorgcode',text:'组织结构代码'})
        fieldList.push({type:'string',value:'uuid',text:'唯一id'})
        fieldList.push({type:'string',value:'testId',text:'试验id，关联与sy_main表uuid关联'})
        fieldList.push({type:'string',value:'processId',text:'工序编号'})
        fieldList.push({type:'string',value:'processName',text:'工序名称'})
        fieldList.push({type:'string',value:'testBy',text:'试验员id'})
        fieldList.push({type:'int',value:'status',text:'试验状态（0：未开始，1：已完成，默认为0）'})
        fieldList.push({type:'date',value:'testTime',text:'试验时间'})
        fieldList.push({type:'string',value:'deviceId',text:'设备编号'})
        fieldList.push({type:'string',value:'testRecord',text:'试验记录(PDF)'})
        fieldList.push({type:'string',value:'testPhoto',text:'试验图片'})
        fieldList.push({type:'string',value:'remark',text:'备注'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
