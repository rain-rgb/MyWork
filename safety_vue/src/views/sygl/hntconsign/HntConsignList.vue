<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label=委托编号>
              <a-input placeholder="请输入委托编号" v-model="queryParam.wtbh"></a-input>
            </a-form-item>
          </a-col>
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="试验状态">-->
<!--              <j-dict-select-tag placeholder="请选择" v-model="queryParam.cstatus" dictCode="cstatus"></j-dict-select-tag>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="试验类型">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.syxm" dictCode="SYLX"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="取样时间范围">
              <j-date placeholder="开始取样时间" v-model="queryParam.qyrq_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束取样时间" v-model="queryParam.qyrq_end" :showTime="true"
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
      <a-button @click="handleAdd" type="primary" v-has="'hntconsign:add'" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" v-has="'hntconsign:dc'" @click="handleExportXls('混凝土见证取样表信息')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'hntconsign:dr'" icon="import">导入</a-button>
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
          <a v-has="'hntconsign:edit'" @click="handleEdit(record)">编辑</a>

<!--          <a-divider type="vertical" />-->
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'hntconsign:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <hnt-consign-modal ref="modalForm" @ok="modalFormOk"></hnt-consign-modal>
  </a-card>
</template>

<script>

  import '@assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import HntConsignModal from './modules/HntConsignModal'
  import JSuperQuery from '@comp/jeecg/JSuperQuery.vue'

  export default {
    name: 'HntConsignList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      HntConsignModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue: '',
        dictOption:[],
        description: '混凝土见证取样表信息管理页面',
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
          // {
          //   title:'委托ID--guid',
          //   align:"center",
          //   dataIndex: 'wtid'
          // },
          {
            title:'委托编号',
            align:"center",
            dataIndex: 'wtbh'
          },
          {
            title:'样品名称',
            align:"center",
            dataIndex: 'ypmc'
          },
          // {
          //   title:'产地厂名（生产厂家）',
          //   align:"center",
          //   dataIndex: 'cdcm'
          // },
          // {
          //   title:'代表数量',
          //   align:"center",
          //   dataIndex: 'sysl'
          // },
          // {
          //   title:'取样地点、取样位置',
          //   align:"center",
          //   dataIndex: 'qydd'
          // },
          // {
          //   title:'样品描述',
          //   align:"center",
          //   dataIndex: 'ypms'
          // },
          {
            title:'取样日期',
            align:"center",
            dataIndex: 'qyrq'
          },
          {
            title:'取样人',
            align:"center",
            dataIndex: 'qyr'
          },
          // {
          //   title:'配比通知单ID',
          //   align:"center",
          //   dataIndex: 'phbtzdid'
          // },
          // {
          //   title:'配比通知单编号',
          //   align:"center",
          //   dataIndex: 'phbtzdbh'
          // },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'gcmc'
          },
          {
            title:'施工部位',
            align:"center",
            dataIndex: 'sgbw'
          },
          // {
          //   title:'试验类型（1：压力试验，2：万能实验）',
          //   align:"center",
          //   dataIndex: 'testtype'
          // },
          // {
          //   title:'试验项目编号',
          //   align:"center",
          //   dataIndex: 'syxm'
          // },
          {
            title:'试验项目名称',
            align:"center",
            dataIndex: 'syxmmc'
          },
          {
            title:'样品试验组数',
            align:"center",
            dataIndex: 'sysuliang'
          },
          // {
          //   title:'强度等级（混凝土）',
          //   align:"center",
          //   dataIndex: 'qddj'
          // },
          // {
          //   title:'混凝土种类（混凝土）',
          //   align:"center",
          //   dataIndex: 'hntzl'
          // },
          // {
          //   title:'搅拌方式（混凝土）',
          //   align:"center",
          //   dataIndex: 'jbfs'
          // },
          // {
          //   title:'样品龄期（混凝土）',
          //   align:"center",
          //   dataIndex: 'yplq'
          // },
          // {
          //   title:'样品备注',
          //   align:"center",
          //   dataIndex: 'sybz'
          // },
          // {
          //   title:'砂浆种类',
          //   align:"center",
          //   dataIndex: 'sjzl'
          // },
          // {
          //   title:'钢筋直径',
          //   align:"center",
          //   dataIndex: 'gjzj'
          // },
          // {
          //   title:'钢筋种类',
          //   align:"center",
          //   dataIndex: 'gjzl'
          // },
          // {
          //   title:'委托单位',
          //   align:"center",
          //   dataIndex: 'wtdw'
          // },
          // {
          //   title:'试验编号',
          //   align:"center",
          //   dataIndex: 'sybh'
          // },
          // {
          //   title:'试验单位',
          //   align:"center",
          //   dataIndex: 'sydw'
          // },
          // {
          //   title:'规格种类',
          //   align:"center",
          //   dataIndex: 'ggzl'
          // },
          // {
          //   title:'单位数量',
          //   align:"center",
          //   dataIndex: 'ddsl'
          // },
          // {
          //   title:'检验标准',
          //   align:"center",
          //   dataIndex: 'jybz'
          // },
          // {
          //   title:'ysyj',
          //   align:"center",
          //   dataIndex: 'ysyj'
          // },
          // {
          //   title:'试验日期',
          //   align:"center",
          //   dataIndex: 'syrq'
          // },
          // {
          //   title:'取证见证人',
          //   align:"center",
          //   dataIndex: 'qyjzr'
          // },
          // {
          //   title:'委托人',
          //   align:"center",
          //   dataIndex: 'wtr'
          // },
          // {
          //   title:'委托单位负责人',
          //   align:"center",
          //   dataIndex: 'wtfzr'
          // },
          // {
          //   title:'地址及邮件',
          //   align:"center",
          //   dataIndex: 'zzyb'
          // },
          // {
          //   title:'联系电话',
          //   align:"center",
          //   dataIndex: 'lldh'
          // },
          // {
          //   title:'收样人',
          //   align:"center",
          //   dataIndex: 'syr'
          // },
          // {
          //   title:'预计报告发出日期',
          //   align:"center",
          //   dataIndex: 'yjbgrq'
          // },
          // {
          //   title:'sfsyj',
          //   align:"center",
          //   dataIndex: 'sfsyj'
          // },
          // {
          //   title:'现场是否完成试验0：未完成1：已完成 默认给0',
          //   align:"center",
          //   dataIndex: 'mstatus'
          // },
          // {
          //   title:'外键，组织机构id',
          //   align:"center",
          //   dataIndex: 'departid'
          // },
          // {
          //   title:'外键，组织机构编码',
          //   align:"center",
          //   dataIndex: 'orgcode'
          // },
          // {
          //   title:'二维码，多项以逗号隔开，最后一个二维码不用加逗号',
          //   align:"center",
          //   dataIndex: 'qrcode'
          // },
          // {
          //   title:'samplejidu',
          //   align:"center",
          //   dataIndex: 'samplejidu'
          // },
          // {
          //   title:'sampleweidu',
          //   align:"center",
          //   dataIndex: 'sampleweidu'
          // },
          // {
          //   title:'ts',
          //   align:"center",
          //   dataIndex: 'ts'
          // },
          // {
          //   title:'试验尺寸',
          //   align:"center",
          //   dataIndex: 'chicun'
          // },
          // {
          //   title:'判断试件的状态开始时间',
          //   align:"center",
          //   dataIndex: 'starttime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          // {
          //   title:'判断试件的状态结束时间',
          //   align:"center",
          //   dataIndex: 'endtime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
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
          list: "/hntconsign/hntConsign/list",
          delete: "/hntconsign/hntConsign/delete",
          deleteBatch: "/hntconsign/hntConsign/deleteBatch",
          exportXlsUrl: "/hntconsign/hntConsign/exportXls",
          importExcelUrl: "hntconsign/hntConsign/importExcel",

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
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'wtid',text:'委托ID--guid',dictCode:''})
        fieldList.push({type:'string',value:'wtbh',text:'委托编号',dictCode:''})
        fieldList.push({type:'string',value:'ypmc',text:'样品名称',dictCode:''})
        fieldList.push({type:'string',value:'cdcm',text:'产地厂名（生产厂家）',dictCode:''})
        fieldList.push({type:'string',value:'sysl',text:'代表数量',dictCode:''})
        fieldList.push({type:'string',value:'qydd',text:'取样地点\取样位置',dictCode:''})
        fieldList.push({type:'string',value:'ypms',text:'样品描述',dictCode:''})
        fieldList.push({type:'string',value:'qyrq',text:'取样日期',dictCode:''})
        fieldList.push({type:'string',value:'qyr',text:'取样人',dictCode:''})
        fieldList.push({type:'string',value:'phbtzdid',text:'配比通知单ID',dictCode:''})
        fieldList.push({type:'string',value:'phbtzdbh',text:'配比通知单编号',dictCode:''})
        fieldList.push({type:'string',value:'gcmc',text:'工程名称',dictCode:''})
        fieldList.push({type:'string',value:'sgbw',text:'施工部位',dictCode:''})
        fieldList.push({type:'int',value:'testtype',text:'试验类型（1：压力试验，2：万能实验）',dictCode:''})
        fieldList.push({type:'string',value:'syxm',text:'试验项目编号',dictCode:''})
        fieldList.push({type:'string',value:'syxmmc',text:'试验项目名称',dictCode:''})
        fieldList.push({type:'string',value:'sysuliang',text:'样品试验组数。默认值（水泥1，水泥混凝土1，钢筋1）',dictCode:''})
        fieldList.push({type:'string',value:'qddj',text:'强度等级（混凝土）',dictCode:''})
        fieldList.push({type:'string',value:'hntzl',text:'混凝土种类（混凝土）',dictCode:''})
        fieldList.push({type:'string',value:'jbfs',text:'搅拌方式（混凝土）',dictCode:''})
        fieldList.push({type:'string',value:'yplq',text:'样品龄期（混凝土）',dictCode:''})
        fieldList.push({type:'string',value:'sybz',text:'样品备注',dictCode:''})
        fieldList.push({type:'string',value:'sjzl',text:'砂浆种类',dictCode:''})
        fieldList.push({type:'string',value:'gjzj',text:'钢筋直径',dictCode:''})
        fieldList.push({type:'string',value:'gjzl',text:'钢筋种类',dictCode:''})
        fieldList.push({type:'string',value:'wtdw',text:'委托单位',dictCode:''})
        fieldList.push({type:'string',value:'sybh',text:'试验编号',dictCode:''})
        fieldList.push({type:'string',value:'sydw',text:'试验单位',dictCode:''})
        fieldList.push({type:'string',value:'ggzl',text:'规格种类',dictCode:''})
        fieldList.push({type:'string',value:'ddsl',text:'单位数量',dictCode:''})
        fieldList.push({type:'string',value:'jybz',text:'检验标准',dictCode:''})
        fieldList.push({type:'string',value:'ysyj',text:'ysyj',dictCode:''})
        fieldList.push({type:'string',value:'syrq',text:'试验日期',dictCode:''})
        fieldList.push({type:'string',value:'qyjzr',text:'取证见证人',dictCode:''})
        fieldList.push({type:'string',value:'wtr',text:'委托人',dictCode:''})
        fieldList.push({type:'string',value:'wtfzr',text:'委托单位负责人',dictCode:''})
        fieldList.push({type:'string',value:'zzyb',text:'地址及邮件',dictCode:''})
        fieldList.push({type:'string',value:'lldh',text:'联系电话',dictCode:''})
        fieldList.push({type:'string',value:'syr',text:'收样人',dictCode:''})
        fieldList.push({type:'string',value:'yjbgrq',text:'预计报告发出日期',dictCode:''})
        fieldList.push({type:'int',value:'sfsyj',text:'sfsyj',dictCode:''})
        fieldList.push({type:'int',value:'mstatus',text:'现场是否完成试验0：未完成1：已完成 默认给0',dictCode:''})
        fieldList.push({type:'string',value:'departid',text:'外键，组织机构id',dictCode:''})
        fieldList.push({type:'string',value:'orgcode',text:'外键，组织机构编码',dictCode:''})
        fieldList.push({type:'Text',value:'qrcode',text:'二维码，多项以逗号隔开，最后一个二维码不用加逗号',dictCode:''})
        fieldList.push({type:'string',value:'samplejidu',text:'samplejidu',dictCode:''})
        fieldList.push({type:'string',value:'sampleweidu',text:'sampleweidu',dictCode:''})
        fieldList.push({type:'string',value:'ts',text:'ts',dictCode:''})
        fieldList.push({type:'string',value:'chicun',text:'试验尺寸',dictCode:''})
        fieldList.push({type:'date',value:'starttime',text:'判断试件的状态开始时间'})
        fieldList.push({type:'date',value:'endtime',text:'判断试件的状态结束时间'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>