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
      <a-button type="primary" icon="download" @click="handleExportXls('中铁一局现场收料数据')">导出</a-button>
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

    <ztwzjincslsjb-modal ref="modalForm" @ok="modalFormOk"></ztwzjincslsjb-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ZtwzjincslsjbModal from './modules/ZtwzjincslsjbModal'

  export default {
    name: 'ZtwzjincslsjbList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ZtwzjincslsjbModal
    },
    data () {
      return {
        description: '中铁一局现场收料数据管理页面',
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
            title:'制单人',
            align:"center",
            dataIndex: 'maker'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title:'审核人',
            align:"center",
            dataIndex: 'auditor'
          },
          {
            title:'版本号',
            align:"center",
            dataIndex: 'version'
          },
          {
            title:'端上的组织机构名称',
            align:"center",
            dataIndex: 'oriorgname'
          },
          {
            title:'组织机构',
            align:"center",
            dataIndex: 'orgid'
          },
          {
            title:'账期',
            align:"center",
            dataIndex: 'orderdate'
          },
          {
            title:'过磅日期',
            align:"center",
            dataIndex: 'recordeddate'
          },
          {
            title:'磅单编号',
            align:"center",
            dataIndex: 'ordercode'
          },
          {
            title:'单据来源',
            align:"center",
            dataIndex: 'orderorigin'
          },
          {
            title:'业务类型',
            align:"center",
            dataIndex: 'servicetype'
          },
          {
            title:'操作类型',
            align:"center",
            dataIndex: 'ordertype'
          },
          {
            title:'制单时间',
            align:"center",
            dataIndex: 'makerdate'
          },
          {
            title:'打印次数',
            align:"center",
            dataIndex: 'printtimes'
          },
          {
            title:'车牌号',
            align:"center",
            dataIndex: 'platenumber'
          },
          {
            title:'供应商ID',
            align:"center",
            dataIndex: 'supplierid'
          },
          {
            title:'供应商名称',
            align:"center",
            dataIndex: 'suppliername'
          },
          {
            title:'冲红状态',
            align:"center",
            dataIndex: 'isred'
          },
          {
            title:'审核状态',
            align:"center",
            dataIndex: 'isaudit'
          },
          {
            title:'审核日期',
            align:"center",
            dataIndex: 'auditdate'
          },
          {
            title:'第三方组织机构',
            align:"center",
            dataIndex: 'oriorgid'
          },
          {
            title:'过磅端主键',
            align:"center",
            dataIndex: 'oriorderid'
          },
          {
            title:'添加人',
            align:"center",
            dataIndex: 'creatorid'
          },
          {
            title:'添加人姓名',
            align:"center",
            dataIndex: 'creatorname'
          },
          {
            title:'添加时间',
            align:"center",
            dataIndex: 'createdat'
          },
          {
            title:'修改人',
            align:"center",
            dataIndex: 'modifierid'
          },
          {
            title:'修改人姓名',
            align:"center",
            dataIndex: 'modifiername'
          },
          {
            title:'修改时间',
            align:"center",
            dataIndex: 'updatedat'
          },
          {
            title:'删除状态',
            align:"center",
            dataIndex: 'isremoved'
          },
          {
            title:'引用主键',
            align:"center",
            dataIndex: 'orderdataid'
          },
          {
            title:'识别车牌',
            align:"center",
            dataIndex: 'discernplatenumber'
          },
          {
            title:'料仓全称',
            align:"center",
            dataIndex: 'stockbinfullname'
          },
          {
            title:'料仓名称',
            align:"center",
            dataIndex: 'stockbinname'
          },
          {
            title:'料仓主键',
            align:"center",
            dataIndex: 'stockbinid'
          },
          {
            title:'第三方料仓主键',
            align:"center",
            dataIndex: 'oristockbinid'
          },
          {
            title:'入场时间',
            align:"center",
            dataIndex: 'entertime'
          },
          {
            title:'出场时间',
            align:"center",
            dataIndex: 'exittime'
          },
          {
            title:'排序时间',
            align:"center",
            dataIndex: 'sorttime'
          },
          {
            title:'是否退料',
            align:"center",
            dataIndex: 'isreturn'
          },
          {
            title:'出场状态',
            align:"center",
            dataIndex: 'isexit'
          },
          {
            title:'是否称皮重',
            align:"center",
            dataIndex: 'istare'
          },
          {
            title:'转换率计算方式 true乘法 false 除法',
            align:"center",
            dataIndex: 'ismultiplication'
          },
          {
            title:'运单结算(true代表确认数量netQuantity 为运单数量，false则代表确认数量netQuantity 为称重数量)',
            align:"center",
            dataIndex: 'isuseorinetquantity'
          },
          {
            title:'抵扣系数',
            align:"center",
            dataIndex: 'deductionrate'
          },
          {
            title:'毛重',
            align:"center",
            dataIndex: 'roughquantity'
          },
          {
            title:'皮重',
            align:"center",
            dataIndex: 'tarequantity'
          },
          {
            title:'扣率',
            align:"center",
            dataIndex: 'deductrate'
          },
          {
            title:'扣吨',
            align:"center",
            dataIndex: 'deductquantity'
          },
          {
            title:'辅单位净重',
            align:"center",
            dataIndex: 'auxiliarynetquantity'
          },
          {
            title:'运单重量',
            align:"center",
            dataIndex: 'waybillweight'
          },
          {
            title:'单据条形码',
            align:"center",
            dataIndex: 'orderbarcode'
          },
          {
            title:'区分一车多料、直进直出、普通进料标识',
            align:"center",
            dataIndex: 'weighttype'
          },
          {
            title:'复称状态',
            align:"center",
            dataIndex: 'isaffirm'
          },
          {
            title:'供应商第三方主键',
            align:"center",
            dataIndex: 'orisupplierid'
          },
          {
            title:'组织机构名称',
            align:"center",
            dataIndex: 'orgname'
          },
          {
            title:'项目部组织ID',
            align:"center",
            dataIndex: 'realorgid'
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
          list: "/ztwzjincslsjb/ztwzjincslsjb/list",
          delete: "/ztwzjincslsjb/ztwzjincslsjb/delete",
          deleteBatch: "/ztwzjincslsjb/ztwzjincslsjb/deleteBatch",
          exportXlsUrl: "/ztwzjincslsjb/ztwzjincslsjb/exportXls",
          importExcelUrl: "ztwzjincslsjb/ztwzjincslsjb/importExcel",
          
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
        fieldList.push({type:'string',value:'maker',text:'制单人',dictCode:''})
        fieldList.push({type:'string',value:'remark',text:'备注',dictCode:''})
        fieldList.push({type:'string',value:'auditor',text:'审核人',dictCode:''})
        fieldList.push({type:'string',value:'version',text:'版本号',dictCode:''})
        fieldList.push({type:'string',value:'oriorgname',text:'端上的组织机构名称',dictCode:''})
        fieldList.push({type:'string',value:'orgid',text:'组织机构',dictCode:''})
        fieldList.push({type:'string',value:'orderdate',text:'账期',dictCode:''})
        fieldList.push({type:'string',value:'recordeddate',text:'过磅日期',dictCode:''})
        fieldList.push({type:'string',value:'ordercode',text:'磅单编号',dictCode:''})
        fieldList.push({type:'int',value:'orderorigin',text:'单据来源',dictCode:''})
        fieldList.push({type:'int',value:'servicetype',text:'业务类型',dictCode:''})
        fieldList.push({type:'int',value:'ordertype',text:'操作类型',dictCode:''})
        fieldList.push({type:'string',value:'makerdate',text:'制单时间',dictCode:''})
        fieldList.push({type:'int',value:'printtimes',text:'打印次数',dictCode:''})
        fieldList.push({type:'string',value:'platenumber',text:'车牌号',dictCode:''})
        fieldList.push({type:'int',value:'supplierid',text:'供应商ID',dictCode:''})
        fieldList.push({type:'string',value:'suppliername',text:'供应商名称',dictCode:''})
        fieldList.push({type:'string',value:'isred',text:'冲红状态',dictCode:''})
        fieldList.push({type:'string',value:'isaudit',text:'审核状态',dictCode:''})
        fieldList.push({type:'string',value:'auditdate',text:'审核日期',dictCode:''})
        fieldList.push({type:'string',value:'oriorgid',text:'第三方组织机构',dictCode:''})
        fieldList.push({type:'string',value:'oriorderid',text:'过磅端主键',dictCode:''})
        fieldList.push({type:'string',value:'creatorid',text:'添加人',dictCode:''})
        fieldList.push({type:'string',value:'creatorname',text:'添加人姓名',dictCode:''})
        fieldList.push({type:'string',value:'createdat',text:'添加时间',dictCode:''})
        fieldList.push({type:'string',value:'modifierid',text:'修改人',dictCode:''})
        fieldList.push({type:'string',value:'modifiername',text:'修改人姓名',dictCode:''})
        fieldList.push({type:'string',value:'updatedat',text:'修改时间',dictCode:''})
        fieldList.push({type:'string',value:'isremoved',text:'删除状态',dictCode:''})
        fieldList.push({type:'string',value:'orderdataid',text:'引用主键',dictCode:''})
        fieldList.push({type:'string',value:'discernplatenumber',text:'识别车牌',dictCode:''})
        fieldList.push({type:'string',value:'stockbinfullname',text:'料仓全称',dictCode:''})
        fieldList.push({type:'string',value:'stockbinname',text:'料仓名称',dictCode:''})
        fieldList.push({type:'string',value:'stockbinid',text:'料仓主键',dictCode:''})
        fieldList.push({type:'string',value:'oristockbinid',text:'第三方料仓主键',dictCode:''})
        fieldList.push({type:'string',value:'entertime',text:'入场时间',dictCode:''})
        fieldList.push({type:'string',value:'exittime',text:'出场时间',dictCode:''})
        fieldList.push({type:'string',value:'sorttime',text:'排序时间',dictCode:''})
        fieldList.push({type:'string',value:'isreturn',text:'是否退料',dictCode:''})
        fieldList.push({type:'string',value:'isexit',text:'出场状态',dictCode:''})
        fieldList.push({type:'string',value:'istare',text:'是否称皮重',dictCode:''})
        fieldList.push({type:'string',value:'ismultiplication',text:'转换率计算方式 true乘法 false 除法',dictCode:''})
        fieldList.push({type:'string',value:'isuseorinetquantity',text:'运单结算(true代表确认数量netQuantity 为运单数量，false则代表确认数量netQuantity 为称重数量)',dictCode:''})
        fieldList.push({type:'string',value:'deductionrate',text:'抵扣系数',dictCode:''})
        fieldList.push({type:'string',value:'roughquantity',text:'毛重',dictCode:''})
        fieldList.push({type:'string',value:'tarequantity',text:'皮重',dictCode:''})
        fieldList.push({type:'string',value:'deductrate',text:'扣率',dictCode:''})
        fieldList.push({type:'string',value:'deductquantity',text:'扣吨',dictCode:''})
        fieldList.push({type:'string',value:'auxiliarynetquantity',text:'辅单位净重',dictCode:''})
        fieldList.push({type:'string',value:'waybillweight',text:'运单重量',dictCode:''})
        fieldList.push({type:'string',value:'orderbarcode',text:'单据条形码',dictCode:''})
        fieldList.push({type:'string',value:'weighttype',text:'区分一车多料、直进直出、普通进料标识',dictCode:''})
        fieldList.push({type:'string',value:'isaffirm',text:'复称状态',dictCode:''})
        fieldList.push({type:'string',value:'orisupplierid',text:'供应商第三方主键',dictCode:''})
        fieldList.push({type:'string',value:'orgname',text:'组织机构名称',dictCode:''})
        fieldList.push({type:'string',value:'realorgid',text:'项目部组织ID',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>