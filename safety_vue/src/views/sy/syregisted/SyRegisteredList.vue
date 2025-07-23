<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
           <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="不合格类型">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.buhegetype" dictCode="buhegetype"></j-dict-select-tag>
            </a-form-item>
          </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="闭合状态">
              <j-dict-select-tag placeholder="请选择" v-model="queryParam.closestatus" dictCode="closestatus"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.time_begin" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="结束时间">
              <j-date placeholder="结束时间" v-model="queryParam.time_end" :showTime="true" dateFormat="YYYY-MM-DD HH:mm:ss" />
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
      <!-- <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('sy_registered')">导出</a-button>
      <a-upload
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

          <span slot="tags" slot-scope="tags, record">
          <a-tag color="red" v-if="record.closestatus === 0">未闭合</a-tag>
          <a-tag color="green" v-if="record.closestatus === 1">已闭合</a-tag>
   
       </span>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item v-has="'snjbz:cz'">
                <a @click="showmadel1(record)">闭合</a>
              </a-menu-item>
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

    <sy-registered-modal ref="modalForm" @ok="modalFormOk"></sy-registered-modal>
     <bihe-modal ref="modalForm1" @ok="modalFormOk"></bihe-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import SyRegisteredModal from './modules/SyRegisteredModal'
import biheModal from './bihemodules/biheModal'

export default {
  name: 'SyRegisteredList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    SyRegisteredModal,
    biheModal,
  },
  data() {
    return {
      description: 'sy_registered管理页面',
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
          title: '检测项目',
          align: 'center',
          dataIndex: 'checkproject_dictText',
        },
        {
          title: '责任人',
          align: 'center',
          dataIndex: 'owner',
        },
        {
          title: '不合格类型',
          align: 'center',
          dataIndex: 'buhegetype_dictText',
        },
   
        {
          title: '原因描述',
          align: 'center',
          dataIndex: 'reason',
        },
        {
          title: '罚款奖励金额数',
          align: 'center',
          dataIndex: 'amount',
        },
        {
          title: '订单所属组织机构',
          align: 'center',
          dataIndex: 'sysorgcode_dictText',
        },
     
         {
            title: '闭合状态',
            align: 'center',
            dataIndex: 'closestatus',
            key: 'closestatus',
            scopedSlots: { customRender: 'tags' },
          },
        {
          title: '闭合时间',
          align: 'center',
          dataIndex: 'closetime',
        },
        {
          title: '闭合人',
          align: 'center',
          dataIndex: 'closeren',
        },
        {
          title: '闭合措施',
          align: 'center',
          dataIndex: 'closesteps',
        },
        // {
        //   title: '登记人',
        //   align: 'center',
        //   dataIndex: 'registerren',
        // },
        // {
        //   title: '登记时间',
        //   align: 'center',
        //   dataIndex: 'registertime',
        // },
         {
          title: '时间',
          align: 'center',
          dataIndex: 'time',
          // customRender:function (text) {
          //   return !text?"":(text.length>10?text.substr(0,10):text)
          // }
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
        list: '/syRegistered/syRegistered/list',
        delete: '/syRegistered/syRegistered/delete',
        deleteBatch: '/syRegistered/syRegistered/deleteBatch',
        exportXlsUrl: '/syRegistered/syRegistered/exportXls',
        importExcelUrl: 'syRegistered/syRegistered/importExcel',
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
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'int', value: 'checkproject', text: '检测项目（0：水泥胶砂强度）' })
      fieldList.push({ type: 'string', value: 'owner', text: '责任人' })
      fieldList.push({ type: 'int', value: 'buhegetype', text: '不合格类型（0：主动；1：被动）' })
      fieldList.push({ type: 'date', value: 'time', text: '时间' })
      fieldList.push({ type: 'string', value: 'reason', text: '原因描述' })
      fieldList.push({ type: 'string', value: 'amount', text: '罚款奖励金额数' })
      fieldList.push({ type: 'string', value: 'sysorgcode', text: '订单所属组织机构' })
      this.superFieldList = fieldList
    },
     showmadel1: function (record) {
        if (record.closestatus == 0){

          this.$refs.modalForm1.add(record);
          this.$refs.modalForm1.process=0;
          this.$refs.modalForm1.title = "闭合";
          this.$refs.modalForm1.disableSubmit = false;

          // this.batchNo = record.id
          // this.flag = 2

        }else {
          this.$message.warning('已闭合，不能重复闭合！')
        }
      },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>