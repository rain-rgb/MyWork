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
      <a-button type="primary" icon="download" @click="handleExportXls('中铁出厂子表')">导出</a-button>
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

    <ztwzchumaterial-modal ref="modalForm" @ok="modalFormOk"></ztwzchumaterial-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ZtwzchumaterialModal from './modules/ZtwzchumaterialModal'

  export default {
    name: 'ZtwzchumaterialList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ZtwzchumaterialModal
    },
    data () {
      return {
        description: '中铁出厂子表管理页面',
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
            title:'组织机构',
            align:"center",
            dataIndex: 'orgid'
          },
          {
            title:'主表主键',
            align:"center",
            dataIndex: 'orderid'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title:'版本号',
            align:"center",
            dataIndex: 'version'
          },
          {
            title:'材料主键',
            align:"center",
            dataIndex: 'materialid'
          },
          {
            title:'材料编码',
            align:"center",
            dataIndex: 'materialcode'
          },
          {
            title:'材料名称',
            align:"center",
            dataIndex: 'materialname'
          },
          {
            title:'规格型号',
            align:"center",
            dataIndex: 'materialmodel'
          },
          {
            title:'STRING	主单位',
            align:"center",
            dataIndex: 'materialunit'
          },
          {
            title:'辅单位',
            align:"center",
            dataIndex: 'auxiliaryunit'
          },
          {
            title:'材料类别id',
            align:"center",
            dataIndex: 'classid'
          },
          {
            title:'材料类别主键链',
            align:"center",
            dataIndex: 'classfullid'
          },
          {
            title:'第三方组织机构',
            align:"center",
            dataIndex: 'oriorgid'
          },
          {
            title:'条码',
            align:"center",
            dataIndex: 'itembarcode'
          },
          {
            title:'确认数量（核算数量）',
            align:"center",
            dataIndex: 'netquantity'
          },
          {
            title:'转化率',
            align:"center",
            dataIndex: 'conversionrate'
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
            title:'净重主单位',
            align:"center",
            dataIndex: 'mainnetquantity'
          },
          {
            title:'上传前id',
            align:"center",
            dataIndex: 'oriitemid'
          },
          {
            title:'上传之前的磅单主键',
            align:"center",
            dataIndex: 'oriorderid'
          },
          {
            title:'添加时间',
            align:"center",
            dataIndex: 'createdat'
          },
          {
            title:'删除状态',
            align:"center",
            dataIndex: 'isremoved'
          },
          {
            title:'添加人',
            align:"center",
            dataIndex: 'creatorid'
          },
          {
            title:'添加人',
            align:"center",
            dataIndex: 'creatorname'
          },
          {
            title:'修改时间',
            align:"center",
            dataIndex: 'updatedat'
          },
          {
            title:'修改人',
            align:"center",
            dataIndex: 'modifierid'
          },
          {
            title:'修改人',
            align:"center",
            dataIndex: 'modifiername'
          },
          {
            title:'业务类型  1代表发料（10） 内调（20） 外调（30） 报废 （40） 发退（-11） 内退（-21） 外退（-31）',
            align:"center",
            dataIndex: 'servicetype'
          },
          {
            title:'操作类型默认值  1冲红（-1）2补录4正常',
            align:"center",
            dataIndex: 'ordertype'
          },
          {
            title:'上传状态',
            align:"center",
            dataIndex: 'externaluploadstate'
          },
          {
            title:'冲红材料的原始材料ID',
            align:"center",
            dataIndex: 'oriitemredid'
          },
          {
            title:'类别名称',
            align:"center",
            dataIndex: 'classname'
          },
          {
            title:'类别编码',
            align:"center",
            dataIndex: 'classcode'
          },
          {
            title:'类别全称',
            align:"center",
            dataIndex: 'classfullname'
          },
          {
            title:'第三方材料ID',
            align:"center",
            dataIndex: 'orimaterialid'
          },
          {
            title:'第三方材料类别ID',
            align:"center",
            dataIndex: 'oriclassid'
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
          list: "/ztwzchumaterial/ztwzchumaterial/list",
          delete: "/ztwzchumaterial/ztwzchumaterial/delete",
          deleteBatch: "/ztwzchumaterial/ztwzchumaterial/deleteBatch",
          exportXlsUrl: "/ztwzchumaterial/ztwzchumaterial/exportXls",
          importExcelUrl: "ztwzchumaterial/ztwzchumaterial/importExcel",
          
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
        fieldList.push({type:'string',value:'orgid',text:'组织机构',dictCode:''})
        fieldList.push({type:'string',value:'orderid',text:'主表主键',dictCode:''})
        fieldList.push({type:'string',value:'remark',text:'备注',dictCode:''})
        fieldList.push({type:'string',value:'version',text:'版本号',dictCode:''})
        fieldList.push({type:'string',value:'materialid',text:'材料主键',dictCode:''})
        fieldList.push({type:'string',value:'materialcode',text:'材料编码',dictCode:''})
        fieldList.push({type:'string',value:'materialname',text:'材料名称',dictCode:''})
        fieldList.push({type:'string',value:'materialmodel',text:'规格型号',dictCode:''})
        fieldList.push({type:'string',value:'materialunit',text:'STRING	主单位',dictCode:''})
        fieldList.push({type:'string',value:'auxiliaryunit',text:'辅单位',dictCode:''})
        fieldList.push({type:'string',value:'classid',text:'材料类别id',dictCode:''})
        fieldList.push({type:'string',value:'classfullid',text:'材料类别主键链',dictCode:''})
        fieldList.push({type:'string',value:'oriorgid',text:'第三方组织机构',dictCode:''})
        fieldList.push({type:'string',value:'itembarcode',text:'条码',dictCode:''})
        fieldList.push({type:'string',value:'netquantity',text:'确认数量（核算数量）',dictCode:''})
        fieldList.push({type:'string',value:'conversionrate',text:'转化率',dictCode:''})
        fieldList.push({type:'string',value:'deductrate',text:'扣率',dictCode:''})
        fieldList.push({type:'string',value:'deductquantity',text:'扣吨',dictCode:''})
        fieldList.push({type:'string',value:'auxiliarynetquantity',text:'辅单位净重',dictCode:''})
        fieldList.push({type:'string',value:'mainnetquantity',text:'净重主单位',dictCode:''})
        fieldList.push({type:'string',value:'oriitemid',text:'上传前id',dictCode:''})
        fieldList.push({type:'string',value:'oriorderid',text:'上传之前的磅单主键',dictCode:''})
        fieldList.push({type:'string',value:'createdat',text:'添加时间',dictCode:''})
        fieldList.push({type:'string',value:'isremoved',text:'删除状态',dictCode:''})
        fieldList.push({type:'string',value:'creatorid',text:'添加人',dictCode:''})
        fieldList.push({type:'string',value:'creatorname',text:'添加人',dictCode:''})
        fieldList.push({type:'string',value:'updatedat',text:'修改时间',dictCode:''})
        fieldList.push({type:'string',value:'modifierid',text:'修改人',dictCode:''})
        fieldList.push({type:'string',value:'modifiername',text:'修改人',dictCode:''})
        fieldList.push({type:'string',value:'servicetype',text:'业务类型  1代表发料（10） 内调（20） 外调（30） 报废 （40） 发退（-11） 内退（-21） 外退（-31）',dictCode:''})
        fieldList.push({type:'string',value:'ordertype',text:'操作类型默认值  1冲红（-1）2补录4正常',dictCode:''})
        fieldList.push({type:'string',value:'externaluploadstate',text:'上传状态',dictCode:''})
        fieldList.push({type:'string',value:'oriitemredid',text:'冲红材料的原始材料ID',dictCode:''})
        fieldList.push({type:'string',value:'classname',text:'类别名称',dictCode:''})
        fieldList.push({type:'string',value:'classcode',text:'类别编码',dictCode:''})
        fieldList.push({type:'string',value:'classfullname',text:'类别全称',dictCode:''})
        fieldList.push({type:'string',value:'orimaterialid',text:'第三方材料ID',dictCode:''})
        fieldList.push({type:'string',value:'oriclassid',text:'第三方材料类别ID',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>