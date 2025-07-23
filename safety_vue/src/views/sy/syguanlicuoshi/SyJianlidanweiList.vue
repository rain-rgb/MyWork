<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
           <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料类型">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.cailiaoType" dictCode="nodetype"></j-dict-select-tag>
            </a-form-item>
          </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="指摘类型">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.zzType" dictCode="zzType"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.zzTime_begin" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="结束时间">
              <j-date placeholder="结束时间" v-model="queryParam.zzTime_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <!-- <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a> -->
            </span>
          </a-col>
       </a-row>
      </a-form>

    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <!-- <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button> -->
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('sy_guanlicuoshi')">导出</a-button> -->
      <!-- <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
      <!-- 高级查询区域 -->
      <!-- <j-super-query
        :fieldList="superFieldList"
        ref="superQueryModal"
        @handleSuperQuery="handleSuperQuery"
      ></j-super-query> -->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
          <img
            v-else
            :src="getImgView(text)"
            height="25px"
            alt=""
            style="max-width: 80px; font-size: 12px; font-style: italic"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
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

    <sy-guanlicuoshi-modal ref="modalForm" @ok="modalFormOk"></sy-guanlicuoshi-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import SyGuanlicuoshiModal from './modules/SyGuanlicuoshiModal'

export default {
  name: 'SyGuanlicuoshiList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    SyGuanlicuoshiModal,
  },
  data() {
    return {
      description: 'sy_guanlicuoshi管理页面',
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
          },
        },
        {
          title: '批次号',
          align: 'center',
          dataIndex: 'batchId',
        },
        {
          title: '材料类型',
          align: 'center',
          dataIndex: 'cailiaoType_dictText',
        },
        {
          title: '规格型号',
          align: 'center',
          dataIndex: 'guigexinghao',
        },
        {
          title: '料仓',
          align: 'center',
          dataIndex: 'liaocang',
        },
        //   {
        //     title:'设备号',
        //     align:"center",
        //     dataIndex: 'shebeiNo'
        //   },
        {
          title: '指摘人',
          align: 'center',
          dataIndex: 'zhizhairen',
        },

        {
          title: '被指摘监理单位',
          align: 'center',
          dataIndex: 'bzzJlDw',
        },

        {
          title: '部门',
          align: 'center',
          dataIndex: 'comment_dictText',
        },

        {
          title: '指摘类型',
          align: 'center',
          dataIndex: 'zzType_dictText',
        },
        {
          title: '指摘原因',
          align: 'center',
          dataIndex: 'zzReason',
        },
        {
          title: '检验委托单号',
          align: 'center',
          dataIndex: 'testCommissionedId',
        },
        {
          title: '指摘时间',
          align: 'center',
          dataIndex: 'zzTime',
          // customRender: function (text) {
          //   return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          // },
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/syguanlicuoshi/syGuanlicuoshi/list3',
        delete: '/syguanlicuoshi/syGuanlicuoshi/delete',
        deleteBatch: '/syguanlicuoshi/syGuanlicuoshi/deleteBatch',
        exportXlsUrl: '/syguanlicuoshi/syGuanlicuoshi/exportXls',
        importExcelUrl: 'syguanlicuoshi/syGuanlicuoshi/importExcel',
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList();
    

  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
  
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'batchId', text: '批次号' })
      fieldList.push({ type: 'string', value: 'cailiaoType', text: '材料类型' })
      fieldList.push({ type: 'string', value: 'guigexinghao', text: '规格型号' })
      fieldList.push({ type: 'string', value: 'liaocang', text: '料仓' })
      fieldList.push({ type: 'string', value: 'shebeiNo', text: '设备号' })
      fieldList.push({ type: 'string', value: 'zhizhairen', text: '指摘人' })
      fieldList.push({ type: 'string', value: 'bzzJlDw', text: '被指摘监理单位' })
      fieldList.push({ type: 'string', value: 'comment', text: '备注' })
      fieldList.push({ type: 'date', value: 'zzTime', text: '指摘时间' })
      fieldList.push({ type: 'string', value: 'zzType', text: '指摘类型' })
      fieldList.push({ type: 'string', value: 'zzReason', text: '指摘原因' })
      fieldList.push({ type: 'string', value: 'testCommissionedId', text: '检验委托单号' })
      this.superFieldList = fieldList
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>