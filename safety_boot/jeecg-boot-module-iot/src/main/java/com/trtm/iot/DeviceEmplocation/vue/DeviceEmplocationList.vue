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
      <a-button type="primary" icon="download" @click="handleExportXls('人员定位表信息')">导出</a-button>
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

    <device-emplocation-modal ref="modalForm" @ok="modalFormOk"></device-emplocation-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceEmplocationModal from './modules/DeviceEmplocationModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'

  export default {
    name: 'DeviceEmplocationList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceEmplocationModal,
      JSuperQuery,
    },
    data () {
      return {
        description: '人员定位表信息管理页面',
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
            title:'用户id',
            align:"center",
            dataIndex: 'empid'
          },
          {
            title:'部门id',
            align:"center",
            dataIndex: 'devid'
          },
          {
            title:'进入时间',
            align:"center",
            dataIndex: 'entersitetime'
          },
          {
            title:'进入隧道时间',
            align:"center",
            dataIndex: 'entersdtime'
          },
          {
            title:'other1',
            align:"center",
            dataIndex: 'other1'
          },
          {
            title:'other2',
            align:"center",
            dataIndex: 'other2'
          },
          {
            title:'other3',
            align:"center",
            dataIndex: 'other3'
          },
          {
            title:'tagid',
            align:"center",
            dataIndex: 'tagid'
          },
          {
            title:'用户名',
            align:"center",
            dataIndex: 'empname'
          },
          {
            title:'隧道名称',
            align:"center",
            dataIndex: 'deptname'
          },
          {
            title:'班组',
            align:"center",
            dataIndex: 'jobname'
          },
          {
            title:'位置名称',
            align:"center",
            dataIndex: 'devname'
          },
          {
            title:'isinside',
            align:"center",
            dataIndex: 'isinside'
          },
          {
            title:'路线名称',
            align:"center",
            dataIndex: 'tunnelid'
          },
          {
            title:'deptid',
            align:"center",
            dataIndex: 'deptid'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'shebeino'
          },
          {
            title:'路线名称',
            align:"center",
            dataIndex: 'tunnelname'
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
          list: "/DeviceEmplocation/deviceEmplocation/list",
          delete: "/DeviceEmplocation/deviceEmplocation/delete",
          deleteBatch: "/DeviceEmplocation/deviceEmplocation/deleteBatch",
          exportXlsUrl: "/DeviceEmplocation/deviceEmplocation/exportXls",
          importExcelUrl: "DeviceEmplocation/deviceEmplocation/importExcel",
          
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
        fieldList.push({type:'int',value:'empid',text:'用户id',dictCode:''})
        fieldList.push({type:'int',value:'devid',text:'部门id',dictCode:''})
        fieldList.push({type:'string',value:'entersitetime',text:'进入时间',dictCode:''})
        fieldList.push({type:'string',value:'entersdtime',text:'进入隧道时间',dictCode:''})
        fieldList.push({type:'string',value:'other1',text:'other1',dictCode:''})
        fieldList.push({type:'string',value:'other2',text:'other2',dictCode:''})
        fieldList.push({type:'string',value:'other3',text:'other3',dictCode:''})
        fieldList.push({type:'string',value:'tagid',text:'tagid',dictCode:''})
        fieldList.push({type:'string',value:'empname',text:'用户名',dictCode:''})
        fieldList.push({type:'string',value:'deptname',text:'隧道名称',dictCode:''})
        fieldList.push({type:'string',value:'jobname',text:'班组',dictCode:''})
        fieldList.push({type:'string',value:'devname',text:'位置名称',dictCode:''})
        fieldList.push({type:'string',value:'isinside',text:'isinside',dictCode:''})
        fieldList.push({type:'int',value:'tunnelid',text:'路线名称',dictCode:''})
        fieldList.push({type:'int',value:'deptid',text:'deptid',dictCode:''})
        fieldList.push({type:'string',value:'shebeino',text:'设备编号',dictCode:''})
        fieldList.push({type:'string',value:'tunnelname',text:'路线名称',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>