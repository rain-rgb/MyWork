<template>
  <a-card :bordered="false">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }"
        ><span style="font-size: 22px; font-weight: bold">张拉未合格：{{ oversum }}</span>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px' }"
        ><span style="font-size: 22px; font-weight: bold">本月未合格：{{ monthOverSum }}</span>
      </a-col>
    </a-row>
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag
                placeholder="请选择设备名称"
                v-model="queryParam.shebeibianhao"
                :dictOptions="dictOption"
              >
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
            <a-form-item label="时间范围">
              <j-date
                placeholder="开始时间"
                v-model="queryParam.sgsj_begin"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束时间"
                v-model="queryParam.sgsj_end"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="工程部位">
              <a-input placeholder="请输入工程部位" v-model="queryParam.gjbh"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="任务单号">
              <a-input placeholder="请输入任务单号" v-model="queryParam.uuid"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="超标查询">
              <j-dict-select-tag
                placeholder="请选择超标等级"
                v-model="queryParam.overLevel"
                dictCode="overLevel"
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
      <a-button @click="handleAdd" v-has="'zlsy:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="export1">导出</a-button>
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" v-has="'zlsy:dr'" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal"-->
      <!--                     @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" @click="batchDel">-->
      <!--            <a-icon type="delete"/>-->
      <!--            删除-->
      <!--          </a-menu-item>-->
      <!--        </a-menu>-->
      <!--        <a-button style="margin-left: 8px"> 批量操作-->
      <!--          <a-icon type="down"/>-->
      <!--        </a-button>-->
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
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <span slot="status" slot-scope="status, record">
          <a-tag color="green" v-if="record.status == '1'">张拉完成</a-tag>
          <a-tag color="red" v-if="record.status == '0'">张拉中</a-tag>
        </span>

        <span slot="overLevel" slot-scope="hege, record">
          <a-tag color="green" v-if="record.overLevel == '0'">合格</a-tag>
            <a-tag color="red" v-if="record.overLevel == '1'">初级超标</a-tag>
            <a-tag color="red" v-if="record.overLevel == '2'">中级超标</a-tag>
            <a-tag color="red" v-if="record.overLevel == '3'">高级超标</a-tag>
           <a-tag color="green" v-if="record.overLevel == null">合格</a-tag>
        </span>
        <span slot="issend" slot-scope="issend, record">
           <a-tag color="grey" v-if="record.issend==0">未推送</a-tag>
          <a-tag color="green" v-if="record.issend==1">推送成功</a-tag>
          <a-tag color="red" v-if="record.issend==2">推送失败</a-tag>
          <a-tag color="yellow" v-if="record.issend==3">未关联任务单</a-tag>
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
          <a v-has="'zlsy:edit'" @click="handleEdit(record)">编辑</a>
          <a-divider v-has="'zlsy:edit'" type="vertical" />
          <a @click="showmadel1(record.syjid)">详情</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a @click="reset(record)">重置推送</a>
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <tr-zhangla-m-modal-two ref="TrZhanglaMModalTwo"></tr-zhangla-m-modal-two>
    <tr-zhangla-xxb-modal ref="modalForm" @ok="modalFormOk"></tr-zhangla-xxb-modal>
  </a-card>
</template>

<script>
import '@assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import TrZhanglaXxbModal from './modules/TrZhanglaXxbModal'
import JSuperQuery from '@comp/jeecg/JSuperQuery.vue'
import { usershebeiList } from '@api/api'
import { handertoken } from '@/mixins/getHanderToken'
import TrZhanglaMModalTwo from '@views/zlyj/modules/TrZhanglaMModalTwo'
import Vue from 'vue'
import { getAction } from '@api/manage'

