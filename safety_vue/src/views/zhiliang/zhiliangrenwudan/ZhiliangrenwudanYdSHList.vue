<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="任务单编号">
              <a-input placeholder="请输入任务单编号" v-model="queryParam.code"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="计划生产时间">
              <j-date placeholder="开始生产日期" v-model="queryParam.productiontime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束生产日期" v-model="queryParam.productiontime_end" :showTime="true"
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
      <a-button @click="handleAdd" v-has="'zlrwdsh:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" v-has="'zlrwdsh:dc'" @click="handleExportXls('任务单（制梁）表信息')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" v-has="'zlrwdsh:dr'" icon="import">导入</a-button>
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
         <span slot="isdel" slot-scope="isdel, record">
          <a-tag color="blue" v-if="record.isdel==0">有效</a-tag>
          <a-tag color="red" v-if="record.isdel==1">无效</a-tag>
        </span>
        <span slot="status" slot-scope="status, record">
          <a-tag color="red" v-if="record.status==0">未审核</a-tag>
          <a-tag color="green" v-if="record.status==1">已通过</a-tag>
           <a-tag color="yellow" v-if="record.status==3">未通过</a-tag>
        </span>
        <span slot="code" slot-scope="code, record">
          <a-tag color="blue" >{{record.code}}</a-tag>
        </span>
