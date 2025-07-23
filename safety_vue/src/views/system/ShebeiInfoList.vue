<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">

        <a-row :gutter="24">

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备类型">
              <j-dict-select-tag placeholder="请选择设备类型" v-model="queryParam.sbtype" dictCode="sbtype"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备编号">
              <a-input placeholder="请输入设备编号" v-model="queryParam.sbjno"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备工作状态">
              <j-dict-select-tag placeholder="请选择设备工作状态" v-model="queryParam.status" dictCode="workstatus"/>
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
      <a-button @click="handleAdd" v-has="'shebei:add'" type="primary" icon="plus">新增</a-button>
      <a-button @click="printXls" v-has="'shebei:print'" type="primary" icon="printer">二维码打印</a-button>
      <a-button type="primary" icon="download" v-has="'shebei:dc'" @click="handleExportXls('设备信息表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" v-has="'shebei:dr'" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
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
        <a-tag color="orange" v-if="record.shebeiStatus == '0'">待审核</a-tag>
        <a-tag color="green" v-if="record.shebeiStatus == '1'">已审核</a-tag>
        <a-tag color="red" v-if="record.shebeiStatus == '2'">已驳回</a-tag>
         <a-tag color="grey" v-if="record.shebeiStatus == '3'">已退场</a-tag>
       </span>
        <span slot="tags1" slot-scope="tags, record">
          <a-tag color="yellow" v-if="record.status === 0">未安装</a-tag>
          <a-tag color="red" v-if="record.status === 1">未工作</a-tag>
          <a-tag color="orange" v-if="record.status === 2">延时</a-tag>
          <a-tag color="green" v-if="record.status === 3">正常工作</a-tag>
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
          <a v-has="'shebei:edit'" @click="handleEdit(record)">编辑</a>
          <a-divider v-has="'shebei:edit'" type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-if="record.shebeiStatus===0" v-has="'shebei:sh'">
                <a-popconfirm title="确定审核吗?" @confirm="() => handUploadStatus(record.id,1)">
                  <a>审核</a>
                </a-popconfirm>
              </a-menu-item>
              <!--              <a-menu-item v-if="record.shebeiStatus==1">-->
              <!--                <a-popconfirm title="确定驳回吗?" @confirm="() => handUploadStatus(record.id,2)">-->
              <!--                  <a>驳回</a>-->
              <!--                </a-popconfirm>-->
              <!--              </a-menu-item>-->
              <a-menu-item v-if="record.shebeiStatus===0" v-has="'shebei:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <shebei-info-modal ref="modalForm" @ok="modalFormOk"></shebei-info-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import ShebeiInfoModal from './modules/ShebeiInfoModal'
import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import JselectDqDepart from '@/components/jeecgbiz/JselectDqDepart.vue'
import { postAction, getAction } from '@api/manage'
import { pushdepartidShebei } from '@/mixins/pushdepartidShebei'
import { changearryOne } from '@/api/api'

export default {
  name: 'ShebeiInfoList',
  mixins: [JeecgListMixin, mixinDevice, pushdepartidShebei],
  components: {
    JDictSelectTag,
    ShebeiInfoModal,
    JSuperQuery,
    JselectDqDepart,
  },
  data() {
    return {
      description: '设备信息表管理页面',
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '创建人',
          align: 'center',
          dataIndex: 'createBy_dictText'
        },
        {
          title: '创建日期',
          align: 'center',
          dataIndex: 'createTime',
          customRender: function (text) {
            return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
          }
        },
        {
          title: '所属部门',
          align: 'center',
          dataIndex: 'sysOrgCode_dictText',

        },
        {
          title: '设备类型',
          align: 'center',
          dataIndex: 'sbtype_dictText',
        },
        {
          title: '设备编号',
          align: 'center',
          dataIndex: 'sbjno'
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'sbname'
        },
        {
          title: '设备简称',
          align: 'center',
          dataIndex: 'sbjsimplename'
        },
        {
          title: '设备审核状态',
          align: 'center',
          dataIndex: 'shebeiStatus',
          key: 'shebeiStatus',
          scopedSlots: { customRender: 'tags' },
        },
        {
          title: '设备状态',
          align: 'center',
          dataIndex: 'status',
          key: 'status',
          scopedSlots: { customRender: 'tags1' },
        },
        {
          title: '更新人',
          align: 'center',
          dataIndex: 'updateBy_dictText'
        },
        {
          title: '更新日期',
          align: 'center',
          dataIndex: 'updateTime',
          customRender: function (text) {
            return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
          }
        },
        {
          title: '审核人',
          align: 'center',
          dataIndex: 'reviewBy_dictText'
        },
        {
          title: '审核日期',
          align: 'center',
          dataIndex: 'reviewTime',
          customRender: function (text) {
            return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
          }
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
        list: '/system/shebeiInfo/list',
        delete: '/system/shebeiInfo/delete',
        deleteBatch: '/system/shebeiInfo/deleteBatch',
        exportXlsUrl: '/system/shebeiInfo/exportXls',
        importExcelUrl: 'system/shebeiInfo/importExcel',
        handBack: '/system/shebeiInfo/backShebei',
        printUrl: 'jmreport/view/645419776666365952'
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    // changearryOneshebei(){
    //   changearryOne().then(res=>{//根据当前用户选择的组织机构改变其所拥有的权限的设备信息
    //     console.log("获取当前用户下的设备信息")
    //   })
    // },
    // routeReload() {//选择全局组织机构之后刷新当前的右侧页面  解决 没这个方法之前点击之后缓存数据不更新
    //   this.reloadFlag = false
    //   let ToggleMultipage = 'ToggleMultipage'
    //   this.$store.dispatch(ToggleMultipage, false)
    //   this.$nextTick(() => {
    //     this.$store.dispatch(ToggleMultipage, true)
    //     this.reloadFlag = true
    //   })
    //   console.log("刷新页面")
    // },
    handUploadStatus(e, a) {
      postAction(this.url.handBack, {
        id: e,
        shebeiStatus: a
      }).then(res => {
        if (res.success) {
          this.$message.success(res.message)
          this.changearryOneshebei()
        } else {
          this.$message.error(res.message)
          this.changearryOneshebei()
        }
      })
      //this.changearryOneshebei();
      //this.getSuperFieldList();
      this.routeReload()
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'sel_user', value: 'createBy', text: '创建人' })
      fieldList.push({ type: 'date', value: 'createTime', text: '创建日期' })
      fieldList.push({ type: 'sel_user', value: 'updateBy', text: '更新人' })
      fieldList.push({ type: 'date', value: 'updateTime', text: '更新日期' })
      fieldList.push({ type: 'sel_depart', value: 'sysOrgCode', text: '所属部门' })
      fieldList.push({ type: 'int', value: 'sbtype', text: '设备类型', dictCode: 'sbtype' })
      fieldList.push({ type: 'string', value: 'sbjno', text: '设备编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sbname', text: '设备名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sbjsimplename', text: '设备简称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shebei_status', text: '设备简称', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>