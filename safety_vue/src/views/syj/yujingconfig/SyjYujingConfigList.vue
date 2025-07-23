<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备类型">
              <j-search-select-tag placeholder="请选择设备类型" v-model="queryParam.sbType" :dictOptions="dictOption"/>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
<!--      <a-button type="primary" icon="download" @click="handleExportXls('syj_yujing_config')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
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

        <span slot="tags" slot-scope="tags, record">
          <a-tag color="red" v-if="record.isCall == '0'">报警</a-tag>
          <a-tag color="green" v-if="record.isCall == '1'">不报警</a-tag>
        </span>

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

    <syj-yujing-config-modal ref="modalForm" @ok="modalFormOk"></syj-yujing-config-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyjYujingConfigModal from './modules/SyjYujingConfigModal'

  export default {
    name: 'SyjYujingConfigList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyjYujingConfigModal
    },
    data () {
      return {
        description: 'syj_yujing_config管理页面',
        dictOption: [{
          text: '万能机',
          value: '3'
        }, {
          text: '压力机',
          value: '4'
        }, {
          text: '抗压抗折机',
          value: '12'
        }],
        selectValue:'',
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
          //   title:'uuid',
          //   align:"center",
          //   dataIndex: 'uuid'
          // },
          {
            title:'设备类型',
            align:"center",
            dataIndex: 'sbType_dictText'
          },
          {
            title:'设备编号',
            align:"center",
            dataIndex: 'shebeiNo_dictText'
          },
          {
            title:'预警人',
            align:"center",
            dataIndex: 'names'
          },
          {
            title:'预警号码',
            align:"center",
            dataIndex: 'phones'
          },
          {
            title:'是否报警',
            align:"center",
            dataIndex: 'isCall',
            scopedSlots: { customRender: 'tags' },
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
          list: "/syjyujingconfig/syjYujingConfig/list",
          delete: "/syjyujingconfig/syjYujingConfig/delete",
          deleteBatch: "/syjyujingconfig/syjYujingConfig/deleteBatch",
          exportXlsUrl: "/syjyujingconfig/syjYujingConfig/exportXls",
          importExcelUrl: "syjyujingconfig/syjYujingConfig/importExcel",

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
        fieldList.push({type:'string',value:'uuid',text:'uuid'})
        fieldList.push({type:'int',value:'sbType',text:'设备类型'})
        fieldList.push({type:'string',value:'shebeiNo',text:'设备编号'})
        fieldList.push({type:'string',value:'names',text:'预警人'})
        fieldList.push({type:'string',value:'phones',text:'预警号码'})
        fieldList.push({type:'int',value:'isCall',text:'是否报警 0:不报警，1:报警'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>