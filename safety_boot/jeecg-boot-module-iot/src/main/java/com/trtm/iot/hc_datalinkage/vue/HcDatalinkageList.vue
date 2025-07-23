<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form @keyup.enter.native="searchQuery" layout="inline">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" icon="plus" type="primary">新增</a-button>
      <a-button @click="handleExportXls('数据联动（运输信息）')" icon="download" type="primary">导出</a-button>
      <a-upload :action="importExcelUrl" :headers="tokenHeader" :multiple="false" :showUploadList="false" @change="handleImportExcel" name="file">
        <a-button icon="import" type="primary">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" @handleSuperQuery="handleSuperQuery" ref="superQueryModal"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item @click="batchDel" key="1"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a @click="onClearSelected" style="margin-left: 24px">清空</a>
      </div>

      <a-table
        :columns="columns"
        :dataSource="dataSource"
        :loading="loading"
        :pagination="ipagination"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        :scroll="{x:true}"
        @change="handleTableChange"
        bordered
        class="j-table-force-nowrap"
        ref="table"
        rowKey="id"
        size="middle">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span style="font-size: 12px;font-style: italic;" v-if="!text">无图片</span>
          <img :src="getImgView(text)" alt="" height="25px" style="max-width:80px;font-size: 12px;font-style: italic;" v-else/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span style="font-size: 12px;font-style: italic;" v-if="!text">无文件</span>
          <a-button
            :ghost="true"
            @click="downloadFile(text)"
            icon="download"
            size="small"
            type="primary"
            v-else>
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
                <a-popconfirm @confirm="() => handleDelete(record.id)" title="确定删除吗?">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <hc-datalinkage-modal @ok="modalFormOk" ref="modalForm"/>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import HcDatalinkageModal from './modules/HcDatalinkageModal'
  import '@/assets/less/TableExpand.less'

  export default {
    name: "HcDatalinkageList",
    mixins:[JeecgListMixin],
    components: {
      HcDatalinkageModal
    },
    data () {
      return {
        description: '数据联动（运输信息）管理页面',
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
            title:'运输车id',
            align:"center",
            dataIndex: 'truckid'
          },
          {
            title:'运输车id',
            align:"center",
            dataIndex: 'truckid'
          },
          {
            title:'出厂时间',
            align:"center",
            dataIndex: 'outstationtime'
          },
          {
            title:'出厂时间',
            align:"center",
            dataIndex: 'outstationtime'
          },
          {
            title:'卸料时间',
            align:"center",
            dataIndex: 'loadoffmixturetime'
          },
          {
            title:'卸料时间',
            align:"center",
            dataIndex: 'loadoffmixturetime'
          },
          {
            title:'接料时长',
            align:"center",
            dataIndex: 'loadmixturetime'
          },
          {
            title:'接料时长',
            align:"center",
            dataIndex: 'loadmixturetime'
          },
          {
            title:'摊铺记录id数组，逗号分隔',
            align:"center",
            dataIndex: 'pavemixtureinfoids'
          },
          {
            title:'摊铺记录id数组，逗号分隔',
            align:"center",
            dataIndex: 'pavemixtureinfoids'
          },
          {
            title:'混合料生产详情id数组，逗号分隔',
            align:"center",
            dataIndex: 'pavemixturematerialinfoids'
          },
          {
            title:'混合料生产详情id数组，逗号分隔',
            align:"center",
            dataIndex: 'pavemixturematerialinfoids'
          },
          {
            title:'混合料类型，1是水稳，2是沥青',
            align:"center",
            dataIndex: 'materialtype'
          },
          {
            title:'混合料类型，1是水稳，2是沥青',
            align:"center",
            dataIndex: 'materialtype'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/hc_datalinkage/hcDatalinkage/list",
          delete: "/hc_datalinkage/hcDatalinkage/delete",
          deleteBatch: "/hc_datalinkage/hcDatalinkage/deleteBatch",
          exportXlsUrl: "/hc_datalinkage/hcDatalinkage/exportXls",
          importExcelUrl: "hc_datalinkage/hcDatalinkage/importExcel",

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
      }
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
         fieldList.push({type:'string',value:'truckid',text:'运输车id'})
         fieldList.push({type:'string',value:'truckid',text:'运输车id',dictCode:''})
         fieldList.push({type:'string',value:'outstationtime',text:'出厂时间'})
         fieldList.push({type:'string',value:'outstationtime',text:'出厂时间',dictCode:''})
         fieldList.push({type:'string',value:'loadoffmixturetime',text:'卸料时间'})
         fieldList.push({type:'string',value:'loadoffmixturetime',text:'卸料时间',dictCode:''})
         fieldList.push({type:'string',value:'loadmixturetime',text:'接料时长',dictCode:''})
         fieldList.push({type:'string',value:'loadmixturetime',text:'接料时长'})
         fieldList.push({type:'string',value:'pavemixtureinfoids',text:'摊铺记录id数组，逗号分隔',dictCode:''})
         fieldList.push({type:'string',value:'pavemixtureinfoids',text:'摊铺记录id数组，逗号分隔'})
         fieldList.push({type:'string',value:'pavemixturematerialinfoids',text:'混合料生产详情id数组，逗号分隔'})
         fieldList.push({type:'string',value:'pavemixturematerialinfoids',text:'混合料生产详情id数组，逗号分隔',dictCode:''})
         fieldList.push({type:'int',value:'materialtype',text:'混合料类型，1是水稳，2是沥青',dictCode:''})
         fieldList.push({type:'int',value:'materialtype',text:'混合料类型，1是水稳，2是沥青'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
