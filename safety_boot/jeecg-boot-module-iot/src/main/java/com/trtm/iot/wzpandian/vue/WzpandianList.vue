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
      <a-button type="primary" icon="download" @click="handleExportXls('wzpandian')">导出</a-button>
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

    <wzpandian-modal ref="modalForm" @ok="modalFormOk"></wzpandian-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WzpandianModal from './modules/WzpandianModal'

  export default {
    name: 'WzpandianList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WzpandianModal
    },
    data () {
      return {
        description: 'wzpandian管理页面',
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
            title:'材料编号',
            align:"center",
            dataIndex: 'cailiaono'
          },
          {
            title:'材料名称',
            align:"center",
            dataIndex: 'cailiaoname'
          },
          {
            title:'材料类型',
            align:"center",
            dataIndex: 'nodetype'
          },
          {
            title:'规格型号',
            align:"center",
            dataIndex: 'guigexinghao'
          },
          {
            title:'材料计量单位',
            align:"center",
            dataIndex: 'cailiaojiliangdanwei'
          },
          {
            title:'盘点材料数量',
            align:"center",
            dataIndex: 'cailiaonum'
          },
          {
            title:'盘点时仓库数量',
            align:"center",
            dataIndex: 'liaocangnum'
          },
          {
            title:'料仓编号',
            align:"center",
            dataIndex: 'liaocangno'
          },
          {
            title:'批次编号',
            align:"center",
            dataIndex: 'picino'
          },
          {
            title:'拍照照片',
            align:"center",
            dataIndex: 'pictures'
          },
          {
            title:'盘点时间',
            align:"center",
            dataIndex: 'pandiantime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createpersonl'
          },
          {
            title:'创建日期',
            align:"center",
            dataIndex: 'createtime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
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
          list: "/wzpandian/wzpandian/list",
          delete: "/wzpandian/wzpandian/delete",
          deleteBatch: "/wzpandian/wzpandian/deleteBatch",
          exportXlsUrl: "/wzpandian/wzpandian/exportXls",
          importExcelUrl: "wzpandian/wzpandian/importExcel",
          
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
        fieldList.push({type:'string',value:'cailiaono',text:'材料编号'})
        fieldList.push({type:'string',value:'cailiaoname',text:'材料名称'})
        fieldList.push({type:'string',value:'nodetype',text:'材料类型'})
        fieldList.push({type:'string',value:'guigexinghao',text:'规格型号'})
        fieldList.push({type:'string',value:'cailiaojiliangdanwei',text:'材料计量单位'})
        fieldList.push({type:'string',value:'cailiaonum',text:'盘点材料数量'})
        fieldList.push({type:'string',value:'liaocangnum',text:'盘点时仓库数量'})
        fieldList.push({type:'string',value:'liaocangno',text:'料仓编号'})
        fieldList.push({type:'string',value:'picino',text:'批次编号'})
        fieldList.push({type:'string',value:'pictures',text:'拍照照片'})
        fieldList.push({type:'date',value:'pandiantime',text:'盘点时间'})
        fieldList.push({type:'string',value:'createpersonl',text:'创建人'})
        fieldList.push({type:'date',value:'createtime',text:'创建日期'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>