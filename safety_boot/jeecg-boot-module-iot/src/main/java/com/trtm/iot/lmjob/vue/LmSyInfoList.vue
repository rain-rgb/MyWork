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
      <a-button type="primary" icon="download" @click="handleExportXls('lm_sy_info')">导出</a-button>
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

    <lm-sy-info-modal ref="modalForm" @ok="modalFormOk"></lm-sy-info-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import LmSyInfoModal from './modules/LmSyInfoModal'

  export default {
    name: 'LmSyInfoList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      LmSyInfoModal
    },
    data () {
      return {
        description: 'lm_sy_info管理页面',
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
            title:'试验类型：施工检测；监理检测；业主检测；竣工检测',
            align:"center",
            dataIndex: 'syType'
          },
          {
            title:'结论描述',
            align:"center",
            dataIndex: 'resultinfo'
          },
          {
            title:'0：合格；1不合格',
            align:"center",
            dataIndex: 'reslut'
          },
          {
            title:'试验日期',
            align:"center",
            dataIndex: 'jctime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'部位',
            align:"center",
            dataIndex: 'usewbs'
          },
          {
            title:'附件上传',
            align:"center",
            dataIndex: 'file'
          },
          {
            title:'闭合状态：0：未闭合；20已闭合',
            align:"center",
            dataIndex: 'bihe'
          },
          {
            title:'试验名称',
            align:"center",
            dataIndex: 'syname'
          },
          {
            title:'beizhu',
            align:"center",
            dataIndex: 'beizhu'
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
          list: "/lmjob/lmSyInfo/list",
          delete: "/lmjob/lmSyInfo/delete",
          deleteBatch: "/lmjob/lmSyInfo/deleteBatch",
          exportXlsUrl: "/lmjob/lmSyInfo/exportXls",
          importExcelUrl: "lmjob/lmSyInfo/importExcel",
          
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
        fieldList.push({type:'string',value:'syType',text:'试验类型：施工检测；监理检测；业主检测；竣工检测'})
        fieldList.push({type:'string',value:'resultinfo',text:'结论描述'})
        fieldList.push({type:'int',value:'reslut',text:'0：合格；1不合格'})
        fieldList.push({type:'date',value:'jctime',text:'试验日期'})
        fieldList.push({type:'string',value:'usewbs',text:'部位'})
        fieldList.push({type:'string',value:'file',text:'附件上传'})
        fieldList.push({type:'string',value:'bihe',text:'闭合状态：0：未闭合；20已闭合'})
        fieldList.push({type:'string',value:'syname',text:'试验名称'})
        fieldList.push({type:'string',value:'beizhu',text:'beizhu'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>