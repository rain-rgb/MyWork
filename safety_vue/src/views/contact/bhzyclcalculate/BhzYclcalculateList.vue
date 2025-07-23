<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="拌合站设备名称">
              <j-search-select-tag placeholder="请选择拌合站名称" v-model="queryParam.shebeiNo" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="统计时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.statisticsTime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.statisticsTime_end" :showTime="true"
                      dateFormat="YYYY-MM-DD"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('拌合站原材料核算')">导出</a-button>
      <!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
      <!--        <a-button type="primary" icon="import">导入</a-button>-->
      <!--      </a-upload>-->
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
        @change="handleTableChange">

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

    <bhz-yclcalculate-modal ref="modalForm" @ok="modalFormOk"></bhz-yclcalculate-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import BhzYclcalculateModal from './modules/BhzYclcalculateModal'
import { usershebeiList } from '@api/api'

export default {
  name: 'BhzYclcalculateList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    BhzYclcalculateModal
  },
  data() {
    return {
      description: 'bhz_yclcalculate管理页面',
      selectValue: '',
      dictOption: [],
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
          title: '设备名称',
          align: 'center',
          dataIndex: 'shebeiNo'
        },
        // {
        //   title:'库别',
        //   align:"center",
        //   dataIndex: 'kubie'
        // },
        // {
        //   title:'类别',
        //   align:"center",
        //   dataIndex: 'leibie'
        // },
        {
          title: '物资名称',
          align: 'center',
          dataIndex: 'wzName'
        },
        // {
        //   title:'型号',
        //   align:"center",
        //   dataIndex: 'xinghao'
        // },
        // {
        //   title:'规格',
        //   align:"center",
        //   dataIndex: 'guige'
        // },
        // {
        //   title:'辅助规格',
        //   align:"center",
        //   dataIndex: 'fuzhuGuige'
        // },
        // {
        //   title:'单位',
        //   align:"center",
        //   dataIndex: 'danwei'
        // },
        // {
        //   title:'上期末物资支出',
        //   align:"center",
        //   dataIndex: 'lastWzzc'
        // },
        // {
        //   title:'上期末系统消耗',
        //   align:"center",
        //   dataIndex: 'lastSysxh'
        // },
        // {
        //   title:'上期末节超(节+超-)',
        //   align:"center",
        //   dataIndex: 'lastJc'
        // },
        // {
        //   title:'本期物资支出',
        //   align:"center",
        //   dataIndex: 'thisWzzc'
        // },
        {
          title: '本期系统消耗',
          align: 'center',
          dataIndex: 'thisSysxh'
        },
        // {
        //   title:'本期节超(节+超-)',
        //   align:"center",
        //   dataIndex: 'thisJc'
        // },
        // {
        //   title:'开累物资支出',
        //   align:"center",
        //   dataIndex: 'klWzzc'
        // },
        // {
        //   title:'开累系统消耗',
        //   align:"center",
        //   dataIndex: 'klSysxh'
        // },
        // {
        //   title:'开累节超(节+超-)',
        //   align:"center",
        //   dataIndex: 'klJc'
        // },
        {
          title: '统计时间',
          align: 'center',
          dataIndex: 'statisticsTime'
        },
        // {
        //   title:'备注',
        //   align:"center",
        //   dataIndex: 'beizhu'
        // },
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
        list: '/bhzyclcalculate/bhzYclcalculate/listTongJi',
        delete: '/bhzyclcalculate/bhzYclcalculate/delete',
        deleteBatch: '/bhzyclcalculate/bhzYclcalculate/deleteBatch',
        exportXlsUrl: '/bhzyclcalculate/bhzYclcalculate/exportXls',
        importExcelUrl: 'bhzyclcalculate/bhzYclcalculate/importExcel',

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.shebeiList()
    this.getSuperFieldList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '0'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
      })
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'kubie', text: '库别' })
      fieldList.push({ type: 'string', value: 'leibie', text: '类别' })
      fieldList.push({ type: 'string', value: 'wzName', text: '物资名称' })
      fieldList.push({ type: 'string', value: 'xinghao', text: '型号' })
      fieldList.push({ type: 'string', value: 'guige', text: '规格' })
      fieldList.push({ type: 'string', value: 'fuzhuGuige', text: '辅助规格' })
      fieldList.push({ type: 'string', value: 'danwei', text: '单位' })
      fieldList.push({ type: 'string', value: 'lastWzzc', text: '上期末物资支出' })
      fieldList.push({ type: 'string', value: 'lastSysxh', text: '上期末系统消耗' })
      fieldList.push({ type: 'string', value: 'lastJc', text: '上期末节超(节+超-)' })
      fieldList.push({ type: 'string', value: 'thisWzzc', text: '本期物资支出' })
      fieldList.push({ type: 'string', value: 'thisSysxh', text: '本期系统消耗' })
      fieldList.push({ type: 'string', value: 'thisJc', text: '本期节超(节+超-)' })
      fieldList.push({ type: 'string', value: 'klWzzc', text: '开累物资支出' })
      fieldList.push({ type: 'string', value: 'klSysxh', text: '开累系统消耗' })
      fieldList.push({ type: 'string', value: 'klJc', text: '开累节超(节+超-)' })
      fieldList.push({ type: 'string', value: 'beizhu', text: '备注' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>