<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('拌合站混凝土生产调度日报')">导出</a-button>
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
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

    <bhzhnt-ribao-modal ref="modalForm" @ok="modalFormOk"></bhzhnt-ribao-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BhzhntRibaoModal from './modules/BhzhntRibaoModal'

  export default {
    name: 'BhzhntRibaoList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BhzhntRibaoModal
    },
    data () {
      return {
        description: 'bhzhnt_ribao管理页面',
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
            title:'单位工程',
            align:"center",
            dataIndex: 'danweiProj'
          },
          {
            title:'分部工程',
            align:"center",
            dataIndex: 'fenbuProj'
          },
          {
            title:'分项工程',
            align:"center",
            dataIndex: 'fenxiangProj'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'danwei'
          },
          {
            title:'总控数量',
            align:"center",
            dataIndex: 'zongkongNum'
          },
          {
            title:'日完成量',
            align:"center",
            dataIndex: 'dayFinish'
          },
          {
            title:'月完成量',
            align:"center",
            dataIndex: 'monthFinish'
          },
          {
            title:'年完成量',
            align:"center",
            dataIndex: 'yearFinish'
          },
          {
            title:'开累',
            align:"center",
            dataIndex: 'allFinish'
          },
          {
            title:'剩余量',
            align:"center",
            dataIndex: 'residue'
          },
          {
            title:'1号完成量',
            align:"center",
            dataIndex: 'day1'
          },
          {
            title:'2号完成量',
            align:"center",
            dataIndex: 'day2'
          },
          {
            title:'3号完成量',
            align:"center",
            dataIndex: 'day3'
          },
          {
            title:'4号完成量',
            align:"center",
            dataIndex: 'day4'
          },
          {
            title:'5号完成量',
            align:"center",
            dataIndex: 'day5'
          },
          {
            title:'6号完成量',
            align:"center",
            dataIndex: 'day6'
          },
          {
            title:'7号完成量',
            align:"center",
            dataIndex: 'day7'
          },
          {
            title:'8号完成量',
            align:"center",
            dataIndex: 'day8'
          },
          {
            title:'9号完成量',
            align:"center",
            dataIndex: 'day9'
          },
          {
            title:'10号完成量',
            align:"center",
            dataIndex: 'day10'
          },
          {
            title:'11号完成量',
            align:"center",
            dataIndex: 'day11'
          },
          {
            title:'12号完成量',
            align:"center",
            dataIndex: 'day12'
          },
          {
            title:'13号完成量',
            align:"center",
            dataIndex: 'day13'
          },
          {
            title:'14号完成量',
            align:"center",
            dataIndex: 'day14'
          },
          {
            title:'15号完成量',
            align:"center",
            dataIndex: 'day15'
          },
          {
            title:'16号完成量',
            align:"center",
            dataIndex: 'day16'
          },
          {
            title:'17号完成量',
            align:"center",
            dataIndex: 'day17'
          },
          {
            title:'18号完成量',
            align:"center",
            dataIndex: 'day18'
          },
          {
            title:'19号完成量',
            align:"center",
            dataIndex: 'day19'
          },
          {
            title:'20号完成量',
            align:"center",
            dataIndex: 'day20'
          },
          {
            title:'21号完成量',
            align:"center",
            dataIndex: 'day21'
          },
          {
            title:'22号完成量',
            align:"center",
            dataIndex: 'day22'
          },
          {
            title:'23号完成量',
            align:"center",
            dataIndex: 'day23'
          },
          {
            title:'24号完成量',
            align:"center",
            dataIndex: 'day24'
          },
          {
            title:'25号完成量',
            align:"center",
            dataIndex: 'day25'
          },
          {
            title:'26号完成量',
            align:"center",
            dataIndex: 'day26'
          },
          {
            title:'27号完成量',
            align:"center",
            dataIndex: 'day27'
          },
          {
            title:'28号完成量',
            align:"center",
            dataIndex: 'day28'
          },
          {
            title:'29号完成量',
            align:"center",
            dataIndex: 'day29'
          },
          {
            title:'30号完成量',
            align:"center",
            dataIndex: 'day30'
          },
          {
            title:'31号完成量',
            align:"center",
            dataIndex: 'day31'
          },
          {
            title:'上月年累',
            align:"center",
            dataIndex: 'lastmonthYearfinish'
          },
          {
            title:'上月开累',
            align:"center",
            dataIndex: 'lastmonthKaifinish'
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
          list: "/bhzhntribao/bhzhntRibao/list",
          delete: "/bhzhntribao/bhzhntRibao/delete",
          deleteBatch: "/bhzhntribao/bhzhntRibao/deleteBatch",
          exportXlsUrl: "/bhzhntribao/bhzhntRibao/exportXls",
          importExcelUrl: "bhzhntribao/bhzhntRibao/importExcel",

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
        fieldList.push({type:'string',value:'danweiProj',text:'单位工程'})
        fieldList.push({type:'string',value:'fenbuProj',text:'分部工程'})
        fieldList.push({type:'string',value:'fenxiangProj',text:'分项工程'})
        fieldList.push({type:'string',value:'danwei',text:'单位'})
        fieldList.push({type:'string',value:'zongkongNum',text:'总控数量'})
        fieldList.push({type:'string',value:'dayFinish',text:'日完成量'})
        fieldList.push({type:'string',value:'monthFinish',text:'月完成量'})
        fieldList.push({type:'string',value:'yearFinish',text:'年完成量'})
        fieldList.push({type:'string',value:'allFinish',text:'开累'})
        fieldList.push({type:'string',value:'residue',text:'剩余量'})
        fieldList.push({type:'int',value:'day1',text:'1号完成量'})
        fieldList.push({type:'int',value:'day2',text:'2号完成量'})
        fieldList.push({type:'int',value:'day3',text:'3号完成量'})
        fieldList.push({type:'int',value:'day4',text:'4号完成量'})
        fieldList.push({type:'int',value:'day5',text:'5号完成量'})
        fieldList.push({type:'int',value:'day6',text:'6号完成量'})
        fieldList.push({type:'int',value:'day7',text:'7号完成量'})
        fieldList.push({type:'int',value:'day8',text:'8号完成量'})
        fieldList.push({type:'int',value:'day9',text:'9号完成量'})
        fieldList.push({type:'int',value:'day10',text:'10号完成量'})
        fieldList.push({type:'int',value:'day11',text:'11号完成量'})
        fieldList.push({type:'int',value:'day12',text:'12号完成量'})
        fieldList.push({type:'int',value:'day13',text:'13号完成量'})
        fieldList.push({type:'int',value:'day14',text:'14号完成量'})
        fieldList.push({type:'int',value:'day15',text:'15号完成量'})
        fieldList.push({type:'int',value:'day16',text:'16号完成量'})
        fieldList.push({type:'int',value:'day17',text:'17号完成量'})
        fieldList.push({type:'int',value:'day18',text:'18号完成量'})
        fieldList.push({type:'int',value:'day19',text:'19号完成量'})
        fieldList.push({type:'int',value:'day20',text:'20号完成量'})
        fieldList.push({type:'int',value:'day21',text:'21号完成量'})
        fieldList.push({type:'int',value:'day22',text:'22号完成量'})
        fieldList.push({type:'int',value:'day23',text:'23号完成量'})
        fieldList.push({type:'int',value:'day24',text:'24号完成量'})
        fieldList.push({type:'int',value:'day25',text:'25号完成量'})
        fieldList.push({type:'int',value:'day26',text:'26号完成量'})
        fieldList.push({type:'int',value:'day27',text:'27号完成量'})
        fieldList.push({type:'int',value:'day28',text:'28号完成量'})
        fieldList.push({type:'int',value:'day29',text:'29号完成量'})
        fieldList.push({type:'int',value:'day30',text:'30号完成量'})
        fieldList.push({type:'int',value:'day31',text:'31号完成量'})
        fieldList.push({type:'string',value:'lastmonthYearfinish',text:'上月年累'})
        fieldList.push({type:'string',value:'lastmonthKaifinish',text:'上月开累'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>