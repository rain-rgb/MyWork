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
      <a-button type="primary" icon="download" @click="handleExportXls('sy_dps_sy_signature')">导出</a-button>
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

    <sy-dps-sy-signature-modal ref="modalForm" @ok="modalFormOk"></sy-dps-sy-signature-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyDpsSySignatureModal from './modules/SyDpsSySignatureModal'

  export default {
    name: 'SyDpsSySignatureList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyDpsSySignatureModal
    },
    data () {
      return {
        description: 'sy_dps_sy_signature管理页面',
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
            title:'外键，样品编号，sy_dps_sy_sample',
            align:"center",
            dataIndex: 'sampleid'
          },
          {
            title:'流程名称',
            align:"center",
            dataIndex: 'liuchengmingcheng'
          },
          {
            title:'签章类型 1：施工单位 2：监理单位',
            align:"center",
            dataIndex: 'qianzhangleixing'
          },
          {
            title:'流程类型 1：记录表 2：报告表 3:报验单 4：审批表 5：评定表',
            align:"center",
            dataIndex: 'liuchengleixing'
          },
          {
            title:'记录表签章状态   默认-1：未启动签章',
            align:"center",
            dataIndex: 'jilubiaoqianzhangzhuangtai'
          },
          {
            title:'报告签章状态  默认-1：未启动签章',
            align:"center",
            dataIndex: 'baogaoqianzhangzhuangtai'
          },
          {
            title:'报验单签章状态  默认-1：未启动签章 0:无报验单        1/3',
            align:"center",
            dataIndex: 'baoyandanqianzhangzhuangtai'
          },
          {
            title:'审批表签章状态  默认-1：未启动签章 0：无审批表',
            align:"center",
            dataIndex: 'shenpibiaoqianzhangzhuangtai'
          },
          {
            title:'外键，用户id，来源t_s_base_user表id字段',
            align:"center",
            dataIndex: 'userid'
          },
          {
            title:'自定义样品编号（雷榕项目）',
            align:"center",
            dataIndex: 'sampleno'
          },
          {
            title:'自定义报告编号（雷榕项目）',
            align:"center",
            dataIndex: 'reportno'
          },
          {
            title:'样品名称',
            align:"center",
            dataIndex: 'samplename'
          },
          {
            title:'工程部位/用途',
            align:"center",
            dataIndex: 'samplegcbw'
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
            title:'审批时间,系统当前时间（YYYY-MM-dd HH:mm:ss）',
            align:"center",
            dataIndex: 'shenpishijian'
          },
          {
            title:'外键，签章流程id，dps_jc_qianzhangliucheng',
            align:"center",
            dataIndex: 'qianzhangliuchengid'
          },
          {
            title:'取样人',
            align:"center",
            dataIndex: 'samplequyangren'
          },
          {
            title:'签章关键字，来源数据字典 shtoone_qzgjz',
            align:"center",
            dataIndex: 'qianzhangguanjianzi'
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
          list: "/syDpsSySignature/syDpsSySignature/list",
          delete: "/syDpsSySignature/syDpsSySignature/delete",
          deleteBatch: "/syDpsSySignature/syDpsSySignature/deleteBatch",
          exportXlsUrl: "/syDpsSySignature/syDpsSySignature/exportXls",
          importExcelUrl: "syDpsSySignature/syDpsSySignature/importExcel",
          
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
        fieldList.push({type:'string',value:'sampleid',text:'外键，样品编号，sy_dps_sy_sample'})
        fieldList.push({type:'string',value:'liuchengmingcheng',text:'流程名称'})
        fieldList.push({type:'int',value:'qianzhangleixing',text:'签章类型 1：施工单位 2：监理单位'})
        fieldList.push({type:'int',value:'liuchengleixing',text:'流程类型 1：记录表 2：报告表 3:报验单 4：审批表 5：评定表'})
        fieldList.push({type:'string',value:'jilubiaoqianzhangzhuangtai',text:'记录表签章状态   默认-1：未启动签章'})
        fieldList.push({type:'string',value:'baogaoqianzhangzhuangtai',text:'报告签章状态  默认-1：未启动签章'})
        fieldList.push({type:'string',value:'baoyandanqianzhangzhuangtai',text:'报验单签章状态  默认-1：未启动签章 0:无报验单        1/3'})
        fieldList.push({type:'string',value:'shenpibiaoqianzhangzhuangtai',text:'审批表签章状态  默认-1：未启动签章 0：无审批表'})
        fieldList.push({type:'string',value:'userid',text:'外键，用户id，来源t_s_base_user表id字段'})
        fieldList.push({type:'string',value:'sampleno',text:'自定义样品编号（雷榕项目）'})
        fieldList.push({type:'string',value:'reportno',text:'自定义报告编号（雷榕项目）'})
        fieldList.push({type:'string',value:'samplename',text:'样品名称'})
        fieldList.push({type:'string',value:'samplegcbw',text:'工程部位/用途'})
        fieldList.push({type:'string',value:'orgcode',text:'外键，组织机构编码（T_S_depart）'})
        fieldList.push({type:'string',value:'titcode',text:'外键，试验项目类型（dps_jc_testItemType表）'})
        fieldList.push({type:'string',value:'shenpishijian',text:'审批时间,系统当前时间（YYYY-MM-dd HH:mm:ss）'})
        fieldList.push({type:'string',value:'qianzhangliuchengid',text:'外键，签章流程id，dps_jc_qianzhangliucheng'})
        fieldList.push({type:'string',value:'samplequyangren',text:'取样人'})
        fieldList.push({type:'string',value:'qianzhangguanjianzi',text:'签章关键字，来源数据字典 shtoone_qzgjz'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>