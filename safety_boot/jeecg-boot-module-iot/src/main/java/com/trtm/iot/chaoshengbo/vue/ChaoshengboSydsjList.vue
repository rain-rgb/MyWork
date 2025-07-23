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
      <a-button type="primary" icon="download" @click="handleExportXls('chaoshengbo_sydsj')">导出</a-button>
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

    <chaoshengbo-sydsj-modal ref="modalForm" @ok="modalFormOk"></chaoshengbo-sydsj-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ChaoshengboSydsjModal from './modules/ChaoshengboSydsjModal'

  export default {
    name: 'ChaoshengboSydsjList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ChaoshengboSydsjModal
    },
    data () {
      return {
        description: 'chaoshengbo_sydsj管理页面',
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
            title:'剖面列表:剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3',
            align:"center",
            dataIndex: 'poumianlist'
          },
          {
            title:'包序号：1、2、...（包分片后，需要按照指定顺序拼接，比如一个包序号为1，第二个包序号为2）',
            align:"center",
            dataIndex: 'baoxuhao'
          },
          {
            title:'大序号',
            align:"center",
            dataIndex: 'daxuhao'
          },
          {
            title:'深度：数据以”,”分隔的128个短整型的点数据组成',
            align:"center",
            dataIndex: 'shendu'
          },
          {
            title:'跨距',
            align:"center",
            dataIndex: 'kuaju'
          },
          {
            title:'接收高度',
            align:"center",
            dataIndex: 'jieshoulength'
          },
          {
            title:'增益',
            align:"center",
            dataIndex: 'zengyi'
          },
          {
            title:'延时',
            align:"center",
            dataIndex: 'yanshi'
          },
          {
            title:'声时索引值',
            align:"center",
            dataIndex: 'shengshiindex'
          },
          {
            title:'首波峰索引值',
            align:"center",
            dataIndex: 'shoubofengindex'
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
            title:'传输内容: 8：分析数据临界值缺陷包，18：分析数据临界值缺陷包',
            align:"center",
            dataIndex: 'chaungshuleirong'
          },
          {
            title:'剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)',
            align:"center",
            dataIndex: 'poumianhao'
          },
          {
            title:'数据',
            align:"center",
            dataIndex: 'shuju'
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
          list: "/chaoshengbo/chaoshengboSydsj/list",
          delete: "/chaoshengbo/chaoshengboSydsj/delete",
          deleteBatch: "/chaoshengbo/chaoshengboSydsj/deleteBatch",
          exportXlsUrl: "/chaoshengbo/chaoshengboSydsj/exportXls",
          importExcelUrl: "chaoshengbo/chaoshengboSydsj/importExcel",
          
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
        fieldList.push({type:'string',value:'poumianlist',text:'剖面列表:剖面列表，以逗号分隔。 比如：1-2,1-3， 2-3'})
        fieldList.push({type:'int',value:'baoxuhao',text:'包序号：1、2、...（包分片后，需要按照指定顺序拼接，比如一个包序号为1，第二个包序号为2）'})
        fieldList.push({type:'int',value:'daxuhao',text:'大序号'})
        fieldList.push({type:'number',value:'shendu',text:'深度：数据以”,”分隔的128个短整型的点数据组成'})
        fieldList.push({type:'number',value:'kuaju',text:'跨距'})
        fieldList.push({type:'number',value:'jieshoulength',text:'接收高度'})
        fieldList.push({type:'number',value:'zengyi',text:'增益'})
        fieldList.push({type:'number',value:'yanshi',text:'延时'})
        fieldList.push({type:'int',value:'shengshiindex',text:'声时索引值'})
        fieldList.push({type:'int',value:'shoubofengindex',text:'首波峰索引值'})
        fieldList.push({type:'string',value:'liushuihao',text:'流水号'})
        fieldList.push({type:'string',value:'shizhuangno',text:'试桩编号'})
        fieldList.push({type:'int',value:'chaungshuleirong',text:'传输内容: 8：分析数据临界值缺陷包，18：分析数据临界值缺陷包'})
        fieldList.push({type:'string',value:'poumianhao',text:'剖面号1-10数据:以”,”分隔组成的数据，由剖面号，跨距，声时，波幅，主频，PSD，I17个数据组成的字符串,I1为(广东规范中的I值)'})
        fieldList.push({type:'string',value:'shuju',text:'数据'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>