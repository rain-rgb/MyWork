<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="强度等级">
              <j-dict-select-tag placeholder="请选择强度等级" v-model="queryParam.betlev"
                                 dictCode="betlev"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="任务单编号">
              <a-input placeholder="请输入任务单编号" v-model="queryParam.code"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="施工部位">
              <a-input placeholder="请输入施工部位" v-model="queryParam.conspos1" @change="conspos1"></a-input>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item  label="是否推送质检资料">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.iszjzl"
                                 dictCode="iszjzl"></j-dict-select-tag>
            </a-form-item>

          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="创建时间范围">
              <j-date placeholder="开始日期" v-model="queryParam.dattim_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束日期" v-model="queryParam.dattim_end" :showTime="true"
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

      <a-button @click="handleAdd" v-has="'renwudan:add'" type="primary" :disabled="addrenwud" icon="plus">新增任务单</a-button>
      <a-button @click="printXls" v-has="'renwudan:print'" type="primary" icon="printer">打印</a-button>
      <a-button  v-has="'renwudan:dc'" type="primary" icon="download" @click="handleExportXls('任务单浇筑令')">导出</a-button>
      <a-upload name="file" v-has="'renwudan:dr'" :showUploadList="false" :multiple="false" :headers="tokenHeader"
                :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除任务单</a-menu-item>-->
      <!--        </a-menu>-->
      <!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
      <!--      </a-dropdown>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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
        <span slot="tags" slot-scope="tags, record">
          <a-tag color="blue" >{{record.code}}</a-tag>
        </span>
<!--        <span slot="recipe" slot-scope="recipe, record">-->
<!--          <a-tag color="blue" >{{record.recipe}}</a-tag>-->
<!--        </span>-->
<!--        <span slot="recipes" slot-scope="recipes, record">-->
<!--          <a-tag color="blue" >{{record.recipes}}</a-tag>-->
<!--        </span>-->
        <span slot="iszjzl" slot-scope="iszjzl, record">
           <a-tag color="grey" v-if="record.iszjzl==0">未推送</a-tag>
          <a-tag color="green" v-if="record.iszjzl==1">推送成功</a-tag>
          <a-tag color="red" v-if="record.iszjzl==2">推送失败</a-tag>
        </span>
        <span slot="status" slot-scope="status, record">
<!--          <a-tag color="red" v-if="record.status==0">未审核</a-tag>-->
          <a-tag color="red" v-if="record.status==0">待配料</a-tag>
           <a-tag color="green" v-if="record.status==1">已通过</a-tag>
           <a-tag color="blue" v-if="record.status==3">已配料</a-tag>
           <a-tag color="yellow" v-if="record.status==4">生产中</a-tag>
           <a-tag color="green" v-if="record.status==5">已完成</a-tag>
          <a-tag color="red" v-if="record.status==6">滞后</a-tag>
        </span>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
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

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'renwudan:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <bhzrenwudan-modal ref="modalForm" @ok="modalFormOk"></bhzrenwudan-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import BhzrenwudanModal from './modules/BhzrenwudanModal'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { getAction } from '@api/manage'

