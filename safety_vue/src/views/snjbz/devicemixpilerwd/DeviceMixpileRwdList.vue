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
            <a-form-item label="里程名称">
              <a-input placeholder="请输入里程" v-model="queryParam.mileage"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.starttime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.starttime_end" :showTime="true"
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('软基工单表')">导出</a-button>
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
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
        @change="handleTableChange">
        <template slot="bfb" slot-scope="text, record, index">
          <a-progress :strokeColor="getPercentColor(record.totalpro)" :format="getPercentFormat" :percent="getFlowRateNumber(record.totalpro)" style="width:100px" />
        </template>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>

        <span slot="tags" slot-scope="tags, record">
          <a-tag color="orange" v-if="record.status == '0'">未开始</a-tag>
        <a-tag color="red" v-if="record.status == '3'">滞后</a-tag>
        <a-tag color="green" v-if="record.status == '2'">完成</a-tag>
        <a-tag color="blue" v-if="record.status == '1'">进行中</a-tag>

       </span>

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
              <a-menu-item v-has="'mixrwd:addrw'">
                <a @click="handleIssued(record)">下发</a>
              </a-menu-item>
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

    <device-mixpile-rwd-modal ref="modalForm" @ok="modalFormOk"></device-mixpile-rwd-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceMixpileRwdModal from './modules/DeviceMixpileRwdModal'
  import { getAction } from '@api/manage'
  import Vue from "vue";
  import {usershebeiList} from "@api/api";

  export default {
    name: 'DeviceMixpileRwdList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceMixpileRwdModal
    },
    data () {
      return {
        dictOption:[],
        description: '软基工单表管理页面',
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
            title:'状态',
            align:"center",
            dataIndex: 'rjrwd',
            scopedSlots: {customRender: 'tags'},
          },
          {
            title:'软基工单号',
            align:"center",
            dataIndex: 'rjrwd'
          },
          {
            title:'组织机构',
            align:"center",
            dataIndex: 'orgcode_dictText'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'shebeino_dictText'
          },
          // {
          //   title:'设备编号',
          //   align:"center",
          //   dataIndex: 'shebeino'
          // },
          {
            title:'里程',
            align:"center",
            dataIndex: 'mileage'
          },
          // {
          //   title:'里程id',
          //   align:"center",
          //   dataIndex: 'mileageid'
          // },
          {
            title:'设计桩基数',
            align:"center",
            dataIndex: 'descount'
          },
          // {
          //   title:'设计每根桩长',
          //   align:"center",
          //   dataIndex: 'deslen'
          // },
          {
            title:'通知人',
            align:"center",
            dataIndex: 'msgperson_dictText'
          },
          {
            title:'开工日期',
            align:"center",
            dataIndex: 'starttime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          // {
          //   title:'截止日期',
          //   align:"center",
          //   dataIndex: 'endtime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          // {
          //   title:'备注',
          //   align:"center",
          //   dataIndex: 'other'
          // },
          // {
          //   title:'工单状态，0：未开始，1：进行中；2：已完成，3：滞后',
          //   align:"center",
          //   dataIndex: 'status'
          // },
          // {
          //   title:'开工日期',
          //   align:"center",
          //   dataIndex: 'starttime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy'
          },
          {
            title:'完成数量',
            align:"center",
            dataIndex: 'handled'
          },
          {
            title:'进度',
            align:"center",
            dataIndex: 'totalpro',
            width:147,
            fixed:"right",
            scopedSlots: { customRender: 'bfb' }
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
          list: "/devicemixpilerwd/deviceMixpileRwd/listjd",
        //  list: "/devicemixpilerwd/deviceMixpileRwd/list",
          delete: "/devicemixpilerwd/deviceMixpileRwd/delete",
          deleteBatch: "/devicemixpilerwd/deviceMixpileRwd/deleteBatch",
          exportXlsUrl: "/devicemixpilerwd/deviceMixpileRwd/exportXls",
          importExcelUrl: "devicemixpilerwd/deviceMixpileRwd/importExcel",
          list1:"/devicemixpilerwd/deviceMixpileRwd/issuedlist"
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
          sbtypes:'16,19,53,54,61'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      handleIssued(record){
        let params = {rjrwd:record.rjrwd}
        getAction(this.url.list1,params).then(res=>{
          if (res.success){
            //console.log("res",res)
            this.$message.success(res.result)
          }else {
            this.$message.error("下发失败")
          }
        })
        console.log("record",record)
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
        fieldList.push({type:'string',value:'rjrwd',text:'软基工单号',dictCode:''})
        fieldList.push({type:'string',value:'orgcode',text:'组织机构',dictCode:''})
        fieldList.push({type:'string',value:'mileage',text:'里程',dictCode:''})
        fieldList.push({type:'string',value:'mileageid',text:'里程id',dictCode:''})
        fieldList.push({type:'int',value:'descount',text:'设计桩基数',dictCode:''})
        fieldList.push({type:'string',value:'deslen',text:'设计每根桩长',dictCode:''})
        fieldList.push({type:'string',value:'msgperson',text:'通知人',dictCode:''})
        fieldList.push({type:'date',value:'starttime',text:'开工日期'})
        fieldList.push({type:'date',value:'endtime',text:'截止日期'})
        fieldList.push({type:'string',value:'other',text:'备注',dictCode:''})
        fieldList.push({type:'int',value:'status',text:'工单状态，0：未开始，1：进行中；2：已完成，3：滞后',dictCode:''})
        fieldList.push({type:'int',value:'alt',text:'是否进行统计',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>