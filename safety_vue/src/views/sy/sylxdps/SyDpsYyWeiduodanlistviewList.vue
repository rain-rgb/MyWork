<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('sy_dps_yy_weiduodanlistview')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
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
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
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

    <sy-dps-yy-weiduodanlistview-modal ref="modalForm" @ok="modalFormOk"></sy-dps-yy-weiduodanlistview-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyDpsYyWeiduodanlistviewModal from './modules/SyDpsYyWeiduodanlistviewModal'

  export default {
    name: 'SyDpsYyWeiduodanlistviewList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyDpsYyWeiduodanlistviewModal
    },
    data () {
      return {
        description: 'sy_dps_yy_weiduodanlistview管理页面',
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
            title:'主键，自动增长',
            align:"center",
            dataIndex: 'wtdid'
          },
          {
            title:'外键，原材进场登记表ID',
            align:"center",
            dataIndex: 'yuancaijinchangdengjiid'
          },
          {
            title:'beizhu',
            align:"center",
            dataIndex: 'beizhu'
          },
          {
            title:'fujian',
            align:"center",
            dataIndex: 'fujian'
          },
          {
            title:'委托单编号，唯一',
            align:"center",
            dataIndex: 'weituodanbianhao'
          },
          {
            title:'施工单位',
            align:"center",
            dataIndex: 'shigongdanwei'
          },
          {
            title:'监理单位',
            align:"center",
            dataIndex: 'jianglidanwei'
          },
          {
            title:'外键，施工单位组织机构编码',
            align:"center",
            dataIndex: 'jianlidanweiorgcode'
          },
          {
            title:'监理单位',
            align:"center",
            dataIndex: 'weituodanwei'
          },
          {
            title:'委托人',
            align:"center",
            dataIndex: 'weituoren'
          },
          {
            title:'委托日期',
            align:"center",
            dataIndex: 'weituoriqi'
          },
          {
            title:'取样地点',
            align:"center",
            dataIndex: 'quyangdidian'
          },
          {
            title:'材料名称',
            align:"center",
            dataIndex: 'cailiaomingcheng'
          },
          {
            title:'guigexinghao',
            align:"center",
            dataIndex: 'guigexinghao'
          },
          {
            title:'生产批号',
            align:"center",
            dataIndex: 'shengchanpihao'
          },
          {
            title:'出厂日期（YYYY-MM-dd）',
            align:"center",
            dataIndex: 'chuchangriqi'
          },
          {
            title:'进场日期（YYYY-MM-dd）',
            align:"center",
            dataIndex: 'jinchangriqi'
          },
          {
            title:'使用部位',
            align:"center",
            dataIndex: 'shiyongbuwei'
          },
          {
            title:'报告份数',
            align:"center",
            dataIndex: 'baogaofenshu'
          },
          {
            title:'样品处理方式',
            align:"center",
            dataIndex: 'yangpinchulifangshi'
          },
          {
            title:'检测项目',
            align:"center",
            dataIndex: 'jiancexiangmu'
          },
          {
            title:'材料厂家',
            align:"center",
            dataIndex: 'cailiaochangjia'
          },
          {
            title:'委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托',
            align:"center",
            dataIndex: 'weituozhuangtai'
          },
          {
            title:'指派状态（0：未指派1：已指派）',
            align:"center",
            dataIndex: 'zhipaizhuangtai'
          },
          {
            title:'指派人，获取当前登录用户外键T_S_Base_User表的UserName字段',
            align:"center",
            dataIndex: 'zhipairen'
          },
          {
            title:'指派时间（YYYY-MM-dd HH:mm:ss）',
            align:"center",
            dataIndex: 'zhipaishijian'
          },
          {
            title:'指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段',
            align:"center",
            dataIndex: 'zhipaiquyangren'
          },
          {
            title:'jinchangshuliang',
            align:"center",
            dataIndex: 'jinchangshuliang'
          },
          {
            title:'样品类型',
            align:"center",
            dataIndex: 'titcode'
          },
          {
            title:'外委状态',
            align:"center",
            dataIndex: 'waiweizhuangtai'
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
          list: "/sylxdps/syDpsYyWeiduodanlistview/list",
          delete: "/sylxdps/syDpsYyWeiduodanlistview/delete",
          deleteBatch: "/sylxdps/syDpsYyWeiduodanlistview/deleteBatch",
          exportXlsUrl: "/sylxdps/syDpsYyWeiduodanlistview/exportXls",
          importExcelUrl: "sylxdps/syDpsYyWeiduodanlistview/importExcel",
          
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
        fieldList.push({type:'int',value:'wtdid',text:'主键，自动增长'})
        fieldList.push({type:'int',value:'yuancaijinchangdengjiid',text:'外键，原材进场登记表ID'})
        fieldList.push({type:'string',value:'beizhu',text:'beizhu'})
        fieldList.push({type:'string',value:'fujian',text:'fujian'})
        fieldList.push({type:'string',value:'weituodanbianhao',text:'委托单编号，唯一'})
        fieldList.push({type:'string',value:'shigongdanwei',text:'施工单位'})
        fieldList.push({type:'string',value:'jianglidanwei',text:'监理单位'})
        fieldList.push({type:'string',value:'jianlidanweiorgcode',text:'外键，施工单位组织机构编码'})
        fieldList.push({type:'string',value:'weituodanwei',text:'监理单位'})
        fieldList.push({type:'string',value:'weituoren',text:'委托人'})
        fieldList.push({type:'string',value:'weituoriqi',text:'委托日期'})
        fieldList.push({type:'string',value:'quyangdidian',text:'取样地点'})
        fieldList.push({type:'string',value:'cailiaomingcheng',text:'材料名称'})
        fieldList.push({type:'number',value:'guigexinghao',text:'guigexinghao'})
        fieldList.push({type:'string',value:'shengchanpihao',text:'生产批号'})
        fieldList.push({type:'string',value:'chuchangriqi',text:'出厂日期（YYYY-MM-dd）'})
        fieldList.push({type:'string',value:'jinchangriqi',text:'进场日期（YYYY-MM-dd）'})
        fieldList.push({type:'string',value:'shiyongbuwei',text:'使用部位'})
        fieldList.push({type:'int',value:'baogaofenshu',text:'报告份数'})
        fieldList.push({type:'string',value:'yangpinchulifangshi',text:'样品处理方式'})
        fieldList.push({type:'string',value:'jiancexiangmu',text:'检测项目'})
        fieldList.push({type:'string',value:'cailiaochangjia',text:'材料厂家'})
        fieldList.push({type:'int',value:'weituozhuangtai',text:'委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托'})
        fieldList.push({type:'int',value:'zhipaizhuangtai',text:'指派状态（0：未指派1：已指派）'})
        fieldList.push({type:'string',value:'zhipairen',text:'指派人，获取当前登录用户外键T_S_Base_User表的UserName字段'})
        fieldList.push({type:'string',value:'zhipaishijian',text:'指派时间（YYYY-MM-dd HH:mm:ss）'})
        fieldList.push({type:'string',value:'zhipaiquyangren',text:'指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段'})
        fieldList.push({type:'number',value:'jinchangshuliang',text:'jinchangshuliang'})
        fieldList.push({type:'string',value:'titcode',text:'样品类型'})
        fieldList.push({type:'string',value:'waiweizhuangtai',text:'外委状态'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>