export default {
  name: 'BhzrenwudanList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    BhzrenwudanModal,
    JSuperQuery,
  },
  data() {
    return {
      description: '任务单浇筑令管理页面',
      addrenwud:false,
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title:'状态',
          align:"center",
          dataIndex: 'status',
          key: 'status',
          scopedSlots: { customRender: 'status' },
        },
        {
          title: '所属组织机构',//（0公用 1 第一生产线  2 第二生产线）
          align: 'center',
          dataIndex: 'sysOrgCode_dictText'
        },
        {
          title: '生产线',//（0公用 1 第一生产线  2 第二生产线）
          align: 'center',
          dataIndex: 'station_dictText'
        },
        {
          title: '任务单编号',
          align: 'center',
          dataIndex: 'code',
          key: 'code',
          scopedSlots: { customRender: 'tags' },
        },
        {
          title: '浇筑日期',
          align: 'center',
          dataIndex: 'begtim',
          // customRender: function (text) {
          //   return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
          // }
        },
        // {
        //   title: '任务性质',
        //   align: 'center',
        //   dataIndex: 'attribute'
        // },
        // {
        //   title: '1线配合比编号',
        //   align: 'center',
        //   dataIndex: 'recipe',
        //   key: 'recipe',
        //   scopedSlots: { customRender: 'recipe' },
        // },
        // {
        //   title: '2线配合比编号',
        //   align: 'center',
        //   dataIndex: 'recipes',
        //   key: 'recipes',
        //   scopedSlots: { customRender: 'recipes' },
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
          title: '工程名称',
          align: 'center',
          dataIndex: 'projname'
        },
        // {
        //   title: '工程类别',
        //   align: 'center',
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
        // {
        //   title:'施工地址',
        //   align:"center",
        //   dataIndex: 'projadr'
        // },
        // {
        //   title:'运输距离',
        //   align:"center",
        //   dataIndex: 'distance'
        // },
        {
          title: '施工部位',
          align: 'center',
          dataIndex: 'conspos'
        },
        {
          title: '浇筑方式',
          align: 'center',
          dataIndex: 'pour'
        },
        // {
        //   title:'产品种类',
        //   align:"center",
        //   dataIndex: 'variety'
        // },
        {
          title: '强度等级',
          align: 'center',
          dataIndex: 'betlev'
        },
        // {
        //   title: '抗渗等级',
        //   align: 'center',
        //   dataIndex: 'filter'
        // },
        // {
        //   title: '抗冻等级',
        //   align: 'center',
        //   dataIndex: 'freeze'
        // },
        {
          title: '坍落度',
          align: 'center',
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
        // {
        //   title: '搅拌时间',
        //   align: 'center',
        //   dataIndex: 'mixlast'
        // },
        {
          title: '任务方量',
          align: 'center',
          dataIndex: 'mete'
        },
        // {
        //   title: '浇注日期',
        //   align: 'center',
        //   dataIndex: 'begtim',
        //   customRender: function (text) {
        //     return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
        //   }
        // },
        // {
        //   title: '截止日期',
        //   align: 'center',
        //   dataIndex: 'endtim',
        //   customRender: function (text) {
        //     return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
        //   }
        // },
        // {
        //   title: '调度人员',
        //   align: 'center',
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
          title: '创建人',
          align: 'center',
          dataIndex: 'createBy_dictText'
        },
        // {
        //   title:'权限',
        //   align:"center",
        //   dataIndex: 'sysOrgCode'
        // },
        // {
        //   title:'是否删除 0未删除 1已删除',
        //   align:"center",
        //   dataIndex: 'isdel'
        // },
        {
          title:'推送质保资料',
          align:"center",
          dataIndex: 'iszjzl',
          key: 'iszjzl',
          scopedSlots: { customRender: 'iszjzl' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/system/bhzrenwudan/list',
        delete: '/system/bhzrenwudan/delete',
        deleteBatch: '/system/bhzrenwudan/deleteBatch',
        exportXlsUrl: '/system/bhzrenwudan/exportXls',
        importExcelUrl: 'system/bhzrenwudan/importExcel',
        printUrl:'jmreport/view/564669579202363392',
        controls:'/hntbhz/bhzCementBase/listCo'

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
    this.addrenwuControl()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    addrenwuControl:function () {
      getAction(this.url.controls,{id_begin: 8152691}).then((res) => {
        if (res.success) {
          console.log(res.result)
          if(res.result.coo>0) {
            this.addrenwud = true
            this.$message.info("生产数据两天前存在未使用浇筑令的情况，请及时填报原因进行审批")
          }
        } else {
          this.$message.error(res.message)
        }
      })
    },
    conspos1:function () {
      this.queryParam.conspos = '*'+this.queryParam.conspos1+'*'
    },
    printUrl:function (){
      return `${window._CONFIG['domianURL']}/${this.url.printUrl}`
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'int', value: 'station', text: '生产线（0公用 1 第一生产线  2 第二生产线）', dictCode: '' })
      fieldList.push({ type: 'string', value: 'code', text: '任务单编号', dictCode: '' })
      fieldList.push({ type: 'date', value: 'dattim', text: '创建时间' })
      fieldList.push({ type: 'string', value: 'attribute', text: '任务性质', dictCode: '' })
      fieldList.push({ type: 'string', value: 'recipe', text: '1线配合比编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'recipes', text: '2线配合比编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'contract', text: '合同信息', dictCode: '' })
      fieldList.push({ type: 'string', value: 'customer', text: '客户名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'projname', text: '工程名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'projtype', text: '工程类别', dictCode: '' })
      fieldList.push({ type: 'string', value: 'projgrade', text: '工程级别', dictCode: '' })
      fieldList.push({ type: 'double', value: 'projarea', text: '开工面积', dictCode: '' })
      fieldList.push({ type: 'string', value: 'projadr', text: '施工地址', dictCode: '' })
      fieldList.push({ type: 'double', value: 'distance', text: '运输距离', dictCode: '' })
      fieldList.push({ type: 'string', value: 'conspos', text: '施工部位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'pour', text: '浇筑方式', dictCode: '' })
      fieldList.push({ type: 'string', value: 'variety', text: '产品种类', dictCode: '' })
      fieldList.push({ type: 'string', value: 'betlev', text: '强度等级', dictCode: '' })
      fieldList.push({ type: 'string', value: 'filter', text: '抗渗等级', dictCode: '' })
      fieldList.push({ type: 'string', value: 'freeze', text: '抗冻等级', dictCode: '' })
      fieldList.push({ type: 'string', value: 'lands', text: '坍落度', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cement', text: '水泥品种', dictCode: '' })
      fieldList.push({ type: 'string', value: 'stone', text: '石子种类', dictCode: '' })
      fieldList.push({ type: 'string', value: 'bnsize', text: '骨料粒径', dictCode: '' })
      fieldList.push({ type: 'string', value: 'addliq', text: '外加剂要求', dictCode: '' })
      fieldList.push({ type: 'string', value: 'request', text: '技术要求', dictCode: '' })
      fieldList.push({ type: 'double', value: 'mixlast', text: '搅拌时间', dictCode: '' })
      fieldList.push({ type: 'double', value: 'mete', text: '任务方量', dictCode: '' })
      fieldList.push({ type: 'date', value: 'begtim', text: '浇注日期' })
      fieldList.push({ type: 'date', value: 'endtim', text: '截止日期' })
      fieldList.push({ type: 'string', value: 'attamper', text: '调度人员', dictCode: '' })
      fieldList.push({ type: 'string', value: 'flag', text: '标识', dictCode: '' })
      fieldList.push({ type: 'string', value: 'note', text: '备注', dictCode: '' })
      fieldList.push({ type: 'string', value: 'createBy', text: '创建人', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sysOrgCode', text: '权限', dictCode: '' })
      fieldList.push({ type: 'int', value: 'isdel', text: '是否删除 0未删除 1已删除', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>