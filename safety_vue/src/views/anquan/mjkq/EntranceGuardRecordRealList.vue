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
            <a-form-item label="姓名">
              <a-input placeholder="请输入姓名" v-model="queryParam.names"></a-input>
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
      <a-button @click="handleAdd" v-has="'kaoqinreal:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'kaoqinreal:dc'" icon="download" @click="handleExportXls('门禁考勤实时表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'kaoqinreal:dr'" icon="import">导入</a-button>
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
          <a v-has="'kaoqinreal:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'kaoqinreal:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <entrance-guard-record-real-modal ref="modalForm" @ok="modalFormOk"></entrance-guard-record-real-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import EntranceGuardRecordRealModal from './modules/EntranceGuardRecordRealModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import Vue from 'vue'
  import { usershebeiList } from '@api/api'
  export default {
    name: 'EntranceGuardRecordRealList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      EntranceGuardRecordRealModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '门禁考勤实时表管理页面',
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
          //   title:'recordid',
          //   align:"center",
          //   dataIndex: 'recordid'
          // },
          // {
          //   title:'types',
          //   align:"center",
          //   dataIndex: 'types'
          // },
          // {
          //   title:'openid',
          //   align:"center",
          //   dataIndex: 'openid'
          // },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'shebeino_dictText'
          },
          {
            title:'姓名',
            align:"center",
            dataIndex: 'names'
          },
          {
            title:'开门时间',
            align:"center",
            dataIndex: 'opentime',
          },
          {
            title:'班组',
            align:"center",
            dataIndex: 'departname'
          },
          // {
          //   title:'doorid',
          //   align:"center",
          //   dataIndex: 'doorid'
          // },
          {
            title:'备注',
            align:"center",
            dataIndex: 'info'
          },
          // {
          //   title:'adminid',
          //   align:"center",
          //   dataIndex: 'adminid'
          // },
          // {
          //   title:'pic',
          //   align:"center",
          //   dataIndex: 'pic'
          // },
          // {
          //   title:'adddate',
          //   align:"center",
          //   dataIndex: 'adddate',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          // {
          //   title:'isoffline',
          //   align:"center",
          //   dataIndex: 'isoffline'
          // },
          // {
          //   title:'capturepic',
          //   align:"center",
          //   dataIndex: 'capturepic'
          // },
          // {
          //   title:'isopen',
          //   align:"center",
          //   dataIndex: 'isopen'
          // },
          // {
          //   title:'serialno',
          //   align:"center",
          //   dataIndex: 'serialno'
          // },
          // {
          //   title:'isupload',
          //   align:"center",
          //   dataIndex: 'isupload'
          // },
          // {
          //   title:'uploaddate',
          //   align:"center",
          //   dataIndex: 'uploaddate',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          // {
          //   title:'温度',
          //   align:"center",
          //   dataIndex: 'temperature'
          // },
          // {
          //   title:'standard',
          //   align:"center",
          //   dataIndex: 'standard'
          // },
          // {
          //   title:'用户ID',
          //   align:"center",
          //   dataIndex: 'cid'
          // },
          // {
          //   title:'部门ID',
          //   align:"center",
          //   dataIndex: 'departmentid'
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
          list: "/entranceguardrecordreal/entranceGuardRecordReal/list",
          delete: "/entranceguardrecordreal/entranceGuardRecordReal/delete",
          deleteBatch: "/entranceguardrecordreal/entranceGuardRecordReal/deleteBatch",
          exportXlsUrl: "/entranceguardrecordreal/entranceGuardRecordReal/exportXls",
          importExcelUrl: "entranceguardrecordreal/entranceGuardRecordReal/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
      this.shebeiList()
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
          sbtypes:'18'
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
        fieldList.push({type:'int',value:'recordid',text:'recordid',dictCode:''})
        fieldList.push({type:'int',value:'types',text:'types',dictCode:''})
        fieldList.push({type:'string',value:'openid',text:'openid',dictCode:''})
        fieldList.push({type:'date',value:'opentime',text:'开门时间'})
        fieldList.push({type:'string',value:'doorid',text:'doorid',dictCode:''})
        fieldList.push({type:'string',value:'info',text:'备注',dictCode:''})
        fieldList.push({type:'int',value:'adminid',text:'adminid',dictCode:''})
        fieldList.push({type:'string',value:'pic',text:'pic',dictCode:''})
        fieldList.push({type:'date',value:'adddate',text:'adddate'})
        fieldList.push({type:'int',value:'isoffline',text:'isoffline',dictCode:''})
        fieldList.push({type:'string',value:'capturepic',text:'capturepic',dictCode:''})
        fieldList.push({type:'int',value:'isopen',text:'isopen',dictCode:''})
        fieldList.push({type:'string',value:'serialno',text:'serialno',dictCode:''})
        fieldList.push({type:'int',value:'isupload',text:'isupload',dictCode:''})
        fieldList.push({type:'date',value:'uploaddate',text:'uploaddate'})
        fieldList.push({type:'string',value:'temperature',text:'温度',dictCode:''})
        fieldList.push({type:'string',value:'standard',text:'standard',dictCode:''})
        fieldList.push({type:'string',value:'cid',text:'用户ID',dictCode:''})
        fieldList.push({type:'string',value:'names',text:'姓名',dictCode:''})
        fieldList.push({type:'string',value:'shebeino',text:'设备编号',dictCode:''})
        fieldList.push({type:'string',value:'departmentid',text:'部门ID',dictCode:''})
        fieldList.push({type:'string',value:'departname',text:'部门名称',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>