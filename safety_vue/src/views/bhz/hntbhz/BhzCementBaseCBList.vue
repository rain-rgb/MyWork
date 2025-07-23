<template>
<!-- 混凝土超标查询 -->
  <a-card :bordered="false">
    <a-row :gutter="24">
      <a-col :xl="4" :lg="7" :md="8" :sm="24" style="font-size:20px;">
        <a-button type="danger" ghost>
          未处理：{{wcz}}盘
        </a-button></a-col>
      <a-col :xl="4" :lg="7" :md="8" :sm="24" style="font-size:20px;">
        <a-button type="danger" ghost>
          待审核：{{yczwsh}}盘
        </a-button></a-col>
      <a-col :xl="4" :lg="7" :md="8" :sm="24" style="font-size:20px;">
        <a-button type="primary" ghost>
          已审核：{{ybh}}盘
        </a-button></a-col>
      <a-col :xl="4" :lg="7" :md="8" :sm="24" style="font-size:20px;">
        <a-button type="danger" >
          初：{{cj}}盘
        </a-button></a-col>
      <a-col :xl="4" :lg="7" :md="8" :sm="24" style="font-size:20px;">
        <a-button type="danger" >
          中：{{zj}}盘
        </a-button></a-col>
      <a-col :xl="4" :lg="7" :md="8" :sm="24" style="font-size:20px;">
        <a-button type="primary" >
          高：{{gj}}盘
        </a-button></a-col>
    </a-row>
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper" style="margin: 25px;margin-bottom: -25px">
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
            <a-form-item label="处置状态">
              <j-search-select-tag placeholder="请选择处置状态" v-model="queryParam.overproofStatus"
                                 :dictOptions="dictOption4"></j-search-select-tag>
            </a-form-item>
          </a-col>
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
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="切换工程">-->
<!--              <j-dict-select-tag placeholder="请选择工程" v-model="queryParam.poureLocation" dictCode="RCXM"></j-dict-select-tag>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
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
      <a-button @click="handleAdd" v-has="'hntbhzcbcx:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'hntbhzcbcx:dc'" icon="download" @click="handleExportXls('拌合站主表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'hntbhzcbcx:dr'" icon="import">导入</a-button>
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
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
      >
       <span slot="tags" slot-scope="tags, record">
        <a-tag color="green" v-if="record.overLevel == '0'">合格</a-tag>
        <a-tag color="orange" v-if="record.overLevel == '1'">初级超标</a-tag>
        <a-tag color="yellow" v-if="record.overLevel == '2'">中级超标</a-tag>
         <a-tag color="red" v-if="record.overLevel == '3'">高级超标</a-tag>
       </span>
        <span slot="tags1" slot-scope="tags1, record">
         <a-tag color="red" v-if="record.overproofStatus === 0">未处理</a-tag>
          <a-tag color="orange" v-if="record.overproofStatus === 10">监理核查中</a-tag>
          <a-tag color="green" v-if="record.overproofStatus === 20">已闭合</a-tag>
          <a-tag color="red" v-if="record.overproofStatus === 30">监理驳回</a-tag>
          <a-tag color="yellow" v-if="record.overproofStatus === 40">指挥部核查中</a-tag>
          <a-tag color="red" v-if="record.overproofStatus === 60">指挥部驳回</a-tag>
       </span>

        <span slot="handle" slot-scope="handle, record">
          <span v-if="record.overproofStatus === 20 && record.bhzCementOverHandler.handlePerson === null">系统自动闭合 </span>
          <span v-else>{{record.bhzCementOverHandler.handlePerson}}</span>
       </span>

<!--        <span slot="approval1" slot-scope="approval1, record">-->
<!--          <span v-if="record.overproofStatus === 20 && bhzCementOverHandler.approvalPerson === null && ( record.overLevel === '2' || record.overLevel === '3' )">系统自动闭合 </span>-->
<!--          <span  v-else>{{bhzCementOverHandler.approvalPerson}}</span>-->

<!--        </span>-->
        <span slot="formulaNo" slot-scope="formulaNo, record">
        <a-tag color="green" >{{record.formulaNo}}</a-tag>
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
<!--          <a v-has="'hntbhzcbcx:edit'" @click="handleEdit(record)">编辑</a>-->

<!--          <a-divider type="vertical" />-->
<!--          <a-dropdown>-->
<!--            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>-->
<!--            <a-menu slot="overlay">-->
<!--              <a-menu-item>-->
<!--               -->
<!--              </a-menu-item>-->
<!--              <a-menu-item v-has="'hntbhzcbcx:del'">-->
<!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
<!--                  <a >删除</a>-->
<!--                </a-popconfirm>-->
<!--              </a-menu-item>-->
<!--            </a-menu>-->
<!--          </a-dropdown>-->
        </span>

      </a-table>
    </div>
    <BhzCementBaseCBModal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import BhzCementBaseCBModal from './modules/BhzCementBaseCBModal'
import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
import '@/assets/less/TableExpand.less'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import Vue from 'vue'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { usershebeiList } from '@api/api'
import { getAction } from '@api/manage'
import { handertoken } from '@/mixins/getHanderToken'

