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
      <a-button @click="handleExportXls('华测获取项目')" icon="download" type="primary">导出</a-button>
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

    <hc-project-modal @ok="modalFormOk" ref="modalForm"></hc-project-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import HcProjectModal from './modules/HcProjectModal'

  export default {
    name: 'HcProjectList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      HcProjectModal
    },
    data () {
      return {
        description: '华测获取项目管理页面',
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
            title:'工程ID',
            align:"center",
            dataIndex: 'pjid'
          },
          {
            title:'工程名',
            align:"center",
            dataIndex: 'pjname'
          },
          {
            title:'桩号规范，K4+398或者DK4+398',
            align:"center",
            dataIndex: 'pjstationprefix'
          },
          {
            title:'项目描述',
            align:"center",
            dataIndex: 'pjdescription'
          },
          {
            title:'项目开始施工时间',
            align:"center",
            dataIndex: 'pjstartdate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'项目形状类型，0：线型，1：面状',
            align:"center",
            dataIndex: 'pjshapetype'
          },
          {
            title:'项目业主单位id',
            align:"center",
            dataIndex: 'pjownercompanyid'
          },
          {
            title:'项目业主单位',
            align:"center",
            dataIndex: 'pjownercompany'
          },
          {
            title:'对应的项目编码',
            align:"center",
            dataIndex: 'orgcode'
          },
          {
            title:'备用',
            align:"center",
            dataIndex: 'beiyong'
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
          list: "/hc_project/hcProject/list",
          delete: "/hc_project/hcProject/delete",
          deleteBatch: "/hc_project/hcProject/deleteBatch",
          exportXlsUrl: "/hc_project/hcProject/exportXls",
          importExcelUrl: "hc_project/hcProject/importExcel",

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
        fieldList.push({type:'string',value:'pjid',text:'工程ID'})
        fieldList.push({type:'string',value:'pjname',text:'工程名'})
        fieldList.push({type:'string',value:'pjstationprefix',text:'桩号规范，K4+398或者DK4+398'})
        fieldList.push({type:'string',value:'pjdescription',text:'项目描述'})
        fieldList.push({type:'date',value:'pjstartdate',text:'项目开始施工时间'})
        fieldList.push({type:'string',value:'pjshapetype',text:'项目形状类型，0：线型，1：面状'})
        fieldList.push({type:'string',value:'pjownercompanyid',text:'项目业主单位id'})
        fieldList.push({type:'string',value:'pjownercompany',text:'项目业主单位'})
        fieldList.push({type:'string',value:'orgcode',text:'对应的项目编码'})
        fieldList.push({type:'string',value:'beiyong',text:'备用'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
