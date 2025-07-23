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
      <a-button type="primary" icon="download" @click="handleExportXls('w_yandu_s')">导出</a-button>
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

    <w-yandu-s-modal ref="modalForm" @ok="modalFormOk"></w-yandu-s-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WYanduSModal from './modules/WYanduSModal'

  export default {
    name: 'WYanduSList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WYanduSModal
    },
    data () {
      return {
        description: 'w_yandu_s管理页面',
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
            title:'syjid',
            align:"center",
            dataIndex: 'syjid'
          },
          {
            title:'fxh',
            align:"center",
            dataIndex: 'fxh'
          },
          {
            title:'yandu1',
            align:"center",
            dataIndex: 'yandu1'
          },
          {
            title:'yandu2',
            align:"center",
            dataIndex: 'yandu2'
          },
          {
            title:'yandu3',
            align:"center",
            dataIndex: 'yandu3'
          },
          {
            title:'testtime',
            align:"center",
            dataIndex: 'testtime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'fWtbh',
            align:"center",
            dataIndex: 'fWtbh'
          },
          {
            title:'fsbbh',
            align:"center",
            dataIndex: 'fsbbh'
          },
          {
            title:'fSjbh',
            align:"center",
            dataIndex: 'fSjbh'
          },
          {
            title:'isvalid',
            align:"center",
            dataIndex: 'isvalid'
          },
          {
            title:'status',
            align:"center",
            dataIndex: 'status'
          },
          {
            title:'submittime',
            align:"center",
            dataIndex: 'submittime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'张拉力',
            align:"center",
            dataIndex: 'zll'
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
          list: "/ydcx/wYanduS/list",
          delete: "/ydcx/wYanduS/delete",
          deleteBatch: "/ydcx/wYanduS/deleteBatch",
          exportXlsUrl: "/ydcx/wYanduS/exportXls",
          importExcelUrl: "ydcx/wYanduS/importExcel",
          
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
        fieldList.push({type:'string',value:'syjid',text:'syjid'})
        fieldList.push({type:'string',value:'fxh',text:'fxh'})
        fieldList.push({type:'string',value:'yandu1',text:'yandu1'})
        fieldList.push({type:'string',value:'yandu2',text:'yandu2'})
        fieldList.push({type:'string',value:'yandu3',text:'yandu3'})
        fieldList.push({type:'date',value:'testtime',text:'testtime'})
        fieldList.push({type:'string',value:'fWtbh',text:'fWtbh'})
        fieldList.push({type:'string',value:'fsbbh',text:'fsbbh'})
        fieldList.push({type:'string',value:'fSjbh',text:'fSjbh'})
        fieldList.push({type:'string',value:'isvalid',text:'isvalid'})
        fieldList.push({type:'string',value:'status',text:'status'})
        fieldList.push({type:'date',value:'submittime',text:'submittime'})
        fieldList.push({type:'string',value:'zll',text:'张拉力'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>