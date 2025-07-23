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
      <a-button @click="handleAdd" v-has="'mixpilereal:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'mixpilereal:dc'" icon="download" @click="handleExportXls('device_mixpile_realdata')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'mixpilereal:dr'" icon="import">导入</a-button>
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

        <span slot="worksta" slot-scope="worksta, record">
          <a-tag color="blue"  v-if="worksta=='0'">在线</a-tag>
          <a-tag color="green"  v-if="worksta=='1'">下钻</a-tag>
          <a-tag color="yellow"  v-if="worksta=='2'">提钻</a-tag>
          <a-tag color="red"  v-if="worksta=='3'">离线</a-tag>
        </span>

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
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <span slot="action" slot-scope="text, record">
          <a v-has="'mixpilereal:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'mixpilereal:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <device-mixpile-realdata-modal ref="modalForm" @ok="modalFormOk"></device-mixpile-realdata-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceMixpileRealdataModal from './modules/DeviceMixpileRealdataModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import Vue from 'vue'
  import { usershebeiList } from '@api/api'

  export default {
    name: 'DeviceMixpileRealdataList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceMixpileRealdataModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: 'device_mixpile_realdata管理页面',
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
            dataIndex: 'shebeino_dictText'
          },
          {
            title:'里程',
            align:"center",
            dataIndex: 'mileage'
          },
          {
            title:'设计深度',
            align:"center",
            dataIndex: 'designdep'
          },
          {
            title:'当前深度',
            align:"center",
            dataIndex: 'curdep',
            customRender:function (t,r,index) {
              return parseFloat(t).toFixed(2);
            }
          },
          {
            title:'x轴角度',
            align:"center",
            dataIndex: 'x',
            customRender:function (t,r,index) {
              return parseFloat(t).toFixed(3);
            }
          },
          {
            title:'y轴角度',
            align:"center",
            dataIndex: 'y',
            customRender:function (t,r,index) {
              return parseFloat(t).toFixed(3);
            }
          },
          {
            title:'当前电流',
            align:"center",
            dataIndex: 'elec'
          },
          {
            title:'已灌浆量',
            align:"center",
            dataIndex: 'grouting'
          },
          {
            title:'瞬时流量',
            align:"center",
            dataIndex: 'flowlst'
          },
          {
            title:'桩号',
            align:"center",
            dataIndex: 'pileno'
          },
          {
            title:'设计水灰比',
            align:"center",
            dataIndex: 'designratio'
          },
          {
            title:'设计灌浆量',
            align:"center",
            dataIndex: 'designgrout'
          },
          {
            title:'水泥量',
            align:"center",
            dataIndex: 'cement'
          },
          {
            title:'用水量',
            align:"center",
            dataIndex: 'water'
          },
          {
            title:'速度',
            align:"center",
            dataIndex: 'speed'
          },
          {
            title:'水灰比',
            align:"center",
            dataIndex: 'ratio'
          },
          {
            title:'压力',
            align:"center",
            dataIndex: 'press'
          },
          {
            title:'盘次',
            align:"center",
            dataIndex: 'serial'
          },
          {
            title:'操作员',
            align:"center",
            dataIndex: 'username'
          },
          {
            title:'数据时间',
            align:"center",
            dataIndex: 'datatime',
          },
          {
            title:'设备状态',
            align:"center",
            dataIndex: 'worksta',
            scopedSlots: { customRender: 'worksta' }
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
          list: "/devicemixpilerealdata/deviceMixpileRealdata/list",
          delete: "/devicemixpilerealdata/deviceMixpileRealdata/delete",
          deleteBatch: "/devicemixpilerealdata/deviceMixpileRealdata/deleteBatch",
          exportXlsUrl: "/devicemixpilerealdata/deviceMixpileRealdata/exportXls",
          importExcelUrl: "devicemixpilerealdata/deviceMixpileRealdata/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
      this.shebeiList();
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
          sbtypes:'16,19'
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
        fieldList.push({type:'string',value:'shebeino',text:'设备编号'})
        fieldList.push({type:'string',value:'designdep',text:'设计深度'})
        fieldList.push({type:'string',value:'curdep',text:'当前深度'})
        fieldList.push({type:'string',value:'x',text:'x轴角度'})
        fieldList.push({type:'string',value:'y',text:'y轴角度'})
        fieldList.push({type:'string',value:'elec',text:'当前电流'})
        fieldList.push({type:'string',value:'grouting',text:'已灌浆量'})
        fieldList.push({type:'string',value:'pileno',text:'桩号'})
        fieldList.push({type:'string',value:'designratio',text:'设计水灰比'})
        fieldList.push({type:'string',value:'designgrout',text:'设计灌浆量'})
        fieldList.push({type:'string',value:'cement',text:'水泥量'})
        fieldList.push({type:'string',value:'water',text:'用水量'})
        fieldList.push({type:'string',value:'ratio',text:'水灰比'})
        fieldList.push({type:'string',value:'serial',text:'盘次'})
        fieldList.push({type:'string',value:'username',text:'操作员'})
        fieldList.push({type:'date',value:'datatime',text:'数据时间'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>