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
      <a-button type="primary" icon="download" @click="handleExportXls('水泥搅拌桩设备配置')">导出</a-button>
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

    <device-mixpile-one-cfg-modal ref="modalForm" @ok="modalFormOk"></device-mixpile-one-cfg-modal>
  </a-card>
</template>

<script>

  import '@assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DeviceMixpileOneCfgModal from './modules/DeviceMixpileOneCfgModal'
  import JSuperQuery from '@comp/jeecg/JSuperQuery.vue'

  export default {
    name: 'DeviceMixpileOneCfgList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      DeviceMixpileOneCfgModal,
      JSuperQuery,
    },
    data () {
      return {
        description: '水泥搅拌桩设备配置管理页面',
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
          //   title:'主键uuid',
          //   align:"center",
          //   dataIndex: 'uid'
          // },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'shebeino_dictText'
          },
          {
            title:'设计桩长',
            align:"center",
            dataIndex: 'designdep'
          },
          {
            title:'施工工艺',
            align:"center",
            dataIndex: 'sggy_dictText'
          },
          {
            title:'提钻水泥浆量下限',
            align:"center",
            dataIndex: 'upbetonx'
          },
          // {
          //   title:'提钻水泥浆量上限',
          //   align:"center",
          //   dataIndex: 'upbetons'
          // },
          {
            title:'提钻速度',
            align:"center",
            dataIndex: 'uspeedx'
          },
          {
            title:'下钻水泥浆量下限',
            align:"center",
            dataIndex: 'downbetonx'
          },
          // {
          //   title:'下钻水泥浆量上限',
          //   align:"center",
          //   dataIndex: 'downbetons'
          // },
          {
            title:'下钻速度',
            align:"center",
            dataIndex: 'dspeedx'
          },
          {
            title:'平均密度',
            align:"center",
            dataIndex: 'cement'
          },
          {
            title: '平均灰量',
            dataIndex: 'pileMinelec',
            align:"center",
            // scopedSlots: { customRender: 'sum2' }
          },
          {
            title:'水泥浆比重',
            align:"center",
            dataIndex: 'pileDensity',
          },
          {
            title:'倾角下限',
            align:"center",
            dataIndex: 'xx'
          },
          {
            title:'倾角上限',
            align:"center",
            dataIndex: 'xs'
          },
          {
            title:'水泥搅拌桩预警号码',
            align:"center",
            dataIndex: 'jbzphones_dictText'
          },
          {
            title:'是否报警',
            align:"center",
            dataIndex: 'isCall_dictText'
          },
          {
            title:'权限配置',
            align:"center",
            dataIndex: 'sysOrgCode_dictText'
          },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'createTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'更新时间',
            align:"center",
            dataIndex: 'updateTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          // {
          //   title:'是否删除（0：未删除，1：已删除）',
          //   align:"center",
          //   dataIndex: 'isdel'
          // },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy'
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
          list: "/devicemixpileoneCfg/deviceMixpileOneCfg/list",
          delete: "/devicemixpileoneCfg/deviceMixpileOneCfg/delete",
          deleteBatch: "/devicemixpileoneCfg/deviceMixpileOneCfg/deleteBatch",
          exportXlsUrl: "/devicemixpileoneCfg/deviceMixpileOneCfg/exportXls",
          importExcelUrl: "devicemixpileoneCfg/deviceMixpileOneCfg/importExcel",
          
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
        fieldList.push({type:'string',value:'uid',text:'主键uuid',dictCode:''})
        fieldList.push({type:'string',value:'shebeino',text:'设备编号',dictCode:''})
        fieldList.push({type:'string',value:'designdep',text:'设计桩长',dictCode:''})
        fieldList.push({type:'string',value:'upbetonx',text:'提钻水泥浆量下限',dictCode:''})
        fieldList.push({type:'string',value:'upbetons',text:'提钻水泥浆量上限',dictCode:''})
        fieldList.push({type:'string',value:'uspeedx',text:'提钻速度',dictCode:''})
        fieldList.push({type:'string',value:'downbetonx',text:'下钻水泥浆量下限',dictCode:''})
        fieldList.push({type:'string',value:'downbetons',text:'下钻水泥浆量上限',dictCode:''})
        fieldList.push({type:'string',value:'dspeedx',text:'下钻速度',dictCode:''})
        fieldList.push({type:'string',value:'cement',text:'水泥浆用量',dictCode:''})
        fieldList.push({type:'string',value:'xx',text:'倾角下限',dictCode:''})
        fieldList.push({type:'string',value:'xs',text:'倾角上限',dictCode:''})
        fieldList.push({type:'string',value:'jbzphones',text:'水泥搅拌桩预警号码',dictCode:''})
        fieldList.push({type:'int',value:'isCall',text:'是否报警:0=报警,1=不报警',dictCode:''})
        fieldList.push({type:'string',value:'sysOrgCode',text:'权限配置',dictCode:''})
        fieldList.push({type:'date',value:'createTime',text:'创建时间'})
        fieldList.push({type:'date',value:'updateTime',text:'更新时间'})
        fieldList.push({type:'int',value:'isdel',text:'是否删除（0：未删除，1：已删除）',dictCode:''})
        fieldList.push({type:'string',value:'createBy',text:'创建人',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>