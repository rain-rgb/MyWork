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
      <a-button type="primary" icon="download" @click="handleExportXls('孔径通道采样数据')">导出</a-button>
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

    <kongjing-pointsdata-modal ref="modalForm" @ok="modalFormOk"></kongjing-pointsdata-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import KongjingPointsdataModal from './modules/KongjingPointsdataModal'

  export default {
    name: 'KongjingPointsdataList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      KongjingPointsdataModal
    },
    data () {
      return {
        description: '孔径通道采样数据管理页面',
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
            title:'基本信息 id',
            align:"center",
            dataIndex: 'basicinfoid'
          },
          {
            title:'通道号 0、1、2、3（其依次对 应 X、X'、Y、Y'）',
            align:"center",
            dataIndex: 'passway'
          },
          {
            title:'深度 单位 m',
            align:"center",
            dataIndex: 'depth'
          },
          {
            title:'声时 单位 us',
            align:"center",
            dataIndex: 'soundtime'
          },
          {
            title:'泥浆声速 单位km/s',
            align:"center",
            dataIndex: 'soundspeed'
          },
          {
            title:'波幅',
            align:"center",
            dataIndex: 'amplitude'
          },
          {
            title:'方位角',
            align:"center",
            dataIndex: 'azimuth'
          },
          {
            title:'半径 单位mm',
            align:"center",
            dataIndex: 'radius'
          },
          {
            title:'增益 ',
            align:"center",
            dataIndex: 'gain'
          },
          {
            title:'延时 单位us',
            align:"center",
            dataIndex: 'delay'
          },
          {
            title:'增强声时 单位us',
            align:"center",
            dataIndex: 'enhancetime'
          },
          {
            title:'修正距离 单位mm',
            align:"center",
            dataIndex: 'correctiondistance'
          },
          {
            title:'修正方向 0:上 90:左 180:下 270: 右',
            align:"center",
            dataIndex: 'correctiondirection'
          },
          {
            title:'噪声档位 范围[0-40]每档 50us 间 隔',
            align:"center",
            dataIndex: 'adjustinggear'
          },
          {
            title:'测点时间 记录离始测点的时间; 始测点为0s ; 单位秒',
            align:"center",
            dataIndex: 'testtime'
          },
          {
            title:'数据  波形数值',
            align:"center",
            dataIndex: 'wavedata'
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
          list: "/kongjingpointsdata/kongjingPointsdata/list",
          delete: "/kongjingpointsdata/kongjingPointsdata/delete",
          deleteBatch: "/kongjingpointsdata/kongjingPointsdata/deleteBatch",
          exportXlsUrl: "/kongjingpointsdata/kongjingPointsdata/exportXls",
          importExcelUrl: "kongjingpointsdata/kongjingPointsdata/importExcel",
          
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
        fieldList.push({type:'string',value:'basicinfoid',text:'基本信息 id',dictCode:''})
        fieldList.push({type:'int',value:'passway',text:'通道号 0、1、2、3（其依次对 应 X、X'、Y、Y'）',dictCode:''})
        fieldList.push({type:'double',value:'depth',text:'深度 单位 m',dictCode:''})
        fieldList.push({type:'double',value:'soundtime',text:'声时 单位 us',dictCode:''})
        fieldList.push({type:'double',value:'soundspeed',text:'泥浆声速 单位km/s',dictCode:''})
        fieldList.push({type:'double',value:'amplitude',text:'波幅',dictCode:''})
        fieldList.push({type:'double',value:'azimuth',text:'方位角',dictCode:''})
        fieldList.push({type:'int',value:'radius',text:'半径 单位mm',dictCode:''})
        fieldList.push({type:'double',value:'gain',text:'增益 ',dictCode:''})
        fieldList.push({type:'double',value:'delay',text:'延时 单位us',dictCode:''})
        fieldList.push({type:'double',value:'enhancetime',text:'增强声时 单位us',dictCode:''})
        fieldList.push({type:'double',value:'correctiondistance',text:'修正距离 单位mm',dictCode:''})
        fieldList.push({type:'int',value:'correctiondirection',text:'修正方向 0:上 90:左 180:下 270: 右',dictCode:''})
        fieldList.push({type:'int',value:'adjustinggear',text:'噪声档位 范围[0-40]每档 50us 间 隔',dictCode:''})
        fieldList.push({type:'int',value:'testtime',text:'测点时间 记录离始测点的时间; 始测点为0s ; 单位秒',dictCode:''})
        fieldList.push({type:'string',value:'wavedata',text:'数据  波形数值',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>