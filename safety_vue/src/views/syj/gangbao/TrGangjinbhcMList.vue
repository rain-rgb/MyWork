<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeiNo" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="任务单编号">
              <a-input placeholder="任务单编号" v-model="queryParam.code"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="检测日期范围">
              <j-date placeholder="开始检测日期" v-model="queryParam.checktime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束检测日期" v-model="queryParam.checktime_end" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="施工部位">
              <a-input placeholder="请输入施工部位" v-model="queryParam.sgbw1" @change="sgbw"></a-input>
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
      <a-button @click="handleAdd" v-has="'gjbhc:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'gjbhc:dc'" icon="download" @click="handleExportXls('钢保数据检测主表')">导出</a-button>
      <a-upload name="file" v-has="'gjbhc:dr'" :showUploadList="false" :multiple="false" :headers="tokenHeader"
                :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->

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
        <span slot="type" slot-scope="type, record">
           <a-tag color="green" v-if="record.type == 1">厚度</a-tag>
           <a-tag color="red" v-if="record.type == 2">波形</a-tag>
           <a-tag color="yellow" v-if="record.type == 3">剖面</a-tag>
           <a-tag color="blue" v-if="record.type == 4">网格</a-tag>
           <a-tag color="#666699" v-if="record.type == 5">规范</a-tag>
       </span>
        <span slot="passrate" slot-scope="passrate, record">
           <a-tag color="green" v-if="record.passrate >60">{{ record.passrate }}</a-tag>
           <a-tag color="red" v-if="record.passrate<60">{{ record.passrate }}</a-tag>
       </span>
        <span slot="hege" slot-scope="hege, record">
           <a-tag color="green" v-if="record.hege >60">{{record.hege}}</a-tag>
           <a-tag color="red" v-if="record.hege<60">{{record.hege}}</a-tag>
       </span>
        <span slot="isbaogao" slot-scope="hege, record">
           <a-tag color="green" v-if="record.isbaogao == 1">已出报告</a-tag>
           <a-tag color="red" v-if="record.isbaogao == 0">未出报告</a-tag>
       </span>
        <span slot="code" slot-scope="code, record">
           <a-tag color="green">{{ record.code }}</a-tag>
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
          <a v-has="'gjbhc:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider v-has="'gjbhc:edit'" type="vertical"/>
           <a-dropdown>
            <a @click="handlePreview(record)">报告预览</a>
          </a-dropdown>
          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetailes(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="handleFile(record.id)">附件上传</a>
              </a-menu-item>
              <a-menu-item v-has="'gjbhc:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <TrGangjinbhcMModaldetail ref="modalForms"></TrGangjinbhcMModaldetail>
    <tr-gangjinbhc-m-modal ref="modalForm" @ok="modalFormOk"></tr-gangjinbhc-m-modal>
    <gangjin-bhc-file :flag="visible" :id="id" @change="change"></gangjin-bhc-file>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import TrGangjinbhcMModal from './modules/TrGangjinbhcMModal'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { usershebeiList } from '@api/api'
import { handertoken } from '@/mixins/getHanderToken'
import Vue from 'vue'
import TrGangjinbhcMModaldetail from '@views/syj/gangbao/modules/TrGangjinbhcMModaldetail'
import { base64Encode } from '@api/kkfileView'
import GangjinBhcFile from '@views/syj/gangbao/modules/GangjinBhcFile'

export default {
  name: 'TrGangjinbhcMList',
  mixins: [JeecgListMixin, mixinDevice, handertoken],
  components: {
    GangjinBhcFile,
    TrGangjinbhcMModal,
    JSuperQuery,
    TrGangjinbhcMModaldetail
  },
  data() {
    return {
      selectValue: '',
      asyncSelectValue: '',
      id: 0,
      sgbw1: '',
      visible: false,
      dictOption: [],
      description: '钢保数据检测主表管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'shebeiNo_dictText'
        },
        {
          title: '任务单编号',
          align: 'center',
          dataIndex: 'code',
          scopedSlots: { customRender: 'code' },
        },
        {
          title: '项目名称',
          align: 'center',
          dataIndex: 'projectname'
        },
        {
          title: '具体部位',
          align: 'center',
          dataIndex: 'sgbw'
        },
        {
          title: '检测时间',
          align: 'center',
          dataIndex: 'checktime',
        },
        {
          title: '检测类型',
          align: 'center',
          dataIndex: 'type',//1:厚度、2:波形、3:剖面、4:网格、5:规范
          scopedSlots: { customRender: 'type' },
        },
        {
          title: '检测对象类型',
          align: 'center',
          dataIndex: 'targettype',//现浇板, 现浇墙, 现浇梁, 现浇柱, 预制板, 预制墙板, 预制梁, 预制柱, 预制桁架, 其他
        },
        {
          title: '测点总数',
          align: 'center',
          dataIndex: 'zonecount'
        },
        {
          title:'修正',
          align:"center",
          dataIndex: 'xiuzheng'
        },
        {
          title:'偏差',
          align:"center",
          dataIndex: 'piancha'
        },
        {
          title:'报告合格率(%)',
          align:"center",
          dataIndex: 'hege',
          scopedSlots: { customRender: 'hege' },
        },
        {
          title:'上传合格率(%)',
          align:"center",
          dataIndex: 'passrate',
          scopedSlots: { customRender: 'passrate' },
        },
        {
          title: '检测位置',
          align: 'center',
          dataIndex: 'checklocation'
        },
        {
          title: '主筋直径(mm)',
          align: 'center',
          dataIndex: 'masterdiameter'
        },
        {
          title: '主筋间距(mm)',
          align: 'center',
          dataIndex: 'masterspacing'
        },
        {
          title: '设计厚度(mm)',
          align: 'center',
          dataIndex: 'designthickness'
        },
        {
          title: '纵向设计厚度',
          align: 'center',
          dataIndex: 'zdesignthickness'
        },
        {
          title: '箍筋直径(mm)',
          align: 'center',
          dataIndex: 'subdiameter'
        },
        {
          title: '箍筋间距(mm)',
          align: 'center',
          dataIndex: 'subspacing'
        },
        {
          title: '曲面直径',
          align: 'center',
          dataIndex: 'curveddiameter'
        },
        {
          title: '检测人',
          align: 'center',
          dataIndex: 'testerid'
        },
        {
          title:'是否出报告',
          align:"center",
          dataIndex: 'isbaogao',
          scopedSlots: { customRender: 'isbaogao' },
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
      isorter: {
        column: 'checktime',
        order: 'desc',
      },
      url: {
        list: '/trgangjinbhcm/trGangjinbhcM/list',
        delete: '/trgangjinbhcm/trGangjinbhcM/delete',
        deleteBatch: '/trgangjinbhcm/trGangjinbhcM/deleteBatch',
        exportXlsUrl: '/trgangjinbhcm/trGangjinbhcM/exportXls',
        importExcelUrl: 'trgangjinbhcm/trGangjinbhcM/importExcel',

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getToken()
    this.shebeiList()
    this.getSuperFieldList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    handleDetailes(e) {
      this.$refs.modalForms.title = '详情'
      this.$refs.modalForms.call(e)
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '42'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
      })
    },
    handlePreview(record) {
      if (record && record.file) {
        let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode(record.file))
        window.open(url, '_blank')
      } else {
        window.open('http://121.40.163.88:8084/CATDPS/TestingMachineController.do?getReportPdfUrl&sampleNo=' + record.code, '_blank')
        // this.$message.info('未上传相关附件！')
      }
    },
    handleFile: function (id) {
      this.visible = true
      this.id = id
    },
    change(data) {
      this.flag = data
    },
    sgbw() {
      this.queryParam.sgbw = '*' + this.queryParam.sgbw1 + '*'
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'point', text: '构件序号', dictCode: '' })
      fieldList.push({ type: 'int', value: 'type', text: '1:厚度、2:波形、3:剖面、4:网格、5:规范', dictCode: '' })
      fieldList.push({ type: 'string', value: 'testid', text: '检测唯一ID', dictCode: '' })
      fieldList.push({
        type: 'string',
        value: 'targettype',
        text: '现浇板, 现浇墙, 现浇梁, 现浇柱, 预制板, 预制墙板, 预制梁, 预制柱, 预制桁架, 其他',
        dictCode: ''
      })
      fieldList.push({ type: 'int', value: 'zonecount', text: '测点总数', dictCode: '' })
      fieldList.push({ type: 'double', value: 'passrate', text: '合格率', dictCode: '' })
      fieldList.push({ type: 'string', value: 'checklocation', text: '检测位置', dictCode: '' })
      fieldList.push({ type: 'string', value: 'testerid', text: '检测人', dictCode: '' })
      fieldList.push({ type: 'date', value: 'checktime', text: '检测时间' })
      fieldList.push({ type: 'int', value: 'masterdiameter', text: '主筋直径mm', dictCode: '' })
      fieldList.push({ type: 'int', value: 'masterspacing', text: '主筋间距mm', dictCode: '' })
      fieldList.push({ type: 'int', value: 'designthickness', text: '设计厚度mm', dictCode: '' })
      fieldList.push({ type: 'int', value: 'zdesignthickness', text: '纵向设计厚度', dictCode: '' })
      fieldList.push({ type: 'int', value: 'subdiameter', text: '箍筋直径mm', dictCode: '' })
      fieldList.push({ type: 'int', value: 'subspacing', text: '箍筋间距mm', dictCode: '' })
      fieldList.push({ type: 'int', value: 'curveddiameter', text: '曲面直径', dictCode: '' })
      fieldList.push({ type: 'string', value: 'code', text: '任务单NO', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shebeiNo', text: '设备编号', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>