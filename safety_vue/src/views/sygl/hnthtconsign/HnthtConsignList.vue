<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="工程名称">
              <a-input placeholder="请输入工程名称" v-model="queryParam.projectname"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="施工部位">
              <a-input placeholder="请输入施工部位" v-model="queryParam.sgbwname"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="任务单号">
              <a-input placeholder="请输入任务单号" v-model="queryParam.uuid"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="试验类型">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.component" dictCode="component"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="试验时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.zldate_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.zldate_end" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
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
<!--      <a-button @click="handleAdd" type="primary" v-has="'hnthtconsign:add'"  icon="plus">新增</a-button>-->
      <a-button @click="handleAdd" type="primary"  icon="plus">新增</a-button>
      <a-button type="primary" v-has="'hnthtconsign:dc'"  icon="download" @click="handleExportXls('检测试验任务单下发信息表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'hnthtconsign:dr'"  icon="import">导入</a-button>
      </a-upload>
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
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >

        <span slot="status" slot-scope="status, record">
        <a-tag color="red" v-if="record.status == '0'">未完成</a-tag>
         <a-tag color="green" v-if="record.status == '1'">已完成</a-tag>
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
          <a v-has="'hnthtconsign:edit'"  @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
<!--              <a-menu-item v-has="'hnthtconsign:status'">-->
                <a @click="handleStatus(record)">任务单完成</a>
              </a-menu-item>
              <a-menu-item v-has="'hnthtconsign:del'"  >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <hntht-consign-status :flag="flag" :id="id" @change="change"></hntht-consign-status>
    <hntht-consign-modal ref="modalForm" @ok="modalFormOk"></hntht-consign-modal>
  </a-card>
</template>

<script>

  import '@assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import HnthtConsignModal from './modules/HnthtConsignModal'
  import JSuperQuery from '@comp/jeecg/JSuperQuery.vue'
  import HnthtConsignStatus from '@views/sygl/hnthtconsign/HnthtConsignStatus'
  import { handertoken } from '@/mixins/getHanderToken'

  export default {
    name: 'HnthtConsignList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      HnthtConsignStatus,
      HnthtConsignModal,
      JSuperQuery,
    },
    data () {
      return {
        flag:0,
        id:0,
        description: '检测试验任务单下发信息表管理页面',
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
            title:'任务单号',
            align:"center",
            dataIndex: 'uuid'
          },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'projectname'
          },
          // {
          //   title:'检测单位 ',
          //   align:"center",
          //   dataIndex: 'jcdw'
          // },
          // {
          //   title:'施工单位 ',
          //   align:"center",
          //   dataIndex: 'sgdw'
          // },
          {
            title:'构件名称',
            align:"center",
            dataIndex: 'component'
          },
          // {
          //   title:'施工部位',
          //   align:"center",
          //   dataIndex: 'sgbwguid'
          // },
          {
            title:'施工部位',
            align:"center",
            dataIndex: 'sgbwname'
          },
          {
            title:'任务单状态',
            align:"center",
            dataIndex: 'status',
            scopedSlots: { customRender: 'status' },
          },
          {
            title:'试验日期',
            align:"center",
            dataIndex: 'zldate'
          },
          // {
          //   title:'组织机构id',
          //   align:"center",
          //   dataIndex: 'departid'
          // },
          // {
          //   title:'sytypeid',
          //   align:"center",
          //   dataIndex: 'sytypeid'
          // },
          // {
          //   title:'departname',
          //   align:"center",
          //   dataIndex: 'departname'
          // },
          {
            title:'所属部门',
            align:"center",
            dataIndex: 'sysOrgCode_dictText'
          },
          {
            title:'试验设备厂家 ',
            align:"center",
            dataIndex: 'shebeichangjia'
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy_dictText'
          },
          {
            title:'创建日期',
            align:"center",
            dataIndex: 'createTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'更新日期',
            align:"center",
            dataIndex: 'updateTime',
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
          list: "/hnthtconsign/hnthtConsign/list",
          delete: "/hnthtconsign/hnthtConsign/delete",
          deleteBatch: "/hnthtconsign/hnthtConsign/deleteBatch",
          exportXlsUrl: "/hnthtconsign/hnthtConsign/exportXls",
          importExcelUrl: "hnthtconsign/hnthtConsign/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getToken();
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      handleStatus(record){
        this.flag = 1;
        this.id=record.id
      },
      change(e) {
        this.flag = e
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'uuid',text:'任务单号',dictCode:''})
        fieldList.push({type:'string',value:'projectname',text:'工程名称',dictCode:''})
        fieldList.push({type:'string',value:'component',text:'构件名称',dictCode:''})
        fieldList.push({type:'string',value:'sgbwguid',text:'施工部位',dictCode:''})
        fieldList.push({type:'string',value:'sgbwname',text:'施工部位名称',dictCode:''})
        fieldList.push({type:'string',value:'status',text:'张拉任务状态码：0：未使用  1：已使用',dictCode:''})
        fieldList.push({type:'string',value:'zldate',text:'试验日期',dictCode:''})
        fieldList.push({type:'string',value:'departid',text:'组织机构id',dictCode:''})
        fieldList.push({type:'string',value:'sytypeid',text:'sytypeid',dictCode:''})
        fieldList.push({type:'string',value:'departname',text:'departname',dictCode:''})
        fieldList.push({type:'string',value:'createBy',text:'创建人',dictCode:''})
        fieldList.push({type:'date',value:'createTime',text:'创建日期'})
        fieldList.push({type:'date',value:'updateTime',text:'更新日期'})
        fieldList.push({type:'string',value:'sysOrgCode',text:'所属部门',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>