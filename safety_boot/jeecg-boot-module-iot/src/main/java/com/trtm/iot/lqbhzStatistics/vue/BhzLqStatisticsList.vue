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
      <a-button type="primary" icon="download" @click="handleExportXls('bhz_lq_statistics')">导出</a-button>
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

    <bhz-lq-statistics-modal ref="modalForm" @ok="modalFormOk"></bhz-lq-statistics-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BhzLqStatisticsModal from './modules/BhzLqStatisticsModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'

  export default {
    name: 'BhzLqStatisticsList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BhzLqStatisticsModal,
      JSuperQuery,
    },
    data () {
      return {
        description: 'bhz_lq_statistics管理页面',
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
            dataIndex: 'shebeibianhao'
          },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'projectName'
          },
          {
            title:'施工地点',
            align:"center",
            dataIndex: 'jobLocation'
          },
          {
            title:'浇筑部位',
            align:"center",
            dataIndex: 'poureLocation'
          },
          {
            title:'配方号',
            align:"center",
            dataIndex: 'formulaNo'
          },
          {
            title:'强度等级',
            align:"center",
            dataIndex: 'strengthRank'
          },
          {
            title:'初级超标多少盘',
            align:"center",
            dataIndex: 'primaryDish'
          },
          {
            title:'初级处置多少盘',
            align:"center",
            dataIndex: 'primaryHandle'
          },
          {
            title:'初级处置百分点',
            align:"center",
            dataIndex: 'primaryPercent'
          },
          {
            title:'中级超标多少盘',
            align:"center",
            dataIndex: 'middleDish'
          },
          {
            title:'中级处置多少盘',
            align:"center",
            dataIndex: 'middleHandle'
          },
          {
            title:'中级处置百分点',
            align:"center",
            dataIndex: 'middlePercent'
          },
          {
            title:'高级超标多少盘',
            align:"center",
            dataIndex: 'advancedDish'
          },
          {
            title:'高级处置多少盘',
            align:"center",
            dataIndex: 'advancedHandle'
          },
          {
            title:'高级处置百分点',
            align:"center",
            dataIndex: 'advancedPercent'
          },
          {
            title:'总盘数',
            align:"center",
            dataIndex: 'allDish'
          },
          {
            title:'总超标盘数',
            align:"center",
            dataIndex: 'allOverproofDish'
          },
          {
            title:'总处理盘数',
            align:"center",
            dataIndex: 'allHandleDish'
          },
          {
            title:'总体超标处置率',
            align:"center",
            dataIndex: 'allPercent'
          },
          {
            title:'方量',
            align:"center",
            dataIndex: 'estimateNumber'
          },
          {
            title:'统计时间，以天为单位',
            align:"center",
            dataIndex: 'statisticsTime',
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
          list: "/lqbhzStatistics/bhzLqStatistics/list",
          delete: "/lqbhzStatistics/bhzLqStatistics/delete",
          deleteBatch: "/lqbhzStatistics/bhzLqStatistics/deleteBatch",
          exportXlsUrl: "/lqbhzStatistics/bhzLqStatistics/exportXls",
          importExcelUrl: "lqbhzStatistics/bhzLqStatistics/importExcel",
          
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
        fieldList.push({type:'string',value:'shebeibianhao',text:'设备编号'})
        fieldList.push({type:'string',value:'projectName',text:'工程名称'})
        fieldList.push({type:'string',value:'jobLocation',text:'施工地点'})
        fieldList.push({type:'string',value:'poureLocation',text:'浇筑部位'})
        fieldList.push({type:'string',value:'formulaNo',text:'配方号'})
        fieldList.push({type:'string',value:'strengthRank',text:'强度等级'})
        fieldList.push({type:'int',value:'primaryDish',text:'初级超标多少盘'})
        fieldList.push({type:'int',value:'primaryHandle',text:'初级处置多少盘'})
        fieldList.push({type:'number',value:'primaryPercent',text:'初级处置百分点'})
        fieldList.push({type:'int',value:'middleDish',text:'中级超标多少盘'})
        fieldList.push({type:'int',value:'middleHandle',text:'中级处置多少盘'})
        fieldList.push({type:'number',value:'middlePercent',text:'中级处置百分点'})
        fieldList.push({type:'int',value:'advancedDish',text:'高级超标多少盘'})
        fieldList.push({type:'int',value:'advancedHandle',text:'高级处置多少盘'})
        fieldList.push({type:'number',value:'advancedPercent',text:'高级处置百分点'})
        fieldList.push({type:'int',value:'allDish',text:'总盘数'})
        fieldList.push({type:'int',value:'allOverproofDish',text:'总超标盘数'})
        fieldList.push({type:'int',value:'allHandleDish',text:'总处理盘数'})
        fieldList.push({type:'number',value:'allPercent',text:'总体超标处置率'})
        fieldList.push({type:'number',value:'estimateNumber',text:'方量'})
        fieldList.push({type:'date',value:'statisticsTime',text:'统计时间，以天为单位'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>