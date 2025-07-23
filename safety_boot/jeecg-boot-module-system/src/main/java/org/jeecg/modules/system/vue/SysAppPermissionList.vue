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
      <a-button type="primary" icon="download" @click="handleExportXls('app菜单数据')">导出</a-button>
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

    <sys-app-permission-modal ref="modalForm" @ok="modalFormOk"></sys-app-permission-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SysAppPermissionModal from './modules/SysAppPermissionModal'

  export default {
    name: 'SysAppPermissionList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SysAppPermissionModal
    },
    data () {
      return {
        description: 'app菜单数据管理页面',
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
            title:'父id',
            align:"center",
            dataIndex: 'parentId'
          },
          {
            title:'菜单标题',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'菜单权限编码',
            align:"center",
            dataIndex: 'menuPerms'
          },
          {
            title:'菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)',
            align:"center",
            dataIndex: 'menuType'
          },
          {
            title:'按钮权限编码',
            align:"center",
            dataIndex: 'btnPerms'
          },
          {
            title:'权限策略1显示2禁用',
            align:"center",
            dataIndex: 'permsType'
          },
          {
            title:'菜单排序',
            align:"center",
            dataIndex: 'sortNo'
          },
          {
            title:'菜单图标',
            align:"center",
            dataIndex: 'icon'
          },
          {
            title:'是否叶子节点:    1:是   0:不是',
            align:"center",
            dataIndex: 'isLeaf'
          },
          {
            title:'是否隐藏路由: 0否,1是',
            align:"center",
            dataIndex: 'hidden'
          },
          {
            title:'描述',
            align:"center",
            dataIndex: 'description'
          },
          {
            title:'删除状态 0正常 1已删除',
            align:"center",
            dataIndex: 'delFlag'
          },
          {
            title:'是否添加数据权限1是0否',
            align:"center",
            dataIndex: 'ruleFlag'
          },
          {
            title:'按钮权限状态(0无效1有效)',
            align:"center",
            dataIndex: 'status'
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
          list: "/system/sysAppPermission/list",
          delete: "/system/sysAppPermission/delete",
          deleteBatch: "/system/sysAppPermission/deleteBatch",
          exportXlsUrl: "/system/sysAppPermission/exportXls",
          importExcelUrl: "system/sysAppPermission/importExcel",
          
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
        fieldList.push({type:'string',value:'parentId',text:'父id',dictCode:''})
        fieldList.push({type:'string',value:'name',text:'菜单标题',dictCode:''})
        fieldList.push({type:'string',value:'menuPerms',text:'菜单权限编码',dictCode:''})
        fieldList.push({type:'int',value:'menuType',text:'菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)',dictCode:''})
        fieldList.push({type:'string',value:'btnPerms',text:'按钮权限编码',dictCode:''})
        fieldList.push({type:'string',value:'permsType',text:'权限策略1显示2禁用',dictCode:''})
        fieldList.push({type:'double',value:'sortNo',text:'菜单排序',dictCode:''})
        fieldList.push({type:'string',value:'icon',text:'菜单图标',dictCode:''})
        fieldList.push({type:'int',value:'isLeaf',text:'是否叶子节点:    1:是   0:不是',dictCode:''})
        fieldList.push({type:'int',value:'hidden',text:'是否隐藏路由: 0否,1是',dictCode:''})
        fieldList.push({type:'string',value:'description',text:'描述',dictCode:''})
        fieldList.push({type:'int',value:'delFlag',text:'删除状态 0正常 1已删除',dictCode:''})
        fieldList.push({type:'int',value:'ruleFlag',text:'是否添加数据权限1是0否',dictCode:''})
        fieldList.push({type:'string',value:'status',text:'按钮权限状态(0无效1有效)',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>