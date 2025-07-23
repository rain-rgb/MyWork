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
      <a-button type="primary" icon="download" @click="handleExportXls('试验人员管理')">导出</a-button>
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
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >

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

    <sy-renyuanguanli-modal ref="modalForm" @ok="modalFormOk"></sy-renyuanguanli-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyRenyuanguanliModal from './modules/SyRenyuanguanliModal'

  export default {
    name: 'SyRenyuanguanliList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyRenyuanguanliModal
    },
    data () {
      return {
        description: '试验人员管理管理页面',
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
            title:'人员编码(工号)',
            align:"center",
            dataIndex: 'usercode'
          },
          {
            title:'人员名称',
            align:"center",
            dataIndex: 'username'
          },
          {
            title:'性别',
            align:"center",
            dataIndex: 'sex'
          },
          {
            title:'所在单位',
            align:"center",
            dataIndex: 'ssdw_dictText'
          },
          // {
          //   title:'所属组织机构',
          //   align:"center",
          //   dataIndex: 'sysorgcode'
          // },
          {
            title:'入职时间',
            align:"center",
            dataIndex: 'ruzhitime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'在职状态',
            align:"center",
            dataIndex: 'iszz_dictText'
          },
          {
            title:'负责检测参数',
            align:"center",
            dataIndex: 'jccs'
          },
          {
            title:'所属职位',
            align:"center",
            dataIndex: 'post_dictText'
          },
          // {
          //   title:'联系方式',
          //   align:"center",
          //   dataIndex: 'phone'
          // },
          // {
          //   title:'通讯地址',
          //   align:"center",
          //   dataIndex: 'addr'
          // },
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
          list: "/syztgl/syRenyuanguanli/list",
          delete: "/syztgl/syRenyuanguanli/delete",
          deleteBatch: "/syztgl/syRenyuanguanli/deleteBatch",
          exportXlsUrl: "/syztgl/syRenyuanguanli/exportXls",
          importExcelUrl: "syztgl/syRenyuanguanli/importExcel",
          
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
        fieldList.push({type:'string',value:'usercode',text:'人员编码(工号)'})
        fieldList.push({type:'string',value:'username',text:'人员名称'})
        fieldList.push({type:'int',value:'sex',text:'性别（0男；1女）'})
        fieldList.push({type:'string',value:'ssdw',text:'所属单位'})
        fieldList.push({type:'string',value:'sysorgcode',text:'所属组织机构'})
        fieldList.push({type:'date',value:'ruzhitime',text:'入职时间'})
        fieldList.push({type:'string',value:'iszz',text:'是否在职（0在职；1离职）'})
        fieldList.push({type:'string',value:'jccs',text:'负责检测参数'})
        fieldList.push({type:'string',value:'post',text:'所属职位'})
        fieldList.push({type:'string',value:'phone',text:'联系方式'})
        fieldList.push({type:'string',value:'addr',text:'通讯地址'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>