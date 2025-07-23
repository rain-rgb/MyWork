<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.deviceCode" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="上报时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.bridgedate_begin" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.bridgedate_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

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

        <span slot="tags" slot-scope="tags, record">
          <a-tag color="green" v-if="record.hookstatus1 == 0">载荷正常</a-tag>
          <a-tag color="red" v-if="record.hookstatus1 == 1">载荷预警</a-tag>
        </span>
        <span slot="tags1" slot-scope="tags1, record">
          <a-tag color="green" v-if="record.hookbrake1status1 == 0">制动开启</a-tag>
          <a-tag color="red" v-if="record.hookbrake1status1 == 1">制动关闭</a-tag>
        </span>
        <span slot="tags2" slot-scope="tags2, record">
          <a-tag color="green" v-if="record.hookbrake2status1 == 0">制动开启</a-tag>
          <a-tag color="red" v-if="record.hookbrake2status1 == 1">制动关闭</a-tag>
        </span>
        <span slot="tags3" slot-scope="tags3, record">
          <a-tag color="green" v-if="record.wireDrumstatus == 0">正常</a-tag>
          <a-tag color="red" v-if="record.wireDrumstatus == 1">预警</a-tag>
        </span>
        <span slot="tags4" slot-scope="tags4, record">
          <a-tag color="green" v-if="record.windAntiskidstatus == 0">正常</a-tag>
          <a-tag color="red" v-if="record.windAntiskidstatus == 1">预警</a-tag>
        </span>
        <span slot="tags5" slot-scope="tags5, record">
          <a-tag color="green" v-if="record.hookstatus2 == 0">载荷正常</a-tag>
          <a-tag color="red" v-if="record.hookstatus2 == 1">载荷预警</a-tag>
        </span>
        <span slot="tags6" slot-scope="tags6, record">
          <a-tag color="green" v-if="record.hookbrake1status2 == 0">制动开启</a-tag>
          <a-tag color="red" v-if="record.hookbrake1status2 == 1">制动关闭</a-tag>
        </span>
        <span slot="tags7" slot-scope="tags7, record">
          <a-tag color="green" v-if="record.hookbrake2status2 == 0">制动开启</a-tag>
          <a-tag color="red" v-if="record.hookbrake2status2 == 1">制动关闭</a-tag>
        </span>

        <span slot="action" slot-scope="text, record">
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <device-bridge-historydata-modal ref="modalForm" @ok="modalFormOk"></device-bridge-historydata-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'
  import { handertoken } from '@/mixins/getHanderToken'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceBridgeHistorydataModal from './modules/DeviceBridgeHistorydataModal'

  export default {
    name: 'DeviceBridgeHistorydataList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      DeviceBridgeHistorydataModal
    },
    data () {
      return {
        description: 'device_bridge_historydata管理页面',
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
            title:'设备名称',
            align:"center",
            dataIndex: 'deviceCode_dictText'
          },
          // {
          //   title:'设备类型',
          //   align:"center",
          //   dataIndex: 'deviceType'
          // },
          {
            title:'大车横向行程(m)',
            align:"center",
            dataIndex: 'bigTransverseCarroute'
          },
          {
            title:'大车纵向行程(m)',
            align:"center",
            dataIndex: 'bigLongitudinalCarroute'
          },
          {
            title:'上传时间',
            align:"center",
            dataIndex: 'bridgedate',
            customRender:function (text) {
              return !text?"":(text.length>10?text:text)
            }
          },
          {
            title:'支腿垂直度',
            align:"center",
            dataIndex: 'verticalityLeg'
          },
          {
            title:'大车水平度',
            align:"center",
            dataIndex: 'carLevelness'
          },
          {
            title:'风速(m/s)',
            align:"center",
            dataIndex: 'windSpeed'
          },
          {
            title:'1#天车高度',
            align:"center",
            dataIndex: 'craneHeight1'
          },
          {
            title:'1#天车吊重',
            align:"center",
            dataIndex: 'craneCrane1'
          },
          {
            title:'1#天车横向行程',
            align:"center",
            dataIndex: 'transverseCarroute1'
          },
          {
            title:'1#天车纵向行程',
            align:"center",
            dataIndex: 'longitudinalCarroute1'
          },
          {
            title:'1#天车吊钩载荷状态',
            align:"center",
            dataIndex: 'hookstatus1',
            scopedSlots: { customRender: 'tags' }
          },
          {
            title:'1#天车吊钩制动器1状态',
            align:"center",
            dataIndex: 'hookbrake1status1',
            scopedSlots: { customRender: 'tags1' }
          },
          {
            title:'1#天车吊钩制动器2状态',
            align:"center",
            dataIndex: 'hookbrake2status1',
            scopedSlots: { customRender: 'tags2' }
          },
          {
            title:'1#天车左限位',
            align:"center",
            dataIndex: 'carleftlimit1'
          },
          {
            title:'1#天车右限位',
            align:"center",
            dataIndex: 'carrightlimit1'
          },
          {
            title:'1#天车前限位',
            align:"center",
            dataIndex: 'carfrontlimit1'
          },
          {
            title:'1#天车后限位',
            align:"center",
            dataIndex: 'carbacklimit1'
          },
          {
            title:'门限位',
            align:"center",
            dataIndex: 'doorLimit'
          },
          {
            title:'抗风防滑状态',
            align:"center",
            dataIndex: 'windAntiskidstatus',
            scopedSlots: { customRender: 'tags3' }
          },
          {
            title:'电缆卷筒状态',
            align:"center",
            dataIndex: 'wireDrumstatus',
            scopedSlots: { customRender: 'tags4' }
          },
          {
            title:'累计时间',
            align:"center",
            dataIndex: 'allTime'
          },
          {
            title:'循环使用次数',
            align:"center",
            dataIndex: 'allTimes'
          },
          {
            title:'预留1',
            align:"center",
            dataIndex: 'reservedOne'
          },
          {
            title:'预留2',
            align:"center",
            dataIndex: 'reservedTwo'
          },
          {
            title:'2#天车高度',
            align:"center",
            dataIndex: 'craneHeight2'
          },
          {
            title:'2#天车吊重',
            align:"center",
            dataIndex: 'craneCrane2'
          },
          {
            title:'2#天车横向行程',
            align:"center",
            dataIndex: 'transverseCarroute2'
          },
          {
            title:'2#天车纵向行程',
            align:"center",
            dataIndex: 'longitudinalCarroute2'
          },
          {
            title:'2#天车吊钩载荷状态',
            align:"center",
            dataIndex: 'hookstatus2',
            scopedSlots: { customRender: 'tags5' }
          },
          {
            title:'2#天车吊钩制动器1状态',
            align:"center",
            dataIndex: 'hookbrake1status2',
            scopedSlots: { customRender: 'tags6' }
          },
          {
            title:'2#天车吊钩制动器2状态',
            align:"center",
            dataIndex: 'hookbrake2status2',
            scopedSlots: { customRender: 'tags7' }
          },
          {
            title:'2#天车左限位',
            align:"center",
            dataIndex: 'carleftlimit2'
          },
          {
            title:'2#天车右限位',
            align:"center",
            dataIndex: 'carrightlimit2'
          },
          {
            title:'2#天车前限位',
            align:"center",
            dataIndex: 'carfrontlimit2'
          },
          {
            title:'2#天车后限位',
            align:"center",
            dataIndex: 'carbacklimit2'
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
          list: "/jiaqiaoji/deviceBridgeHistorydata/list",
          delete: "/jiaqiaoji/deviceBridgeHistorydata/delete",
          deleteBatch: "/jiaqiaoji/deviceBridgeHistorydata/deleteBatch",
          exportXlsUrl: "/jiaqiaoji/deviceBridgeHistorydata/exportXls",
          importExcelUrl: "jiaqiaoji/deviceBridgeHistorydata/importExcel",

        },
        selectValue: '',
        dictOption: [],
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
      this.shebeiList();
      this.getToken();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'23'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'deviceCode',text:'设备编号'})
        fieldList.push({type:'int',value:'deviceType',text:'设备类型2桥门机3架桥机'})
        fieldList.push({type:'number',value:'bigTransverseCarroute',text:'大车横向行程'})
        fieldList.push({type:'date',value:'bridgedate',text:'数据上传时间'})
        fieldList.push({type:'number',value:'bigLongitudinalCarroute',text:'大车纵向行程'})
        fieldList.push({type:'number',value:'verticalityLeg',text:'支腿垂直度'})
        fieldList.push({type:'number',value:'carLevelness',text:'大车水平度'})
        fieldList.push({type:'number',value:'craneHeight1',text:'1#天车高度'})
        fieldList.push({type:'number',value:'craneCrane1',text:'1#天车吊重'})
        fieldList.push({type:'number',value:'transverseCarroute1',text:'1#天车横向行程'})
        fieldList.push({type:'number',value:'longitudinalCarroute1',text:'1#天车纵向行程'})
        fieldList.push({type:'int',value:'hookstatus1',text:'1#天车吊钩载荷状态'})
        fieldList.push({type:'int',value:'hookbrake1status1',text:'1#天车吊钩制动器1状态'})
        fieldList.push({type:'int',value:'hookbrake2status1',text:'1#天车吊钩制动器2状态'})
        fieldList.push({type:'int',value:'carleftlimit1',text:'1#天车左限位'})
        fieldList.push({type:'int',value:'carrightlimit1',text:'1#天车右限位'})
        fieldList.push({type:'int',value:'carfrontlimit1',text:'1#天车前限位'})
        fieldList.push({type:'int',value:'carbacklimit1',text:'1#天车后限位'})
        fieldList.push({type:'int',value:'doorLimit',text:'门限位'})
        fieldList.push({type:'int',value:'windAntiskidstatus',text:'抗风防滑状态'})
        fieldList.push({type:'int',value:'wireDrumstatus',text:'电缆卷筒状态'})
        fieldList.push({type:'number',value:'allTime',text:'累计时间'})
        fieldList.push({type:'int',value:'allTimes',text:'循环使用次数'})
        fieldList.push({type:'int',value:'reservedOne',text:'预留'})
        fieldList.push({type:'int',value:'reservedTwo',text:'预留'})
        fieldList.push({type:'number',value:'craneHeight2',text:'2#天车高度'})
        fieldList.push({type:'number',value:'craneCrane2',text:'2#天车吊重'})
        fieldList.push({type:'number',value:'transverseCarroute2',text:'2#天车横向行程'})
        fieldList.push({type:'number',value:'longitudinalCarroute2',text:'2#天车纵向行程'})
        fieldList.push({type:'int',value:'hookstatus2',text:'2#天车吊钩载荷状态'})
        fieldList.push({type:'int',value:'hookbrake1status2',text:'2#天车吊钩制动器1状态'})
        fieldList.push({type:'int',value:'hookbrake2status2',text:'2#天车吊钩制动器2状态'})
        fieldList.push({type:'int',value:'carleftlimit2',text:'2#天车左限位'})
        fieldList.push({type:'int',value:'carrightlimit2',text:'2#天车右限位'})
        fieldList.push({type:'int',value:'carfrontlimit2',text:'2#天车前限位'})
        fieldList.push({type:'int',value:'carbacklimit2',text:'2#天车后限位'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>