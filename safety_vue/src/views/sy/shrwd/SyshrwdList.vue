<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
<!--    <div class="table-page-search-wrapper">-->
<!--      <a-form layout="inline" @keyup.enter.native="searchQuery">-->
<!--        <a-row :gutter="24">-->
<!--        </a-row>-->
<!--      </a-form>-->
<!--    </div>-->

    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料类型">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.cailiaotype" dictCode="nodeType"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="任务单状态">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.status" dictCode="shStatus"></j-dict-select-tag>
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
      <a-button @click="handleAdd" v-has="'shrwd:add'" type="primary" icon="plus">新增</a-button>
<!--      <a-button type="primary" icon="download" @click="handleExportXls('试验收货任务单')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
      <!-- 高级查询区域 -->
<!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
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
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >

          <span slot="tags" slot-scope="tags, record">
        <a-tag color="green" v-if="record.status == '3'">{{record.status_dictText}}</a-tag>
            <a-tag color="blue" v-if="record.status == '2'">{{record.status_dictText}}</a-tag>
        <a-tag color="orange" v-if="record.status == '0'">{{record.status_dictText}}</a-tag>
        <a-tag color="yellow" v-if="record.status == '1'">{{record.status_dictText}}</a-tag>
         <a-tag color="red" v-if="record.status == '4'">{{record.status_dictText}}</a-tag>
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
          <a v-has="'shrwd:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'shrwd:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
               <a-menu-item v-has="'shrwd:sh'">
                <a-popconfirm title="确认通过该送货任务单吗?" @confirm="() => jlhandleDetail(record)">
                  <a>审核</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <syshrwd-modal ref="modalForm" @ok="modalFormOk"></syshrwd-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyshrwdModal from './modules/SyshrwdModal'
  import {getAction, putAction} from "../../../api/manage";

  export default {
    name: 'SyshrwdList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyshrwdModal
    },
    data () {
      return {
        description: '试验收货任务单管理页面',
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
            title:'送货任务单号',
            align:"center",
            dataIndex: 'shrwd'
          },
          {
            title:'目的地',
            align:"center",
            dataIndex: 'mudidi_dictText'
          },

          // {
          //   title:'guid',
          //   align:"center",
          //   dataIndex: 'guid'
          // },
          {
            title:'材料类型',
            align:"center",
            dataIndex: 'cailiaotype_dictText'
          },
          {
            title:'规格型号',
            align:"center",
            dataIndex: 'ggxh'
          },
          {
            title:'数量（吨）',
            align:"center",
            dataIndex: 'shuliang'
          },

          {
            title:'要求送达时间',
            align:"center",
            dataIndex: 'endtime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'供应商',
            align:"center",
            dataIndex: 'gysguid_dictText'
          },
          {
            title:'供应商联系人',
            align:"center",
            dataIndex: 'lianxr'
          },
          // {
          //   title:'材料编号',
          //   align:"center",
          //   dataIndex: 'cailaiobh'
          // },
          // {
          //   title:'批次号',
          //   align:"center",
          //   dataIndex: 'picihao'
          // },
          //
          // {
          //   title:'预计车次数',
          //   align:"center",
          //   dataIndex: 'cartimes'
          // },
          {
            title:'状态',//（）
            align:"center",
            dataIndex: 'status_dictText',
            scopedSlots: {customRender: 'tags'},
          },
          {
            title:'采购人',
            align:"center",
            dataIndex: 'createby_dictText'
          },
          {
            title:'采购时间',
            align:"center",
            dataIndex: 'caeatetime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          // {
          //   title:'厂家确认时间',
          //   align:"center",
          //   dataIndex: 'oktime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          // {
          //   title:'厂家确认人',
          //   align:"center",
          //   dataIndex: 'okby'
          // },

          //
          // {
          //   title:'单位（吨，kg）',
          //   align:"center",
          //   dataIndex: 'danwei'
          // },
          // {
          //   title:'备注',
          //   align:"center",
          //   dataIndex: 'beizhu'
          // },
          // {
          //   title:'质保单',
          //   align:"center",
          //   dataIndex: 'zhibaodan'
          // },


          // {
          //   title:'收货部门',
          //   align:"center",
          //   dataIndex: 'shouhuodp'
          // },

          // {
          //   title:'收货时间',
          //   align:"center",
          //   dataIndex: 'shouhuotime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          // {
          //   title:'收料检验员',
          //   align:"center",
          //   dataIndex: 'shouliaojy'
          // },
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
          list: "/syshrwd/syshrwd/list",
          delete: "/syshrwd/syshrwd/delete",
          deleteBatch: "/syshrwd/syshrwd/deleteBatch",
          exportXlsUrl: "/syshrwd/syshrwd/exportXls",
          importExcelUrl: "syshrwd/syshrwd/importExcel",
          jlsh:"/syshrwd/syshrwd/jledit",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {

      jlhandleDetail(record){
        if(record.status != '4'){
          this.$message.info("已通过审核！无法再次审核")
        }else{
          putAction(this.url.jlsh, record).then(res =>{
            if(res.success){
              this.$message.success("审核成功")
            }else{
              this.$message.error("审核失败")
            }
          })
          this.routeReload();
        }


      },
      routeReload() {//刷新页面
        this.reloadFlag = false
        let ToggleMultipage = 'ToggleMultipage'
        this.$store.dispatch(ToggleMultipage, false)
        this.$nextTick(() => {
          this.$store.dispatch(ToggleMultipage, true)
          this.reloadFlag = true
        })
        console.log("刷新页面")
      },

      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'shrwd',text:'送货任务单号'})
        fieldList.push({type:'int',value:'guid',text:'guid'})
        fieldList.push({type:'string',value:'cailiaotype',text:'材料类型'})
        fieldList.push({type:'string',value:'ggxh',text:'材料规格型号'})
        fieldList.push({type:'string',value:'cailaiobh',text:'材料编号'})
        fieldList.push({type:'string',value:'picihao',text:'批次号'})
        fieldList.push({type:'date',value:'endtime',text:'要求到货时间'})
        fieldList.push({type:'string',value:'cartimes',text:'预计车次数'})
        fieldList.push({type:'string',value:'status',text:'状态（0厂家未确认；1厂家确认回函；2已发货；3已签收）'})
        fieldList.push({type:'string',value:'createby',text:'送货任务单创建人'})
        fieldList.push({type:'date',value:'caeatetime',text:'送货任务单创建时间'})
        fieldList.push({type:'date',value:'oktime',text:'厂家确认时间'})
        fieldList.push({type:'string',value:'okby',text:'厂家确认人'})
        fieldList.push({type:'string',value:'shuliang',text:'数量（单位吨）'})
        fieldList.push({type:'string',value:'mudidi',text:'目的地'})
        fieldList.push({type:'string',value:'danwei',text:'单位（吨，kg）'})
        fieldList.push({type:'string',value:'beizhu',text:'备注'})
        fieldList.push({type:'string',value:'zhibaodan',text:'质保单'})
        fieldList.push({type:'string',value:'lianxr',text:'厂家联系人'})
        fieldList.push({type:'string',value:'jd',text:'经度'})
        fieldList.push({type:'string',value:'wd',text:'纬度'})
        fieldList.push({type:'string',value:'shouhuodp',text:'收货部门'})
        fieldList.push({type:'string',value:'sysorgcode',text:'订单所属组织机构'})
        fieldList.push({type:'date',value:'shouhuotime',text:'收货时间'})
        fieldList.push({type:'string',value:'shouliaojy',text:'收料检验员'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>

</style>