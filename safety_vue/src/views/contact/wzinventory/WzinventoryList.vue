<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="库别">
              <j-search-select-tag placeholder="请选择库别" v-model="queryParam.kubie" :dictOptions="dictOption1"
                                   @change="getLiebie">
              </j-search-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="类别">
              <j-search-select-tag placeholder="请选择类别" v-model="queryParam.leibie" :dictOptions="dictOption2">
              </j-search-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label=物资名称>
              <a-input placeholder="请输入物资名称" v-model="queryParam.wzName"></a-input>
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
      <a-button type="primary" icon="download" @click="handleExportXls('物资盘点登记记录')">导出</a-button>
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

    <wzinventory-modal ref="modalForm" @ok="modalFormOk"></wzinventory-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import WzinventoryModal from './modules/WzinventoryModal'
import { getAction } from '@api/manage'

export default {
  name: 'WzinventoryList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    WzinventoryModal
  },
  data() {
    return {
      description: 'wzinventory管理页面',
      dictOption1: [],
      dictOption2: [],
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
          title: '库别',
          align: 'center',
          dataIndex: 'kubie'
        },
        {
          title: '类别',
          align: 'center',
          dataIndex: 'leibie'
        },
        {
          title: '物资名称',
          align: 'center',
          dataIndex: 'wzName'
        },
        {
          title: '型号',
          align: 'center',
          dataIndex: 'xinghao'
        },
        {
          title: '规格',
          align: 'center',
          dataIndex: 'guige'
        },
        {
          title: '辅助规格',
          align: 'center',
          dataIndex: 'fuzhuguige'
        },
        {
          title: '存料地点',
          align: 'center',
          dataIndex: 'cunliaodi'
        },
        {
          title: '单位',
          align: 'center',
          dataIndex: 'danwei'
        },
        {
          title: '盘点数量',
          align: 'center',
          dataIndex: 'pdnum'
        },
        // {
        //   title: '账期',
        //   align: 'center',
        //   dataIndex: 'zhangqi'
        // },
        {
          title: '计算依据',
          align: 'center',
          dataIndex: 'jisuanyiju'
        },
        {
          title: '备注',
          align: 'center',
          dataIndex: 'beizhu'
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
        list: '/inventory/wzinventory/list',
        delete: '/inventory/wzinventory/delete',
        deleteBatch: '/inventory/wzinventory/deleteBatch',
        exportXlsUrl: '/inventory/wzinventory/exportXls',
        importExcelUrl: 'inventory/wzinventory/importExcel',
        getKubie: '/wzcailiaonamedictall/wzcailiaonamedictAll/getKubie',
        getType: '/wzcailiaonamedictall/wzcailiaonamedictAll/getTypeNode',
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
    this.getKubie1()
    this.getLiebie()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    getKubie1: function () {
      var params = {}
      getAction(this.url.getKubie, params).then(res => {
        this.dictOption1 = []
        let result = res.result
        result.forEach(re => {
          this.dictOption1.push({ text: re.parentNode, value: re.parentNode })
        })
      })
    },
    getLiebie: function () {
      var params = {
        parentnode: this.queryParam.kubie
      }
      getAction(this.url.getType, params).then(res => {
        this.dictOption2 = []
        let result = res.result
        result.forEach(re => {
          this.dictOption2.push({ text: re.chandi, value: re.chandi })
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
      fieldList.push({ type: 'string', value: 'fuzhuguige', text: '辅助规格' })
      fieldList.push({ type: 'string', value: 'cunliaodi', text: '存料地点' })
      fieldList.push({ type: 'string', value: 'danwei', text: '单位' })
      fieldList.push({ type: 'string', value: 'pdnum', text: '盘点数量' })
      fieldList.push({ type: 'string', value: 'zhangqi', text: '账期' })
      fieldList.push({ type: 'string', value: 'jisuanyiju', text: '计算依据' })
      fieldList.push({ type: 'string', value: 'beizhu', text: '备注' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>