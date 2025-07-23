<template>
<!-- 混凝土用量查询 -->
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
            <a-form-item label="超标等级">
              <j-dict-select-tag placeholder="请选择超标等级" v-model="queryParam.overLevel" dictCode="over_level"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="强度等级">
<!--              <j-search-select-tag placeholder="请选择强度等级" v-model="queryParam.strengthRank" :dictOptions="dictOption1"-->
<!--                                   @change="getQddj">-->
<!--              </j-search-select-tag>-->
              <a-input placeholder="请输入强度等级" v-model="queryParam.strengthRank" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="浇筑部位">
              <a-input placeholder="请输入浇筑部位" v-model="poureLocation1" @change = "poureLocation">
              </a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="出料时间范围">
              <j-date placeholder="开始出料时间" v-model="queryParam.productDatetime_begin" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束出料时间" v-model="queryParam.productDatetime_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
               <a-button type="primary" icon="download" @click="print1" style="margin-left: 8px">材料消耗统计导出</a-button>
              <a-button type="primary"  icon="download" style="margin-left: 8px" @click="handleExportXls('混凝土用量表')">导出</a-button>

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
      <a-button @click="handleAdd" v-has="'hntbhzyl:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'hntbhzyl:dc'" icon="download" @click="handleExportXls('拌合站主表')">导出</a-button>

      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'hntbhzyl:dr'" icon="import">导入</a-button>
      </a-upload>
      <!-- 禁用高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" v-has="'hntbhz:del'" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
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
        bordered
        rowKey="id"
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
        <a-tag color="green" v-if="record.overLevel == '0'">合格</a-tag>
        <a-tag color="orange" v-if="record.overLevel == '1'">初级超标</a-tag>
        <a-tag color="yellow" v-if="record.overLevel == '2'">中级超标</a-tag>
         <a-tag color="red" v-if="record.overLevel == '3'">高级超标</a-tag>
       </span>
        <span slot="formulaNo" slot-scope="formulaNo, record">
        <a-tag color="green" >{{record.formulaNo}}</a-tag>
       </span>
        <span class="separate" slot="cement1" slot-scope="text,record" >
          <div v-for="(item,index) in record.bhzCementDetailList" :key="index">
             <a-divider style="margin:0;height:1px" />
             <div>{{item.materialeName}}</div>
          </div>
        </span>
        <span class="separate" slot="cement2" slot-scope="text,record" >
          <div v-for="(item,index) in record.bhzCementDetailList" :key="index">
             <a-divider style="margin:0;height:1px" />
             <div>{{item.theoryNumber}}</div>
          </div>
        </span>
        <span class="separate" slot="cement3" slot-scope="text,record" >
          <div v-for="(item,index) in record.bhzCementDetailList" :key="index">
             <a-divider style="margin:0;height:1px" />
             <div>{{item.realityNumber}}</div>
          </div>
        </span>
        <span class="separate" slot="cement4" slot-scope="text,record" >
          <div v-for="(item,index) in record.bhzCementDetailList" :key="index">
             <a-divider style="margin:0;height:1px" />
             <div>{{item.errorValue}}</div>
          </div>
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

        <span slot="action"  slot-scope="text, record">
           <a @click="handleDetail(record)">详情</a>

<!--          <a v-has="'hntbhzyl:edit'" @click="handleEdit(record)">编辑</a>-->

<!--          <a-divider type="vertical" />-->
<!--          <a-dropdown>-->
<!--            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>-->
<!--            <a-menu slot="overlay">-->
<!--              <a-menu-item>-->
<!--                <a @click="handleDetail(record)">详情</a>-->
<!--              </a-menu-item>-->
<!--              <a-menu-item v-has="'hntbhzyl:del'">-->
<!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
<!--                  <a >删除</a>-->
<!--                </a-popconfirm>-->
<!--              </a-menu-item>-->
<!--            </a-menu>-->
<!--          </a-dropdown>-->
<!--           <shen-he :flag="flag" :hntbatch="record.batchNo" @change="change"></shen-he>-->
<!--          <chu-zhi :flag="flag" :hntbatch="record.batchNo" @change="change2"></chu-zhi>-->
        </span>

      </a-table>
    </div>
    <BhzCementBaseModal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import BhzCementBaseModal from './modules/BhzCementBaseModal'
