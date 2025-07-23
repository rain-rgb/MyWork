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
      <a-button type="primary" icon="download" @click="handleExportXls('sy_dps_yy_qydlistview')">导出</a-button>
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

    <sy-dps-yy-qydlistview-modal ref="modalForm" @ok="modalFormOk"></sy-dps-yy-qydlistview-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyDpsYyQydlistviewModal from './modules/SyDpsYyQydlistviewModal'

  export default {
    name: 'SyDpsYyQydlistviewList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyDpsYyQydlistviewModal
    },
    data () {
      return {
        description: 'sy_dps_yy_qydlistview管理页面',
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
            title:'取样单流水号',
            align:"center",
            dataIndex: 'quyangdanliushuihao'
          },
          {
            title:'主键，自增长',
            align:"center",
            dataIndex: 'quyangdanid'
          },
          {
            title:'材料名称',
            align:"center",
            dataIndex: 'yangpingmingcheng'
          },
          {
            title:'材料厂家',
            align:"center",
            dataIndex: 'cailiaochangjia'
          },
          {
            title:'guigexinghao',
            align:"center",
            dataIndex: 'guigexinghao'
          },
          {
            title:'材料规格',
            align:"center",
            dataIndex: 'guigexinghao1'
          },
          {
            title:'guigexinghao2',
            align:"center",
            dataIndex: 'guigexinghao2'
          },
          {
            title:'guigexinghao3',
            align:"center",
            dataIndex: 'guigexinghao3'
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
            title:'外键，施工单位组织机构编码',
            align:"center",
            dataIndex: 'orgcode'
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
            title:'取样地点',
            align:"center",
            dataIndex: 'quyangdidian'
          },
          {
            title:'成型日期(YYYY-MM-dd)',
            align:"center",
            dataIndex: 'chengxingriqi'
          },
          {
            title:'样品数量',
            align:"center",
            dataIndex: 'yangpingshuliang'
          },
          {
            title:'样品描述',
            align:"center",
            dataIndex: 'yangpinmiaoshu'
          },
          {
            title:'daibiaoshuliang',
            align:"center",
            dataIndex: 'daibiaoshuliang'
          },
          {
            title:'进场数量(代表数量)',
            align:"center",
            dataIndex: 'jinchangshuliang1'
          },
          {
            title:'jinchangshuliang2',
            align:"center",
            dataIndex: 'jinchangshuliang2'
          },
          {
            title:'jinchangshuliang3',
            align:"center",
            dataIndex: 'jinchangshuliang3'
          },
          {
            title:'检测项目',
            align:"center",
            dataIndex: 'jiancexiangmu'
          },
          {
            title:'抽样人员',
            align:"center",
            dataIndex: 'chouyangrenyuan'
          },
          {
            title:'外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段',
            align:"center",
            dataIndex: 'jianzhengren'
          },
          {
            title:'外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段',
            align:"center",
            dataIndex: 'shouyangren'
          },
          {
            title:'主键，自动增长',
            align:"center",
            dataIndex: 'wtdid'
          },
          {
            title:'收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0',
            align:"center",
            dataIndex: 'shouyangzhuangtai'
          },
          {
            title:'收样时间（YYYY-MM-dd HH:mm:ss）',
            align:"center",
            dataIndex: 'shouyangshijian'
          },
          {
            title:'外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段',
            align:"center",
            dataIndex: 'quyangren'
          },
          {
            title:'取样时间（YYYY-MM-dd HH:mm:ss）',
            align:"center",
            dataIndex: 'quyangshijian'
          },
          {
            title:'取样状态（0：未取样  1：已取样）默认0',
            align:"center",
            dataIndex: 'quyangzhuangtai'
          },
          {
            title:'二维码编号(处理后字段，id+序号)',
            align:"center",
            dataIndex: 'erweimabianhao'
          },
          {
            title:'gongcghengmingcheng',
            align:"center",
            dataIndex: 'gongcghengmingcheng'
          },
          {
            title:'主键，自动增长',
            align:"center",
            dataIndex: 'yuancaijinchangdengjiid'
          },
          {
            title:'外键，样品编号',
            align:"center",
            dataIndex: 'sampleno'
          },
          {
            title:'样品类型',
            align:"center",
            dataIndex: 'titcode'
          },
          {
            title:'renwudanliushuihao',
            align:"center",
            dataIndex: 'renwudanliushuihao'
          },
          {
            title:'检测人员',
            align:"center",
            dataIndex: 'jiancerenyuan'
          },
          {
            title:'外委状态',
            align:"center",
            dataIndex: 'waiweizhuangtai'
          },
          {
            title:'外键，试验项目类型（dps_jc_testItemType表）',
            align:"center",
            dataIndex: 'sampletitcode'
          },
          {
            title:'材料名称',
            align:"center",
            dataIndex: 'cailiaomingcheng'
          },
          {
            title:'委托日期',
            align:"center",
            dataIndex: 'weituoriqi'
          },
          {
            title:'指派时间（YYYY-MM-dd HH:mm:ss）',
            align:"center",
            dataIndex: 'zhipaishijian'
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
          list: "/sylxdps/syDpsYyQydlistview/list",
          delete: "/sylxdps/syDpsYyQydlistview/delete",
          deleteBatch: "/sylxdps/syDpsYyQydlistview/deleteBatch",
          exportXlsUrl: "/sylxdps/syDpsYyQydlistview/exportXls",
          importExcelUrl: "sylxdps/syDpsYyQydlistview/importExcel",
          
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
        fieldList.push({type:'string',value:'quyangdanliushuihao',text:'取样单流水号'})
        fieldList.push({type:'int',value:'quyangdanid',text:'主键，自增长'})
        fieldList.push({type:'string',value:'yangpingmingcheng',text:'材料名称'})
        fieldList.push({type:'string',value:'cailiaochangjia',text:'材料厂家'})
        fieldList.push({type:'number',value:'guigexinghao',text:'guigexinghao'})
        fieldList.push({type:'string',value:'guigexinghao1',text:'材料规格'})
        fieldList.push({type:'string',value:'guigexinghao2',text:'guigexinghao2'})
        fieldList.push({type:'string',value:'guigexinghao3',text:'guigexinghao3'})
        fieldList.push({type:'string',value:'shengchanpihao',text:'生产批号'})
        fieldList.push({type:'string',value:'chuchangriqi',text:'出厂日期（YYYY-MM-dd）'})
        fieldList.push({type:'string',value:'jinchangriqi',text:'进场日期（YYYY-MM-dd）'})
        fieldList.push({type:'string',value:'shiyongbuwei',text:'使用部位'})
        fieldList.push({type:'string',value:'orgcode',text:'外键，施工单位组织机构编码'})
        fieldList.push({type:'string',value:'shigongdanwei',text:'施工单位'})
        fieldList.push({type:'string',value:'jianglidanwei',text:'监理单位'})
        fieldList.push({type:'string',value:'quyangdidian',text:'取样地点'})
        fieldList.push({type:'string',value:'chengxingriqi',text:'成型日期(YYYY-MM-dd)'})
        fieldList.push({type:'string',value:'yangpingshuliang',text:'样品数量'})
        fieldList.push({type:'string',value:'yangpinmiaoshu',text:'样品描述'})
        fieldList.push({type:'number',value:'daibiaoshuliang',text:'daibiaoshuliang'})
        fieldList.push({type:'string',value:'jinchangshuliang1',text:'进场数量(代表数量)'})
        fieldList.push({type:'string',value:'jinchangshuliang2',text:'jinchangshuliang2'})
        fieldList.push({type:'string',value:'jinchangshuliang3',text:'jinchangshuliang3'})
        fieldList.push({type:'string',value:'jiancexiangmu',text:'检测项目'})
        fieldList.push({type:'string',value:'chouyangrenyuan',text:'抽样人员'})
        fieldList.push({type:'string',value:'jianzhengren',text:'外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段'})
        fieldList.push({type:'string',value:'shouyangren',text:'外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段'})
        fieldList.push({type:'int',value:'wtdid',text:'主键，自动增长'})
        fieldList.push({type:'int',value:'shouyangzhuangtai',text:'收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0'})
        fieldList.push({type:'string',value:'shouyangshijian',text:'收样时间（YYYY-MM-dd HH:mm:ss）'})
        fieldList.push({type:'string',value:'quyangren',text:'外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段'})
        fieldList.push({type:'string',value:'quyangshijian',text:'取样时间（YYYY-MM-dd HH:mm:ss）'})
        fieldList.push({type:'int',value:'quyangzhuangtai',text:'取样状态（0：未取样  1：已取样）默认0'})
        fieldList.push({type:'string',value:'erweimabianhao',text:'二维码编号(处理后字段，id+序号)'})
        fieldList.push({type:'string',value:'gongcghengmingcheng',text:'gongcghengmingcheng'})
        fieldList.push({type:'int',value:'yuancaijinchangdengjiid',text:'主键，自动增长'})
        fieldList.push({type:'string',value:'sampleno',text:'外键，样品编号'})
        fieldList.push({type:'string',value:'titcode',text:'样品类型'})
        fieldList.push({type:'string',value:'renwudanliushuihao',text:'renwudanliushuihao'})
        fieldList.push({type:'string',value:'jiancerenyuan',text:'检测人员'})
        fieldList.push({type:'string',value:'waiweizhuangtai',text:'外委状态'})
        fieldList.push({type:'string',value:'sampletitcode',text:'外键，试验项目类型（dps_jc_testItemType表）'})
        fieldList.push({type:'string',value:'cailiaomingcheng',text:'材料名称'})
        fieldList.push({type:'string',value:'weituoriqi',text:'委托日期'})
        fieldList.push({type:'string',value:'zhipaishijian',text:'指派时间（YYYY-MM-dd HH:mm:ss）'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>