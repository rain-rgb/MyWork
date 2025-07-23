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
      <a-button type="primary" icon="download" @click="handleExportXls('中铁图片')">导出</a-button>
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

    <ztwzphoto-modal ref="modalForm" @ok="modalFormOk"></ztwzphoto-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ZtwzphotoModal from './modules/ZtwzphotoModal'

  export default {
    name: 'ZtwzphotoList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ZtwzphotoModal
    },
    data () {
      return {
        description: '中铁图片管理页面',
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
            title:'组织机构',
            align:"center",
            dataIndex: 'orgid'
          },
          {
            title:'主表主键',
            align:"center",
            dataIndex: 'orderid'
          },
          {
            title:'相机位置',
            align:"center",
            dataIndex: 'cameraposition'
          },
          {
            title:'相机编码',
            align:"center",
            dataIndex: 'cameracode'
          },
          {
            title:'照片类型（入场、出场）',
            align:"center",
            dataIndex: 'phototype'
          },
          {
            title:'照片链接Key',
            align:"center",
            dataIndex: 'photourl'
          },
          {
            title:'上传之前端上的主键',
            align:"center",
            dataIndex: 'oriitemid'
          },
          {
            title:'上传之前端上的主表主键',
            align:"center",
            dataIndex: 'oriorderid'
          },
          {
            title:'删除状态',
            align:"center",
            dataIndex: 'isremoved'
          },
          {
            title:'上传状态',
            align:"center",
            dataIndex: 'externaluploadstate'
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
          list: "/ztwzphoto/ztwzphoto/list",
          delete: "/ztwzphoto/ztwzphoto/delete",
          deleteBatch: "/ztwzphoto/ztwzphoto/deleteBatch",
          exportXlsUrl: "/ztwzphoto/ztwzphoto/exportXls",
          importExcelUrl: "ztwzphoto/ztwzphoto/importExcel",
          
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
        fieldList.push({type:'string',value:'orgid',text:'组织机构',dictCode:''})
        fieldList.push({type:'string',value:'orderid',text:'主表主键',dictCode:''})
        fieldList.push({type:'string',value:'cameraposition',text:'相机位置',dictCode:''})
        fieldList.push({type:'string',value:'cameracode',text:'相机编码',dictCode:''})
        fieldList.push({type:'string',value:'phototype',text:'照片类型（入场、出场）',dictCode:''})
        fieldList.push({type:'string',value:'photourl',text:'照片链接Key',dictCode:''})
        fieldList.push({type:'string',value:'oriitemid',text:'上传之前端上的主键',dictCode:''})
        fieldList.push({type:'string',value:'oriorderid',text:'上传之前端上的主表主键',dictCode:''})
        fieldList.push({type:'string',value:'isremoved',text:'删除状态',dictCode:''})
        fieldList.push({type:'string',value:'externaluploadstate',text:'上传状态',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>