<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeiNo" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="工作状态">
              <j-dict-select-tag placeholder="请选择工作状态" v-model="queryParam.status" dictCode="workstatus"></j-dict-select-tag>
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
      <a-button @click="handleAdd" v-has="'bhzworkstatus:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" v-has="'bhzworkstatus:dc'" @click="handleExportXls('砼拌合站工作状态表信息')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'bhzworkstatus:dr'" icon="import">导入</a-button>
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
          <a-tag color="yellow" v-if="record.status === 0">未安装</a-tag>
          <a-tag color="red" v-if="record.status === 1">未工作</a-tag>
          <a-tag color="orange" v-if="record.status === 2">延时</a-tag>
          <a-tag color="green" v-if="record.status === 3">正常工作</a-tag>
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
          <a v-has="'bhzworkstatus:edit'" @click="handleEdit(record)">编辑</a>

<!--          <a-divider type="vertical" />-->
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'bhzworkstatus:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <bhz-cement-workstatus-modal ref="modalForm" @ok="modalFormOk"></bhz-cement-workstatus-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BhzCementWorkstatusModal from './modules/BhzCementWorkstatusModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { usershebeiList } from '@api/api'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import Vue from 'vue'

  export default {
    name: 'BhzCementWorkstatusList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BhzCementWorkstatusModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
         queryParam: { sbtype:'0' },
        description: '砼拌合站工作状态表信息管理页面',
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
            dataIndex: 'sbname'
          },
          {
            title:'所属组织机构',
            align:"center",
            dataIndex: 'sysOrgCode_dictText'
          },
          {
            title:'最新数据时间',
            align:"center",
            dataIndex: 'uploadDatetime',
          },
          // {
          //   title:'采集时间',
          //   align:"center",
          //   dataIndex: 'collectDatetime',
          // },
          // {
          //   title:'出料时间',
          //   align:"center",
          //   dataIndex: 'productDatetime',
          // },
          {
            title:'工作状态',
            align:"center",
            dataIndex: 'status',
            scopedSlots: { customRender: 'tags' },
          },
          // {
          //   title: '操作',
          //   dataIndex: 'action',
          //   align:"center",
          //   fixed:"right",
          //   width:147,
          //   scopedSlots: { customRender: 'action' }
          // }
        ],
        url: {
          list: "/system/shebeiInfo/list",
          delete: "/bhzcementworkstatus/bhzCementWorkstatus/delete",
          deleteBatch: "/bhzcementworkstatus/bhzCementWorkstatus/deleteBatch",
          exportXlsUrl: "/bhzcementworkstatus/bhzCementWorkstatus/exportXls",
          importExcelUrl: "bhzcementworkstatus/bhzCementWorkstatus/importExcel",

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
          sbtypes:'0'
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
        fieldList.push({type:'string',value:'sbname',text:'设备名称',dictCode:''})
        fieldList.push({type:'string',value:'sysOrgCode',text:'控制权限',dictCode:''})
        fieldList.push({type:'date',value:'saveDatetime',text:'最新数据时间'})
        fieldList.push({type:'date',value:'collectDatetime',text:'采集时间'})
        fieldList.push({type:'date',value:'productDatetime',text:'出料时间'})
        fieldList.push({type:'int',value:'status',text:'工作状态 0：未安装 1：未工作 2：延时 3：正常工作',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>