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
            <a-form-item label="主钩载荷状态">
              <j-dict-select-tag placeholder="请选择主钩载荷状态" v-model="queryParam.mainHookstatus" dictCode="windspeedwarn"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="预留副钩载荷状态">
              <j-dict-select-tag placeholder="请选择预留副钩载荷状态" v-model="queryParam.reservedVicehookstatus" dictCode="windspeedwarn"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="风速报警">
              <j-dict-select-tag placeholder="请选择风速报警" v-model="queryParam.windSpeedalarm" dictCode="windspeedwarn"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="支腿偏差报警">
              <j-dict-select-tag placeholder="请选择支腿偏差报警" v-model="queryParam.bigcarAlm" dictCode="windspeedwarn"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="上报时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.cranedate_begin" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.cranedate_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button type="primary" @click="handle" icon="profile" style="margin-left: 8px">处置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" v-has="'lmd:add'" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'lmd:dc'" icon="download" @click="handleExportXls('龙门吊表信息')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'lmd:dr'" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
<!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
<!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
<!--        <a-menu slot="overlay">-->
<!--          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
<!--        </a-menu>-->
<!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
<!--      </a-dropdown>-->
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

        <span slot="reservedVicehookstatus" slot-scope="reservedVicehookstatus, record">
        <a-tag color="green" v-if="record.reservedVicehookstatus === 0">正常</a-tag>
        <a-tag color="red" v-if="record.reservedVicehookstatus === 1">预警</a-tag>
       </span>

        <span slot="windSpeedalarm" slot-scope="windSpeedalarm, record">
        <a-tag color="green" v-if="record.windSpeedalarm === 0">正常</a-tag>
        <a-tag color="red" v-if="record.windSpeedalarm === 1">预警</a-tag>
       </span>

        <span slot="mainHookstatus" slot-scope="mainHookstatus, record">
        <a-tag color="green" v-if="record.mainHookstatus === 0">正常</a-tag>
        <a-tag color="red" v-if="record.mainHookstatus === 1">预警</a-tag>
       </span>

        <span slot="bigcarAlm" slot-scope="bigcarAlm, record">
        <a-tag color="green" v-if="record.bigcarAlm === 0">正常</a-tag>
        <a-tag color="red" v-if="record.bigcarAlm === 1">预警</a-tag>
       </span>

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
          <a v-has="'lmd:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'lmd:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <device-crane-historydata-modal ref="modalForm" @ok="modalFormOk"></device-crane-historydata-modal>
    <chuzhi ref="modalChuZhi" :id="chuzhiId" :shebeiNo="shebeiNo" ></chuzhi>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import chuzhi from './modules/chuzhi'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceCraneHistorydataModal from './modules/DeviceCraneHistorydataModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'
  import { handertoken } from '@/mixins/getHanderToken'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  export default {
    name: 'DeviceCraneHistorydataList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      DeviceCraneHistorydataModal,
      JSuperQuery,
      chuzhi
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        chuzhiId: '',
        shebeiNo: '',
        dictOption: [],
        description: '龙门吊表信息管理页面',
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
          //   dataIndex: 'deviceType_dictText'
          // },
          {
            title:'大车行程',
            align:"center",
            dataIndex: 'bigCarroute'
          },
          {
            title:'数据上传时间',
            align:"center",
            dataIndex: 'cranedate',
          },
          {
            title:'主钩高度',
            align:"center",
            dataIndex: 'mainHookheight'
          },
          {
            title:'预留副钩高度',
            align:"center",
            dataIndex: 'reservedVicehookheight'
          },
          {
            title:'主钩吊重',
            align:"center",
            dataIndex: 'mainHookload'
          },
          {
            title:'预留副钩吊重',
            align:"center",
            dataIndex: 'reservedVicehookload'
          },
          {
            title:'小车行程',
            align:"center",
            dataIndex: 'smallCarroute'
          },
          {
            title:'预留小车2行程',
            align:"center",
            dataIndex: 'reservedSmallcarroute'
          },
          {
            title:'风速',
            align:"center",
            dataIndex: 'windSpeed'
          },
          {
            title:'预留模拟量',
            align:"center",
            dataIndex: 'reservedAnalogquantityone'
          },
          {
            title:'预留模拟量',
            align:"center",
            dataIndex: 'reservedAnalogquantitytwo'
          },
          {
            title:'主钩载荷状态',
            align:"center",
            dataIndex: 'mainHookstatus',
            scopedSlots: { customRender: 'mainHookstatus' }
          },
          {
            title:'预留副钩载荷状态',
            align:"center",
            dataIndex: 'reservedVicehookstatus',
            scopedSlots: { customRender: 'reservedVicehookstatus' }

          },
          {
            title:'风速报警',
            align:"center",
            dataIndex: 'windSpeedalarm',
            scopedSlots: { customRender: 'windSpeedalarm' }
          },
          {
            title:'大车左限位',
            align:"center",
            dataIndex: 'bigCarleftlimit'
          },
          {
            title:'大车右限位',
            align:"center",
            dataIndex: 'bigCarrightlimit'
          },
          {
            title:'小车前限位',
            align:"center",
            dataIndex: 'smallCarfrontlimit'
          },
          {
            title:'小车后限位',
            align:"center",
            dataIndex: 'smallCarbacklimit'
          },
          {
            title:'预留小车2前限位',
            align:"center",
            dataIndex: 'reservedSmallcarfrontlimit'
          },
          {
            title:'预留小车2后限位',
            align:"center",
            dataIndex: 'reservedSmallcarbacklimit'
          },
          {
            title:'吊钩上限位',
            align:"center",
            dataIndex: 'hookUplimit'
          },
          {
            title:'预留副吊钩上限位',
            align:"center",
            dataIndex: 'reservedVicehookuplimit'
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
            scopedSlots: { customRender: 'windAntiskidstatus' }
          },
          {
            title:'电缆卷筒状态',
            align:"center",
            dataIndex: 'wireDrumstatus',
            scopedSlots: { customRender: 'wireDrumstatus' }
          },
          {
            title:'小车1制动器1',
            align:"center",
            dataIndex: 'smallCar1brake1'
          },
          {
            title:'小车1制动器2',
            align:"center",
            dataIndex: 'smallCar1brake2'
          },
          {
            title:'小车2制动器1',
            align:"center",
            dataIndex: 'smallCar2brake1'
          },
          {
            title:'小车2制动器2',
            align:"center",
            dataIndex: 'smallCar2brake2'
          },
          {
            title:'大车支腿偏差',
            align:"center",
            dataIndex: 'bigCarlegdeviation'
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
            title:'支腿偏差报警',
            align:"center",
            dataIndex: 'bigcarAlm',
            scopedSlots: { customRender: 'bigcarAlm' }
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
          list: "/lmd/deviceCraneHistorydata/list",
          delete: "/lmd/deviceCraneHistorydata/delete",
          deleteBatch: "/lmd/deviceCraneHistorydata/deleteBatch",
          exportXlsUrl: "/lmd/deviceCraneHistorydata/exportXls",
          importExcelUrl: "lmd/deviceCraneHistorydata/importExcel",

        },
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
          sbtypes:'21'
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
        fieldList.push({type:'string',value:'deviceCode',text:'设备编号',dictCode:''})
        fieldList.push({type:'int',value:'deviceType',text:'设备类型2桥门机3架桥机',dictCode:''})
        fieldList.push({type:'double',value:'bigCarroute',text:'大车行程',dictCode:''})
        fieldList.push({type:'date',value:'cranedate',text:'数据上传时间'})
        fieldList.push({type:'double',value:'mainHookheight',text:'主钩高度',dictCode:''})
        fieldList.push({type:'double',value:'reservedVicehookheight',text:'预留副钩高度',dictCode:''})
        fieldList.push({type:'double',value:'mainHookload',text:'主钩吊重',dictCode:''})
        fieldList.push({type:'double',value:'reservedVicehookload',text:'预留副钩吊重',dictCode:''})
        fieldList.push({type:'double',value:'smallCarroute',text:'小车行程',dictCode:''})
        fieldList.push({type:'double',value:'reservedSmallcarroute',text:'预留小车2行程',dictCode:''})
        fieldList.push({type:'double',value:'windSpeed',text:'风速',dictCode:''})
        fieldList.push({type:'double',value:'reservedAnalogquantityone',text:'预留模拟量',dictCode:''})
        fieldList.push({type:'double',value:'reservedAnalogquantitytwo',text:'预留模拟量',dictCode:''})
        fieldList.push({type:'int',value:'mainHookstatus',text:'主钩载荷状态',dictCode:''})
        fieldList.push({type:'int',value:'reservedVicehookstatus',text:'预留副钩载荷状态',dictCode:''})
        fieldList.push({type:'int',value:'windSpeedalarm',text:'风速报警',dictCode:''})
        fieldList.push({type:'int',value:'bigCarleftlimit',text:'大车左限位',dictCode:''})
        fieldList.push({type:'int',value:'bigCarrightlimit',text:'大车右限位',dictCode:''})
        fieldList.push({type:'int',value:'smallCarfrontlimit',text:'小车前限位',dictCode:''})
        fieldList.push({type:'int',value:'smallCarbacklimit',text:'小车后限位',dictCode:''})
        fieldList.push({type:'int',value:'reservedSmallcarfrontlimit',text:'预留小车2前限位',dictCode:''})
        fieldList.push({type:'int',value:'reservedSmallcarbacklimit',text:'预留小车2后限位',dictCode:''})
        fieldList.push({type:'int',value:'hookUplimit',text:'吊钩上限位',dictCode:''})
        fieldList.push({type:'int',value:'reservedVicehookuplimit',text:'预留副吊钩上限位',dictCode:''})
        fieldList.push({type:'int',value:'doorLimit',text:'门限位',dictCode:''})
        fieldList.push({type:'int',value:'windAntiskidstatus',text:'抗风防滑状态',dictCode:''})
        fieldList.push({type:'int',value:'wireDrumstatus',text:'电缆卷筒状态',dictCode:''})
        fieldList.push({type:'int',value:'smallCar1brake1',text:'小车1制动器1',dictCode:''})
        fieldList.push({type:'int',value:'smallCar1brake2',text:'小车1制动器2',dictCode:''})
        fieldList.push({type:'int',value:'smallCar2brake1',text:'小车2制动器1',dictCode:''})
        fieldList.push({type:'int',value:'smallCar2brake2',text:'小车2制动器2',dictCode:''})
        fieldList.push({type:'double',value:'bigCarlegdeviation',text:'大车支腿偏差',dictCode:''})
        fieldList.push({type:'double',value:'allTime',text:'累计时间',dictCode:''})
        fieldList.push({type:'int',value:'allTimes',text:'循环使用次数',dictCode:''})
        fieldList.push({type:'int',value:'bigcarAlm',text:'支腿偏差报警',dictCode:''})
        fieldList.push({type:'int',value:'reservedOne',text:'预留',dictCode:''})
        fieldList.push({type:'int',value:'reservedTwo',text:'预留',dictCode:''})
        this.superFieldList = fieldList
      },
      handle(){
        if(this.selectionRows.length){
          // console.log(this.selectionRows,"选中数据")
          let type = true
          this.selectionRows.forEach(item => {
            if(item.deviceCode != this.selectionRows[0].deviceCode){
              type = false
            }
          })
          if(!type){
            this.$message.warning("请选择相同设备！")
          }else{
            let strID = '';
            this.selectionRows.forEach(item => {
              strID += `${item.id},`
            });
            strID = strID.slice(0, -1)
            this.chuzhiId = strID
            this.shebeiNo = this.selectionRows[0].deviceCode
            this.$refs.modalChuZhi.counts = this.selectionRows.length
            this.$refs.modalChuZhi.visible = true
          }
        }else{
          this.$message.warning("请选择一行或多行数据！")
        }
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>