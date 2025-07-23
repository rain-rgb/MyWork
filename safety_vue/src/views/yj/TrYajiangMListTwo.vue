<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.yjsbbh" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="是否完成">
              <j-dict-select-tag
                placeholder="请选择是否完成"
                v-model="queryParam.status"
                dictCode="mstatus"
              ></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" v-has="'tryj:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'tryj:dc'" icon="download" @click="handleExportXls('压浆主表信息')"
        >导出</a-button
      >
      <a-upload
        name="file"
        v-has="'tryj:dr'"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" icon="import">导入</a-button>
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
        :rowKey="(record, index) => index"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <span v-if="record.hege != '1'&&record.hege != null" slot="status" slot-scope="status, record">
          <a-tag color="green" v-if="record.overproof_status == '20'">已闭合</a-tag>
          <a-tag color="red" v-if="record.overproof_status == '0'">未闭合</a-tag>
        </span>
        <span slot="hege" slot-scope="hege, record">
          <a-tag color="green" v-if="record.hege == '1'">合格</a-tag>
          <a-tag color="red" v-if="record.hege == '0'">不合格</a-tag>
        </span>
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
          <a v-has="'tryj:edit'" @click="handleEdit(record)">编辑</a>
          <a @click="showmadel1(record.syjid)">详情</a>
          <a-divider type="vertical" v-has="'tryj:edit'" />
          <!--          <a-dropdown>-->
          <!--            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>-->
          <!--            <a-menu slot="overlay">-->
          <!--              <a-menu-item>-->
          <!--                <a @click="handleDetail(record)">详情</a>-->
          <!--              </a-menu-item>-->
          <!--              <a-menu-item>-->
          <!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
          <!--                  <a>删除</a>-->
          <!--                </a-popconfirm>-->
          <!--              </a-menu-item>-->
          <!--            </a-menu>-->
          <!--          </a-dropdown>-->
        </span>
      </a-table>
    </div>
    <TrYajiangMSModal ref="TrYajiangMSModal"></TrYajiangMSModal>
    <tr-yajiang-m-modal ref="modalForm" @ok="modalFormOk"></tr-yajiang-m-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import TrYajiangMModal from './modules/TrYajiangMModal'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { usershebeiList } from '@api/api'
import TrYajiangMSModal from '@views/yj/modules/TrYajiangMSModal'
import Vue from 'vue'
export default {
  name: 'TrYajiangMList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    TrYajiangMModal,
    JSuperQuery,
    TrYajiangMSModal,
  },
  data() {
    return {
      dictOption: [],
      selectValue: '',
      description: '压浆主表信息管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },

        {
          title: '标段',
          align: 'center',
          dataIndex: 'departName',
          customRender: (text, record, index) => {
            let val = text ? text : 0
            return this.columnsInit(val, index, 3)
          },
        },
        // {
        //   title: '预制场',
        //   align: 'center',
        //   dataIndex: 'yjsj'
        // },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'shebeiNo',
          customRender: (text, record, index) => {
            let val = text ? text : 0
            return this.columnsInit(val, index, 3)
          },
        },
        {
          title: '工程部位',
          align: 'center',
          dataIndex: 'gcbw',
          customRender: (text, record, index) => {
            let val = text ? text : 0
            return this.columnsInit(val, index, 3)
          },
        },
        {
          title: '孔道',
          align: 'center',
          dataIndex: 'kongdao',
        },
        {
          title: '压浆时间',
          align: 'center',
          dataIndex: 'yajiangsj',
        },
        {
          title: '设计压浆量（L）',
          align: 'center',
          dataIndex: 'lljl',
        },
        {
          title: '实际压浆量（L）',
          align: 'center',
          dataIndex: 'jinjiangshu',
        },
        {
          title: '要求压力（MPA）',
          align: 'center',
          dataIndex: 'sjyl',
        },
        {
          title: '实际压力（MPA）',
          align: 'center',
          dataIndex: 'jinjiangyal',
        },
        {
          title: '超标等级',
          align: 'center',
          dataIndex: 'hege',
          scopedSlots: { customRender: 'hege' },
        },
        {
          title: '闭合情况',
          align: 'center',
          dataIndex: 'overproof_status',
          scopedSlots: { customRender: 'status' },
        },
        {
          title: '闭合时间',
          align: 'center',
          dataIndex: 'bhtime',
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
       ipagination: {
        current: 1,
        pageSize: 9,
        pageSizeOptions: ['9', '18', '27'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      //  isorter: {
      //   column: 'yjsj',
      //   order: 'desc',
      // },
      url: {
        list: '/yajiangm/trYajiangM/newlist',
        delete: '/yajiangm/trYajiangM/delete',
        deleteBatch: '/yajiangm/trYajiangM/deleteBatch',
        exportXlsUrl: '/yajiangm/trYajiangM/exportXls',
        importExcelUrl: 'yajiangm/trYajiangM/importExcel',
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
    showmadel1(syjid) {
      this.$refs.TrYajiangMSModal.title = '详情'
      this.$refs.TrYajiangMSModal.call(syjid)
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '10',
      }).then((res) => {
        //console.log(res)
        this.dictOption = []
        let result = res.result
        result.forEach((re) => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
      })
    },
    initDictConfig() {},
    columnsInit(text, index, num) {
      const obj = {
        children: text,
        attrs: {},
      }
      if (index === 0 || index % num === 0) {
        obj.attrs.rowSpan = num
      } else {
        obj.attrs.rowSpan = 0
      }
      return obj
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>