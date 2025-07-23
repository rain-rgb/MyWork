<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeiNo" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
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
<!--      <a-button type="primary" icon="download" @click="handleExportXls('张拉预警配置表')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
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
          <a-tag v-if="record.yesornot == '0'">发送</a-tag>
          <a-tag v-if="record.yesornot == '1'">不发送</a-tag>
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

    <tr-zhangla-configure-modal ref="modalForm" @ok="modalFormOk"></tr-zhangla-configure-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import TrZhanglaConfigureModal from './modules/TrZhanglaConfigureModal'
  import { usershebeiList } from '@api/api'
  import Vue from "vue";

  export default {
    name: 'TrZhanglaConfigureList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      TrZhanglaConfigureModal,
      JSuperQuery
    },
    data () {
      return {
        dictOption: [],
        description: '张拉预警配置表管理页面',
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
            title:'设备名称',
            align:"center",
            dataIndex: 'shebeiNo_dictText'
          },
          {
            title:'是否预警',
            align:"center",
            dataIndex: 'alertOrNot_dictText'
          },
          {
            title:'张拉初级预警',
            align:"center",
            dataIndex: 'tensionChu'
          },
          {
            title:'张拉中级预警',
            align:"center",
            dataIndex: 'tensionZhong'
          },
          {
            title:'张拉高级预警',
            align:"center",
            dataIndex: 'tensionGao'
          },
          {
            title:'伸长率初级',
            align:"center",
            dataIndex: 'elongationChu'
          },
          {
            title:'伸长率中级',
            align:"center",
            dataIndex: 'elongationZhong'
          },
          {
            title:'伸长率高级',
            align:"center",
            dataIndex: 'elongationGao'
          },
          {
            title:'短信预警负责人',
            align:"center",
            dataIndex: 'names'
          },
          {
            title:'手机号码',
            align:"center",
            dataIndex: 'phones'
          },
          {
            title:'是否发送',
            align:"center",
            dataIndex: 'yesornot',
            scopedSlots: {customRender: 'tags'},
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
          list: "/trzhanglaconfigure/trZhanglaConfigure/list",
          delete: "/trzhanglaconfigure/trZhanglaConfigure/delete",
          deleteBatch: "/trzhanglaconfigure/trZhanglaConfigure/deleteBatch",
          exportXlsUrl: "/trzhanglaconfigure/trZhanglaConfigure/exportXls",
          importExcelUrl: "trzhanglaconfigure/trZhanglaConfigure/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
      this.shebeiList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'9'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'shebeiNo',text:'设备编号',dictCode:''})
        fieldList.push({type:'string',value:'alertOrNot',text:'是否预警',dictCode:''})
        fieldList.push({type:'double',value:'tensionChu',text:'张拉初级预警范围1',dictCode:''})
        fieldList.push({type:'double',value:'tensionChuend',text:'张拉初级预警范围2',dictCode:''})
        fieldList.push({type:'double',value:'tensionZhong',text:'张拉中级预警',dictCode:''})
        fieldList.push({type:'double',value:'tensionZhongend',text:'张拉中级预警2',dictCode:''})
        fieldList.push({type:'double',value:'tensionGao',text:'张拉高级预警',dictCode:''})
        fieldList.push({type:'double',value:'tensionGaoend',text:'张拉高级预警2',dictCode:''})
        fieldList.push({type:'double',value:'elongationChu',text:'伸长率初级',dictCode:''})
        fieldList.push({type:'double',value:'elongationChuend',text:'伸长率初级2',dictCode:''})
        fieldList.push({type:'double',value:'elongationZhong',text:'伸长率中级',dictCode:''})
        fieldList.push({type:'double',value:'elongationZhongend',text:'伸长率中级2',dictCode:''})
        fieldList.push({type:'double',value:'elongationGao',text:'伸长率高级',dictCode:''})
        fieldList.push({type:'double',value:'elongationGaoend',text:'伸长率高级2',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>