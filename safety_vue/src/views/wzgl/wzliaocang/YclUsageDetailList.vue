<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="工程部位">
              <a-input placeholder="请输入工程部位" v-model="queryParam.projectPart"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料类别">
              <j-dict-select-tag placeholder="请选择材料类别" v-model="queryParam.nodetype"
                                 dictCode="nodeType"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.dosingTime_begin" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.dosingTime_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
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
    <!-- <div class="table-operator"> -->
    <!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
    <!--      <a-button type="primary" icon="download" @click="handleExportXls('ycl_usage_detail')">导出</a-button>-->
    <!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
    <!--        <a-button type="primary" icon="import">导入</a-button>-->
    <!--      </a-upload>-->
    <!-- 高级查询区域 -->
    <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
    <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
    <!--        <a-menu slot="overlay">-->
    <!--          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
    <!--        </a-menu>-->
    <!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
    <!--      </a-dropdown>-->
    <!-- </div> -->

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
        <template slot="inspectionLotNumber" slot-scope="inspectionLotNumber, record">
          <a @click="inspectionDetail(record)">{{ inspectionLotNumber }}</a>
        </template>

        <template slot="projectPart" slot-scope="projectPart, record">
          <a v-if="projectPart.indexOf('|')==-1"> {{record.description}}</a>
          <a else>{{projectPart}}</a>
        </template>

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
              <!--              <a-menu-item>-->
              <!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
              <!--                  <a>删除</a>-->
              <!--                </a-popconfirm>-->
              <!--              </a-menu-item>-->
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <ycl-usage-detail-modal ref="modalForm" @ok="modalFormOk"></ycl-usage-detail-modal>
    <wztaizhang-modal ref="wztaizhangForm"></wztaizhang-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import YclUsageDetailModal from './modules/YclUsageDetailModal'
import { getAction } from '@api/manage'
import WztaizhangModal from '@/views/zlgl/wztaizhang/wztaizhangnew/modules/WztaizhangModal'


export default {
  name: 'YclUsageDetailList',
  mixins:[JeecgListMixin, mixinDevice],
  components: {
    YclUsageDetailModal,
    WztaizhangModal
  },
  data () {
    return {
      description: 'ycl_usage_detail管理页面',
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
          title:'材料名称',
          align:"center",
          dataIndex: 'storageName'
        },
        {
          title:'使用时间',
          align:"center",
          dataIndex: 'dosingTime',
          customRender:function (text) {
            return !text?"":(text.length>10?text.substr(0,19):text)
          }
        },
        {
          title:'工程部位',
          align:"center",
          dataIndex: 'projectPart',
          scopedSlots: { customRender: 'projectPart' },
        },
        {
          title:'使用量(kg)',
          align:"center",
          dataIndex: 'uses'
        },
        {
          title:'检验批编号',
          align:"center",
          dataIndex: 'inspectionLotNumber',
          scopedSlots: { customRender: 'inspectionLotNumber' },
        },
        {
          title:'配料单号',
          align:"center",
          dataIndex: 'dosingOrderNumber'
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
        list: "/yclud/yclUsageDetail/list",
        delete: "/yclud/yclUsageDetail/delete",
        deleteBatch: "/yclud/yclUsageDetail/deleteBatch",
        exportXlsUrl: "/yclud/yclUsageDetail/exportXls",
        importExcelUrl: "yclud/yclUsageDetail/importExcel",

      },
      dictOptions:{},
      // superFieldList:[],
    }
  },
  created() {
    // this.getSuperFieldList();
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    inspectionDetail({ inspectionLotNumber }) {
      let params = {
        pici: inspectionLotNumber
      }
      getAction('/wztaizhang/wztaizhang/lists', params).then((res) => {
        if (res.success) {
          let record = res.result.records[0] || {}
          this.$refs.wztaizhangForm.edit(record)
          this.$refs.wztaizhangForm.title = '详情'
          this.$refs.wztaizhangForm.disableSubmit = true
        } else {
          this.$message.warning(res.message)
        }
      })
    },
    initDictConfig(){
    },
    // getSuperFieldList(){
    //   let fieldList=[];
    //   fieldList.push({type:'date',value:'dosingTime',text:'配料时间（领料时间）'})
    //   fieldList.push({type:'string',value:'dosingOrderNumber',text:'配料单号'})
    //   fieldList.push({type:'string',value:'projectPart',text:'工程部位'})
    //   fieldList.push({type:'int',value:'usage',text:'使用量'})
    //   fieldList.push({type:'string',value:'storageId',text:'料仓id'})
    //   fieldList.push({type:'string',value:'inspectionLotNumber',text:'检验批编号'})
    //   this.superFieldList = fieldList
    // }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>