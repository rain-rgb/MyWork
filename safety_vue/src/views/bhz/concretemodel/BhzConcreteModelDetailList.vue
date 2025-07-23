<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="模具名称">
              <a-input v-model="queryParam.name" placeholder="请输入试块模具名称"  ></a-input>
<!--              <j-search-select-tag placeholder="输入模具名称" v-model="queryParam.code" :dictOptions="dictOption">-->
<!--              </j-search-select-tag>-->
<!--              {{ selectValue }}-->
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date
                placeholder="开始时间"
                v-model="queryParam.createTime_begin"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束时间"
                v-model="queryParam.createTime_end"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
<!--      <a-button type="primary" icon="download" @click="handleExportXls('bhz_concrete_model_detail')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
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
      <span slot="smzt" slot-scope="smzt, record">
         <a-tag color="green" v-if="record.smzt == '20'">已完成脱模</a-tag>
         <a-tag color="blue" v-if="record.smzt == '10'">试块制作中</a-tag>
        </span>

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
           <a @click="handleDetail(record)">详情</a>
<!--          <a @click="handleEdit(record)">编辑</a>-->

<!--          <a-divider type="vertical" />-->
<!--          <a-dropdown>-->
<!--            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>-->
<!--            <a-menu slot="overlay">-->
<!--              <a-menu-item>-->
<!--                <a @click="handleDetail(record)">详情</a>-->
<!--              </a-menu-item>-->
<!--              <a-menu-item>-->
<!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
<!--                  <a>删除</a>-->
<!--                </a-popconfirm>-->
<!--              </a-menu-item>-->
<!--            </a-menu>-->
<!--          </a-dropdown>-->
        </span>

      </a-table>
    </div>

    <bhz-concrete-model-detail-modal ref="modalForm" @ok="modalFormOk"></bhz-concrete-model-detail-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BhzConcreteModelDetailModal from './modules/BhzConcreteModelDetailModal'

  export default {
    name: 'BhzConcreteModelDetailList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BhzConcreteModelDetailModal
    },
    data () {
      return {
        description: 'bhz_concrete_model_detail管理页面',
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
            title:'标段',
            align:"center",
            dataIndex: 'sysOrgCode_dictText'
          },
          {
            title:'模具编码',
            align:"center",
            dataIndex: 'code'
          },
          {
            title:'模具名称',
            align:"center",
            dataIndex: 'name'
          },
          // {
          //   title:'尺寸',
          //   align:"center",
          //   dataIndex: 'chicun'
          // },
          {
            title:'规格',
            align:"center",
            dataIndex: 'guige'
          },
          {
            title:'部位名称',
            align:"center",
            dataIndex: 'wbs'
          },
          {
            title:'开始时间',
            align:"center",
            dataIndex: 'createTime'
          },
          {
            title:'完成时间',
            align:"center",
            dataIndex: 'updateTime'
          },
          // {
          //   title:'任务单号',
          //   align:"center",
          //   dataIndex: 'rwd'
          // },

          {
            title:'试块状态',
            align:"center",
            dataIndex: 'smzt',
            scopedSlots: { customRender: 'smzt' },
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
          list: "/bhzconcretemodel/bhzConcreteModelDetail/list",
          delete: "/bhzconcretemodel/bhzConcreteModelDetail/delete",
          deleteBatch: "/bhzconcretemodel/bhzConcreteModelDetail/deleteBatch",
          exportXlsUrl: "/bhzconcretemodel/bhzConcreteModelDetail/exportXls",
          importExcelUrl: "bhzconcretemodel/bhzConcreteModelDetail/importExcel",

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
        fieldList.push({type:'string',value:'name',text:'试块模具名称'})
        fieldList.push({type:'string',value:'chicun',text:'尺寸'})
        fieldList.push({type:'string',value:'guige',text:'规格'})
        fieldList.push({type:'string',value:'rwd',text:'当前任务单号'})
        fieldList.push({type:'string',value:'code',text:'模具编码'})
        fieldList.push({type:'string',value:'smzt',text:'制模状态10：形成中；20：已完成'})
        fieldList.push({type:'string',value:'wbs',text:'wbs部位名称'})
        fieldList.push({type:'string',value:'note',text:'说明'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>