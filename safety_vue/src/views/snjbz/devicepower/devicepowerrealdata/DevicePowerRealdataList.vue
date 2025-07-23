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
<!--    <div class="table-operator">-->
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
<!--      <a-button type="primary" icon="download" @click="handleExportXls('device_power_realdata')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
<!--      &lt;!&ndash; 高级查询区域 &ndash;&gt;-->
<!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
<!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
<!--        <a-menu slot="overlay">-->
<!--          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
<!--        </a-menu>-->
<!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
<!--      </a-dropdown>-->
<!--    </div>-->

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

    <device-power-realdata-modal ref="modalForm" @ok="modalFormOk"></device-power-realdata-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DevicePowerRealdataModal from './modules/DevicePowerRealdataModal'

  export default {
    name: 'DevicePowerRealdataList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DevicePowerRealdataModal
    },
    data () {
      return {
        description: 'device_power_realdata管理页面',
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
            dataIndex: 'imei_dictText'
          },
          {
            title:'数据上传时间',
            align:"center",
            dataIndex: 'powerdate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'设备状态',
            align:"center",
            dataIndex: 'devicestatus_dictText'
          },
          {
            title:'信号强度',
            align:"center",
            dataIndex: 'signal1'
          },
          {
            title:'通道1漏电mA',
            align:"center",
            dataIndex: 't1'
          },
          {
            title:'通道2A相温度℃',
            align:"center",
            dataIndex: 't2'
          },
          {
            title:'通道B相温度℃',
            align:"center",
            dataIndex: 't3'
          },
          {
            title:'通道4C相温度℃',
            align:"center",
            dataIndex: 't4'
          },
          {
            title:'通道5N相温度℃',
            align:"center",
            dataIndex: 't5'
          },
          {
            title:'通道6A相电流A',
            align:"center",
            dataIndex: 't6'
          },
          {
            title:'通道7B相电流A',
            align:"center",
            dataIndex: 't7'
          },
          {
            title:'通道8C相电流A',
            align:"center",
            dataIndex: 't8'
          },
          {
            title:'通道9A相电压V',
            align:"center",
            dataIndex: 't9'
          },
          {
            title:'通道10B相电压V',
            align:"center",
            dataIndex: 't10'
          },
          {
            title:'通道11C相电压V',
            align:"center",
            dataIndex: 't11'
          },
          {
            title:'通道12A相频率Hz',
            align:"center",
            dataIndex: 't12'
          },
          {
            title:'通道13B相频率Hz',
            align:"center",
            dataIndex: 't13'
          },
          {
            title:'通道14C相频率Hz',
            align:"center",
            dataIndex: 't14'
          },
          {
            title:'通道15A相电压相位角°',
            align:"center",
            dataIndex: 't15'
          },
          {
            title:'通道16B相电压相位角°',
            align:"center",
            dataIndex: 't16'
          },
          {
            title:'通道17C相电压相位角°',
            align:"center",
            dataIndex: 't17'
          },
          {
            title:'通道18A相电流相位角°',
            align:"center",
            dataIndex: 't18'
          },
          {
            title:'通道19B相电流相位角°',
            align:"center",
            dataIndex: 't19'
          },
          {
            title:'通道20C相电流相位角°',
            align:"center",
            dataIndex: 't20'
          },
          {
            title:'通道21A相功率因素%',
            align:"center",
            dataIndex: 't21'
          },
          {
            title:'通道22B相功率因素%',
            align:"center",
            dataIndex: 't22'
          },
          {
            title:'通道23C相功率因素%',
            align:"center",
            dataIndex: 't23'
          },
          {
            title:'通道24合相功率因素%',
            align:"center",
            dataIndex: 't24'
          },
          {
            title:'通道25A相有功功率KW',
            align:"center",
            dataIndex: 't25'
          },
          {
            title:'通道26B相有功功率KW',
            align:"center",
            dataIndex: 't26'
          },
          {
            title:'通道27C相有功功率KW',
            align:"center",
            dataIndex: 't27'
          },
          {
            title:'通道28合相有功功率KW',
            align:"center",
            dataIndex: 't28'
          },
          {
            title:'通道29A相无功功率KVar',
            align:"center",
            dataIndex: 't29'
          },
          {
            title:'通道30B相无功功率KVar',
            align:"center",
            dataIndex: 't30'
          },
          {
            title:'通道31C相无功功率KVar',
            align:"center",
            dataIndex: 't31'
          },
          {
            title:'通道32合相无功功率KVar',
            align:"center",
            dataIndex: 't32'
          },
          {
            title:'通道33A相视在功率KVA',
            align:"center",
            dataIndex: 't33'
          },
          {
            title:'通道34B相视在功率KVA',
            align:"center",
            dataIndex: 't34'
          },
          {
            title:'通道35C相视在功率KVA',
            align:"center",
            dataIndex: 't35'
          },
          {
            title:'通道36合相视在功率KVA',
            align:"center",
            dataIndex: 't36'
          },
          {
            title:'通道37A相有功电能高位W KWH',
            align:"center",
            dataIndex: 't37'
          },
          {
            title:'通道37A相有功电能低位KWH',
            align:"center",
            dataIndex: 't38'
          },
          {
            title:'通道39B相有功电能高位W KWH',
            align:"center",
            dataIndex: 't39'
          },
          {
            title:'通道40B相有功电能低位KWH',
            align:"center",
            dataIndex: 't40'
          },
          {
            title:'通道41C相有功电能高位W KWH',
            align:"center",
            dataIndex: 't41'
          },
          {
            title:'通道42C相有功电能低位KWH',
            align:"center",
            dataIndex: 't42'
          },
          {
            title:'通道43合相有功电能高位W KWH',
            align:"center",
            dataIndex: 't43'
          },
          {
            title:'通道44合相有功电能低位KWH',
            align:"center",
            dataIndex: 't44'
          },
          {
            title:'通道45A相无功电能高位W KVarH',
            align:"center",
            dataIndex: 't45'
          },
          {
            title:'通道46A相无功电能低位KVarH',
            align:"center",
            dataIndex: 't46'
          },
          {
            title:'通道47B相无功电能高位W KVarH',
            align:"center",
            dataIndex: 't47'
          },
          {
            title:'通道48B相无功电能低位KVarH',
            align:"center",
            dataIndex: 't48'
          },
          {
            title:'通道49C相无功电能高位W KVarH',
            align:"center",
            dataIndex: 't49'
          },
          {
            title:'通道50C相无功电能低位KVarH',
            align:"center",
            dataIndex: 't50'
          },
          {
            title:'通道51合相无功电能高位W KVarH',
            align:"center",
            dataIndex: 't51'
          },
          {
            title:'通道52合相无功电能低位KVarH',
            align:"center",
            dataIndex: 't52'
          },
          {
            title:'通道53A相视在电能高位W KVAH',
            align:"center",
            dataIndex: 't53'
          },
          {
            title:'通道54A相视在电能低位KVAH',
            align:"center",
            dataIndex: 't54'
          },
          {
            title:'通道55B相视在电能高位W KVAH',
            align:"center",
            dataIndex: 't55'
          },
          {
            title:'通道56B相视在电能低位KVAH',
            align:"center",
            dataIndex: 't56'
          },
          {
            title:'通道57C相视在电能高位W KVAH',
            align:"center",
            dataIndex: 't57'
          },
          {
            title:'通道58C相视在电能低位KVAH',
            align:"center",
            dataIndex: 't58'
          },
          {
            title:'通道59合相视在电能高位W KVAH',
            align:"center",
            dataIndex: 't59'
          },
          {
            title:'通道60合相视在电能低位KVAH',
            align:"center",
            dataIndex: 't60'
          },
          {
            title:'通道61三相电流平衡度%',
            align:"center",
            dataIndex: 't61'
          },
          {
            title:' 通道62三相电压平衡度%',
            align:"center",
            dataIndex: 't62'
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
          list: "/devicepowerrealdata/devicePowerRealdata/list",
          delete: "/devicepowerrealdata/devicePowerRealdata/delete",
          deleteBatch: "/devicepowerrealdata/devicePowerRealdata/deleteBatch",
          exportXlsUrl: "/devicepowerrealdata/devicePowerRealdata/exportXls",
          importExcelUrl: "devicepowerrealdata/devicePowerRealdata/importExcel",
          
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
        fieldList.push({type:'string',value:'imei',text:'设备id'})
        fieldList.push({type:'date',value:'powerdate',text:'数据上传时间'})
        fieldList.push({type:'int',value:'devicestatus',text:'设备状态'})
        fieldList.push({type:'int',value:'signal',text:'信号强度'})
        fieldList.push({type:'number',value:'t1',text:'通道1漏电mA'})
        fieldList.push({type:'number',value:'t2',text:'通道2A相温度℃'})
        fieldList.push({type:'number',value:'t3',text:'通道B相温度℃'})
        fieldList.push({type:'number',value:'t4',text:'通道4C相温度℃'})
        fieldList.push({type:'number',value:'t5',text:'通道5N相温度℃'})
        fieldList.push({type:'number',value:'t6',text:'通道6A相电流A'})
        fieldList.push({type:'number',value:'t7',text:'通道7B相电流A'})
        fieldList.push({type:'number',value:'t8',text:'通道8C相电流A'})
        fieldList.push({type:'number',value:'t9',text:'通道9A相电压V'})
        fieldList.push({type:'number',value:'t10',text:'通道10B相电压V'})
        fieldList.push({type:'number',value:'t11',text:'通道11C相电压V'})
        fieldList.push({type:'number',value:'t12',text:'通道12A相频率Hz'})
        fieldList.push({type:'number',value:'t13',text:'通道13B相频率Hz'})
        fieldList.push({type:'number',value:'t14',text:'通道14C相频率Hz'})
        fieldList.push({type:'number',value:'t15',text:'通道15A相电压相位角°'})
        fieldList.push({type:'number',value:'t16',text:'通道16B相电压相位角°'})
        fieldList.push({type:'number',value:'t17',text:'通道17C相电压相位角°'})
        fieldList.push({type:'number',value:'t18',text:'通道18A相电流相位角°'})
        fieldList.push({type:'number',value:'t19',text:'通道19B相电流相位角°'})
        fieldList.push({type:'number',value:'t20',text:'通道20C相电流相位角°'})
        fieldList.push({type:'number',value:'t21',text:'通道21A相功率因素%'})
        fieldList.push({type:'number',value:'t22',text:'通道22B相功率因素%'})
        fieldList.push({type:'number',value:'t23',text:'通道23C相功率因素%'})
        fieldList.push({type:'number',value:'t24',text:'通道24合相功率因素%'})
        fieldList.push({type:'number',value:'t25',text:'通道25A相有功功率KW'})
        fieldList.push({type:'number',value:'t26',text:'通道26B相有功功率KW'})
        fieldList.push({type:'number',value:'t27',text:'通道27C相有功功率KW'})
        fieldList.push({type:'number',value:'t28',text:'通道28合相有功功率KW'})
        fieldList.push({type:'number',value:'t29',text:'通道29A相无功功率KVar'})
        fieldList.push({type:'number',value:'t30',text:'通道30B相无功功率KVar'})
        fieldList.push({type:'number',value:'t31',text:'通道31C相无功功率KVar'})
        fieldList.push({type:'number',value:'t32',text:'通道32合相无功功率KVar'})
        fieldList.push({type:'number',value:'t33',text:'通道33A相视在功率KVA'})
        fieldList.push({type:'number',value:'t34',text:'通道34B相视在功率KVA'})
        fieldList.push({type:'number',value:'t35',text:'通道35C相视在功率KVA'})
        fieldList.push({type:'number',value:'t36',text:'通道36合相视在功率KVA'})
        fieldList.push({type:'number',value:'t37',text:'通道37A相有功电能高位W KWH'})
        fieldList.push({type:'number',value:'t38',text:'通道37A相有功电能低位KWH'})
        fieldList.push({type:'number',value:'t39',text:'通道39B相有功电能高位W KWH'})
        fieldList.push({type:'number',value:'t40',text:'通道40B相有功电能低位KWH'})
        fieldList.push({type:'number',value:'t41',text:'通道41C相有功电能高位W KWH'})
        fieldList.push({type:'number',value:'t42',text:'通道42C相有功电能低位KWH'})
        fieldList.push({type:'number',value:'t43',text:'通道43合相有功电能高位W KWH'})
        fieldList.push({type:'number',value:'t44',text:'通道44合相有功电能低位KWH'})
        fieldList.push({type:'number',value:'t45',text:'通道45A相无功电能高位W KVarH'})
        fieldList.push({type:'number',value:'t46',text:'通道46A相无功电能低位KVarH'})
        fieldList.push({type:'number',value:'t47',text:'通道47B相无功电能高位W KVarH'})
        fieldList.push({type:'number',value:'t48',text:'通道48B相无功电能低位KVarH'})
        fieldList.push({type:'number',value:'t49',text:'通道49C相无功电能高位W KVarH'})
        fieldList.push({type:'number',value:'t50',text:'通道50C相无功电能低位KVarH'})
        fieldList.push({type:'number',value:'t51',text:'通道51合相无功电能高位W KVarH'})
        fieldList.push({type:'number',value:'t52',text:'通道52合相无功电能低位KVarH'})
        fieldList.push({type:'number',value:'t53',text:'通道53A相视在电能高位W KVAH'})
        fieldList.push({type:'number',value:'t54',text:'通道54A相视在电能低位KVAH'})
        fieldList.push({type:'number',value:'t55',text:'通道55B相视在电能高位W KVAH'})
        fieldList.push({type:'number',value:'t56',text:'通道56B相视在电能低位KVAH'})
        fieldList.push({type:'number',value:'t57',text:'通道57C相视在电能高位W KVAH'})
        fieldList.push({type:'number',value:'t58',text:'通道58C相视在电能低位KVAH'})
        fieldList.push({type:'number',value:'t59',text:'通道59合相视在电能高位W KVAH'})
        fieldList.push({type:'number',value:'t60',text:'通道60合相视在电能低位KVAH'})
        fieldList.push({type:'number',value:'t61',text:'通道61三相电流平衡度%'})
        fieldList.push({type:'number',value:'t62',text:' 通道62三相电压平衡度%'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>