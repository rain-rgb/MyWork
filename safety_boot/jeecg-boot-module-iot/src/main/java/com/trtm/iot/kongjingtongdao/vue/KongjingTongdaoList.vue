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
      <a-button type="primary" icon="download" @click="handleExportXls('通道参数信息表')">导出</a-button>
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

    <kongjing-tongdao-modal ref="modalForm" @ok="modalFormOk"></kongjing-tongdao-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import KongjingTongdaoModal from './modules/KongjingTongdaoModal'

  export default {
    name: 'KongjingTongdaoList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      KongjingTongdaoModal
    },
    data () {
      return {
        description: '通道参数信息表管理页面',
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
            title:'guid',
            align:"center",
            dataIndex: 'guid'
          },
          {
            title:'通道号 0、1、2、3（其依次对 应X、X'、Y、Y'）',
            align:"center",
            dataIndex: 'channelnum'
          },
          {
            title:'状态 0：未启用 1：启用并 显示',
            align:"center",
            dataIndex: 'status'
          },
          {
            title:'中心距 单位mm',
            align:"center",
            dataIndex: 'centerdistance'
          },
          {
            title:'增益',
            align:"center",
            dataIndex: 'gain'
          },
          {
            title:'延时 单位us',
            align:"center",
            dataIndex: 'delay'
          },
          {
            title:'校零时间 单位us',
            align:"center",
            dataIndex: 'zerotime'
          },
          {
            title:'GPS 状态 0 无效 1 有效',
            align:"center",
            dataIndex: 'gpsvalid'
          },
          {
            title:'经度',
            align:"center",
            dataIndex: 'gpslongitude'
          },
          {
            title:'纬度',
            align:"center",
            dataIndex: 'gpslatitude'
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
          list: "/kongjingtongdao/kongjingTongdao/list",
          delete: "/kongjingtongdao/kongjingTongdao/delete",
          deleteBatch: "/kongjingtongdao/kongjingTongdao/deleteBatch",
          exportXlsUrl: "/kongjingtongdao/kongjingTongdao/exportXls",
          importExcelUrl: "kongjingtongdao/kongjingTongdao/importExcel",
          
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
        fieldList.push({type:'string',value:'guid',text:'guid',dictCode:''})
        fieldList.push({type:'int',value:'channelnum',text:'通道号 0、1、2、3（其依次对 应X、X'、Y、Y'）',dictCode:''})
        fieldList.push({type:'int',value:'status',text:'状态 0：未启用 1：启用并 显示',dictCode:''})
        fieldList.push({type:'int',value:'centerdistance',text:'中心距 单位mm',dictCode:''})
        fieldList.push({type:'int',value:'gain',text:'增益',dictCode:''})
        fieldList.push({type:'double',value:'delay',text:'延时 单位us',dictCode:''})
        fieldList.push({type:'double',value:'zerotime',text:'校零时间 单位us',dictCode:''})
        fieldList.push({type:'int',value:'gpsvalid',text:'GPS 状态 0 无效 1 有效',dictCode:''})
        fieldList.push({type:'double',value:'gpslongitude',text:'经度',dictCode:''})
        fieldList.push({type:'double',value:'gpslatitude',text:'纬度',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>