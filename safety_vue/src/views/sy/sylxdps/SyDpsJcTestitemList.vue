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
      <a-button type="primary" icon="download" @click="handleExportXls('sy_dps_jc_testitem')">导出</a-button>
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

    <sy-dps-jc-testitem-modal ref="modalForm" @ok="modalFormOk"></sy-dps-jc-testitem-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyDpsJcTestitemModal from './modules/SyDpsJcTestitemModal'

  export default {
    name: 'SyDpsJcTestitemList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyDpsJcTestitemModal
    },
    data () {
      return {
        description: 'sy_dps_jc_testitem管理页面',
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
          // {
          //   title:'主键UUID',
          //   align:"center",
          //   dataIndex: 'uuid'
          // },
          {
            title:'试验项目类型',
            align:"center",
            dataIndex: 'titcode'
          },
          {
            title:'规程编号',
            align:"center",
            dataIndex: 'ruleno'
          },
          {
            title:'试验编号',
            align:"center",
            dataIndex: 'tino'
          },
          {
            title:'试验项目名称',
            align:"center",
            dataIndex: 'tiname'
          },
          {
            title:'试验项目参数号',
            align:"center",
            dataIndex: 'tiparameternum'
          },
          {
            title:'试验项目表号',
            align:"center",
            dataIndex: 'titablenum'
          },
          {
            title:'试验项目表格名称',
            align:"center",
            dataIndex: 'titablename'
          },
          {
            title:'试验项目默认组数',
            align:"center",
            dataIndex: 'tidefaultgroupnum'
          },
          {
            title:'试验项目排序号',
            align:"center",
            dataIndex: 'tisort'
          },
          {
            title:'试验项目备注',
            align:"center",
            dataIndex: 'tiremark'
          },
          {
            title:'自定义表号',
            align:"center",
            dataIndex: 'ticustomtablenum'
          },
          {
            title:'类型（0：试验项目1：报告2：评定）',
            align:"center",
            dataIndex: 'titype'
          },
          {
            title:'删除状态',
            align:"center",
            dataIndex: 'tiisdel'
          },
          {
            title:'技术指标',
            align:"center",
            dataIndex: 'tijishuzhibiao'
          },
          {
            title:'重要指标',
            align:"center",
            dataIndex: 'tizhongyaozhibiao'
          },
          {
            title:'重要指标名称',
            align:"center",
            dataIndex: 'tizhongyaozhibiaoname'
          },
          {
            title:'报告台帐名称',
            align:"center",
            dataIndex: 'tibaogaotaizhangname'
          },
          {
            title:'报告台帐编号',
            align:"center",
            dataIndex: 'tibaogaotaizhangno'
          },
          // {
          //   title:'plusmark',
          //   align:"center",
          //   dataIndex: 'plusmark'
          // },
          // {
          //   title:'tip',
          //   align:"center",
          //   dataIndex: 'tip'
          // },
          // {
          //   title:'tipandingyiju',
          //   align:"center",
          //   dataIndex: 'tipandingyiju'
          // },
          // {
          //   title:'tishiyanyiju',
          //   align:"center",
          //   dataIndex: 'tishiyanyiju'
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
          list: "/sylxdps/syDpsJcTestitem/list",
          delete: "/sylxdps/syDpsJcTestitem/delete",
          deleteBatch: "/sylxdps/syDpsJcTestitem/deleteBatch",
          exportXlsUrl: "/sylxdps/syDpsJcTestitem/exportXls",
          importExcelUrl: "sylxdps/syDpsJcTestitem/importExcel",
          
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
        fieldList.push({type:'string',value:'uuid',text:'主键UUID'})
        fieldList.push({type:'string',value:'titcode',text:'试验项目类型'})
        fieldList.push({type:'string',value:'ruleno',text:'规程编号'})
        fieldList.push({type:'string',value:'tino',text:'试验编号'})
        fieldList.push({type:'string',value:'tiname',text:'试验项目名称'})
        fieldList.push({type:'string',value:'tiparameternum',text:'试验项目参数号'})
        fieldList.push({type:'string',value:'titablenum',text:'试验项目表号'})
        fieldList.push({type:'string',value:'titablename',text:'试验项目表格名称'})
        fieldList.push({type:'int',value:'tidefaultgroupnum',text:'试验项目默认组数'})
        fieldList.push({type:'int',value:'tisort',text:'试验项目排序号'})
        fieldList.push({type:'string',value:'tiremark',text:'试验项目备注'})
        fieldList.push({type:'string',value:'ticustomtablenum',text:'自定义表号'})
        fieldList.push({type:'int',value:'titype',text:'类型（0：试验项目1：报告2：评定）'})
        fieldList.push({type:'int',value:'tiisdel',text:'删除状态'})
        fieldList.push({type:'string',value:'tijishuzhibiao',text:'tijishuzhibiao'})
        fieldList.push({type:'string',value:'tizhongyaozhibiao',text:'tizhongyaozhibiao'})
        fieldList.push({type:'string',value:'tizhongyaozhibiaoname',text:'tizhongyaozhibiaoname'})
        fieldList.push({type:'string',value:'tibaogaotaizhangname',text:'tibaogaotaizhangname'})
        fieldList.push({type:'string',value:'tibaogaotaizhangno',text:'tibaogaotaizhangno'})
        fieldList.push({type:'int',value:'plusmark',text:'plusmark'})
        fieldList.push({type:'string',value:'tip',text:'tip'})
        fieldList.push({type:'string',value:'tipandingyiju',text:'tipandingyiju'})
        fieldList.push({type:'string',value:'tishiyanyiju',text:'tishiyanyiju'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>