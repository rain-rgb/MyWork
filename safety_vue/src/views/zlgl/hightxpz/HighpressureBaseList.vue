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
      <a-button type="primary" icon="download" @click="handleExportXls('高压旋喷桩主表')">导出</a-button>
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

    <highpressure-base-modal ref="modalForm" @ok="modalFormOk"></highpressure-base-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import HighpressureBaseModal from './modules/HighpressureBaseModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'

  export default {
    name: 'HighpressureBaseList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      HighpressureBaseModal,
      JSuperQuery,
    },
    data () {
      return {
        description: '高压旋喷桩主表管理页面',
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
            title:'设备编码',
            align:"center",
            dataIndex: 'equipmentcode'
          },
          {
            title:'桩号',
            align:"center",
            dataIndex: 'constructionstakenum'
          },
          {
            title:'客户端当前记录产生时间（实施采集时间）',
            align:"center",
            dataIndex: 'realtime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'开始时间（该根桩的开始时间）',
            align:"center",
            dataIndex: 'starttime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'记录状态：1-开始，2-暂停，3-结束,4-报警；',
            align:"center",
            dataIndex: 'recordstatus'
          },
          {
            title:'钻杆状态：1-下钻，2-提钻，3-复下，4-复提；',
            align:"center",
            dataIndex: 'pipestatus'
          },
          {
            title:'喷浆状态：1-喷浆，0-止喷',
            align:"center",
            dataIndex: 'nozzlestatus'
          },
          {
            title:'喷射模式：1-单管法，6-二重管法，3-三重管法',
            align:"center",
            dataIndex: 'pumpmode'
          },
          {
            title:'记录模式：0-深度模式，1-时间模式',
            align:"center",
            dataIndex: 'recordmode'
          },
          {
            title:'钻杆模式：0-单杆模式，1-接杆模式',
            align:"center",
            dataIndex: 'pipemode'
          },
          {
            title:'水灰比',
            align:"center",
            dataIndex: 'waterashratio'
          },
          {
            title:'记录间隔：间隔的数值，深度对应单位 cm，时间对应单位 s',
            align:"center",
            dataIndex: 'recordinterval'
          },
          {
            title:'段浆量(L)',
            align:"center",
            dataIndex: 'segmentpulp'
          },
          {
            title:'段灰量(kg)',
            align:"center",
            dataIndex: 'segmentash'
          },
          {
            title:'累计浆量（L）',
            align:"center",
            dataIndex: 'totalpulp'
          },
          {
            title:'累计灰量（KG）',
            align:"center",
            dataIndex: 'totalash'
          },
          {
            title:'累计水量(L)',
            align:"center",
            dataIndex: 'totalwater'
          },
          {
            title:'累计气量(m3)',
            align:"center",
            dataIndex: 'totalair'
          },
          {
            title:'累计返浆量(L)',
            align:"center",
            dataIndex: 'totalreturnpulp'
          },
          {
            title:'水喷压(MPa)',
            align:"center",
            dataIndex: 'waterpressure'
          },
          {
            title:'水流量(L)',
            align:"center",
            dataIndex: 'waterflow'
          },
          {
            title:'气流量(m3)',
            align:"center",
            dataIndex: 'airflow'
          },
          {
            title:'气喷压(MPa)',
            align:"center",
            dataIndex: 'airpressure'
          },
          {
            title:'返浆量(L)',
            align:"center",
            dataIndex: 'returnpulp'
          },
          {
            title:'返浆压力(MPa)',
            align:"center",
            dataIndex: 'returnpulppressure'
          },
          {
            title:'密度(g/cm3)',
            align:"center",
            dataIndex: 'density'
          },
          {
            title:'浆喷压(MPa)',
            align:"center",
            dataIndex: 'pressure'
          },
          {
            title:'下钻/提钻速度(cm/min)',
            align:"center",
            dataIndex: 'speed'
          },
          {
            title:'钻速(r/min)',
            align:"center",
            dataIndex: 'drillingrate'
          },
          {
            title:'深度(m)',
            align:"center",
            dataIndex: 'currentdepth'
          },
          {
            title:'最大深度(m)',
            align:"center",
            dataIndex: 'maxdepth'
          },
          {
            title:'流量',
            align:"center",
            dataIndex: 'flow'
          },
          {
            title:'前后倾角',
            align:"center",
            dataIndex: 'xangle'
          },
          {
            title:'左右倾角',
            align:"center",
            dataIndex: 'yangle'
          },
          {
            title:'倾斜度',
            align:"center",
            dataIndex: 'gradient'
          },
          {
            title:'电流(A)',
            align:"center",
            dataIndex: 'electricity'
          },
          {
            title:'经度',
            align:"center",
            dataIndex: 'longitudu'
          },
          {
            title:'纬度',
            align:"center",
            dataIndex: 'latitude'
          },
          {
            title:'关联id',
            align:"center",
            dataIndex: 'guid'
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
          list: "/Hightxpz/highpressureBase/list",
          delete: "/Hightxpz/highpressureBase/delete",
          deleteBatch: "/Hightxpz/highpressureBase/deleteBatch",
          exportXlsUrl: "/Hightxpz/highpressureBase/exportXls",
          importExcelUrl: "Hightxpz/highpressureBase/importExcel",
          
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
        fieldList.push({type:'string',value:'equipmentcode',text:'设备编码',dictCode:''})
        fieldList.push({type:'string',value:'constructionstakenum',text:'桩号',dictCode:''})
        fieldList.push({type:'date',value:'realtime',text:'客户端当前记录产生时间（实施采集时间）'})
        fieldList.push({type:'date',value:'starttime',text:'开始时间（该根桩的开始时间）'})
        fieldList.push({type:'int',value:'recordstatus',text:'记录状态：1-开始，2-暂停，3-结束,4-报警；',dictCode:''})
        fieldList.push({type:'int',value:'pipestatus',text:'钻杆状态：1-下钻，2-提钻，3-复下，4-复提；',dictCode:''})
        fieldList.push({type:'int',value:'nozzlestatus',text:'喷浆状态：1-喷浆，0-止喷',dictCode:''})
        fieldList.push({type:'int',value:'pumpmode',text:'喷射模式：1-单管法，6-二重管法，3-三重管法',dictCode:''})
        fieldList.push({type:'int',value:'recordmode',text:'记录模式：0-深度模式，1-时间模式',dictCode:''})
        fieldList.push({type:'int',value:'pipemode',text:'钻杆模式：0-单杆模式，1-接杆模式',dictCode:''})
        fieldList.push({type:'double',value:'waterashratio',text:'水灰比',dictCode:''})
        fieldList.push({type:'int',value:'recordinterval',text:'记录间隔：间隔的数值，深度对应单位 cm，时间对应单位 s',dictCode:''})
        fieldList.push({type:'double',value:'segmentpulp',text:'段浆量(L)',dictCode:''})
        fieldList.push({type:'double',value:'segmentash',text:'段灰量(kg)',dictCode:''})
        fieldList.push({type:'double',value:'totalpulp',text:'累计浆量（L）',dictCode:''})
        fieldList.push({type:'double',value:'totalash',text:'累计灰量（KG）',dictCode:''})
        fieldList.push({type:'double',value:'totalwater',text:'累计水量(L)',dictCode:''})
        fieldList.push({type:'double',value:'totalair',text:'累计气量(m3)',dictCode:''})
        fieldList.push({type:'double',value:'totalreturnpulp',text:'累计返浆量(L)',dictCode:''})
        fieldList.push({type:'double',value:'waterpressure',text:'水喷压(MPa)',dictCode:''})
        fieldList.push({type:'double',value:'waterflow',text:'水流量(L)',dictCode:''})
        fieldList.push({type:'double',value:'airflow',text:'气流量(m3)',dictCode:''})
        fieldList.push({type:'double',value:'airpressure',text:'气喷压(MPa)',dictCode:''})
        fieldList.push({type:'double',value:'returnpulp',text:'返浆量(L)',dictCode:''})
        fieldList.push({type:'double',value:'returnpulppressure',text:'返浆压力(MPa)',dictCode:''})
        fieldList.push({type:'double',value:'density',text:'密度(g/cm3)',dictCode:''})
        fieldList.push({type:'double',value:'pressure',text:'浆喷压(MPa)',dictCode:''})
        fieldList.push({type:'double',value:'speed',text:'下钻/提钻速度(cm/min)',dictCode:''})
        fieldList.push({type:'double',value:'drillingrate',text:'钻速(r/min)',dictCode:''})
        fieldList.push({type:'double',value:'currentdepth',text:'深度(m)',dictCode:''})
        fieldList.push({type:'double',value:'maxdepth',text:'最大深度(m)',dictCode:''})
        fieldList.push({type:'double',value:'flow',text:'流量',dictCode:''})
        fieldList.push({type:'double',value:'xangle',text:'前后倾角',dictCode:''})
        fieldList.push({type:'double',value:'yangle',text:'左右倾角',dictCode:''})
        fieldList.push({type:'double',value:'gradient',text:'倾斜度',dictCode:''})
        fieldList.push({type:'double',value:'electricity',text:'电流(A)',dictCode:''})
        fieldList.push({type:'double',value:'longitudu',text:'经度',dictCode:''})
        fieldList.push({type:'double',value:'latitude',text:'纬度',dictCode:''})
        fieldList.push({type:'string',value:'guid',text:'关联id',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>