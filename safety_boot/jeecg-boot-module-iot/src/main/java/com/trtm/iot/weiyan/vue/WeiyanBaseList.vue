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
      <a-button type="primary" icon="download" @click="handleExportXls('围岩量测主表')">导出</a-button>
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

    <weiyan-base-modal ref="modalForm" @ok="modalFormOk"></weiyan-base-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WeiyanBaseModal from './modules/WeiyanBaseModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'

  export default {
    name: 'WeiyanBaseList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WeiyanBaseModal,
      JSuperQuery,
    },
    data () {
      return {
        description: '围岩量测主表管理页面',
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
            title:'唯一ID',
            align:"center",
            dataIndex: 'batchNo'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'shebeiNo'
          },
          {
            title:'施工部位',
            align:"center",
            dataIndex: 'jobLocation'
          },
          {
            title:'切面名称',
            align:"center",
            dataIndex: 'sectionName'
          },
          {
            title:'切面里程',
            align:"center",
            dataIndex: 'sectionLength'
          },
          {
            title:'切面宽度',
            align:"center",
            dataIndex: 'sectionWidth'
          },
          {
            title:'开挖方式',
            align:"center",
            dataIndex: 'excavationType'
          },
          {
            title:'围岩级别',
            align:"center",
            dataIndex: 'wallRockLevel'
          },
          {
            title:'测点',
            align:"center",
            dataIndex: 'meauringPoint'
          },
          {
            title:'测量值（单位m）',
            align:"center",
            dataIndex: 'meauringNumber'
          },
          {
            title:'单次变形量（单位mm）',
            align:"center",
            dataIndex: 'onceDeformation'
          },
          {
            title:'间隔时间  （单位天）',
            align:"center",
            dataIndex: 'intervalTime'
          },
          {
            title:'变形速率',
            align:"center",
            dataIndex: 'deformationRate'
          },
          {
            title:'单次是否超标 (0:未超标，1:超标)',
            align:"center",
            dataIndex: 'onceStatus'
          },
          {
            title:'累计是否超标  (0:未超标，1:超标)',
            align:"center",
            dataIndex: 'accumulatedStatus'
          },
          {
            title:'累计变形量单位（mm）',
            align:"center",
            dataIndex: 'accumulatedDeformation'
          },
          {
            title:'是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计      40为数据异常',
            align:"center",
            dataIndex: 'alertstate'
          },
          {
            title:'测量时间',
            align:"center",
            dataIndex: 'birthTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'数据上传时间',
            align:"center",
            dataIndex: 'uploadTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'isUse'
          },
          {
            title:'报警级别(0,代表合格，1代表黄色报警，21代表红色报警)',
            align:"center",
            dataIndex: 'callLevel'
          },
          {
            title:'检测类型（0:速率，1累计）',
            align:"center",
            dataIndex: 'superviseType'
          },
          {
            title:'callSum',
            align:"center",
            dataIndex: 'callSum'
          },
          {
            title:'间隔时间(距离初次测试时间)',
            align:"center",
            dataIndex: 'intervalFirstTime'
          },
          {
            title:'测量状态（0：正常 ,1：重新布设）',
            align:"center",
            dataIndex: 'measuringStatus'
          },
          {
            title:'超标问题中文描述',
            align:"center",
            dataIndex: 'problemRecord'
          },
          {
            title:'临建再统计后的测点',
            align:"center",
            dataIndex: 'measuringPointLj'
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
          list: "/weiyan/weiyanBase/list",
          delete: "/weiyan/weiyanBase/delete",
          deleteBatch: "/weiyan/weiyanBase/deleteBatch",
          exportXlsUrl: "/weiyan/weiyanBase/exportXls",
          importExcelUrl: "weiyan/weiyanBase/importExcel",
          
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
        fieldList.push({type:'string',value:'batchNo',text:'唯一ID',dictCode:''})
        fieldList.push({type:'string',value:'shebeiNo',text:'设备编号',dictCode:''})
        fieldList.push({type:'string',value:'jobLocation',text:'施工部位',dictCode:''})
        fieldList.push({type:'string',value:'sectionName',text:'切面名称',dictCode:''})
        fieldList.push({type:'string',value:'sectionLength',text:'切面里程',dictCode:''})
        fieldList.push({type:'string',value:'sectionWidth',text:'切面宽度',dictCode:''})
        fieldList.push({type:'string',value:'excavationType',text:'开挖方式',dictCode:''})
        fieldList.push({type:'string',value:'wallRockLevel',text:'围岩级别',dictCode:''})
        fieldList.push({type:'string',value:'meauringPoint',text:'测点',dictCode:''})
        fieldList.push({type:'double',value:'meauringNumber',text:'测量值（单位m）',dictCode:''})
        fieldList.push({type:'double',value:'onceDeformation',text:'单次变形量（单位mm）',dictCode:''})
        fieldList.push({type:'double',value:'intervalTime',text:'间隔时间  （单位天）',dictCode:''})
        fieldList.push({type:'double',value:'deformationRate',text:'变形速率',dictCode:''})
        fieldList.push({type:'int',value:'onceStatus',text:'单次是否超标 (0:未超标，1:超标)',dictCode:''})
        fieldList.push({type:'int',value:'accumulatedStatus',text:'累计是否超标  (0:未超标，1:超标)',dictCode:''})
        fieldList.push({type:'double',value:'accumulatedDeformation',text:'累计变形量单位（mm）',dictCode:''})
        fieldList.push({type:'int',value:'alertstate',text:'是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计      40为数据异常',dictCode:''})
        fieldList.push({type:'date',value:'birthTime',text:'测量时间'})
        fieldList.push({type:'date',value:'uploadTime',text:'数据上传时间'})
        fieldList.push({type:'int',value:'isUse',text:'状态',dictCode:''})
        fieldList.push({type:'int',value:'callLevel',text:'报警级别(0,代表合格，1代表黄色报警，21代表红色报警)',dictCode:''})
        fieldList.push({type:'int',value:'superviseType',text:'检测类型（0:速率，1累计）',dictCode:''})
        fieldList.push({type:'int',value:'callSum',text:'callSum',dictCode:''})
        fieldList.push({type:'int',value:'intervalFirstTime',text:'间隔时间(距离初次测试时间)',dictCode:''})
        fieldList.push({type:'string',value:'measuringStatus',text:'测量状态（0：正常 ,1：重新布设）',dictCode:''})
        fieldList.push({type:'string',value:'problemRecord',text:'超标问题中文描述',dictCode:''})
        fieldList.push({type:'string',value:'measuringPointLj',text:'临建再统计后的测点',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>