import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
import '@/assets/less/TableExpand.less'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import ChuZhi from "../czsh/ChuZhi";
import ShenHe from "../czsh/ShenHe";
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { usershebeiList } from '@api/api'
import { handertoken } from '@/mixins/getHanderToken'

import Vue from 'vue'
import {getAction} from "@api/manage";

export default {
  name: "BhzCementBaseYLList",
  mixins:[JeecgListMixin,handertoken],
  components: {
    ChuZhi,
    ShenHe,
    BhzCementBaseModal,
    JSuperQuery
  },
  data () {
    return {
      poureLocation1:'',
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      dictOption1:[],
      flag: 0,
      description: '混凝土主表管理页面',
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
        // {
        //   title:'所属组织机构',
        //   align:"center",
        //   dataIndex: 'sysOrgCode_dictText'
        // },
        {
          title:'设备名称',
          align:"center",
          width: 200,
          dataIndex: 'shebeiNo',
          key:'shebeiNo',
        },
        {
          title:'浇筑部位',
          align:"center",
          width: 350,
          dataIndex: 'poureLocation',
          key:'poureLocation',
        },
        {
          title:'材料名称',
          align:"center",
          scopedSlots: { customRender: 'cement1' },
        },
        {
          title:'理论用量',
          align:"center",
          scopedSlots: { customRender: 'cement2' },
        },
        {
          title:'实际用量',
          align:"center",
          scopedSlots: { customRender: 'cement3' },
        },
        {
          title:'生产方量',
          align:"center",
          dataIndex: 'estimateNumber',
          key:'estimateNumber',
        },
        {
          title:'强度等级',
          align:"center",
          dataIndex: 'strengthRank',
          key:'strengthRank',
        },
        {
          title:'出料时间',
          align:"center",
          dataIndex: 'productDatetime',
          key:'productDatetime',
        },
        // {
        //   title:'工程名称',
        //   align:"center",
        //   dataIndex: 'projectName',
        //   key:'projectName',
        // },
        // {
        //   title: '配方号',
        //   align: 'center',
        //   dataIndex: 'formulaNo',
        //   scopedSlots: { customRender: 'formulaNo' },
        // },
        // {
        //   title:'操作人',
        //   align:"center",
        //   dataIndex: 'handlers',
        //   key:'handlers',
        // },
        // {
        //   title:'施工地点',
        //   align:"center",
        //   dataIndex: 'jobLocation',
        //   key:'jobLocation',
        // },
        // {
        //   title:'误差',
        //   align:"center",
        //   scopedSlots: { customRender: 'cement4' },
        // },
        // {
        //   title:'坍落度',
        //   align:"center",
        //   dataIndex: 'slump',
        //   key:'slump',
        // },
        // {
        //   title:'超标等级',
        //   align:"center",
        //   dataIndex: 'overLevel',//overLevel_dictText
        //   key:'overLevel',
        //   scopedSlots: { customRender: 'tags' },
        // },
        {
          title: '详情',
          dataIndex: 'action',
          align:"center",
          fixed:"right",
          width:100,
          scopedSlots: { customRender: 'action' },
        }
      ],
      isorter: {
        column: 'productDatetime',
        order: 'desc',
      },
      url: {
        list: "/hntbhz/bhzCementBase/list3",
        delete: "/hntbhz/bhzCementBase/delete",
        deleteBatch: "/hntbhz/bhzCementBase/deleteBatch",
        // exportXlsUrl: "/hntbhz/bhzCementBase/exportXls",
        importExcelUrl: "hntbhz/bhzCementBase/importExcel",
        exportXlsUrl: "/hntbhz/bhzCementBase/exportCementUsage",
        printUrl:"/jmreport/view/878884334011527168",
        printUrl1:"/jmreport/view/878879140903149568",
        getQddjList: 'hntbhz/bhzCementBase/getQddjList',

      },
      dictOptions:{},
      superFieldList:[],
    }
  },
  created() {
    this.getToken();
    this.getSuperFieldList();
    this.shebeiList();
    this.getQddj();
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    poureLocation() {
      this.queryParam.poureLocation = '*'+ this.poureLocation1+'*'
    },
    getQddj: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      var params = {
        sys_depart_orgcode: sys_depart_orgcode
      }
      getAction(this.url.getQddjList, params).then(res => {
        this.dictOption1 = []
        let result = res.result
        result.forEach(re => {
          this.dictOption1.push({ text: re.strengthRank, value: re.strengthRank })
        })
      })
    },
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
    print1: function () {//打印功能需要先去报表设计页面设计打印格式
      let param = this.getQueryParams()
      if (param.productDatetime_begin == null) {
        this.$message.error('请选择出料开始时间!')
      } else {
        if (param.shebeiNo == null) {
          let url = `${window._CONFIG['domianURL']}/${this.url.printUrl1}?productDatetime_begin=${param.productDatetime_begin}&productDatetime_end=${param.productDatetime_end}&token=${this.token}`
          window.open(url)
        } else {
          let url = `${window._CONFIG['domianURL']}/${this.url.printUrl}?shebeiNo=${param.shebeiNo}&productDatetime_begin=${param.productDatetime_begin}&productDatetime_end=${param.productDatetime_end}&token=${this.token}`
          window.open(url)
        }
      }
    },
    initDictConfig(){
    },
    getSuperFieldList(){
      let fieldList=[];
      fieldList.push({type:'string',value:'batchNo',text:'唯一ID',dictCode:''})
      fieldList.push({type:'string',value:'shebeiNo',text:'设备编号',dictCode:''})
      fieldList.push({type:'string',value:'workNo',text:'工单号',dictCode:''})
      fieldList.push({type:'string',value:'handlers',text:'操作者',dictCode:''})
      fieldList.push({type:'string',value:'projectName',text:'工程名称',dictCode:''})
      fieldList.push({type:'string',value:'jobLocation',text:'施工地点',dictCode:''})
      fieldList.push({type:'string',value:'poureLocation',text:'浇筑部位',dictCode:''})
      fieldList.push({type:'string',value:'cementVariety',text:'水泥品种',dictCode:''})
      fieldList.push({type:'string',value:'additiveVariety',text:'外加剂品种',dictCode:''})
      fieldList.push({type:'string',value:'formulaNo',text:'配方号',dictCode:''})
      fieldList.push({type:'string',value:'strengthRank',text:'强度等级',dictCode:''})
      fieldList.push({type:'int',value:'stirDatetime',text:'搅拌时间',dictCode:''})
      fieldList.push({type:'date',value:'saveDatetime',text:'保存时间'})
      fieldList.push({type:'string',value:'clientNo',text:'客户端编号',dictCode:''})
      fieldList.push({type:'int',value:'status',text:'状态',dictCode:''})
      fieldList.push({type:'date',value:'collectDatetime',text:'采集时间'})
      fieldList.push({type:'double',value:'estimateNumber',text:'方量',dictCode:''})
      fieldList.push({type:'date',value:'productDatetime',text:'出料时间'})
      fieldList.push({type:'string',value:'slump',text:'坍落度',dictCode:''})
      fieldList.push({type:'int',value:'overLevel',text:'超标等级',dictCode:'over_level'})
      fieldList.push({type:'int',value:'alertstate',text:'是否超标',dictCode:''})
      fieldList.push({type:'string',value:'formulaId',text:'配方uuid(车结束符)',dictCode:''})
      fieldList.push({type:'int',value:'timeOverLevel',text:'搅拌时间超标等级',dictCode:''})
      this.superFieldList = fieldList
    },
    change(data) {
      this.flag = data
    },
    change2(data) {
      this.flag = data
    },
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
.separate div{
  /* flex: 1; */
  height: 25px;
  line-height: 25px;

  /* border-bottom: 1px solid #999999; */

}
</style>