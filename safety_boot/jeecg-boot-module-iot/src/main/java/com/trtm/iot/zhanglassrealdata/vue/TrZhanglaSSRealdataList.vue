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
      <a-button type="primary" icon="download" @click="handleExportXls('tr_zhangla_s_s_realdata')">导出</a-button>
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

    <tr-zhangla-s-s-realdata-modal ref="modalForm" @ok="modalFormOk"></tr-zhangla-s-s-realdata-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import TrZhanglaSSRealdataModal from './modules/TrZhanglaSSRealdataModal'

  export default {
    name: 'TrZhanglaSSRealdataList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      TrZhanglaSSRealdataModal
    },
    data () {
      return {
        description: 'tr_zhangla_s_s_realdata管理页面',
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
            title:'唯一码',
            align:"center",
            dataIndex: 'ssid'
          },
          {
            title:'孔号，和主表gsbh相同',
            align:"center",
            dataIndex: 'holeid'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'shebeibianhao'
          },
          {
            title:'孔道名称',
            align:"center",
            dataIndex: 'holename'
          },
          {
            title:'梁名称',
            align:"center",
            dataIndex: 'sname'
          },
          {
            title:'记录时间',
            align:"center",
            dataIndex: 'jlsj'
          },
          {
            title:'张拉次数',
            align:"center",
            dataIndex: 'zlcs'
          },
          {
            title:'状态1',
            align:"center",
            dataIndex: 'zt1'
          },
          {
            title:'张拉力1(KN)',
            align:"center",
            dataIndex: 'zll1'
          },
          {
            title:'油压1（Mpa）',
            align:"center",
            dataIndex: 'yy1'
          },
          {
            title:'顶行程1（mm）',
            align:"center",
            dataIndex: 'dxc1'
          },
          {
            title:'伸长量1（mm）',
            align:"center",
            dataIndex: 'scl1'
          },
          {
            title:'状态2',
            align:"center",
            dataIndex: 'zt2'
          },
          {
            title:'张拉力2（KN）',
            align:"center",
            dataIndex: 'zll2'
          },
          {
            title:'油压2（MPa）',
            align:"center",
            dataIndex: 'yy2'
          },
          {
            title:'顶行程2（mm）',
            align:"center",
            dataIndex: 'dxc2'
          },
          {
            title:'伸长量2（mm）',
            align:"center",
            dataIndex: 'scl2'
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
          list: "/zhanglassrealdata/trZhanglaSSRealdata/list",
          delete: "/zhanglassrealdata/trZhanglaSSRealdata/delete",
          deleteBatch: "/zhanglassrealdata/trZhanglaSSRealdata/deleteBatch",
          exportXlsUrl: "/zhanglassrealdata/trZhanglaSSRealdata/exportXls",
          importExcelUrl: "zhanglassrealdata/trZhanglaSSRealdata/importExcel",
          
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
        fieldList.push({type:'string',value:'ssid',text:'唯一码'})
        fieldList.push({type:'string',value:'holeid',text:'孔号，和主表gsbh相同'})
        fieldList.push({type:'string',value:'shebeibianhao',text:'设备编号'})
        fieldList.push({type:'string',value:'holename',text:'孔道名称'})
        fieldList.push({type:'string',value:'sname',text:'梁名称'})
        fieldList.push({type:'string',value:'jlsj',text:'记录时间'})
        fieldList.push({type:'string',value:'zlcs',text:'张拉次数'})
        fieldList.push({type:'string',value:'zt1',text:'状态1'})
        fieldList.push({type:'string',value:'zll1',text:'张拉力1(KN)'})
        fieldList.push({type:'string',value:'yy1',text:'油压1（Mpa）'})
        fieldList.push({type:'string',value:'dxc1',text:'顶行程1（mm）'})
        fieldList.push({type:'string',value:'scl1',text:'伸长量1（mm）'})
        fieldList.push({type:'string',value:'zt2',text:'状态2'})
        fieldList.push({type:'string',value:'zll2',text:'张拉力2（KN）'})
        fieldList.push({type:'string',value:'yy2',text:'油压2（MPa）'})
        fieldList.push({type:'string',value:'dxc2',text:'顶行程2（mm）'})
        fieldList.push({type:'string',value:'scl2',text:'伸长量2（mm）'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>