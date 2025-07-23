<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form @keyup.enter.native="searchQuery" layout="inline">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" icon="plus" type="primary">新增</a-button>
      <a-button @click="handleExportXls('摊铺碾压预警')" icon="download" type="primary">导出</a-button>
      <a-upload :action="importExcelUrl" :headers="tokenHeader" :multiple="false" :showUploadList="false" @change="handleImportExcel" name="file">
        <a-button icon="import" type="primary">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" @handleSuperQuery="handleSuperQuery" ref="superQueryModal"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item @click="batchDel" key="1"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a @click="onClearSelected" style="margin-left: 24px">清空</a>
      </div>

      <a-table
        :columns="columns"
        :dataSource="dataSource"
        :loading="loading"
        :pagination="ipagination"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        :scroll="{x:true}"
        @change="handleTableChange"
        bordered
        class="j-table-force-nowrap"
        ref="table"
        rowKey="id"
        size="middle">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span style="font-size: 12px;font-style: italic;" v-if="!text">无图片</span>
          <img :src="getImgView(text)" alt="" height="25px" style="max-width:80px;font-size: 12px;font-style: italic;" v-else/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span style="font-size: 12px;font-style: italic;" v-if="!text">无文件</span>
          <a-button
            :ghost="true"
            @click="downloadFile(text)"
            icon="download"
            size="small"
            type="primary"
            v-else>
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
                <a-popconfirm @confirm="() => handleDelete(record.id)" title="确定删除吗?">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <hc-pavement-alarm-modal @ok="modalFormOk" ref="modalForm"></hc-pavement-alarm-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import HcPavementAlarmModal from './modules/HcPavementAlarmModal'

  export default {
    name: 'HcPavementAlarmList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      HcPavementAlarmModal
    },
    data () {
      return {
        description: '摊铺碾压预警管理页面',
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
            title:'告警类型，1碾压超速，2碾压温度过低，3摊铺超速，4摊铺温度过低，5碾压距摊铺机过远',
            align:"center",
            dataIndex: 'alarmtypeid'
          },
          {
            title:'机械类型id',
            align:"center",
            dataIndex: 'machinetypeid'
          },
          {
            title:'告警级别，0为无级别，1为轻微，2为一般，3为严重',
            align:"center",
            dataIndex: 'alarmlevel'
          },
          {
            title:'设备sn号',
            align:"center",
            dataIndex: 'sncode'
          },
          {
            title:'机械id',
            align:"center",
            dataIndex: 'machineid'
          },
          {
            title:'机械名称',
            align:"center",
            dataIndex: 'machinename'
          },
          {
            title:'告警时间',
            align:"center",
            dataIndex: 'datatime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'标准值',
            align:"center",
            dataIndex: 'standard'
          },
          {
            title:'真实值',
            align:"center",
            dataIndex: 'actual'
          },
          {
            title:'经度',
            align:"center",
            dataIndex: 'lon'
          },
          {
            title:'纬度',
            align:"center",
            dataIndex: 'lat'
          },
          {
            title:'桩号',
            align:"center",
            dataIndex: 'roadstation'
          },
          {
            title:'左右幅，左负右正',
            align:"center",
            dataIndex: 'roadoffset'
          },
          {
            title:'
告警内容',
            align:"center",
            dataIndex: 'alarminfo'
          },
          {
            title:'projectid',
            align:"center",
            dataIndex: 'projectid'
          },
          {
            title:'sectionid',
            align:"center",
            dataIndex: 'sectionid'
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
          list: "/hc_pavement_alarm/hcPavementAlarm/list",
          delete: "/hc_pavement_alarm/hcPavementAlarm/delete",
          deleteBatch: "/hc_pavement_alarm/hcPavementAlarm/deleteBatch",
          exportXlsUrl: "/hc_pavement_alarm/hcPavementAlarm/exportXls",
          importExcelUrl: "hc_pavement_alarm/hcPavementAlarm/importExcel",

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
        fieldList.push({type:'int',value:'alarmtypeid',text:'告警类型，1碾压超速，2碾压温度过低，3摊铺超速，4摊铺温度过低，5碾压距摊铺机过远'})
        fieldList.push({type:'string',value:'machinetypeid',text:'机械类型id'})
        fieldList.push({type:'int',value:'alarmlevel',text:'告警级别，0为无级别，1为轻微，2为一般，3为严重'})
        fieldList.push({type:'string',value:'sncode',text:'设备sn号'})
        fieldList.push({type:'string',value:'machineid',text:'机械id'})
        fieldList.push({type:'string',value:'machinename',text:'机械名称'})
        fieldList.push({type:'date',value:'datatime',text:'告警时间'})
        fieldList.push({type:'string',value:'standard',text:'标准值'})
        fieldList.push({type:'string',value:'actual',text:'真实值'})
        fieldList.push({type:'string',value:'lon',text:'经度'})
        fieldList.push({type:'string',value:'lat',text:'纬度'})
        fieldList.push({type:'string',value:'roadstation',text:'桩号'})
        fieldList.push({type:'string',value:'roadoffset',text:'左右幅，左负右正'})
        fieldList.push({type:'string',value:'alarminfo',text:'
告警内容'})
        fieldList.push({type:'string',value:'projectid',text:'projectid'})
        fieldList.push({type:'string',value:'sectionid',text:'sectionid'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
