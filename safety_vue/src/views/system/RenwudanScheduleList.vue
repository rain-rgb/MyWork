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
            <a-form-item label="开始时间范围">
              <j-date placeholder="开始出料时间" v-model="queryParam.starttim_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束出料时间" v-model="queryParam.starttim_end" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
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
      <a-button @click="handleAdd" v-has="'renwudanjd:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'renwudanjd:dc'" icon="download" @click="handleExportXls('拌合站任务单进度')">导出</a-button>
      <a-upload v-has="'renwudanjd:dr'" name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
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
        <span slot="code" slot-scope="code, record">
          <a-tag color="blue" >{{record.code}}</a-tag>
        </span>
        <template slot="bfb" slot-scope="text, record, index">
          <a-progress :strokeColor="getPercentColor(record.bfb)" :format="getPercentFormat" :percent="getFlowRateNumber(record.bfb)" style="width:100px" />
        </template>
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
          <a v-has="'renwudanjd:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"  v-has="'renwudanjd:edit'"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'renwudanjd:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <renwudan-schedule-modal ref="modalForm" @ok="modalFormOk"></renwudan-schedule-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import RenwudanScheduleModal from './modules/RenwudanScheduleModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'
  export default {
    selectValue: '',
    asyncSelectValue: '',
    dictOption: [],
    name: 'RenwudanScheduleList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      RenwudanScheduleModal,
      JSuperQuery,
    },
    data () {
      return {
        description: '分部分项工程进度',
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
            dataIndex: 'shebeiNo_dictText'
          },
          {
            title:'生产线',
            align:"center",
            dataIndex: 'station_dictText'
          },
          {
            title:'任务单编号',
            align:"center",
            dataIndex: 'code',
            key: 'code',
            scopedSlots: { customRender: 'code' },
          },
          {
            title:'项目名称',
            align:"center",
            dataIndex: 'projectname'
          },
          {
            title:'施工部位',
            align:"center",
            dataIndex: 'conspos'
          },
          {
            title:'浇筑方式',
            align:"center",
            dataIndex: 'pour'
          },
          {
            title:'开始生产时间',
            align:"center",
            dataIndex: 'starttim',
          },
          {
            title:'结束生产时间',
            align:"center",
            dataIndex: 'endtim',
          },
          {
            title:'强度等级',
            align:"center",
            dataIndex: 'betlev'
          },
          {
            title:'坍落度',
            align:"center",
            dataIndex: 'lands'
          },
          {
            title:'任务方量(m³)',
            align:"center",
            dataIndex: 'mete'
          },
          {
            title:'生产方量(m³)',
            align:"center",
            dataIndex: 'metes'
          },
          // {
          //   title:'权限',
          //   align:"center",
          //   dataIndex: 'sysOrgCode'
          // },
          {
            title:'分部分项',
            align:"center",
            dataIndex: 'sysDepartProject_dictText'
          },
          {
            title:'进度',
            align:"center",
            dataIndex: 'bfb',
            width:147,
            fixed:"right",
            scopedSlots: { customRender: 'bfb' }
          }
        ],
        url: {
          list: "/renwudan/renwudanSchedule/list",
          delete: "/renwudan/renwudanSchedule/delete",
          deleteBatch: "/renwudan/renwudanSchedule/deleteBatch",
          exportXlsUrl: "/renwudan/renwudanSchedule/exportXls",
          importExcelUrl: "renwudan/renwudanSchedule/importExcel",

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
      getPercentColor(value) {
        let p = Number(value)
        if (p >= 90 && p < 100) {
          return 'rgb(244, 240, 89)'
        } else if (p >= 100) {
          return 'red'
        } else {
          return 'rgb(16, 142, 233)'
        }
      },
      getPercentFormat(value) {
        if (value == 100) {
          return "完成"
        } else {
          return value + "%"
        }
      },
      getFlowRateNumber(value) {
        return Number(value)
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'shebeiNo',text:'设备编号',dictCode:''})
        fieldList.push({type:'int',value:'station',text:'生产线（0公用 1 第一生产线  2 第二生产线）',dictCode:''})
        fieldList.push({type:'string',value:'code',text:'任务单编号',dictCode:''})
        fieldList.push({type:'string',value:'projectname',text:'项目名称',dictCode:''})
        fieldList.push({type:'string',value:'conspos',text:'施工部位',dictCode:''})
        fieldList.push({type:'string',value:'pour',text:'浇筑方式',dictCode:''})
        fieldList.push({type:'date',value:'starttim',text:'开始生产时间'})
        fieldList.push({type:'date',value:'endtim',text:'结束生产时间'})
        fieldList.push({type:'string',value:'betlev',text:'强度等级',dictCode:''})
        fieldList.push({type:'string',value:'lands',text:'坍落度',dictCode:''})
        fieldList.push({type:'double',value:'mete',text:'任务方量',dictCode:''})
        fieldList.push({type:'double',value:'metes',text:'生产方量',dictCode:''})
        fieldList.push({type:'string',value:'sysOrgCode',text:'权限',dictCode:''})
        fieldList.push({type:'int',value:'isdel',text:'是否删除 0未删除 1已删除',dictCode:''})
        fieldList.push({type:'string',value:'sysDepartProject',text:'分部分项节点',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>