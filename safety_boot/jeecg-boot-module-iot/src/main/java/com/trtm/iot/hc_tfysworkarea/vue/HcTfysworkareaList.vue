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
      <a-button @click="handleExportXls('土方工作区施工成果')" icon="download" type="primary">导出</a-button>
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

    <hc-tfysworkarea-modal @ok="modalFormOk" ref="modalForm"></hc-tfysworkarea-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import HcTfysworkareaModal from './modules/HcTfysworkareaModal'

  export default {
    name: 'HcTfysworkareaList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      HcTfysworkareaModal
    },
    data () {
      return {
        description: '土方工作区施工成果管理页面',
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
            title:'工作区id 唯一',
            align:"center",
            dataIndex: 'blockid'
          },
          {
            title:'工作区名称',
            align:"center",
            dataIndex: 'blockname'
          },
          {
            title:'所属层',
            align:"center",
            dataIndex: 'layername'
          },
          {
            title:'开始桩号 面型工程没有桩号',
            align:"center",
            dataIndex: 'startstation'
          },
          {
            title:'结束桩号 面型工程没有桩号',
            align:"center",
            dataIndex: 'endstation'
          },
          {
            title:'开始时间',
            align:"center",
            dataIndex: 'starttime',
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
            title:'工作时长（h）',
            align:"center",
            dataIndex: 'worktime'
          },
          {
            title:'施工长度（m） 面型工程没有长度',
            align:"center",
            dataIndex: 'workmile'
          },
          {
            title:'
施工面积（㎡）',
            align:"center",
            dataIndex: 'workarea'
          },
          {
            title:'工作区边界坐标，格式：[[北,东]]',
            align:"center",
            dataIndex: 'border'
          },
          {
            title:'开振占比（%）',
            align:"center",
            dataIndex: 'vibrateratio'
          },
          {
            title:'平均碾压遍数',
            align:"center",
            dataIndex: 'timesavg'
          },
          {
            title:'
平均振动遍数',
            align:"center",
            dataIndex: 'timesvibrateavg'
          },
          {
            title:'平均碾压速度（km/h）',
            align:"center",
            dataIndex: 'speedavg'
          },
          {
            title:'平均厚度（m）',
            align:"center",
            dataIndex: 'thickavg'
          },
          {
            title:'告警条数',
            align:"center",
            dataIndex: 'alarmnum'
          },
          {
            title:'施工质量，0正常，1异常，2异常已处理',
            align:"center",
            dataIndex: 'qualitystat'
          },
          {
            title:'施工状态，0施工中，1已完工',
            align:"center",
            dataIndex: 'workstat'
          },
          {
            title:'是否报检，0未报检，1已报检',
            align:"center",
            dataIndex: 'inspectstat'
          },
          {
            title:'projectid',
            align:"center",
            dataIndex: 'projectid'
          },
          {
            title:'sectionid',
            align:"center",
            dataIndex: 'sectionid'
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
          list: "/hc_tfysworkarea/hcTfysworkarea/list",
          delete: "/hc_tfysworkarea/hcTfysworkarea/delete",
          deleteBatch: "/hc_tfysworkarea/hcTfysworkarea/deleteBatch",
          exportXlsUrl: "/hc_tfysworkarea/hcTfysworkarea/exportXls",
          importExcelUrl: "hc_tfysworkarea/hcTfysworkarea/importExcel",

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
        fieldList.push({type:'string',value:'blockid',text:'工作区id 唯一'})
        fieldList.push({type:'string',value:'blockname',text:'工作区名称'})
        fieldList.push({type:'string',value:'layername',text:'所属层'})
        fieldList.push({type:'string',value:'startstation',text:'开始桩号 面型工程没有桩号'})
        fieldList.push({type:'string',value:'endstation',text:'结束桩号 面型工程没有桩号'})
        fieldList.push({type:'date',value:'starttime',text:'开始时间'})
        fieldList.push({type:'date',value:'endtime',text:'结束时间'})
        fieldList.push({type:'string',value:'worktime',text:'工作时长（h）'})
        fieldList.push({type:'string',value:'workmile',text:'施工长度（m） 面型工程没有长度'})
        fieldList.push({type:'string',value:'workarea',text:'
施工面积（㎡）'})
        fieldList.push({type:'string',value:'border',text:'工作区边界坐标，格式：[[北,东]]'})
        fieldList.push({type:'string',value:'vibrateratio',text:'开振占比（%）'})
        fieldList.push({type:'string',value:'timesavg',text:'平均碾压遍数'})
        fieldList.push({type:'string',value:'timesvibrateavg',text:'
平均振动遍数'})
        fieldList.push({type:'string',value:'speedavg',text:'平均碾压速度（km/h）'})
        fieldList.push({type:'string',value:'thickavg',text:'平均厚度（m）'})
        fieldList.push({type:'string',value:'alarmnum',text:'告警条数'})
        fieldList.push({type:'string',value:'qualitystat',text:'施工质量，0正常，1异常，2异常已处理'})
        fieldList.push({type:'string',value:'workstat',text:'施工状态，0施工中，1已完工'})
        fieldList.push({type:'string',value:'inspectstat',text:'是否报检，0未报检，1已报检'})
        fieldList.push({type:'string',value:'projectid',text:'projectid'})
        fieldList.push({type:'string',value:'sectionid',text:'sectionid'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
