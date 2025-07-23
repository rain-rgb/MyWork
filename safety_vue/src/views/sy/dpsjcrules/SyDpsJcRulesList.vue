<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="规程编号">
              <a-input placeholder="请输入规程编号" v-model="queryParam.ruleno"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="规程名称">
              <a-input placeholder="请输入规程名称" v-model="queryParam.rulename"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="删除状态">
              <j-dict-select-tag placeholder="请选择删除状态" v-model="queryParam.ruleisdel"
                                 dictCode="ruleisdel">
              </j-dict-select-tag>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('sy_dps_jc_rules')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
<!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
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

        <span slot="tags" slot-scope="tags, record">
          <a-tag color="green" v-if="record.ruleisdel == '0'">正常</a-tag>
          <a-tag color="red" v-if="record.ruleisdel == '1'">已删除</a-tag>
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

    <sy-dps-jc-rules-modal ref="modalForm" @ok="modalFormOk"></sy-dps-jc-rules-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyDpsJcRulesModal from './modules/SyDpsJcRulesModal'

  export default {
    name: 'SyDpsJcRulesList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyDpsJcRulesModal
    },
    data () {
      return {
        dictOption: [],
        description: 'sy_dps_jc_rules管理页面',
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
            title:'规程编号',
            align:"center",
            dataIndex: 'ruleno'
          },
          {
            title:'规程名称',
            align:"center",
            dataIndex: 'rulename'
          },
          {
            title:'规程依据类型',
            align:"center",
            dataIndex: 'ruletype'
          },
          {
            title:'规程单位',
            align:"center",
            dataIndex: 'ruleunit'
          },
          {
            title:'规程实施时间',
            align:"center",
            dataIndex: 'rulebegindate'
          },
          // {
          //   title:'ruleisshow',
          //   align:"center",
          //   dataIndex: 'ruleisshow'
          // },
          // {
          //   title:'ruleremark',
          //   align:"center",
          //   dataIndex: 'ruleremark'
          // },
          {
            title:'删除状态',
            align:"center",
            dataIndex: 'ruleisdel_dictText',
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
          list: "/sydpsjcrules/syDpsJcRules/list",
          delete: "/sydpsjcrules/syDpsJcRules/delete",
          deleteBatch: "/sydpsjcrules/syDpsJcRules/deleteBatch",
          exportXlsUrl: "/sydpsjcrules/syDpsJcRules/exportXls",
          importExcelUrl: "sydpsjcrules/syDpsJcRules/importExcel",

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
        fieldList.push({type:'string',value:'titcode',text:'titcode'})
        fieldList.push({type:'string',value:'ruleno',text:'ruleno'})
        fieldList.push({type:'string',value:'rulename',text:'rulename'})
        fieldList.push({type:'string',value:'ruletype',text:'ruletype'})
        fieldList.push({type:'string',value:'ruleunit',text:'ruleunit'})
        fieldList.push({type:'string',value:'rulebegindate',text:'rulebegindate'})
        fieldList.push({type:'int',value:'ruleisshow',text:'ruleisshow'})
        fieldList.push({type:'string',value:'ruleremark',text:'ruleremark'})
        fieldList.push({type:'int',value:'ruleisdel',text:'ruleisdel'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>