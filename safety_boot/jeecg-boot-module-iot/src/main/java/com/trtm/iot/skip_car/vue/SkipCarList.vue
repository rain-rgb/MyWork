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
      <a-button @click="handleExportXls('运输温度')" icon="download" type="primary">导出</a-button>
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
        :pagination="ipagination"
        :scroll="{x:true}"
        @change="handleTableChange"
        bordered
        ref="table"
        :columns="columns"
        :dataSource="dataSource"
        rowKey="id"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
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
            @click="downloadFile(text)"
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
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

    <skip-car-modal @ok="modalFormOk" ref="modalForm"></skip-car-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SkipCarModal from './modules/SkipCarModal'

  export default {
    name: 'SkipCarList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SkipCarModal
    },
    data () {
      return {
        description: '运输温度管理页面',
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
            title:'设备编号',
            align:"center",
            dataIndex: 'deviceid'
          },
          {
            title:'温度',
            align:"center",
            dataIndex: 'temperature'
          },
          {
            title:'采集时间',
            align:"center",
            dataIndex: 'collectiontime'
          },
          {
            title:'航向',
            align:"center",
            dataIndex: 'hangxiang'
          },
          {
            title:'经度',
            align:"center",
            dataIndex: 'lon'
          },
          {
            title:'纬度',
            align:"center",
            dataIndex: 'lat'
          },
          {
            title:'高程',
            align:"center",
            dataIndex: 'gaocheng'
          },
          {
            title:'速度',
            align:"center",
            dataIndex: 'speed'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'devicename'
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
          list: "/skip_car/skipCar/list",
          delete: "/skip_car/skipCar/delete",
          deleteBatch: "/skip_car/skipCar/deleteBatch",
          exportXlsUrl: "/skip_car/skipCar/exportXls",
          importExcelUrl: "skip_car/skipCar/importExcel",

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
        fieldList.push({type:'string',value:'deviceid',text:'设备编号'})
        fieldList.push({type:'string',value:'temperature',text:'温度'})
        fieldList.push({type:'string',value:'collectiontime',text:'采集时间'})
        fieldList.push({type:'string',value:'hangxiang',text:'航向'})
        fieldList.push({type:'string',value:'lon',text:'经度'})
        fieldList.push({type:'string',value:'lat',text:'纬度'})
        fieldList.push({type:'string',value:'gaocheng',text:'高程'})
        fieldList.push({type:'number',value:'speed',text:'速度'})
        fieldList.push({type:'string',value:'devicename',text:'设备名称'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
