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
      <a-button type="primary" icon="download" @click="handleExportXls('sy_dps_yy_ycljclist')">导出</a-button>
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

    <sy-dps-yy-ycljclist-modal ref="modalForm" @ok="modalFormOk"></sy-dps-yy-ycljclist-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyDpsYyYcljclistModal from './modules/SyDpsYyYcljclistModal'

  export default {
    name: 'SyDpsYyYcljclistList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyDpsYyYcljclistModal
    },
    data () {
      return {
        description: 'sy_dps_yy_ycljclist管理页面',
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
            title:'材料厂家',
            align:"center",
            dataIndex: 'cailiaochangjia'
          },
          {
            title:'材料登记人',
            align:"center",
            dataIndex: 'cailiaodengjiren'
          },
          {
            title:'样品类型',
            align:"center",
            dataIndex: 'titcode'
          },
          {
            title:'材料登记时间（YYYY-MM-dd HH:mm:ss）',
            align:"center",
            dataIndex: 'cailiaodengjishijian'
          },
          {
            title:'材料名称',
            align:"center",
            dataIndex: 'cailiaomingcheng'
          },
          {
            title:'出厂日期（YYYY-MM-dd）',
            align:"center",
            dataIndex: 'chuchangriqi'
          },
          {
            title:'存放地点',
            align:"center",
            dataIndex: 'chunfangdidian'
          },
          {
            title:'二维码编号(处理后字段，id+序号)',
            align:"center",
            dataIndex: 'erweimabianhao'
          },
          {
            title:'二维码UUID',
            align:"center",
            dataIndex: 'erweimaweiyima'
          },
          {
            title:'guigexinghao',
            align:"center",
            dataIndex: 'guigexinghao'
          },
          {
            title:'检测依据',
            align:"center",
            dataIndex: 'jianceyiju'
          },
          {
            title:'监理抽检（0：否 1：是）默认0',
            align:"center",
            dataIndex: 'jianlichoujianzhuangtai'
          },
          {
            title:'外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段',
            align:"center",
            dataIndex: 'jianzhengren'
          },
          {
            title:'见证时间（YYYY-MM-dd HH:mm:ss）',
            align:"center",
            dataIndex: 'jianzhengshijian'
          },
          {
            title:'见证状态（0：未见证  1：已见证）默认0',
            align:"center",
            dataIndex: 'jianzhengzhuangtai'
          },
          {
            title:'进场日期（YYYY-MM-dd）',
            align:"center",
            dataIndex: 'jinchangriqi'
          },
          {
            title:'jinchangshuliang',
            align:"center",
            dataIndex: 'jinchangshuliang'
          },
          {
            title:'jinchangshuliang1',
            align:"center",
            dataIndex: 'jinchangshuliang1'
          },
          {
            title:'jinchangshuliang2',
            align:"center",
            dataIndex: 'jinchangshuliang2'
          },
          {
            title:'评定依据',
            align:"center",
            dataIndex: 'pandingyiju'
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
            title:'外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段',
            align:"center",
            dataIndex: 'renwuzhixingren'
          },
          {
            title:'外键，样品编号',
            align:"center",
            dataIndex: 'sampleno'
          },
          {
            title:'生产批号',
            align:"center",
            dataIndex: 'shengchanpihao'
          },
          {
            title:'外键，施工单位组织机构编码',
            align:"center",
            dataIndex: 'shigongdanweiorgcode'
          },
          {
            title:'外键，施工单位组织机构id',
            align:"center",
            dataIndex: 'shigongdasnweidepartid'
          },
          {
            title:'试验完成期限',
            align:"center",
            dataIndex: 'shiyanwanchengqixian'
          },
          {
            title:'使用部位',
            align:"center",
            dataIndex: 'shiyongbuwei'
          },
          {
            title:'外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段',
            align:"center",
            dataIndex: 'shouyangren'
          },
          {
            title:'收样时间（YYYY-MM-dd HH:mm:ss）',
            align:"center",
            dataIndex: 'shouyangshijian'
          },
          {
            title:'收样状态（0：未收样  1：已收样 2：在检）默认0',
            align:"center",
            dataIndex: 'shouyangzhuangtai'
          },
          {
            title:'委托单编号，唯一',
            align:"center",
            dataIndex: 'weituodanbianhao'
          },
          {
            title:'委托状态（0：未委托 1：已委托 2:已取样 3：已收样 4：未执行 5：执行中 6：已完成）',
            align:"center",
            dataIndex: 'weituozhuangtai'
          },
          {
            title:'质保单（附件）',
            align:"center",
            dataIndex: 'zhibaodan'
          },
          {
            title:'执行状态（0：未执行 1：执行中 2：已完成）',
            align:"center",
            dataIndex: 'zhixingzhuangtai'
          },
          {
            title:'主键，自动增长',
            align:"center",
            dataIndex: 'wtdid'
          },
          {
            title:'委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托',
            align:"center",
            dataIndex: 'wtzt'
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
          list: "/sylxdps/syDpsYyYcljclist/list",
          delete: "/sylxdps/syDpsYyYcljclist/delete",
          deleteBatch: "/sylxdps/syDpsYyYcljclist/deleteBatch",
          exportXlsUrl: "/sylxdps/syDpsYyYcljclist/exportXls",
          importExcelUrl: "sylxdps/syDpsYyYcljclist/importExcel",
          
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
        fieldList.push({type:'string',value:'cailiaochangjia',text:'材料厂家'})
        fieldList.push({type:'string',value:'cailiaodengjiren',text:'材料登记人'})
        fieldList.push({type:'string',value:'titcode',text:'样品类型'})
        fieldList.push({type:'string',value:'cailiaodengjishijian',text:'材料登记时间（YYYY-MM-dd HH:mm:ss）'})
        fieldList.push({type:'string',value:'cailiaomingcheng',text:'材料名称'})
        fieldList.push({type:'string',value:'chuchangriqi',text:'出厂日期（YYYY-MM-dd）'})
        fieldList.push({type:'string',value:'chunfangdidian',text:'存放地点'})
        fieldList.push({type:'string',value:'erweimabianhao',text:'二维码编号(处理后字段，id+序号)'})
        fieldList.push({type:'string',value:'erweimaweiyima',text:'二维码UUID'})
        fieldList.push({type:'number',value:'guigexinghao',text:'guigexinghao'})
        fieldList.push({type:'string',value:'jianceyiju',text:'检测依据'})
        fieldList.push({type:'int',value:'jianlichoujianzhuangtai',text:'监理抽检（0：否 1：是）默认0'})
        fieldList.push({type:'string',value:'jianzhengren',text:'外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段'})
        fieldList.push({type:'string',value:'jianzhengshijian',text:'见证时间（YYYY-MM-dd HH:mm:ss）'})
        fieldList.push({type:'int',value:'jianzhengzhuangtai',text:'见证状态（0：未见证  1：已见证）默认0'})
        fieldList.push({type:'string',value:'jinchangriqi',text:'进场日期（YYYY-MM-dd）'})
        fieldList.push({type:'number',value:'jinchangshuliang',text:'jinchangshuliang'})
        fieldList.push({type:'string',value:'jinchangshuliang1',text:'jinchangshuliang1'})
        fieldList.push({type:'string',value:'jinchangshuliang2',text:'jinchangshuliang2'})
        fieldList.push({type:'string',value:'pandingyiju',text:'评定依据'})
        fieldList.push({type:'string',value:'quyangren',text:'外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段'})
        fieldList.push({type:'string',value:'quyangshijian',text:'取样时间（YYYY-MM-dd HH:mm:ss）'})
        fieldList.push({type:'int',value:'quyangzhuangtai',text:'取样状态（0：未取样  1：已取样）默认0'})
        fieldList.push({type:'string',value:'renwuzhixingren',text:'外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段'})
        fieldList.push({type:'string',value:'sampleno',text:'外键，样品编号'})
        fieldList.push({type:'string',value:'shengchanpihao',text:'生产批号'})
        fieldList.push({type:'string',value:'shigongdanweiorgcode',text:'外键，施工单位组织机构编码'})
        fieldList.push({type:'string',value:'shigongdasnweidepartid',text:'外键，施工单位组织机构id'})
        fieldList.push({type:'string',value:'shiyanwanchengqixian',text:'试验完成期限'})
        fieldList.push({type:'string',value:'shiyongbuwei',text:'使用部位'})
        fieldList.push({type:'string',value:'shouyangren',text:'外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段'})
        fieldList.push({type:'string',value:'shouyangshijian',text:'收样时间（YYYY-MM-dd HH:mm:ss）'})
        fieldList.push({type:'int',value:'shouyangzhuangtai',text:'收样状态（0：未收样  1：已收样 2：在检）默认0'})
        fieldList.push({type:'string',value:'weituodanbianhao',text:'委托单编号，唯一'})
        fieldList.push({type:'int',value:'weituozhuangtai',text:'委托状态（0：未委托 1：已委托 2:已取样 3：已收样 4：未执行 5：执行中 6：已完成）'})
        fieldList.push({type:'string',value:'zhibaodan',text:'质保单（附件）'})
        fieldList.push({type:'int',value:'zhixingzhuangtai',text:'执行状态（0：未执行 1：执行中 2：已完成）'})
        fieldList.push({type:'int',value:'wtdid',text:'主键，自动增长'})
        fieldList.push({type:'int',value:'wtzt',text:'委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>