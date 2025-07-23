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
      <a-button type="primary" icon="download" @click="handleExportXls('bhz_sw_jipei_statistics')">导出</a-button>
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

    <bhz-sw-jipei-statistics-modal ref="modalForm" @ok="modalFormOk"></bhz-sw-jipei-statistics-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BhzSwJipeiStatisticsModal from './modules/BhzSwJipeiStatisticsModal'

  export default {
    name: 'BhzSwJipeiStatisticsList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BhzSwJipeiStatisticsModal
    },
    data () {
      return {
        description: 'bhz_sw_jipei_statistics管理页面',
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
            title:'沥青拌合唯一标识',
            align:"center",
            dataIndex: 'baseid'
          },
          {
            title:'出料时间',
            align:"center",
            dataIndex: 'chuliaoshijian'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'sbjno'
          },
          {
            title:'筛孔',
            align:"center",
            dataIndex: 'shaikong'
          },
          {
            title:'合成级配',
            align:"center",
            dataIndex: 'hechengjipei'
          },
          {
            title:'中值',
            align:"center",
            dataIndex: 'zhongzhi'
          },
          {
            title:'上限',
            align:"center",
            dataIndex: 'shangxian'
          },
          {
            title:'下限',
            align:"center",
            dataIndex: 'xiaxian'
          },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'createtime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'修改时间',
            align:"center",
            dataIndex: 'updatetime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
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
          list: "/bhzSwJipeiStatistics/bhzSwJipeiStatistics/list",
          delete: "/bhzSwJipeiStatistics/bhzSwJipeiStatistics/delete",
          deleteBatch: "/bhzSwJipeiStatistics/bhzSwJipeiStatistics/deleteBatch",
          exportXlsUrl: "/bhzSwJipeiStatistics/bhzSwJipeiStatistics/exportXls",
          importExcelUrl: "bhzSwJipeiStatistics/bhzSwJipeiStatistics/importExcel",
          
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
        fieldList.push({type:'string',value:'baseid',text:'沥青拌合唯一标识'})
        fieldList.push({type:'string',value:'chuliaoshijian',text:'出料时间'})
        fieldList.push({type:'string',value:'sbjno',text:'设备编号'})
        fieldList.push({type:'string',value:'shaikong',text:'筛孔'})
        fieldList.push({type:'number',value:'hechengjipei',text:'合成级配'})
        fieldList.push({type:'string',value:'zhongzhi',text:'中值'})
        fieldList.push({type:'string',value:'shangxian',text:'上限'})
        fieldList.push({type:'string',value:'xiaxian',text:'下限'})
        fieldList.push({type:'date',value:'createtime',text:'创建时间'})
        fieldList.push({type:'date',value:'updatetime',text:'修改时间'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>