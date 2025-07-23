<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label=材料名称>
              <a-input placeholder="请输入材料名称" v-model="queryParam.cailiaoname"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料类型">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.nodetypeCL" dictCode="nodetypeCL"></j-dict-select-tag>
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
      <a-button @click="mountData" v-has="'cailiao:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'cailiao:dc'" icon="download" @click="handleExportXls('材料配置主表')">导出</a-button>
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" v-has="'cailiao:dr'" icon="import">导入</a-button>-->
<!--      </a-upload>-->
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

        <span slot="iselocks" slot-scope="iselocks, record">
          <a-tag color="orange" v-if="record.iselocks === 0">未使用</a-tag>
          <a-tag color="green" v-if="record.iselocks === 1">使用</a-tag>
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
          <a v-has="'cailiao:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'cailiao:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <manAddModal ref="man"  :dict-type="1" ></manAddModal>
    <wzcailiaonamedict-modal ref="modalForm" @ok="modalFormOk"></wzcailiaonamedict-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WzcailiaonamedictModal from './modules/WzcailiaonamedictModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import { handertoken } from '@/mixins/getHanderToken'
  import manAddModal from '../wzcailiaonamedictman/modules/manAddModal'

  export default {
    name: 'WzcailiaonamedictList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      manAddModal,
      WzcailiaonamedictModal,
      JSuperQuery,
      JselectDqDepart,
    },
    data () {
      return {
        description: '材料配置主表管理页面',
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
          {
            title:'所属部门',
            align:"center",
            dataIndex: 'sysOrgCode_dictText'
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
          // {
          //   title:'parentnode',
          //   align:"center",
          //   dataIndex: 'parentnode'
          // },
          {
            title:'材料类型',
            align:"center",
            dataIndex: 'nodetype_dictText'
          },
          {
            title:'规格类型',
            align:"center",
            dataIndex: 'guigexinghao'
          },
          {
            title:'材料计量单位',
            align:"center",
            dataIndex: 'wzcailiaojiliangdanwei_dictText'
          },
          {
            title:'材料分类',
            align:"center",
            dataIndex: 'lmcailiaolx_dictText'
          },
          // {
          //   title:'称重',
          //   align:"center",
          //   dataIndex: 'ischengzhong'
          // },
          // {
          //   title:'描述',
          //   align:"center",
          //   dataIndex: 'miaoshu'
          // },
          // {
          //   title:'偏差',
          //   align:"center",
          //   dataIndex: 'isyunxupiancha'
          // },
          // {
          //   title:'正偏差',
          //   align:"center",
          //   dataIndex: 'zhengpiancha'
          // },
          // {
          //   title:'负偏差',
          //   align:"center",
          //   dataIndex: 'fupiancha'
          // },
          // {
          //   title:'pinpai',
          //   align:"center",
          //   dataIndex: 'pinpai'
          // },
          // {
          //   title:'chandi',
          //   align:"center",
          //   dataIndex: 'chandi'
          // },
          // {
          //   title:'danjia',
          //   align:"center",
          //   dataIndex: 'danjia'
          // },
          {
            title:'是否使用电子锁',
            align:"center",
            dataIndex: 'iselocks',
            scopedSlots: { customRender: 'iselocks' },
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy_dictText'
          },
          {
            title:'创建日期',
            align:"center",
            dataIndex: 'createTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          // {
          //   title:'材料字典的唯一id',
          //   align:"center",
          //   dataIndex: 'guid'
          // },
          // {
          //   title:'时间戳',
          //   align:"center",
          //   dataIndex: 'ts'
          // },
          // {
          //   title:'isdel',
          //   align:"center",
          //   dataIndex: 'isdel'
          // },
          // {
          //   title:'status',
          //   align:"center",
          //   dataIndex: 'status'
          // },
          // {
          //   title:'原材料上限',
          //   align:"center",
          //   dataIndex: 'shangxian'
          // },
          {
            title:'更新人',
            align:"center",
            dataIndex: 'updateBy_dictText'
          },
          {
            title:'更新日期',
            align:"center",
            dataIndex: 'updateTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
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
          list: "/wzcailiaonamedict/wzcailiaonamedict/list",
          delete: "/wzcailiaonamedict/wzcailiaonamedict/delete",
          deleteBatch: "/wzcailiaonamedict/wzcailiaonamedict/deleteBatch",
          exportXlsUrl: "/wzcailiaonamedict/wzcailiaonamedict/exportXls",
          importExcelUrl: "wzcailiaonamedict/wzcailiaonamedict/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getSuperFieldList();
      this.getToken();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      mountData() {
        // this.$refs.man.modelData = Object.assign({}, record)
        this.$refs.man.getList()
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'cailiaoname',text:'材料名称',dictCode:''})
        fieldList.push({type:'string',value:'cailiaono',text:'材料编号',dictCode:''})
        fieldList.push({type:'string',value:'parentnode',text:'parentnode',dictCode:''})
        fieldList.push({type:'string',value:'nodetype',text:'材料类型',dictCode:''})
        fieldList.push({type:'string',value:'guigexinghao',text:'规格类型',dictCode:''})
        fieldList.push({type:'string',value:'wzcailiaojiliangdanwei',text:'材料计量单位',dictCode:''})
        fieldList.push({type:'string',value:'ischengzhong',text:'称重',dictCode:''})
        fieldList.push({type:'string',value:'miaoshu',text:'描述',dictCode:''})
        fieldList.push({type:'string',value:'isyunxupiancha',text:'偏差',dictCode:''})
        fieldList.push({type:'string',value:'zhengpiancha',text:'正偏差',dictCode:''})
        fieldList.push({type:'string',value:'fupiancha',text:'负偏差',dictCode:''})
        fieldList.push({type:'string',value:'sysOrgCode',text:'所属部门',dictCode:''})
        fieldList.push({type:'string',value:'pinpai',text:'pinpai',dictCode:''})
        fieldList.push({type:'string',value:'chandi',text:'chandi',dictCode:''})
        fieldList.push({type:'string',value:'danjia',text:'danjia',dictCode:''})
        fieldList.push({type:'string',value:'createBy',text:'创建人',dictCode:''})
        fieldList.push({type:'string',value:'createTime',text:'创建日期',dictCode:''})
        fieldList.push({type:'string',value:'guid',text:'材料字典的唯一id',dictCode:''})
        fieldList.push({type:'int',value:'ts',text:'时间戳',dictCode:''})
        fieldList.push({type:'int',value:'isdel',text:'isdel',dictCode:''})
        fieldList.push({type:'int',value:'status',text:'status',dictCode:''})
        fieldList.push({type:'int',value:'lmcailiaolx',text:'路面项目 1 原材料  2混合料',dictCode:''})
        fieldList.push({type:'double',value:'shangxian',text:'原材料上限',dictCode:''})
        fieldList.push({type:'string',value:'updateBy',text:'更新人',dictCode:''})
        fieldList.push({type:'date',value:'updateTime',text:'更新日期'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>