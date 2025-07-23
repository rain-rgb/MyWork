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
      <a-button @click="handleAdd" v-has="'hntht:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'hntht:dc'" icon="download" @click="handleExportXls('混凝土回弹主表')">导出</a-button>
      <a-upload name="file" v-has="'hntht:dr'" :showUploadList="false" :multiple="false" :headers="tokenHeader"
                :action="importExcelUrl" @change="handleImportExcel">
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
       <span slot="pouringsurface" slot-scope="pouringsurface, record">
           <a-tag color="green" v-if="record.pouringsurface == 0">浇筑侧面</a-tag>
           <a-tag color="red" v-if="record.pouringsurface == 1">浇筑底面</a-tag>
       </span>
        <span slot="code" slot-scope="code, record">
           <a-tag color="green">{{ record.code }}</a-tag>
       </span>
        <span slot="isbaogao" slot-scope="hege, record">
           <a-tag color="green" v-if="record.isbaogao == 1">已出报告</a-tag>
           <a-tag color="red" v-if="record.isbaogao == 0">未出报告</a-tag>
       </span>
        <span slot="pdjg" slot-scope="hege, record">
           <a-tag color="green" v-if="record.pdjg == 合格">合格</a-tag>
           <a-tag color="red" v-if="record.pdjg == 不合格">不合格</a-tag>
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
          <a-dropdown>
            <a @click="handlePreview(record)">报告预览</a>
          </a-dropdown>
          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a v-has="'hntht:edit'" @click="handleEdit(record)">编辑</a>
                <a @click="handleDetails(record)">回弹数据分析</a>
                <a-divider v-has="'hntht:edit'" type="vertical"/>
              </a-menu-item>
              <a-menu-item>
                <a @click="handleFile(record.id)">附件上传</a>
              </a-menu-item>
              <!--              <a-menu-item v-has="'hntht:del'">-->
              <!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
              <!--                  <a>删除</a>-->
              <!--                </a-popconfirm>-->
              <!--              </a-menu-item>-->
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <TrHnthtMModals ref="modalForms"></TrHnthtMModals>
    <tr-hntht-m-modal ref="modalForm" @ok="modalFormOk"></tr-hntht-m-modal>
    <hntht-file :flag="visible" :id="id" @change="change"></hntht-file>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import TrHnthtMModal from './modules/TrHnthtMModal'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { usershebeiList } from '@api/api'
import { handertoken } from '@/mixins/getHanderToken'
import Vue from 'vue'
import TrHnthtMModals from '@views/syj/hntht/modules/TrHnthtMModals'
import { base64Encode } from '@api/kkfileView'
import HnthtFile from '@views/syj/hntht/modules/HnthtFile'

