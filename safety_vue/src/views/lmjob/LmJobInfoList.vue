<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
<!--    <div class="table-page-search-wrapper">-->
<!--      <a-form layout="inline" @keyup.enter.native="searchQuery">-->
<!--        <a-row :gutter="24">-->
<!--        </a-row>-->
<!--      </a-form>-->
<!--    </div>-->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="桩号里程">
              <a-input placeholder="请输入桩号里程" v-model="queryParam.lmname"></a-input>
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
<!--      <a-button type="primary" icon="download" @click="handleExportXls('lm_job_info')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
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

         <a @click="pourDetail(record)">文件上传</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="handleEdit(record)">编辑</a>
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
    <LmJobFilesModal ref="files" @ok="modalFormOk" ></LmJobFilesModal>
    <lm-job-info-modal ref="modalForm" @ok="modalFormOk"></lm-job-info-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import LmJobFilesForm1 from './modules/LmJobFilesForm1'
  import LmJobFilesModal from './modules/LmJobFilesModal'
  import LmJobInfoModal from './modules/LmJobInfoModal'
  import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
  export default {
    name: 'LmJobInfoList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      LmJobFilesModal,
      LmJobInfoModal,
      LmJobFilesForm1,JSelectDqProjName
    },
    data () {
      return {
        description: 'lm_job_info管理页面',
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
            title: '所属部门',
            align: 'center',
            dataIndex: 'sysOrgCode_dictText',
          },
          {
            title:'桩号里程',
            align:"center",
            dataIndex: 'zhlc'
          },
          {
            title:'路面结构类型',
            align:"center",
            dataIndex: 'wbstype'
          },

          {
            title:'底基层方量m³',
            align:"center",
            dataIndex: 'djcInfo1'
          },
          {
            title:'基层方量m³',
            align:"center",
            dataIndex: 'jcInfo1'
          },
          {
            title:'下面层方量m³',
            align:"center",
            dataIndex: 'xmcInfo1'
          },
          {
            title:'中面层方量m³',
            align:"center",
            dataIndex: 'zmcInfo1'
          },
          {
            title:'上面层方量m³',
            align:"center",
            dataIndex: 'smcInfo1'
          },
          {
            title:'文件数量',
            align:"center",
            dataIndex: 'files'
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
          list: "/lmjob/lmJobInfo/list",
          delete: "/lmjob/lmJobInfo/delete",
          deleteBatch: "/lmjob/lmJobInfo/deleteBatch",
          exportXlsUrl: "/lmjob/lmJobInfo/exportXls",
          importExcelUrl: "lmjob/lmJobInfo/importExcel",
          
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

      pourDetail(record){
        // this.$refs.files.model = { infoid: record.id}
        this.$refs.files.add({ infoid: record.id, type1:'lmjd1',wbsOrgCode:record.wbsOrgCode});
        this.$refs.files.title = "上传文件";
        this.$refs.files.disableSubmit = false;
        this.$refs.files.visible = true
      },

      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'zhlc',text:'桩号里程'})
        fieldList.push({type:'string',value:'wbstype',text:'路面结构类型'})
        fieldList.push({type:'string',value:'wbsOrgCode',text:'分部分项code'})
        fieldList.push({type:'string',value:'djcInfo1',text:'底基层混合料总方量'})
        fieldList.push({type:'string',value:'jcInfo1',text:'基层混合料总方量'})
        fieldList.push({type:'string',value:'djcInfo2',text:'底基层供应商'})
        fieldList.push({type:'string',value:'jcInfo2',text:'基层供应商'})
        fieldList.push({type:'string',value:'xmcInfo1',text:'下面层混合料总方量'})
        fieldList.push({type:'string',value:'xmcInfo2',text:'下面层供应商'})
        fieldList.push({type:'string',value:'zmcInfo1',text:'中面层混合料总方量'})
        fieldList.push({type:'string',value:'zmcInfo2',text:'中面层供应商'})
        fieldList.push({type:'string',value:'smcInfo1',text:'上面层混合料总方量'})
        fieldList.push({type:'string',value:'smcInfo2',text:'上面层供应商'})
        fieldList.push({type:'int',value:'files',text:'文件数量'})
        fieldList.push({type:'string',value:'guid',text:'guid'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>