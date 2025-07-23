<template>
  <a-card :bordered="false">
      <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }"><span style="font-size: 22px;font-weight: bold">架桥机预警：{{jqjALlWarn}}</span></a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }"><span style="font-size: 22px;font-weight: bold">本月预警：{{jqjMonthWarn}}</span></a-col>
    </a-row>
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-dict-select-tag placeholder="请选择设备名称" v-model="queryParam.deviceCode"
                                 dictCode="shebei_info,sbname,sbjno,sbtype='23' and shebei_status='1'" dicText="sbname" dictTable="shebei_info"/>
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
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
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

    <jqj-shujubase-modal ref="modalForm" @ok="modalFormOk"></jqj-shujubase-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JqjShujubaseModal from './modules/JqjShujubaseModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { handertoken } from '@/mixins/getHanderToken'
import { getAction } from '@api/manage'

  export default {
    name: 'JqjShujubaseList',
    mixins:[JeecgListMixin, mixinDevice,handertoken],
    components: {
      JqjShujubaseModal,
      JSuperQuery,
    },
    data () {

      return {
        jqjAllWarn:'',
        jqjMonthWarn:'',
        columns: [
          {
            title: '序号',
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
            dataIndex: 'deviceCode_dictText'
          },
          {
            title:'数据类标志',
            align:"center",
            dataIndex: 'dataType'
          },
          {
            title:'大车横向行程',
            align:"center",
            dataIndex: 'bigCrosswise'
          },
          {
            title:'大车纵向行程',
            align:"center",
            dataIndex: 'bigPortrait'
          },
          {
            title:'支腿垂直度',
            align:"center",
            dataIndex: 'legPerpendicularity'
          },
          {
            title:'大车水平度',
            align:"center",
            dataIndex: 'bigLevel'
          },
          {
            title:'天车高度',
            align:"center",
            dataIndex: 'skyHeight1'
          },
          {
            title:'天车吊重',
            align:"center",
            dataIndex: 'skyWeight1'
          },
          {
            title:'天车横向行程',
            align:"center",
            dataIndex: 'skyCrosswise1'
          },
          {
            title:'天车纵向行程',
            align:"center",
            dataIndex: 'skyPortrait1'
          },
          {
            title:'天车吊钩载荷状态',
            align:"center",
            dataIndex: 'skyLoad'
          },
          {
            title:'天车吊钩制动器1状态',
            align:"center",
            dataIndex: 'skyBraking11'
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
          list: "/jqj/jqjShujubase/list",
          delete: "/jqj/jqjShujubase/delete",
          deleteBatch: "/jqj/jqjShujubase/deleteBatch",
          exportXlsUrl: "/jqj/jqjShujubase/exportXls",
          importExcelUrl: "jqj/jqjShujubase/importExcel",
        syjwarn:"/tsyjzbStatistics/tSyjzbStatistics/jqjwarn"

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getToken();
    this.getSuperFieldList();
    this.tongji();

    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
        tongji:function(){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        getAction(this.url.syjwarn,{sys_depart_orgcode})
          .then(res=>{
            var data = res.result
            this.jqjMonthWarn = data.mwarn;
            this.jqjALlWarn = data.allWarn;
          })
      },

      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'deviceCode',text:'设备编号'})
        fieldList.push({type:'int',value:'dataType',text:'数据类标志'})
        fieldList.push({type:'int',value:'bigCrosswise',text:'大车横向行程(放大100倍)'})
        fieldList.push({type:'int',value:'bigPortrait',text:'大车纵向行程（放大100倍）'})
        fieldList.push({type:'int',value:'legPerpendicularity',text:'支腿垂直度(放大100倍)'})
        fieldList.push({type:'int',value:'bigLevel',text:'大车水平度(放大100倍)'})
        fieldList.push({type:'int',value:'skyHeight1',text:'1#天车高度(放大100倍)'})
        fieldList.push({type:'int',value:'skyWeight1',text:'1#天车吊重（放大100倍）'})
        fieldList.push({type:'number',value:'skyCrosswise1',text:'1#天车横向行程（）'})
        fieldList.push({type:'number',value:'skyPortrait1',text:'1#天车纵向行程（）'})
        fieldList.push({type:'int',value:'skyLoad',text:'1#天车吊钩载荷状态'})
        fieldList.push({type:'int',value:'skyBraking11',text:'1#天车吊钩制动器1状态'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>