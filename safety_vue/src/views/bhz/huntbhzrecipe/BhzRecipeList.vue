<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.bhjno" :dictOptions="dictOption" >
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
      <a-button type="primary" v-has="'recipe:dc'" icon="download" @click="handleExportXls('砼拌合站理论配合比主表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'recipe:dr'" icon="import">导入</a-button>
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
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >
       <span slot="code" slot-scope="code, record">
        <a-tag color="green" >{{record.code}}</a-tag>
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

    <bhz-recipe-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BhzRecipeModal from './modules/BhzRecipeModal'
  import '@/assets/less/TableExpand.less'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'

  export default {
    name: "BhzRecipeList",
    mixins:[JeecgListMixin],
    components: {
      BhzRecipeModal,
      JSuperQuery
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '砼拌合站理论配合比主表管理页面',
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
          //   title:'唯一ID',
          //   align:"center",
          //   dataIndex: 'uuid'
          // },
          {
            title:'配合比编号',
            align:"center",
            dataIndex: 'code',
            key: 'code',
            scopedSlots: { customRender: 'code' },
          },
          // {
          //   title:'砼标记',
          //   align:"center",
          //   dataIndex: 'tag'
          // },
          // {
          //   title:'混凝土类别',
          //   align:"center",
          //   dataIndex: 'variety'
          // },
          {
            title:'强度等级',
            align:"center",
            dataIndex: 'betlevel'
          },
          {
            title:'抗渗等级',
            align:"center",
            dataIndex: 'filters'
          },
          {
            title:'抗冻等级',
            align:"center",
            dataIndex: 'freeze'
          },
          {
            title:'抗折等级',
            align:"center",
            dataIndex: 'bend'
          },
          // {
          //   title:'施工季节',
          //   align:"center",
          //   dataIndex: 'season'
          // },
          {
            title:'坍落度',
            align:"center",
            dataIndex: 'lands'
          },
          {
            title:'搅拌时长',
            align:"center",
            dataIndex: 'mixlast'
          },
          // {
          //   title:'设计比例',
          //   align:"center",
          //   dataIndex: 'scale'
          // },
          // {
          //   title:'骨料最大粒径',
          //   align:"center",
          //   dataIndex: 'bonesz'
          // },
          // {
          //   title:'水泥品种',
          //   align:"center",
          //   dataIndex: 'cementtype'
          // },
          // {
          //   title:'status',
          //   align:"center",
          //   dataIndex: 'status'
          // },
          {
            title:'建立时间',
            align:"center",
            dataIndex: 'createdate'
          },
          {
            title:'拌合机名称',
            align:"center",
            dataIndex: 'bhjno_dictText'
          },
          // {
          //   title:'砂率',
          //   align:"center",
          //   dataIndex: 'sandratio'
          // },
          {
            title:'水胶比',
            align:"center",
            dataIndex: 'waterbindratio'
          },
          // {
          //   title:'每方重量kg',
          //   align:"center",
          //   dataIndex: 'onevolume'
          // },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/bhzrecipe/bhzRecipe/list",
          delete: "/bhzrecipe/bhzRecipe/delete",
          deleteBatch: "/bhzrecipe/bhzRecipe/deleteBatch",
          exportXlsUrl: "/bhzrecipe/bhzRecipe/exportXls",
          importExcelUrl: "bhzrecipe/bhzRecipe/importExcel",

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
      }
    },
    methods: {
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'0'
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
         fieldList.push({type:'string',value:'uuid',text:'唯一ID',dictCode:''})
         fieldList.push({type:'string',value:'code',text:'配合比编号',dictCode:''})
         fieldList.push({type:'string',value:'tag',text:'砼标记',dictCode:''})
         fieldList.push({type:'string',value:'variety',text:'混凝土类别',dictCode:''})
         fieldList.push({type:'string',value:'betlevel',text:'强度等级',dictCode:''})
         fieldList.push({type:'string',value:'filters',text:'抗渗等级',dictCode:''})
         fieldList.push({type:'string',value:'freeze',text:'抗冻等级',dictCode:''})
         fieldList.push({type:'string',value:'bend',text:'抗折等级',dictCode:''})
         fieldList.push({type:'string',value:'season',text:'施工季节',dictCode:''})
         fieldList.push({type:'string',value:'lands',text:'坍落度',dictCode:''})
         fieldList.push({type:'int',value:'mixlast',text:'搅拌时长',dictCode:''})
         fieldList.push({type:'string',value:'scale',text:'设计比例',dictCode:''})
         fieldList.push({type:'string',value:'bonesz',text:'骨料最大粒径',dictCode:''})
         fieldList.push({type:'string',value:'cementtype',text:'水泥品种',dictCode:''})
         fieldList.push({type:'int',value:'status',text:'status',dictCode:''})
         fieldList.push({type:'string',value:'createdate',text:'建立时间',dictCode:''})
         fieldList.push({type:'string',value:'bhjno',text:'拌合机编号',dictCode:''})
         fieldList.push({type:'string',value:'sandratio',text:'砂率',dictCode:''})
         fieldList.push({type:'string',value:'waterbindratio',text:'水胶比',dictCode:''})
         fieldList.push({type:'double',value:'onevolume',text:'每方重量kg',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>