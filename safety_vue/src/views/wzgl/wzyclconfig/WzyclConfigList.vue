<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.sbjno" :dictOptions="dictOption" >
              </j-search-select-tag>
              <!--              {{ selectValue }}-->
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
      <a-button type="primary" icon="download" @click="handleExportXls('wzycl_config')">导出</a-button>
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

    <wzycl-config-modal ref="modalForm" @ok="modalFormOk"></wzycl-config-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WzyclConfigModal from './modules/WzyclConfigModal'
  import Vue from "vue";
  import {usershebeiList} from "@api/api";

  export default {
    name: 'WzyclConfigList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WzyclConfigModal
    },
    data () {
      return {
        description: 'wzycl_config管理页面',
        dictOption:[],
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
            title:'设备编号',
            align:"center",
            dataIndex: 'sbjno_dictText'
          },
          {
            title:'生产批次时间（h）',
            align:"center",
            dataIndex: 'hour'
          },
          {
            title:'粗集料(t)',
            align:"center",
            dataIndex: 'cujiliao'
          },
          {
            title:'细集料(t)',
            align:"center",
            dataIndex: 'xijiliao'
          },
          {
            title:'水泥(t)',
            align:"center",
            dataIndex: 'shuini'
          },
          {
            title:'矿粉(t)',
            align:"center",
            dataIndex: 'kuangfeng'
          },
          {
            title:'粉煤灰(t)',
            align:"center",
            dataIndex: 'fenmeihui'
          },
          {
            title:'减水剂(t)',
            align:"center",
            dataIndex: 'jianshuiji'
          },
          {
            title:'其他(t)',
            align:"center",
            dataIndex: 'other'
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
          list: "/wztaizhang/wzyclConfig/list",
          delete: "/wztaizhang/wzyclConfig/delete",
          deleteBatch: "/wztaizhang/wzyclConfig/deleteBatch",
          exportXlsUrl: "/wztaizhang/wzyclConfig/exportXls",
          importExcelUrl: "wztaizhang/wzyclConfig/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.shebeiList();
    this.getSuperFieldList();
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
          sbtypes:'27'
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
        fieldList.push({type:'string',value:'sbjno',text:'设备编号'})
        fieldList.push({type:'int',value:'hour',text:'生产批次时间（h）'})
        fieldList.push({type:'int',value:'cujiliao',text:'粗集料(t)'})
        fieldList.push({type:'int',value:'xijiliao',text:'细集料(t)'})
        fieldList.push({type:'int',value:'shuini',text:'水泥(t)'})
        fieldList.push({type:'int',value:'kuangfeng',text:'矿粉(t)'})
        fieldList.push({type:'int',value:'fenmeihui',text:'粉煤灰(t)'})
        fieldList.push({type:'int',value:'jianshuiji',text:'减水剂(t)'})
        fieldList.push({type:'int',value:'other',text:'其他(t)'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>