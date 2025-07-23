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
      <a-button type="primary" icon="download" @click="handleExportXls('chaoshengbo_syljzs')">导出</a-button>
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

    <chaoshengbo-syljzs-modal ref="modalForm" @ok="modalFormOk"></chaoshengbo-syljzs-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ChaoshengboSyljzsModal from './modules/ChaoshengboSyljzsModal'

  export default {
    name: 'ChaoshengboSyljzsList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ChaoshengboSyljzsModal
    },
    data () {
      return {
        description: 'chaoshengbo_syljzs管理页面',
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
            title:'设备商标识',
            align:"center",
            dataIndex: 'shebeino'
          },
          {
            title:'测试仪编号：仪器商必须保证编号的唯一性',
            align:"center",
            dataIndex: 'ceshiyino'
          },
          {
            title:'剖面列表:varchar(40),剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3',
            align:"center",
            dataIndex: 'poumianlist'
          },
          {
            title:'流水号',
            align:"center",
            dataIndex: 'liushuihao'
          },
          {
            title:'试桩编号',
            align:"center",
            dataIndex: 'shizhuangno'
          },
          {
            title:'传输内容: 7：原始数据临界值包，17：分析数据临界值包',
            align:"center",
            dataIndex: 'chuanshuleirong'
          },
          {
            title:'包序号：用于异步传输是统计回包是否全部传输完成',
            align:"center",
            dataIndex: 'baoxuhao'
          },
          {
            title:'剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成',
            align:"center",
            dataIndex: 'poumian1'
          },
          {
            title:'剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成',
            align:"center",
            dataIndex: 'poumian2'
          },
          {
            title:'剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成',
            align:"center",
            dataIndex: 'poumian3'
          },
          {
            title:'剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成',
            align:"center",
            dataIndex: 'poumian4'
          },
          {
            title:'剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成',
            align:"center",
            dataIndex: 'poumian5'
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
          list: "/chaoshengbo/chaoshengboSyljzs/list",
          delete: "/chaoshengbo/chaoshengboSyljzs/delete",
          deleteBatch: "/chaoshengbo/chaoshengboSyljzs/deleteBatch",
          exportXlsUrl: "/chaoshengbo/chaoshengboSyljzs/exportXls",
          importExcelUrl: "chaoshengbo/chaoshengboSyljzs/importExcel",
          
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
        fieldList.push({type:'string',value:'shebeino',text:'设备商标识'})
        fieldList.push({type:'string',value:'ceshiyino',text:'测试仪编号：仪器商必须保证编号的唯一性'})
        fieldList.push({type:'string',value:'poumianlist',text:'剖面列表:varchar(40),剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3'})
        fieldList.push({type:'string',value:'liushuihao',text:'流水号'})
        fieldList.push({type:'string',value:'shizhuangno',text:'试桩编号'})
        fieldList.push({type:'int',value:'chuanshuleirong',text:'传输内容: 7：原始数据临界值包，17：分析数据临界值包'})
        fieldList.push({type:'int',value:'baoxuhao',text:'包序号：用于异步传输是统计回包是否全部传输完成'})
        fieldList.push({type:'string',value:'poumian1',text:'剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成'})
        fieldList.push({type:'string',value:'poumian2',text:'剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成'})
        fieldList.push({type:'string',value:'poumian3',text:'剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成'})
        fieldList.push({type:'string',value:'poumian4',text:'剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成'})
        fieldList.push({type:'string',value:'poumian5',text:'剖面数据1-5:以”,”分隔组成的数据,由平均声速，声速异常判定值（临界值），声速标准差，离散系数，均匀性等级，类别，波速最小值，平均波幅，波幅临界值，波幅最小值10个float类型数据组成'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>