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
      <a-button @click="handleExportXls('路面质检资料')" icon="download" type="primary">导出</a-button>
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

    <lmzjzl-modal @ok="modalFormOk" ref="modalForm"></lmzjzl-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import LmzjzlModal from './modules/LmzjzlModal'

  export default {
    name: 'LmzjzlList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      LmzjzlModal
    },
    data () {
      return {
        description: '路面质检资料管理页面',
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
            title:'配方号',
            align:"center",
            dataIndex: 'formulaName'
          },
          {
            title:'卸料前温度',
            align:"center",
            dataIndex: 'beforetemperature'
          },
          {
            title:'摊铺温度',
            align:"center",
            dataIndex: 'pavingtemperature'
          },
          {
            title:'初压温度',
            align:"center",
            dataIndex: 'temperatureFirst'
          },
          {
            title:'复压温度',
            align:"center",
            dataIndex: 'temperatureRepeat'
          },
          {
            title:'终压温度',
            align:"center",
            dataIndex: 'finallytemperature'
          },
          {
            title:'初压遍数',
            align:"center",
            dataIndex: 'timesFirst'
          },
          {
            title:'复压变数',
            align:"center",
            dataIndex: 'timesRepeat'
          },
          {
            title:'小于7占比',
            align:"center",
            dataIndex: 'lessthan7'
          },
          {
            title:'等于7占比',
            align:"center",
            dataIndex: 'equalto7'
          },
          {
            title:'等于8占比',
            align:"center",
            dataIndex: 'equalto8'
          },
          {
            title:'大于8占比',
            align:"center",
            dataIndex: 'greaterthan8'
          },
          {
            title:'时间',
            align:"center",
            dataIndex: 'date',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
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
          list: "/lmzjzl/lmzjzl/list",
          delete: "/lmzjzl/lmzjzl/delete",
          deleteBatch: "/lmzjzl/lmzjzl/deleteBatch",
          exportXlsUrl: "/lmzjzl/lmzjzl/exportXls",
          importExcelUrl: "lmzjzl/lmzjzl/importExcel",

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
        fieldList.push({type:'string',value:'formulaName',text:'配方号'})
        fieldList.push({type:'string',value:'beforetemperature',text:'卸料前温度'})
        fieldList.push({type:'string',value:'pavingtemperature',text:'摊铺温度'})
        fieldList.push({type:'string',value:'temperatureFirst',text:'初压温度'})
        fieldList.push({type:'string',value:'temperatureRepeat',text:'复压温度'})
        fieldList.push({type:'string',value:'finallytemperature',text:'终压温度'})
        fieldList.push({type:'string',value:'timesFirst',text:'初压遍数'})
        fieldList.push({type:'string',value:'timesRepeat',text:'复压变数'})
        fieldList.push({type:'string',value:'lessthan7',text:'小于7占比'})
        fieldList.push({type:'string',value:'equalto7',text:'等于7占比'})
        fieldList.push({type:'string',value:'equalto8',text:'等于8占比'})
        fieldList.push({type:'string',value:'greaterthan8',text:'大于8占比'})
        fieldList.push({type:'date',value:'date',text:'时间'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
