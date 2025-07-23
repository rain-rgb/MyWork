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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" v-has="'cranehistory:dc'" @click="handleExportXls('大型设备(提梁机/架桥机)配置数据表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'cranehistory:dr'" icon="import">导入</a-button>
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
          <a-tag color="green" v-if="record.isCall === 0">报警</a-tag>
          <a-tag color="red" v-if="record.isCall === 1">不报警</a-tag>
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

    <device-crane-cfg-modal ref="modalForm" @ok="modalFormOk"></device-crane-cfg-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceCraneCfgModal from './modules/DeviceCraneCfgModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import Vue from 'vue'
  import { usershebeiList } from '@api/api'

  export default {
    name: 'DeviceCraneCfgList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceCraneCfgModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '大型设备(提梁机/架桥机)配置数据表管理页面',
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
            title:'所属组织机构',
            align:"center",
            dataIndex: 'sysOrgCode_dictText'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'deviceCode_dictText'
          },
          // {
          //   title:'主键uuid',
          //   align:"center",
          //   dataIndex: 'uid'
          // },
          {
            title:'主钩吊重超额重比例(%)',
            align:"center",
            dataIndex: 'mainHookloadlv'
          },
          {
            title:'副钩吊重超额重比例(%)',
            align:"center",
            dataIndex: 'vicehookloadlv'
          },
          {
            title:'副钩吊重超主钩吊重比例(%)',
            align:"center",
            dataIndex: 'mainVicelv'
          },
          {
            title:'风速报警值(m/s)',
            align:"center",
            dataIndex: 'windSpeedalarm'
          },
          {
            title:'激光测距偏差值(cm)',
            align:"center",
            dataIndex: 'slr'
          },
          // {
          //   title:'是否删除（0：未删除，1：已删除）',
          //   align:"center",
          //   dataIndex: 'isdel'
          // },
          {
            title:'是否报警',
            align:"center",
            dataIndex: 'isCall_dictText',
            scopedSlots: {customRender: 'tags'},
          },
          {
            title:'大型设备手机号码组',
            align:"center",
            dataIndex: 'cranePhones_dictText'
          },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'createTime',
          },
          {
            title:'更新时间',
            align:"center",
            dataIndex: 'updateTime',
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy'
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
          list: "/devicecranecfg/deviceCraneCfg/list",
          delete: "/devicecranecfg/deviceCraneCfg/delete",
          deleteBatch: "/devicecranecfg/deviceCraneCfg/deleteBatch",
          exportXlsUrl: "/devicecranecfg/deviceCraneCfg/exportXls",
          importExcelUrl: "devicecranecfg/deviceCraneCfg/importExcel",

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
          sbtypes:'21,23,50'
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
        fieldList.push({type:'string',value:'uid',text:'主键uuid',dictCode:''})
        fieldList.push({type:'double',value:'mainHookloadlv',text:'主钩吊重超额重比例(%)',dictCode:''})
        fieldList.push({type:'double',value:'vicehookloadlv',text:'副钩吊重超额重比例(%)',dictCode:''})
        fieldList.push({type:'double',value:'windSpeedalarm',text:'风速报警值',dictCode:''})
        fieldList.push({type:'double',value:'slr',text:'激光测距偏差值',dictCode:''})
        fieldList.push({type:'int',value:'isdel',text:'是否删除（0：未删除，1：已删除）',dictCode:''})
        fieldList.push({type:'int',value:'isCall',text:'是否报警:0=报警,1=不报警',dictCode:''})
        fieldList.push({type:'string',value:'cranePhones',text:'提梁机手机号码组',dictCode:''})
        fieldList.push({type:'date',value:'createTime',text:'创建时间'})
        fieldList.push({type:'date',value:'updateTime',text:'更新时间'})
        fieldList.push({type:'string',value:'sysOrgCode',text:'权限配置',dictCode:''})
        fieldList.push({type:'string',value:'createBy',text:'创建人',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>