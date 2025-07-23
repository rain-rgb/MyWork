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
      <!--      <a-button type="primary" icon="download" @click="handleExportXls('拌合站超标配置')">导出</a-button>-->
      <!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"-->
      <!--                @change="handleImportExcel">-->
      <!--        <a-button type="primary" icon="import">导入</a-button>-->
      <!--      </a-upload>-->
      <!-- 高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal"-->
      <!--                     @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" @click="batchDel">-->
      <!--            <a-icon type="delete"/>-->
      <!--            删除-->
      <!--          </a-menu-item>-->
      <!--        </a-menu>-->
      <!--        <a-button style="margin-left: 8px"> 批量操作-->
      <!--          <a-icon type="down"/>-->
      <!--        </a-button>-->
      <!--      </a-dropdown>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >
        <span slot="tags" slot-scope="tags, record">
        <a-tag color="green" v-if="record.isCall == '0'">报警</a-tag>
         <a-tag color="red" v-if="record.isCall == '1'">不报警</a-tag>
       </span>
        <span slot="tags1" slot-scope="tags1, record">
        <a-tag color="geekblue" v-if="record.primaryPhones_dictText">{{record.primaryPhones_dictText}}</a-tag>
       </span>
        <span slot="tags2" slot-scope="tags2, record">
          <a-tag color="orange" v-if="record.middlePhones_dictText">{{record.middlePhones_dictText}}</a-tag>
       </span>
        <span slot="tags3" slot-scope="tags3, record">
          <a-tag color="red" v-if="record.advancedPhones_dictText">{{record.advancedPhones_dictText}}</a-tag>
       </span>
        <span slot="tags4" slot-scope="tags4, record">
          <a-tag color="yellow" v-if="record.wenduyujingPhones_dictText">{{record.wenduyujingPhones_dictText}}</a-tag>
       </span>
        <span slot="tags5" slot-scope="tags5, record">
        <a-tag color="green" v-if="record.csyj == '0'">报警</a-tag>
         <a-tag color="red" v-if="record.csyj == '1'">不报警</a-tag>
       </span>
        <span slot="tags6" slot-scope="tags6, record">
        <a-tag color="green" v-if="record.cssjdw == '0'">分钟</a-tag>
         <a-tag color="green" v-if="record.cssjdw == '1'">小时</a-tag>
       </span>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
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

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
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
    <bhz-call-cfg-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BhzCallCfgModal from './modules/BhzCallCfgModal'
  import '@/assets/less/TableExpand.less'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import Vue from 'vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList } from '@api/api'

  export default {
    name: "BhzCallCfgList",
    mixins:[JeecgListMixin],
    components: {
      BhzCallCfgModal,
      JSuperQuery
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '拌合站超标配置管理页面',
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
          {
            title: '所属机构',
            align: "center",
            dataIndex: 'sysOrgCode_dictText'
          },
          {
            title: '设备名称',
            align: "center",
            dataIndex: 'bhjno_dictText'
          },
          {
            title: '是否报警',
            align: "center",
            dataIndex: 'isCall_dictText',
            key: 'isCall_dictText',
            scopedSlots: {customRender: 'tags'},
          },
          {
            title: '初级号码组',
            align: "center",
            dataIndex: 'primaryPhones_dictText',
            key: 'primaryPhones_dictText',
            scopedSlots: {customRender: 'tags1'},
          },
          {
            title: '中级号码组',
            align: "center",
            dataIndex: 'middlePhones',
            key: 'middlePhones_dictText',
            scopedSlots: {customRender: 'tags2'},
          },
          {
            title: '高级号码组',
            align: "center",
            dataIndex: 'advancedPhones',
            key: 'advancedPhones_dictText',
            scopedSlots: {customRender: 'tags3'},
          },
          {
            title: '温度预警号码组',
            align: "center",
            dataIndex: 'wenduyujingPhones',
            key: 'wenduyujingPhones_dictText',
            scopedSlots: {customRender: 'tags4'},
          },
          {
            title: '处置负责人',
            align: "center",
            dataIndex: 'czperson'
          },
          {
            title: '审核负责人',
            align: "center",
            dataIndex: 'shperson'
          },
          {
            title: '审核负责人',
            align: "center",
            dataIndex: 'spperson'
          },
          {
            title: '超时是否报警',
            align: "center",
            dataIndex: 'csyj_dictText',
            key: 'csyj_dictText',
            scopedSlots: {customRender: 'tags5'},
          },
          {
            title: '超时时长',
            align: "center",
            dataIndex: 'cssj',
          },
          {
            title: '超时单位',
            align: "center",
            dataIndex: 'cssjdw_dictText',
            key: 'cssjdw_dictText',
            scopedSlots: {customRender: 'tags6'},
          },
          // {
          //   title: '创建时间',
          //   align: "center",
          //   dataIndex: 'createTime',
          //   customRender: function (text) {
          //     return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
          //   }
          // },
          // {
          //   title: '创建人',
          //   align: "center",
          //   dataIndex: 'createBy'
          // },
          // {
          //   title: '更新时间',
          //   align: "center",
          //   dataIndex: 'updateTime',
          //   customRender: function (text) {
          //     return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
          //   }
          // },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/bhzcfg/bhzCallCfg/list",
          delete: "/bhzcfg/bhzCallCfg/delete",
          deleteBatch: "/bhzcfg/bhzCallCfg/deleteBatch",
          exportXlsUrl: "/bhzcfg/bhzCallCfg/exportXls",
          importExcelUrl: "bhzcfg/bhzCallCfg/importExcel",

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
      }
    },
    methods: {
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'0,1,2'
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
         fieldList.push({type:'string',value:'uid',text:'主键uuid',dictCode:''})
         fieldList.push({type:'string',value:'departid',text:'所属组织机构id',dictCode:''})
         fieldList.push({type:'string',value:'bhjno',text:'拌合机编号',dictCode:''})
         fieldList.push({type:'int',value:'isCall',text:'是否报警:0=报警,1=不报警',dictCode:''})
         fieldList.push({type:'int',value:'stirDatetimeDesign',text:'搅拌时间报警设计值',dictCode:''})
         fieldList.push({type:'string',value:'primaryPhones',text:'初级号码组',dictCode:''})
         fieldList.push({type:'string',value:'middlePhones',text:'中级号码组',dictCode:''})
         fieldList.push({type:'string',value:'advancedPhones',text:'高级号码组',dictCode:''})
         fieldList.push({type:'string',value:'topprimaryPhones',text:'所属上级初级号码组',dictCode:''})
         fieldList.push({type:'string',value:'topmiddlePhones',text:'所属上级中级号码组',dictCode:''})
         fieldList.push({type:'string',value:'topadvancedPhones',text:'所属上级高级号码组',dictCode:''})
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