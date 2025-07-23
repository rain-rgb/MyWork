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
      <a-button type="primary" icon="download" @click="handleExportXls('wzgongyingshang_man')">导出</a-button>
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

    <wzgongyingshang-man-modal ref="modalForm" @ok="modalFormOk"></wzgongyingshang-man-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WzgongyingshangManModal from './modules/WzgongyingshangManModal'

  export default {
    name: 'WzgongyingshangManList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WzgongyingshangManModal
    },
    data () {
      return {
        description: 'wzgongyingshang_man管理页面',
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
            title:'供应商名',
            align:"center",
            dataIndex: 'gongyingshangname'
          },
          {
            title:'供应商名字母缩写',
            align:"center",
            dataIndex: 'pinyin'
          },
          {
            title:'单位编码',
            align:"center",
            dataIndex: 'danweibianma'
          },
          {
            title:'供应材料',
            align:"center",
            dataIndex: 'gongyingcailiao'
          },
          {
            title:'所属地区',
            align:"center",
            dataIndex: 'suoshudiqu'
          },
          {
            title:'联系人',
            align:"center",
            dataIndex: 'lianxiren'
          },
          {
            title:'联系电话',
            align:"center",
            dataIndex: 'lianxidianhua'
          },
          {
            title:'手机',
            align:"center",
            dataIndex: 'shouji'
          },
          {
            title:'传真',
            align:"center",
            dataIndex: 'chuanzhen'
          },
          {
            title:'邮编',
            align:"center",
            dataIndex: 'youbian'
          },
          {
            title:'通讯地址',
            align:"center",
            dataIndex: 'tongxundizhi'
          },
          {
            title:'网址',
            align:"center",
            dataIndex: 'wangzhi'
          },
          {
            title:'证书名称',
            align:"center",
            dataIndex: 'zhengshumingcheng'
          },
          {
            title:'证书编号',
            align:"center",
            dataIndex: 'zhengshubianhao'
          },
          {
            title:'发证时间',
            align:"center",
            dataIndex: 'fazhengshijian',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'有效期限',
            align:"center",
            dataIndex: 'youxiaoqixian',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'发证机关',
            align:"center",
            dataIndex: 'fazhengjiguan'
          },
          {
            title:'证件图片',
            align:"center",
            dataIndex: 'zhengjiantupian'
          },
          {
            title:'开户银行',
            align:"center",
            dataIndex: 'kaihuyinhang'
          },
          {
            title:'银行账号',
            align:"center",
            dataIndex: 'yinhangzhanghao'
          },
          {
            title:'企业税号',
            align:"center",
            dataIndex: 'qiyeshuihao'
          },
          {
            title:'邮箱',
            align:"center",
            dataIndex: 'youxiang'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title:'企业状态:0=正常,1=异常',
            align:"center",
            dataIndex: 'states'
          },
          {
            title:'供应材料名',
            align:"center",
            dataIndex: 'gongyingcailiaoname'
          },
          {
            title:'UUID',
            align:"center",
            dataIndex: 'guid'
          },
          {
            title:'时间戳',
            align:"center",
            dataIndex: 'ts'
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'status'
          },
          {
            title:'是否删除:0=否,1=是',
            align:"center",
            dataIndex: 'isdel'
          },
          {
            title:'组织机构ID',
            align:"center",
            dataIndex: 'departid'
          },
          {
            title:'修改人',
            align:"center",
            dataIndex: 'editperson'
          },
          {
            title:'修改时间',
            align:"center",
            dataIndex: 'editdatetime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'是否使用电子锁 0：否，1：是',
            align:"center",
            dataIndex: 'iselocks'
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
          list: "/wzgongyingshangman/wzgongyingshangMan/list",
          delete: "/wzgongyingshangman/wzgongyingshangMan/delete",
          deleteBatch: "/wzgongyingshangman/wzgongyingshangMan/deleteBatch",
          exportXlsUrl: "/wzgongyingshangman/wzgongyingshangMan/exportXls",
          importExcelUrl: "wzgongyingshangman/wzgongyingshangMan/importExcel",
          
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
        fieldList.push({type:'string',value:'gongyingshangname',text:'供应商名'})
        fieldList.push({type:'string',value:'pinyin',text:'供应商名字母缩写'})
        fieldList.push({type:'string',value:'danweibianma',text:'单位编码'})
        fieldList.push({type:'string',value:'gongyingcailiao',text:'供应材料'})
        fieldList.push({type:'string',value:'suoshudiqu',text:'所属地区'})
        fieldList.push({type:'string',value:'lianxiren',text:'联系人'})
        fieldList.push({type:'string',value:'lianxidianhua',text:'联系电话'})
        fieldList.push({type:'string',value:'shouji',text:'手机'})
        fieldList.push({type:'string',value:'chuanzhen',text:'传真'})
        fieldList.push({type:'string',value:'youbian',text:'邮编'})
        fieldList.push({type:'string',value:'tongxundizhi',text:'通讯地址'})
        fieldList.push({type:'string',value:'wangzhi',text:'网址'})
        fieldList.push({type:'string',value:'zhengshumingcheng',text:'证书名称'})
        fieldList.push({type:'string',value:'zhengshubianhao',text:'证书编号'})
        fieldList.push({type:'date',value:'fazhengshijian',text:'发证时间'})
        fieldList.push({type:'date',value:'youxiaoqixian',text:'有效期限'})
        fieldList.push({type:'string',value:'fazhengjiguan',text:'发证机关'})
        fieldList.push({type:'string',value:'zhengjiantupian',text:'证件图片'})
        fieldList.push({type:'string',value:'kaihuyinhang',text:'开户银行'})
        fieldList.push({type:'string',value:'yinhangzhanghao',text:'银行账号'})
        fieldList.push({type:'string',value:'qiyeshuihao',text:'企业税号'})
        fieldList.push({type:'string',value:'youxiang',text:'邮箱'})
        fieldList.push({type:'string',value:'remark',text:'备注'})
        fieldList.push({type:'string',value:'states',text:'企业状态:0=正常,1=异常'})
        fieldList.push({type:'string',value:'gongyingcailiaoname',text:'供应材料名'})
        fieldList.push({type:'string',value:'guid',text:'UUID'})
        fieldList.push({type:'int',value:'ts',text:'时间戳'})
        fieldList.push({type:'int',value:'status',text:'状态'})
        fieldList.push({type:'int',value:'isdel',text:'是否删除:0=否,1=是'})
        fieldList.push({type:'string',value:'departid',text:'组织机构ID'})
        fieldList.push({type:'string',value:'editperson',text:'修改人'})
        fieldList.push({type:'date',value:'editdatetime',text:'修改时间'})
        fieldList.push({type:'int',value:'iselocks',text:'是否使用电子锁 0：否，1：是'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>