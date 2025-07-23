<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeibianh" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="工程名称">
              <a-input placeholder="请输入工程名称" v-model="queryParam.projectname"></a-input>
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
      <a-button @click="handleAdd" v-has="'zlrwd:add'" type="primary" icon="plus">新增张拉任务单</a-button>
      <a-button type="primary" v-has="'zlrwd:dc'" icon="download" @click="handleExportXls('张拉任务单')">导出</a-button>
      <a-upload name="file" v-has="'zlrwd:dr'" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
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
         <span slot="uuid" slot-scope="uuid, record">
         <a-tag color="blue" @click="zhanglamessageList(record.uuid)">{{record.uuid}}</a-tag>
         </span>
        <span slot="wcstatus" slot-scope="wcstatus, record">
        <a-tag color="red" v-if="record.wcstatus == '0'">未完成</a-tag>
         <a-tag color="green" v-if="record.wcstatus == '1'">已完成</a-tag>
       </span>
       <span slot="status" slot-scope="status, record">
        <a-tag color="red" v-if="record.status == '0'">未完成</a-tag>
         <a-tag color="green" v-if="record.status == '1'">已完成</a-tag>
       </span>
        <span slot="status2" slot-scope="status2, record">
        <a-tag color="red" v-if="record.status2 == '0'">未完成</a-tag>
         <a-tag color="green" v-if="record.status2 == '1'">已完成</a-tag>
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
          <a v-has="'zlrwd:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" v-has="'zlrwd:edit'" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'zlrwd:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <TrZhanglaMModal ref="TrZhanglaMModal" ></TrZhanglaMModal>
    <tr-zhangla-renwudan-modal ref="modalForm" @ok="modalFormOk"></tr-zhangla-renwudan-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import TrZhanglaRenwudanModal from './modules/TrZhanglaRenwudanModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { usershebeiList } from '@api/api'
  import TrZhanglaMModal from '@views/zl/modules/TrZhanglaMModal'
  import Vue from 'vue'
  export default {
    name: 'TrZhanglaRenwudanList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      TrZhanglaRenwudanModal,
      JSuperQuery,
      TrZhanglaMModal
    },
    data () {
      return {
        dictOption: [],
        selectValue:'',
        description: '张拉任务单管理页面',
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
            title:'任务单号',
            align:"center",
            dataIndex: 'uuid',
            scopedSlots: { customRender: 'uuid' },
          },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'projectname'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'shebeibianh_dictText'
          },
          {
            title:'施工部位名称',
            align:"center",
            width: 200,
            dataIndex: 'sgbwname'
          },
          {
            title:'一次张拉力',
            align:"center",
            dataIndex: 'sjzll'
          },
          {
            title:'二次张拉力',
            align:"center",
            dataIndex: 'sjzll2'
          },
          {
            title:'一次张拉状态',
            align:"center",
            dataIndex: 'status',
            scopedSlots: { customRender: 'status' },
          },
          {
            title:'二次张拉状态',
            align:"center",
            dataIndex: 'status2',
            scopedSlots: { customRender: 'status2' },
          },
          {
            title:'任务单状态',
            align:"center",
            dataIndex: 'wcstatus',
            scopedSlots: { customRender: 'wcstatus' },
          },
          {
            title:'一次张拉日期',
            align:"center",
            dataIndex: 'zldate'
          },
          {
            title:'二次张拉日期',
            align:"center",
            dataIndex: 'zldate2'
          },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'createTime'
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy_dictText'
          },
          {
            title:'所属组织机构',
            align:"center",
            dataIndex: 'sysOrgCode_dictText'
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
          list: "/trzhanglarenwudan/trZhanglaRenwudan/list",
          delete: "/trzhanglarenwudan/trZhanglaRenwudan/delete",
          deleteBatch: "/trzhanglarenwudan/trZhanglaRenwudan/deleteBatch",
          exportXlsUrl: "/trzhanglarenwudan/trZhanglaRenwudan/exportXls",
          importExcelUrl: "trzhanglarenwudan/trZhanglaRenwudan/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.shebeiList();
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      zhanglamessageList:function (e){
        this.$refs.TrZhanglaMModal.title="张拉试验检测数据详情";
        this.$refs.TrZhanglaMModal.callzhanglarenwudown(e);
        // this.$message.success(e)
      },
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'9'
        }).then(res=>{
          //console.log(res)
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })

        })
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'uuid',text:'任务单号',dictCode:''})
        fieldList.push({type:'string',value:'projectname',text:'工程名称',dictCode:''})
        fieldList.push({type:'string',value:'girderplace',text:'梁场',dictCode:''})
        fieldList.push({type:'string',value:'component',text:'构件名称（梁名称）',dictCode:''})
        fieldList.push({type:'string',value:'sgbwuuid',text:'施工部位',dictCode:''})
        fieldList.push({type:'string',value:'sgbwname',text:'施工部位名称',dictCode:''})
        fieldList.push({type:'string',value:'pedestal',text:'台座',dictCode:''})
        fieldList.push({type:'string',value:'status',text:'一次张拉任务状态码：0：未使用  1：已使用',dictCode:''})
        fieldList.push({type:'string',value:'zldate',text:'张拉日期',dictCode:''})
        fieldList.push({type:'string',value:'createTime',text:'创建时间',dictCode:''})
        fieldList.push({type:'string',value:'updateTime',text:'修改时间',dictCode:''})
        fieldList.push({type:'string',value:'departid',text:'组织机构',dictCode:''})
        fieldList.push({type:'string',value:'sjzll',text:'设计张拉力',dictCode:''})
        fieldList.push({type:'string',value:'createBy',text:'创建人',dictCode:''})
        fieldList.push({type:'string',value:'sysOrgCode',text:'sysOrgCode',dictCode:''})
        fieldList.push({type:'string',value:'zldate2',text:'二次张拉日期',dictCode:''})
        fieldList.push({type:'string',value:'sjzll2',text:'二次张拉力',dictCode:''})
        fieldList.push({type:'string',value:'status2',text:'二次张拉任务状态码：0：未使用  1：已使用',dictCode:''})
        fieldList.push({type:'string',value:'departname',text:'departname',dictCode:''})
        fieldList.push({type:'string',value:'shebeibianh',text:'shebeibianh',dictCode:''})
        fieldList.push({type:'string',value:'yylgczlgg',text:'预应力钢材种类规格',dictCode:''})
        fieldList.push({type:'string',value:'yylgccdpp',text:'预应力钢材产地品牌',dictCode:''})
        fieldList.push({type:'string',value:'yylgcbh',text:'预应力钢材检验单编号',dictCode:''})
        fieldList.push({type:'string',value:'mjjzlgg',text:'锚夹具种类规格',dictCode:''})
        fieldList.push({type:'string',value:'mjjcdpp',text:'锚夹具产地品牌',dictCode:''})
        fieldList.push({type:'string',value:'mjjbh',text:'锚夹具检验单编号',dictCode:''})
        fieldList.push({type:'string',value:'mdbzlgg',text:'锚垫板种类规格',dictCode:''})
        fieldList.push({type:'string',value:'mdbcdpp',text:'锚垫板产地品牌',dictCode:''})
        fieldList.push({type:'string',value:'mdbbh',text:'锚垫板检验单编号',dictCode:''})
        fieldList.push({type:'string',value:'lbgd',text:'梁板理论上拱度',dictCode:''})
        fieldList.push({type:'string',value:'hntsjqd',text:'混凝土的设计强度',dictCode:''})
        fieldList.push({type:'string',value:'zllwc',text:'两端千斤顶的张拉力误差指标',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>