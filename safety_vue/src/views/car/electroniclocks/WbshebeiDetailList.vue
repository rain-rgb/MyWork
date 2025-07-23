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
      <a-button type="primary" icon="download" @click="handleExportXls('电子锁详情数据表')">导出</a-button>
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
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >

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

    <wbshebei-detail-modal ref="modalForm" @ok="modalFormOk"></wbshebei-detail-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WbshebeiDetailModal from './modules/WbshebeiDetailModal'

  export default {
    name: 'WbshebeiDetailList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WbshebeiDetailModal
    },
    data () {
      return {
        description: '电子锁详情数据表管理页面',
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
            title:'设备编号',
            align:"center",
            dataIndex: 'sbbh'
          },
          {
            title:'到达时间',
            align:"center",
            dataIndex: 'ddtime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'出发时间',
            align:"center",
            dataIndex: 'cftime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'单号',
            align:"center",
            dataIndex: 'danhao'
          },
          {
            title:'车牌号',
            align:"center",
            dataIndex: 'cph'
          },
          {
            title:'是否到达0为未到达，1为到达',
            align:"center",
            dataIndex: 'zhuangtai'
          },
          {
            title:'是否合格',
            align:"center",
            dataIndex: 'sfhg'
          },
          {
            title:'0-安徽环宇公路沥青材料有限公司;1-上海海太工程科技有限公司;2-湖南路安达沥青技术有限公司',
            align:"center",
            dataIndex: 'ghdw'
          },
          {
            title:'材料',
            align:"center",
            dataIndex: 'cailiao'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'guige'
          },
          {
            title:'发车纬度',
            align:"center",
            dataIndex: 'fclat'
          },
          {
            title:'发车经度',
            align:"center",
            dataIndex: 'fclng'
          },
          {
            title:'到达纬度',
            align:"center",
            dataIndex: 'ddlat'
          },
          {
            title:'到达经度',
            align:"center",
            dataIndex: 'ddlng'
          },
          {
            title:'负责人',
            align:"center",
            dataIndex: 'fzr'
          },
          {
            title:'0为报警 1为不报警',
            align:"center",
            dataIndex: 'sfbj'
          },
          {
            title:'0为未发送1为已发送 发送到达信息',
            align:"center",
            dataIndex: 'fsxx'
          },
          {
            title:'发车数量',
            align:"center",
            dataIndex: 'jcsl'
          },
          {
            title:'进场过磅数量',
            align:"center",
            dataIndex: 'jcgbl'
          },
          {
            title:'出场过磅数量',
            align:"center",
            dataIndex: 'ccgbl'
          },
          {
            title:'当前库存',
            align:"center",
            dataIndex: 'dqkc'
          },
          {
            title:'实际消耗数量',
            align:"center",
            dataIndex: 'sjxhsl'
          },
          {
            title:'理论消耗数量由理论配合比和工程任务单计算得出',
            align:"center",
            dataIndex: 'llxhsl'
          },
          {
            title:'理论库存 = 实际进场 - 理论消耗',
            align:"center",
            dataIndex: 'llkc'
          },
          {
            title:'理论消耗',
            align:"center",
            dataIndex: 'llxh'
          },
          {
            title:'理论配合比',
            align:"center",
            dataIndex: 'llphb'
          },
          {
            title:'0为已拿到地磅数据1为未拿到',
            align:"center",
            dataIndex: 'sfcssj'
          },
          {
            title:'目的地',
            align:"center",
            dataIndex: 'mdd'
          },
          {
            title:'照片地址',
            align:"center",
            dataIndex: 'imgfile'
          },
          {
            title:'铅封号',
            align:"center",
            dataIndex: 'qianfenghao'
          },
          {
            title:'所属标段',
            align:"center",
            dataIndex: 'userdepartid'
          },
          {
            title:'0 未到达  1已到达 2已解锁',
            align:"center",
            dataIndex: 'isjiesuo'
          },
          {
            title:'解锁时间',
            align:"center",
            dataIndex: 'timestatime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'解锁人',
            align:"center",
            dataIndex: 'name'
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
          list: "/wbshebeidetail/wbshebeiDetail/list",
          delete: "/wbshebeidetail/wbshebeiDetail/delete",
          deleteBatch: "/wbshebeidetail/wbshebeiDetail/deleteBatch",
          exportXlsUrl: "/wbshebeidetail/wbshebeiDetail/exportXls",
          importExcelUrl: "wbshebeidetail/wbshebeiDetail/importExcel",

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
        fieldList.push({type:'string',value:'sbbh',text:'设备编号',dictCode:''})
        fieldList.push({type:'date',value:'ddtime',text:'到达时间'})
        fieldList.push({type:'date',value:'cftime',text:'出发时间'})
        fieldList.push({type:'string',value:'danhao',text:'单号',dictCode:''})
        fieldList.push({type:'string',value:'cph',text:'车牌号',dictCode:''})
        fieldList.push({type:'int',value:'zhuangtai',text:'是否到达0为未到达，1为到达',dictCode:''})
        fieldList.push({type:'string',value:'sfhg',text:'是否合格',dictCode:''})
        fieldList.push({type:'string',value:'ghdw',text:'0-安徽环宇公路沥青材料有限公司;1-上海海太工程科技有限公司;2-湖南路安达沥青技术有限公司',dictCode:''})
        fieldList.push({type:'string',value:'cailiao',text:'材料',dictCode:''})
        fieldList.push({type:'string',value:'guige',text:'规格',dictCode:''})
        fieldList.push({type:'string',value:'fclat',text:'发车纬度',dictCode:''})
        fieldList.push({type:'string',value:'fclng',text:'发车经度',dictCode:''})
        fieldList.push({type:'string',value:'ddlat',text:'到达纬度',dictCode:''})
        fieldList.push({type:'string',value:'ddlng',text:'到达经度',dictCode:''})
        fieldList.push({type:'string',value:'fzr',text:'负责人',dictCode:''})
        fieldList.push({type:'string',value:'sfbj',text:'0为报警 1为不报警',dictCode:''})
        fieldList.push({type:'string',value:'fsxx',text:'0为未发送1为已发送 发送到达信息',dictCode:''})
        fieldList.push({type:'string',value:'jcsl',text:'发车数量',dictCode:''})
        fieldList.push({type:'string',value:'jcgbl',text:'进场过磅数量',dictCode:''})
        fieldList.push({type:'string',value:'ccgbl',text:'出场过磅数量',dictCode:''})
        fieldList.push({type:'string',value:'dqkc',text:'当前库存',dictCode:''})
        fieldList.push({type:'string',value:'sjxhsl',text:'实际消耗数量',dictCode:''})
        fieldList.push({type:'string',value:'llxhsl',text:'理论消耗数量由理论配合比和工程任务单计算得出',dictCode:''})
        fieldList.push({type:'string',value:'llkc',text:'理论库存 = 实际进场 - 理论消耗',dictCode:''})
        fieldList.push({type:'string',value:'llxh',text:'理论消耗',dictCode:''})
        fieldList.push({type:'string',value:'llphb',text:'理论配合比',dictCode:''})
        fieldList.push({type:'string',value:'sfcssj',text:'0为已拿到地磅数据1为未拿到',dictCode:''})
        fieldList.push({type:'string',value:'mdd',text:'目的地',dictCode:''})
        fieldList.push({type:'string',value:'imgfile',text:'照片地址',dictCode:''})
        fieldList.push({type:'string',value:'qianfenghao',text:'铅封号',dictCode:''})
        fieldList.push({type:'string',value:'userdepartid',text:'所属标段',dictCode:''})
        fieldList.push({type:'int',value:'isjiesuo',text:'0 未到达  1已到达 2已解锁',dictCode:''})
        fieldList.push({type:'date',value:'timestatime',text:'解锁时间'})
        fieldList.push({type:'string',value:'name',text:'解锁人',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>