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
      <a-button type="primary" icon="download" @click="handleExportXls('沥青级配范围配置表')">导出</a-button>
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
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >

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

    <bhz-lq-jipei-fanwei-modal ref="modalForm" @ok="modalFormOk"></bhz-lq-jipei-fanwei-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BhzLqJipeiFanweiModal from './modules/BhzLqJipeiFanweiModal'

  export default {
    name: 'BhzLqJipeiFanweiList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BhzLqJipeiFanweiModal
    },
    data () {
      return {
        description: '沥青级配范围配置表管理页面',
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
          // {
          //   title:'设备编号',
          //   align:"center",
          //   dataIndex: 'sbjno'
          // },
          {
            title:'级配标准',
            align:"center",
            dataIndex: 'poureLocation'
          },
          {
            title:'0.075筛孔下限',
            align:"center",
            dataIndex: 'sk0075xiaxian'
          },
          {
            title:'0.075筛孔中限',
            align:"center",
            dataIndex: 'sk0075zhongxian'
          },
          {
            title:'0.075筛孔上限',
            align:"center",
            dataIndex: 'sk0075shangxian'
          },
          {
            title:'0.15筛孔下限',
            align:"center",
            dataIndex: 'sk015xiaxian'
          },
          {
            title:'0.15筛孔中限',
            align:"center",
            dataIndex: 'sk015zhongxian'
          },
          {
            title:'0.15筛孔上限',
            align:"center",
            dataIndex: 'sk015shangxian'
          },
          {
            title:'0.3筛孔下限',
            align:"center",
            dataIndex: 'sk03xiaxian'
          },
          {
            title:'0.3筛孔中限',
            align:"center",
            dataIndex: 'sk03zhongxian'
          },
          {
            title:'0.3筛孔上限',
            align:"center",
            dataIndex: 'sk03shangxian'
          },
          {
            title:'0.6筛孔下限',
            align:"center",
            dataIndex: 'sk06xiaxian'
          },
          {
            title:'0.6筛孔中限',
            align:"center",
            dataIndex: 'sk06zhongxian'
          },
          {
            title:'0.6筛孔上限',
            align:"center",
            dataIndex: 'sk06shangxian'
          },
          {
            title:'1.18筛孔下限',
            align:"center",
            dataIndex: 'sk118xiaxian'
          },
          {
            title:'1.18筛孔中限',
            align:"center",
            dataIndex: 'sk118zhongxian'
          },
          {
            title:'1.18筛孔上限',
            align:"center",
            dataIndex: 'sk118shangxian'
          },
          {
            title:'2.36筛孔下限',
            align:"center",
            dataIndex: 'sk236xiaxian'
          },
          {
            title:'2.36筛孔中限',
            align:"center",
            dataIndex: 'sk236zhongxian'
          },
          {
            title:'2.36筛孔上限',
            align:"center",
            dataIndex: 'sk236shangxian'
          },
          {
            title:'4.75筛孔下限',
            align:"center",
            dataIndex: 'sk475xiaxian'
          },
          {
            title:'4.75筛孔中限',
            align:"center",
            dataIndex: 'sk475zhongxian'
          },
          {
            title:'4.75筛孔上限',
            align:"center",
            dataIndex: 'sk475shangxian'
          },
          {
            title:'9.5筛孔下限',
            align:"center",
            dataIndex: 'sk95xiaxian'
          },
          {
            title:'9.5筛孔中限',
            align:"center",
            dataIndex: 'sk95zhongxian'
          },
          {
            title:'9.5筛孔上限',
            align:"center",
            dataIndex: 'sk95shangxian'
          },
          {
            title:'13.2筛孔下限',
            align:"center",
            dataIndex: 'sk132xiaxian'
          },
          {
            title:'13.2筛孔中限',
            align:"center",
            dataIndex: 'sk132zhongxian'
          },
          {
            title:'13.2筛孔上限',
            align:"center",
            dataIndex: 'sk132shangxian'
          },
          {
            title:'16筛孔下限',
            align:"center",
            dataIndex: 'sk16xiaxian'
          },
          {
            title:'16筛孔中限',
            align:"center",
            dataIndex: 'sk16zhongxian'
          },
          {
            title:'16筛孔上限',
            align:"center",
            dataIndex: 'sk16shangxian'
          },
          {
            title:'19筛孔下限',
            align:"center",
            dataIndex: 'sk19xiaxian'
          },
          {
            title:'19筛孔中限',
            align:"center",
            dataIndex: 'sk19zhongxian'
          },
          {
            title:'19筛孔上限',
            align:"center",
            dataIndex: 'sk19shangxian'
          },
          {
            title:'26.5筛孔下限',
            align:"center",
            dataIndex: 'sk265xiaxian'
          },
          {
            title:'26.5筛孔中限',
            align:"center",
            dataIndex: 'sk265zhongxian'
          },
          {
            title:'26.5筛孔上限',
            align:"center",
            dataIndex: 'sk265shangxian'
          },
          {
            title:'31.5筛孔下限',
            align:"center",
            dataIndex: 'sk315xiaxian'
          },
          {
            title:'31.5筛孔中限',
            align:"center",
            dataIndex: 'sk315zhongxian'
          },
          {
            title:'31.5筛孔上限',
            align:"center",
            dataIndex: 'sk315shangxian'
          },
          {
            title:'37.5筛孔下限',
            align:"center",
            dataIndex: 'sk375xiaxian'
          },
          {
            title:'37.5筛孔中限',
            align:"center",
            dataIndex: 'sk375zhongxian'
          },
          {
            title:'37.5筛孔上限',
            align:"center",
            dataIndex: 'sk375shangxian'
          },
          {
            title:'53筛孔下限',
            align:"center",
            dataIndex: 'sk53xiaxian'
          },
          {
            title:'53筛孔中限',
            align:"center",
            dataIndex: 'sk53zhongxian'
          },
          {
            title:'53筛孔上限',
            align:"center",
            dataIndex: 'sk53shangxian'
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
          list: "/bhzlqjipeifanwei/bhzLqJipeiFanwei/list",
          delete: "/bhzlqjipeifanwei/bhzLqJipeiFanwei/delete",
          deleteBatch: "/bhzlqjipeifanwei/bhzLqJipeiFanwei/deleteBatch",
          exportXlsUrl: "/bhzlqjipeifanwei/bhzLqJipeiFanwei/exportXls",
          importExcelUrl: "bhzlqjipeifanwei/bhzLqJipeiFanwei/importExcel",

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
        fieldList.push({type:'string',value:'sbjno',text:'设备编号',dictCode:''})
        fieldList.push({type:'string',value:'poureLocation',text:'级配标准',dictCode:''})
        fieldList.push({type:'string',value:'sk0075xiaxian',text:'0.075筛孔下限',dictCode:''})
        fieldList.push({type:'string',value:'sk0075zhongxian',text:'0.075筛孔中限',dictCode:''})
        fieldList.push({type:'string',value:'sk0075shangxian',text:'0.075筛孔上限',dictCode:''})
        fieldList.push({type:'string',value:'sk015xiaxian',text:'0.15筛孔下限',dictCode:''})
        fieldList.push({type:'string',value:'sk015zhongxian',text:'0.15筛孔中限',dictCode:''})
        fieldList.push({type:'string',value:'sk015shangxian',text:'0.15筛孔上限',dictCode:''})
        fieldList.push({type:'string',value:'sk03xiaxian',text:'0.3筛孔下限',dictCode:''})
        fieldList.push({type:'string',value:'sk03zhongxian',text:'0.3筛孔中限',dictCode:''})
        fieldList.push({type:'string',value:'sk03shangxian',text:'0.3筛孔上限',dictCode:''})
        fieldList.push({type:'string',value:'sk06xiaxian',text:'0.6筛孔下限',dictCode:''})
        fieldList.push({type:'string',value:'sk06zhongxian',text:'0.6筛孔中限',dictCode:''})
        fieldList.push({type:'string',value:'sk06shangxian',text:'0.6筛孔上限',dictCode:''})
        fieldList.push({type:'string',value:'sk118xiaxian',text:'1.18筛孔下限',dictCode:''})
        fieldList.push({type:'string',value:'sk118zhongxian',text:'1.18筛孔中限',dictCode:''})
        fieldList.push({type:'string',value:'sk118shangxian',text:'1.18筛孔上限',dictCode:''})
        fieldList.push({type:'string',value:'sk236xiaxian',text:'2.36筛孔下限',dictCode:''})
        fieldList.push({type:'string',value:'sk236zhongxian',text:'2.36筛孔中限',dictCode:''})
        fieldList.push({type:'string',value:'sk236shangxian',text:'2.36筛孔上限',dictCode:''})
        fieldList.push({type:'string',value:'sk475xiaxian',text:'4.75筛孔下限',dictCode:''})
        fieldList.push({type:'string',value:'sk475zhongxian',text:'4.75筛孔中限',dictCode:''})
        fieldList.push({type:'string',value:'sk475shangxian',text:'4.75筛孔上限',dictCode:''})
        fieldList.push({type:'string',value:'sk95xiaxian',text:'9.5筛孔下限',dictCode:''})
        fieldList.push({type:'string',value:'sk95zhongxian',text:'9.5筛孔中限',dictCode:''})
        fieldList.push({type:'string',value:'sk95shangxian',text:'9.5筛孔上限',dictCode:''})
        fieldList.push({type:'string',value:'sk132xiaxian',text:'13.2筛孔下限',dictCode:''})
        fieldList.push({type:'string',value:'sk132zhongxian',text:'13.2筛孔中限',dictCode:''})
        fieldList.push({type:'string',value:'sk132shangxian',text:'13.2筛孔上限',dictCode:''})
        fieldList.push({type:'string',value:'sk16xiaxian',text:'16筛孔下限',dictCode:''})
        fieldList.push({type:'string',value:'sk16zhongxian',text:'16筛孔中限',dictCode:''})
        fieldList.push({type:'string',value:'sk16shangxian',text:'16筛孔上限',dictCode:''})
        fieldList.push({type:'string',value:'sk19xiaxian',text:'19筛孔下限',dictCode:''})
        fieldList.push({type:'string',value:'sk19zhongxian',text:'19筛孔中限',dictCode:''})
        fieldList.push({type:'string',value:'sk19shangxian',text:'19筛孔上限',dictCode:''})
        fieldList.push({type:'string',value:'sk265xiaxian',text:'26.5筛孔下限',dictCode:''})
        fieldList.push({type:'string',value:'sk265zhongxian',text:'26.5筛孔中限',dictCode:''})
        fieldList.push({type:'string',value:'sk265shangxian',text:'26.5筛孔上限',dictCode:''})
        fieldList.push({type:'string',value:'sk315xiaxian',text:'31.5筛孔下限',dictCode:''})
        fieldList.push({type:'string',value:'sk315zhongxian',text:'31.5筛孔中限',dictCode:''})
        fieldList.push({type:'string',value:'sk315shangxian',text:'31.5筛孔上限',dictCode:''})
        fieldList.push({type:'string',value:'sk375xiaxian',text:'37.5筛孔下限',dictCode:''})
        fieldList.push({type:'string',value:'sk375zhongxian',text:'37.5筛孔中限',dictCode:''})
        fieldList.push({type:'string',value:'sk375shangxian',text:'37.5筛孔上限',dictCode:''})
        fieldList.push({type:'string',value:'sk53xiaxian',text:'53筛孔下限',dictCode:''})
        fieldList.push({type:'string',value:'sk53zhongxian',text:'53筛孔中限',dictCode:''})
        fieldList.push({type:'string',value:'sk53shangxian',text:'53筛孔上限',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>