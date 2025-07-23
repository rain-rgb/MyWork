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
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
<!--      <a-button type="primary" icon="download" @click="handleExportXls('孔道灌浆（定位检测）主表')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
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
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
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

    <tr-kongdaogj-dwm-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import TrKongdaogjDwmModal from './modules/TrKongdaogjDwmModal'
  import '@/assets/less/TableExpand.less'

  export default {
    name: "TrKongdaogjDwmList",
    mixins:[JeecgListMixin],
    components: {
      TrKongdaogjDwmModal
    },
    data () {
      return {
        description: '孔道灌浆（定位检测）主表管理页面',
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
          // {
          //   title:'设备商标识',
          //   align:"center",
          //   dataIndex: 'vendorid'
          // },
          {
            title:'任务单 ',
            align:"center",
            dataIndex: 'serialno'
          },
          {
            title:'检测单位',
            align:"center",
            dataIndex: 'testunit'
          },
          {
            title:'施工单位',
            align:"center",
            dataIndex: 'constructionunit'
          },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'projectname'
          },
          {
            title:'测试仪编号',
            align:"center",
            dataIndex: 'machineid_dictText'
          },
          {
            title:'孔道编号',
            align:"center",
            dataIndex: 'holeno'
          },
          {
            title:'梁板名称',
            align:"center",
            dataIndex: 'beamname'
          },

          {
            title:'梁板长度 ',
            align:"center",
            dataIndex: 'beamlength'
          },
          {
            title:'混凝土波速',
            align:"center",
            dataIndex: 'concretev'
          },
          {
            title:'采样间隔 （μs）',
            align:"center",
            dataIndex: 'sampleinterval'
          },
          {
            title:'低通（kHz）',
            align:"center",
            dataIndex: 'lowfilter'
          },
          {
            title:'高通 （Hz）',
            align:"center",
            dataIndex: 'highfilter'
          },
          {
            title:'采样长度',
            align:"center",
            dataIndex: 'samplelength'
          },
          {
            title:'延迟点数',
            align:"center",
            dataIndex: 'delaydot'
          },
          {
            title:'触发通道',
            align:"center",
            dataIndex: 'triggerchannel_dictText'
          },
          // {
          //   title:'触发电平',
          //   align:"center",
          //   dataIndex: 'triggerlevel'
          // },
          {
            title:'梁板厚度（m）',
            align:"center",
            dataIndex: 'beamthickness'
          },
          {
            title:'始测点位置（m）',
            align:"center",
            dataIndex: 'initpos'
          },
          {
            title:'测点间距（m）',
            align:"center",
            dataIndex: 'pointspace'
          },
          {
            title:'模式',
            align:"center",
            dataIndex: 'testmode_dictText'
          },
          {
            title:'检测人员',
            align:"center",
            dataIndex: 'worker'
          },

          // {
          //   title:'GPS 状态',
          //   align:"center",
          //   dataIndex: 'gpsvalid_dictText'
          // },
          // {
          //   title:'经度 ',
          //   align:"center",
          //   dataIndex: 'gpslongitude'
          // },
          // {
          //   title:'纬度',
          //   align:"center",
          //   dataIndex: 'gpslatitude'
          // },


          // {
          //   title:'检测日期',
          //   align:"center",
          //   dataIndex: 'testdate',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          {
            title:'检测时间',
            align:"center",
            dataIndex: 'testtime',
            // customRender:function (text) {
            //   return !text?"":(text.length>10?text.substr(0,10):text)
            // }
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remarks'
          },
          // {
          //   title:'通道号',
          //   align:"center",
          //   dataIndex: 'channelnum'
          // },
          // {
          //   title:'传感器类型 （0:加速度计1:速度计）',
          //   align:"center",
          //   dataIndex: 'sensortype_dictText'
          // },
          // {
          //   title:'传感器灵敏度系数',
          //   align:"center",
          //   dataIndex: 'sensorsensitive'
          // },
          // {
          //   title:'积分状态（是否积分, 1:积分 0：不积分）',
          //   align:"center",
          //   dataIndex: 'integralflag_dictText'
          // },
          // {
          //   title: '操作',
          //   dataIndex: 'action',
          //   align:"center",
          //   fixed:"right",
          //   width:147,
          //   scopedSlots: { customRender: 'action' },
          // }
        ],
        url: {
          list: "/trkongdaogjdwm/trKongdaogjDwm/list",
          delete: "/trkongdaogjdwm/trKongdaogjDwm/delete",
          deleteBatch: "/trkongdaogjdwm/trKongdaogjDwm/deleteBatch",
          exportXlsUrl: "/trkongdaogjdwm/trKongdaogjDwm/exportXls",
          importExcelUrl: "trkongdaogjdwm/trKongdaogjDwm/importExcel",

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
      }
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
         fieldList.push({type:'string',value:'vendorid',text:'设备商标识',dictCode:''})
         fieldList.push({type:'string',value:'vendorid',text:'设备商标识'})
         fieldList.push({type:'string',value:'serialno',text:'流水号 '})
         fieldList.push({type:'string',value:'serialno',text:'流水号 ',dictCode:''})
         fieldList.push({type:'string',value:'machineid',text:'测试仪编号'})
         fieldList.push({type:'string',value:'machineid',text:'测试仪编号',dictCode:''})
         fieldList.push({type:'string',value:'holeno',text:'孔道编号'})
         fieldList.push({type:'string',value:'holeno',text:'孔道编号',dictCode:''})
         fieldList.push({type:'string',value:'beamname',text:'梁板名称',dictCode:''})
         fieldList.push({type:'string',value:'beamname',text:'梁板名称'})
         fieldList.push({type:'date',value:'testdate',text:'检测日期'})
         fieldList.push({type:'date',value:'testdate',text:'检测日期'})
         fieldList.push({type:'date',value:'testtime',text:'检测时间'})
         fieldList.push({type:'date',value:'testtime',text:'检测时间'})
         fieldList.push({type:'string',value:'worker',text:'检测人员'})
         fieldList.push({type:'string',value:'worker',text:'检测人员',dictCode:''})
         fieldList.push({type:'string',value:'testunit',text:'检测单位'})
         fieldList.push({type:'string',value:'testunit',text:'检测单位',dictCode:''})
         fieldList.push({type:'string',value:'constructionunit',text:'施工单位',dictCode:''})
         fieldList.push({type:'string',value:'constructionunit',text:'施工单位'})
         fieldList.push({type:'string',value:'projectname',text:'工程名称',dictCode:''})
         fieldList.push({type:'string',value:'projectname',text:'工程名称'})
         fieldList.push({type:'number',value:'beamlength',text:'梁板长度 '})
         fieldList.push({type:'BigDecimal',value:'beamlength',text:'梁板长度 ',dictCode:''})
         fieldList.push({type:'int',value:'concretev',text:'混凝土波速'})
         fieldList.push({type:'int',value:'concretev',text:'混凝土波速',dictCode:''})
         fieldList.push({type:'BigDecimal',value:'sampleinterval',text:'采样间隔 （单位 μs）',dictCode:''})
         fieldList.push({type:'number',value:'sampleinterval',text:'采样间隔 （单位 μs）'})
         fieldList.push({type:'int',value:'lowfilter',text:'低通（单位kHz，3kHz、5kHz、10kHz……全通若值为 99999 表示全通）',dictCode:''})
         fieldList.push({type:'int',value:'lowfilter',text:'低通（单位kHz，3kHz、5kHz、10kHz……全通若值为 99999 表示全通）'})
         fieldList.push({type:'int',value:'highfilter',text:'高通 （单位 Hz，0Hz、50Hz、100Hz、200Hz…）'})
         fieldList.push({type:'int',value:'highfilter',text:'高通 （单位 Hz，0Hz、50Hz、100Hz、200Hz…）',dictCode:''})
         fieldList.push({type:'int',value:'samplelength',text:'采样长度',dictCode:''})
         fieldList.push({type:'int',value:'samplelength',text:'采样长度'})
         fieldList.push({type:'int',value:'delaydot',text:'延迟点数'})
         fieldList.push({type:'int',value:'delaydot',text:'延迟点数',dictCode:''})
         fieldList.push({type:'int',value:'triggerchannel',text:'触发通道（0:任意 1:CH1 2:CH2）'})
         fieldList.push({type:'int',value:'triggerchannel',text:'触发通道（0:任意 1:CH1 2:CH2）',dictCode:''})
         fieldList.push({type:'int',value:'triggerlevel',text:'触发电平'})
         fieldList.push({type:'int',value:'triggerlevel',text:'触发电平',dictCode:''})
         fieldList.push({type:'int',value:'testmode',text:'测试模式（0：无线 1：有线）'})
         fieldList.push({type:'int',value:'testmode',text:'测试模式（0：无线 1：有线）',dictCode:''})
         fieldList.push({type:'int',value:'gpsvalid',text:'GPS 状态（0：无效 1：有效）',dictCode:''})
         fieldList.push({type:'int',value:'gpsvalid',text:'GPS 状态（0：无效 1：有效）'})
         fieldList.push({type:'BigDecimal',value:'gpslongitude',text:'经度 ',dictCode:''})
         fieldList.push({type:'number',value:'gpslongitude',text:'经度 '})
         fieldList.push({type:'BigDecimal',value:'gpslatitude',text:'纬度',dictCode:''})
         fieldList.push({type:'number',value:'gpslatitude',text:'纬度'})
         fieldList.push({type:'string',value:'remarks',text:'备注',dictCode:''})
         fieldList.push({type:'string',value:'remarks',text:'备注'})
         fieldList.push({type:'number',value:'beamthickness',text:'梁板厚度 （单位：m）'})
         fieldList.push({type:'BigDecimal',value:'beamthickness',text:'梁板厚度 （单位：m）',dictCode:''})
         fieldList.push({type:'number',value:'initpos',text:'始测点位置 （单位：m）'})
         fieldList.push({type:'BigDecimal',value:'initpos',text:'始测点位置 （单位：m）',dictCode:''})
         fieldList.push({type:'BigDecimal',value:'pointspace',text:'测点间距  （单位：m）',dictCode:''})
         fieldList.push({type:'number',value:'pointspace',text:'测点间距  （单位：m）'})
         fieldList.push({type:'int',value:'channelnum',text:'通道号'})
         fieldList.push({type:'int',value:'channelnum',text:'通道号',dictCode:''})
         fieldList.push({type:'int',value:'sensortype',text:'传感器类型 （0:加速度计1:速度计）'})
         fieldList.push({type:'int',value:'sensortype',text:'传感器类型 （0:加速度计1:速度计）',dictCode:''})
         fieldList.push({type:'int',value:'sensorsensitive',text:'传感器灵敏度系数 （探头类型为加速度计,单位 mv/g ; 探头类型 为 速 度 计 , 单 位mv/(m/s)）'})
         fieldList.push({type:'int',value:'sensorsensitive',text:'传感器灵敏度系数 （探头类型为加速度计,单位 mv/g ; 探头类型 为 速 度 计 , 单 位mv/(m/s)）',dictCode:''})
         fieldList.push({type:'int',value:'integralflag',text:'积分状态（是否积分, 1:积分 0：不积分）',dictCode:''})
         fieldList.push({type:'int',value:'integralflag',text:'积分状态（是否积分, 1:积分 0：不积分）'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>