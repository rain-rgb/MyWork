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
      <a-button type="primary" icon="download" @click="handleExportXls('监控量测报警数据表')">导出</a-button>
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

    <weiyan-alarm-modal ref="modalForm" @ok="modalFormOk"></weiyan-alarm-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WeiyanAlarmModal from './modules/WeiyanAlarmModal'

  export default {
    name: 'WeiyanAlarmList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WeiyanAlarmModal
    },
    data () {
      return {
        description: '监控量测报警数据表管理页面',
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
            title:'围岩级别',
            align:"center",
            dataIndex: 'wallRockLevel'
          },
          {
            title:'测点/线',
            align:"center",
            dataIndex: 'measuringPoint'
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
            title:'变形速率(mm/d)',
            align:"center",
            dataIndex: 'deformationRate'
          },
          {
            title:'累计变形量 单位（mm）',
            align:"center",
            dataIndex: 'accumulatedDeformation'
          },
          {
            title:'报警级别(0,代表合格，1代表黄色报警，21代表红色报警)',
            align:"center",
            dataIndex: 'callLevel'
          },
          {
            title:'状态(0 未关闭 1 已关闭)',
            align:"center",
            dataIndex: 'isUse'
          },
          {
            title:'处置数',
            align:"center",
            dataIndex: 'handleNum'
          },
          {
            title:'工点',
            align:"center",
            dataIndex: 'workPoint'
          },
          {
            title:'断面名称',
            align:"center",
            dataIndex: 'sectionName'
          },
          {
            title:'工区',
            align:"center",
            dataIndex: 'workArea'
          },
          {
            title:'测量类型',
            align:"center",
            dataIndex: 'measurementType'
          },
          {
            title:'处置状态：0为未处理，10为施工方已处理，20为监理方已处理 30驳回',
            align:"center",
            dataIndex: 'dispositionStatus'
          },
          {
            title:'测量人员',
            align:"center",
            dataIndex: 'surveyor'
          },
          {
            title:'速率阈值(mm/d)',
            align:"center",
            dataIndex: 'rateThreshold'
          },
          {
            title:'累计阈值(mm)',
            align:"center",
            dataIndex: 'cumulativeHreshold'
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
          list: "/weiyanalarm/weiyanAlarm/list",
          delete: "/weiyanalarm/weiyanAlarm/delete",
          deleteBatch: "/weiyanalarm/weiyanAlarm/deleteBatch",
          exportXlsUrl: "/weiyanalarm/weiyanAlarm/exportXls",
          importExcelUrl: "weiyanalarm/weiyanAlarm/importExcel",
          
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
        fieldList.push({type:'string',value:'wallRockLevel',text:'围岩级别',dictCode:''})
        fieldList.push({type:'string',value:'measuringPoint',text:'测点/线',dictCode:''})
        fieldList.push({type:'date',value:'birthTime',text:'测量时间'})
        fieldList.push({type:'double',value:'deformationRate',text:'变形速率(mm/d)',dictCode:''})
        fieldList.push({type:'double',value:'accumulatedDeformation',text:'累计变形量 单位（mm）',dictCode:''})
        fieldList.push({type:'int',value:'callLevel',text:'报警级别(0,代表合格，1代表黄色报警，21代表红色报警)',dictCode:''})
        fieldList.push({type:'int',value:'isUse',text:'状态(0 未关闭 1 已关闭)',dictCode:''})
        fieldList.push({type:'int',value:'handleNum',text:'处置数',dictCode:''})
        fieldList.push({type:'string',value:'workPoint',text:'工点',dictCode:''})
        fieldList.push({type:'string',value:'sectionName',text:'断面名称',dictCode:''})
        fieldList.push({type:'string',value:'workArea',text:'工区',dictCode:''})
        fieldList.push({type:'string',value:'measurementType',text:'测量类型',dictCode:''})
        fieldList.push({type:'int',value:'dispositionStatus',text:'处置状态：0为未处理，10为施工方已处理，20为监理方已处理 30驳回',dictCode:''})
        fieldList.push({type:'string',value:'surveyor',text:'测量人员',dictCode:''})
        fieldList.push({type:'string',value:'rateThreshold',text:'速率阈值(mm/d)',dictCode:''})
        fieldList.push({type:'string',value:'cumulativeHreshold',text:'累计阈值(mm)',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>