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
      <a-button type="primary" icon="download" @click="handleExportXls('anquan_fxaqjc_info')">导出</a-button>
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

    <anquan-fxaqjc-info-modal ref="modalForm" @ok="modalFormOk"></anquan-fxaqjc-info-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import AnquanFxaqjcInfoModal from './modules/AnquanFxaqjcInfoModal'

  export default {
    name: 'AnquanFxaqjcInfoList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      AnquanFxaqjcInfoModal
    },
    data () {
      return {
        description: 'anquan_fxaqjc_info管理页面',
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
            title:'年度如 2024年度研判',
            align:"center",
            dataIndex: 'niandu'
          },
          {
            title:'项目名称',
            align:"center",
            dataIndex: 'projectname'
          },
          {
            title:'所属业务部门',
            align:"center",
            dataIndex: 'departname'
          },
          {
            title:'guid',
            align:"center",
            dataIndex: 'guid'
          },
          {
            title:'说明 备注一栏根据对问题进行归类，如工程质量、施工安全、文明施工、内业资料、综合管理等',
            align:"center",
            dataIndex: 'note'
          },
          {
            title:'整改单位（责任单位）',
            align:"center",
            dataIndex: 'zhenggaiUnit'
          },
          {
            title:'整改责任人【暂不使用】',
            align:"center",
            dataIndex: 'zhengaiPeople'
          },
          {
            title:'整改情况 整改情况应填写“已整改”或“整改中”，不得采用其他描述',
            align:"center",
            dataIndex: 'zhenggaiInfo'
          },
          {
            title:'工点名称',
            align:"center",
            dataIndex: 'manageLocation'
          },
          {
            title:'验收单位',
            align:"center",
            dataIndex: 'yanshouUnit'
          },
          {
            title:'整改期限年月日（完成期限）',
            align:"center",
            dataIndex: 'zhenggaiTime'
          },
          {
            title:'发现问题内容 问题简要描述应具体扼要，原则上一行一序号且只描述1个问题',
            align:"center",
            dataIndex: 'problems'
          },
          {
            title:'整改措施和要求',
            align:"center",
            dataIndex: 'zhenggaiRequire'
          },
          {
            title:'检查人',
            align:"center",
            dataIndex: 'checkPeople'
          },
          {
            title:'检查年月日',
            align:"center",
            dataIndex: 'checkTime'
          },
          {
            title:'问题类别： ⼀般问题；⼀般隐患；重⼤隐患',
            align:"center",
            dataIndex: 'problemType'
          },
          {
            title:'治理要求',
            align:"center",
            dataIndex: 'procedure1Info'
          },
          {
            title:'procedure1Reslut',
            align:"center",
            dataIndex: 'procedure1Reslut'
          },
          {
            title:'签发人',
            align:"center",
            dataIndex: 'procedure1People'
          },
          {
            title:'检查年月日',
            align:"center",
            dataIndex: 'procedure1Time'
          },
          {
            title:'procedure2Reslut',
            align:"center",
            dataIndex: 'procedure2Reslut'
          },
          {
            title:'接收人（整改责任人）',
            align:"center",
            dataIndex: 'procedure2People'
          },
          {
            title:'接收时间年月日',
            align:"center",
            dataIndex: 'procedure2Time'
          },
          {
            title:'编号',
            align:"center",
            dataIndex: 'zlsNo'
          },
          {
            title:'是否销号应填写“是”或“否”，不得采用其他描述。',
            align:"center",
            dataIndex: 'isover'
          },
          {
            title:'隐患类别',
            align:"center",
            dataIndex: 'yhlb'
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
          list: "/anquanfxgk/anquanFxaqjcInfo/list",
          delete: "/anquanfxgk/anquanFxaqjcInfo/delete",
          deleteBatch: "/anquanfxgk/anquanFxaqjcInfo/deleteBatch",
          exportXlsUrl: "/anquanfxgk/anquanFxaqjcInfo/exportXls",
          importExcelUrl: "anquanfxgk/anquanFxaqjcInfo/importExcel",
          
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
        fieldList.push({type:'string',value:'niandu',text:'年度如 2024年度研判'})
        fieldList.push({type:'string',value:'projectname',text:'项目名称'})
        fieldList.push({type:'string',value:'departname',text:'所属业务部门'})
        fieldList.push({type:'string',value:'guid',text:'guid'})
        fieldList.push({type:'string',value:'note',text:'说明 备注一栏根据对问题进行归类，如工程质量、施工安全、文明施工、内业资料、综合管理等'})
        fieldList.push({type:'string',value:'zhenggaiUnit',text:'整改单位（责任单位）'})
        fieldList.push({type:'string',value:'zhengaiPeople',text:'整改责任人【暂不使用】'})
        fieldList.push({type:'string',value:'zhenggaiInfo',text:'整改情况 整改情况应填写“已整改”或“整改中”，不得采用其他描述'})
        fieldList.push({type:'string',value:'manageLocation',text:'工点名称'})
        fieldList.push({type:'string',value:'yanshouUnit',text:'验收单位'})
        fieldList.push({type:'string',value:'zhenggaiTime',text:'整改期限年月日（完成期限）'})
        fieldList.push({type:'string',value:'problems',text:'发现问题内容 问题简要描述应具体扼要，原则上一行一序号且只描述1个问题'})
        fieldList.push({type:'string',value:'zhenggaiRequire',text:'整改措施和要求'})
        fieldList.push({type:'string',value:'checkPeople',text:'检查人'})
        fieldList.push({type:'string',value:'checkTime',text:'检查年月日'})
        fieldList.push({type:'string',value:'problemType',text:'问题类别： ⼀般问题；⼀般隐患；重⼤隐患'})
        fieldList.push({type:'string',value:'procedure1Info',text:'治理要求'})
        fieldList.push({type:'string',value:'procedure1Reslut',text:'procedure1Reslut'})
        fieldList.push({type:'string',value:'procedure1People',text:'签发人'})
        fieldList.push({type:'string',value:'procedure1Time',text:'检查年月日'})
        fieldList.push({type:'string',value:'procedure2Reslut',text:'procedure2Reslut'})
        fieldList.push({type:'string',value:'procedure2People',text:'接收人（整改责任人）'})
        fieldList.push({type:'string',value:'procedure2Time',text:'接收时间年月日'})
        fieldList.push({type:'string',value:'zlsNo',text:'编号'})
        fieldList.push({type:'int',value:'isover',text:'是否销号应填写“是”或“否”，不得采用其他描述。'})
        fieldList.push({type:'string',value:'yhlb',text:'隐患类别'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>