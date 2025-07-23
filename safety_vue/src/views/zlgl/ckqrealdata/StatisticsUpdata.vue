<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="标段号">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.bidCode" dictCode="ckqbd"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="搅拌站">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.mixinStationCode" dictCode="ckqz"></j-dict-select-tag>
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
      <a-button @click="handleAdd" v-has="'ckqreal:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'ckqreal:dc'" icon="download" @click="handleExportXls('采空区实时表信息')">导出</a-button>
      <a-upload name="file" v-has="'ckqreal:dr'" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
    </div>
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
        <span slot="action"  slot-scope="text, record">
          <a @click="handleOk(record)">保存</a>
        </span>

      </a-table>
    </div>

    <device-traffic-realdata-modal ref="modalForm" @ok="modalFormOk"></device-traffic-realdata-modal>
  </a-card>
</template>

<script>
  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceTrafficRealdataModal from './modules/DeviceTrafficRealdataModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import Vue from 'vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList } from '@api/api'
  import { getAction, postAction } from '../../../api/manage'
  export default {
    name: 'StatisticsUpdata',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceTrafficRealdataModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '数据统计更新记录',
        plsczl:'0',
        zjzl:'0',
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'标段号',
            align:"center",
            dataIndex: 'bidCode',
            key: 'bidCode',
          },
          {
            title:'搅拌站号',
            align:"center",
            dataIndex: 'mixinStationCode',
            key: 'mixinStationCode',
          },
          {
            title:'修改配料生产量(kg)',
            align:"center",
            dataIndex: 'batchingProductionUpdate',
          },
          {
            title:'修改注浆量(m³)',
            align:"center",
            dataIndex: 'groutingTotalUpdate',
          },
          {
            title:'更新时间',
            align:"center",
            dataIndex: 'updateTime'
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
          list: "/mixin/station/update/log/selectMixinStationUpdateLogPage",
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
      handleOk(records){
        console.log(records)
        let params = {
          id: records.id,
          batchingProductionUpdate :records.plsczl,
          groutingTotalUpdate : records.zjzl
        }
        getAction(this.url.sjtjedit,params).then(res=>{
          console.log(res)
        })
      },
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'36,37'
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
        fieldList.push({type:'string',value:'bidCode',text:'标段号'})
        fieldList.push({type:'string',value:'mixinStationCode',text:'搅拌站号'})
        fieldList.push({type:'number',value:'batchingProductionTotal',text:'配料生产总量'})
        fieldList.push({type:'integer',value:'groutingWellsNumber',text:'已注浆井数量'})
        fieldList.push({type:'number',value:'groutingTotal',text:'注浆总量'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>