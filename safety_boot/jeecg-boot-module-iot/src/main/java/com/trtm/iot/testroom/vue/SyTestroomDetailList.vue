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
      <a-button type="primary" icon="download" @click="handleExportXls('sy_testroom_detail')">导出</a-button>
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

    <sy-testroom-detail-modal ref="modalForm" @ok="modalFormOk"></sy-testroom-detail-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyTestroomDetailModal from './modules/SyTestroomDetailModal'

  export default {
    name: 'SyTestroomDetailList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyTestroomDetailModal
    },
    data () {
      return {
        description: 'sy_testroom_detail管理页面',
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
            title:'试验室',
            align:"center",
            dataIndex: 'testroom'
          },
          {
            title:'试验室编号',
            align:"center",
            dataIndex: 'testroomid'
          },
          {
            title:'危险源名称',
            align:"center",
            dataIndex: 'weixianname'
          },
          {
            title:'危险编号',
            align:"center",
            dataIndex: 'wxid'
          },
          {
            title:'危险级别',
            align:"center",
            dataIndex: 'wxlevel'
          },
          {
            title:'易发生事故伤害种类',
            align:"center",
            dataIndex: 'miaoshu'
          },
          {
            title:'控制要点',
            align:"center",
            dataIndex: 'kongzhipoint'
          },
          {
            title:'责任人',
            align:"center",
            dataIndex: 'zerenr'
          },
          {
            title:'检查周期',
            align:"center",
            dataIndex: 'jianchazhouqi'
          },
          {
            title:'公司名称',
            align:"center",
            dataIndex: 'company'
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
          list: "/testroom/syTestroomDetail/list",
          delete: "/testroom/syTestroomDetail/delete",
          deleteBatch: "/testroom/syTestroomDetail/deleteBatch",
          exportXlsUrl: "/testroom/syTestroomDetail/exportXls",
          importExcelUrl: "testroom/syTestroomDetail/importExcel",
          
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
        fieldList.push({type:'string',value:'testroom',text:'试验室'})
        fieldList.push({type:'string',value:'testroomid',text:'试验室编号'})
        fieldList.push({type:'string',value:'weixianname',text:'危险源名称'})
        fieldList.push({type:'string',value:'wxid',text:'危险编号'})
        fieldList.push({type:'string',value:'wxlevel',text:'危险级别'})
        fieldList.push({type:'string',value:'miaoshu',text:'易发生事故伤害种类'})
        fieldList.push({type:'string',value:'kongzhipoint',text:'控制要点'})
        fieldList.push({type:'string',value:'zerenr',text:'责任人'})
        fieldList.push({type:'string',value:'jianchazhouqi',text:'检查周期'})
        fieldList.push({type:'string',value:'company',text:'公司名称'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>