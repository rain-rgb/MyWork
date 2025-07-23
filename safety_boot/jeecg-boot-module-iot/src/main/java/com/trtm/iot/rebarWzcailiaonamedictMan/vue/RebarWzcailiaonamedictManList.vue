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
      <a-button type="primary" icon="download" @click="handleExportXls('rebar_wzcailiaonamedict_man')">导出</a-button>
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

    <rebar-wzcailiaonamedict-man-modal ref="modalForm" @ok="modalFormOk"></rebar-wzcailiaonamedict-man-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import RebarWzcailiaonamedictManModal from './modules/RebarWzcailiaonamedictManModal'

  export default {
    name: 'RebarWzcailiaonamedictManList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      RebarWzcailiaonamedictManModal
    },
    data () {
      return {
        description: 'rebar_wzcailiaonamedict_man管理页面',
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
            title:'材料名称',
            align:"center",
            dataIndex: 'cailiaoname'
          },
          {
            title:'材料编号',
            align:"center",
            dataIndex: 'cailiaono'
          },
          {
            title:'parentnode',
            align:"center",
            dataIndex: 'parentnode'
          },
          {
            title:'材料类型',
            align:"center",
            dataIndex: 'nodetype'
          },
          {
            title:'规格类型',
            align:"center",
            dataIndex: 'guigexinghao'
          },
          {
            title:'称重',
            align:"center",
            dataIndex: 'ischengzhong'
          },
          {
            title:'描述',
            align:"center",
            dataIndex: 'miaoshu'
          },
          {
            title:'偏差',
            align:"center",
            dataIndex: 'isyunxupiancha'
          },
          {
            title:'正偏差',
            align:"center",
            dataIndex: 'zhengpiancha'
          },
          {
            title:'负偏差',
            align:"center",
            dataIndex: 'fupiancha'
          },
          {
            title:'材料计量单位',
            align:"center",
            dataIndex: 'wzcailiaojiliangdanwei'
          },
          {
            title:'品牌',
            align:"center",
            dataIndex: 'pinpai'
          },
          {
            title:'产地',
            align:"center",
            dataIndex: 'chandi'
          },
          {
            title:'单价',
            align:"center",
            dataIndex: 'danjia'
          },
          {
            title:'材料字典的唯一id',
            align:"center",
            dataIndex: 'guid'
          },
          {
            title:'时间戳',
            align:"center",
            dataIndex: 'ts'
          },
          {
            title:'isdel',
            align:"center",
            dataIndex: 'isdel'
          },
          {
            title:'status',
            align:"center",
            dataIndex: 'status'
          },
          {
            title:'路面项目 1 原材料  2混合料',
            align:"center",
            dataIndex: 'lmcailiaolx'
          },
          {
            title:'原材料上限',
            align:"center",
            dataIndex: 'shangxian'
          },
          {
            title:'testid',
            align:"center",
            dataIndex: 'testid'
          },
          {
            title:'editperson',
            align:"center",
            dataIndex: 'editperson'
          },
          {
            title:'editdatetime',
            align:"center",
            dataIndex: 'editdatetime'
          },
          {
            title:'wzcailiaodanweihuansuan',
            align:"center",
            dataIndex: 'wzcailiaodanweihuansuan'
          },
          {
            title:'是否使用电子锁 0：否，1：是',
            align:"center",
            dataIndex: 'iselocks'
          },
          {
            title:'重量',
            align:"center",
            dataIndex: 'weight'
          },
          {
            title:'过磅类型: 0:无人过磅 1:人工过磅',
            align:"center",
            dataIndex: 'gblx'
          },
          {
            title:'材料字典表id',
            align:"center",
            dataIndex: 'allguid'
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
          list: "/rebarWzcailiaonamedictMan/rebarWzcailiaonamedictMan/list",
          delete: "/rebarWzcailiaonamedictMan/rebarWzcailiaonamedictMan/delete",
          deleteBatch: "/rebarWzcailiaonamedictMan/rebarWzcailiaonamedictMan/deleteBatch",
          exportXlsUrl: "/rebarWzcailiaonamedictMan/rebarWzcailiaonamedictMan/exportXls",
          importExcelUrl: "rebarWzcailiaonamedictMan/rebarWzcailiaonamedictMan/importExcel",
          
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
        fieldList.push({type:'string',value:'cailiaoname',text:'材料名称'})
        fieldList.push({type:'string',value:'cailiaono',text:'材料编号'})
        fieldList.push({type:'string',value:'parentnode',text:'parentnode'})
        fieldList.push({type:'string',value:'nodetype',text:'材料类型'})
        fieldList.push({type:'string',value:'guigexinghao',text:'规格类型'})
        fieldList.push({type:'string',value:'ischengzhong',text:'称重'})
        fieldList.push({type:'string',value:'miaoshu',text:'描述'})
        fieldList.push({type:'string',value:'isyunxupiancha',text:'偏差'})
        fieldList.push({type:'string',value:'zhengpiancha',text:'正偏差'})
        fieldList.push({type:'string',value:'fupiancha',text:'负偏差'})
        fieldList.push({type:'string',value:'wzcailiaojiliangdanwei',text:'材料计量单位'})
        fieldList.push({type:'string',value:'pinpai',text:'品牌'})
        fieldList.push({type:'string',value:'chandi',text:'产地'})
        fieldList.push({type:'string',value:'danjia',text:'单价'})
        fieldList.push({type:'string',value:'guid',text:'材料字典的唯一id'})
        fieldList.push({type:'int',value:'ts',text:'时间戳'})
        fieldList.push({type:'int',value:'isdel',text:'isdel'})
        fieldList.push({type:'int',value:'status',text:'status'})
        fieldList.push({type:'int',value:'lmcailiaolx',text:'路面项目 1 原材料  2混合料'})
        fieldList.push({type:'number',value:'shangxian',text:'原材料上限'})
        fieldList.push({type:'int',value:'testid',text:'testid'})
        fieldList.push({type:'string',value:'editperson',text:'editperson'})
        fieldList.push({type:'string',value:'editdatetime',text:'editdatetime'})
        fieldList.push({type:'string',value:'wzcailiaodanweihuansuan',text:'wzcailiaodanweihuansuan'})
        fieldList.push({type:'int',value:'iselocks',text:'是否使用电子锁 0：否，1：是'})
        fieldList.push({type:'number',value:'weight',text:'重量'})
        fieldList.push({type:'int',value:'gblx',text:'过磅类型: 0:无人过磅 1:人工过磅'})
        fieldList.push({type:'string',value:'allguid',text:'材料字典表id'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>