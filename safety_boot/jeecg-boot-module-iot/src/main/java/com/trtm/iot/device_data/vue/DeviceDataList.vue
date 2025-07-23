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
      <a-button type="primary" icon="download" @click="handleExportXls('搅拌车监控')">导出</a-button>
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

    <device-data-modal ref="modalForm" @ok="modalFormOk"></device-data-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceDataModal from './modules/DeviceDataModal'

  export default {
    name: 'DeviceDataList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceDataModal
    },
    data () {
      return {
        description: '搅拌车监控管理页面',
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
            title:'设备序列号，全局唯一',
            align:"center",
            dataIndex: 'deviceid'
          },
          {
            title:'gps设备上报的时间',
            align:"center",
            dataIndex: 'devicetime'
          },
          {
            title:'到达服务器时间',
            align:"center",
            dataIndex: 'arrivedtime'
          },
          {
            title:'综合计算后位置更新时间',
            align:"center",
            dataIndex: 'updatetime'
          },
          {
            title:'最后位置定位时间',
            align:"center",
            dataIndex: 'validpoistiontime'
          },
          {
            title:'计算出来的纬度',
            align:"center",
            dataIndex: 'callat'
          },
          {
            title:'计算出来的经度',
            align:"center",
            dataIndex: 'callon'
          },
          {
            title:'高度(米)',
            align:"center",
            dataIndex: 'altitude'
          },
          {
            title:'定位精度米',
            align:"center",
            dataIndex: 'radius'
          },
          {
            title:'速度（米）',
            align:"center",
            dataIndex: 'speed'
          },
          {
            title:'方向（0 - 360）',
            align:"center",
            dataIndex: 'course'
          },
          {
            title:'总里程米',
            align:"center",
            dataIndex: 'totaldistance'
          },
          {
            title:'总油量，单位毫升',
            align:"center",
            dataIndex: 'totaloil'
          },
          {
            title:'总非行驶油耗，单位毫升',
            align:"center",
            dataIndex: 'totalnotrunningad'
          },
          {
            title:'分开统计的时候第1个油箱，单位毫升',
            align:"center",
            dataIndex: 'masteroil'
          },
          {
            title:'分开统计的时候第2个油箱，单位毫升',
            align:"center",
            dataIndex: 'auxoil'
          },
          {
            title:'分开统计的时候第3个油箱，单位毫升',
            align:"center",
            dataIndex: 'thirdoil'
          },
          {
            title:'分开统计的时候第4个油箱，单位毫升',
            align:"center",
            dataIndex: 'fourthoil'
          },
          {
            title:'油量模拟量1',
            align:"center",
            dataIndex: 'srcad0'
          },
          {
            title:'油量模拟量2',
            align:"center",
            dataIndex: 'srcad1'
          },
          {
            title:'油量模拟量3',
            align:"center",
            dataIndex: 'srcad2'
          },
          {
            title:'油量模拟量4',
            align:"center",
            dataIndex: 'srcad3'
          },
          {
            title:'对应部标状态值',
            align:"center",
            dataIndex: 'status'
          },
          {
            title:'对应部标状态文字描述',
            align:"center",
            dataIndex: 'strstatus'
          },
          {
            title:'对应部标状态英文文字描述',
            align:"center",
            dataIndex: 'strstatusen'
          },
          {
            title:'对应部标报警值',
            align:"center",
            dataIndex: 'alarm'
          },
          {
            title:'对应部标报警文字描述',
            align:"center",
            dataIndex: 'stralarm'
          },
          {
            title:'对应部标报警英文文字描述',
            align:"center",
            dataIndex: 'stralarmsen'
          },
          {
            title:'对应部标视频报警值',
            align:"center",
            dataIndex: 'videoalarm'
          },
          {
            title:'对应部标视频报警文字描述',
            align:"center",
            dataIndex: 'strvideoalarm'
          },
          {
            title:'对应部标视频报警英文文字描述',
            align:"center",
            dataIndex: 'strvideoalarmen'
          },
          {
            title:'视频信号丢失状态',
            align:"center",
            dataIndex: 'videosignalloststatus'
          },
          {
            title:'视频遮挡状态',
            align:"center",
            dataIndex: 'videosignalcoverstatus'
          },
          {
            title:'视频异常驾驶行为状态',
            align:"center",
            dataIndex: 'videobehavior'
          },
          {
            title:'视频疲劳程度',
            align:"center",
            dataIndex: 'videofatiguedegree'
          },
          {
            title:'位置信息来源定位类型 (gps,wifi,cell)',
            align:"center",
            dataIndex: 'gotsrc'
          },
          {
            title:'基站信号强度',
            align:"center",
            dataIndex: 'rxlevel'
          },
          {
            title:'gps有效数',
            align:"center",
            dataIndex: 'gpsvalidnum'
          },
          {
            title:'外部电压，单位0.01V 默认是-1,0代表断电了',
            align:"center",
            dataIndex: 'exvoltage'
          },
          {
            title:'电压，单位0.01V 默认是-1,0代表断电了',
            align:"center",
            dataIndex: 'voltagev'
          },
          {
            title:'电量百分比，有的设备百分比是通过上报次数计算百分比',
            align:"center",
            dataIndex: 'voltagepercent'
          },
          {
            title:'是否移动，是否在运动 0代表没运动 1代表在运动',
            align:"center",
            dataIndex: 'moving'
          },
          {
            title:'最后停留的lat',
            align:"center",
            dataIndex: 'parklat'
          },
          {
            title:'最后停留的lon',
            align:"center",
            dataIndex: 'parklon'
          },
          {
            title:'最后停留时间',
            align:"center",
            dataIndex: 'parktime'
          },
          {
            title:'停留了多久',
            align:"center",
            dataIndex: 'parkduration'
          },
          {
            title:'温度1，单位 1/100度， 65535 代表无效',
            align:"center",
            dataIndex: 'temp1'
          },
          {
            title:'温度2，单位 1/100度， 65535 代表无效',
            align:"center",
            dataIndex: 'temp2'
          },
          {
            title:'温度3，单位 1/100度， 65535 代表无效',
            align:"center",
            dataIndex: 'temp3'
          },
          {
            title:'温度4，单位 1/100度， 65535 代表无效',
            align:"center",
            dataIndex: 'temp4'
          },
          {
            title:'湿度1',
            align:"center",
            dataIndex: 'humi1'
          },
          {
            title:'湿度2',
            align:"center",
            dataIndex: 'humi2'
          },
          {
            title:'IO 状态位',
            align:"center",
            dataIndex: 'iostatus'
          },
          {
            title:'1代表正在超速 2代表未超速状态',
            align:"center",
            dataIndex: 'currentoverspeedstate'
          },
          {
            title:'正反转(0:未知；1：正转（空车）2:反转（重车）；3：停转)',
            align:"center",
            dataIndex: 'rotatestatus'
          },
          {
            title:'载重状态 0x00：空车；0x01：半载；0x02：超载；0x03：满载 0x04 装载 0x05 卸载',
            align:"center",
            dataIndex: 'loadstatus'
          },
          {
            title:'重量 单位0.1kg',
            align:"center",
            dataIndex: 'weight'
          },
          {
            title:'载重里面的ad0,目前只做1路',
            align:"center",
            dataIndex: 'srcweightad0'
          },
          {
            title:'上报类型',
            align:"center",
            dataIndex: 'reportmode'
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
          list: "/device_data/deviceData/list",
          delete: "/device_data/deviceData/delete",
          deleteBatch: "/device_data/deviceData/deleteBatch",
          exportXlsUrl: "/device_data/deviceData/exportXls",
          importExcelUrl: "device_data/deviceData/importExcel",
          
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
        fieldList.push({type:'string',value:'deviceid',text:'设备序列号，全局唯一'})
        fieldList.push({type:'int',value:'devicetime',text:'gps设备上报的时间'})
        fieldList.push({type:'int',value:'arrivedtime',text:'到达服务器时间'})
        fieldList.push({type:'int',value:'updatetime',text:'综合计算后位置更新时间'})
        fieldList.push({type:'int',value:'validpoistiontime',text:'最后位置定位时间'})
        fieldList.push({type:'number',value:'callat',text:'计算出来的纬度'})
        fieldList.push({type:'number',value:'callon',text:'计算出来的经度'})
        fieldList.push({type:'number',value:'altitude',text:'高度(米)'})
        fieldList.push({type:'int',value:'radius',text:'定位精度米'})
        fieldList.push({type:'number',value:'speed',text:'速度（米）'})
        fieldList.push({type:'int',value:'course',text:'方向（0 - 360）'})
        fieldList.push({type:'int',value:'totaldistance',text:'总里程米'})
        fieldList.push({type:'int',value:'totaloil',text:'总油量，单位毫升'})
        fieldList.push({type:'int',value:'totalnotrunningad',text:'总非行驶油耗，单位毫升'})
        fieldList.push({type:'int',value:'masteroil',text:'分开统计的时候第1个油箱，单位毫升'})
        fieldList.push({type:'int',value:'auxoil',text:'分开统计的时候第2个油箱，单位毫升'})
        fieldList.push({type:'int',value:'thirdoil',text:'分开统计的时候第3个油箱，单位毫升'})
        fieldList.push({type:'int',value:'fourthoil',text:'分开统计的时候第4个油箱，单位毫升'})
        fieldList.push({type:'int',value:'srcad0',text:'油量模拟量1'})
        fieldList.push({type:'int',value:'srcad1',text:'油量模拟量2'})
        fieldList.push({type:'int',value:'srcad2',text:'油量模拟量3'})
        fieldList.push({type:'int',value:'srcad3',text:'油量模拟量4'})
        fieldList.push({type:'int',value:'status',text:'对应部标状态值'})
        fieldList.push({type:'string',value:'strstatus',text:'对应部标状态文字描述'})
        fieldList.push({type:'string',value:'strstatusen',text:'对应部标状态英文文字描述'})
        fieldList.push({type:'int',value:'alarm',text:'对应部标报警值'})
        fieldList.push({type:'string',value:'stralarm',text:'对应部标报警文字描述'})
        fieldList.push({type:'string',value:'stralarmsen',text:'对应部标报警英文文字描述'})
        fieldList.push({type:'int',value:'videoalarm',text:'对应部标视频报警值'})
        fieldList.push({type:'string',value:'strvideoalarm',text:'对应部标视频报警文字描述'})
        fieldList.push({type:'string',value:'strvideoalarmen',text:'对应部标视频报警英文文字描述'})
        fieldList.push({type:'int',value:'videosignalloststatus',text:'视频信号丢失状态'})
        fieldList.push({type:'int',value:'videosignalcoverstatus',text:'视频遮挡状态'})
        fieldList.push({type:'int',value:'videobehavior',text:'视频异常驾驶行为状态'})
        fieldList.push({type:'int',value:'videofatiguedegree',text:'视频疲劳程度'})
        fieldList.push({type:'string',value:'gotsrc',text:'位置信息来源定位类型 (gps,wifi,cell)'})
        fieldList.push({type:'int',value:'rxlevel',text:'基站信号强度'})
        fieldList.push({type:'int',value:'gpsvalidnum',text:'gps有效数'})
        fieldList.push({type:'number',value:'exvoltage',text:'外部电压，单位0.01V 默认是-1,0代表断电了'})
        fieldList.push({type:'number',value:'voltagev',text:'电压，单位0.01V 默认是-1,0代表断电了'})
        fieldList.push({type:'number',value:'voltagepercent',text:'电量百分比，有的设备百分比是通过上报次数计算百分比'})
        fieldList.push({type:'int',value:'moving',text:'是否移动，是否在运动 0代表没运动 1代表在运动'})
        fieldList.push({type:'number',value:'parklat',text:'最后停留的lat'})
        fieldList.push({type:'number',value:'parklon',text:'最后停留的lon'})
        fieldList.push({type:'int',value:'parktime',text:'最后停留时间'})
        fieldList.push({type:'int',value:'parkduration',text:'停留了多久'})
        fieldList.push({type:'int',value:'temp1',text:'温度1，单位 1/100度， 65535 代表无效'})
        fieldList.push({type:'int',value:'temp2',text:'温度2，单位 1/100度， 65535 代表无效'})
        fieldList.push({type:'int',value:'temp3',text:'温度3，单位 1/100度， 65535 代表无效'})
        fieldList.push({type:'int',value:'temp4',text:'温度4，单位 1/100度， 65535 代表无效'})
        fieldList.push({type:'int',value:'humi1',text:'湿度1'})
        fieldList.push({type:'int',value:'humi2',text:'湿度2'})
        fieldList.push({type:'int',value:'iostatus',text:'IO 状态位'})
        fieldList.push({type:'int',value:'currentoverspeedstate',text:'1代表正在超速 2代表未超速状态'})
        fieldList.push({type:'int',value:'rotatestatus',text:'正反转(0:未知；1：正转（空车）2:反转（重车）；3：停转)'})
        fieldList.push({type:'int',value:'loadstatus',text:'载重状态 0x00：空车；0x01：半载；0x02：超载；0x03：满载 0x04 装载 0x05 卸载'})
        fieldList.push({type:'int',value:'weight',text:'重量 单位0.1kg'})
        fieldList.push({type:'int',value:'srcweightad0',text:'载重里面的ad0,目前只做1路'})
        fieldList.push({type:'int',value:'reportmode',text:'上报类型'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>