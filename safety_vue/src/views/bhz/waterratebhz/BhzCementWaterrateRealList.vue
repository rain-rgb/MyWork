<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="拌合站设备">
              <j-search-select-tag placeholder="请选择拌合站设备名称" v-model="queryParam.bhsbjno" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="含水率设备">
              <j-search-select-tag placeholder="请选择含水率设备" v-model="queryParam.shebeiid" :dictOptions="dictOption1">
              </j-search-select-tag>
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
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
      <a-button type="primary" icon="download" @click="handleExportXls('bhz_cement_waterrate_real')">导出</a-button>
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
          <a @click="handleDetail(record)">详情</a>
          <!-- <a @click="handleEdit(record)">编辑</a> -->

          <!-- <a-divider type="vertical" /> -->
          <!-- <a-dropdown>
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
          </a-dropdown> -->
        </span>

      </a-table>
    </div>

    <bhz-cement-waterrate-real-modal ref="modalForm" @ok="modalFormOk"></bhz-cement-waterrate-real-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BhzCementWaterrateRealModal from './modules/BhzCementWaterrateRealModal'
  import Vue from "vue";
  import {usershebeiList} from "@api/api";

  export default {
    name: 'BhzCementWaterrateRealList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BhzCementWaterrateRealModal
    },
    data () {
      return {
        dictOption: [],
        dictOption1: [],
        description: 'bhz_cement_waterrate_real管理页面',
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
            title:'含水率设备',
            align:"center",
            dataIndex: 'shebeiid_dictText'
          },
          {
            title:'温度(°C)',
            align:"center",
            dataIndex: 'temperature'
          },
          // {
          //   title:'含水率(%)',
          //   align:"center",
          //   dataIndex: 'water'
          // },
          // {
          //   title:'湿度(%)',
          //   align:"center",
          //   dataIndex: 'humidity'
          // },
          {
            title:'材料名称',
            align:"center",
            dataIndex: 'cailiaoname'
          },
          {
            title:'拌合站设备',
            align:"center",
            dataIndex: 'bhsbjno_dictText'
          },
          // {
          //   title:'料仓id',
          //   align:"center",
          //   dataIndex: 'liaocangid'
          // },
          {
            title:'料仓名称',
            align:"center",
            dataIndex: 'lcname'
          },
          {
            title:'平均含水率(%)',
            align:"center",
            dataIndex: 'averate'
          },
          // {
          //   title:'平均湿度(%)',
          //   align:"center",
          //   dataIndex: 'avehum'
          // },
          {
            title:'上传时间',
            align:"center",
            dataIndex: 'uploadTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text:text)
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
          list: "/bhzcementwaterratereal/bhzCementWaterrateReal/list",
          delete: "/bhzcementwaterratereal/bhzCementWaterrateReal/delete",
          deleteBatch: "/bhzcementwaterratereal/bhzCementWaterrateReal/deleteBatch",
          exportXlsUrl: "/bhzcementwaterratereal/bhzCementWaterrateReal/exportXls",
          importExcelUrl: "bhzcementwaterratereal/bhzCementWaterrateReal/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.shebeiList();
      this.shebeiList1()
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      shebeiList: function () {
        var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
        usershebeiList({
          sys_depart_orgcode: sys_depart_orgcode,
          sbtypes: '0'
        }).then(res => {
          this.dictOption = []
          let result = res.result
          result.forEach(re => {
            this.dictOption.push({ text: re.sbname, value: re.sbjno })
          })
          //console.log(res)
        })
      },
      shebeiList1: function () {
        var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
        usershebeiList({
          sys_depart_orgcode: sys_depart_orgcode,
          sbtypes: '66'
        }).then(res => {
          this.dictOption1 = []
          let result = res.result
          result.forEach(re => {
            this.dictOption1.push({ text: re.sbname, value: re.sbjno })
          })
          //console.log(res)
        })
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'date',value:'uploadTime',text:'uploadTime'})
        fieldList.push({type:'string',value:'temperature',text:'温度'})
        fieldList.push({type:'string',value:'water',text:'含水率'})
        fieldList.push({type:'string',value:'humidity',text:'湿度'})
        fieldList.push({type:'string',value:'shebeiid',text:'设备id'})
        fieldList.push({type:'string',value:'cailiaoname',text:'材料名称'})
        fieldList.push({type:'string',value:'bhsbjno',text:'拌合站设备'})
        fieldList.push({type:'string',value:'liaocangid',text:'料仓id'})
        fieldList.push({type:'string',value:'lcname',text:'料仓名称'})
        fieldList.push({type:'string',value:'averate',text:'平均含水率'})
        fieldList.push({type:'string',value:'avehum',text:'平均湿度'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
