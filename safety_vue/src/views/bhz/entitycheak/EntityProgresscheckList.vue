<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="单位工程">
              <a-input placeholder="请输入单位工程" v-model="queryParam.unitProject"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="工程类型">
              <j-dict-select-tag placeholder="请选择工程类型" v-model="queryParam.projectType" dictCode="projectType"></j-dict-select-tag>
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
      <a-button @click="handleAdd" type="primary" icon="plus" v-has="'entitupro:add'">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('实体进度清单数据表')" v-has="'entitupro:dc'">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import" v-has="'entitupro:dr'">导入</a-button>
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
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
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
          <a v-has="'entitupro:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'entitupro:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <entity-progresscheck-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import EntityProgresscheckModal from './modules/EntityProgresscheckModal'
  import '@/assets/less/TableExpand.less'
  import { handertoken } from '@/mixins/getHanderToken'

  export default {
    name: "EntityProgresscheckList",
    mixins:[JeecgListMixin,handertoken],
    components: {
      EntityProgresscheckModal
    },
    data () {
      return {
        description: '实体进度清单数据表管理页面',
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
            title:'单位工程',
            align:"center",
            dataIndex: 'unitProject'
          },
          {
            title:'工程类型',
            align:"center",
            dataIndex: 'projectType_dictText'
          },
          {
            title:'设计总数量',
            align:"center",
            dataIndex: 'designQuantity'
          },
          {
            title:'开累完成总数量',
            align:"center",
            dataIndex: 'finishedAmount'
          },
          {
            title:'剩余数量',
            align:"center",
            dataIndex: 'remainingAmount'
          },
          {
            title:'开累完成进度(%)',
            align:"center",
            dataIndex: 'schedule'
          },
          {
            title:'数量单位',
            align:"center",
            dataIndex: 'quantityUnit'
          },
          {
            title:'是否是关键节点',
            align:"center",
            dataIndex: 'iskey'
          },
          // {
          //   title:'状态',
          //   align:"center",
          //   dataIndex: 'status'
          // },
          // {
          //   title:'备注',
          //   align:"center",
          //   dataIndex: 'remark'
          // },
          // {
          //   title:'唯一码',
          //   align:"center",
          //   dataIndex: 'uuid'
          // },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/entityprogresscheck/entityProgresscheck/list",
          delete: "/entityprogresscheck/entityProgresscheck/delete",
          deleteBatch: "/entityprogresscheck/entityProgresscheck/deleteBatch",
          exportXlsUrl: "/entityprogresscheck/entityProgresscheck/exportXls",
          importExcelUrl: "entityprogresscheck/entityProgresscheck/importExcel",

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
      }
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
         fieldList.push({type:'string',value:'projectType',text:'工程类型',dictCode:''})
         fieldList.push({type:'string',value:'designQuantity',text:'设计总数量',dictCode:''})
         fieldList.push({type:'string',value:'finishedAmount',text:'开累完成总数量',dictCode:''})
         fieldList.push({type:'string',value:'remainingAmount',text:'剩余数量',dictCode:''})
         fieldList.push({type:'string',value:'schedule',text:'开累完成进度(%)',dictCode:''})
         fieldList.push({type:'string',value:'quantityUnit',text:'数量单位',dictCode:''})
         fieldList.push({type:'string',value:'unitProject',text:'单位工程',dictCode:''})
         fieldList.push({type:'string',value:'iskey',text:'是否是关键节点',dictCode:''})
         fieldList.push({type:'int',value:'status',text:'状态',dictCode:''})
         fieldList.push({type:'string',value:'remark',text:'备注',dictCode:''})
         fieldList.push({type:'string',value:'uuid',text:'唯一码',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>