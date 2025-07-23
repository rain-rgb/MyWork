<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form @keyup.enter.native="searchQuery" layout="inline">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" icon="plus" type="primary">新增</a-button>
      <a-button @click="handleExportXls('摊铺里程分布')" icon="download" type="primary">导出</a-button>
      <a-upload :action="importExcelUrl" :headers="tokenHeader" :multiple="false" :showUploadList="false" @change="handleImportExcel" name="file">
        <a-button icon="import" type="primary">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" @handleSuperQuery="handleSuperQuery" ref="superQueryModal"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item @click="batchDel" key="1"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a @click="onClearSelected" style="margin-left: 24px">清空</a>
      </div>

      <a-table
        :columns="columns"
        :dataSource="dataSource"
        :loading="loading"
        :pagination="ipagination"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        :scroll="{x:true}"
        @change="handleTableChange"
        bordered
        class="j-table-force-nowrap"
        ref="table"
        rowKey="id"
        size="middle">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span style="font-size: 12px;font-style: italic;" v-if="!text">无图片</span>
          <img :src="getImgView(text)" alt="" height="25px" style="max-width:80px;font-size: 12px;font-style: italic;" v-else/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span style="font-size: 12px;font-style: italic;" v-if="!text">无文件</span>
          <a-button
            :ghost="true"
            @click="downloadFile(text)"
            icon="download"
            size="small"
            type="primary"
            v-else>
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
                <a-popconfirm @confirm="() => handleDelete(record.id)" title="确定删除吗?">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <hc-result-collect-modal @ok="modalFormOk" ref="modalForm"></hc-result-collect-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import HcResultCollectModal from './modules/HcResultCollectModal'

  export default {
    name: 'HcResultCollectList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      HcResultCollectModal
    },
    data () {
      return {
        description: '摊铺里程分布管理页面',
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
            title:'主键id',
            align:"center",
            dataIndex: 'blockid'
          },
          {
            title:'日期',
            align:"center",
            dataIndex: 'paveday',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'开始时间',
            align:"center",
            dataIndex: 'begintime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'结束时间',
            align:"center",
            dataIndex: 'endtime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'projectname'
          },
          {
            title:'工程id',
            align:"center",
            dataIndex: 'projectid'
          },
          {
            title:'标段名称',
            align:"center",
            dataIndex: 'sectionname'
          },
          {
            title:'标段id',
            align:"center",
            dataIndex: 'sectionid'
          },
          {
            title:'分层名称',
            align:"center",
            dataIndex: 'layername'
          },
          {
            title:'分层id',
            align:"center",
            dataIndex: 'layerindex'
          },
          {
            title:'
开始桩号',
            align:"center",
            dataIndex: 'beginstation'
          },
          {
            title:'格式化后的开始桩号',
            align:"center",
            dataIndex: 'beginstationformat'
          },
          {
            title:'结束桩号',
            align:"center",
            dataIndex: 'endstation'
          },
          {
            title:'格式化后的结束桩号',
            align:"center",
            dataIndex: 'endstationformat'
          },
          {
            title:'幅,0-左幅,1-右幅,2-全幅',
            align:"center",
            dataIndex: 'roadside'
          },
          {
            title:'分层方式,0-自动,1-手动',
            align:"center",
            dataIndex: 'createtype'
          },
          {
            title:'摊铺厚度',
            align:"center",
            dataIndex: 'thickavg'
          },
          {
            title:'摊铺里程',
            align:"center",
            dataIndex: 'pavemileage'
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
          list: "/hc_result_collect/hcResultCollect/list",
          delete: "/hc_result_collect/hcResultCollect/delete",
          deleteBatch: "/hc_result_collect/hcResultCollect/deleteBatch",
          exportXlsUrl: "/hc_result_collect/hcResultCollect/exportXls",
          importExcelUrl: "hc_result_collect/hcResultCollect/importExcel",

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
        fieldList.push({type:'string',value:'blockid',text:'主键id'})
        fieldList.push({type:'date',value:'paveday',text:'日期'})
        fieldList.push({type:'date',value:'begintime',text:'开始时间'})
        fieldList.push({type:'date',value:'endtime',text:'结束时间'})
        fieldList.push({type:'string',value:'projectname',text:'工程名称'})
        fieldList.push({type:'string',value:'projectid',text:'工程id'})
        fieldList.push({type:'string',value:'sectionname',text:'标段名称'})
        fieldList.push({type:'string',value:'sectionid',text:'标段id'})
        fieldList.push({type:'string',value:'layername',text:'分层名称'})
        fieldList.push({type:'string',value:'layerindex',text:'分层id'})
        fieldList.push({type:'string',value:'beginstation',text:'
开始桩号'})
        fieldList.push({type:'string',value:'beginstationformat',text:'格式化后的开始桩号'})
        fieldList.push({type:'string',value:'endstation',text:'结束桩号'})
        fieldList.push({type:'string',value:'endstationformat',text:'格式化后的结束桩号'})
        fieldList.push({type:'int',value:'roadside',text:'幅,0-左幅,1-右幅,2-全幅'})
        fieldList.push({type:'int',value:'createtype',text:'分层方式,0-自动,1-手动'})
        fieldList.push({type:'string',value:'thickavg',text:'摊铺厚度'})
        fieldList.push({type:'string',value:'pavemileage',text:'摊铺里程'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
