<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="季度">
              <j-dict-select-tag placeholder="请选择季度" v-model="queryParam.quarter" dictCode="quarter"></j-dict-select-tag>
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
      <a-button @click="handleAdd" type="primary" icon="plus" v-has="'comrecord:add'" >新增</a-button>
      <a-button type="primary" icon="download" v-has="'comrecord:dc'" @click="handleExportXls('评比记录表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'comrecord:dr'" icon="import">导入</a-button>
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

        <span slot="tags" slot-scope="tags, record">
          <a-tag color="green" v-if="record.status === 1">达标</a-tag>
          <a-tag color="orange" v-if="record.status === 2">优良</a-tag>
          <a-tag color="yellow" v-if="record.status === 3">优胜</a-tag>
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
          <a v-has="'comrecord:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'comrecord:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <comparison-record-modal ref="modalForm" @ok="modalFormOk"></comparison-record-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ComparisonRecordModal from './modules/ComparisonRecordModal'
  import { handertoken } from '@/mixins/getHanderToken'

  export default {
    name: 'ComparisonRecordList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      ComparisonRecordModal
    },
    data () {
      return {
        description: '评比记录表管理页面',
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
            title:'组织机构',
            align:"center",
            dataIndex: 'sysOrgCode_dictText'
          },
          {
            title:'优胜名单',
            align:"center",
            dataIndex: 'namewin'
          },
          {
            title:'优良名单',
            align:"center",
            dataIndex: 'nameelle'
          },
          {
            title:'达标名单',
            align:"center",
            dataIndex: 'namestan'
          },
          {
            title:'季度',
            align:"center",
            dataIndex: 'quarter_dictText'
          },
          // {
          //   title:'优胜数',
          //   align:"center",
          //   dataIndex: 'winningnum'
          // },
          // {
          //   title:'优良数',
          //   align:"center",
          //   dataIndex: 'excellentnum'
          // },
          // {
          //   title:'达标数',
          //   align:"center",
          //   dataIndex: 'standardnum'
          // },
          {
            title:'评比状态',
            align:"center",
            dataIndex: 'status',
            scopedSlots: { customRender: 'tags' }
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
          list: "/comparisonrecord/comparisonRecord/list",
          delete: "/comparisonrecord/comparisonRecord/delete",
          deleteBatch: "/comparisonrecord/comparisonRecord/deleteBatch",
          exportXlsUrl: "/comparisonrecord/comparisonRecord/exportXls",
          importExcelUrl: "comparisonrecord/comparisonRecord/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getToken()
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
        fieldList.push({type:'string',value:'name',text:'姓名',dictCode:''})
        fieldList.push({type:'int',value:'quarter',text:'季度 1 第一季度 2 第二季度 3 第三季度  4 第四季度',dictCode:''})
        fieldList.push({type:'int',value:'winningnum',text:'优胜数',dictCode:''})
        fieldList.push({type:'int',value:'excellentnum',text:'优良数',dictCode:''})
        fieldList.push({type:'int',value:'standardnum',text:'达标数',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>