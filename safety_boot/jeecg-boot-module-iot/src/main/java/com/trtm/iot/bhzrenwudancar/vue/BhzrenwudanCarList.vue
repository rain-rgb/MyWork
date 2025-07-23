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
      <a-button type="primary" icon="download" @click="handleExportXls('中铁一局拌合站生产流向')">导出</a-button>
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

    <bhzrenwudan-car-modal ref="modalForm" @ok="modalFormOk"></bhzrenwudan-car-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BhzrenwudanCarModal from './modules/BhzrenwudanCarModal'

  export default {
    name: 'BhzrenwudanCarList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BhzrenwudanCarModal
    },
    data () {
      return {
        description: '中铁一局拌合站生产流向管理页面',
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
            title:'票据号',
            align:"center",
            dataIndex: 'recipe'
          },
          {
            title:'任务单编号',
            align:"center",
            dataIndex: 'code'
          },
          {
            title:'生产线(1或2)',
            align:"center",
            dataIndex: 'station'
          },
          {
            title:'砂浆配合比编号',
            align:"center",
            dataIndex: 'morrec'
          },
          {
            title:'单位工程',
            align:"center",
            dataIndex: 'projname'
          },
          {
            title:'分部工程',
            align:"center",
            dataIndex: 'projtype'
          },
          {
            title:'分项工程',
            align:"center",
            dataIndex: 'projgrade'
          },
          {
            title:'里程',
            align:"center",
            dataIndex: 'pilemile'
          },
          {
            title:'强度等级',
            align:"center",
            dataIndex: 'betlev'
          },
          {
            title:'生产方量(含砂浆方量',
            align:"center",
            dataIndex: 'prodmete'
          },
          {
            title:'砂浆方量',
            align:"center",
            dataIndex: 'mormete'
          },
          {
            title:'累计方量',
            align:"center",
            dataIndex: 'totmete'
          },
          {
            title:'运输方量',
            align:"center",
            dataIndex: 'transportvolume'
          },
          {
            title:'任务方量',
            align:"center",
            dataIndex: 'mete'
          },
          {
            title:'节超方量',
            align:"center",
            dataIndex: 'dvalue'
          },
          {
            title:'车辆编号',
            align:"center",
            dataIndex: 'vehicle'
          },
          {
            title:'驾驶员',
            align:"center",
            dataIndex: 'driver'
          },
          {
            title:'发货日期',
            align:"center",
            dataIndex: 'dattim'
          },
          {
            title:'发货时间',
            align:"center",
            dataIndex: 'dattimShij'
          },
          {
            title:'浇注日期',
            align:"center",
            dataIndex: 'begtim'
          },
          {
            title:'截止日期',
            align:"center",
            dataIndex: 'endtim'
          },
          {
            title:'调度人员',
            align:"center",
            dataIndex: 'attamper'
          },
          {
            title:'到工作面时间',
            align:"center",
            dataIndex: 'dgzmsj'
          },
          {
            title:'卸料时间',
            align:"center",
            dataIndex: 'xlsj'
          },
          {
            title:'卸完时间',
            align:"center",
            dataIndex: 'xwsj'
          },
          {
            title:'账期',
            align:"center",
            dataIndex: 'zq'
          },
          {
            title:'使用单位',
            align:"center",
            dataIndex: 'sydw'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'note'
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
          list: "/bhzrenwudancar/bhzrenwudanCar/list",
          delete: "/bhzrenwudancar/bhzrenwudanCar/delete",
          deleteBatch: "/bhzrenwudancar/bhzrenwudanCar/deleteBatch",
          exportXlsUrl: "/bhzrenwudancar/bhzrenwudanCar/exportXls",
          importExcelUrl: "bhzrenwudancar/bhzrenwudanCar/importExcel",
          
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
        fieldList.push({type:'string',value:'recipe',text:'票据号',dictCode:''})
        fieldList.push({type:'string',value:'code',text:'任务单编号',dictCode:''})
        fieldList.push({type:'string',value:'station',text:'生产线(1或2)',dictCode:''})
        fieldList.push({type:'string',value:'morrec',text:'砂浆配合比编号',dictCode:''})
        fieldList.push({type:'string',value:'projname',text:'单位工程',dictCode:''})
        fieldList.push({type:'string',value:'projtype',text:'分部工程',dictCode:''})
        fieldList.push({type:'string',value:'projgrade',text:'分项工程',dictCode:''})
        fieldList.push({type:'string',value:'pilemile',text:'里程',dictCode:''})
        fieldList.push({type:'string',value:'betlev',text:'强度等级',dictCode:''})
        fieldList.push({type:'double',value:'prodmete',text:'生产方量(含砂浆方量',dictCode:''})
        fieldList.push({type:'double',value:'mormete',text:'砂浆方量',dictCode:''})
        fieldList.push({type:'double',value:'totmete',text:'累计方量',dictCode:''})
        fieldList.push({type:'double',value:'transportvolume',text:'运输方量',dictCode:''})
        fieldList.push({type:'double',value:'mete',text:'任务方量',dictCode:''})
        fieldList.push({type:'double',value:'dvalue',text:'节超方量',dictCode:''})
        fieldList.push({type:'string',value:'vehicle',text:'车辆编号',dictCode:''})
        fieldList.push({type:'string',value:'driver',text:'驾驶员',dictCode:''})
        fieldList.push({type:'Date',value:'dattim',text:'发货日期',dictCode:''})
        fieldList.push({type:'Date',value:'dattimShij',text:'发货时间',dictCode:''})
        fieldList.push({type:'Date',value:'begtim',text:'浇注日期',dictCode:''})
        fieldList.push({type:'Date',value:'endtim',text:'截止日期',dictCode:''})
        fieldList.push({type:'string',value:'attamper',text:'调度人员',dictCode:''})
        fieldList.push({type:'Date',value:'dgzmsj',text:'到工作面时间',dictCode:''})
        fieldList.push({type:'string',value:'xlsj',text:'卸料时间',dictCode:''})
        fieldList.push({type:'string',value:'xwsj',text:'卸完时间',dictCode:''})
        fieldList.push({type:'string',value:'zq',text:'账期',dictCode:''})
        fieldList.push({type:'string',value:'sydw',text:'使用单位',dictCode:''})
        fieldList.push({type:'string',value:'note',text:'备注',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>