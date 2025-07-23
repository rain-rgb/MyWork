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
      <a-button type="primary" icon="download" @click="handleExportXls('压浆统计表')">导出</a-button>
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

    <tr-yajiang-statistics-modal ref="modalForm" @ok="modalFormOk"></tr-yajiang-statistics-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import TrYajiangStatisticsModal from './modules/TrYajiangStatisticsModal'

  export default {
    name: 'TrYajiangStatisticsList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      TrYajiangStatisticsModal
    },
    data () {
      return {
        description: '压浆统计表管理页面',
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
            title:'合同号',
            align:"center",
            dataIndex: 'htbh'
          },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'gcmc'
          },
          {
            title:'压浆时间',
            align:"center",
            dataIndex: 'yjsj'
          },
          {
            title:'桩号及部位',
            align:"center",
            dataIndex: 'zhbw'
          },
          {
            title:'施工部位',
            align:"center",
            dataIndex: 'sgbw'
          },
          {
            title:'构件结构',
            align:"center",
            dataIndex: 'gjjg'
          },
          {
            title:'构件编号及长度',
            align:"center",
            dataIndex: 'gjbh'
          },
          {
            title:'压浆设备编号',
            align:"center",
            dataIndex: 'yjsbbh'
          },
          {
            title:'梁板类型',
            align:"center",
            dataIndex: 'lblx'
          },
          {
            title:'部门名称',
            align:"center",
            dataIndex: 'sysOrgCode'
          },
          {
            title:'总条数',
            align:"center",
            dataIndex: 'trYajiangSum'
          },
          {
            title:'不合格数',
            align:"center",
            dataIndex: 'trYajaingOversum'
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
          list: "/tryajiangstatistics/trYajiangStatistics/list",
          delete: "/tryajiangstatistics/trYajiangStatistics/delete",
          deleteBatch: "/tryajiangstatistics/trYajiangStatistics/deleteBatch",
          exportXlsUrl: "/tryajiangstatistics/trYajiangStatistics/exportXls",
          importExcelUrl: "tryajiangstatistics/trYajiangStatistics/importExcel",
          
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
        fieldList.push({type:'string',value:'htbh',text:'合同号',dictCode:''})
        fieldList.push({type:'string',value:'gcmc',text:'工程名称',dictCode:''})
        fieldList.push({type:'string',value:'yjsj',text:'压浆时间',dictCode:''})
        fieldList.push({type:'string',value:'zhbw',text:'桩号及部位',dictCode:''})
        fieldList.push({type:'string',value:'sgbw',text:'施工部位',dictCode:''})
        fieldList.push({type:'string',value:'gjjg',text:'构件结构',dictCode:''})
        fieldList.push({type:'string',value:'gjbh',text:'构件编号及长度',dictCode:''})
        fieldList.push({type:'string',value:'yjsbbh',text:'压浆设备编号',dictCode:''})
        fieldList.push({type:'string',value:'lblx',text:'梁板类型',dictCode:''})
        fieldList.push({type:'string',value:'sysOrgCode',text:'部门名称',dictCode:''})
        fieldList.push({type:'string',value:'trYajiangSum',text:'总条数',dictCode:''})
        fieldList.push({type:'string',value:'trYajaingOversum',text:'不合格数',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>