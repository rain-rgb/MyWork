<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="车牌号">
              <j-search-select-tag placeholder="请选择车牌号" v-model="queryParam.cph" :dictOptions="dictOption" >
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
      <a-button @click="handleAdd" type="primary" v-has="'wbsbdetail:add'" icon="plus">新增</a-button>
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

        <span slot="action" slot-scope="text, record">
          <a v-has="'wbsbdetail:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'wbsbdetail:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <wbshebei-detail-print-model ref="modalForm1"></wbshebei-detail-print-model>
    <wbshebei-detail-modal ref="modalForm" @ok="modalFormOk"></wbshebei-detail-modal>
  </a-card>
</template>

<script>

import '@assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import WbshebeiDetailModal from './modules/WbshebeiDetailModal'
import Vue from 'vue'
import { usershebeiList } from '@api/api'
import { handertoken } from '@/mixins/getHanderToken'
import WbshebeiDetailPrintModel from '@views/car/wbshebeidetail/modules/WbshebeiDetailPrintModel'
export default {
  name: 'WbshebeiDetailToList',
  mixins:[JeecgListMixin, mixinDevice,handertoken],
  components: {
    WbshebeiDetailPrintModel,
    WbshebeiDetailModal
  },
  data () {
    return {
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      description: '车辆到达目的地次数',
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
          title:'所属标段',
          align:"center",
          dataIndex: 'userdepartid_dictText'
        },
        {
          title:'出发时间',
          align:"center",
          dataIndex: 'cftime'
        },
        {
          title:'负责人',
          align:"center",
          dataIndex: 'fzr'
        },
        {
          title:'车牌号',
          align:"center",
          dataIndex: 'cph'
        },
        {
          title:'目的地',
          align:"center",
          dataIndex: 'mdd'
        },
        {
          title:'到达次数',
          align:"center",
          dataIndex: 'fctype',
        },
      ],
      url: {
        list: "/wbshebeidetail/wbshebeiDetail/list7",
        delete: "/wbshebeidetail/wbshebeiDetail/delete",
        deleteBatch: "/wbshebeidetail/wbshebeiDetail/deleteBatch",
        exportXlsUrl: "/wbshebeidetail/wbshebeiDetail/exportXls",
        importExcelUrl: "wbshebeidetail/wbshebeiDetail/importExcel",
        printUrl:'jmreport/view/656652541891207168',
      },
      dictOptions:{},
      superFieldList:[],
    }
  },
  created() {
    this.getToken()
    this.getSuperFieldList();
    this.shebeiList();

  },

  methods: {
    shebeiList:function (){
      var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
      usershebeiList({
        sys_depart_orgcode:sys_depart_orgcode,
        sbtypes:'55'
      }).then(res=>{
        this.dictOption=[];
        let result=res.result;
        result.forEach(re=>{
          this.dictOption.push({text:re.sbname,value:re.sbjno})
        })
        //console.log(res)
      })
    },

    getSuperFieldList(){
      let fieldList=[];
      fieldList.push({type:'string',value:'sbbh',text:'设备编号',dictCode:''})
      fieldList.push({type:'date',value:'ddtime',text:'到达时间'})
      fieldList.push({type:'date',value:'cftime',text:'出发时间'})
      fieldList.push({type:'string',value:'danhao',text:'单号',dictCode:''})
      fieldList.push({type:'string',value:'cph',text:'车牌号',dictCode:''})
      fieldList.push({type:'int',value:'zhuangtai',text:'是否到达0为未到达，1为到达',dictCode:''})
      fieldList.push({type:'int',value:'ddcs',text:'到达次数',dictCode:''})
      fieldList.push({type:'string',value:'sfhg',text:'是否合格',dictCode:''})
      fieldList.push({type:'string',value:'ghdw',text:'0-安徽环宇公路沥青材料有限公司;1-上海海太工程科技有限公司;2-湖南路安达沥青技术有限公司',dictCode:''})
      fieldList.push({type:'string',value:'cailiao',text:'材料',dictCode:''})
      fieldList.push({type:'string',value:'guige',text:'规格',dictCode:''})
      fieldList.push({type:'string',value:'fclat',text:'发车纬度',dictCode:''})
      fieldList.push({type:'string',value:'fclng',text:'发车经度',dictCode:''})
      fieldList.push({type:'string',value:'ddlat',text:'到达纬度',dictCode:''})
      fieldList.push({type:'string',value:'ddlng',text:'到达经度',dictCode:''})
      fieldList.push({type:'string',value:'fzr',text:'负责人',dictCode:''})
      fieldList.push({type:'string',value:'sfbj',text:'0为报警 1为不报警',dictCode:''})
      fieldList.push({type:'string',value:'fsxx',text:'0为未发送1为已发送 发送到达信息',dictCode:''})
      fieldList.push({type:'string',value:'jcsl',text:'发车数量',dictCode:''})
      fieldList.push({type:'string',value:'jcgbl',text:'进场过磅数量',dictCode:''})
      fieldList.push({type:'string',value:'ccgbl',text:'出场过磅数量',dictCode:''})
      fieldList.push({type:'string',value:'dqkc',text:'当前库存',dictCode:''})
      fieldList.push({type:'string',value:'sjxhsl',text:'实际消耗数量',dictCode:''})
      fieldList.push({type:'string',value:'llxhsl',text:'理论消耗数量由理论配合比和工程任务单计算得出',dictCode:''})
      fieldList.push({type:'string',value:'llkc',text:'理论库存 = 实际进场 - 理论消耗',dictCode:''})
      fieldList.push({type:'string',value:'llxh',text:'理论消耗',dictCode:''})
      fieldList.push({type:'string',value:'llphb',text:'理论配合比',dictCode:''})
      fieldList.push({type:'string',value:'sfcssj',text:'0为已拿到地磅数据1为未拿到',dictCode:''})
      fieldList.push({type:'string',value:'mdd',text:'目的地',dictCode:''})
      fieldList.push({type:'string',value:'imgfile',text:'照片地址',dictCode:''})
      fieldList.push({type:'string',value:'qianfenghao',text:'铅封号',dictCode:''})
      fieldList.push({type:'string',value:'userdepartid',text:'所属标段',dictCode:''})
      fieldList.push({type:'int',value:'isjiesuo',text:'0 未到达  1已到达 2已解锁',dictCode:''})
      fieldList.push({type:'date',value:'timestatime',text:'解锁时间'})
      fieldList.push({type:'string',value:'name',text:'解锁人',dictCode:''})
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>