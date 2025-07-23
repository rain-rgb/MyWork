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
      <a-button type="primary" icon="download" @click="handleExportXls('anquan_fxrwzk_xinxi')">导出</a-button>
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

    <anquan-fxrwzk-xinxi-modal ref="modalForm" @ok="modalFormOk"></anquan-fxrwzk-xinxi-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import AnquanFxrwzkXinxiModal from './modules/AnquanFxrwzkXinxiModal'

  export default {
    name: 'AnquanFxrwzkXinxiList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      AnquanFxrwzkXinxiModal
    },
    data () {
      return {
        description: 'anquan_fxrwzk_xinxi管理页面',
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
            title:'主表guid',
            align:"center",
            dataIndex: 'baseGuid'
          },
          {
            title:'唯一键',
            align:"center",
            dataIndex: 'baseid'
          },
          {
            title:'项目名称',
            align:"center",
            dataIndex: 'projectname'
          },
          {
            title:'工点名称',
            align:"center",
            dataIndex: 'workPointsName'
          },
          {
            title:'主要内容',
            align:"center",
            dataIndex: 'primaryCoverage'
          },
          {
            title:'风险等级',
            align:"center",
            dataIndex: 'riskLevel'
          },
          {
            title:'施工单位',
            align:"center",
            dataIndex: 'construction'
          },
          {
            title:'监理单位',
            align:"center",
            dataIndex: 'supervisionUnit'
          },
          {
            title:'主要风险因素',
            align:"center",
            dataIndex: 'mainRiskFactor'
          },
          {
            title:'对策措施',
            align:"center",
            dataIndex: 'counterMeasures'
          },
          {
            title:'管控-建设单位',
            align:"center",
            dataIndex: 'managDevelopment'
          },
          {
            title:'管控-施工单位',
            align:"center",
            dataIndex: 'managConstruction'
          },
          {
            title:'管控-监理单位',
            align:"center",
            dataIndex: 'managSupervision'
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
          list: "/anquanfxgk/anquanFxrwzkXinxi/list",
          delete: "/anquanfxgk/anquanFxrwzkXinxi/delete",
          deleteBatch: "/anquanfxgk/anquanFxrwzkXinxi/deleteBatch",
          exportXlsUrl: "/anquanfxgk/anquanFxrwzkXinxi/exportXls",
          importExcelUrl: "anquanfxgk/anquanFxrwzkXinxi/importExcel",
          
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
        fieldList.push({type:'string',value:'baseGuid',text:'主表guid'})
        fieldList.push({type:'string',value:'baseid',text:'唯一键'})
        fieldList.push({type:'string',value:'projectname',text:'项目名称'})
        fieldList.push({type:'string',value:'workPointsName',text:'工点名称'})
        fieldList.push({type:'string',value:'primaryCoverage',text:'主要内容'})
        fieldList.push({type:'string',value:'riskLevel',text:'风险等级'})
        fieldList.push({type:'string',value:'construction',text:'施工单位'})
        fieldList.push({type:'string',value:'supervisionUnit',text:'监理单位'})
        fieldList.push({type:'string',value:'mainRiskFactor',text:'主要风险因素'})
        fieldList.push({type:'string',value:'counterMeasures',text:'对策措施'})
        fieldList.push({type:'string',value:'managDevelopment',text:'管控-建设单位'})
        fieldList.push({type:'string',value:'managConstruction',text:'管控-施工单位'})
        fieldList.push({type:'string',value:'managSupervision',text:'管控-监理单位'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>