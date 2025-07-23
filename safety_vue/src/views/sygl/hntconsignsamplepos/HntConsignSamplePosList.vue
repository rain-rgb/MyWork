<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="货架名称">
              <a-input placeholder="请输入货架名称" v-model="queryParam.huojianame"></a-input>
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
      <a-button @click="handleAdd" type="primary" v-has="'hntpos:add'" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'hntpos:dc'" icon="download" @click="handleExportXls('混凝土见证取样货架表信息')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'hntpos:dr'" icon="import">导入</a-button>
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
        <span slot="huojianame" slot-scope="huojianame, record">
        <a-tag color="green" >{{record.huojianame}}</a-tag>
       </span>
        <span slot="huojiahang" slot-scope="huojiahang, record">
        <a-tag color="blue" >{{record.huojiahang}}</a-tag>
       </span>
        <span slot="huojiacenshu" slot-scope="huojiacenshu, record">
        <a-tag color="red" >{{record.huojiacenshu}}</a-tag>
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
          <a v-has="'hntpos:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'hntpos:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <hnt-consign-sample-pos-modal ref="modalForm" @ok="modalFormOk"></hnt-consign-sample-pos-modal>
  </a-card>
</template>

<script>

  import '@assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import HntConsignSamplePosModal from './modules/HntConsignSamplePosModal'
  import JSuperQuery from '@comp/jeecg/JSuperQuery.vue'

  export default {
    name: 'HntConsignSamplePosList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      HntConsignSamplePosModal,
      JSuperQuery,
    },
    data () {
      return {
        description: '混凝土见证取样货架表信息管理页面',
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
          //   title:'外键，组织机构id,只能是试验类型的组织机构',
          //   align:"center",
          //   dataIndex: 'departid'
          // },
          {
            title:'所属部门',
            align:"center",
            dataIndex: 'sysOrgCode_dictText'
          },
          {
            title:'货架名称',
            align:"center",
            dataIndex: 'huojianame',
            key: 'huojianame',
            scopedSlots: {customRender: 'huojianame'},
          },
          {
            title:'货架行',
            align:"center",
            dataIndex: 'huojiahang',
            key: 'huojiahang',
            scopedSlots: {customRender: 'huojiahang'}
          },
          {
            title:'货架列',
            align:"center",
            dataIndex: 'huojialie'
          },
          {
            title:'货架总层数',
            align:"center",
            dataIndex: 'huojiacenshu',
            key: 'huojiacenshu',
            scopedSlots: {customRender: 'huojiacenshu'}
          },
          {
            title:'所属标养室',
            align:"center",
            dataIndex: 'temperatureid_dictText'
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
          list: "/hntconsignsamplepos/hntConsignSamplePos/list",
          delete: "/hntconsignsamplepos/hntConsignSamplePos/delete",
          deleteBatch: "/hntconsignsamplepos/hntConsignSamplePos/deleteBatch",
          exportXlsUrl: "/hntconsignsamplepos/hntConsignSamplePos/exportXls",
          importExcelUrl: "hntconsignsamplepos/hntConsignSamplePos/importExcel",
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
        fieldList.push({type:'string',value:'departid',text:'外键，组织机构id,只能是试验类型的组织机构',dictCode:''})
        fieldList.push({type:'string',value:'sys_org_code',text:'外键，组织机构编码',dictCode:''})
        fieldList.push({type:'string',value:'huojianame',text:'货架名称或者编号',dictCode:''})
        fieldList.push({type:'string',value:'huojiahang',text:'货架排行',dictCode:''})
        fieldList.push({type:'string',value:'huojialie',text:'货架排列',dictCode:''})
        fieldList.push({type:'string',value:'huojiacenshu',text:'货架从下到上总层数',dictCode:''})
        fieldList.push({type:'string',value:'temperatureid',text:'外键，标养室id',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>