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
<!--    <div class="table-operator">-->
<!--      <a-button @click="handleAdd" v-has="'pipepilehistory:add'" type="primary" icon="plus">新增</a-button>-->
<!--      <a-button type="primary" icon="download" v-has="'pipepilehistory:dc'" @click="handleExportXls('device_mixpile_historydata')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" v-has="'pipepilehistory:dr'" icon="import">导入</a-button>-->
<!--      </a-upload>-->
<!--      &lt;!&ndash; 高级查询区域 &ndash;&gt;-->
<!--&lt;!&ndash;      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>&ndash;&gt;-->
<!--&lt;!&ndash;      <a-dropdown v-if="selectedRowKeys.length > 0">&ndash;&gt;-->
<!--&lt;!&ndash;        <a-menu slot="overlay">&ndash;&gt;-->
<!--&lt;!&ndash;          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>&ndash;&gt;-->
<!--&lt;!&ndash;        </a-menu>&ndash;&gt;-->
<!--&lt;!&ndash;        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>&ndash;&gt;-->
<!--&lt;!&ndash;      </a-dropdown>&ndash;&gt;-->
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
          <a v-has="'pipepilehistory:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'pipepilehistory:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <device-pipepile-historydata-modal ref="modalForm" @ok="modalFormOk"></device-pipepile-historydata-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DevicePipepileHistorydataModal from './modules/DevicePipepileHistorydataModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'

  export default {
    name: 'DevicePipepileHistorydataList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DevicePipepileHistorydataModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: 'device_pipepile_historydata管理页面',
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
            dataIndex: 'shebeino_dictText'
          },
          {
            title:'桩号',
            align:"center",
            dataIndex: 'pileno'
          },
          {
            title:'设计桩长(m)',
            align:"center",
            dataIndex: 'designdep'
          },
          {
            title:'当前桩长度(m)',
            align:"center",
            dataIndex: 'curdep'
          },
          {
            title:'离地桩长(m)',
            align:"center",
            dataIndex: 'grounddep'
          },
          {
            title:'最大垂直度(%)',
            align:"center",
            dataIndex: 'pileY'
          },
          {
            title:'最大压桩力(兆帕)',
            align:"center",
            dataIndex: 'pileUpress'
          },
          {
            title:'最大夹持力(兆帕)',
            align:"center",
            dataIndex: 'pileDpress'
          },
          {
            title:'平均桩压力(兆帕)',
            align:"center",
            dataIndex: 'pileAveupress'
          },
          {
            title:'平均夹持力(兆帕)',
            align:"center",
            dataIndex: 'pileAvedpress'
          },
          {
            title:'开始时间',
            align:"center",
            dataIndex: 'pileStarttime'
          },
          {
            title:'数据时间',
            align:"center",
            dataIndex: 'datatime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'接桩时间',
            align:"center",
            dataIndex: 'piletime'
          },
          // {
          //   title:'打桩次数',
          //   align:"center",
          //   dataIndex: 'times'
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
          list: "/devicepipepilehistorydata/devicePipepileHistorydata/list",
          delete: "/devicepipepilehistorydata/devicePipepileHistorydata/delete",
          deleteBatch: "/devicepipepilehistorydata/devicePipepileHistorydata/deleteBatch",
          exportXlsUrl: "/devicepipepilehistorydata/devicePipepileHistorydata/exportXls",
          importExcelUrl: "devicepipepilehistorydata/devicePipepileHistorydata/importExcel",
          
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
          sbtypes:'61'
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
        fieldList.push({type:'string',value:'pileno',text:'桩号'})
        fieldList.push({type:'string',value:'designdep',text:'设计桩长(m)'})
        fieldList.push({type:'string',value:'curdep',text:'当前桩长度(m)'})
        fieldList.push({type:'string',value:'grounddep',text:'离地桩长(m)'})
        fieldList.push({type:'string',value:'pileY',text:'最大垂直度(%)'})
        fieldList.push({type:'string',value:'pileUpress',text:'最大压桩力(KN)'})
        fieldList.push({type:'string',value:'pileDpress',text:'最大夹持力(KN)'})
        fieldList.push({type:'string',value:'pileAveupress',text:'平均桩压力(KN)'})
        fieldList.push({type:'string',value:'pileAvedpress',text:'平均夹持力(KN)'})
        fieldList.push({type:'string',value:'pileStarttime',text:'开始时间'})
        fieldList.push({type:'date',value:'datatime',text:'数据时间'})
        fieldList.push({type:'string',value:'piletime',text:'接桩时间'})
        fieldList.push({type:'int',value:'times',text:'打桩次数'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>