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
      <a-button type="primary" icon="download" @click="export1">打印</a-button>
<!--      <a-button type="primary" icon="download" @click="handleExportXls('岗前培训计划表')">导出</a-button>-->
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

    <gqpx-training-plan-modal ref="modalForm" @ok="modalFormOk"></gqpx-training-plan-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import GqpxTrainingPlanModal from './modules/GqpxTrainingPlanModal'

  export default {
    name: 'GqpxTrainingPlanList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      GqpxTrainingPlanModal
    },
    data () {
      return {
        description: '岗前培训计划表管理页面',
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
          //   title:'唯一id',
          //   align:"center",
          //   dataIndex: 'guid'
          // },
          {
            title:'培训计划名称',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'培训类型',
            align:"center",
            dataIndex: 'type'
          },
          {
            title:'开始时间',
            align:"center",
            dataIndex: 'startTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'结束时间',
            align:"center",
            dataIndex: 'endTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'培训地点',
            align:"center",
            dataIndex: 'location'
          },
          {
            title:'参与工种',
            align:"center",
            dataIndex: 'worktype'
          },
          {
            title:'描述',
            align:"center",
            dataIndex: 'describe'
          },
          {
            title:'拟参与人员',
            align:"center",
            dataIndex: 'staff'
          },
          {
            title:'审核状态',
            align:"center",
            dataIndex: 'state'
          },
          {
            title:'监理审核意见',
            align:"center",
            dataIndex: 'supervisorApproval'
          },
          {
            title:'监理审核时间',
            align:"center",
            dataIndex: 'supervisorHandleTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'指挥部审核意见',
            align:"center",
            dataIndex: 'headquartersApproval'
          },
          {
            title:'指挥部审核时间',
            align:"center",
            dataIndex: 'headquartersHandleTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'交警审核意见',
            align:"center",
            dataIndex: 'trafficPoliceApproval'
          },
          {
            title:'交警审核时间',
            align:"center",
            dataIndex: 'trafficPoliceHandleTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'完成状态',
            align:"center",
            dataIndex: 'completeState'
          },
          // {
          //   title:'现场图片',
          //   align:"center",
          //   dataIndex: 'pictureurl1'
          // },
          // {
          //   title:'现场图片',
          //   align:"center",
          //   dataIndex: 'pictureurl2'
          // },
          // {
          //   title:'现场图片',
          //   align:"center",
          //   dataIndex: 'pictureurl3'
          // },
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
          list: "/gqpx_training_plan/gqpxTrainingPlan/list",
          delete: "/gqpx_training_plan/gqpxTrainingPlan/delete",
          deleteBatch: "/gqpx_training_plan/gqpxTrainingPlan/deleteBatch",
          exportXlsUrl: "/gqpx_training_plan/gqpxTrainingPlan/exportXls",
          importExcelUrl: "gqpx_training_plan/gqpxTrainingPlan/importExcel",
          exportUrl: 'jmreport/view/1056836292348325888',
          
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
      export1: function () {
        //打印功能需要先去报表设计页面设计打印格式
        if (this.selectedRowKeys.length !== 1) {
          let param = this.getQueryParams()
          console.log(param, '打印信息')
          this.$message.error('请选择一条张拉数据进行打印')
        } else if (this.selectedRowKeys.length == 1) {
          //?paramsStr=${paramsStr}
          let param = this.getQueryParams()
          param['selections'] = this.selectedRowKeys.join(',')
          console.log(param, '打印信息')
          let url = `${window._CONFIG['domianURL']}/${this.url.exportUrl}?id=${param.selections}&token=${this.token}`
          window.open(url)
        }
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'guid',text:'唯一id'})
        fieldList.push({type:'string',value:'name',text:'培训计划名称'})
        fieldList.push({type:'string',value:'type',text:'培训类型'})
        fieldList.push({type:'date',value:'startTime',text:'开始时间'})
        fieldList.push({type:'date',value:'endTime',text:'结束时间'})
        fieldList.push({type:'string',value:'location',text:'培训地点'})
        fieldList.push({type:'string',value:'worktype',text:'参与工种'})
        fieldList.push({type:'string',value:'describe',text:'描述'})
        fieldList.push({type:'string',value:'staff',text:'拟参与人员'})
        fieldList.push({type:'int',value:'state',text:'审核状态(0待审核；10监理同意；15监理已驳回；20指挥部同意；25指挥部已驳回；30交警同意；35：交警已驳回)'})
        fieldList.push({type:'string',value:'supervisorApproval',text:'监理审核意见'})
        fieldList.push({type:'date',value:'supervisorHandleTime',text:'监理审核时间'})
        fieldList.push({type:'string',value:'headquartersApproval',text:'指挥部审核意见'})
        fieldList.push({type:'date',value:'headquartersHandleTime',text:'指挥部审核时间'})
        fieldList.push({type:'string',value:'trafficPoliceApproval',text:'交警审核意见'})
        fieldList.push({type:'date',value:'trafficPoliceHandleTime',text:'交警审核时间'})
        fieldList.push({type:'int',value:'completeState',text:'完成状态（10监理批准；20指挥部批准；30运营公司批准；40交警批准）'})
        fieldList.push({type:'string',value:'pictureurl1',text:'现场图片'})
        fieldList.push({type:'string',value:'pictureurl2',text:'现场图片'})
        fieldList.push({type:'string',value:'pictureurl3',text:'现场图片'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>