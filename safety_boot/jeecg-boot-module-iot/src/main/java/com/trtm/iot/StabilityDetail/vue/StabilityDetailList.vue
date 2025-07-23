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
      <a-button type="primary" icon="download" @click="handleExportXls('稳定度子表')">导出</a-button>
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

    <stability-detail-modal ref="modalForm" @ok="modalFormOk"></stability-detail-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import StabilityDetailModal from './modules/StabilityDetailModal'

  export default {
    name: 'StabilityDetailList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      StabilityDetailModal
    },
    data () {
      return {
        description: '稳定度子表管理页面',
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
            title:'唯一id',
            align:"center",
            dataIndex: 'fGuid'
          },
          {
            title:'试验id',
            align:"center",
            dataIndex: 'syjid'
          },
          {
            title:'fXh',
            align:"center",
            dataIndex: 'fXh'
          },
          {
            title:'流值',
            align:"center",
            dataIndex: 'liuzhi'
          },
          {
            title:'稳定度',
            align:"center",
            dataIndex: 'wendingdu'
          },
          {
            title:'测试时间',
            align:"center",
            dataIndex: 'testtime'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'fSbbh'
          },
          {
            title:'isvalid',
            align:"center",
            dataIndex: 'isvalid'
          },
          {
            title:'抗压力值',
            align:"center",
            dataIndex: 'fYskylz'
          },
          {
            title:'fYskyxb',
            align:"center",
            dataIndex: 'fYskyxb'
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'status'
          },
          {
            title:'提交时间',
            align:"center",
            dataIndex: 'submittime'
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
          list: "/StabilityDetail/stabilityDetail/list",
          delete: "/StabilityDetail/stabilityDetail/delete",
          deleteBatch: "/StabilityDetail/stabilityDetail/deleteBatch",
          exportXlsUrl: "/StabilityDetail/stabilityDetail/exportXls",
          importExcelUrl: "StabilityDetail/stabilityDetail/importExcel",

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
        fieldList.push({type:'string',value:'fGuid',text:'唯一id'})
        fieldList.push({type:'string',value:'syjid',text:'试验id'})
        fieldList.push({type:'int',value:'fXh',text:'fXh'})
        fieldList.push({type:'number',value:'liuzhi',text:'流值'})
        fieldList.push({type:'number',value:'wendingdu',text:'稳定度'})
        fieldList.push({type:'string',value:'testtime',text:'测试时间'})
        fieldList.push({type:'string',value:'fSbbh',text:'设备编号'})
        fieldList.push({type:'string',value:'isvalid',text:'isvalid'})
        fieldList.push({type:'string',value:'fYskylz',text:'抗压力值'})
        fieldList.push({type:'string',value:'fYskyxb',text:'fYskyxb'})
        fieldList.push({type:'int',value:'status',text:'状态'})
        fieldList.push({type:'string',value:'submittime',text:'提交时间'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
