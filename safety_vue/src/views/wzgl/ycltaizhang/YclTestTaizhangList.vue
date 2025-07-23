<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料类别">
              <j-dict-select-tag placeholder="请选择材料类别" v-model="queryParam.nodetype"
                                 dictCode="nodeType"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="批次">
              <a-input placeholder="请输入批次" v-model="queryParam.pici"></a-input>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.jinchangtime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.jinchangtime_end" :showTime="true"
                      dateFormat="YYYY-MM-DD"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="自检状态">
              <j-dict-select-tag placeholder="请选择自检结果" v-model="queryParam.reslut"
                                 dictCode="syreslut">
              </j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="抽检">
              <j-dict-select-tag placeholder="请选择抽检结果" v-model="queryParam.cjreslut"
                                 dictCode="syreslut">
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
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
<!--      <a-button type="primary" icon="download" @click="handleExportXls('ycl_test_taizhang')">导出</a-button>-->
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

         <span slot="tags1" slot-scope="tags, record">
          <a-tag color="green" v-if="record.cjreslut == '合格'">合格</a-tag>
           <a-tag color="orange" v-if="record.cjreslut == '不合格'" >不合格</a-tag>
        </span>
        <span slot="tags2" slot-scope="tags, record">
           <a-tag color="green" v-if="record.reslut == '合格'">合格</a-tag>
           <a-tag color="orange" v-if="record.reslut == '不合格'" >不合格</a-tag>
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
           <a @click="handlePreview(record)">预览</a>

<!--          <a-divider type="vertical" />-->
<!--          <a-dropdown>-->
<!--            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>-->
<!--            <a-menu slot="overlay">-->
<!--              <a-menu-item>-->
<!--                <a @click="handleDetail(record)">详情</a>-->
<!--              </a-menu-item>-->
<!--              <a-menu-item>-->
<!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
<!--                  <a>删除</a>-->
<!--                </a-popconfirm>-->
<!--              </a-menu-item>-->
<!--            </a-menu>-->
<!--          </a-dropdown>-->
        </span>

      </a-table>
    </div>

    <ycl-test-taizhang-modal ref="modalForm" @ok="modalFormOk"></ycl-test-taizhang-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import YclTestTaizhangModal from './modules/YclTestTaizhangModal'
  import {base64Encode} from "@api/kkfileView";

  export default {
    name: 'YclTestTaizhangList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      YclTestTaizhangModal
    },
    data () {
      return {
        description: 'ycl_test_taizhang管理页面',
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
            title:'部门',
            align:"center",
            dataIndex: 'sysOrgCode_dictText'
          },
          {
            title:'材料名称',
            align:"center",
            dataIndex: 'cailiaoname'
          },
          {
            title:'规格型号',
            align:"center",
            dataIndex: 'guige'
          },
          {
            title:'材料类型',
            align:"center",
            dataIndex: 'nodetype_dictText'
          },
          {
            title:'供应商名称',
            align:"center",
            dataIndex: 'gongyingshang'
          },
          {
            title:'生产批号',
            align:"center",
            dataIndex: 'pici'
          },
          {
            title:'进厂时间',
            align:"center",
            dataIndex: 'jinchangtime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'存放地点',
            align:"center",
            dataIndex: 'cunfangplace'
          },
          // {
          //   title:'数量',
          //   align:"center",
          //   dataIndex: 'shuliang'
          // },
          // {
          //   title:'使用部位',
          //   align:"center",
          //   dataIndex: 'usepart'
          // },
          {
            title:'自检结果',
            align:"center",
            dataIndex: 'reslut',
            scopedSlots: { customRender: 'tags2' },
          },
          // {
          //   title:'自检pdf',
          //   align:"center",
          //   dataIndex: 'zjpdf'
          // },
          {
            title:'抽检结果',
            align:"center",
            dataIndex: 'cjreslut',
            scopedSlots: { customRender: 'tags1' },
          },
          // {
          //   title:'抽检pdf',
          //   align:"center",
          //   dataIndex: 'cjpdf'
          // },
          // {
          //   title:'计算值',
          //   align:"center",
          //   dataIndex: 'count'
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
          list: "/ycltesttaizhang/yclTestTaizhang/list",
          delete: "/ycltesttaizhang/yclTestTaizhang/delete",
          deleteBatch: "/ycltesttaizhang/yclTestTaizhang/deleteBatch",
          exportXlsUrl: "/ycltesttaizhang/yclTestTaizhang/exportXls",
          importExcelUrl: "ycltesttaizhang/yclTestTaizhang/importExcel",
          
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
      handlePreview(record) {
        if (record.zjpdf ) {
         // let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode(record.url))
          window.open(record.zjpdf, '_blank')
        }
        if(record.cjpdf){
          window.open(record.cjpdf, '_blank')
        }
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'cailiaoname',text:'材料名称'})
        fieldList.push({type:'string',value:'guige',text:'规格型号'})
        fieldList.push({type:'string',value:'nodetype',text:'材料 nodetype'})
        fieldList.push({type:'string',value:'gongyingshang',text:'供应商名称'})
        fieldList.push({type:'string',value:'pici',text:'生产批号'})
        fieldList.push({type:'date',value:'jinchangtime',text:'进厂时间'})
        fieldList.push({type:'string',value:'cunfangplace',text:'存放地点'})
        fieldList.push({type:'string',value:'reslut',text:'自检结果'})
        fieldList.push({type:'string',value:'zjpdf',text:'自检pdf'})
        fieldList.push({type:'string',value:'cjreslut',text:'抽检结果'})
        fieldList.push({type:'string',value:'cjpdf',text:'抽检pdf'})

        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>