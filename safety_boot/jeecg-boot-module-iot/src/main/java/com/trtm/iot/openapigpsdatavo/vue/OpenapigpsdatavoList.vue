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
      <a-button @click="handleExportXls('摊铺碾压数据')" icon="download" type="primary">导出</a-button>
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

    <openapigpsdatavo-modal @ok="modalFormOk" ref="modalForm"></openapigpsdatavo-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import OpenapigpsdatavoModal from './modules/OpenapigpsdatavoModal'

  export default {
    name: 'OpenapigpsdatavoList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      OpenapigpsdatavoModal
    },
    data () {
      return {
        description: '摊铺碾压数据管理页面',
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
            title:'机械终端安装关系id',
            align:"center",
            dataIndex: 'machineid'
          },
          {
            title:'数据上传时间',
            align:"center",
            dataIndex: 'gpstime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'定位质量，1：单点、2：码差分、4：固定、5：浮动',
            align:"center",
            dataIndex: 'qualityindex'
          },
          {
            title:'北坐标',
            align:"center",
            dataIndex: 'north'
          },
          {
            title:'东坐标',
            align:"center",
            dataIndex: 'east'
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
            title:'摊铺温度',
            align:"center",
            dataIndex: 'temperature'
          },
          {
            title:'所在桩号',
            align:"center",
            dataIndex: 'roadstation'
          },
          {
            title:'位置相对道路中线偏移量',
            align:"center",
            dataIndex: 'offset'
          },
          {
            title:'速度(m/s)',
            align:"center",
            dataIndex: 'velocity'
          },
          {
            title:'温度离析(℃)',
            align:"center",
            dataIndex: 'tempdiff'
          },
          {
            title:'高程',
            align:"center",
            dataIndex: 'geoh'
          },
          {
            title:'压实度',
            align:"center",
            dataIndex: 'cmv'
          },
          {
            title:'振动频率',
            align:"center",
            dataIndex: 'frequency'
          },
          {
            title:'运料车rfid',
            align:"center",
            dataIndex: 'rfid'
          },
          {
            title:'所属层，层属于后处理计算结果，实时性一般，初始值为65535，计算后会将其更新',
            align:"center",
            dataIndex: 'layerindex'
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
          list: "/openapigpsdatavo/openapigpsdatavo/list",
          delete: "/openapigpsdatavo/openapigpsdatavo/delete",
          deleteBatch: "/openapigpsdatavo/openapigpsdatavo/deleteBatch",
          exportXlsUrl: "/openapigpsdatavo/openapigpsdatavo/exportXls",
          importExcelUrl: "openapigpsdatavo/openapigpsdatavo/importExcel",

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
        fieldList.push({type:'int',value:'machineid',text:'机械终端安装关系id'})
        fieldList.push({type:'date',value:'gpstime',text:'数据上传时间'})
        fieldList.push({type:'string',value:'qualityindex',text:'定位质量，1：单点、2：码差分、4：固定、5：浮动'})
        fieldList.push({type:'string',value:'north',text:'北坐标'})
        fieldList.push({type:'string',value:'east',text:'东坐标'})
        fieldList.push({type:'string',value:'lon',text:'经度'})
        fieldList.push({type:'string',value:'lat',text:'纬度'})
        fieldList.push({type:'string',value:'temperature',text:'摊铺温度'})
        fieldList.push({type:'string',value:'roadstation',text:'所在桩号'})
        fieldList.push({type:'string',value:'offset',text:'位置相对道路中线偏移量'})
        fieldList.push({type:'string',value:'velocity',text:'速度(m/s)'})
        fieldList.push({type:'string',value:'tempdiff',text:'温度离析(℃)'})
        fieldList.push({type:'string',value:'geoh',text:'高程'})
        fieldList.push({type:'string',value:'cmv',text:'压实度'})
        fieldList.push({type:'string',value:'frequency',text:'振动频率'})
        fieldList.push({type:'string',value:'rfid',text:'运料车rfid'})
        fieldList.push({type:'string',value:'layerindex',text:'所属层，层属于后处理计算结果，实时性一般，初始值为65535，计算后会将其更新'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
