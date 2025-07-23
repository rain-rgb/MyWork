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
      <a-button type="primary" icon="download" @click="handleExportXls('dps_sy_qianzhangliucheng')">导出</a-button>
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

    <dps-sy-qianzhangliucheng-modal ref="modalForm" @ok="modalFormOk"></dps-sy-qianzhangliucheng-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DpsSyQianzhangliuchengModal from './modules/DpsSyQianzhangliuchengModal'

  export default {
    name: 'DpsSyQianzhangliuchengList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DpsSyQianzhangliuchengModal
    },
    data () {
      return {
        description: 'dps_sy_qianzhangliucheng管理页面',
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
            title:'外键，签章流程id，dps_jc_qianzhangliucheng',
            align:"center",
            dataIndex: 'qianzhangliuchengid'
          },
          {
            title:'外键，签章id，dps_jc_CAUsbKey',
            align:"center",
            dataIndex: 'caid'
          },
          {
            title:'签章人，当前登录用户名',
            align:"center",
            dataIndex: 'qianzhangren'
          },
          {
            title:'签章时间',
            align:"center",
            dataIndex: 'qianzhangshijian'
          },
          {
            title:'签章状态 0：未签章 1：已签章 2：已退回',
            align:"center",
            dataIndex: 'qianzhangzhuangtai'
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
            title:'流程状态序号（值等于 流程类型值+填写值）',
            align:"center",
            dataIndex: 'liuchengzhuangtaixuhao'
          },
          {
            title:'流程名称',
            align:"center",
            dataIndex: 'liuchengmingcheng'
          },
          {
            title:'签章关键字，来源数据字典 shtoone_qzgjz',
            align:"center",
            dataIndex: 'qianzhangguanjianzi'
          },
          {
            title:'当前流程需要签章页面数量',
            align:"center",
            dataIndex: 'qianzhangyemianshuliang'
          },
          {
            title:'evaluateid',
            align:"center",
            dataIndex: 'evaluateid'
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
          list: "/sydpsqzlc/dpsSyQianzhangliucheng/list",
          delete: "/sydpsqzlc/dpsSyQianzhangliucheng/delete",
          deleteBatch: "/sydpsqzlc/dpsSyQianzhangliucheng/deleteBatch",
          exportXlsUrl: "/sydpsqzlc/dpsSyQianzhangliucheng/exportXls",
          importExcelUrl: "sydpsqzlc/dpsSyQianzhangliucheng/importExcel",
          
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
        fieldList.push({type:'string',value:'qianzhangliuchengid',text:'外键，签章流程id，dps_jc_qianzhangliucheng'})
        fieldList.push({type:'string',value:'caid',text:'外键，签章id，dps_jc_CAUsbKey'})
        fieldList.push({type:'string',value:'qianzhangren',text:'签章人，当前登录用户名'})
        fieldList.push({type:'string',value:'qianzhangshijian',text:'签章时间'})
        fieldList.push({type:'int',value:'qianzhangzhuangtai',text:'签章状态 0：未签章 1：已签章 2：已退回'})
        fieldList.push({type:'int',value:'qianzhangleixing',text:'签章类型 1：施工单位 2：监理单位'})
        fieldList.push({type:'int',value:'liuchengleixing',text:'流程类型 1：记录表 2：报告表 3:报验单 4：审批表 5：评定表'})
        fieldList.push({type:'int',value:'liuchengzhuangtaixuhao',text:'流程状态序号（值等于 流程类型值+填写值）'})
        fieldList.push({type:'string',value:'liuchengmingcheng',text:'流程名称'})
        fieldList.push({type:'string',value:'qianzhangguanjianzi',text:'签章关键字，来源数据字典 shtoone_qzgjz'})
        fieldList.push({type:'int',value:'qianzhangyemianshuliang',text:'当前流程需要签章页面数量'})
        fieldList.push({type:'string',value:'evaluateid',text:'evaluateid'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>