export default {
  name: "BhzCementBaseCBList",
  mixins:[JeecgListMixin,handertoken],
  components: {
    BhzCementBaseCBModal,
    JSuperQuery
  },
  data () {
    return {
      flag: 0,
      bhz: 0,
      wcz: 0,
      yczwsh: 0,
      ybh: 0,
      cj: 0,
      zj: 0,
      gj: 0,
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      dictOption4: [{ text: '未处理', value: '0' },{ text: '已闭合', value: '20' },{ text: '监理驳回', value: '30' },{ text: '监理核查中', value: '10' },{ text: '指挥部驳回', value: '60' },{ text: '指挥部核查中', value: '40' }],
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
          dataIndex: 'shebeiNo',
          key:'shebeiNo',
        },
        {
          title:'浇筑部位',
          align:"center",
          dataIndex: 'poureLocation',
          key:'poureLocation',
        },
        {
          title:'最高超限率(%)',
          align:"center",
          dataIndex: 'additiveVariety',
          key:'additiveVariety',
        },
        {
          title:'超标等级',
          align:"center",
          dataIndex: 'overLevel',//overLevel_dictText
          key:'overLevel',
          scopedSlots: { customRender: 'tags' },
        },
        {
          title:'处理状态',
          align:"center",
          dataIndex: 'overproofStatus',
          key:'overproofStatus',
          scopedSlots: { customRender: 'tags1' },
        },
        {
          title:'处置人',
          align:"center",
          dataIndex: 'bhzCementOverHandler.handlePerson',
          key:'bhzCementOverHandler.handlePerson',
          scopedSlots: { customRender: 'handle' },
        },
        {
          title:'审核人',
          align:"center",
          dataIndex: 'bhzCementOverHandler.approvalPerson',
          key:'bhzCementOverHandler.approvalPerson',
          // scopedSlots: { customRender: 'approval1' },
        },
        {
          title:'生产方量',
          align:"center",
          dataIndex: 'estimateNumber',
          key:'estimateNumber'
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
        {
          title: '采集时间',
          align: 'center',
          dataIndex: 'collectDatetime',
          key:'collectDatetime',
        },
        {
          title: '配方号',
          align: 'center',
          dataIndex: 'formulaNo',
          scopedSlots: { customRender: 'formulaNo' },
        },
        {
          title:'操作人',
          align:"center",
          dataIndex: 'handlers',
          key:'handlers',
        },
        {
          title: '详情',
          dataIndex: 'action',
          align:"center",
          fixed:"right",
          width:100,
          scopedSlots: { customRender: 'action' },
        },
        // {
        //   title:'工程名称',
        //   align:"center",
        //   dataIndex: 'projectName',
        //   key:'projectName',
        // },
        // {
        //   title:'施工地点',
        //   align:"center",
        //   dataIndex: 'jobLocation',
        //   key:'jobLocation',
        // },
        // {
        //   title:'方量',
        //   align:"center",
        //   dataIndex: 'estimateNumber',
        //   key:'estimateNumber',
        // },
        // {
        //   title:'坍落度',
        //   align:"center",
        //   dataIndex: 'slump',
        //   key:'slump',
        // },
      ],
      isorter: {
        column: 'productDatetime',
        order: 'desc',
      },
      url: {
        list: "/hntbhz/bhzCementBase/list2",
        // list: "/hntbhz/bhzCementBase/listrc2",
        delete: "/hntbhz/bhzCementBase/delete",
        deleteBatch: "/hntbhz/bhzCementBase/deleteBatch",
        exportXlsUrl: "/hntbhz/bhzCementBase/exportXlsCB",
        importExcelUrl: "hntbhz/bhzCementBase/importExcel",
        djcount: '/hntbhz/bhzCementBase/djcount',
        czcount: '/hntbhz/bhzCementBase/czcount',
      },
      dictOptions:{},
      superFieldList:[],
    }
  },
  created() {
    this.getToken();
    this.getSuperFieldList();
    this.shebeiList();
    this.getdjcount()
    this.getczcount()
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
  methods: {
    getdjcount:function (){
      getAction(this.url.djcount)
        .then(res=>{
          let data = res.result
          data.forEach(item => {
            if (item.overLevel==1){
              this.cj = item.status
            }else if (item.overLevel==2){
              this.zj = item.status
            }else {
              this.gj = item.status
            }
          })
        })
    },
    getczcount:function (){
      getAction(this.url.czcount)
        .then(res=>{
          let data = res.result
          data.forEach(item => {
            if (item.overproofStatus==0){
              this.wcz = item.status
            }else if (item.overproofStatus==10){
              this.yczwsh = item.status
            }else if (item.overproofStatus==20){
              this.ybh = item.status
            }
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
      this.superFieldList = fieldList
    }
  },
  watch:{
    'queryParam.overLevel'(){
      this.queryParam.overproofStatus=""
      this.dictOption4 = []
      if(this.queryParam.overLevel==1){
          this.dictOption4.push({ text: '未处理', value: '0' },{ text: '已闭合', value: '20' })
      }else if (this.queryParam.overLevel==2){
          this.dictOption4.push({ text: '未处理', value: '0' },{ text: '已闭合', value: '20' },{ text: '监理驳回', value: '30' },{ text: '监理核查中', value: '10' })
      }else {
         this.dictOption4.push({ text: '未处理', value: '0' },{ text: '已闭合', value: '20' },{ text: '监理驳回', value: '30' },{ text: '监理核查中', value: '10' },{ text: '指挥部驳回', value: '60' },{ text: '指挥部核查中', value: '40' })
        
  }
}
},
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>