export default {
  name: 'TrZhanglaXxbListTwo',
  mixins: [JeecgListMixin, mixinDevice, handertoken],
  components: {
    TrZhanglaXxbModal,
    JSuperQuery,
    TrZhanglaMModalTwo,
  },
  data() {
    return {
      oversum: '',
      monthOverSum: '',
      dictOption: [],
      selectValue: '',
      description: '张拉信息表管理页面',
      // 表头
      columns: [
        {
          title: '序号',
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
          dataIndex: 'gcmc',
        },
        // {
        //   title: '工程名称',
        //   align: 'center',
        //   dataIndex: 'gcmc'
        // },

         {
          title: '设备名称',
          align: 'center',
          dataIndex: 'shebeibianhao_dictText'
        },
        {
          title: '工程部位',
          align: 'center',
          dataIndex: 'gjbh',
        },
        // {
        //   title: '张拉梁型',
        //   align: 'center',
        //   dataIndex: 'gjmc'
        // },
        // {
        //   title: '预制梁场',
        //   align: 'center',
        //   dataIndex: 'yzlc'
        // },

        {
          title: '砼设计强度(MPa)',
          align: 'center',
          dataIndex: 'snsjqd',
        },
        {
          title: '施工时间',
          align: 'center',
          dataIndex: 'sgsj',
        },
        {
          title: '任务单号',
          align: 'center',
          dataIndex: 'uuid',
        },
        // {
        //   title: '砼强度(MPa)',
        //   align: 'center',
        //   dataIndex: 'snskqd'
        // },
        // {
        //   title: '张拉加载速度',
        //   align: 'center',
        //   dataIndex: 'zlcsu',
        // },
        // {
        //   title: '张拉加载预应力',
        //   align: 'center',
        //   dataIndex: 'zlcsk',
        // },
        // {
        //   title: '起拱度',
        //   align: 'center',
        //   dataIndex: 'zlcsep',
        // },

        {
          title: '推送质保资料',
          align: 'center',
          dataIndex: 'issend',
          scopedSlots: { customRender: 'issend' },
        },
        {
          title: '张拉状态',
          align: 'center',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
        },
        {
          title: '是否合格',
          align: 'center',
          dataIndex: 'overLevel',
          scopedSlots: { customRender: 'overLevel' },
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
      isorter: {
        column: 'sgsj',
        order: 'desc',
      },
      url: {
        list: '/zhanglaxxb/trZhanglaXxb/list47',
        delete: '/zhanglaxxb/trZhanglaXxb/delete',
        deleteBatch: '/zhanglaxxb/trZhanglaXxb/deleteBatch',
        importExcelUrl: 'zhanglaxxb/trZhanglaXxb/importExcel',
        zltjUrl: '/trzhanglastatistics/trZhanglaStatistics/zlwarn',
        exportUrl: 'jmreport/view/750252270493253632',
        resetPush: '/zhanglaxxb/trZhanglaXxb/resetPush'
      },
      dictOptions: {},
      superFieldList: [],
      syjid: '',
      flag: '',
    }
  },
  created() {
    this.getToken()
    this.getSuperFieldList()
    this.shebeiList()
    this.tongji()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    export1: function () {
      //打印功能需要先去报表设计页面设计打印格式
      if (this.selectedRowKeys.length !== 1) {
        let param = this.getQueryParams()
        console.log(param, '打印信息')
        this.$message.error('请选择一条张拉数据进行打印')
      } else if (this.selectedRowKeys.length == 1) {
        //?paramsStr=${paramsStr}
        let param = this.getQueryParams()
        param['selections'] = this.selectedRowKeys.join(',')
        console.log(param, '打印信息')
        let url = `${window._CONFIG['domianURL']}/${this.url.exportUrl}?id=${param.selections}&token=${this.token}`
        window.open(url)
      }
    },
    tongji: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      getAction(this.url.zltjUrl, { sys_depart_orgcode }).then((res) => {
        var data = res.result
        this.oversum = data.oversum
        this.monthOverSum = data.monthOverSum
      })
    },
    showmadel1(syjid) {
      this.$refs.TrZhanglaMModalTwo.title = '详情'
      this.$refs.TrZhanglaMModalTwo.call(syjid)
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '9',
      }).then((res) => {
        //console.log(res)
        this.dictOption = []
        let result = res.result
        result.forEach((re) => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
      })
    },
    reset(record) {
      try {
        // 调用重置接口，根据实际情况修改请求参数
        getAction(this.url.resetPush, { id: record.id })
        this.$message.success('重置推送成功')
        // 刷新表格数据
        this.loadData()
      } catch (error) {
        console.error('重置推送失败', error)
        this.$message.error('重置推送失败，请稍后重试')
      }
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'syjid', text: '唯一码', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gcmc', text: '工程名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yzlc', text: '预制梁场', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gjbh', text: '构件编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shebeibianhao', text: '设备编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gjmc', text: '构件名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'snsjqd', text: '砼设计强度', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sgsj', text: '施工时间', dictCode: '' })
      fieldList.push({ type: 'string', value: 'snskqd', text: '砼试块强度', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zly1', text: '张拉仪1编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'ybbh1', text: '油表编号1', dictCode: '' })
      fieldList.push({ type: 'string', value: 'bdrq1', text: '标定日期1', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zly2', text: '张拉仪2编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'ybbh2', text: '油表编号2', dictCode: '' })
      fieldList.push({ type: 'string', value: 'bdrq2', text: '标定日期2', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cbunit', text: '承包单位,施工单位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jlunit', text: '监理单位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'htbh', text: '土建合同号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zlcsu', text: '张拉参数u', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zlcsk', text: '张拉参数k', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zlcsep', text: '张拉参数ep', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zlbwpic', text: '张拉部位图片', dictCode: '' })
      fieldList.push({ type: 'string', value: 'kualiang', text: '跨梁', dictCode: '' })
      fieldList.push({ type: 'string', value: 'scljss', text: '伸长量计算式', dictCode: '' })
      fieldList.push({ type: 'string', value: 'fmqkms', text: '封锚情况描述', dictCode: '' })
      fieldList.push({ type: 'string', value: 'memo', text: '备注', dictCode: '' })
      fieldList.push({ type: 'string', value: 'status', text: '完成状态(0为未完成,状态为1代表', dictCode: '' })
      fieldList.push({ type: 'string', value: 'uuid', text: '张拉任务单号', dictCode: '' })
      this.superFieldList = fieldList
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>