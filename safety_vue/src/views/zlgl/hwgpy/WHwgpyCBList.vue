<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24"> </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.customer" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="是否合格">
              <j-dict-select-tag
                placeholder="请选择是否合格"
                v-model="queryParam.result"
                dictCode="over_level"
              ></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date
                placeholder="开始时间范围"
                v-model="queryParam.datatime_begin"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束时间范围"
                v-model="queryParam.datatime_end"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 操作按钮区域 -->
    <!-- <div class="table-operator">
      <a-button type="primary" icon="download" @click="handleExportXls('w_hwgpy')">导出</a-button>
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
    </div> -->

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
      >
        <span slot="tags" slot-scope="tags, record">
          <a-tag color="green" v-if="record.result == '0'">匹配</a-tag>

          <a-tag color="red" v-if="record.result == '1'">不匹配</a-tag>
        </span>
        <span slot="tags1" slot-scope="tags1, record">
        <a-tag color="red" v-if="record.overproofStatus === 0">未处理</a-tag>
        <a-tag color="orange" v-if="record.overproofStatus === 10">监理核查中</a-tag>
        <a-tag color="green" v-if="record.overproofStatus === 20">已闭合</a-tag>
        <a-tag color="red" v-if="record.overproofStatus === 30">监理驳回</a-tag>
        <a-tag color="yellow" v-if="record.overproofStatus === 40">指挥部核查中</a-tag>
        <a-tag color="red" v-if="record.overproofStatus === 60">指挥部驳回</a-tag>
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
          <a v-has="'hntsyj:edit'" @click="handleEdit(record)">编辑</a>
          <a @click="detail(record)">详情</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay" v-show="record.overproofStatus !== 20">
              <a-menu-item
                v-show="record.overproofStatus === 0 || record.overproofStatus === 30 || record.overproofStatus === 60"
              >
                <a @click="showmadelchuzhi(record)">施工处置</a>
              </a-menu-item>
              <a-menu-item v-show="record.overproofStatus === 10">
                <a @click="showmadelshenhe(record)">监理审核</a>
              </a-menu-item>
              <a-menu-item v-show="record.overproofStatus === 40">
                <a @click="showmadelshenpi(record)">指挥部核查</a>
              </a-menu-item>
              <!-- <a-menu-item v-has="'hntsyj:del'">
              <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                <a>删除</a>
              </a-popconfirm>
            </a-menu-item> -->
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <chu-zhi-two ref="chuzhi" @change="change"></chu-zhi-two>
    <shen-he-two ref="shenhe" @change="change"></shen-he-two>
    <shen-pi ref="zhbshenpi" @change="change"></shen-pi>
    <detail ref="detail"></detail>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import WHwgpyModal from './modules/WHwgpyModal'
import ChuZhiTwo from './cbcz/ChuZhiTwo'
import ShenHeTwo from './cbcz/ShenHeTwo'
import ShenPi from './cbcz/ShenPi'
import detail from './cbcz/detail'
import Vue from 'vue'
import { usershebeiList } from '@api/api'

export default {
  name: 'WHwgpyList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    WHwgpyModal,
    ChuZhiTwo,
    ShenHeTwo,
    ShenPi,
    detail,
  },
  data() {
    return {
      dictOption: [],
      description: 'w_hwgpy管理页面',
      queryParam: { result: 1 },

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
          title: '设备归属',
          align: 'center',
          dataIndex: 'customer',
        },
        {
          title: '项目id',
          align: 'center',
          dataIndex: 'projectid',
        },
        {
          title: '项目名称',
          align: 'center',
          dataIndex: 'projectname',
        },
        {
          title: '沥青种类',
          align: 'center',
          dataIndex: 'product',
        },
        {
          title: '车号',
          align: 'center',
          dataIndex: 'grade',
        },
        {
          title: '标段',
          align: 'center',
          dataIndex: 'source',
        },
        {
          title: '匹配情况',
          align: 'center',
          dataIndex: 'result',
          scopedSlots: { customRender: 'tags' },
        },
        {
          title: '试验时间',
          align: 'center',
          dataIndex: 'datetime',
          customRender: function (text) {
            return !text ? '' : text.length > 10 ? text.substr(0, 10) : text
          },
        },
        {
          title: '试验种类；',
          align: 'center',
          dataIndex: 'type',
        },
        {
          title: '通过率',
          align: 'center',
          dataIndex: 'access',
        },
        {
          title: 'Sbs掺量',
          align: 'center',
          dataIndex: 'sbsaccess',
        },
        // {
        //   title: '光谱图文件',
        //   align: 'center',
        //   dataIndex: 'lighfileString',
        // },
        {
          title: 'isdj',
          align: 'center',
          dataIndex: 'isdj',
        },
        {
          title: '预警原因',
          align: 'center',
          dataIndex: 'overReason',
        },
         {
          title: '处理状态',
          align: 'center',
          dataIndex: 'overproofStatus',
          scopedSlots: { customRender: 'tags1' },
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
        list: '/whwgpy/wHwgpy/list',
        delete: '/whwgpy/wHwgpy/delete',
        deleteBatch: '/whwgpy/wHwgpy/deleteBatch',
        exportXlsUrl: '/whwgpy/wHwgpy/exportXls',
        importExcelUrl: 'whwgpy/wHwgpy/importExcel',
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
    this.shebeiList()
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
        sbtypes: '82',
      }).then((res) => {
        // this.dictOption=[];
        let result = res.result
        console.log(result)
        result.forEach((re) => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
          // console.log('+++++++++++'+re.sbname)
        })
        //console.log(res)
      })
    },
    detail: function (record) {
      this.$refs.detail.title = '详情'
      this.$refs.detail.callchuzhishenhe(record)
    },
    change(type) {
      if (type) {
        this.loadData()
      }
    },

    showmadelchuzhi(record) {
      console.log(record, 'record')
      this.$refs.chuzhi.showModal(record)
    },
    showmadelshenhe(record) {
      this.$refs.shenhe.showModal(record)
    },
    showmadelshenpi(record) {
      this.$refs.zhbshenpi.showModal(record)
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'customer', text: '设备归属' })
      fieldList.push({ type: 'int', value: 'projectid', text: '项目id' })
      fieldList.push({ type: 'string', value: 'projectname', text: '项目名称' })
      fieldList.push({ type: 'string', value: 'product', text: '沥青种类' })
      fieldList.push({ type: 'string', value: 'grade', text: '车号' })
      fieldList.push({ type: 'string', value: 'source', text: '标段' })
      fieldList.push({ type: 'int', value: 'result', text: '0代表匹配，1代表不匹配' })
      fieldList.push({ type: 'date', value: 'datetime', text: '试验的时间yyyy-MM-dd HH:mm:ss' })
      fieldList.push({ type: 'int', value: 'type', text: '试验种类‘1’代表普通沥青;’2’改性沥青；' })
      fieldList.push({ type: 'string', value: 'access', text: '通过率' })
      fieldList.push({ type: 'string', value: 'sbsaccess', text: 'Sbs掺量' })
      fieldList.push({ type: 'byte', value: 'lighfile', text: '光谱图文件 指定文件格式csv' })
      fieldList.push({ type: 'int', value: 'isdj', text: 'isdj' })
      fieldList.push({ type: 'string', value: 'overReason', text: '预警原因' })
      fieldList.push({ type: 'int', value: 'overproofStatus', text: '审核状态' })
      this.superFieldList = fieldList
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>