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
      <a-button @click="handleExportXls('摊铺碾压')" icon="download" type="primary">导出</a-button>
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

    <spreadandcrush-modal @ok="modalFormOk" ref="modalForm"></spreadandcrush-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SpreadandcrushModal from './modules/SpreadandcrushModal'

  export default {
    name: 'SpreadandcrushList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SpreadandcrushModal
    },
    data () {
      return {
        description: '摊铺碾压管理页面',
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
            title:'桩号',
            align:"center",
            dataIndex: 'station'
          },
          {
            title:'结构层，数值越大为越高层',
            align:"center",
            dataIndex: 'layer'
          },
          {
            title:'左右幅，0为左幅，1为右幅',
            align:"center",
            dataIndex: 'offset'
          },
          {
            title:'摊铺温度（°C）',
            align:"center",
            dataIndex: 'pavingtemperature'
          },
          {
            title:'温度离析（°C）',
            align:"center",
            dataIndex: 'temperaturesegregation'
          },
          {
            title:'摊铺速度（m/min）',
            align:"center",
            dataIndex: 'pavingspeed'
          },
          {
            title:'初压温度（°C）钢轮最高温度',
            align:"center",
            dataIndex: 'temperaturefirst'
          },
          {
            title:'复压温度（°C）胶轮最高温度',
            align:"center",
            dataIndex: 'temperaturerepeat'
          },
          {
            title:'初压遍数（遍）钢轮遍数',
            align:"center",
            dataIndex: 'timesfirst'
          },
          {
            title:'复压遍数（遍）胶轮遍数',
            align:"center",
            dataIndex: 'timesrepeat'
          },
          {
            title:'钢轮欠压率',
            align:"center",
            dataIndex: 'steelrate'
          },
          {
            title:'胶轮欠压率',
            align:"center",
            dataIndex: 'rubberrate'
          },
          {
            title:'1:摊铺；2:碾压',
            align:"center",
            dataIndex: 'type'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'shebeino'
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
          list: "/spreadandcrush/spreadandcrush/list",
          delete: "/spreadandcrush/spreadandcrush/delete",
          deleteBatch: "/spreadandcrush/spreadandcrush/deleteBatch",
          exportXlsUrl: "/spreadandcrush/spreadandcrush/exportXls",
          importExcelUrl: "spreadandcrush/spreadandcrush/importExcel",

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
        fieldList.push({type:'string',value:'station',text:'桩号'})
        fieldList.push({type:'string',value:'layer',text:'结构层，数值越大为越高层'})
        fieldList.push({type:'int',value:'offset',text:'左右幅，0为左幅，1为右幅'})
        fieldList.push({type:'string',value:'pavingtemperature',text:'摊铺温度（°C）'})
        fieldList.push({type:'string',value:'temperaturesegregation',text:'温度离析（°C）'})
        fieldList.push({type:'number',value:'pavingspeed',text:'摊铺速度（m/min）'})
        fieldList.push({type:'string',value:'temperaturefirst',text:'初压温度（°C）钢轮最高温度'})
        fieldList.push({type:'string',value:'temperaturerepeat',text:'复压温度（°C）胶轮最高温度'})
        fieldList.push({type:'string',value:'timesfirst',text:'初压遍数（遍）钢轮遍数'})
        fieldList.push({type:'string',value:'timesrepeat',text:'复压遍数（遍）胶轮遍数'})
        fieldList.push({type:'string',value:'steelrate',text:'钢轮欠压率'})
        fieldList.push({type:'string',value:'rubberrate',text:'胶轮欠压率'})
        fieldList.push({type:'int',value:'type',text:'1:摊铺；2:碾压'})
        fieldList.push({type:'string',value:'shebeino',text:'设备编号'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
