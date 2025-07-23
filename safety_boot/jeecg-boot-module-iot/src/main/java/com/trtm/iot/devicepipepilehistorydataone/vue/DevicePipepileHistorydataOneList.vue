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
      <a-button type="primary" icon="download" @click="handleExportXls('device_pipepile_historydata_one')">导出</a-button>
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

    <device-pipepile-historydata-one-modal ref="modalForm" @ok="modalFormOk"></device-pipepile-historydata-one-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DevicePipepileHistorydataOneModal from './modules/DevicePipepileHistorydataOneModal'

  export default {
    name: 'DevicePipepileHistorydataOneList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DevicePipepileHistorydataOneModal
    },
    data () {
      return {
        description: 'device_pipepile_historydata_one管理页面',
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
            dataIndex: 'shebeino'
          },
          {
            title:'桩号',
            align:"center",
            dataIndex: 'pileNo'
          },
          {
            title:'施工长度(m)',
            align:"center",
            dataIndex: 'pileRealdep'
          },
          {
            title:'成桩时间',
            align:"center",
            dataIndex: 'pileWorktime'
          },
          {
            title:'最大垂直度(%)',
            align:"center",
            dataIndex: 'pileY'
          },
          {
            title:'结束时间',
            align:"center",
            dataIndex: 'pileTime'
          },
          {
            title:'开始时间',
            align:"center",
            dataIndex: 'pileStarttime'
          },
          {
            title:'最大压桩力(KN)',
            align:"center",
            dataIndex: 'pileUpress'
          },
          {
            title:'最大夹持力(KN)',
            align:"center",
            dataIndex: 'pileDpress'
          },
          {
            title:'平均速度(cm/min)',
            align:"center",
            dataIndex: 'pileSpeed'
          },
          {
            title:'接桩次数',
            align:"center",
            dataIndex: 'times'
          },
          {
            title:'设计桩长',
            align:"center",
            dataIndex: 'pileDesigndep'
          },
          {
            title:'关联软基任务单 device_mixpile_rwd',
            align:"center",
            dataIndex: 'rjrwd'
          },
          {
            title:'唯一码',
            align:"center",
            dataIndex: 'uuid'
          },
          {
            title:'里程',
            align:"center",
            dataIndex: 'pileMileage'
          },
          {
            title:'数据时间',
            align:"center",
            dataIndex: 'datatime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'时间戳',
            align:"center",
            dataIndex: 'ts'
          },
          {
            title:'桩经度',
            align:"center",
            dataIndex: 'pileLgd'
          },
          {
            title:'桩纬度',
            align:"center",
            dataIndex: 'pileLtd'
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
          list: "/devicepipepilehistorydataone/devicePipepileHistorydataOne/list",
          delete: "/devicepipepilehistorydataone/devicePipepileHistorydataOne/delete",
          deleteBatch: "/devicepipepilehistorydataone/devicePipepileHistorydataOne/deleteBatch",
          exportXlsUrl: "/devicepipepilehistorydataone/devicePipepileHistorydataOne/exportXls",
          importExcelUrl: "devicepipepilehistorydataone/devicePipepileHistorydataOne/importExcel",
          
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
        fieldList.push({type:'string',value:'shebeino',text:'设备编号'})
        fieldList.push({type:'string',value:'pileNo',text:'桩号'})
        fieldList.push({type:'string',value:'pileRealdep',text:'施工长度(m)'})
        fieldList.push({type:'string',value:'pileWorktime',text:'成桩时间'})
        fieldList.push({type:'string',value:'pileY',text:'最大垂直度(%)'})
        fieldList.push({type:'string',value:'pileTime',text:'结束时间'})
        fieldList.push({type:'string',value:'pileStarttime',text:'开始时间'})
        fieldList.push({type:'string',value:'pileUpress',text:'最大压桩力(KN)'})
        fieldList.push({type:'string',value:'pileDpress',text:'最大夹持力(KN)'})
        fieldList.push({type:'string',value:'pileSpeed',text:'平均速度(cm/min)'})
        fieldList.push({type:'int',value:'times',text:'接桩次数'})
        fieldList.push({type:'string',value:'pileDesigndep',text:'设计桩长'})
        fieldList.push({type:'string',value:'rjrwd',text:'关联软基任务单 device_mixpile_rwd'})
        fieldList.push({type:'string',value:'uuid',text:'唯一码'})
        fieldList.push({type:'string',value:'pileMileage',text:'里程'})
        fieldList.push({type:'date',value:'datatime',text:'数据时间'})
        fieldList.push({type:'string',value:'ts',text:'时间戳'})
        fieldList.push({type:'string',value:'pileLgd',text:'桩经度'})
        fieldList.push({type:'string',value:'pileLtd',text:'桩纬度'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>