<!--        <span slot="recipe" slot-scope="recipe, record">-->
<!--          <a-tag color="blue" >{{record.recipe}}</a-tag>-->
<!--        </span>-->
        <span slot="wiretiestatus" slot-scope="wiretiestatus, record">
          <a-tag color="blue" v-if="record.wiretiestatus == 0">未开始</a-tag>
          <a-tag color="yellow" v-if="record.wiretiestatus == 1">进行中</a-tag>
          <a-tag color="green" v-if="record.wiretiestatus == 2">已完成</a-tag>
        </span>
        <span slot="jiaozhustatus" slot-scope="jiaozhustatus, record">
          <a-tag color="blue" v-if="record.jiaozhustatus == 0">未开始</a-tag>
          <a-tag color="yellow" v-if="record.jiaozhustatus == 1">进行中</a-tag>
          <a-tag color="green" v-if="record.jiaozhustatus == 2">已完成</a-tag>
        </span>
        <span slot="jingzhistatus" slot-scope="jingzhistatus, record">
          <a-tag color="blue" v-if="record.jingzhistatus == 0">未开始</a-tag>
          <a-tag color="yellow" v-if="record.jingzhistatus == 1">进行中</a-tag>
          <a-tag color="green" v-if="record.jingzhistatus == 2">已完成</a-tag>
        </span>
        <span slot="zhengyangstatus1" slot-scope="zhengyangstatus1, record">
          <a-tag color="blue" v-if="record.zhengyangstatus1 == 0">未开始</a-tag>
          <a-tag color="yellow" v-if="record.zhengyangstatus1 == 1">进行中</a-tag>
          <a-tag color="green" v-if="record.zhengyangstatus1 == 2">已完成</a-tag>
        </span>
        <!--        <span slot="zhengyangstatus2" slot-scope="zhengyangstatus2, record">-->
        <!--          <a-tag color="blue" v-if="record.zhengyangstatus2 == 0">未开始</a-tag>-->
        <!--          <a-tag color="yellow" v-if="record.zhengyangstatus2 == 1">进行中</a-tag>-->
        <!--          <a-tag color="green" v-if="record.zhengyangstatus2 == 2">已完成</a-tag>-->
        <!--        </span>-->
        <span slot="zhanglastatus" slot-scope="zhanglastatus, record">
          <a-tag color="blue" v-if="record.zhanglastatus == 0">未开始</a-tag>
          <a-tag color="yellow" v-if="record.zhanglastatus == 1">进行中</a-tag>
          <a-tag color="green" v-if="record.zhanglastatus == 2">已完成</a-tag>
        </span>
        <span slot="fengduanstatus" slot-scope="fengduanstatus, record">
          <a-tag color="blue" v-if="record.fengduanstatus == 0">未开始</a-tag>
          <a-tag color="yellow" v-if="record.fengduanstatus == 1">进行中</a-tag>
          <a-tag color="green" v-if="record.fengduanstatus == 2">已完成</a-tag>
        </span>
        <!--        <span slot="tiliangstatus" slot-scope="tiliangstatus, record">-->
        <!--          <a-tag color="blue" v-if="record.tiliangstatus == 0">未开始</a-tag>-->
        <!--          <a-tag color="yellow" v-if="record.tiliangstatus == 1">进行中</a-tag>-->
        <!--          <a-tag color="green" v-if="record.tiliangstatus == 2">已完成</a-tag>-->
        <!--        </span>-->
        <span slot="cunliangstatus" slot-scope="cunliangstatus, record">
          <a-tag color="blue" v-if="record.cunliangstatus == 0">未存梁</a-tag>
          <a-tag color="yellow" v-if="record.cunliangstatus == 1">已存梁</a-tag>
          <a-tag color="green" v-if="record.cunliangstatus == 2">已提梁</a-tag>
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
          <a @click="showModal(record.id)">审核</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item >
                <a @click="handleEdit(record)" v-has="'zlrwdsh:edit'" >编辑</a>
              </a-menu-item>
              <a-menu-item v-has="'zlrwdsh:del'" >
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <ZhiliangBhzrenwudanShenHe :flag="visible" :id="id" @change="change"></ZhiliangBhzrenwudanShenHe>
    <zhiliangrenwudan-modal ref="modalForm" @ok="modalFormOk"></zhiliangrenwudan-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ZhiliangrenwudanModal from './modules/ZhiliangrenwudanModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import ZhiliangBhzrenwudanShenHe from '@views/zhiliang/zhiliangrenwudan/ZhiliangBhzrenwudanShenHe'

  export default {
    name: 'ZhiliangrenwudanYdSHList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ZhiliangBhzrenwudanShenHe,
      ZhiliangrenwudanModal,
      JSuperQuery,
    },
    data () {
      return {
        flag: 0,
        id:0,
        visible: false,
        description: '任务单（制梁）表信息管理页面',
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
            title:'审核状态',
            align:"center",
            dataIndex: 'status',
            key: 'status',
            scopedSlots: { customRender: 'status' },
          },
          {
            title:'所属组织机构',
            align:"center",
            dataIndex: 'sysOrgCode_dictText'
          },
          {
            title:'生产线',
            align:"center",
            dataIndex: 'station_dictText'
          },
          {
            title:'任务单编号',
            align:"center",
            dataIndex: 'code',
            key: 'code',
            scopedSlots: { customRender: 'code' },
          },
          // {
          //   title:'配合比编号',
          //   align:"center",
          //   dataIndex: 'recipe',
          //   key: 'recipe',
          //   scopedSlots: { customRender: 'recipe' },
          // },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'dattim',
          },
          {
            title:'计划生产时间',
            align:"center",
            dataIndex: 'productiontime',
          },
          // {
          //   title:'任务性质',
          //   align:"center",
          //   dataIndex: 'attribute'
          // },
          // {
          //   title:'2线配合比编号',
          //   align:"center",
          //   dataIndex: 'recipes'
          // },
          // {
          //   title:'合同信息',
          //   align:"center",
          //   dataIndex: 'contract'
          // },
          // {
          //   title:'客户名称',
          //   align:"center",
          //   dataIndex: 'customer'
          // },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'projname'
          },
          // {
          //   title:'工程类别',
          //   align:"center",
          //   dataIndex: 'projtype'
          // },
          // {
          //   title:'工程级别',
          //   align:"center",
          //   dataIndex: 'projgrade'
          // },
          // {
          //   title:'开工面积',
          //   align:"center",
          //   dataIndex: 'projarea'
          // },
          {
            title:'施工地址',
            align:"center",
            dataIndex: 'projadr'
          },
          {
            title:'运输距离',
            align:"center",
            dataIndex: 'distance'
          },
          {
            title:'施工部位',
            align:"center",
            dataIndex: 'conspos'
          },
          {
            title:'浇筑方式',
            align:"center",
            dataIndex: 'pour'
          },
          // {
          //   title:'产品种类',
          //   align:"center",
          //   dataIndex: 'variety'
          // },
          {
            title:'强度等级',
            align:"center",
            dataIndex: 'betlev'
          },
          // {
          //   title:'抗渗等级',
          //   align:"center",
          //   dataIndex: 'filter'
          // },
          // {
          //   title:'抗冻等级',
          //   align:"center",
          //   dataIndex: 'freeze'
          // },
          {
            title:'坍落度',
            align:"center",
            dataIndex: 'lands'
          },
          // {
          //   title:'水泥品种',
          //   align:"center",
          //   dataIndex: 'cement'
          // },
          // {
          //   title:'石子种类',
          //   align:"center",
          //   dataIndex: 'stone'
          // },
          // {
          //   title:'骨料粒径',
          //   align:"center",
          //   dataIndex: 'bnsize'
          // },
          // {
          //   title:'外加剂要求',
          //   align:"center",
          //   dataIndex: 'addliq'
          // },
          // {
          //   title:'技术要求',
          //   align:"center",
          //   dataIndex: 'request'
          // },
          {
            title:'搅拌时间',
            align:"center",
            dataIndex: 'mixlast'
          },
          {
            title:'任务方量',
            align:"center",
            dataIndex: 'mete'
          },
          {
            title:'浇注日期',
            align:"center",
            dataIndex: 'begtim',
          },
          {
            title:'截止日期',
            align:"center",
            dataIndex: 'endtim',
          },
          // {
          //   title:'调度人员',
          //   align:"center",
          //   dataIndex: 'attamper'
          // },
          // {
          //   title:'标识',
          //   align:"center",
          //   dataIndex: 'flag'
          // },
          // {
          //   title:'备注',
          //   align:"center",
          //   dataIndex: 'note'
          // },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy'
          },
          // {
          //   title:'是否删除 0未删除 1已删除',
          //   align:"center",
          //   dataIndex: 'isdel'
          // },
          // {
          {
            title:'钢筋绑扎状态',
            align:"center",
            dataIndex: 'wiretiestatus',
            scopedSlots: { customRender: 'wiretiestatus' },
          },
          {
            title:'浇筑状态',
            align:"center",
            dataIndex: 'jiaozhustatus',
            scopedSlots: { customRender: 'jiaozhustatus' },
          },
          {
            title:'收面静置状态',
            align:"center",
            dataIndex: 'jingzhistatus',
            scopedSlots: { customRender: 'jingzhistatus' },
          },
          {
            title:'养生状态',
            align:"center",
            dataIndex: 'zhengyangstatus1',
            scopedSlots: { customRender: 'zhengyangstatus1' },
          },
          // {
          //   title:'蒸养状态2',
          //   align:"center",
          //   dataIndex: 'zhengyangstatus2',
          //   scopedSlots: { customRender: 'zhengyangstatus2' },
          // },
          {
            title:'张拉状态',
            align:"center",
            dataIndex: 'zhanglastatus',
            scopedSlots: { customRender: 'zhanglastatus' },
          },
          {
            title:'封存状态',
            align:"center",
            dataIndex: 'fengcunstatus',
            scopedSlots: { customRender: 'fengcunstatus' },
          },
          {
            title:'存梁状态',
            align:"center",
            dataIndex: 'cunliangstatus',
            scopedSlots: { customRender: 'cunliangstatus' },
          },
          {
            title:'是否有效',
            align:"center",
            dataIndex: 'isdel',
            key: 'isdel',
            scopedSlots: { customRender: 'isdel' },
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
          list: "/zhiliangrenwudan/zhiliangrenwudan/ydshlist",
          delete: "/zhiliangrenwudan/zhiliangrenwudan/delete",
          deleteBatch: "/zhiliangrenwudan/zhiliangrenwudan/deleteBatch",
          exportXlsUrl: "/zhiliangrenwudan/zhiliangrenwudan/exportXls",
          importExcelUrl: "zhiliangrenwudan/zhiliangrenwudan/importExcel",

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
      change(data) {
        this.flag = data
      },
      showModal:function (id){
        this.visible=true;
        this.id=id;
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'int',value:'station',text:'生产线（0公用 1 第一生产线  2 第二生产线）',dictCode:''})
        fieldList.push({type:'string',value:'code',text:'任务单编号',dictCode:''})
        fieldList.push({type:'date',value:'dattim',text:'创建时间'})
        fieldList.push({type:'string',value:'attribute',text:'任务性质',dictCode:''})
        fieldList.push({type:'string',value:'recipe',text:'1线配合比编号',dictCode:''})
        fieldList.push({type:'string',value:'recipes',text:'2线配合比编号',dictCode:''})
        fieldList.push({type:'string',value:'contract',text:'合同信息',dictCode:''})
        fieldList.push({type:'string',value:'customer',text:'客户名称',dictCode:''})
        fieldList.push({type:'string',value:'projname',text:'工程名称',dictCode:''})
        fieldList.push({type:'string',value:'projtype',text:'工程类别',dictCode:''})
        fieldList.push({type:'string',value:'projgrade',text:'工程级别',dictCode:''})
        fieldList.push({type:'double',value:'projarea',text:'开工面积',dictCode:''})
        fieldList.push({type:'string',value:'projadr',text:'施工地址',dictCode:''})
        fieldList.push({type:'double',value:'distance',text:'运输距离',dictCode:''})
        fieldList.push({type:'string',value:'conspos',text:'施工部位',dictCode:''})
        fieldList.push({type:'string',value:'pour',text:'浇筑方式',dictCode:''})
        fieldList.push({type:'string',value:'variety',text:'产品种类',dictCode:''})
        fieldList.push({type:'string',value:'betlev',text:'强度等级',dictCode:''})
        fieldList.push({type:'string',value:'filter',text:'抗渗等级',dictCode:''})
        fieldList.push({type:'string',value:'freeze',text:'抗冻等级',dictCode:''})
        fieldList.push({type:'string',value:'lands',text:'坍落度',dictCode:''})
        fieldList.push({type:'string',value:'cement',text:'水泥品种',dictCode:''})
        fieldList.push({type:'string',value:'stone',text:'石子种类',dictCode:''})
        fieldList.push({type:'string',value:'bnsize',text:'骨料粒径',dictCode:''})
        fieldList.push({type:'string',value:'addliq',text:'外加剂要求',dictCode:''})
        fieldList.push({type:'string',value:'request',text:'技术要求',dictCode:''})
        fieldList.push({type:'double',value:'mixlast',text:'搅拌时间',dictCode:''})
        fieldList.push({type:'double',value:'mete',text:'任务方量',dictCode:''})
        fieldList.push({type:'date',value:'begtim',text:'浇注日期'})
        fieldList.push({type:'date',value:'endtim',text:'截止日期'})
        fieldList.push({type:'string',value:'attamper',text:'调度人员',dictCode:''})
        fieldList.push({type:'string',value:'flag',text:'标识',dictCode:''})
        fieldList.push({type:'string',value:'note',text:'备注',dictCode:''})
        fieldList.push({type:'string',value:'createBy',text:'创建人',dictCode:''})
        fieldList.push({type:'string',value:'sysOrgCode',text:'权限',dictCode:''})
        fieldList.push({type:'int',value:'isdel',text:'是否删除 0未删除 1已删除',dictCode:''})
        fieldList.push({type:'int',value:'status',text:'0 未审核  1已审核',dictCode:''})
        fieldList.push({type:'int',value:'jiaozhustatus',text:'浇筑状态',dictCode:''})
        fieldList.push({type:'int',value:'jingzhistatus',text:'收面静置状态',dictCode:''})
        fieldList.push({type:'int',value:'zhengyangstatus1',text:'蒸养状态1',dictCode:''})
        fieldList.push({type:'int',value:'zhengyangstatus2',text:'蒸养状态2',dictCode:''})
        fieldList.push({type:'int',value:'zhanglastatus',text:'张拉状态',dictCode:''})
        fieldList.push({type:'int',value:'tiliangstatus',text:'提梁状态',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>