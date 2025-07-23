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
      <a-button @click="handleExportXls('华测获取标段')" icon="download" type="primary">导出</a-button>
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

    <hc-section-modal @ok="modalFormOk" ref="modalForm"></hc-section-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import HcSectionModal from './modules/HcSectionModal'

  export default {
    name: 'HcSectionList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      HcSectionModal
    },
    data () {
      return {
        description: '华测获取标段管理页面',
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
            title:'标段ID',
            align:"center",
            dataIndex: 'sectionid'
          },
          {
            title:'标段名',
            align:"center",
            dataIndex: 'sectionname'
          },
          {
            title:'左右幅，0是其他，1是双幅，2是单幅',
            align:"center",
            dataIndex: 'offsettype'
          },
          {
            title:'设置单遍复遍，0为单遍，1为复遍',
            align:"center",
            dataIndex: 'timestype'
          },
          {
            title:'分层模式：0不分层，2路基块自动分层，5路面信息化自动分层，6土方压实自动分层，7土方压实手动分层(压路机手点击开始结束)',
            align:"center",
            dataIndex: 'stratifymode'
          },
          {
            title:'标段边界北东数据，格式：北坐标,东坐标 北坐标,东坐标',
            align:"center",
            dataIndex: 'sectionboundary'
          },
          {
            title:'标段边界经纬度数据，格式：[[经度,纬度],[经度,纬度]]',
            align:"center",
            dataIndex: 'sectionwgsboundary'
          },
          {
            title:'开始施工日期',
            align:"center",
            dataIndex: 'sectionstartdate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'监理单位id',
            align:"center",
            dataIndex: 'sectionsupcompanyid'
          },
          {
            title:'监理单位名称',
            align:"center",
            dataIndex: 'sectionsupcompanyname'
          },
          {
            title:'施工单位id',
            align:"center",
            dataIndex: 'sectionbuildercompanyid'
          },
          {
            title:'施工单位名称',
            align:"center",
            dataIndex: 'sectionbuildercompanyname'
          },
          {
            title:'对应的标段编码',
            align:"center",
            dataIndex: 'orgcode'
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
          list: "/hc_section/hcSection/list",
          delete: "/hc_section/hcSection/delete",
          deleteBatch: "/hc_section/hcSection/deleteBatch",
          exportXlsUrl: "/hc_section/hcSection/exportXls",
          importExcelUrl: "hc_section/hcSection/importExcel",

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
        fieldList.push({type:'string',value:'sectionid',text:'标段ID'})
        fieldList.push({type:'string',value:'sectionname',text:'标段名'})
        fieldList.push({type:'string',value:'offsettype',text:'左右幅，0是其他，1是双幅，2是单幅'})
        fieldList.push({type:'string',value:'timestype',text:'设置单遍复遍，0为单遍，1为复遍'})
        fieldList.push({type:'string',value:'stratifymode',text:'分层模式：0不分层，2路基块自动分层，5路面信息化自动分层，6土方压实自动分层，7土方压实手动分层(压路机手点击开始结束)'})
        fieldList.push({type:'string',value:'sectionboundary',text:'标段边界北东数据，格式：北坐标,东坐标 北坐标,东坐标'})
        fieldList.push({type:'string',value:'sectionwgsboundary',text:'标段边界经纬度数据，格式：[[经度,纬度],[经度,纬度]]'})
        fieldList.push({type:'date',value:'sectionstartdate',text:'开始施工日期'})
        fieldList.push({type:'string',value:'sectionsupcompanyid',text:'监理单位id'})
        fieldList.push({type:'string',value:'sectionsupcompanyname',text:'监理单位名称'})
        fieldList.push({type:'string',value:'sectionbuildercompanyid',text:'施工单位id'})
        fieldList.push({type:'string',value:'sectionbuildercompanyname',text:'施工单位名称'})
        fieldList.push({type:'string',value:'orgcode',text:'对应的标段编码'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
