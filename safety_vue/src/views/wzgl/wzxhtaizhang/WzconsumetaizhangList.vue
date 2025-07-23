<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.starttim_begin" :showTime="true" dateFormat="YYYY-MM-DD" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.starttim_end" :showTime="true" dateFormat="YYYY-MM-DD" />
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
      <a-button @click="handleAdd" v-has="'wzxiahao:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" v-has="'wzxiahao:dc'" @click="handleExportXls('物资原材料消耗台账主表信息')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'wzxiahao:dr'" icon="import">导入</a-button>
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

        <span class="separate" slot="cement1" slot-scope="text,record" >
          <div v-for="(item,index) in record.wzconsumetaizhangDetailList">
             <a-divider style="margin:0;height:1px" />
             <div>{{item.name}}</div>
          </div>
        </span>
        <span class="separate" slot="cement2" slot-scope="text,record" >
          <div v-for="(item,index) in record.wzconsumetaizhangDetailList">
             <a-divider style="margin:0;height:1px" />
             <div>{{item.materialeName}}</div>
          </div>
        </span>
        <span class="separate" slot="cement3" slot-scope="text,record" >
          <div v-for="(item,index) in record.wzconsumetaizhangDetailList">
             <a-divider style="margin:0;height:1px" />
             <div>{{item.pici}}</div>
          </div>
        </span>
        <span class="separate" slot="cement4" slot-scope="text,record" >
          <div v-for="(item,index) in record.wzconsumetaizhangDetailList">
             <a-divider style="margin:0;height:1px" />
             <div>{{item.picizhong}}</div>
          </div>
        </span>
        <span class="separate" slot="cement5" slot-scope="text,record" >
          <div v-for="(item,index) in record.wzconsumetaizhangDetailList">
             <a-divider style="margin:0;height:1px" />
             <div>{{item.realityNumber}}</div>
          </div>
        </span>
        <span class="separate" slot="cement6" slot-scope="text,record" >
          <div v-for="(item,index) in record.wzconsumetaizhangDetailList">
             <a-divider style="margin:0;height:1px" />
             <div>{{item.cailiaoshengyut}}</div>
          </div>
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
          <a v-has="'wzxiahao:edit'" @click="handleEdit(record)">编辑</a>

<!--          <a-divider type="vertical" />-->
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'wzxiahao:add'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <wzconsumetaizhang-modal ref="modalForm" @ok="modalFormOk"></wzconsumetaizhang-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WzconsumetaizhangModal from './modules/WzconsumetaizhangModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import { userliaocangList } from '@api/api'
  import Vue from 'vue'

  export default {
    name: 'WzconsumetaizhangList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      WzconsumetaizhangModal,
      JSuperQuery,
    },
    data () {
      return {
        selectValue1:'',
        dictOption1:[],
        description: '物资原材料消耗台账主表信息管理页面',
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
            title:'所属组织机构',
            align:"center",
            dataIndex: 'sysOrgCode'
          },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'projectName'
          },
          {
            title:'材料消耗部位',
            align:"center",
            dataIndex: 'poureLocation'
          },
          {
            title:'砼消耗数量(m³)',
            align:"center",
            dataIndex: 'estimateNumber'
          },
          {
            title:'料仓名称',
            align:"center",
            scopedSlots: { customRender: 'cement1' },
          },
          {
            title:'材料名称',
            align:"center",
            scopedSlots: { customRender: 'cement2' },
          },
          {
            title:'检验批次',
            align:"center",
            scopedSlots: { customRender: 'cement3' },
          },
          {
            title:'批次库存(吨)',
            align:"center",
            scopedSlots: { customRender: 'cement4' },
          },
          {
            title:'材料消耗(吨)',
            align:"center",
            scopedSlots: { customRender: 'cement5' },
          },
          {
            title:'材料剩余(吨)',
            align:"center",
            scopedSlots: { customRender: 'cement6' },
          },
          {
            title:'砼标记',
            align:"center",
            dataIndex: 'strengthRank'
          },
          {
            title: '开始统计时间',
            align: 'center',
            dataIndex: 'starttim',
          },
          {
            title: '当前统计时间',
            align: 'center',
            dataIndex: 'endtim',
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
          list: "/sys/sysDepart/list1",
          delete: "/wzconsumetaizhang/wzconsumetaizhang/delete",
          deleteBatch: "/wzconsumetaizhang/wzconsumetaizhang/deleteBatch",
          exportXlsUrl: "/wzconsumetaizhang/wzconsumetaizhang/exportXls",
          importExcelUrl: "wzconsumetaizhang/wzconsumetaizhang/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    this.liaocangList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      liaocangList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        userliaocangList({
          sys_depart_orgcode:sys_depart_orgcode,
        }).then(res=>{
          this.dictOption1=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption1.push({text:re.name,value:re.guid})
          })
          //console.log(res)
        })
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'projectName',text:'工程名称',dictCode:''})
        fieldList.push({type:'string',value:'poureLocation',text:'材料消耗部位',dictCode:''})
        fieldList.push({type:'double',value:'estimateNumber',text:'砼消耗数量(m³)',dictCode:''})
        fieldList.push({type:'string',value:'strengthRank',text:'砼标记',dictCode:''})
        fieldList.push({type:'string',value:'sysOrgCode',text:'控制权限',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>