export default {
  name: 'TrHnthtMList',
  mixins: [JeecgListMixin, mixinDevice, handertoken],
  components: {
    HnthtFile,
    TrHnthtMModal,
    JSuperQuery,
    TrHnthtMModals
  },
  data() {
    return {
      selectValue: '',
      asyncSelectValue: '',
      id: 0,
      sgbw1: '',
      visible: false,
      dictOption: [],
      description: '混凝土回弹主表管理页面',
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
          dataIndex: 'shebeiNo_dictText'
        },
        {
          title: '项目名称',
          align: 'center',
          dataIndex: 'projectname'
        },
        {
          title: '施工部位',
          align: 'center',
          dataIndex: 'sgbw'
        },
        {
          title: '检测时间',
          align: 'center',
          dataIndex: 'checktime',
        },
        {
          title: '检测位置',
          align: 'center',
          dataIndex: 'checklocation'
        },
        {
          title: '构件部位',
          align: 'center',
          dataIndex: 'place'
        },
        {
          title: '强度等级',
          align: 'center',
          dataIndex: 'strength'
        },
        {
          title: '任务单编号',
          align: 'center',
          dataIndex: 'code',
          scopedSlots: { customRender: 'code' },
        },
        {
          title: '测区数量',
          align: 'center',
          dataIndex: 'testingcount'
        },
        {
          title: '检测面',
          align: 'center',
          dataIndex: 'detectionsurface'
        },
        {
          title: '浇筑面',
          align: 'center',
          dataIndex: 'pouringsurface',//0：浇筑侧面 1：浇筑底面
          scopedSlots: { customRender: 'pouringsurface' },
        },
        {
          title: '标准碳化深度值',
          align: 'center',
          dataIndex: 'carbonizedepth'
        },
        {
          title: '检测角度',
          align: 'center',
          dataIndex: 'detectionangle'
        },
        {
          title: '是否泵送',
          align: 'center',
          dataIndex: 'ispumping'
        },
        {
          title: '浇筑日期',
          align: 'center',
          dataIndex: 'pouringdate',
          customRender: function (text) {
            return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
          }
        },
        {
          title: '测试最小数值',
          align: 'center',
          dataIndex: 'testingmin'
        },
        {
          title: '测区平均数值',
          align: 'center',
          dataIndex: 'testingaverage'
        },
        {
          title: '标准差',
          align: 'center',
          dataIndex: 'standarddeviation'
        },
        {
          title: '推定值',
          align: 'center',
          dataIndex: 'estimatedvalue'
        },
        {
          title: '测区最小换算值',
          align: 'center',
          dataIndex: 'showmin'
        },
        {
          title: '测区平均换算值',
          align: 'center',
          dataIndex: 'showaverage'
        },
        {
          title: '标准差',
          align: 'center',
          dataIndex: 'showstandarddev'
        },
        {
          title: '推定值',
          align: 'center',
          dataIndex: 'showestimatedval'
        },
        {
          title:'判定结果',
          align:"center",
          dataIndex: 'pdjg',
          scopedSlots: { customRender: 'pdjg' },
        },
        {
          title:'是否出报告',
          align:"center",
          dataIndex: 'isbaogao',
          scopedSlots: { customRender: 'isbaogao' },
        },
        {
          title: '检测人',
          align: 'center',
          dataIndex: 'testerpeople'
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
        list: '/trhnthtm/trHnthtM/list',
        delete: '/trhnthtm/trHnthtM/delete',
        deleteBatch: '/trhnthtm/trHnthtM/deleteBatch',
        exportXlsUrl: '/trhnthtm/trHnthtM/exportXls',
        importExcelUrl: 'trhnthtm/trHnthtM/importExcel',

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
    handleDetails(e) {
      this.$refs.modalForms.call(e)
      this.$refs.modalForms.title = '混凝土回弹数据详情'
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '43'
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
      fieldList.push({ type: 'date', value: 'checktime', text: '检测时间' })
      fieldList.push({ type: 'string', value: 'checklocation', text: '检测位置', dictCode: '' })
      fieldList.push({ type: 'string', value: 'testid', text: '检测ID', dictCode: '' })
      fieldList.push({ type: 'string', value: 'testerpeople', text: '检测人', dictCode: '' })
      fieldList.push({ type: 'string', value: 'place', text: '构件部位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'strength', text: '强度等级', dictCode: '' })
      fieldList.push({ type: 'int', value: 'testingcount', text: '测区数量', dictCode: '' })
      fieldList.push({ type: 'string', value: 'detectionsurface', text: '检测面', dictCode: '' })
      fieldList.push({ type: 'int', value: 'pouringsurface', text: '浇筑面 0：浇筑侧面 1：浇筑底面', dictCode: '' })
      fieldList.push({ type: 'double', value: 'carbonizedepth', text: '标准碳化深度值', dictCode: '' })
      fieldList.push({ type: 'string', value: 'detectionangle', text: '检测角度', dictCode: '' })
      fieldList.push({ type: 'string', value: 'ispumping', text: '是否泵送   是/否', dictCode: '' })
      fieldList.push({ type: 'string', value: 'detectionstandard', text: '检测依据', dictCode: '' })
      fieldList.push({ type: 'date', value: 'pouringdate', text: '浇筑日期' })
      fieldList.push({ type: 'int', value: 'testingmin', text: '测试最小数值', dictCode: '' })
      fieldList.push({ type: 'int', value: 'testingaverage', text: '测区平均数值', dictCode: '' })
      fieldList.push({ type: 'int', value: 'standarddeviation', text: '标准差', dictCode: '' })
      fieldList.push({ type: 'int', value: 'estimatedvalue', text: '推定值', dictCode: '' })
      fieldList.push({ type: 'string', value: 'showmin', text: '测区最小换算值', dictCode: '' })
      fieldList.push({ type: 'string', value: 'showaverage', text: '测区平均换算值', dictCode: '' })
      fieldList.push({ type: 'string', value: 'showstandarddev', text: '标准差', dictCode: '' })
      fieldList.push({ type: 'string', value: 'showestimatedval', text: '推定值', dictCode: '' })
      fieldList.push({ type: 'string', value: 'code', text: '任务单编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shebeiNo', text: '试验设备编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'projectname', text: '项目名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sgbw', text: '施工部位', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>