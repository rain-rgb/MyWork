<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.sbbh" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="车牌号">
              <a-input placeholder="请输入车牌号" v-model="queryParam.cph"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="出发时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.cftime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.cftime_end" :showTime="true"
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
      <a-button @click="handleAdd" type="primary" v-has="'wbsbdetail:add'" icon="plus">新增</a-button>
      <a-button @click="handlePrints" v-has="'wbsbdetail:print'" type="primary" icon="printer">打印</a-button>
      <a-button type="primary" v-has="'wbsbdetail:dc'" icon="download" @click="handleExportXls('电子锁详情数据表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'wbsbdetail:dr'" icon="import">导入</a-button>
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
        <span slot="zhuangtai" slot-scope="zhuangtai, record">
          <a-tag color="orange" v-if="record.zhuangtai === 0">未到达</a-tag>
          <a-tag color="green" v-if="record.zhuangtai === 1">到达</a-tag>
        </span>
        <span slot="sfbj" slot-scope="sfbj, record">
          <a-tag color="red" v-if="record.sfbj === '0'">报警</a-tag>
          <a-tag color="green" v-if="record.sfbj === '1'">不报警</a-tag>
        </span>
        <span slot="fsxx" slot-scope="fsxx, record">
          <a-tag color="orange" v-if="record.fsxx === '0'">未发送</a-tag>
          <a-tag color="green" v-if="record.fsxx === '1'">已发送</a-tag>
        </span>
        <span slot="sfcssj" slot-scope="sfcssj, record">
          <a-tag color="green" v-if="record.sfcssj === '0'">已拿到</a-tag>
          <a-tag color="orange" v-if="record.sfcssj === '1'">未拿到</a-tag>
        </span>
        <span slot="isjiesuo" slot-scope="isjiesuo, record">
          <a-tag color="blue" v-if="record.isjiesuo === 0">未到达</a-tag>
          <a-tag color="green" v-if="record.isjiesuo === 1">已到达</a-tag>
          <a-tag color="orange" v-if="record.isjiesuo === 2">已解锁</a-tag>
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
          <a v-has="'wbsbdetail:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="handleDetail1(record)">轨迹查看</a>
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
    <clxx-realdata-modal ref="modalFormC" @ok="modalFormOk"></clxx-realdata-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WbshebeiDetailModal from './modules/WbshebeiDetailModal'
  import Vue from 'vue'
  import { usershebeiList } from '@api/api'
  import { handertoken } from '@/mixins/getHanderToken'
  import WbshebeiDetailPrintModel from '@views/car/wbshebeidetail/modules/WbshebeiDetailPrintModel'
  import ClxxRealdataModal from './modules/ClxxRealdataModal'

  export default {
    name: 'WbshebeiDetailList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      WbshebeiDetailPrintModel,
      WbshebeiDetailModal,
      ClxxRealdataModal,
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '电子锁详情数据表管理页面',
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
            dataIndex: 'userdepartid'
          },
          {
            title:'电子锁锁号',
            align:"center",
            dataIndex: 'sbbh_dictText'
          },
          {
            title:'到达时间',
            align:"center",
            dataIndex: 'ddtime'
          },
          {
            title:'出发时间',
            align:"center",
            dataIndex: 'cftime'
          },
          {
            title:'单号',
            align:"center",
            dataIndex: 'danhao'
          },
          {
            title:'车牌号',
            align:"center",
            dataIndex: 'cph'
          },
          {
            title:'是否到达',
            align:"center",
            dataIndex: 'zhuangtai',
            scopedSlots: { customRender: 'zhuangtai' },
          },
          {
            title:'是否合格',
            align:"center",
            dataIndex: 'sfhg'
          },
          {
            title:'供货单位',
            align:"center",
            dataIndex: 'ghdw'
          },
          {
            title:'材料',
            align:"center",
            dataIndex: 'cailiao'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'guige'
          },
          // {
          //   title:'发车纬度',
          //   align:"center",
          //   dataIndex: 'fclat'
          // },
          // {
          //   title:'发车经度',
          //   align:"center",
          //   dataIndex: 'fclng'
          // },
          // {
          //   title:'到达纬度',
          //   align:"center",
          //   dataIndex: 'ddlat'
          // },
          // {
          //   title:'到达经度',
          //   align:"center",
          //   dataIndex: 'ddlng'
          // },
          {
            title:'负责人',
            align:"center",
            dataIndex: 'fzr'
          },
          // {
          //   title:'是否报警',
          //   align:"center",
          //   dataIndex: 'sfbj',
          //   scopedSlots: { customRender: 'sfbj' },
          // },
          // {
          //   title:'发送到达信息',
          //   align:"center",
          //   dataIndex: 'fsxx',
          //   scopedSlots: { customRender: 'fsxx' },
          // },
          {
            title:'发车数量',
            align:"center",
            dataIndex: 'jcsl'
          },
          // {
          //   title:'进场过磅数量',
          //   align:"center",
          //   dataIndex: 'jcgbl'
          // },
          // {
          //   title:'出场过磅数量',
          //   align:"center",
          //   dataIndex: 'ccgbl'
          // },
          // {
          //   title:'当前库存',
          //   align:"center",
          //   dataIndex: 'dqkc'
          // },
          // {
          //   title:'实际消耗数量',
          //   align:"center",
          //   dataIndex: 'sjxhsl'
          // },
          // {
          //   title:'理论消耗数量',
          //   align:"center",
          //   dataIndex: 'llxhsl'
          // },
          // {
          //   title:'理论库存',
          //   align:"center",
          //   dataIndex: 'llkc'
          // },
          // {
          //   title:'理论消耗',
          //   align:"center",
          //   dataIndex: 'llxh'
          // },
          // {
          //   title:'理论配合比',
          //   align:"center",
          //   dataIndex: 'llphb'
          // },
          // {
          //   title:'是否拿到地磅数据',
          //   align:"center",
          //   dataIndex: 'sfcssj',
          //   scopedSlots: { customRender: 'sfcssj' },
          // },
          {
            title:'目的地',
            align:"center",
            dataIndex: 'mdd'
          },
          // {
          //   title:'上锁照片',
          //   align:"center",
          //   dataIndex: 'imgfile',
          //   scopedSlots: { customRender: 'imgSlot' },
          // },
          {
            title:'铅封号',
            align:"center",
            dataIndex: 'qianfenghao'
          },

          {
            title:'是否解锁',
            align:"center",
            dataIndex: 'isjiesuo',
            scopedSlots: { customRender: 'isjiesuo' },
          },
          {
            title:'解锁时间',
            align:"center",
            dataIndex: 'timestatime'
          },
          {
            title:'解锁人',
            align:"center",
            dataIndex: 'name'
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
          list: "/wbshebeidetail/wbshebeiDetail/list",
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
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      handleDetail1: function (record) {
        console.log(record)
        this.$refs.modalFormC.detail(record);
        this.$refs.modalFormC.title = "轨迹回放";
        this.$refs.modalFormC.disableSubmit = false;
      },
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
      handlePrints(){
        console.log("调用")
        console.log("selectedRowKeys",this.selectedRowKeys)
        if (this.selectedRowKeys.length===1){
          this.$refs.modalForm1.show(this.selectedRowKeys)
        }else {
          this.$message.error("请选择一条电子锁发车单进行打印")
        }

      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'sbbh',text:'设备编号',dictCode:''})
        fieldList.push({type:'date',value:'ddtime',text:'到达时间'})
        fieldList.push({type:'date',value:'cftime',text:'出发时间'})
        fieldList.push({type:'string',value:'danhao',text:'单号',dictCode:''})
        fieldList.push({type:'string',value:'cph',text:'车牌号',dictCode:''})
        fieldList.push({type:'int',value:'zhuangtai',text:'是否到达0为未到达，1为到达',dictCode:''})
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