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
      <a-button type="primary" icon="download" @click="handleExportXls('sy_main')">导出</a-button>
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

    <sy-main-modal ref="modalForm" @ok="modalFormOk"></sy-main-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SyMainModal from './modules/SyMainModal'

  export default {
    name: 'SyMainList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SyMainModal
    },
    data () {
      return {
        description: 'sy_main管理页面',
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
          //   dataIndex: 'uuid'
          // },
          {
            title:'部门',
            align:"center",
            dataIndex: 'sysorgcode_dictText'
          },
          // {
          //   title:'设备编号',
          //   align:"center",
          //   dataIndex: 'deviceId'
          // },
          // {
          //   title:'样品封签照片（图片）',
          //   align:"center",
          //   dataIndex: 'fqzpstart'
          // },
          // {
          //   title:'留样封签照片（图片）',
          //   align:"center",
          //   dataIndex: 'fqzpend'
          // },
          // {
          //   title:'仪器校准照片（图片）',
          //   align:"center",
          //   dataIndex: 'yqjzzp'
          // },
          // {
          //   title:'检测环境照片（图片）',
          //   align:"center",
          //   dataIndex: 'jchjzp'
          // },
          // {
          //   title:'试验类型，与dps_jc_testItemType表titCode关联',
          //   align:"center",
          //   dataIndex: 'testType'
          // },
          {
            title:'检测标准',
            align:"center",
            dataIndex: 'testStandard'
          },
          {
            title:'检测状态',
            align:"center",
            dataIndex: 'testStatus_dictText'
          },
          {
            title:'试验结果',
            align:"center",
            dataIndex: 'testResult'
          },
          {
            title:'检测数据',
            align:"center",
            dataIndex: 'testData'
          },
          {
            title:'检验批',
            align:"center",
            dataIndex: 'inspectionIot'
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy_dictText'
          },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'createTime'
          },
          // {
          //   title:'摄像头id',
          //   align:"center",
          //   dataIndex: 'cameraId'
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
          list: "/syMain/syMain/list",
          delete: "/syMain/syMain/delete",
          deleteBatch: "/syMain/syMain/deleteBatch",
          exportXlsUrl: "/syMain/syMain/exportXls",
          importExcelUrl: "syMain/syMain/importExcel",

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
      handleDetail(record){

      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'uuid',text:'唯一id'})
        fieldList.push({type:'string',value:'sysorgcode',text:'组织结构代码'})
        fieldList.push({type:'string',value:'deviceId',text:'设备编号'})
        fieldList.push({type:'string',value:'fqzpstart',text:'样品封签照片（图片）'})
        fieldList.push({type:'string',value:'fqzpend',text:'留样封签照片（图片）'})
        fieldList.push({type:'string',value:'yqjzzp',text:'仪器校准照片（图片）'})
        fieldList.push({type:'string',value:'jchjzp',text:'检测环境照片（图片）'})
        fieldList.push({type:'string',value:'testType',text:'试验类型，与dps_jc_testItemType表titCode关联'})
        fieldList.push({type:'string',value:'testStandard',text:'检测标准'})
        fieldList.push({type:'int',value:'testStatus',text:'检测状态（0：未开始，1：准备中，2：试验中，3：已结束，10：合格，11：不合格，默认为0）'})
        fieldList.push({type:'int',value:'testResult',text:'试验结果（0：合格，1：不合格，暂不使用）'})
        fieldList.push({type:'string',value:'testData',text:'检测数据（PDF文件）'})
        fieldList.push({type:'string',value:'cameraId',text:'摄像头id'})
        fieldList.push({type:'string',value:'inspectionIot',text:'检验批'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
