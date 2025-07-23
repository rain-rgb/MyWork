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
      <a-button type="primary" icon="download" @click="handleExportXls('委外试验')">导出</a-button>
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

    <f-trial-modal ref="modalForm" @ok="modalFormOk"></f-trial-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import FTrialModal from './modules/FTrialModal'

  export default {
    name: 'FTrialList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      FTrialModal
    },
    data () {
      return {
        description: '委外试验管理页面',
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
            title:'实验ID,唯一键',
            align:"center",
            dataIndex: 'trialid'
          },
          {
            title:'指挥部实验室ID',
            align:"center",
            dataIndex: 'labid'
          },
          {
            title:'第三方名称',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'实验类型',
            align:"center",
            dataIndex: 'trialtype'
          },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'engineeringname'
          },
          {
            title:'试件编号',
            align:"center",
            dataIndex: 'reagentno'
          },
          {
            title:'龄期',
            align:"center",
            dataIndex: 'age'
          },
          {
            title:'试剂尺寸',
            align:"center",
            dataIndex: 'trialsize'
          },
          {
            title:'试件数量',
            align:"center",
            dataIndex: 'trialnum'
          },
          {
            title:'设计强度',
            align:"center",
            dataIndex: 'designstrength'
          },
          {
            title:'设计特征值',
            align:"center",
            dataIndex: 'designvalue'
          },
          {
            title:'实验时间',
            align:"center",
            dataIndex: 'trialtime'
          },
          {
            title:'实验结果0合格1不合格2未出报告',
            align:"center",
            dataIndex: 'trialresult'
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createid'
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
          list: "/outsource/fTrial/list",
          delete: "/outsource/fTrial/delete",
          deleteBatch: "/outsource/fTrial/deleteBatch",
          exportXlsUrl: "/outsource/fTrial/exportXls",
          importExcelUrl: "outsource/fTrial/importExcel",

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
        fieldList.push({type:'string',value:'trialid',text:'实验ID,唯一键'})
        fieldList.push({type:'string',value:'labid',text:'指挥部实验室ID'})
        fieldList.push({type:'string',value:'name',text:'第三方名称'})
        fieldList.push({type:'string',value:'trialtype',text:'实验类型'})
        fieldList.push({type:'string',value:'engineeringname',text:'工程名称'})
        fieldList.push({type:'string',value:'reagentno',text:'试件编号'})
        fieldList.push({type:'int',value:'age',text:'龄期'})
        fieldList.push({type:'string',value:'trialsize',text:'试剂尺寸'})
        fieldList.push({type:'int',value:'trialnum',text:'试件数量'})
        fieldList.push({type:'string',value:'designstrength',text:'设计强度'})
        fieldList.push({type:'string',value:'designvalue',text:'设计特征值'})
        fieldList.push({type:'string',value:'trialtime',text:'实验时间'})
        fieldList.push({type:'int',value:'trialresult',text:'实验结果0合格1不合格2未出报告'})
        fieldList.push({type:'string',value:'createid',text:'创建人'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
