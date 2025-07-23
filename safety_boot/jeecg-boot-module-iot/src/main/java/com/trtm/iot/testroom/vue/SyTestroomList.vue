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
      <a-button type="primary" icon="download" @click="handleExportXls('sy_testroom')">导出</a-button>
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

    <sy-testroom-modal ref="modalForm" @ok="modalFormOk"></sy-testroom-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyTestroomModal from './modules/SyTestroomModal'

  export default {
    name: 'SyTestroomList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyTestroomModal
    },
    data () {
      return {
        description: 'sy_testroom管理页面',
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
            title:'试验室',
            align:"center",
            dataIndex: 'testroom'
          },
          {
            title:'类型',
            align:"center",
            dataIndex: 'roomType'
          },
          {
            title:'面积',
            align:"center",
            dataIndex: 'roomArea'
          },
          {
            title:'负责人',
            align:"center",
            dataIndex: 'fuzer'
          },
          {
            title:'设备数',
            align:"center",
            dataIndex: 'shebeishu'
          },
          {
            title:'人员数',
            align:"center",
            dataIndex: 'renyuanshu'
          },
          {
            title:'环境设备',
            align:"center",
            dataIndex: 'huanjsb'
          },
          {
            title:'温度要求',
            align:"center",
            dataIndex: 'wendu1'
          },
          {
            title:'实时温度',
            align:"center",
            dataIndex: 'wendu2'
          },
          {
            title:'湿度要求',
            align:"center",
            dataIndex: 'shidu1'
          },
          {
            title:'实时湿度',
            align:"center",
            dataIndex: 'shidu2'
          },
          {
            title:'设备列表',
            align:"center",
            dataIndex: 'shebeis'
          },
          {
            title:'人员列表',
            align:"center",
            dataIndex: 'renyuans'
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
          list: "/testroom/syTestroom/list",
          delete: "/testroom/syTestroom/delete",
          deleteBatch: "/testroom/syTestroom/deleteBatch",
          exportXlsUrl: "/testroom/syTestroom/exportXls",
          importExcelUrl: "testroom/syTestroom/importExcel",
          
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
        fieldList.push({type:'string',value:'testroom',text:'试验室'})
        fieldList.push({type:'string',value:'roomType',text:'类型'})
        fieldList.push({type:'string',value:'roomArea',text:'面积'})
        fieldList.push({type:'string',value:'fuzer',text:'负责人'})
        fieldList.push({type:'string',value:'shebeishu',text:'设备数'})
        fieldList.push({type:'string',value:'renyuanshu',text:'人员数'})
        fieldList.push({type:'string',value:'huanjsb',text:'环境设备'})
        fieldList.push({type:'string',value:'wendu1',text:'温度要求'})
        fieldList.push({type:'string',value:'wendu2',text:'实时温度'})
        fieldList.push({type:'string',value:'shidu1',text:'湿度要求'})
        fieldList.push({type:'string',value:'shidu2',text:'实时湿度'})
        fieldList.push({type:'string',value:'shebeis',text:'设备列表'})
        fieldList.push({type:'string',value:'renyuans',text:'人员列表'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>