<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeino" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="报警类型">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.alarmtype" dictCode="alarmtype"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.datatime_begin" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.datatime_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
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
      <a-button @click="handleAdd" type="primary" v-has="'sdhj:add'" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'sdhj:dc'" icon="download" @click="handleExportXls('隧道环境（有害气体）监测主表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'sdhj:dr'" icon="import">导入</a-button>
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

        <span slot="tags" slot-scope="tags, record">
          <a-tag color="green" v-if="record.alarmtype == '0'">无报警</a-tag>
          <a-tag color="orange" v-if="record.alarmtype == '1'">低报警</a-tag>
          <a-tag color="red" v-if="record.alarmtype == '2'">高报警</a-tag>
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
          <a v-has="'sdhj:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'sdhj:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <device-tunnel-environmentdata-modal ref="modalForm" @ok="modalFormOk"></device-tunnel-environmentdata-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceTunnelEnvironmentdataModal from './modules/DeviceTunnelEnvironmentdataModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { usershebeiList } from '@api/api'
  import { handertoken } from '@/mixins/getHanderToken'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import Vue from 'vue'

  export default {
    name: 'DeviceTunnelEnvironmentdataList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      DeviceTunnelEnvironmentdataModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '隧道环境（有害气体）监测主表管理页面',
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
            title:'地点',
            align:"center",
            dataIndex: 'site'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'shebeino_dictText'
          },
          {
            title:'数据上传时间',
            align:"center",
            dataIndex: 'datatime',
          },
          {
            title:'报警类型',
            align:"center",
            dataIndex: 'alarmtype',
            scopedSlots: { customRender: 'tags' },
          },
          {
            title:'氧气',
            align:"center",
            dataIndex: 'oxygen'
          },
          {
            title:'甲烷',
            align:"center",
            dataIndex: 'methane'
          },
          {
            title:'硫化氢',
            align:"center",
            dataIndex: 'hsulfide'
          },
          {
            title:'二氧化碳',
            align:"center",
            dataIndex: 'cdioxide'
          },
          {
            title:'一氧化碳',
            align:"center",
            dataIndex: 'cmonoxide'
          },
          {
            title:'风速',
            align:"center",
            dataIndex: 'windspeed'
          },
          {
            title:'温度',
            align:"center",
            dataIndex: 'temperature'
          },
          {
            title:'湿度',
            align:"center",
            dataIndex: 'humidity'
          },
          // {
          //   title:'粉尘',
          //   align:"center",
          //   dataIndex: 'dust'
          // },
          {
            title:'噪声',
            align:"center",
            dataIndex: 'noise'
          },
          {
            title:'PM1.0',
            align:"center",
            dataIndex: 'pm1'
          },
          {
            title:'PM2.5',
            align:"center",
            dataIndex: 'pm2'
          },
          {
            title:'PM10',
            align:"center",
            dataIndex: 'pm10'
          },
          // {
          //   title:'数据时间',
          //   align:"center",
          //   dataIndex: 'uploadtime',
          // },
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
          list: "/devicetunnelenvironmentdata/deviceTunnelEnvironmentdata/list",
          delete: "/devicetunnelenvironmentdata/deviceTunnelEnvironmentdata/delete",
          deleteBatch: "/devicetunnelenvironmentdata/deviceTunnelEnvironmentdata/deleteBatch",
          exportXlsUrl: "/devicetunnelenvironmentdata/deviceTunnelEnvironmentdata/exportXls",
          importExcelUrl: "devicetunnelenvironmentdata/deviceTunnelEnvironmentdata/importExcel",

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
          sbtypes:'20'
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
        fieldList.push({type:'string',value:'site',text:'地点'})
        fieldList.push({type:'string',value:'shebeino',text:'设备编号'})
        fieldList.push({type:'date',value:'datatime',text:'数据上传时间'})
        fieldList.push({type:'int',value:'alarmtype',text:'0无报警，1低报警，2高报警'})
        fieldList.push({type:'string',value:'oxygen',text:'氧气'})
        fieldList.push({type:'string',value:'methane',text:'甲烷'})
        fieldList.push({type:'string',value:'hSulfide',text:'硫化氢'})
        fieldList.push({type:'string',value:'cDioxide',text:'二氧化碳'})
        fieldList.push({type:'string',value:'cMonoxide',text:'一氧化碳'})
        fieldList.push({type:'date',value:'uploadtime',text:'数据时间'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
