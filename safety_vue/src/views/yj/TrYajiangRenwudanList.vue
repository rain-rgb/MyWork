<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeibianhao" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="工程名称">
              <a-input placeholder="请输入工程名称" v-model="queryParam.projectname"></a-input>
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
      <a-button @click="handleAdd" type="primary" v-has="'yjrenwudan:add'" icon="plus">新增压浆任务单</a-button>
      <a-button type="primary" v-has="'yjrenwudan:dc'" icon="download" @click="handleExportXls('压浆任务单')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'yjrenwudan:dr'" icon="import">导入</a-button>
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
        <a-tag color="red" v-if="record.status == '0'">未使用</a-tag>
         <a-tag color="green" v-if="record.status == '1'">已使用</a-tag>
        </span>

        <span slot="uuid" slot-scope="uuid, record">
         <a-tag color="blue" @click="yajiangmessages(record.uuid)">{{record.uuid}}</a-tag>
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
          <a v-has="'yjrenwudan:edit'" @click="handleEdit(record)">编辑</a>

<!--          <a-divider type="vertical" />-->
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'yjrenwudan:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <TrYajiangMSModal ref="TrYajiangMSModal"></TrYajiangMSModal>
    <tr-yajiang-renwudan-modal ref="modalForm" @ok="modalFormOk"></tr-yajiang-renwudan-modal>
  </a-card>
</template>

<script>

  import '@assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import TrYajiangRenwudanModal from './modules/TrYajiangRenwudanModal'
  import JSuperQuery from '@comp/jeecg/JSuperQuery.vue'
  import { usershebeiList } from '@api/api'
  import TrYajiangMSModal from '@views/yj/modules/TrYajiangMSModal'
  import Vue from 'vue'

  export default {
    name: 'TrYajiangRenwudanList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      TrYajiangRenwudanModal,
      JSuperQuery,
      TrYajiangMSModal
    },
    data () {
      return {
        dictOption: [],
        selectValue:'',
        description: '压浆任务单管理页面',
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
            dataIndex: 'shebeibianhao_dictText'
          },
          {
            title:'任务单号',
            align:"center",
            dataIndex: 'uuid',
            scopedSlots: { customRender: 'uuid' },
          },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'projectname'
          },
          // {
          //   title:'梁场',
          //   align:"center",
          //   dataIndex: 'girderplace'
          // },
          // {
          //   title:'构件名称（梁名称）',
          //   align:"center",
          //   dataIndex: 'component'
          // },
          // {
          //   title:'施工部位',
          //   align:"center",
          //   dataIndex: 'sgbwuuid'
          // },
          {
            title:'施工部位',
            align:"center",
            dataIndex: 'sgbwname'
          },
          {
            title:'理论浆量',
            align:"center",
            dataIndex: 'lljl'
          },
          {
            title:'设计密度',
            align:"center",
            dataIndex: 'sjmd'
          },
          {
            title:'设计压力',
            align:"center",
            dataIndex: 'sjyl'
          },
          // {
          //   title:'台座',
          //   align:"center",
          //   dataIndex: 'pedestal'
          // },
          {
            title:'压浆任务状态',
            align:"center",
            dataIndex: 'status',
            scopedSlots: { customRender: 'status' },
          },
          {
            title:'压浆日期',
            align:"center",
            dataIndex: 'yjdate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          // {
          //   title:'组织机构',
          //   align:"center",
          //   dataIndex: 'departid'
          // },
          // {
          //   title:'orgcode',
          //   align:"center",
          //   dataIndex: 'orgcode'
          // },
          // {
          //   title:'departname',
          //   align:"center",
          //   dataIndex: 'departname'
          // },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'createTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'修改时间',
            align:"center",
            dataIndex: 'updateTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy_dictText'
          },
          {
            title:'所属组织机构',
            align:"center",
            dataIndex: 'sysOrgCode_dictText'
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
          list: "/yajiangrenwudan/trYajiangRenwudan/list",
          delete: "/yajiangrenwudan/trYajiangRenwudan/delete",
          deleteBatch: "/yajiangrenwudan/trYajiangRenwudan/deleteBatch",
          exportXlsUrl: "/yajiangrenwudan/trYajiangRenwudan/exportXls",
          importExcelUrl: "yajiangrenwudan/trYajiangRenwudan/importExcel",

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
      yajiangmessages:function (e){
        this.$refs.TrYajiangMSModal.title="压浆试验检测数据详情";
        this.$refs.TrYajiangMSModal.callmeaages(e);
      },
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'10'
        }).then(res=>{
          //console.log(res)
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })

        })
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'uuid',text:'任务单号',dictCode:''})
        fieldList.push({type:'string',value:'projectname',text:'工程名称',dictCode:''})
        fieldList.push({type:'string',value:'girderplace',text:'梁场',dictCode:''})
        fieldList.push({type:'string',value:'component',text:'构件名称（梁名称）',dictCode:''})
        fieldList.push({type:'string',value:'sgbwuuid',text:'施工部位',dictCode:''})
        fieldList.push({type:'string',value:'sgbwname',text:'施工部位名称',dictCode:''})
        fieldList.push({type:'string',value:'pedestal',text:'台座',dictCode:''})
        fieldList.push({type:'string',value:'status',text:'压浆任务状态码：0：未使用  1：已使用',dictCode:''})
        fieldList.push({type:'date',value:'yjdate',text:'压浆日期'})
        fieldList.push({type:'string',value:'departid',text:'组织机构',dictCode:''})
        fieldList.push({type:'string',value:'orgcode',text:'orgcode',dictCode:''})
        fieldList.push({type:'string',value:'departname',text:'departname',dictCode:''})
        fieldList.push({type:'string',value:'shebeibianhao',text:'设备编号',dictCode:''})
        fieldList.push({type:'string',value:'sysOrgCode',text:'所属组织机构',dictCode:''})
        fieldList.push({type:'date',value:'createTime',text:'创建时间'})
        fieldList.push({type:'date',value:'updateTime',text:'修改时间'})
        fieldList.push({type:'string',value:'createBy',text:'创建人',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>