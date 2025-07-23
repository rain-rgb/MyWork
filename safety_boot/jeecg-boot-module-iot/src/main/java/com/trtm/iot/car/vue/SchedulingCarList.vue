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
      <a-button type="primary" icon="download" @click="handleExportXls('调度车辆')">导出</a-button>
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

    <scheduling-car-modal ref="modalForm" @ok="modalFormOk"></scheduling-car-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SchedulingCarModal from './modules/SchedulingCarModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'

  export default {
    name: 'SchedulingCarList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SchedulingCarModal,
      JSuperQuery,
    },
    data () {
      return {
        description: '调度车辆管理页面',
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
            title:'Erp端ID',
            align:"center",
            dataIndex: 'erpid'
          },
          {
            title:'生产线(1或2)',
            align:"center",
            dataIndex: 'station'
          },
          {
            title:'任务单编号',
            align:"center",
            dataIndex: 'code'
          },
          {
            title:'施工配合比编号',
            align:"center",
            dataIndex: 'recipe'
          },
          {
            title:'砂浆配合比编号',
            align:"center",
            dataIndex: 'morrec'
          },
          {
            title:'创建日期',
            align:"center",
            dataIndex: 'dattim',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
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
            title:'生产方量(含砂浆方量)',
            align:"center",
            dataIndex: 'prodmete'
          },
          {
            title:'砂浆方量',
            align:"center",
            dataIndex: 'mormete'
          },
          {
            title:'累计车次',
            align:"center",
            dataIndex: 'totvehs'
          },
          {
            title:'累计方量',
            align:"center",
            dataIndex: 'totmete'
          },
          {
            title:'质检员',
            align:"center",
            dataIndex: 'qualitor'
          },
          {
            title:'标识',
            align:"center",
            dataIndex: 'flag'
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
          list: "/car/schedulingCar/list",
          delete: "/car/schedulingCar/delete",
          deleteBatch: "/car/schedulingCar/deleteBatch",
          exportXlsUrl: "/car/schedulingCar/exportXls",
          importExcelUrl: "car/schedulingCar/importExcel",
          
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
        fieldList.push({type:'int',value:'erpid',text:'Erp端ID',dictCode:''})
        fieldList.push({type:'int',value:'station',text:'生产线(1或2)',dictCode:''})
        fieldList.push({type:'string',value:'code',text:'任务单编号',dictCode:''})
        fieldList.push({type:'string',value:'recipe',text:'施工配合比编号',dictCode:''})
        fieldList.push({type:'string',value:'morrec',text:'砂浆配合比编号',dictCode:''})
        fieldList.push({type:'date',value:'dattim',text:'创建日期'})
        fieldList.push({type:'string',value:'vehicle',text:'车辆编号',dictCode:''})
        fieldList.push({type:'string',value:'driver',text:'驾驶员',dictCode:''})
        fieldList.push({type:'double',value:'prodmete',text:'生产方量(含砂浆方量)',dictCode:''})
        fieldList.push({type:'double',value:'mormete',text:'砂浆方量',dictCode:''})
        fieldList.push({type:'int',value:'totvehs',text:'累计车次',dictCode:''})
        fieldList.push({type:'double',value:'totmete',text:'累计方量',dictCode:''})
        fieldList.push({type:'string',value:'qualitor',text:'质检员',dictCode:''})
        fieldList.push({type:'string',value:'flag',text:'标识',dictCode:''})
        fieldList.push({type:'string',value:'note',text:'备注',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>