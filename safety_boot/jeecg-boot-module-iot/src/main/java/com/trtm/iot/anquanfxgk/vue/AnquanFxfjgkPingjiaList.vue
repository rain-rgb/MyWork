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
      <a-button type="primary" icon="download" @click="handleExportXls('anquan_fxfjgk_pingjia')">导出</a-button>
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

    <anquan-fxfjgk-pingjia-modal ref="modalForm" @ok="modalFormOk"></anquan-fxfjgk-pingjia-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import AnquanFxfjgkPingjiaModal from './modules/AnquanFxfjgkPingjiaModal'

  export default {
    name: 'AnquanFxfjgkPingjiaList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      AnquanFxfjgkPingjiaModal
    },
    data () {
      return {
        description: 'anquan_fxfjgk_pingjia管理页面',
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
            title:'项目',
            align:"center",
            dataIndex: 'projectName'
          },
          {
            title:'位置',
            align:"center",
            dataIndex: 'position'
          },
          {
            title:'风险',
            align:"center",
            dataIndex: 'risk'
          },
          {
            title:'相关单位',
            align:"center",
            dataIndex: 'conherentUnit'
          },
          {
            title:'管控负责人管控情况',
            align:"center",
            dataIndex: 'managementHead'
          },
          {
            title:'管控负责人管控情况评价',
            align:"center",
            dataIndex: 'managementHeadPj'
          },
          {
            title:'安全管理人员管控情况',
            align:"center",
            dataIndex: 'securityManagement'
          },
          {
            title:'安全管理人员管控情况评价',
            align:"center",
            dataIndex: 'securityManagementPj'
          },
          {
            title:'安全负责人管控情况',
            align:"center",
            dataIndex: 'securityHead'
          },
          {
            title:'安全负责人管控情况评价',
            align:"center",
            dataIndex: 'securityHeadPj'
          },
          {
            title:'主要负责人管控情况',
            align:"center",
            dataIndex: 'head'
          },
          {
            title:'主要负责人管控情况评价',
            align:"center",
            dataIndex: 'headPj'
          },
          {
            title:'单位管控情况',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'单位管控情况评价',
            align:"center",
            dataIndex: 'unitPj'
          },
          {
            title:'总体评价',
            align:"center",
            dataIndex: 'totalityPj'
          },
          {
            title:'评价人',
            align:"center",
            dataIndex: 'pjName'
          },
          {
            title:'评价时间',
            align:"center",
            dataIndex: 'pjTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'主表guid',
            align:"center",
            dataIndex: 'baseGuid'
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
          list: "/anquanfxgk/anquanFxfjgkPingjia/list",
          delete: "/anquanfxgk/anquanFxfjgkPingjia/delete",
          deleteBatch: "/anquanfxgk/anquanFxfjgkPingjia/deleteBatch",
          exportXlsUrl: "/anquanfxgk/anquanFxfjgkPingjia/exportXls",
          importExcelUrl: "anquanfxgk/anquanFxfjgkPingjia/importExcel",
          
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
        fieldList.push({type:'string',value:'projectName',text:'项目'})
        fieldList.push({type:'string',value:'position',text:'位置'})
        fieldList.push({type:'string',value:'risk',text:'风险'})
        fieldList.push({type:'string',value:'conherentUnit',text:'相关单位'})
        fieldList.push({type:'string',value:'managementHead',text:'管控负责人管控情况'})
        fieldList.push({type:'string',value:'managementHeadPj',text:'管控负责人管控情况评价'})
        fieldList.push({type:'string',value:'securityManagement',text:'安全管理人员管控情况'})
        fieldList.push({type:'string',value:'securityManagementPj',text:'安全管理人员管控情况评价'})
        fieldList.push({type:'string',value:'securityHead',text:'安全负责人管控情况'})
        fieldList.push({type:'string',value:'securityHeadPj',text:'安全负责人管控情况评价'})
        fieldList.push({type:'string',value:'head',text:'主要负责人管控情况'})
        fieldList.push({type:'string',value:'headPj',text:'主要负责人管控情况评价'})
        fieldList.push({type:'string',value:'unitName',text:'单位管控情况'})
        fieldList.push({type:'string',value:'unitPj',text:'单位管控情况评价'})
        fieldList.push({type:'string',value:'totalityPj',text:'总体评价'})
        fieldList.push({type:'string',value:'pjName',text:'评价人'})
        fieldList.push({type:'date',value:'pjTime',text:'评价时间'})
        fieldList.push({type:'string',value:'baseGuid',text:'主表guid'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>