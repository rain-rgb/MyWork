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
      <a-button type="primary" icon="download" @click="handleExportXls('device_mixpile_rwdsta')">导出</a-button>
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

    <device-mixpile-rwdsta-modal ref="modalForm" @ok="modalFormOk"></device-mixpile-rwdsta-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceMixpileRwdstaModal from './modules/DeviceMixpileRwdstaModal'

  export default {
    name: 'DeviceMixpileRwdstaList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceMixpileRwdstaModal
    },
    data () {
      return {
        description: 'device_mixpile_rwdsta管理页面',
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
            title:'软基工单号',
            align:"center",
            dataIndex: 'rjrwd'
          },
          {
            title:'组织机构',
            align:"center",
            dataIndex: 'orgcode'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'shebeino'
          },
          {
            title:'里程',
            align:"center",
            dataIndex: 'mileage'
          },
          {
            title:'设计桩基数',
            align:"center",
            dataIndex: 'descount'
          },
          {
            title:'开工日期',
            align:"center",
            dataIndex: 'starttime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'截止日期',
            align:"center",
            dataIndex: 'endtime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'rjstatus',
            align:"center",
            dataIndex: 'rjstatus'
          },
          {
            title:'totalpro',
            align:"center",
            dataIndex: 'totalpro'
          },
          {
            title:'mileagetotal',
            align:"center",
            dataIndex: 'mileagetotal'
          },
          {
            title:'chaobiaototal',
            align:"center",
            dataIndex: 'chaobiaototal'
          },
          {
            title:'handled',
            align:"center",
            dataIndex: 'handled'
          },
          {
            title:'checked',
            align:"center",
            dataIndex: 'checked'
          },
          {
            title:'chaobiaolv',
            align:"center",
            dataIndex: 'chaobiaolv'
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
          list: "/devicemixpilerwdsta/deviceMixpileRwdsta/list",
          delete: "/devicemixpilerwdsta/deviceMixpileRwdsta/delete",
          deleteBatch: "/devicemixpilerwdsta/deviceMixpileRwdsta/deleteBatch",
          exportXlsUrl: "/devicemixpilerwdsta/deviceMixpileRwdsta/exportXls",
          importExcelUrl: "devicemixpilerwdsta/deviceMixpileRwdsta/importExcel",
          
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
        fieldList.push({type:'string',value:'rjrwd',text:'软基工单号'})
        fieldList.push({type:'string',value:'orgcode',text:'组织机构'})
        fieldList.push({type:'string',value:'shebeino',text:'设备编号'})
        fieldList.push({type:'string',value:'mileage',text:'里程'})
        fieldList.push({type:'int',value:'descount',text:'设计桩基数'})
        fieldList.push({type:'date',value:'starttime',text:'开工日期'})
        fieldList.push({type:'date',value:'endtime',text:'截止日期'})
        fieldList.push({type:'int',value:'rjstatus',text:'rjstatus'})
        fieldList.push({type:'number',value:'totalpro',text:'totalpro'})
        fieldList.push({type:'number',value:'mileagetotal',text:'mileagetotal'})
        fieldList.push({type:'number',value:'chaobiaototal',text:'chaobiaototal'})
        fieldList.push({type:'number',value:'handled',text:'handled'})
        fieldList.push({type:'number',value:'checked',text:'checked'})
        fieldList.push({type:'number',value:'chaobiaolv',text:'chaobiaolv'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>