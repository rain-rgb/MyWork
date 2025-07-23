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
      <a-button type="primary" icon="download" @click="handleExportXls('原材料试验详情')">导出</a-button>
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

    <ycl-test-detail-modal ref="modalForm" @ok="modalFormOk"></ycl-test-detail-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import YclTestDetailModal from './modules/YclTestDetailModal'

  export default {
    name: 'YclTestDetailList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      YclTestDetailModal
    },
    data () {
      return {
        description: '原材料试验详情管理页面',
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
            title:'料仓id',
            align:"center",
            dataIndex: 'storageId'
          },
          {
            title:'料仓id',
            align:"center",
            dataIndex: 'storageId'
          },
          {
            title:'检验批编号',
            align:"center",
            dataIndex: 'inspectionLotNumber'
          },
          {
            title:'检验批编号',
            align:"center",
            dataIndex: 'inspectionLotNumber'
          },
          {
            title:'样品编号',
            align:"center",
            dataIndex: 'sampleNumber'
          },
          {
            title:'样品编号',
            align:"center",
            dataIndex: 'sampleNumber'
          },
          {
            title:'试验名称',
            align:"center",
            dataIndex: 'testName'
          },
          {
            title:'试验名称',
            align:"center",
            dataIndex: 'testName'
          },
          {
            title:'取样时间',
            align:"center",
            dataIndex: 'samplingTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'取样时间',
            align:"center",
            dataIndex: 'samplingTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
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
            title:'试验时间',
            align:"center",
            dataIndex: 'testTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'试验结论',
            align:"center",
            dataIndex: 'conclusion'
          },
          {
            title:'试验结论',
            align:"center",
            dataIndex: 'conclusion'
          },
          {
            title:'试验员',
            align:"center",
            dataIndex: 'tester'
          },
          {
            title:'试验员',
            align:"center",
            dataIndex: 'tester'
          },
          {
            title:'报告详情',
            align:"center",
            dataIndex: 'report'
          },
          {
            title:'报告详情',
            align:"center",
            dataIndex: 'report'
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
          list: "/ycltd/yclTestDetail/list",
          delete: "/ycltd/yclTestDetail/delete",
          deleteBatch: "/ycltd/yclTestDetail/deleteBatch",
          exportXlsUrl: "/ycltd/yclTestDetail/exportXls",
          importExcelUrl: "ycltd/yclTestDetail/importExcel",

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
        fieldList.push({type:'string',value:'storageId',text:'料仓id',dictCode:''})
        fieldList.push({type:'string',value:'storageId',text:'料仓id'})
        fieldList.push({type:'string',value:'inspectionLotNumber',text:'检验批编号',dictCode:''})
        fieldList.push({type:'string',value:'inspectionLotNumber',text:'检验批编号'})
        fieldList.push({type:'string',value:'sampleNumber',text:'样品编号'})
        fieldList.push({type:'string',value:'sampleNumber',text:'样品编号',dictCode:''})
        fieldList.push({type:'string',value:'testName',text:'试验名称'})
        fieldList.push({type:'string',value:'testName',text:'试验名称',dictCode:''})
        fieldList.push({type:'date',value:'samplingTime',text:'取样时间'})
        fieldList.push({type:'date',value:'samplingTime',text:'取样时间'})
        fieldList.push({type:'date',value:'testTime',text:'试验时间'})
        fieldList.push({type:'date',value:'testTime',text:'试验时间'})
        fieldList.push({type:'string',value:'conclusion',text:'试验结论'})
        fieldList.push({type:'string',value:'conclusion',text:'试验结论',dictCode:''})
        fieldList.push({type:'string',value:'tester',text:'试验员'})
        fieldList.push({type:'string',value:'tester',text:'试验员',dictCode:''})
        fieldList.push({type:'string',value:'report',text:'报告详情',dictCode:''})
        fieldList.push({type:'string',value:'report',text:'报告详情'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
