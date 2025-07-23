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
      <a-button type="primary" icon="download" @click="handleExportXls('sy_dps_sy_sample_view2')">导出</a-button>
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

    <sy-dps-sy-sample-view2-modal ref="modalForm" @ok="modalFormOk"></sy-dps-sy-sample-view2-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyDpsSySampleView2Modal from './modules/SyDpsSySampleView2Modal'

  export default {
    name: 'SyDpsSySampleView2List',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyDpsSySampleView2Modal
    },
    data () {
      return {
        description: 'sy_dps_sy_sample_view2管理页面',
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
            title:'样品编号(根据基础配置的编号规则生成)',
            align:"center",
            dataIndex: 'sampleno'
          },
          {
            title:'样品名称',
            align:"center",
            dataIndex: 'samplename'
          },
          {
            title:'样品描述',
            align:"center",
            dataIndex: 'sampledescribe'
          },
          {
            title:'工程部位/用途',
            align:"center",
            dataIndex: 'samplegcbw'
          },
          {
            title:'取样日期（yyyy-MM-dd）',
            align:"center",
            dataIndex: 'sampledate'
          },
          {
            title:'取样地点/取样位置',
            align:"center",
            dataIndex: 'samplequyangdidian'
          },
          {
            title:'样品状态（0：待检1：在检2：已检）',
            align:"center",
            dataIndex: 'samplestate'
          },
          {
            title:'真实名字',
            align:"center",
            dataIndex: 'realname'
          },
          {
            title:'取样人',
            align:"center",
            dataIndex: 'samplequyangren'
          },
          {
            title:'创建人（系统登录用户）',
            align:"center",
            dataIndex: 'samplecreateperson'
          },
          {
            title:'创建日期（yyyy-MM-dd）',
            align:"center",
            dataIndex: 'samplecreatedate'
          },
          {
            title:'外键，组织机构编码（T_S_depart）',
            align:"center",
            dataIndex: 'orgcode'
          },
          {
            title:'外键，试验项目类型（dps_jc_testItemType表）',
            align:"center",
            dataIndex: 'titcode'
          },
          {
            title:'强度等级，使用数据字典（水泥样品用到）',
            align:"center",
            dataIndex: 'sampleqiangdudengji'
          },
          {
            title:'龄期 ',
            align:"center",
            dataIndex: 'samplelingqi'
          },
          {
            title:'报告日期（来源编辑报告时填写的时间）',
            align:"center",
            dataIndex: 'reportcreatedate'
          },
          {
            title:'报告编号(根据基础设置的规则生成)',
            align:"center",
            dataIndex: 'reportno'
          },
          {
            title:'报告编辑人',
            align:"center",
            dataIndex: 'reporteditperson'
          },
          {
            title:'真实名字',
            align:"center",
            dataIndex: 'realname2'
          },
          {
            title:'试验项目名称',
            align:"center",
            dataIndex: 'titname'
          },
          {
            title:'外键（dps_jc_testItemType）试验项目类型',
            align:"center",
            dataIndex: 'tittype'
          },
          {
            title:'testname',
            align:"center",
            dataIndex: 'testname'
          },
          {
            title:'见证取样 0：普通 1：见证取样 默认给0',
            align:"center",
            dataIndex: 'plusmark'
          },
          {
            title:'样品编号(根据基础配置的编号规则生成)',
            align:"center",
            dataIndex: 'samplenonew'
          },
          {
            title:'自定义报告编号（雷榕项目）',
            align:"center",
            dataIndex: 'reportnonew'
          },
          {
            title:'自定义记录编号（雷榕项目）',
            align:"center",
            dataIndex: 'tablenumbernew'
          },
          {
            title:'审批状态 0：未提交 1：未审核 2：审核通过  3：审核退回',
            align:"center",
            dataIndex: 'shenpizhuangtai'
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
          list: "/sylxdps/syDpsSySampleView2/list",
          delete: "/sylxdps/syDpsSySampleView2/delete",
          deleteBatch: "/sylxdps/syDpsSySampleView2/deleteBatch",
          exportXlsUrl: "/sylxdps/syDpsSySampleView2/exportXls",
          importExcelUrl: "sylxdps/syDpsSySampleView2/importExcel",
          
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
        fieldList.push({type:'string',value:'sampleno',text:'样品编号(根据基础配置的编号规则生成)'})
        fieldList.push({type:'string',value:'samplename',text:'样品名称'})
        fieldList.push({type:'string',value:'sampledescribe',text:'样品描述'})
        fieldList.push({type:'string',value:'samplegcbw',text:'工程部位/用途'})
        fieldList.push({type:'string',value:'sampledate',text:'取样日期（yyyy-MM-dd）'})
        fieldList.push({type:'string',value:'samplequyangdidian',text:'取样地点/取样位置'})
        fieldList.push({type:'int',value:'samplestate',text:'样品状态（0：待检1：在检2：已检）'})
        fieldList.push({type:'string',value:'realname',text:'真实名字'})
        fieldList.push({type:'string',value:'samplequyangren',text:'取样人'})
        fieldList.push({type:'string',value:'samplecreateperson',text:'创建人（系统登录用户）'})
        fieldList.push({type:'string',value:'samplecreatedate',text:'创建日期（yyyy-MM-dd）'})
        fieldList.push({type:'string',value:'orgcode',text:'外键，组织机构编码（T_S_depart）'})
        fieldList.push({type:'string',value:'titcode',text:'外键，试验项目类型（dps_jc_testItemType表）'})
        fieldList.push({type:'string',value:'sampleqiangdudengji',text:'强度等级，使用数据字典（水泥样品用到）'})
        fieldList.push({type:'string',value:'samplelingqi',text:'龄期 '})
        fieldList.push({type:'string',value:'reportcreatedate',text:'报告日期（来源编辑报告时填写的时间）'})
        fieldList.push({type:'string',value:'reportno',text:'报告编号(根据基础设置的规则生成)'})
        fieldList.push({type:'string',value:'reporteditperson',text:'报告编辑人'})
        fieldList.push({type:'string',value:'realname2',text:'真实名字'})
        fieldList.push({type:'string',value:'titname',text:'试验项目名称'})
        fieldList.push({type:'string',value:'tittype',text:'外键（dps_jc_testItemType）试验项目类型'})
        fieldList.push({type:'string',value:'testname',text:'testname'})
        fieldList.push({type:'int',value:'plusmark',text:'见证取样 0：普通 1：见证取样 默认给0'})
        fieldList.push({type:'string',value:'samplenonew',text:'样品编号(根据基础配置的编号规则生成)'})
        fieldList.push({type:'string',value:'reportnonew',text:'自定义报告编号（雷榕项目）'})
        fieldList.push({type:'string',value:'tablenumbernew',text:'自定义记录编号（雷榕项目）'})
        fieldList.push({type:'int',value:'shenpizhuangtai',text:'审批状态 0：未提交 1：未审核 2：审核通过  3：审核退回'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>